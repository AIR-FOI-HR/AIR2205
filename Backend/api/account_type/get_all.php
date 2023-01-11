<?php
    header("Access-Control-Allow-Origin: *");
    header("Content-Type: application/json");

    require_once "../../config.php";
    require_once "../../db.php";
    require_once "../../models/account_type.php";

    $account_type = new Account_type();

    $data = $account_type->get_account_types();
    $account_types = array();

    while ($row = $data->fetch_object()) {
        if ($row) {
            array_push($account_types, [
                "vrsta_racuna_id" => $row->vrsta_racuna_id,
                "naziv" => $row->naziv
                ]
            );
        }
    }

    echo json_encode($account_types);