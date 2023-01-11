<?php
    header("Access-Control-Allow-Origin: *");
    header("Content-Type: application/json");
    header("Access-Control-Allow-Methods: PUT");
    header("Access-Control-Allow-Headers: Access-Control-Allow-Headers,Content-Type,Access-Control-Allow-Methods,Authorization,X-Requested-With");

    require_once "../../config.php";
    require_once "../../db.php";
    require_once "../../models/notification.php";

    $notification = new Notification();
    $data = json_decode(file_get_contents("php://input"));

    if ($notification->update_notification($data->obavijest_id, $data->sadrzaj, $data->datum, $data->korisnik_id)) {
        http_response_code(200);
        echo json_encode(array(["message" => "Notification Updated."]));
    } else {
        http_response_code(400);
        echo json_encode(array(["message" => "Notification Not Updated."]));
    }