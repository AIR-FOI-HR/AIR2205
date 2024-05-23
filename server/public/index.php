<?php
declare(strict_types=1);

use Selective\BasePath\BasePathMiddleware;
use Slim\Factory\AppFactory;
use DI\Container;
use App\Middleware\AddJsonResponseHeader;
use Psr\Http\Message\ServerRequestInterface;
use Psr\Log\LoggerInterface;
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

$app->group('/api/users', function (RouteCollectorProxy $group) {
    $group->get('', App\Controllers\KorisnikController::class . ':get_users');
    $group->get('/{id}', App\Controllers\KorisnikController::class . ':get_user');
    $group->post('', App\Controllers\KorisnikController::class . ':create_user');
    $group->patch('/{id}', App\Controllers\KorisnikController::class . ':update_user');
    $group->delete('/{id}', App\Controllers\KorisnikController::class . ':delete_user');
    $group->post('/auth', App\Controllers\KorisnikController::class . ':login_user');
    $group->post('/restore', App\Controllers\KorisnikController::class . ':restore_user');
});

$app->run();