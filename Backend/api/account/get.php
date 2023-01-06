<?php
    header("Access-Control-Allow-Origin: *");
    header("Content-Type: application/json");

    require_once "../../config.php";
    require_once "../../db.php";
    require_once "../../models/account.php";

    if (isset($_GET["iban"])) {
        $iban = $_GET["iban"];

        $account = new Account();
        $data = $account->get_account($iban);
        $account_data = array();

        while ($row = $data->fetch_object()) {
            if ($row) {
                array_push($account_data, [
                    "iban" => $row->iban,
                    "stanje" => $row->stanje,
                    "aktivnost" => $row->aktivnost,
                    "korisnik_id" => $row->korisnik_korisnik_id,
                    "vrsta_racuna_id" => $row->vrsta_racuna_vrsta_racuna_id,
                    "qr_kod" => $row->qr_kod,
                    ]
                );
            }
        }

        if (!$account_data) {
            http_response_code(204);
            echo json_encode(array(["message" => "No Data Found."]));
        } else {
            echo json_encode($account_data);
        }
    } else {
        http_response_code(400);
        echo json_encode(array(["error" => "Invalid Parameters."]));
    }