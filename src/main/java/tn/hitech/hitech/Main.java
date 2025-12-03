package tn.hitech.hitech;

import javafx.application.Application;
import tn.hitech.Database.*;
import tn.hitech.Database.config.DbInitialiser;
import tn.hitech.Models.*;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        ImprimanteRepo imprimanteRepo = new ImprimanteRepo();
        SmartphoneRepo smartphoneRepo = new SmartphoneRepo();
        ClientPPRepo clientPPRepo = new ClientPPRepo();
        ClientPMRepo clientPMRepo = new ClientPMRepo();
        CommandeRepo commandeRepo = new CommandeRepo();
        LigneCmdRepo ligneCmdRepo = new LigneCmdRepo();
        Scanner sc = new Scanner(System.in);
        DbInitialiser.dropAllTables();
        DbInitialiser.initialise();

        Article[] articles = new Article[4];
        articles[0] = new Imprimante(1, "Canon", "C://", 200, 70, 10, Imprimante.Type.JETENCRE, "CANON", 40);
        articles[1] = new Smartphone(2, "Galaxy A5", "C://", 600, 40, 0, "samsung", 128, 4, "android", 6);
        articles[2] = new Smartphone(3, "Iphone", "C://", 900, 20, 0, "Apple", 256, 4, "ios", 5);
        articles[3] = new Imprimante(1, "HP", "C://", 200, 70, 20, Imprimante.Type.LAZER, "HP", 80);

        imprimanteRepo.insert((Imprimante) articles[0]);
        smartphoneRepo.insert((Smartphone) articles[1]);
        smartphoneRepo.insert((Smartphone) articles[2]);
        imprimanteRepo.insert((Imprimante) articles[3]);

        for (int i = 0; i < articles.length; i++) {
            articles[i].afficher();
        }


        ClientPP cl1 = new ClientPP(1, "sfax", "bilel@gmail.com", 11111111, "triki", "bilel");
        ClientPM cl2 = new ClientPM(2, "sfax", "abc@gmail.com", 2222222, "A1234477S", "abc");

        clientPPRepo.insert(cl1);
        clientPMRepo.insert(cl2);

        cl1.afficher();
        cl2.afficher();


        Commande c1 = new Commande(1, new Date(20, 10, 2025), new Date(25, 10, 2025), Commande.PayMethode.CARTE, cl1);
        Commande c2 = new Commande(2, new Date(22, 10, 2025), new Date(25, 10, 2025), Commande.PayMethode.CASH, cl2);
        Commande c3 = new Commande(3, new Date(29, 11, 2025), new Date(3, 12, 2025), Commande.PayMethode.CARTE, cl1);
        Commande c4 = new Commande(4, new Date(29, 11, 2025), new Date(6, 12, 2025), Commande.PayMethode.CHECK, cl2);

        commandeRepo.insert(c1);
        commandeRepo.insert(c2);
        commandeRepo.insert(c3);
        commandeRepo.insert(c4);


        LigneCmd lc1 = new LigneCmd(1, 2, (Smartphone) articles[1], c1);
        LigneCmd lc9 = new LigneCmd(9, 2, (Imprimante) articles[3], c1);
        LigneCmd lc5 = new LigneCmd(5, 20, (Imprimante) articles[0], c1);
        LigneCmd lc2 = new LigneCmd(2, 3, (Smartphone) articles[2], c2);
        LigneCmd lc6 = new LigneCmd(6, 3, (Imprimante) articles[0], c2);
        LigneCmd lc10 = new LigneCmd(10, 1, (Imprimante) articles[3], c2);
        LigneCmd lc3 = new LigneCmd(3, 6, (Imprimante) articles[3], c3);
        LigneCmd lc7 = new LigneCmd(7, 8, (Smartphone) articles[2], c3);
        LigneCmd lc4 = new LigneCmd(4, 44, (Smartphone) articles[1], c4);
        LigneCmd lc8 = new LigneCmd(8, 3, (Smartphone) articles[2], c4);

        ligneCmdRepo.insert(lc1);
        ligneCmdRepo.insert(lc2);
        ligneCmdRepo.insert(lc3);
        ligneCmdRepo.insert(lc4);
        ligneCmdRepo.insert(lc5);
        ligneCmdRepo.insert(lc6);
        ligneCmdRepo.insert(lc7);
        ligneCmdRepo.insert(lc8);
        ligneCmdRepo.insert(lc9);
        ligneCmdRepo.insert(lc10);


        c1.afficher();
        c1.livrer();
        c1.afficher();
        c1.annuler();
        c1.afficher();
        c2.afficher();
        c2.livrer();
        c2.afficher();

        commandeRepo.updateStatus(c1.getId(),Commande.Etat.LIVREE);
        commandeRepo.updateStatus(c1.getId(),Commande.Etat.ANNULEE);
        commandeRepo.updateStatus(c2.getId(),Commande.Etat.LIVREE);

        for (int i = 0; i < articles.length; i++) {
            articles[i].afficher();
        }


        System.out.println();
        System.out.println("*".repeat(58));
        System.out.println("======= Voulez vous ouvrir l'interface graphique ? =======");
        System.out.println("*".repeat(58));
        System.out.println("(O) Oui");
        System.out.println("(N) Non");
        if (sc.nextLine().equalsIgnoreCase("o")) {
            Application.launch(HiTechApplication.class, args);
        }
    }
}
