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
        System.out.println("L'imprimante " + getDesignation() +
                " de marque " + marque +
                " est de type " + type.name() +
                " et imprime " + pageParMinute + " pages par minute. " +
                "Elle est au prix de " + getPrixHt() + " DT HT " +
                (hasPromo() ? "avec " + getPromo() + "% de réduction " : "") +
                "et il reste " + getQteStock() + " unités en stock.");
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
