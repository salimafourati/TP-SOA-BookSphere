package org.example;

import java.io.File;
import java.util.Scanner;

import javax.xml.XMLConstants;
import javax.xml.validation.*;
import javax.xml.transform.stream.StreamSource;
import javax.xml.parsers.*;
import javax.xml.xpath.*;
import org.w3c.dom.*;

public class Main {

    public static void main(String[] args) {

        try {
            // Les fichiers XML et XSD
            File xml = new File("src/main/resources/bibliotheque.xml");
            File xsd = new File("src/main/resources/bibliotheque.xsd");

            // 1. Validation du fichier XML
            SchemaFactory sf = SchemaFactory.newInstance(
                    XMLConstants.W3C_XML_SCHEMA_NS_URI);

            Schema schema = sf.newSchema(xsd);
            Validator validateur = schema.newValidator();
            validateur.validate(new StreamSource(xml));

            System.out.println("Le fichier XML est valide !");

            // 2. Lecture du fichier XML
            DocumentBuilder constructeur =
                    DocumentBuilderFactory.newInstance().newDocumentBuilder();

            Document document = constructeur.parse(xml);

            // 3. Préparation de XPath
            XPath xpath = XPathFactory.newInstance().newXPath();

            Scanner scanner = new Scanner(System.in);
            int choix;

            // 4. Menu principal
            do {
                System.out.println("\n===== MENU BIBLIOTHEQUE =====");
                System.out.println("1. Afficher tous les produits");
                System.out.println("2. Afficher les livres");
                System.out.println("3. Afficher les magazines");
                System.out.println("4. Afficher les bandes dessinees");
                System.out.println("5. Afficher les produits disponibles");
                System.out.println("0. Quitter");
                System.out.print("Votre choix : ");

                choix = scanner.nextInt();

                String requete = "";

                // 5. Choix de la requête XPath
                switch (choix) {
                    case 1:
                        requete = "/bibliotheque/*";
                        break;

                    case 2:
                        requete = "/bibliotheque/livre";
                        break;

                    case 3:
                        requete = "/bibliotheque/magazine";
                        break;

                    case 4:
                        requete = "/bibliotheque/bandeDessinee";
                        break;

                    case 5:
                        requete = "/bibliotheque/*[disponible='true']";
                        break;

                    case 0:
                        System.out.println("Au revoir !");
                        break;

                    default:
                        System.out.println("Choix invalide !");
                }

                // 6. Affichage des résultats
                if (choix >= 1 && choix <= 5) {

                    NodeList produits = (NodeList) xpath.evaluate(
                            requete,
                            document,
                            XPathConstants.NODESET);

                    System.out.println("\n===== RESULTATS =====");

                    for (int i = 0; i < produits.getLength(); i++) {

                        Element produit = (Element) produits.item(i);

                        String type = produit.getTagName();
                        String id = produit.getAttribute("id");

                        String titre = produit
                                .getElementsByTagName("titre")
                                .item(0).getTextContent();

                        String prix = produit
                                .getElementsByTagName("prix")
                                .item(0).getTextContent();

                        String disponible = produit
                                .getElementsByTagName("disponible")
                                .item(0).getTextContent();

                        System.out.println("\n------------------------");
                        System.out.println("Type : " + type);
                        System.out.println("ID : " + id);
                        System.out.println("Titre : " + titre);
                        System.out.println("Prix : " + prix + " DT");
                        System.out.println("Disponible : " + disponible);

                        if (type.equals("livre")) {

                            String auteur = produit
                                    .getElementsByTagName("auteur")
                                    .item(0).getTextContent();

                            System.out.println("Auteur : " + auteur);

                        } else if (type.equals("magazine")) {

                            String numero = produit
                                    .getElementsByTagName("numero")
                                    .item(0).getTextContent();

                            System.out.println("Numero : " + numero);

                        } else if (type.equals("bandeDessinee")) {

                            String illustrateur = produit
                                    .getElementsByTagName("illustrateur")
                                    .item(0).getTextContent();

                            System.out.println("Illustrateur : " + illustrateur);
                        }
                    }

                    if (produits.getLength() == 0) {
                        System.out.println("Aucun produit trouve.");
                    }
                }

            } while (choix != 0);

            scanner.close();

        } catch (Exception e) {
            System.out.println("Erreur : " + e.getMessage());
        }
    }
}
