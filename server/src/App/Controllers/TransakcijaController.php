<?php
declare(strict_types=1);

namespace App\Controllers;

use App\Repositories\TransakcijaRepository;
use ErrorException;
use Psr\Http\Message\ResponseInterface as Response;
use Psr\Http\Message\ServerRequestInterface as Request;

class TransakcijaController {
    public function __construct(private TransakcijaRepository $repository) {}

    public function get_transakcije(Request $request, Response $response, string $iban) : Response {
        try {
            $data = $this->repository->get_all($iban);
        } catch (ErrorException $ex) {
            throw new \Slim\Exception\HttpInternalServerErrorException($request, message: $ex->getMessage());
        }

        if ($data == null) {
            throw new \Slim\Exception\HttpNotFoundException($request, message: "Nisu pronađene transakcije za zadani račun.");
        }

        $body = json_encode($data);

        $response->getBody()->write($body);
        return $response;
    }

    public function get_transakcije_korisnika(Request $request, Response $response, string $kor_id) : Response {
        try {
            $numRows = $request->getQueryParams()["numRows"] ?? "0";
            $data = $this->repository->get_all_kor(intval($kor_id), intval($numRows));
        } catch (ErrorException $ex) {
            throw new \Slim\Exception\HttpInternalServerErrorException($request, message: $ex->getMessage());
        }

        if ($data == null) {
            throw new \Slim\Exception\HttpNotFoundException($request, message: "Nisu pronađene transakcije za zadanog korisnika.");
        }

        $body = json_encode($data);

        $response->getBody()->write($body);
        return $response;
    }

    public function get_transakcija(Request $request, Response $response, string $tran_id) : Response {
        try {
            $data = $this->repository->get(intval($tran_id));
        } catch (ErrorException $ex) {
            throw new \Slim\Exception\HttpInternalServerErrorException($request, message: $ex->getMessage());
        }

        if ($data == null) {
            throw new \Slim\Exception\HttpNotFoundException($request, message: "Nije pronađena transakcija.");
        }

        $body = json_encode($data);

        $response->getBody()->write($body);
        return $response;
    }
}