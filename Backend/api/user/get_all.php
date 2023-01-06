<?php
    header("Access-Control-Allow-Origin: *");
    header("Content-Type: application/json");

    require_once "../../config.php";
    require_once "../../db.php";
    require_once "../../models/user.php";

    $user = new User();

    $data = $user->get_users();
    $users = array();

    while ($row = $data->fetch_object()) {
        if ($row) {
            array_push($users, [
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

    echo json_encode($users);