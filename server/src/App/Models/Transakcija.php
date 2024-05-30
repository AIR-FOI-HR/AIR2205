<?php
declare(strict_types=1);

namespace App\Models;
use JsonSerializable;

class Transakcija implements JsonSerializable {
    private int $id;
    private string $opis_placanja;
    private string $iznos;
    private string $model;
    private string $poziv_na_broj;
    private string $datum;
    private string $platitelj_iban;
    private string $platitelj_vlasnik;
    private string $primatelj_iban;
    private string $primatelj_vlasnik;

    public function __construct(int $id, string $opis_placanja, string $iznos, string $model, string $poziv_na_broj, 
        string $datum, string $platitelj_iban, string $platitelj_vlasnik, string $primatelj_iban, string $primatelj_vlasnik)
    {
        $this->id = $id;
        $this->opis_placanja = $opis_placanja;
        $this->iznos = $iznos;
        $this->model = $model;
        $this->poziv_na_broj = $poziv_na_broj;
        $this->datum = $datum;
        $this->platitelj_iban = $platitelj_iban;
        $this->platitelj_vlasnik = $platitelj_vlasnik;
        $this->primatelj_iban = $primatelj_iban;
        $this->primatelj_vlasnik = $primatelj_vlasnik;
    }

    public function jsonSerialize(): mixed
    {
        return [
            "tran_id" => $this->id, 
            "opis_placanja" => $this->opis_placanja, 
            "iznos" => $this->iznos,
            "model" => $this->model,
            "poziv_na_broj" => $this->poziv_na_broj, 
            "datum" => $this->datum,
            "platitelj_iban" => $this->platitelj_iban,
            "platitelj_vlasnik" => $this->platitelj_vlasnik,
            "primatelj_iban" => $this->primatelj_iban,
            "primatelj_vlasnik" => $this->primatelj_vlasnik,
        ];
    }

    public function get_id() : int {
        return $this->id;
    }
}