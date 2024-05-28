<?php
declare(strict_types=1);

namespace App\Models;
use JsonSerializable;

class Racun implements JsonSerializable {
    private string $iban;
    private string $vrijedi_od;
    private string $aktivnost;
    private float $stanje;
    private string $vlasnik;
    private string $valuta;
    private string $vrsta_rac;

    public function __construct(string $iban, string $vrijedi_od, string $aktivnost, float $stanje, 
        string $vlasnik, string $valuta, string $vrsta_rac)
    {
        $this->iban = $iban;
        $this->vrijedi_od = $vrijedi_od;
        $this->aktivnost = $aktivnost;
        $this->stanje = $stanje;
        $this->vlasnik = $vlasnik;
        $this->valuta = $valuta;
        $this->vrsta_rac = $vrsta_rac;
    }

    public function jsonSerialize(): mixed
    {
        return [
            "iban" => $this->iban, 
            "vrijedi_od" => $this->vrijedi_od, 
            "aktivnost" => $this->aktivnost, 
            "stanje" => $this->stanje, 
            "vlasnik" => $this->vlasnik,
            "valuta" => $this->valuta,
            "vrsta_rac" => $this->vrsta_rac
        ];
    }

    public function get_iban() : string {
        return $this->iban;
    }
}