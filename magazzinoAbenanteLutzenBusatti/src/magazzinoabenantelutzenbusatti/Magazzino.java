/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package magazzinoabenantelutzenbusatti;

import java.util.HashMap;
import java.util.HashSet;

/**
 *
 * @author abenante.lucia
 */
public class Magazzino {
    /**
     * prodotto con l'id, aggiungere e levare il prodotto. i metodi dei controlli e statistiche 
     * 
     */
    private HashSet<Integer> id = new HashSet<>();
    private HashSet<Prodotto> prodotti = new HashSet<>();
    
    private HashMap<Integer, HashSet<Prodotto>> magazzino = new HashMap<>();


     public boolean aggiungiIdProdotto(int idProdotto) {
        boolean nuovo = id.add(idProdotto); // add restituisce false se già presente
        if (nuovo) {
            magazzino.put(idProdotto, new HashSet<>());
        }
        return nuovo;
    }

    /** Rimuove id del prodotto da id e dalla mappa */
    public boolean rimuoviIdProdotto(int idProdotto) {
        magazzino.remove(idProdotto);
        return id.remove(idProdotto);
    }

    /** Controlla se una prdotto esiste */
    public boolean esisteIdProdotto(int idProdotto) {
        return id.contains(idProdotto);
    }
    
    /**
     * Aggiunge un id gita al sistema.
     */
    public boolean aggiungiProdotto(Prodotto p) {
        return prodotti.add(p); // add restituisce false se già presente
    }

    /** Rimuove un id gita */
    public boolean rimuoviProdotto(Prodotto p) {
        return prodotti.remove(p);
    }

    /** Controlla se un id gita esiste */
    public boolean esisteProdotto(Prodotto p) {
        return prodotti.contains(p);
    }

    public boolean aggiungiAlMagazzino(int id, Prodotto p) {
        aggiungiIdProdotto(id); // crea l'entry nella mappa se non c'è
        aggiungiProdotto(p);
        return magazzino.get(id).add(p);
    }

    /**
     * Rimuove l'associazione studente-gita dalla mappa.
     */
    public boolean rimuoviDalMagazzino(int id, Prodotto p) {
        if (!magazzino.containsKey(id)) {
            return false;
        }
        return magazzino.get(id).remove(p);
    }

    /**
     * Controlla se uno studente è iscritto a una gita specifica.
     */
    public boolean esisteIscrizione(int id, Prodotto p) {
        if (!magazzino.containsKey(id)) {
            return false;
        }
        return magazzino.get(id).contains(p);
    }

    
    // Get & Set
    public HashSet<Integer> getId() {
        return id;
    }

    public HashSet<Prodotto> getProdotti() {
        return prodotti;
    }

    public HashMap<Integer, HashSet<Prodotto>> getMagazzino() {
        return magazzino;
    }

    public void setId(HashSet<Integer> id) {
        this.id = id;
    }

    public void setProdotti(HashSet<Prodotto> prodotti) {
        this.prodotti = prodotti;
    }

    public void setMagazzino(HashMap<Integer, HashSet<Prodotto>> magazzino) {
        this.magazzino = magazzino;
    }
   
    
}
