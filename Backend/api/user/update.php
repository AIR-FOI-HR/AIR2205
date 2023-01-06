<?php
    header("Access-Control-Allow-Origin: *");
    header("Content-Type: application/json");
    header("Access-Control-Allow-Methods: PUT");
    header("Access-Control-Allow-Headers: Access-Control-Allow-Headers,Content-Type,Access-Control-Allow-Methods,Authorization,X-Requested-With");

    require_once "../../config.php";
    require_once "../../db.php";
    require_once "../../models/user.php";

    $user = new User();
    $data = json_decode(file_get_contents("php://input"));

    if ($user->update_user($data->korisnik_id, $data->ime, $data->prezime, $data->email, $data->lozinka, $data->adresa, $data->mobitel)) {
        http_response_code(200);
        echo json_encode(array(["message" => "User Updated."]));
    } else {
        http_response_code(400);
        echo json_encode(array(["message" => "User Not Updated."]));
    }