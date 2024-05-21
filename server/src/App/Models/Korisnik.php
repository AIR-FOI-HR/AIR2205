<?php
    declare(strict_types=1);

    namespace App\Models;
    use JsonSerializable;

    class Korisnik implements JsonSerializable {
        private int $id;
        private string $ime;
        private string $prezime;
        private string $email;
        private string $oib;
        private string $pin;
        private string $kod;
        private string $tel_broj;

        public function __construct(int $id, string $ime, string $prezime, string $email, 
            string $oib, string $pin, string $kod, string $tel_broj)
        {
            $this->id = $id;
            $this->ime = $ime;
            $this->prezime = $prezime;
            $this->email = $email;
            $this->oib = $oib;
            $this->pin = $pin;
            $this->kod = $kod;
            $this->tel_broj = $tel_broj;
        }

        public function jsonSerialize(): mixed
        {
            return [
                "kor_id" => $this->id, 
                "ime" => $this->ime, 
                "prezime" => $this->prezime, 
                "email" => $this->email, 
                "oib" => $this->oib,
                "pin" => $this->pin,
                "kod" => $this->kod,
                "tel_broj" => $this->tel_broj,
            ];
        }

        public function get_pin() : string {
            return $this->pin;
        }
    }