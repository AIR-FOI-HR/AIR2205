<?php
declare(strict_types=1);

namespace App\Repositories;
use App\Database;
use App\Models\Racun;
use DateTimeImmutable;
use ErrorException;

class RacunRepository {
    public function __construct(private Database $database) {}

    public function get_all(int $kor_id): array {
        $racuni = array();
        $sql = "SELECT 
                    iban, vrijedi_od, aktivnost, stanje,
                    CONCAT(k.ime, ' ', k.prezime) as vlasnik,
                    v.oznaka as valuta, vr.naziv as vrsta_rac
                FROM racun r
                LEFT JOIN korisnik k on k.kor_id = r.kor_id
                LEFT JOIN valuta v on v.valuta_id = r.valuta_id
                LEFT JOIN vrsrac vr on vr.vrsrac_id = r.vrsrac_id
                WHERE r.kor_id = ?";

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
                $datum = new DateTimeImmutable($row->vrijedi_od);
                $racun = new Racun(
                    $row->iban, 
                    $datum->format('d.m.Y.'),
                    $row->aktivnost, 
                    floatval($row->stanje),
                    $row->vlasnik,
                    $row->valuta,
                    $row->vrsta_rac,
                );

                array_push($racuni, $racun);
            } 
        }

        return $racuni;
    }

    public function get(string $iban): ?Racun {
        $racun = null;
        $sql = "SELECT 
                    iban, vrijedi_od, aktivnost, stanje,
                    CONCAT(k.ime, ' ', k.prezime) as vlasnik,
                    v.oznaka as valuta, vr.naziv as vrsta_rac
                FROM racun r
                LEFT JOIN korisnik k on k.kor_id = r.kor_id
                LEFT JOIN valuta v on v.valuta_id = r.valuta_id
                LEFT JOIN vrsrac vr on vr.vrsrac_id = r.vrsrac_id
                WHERE r.iban = ?";

        $this->database->connect();
        $stmt = $this->database->get_connection()->prepare($sql);
        $stmt->bind_param("s", $iban);

        if (!$stmt->execute()) {
            trigger_error("Error executing query: " . $stmt->error);
            $this->database->disconnect();
            throw new ErrorException("Database error occured!");
        }

        $data = $stmt->get_result();
        $this->database->disconnect();

        while ($row = $data->fetch_object()) {
            if ($row) {
                $datum = new DateTimeImmutable($row->vrijedi_od);
                $racun = new Racun(
                    $row->iban, 
                    $datum->format('d.m.Y.'),
                    $row->aktivnost, 
                    floatval($row->stanje),
                    $row->vlasnik,
                    $row->valuta,
                    $row->vrsta_rac,
                );
            } 
        }

        return $racun;
    }

    public function get_from_tel_broj(string $tel_broj): ?Racun {
        $racun = null;
        $sql = "SELECT 
                    iban, vrijedi_od, aktivnost, stanje,
                    CONCAT(k.ime, ' ', k.prezime) as vlasnik,
                    v.oznaka as valuta, vr.naziv as vrsta_rac
                FROM racun r
                LEFT JOIN korisnik k on k.kor_id = r.kor_id
                LEFT JOIN valuta v on v.valuta_id = r.valuta_id
                LEFT JOIN vrsrac vr on vr.vrsrac_id = r.vrsrac_id
                WHERE k.tel_broj = ? and r.vrsrac_id = 1";

        $this->database->connect();
        $stmt = $this->database->get_connection()->prepare($sql);
        $stmt->bind_param("s", $tel_broj);

        if (!$stmt->execute()) {
            trigger_error("Error executing query: " . $stmt->error);
            $this->database->disconnect();
            throw new ErrorException("Database error occured!");
        }

        $data = $stmt->get_result();
        $this->database->disconnect();

        while ($row = $data->fetch_object()) {
            if ($row) {
                $datum = new DateTimeImmutable($row->vrijedi_od);
                $racun = new Racun(
                    $row->iban, 
                    $datum->format('d.m.Y.'),
                    $row->aktivnost, 
                    floatval($row->stanje),
                    $row->vlasnik,
                    $row->valuta,
                    $row->vrsta_rac,
                );
            } 
        }

        return $racun;
    }
}