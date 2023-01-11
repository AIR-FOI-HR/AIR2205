<?php
    header("Access-Control-Allow-Origin: *");
    header("Content-Type: application/json");

    require_once "../../config.php";
    require_once "../../db.php";
    require_once "../../models/account_type.php";

    if (isset($_GET["vrsta_racuna_id"])) {
        $vrsta_racuna_id = $_GET["vrsta_racuna_id"];

        $account_type = new Account_type();
        $data = $account_type->get_account_type($vrsta_racuna_id);
        $account_type_data = array();

        while ($row = $data->fetch_object()) {
            if ($row) {
                array_push($account_type_data, [
                    "vrsta_racuna_id" => $row->vrsta_racuna_id,
                    "naziv" => $row->naziv
                    ]
                );
            }
        }

        if (!$account_type_data) {
            http_response_code(204);
            echo json_encode(array(["message" => "No Data Found."]));
        } else {
            echo json_encode($account_type_data);
        }
    } else {
        http_response_code(400);
        echo json_encode(array(["error" => "Invalid Parameters."]));
    }