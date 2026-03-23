/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package magazzinoabenantelutzenbusatti;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 *
 * @author abenante.lucia
 */
public class Controllo {

    private static final String FILE_MAGAZZINO = "elencoProdotti.pdm";

    /**
     * inseriesce gli * per lo spazio che rimane dai 20 char
     *
     * @param s stringa da controllare
     * @return la stringa modificata
     */
    public String aggiustaLunghezzaStringa(String s) {
        String aggiustata = s;
        if (s.length() < 20) {
            for (int i = 0; i < (20 - s.length()); i++) {
                aggiustata += "*";
            }
            return aggiustata;
        } else if (s.length() > 20) {
            aggiustata = s.substring(0, 19);
            return aggiustata;
        }
        return s;
    }

    /**
     * controlla se una variabile String è un intero
     *
     * @param n parametro da controllare
     * @return boolean se ci somo errori
     */
    public boolean controlloInt(String n) {
        try {
            //prova a convertire il parametro in un int
            int numero = Integer.parseInt(n);
        } catch (NumberFormatException e) {
            // n è una Stringa
            System.out.println("Errore: Il dato inserito non è un numero valido!");
            return false;
        }
        // n è un intero
        return true;
    }

    /**
     * controlla se una variabile String è una String
     *
     * @param n parametro da controllare
     * @return boolean se ci somo errori
     */
    public boolean controlloString(String n) {
        //provo a convertire il parametro in un int
        try {
            int numero = Integer.parseInt(n);
        } catch (NumberFormatException e) {
            // n è una Stringa
            return true;
        }
        // n è un intero
        System.out.println("Errore: Il dato inserito è un numero !");
        return false;
    }

    /**
     * controlla se una variabile String è nulla
     *
     * @param n parametro da controllare
     * @return boolean se ci somo errori
     */
    public boolean controlloNull(String n) {
        if (!n.isEmpty()) {
            return true;
        }
        return false;
    }

    /**
     * gli passi il file e legge la stringa leva gli * e lo spazio
     *
     * @param file file da modificare 
     * @return file modificato
     * @throws IOException
     */
    public String leggiStringaDalFile(RandomAccessFile file) throws IOException {
        StringBuilder sb = new StringBuilder();

        // Legge esattamente 'lunghezza' caratteri dal file
        for (int i = 0; i < 20; i++) {
            sb.append(file.readChar());
        }

        // Converte in Stringa
        String letta = sb.toString();

        // Rimuove tutti gli asterischi aggiunti in fase di scrittura
        // Usa replace("*", "") per sostituire gli asterischi con ""
        return letta.replace("*", "").trim();
    }
    
    /**
     * 
     * @param file file da dove prendere i dati
     * @param DIM_RECORD grandezza recod
     * @param nBit somma di tutti i campi
     * @return valore massimo
     * @throws IOException 
     */
    public int controlloMax(RandomAccessFile file, int DIM_RECORD, int nBit) throws IOException {

        Integer max = Integer.MIN_VALUE;
        //ciclo per controllare il valore massimo
        for (int i = 0; i < file.length() / DIM_RECORD; i++) {
            long posizioneByte = (long) (i) * DIM_RECORD; // long è un intero molto grande
            if (posizioneByte >= file.length()) {
                System.out.println("Nessun record trovato in questa posizione.");
                return Integer.MIN_VALUE;
            }
            file.seek(posizioneByte + nBit); //iniza sul record desiderato
            int n = file.readInt();
            if (n > max) {
                max = n;
            }

        }
        return max;
    }
    /**
     * 
     * @param file file da dove prendere i dati
     * @param DIM_RECORD grandezza recod
     * @param nBit somma di tutti i campi
     * @return valore minimo
     * @throws IOException 
     */
    public int controlloMin(RandomAccessFile file, int DIM_RECORD, int nBit) throws IOException {

        Integer min = Integer.MAX_VALUE;
        //ciclo per controllare il valore minimo
        for (int i = 0; i < file.length() / DIM_RECORD; i++) {
            long posizioneByte = (long) (i) * DIM_RECORD;// long è un intero molto grande
            if (posizioneByte >= file.length()) {
                System.out.println("Nessun record trovato in questa posizione.");
                return Integer.MAX_VALUE;
            }
            file.seek(posizioneByte + nBit);  //iniza sul record desiderato
            int n = file.readInt();
            if (n < min) {
                min = n;
            }

        }
        return min;//valore min
    }
    /**
     * metodo che controlla se ci sono le scorte 
     * @param p prodotto delle scorte 
     * @return se le scorte sono troppe poche
     */
    public boolean controlloScorte(Prodotto p){
        if(p.getScorta()>p.getScortaMin()) return true;
        return false;
    }

}
