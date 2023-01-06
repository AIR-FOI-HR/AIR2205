<?php

    class Account { 
        function __construct() {
            $this->db = new DB();
        }

        function get_accounts() {
            $sql = "SELECT * from racun";

            $conn = $this->db->connect();
            $accounts = $conn->query($sql);
            $this->db->disconnect();

            return $accounts;
        }

        function get_account($iban) {
            $conn = $this->db->connect();
            $stmt = $conn->prepare("SELECT * from racun WHERE iban = ?");

            $stmt->bind_param("s", $iban);

            if (!$stmt->execute()) {
                trigger_error("Error executing query: " . $stmt->error);
                $this->db->disconnect();
                return false;
            }

            $account = $stmt->get_result();
            $this->db->disconnect();

            return $account;
        }

        function create_account($iban, $stanje = 0.00, $aktivnost = 'D', $korisnik_id, $vrsta_racuna_id, $qr_kod = '') {
            $conn = $this->db->connect();
            $stmt = $conn->prepare("INSERT INTO racun (iban, stanje, aktivnost, korisnik_korisnik_id, vrsta_racuna_vrsta_racuna_id, qr_kod) VALUES (?,?,?,?,?,?)");
            $stmt->bind_param("ssssss", $iban, $stanje, $aktivnost, $korisnik_id, $vrsta_racuna_id, $qr_kod);

            if (!$stmt->execute()) {
                trigger_error("Error executing query: " . $stmt->error);
                $this->db->disconnect();
                return false;
            }

            $this->db->disconnect();

            return true;
        }

        function update_account($iban, $stanje, $aktivnost, $korisnik_id, $vrsta_racuna_id, $qr_kod) { 
            $conn = $this->db->connect();
            $stmt = $conn->prepare(
                                "UPDATE racun 
                                SET 
                                    stanje = ?, 
                                    aktivnost = ?, 
                                    korisnik_korisnik_id = ?, 
                                    vrsta_racuna_vrsta_racuna_id = ?, 
                                    qr_kod = ? 
                                WHERE iban = ?");

            $stmt->bind_param("ssssss", $stanje, $aktivnost, $korisnik_id, $vrsta_racuna_id, $qr_kod, $iban);

            if (!$stmt->execute()) {
                trigger_error("Error executing query: " . $stmt->error);
                die();
            }

            $stmt->store_result();
            $response = $stmt->affected_rows;
            $this->db->disconnect();

            return $response;
        }

        function delete_account($iban) {
            $conn = $this->db->connect();
            $stmt = $conn->prepare("DELETE FROM racun WHERE iban = ?");

            $stmt->bind_param("s", $iban);

            if (!$stmt->execute()) {
                trigger_error("Error executing query: " . $stmt->error);
                die();
            }

            $stmt->store_result();
            $response = $stmt->affected_rows;
            $this->db->disconnect();

            return $response;
        }
    }