package fr.maps;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class exporterItineraireXML {

    String repertoireCourant(){
        return System.getProperty("user.dir");
    }

    public void exporterItineraireXML(List<Noeud> itineraire, String nomFichier,TransportType transportType) {
        // Créer l'élément racine du document XML
        Element racine = new Element("itineraire");

        Element typeTrans = new Element("Type_de_transport");
        typeTrans.setText(String.valueOf(transportType));
        // Créer les éléments pour les différentes propriétés de l'itinéraire
        Element depart = new Element("depart");
        depart.setText(itineraire.get(0).getNom());

        Element arrivee = new Element("arrivee");
        arrivee.setText(itineraire.get(itineraire.size()-1).getNom());

        Element distance = new Element("Temps");
        distance.setText(String.valueOf(itineraire.get(itineraire.size()-1).convertTimeInTimeFormat()));

        // Créer les éléments pour les nœuds intermédiaires
        Element noeudsIntermediaires = new Element("noeudsIntermediaires");
        for (int i = 1;i<itineraire.size()-1;i++) {

            Element noeudElement = new Element("noeud");
            noeudElement.setAttribute("Id", String.valueOf(itineraire.get(i).getID()));
            noeudElement.setAttribute("lat", String.valueOf(itineraire.get(i).getLat()));
            noeudElement.setAttribute("lon", String.valueOf(itineraire.get(i).getLon()));
            noeudElement.setText(itineraire.get(i).getNom());
            noeudsIntermediaires.addContent(noeudElement);
        }

        Element distance1 = new Element("Distance");
        distance1.setText(String.valueOf(itineraire.get(itineraire.size()-1).getDistance()));


        // Ajouter les éléments à l'élément racine
        racine.addContent(typeTrans);
        racine.addContent(depart);
        racine.addContent(arrivee);
        racine.addContent(noeudsIntermediaires);
        racine.addContent(distance);
        racine.addContent(distance1);

        // Créer le document XML avec l'élément racine
        Document document = new Document(racine);

        String cheminRepertoire = repertoireCourant()+"/région de la france/";

        try {
            XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());

            // Définir le chemin complet du fichier de sortie
            String cheminComplet = cheminRepertoire + File.separator + nomFichier + ".xml";

            // Écrire le document XML dans le fichier de sortie
            xmlOutputter.output(document, new FileOutputStream(cheminComplet));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static  void main(String [] args){
        exporterItineraireXML exporterItineraireXML = new exporterItineraireXML();
        Graphe G = new Graphe(43.9492493, 4.8059012);

        Noeud source = G.rueToNode("place pie");
        Noeud destination = G.rueToNode("place des études");//"cours jean jaur�s");

        List<Noeud> noeudList = G.dijkstra(source, destination,TransportType.BUS,true,0);

        exporterItineraireXML.exporterItineraireXML(noeudList,"XML",TransportType.BICYCLE);
    }

}
