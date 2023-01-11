<?php

    class User {
        function __construct() {
            $this->db = new DB();
        }

        function get_users() {
            $sql = "SELECT * FROM korisnik";

            $conn = $this->db->connect();
            $users = $conn->query($sql);
            $this->db->disconnect();

            return $users;
        }

        function get_user($id) {
            $conn = $this->db->connect();
            $stmt = $conn->prepare("SELECT * FROM korisnik WHERE korisnik_id = ?");

            $stmt->bind_param("s", $id);

            if (!$stmt->execute()) {
                trigger_error("Error executing query: " . $stmt->error);
                $this->db->disconnect();
                return false;
            }

            $user = $stmt->get_result();
            $this->db->disconnect();

            return $user;
        }

        function create_user($first_name, $last_name, $email, $password, $address, $mobile_number) {
            $conn = $this->db->connect();
            $stmt = $conn->prepare("INSERT INTO korisnik (ime, prezime, email, lozinka, adresa, mobitel) VALUES (?,?,?,?,?,?)");
            $stmt->bind_param("ssssss", $first_name, $last_name, $email, $password, $address, $mobile_number);

            if (!$stmt->execute()) {
                trigger_error("Error executing query: " . $stmt->error);
                $this->db->disconnect();
                return false;
            }

            $this->db->disconnect();

            return true;
        }

        function update_user($id, $first_name, $last_name, $email, $password, $address, $mobile_number) {
            $conn = $this->db->connect();
            $stmt = $conn->prepare(
                                "UPDATE korisnik 
                                SET 
                                    ime = ?, 
                                    prezime = ?, 
                                    email = ?, 
                                    lozinka = ?, 
                                    adresa = ?, 
                                    mobitel = ?
                                WHERE korisnik_id = ?");

            $stmt->bind_param("sssssss", $first_name, $last_name, $email, $password, $address, $mobile_number, $id);

            if (!$stmt->execute()) {
                trigger_error("Error executing query: " . $stmt->error);
                die();
            }

            $stmt->store_result();
            $response = $stmt->affected_rows;
            $this->db->disconnect();

            return $response;
        }

        function delete_user($id) {
            $conn = $this->db->connect();
            $stmt = $conn->prepare("DELETE FROM korisnik WHERE korisnik_id = ?");

            $stmt->bind_param("s", $id);

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