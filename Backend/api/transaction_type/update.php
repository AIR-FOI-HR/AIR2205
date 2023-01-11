<?php
    header("Access-Control-Allow-Origin: *");
    header("Content-Type: application/json");
    header("Access-Control-Allow-Methods: PUT");
    header("Access-Control-Allow-Headers: Access-Control-Allow-Headers,Content-Type,Access-Control-Allow-Methods,Authorization,X-Requested-With");

    require_once "../../config.php";
    require_once "../../db.php";
    require_once "../../models/transaction_type.php";

    $transaction_type = new Transaction_Type();
    $data = json_decode(file_get_contents("php://input"));

    if ($transaction_type->update_transaction_type($data->vrsta_transakcije_id, $data->naziv)) {
        http_response_code(200);
        echo json_encode(array(["message" => "Transaction Type Updated."]));
    } else {
        http_response_code(400);
        echo json_encode(array(["message" => "Transaction Type Not Updated."]));
    }