<?php
    header("Access-Control-Allow-Origin: *");
    header("Content-Type: application/json");
    header("Access-Control-Allow-Methods: DELETE");
    header("Access-Control-Allow-Headers: Access-Control-Allow-Headers,Content-Type,Access-Control-Allow-Methods,Authorization,X-Requested-With");

    require_once "../../config.php";
    require_once "../../db.php";
    require_once "../../models/transaction_type.php";

    if (isset($_GET["id"])) {
        $id = $_GET["id"];

        $transaction_type = new Transaction_Type();
        if ($transaction_type->delete_transaction_type($id)) {
            http_response_code(200);
            echo json_encode(array(["message" => "Transaction Type Deleted."]));
        } else {
            http_response_code(400);
            echo json_encode(array(["message" => "Transaction Type Not Deleted."]));
        }
    } else {
        http_response_code(400);
        echo json_encode(array(["error" => "Invalid params."]));
    }