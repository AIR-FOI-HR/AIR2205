<?php

    class Notification { 
        function __construct() {
            $this->db = new DB();
        }

        function get_notifications() {
            $sql = "SELECT * FROM obavijest";

            $conn = $this->db->connect();
            $notifications = $conn->query($sql);
            $this->db->disconnect();

            return $notifications;
        }

        function get_notification($obavijest_id) {
            $conn = $this->db->connect();
            $stmt = $conn->prepare("SELECT * FROM obavijest WHERE obavijest_id = ?");

            $stmt->bind_param("s", $obavijest_id);

            if (!$stmt->execute()) {
                trigger_error("Error executing query: " . $stmt->error);
                $this->db->disconnect();
                return false;
            }

            $account = $stmt->get_result();
            $this->db->disconnect();

            return $account;
        }

        function create_notification($sadrzaj, $datum, $korisnik_id) {
            $conn = $this->db->connect();
            $stmt = $conn->prepare("INSERT INTO obavijest (sadrzaj, datum, korisnik_korisnik_id) VALUES (?,?,?)");
            $stmt->bind_param("sss", $sadrzaj, $datum, $korisnik_id);

            if (!$stmt->execute()) {
                trigger_error("Error executing query: " . $stmt->error);
                $this->db->disconnect();
                return false;
            }

            $this->db->disconnect();

            return true;
        }

        function update_notification($obavijest_id, $sadrzaj, $datum, $korisnik_id) { 
            $conn = $this->db->connect();
            $stmt = $conn->prepare(
                                "UPDATE obavijest 
                                SET 
                                    sadrzaj = ?,
                                    datum = ?, 
                                    korisnik_korisnik_id = ?
                                WHERE obavijest_id = ?");

            $stmt->bind_param("ssss",$sadrzaj, $datum, $korisnik_id, $obavijest_id);

            if (!$stmt->execute()) {
                trigger_error("Error executing query: " . $stmt->error);
                die();
            }

            $stmt->store_result();
            $response = $stmt->affected_rows;
            $this->db->disconnect();

            return $response;
        }

        function delete_notification($obavijest_id) {
            $conn = $this->db->connect();
            $stmt = $conn->prepare("DELETE FROM obavijest WHERE obavijest_id = ?");

            $stmt->bind_param("s", $obavijest_id);

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