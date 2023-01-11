<?php
    header("Access-Control-Allow-Origin: *");
    header("Content-Type: application/json");

    require_once "../../config.php";
    require_once "../../db.php";
    require_once "../../models/transaction.php";

    if (isset($_GET["id"])) {
        $id = $_GET["id"];

        $transaction = new Transaction();
        $data = $transaction->get_transaction($id);
        $transaction_data = array();

        while ($row = $data->fetch_object()) {
            if ($row) {
                array_push($transaction_data, [
                    "transakcija_id" => $row->transakcija_id,
                    "iznos" => $row->iznos,
                    "opis_placanja" => $row->opis_placanja,
                    "model" => $row->model,
                    "poziv_na_broj" => $row->poziv_na_broj,
                    "datum_izvrsenja" => $row->datum_izvrsenja,
                    "vrsta_transakcije_id" => $row->vrsta_transakcije_vrsta_transakcije_id,
                    "iban" => $row->racun_iban
                    ]
                );
            }
        }

        if (!$transaction_data) {
            http_response_code(204);
        } else {
            echo json_encode($transaction_data);
        }

    } else {
        http_response_code(400);
    }