package tn.hitech.Models;

public class LigneCmd {
    private int id;
    private int qte;
    private double totalTtcLigne;
    private Article article;
    private Commande commande;


    public LigneCmd() {
        this.id = 1;
        this.qte = 10;
        this.totalTtcLigne = article.calculerPrixTtc(qte);
        this.article = new Article();
        this.commande = new Commande();
        this.commande.getLigneCmds().add(this);
    }

    public LigneCmd(int id, int qte, Article article, Commande commande) {
        this.id = id;
        this.qte = qte;
        this.totalTtcLigne = article.calculerPrixTtc(qte);
        this.article = article;
        this.commande = commande;
        this.commande.getLigneCmds().add(this);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQte() {
        return qte;
    }

    public void setQte(int qte) {
        this.qte = qte;
    }

    public double getTotalTtcLigne() {
        return totalTtcLigne;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public Commande getCommande() {
        return commande;
    }

    public void setCommande(Commande commande) {
        this.commande = commande;
    }

    public void afficher() {
        System.out.println("Ligne de commande: " + qte + " x " +
                article.getDesignation() +
                " (réf: " + article.getRefArticle() + ") " +
                "pour un total de " + totalTtcLigne + " DT TTC.");
    }

    @Override
    public String toString() {
        return "LigneCmd{" +
                "id=" + id +
                ", qte=" + qte +
                ", totalTtcLigne=" + totalTtcLigne +
                ", article=" + article +
                ", commande=" + commande +
                '}';
    }
}
