package tn.hitech.Models;

public class Imprimante extends Article {
    private Type type;
    private String marque;
    private int pageParMinute;

    public Imprimante() {
    }

    public Imprimante(int refArticle, String designation, String image, double prixHt, int qteStock, int promo, Type type, String marque, int page_par_minute) {
        super(refArticle, designation, image, prixHt, qteStock, promo);
        this.type = type;
        this.marque = marque;
        pageParMinute = page_par_minute;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getMarque() {
        return marque;
    }

    public void setMarque(String marque) {
        this.marque = marque;
    }

    public int getPageParMinute() {
        return pageParMinute;
    }

    public void setPageParMinute(int pageParMinute) {
        this.pageParMinute = pageParMinute;
    }

    @Override
    public void afficher() {
        int colSize = 20;
        System.out.println("-".repeat(colSize * 8));
        System.out.print("| " + getDesignation() + " ".repeat(colSize - getDesignation().length()) + " |");
        System.out.print("| " + getPrixHt() + " ".repeat(colSize - String.valueOf(getPrixHt()).length()) + " |");
        System.out.print("| " + getPromo() + " ".repeat(colSize - String.valueOf(getPromo()).length()) + " |");
        System.out.println("| " + getQteStock() + " ".repeat(colSize - String.valueOf(getQteStock()).length()) + " |");
        System.out.print("| " + getType().name() + " ".repeat(colSize - getType().name().length()) + " |");
        System.out.print("| " + getMarque() + " ".repeat(colSize - getMarque().length()) + " |");
        System.out.print("| " + getPageParMinute() + " ".repeat(colSize - String.valueOf(getPageParMinute()).length()) + " |");
    }

    public String toString() {
        return "imprimante{" +
                "type=" + type +
                ", marque='" + marque + '\'' +
                ", pageParMinute=" + pageParMinute +
                '}';
    }

    public enum Type {
        JETENCRE,
        LAZER
    }
}
