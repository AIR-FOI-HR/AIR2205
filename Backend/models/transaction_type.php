<?php

    class Transaction_Type {
        function __construct() {
            $this->db = new DB();
        }

        function get_transaction_types() {
            $sql = "SELECT * FROM vrsta_transakcije";

            $conn = $this->db->connect();
            $transaction_types = $conn->query($sql);
            $this->db->disconnect();

            return $transaction_types;
        }

        function get_transaction_type($transaction_type_id) {
            $conn = $this->db->connect();
            $stmt = $conn->prepare("SELECT * FROM vrsta_transakcije WHERE vrsta_transakcije_id = ?");

            $stmt->bind_param("s", $transaction_type_id);

            if (!$stmt->execute()) {
                trigger_error("Error executing query: " . $stmt->error);
                $this->db->disconnect();
                return false;
            }

            $transaction_type = $stmt->get_result();
            $this->db->disconnect();

            return $transaction_type;
        }

        function create_transaction_type($naziv) {
            $conn = $this->db->connect();
            $stmt = $conn->prepare("INSERT INTO vrsta_transakcije (naziv) VALUES (?)");
            $stmt->bind_param("s", $naziv);

            if (!$stmt->execute()) {
                trigger_error("Error executing query: " . $stmt->error);
                $this->db->disconnect();
                return false;
            }

            $this->db->disconnect();

            return true;
        }

        function update_transaction_type($vrsta_transakcije_id, $naziv) { 
            $conn = $this->db->connect();
            $stmt = $conn->prepare(
                                "UPDATE vrsta_transakcije
                                SET 
                                    naziv = ?
                                WHERE vrsta_transakcije_id = ?");

            $stmt->bind_param("ss", $naziv, $vrsta_transakcije_id);
            if (!$stmt->execute()) {
                trigger_error("Error executing query: " . $stmt->error);
                die();
            }

            $stmt->store_result();
            $response = $stmt->affected_rows;
            $this->db->disconnect();

            return $response;
        }

        function delete_transaction_type($vrsta_transakcije_id) {
            $conn = $this->db->connect();
            $stmt = $conn->prepare("DELETE FROM vrsta_transakcije WHERE vrsta_transakcije_id = ?");

            $stmt->bind_param("s", $vrsta_transakcije_id);

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