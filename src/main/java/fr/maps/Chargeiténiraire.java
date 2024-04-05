package fr.maps;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
public class Chargeiténiraire {


    public List<Noeud> chargeité(String fichierXml) throws ParserConfigurationException, IOException, SAXException {
        // Chemin du fichier XML
        String filePath = repertoireCourant();

        // Création du parseur de document
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        // Chargement du document XML
        File file = new File(filePath+"/région de la france/"+fichierXml+".xml");
        Document document = builder.parse(file);

        // Récupération de la racine du document
        Element root = document.getDocumentElement();

        // Création de la liste de nœuds
        List<Noeud> listeNoeuds = new ArrayList<Noeud>();

        // Lecture des éléments noeud du fichier XML
        NodeList noeudsIntermediaires = root.getElementsByTagName("noeud");
        for (int i = 0; i < noeudsIntermediaires.getLength(); i++) {
            Element elementNoeud = (Element) noeudsIntermediaires.item(i);

            // Récupération des attributs du nœud
            long id = Long.parseLong(elementNoeud.getAttribute("Id"));
            double lat = Double.parseDouble(elementNoeud.getAttribute("lat"));
            double lon = Double.parseDouble(elementNoeud.getAttribute("lon"));
            String nom = elementNoeud.getTextContent();

            // Création du nœud et ajout à la liste
            Noeud noeud = new Noeud(id, lat, lon, nom);
            listeNoeuds.add(noeud);
        }

        // Affichage des nœuds
        for (Noeud noeud : listeNoeuds) {
            System.out.println(noeud.getNom());
        }
        return listeNoeuds;

    }

    String repertoireCourant(){
        return System.getProperty("user.dir");
    }


    public static void main(String [] args) throws ParserConfigurationException, IOException, SAXException {
        Chargeiténiraire chargeiténiraire = new Chargeiténiraire();
        List<Noeud> noeudList = chargeiténiraire.chargeité("place pie_place des études");
        for(Noeud noeud:noeudList){
            System.out.println(noeud.getID());
        }
    }
}

