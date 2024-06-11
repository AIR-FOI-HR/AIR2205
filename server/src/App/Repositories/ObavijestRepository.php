<?php
declare(strict_types=1);

namespace App\Repositories;
use App\Database;
use App\Models\Obavijest;
use DateTimeImmutable;
use ErrorException;

class ObavijestRepository {
    public function __construct(private Database $database) {} 

    public function get_all_for_korisnik(int $kor_id): array {
        $obavijesti = array();
        $sql = "SELECT o.obav_id, o.naslov, o.tekst, o.datum, pr.procitano
                FROM obavijest o
                INNER JOIN procitane_obavijesti pr on pr.obav_id = o.obav_id and pr.kor_id = ?
                ORDER BY o.datum DESC";

        $this->database->connect();
        $stmt = $this->database->get_connection()->prepare($sql);
        $stmt->bind_param('i', $kor_id);

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
                $obavijest = new Obavijest(
                    intval($row->obav_id),  
                    $row->naslov,
                    $row->tekst,
                    $datum->format('d.m.Y.'),
                    $row->procitano == 0 ? false : true,
                );

                array_push($obavijesti, $obavijest);
            } 
        }

        return $obavijesti;
    }

    public function get(int $obav_id): ?Obavijest {
        $obavijest = null;
        $sql = "SELECT obav_id, naslov, tekst, datum
                FROM obavijest 
                WHERE obav_id = ?";

        $this->database->connect();
        $stmt = $this->database->get_connection()->prepare($sql);
        $stmt->bind_param('i', $obav_id);

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
                $obavijest = new Obavijest(
                    intval($row->obav_id), 
                    $row->naslov,
                    $row->tekst,
                    $datum->format('d.m.Y.')
                );
            } 
        }

        return $obavijest;
    }

    public function create(array $data): ?Obavijest {
        $obavijest = null;
        $sql = "INSERT INTO obavijest (naslov, tekst) VALUES (?, ?)";

        $this->database->connect();
        $stmt = $this->database->get_connection()->prepare($sql);
        $stmt->bind_param('ss', $data["naslov"], $data["tekst"]);

        if (!$stmt->execute()) {
            trigger_error("Error executing query: " . $stmt->error);
            $this->database->disconnect();
            throw new ErrorException("Database error occured!");
        }
        $this->database->disconnect();
        $obavijest = $this->get($stmt->insert_id);

        if ($obavijest != null) {
            if (isset($data["send_to_all"]) && $data["send_to_all"] == true) {
                $sql = "INSERT INTO procitane_obavijesti (kor_id, obav_id)
                        SELECT kor_id, ? FROM korisnik";

                $this->database->connect();
                $stmt = $this->database->get_connection()->prepare($sql);
                $stmt->bind_param('i', $obavijest->get_id());

                if (!$stmt->execute()) {
                    trigger_error("Error executing query: " . $stmt->error);
                    $this->database->disconnect();
                    throw new ErrorException("Database error occured!");
                }
                $this->database->disconnect();
            } elseif (isset($data["send_to_kor"])) {
                $sql = "INSERT INTO procitane_obavijesti (kor_id, obav_id) VALUES (?,?)";
                
                $this->database->connect();
                $stmt = $this->database->get_connection()->prepare($sql);
                $stmt->bind_param('ii', $data["send_to_kor"], $obavijest->get_id());

                if (!$stmt->execute()) {
                    trigger_error("Error executing query: " . $stmt->error);
                    $this->database->disconnect();
                    throw new ErrorException("Database error occured!");
                }
                $this->database->disconnect();
            }
        }

        return $obavijest;
    }

    public function update_procitano(array $data) {
        $sql = "UPDATE procitane_obavijesti SET procitano = 1 WHERE kor_id = ? AND obav_id = ?";

        $this->database->connect();
        $stmt = $this->database->get_connection()->prepare($sql);
        $stmt->bind_param('ii', intval($data["kor_id"]), intval($data["obav_id"]));

        if (!$stmt->execute()) {
            trigger_error("Error executing query: " . $stmt->error);
            $this->database->disconnect();
            throw new ErrorException("Database error occured!");
        }

        if ($stmt->affected_rows == 0) {
            $this->database->disconnect();
            throw new ErrorException("Obavijest nije ažurirana.");
        }

        $this->database->disconnect();
    }
}