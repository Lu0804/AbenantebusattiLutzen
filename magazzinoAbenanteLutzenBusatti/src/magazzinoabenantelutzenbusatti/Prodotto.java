/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package magazzinoabenantelutzenbusatti;

/**
 *
 * @author abenante.lucia
 */
public class Prodotto {
    private int Id;//i del prodotto
    private String nome; // nome del prodotto
    private int prezzoVendita; // prezzo di vendita
    private int prezzoAcquisto; // prezzo d'acquiato
    private int scorta; // scorte attuali
    private int scortaMin; //scorte minime, sotto non ci può andare  
    private int nVenduti; // prodotto venduto di più 

    public Prodotto(int Id, String nome, int prezzoVendita, int prezzoAcquisto, int scorta, int scortaMin, int nVenduti) {
        this.Id = Id;
        this.nome = nome;
        this.prezzoVendita = prezzoVendita;
        this.prezzoAcquisto = prezzoAcquisto;
        this.scorta = scorta;
        this.scortaMin = scortaMin;
        this.nVenduti = nVenduti;
    }

    
    //Get & Set
    public int getId() {
        return Id;
    }

    public String getNome() {
        return nome;
    }

    public int getPrezzoVendita() {
        return prezzoVendita;
    }

    public int getPrezzoAcquisto() {
        return prezzoAcquisto;
    }

    public int getScorta() {
        return scorta;
    }

    public int getScortaMin() {
        return scortaMin;
    }

    public int getnVenduti() {
        return nVenduti;
    }

    public void setId(int Id) {
        this.Id = Id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setPrezzoVendita(int prezzoVendita) {
        this.prezzoVendita = prezzoVendita;
    }

    public void setPrezzoAcquisto(int prezzoAcquisto) {
        this.prezzoAcquisto = prezzoAcquisto;
    }

    public void setScorta(int scorta) {
        this.scorta = scorta;
    }

    public void setScortaMin(int scortaMin) {
        this.scortaMin = scortaMin;
    }

    public void setnVenduti(int nVenduti) {
        this.nVenduti = nVenduti;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + this.Id;
        return hash;
    } 
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Prodotto other = (Prodotto) obj;
        return this.Id == other.Id;
    }

    @Override
    public String toString() {
        return "Il prodotto" + nome + "con Id " + Id + " che costa " + prezzoVendita + " £, che è sato comprato a " + prezzoAcquisto + " £, sono in scorta " + scorta + " prodotti, la sua scorta minima è" + scortaMin + " Ne sono stati venuti" + nVenduti;
    }
    
    
}
