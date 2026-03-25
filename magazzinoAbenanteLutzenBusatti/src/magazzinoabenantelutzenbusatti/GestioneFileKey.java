package magazzinoabenantelutzenbusatti;

import java.io.*;
import java.util.ArrayList;
/**
 *
 * @author abenante.lucia
 * Gestisce il file testo delle chiavi: chiavi.txt
 *
 * Ogni riga ha il formato:  id,posizione
 * Esempio:
 *   1,0
 *   2,1
 *   5,2
 *
 * "posizione" è l'indice record (0-based) nel file elencoProdotti.pdm.
 * Una riga con id = -1 indica un record cancellato (tombstone).
 */
public class GestioneFileKey {

    private static final String FILE_KEY      = "chiavi.txt";
    private static final int    ID_CANCELLATO = -1;

    // ── CREAZIONE / APERTURA ──────────────────────────────────

    /**
     * Crea o apre il file chiavi.txt. Stampa quante chiavi contiene.
     */
    public boolean creaFileKey() {
        File f = new File(FILE_KEY);
        try {
            if (!f.exists()) f.createNewFile();
            int nChiavi = leggiTutteLeRighe().size();
            System.out.println("File chiavi aperto. Chiavi presenti: " + nChiavi);
            return true;
        } catch (IOException e) {
            System.out.println("Errore apertura file chiavi: " + e.getMessage());
            return false;
        }
    }

    // ── SCRITTURA ─────────────────────────────────────────────

    /**
     * Aggiunge una coppia (id, posizione) nel file chiavi.
     * Riutilizza uno slot cancellato (id=-1) se disponibile,
     * altrimenti aggiunge una nuova riga in fondo.
     *
     * @param id        id del prodotto
     * @param posizione posizione nel file principale (0-based)
     * @return true se riuscito
     */
    public boolean aggiungiChiave(int id, int posizione) {
        // Controlla duplicato
        if (cercaPosizionePerID(id) >= 0) {
            System.out.println("Chiave già esistente per id: " + id);
            return false;
        }

        ArrayList<String> righe = leggiTutteLeRighe();

        // Cerca uno slot cancellato da riutilizzare
        for (int i = 0; i < righe.size(); i++) {
            String[] parti = righe.get(i).split(",");
            if (parti.length == 2 && Integer.parseInt(parti[0].trim()) == ID_CANCELLATO) {
                righe.set(i, id + "," + posizione);
                scriviTutteLeRighe(righe);
                System.out.println("Chiave aggiunta (slot riutilizzato): id=" + id + " → posizione=" + posizione);
                return true;
            }
        }

        // Nessuno slot libero: aggiunge in fondo
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_KEY, true))) {
            bw.write(id + "," + posizione);
            bw.newLine();
            System.out.println("Chiave aggiunta: id=" + id + " → posizione=" + posizione);
            return true;
        } catch (IOException e) {
            System.out.println("Errore scrittura chiave: " + e.getMessage());
            return false;
        }
    }

    // ── LETTURA ───────────────────────────────────────────────

    /**
     * Dato un id, restituisce la posizione del prodotto nel file principale.
     *
     * @param idCercato id da cercare
     * @return posizione (0-based) nel file principale, oppure -1 se non trovato
     */
    public int cercaPosizionePerID(int idCercato) {
        for (String riga : leggiTutteLeRighe()) {
            String[] parti = riga.split(",");
            if (parti.length == 2) {
                int id  = Integer.parseInt(parti[0].trim());
                int pos = Integer.parseInt(parti[1].trim());
                if (id == ID_CANCELLATO) continue;
                if (id == idCercato) return pos;
                
            }
        }
        return -1;
    }

    /**
     * Stampa tutte le righe del file chiavi (utile per debug).
     */
    public void stampaTutteLeChiavi() {
        ArrayList<String> righe = leggiTutteLeRighe();
        if (righe.isEmpty()) {
            System.out.println("File chiavi vuoto.");
            return;
        }
        System.out.println("=== File chiavi (" + righe.size() + " righe) ===");
        for (int i = 0; i < righe.size(); i++) {
            String[] parti = righe.get(i).split(",");
            int id  = Integer.parseInt(parti[0].trim());
            int pos = Integer.parseInt(parti[1].trim());
            if (id == ID_CANCELLATO) {
                System.out.println("[" + i + "] --- cancellato ---");
            } else {
                System.out.println("[" + i + "] id=" + id + " → posizione=" + pos);
            }
        }
    }

    // ── CANCELLAZIONE ─────────────────────────────────────────

    /**
     * Cancella logicamente la chiave: sovrascrive l'id con -1 mantenendo la posizione.
     *
     * @param idDaRimuovere id da cancellare
     * @return true se trovato e cancellato
     */
    public boolean rimuoviChiave(int idDaRimuovere) {
        ArrayList<String> righe = leggiTutteLeRighe();
        boolean trovato = false;

        for (int i = 0; i < righe.size(); i++) {
            String[] parti = righe.get(i).split(",");
            if (parti.length == 2 && Integer.parseInt(parti[0].trim()) == idDaRimuovere) {
                righe.set(i, ID_CANCELLATO + "," + parti[1].trim()); // tombstone
                trovato = true;
                break;
            }
        }

        if (!trovato) {
            System.out.println("ID " + idDaRimuovere + " non trovato nel file chiavi.");
            return false;
        }

        scriviTutteLeRighe(righe);
        System.out.println("Chiave id=" + idDaRimuovere + " rimossa.");
        return true;
    }

    // ── AGGIORNA POSIZIONE ────────────────────────────────────

    /**
     * Aggiorna la posizione associata a un id.
     *
     * @param id             id del prodotto
     * @param nuovaPosizione nuova posizione nel file principale
     * @return true se riuscito
     */
    public boolean aggiornaPosizioneChiave(int id, int nuovaPosizione) {
        ArrayList<String> righe = leggiTutteLeRighe();
        boolean trovato = false;

        for (int i = 0; i < righe.size(); i++) {
            String[] parti = righe.get(i).split(",");
            if (parti.length == 2 && Integer.parseInt(parti[0].trim()) == id) {
                righe.set(i, id + "," + nuovaPosizione);
                trovato = true;
                break;
            }
        }

        if (!trovato) {
            System.out.println("ID " + id + " non trovato nel file chiavi.");
            return false;
        }

        scriviTutteLeRighe(righe);
        System.out.println("Posizione aggiornata: id=" + id + " → " + nuovaPosizione);
        return true;
    }

    // ── PRIVATI ───────────────────────────────────────────────

    /** Legge tutte le righe non vuote dal file. */
    private ArrayList<String> leggiTutteLeRighe() {
        ArrayList<String> righe = new ArrayList<>();
        File file = new File(FILE_KEY);
        if (!file.exists()) return righe;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String riga;
            while ((riga = br.readLine()) != null) {
                if (!riga.trim().isEmpty()) righe.add(riga.trim());
            }
        } catch (IOException e) {
            System.out.println("Errore lettura file chiavi: " + e.getMessage());
        }
        return righe;
    }

    /** Riscrive tutto il file con le righe fornite (sovrascrittura completa). */
    private void scriviTutteLeRighe(ArrayList<String> righe) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_KEY, false))) {
            for (String riga : righe) {
                bw.write(riga);
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Errore riscrittura file chiavi: " + e.getMessage());
        }
    }
}