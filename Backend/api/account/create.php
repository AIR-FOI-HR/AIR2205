<?php
    header("Access-Control-Allow-Origin: *");
    header("Content-Type: application/json");
    header("Access-Control-Allow-Methods: POST");
    header("Access-Control-Allow-Headers: Access-Control-Allow-Headers,Content-Type,Access-Control-Allow-Methods,Authorization,X-Requested-With");

    require_once "../../config.php";
    require_once "../../db.php";
    require_once "../../models/account.php";

    $account = new Account();
    $data = json_decode(file_get_contents("php://input"));

    if ($account->create_account($data->iban, $data->stanje, $data->aktivnost, $data->korisnik_id, $data->vrsta_racuna_id, $data->qr_kod)) {
        http_response_code(201);
        echo json_encode(array(["message" => "Account Created."]));
    } else {
        http_response_code(400);
        echo json_encode(array(["message" => "Account Not Created."]));
    }