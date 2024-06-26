<?php
    declare(strict_types=1);

    namespace App;
    use mysqli;

    class Database {
        private mysqli $db;
        public function __constructor() {}

        public function connect() {
            $env = parse_ini_file("config.ini", true);
            $this->db = new mysqli($env["database"]["hostname"], $env["database"]["username"], $env["database"]["password"], $env["database"]["database"]);

            if ($this->db->connect_errno) {
                die("Connection to DB failed: " . $this->db->connect_errno);
            }

            $this->db->set_charset("utf8");
            if ($this->db->connect_errno) {
                die("Connection to DB failed: " . $this->db->connect_errno);
            }
        }

        public function get_connection() {
            return $this->db;
        }

        public function disconnect() {
            $this->db->close();
        }
    }