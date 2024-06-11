<?php
declare(strict_types=1);

namespace App\Models;
use JsonSerializable;

class Obavijest implements JsonSerializable {
    private int $id;
    private string $naslov;
    private string $tekst;
    private string $datum;
    private bool $procitano;

    public function __construct(int $id, string $naslov, string $tekst, string $datum, bool $procitano = false)
    {
        $this->id = $id;
        $this->naslov = $naslov;
        $this->tekst = $tekst;
        $this->datum = $datum;
        $this->procitano = $procitano;
    }

    public function jsonSerialize(): mixed
    {
        return [
            "obav_id" => $this->id, 
            "naslov" => $this->naslov, 
            "tekst" => $this->tekst, 
            "datum" => $this->datum, 
            "procitano" => $this->procitano,
        ];
    }

    public function get_id() : int {
        return $this->id;
    }
}