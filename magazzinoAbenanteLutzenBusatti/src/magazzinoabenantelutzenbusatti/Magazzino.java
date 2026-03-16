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
    private HashSet<Integer> prodotti = new HashSet<>();
    private HashMap<Integer, HashSet<Integer>> magazzino = new HashMap<>();


    
    
    
    
    // Get & Set
    public HashSet<Integer> getId() {
        return id;
    }

    public HashSet<Integer> getProdotti() {
        return prodotti;
    }

    public HashMap<Integer, HashSet<Integer>> getMagazzino() {
        return magazzino;
    }

    public void setId(HashSet<Integer> id) {
        this.id = id;
    }

    public void setProdotti(HashSet<Integer> prodotti) {
        this.prodotti = prodotti;
    }

    public void setMagazzino(HashMap<Integer, HashSet<Integer>> magazzino) {
        this.magazzino = magazzino;
    }

    
    
    
    
}
