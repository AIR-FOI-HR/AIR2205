<?php
    header("Access-Control-Allow-Origin: *");
    header("Content-Type: application/json");
    header("Access-Control-Allow-Methods: DELETE");
    header("Access-Control-Allow-Headers: Access-Control-Allow-Headers,Content-Type,Access-Control-Allow-Methods,Authorization,X-Requested-With");

    require_once "../../config.php";
    require_once "../../db.php";
    require_once "../../models/account.php";

    if (isset($_GET["iban"])) {
        $iban = $_GET["iban"];

        $account = new Account();
        if ($account->delete_account($iban)) {
            http_response_code(200);
            echo json_encode(array(["message" => "Account Deleted."]));
        } else {
            http_response_code(400);
            echo json_encode(array(["message" => "Account Not Deleted."]));
        }
    } else {
        http_response_code(400);
        echo json_encode(array(["error" => "Invalid Parameters."]));
    }