<?php
declare(strict_types=1);

namespace App\Repositories;
use App\Database;
use App\Models\Transakcija;
use DateTimeImmutable;
use ErrorException;

class TransakcijaRepository { 
    public function __construct(private Database $database) {}

    public function get_all(string $iban) : array {
        $transakcije = array();
        $sql = "SELECT 
                    t.tran_id, t.opis_placanja, 
                    CONCAT(case when t.iban_platitelj = ? then '-' else '+' end, cast(t.iznos as char(100))) as iznos,
                    t.model, t.poziv_na_broj,
                    t.datum, t.iban_platitelj, t.iban_primatelj,
                    CONCAT(kpl.ime, ' ', kpl.prezime) platitelj_vlasnik,
                    CONCAT(kpr.ime, ' ', kpr.prezime) primatelj_vlasnik
                FROM transakcija t
                LEFT JOIN racun rpl on rpl.iban = t.iban_platitelj
                LEFT JOIN racun rpr on rpr.iban = t.iban_primatelj
                LEFT JOIN korisnik kpl on kpl.kor_id = rpl.kor_id
                LEFT JOIN korisnik kpr on kpr.kor_id = rpr.kor_id
                WHERE t.iban_platitelj = ? OR t.iban_primatelj = ?";

        $this->database->connect();
        $stmt = $this->database->get_connection()->prepare($sql);
        $stmt->bind_param("sss", $iban, $iban, $iban);

        if (!$stmt->execute()) {
            trigger_error("Error executing query: " . $stmt->error);
            $this->database->disconnect();
            throw new ErrorException("Database error occured!");
        }

        $data = $stmt->get_result();
        $this->database->disconnect();

        while ($row = $data->fetch_object()) {
            if ($row) {
                $datum = new DateTimeImmutable($row->datum);
                $transakcija = new Transakcija(
                    intval($row->tran_id), 
                    $row->opis_placanja,
                    $row->iznos,
                    $row->model,
                    $row->poziv_na_broj,
                    $datum->format('d.m.Y.'),
                    $row->iban_platitelj, 
                    $row->platitelj_vlasnik,
                    $row->iban_primatelj, 
                    $row->primatelj_vlasnik,
                );

                array_push($transakcije, $transakcija);
            } 
        }

        return $transakcije;
    }
}