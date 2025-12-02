package tn.hitech.hitech;

import javafx.application.Application;
import tn.hitech.Database.*;
import tn.hitech.Database.config.DbInitialiser;
import tn.hitech.Models.*;

import java.util.Scanner;

public class Launcher {
    public static void main(String[] args) {
        ImprimanteRepo imprimanteRepo = new ImprimanteRepo();
        SmartphoneRepo smartphoneRepo = new SmartphoneRepo();
        ClientPPRepo clientPPRepo = new ClientPPRepo();
        ClientPMRepo clientPMRepo = new ClientPMRepo();
        CommandeRepo  commandeRepo = new CommandeRepo();
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

        ClientPP cl1=new ClientPP(1,"sfax","bilel@gmail.com",11111111,"triki","bilel");
        ClientPM cl2=new ClientPM(2,"sfax","abc@gmail.com",2222222,"A1234477S","abc");

        clientPPRepo.insert(cl1);
        clientPMRepo.insert(cl2);

        cl1.afficher();
        cl2.afficher();

        Commande c1 = new Commande(1,"20-10-2025","25-10-2025", Commande.PayMethode.CARTE,cl1);
        Commande c2=new Commande(2,"22-10-2025","25-10-2025", Commande.PayMethode.CASH,cl2);
        Commande c3=new Commande(3,"29-11-2025","03-12-2025", Commande.PayMethode.CARTE,cl1);
        Commande c4=new Commande(4,"29-11-2025","06-12-2025", Commande.PayMethode.CASH,cl2);

        //TODO: ajouter le reste de la question C

        commandeRepo.insert(c1);
        commandeRepo.insert(c2);
        commandeRepo.insert(c3);
        commandeRepo.insert(c4);

        System.out.println();
        System.out.println("*".repeat(58));
        System.out.println("======= Voulez vous ouvrir l'interface graphique ? =======");
        System.out.println("*".repeat(58));
        if (sc.nextInt() == 1) {
            Application.launch(HiTechApplication.class, args);
        }
    }
}
