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
                    CONCAT(case when t.iban_platitelj = ? then '-' else '+' end, cast(round(t.iznos,2) as char(100))) as iznos,
                    t.model, t.poziv_na_broj,
                    t.datum, t.iban_platitelj, t.iban_primatelj,
                    CONCAT(kpl.ime, ' ', kpl.prezime) platitelj_vlasnik,
                    CONCAT(kpr.ime, ' ', kpr.prezime) primatelj_vlasnik
                FROM transakcija t
                LEFT JOIN racun rpl on rpl.iban = t.iban_platitelj
                LEFT JOIN racun rpr on rpr.iban = t.iban_primatelj
                LEFT JOIN korisnik kpl on kpl.kor_id = rpl.kor_id
                LEFT JOIN korisnik kpr on kpr.kor_id = rpr.kor_id
                WHERE t.iban_platitelj = ? OR t.iban_primatelj = ?
                ORDER BY t.datum desc";

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

    public function get_all_kor(
        int $kor_id, 
        int $num_rows, 
        string $vrsta_tran = "",
        string $od_datuma = "",
        string $do_datuma = "",
        string $od_iznosa = "",
        string $do_iznosa = ""
    ) : array {
        $transakcije = array();
        $num_rows_str = $num_rows != 0 ? "LIMIT $num_rows" : "";
        $filter = "";
        
        if ($vrsta_tran != "") {
            if ($vrsta_tran == "ISPLATA") {
                $filter = $filter . " kpl.kor_id = $kor_id ";
            } else if ($vrsta_tran == "UPLATA") {
                $filter = $filter . " kpr.kor_id = $kor_id ";
            } else {
                $filter = $filter . " (kpl.kor_id = $kor_id OR kpr.kor_id = $kor_id) ";
            }
        } else {
            $filter = $filter . " (kpl.kor_id = $kor_id OR kpr.kor_id = $kor_id) ";
        }

        if ($od_datuma != "") {
            $datum = new DateTimeImmutable($od_datuma);
            $filter = $filter . " and t.datum >= '" . $datum->format('Y-m-d') . "'";
        }

        if ($do_datuma != "") {
            $datum = new DateTimeImmutable($do_datuma);
            $filter = $filter . " and t.datum <= '" . $datum->format('Y-m-d') . "'";
        }


        $filter = $filter . ($od_iznosa != "" ? " and t.iznos >= " . $od_iznosa : "");
        $filter = $filter . ($do_iznosa != "" ? " and t.iznos <= " . $do_iznosa : "");


        $sql = "SELECT
                    t.tran_id, t.opis_placanja, 
                    CONCAT(case when kpl.kor_id = ? then '-' else '+' end, cast(round(t.iznos,2) as char(100))) as iznos,
                    t.model, t.poziv_na_broj,
                    t.datum, t.iban_platitelj, t.iban_primatelj,
                    CONCAT(kpl.ime, ' ', kpl.prezime) platitelj_vlasnik,
                    CONCAT(kpr.ime, ' ', kpr.prezime) primatelj_vlasnik
                FROM transakcija t
                LEFT JOIN racun rpl on rpl.iban = t.iban_platitelj
                LEFT JOIN racun rpr on rpr.iban = t.iban_primatelj
                LEFT JOIN korisnik kpl on kpl.kor_id = rpl.kor_id
                LEFT JOIN korisnik kpr on kpr.kor_id = rpr.kor_id
                WHERE $filter
                ORDER BY t.datum DESC
                $num_rows_str";

        $this->database->connect();
        $stmt = $this->database->get_connection()->prepare($sql);
        $stmt->bind_param("i", $kor_id);

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

    public function get(int $tran_id): ?Transakcija {
        $transakcija = null;
        $sql = "SELECT
                    t.tran_id, t.opis_placanja, t.iznos,
                    t.model, t.poziv_na_broj,
                    t.datum, t.iban_platitelj, t.iban_primatelj,
                    CONCAT(kpl.ime, ' ', kpl.prezime) platitelj_vlasnik,
                    CONCAT(kpr.ime, ' ', kpr.prezime) primatelj_vlasnik
                FROM transakcija t
                LEFT JOIN racun rpl on rpl.iban = t.iban_platitelj
                LEFT JOIN racun rpr on rpr.iban = t.iban_primatelj
                LEFT JOIN korisnik kpl on kpl.kor_id = rpl.kor_id
                LEFT JOIN korisnik kpr on kpr.kor_id = rpr.kor_id
                WHERE t.tran_id = ?";

        $this->database->connect();
        $stmt = $this->database->get_connection()->prepare($sql);
        $stmt->bind_param("i", $tran_id);

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
            } 
        }

        return $transakcija;
    }

    public function create(array $data) : ?Transakcija {
        $sql = "INSERT INTO transakcija 
            (opis_placanja, iznos, model, poziv_na_broj, iban_platitelj, iban_primatelj) values (?,?,?,?,?,?);";

        $this->database->connect();
        $stmt = $this->database->get_connection()->prepare($sql);
        $stmt->bind_param("sdsssss", $data["opis_placanja"], $data["iznos"], $data["model"], $data["poziv_na_broj"],
            $data["platitelj_iban"], $data["primatelj_iban"]
        );

        if (!$stmt->execute()) {
            trigger_error("Error executing query: " . $stmt->error);
            $this->database->disconnect();
            throw new ErrorException("Database error occured!");
        }

        $inserted = $stmt->insert_id;
        $this->database->disconnect();

        return $this->get($inserted);
    }
}