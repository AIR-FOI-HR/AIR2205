<?php
    header("Access-Control-Allow-Origin: *");
    header("Content-Type: application/json");
    header("Access-Control-Allow-Methods: POST");
    header("Access-Control-Allow-Headers: Access-Control-Allow-Headers,Content-Type,Access-Control-Allow-Methods,Authorization,X-Requested-With");

    require_once "../../config.php";
    require_once "../../db.php";
    require_once "../../models/account_type.php";

    $account_type = new Account_type();
    $data = json_decode(file_get_contents("php://input"));

    if ($account_type->create_account_type($data->naziv)) {
        http_response_code(201);
        echo json_encode(array(["message" => "Account Type Created."]));
    } else {
        http_response_code(400);
        echo json_encode(array(["message" => "Account Type Not Created."]));
    }