package tn.hitech.Models;

public class Article {
    private final double TVA = 0.19;

    private int refArticle;
    private String designation;
    private String image;
    private double prixHt;
    private int qteStock;
    private int promo;

    public Article() {
    }

    public Article(int refArticle, String designation, String image, double prixHt, int qteStock, int promo) {
        this.refArticle = refArticle;
        this.designation = designation;
        this.image = image;
        this.prixHt = prixHt;
        this.qteStock = qteStock;
        this.promo = promo;
    }

    public int getRefArticle() {
        return refArticle;
    }

    public void setRefArticle(int refArticle) {
        this.refArticle = refArticle;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public double getPrixHt() {
        return prixHt;
    }

    public void setPrixHt(double prixHt) {
        if (prixHt > 0) this.prixHt = prixHt;

    }

    public int getQteStock() {
        return qteStock;
    }

    public void setQteStock(int qteStock) {
        if (qteStock > 0) this.qteStock = qteStock;
    }

    public int getPromo() {
        return promo;
    }

    public void setPromo(int promo) {
        if (promo >= 0 && promo <100) this.promo = promo;
    }

    public boolean hasPromo() {
        return promo != 0;
    }

    public double calculerPrixTtc() {
        return prixHt * (1 - (double) promo / 100) * (1 + TVA);
    }

    public boolean isDisponible() {
        return qteStock > 0;
    }

    public void mouvmentStock(int qte) {
        qteStock += qte;
    }

    public void afficher() {
        System.out.println("L'article " + designation + " (réf: " + refArticle + ") " +
                "est au prix de " + prixHt + " DT HT " +
                (hasPromo() ? "avec une promotion de " + promo + "% " : "") +
                "et il y a " + qteStock + " unités en stock.");
    }
}

