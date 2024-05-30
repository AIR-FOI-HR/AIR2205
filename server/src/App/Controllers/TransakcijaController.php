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
        $data = $this->repository->get_all($iban);
        $body = json_encode($data);

        $response->getBody()->write($body);
        return $response;
    }
}