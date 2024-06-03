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
            $num_rows = $request->getQueryParams()["numRows"] ?? "0";
            $vrsta_tran = $request->getQueryParams()["vrsta_tran"] ?? "";
            $od_datuma = $request->getQueryParams()["od_datuma"] ?? "";
            $do_datuma = $request->getQueryParams()["do_datuma"] ?? "";
            $od_iznosa = $request->getQueryParams()["od_iznosa"] ?? "";
            $do_iznosa = $request->getQueryParams()["do_iznosa"] ?? "";
            $data = $this->repository->get_all_kor(intval(
                $kor_id), 
                intval($num_rows), 
                $vrsta_tran, 
                $od_datuma, 
                $do_datuma, 
                $od_iznosa, 
                $do_iznosa
            );
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

    public function create_transakcija(Request $request, Response $response) : Response {
        $body = $request->getParsedBody();
        
        try {
            $data = $this->repository->create($body);
        } catch (ErrorException $ex) {
            throw new \Slim\Exception\HttpInternalServerErrorException($request, message: $ex->getMessage());
        }

        if ($data == null) {
            throw new \Slim\Exception\HttpInternalServerErrorException($request, message: "Transakcija nije provedena. Provjerite podatke.");
        }

        $body = json_encode($data);
        $response->getBody()->write($body);
        return $response->withStatus(201);
    }
}