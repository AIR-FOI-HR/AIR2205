<?php
declare(strict_types=1);

namespace App\Controllers;

use App\Exceptions\InvalidUserException;
use App\Repositories\KorisnikRepository;
use ErrorException;
use Psr\Http\Message\ResponseInterface as Response;
use Psr\Http\Message\ServerRequestInterface as Request;

class KorisnikController {

    public function __construct(private KorisnikRepository $repository) {}

    public function get_users(Request $request, Response $response) : Response {
        $data = $this->repository->get_all();
        $body = json_encode($data);

        $response->getBody()->write($body);
        return $response;
    }

    public function get_user(Request $request, Response $response, string $id) : Response {
        try {
            $data = $this->repository->get(intval($id));
        } catch (ErrorException $ex) {
            throw new \Slim\Exception\HttpInternalServerErrorException($request, message: $ex->getMessage());
        }

        if ($data == null) {
            throw new \Slim\Exception\HttpNotFoundException($request, message: "Korisnik nije pronađen.");
        }

        $body = json_encode($data);

        $response->getBody()->write($body);
        return $response;
    }

    public function create_user(Request $request, Response $response) : Response { 
        $body = $request->getParsedBody();
        
        try {
            $data = $this->repository->create($body);
        } catch (ErrorException $ex) {
            throw new \Slim\Exception\HttpInternalServerErrorException($request, message: $ex->getMessage());
        }

        if ($data == null) {
            throw new \Slim\Exception\HttpInternalServerErrorException($request, message: "Korisnik nije kreiran.");
        }

        $body = json_encode($data);
        $response->getBody()->write($body);
        return $response->withStatus(201);
    }

    public function update_user(Request $request, Response $response, string $id) : Response { 
        $body = $request->getParsedBody();
        try {
            $data = $this->repository->update(intval($id), $body);
        } catch (ErrorException $ex) {
            throw new \Slim\Exception\HttpInternalServerErrorException($request, message: $ex->getMessage());
        }

        if ($data == null) {
            throw new \Slim\Exception\HttpInternalServerErrorException($request, message: "Korisnik nije ažuriran.");
        }

        $body = json_encode($data);
        $response->getBody()->write($body);
        return $response->withStatus(201);
    }

    public function delete_user(Request $request, Response $response, string $id) : Response {
        try {
            $data = $this->repository->delete(intval($id));
        } catch (ErrorException $ex) {
            throw new \Slim\Exception\HttpInternalServerErrorException($request, message: $ex->getMessage());
        }

        $body = json_encode(["kor_id" => $id, "deleted" => true]);

        $response->getBody()->write($body);
        return $response;
    }

    public function login_user(Request $request, Response $response) : Response { 
        $body = $request->getParsedBody();
        try {
            $data = $this->repository->login($body);
        } catch (ErrorException $ex) {
            throw new \Slim\Exception\HttpInternalServerErrorException($request, message: $ex->getMessage());
        } catch (InvalidUserException $ex) {
            throw new \Slim\Exception\HttpForbiddenException($request, message: $ex->getMessage());
        }

        if ($data == null) {
            throw new \Slim\Exception\HttpBadRequestException($request, message: "Korisnik nije pronađen.");
        }

        $body = json_encode($data);
        $response->getBody()->write($body);
        return $response->withStatus(200);
    }

    public function restore_user(Request $request, Response $response) : Response { 
        $body = $request->getParsedBody();
        try {
            $data = $this->repository->restore($body);
        } catch (ErrorException $ex) {
            throw new \Slim\Exception\HttpInternalServerErrorException($request, message: $ex->getMessage());
        } catch (InvalidUserException $ex) {
            throw new \Slim\Exception\HttpForbiddenException($request, message: $ex->getMessage());
        }

        if ($data == null) {
            throw new \Slim\Exception\HttpBadRequestException($request, message: "Neuspješno generiranje koda.");
        }

        $body = json_encode($data);
        $response->getBody()->write($body);
        return $response->withStatus(200);
    }
}