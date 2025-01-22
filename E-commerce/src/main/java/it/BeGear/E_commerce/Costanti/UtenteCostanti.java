    package it.BeGear.E_commerce.Costanti;

    public class UtenteCostanti {

        private UtenteCostanti(){}

        public static final String STATUS_200 = "200";
        public static final String STATUS_200_MESSAGE = "Operazione completata";
        public static final String STATUS_201 = "201";
        public static final String STATUS_201_MESSAGE = "Utente creato con successo";
        public static final String STATUS_500 = "500";
        public static final String STATUS_500_MESSAGE = "Errore del server";
        public static final String STATUS_400 = "400";
        public static final String STATUS_400_MESSAGE = "Dati utente non validi";
        public static final String STATUS_404 = "404";
        public static final String STATUS_404_MESSAGE = "Utente non trovato";
        public static final String STATUS_409 = "409";  // Aggiungi lo stato per il conflitto
        public static final String STATUS_409_MESSAGE = "Email già registrata"; // Messaggio per il conflitto
    }
