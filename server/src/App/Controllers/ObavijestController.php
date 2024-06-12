<?php
declare(strict_types=1);

namespace App\Controllers;

use App\Repositories\ObavijestRepository;
use ErrorException;
use Psr\Http\Message\ResponseInterface as Response;
use Psr\Http\Message\ServerRequestInterface as Request;

class ObavijestController {
    public function __construct(private ObavijestRepository $repository) {} 

    public function get_obavijesti_for_korisnik(Request $request, Response $response, string $kor_id): Response {
        try {
            $data = $this->repository->get_all_for_korisnik(intval($kor_id));
        } catch (ErrorException $ex)  {
            throw new \Slim\Exception\HttpInternalServerErrorException($request, message: $ex->getMessage());
        }

        if ($data == null) {
            throw new \Slim\Exception\HttpNotFoundException($request, message: "Nisu pronađene obavijesti za korisnika.");
        }

        $body = json_encode($data);

        $response->getBody()->write($body);
        return $response;
    }

    public function get_obavijest(Request $request, Response $response, string $obav_id): Response {
        try {
            $data = $this->repository->get(intval($obav_id));
        } catch (ErrorException $ex)  {
            throw new \Slim\Exception\HttpInternalServerErrorException($request, message: $ex->getMessage());
        }

        if ($data == null) {
            throw new \Slim\Exception\HttpNotFoundException($request, message: "Nije pronađena obavijest.");
        }

        $body = json_encode($data);
        
        $response->getBody()->write($body);
        return $response;
    }

    public function create_obavijest(Request $request, Response $response) : Response {
        $body = $request->getParsedBody();
        
        try {
            $data = $this->repository->create($body);
        } catch (ErrorException $ex) {
            throw new \Slim\Exception\HttpInternalServerErrorException($request, message: $ex->getMessage());
        }

        if ($data == null) {
            throw new \Slim\Exception\HttpInternalServerErrorException($request, message: "Slanje obavijesti nije uspjelo!");
        }

        $body = json_encode($data);
        $response->getBody()->write($body);
        return $response->withStatus(201);
    }

    public function update_obavijest_procitano(Request $request, Response $response) : Response {
        $body = $request->getParsedBody();
        
        try {
            $this->repository->update_procitano($body);
        } catch (ErrorException $ex) {
            throw new \Slim\Exception\HttpInternalServerErrorException($request, message: $ex->getMessage());
        }

        $body = json_encode(["procitano" => true]);
        $response->getBody()->write($body);
        return $response->withStatus(200);
    }
}