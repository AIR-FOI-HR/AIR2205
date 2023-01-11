<?php
    header("Access-Control-Allow-Origin: *");
    header("Content-Type: application/json");
    header("Access-Control-Allow-Methods: POST");
    header("Access-Control-Allow-Headers: Access-Control-Allow-Headers,Content-Type,Access-Control-Allow-Methods,Authorization,X-Requested-With");

    require_once "../../config.php";
    require_once "../../db.php";
    require_once "../../models/transaction_type.php";

    $transaction_type = new Transaction_Type();
    $data = json_decode(file_get_contents("php://input"));

    if ($transaction_type->create_transaction_type($data->naziv)) {
        http_response_code(201);
        echo json_encode(array(["message" => "Transaction Type Created."]));
    } else {
        http_response_code(400);
        echo json_encode(array(["message" => "Transaction Type Not Created."]));
    }