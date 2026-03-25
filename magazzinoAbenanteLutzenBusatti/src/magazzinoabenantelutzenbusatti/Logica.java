/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package magazzinoabenantelutzenbusatti;

import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;

/**
 * @author abenante.lucia
 * Classe che gestisce TUTTA la logica dell'applicazione.
 * Il JFrame chiama solo i metodi di questa classe.
 * Nessun riferimento a componenti grafici qui dentro.
 */
public class Logica {

    // Qui dichiari le istanze delle classi di gestione che userai in tutti i metodi
    private GestioneFileMagazzino gfm      = new GestioneFileMagazzino();
    private GestioneFileKey       gfk      = new GestioneFileKey();
    private Magazzino             magazzino = new Magazzino();
    private Controllo             controllo = new Controllo();

    /**
     * Richiama all'avvio: crea i file se non esistono.
     */
    public void inizializza() {
        gfm.creaFileProdotti();
        gfk.creaFileKey();
    }

    // ── AGGIUNGI ──────────────────────────────────────────────

    /**
     * Aggiunge un prodotto dopo aver validato tutti i campi.
     * Qui aggiungi anche la riga al modello tabella passato dal JFrame.
     *
     * @return messaggio di esito (stringa da mostrare nella GUI)
     */
    public String aggiungiProdotto(String idStr, String nome, String acqStr,
                                   String venStr, String scortaStr, String scMinStr,
                                   DefaultTableModel modelTabella) {

        // Qui controlli che nessun campo sia rimasto vuoto
        if (!controllo.controlloNull(idStr)     || !controllo.controlloNull(nome)    ||
            !controllo.controlloNull(acqStr)    || !controllo.controlloNull(venStr)  ||
            !controllo.controlloNull(scortaStr) || !controllo.controlloNull(scMinStr)) {
            return "ERRORE: Tutti i campi sono obbligatori.";
        }

        // Qui verifichi che ID e tutti i campi numerici siano effettivamente interi
        if (!controllo.controlloInt(idStr)    || !controllo.controlloInt(acqStr)    ||
            !controllo.controlloInt(venStr)   || !controllo.controlloInt(scortaStr) ||
            !controllo.controlloInt(scMinStr)) {
            return "ERRORE: ID, prezzi e scorte devono essere numeri interi.";
        }

        // Qui converti le stringhe in int ora che sei sicuro siano validi
        int id        = Integer.parseInt(idStr);
        int acquisto  = Integer.parseInt(acqStr);
        int vendita   = Integer.parseInt(venStr);
        int scorta    = Integer.parseInt(scortaStr);
        int scortaMin = Integer.parseInt(scMinStr);

        // Qui controlli che l'ID non esista già nel Magazzino in memoria
        if (magazzino.esisteIdProdotto(id)) {
            return "ERRORE: Esiste già un prodotto con ID " + id + ".";
        }

        // Qui crei il Prodotto con nVenduti = 0 (nuovo prodotto appena inserito)
        Prodotto p = new Prodotto(id, nome, vendita, acquisto, scorta, scortaMin, 0);

        // Qui aggiungi il prodotto al Magazzino in memoria (HashMap + HashSet)
        magazzino.aggiungiAlMagazzino(id, p);

        // Qui aggiungi la riga alla JTable così l'utente la vede subito
        modelTabella.addRow(new Object[]{id, nome, acquisto, vendita, scorta, scortaMin});

        return "OK: Prodotto aggiunto con successo!";
    }

    // ── SALVA ─────────────────────────────────────────────────

    /**
     * Scansiona tutta la tabella e scrive ogni prodotto sul file binario
     * e sul file chiavi.
     * Qui ricevi il modello tabella dal JFrame per leggerne le righe.
     *
     * @return messaggio di esito
     */
    public String salvaTutti(DefaultTableModel modelTabella) {

        // Qui controlli che ci sia almeno un prodotto da salvare
        if (modelTabella.getRowCount() == 0) {
            return "ERRORE: Nessun prodotto da salvare.";
        }

        // Qui scorri ogni riga della tabella e scrivi il record sul file
        for (int i = 0; i < modelTabella.getRowCount(); i++) {

            // Qui leggi i valori di ogni cella della riga corrente
            int    id        = (int)    modelTabella.getValueAt(i, 0);
            String nome      = (String) modelTabella.getValueAt(i, 1);
            int    acquisto  = (int)    modelTabella.getValueAt(i, 2);
            int    vendita   = (int)    modelTabella.getValueAt(i, 3);
            int    scorta    = (int)    modelTabella.getValueAt(i, 4);
            int    scortaMin = (int)    modelTabella.getValueAt(i, 5);

            // Qui scrivi il record nel file binario elencoProdotti.pdm
            gfm.inserisciProdotto(id, nome, vendita, acquisto, scorta, scortaMin, 0);

            // Qui aggiungi la coppia (id → posizione i) nel file chiavi.txt
            gfk.aggiungiChiave(id, i);
        }

        return "OK: Tutti i prodotti sono stati salvati sul file.";
    }

    // ── ELIMINA ───────────────────────────────────────────────

    /**
     * Rimuove il prodotto nella riga selezionata dal Magazzino in memoria,
     * segna il tombstone nel file chiavi e toglie la riga dalla tabella.
     * Qui ricevi la riga selezionata dal JFrame (getSelectedRow()).
     *
     * @return messaggio di esito
     */
    public String eliminaProdotto(int rigaSelezionata, DefaultTableModel modelTabella) {

        // Qui controlli che l'utente abbia effettivamente selezionato una riga
        if (rigaSelezionata == -1) {
            return "ERRORE: Seleziona un prodotto dalla tabella prima di eliminare.";
        }

        // Qui leggi l'ID del prodotto nella colonna 0 della riga selezionata
        int id = (int) modelTabella.getValueAt(rigaSelezionata, 0);

        // Qui rimuovi il prodotto dal Magazzino in memoria (HashMap + HashSet)
        magazzino.rimuoviIdProdotto(id);

        // Qui segni la chiave come cancellata nel file chiavi.txt (tombstone id = -1)
        gfk.rimuoviChiave(id);

        // Qui rimuovi la riga dalla tabella grafica
        modelTabella.removeRow(rigaSelezionata);

        return "OK: Prodotto eliminato con successo.";
    }

    // ── CERCA ─────────────────────────────────────────────────

    /**
     * Costruisce la stringa formattata con tutti i dati del prodotto
     * nella riga selezionata, pronta per il JOptionPane.
     * Qui ricevi la riga selezionata dal JFrame (getSelectedRow()).
     *
     * @return stringa formattata oppure null se nessuna riga è selezionata
     */
    public String getDettagliProdotto(int rigaSelezionata, DefaultTableModel modelTabella) {

        // Qui controlli che l'utente abbia selezionato qualcosa
        if (rigaSelezionata == -1) {
            return null;
        }

        // Qui leggi tutti i valori della riga selezionata, colonna per colonna
        int    id        = (int)    modelTabella.getValueAt(rigaSelezionata, 0);
        String nome      = (String) modelTabella.getValueAt(rigaSelezionata, 1);
        int    acquisto  = (int)    modelTabella.getValueAt(rigaSelezionata, 2);
        int    vendita   = (int)    modelTabella.getValueAt(rigaSelezionata, 3);
        int    scorta    = (int)    modelTabella.getValueAt(rigaSelezionata, 4);
        int    scortaMin = (int)    modelTabella.getValueAt(rigaSelezionata, 5);

        // Qui costruisci e restituisci il messaggio formattato con tutti i campi
        return  "═══════════════════════════════\n" +
                "       DETTAGLI PRODOTTO\n"         +
                "═══════════════════════════════\n" +
                "ID Prodotto    : " + id        + "\n" +
                "Nome           : " + nome      + "\n" +
                "Prezzo Acquisto: " + acquisto  + " €\n" +
                "Prezzo Vendita : " + vendita   + " €\n" +
                "Scorta Attuale : " + scorta    + " pz\n" +
                "Scorta Minima  : " + scortaMin + " pz\n" +
                "═══════════════════════════════";
    }

    // ── UTILITÀ ───────────────────────────────────────────────

    /**
     * Restituisce tutti i prodotti caricati dal Magazzino in memoria.
     * Utile se vuoi popolare la tabella al riavvio.
     */
    public ArrayList<Prodotto> getProdotti() {
        return new ArrayList<>(magazzino.getProdotti());
    }
    // ── VENDI ─────────────────────────────────────────────────

/**
 * Scala la scorta del prodotto selezionato della quantità indicata.
 * Qui controlli che la scorta non scenda sotto il minimo prima di procedere.
 *
 * @return messaggio di esito
 */
public String vendiProdotto(int riga, int quantita, DefaultTableModel modelTabella) {
    if (quantita <= 0) return "ERRORE: La quantità deve essere maggiore di zero.";

    int scorta    = (int) modelTabella.getValueAt(riga, 4);
    int scortaMin = (int) modelTabella.getValueAt(riga, 5);
    int nuovaScorta = scorta - quantita;

    // Qui blocchi la vendita se la nuova scorta andrebbe sotto il minimo
    if (nuovaScorta < scortaMin) {
        return "ERRORE: Non puoi vendere " + quantita + " pezzi. Le scorte scenderebbero a "
             + nuovaScorta + ", sotto il minimo di " + scortaMin
             + ". Devi prima comprare scorte!";
    }

    // Qui aggiorni la cella Giacenza nella tabella
    modelTabella.setValueAt(nuovaScorta, riga, 4);

    // Qui aggiorni anche scorta e nVenduti nel Magazzino in memoria
    int id = (int) modelTabella.getValueAt(riga, 0);
    for (Prodotto p : magazzino.getProdotti()) {
        if (p.getId() == id) {
            p.setScorta(nuovaScorta);
            p.setnVenduti(p.getnVenduti() + quantita);
            break;
        }
    }

    return "OK: Vendita di " + quantita + " pezzi registrata. Scorta attuale: " + nuovaScorta + ".";
}

// ── COMPRA ────────────────────────────────────────────────

/**
 * Aumenta la scorta del prodotto selezionato della quantità indicata.
 * Qui non ci sono vincoli minimi: si possono sempre comprare scorte.
 *
 * @return messaggio di esito
 */
public String compraProdotto(int riga, int quantita, DefaultTableModel modelTabella) {
    if (quantita <= 0) return "ERRORE: La quantità deve essere maggiore di zero.";

    int scorta = (int) modelTabella.getValueAt(riga, 4);
    int nuovaScorta = scorta + quantita;

    // Qui aggiorni la cella Giacenza nella tabella
    modelTabella.setValueAt(nuovaScorta, riga, 4);

    // Qui aggiorni anche la scorta nel Magazzino in memoria
    int id = (int) modelTabella.getValueAt(riga, 0);
    for (Prodotto p : magazzino.getProdotti()) {
        if (p.getId() == id) {
            p.setScorta(nuovaScorta);
            break;
        }
    }

    return "OK: Acquisto di " + quantita + " pezzi registrato. Scorta attuale: " + nuovaScorta + ".";
}

// ── STATISTICHE ───────────────────────────────────────────

/**
 * Calcola tutte le statistiche dal modelTabella e dal Magazzino in memoria.
 * Qui restituisci un array di 8 stringhe pronte per le label:
 * [0] prodottiDiversi, [1] totPezzi, [2] valoreMagazzino, [3] margine,
 * [4] piuVenduto, [5] menoVenduto, [6] piuCostoso, [7] menoCostoso
 */
public String[] getStatistiche(DefaultTableModel modelTabella) {
    if (modelTabella.getRowCount() == 0) {
        return new String[]{"0", "0", "0 €", "0 €", "—", "—", "—", "—"};
    }

    int totPezzi = 0, valoreMagazzino = 0, margine = 0;
    int maxVenduti = Integer.MIN_VALUE, minVenduti = Integer.MAX_VALUE;
    int maxCosto   = Integer.MIN_VALUE, minCosto   = Integer.MAX_VALUE;
    String piuVenduto = "—", menoVenduto = "—", piuCostoso = "—", menoCostoso = "—";

    // Qui scorri ogni riga della tabella e accumuli i valori
    for (int i = 0; i < modelTabella.getRowCount(); i++) {
        int    id       = (int)    modelTabella.getValueAt(i, 0);
        String nome     = (String) modelTabella.getValueAt(i, 1);
        int    acquisto = (int)    modelTabella.getValueAt(i, 2);
        int    vendita  = (int)    modelTabella.getValueAt(i, 3);
        int    scorta   = (int)    modelTabella.getValueAt(i, 4);

        totPezzi        += scorta;
        valoreMagazzino += acquisto * scorta;
        margine         += (vendita - acquisto) * scorta;

        // Qui cerchi il prodotto in memoria per leggere nVenduti (non è in tabella)
        int nVenduti = 0;
        for (Prodotto p : magazzino.getProdotti()) {
            if (p.getId() == id) { nVenduti = p.getnVenduti(); break; }
        }

        if (nVenduti > maxVenduti) { maxVenduti = nVenduti; piuVenduto  = nome; }
        if (nVenduti < minVenduti) { minVenduti = nVenduti; menoVenduto = nome; }
        if (vendita  > maxCosto)   { maxCosto   = vendita;  piuCostoso  = nome; }
        if (vendita  < minCosto)   { minCosto   = vendita;  menoCostoso = nome; }
    }

    return new String[]{
        String.valueOf(modelTabella.getRowCount()),
        String.valueOf(totPezzi),
        valoreMagazzino + " €",
        margine + " €",
        piuVenduto,
        menoVenduto,
        piuCostoso,
        menoCostoso
    };
}

// ── ALLARME SCORTE ────────────────────────────────────────

/**
 * Restituisce le righe dei prodotti la cui scorta è <= scorta minima.
 * Qui le usi per popolare jTable4 nella scheda statistiche.
 */
public ArrayList<Object[]> getProdottiSottoScorta(DefaultTableModel modelTabella) {
    ArrayList<Object[]> lista = new ArrayList<>();
    for (int i = 0; i < modelTabella.getRowCount(); i++) {
        int scorta    = (int) modelTabella.getValueAt(i, 4);
        int scortaMin = (int) modelTabella.getValueAt(i, 5);
        // Qui includi il prodotto solo se è sotto o uguale al minimo
        if (scorta <= scortaMin) {
            lista.add(new Object[]{
                modelTabella.getValueAt(i, 0), // ID
                modelTabella.getValueAt(i, 1), // Nome
                scorta,
                scortaMin
            });
        }
    }
    return lista;
}
}