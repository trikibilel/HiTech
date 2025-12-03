package tn.hitech.Models;

public class Smartphone extends Article {
    private String marque;
    private int stockage;
    private int ram;
    private String os;
    private double taille_ecran;

    public Smartphone() {
        super();
        this.marque = "marque";
        this.stockage = 128;
        this.ram = 4;
        this.os = "os";
        this.taille_ecran = 5;
    }

    public Smartphone(int refArticle, String designation, String image, double prixHt, int qteStock, int promo, String marque, int stockage, int ram, String os, double tailleEcran) {
        super(refArticle, designation, image, prixHt, qteStock, promo);
        this.marque = marque;
        this.stockage = stockage;
        this.ram = ram;
        this.os = os;
        this.taille_ecran = tailleEcran;
    }

    public String getMarque() {
        return marque;
    }

    public void setMarque(String marque) {
        this.marque = marque;
    }

    public int getStockage() {
        return stockage;
    }

    public void setStockage(int stockage) {
        this.stockage = stockage;
    }

    public int getRam() {
        return ram;
    }

    public void setRam(int ram) {
        this.ram = ram;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public double getTailleEcran() {
        return taille_ecran;
    }

    public void setTailleEcran(double taille_ecran) {
        this.taille_ecran = taille_ecran;
    }

    @Override
    public void afficher() {
        System.out.println("## Le smartphone " + getDesignation() +
                " de marque " + marque +
                " fonctionne sous " + os +
                ", dispose de " + ram + " Go de RAM, " +
                stockage + " Go de stockage, " +
                "un écran de " + taille_ecran + " pouces. " +
                "Il est au prix de " + getPrixHt() + " DT HT " +
                (promotion() ? "avec " + getPromo() + "% de réduction " : "") +
                "et il reste " + getQteStock() + " unités en stock.");
    }

    public String toString() {
        return "Smartphone{" +
                "marque='" + marque + '\'' +
                ", stockage=" + stockage +
                ", ram=" + ram +
                ", os='" + os + '\'' +
                ", tailleEcran=" + taille_ecran +
                '}';
    }
}
