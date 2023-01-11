<?php
    header("Access-Control-Allow-Origin: *");
    header("Content-Type: application/json");

    require_once "../../config.php";
    require_once "../../db.php";
    require_once "../../models/notification.php";

    if (isset($_GET["obavijest_id"])) {
        $obavijest_id = $_GET["obavijest_id"];

        $notification = new Notification();
        $data = $notification->get_notification($obavijest_id);
        $notifications = array();

        while ($row = $data->fetch_object()) {
            if ($row) {
                array_push($notifications, [
                    "obavijest_id" => $row->obavijest_id,
                    "sadrzaj" => $row->sadrzaj,
                    "datum" => $row->datum,
                    "korisnik_id" => $row->korisnik_korisnik_id
                    ]
                );
            }
        }

        if (!$notifications) {
            http_response_code(204);
            echo json_encode(array(["message" => "No Data Found."]));
        } else {
            echo json_encode($notifications);
        }
    } else {
        http_response_code(400);
        echo json_encode(array(["error" => "Invalid Parameters."]));
    }