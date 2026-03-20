/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package magazzinoabenantelutzenbusatti;

import java.io.*;

/**
 *
 * @author lutzen.jacopo
 */
public class GestioneFileMagazzino {
    private final int id =4;
    private final int nome =40;
    private final int prezzoVendita =4;
    private final int prezzoAcquisto =4;
    private final int scorta =4;
    private final int scortaMin =4;
    private final int nVenduti =4;
    private final int DIM_RECORD =64;
    private static final String FILE_MAGAZZINO = "elencoProdotti.pdm";
    private Controllo c=new Controllo();
    
    public boolean creaFileStudente() {
        try (RandomAccessFile file = new RandomAccessFile(FILE_MAGAZZINO, "rw")) {
            int nRecord = (int) (file.length() / DIM_RECORD);
            System.out.println("File studenti creato/aperto con successo.");
            System.out.println("Record attualmente presenti: " + nRecord);
            return true;
        } catch (IOException e) {
            System.out.println("Problema in creazione/lettura file studenti: " + e.getMessage());
            return false;
        }
    }
    
    public boolean inserisciProdotto(int id, String nome, int prezzoVendita,int prezzoAcquisto,int scorta,int scortaMin,int nVenduti) {
        try (RandomAccessFile file = new RandomAccessFile(FILE_MAGAZZINO, "rw")) {
            file.seek(file.length());
            file.writeInt(id);
            file.writeChars(c.aggiustaLunghezzaStringa(nome));
            file.writeInt(prezzoVendita);
            file.writeInt(prezzoAcquisto);
            file.writeInt(scorta);
            file.writeInt(scortaMin);
            file.writeInt(nVenduti);
            System.out.println("Prodotto " + id + " " + nome +" " + prezzoVendita +" " + prezzoAcquisto +" " + scorta +" " + scortaMin +" " + nVenduti + " salvato con successo!");
            return true;
        } catch (IOException e) {
            System.out.println("Errore durante la scrittura: " + e.getMessage());
            return false;
        }
    }
    
    public boolean leggiStudente(int posizione) {
        try (RandomAccessFile file = new RandomAccessFile(FILE_MAGAZZINO, "r")) {
            long posizioneByte = (long) (posizione - 1) * DIM_RECORD;
            if (posizioneByte >= file.length()) {
                System.out.println("Nessun record trovato in questa posizione.");
                return false;
            }
            file.seek(posizioneByte);
            int id = file.readInt();
            String nome = c.leggiStringaDalFile(file);
            int prezzoVendita =file.readInt();
            int prezzoAcquisto=file.readInt();
            int scorta =file.readInt();
            int scortaMin = file.readInt();
            int nVenduti =file.readInt();
            System.out.println("--- Prodotto trovato ---");
            System.out.println("Prodotto " +" | Id:"+ id + " | Nome:" + nome +" | Prezzo Vendita:" + prezzoVendita +" | Prezzo Acquisto:" + prezzoAcquisto +" | Scorta:" + scorta +" | Scorta Minima:" + scortaMin +" | Quantita Vendute:" + nVenduti);
            return true;
        } catch (IOException e) {
            System.out.println("Errore in lettura: " + e.getMessage());
            return false;
        }
    }
    
}
