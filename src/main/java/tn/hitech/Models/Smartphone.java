package tn.hitech.Models;

public class Smartphone extends Article {
    private String marque;
    private int stockage;
    private int ram;
    private String os;
    private double taille_ecran;

    public Smartphone() {
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
        int colSize = 15;
        System.out.println("-".repeat(colSize * 10));
        System.out.print("| " + getDesignation() + " ".repeat(colSize - getDesignation().length()) + " |");
        System.out.print("| " + getPrixHt() + " ".repeat(colSize - String.valueOf(getPrixHt()).length()) + " |");
        System.out.print("| " + getPromo() + " ".repeat(colSize - String.valueOf(getPromo()).length()) + " |");
        System.out.print("| " + getQteStock() + " ".repeat(colSize - String.valueOf(getQteStock()).length()) + " |");
        System.out.print("| " + marque + " ".repeat(colSize - marque.length()) + " |");
        System.out.print("| " + os + " ".repeat(colSize - os.length()) + " |");
        System.out.print("| " + taille_ecran + " ".repeat(colSize - String.valueOf(taille_ecran).length()) + " |");
        System.out.println("| " + ram + " ".repeat(colSize - String.valueOf(ram).length()) + " |");
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
