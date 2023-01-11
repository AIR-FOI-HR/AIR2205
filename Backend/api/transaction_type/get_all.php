<?php
    header("Access-Control-Allow-Origin: *");
    header("Content-Type: application/json");

    require_once "../../config.php";
    require_once "../../db.php";
    require_once "../../models/transaction_type.php";

    $transaction_type = new Transaction_Type();

    $data = $transaction_type->get_transaction_types();
    $transaction_types = array();

    while ($row = $data->fetch_object()) {
        if ($row) {
            array_push($transaction_types, [
                "vrsta_transakcije_id" => $row->vrsta_transakcije_id,
                "naziv" => $row->naziv
                ]
            );
        }
    }

    echo json_encode($transaction_types);