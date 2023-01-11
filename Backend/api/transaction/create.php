<?php
    header("Access-Control-Allow-Origin: *");
    header("Content-Type: application/json");
    header("Access-Control-Allow-Methods: POST");
    header("Access-Control-Allow-Headers: Access-Control-Allow-Headers,Content-Type,Access-Control-Allow-Methods,Authorization,X-Requested-With");

    require_once "../../config.php";
    require_once "../../db.php";
    require_once "../../models/transaction.php";

    $transaction = new Transaction();
    $data = json_decode(file_get_contents("php://input"));

    if ($transaction->create_transaction(
        $data->iznos, 
        $data->opis_placanja, 
        $data->model, 
        $data->poziv_na_broj, 
        $data->datum_izvrsenja, 
        $data->vrsta_transakcije_vrsta_transakcije_id, 
        $data->racun_iban)) {
        http_response_code(201);
        echo json_encode(array(["message" => "Transaction Created."]));
    } else {
        http_response_code(400);
        echo json_encode(array(["message" => "Transaction Not Created."]));
    }