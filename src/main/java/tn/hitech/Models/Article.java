package tn.hitech.Models;

public class Article {
    private final double TVA = 0.19;

    private int refArticle;
    private String designation;
    private String image;
    private double prixHt;
    private int qteStock;
    private int tauxRemPromo;

    public Article() {
        refArticle = 1;
        designation = "designation";
        image = "image";
        prixHt = 100;
        qteStock = 10;
        tauxRemPromo = 0;
    }

    public Article(int refArticle, String designation, String image, double prixHt, int qteStock, int tauxRemPromo) {
        this.refArticle = refArticle;
        this.designation = designation;
        this.image = image;
        setPrixHt(prixHt);
        setQteStock(qteStock);
        setPromo(tauxRemPromo);

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
        return tauxRemPromo;
    }

    public void setPromo(int tauxRemPromo) {
        if (tauxRemPromo >= 0 && tauxRemPromo < 100) this.tauxRemPromo = tauxRemPromo;
    }

    public boolean promotion() {
        return tauxRemPromo != 0;
    }

    public double calculerPrixTtc(int qte) {
        return prixHt * qte * (1 - (double) tauxRemPromo / 100) * (1 + TVA);
    }

    public boolean isDisponible() {
        return qteStock > 0;
    }

    public void mouvmentStock(int qte) {
        int nouvelleQte = qteStock + qte;
        if (nouvelleQte > 0)
            this.qteStock = nouvelleQte;
    }

    public void afficher() {
        System.out.println();
        System.out.println("## L'article " + designation + " (réf: " + refArticle + ") " +
                "est au prix de " + prixHt + " DT HT " +
                (promotion() ? "avec une promotion de " + tauxRemPromo + "% " : "") +
                "et il y a " + qteStock + " unités en stock.");
    }

    @Override
    public String toString() {
        return "Article{" +
                "TVA=" + TVA +
                ", refArticle=" + refArticle +
                ", designation='" + designation + '\'' +
                ", image='" + image + '\'' +
                ", prixHt=" + prixHt +
                ", qteStock=" + qteStock +
                ", promo=" + tauxRemPromo +
                '}';
    }
}

