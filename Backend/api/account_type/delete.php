<?php
    header("Access-Control-Allow-Origin: *");
    header("Content-Type: application/json");
    header("Access-Control-Allow-Methods: DELETE");
    header("Access-Control-Allow-Headers: Access-Control-Allow-Headers,Content-Type,Access-Control-Allow-Methods,Authorization,X-Requested-With");

    require_once "../../config.php";
    require_once "../../db.php";
    require_once "../../models/account_type.php";

    if (isset($_GET["vrsta_racuna_id"])) {
        $vrsta_racuna_id = $_GET["vrsta_racuna_id"];

        $account_type = new Account_type();
        if ($account_type->delete_account_type($vrsta_racuna_id)) {
            http_response_code(200);
            echo json_encode(array(["message" => "Account Type Deleted."]));
        } else {
            http_response_code(400);
            echo json_encode(array(["message" => "Account Type Not Deleted."]));
        }
    } else {
        http_response_code(400);
        echo json_encode(array(["error" => "Invalid Parameters."]));
    }