<?php
declare(strict_types=1);

namespace App\Repositories;
use App\Database;
use App\Exceptions\InvalidUserException;
use App\Models\Korisnik;
use ErrorException;
use PHPMailer\PHPMailer\Exception;
use PHPMailer\PHPMailer\PHPMailer;

class KorisnikRepository {
    public function __construct(private Database $database) {}

    public function get_all(): array {
        $korisnici = array();
        $this->database->connect();
        $data = $this->database->get_connection()->query("SELECT * FROM korisnik");
        $this->database->disconnect();

        while ($row = $data->fetch_object()) {
            if ($row) {
                $korisnik = new Korisnik(
                    intval($row->kor_id), 
                    $row->ime, 
                    $row->prezime, 
                    $row->email, 
                    $row->oib,
                    $row->pin,
                    $row->kod,
                    $row->tel_broj,
                );
                array_push($korisnici, $korisnik);
            } 
        }

        return $korisnici;
    }

    public function get(int $id): ?Korisnik {
        $korisnik = null;
        $this->database->connect();
        $stmt = $this->database->get_connection()->prepare("SELECT * FROM korisnik WHERE kor_id = ?");
        $stmt->bind_param("i", $id);

        if (!$stmt->execute()) {
            trigger_error("Error executing query: " . $stmt->error);
            $this->database->disconnect();
            throw new ErrorException("Database error occured!");
        }

        $data = $stmt->get_result();
        $this->database->disconnect();

        while ($row = $data->fetch_object()) {
            if ($row) {
                $korisnik = new Korisnik(
                    intval($row->kor_id), 
                    $row->ime, 
                    $row->prezime, 
                    $row->email, 
                    $row->oib,
                    $row->pin,
                    $row->kod,
                    $row->tel_broj,
                );
            } 
        }

        return $korisnik;
    }

    public function create(array $data) : ?Korisnik {
        $sql = "INSERT INTO korisnik (ime, prezime, email, oib, pin, kod, tel_broj) values (?,?,?,?,?,?,?);";

        $this->database->connect();
        $stmt = $this->database->get_connection()->prepare($sql);
        $stmt->bind_param("sssssss", $data["ime"], $data["prezime"], $data["email"], $data["oib"],
            $data["pin"], $data["kod"], $data["tel_broj"]);

        if (!$stmt->execute()) {
            trigger_error("Error executing query: " . $stmt->error);
            $this->database->disconnect();
            throw new ErrorException("Database error occured!");
        }

        $inserted = $stmt->insert_id;
        $this->database->disconnect();

        return $this->get($inserted);
    }

    public function update(int $id, array $data) : ?Korisnik {
        $sql = "UPDATE korisnik SET ime = ?, prezime = ?, email = ?, oib = ?, pin = ?, kod = ?, tel_broj = ? WHERE kor_id = ?;";

        $this->database->connect();
        $stmt = $this->database->get_connection()->prepare($sql);
        $stmt->bind_param("sssssssi", $data["ime"], $data["prezime"], $data["email"], $data["oib"],
            $data["pin"], $data["kod"], $data["tel_broj"], $id);

        if (!$stmt->execute()) {
            trigger_error("Error executing query: " . $stmt->error);
            $this->database->disconnect();
            throw new ErrorException("Database error occured!");
        }

        if ($stmt->affected_rows == 0) {
            $this->database->disconnect();
            throw new ErrorException("Korisnik nije ažuriran.");
        }

        $this->database->disconnect();

        return $this->get($id);
    }

    public function delete(int $id) : bool {
        $sql = "DELETE FROM korisnik WHERE kor_id = ?;";

        $this->database->connect();
        $stmt = $this->database->get_connection()->prepare($sql);
        $stmt->bind_param("i", $id);

        if (!$stmt->execute()) {
            trigger_error("Error executing query: " . $stmt->error);
            $this->database->disconnect();
            throw new ErrorException("Database error occured!");
        }

        if ($stmt->affected_rows == 0) {
            $this->database->disconnect();
            throw new ErrorException("Korisnik nije obrisan.");
        }

        $this->database->disconnect();

        return true;
    }

    public function login(array $data) : ?Korisnik {
        $sql = "SELECT * FROM korisnik WHERE email = ?;";

        $this->database->connect();
        $stmt = $this->database->get_connection()->prepare($sql);
        $stmt->bind_param("s", $data["email"]);

        if (!$stmt->execute()) {
            trigger_error("Error executing query: " . $stmt->error);
            $this->database->disconnect();
            throw new ErrorException("Database error occured!");
        }

        $result = $stmt->get_result();
        $this->database->disconnect();

        while ($row = $result->fetch_object()) {
            if ($row) {
                $korisnik = new Korisnik(
                    intval($row->kor_id), 
                    $row->ime, 
                    $row->prezime, 
                    $row->email, 
                    $row->oib,
                    $row->pin,
                    $row->kod,
                    $row->tel_broj,
                );
            }
        }

        if (!isset($korisnik)) {
            throw new InvalidUserException("Korisnik ne postoji.");
        } 

        if ($korisnik->get_pin() != $data["pin"]) {
            throw new InvalidUserException("Pogrešan PIN!");
        }

        return $korisnik;
    }

    public function restore(array $data) : ?Korisnik {
        $sql = "SELECT * FROM korisnik WHERE email = ?;";

        $this->database->connect();
        $stmt = $this->database->get_connection()->prepare($sql);
        $stmt->bind_param("s", $data["email"]);

        if (!$stmt->execute()) {
            trigger_error("Error executing query: " . $stmt->error);
            $this->database->disconnect();
            throw new ErrorException("Database error occured!");
        }

        $result = $stmt->get_result();
        $this->database->disconnect();

        while ($row = $result->fetch_object()) {
            if ($row) {
                $korisnik = new Korisnik(
                    intval($row->kor_id), 
                    $row->ime, 
                    $row->prezime, 
                    $row->email, 
                    $row->oib,
                    $row->pin,
                    $row->kod,
                    $row->tel_broj,
                );
            }
        }

        if (!isset($korisnik)) {
            throw new InvalidUserException("Korisnik ne postoji.");
        }

        $length = 12;
        $kod = substr(str_shuffle('0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ'),1,$length);

        $sql = "UPDATE korisnik SET kod = ? WHERE kor_id = ?;";

        $this->database->connect();
        $stmt = $this->database->get_connection()->prepare($sql);
        $stmt->bind_param("si", $kod, $korisnik->get_id());

        if (!$stmt->execute()) {
            trigger_error("Error executing query: " . $stmt->error);
            $this->database->disconnect();
            throw new ErrorException("Database error occured!");
        }

        if ($stmt->affected_rows == 0) {
            $this->database->disconnect();
            throw new ErrorException("Greška kod postavljanja koda za oporavak! Pokušajte ponovno.");
        }
        $this->database->disconnect();
        $ini = file_get_contents(__DIR__ . "/../config.ini");
        $env = parse_ini_string($ini, true);
        
        $mail = new PHPMailer(true);
        $mail->IsSMTP();
        $mail->Host = "smtp.gmail.com";
        $mail->SMTPDebug = 2;
        $mail->SMTPAuth = true;
        $mail->SMTPSecure = PHPMailer::ENCRYPTION_SMTPS;
        $mail->Port = 465;
        $mail->Username = $env["mail"]["mail_addr"];
        $mail->Password = $env["mail"]["password"];

        $mail->setFrom($env["mail"]["mail_addr"], "mBankingDev");
        $mail->addAddress($korisnik->get_email(), $korisnik->get_ime());
        $mail->isHTML(false);
        $mail->Subject = "mBanking - Kod za oporavak";
        $mail->Body = "mBanking vam šalje kod za oporavak PIN-a: $kod";

        try {
            if (!$mail->send()) {
                throw new ErrorException("Neuspješno slanje e-maila.");
            }
        } catch (Exception $ex) {
            throw new ErrorException("Neuspješno slanje e-maila.");
        }

        return $this->get($korisnik->get_id());
    }
}