<?php

    class Transaction { 
        function __construct() {
            $this->db = new DB();
        }

        function get_transactions() {
            $sql = "SELECT * FROM transakcija";

            $conn = $this->db->connect();
            $transactions = $conn->query($sql);
            $this->db->disconnect();

            return $transactions;
        }

        function get_transaction($transaction_id) {
            $conn = $this->db->connect();
            $stmt = $conn->prepare("SELECT * FROM transakcija WHERE transakcija_id = ?");

            $stmt->bind_param("s", $transaction_id);

            if (!$stmt->execute()) {
                trigger_error("Error executing query: " . $stmt->error);
                $this->db->disconnect();
                return false;
            }

            $transaction = $stmt->get_result();
            $this->db->disconnect();

            return $transaction;
        }

        function create_transaction($iznos, $opis_placanja, $model, $poziv_na_broj, $datum_izvrsenja, $vrsta_transakcije, $iban) {
            $conn = $this->db->connect();
            $stmt = $conn->prepare("INSERT INTO transakcija (iznos, opis_placanja, model, poziv_na_broj, datum_izvrsenja, vrsta_transakcije_vrsta_transakcije_id, racun_iban) VALUES (?,?,?,?,?,?,?)");
            $stmt->bind_param("sssssss", $iznos, $opis_placanja, $model, $poziv_na_broj, $datum_izvrsenja, $vrsta_transakcije, $iban);

            if (!$stmt->execute()) {
                trigger_error("Error executing query: " . $stmt->error);
                $this->db->disconnect();
                return false;
            }

            $this->db->disconnect();

            return true;
        }

        function update_transaction($transakcija_id, $iznos, $opis_placanja, $model, $poziv_na_broj, $datum_izvrsenja, $vrsta_transakcije, $iban) { 
            $conn = $this->db->connect();
            $stmt = $conn->prepare(
                                "UPDATE transakcija 
                                SET 
                                    iznos = ?, 
                                    opis_placanja = ?, 
                                    model = ?, 
                                    poziv_na_broj = ?, 
                                    datum_izvrsenja = ?,
                                    vrsta_transakcije_vrsta_transakcije_id = ?,
                                    racun_iban = ? 
                                WHERE transakcija_id = ?");

            $stmt->bind_param("ssssssss", $iznos, $opis_placanja, $model, $poziv_na_broj, $datum_izvrsenja, $vrsta_transakcije, $iban, $transakcija_id);

            if (!$stmt->execute()) {
                trigger_error("Error executing query: " . $stmt->error);
                die();
            }

            $stmt->store_result();
            $response = $stmt->affected_rows;
            $this->db->disconnect();

            return $response;
        }

        function delete_transaction($transakcija_id) {
            $conn = $this->db->connect();
            $stmt = $conn->prepare("DELETE FROM transakcija WHERE transakcija_id = ?");

            $stmt->bind_param("s", $transakcija_id);

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