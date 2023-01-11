<?php

    class Account_type { 
        function __construct() {
            $this->db = new DB();
        }

        function get_account_types() {
            $sql = "SELECT * FROM vrsta_racuna";

            $conn = $this->db->connect();
            $account_types = $conn->query($sql);
            $this->db->disconnect();

            return $account_types;
        }

        function get_account_type($vrsta_racuna_id) {
            $conn = $this->db->connect();
            $stmt = $conn->prepare("SELECT * from vrsta_racuna WHERE vrsta_racuna_id = ?");

            $stmt->bind_param("s", $vrsta_racuna_id);

            if (!$stmt->execute()) {
                trigger_error("Error executing query: " . $stmt->error);
                $this->db->disconnect();
                return false;
            }

            $account_type = $stmt->get_result();
            $this->db->disconnect();

            return $account_type;
        }

        function create_account_type($naziv) {
            $conn = $this->db->connect();
            $stmt = $conn->prepare("INSERT INTO vrsta_racuna (naziv) VALUES (?)");
            $stmt->bind_param("s", $naziv);

            if (!$stmt->execute()) {
                trigger_error("Error executing query: " . $stmt->error);
                $this->db->disconnect();
                return false;
            }

            $this->db->disconnect();

            return true;
        }

        function update_account_type($vrsta_racuna_id, $naziv) { 
            $conn = $this->db->connect();
            $stmt = $conn->prepare(
                                "UPDATE vrsta_racuna
                                SET 
                                    naziv = ?
                                WHERE vrsta_racuna_id = ?");

            $stmt->bind_param("ss", $naziv, $vrsta_racuna_id);

            if (!$stmt->execute()) {
                trigger_error("Error executing query: " . $stmt->error);
                die();
            }

            $stmt->store_result();
            $response = $stmt->affected_rows;
            $this->db->disconnect();

            return $response;
        }

        function delete_account_type($vrsta_racuna_id) {
            $conn = $this->db->connect();
            $stmt = $conn->prepare("DELETE FROM vrsta_racuna WHERE vrsta_racuna_id = ?");

            $stmt->bind_param("s", $vrsta_racuna_id);

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