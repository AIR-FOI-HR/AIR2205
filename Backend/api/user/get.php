<?php
    header("Access-Control-Allow-Origin: *");
    header("Content-Type: application/json");

    require_once "../../config.php";
    require_once "../../db.php";
    require_once "../../models/user.php";

    if (isset($_GET["id"])) {
        $id = $_GET["id"];

        $user = new User();
        $data = $user->get_user($id);
        $user_data = array();

        while ($row = $data->fetch_object()) {
            if ($row) {
                array_push($user_data, [
                    "korisnik_id" => $row->korisnik_id,
                    "ime" => $row->ime,
                    "prezime" => $row->prezime,
                    "email" => $row->email,
                    "lozinka" => $row->lozinka,
                    "adresa" => $row->adresa,
                    "mobitel" => $row->mobitel
                    ]
                );
            }
        }

        if (!$user_data) {
            http_response_code(204);
        } else {
            echo json_encode($user_data);
        }

    } else {
        http_response_code(400);
    }