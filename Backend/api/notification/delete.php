<?php
    header("Access-Control-Allow-Origin: *");
    header("Content-Type: application/json");
    header("Access-Control-Allow-Methods: DELETE");
    header("Access-Control-Allow-Headers: Access-Control-Allow-Headers,Content-Type,Access-Control-Allow-Methods,Authorization,X-Requested-With");

    require_once "../../config.php";
    require_once "../../db.php";
    require_once "../../models/notification.php";

    if (isset($_GET["obavijest_id"])) {
        $obavijest_id = $_GET["obavijest_id"];

        $notification = new Notification();
        if ($notification->delete_notification($obavijest_id)) {
            http_response_code(200);
            echo json_encode(array(["message" => "Notification Deleted."]));
        } else {
            http_response_code(400);
            echo json_encode(array(["message" => "Notification Not Deleted."]));
        }
    } else {
        http_response_code(400);
        echo json_encode(array(["error" => "Invalid Parameters."]));
    }