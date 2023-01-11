<?php
    header("Access-Control-Allow-Origin: *");
    header("Content-Type: application/json");

    require_once "../../config.php";
    require_once "../../db.php";
    require_once "../../models/notification.php";

    $notification = new Notification();

    $data = $notification->get_notifications();
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

    echo json_encode($notifications);