<?php
    header("Access-Control-Allow-Origin: *");
    header("Content-Type: application/json");

    require_once "../../config.php";
    require_once "../../db.php";
    require_once "../../models/account.php";

    $account = new Account();

    $data = $account->get_accounts();
    $accounts = array();

    while ($row = $data->fetch_object()) {
        if ($row) {
            array_push($accounts, [
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

    echo json_encode($accounts);