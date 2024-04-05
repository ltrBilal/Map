package fr.maps.grapheUtils;


import fr.maps.*;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

public class GrapheUtil {

    //Retourne le noeud de distance minimale
    public Noeud getMinimumNode(Set<Noeud> nodeList) {
        double min = Double.MAX_VALUE;
        Noeud node = null;

        for(Noeud n : nodeList) {
            double nodeDistance = n.getDistance();
            if(nodeDistance < min) {
                min = nodeDistance;
                node = n;
            }
        }

        return node;
    }

    //Met a jour les distances et les noeuds parcouru (equivalent d'un tableau des predecesseurs)
    public static void calculateDistanceAndShortestPath(Noeud neighbor,double weight, Noeud currentNode) {
        double sourceDistance = currentNode.getDistance();
        if(sourceDistance + weight < neighbor.getDistance()) {
            neighbor.setDistance(sourceDistance + weight);
            LinkedList<Noeud> shortestPath = new LinkedList<>(currentNode.getShortestPath());
            shortestPath.add(currentNode);
            neighbor.setShortestPath(shortestPath);
        }
    }

    public List<Noeud> dijkstra(Graphe g, Noeud source, Noeud destination) {

        //Etape 1 : R�aliser le calcul du plus court chemin
        //Initialisation
        List<Noeud> visited = new ArrayList<>(); 		//List de noeuds qui contiendra les noeuds visit�s durant l'algorithme
        Set<Noeud> Q = new HashSet<>(); 				//Set de noeuds qui contiendra les noeuds visit�s durant l'algorithme

        source.setDistance(0.0);
        Q.add(source);


        //Debut Algorithme de Dijkstra
        while(Q.size() != 0) {
            Noeud u = getMinimumNode(Q);
            Q.remove(u);

            for(Noeud v : u.getAdj()) { 				//Pour tous les noeuds voisins du noeud u, faire :
                double w = u.calculateDistance(v);		//Calcul des distances entre le noeud u et ses voisins (poids des diff�rents arcs)
                if(!visited.contains(v)) {				//Si le noeud voisin n'a pas d�j� �t� visit� (trait�)
                    calculateDistanceAndShortestPath(v,w,u);	//Mise � jour des distances et de la liste "shortestPath" qui permettra de retrouver le chemin parcouru
                    Q.add(v);
                }
            }
            visited.add(u);								//Ajout du noeuds u visit�
        }

        //Fin Algorithme de Dijkstra


        //Etape 2 : R�cup�ration du plus court chemin calcul�
        //Initialisation
        List<List<Noeud>> listOfShortestPath = new ArrayList<List<Noeud>>();	//List de List de noeud
        List<Noeud> shortestPath = new ArrayList<Noeud>();						//La list qui contiendra le plus court chemin entre le noeud source et le noeud de destination


        //R�cup�ration de tous les chemins ayant au moins 2 noeuds dans leur chemin parcouru (shortestPath)
        for(Noeud n : g.getNodeMap().values()) {
            if(n.getShortestPath().size() > 1) {
                listOfShortestPath.add(n.getShortestPath());
            }
        }

        //R�cup�ration du chemin le plus court ayant comme noeud terminal le noeud de destination
        for(List<Noeud> nodeList : listOfShortestPath) {
            if(nodeList.get(nodeList.size()-1) == destination) {
                shortestPath = nodeList;
            }
        }

        return shortestPath;
    }

    //Fonction qui affiche le plus court chemin
    public void printPath(List<Noeud> shortestPath) {

        System.out.println("------- Affichage du plus court chemin -------");

        System.out.println("Distance depuis la source : " + shortestPath.get(shortestPath.size()-1).getDistance() + " m�tres");
        System.out.print("Chemin : ");
        for(int i = 0; i<shortestPath.size(); i++) {
            System.out.print(" -> "+shortestPath.get(i).getID());
        }
        System.out.println("");

        System.out.println("Fin");
    }

    //Renvoie un document (xml) grace a la boundingbox donnee
    public static Document getDocWithUrl(String address) {

        try {
        	URL url = new URL(address);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);          
            
            //creation du document par rapport a la connexion etablie (avec l'url contenant la requ�te)
            DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = dbfac.newDocumentBuilder();

            return docBuilder.parse(connection.getInputStream());

        } catch (IOException | SAXException | ParserConfigurationException e1) {
            e1.printStackTrace();
        }
        return null;
    }
    
  //Renvoie un InputStream correspondant � une url
    public static InputStream getInputStreamWithUrl(String address) {

        try {
        	URL url = new URL(address);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);

            return connection.getInputStream();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static void copyInputStreamToFile(InputStream inputStream, File file) throws IOException {

        try (FileOutputStream outputStream = new FileOutputStream(file, false)) {
            int read;
            byte[] bytes = new byte[8192];
            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }
        }
    }
}