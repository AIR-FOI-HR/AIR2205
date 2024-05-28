<?php
declare(strict_types=1);

namespace App\Controllers;

use App\Repositories\RacunRepository;
use ErrorException;
use Psr\Http\Message\ResponseInterface as Response;
use Psr\Http\Message\ServerRequestInterface as Request;

class RacunController {

    public function __construct(private RacunRepository $repository) {}

    public function get_racuni(Request $request, Response $response, string $kor_id) : Response {
        try {
            $data = $this->repository->get_all(intval($kor_id));
        } catch (ErrorException $ex) {
            throw new \Slim\Exception\HttpInternalServerErrorException($request, message: $ex->getMessage());
        }

        if ($data == null) {
            throw new \Slim\Exception\HttpNotFoundException($request, message: "Nisu pronađeni računi za zadanog korisnika.");
        }

        $body = json_encode($data);

        $response->getBody()->write($body);
        return $response;
    }

    public function get_racun(Request $request, Response $response, string $iban) : Response {
        try {
            $data = $this->repository->get($iban);
        } catch (ErrorException $ex) {
            throw new \Slim\Exception\HttpInternalServerErrorException($request, message: $ex->getMessage());
        }

        if ($data == null) {
            throw new \Slim\Exception\HttpNotFoundException($request, message: "Nije pronađen račun.");
        }

        $body = json_encode($data);

        $response->getBody()->write($body);
        return $response;
    }
}