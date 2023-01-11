<?php
    header("Access-Control-Allow-Origin: *");
    header("Content-Type: application/json");

    require_once "../../config.php";
    require_once "../../db.php";
    require_once "../../models/transaction_type.php";

    if (isset($_GET["id"])) {
        $id = $_GET["id"];

        $transaction_type = new Transaction_Type();
        $data = $transaction_type->get_transaction_type($id);
        $transaction_type_data = array();

        while ($row = $data->fetch_object()) {
            if ($row) {
                array_push($transaction_type_data, [
                    "vrsta_transakcije_id" => $row->vrsta_transakcije_id,
                    "naziv" => $row->naziv
                    ]
                );
            }
        }

        if (!$transaction_type_data) {
            http_response_code(204);
        } else {
            echo json_encode($transaction_type_data);
        }

    } else {
        http_response_code(400);
    }