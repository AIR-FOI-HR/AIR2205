<?php
declare(strict_types=1);

use Selective\BasePath\BasePathMiddleware;
use Slim\Factory\AppFactory;
use DI\Container;
use App\Middleware\AddJsonResponseHeader;
use Slim\Handlers\Strategies\RequestResponseArgs;
use Slim\Routing\RouteCollectorProxy;

require __DIR__ . '/../vendor/autoload.php';

$container = new Container();

AppFactory::setContainer($container);

$app = AppFactory::create();

$collector = $app->getRouteCollector();
$collector->setDefaultInvocationStrategy(new RequestResponseArgs);

$app->addRoutingMiddleware();
$app->add(new BasePathMiddleware($app));
$app->add(new AddJsonResponseHeader);
$app->addBodyParsingMiddleware();

$error_middleware = $app->addErrorMiddleware(true, true, true);
$error_handler = $error_middleware->getDefaultErrorHandler();
$error_handler->forceContentType('application/json');

$app->group('/api/korisnici', function (RouteCollectorProxy $group) {
    $group->get('', App\Controllers\KorisnikController::class . ':get_users');
    $group->get('/{id}', App\Controllers\KorisnikController::class . ':get_user');
    $group->post('', App\Controllers\KorisnikController::class . ':create_user');
    $group->patch('/{id}', App\Controllers\KorisnikController::class . ':update_user');
    $group->delete('/{id}', App\Controllers\KorisnikController::class . ':delete_user');
    $group->post('/auth', App\Controllers\KorisnikController::class . ':login_user');
    $group->post('/restore', App\Controllers\KorisnikController::class . ':restore_user');
});

$app->group('/api/racuni', function (RouteCollectorProxy $group) {
    $group->get('/{iban}', App\Controllers\RacunController::class . ':get_racun');
    $group->get('/korisnik/{kor_id}', App\Controllers\RacunController::class . ':get_racuni');
    $group->get('/telbroj/{tel_broj}', App\Controllers\RacunController::class . ':get_racun_from_tel_broj');
});

$app->group('/api/transakcije', function (RouteCollectorProxy $group) {
    $group->get('/racun/{iban}', App\Controllers\TransakcijaController::class . ':get_transakcije');
    $group->get('/korisnik/{kor_id}', App\Controllers\TransakcijaController::class . ':get_transakcije_korisnika');
    $group->get('/{tran_id}', App\Controllers\TransakcijaController::class . ':get_transakcija');
    $group->post('', App\Controllers\TransakcijaController::class . ':create_transakcija');
});

$app->group('/api/obavijesti', function (RouteCollectorProxy $group) {
    $group->get('/{obav_id}', App\Controllers\ObavijestController::class . ':get_obavijest');
    $group->get('/korisnik/{kor_id}', App\Controllers\ObavijestController::class . ':get_obavijesti_for_korisnik');
    $group->post('', App\Controllers\ObavijestController::class . ':create_obavijest');
    $group->patch('/procitaj', App\Controllers\ObavijestController::class . ':update_obavijest_procitano');
});

$app->run();