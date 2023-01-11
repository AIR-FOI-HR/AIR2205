<?php
    header("Access-Control-Allow-Origin: *");
    header("Content-Type: application/json");
    header("Access-Control-Allow-Methods: DELETE");
    header("Access-Control-Allow-Headers: Access-Control-Allow-Headers,Content-Type,Access-Control-Allow-Methods,Authorization,X-Requested-With");

    require_once "../../config.php";
    require_once "../../db.php";
    require_once "../../models/transaction.php";

    if (isset($_GET["id"])) {
        $id = $_GET["id"];

        $transaction = new Transaction();
        if ($transaction->delete_transaction($id)) {
            http_response_code(200);
            echo json_encode(array(["message" => "Transaction Deleted."]));
        } else {
            http_response_code(400);
            echo json_encode(array(["message" => "Transaction Not Deleted."]));
        }
    } else {
        http_response_code(400);
        echo json_encode(array(["error" => "Invalid params."]));
    }