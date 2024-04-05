package fr.maps;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.NavigableMap;
import java.util.Set;

import javafx.scene.control.Label;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import fr.maps.grapheUtils.SaxParser;
import fr.maps.grapheUtils.GrapheUtil;

public class Graphe {

    private List<Way> wayList;
    private List<Relation> relList;

    private NavigableMap<Long, Noeud> nodeMap;

    private double minLat;
    private double minLon;
    private double maxLon;
    private double maxLat;

    public static Label label = new Label(" ");

    public void terminal ( String str) {
        label.setText(label.getText() + "\n"+str);
    }

    public Graphe(String adresse) {

        InputStream is = getInputStreamViaAdresse(adresse);
        System.out.println("SAX TRANSFER INFO! ");
        SaxParser.transfertInfo(this, is);

        System.out.println("Initialisation finie ! ");
        System.out.println("Taille nodeMap: "+nodeMap.size());
        System.out.println("Taille wayList: "+wayList.size());
        System.out.println("Taille relList: "+relList.size());
    }

    public Graphe(double lat, double lon) {

        InputStream is = getInputStreamViaCoordonnees(lat, lon);
        System.out.println("SAX TRANSFER INFO! ");
        SaxParser.transfertInfo(this, is);

        System.out.println("Initialisation finie ! ");
        System.out.println("Taille nodeMap: "+nodeMap.size());
        System.out.println("Taille wayList: "+wayList.size());
        System.out.println("Taille relList: "+relList.size());
    }


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

    //Retourne le noeud de temps minimum
    public Noeud getMinimumTimeNode(Set<Noeud> nodeList) {
        double min = Double.MAX_VALUE;
        Noeud node = null;

        for(Noeud n : nodeList) {
            double nodeTime = n.getTime();
            if(nodeTime < min) {
                min = nodeTime;
                node = n;
            }
        }

        return node;
    }

      /*
      //Met  jour les distances et les noeuds parcouru (quivalent d'un tableau des prdecesseurs)
      public static void calculateDistanceAndShortestPath(Noeud neighbor,double weight, timeWeight,Noeud currentNode) {

            double sourceDistance = currentNode.getDistance();
            if(sourceDistance + weight < neighbor.getDistance()) {
               neighbor.setDistance(sourceDistance + weight);
               LinkedList<Noeud> shortestPath = new LinkedList<>(currentNode.getShortestPath());
               shortestPath.add(currentNode);
               neighbor.setShortestPath(shortestPath);
             }
      }
      */

    //Met  jour les distances, les temps et les noeuds parcouru (quivalent d'un tableau des prdecesseurs)
    public static void calculateDistanceTimeAndShortestPath(Noeud neighbor,double weight,double timeWeight, Noeud currentNode,boolean isTimeChoosen) {

        double sourceDistance = currentNode.getDistance();
        double sourceTime = currentNode.getTime();

        if(isTimeChoosen){
            //System.out.println("TEMPS");

            if(sourceTime + timeWeight < neighbor.getTime()) {
                neighbor.setDistance(sourceDistance + weight);
                neighbor.setTime(sourceTime + timeWeight);
                LinkedList<Noeud> shortestPath = new LinkedList<>(currentNode.getShortestPath());
                shortestPath.add(currentNode);
                neighbor.setShortestPath(shortestPath);
            }
        }

        else {
            //System.out.println("DISTANCE");
            if(sourceDistance + weight < neighbor.getDistance()) {
                neighbor.setDistance(sourceDistance + weight);
                neighbor.setTime(sourceTime + timeWeight);
                LinkedList<Noeud> shortestPath = new LinkedList<>(currentNode.getShortestPath());
                shortestPath.add(currentNode);
                neighbor.setShortestPath(shortestPath);
            }
        }
    }



    public Noeud getNearestBusStop(Noeud noeud) {
        double dist = Double.MAX_VALUE;
        Noeud targetNoeud = null;

        for(Way w : getWayList()) {
            for(Noeud n : w.getlist()) {
                if(n.isBusStop()) {
                    double tempDist = noeud.calculateDistance(n);
                    if(tempDist < dist) {
                        dist = tempDist;
                        targetNoeud = n;
                    }
                }
            }
        }

        return targetNoeud;


    }

    public Noeud getNearestBusStopInSameBusRoute(Noeud source, Noeud destination) {

        double dist = Double.MAX_VALUE;
        Noeud targetNoeud = null;

        Relation busRelation = null;
        for(Relation rel : relList) {
            for(Member member : rel.getmember()) {
                if(member.getRef() == source.getID()){
                    busRelation = rel;
                    break;
                }
            }
        }

        for(Member member : busRelation.getmember()) {

            if(member.getType().equals("node")) {
                for(Noeud noeud : nodeMap.values()) {
                    if(member.getRef() == noeud.getID()) {
                        if(noeud.isBusStop()) {
                            double tempDist = destination.calculateDistance(noeud);
                            if(tempDist < dist) {
                                dist = tempDist;
                                targetNoeud = noeud;
                            }
                        }
                    }
                }

            }

        }

        return targetNoeud;


    }

    public List<Way> getBusRoute(Noeud source) {

        System.out.println("Source = "+source.getID());

        Relation busRelation = null;
        for(Relation rel : relList) {
            for(Member member : rel.getmember()) {
                if(member.getRef() == source.getID()){
                    busRelation = rel;
                    break;
                }
            }
        }

        List<Way> busRoute = new ArrayList<>();
        for(Member member : busRelation.getmember()) {

            if(member.getType().equals("way")) {
                for(Way way : wayList) {
                    if(member.getRef() == way.getID()) {
                        busRoute.add(way);
                    }
                }
            }

        }

        return busRoute;

    }



    public Noeud getNearestMotorizedVehicleNode(Noeud noeud) {
        double dist = Double.MAX_VALUE;
        Noeud targetNoeud = null;

        for(Way w : getWayList()) {
            for(Noeud n : w.getlist()) {
                if(n.isMotorizedVehicle()) {
                    double tempDist = noeud.calculateDistance(n);
                    if(tempDist < dist) {
                        dist = tempDist;
                        targetNoeud = n;
                    }
                }
            }
        }

        return targetNoeud;


    }


//    public static void main(String[] args) {
//
//
//       Graphe g = new Graphe(43.949317, 4.805528);
//
//       Noeud source = g.rueToNode("rue f�licien david");
//       Noeud destination = g.rueToNode("place pie");//"cours jean jaur�s");
//
//       List<Noeud> noeudList = g.dijkstra(source, destination,TransportType.BUS,true,0);
//       g.printPath(noeudList);
//
//    }

    public List<Noeud> busRoute(Noeud source, Noeud destination, TransportType transportType, TransportType tempTransportType, boolean isTimeChoosen, int cpt) {
        if(transportType == TransportType.BUS) {
            if(tempTransportType == TransportType.BUS) {

            }

            else if(tempTransportType == TransportType.PEDESTRIAN) {


                List<Noeud> path = new LinkedList<Noeud>();
                double pathDistance = 0.0;
                double pathTime = 0.0;

                List<Noeud> footPath = dijkstra(source,destination,TransportType.PEDESTRIAN,isTimeChoosen,cpt);
                double pathFootDistance = footPath.get(footPath.size()-1).getDistance();
                double pathFootTime = footPath.get(footPath.size()-1).getTime();

                Noeud currentSource = source;
                Noeud currentDestination = destination;
                Noeud busStop = null;

                while( ((path.isEmpty() != true && path.get(path.size() - 1) != currentDestination)) || path.isEmpty()) {

                    if(currentSource.getID() == source.getID()) {
                        busStop = getNearestBusStop(currentSource);
                    }
                    else if(getNearestBusStop(currentDestination) != busStop) {
                        System.out.println(currentSource.getID() +" "+busStop.getID() +" "+destination.getID());
                        busStop = getNearestBusStopInSameBusRoute(getNearestBusStop(currentDestination),currentDestination);
                        System.out.println(currentSource.getID() +" "+busStop.getID() +" "+destination.getID());
                    }
                    else {
                        busStop = destination;
                    }


                    List<Noeud> shortestPath = dijkstra(currentSource,busStop,TransportType.PEDESTRIAN,isTimeChoosen,cpt);




                    if(path.size() == 1) {
                        continue;
                    }
                    path.addAll(shortestPath);
                    printPath(shortestPath);
                    pathDistance += shortestPath.get(shortestPath.size()-1).getDistance();
                    pathTime += shortestPath.get(shortestPath.size()-1).getTime();

                    if(path.get(path.size() - 1) == destination)
                        break;



                    for(Noeud n : nodeMap.values()) {
                        n.setDistance(Double.MAX_VALUE);
                        n.setTime(Double.MAX_VALUE);
                        n.setShortestPath(new LinkedList<>());
                    }

                    Noeud dest = getNearestBusStopInSameBusRoute(busStop,currentDestination);
                    shortestPath = dijkstra(busStop,dest,TransportType.BUS,isTimeChoosen,cpt);



                    if(path.size() == 1) {
                        continue;
                    }
                    printPath(shortestPath);
                    path.addAll(shortestPath);
                    pathDistance += shortestPath.get(shortestPath.size()-1).getDistance();
                    pathTime += shortestPath.get(shortestPath.size()-1).getTime();

                    for(Noeud n : nodeMap.values()) {
                        n.setDistance(Double.MAX_VALUE);
                        n.setTime(Double.MAX_VALUE);
                        n.setShortestPath(new LinkedList<>());
                    }


                    currentSource = path.get(path.size() - 1);

                }


                footPath.get(footPath.size()-1).setTime(pathFootTime);
                footPath.get(footPath.size()-1).setDistance(pathFootDistance);

                System.out.println("FINIIII" + "  "+footPath.get(footPath.size()-1).getTime() + "  "+pathTime+ "  "+footPath.get(footPath.size()-1).getDistance() +"  "+pathDistance);

                if(isTimeChoosen) {
                    if(footPath.get(footPath.size()-1).getTime() < pathTime) {
                        return footPath;
                    }
                }
                else {
                    if(footPath.get(footPath.size()-1).getDistance() < pathDistance) {
                        return footPath;
                    }
                }

                path.get(path.size()-1).setTime(pathTime);
                path.get(path.size()-1).setDistance(pathDistance);

                return path;
            }
        }
        return null;
    }

    public List<Noeud> dijkstra(Noeud source, Noeud destination, TransportType transportType, boolean isTimeChoosen,int cpt) {
        //Etape 1 : Raliser le calcul du plus court chemin
        //Initialisation
        List<Noeud> visited = new ArrayList<>();      //List de noeuds qui contiendra les noeuds visits durant l'algorithme
        Set<Noeud> Q = new HashSet<>();             //Set de noeuds qui contiendra les noeuds visits durant l'algorithme

        List<Noeud> path = new LinkedList<Noeud>();

        source.setDistance(0.0);
        source.setTime(0.0);
        Q.add(source);


        if(transportType == transportType.CAR) {
            if(!destination.isMotorizedVehicle()) { destination = getNearestMotorizedVehicleNode(destination); }
        }

        TransportType tempTransportType = source.isBusStop() ? TransportType.BUS : TransportType.PEDESTRIAN;

        if(transportType == TransportType.BUS && cpt == 0) {
            path = busRoute(source,destination,transportType,tempTransportType,isTimeChoosen,cpt+1);
            System.out.println(path.get(path.size()-1).getDistance());
            printPath(path);
            return path;
        }

        System.out.println("BUS STOP = "+source.getID() + "  " + "BUS STOP DEST = "+destination.getID() + "  " + transportType + "  " + tempTransportType);

        //Debut Algorithme de Dijkstra
        while(Q.size() != 0) {
            Noeud u = isTimeChoosen ? getMinimumTimeNode(Q) : getMinimumNode(Q);
            Q.remove(u);



            for(Noeud v : u.getAdj()) {             //Pour tous les noeuds voisins du noeud u, faire :
                boolean isTheNodeIsCorrect = false;

                if(transportType == TransportType.PEDESTRIAN) {
                    tempTransportType = transportType;
                    isTheNodeIsCorrect = v.isPedestrian();
                }

                if(transportType == TransportType.BICYCLE) {
                    tempTransportType = transportType;
                    isTheNodeIsCorrect = v.isBicycle();
                }

                if(transportType == TransportType.BUS) {
                    if(tempTransportType == TransportType.BUS) {
                        isTheNodeIsCorrect = v.isBusStop() || v.isBus();
                    }

                    else if(tempTransportType == TransportType.PEDESTRIAN) {
                        isTheNodeIsCorrect =  v.isPedestrian();
                    }

                }

                if(transportType == TransportType.CAR) {
                    tempTransportType = transportType;
                    isTheNodeIsCorrect = v.isMotorizedVehicle();
                }

                double w = u.calculateDistance(v);    //Calcul des distances entre le noeud u et ses voisins (poids des diffrents arcs)
                double t = u.calculateTime(w,tempTransportType);


                if(!visited.contains(v) && isTheNodeIsCorrect) {            //Si le noeud voisin n'a pas dj t visit (trait)
                    calculateDistanceTimeAndShortestPath(v,w,t,u,isTimeChoosen); //Mise  jour des distances et de la liste "shortestPath" qui permettra de retrouver le chemin parcouru
                    Q.add(v);
                }


            }

            visited.add(u);                         //Ajout du noeuds u visit
        }

        //Fin Algorithme de Dijkstra


        //Etape 2 : Recupration du plus court chemin calcul
        //Initialisation

        LinkedList<Noeud> shortestPath = destination.getShortestPath();
        shortestPath.add(destination);


        System.out.println("dest = "+destination.getTime()+" "+destination.convertTimeInTimeFormat() +"  "+destination.getMaxMotorizedVehicleSpeed()+ "  "+destination.getDistance());
        double averageSpeed = 0.0;
        for(Noeud n : destination.getShortestPath()) {
            System.out.println(n.getMaxMotorizedVehicleSpeed()+"  "+n.getTime()+"  "+n.convertTimeInTimeFormat() + "  "+n.getDistance()+"  "+ n.getID());
            averageSpeed += n.getMaxMotorizedVehicleSpeed();
        }
        averageSpeed = averageSpeed / destination.getShortestPath().size();

        System.out.println("temps = "+destination.getDistance() / averageSpeed);
        printPath(shortestPath);


        return shortestPath;
    }

    //Fonction qui affiche le plus court chemin
    public void printPath(List<Noeud> shortestPath) {

        System.out.println("------- Affichage du plus court chemin -------");

        System.out.println("Distance depuis la source : " + shortestPath.get(shortestPath.size()-1).getDistance() + " mtres");
        System.out.println("Temps �coul� depuis la source : " + shortestPath.get(shortestPath.size()-1).convertTimeInTimeFormat());
        System.out.print("Chemin : ");
        for(int i = 0; i<shortestPath.size(); i++) {
            System.out.print(" -> "+shortestPath.get(i).getID());
        }
        System.out.println("");

        System.out.println("Fin");

    }

    public void printPathterminal(List<Noeud> shortestPath){
       terminal("------- Affichage du plus court chemin -------");
        terminal("Distance depuis la source : " + shortestPath.get(shortestPath.size()-1).getDistance() + " mtres");
        terminal("Temps �coul� depuis la source : " + shortestPath.get(shortestPath.size()-1).convertTimeInTimeFormat()+"minutes");
        terminal("Chemin : ");
        for(int i = 0; i<shortestPath.size(); i++) {
            terminal(" -> "+shortestPath.get(i).getNom());

        }
        terminal(" ");
        terminal("Fin");

    }




    // r�cup�re les coordonn�es GPS (boundingbox) par rapport � une adresse donnee
    public static  String getBoundingbox(String adresse){

        //creation l'url qui permet de recuperer le resultat de la recherche (sous forme d'un fichier xml)
        adresse = adresse.replaceAll("\\s+","+");
        String address = "https://nominatim.openstreetmap.org/search?q="+adresse+"&format=xml&polygon=1&addressdetails=1";
        String bbox = null;

        Document doc = GrapheUtil.getDocWithUrl(address);

        Element place = (Element)doc.getElementsByTagName("place").item(0);
        String coordonnees = place.getAttribute("boundingbox");

        //reoganisation des coordonnees -> doit correspondre au format OUEST/SUD/EST/NORD
        String bboxElement[] = coordonnees.split(",");
        //bbox = bboxElement[2]+","+bboxElement[0]+","+bboxElement[3]+","+ bboxElement[1];
        bbox = bboxElement[0]+","+bboxElement[2]+","+bboxElement[1]+","+ bboxElement[3];


        return bbox;
    }

    //Renvoie un document (xml) correspondant � l'adresse donnee
    public static InputStream getInputStreamViaAdresse(String adresse) {

        String bbox = getBoundingbox(adresse);
        return getInputStream(bbox);
    }

    //Renvoie un document (xml) correspondant aux coordonnees donnees
    public static InputStream getInputStreamViaCoordonnees(double lat, double lon) {

        double range = 0.01;
        String ouest = Double.toString(lon - range);
        String sud = Double.toString(lat - range);
        String est = Double.toString(lon + range);
        String nord = Double.toString(lat + range);

        //String bbox = ouest+","+sud+","+est+","+nord;
        String bbox = sud+","+ouest+","+nord+","+est;
        return getInputStream(bbox);
    }

    //Renvoie un document (xml) grace a la boundingbox donnee
    public static InputStream getInputStream(String bbox) {

        //Utilisation d'une requete par url
        String address = "https://overpass-api.de/api/interpreter?data=[timeout:300][out:xml][maxsize:1073741824][bbox:"+bbox+"];(node;way;rel;);out;";
        System.out.println(address);

        InputStream is = GrapheUtil.getInputStreamWithUrl(address);
      /*File f = new File("bbox.xml");
      try {
         GrapheUtil.copyInputStreamToFile(is, f);
      } catch (IOException e) {
         e.printStackTrace();
      }*/
        System.out.println("InputStream recupere");
        return is;
    }

    public Noeud rueToNode(String nomRue) {

        //System.out.println(nomRue +"  "+ getnodeMap().containsValue(nomRue));
        for(Noeud n : getNodeMap().values()) {
            if(n.getNom() != null) {
                //System.out.println(n.getNom());
            }
        }


        Noeud noeud = null;
        for(Way w : getWayList()) {
            if(w.getNom()!=null && w.getNom().equals(nomRue)) {
                for(Noeud n : w.getlist()) {
                    noeud = n;
                    noeud.setNom(nomRue);
                    System.out.println("Noeud trouv� directement � partir du nom du way = "+noeud.getNom());
                    return (noeud);
                }
            }

            // SINON calculer la distance entre les noeuds pour savoir quels noeuds est le plus proche du noeud avec cette addresse
        }

        for(Noeud n : getNodeMap().values()) {
            if(n.getNom() != null && n.getNom().equals(nomRue)) {
                noeud = n;
            }

        }


        if(noeud == null)
            return null;

        double dist = Double.MAX_VALUE;
        Noeud targetNoeud = null;

        for(Way w : getWayList()) {
            for(Noeud n : w.getlist()) {
                double tempDist = noeud.calculateDistance(n);
                if(tempDist < dist) {
                    dist = tempDist;
                    targetNoeud = n;

                }
            }
        }

        System.out.println("Noeud trouv� � partir du noeud le plus proche = "+targetNoeud.getNom());
        return targetNoeud;

    }

    public List<Way> getwayList() {
        return wayList;
    }

    public List<Way> getWayList() {
        return wayList;
    }

    public void setWayList(List<Way> wayList) {
        this.wayList = wayList;
    }

    public List<Relation> getRelList() {
        return relList;
    }

    public void setRelList(List<Relation> relList) {
        this.relList = relList;
    }

    public NavigableMap<Long, Noeud> getNodeMap() {
        return nodeMap;
    }

    public void setNodeMap(NavigableMap<Long, Noeud> nodeMap) {
        this.nodeMap = nodeMap;
    }


    public double getMinLat() {
        return minLat;
    }

    public void setMinLat(double minLat) {
        this.minLat = minLat;
    }

    public double getMinLon() {
        return minLon;
    }

    public void setMinLon(double minLon) {
        this.minLon = minLon;
    }

    public double getMaxLon() {
        return maxLon;
    }

    public void setMaxLon(double maxLon) {
        this.maxLon = maxLon;
    }

    public double getMaxLat() {
        return maxLat;
    }

    public void setMaxLat(double maxLat) {
        this.maxLat = maxLat;
    }
    public static void main(String [] args){
        //PourBounds

      Graphe G = new Graphe(43.9492493, 4.8059012);

       Noeud source = G.rueToNode("place pie");
       Noeud destination = G.rueToNode("place des études");//"cours jean jaur�s");

       List<Noeud> noeudList = G.dijkstra(source, destination,TransportType.BUS,true,0);
       G.printPath(noeudList);

       for(Noeud noeud: noeudList){
           System.out.println("latitude : "+noeud.getLat());
           System.out.println("longuitude : "+noeud.getLon());
           System.out.println("nom :"+noeud.getNom());
       }
       System.out.println(noeudList.get(noeudList.size()-1).convertTimeInTimeFormat());
       //G.printPath(G.dijkstra(source, destination,TransportType.BUS,true,0));


        //for(Noeud n : w.getlist()){
        //Noeud n1=G.PC(w.getlist(),n);
        //if(n1!=null) {
        //System.out.println(n1.getLon());
        //}
        //}
//}
//Pour NodeList
//for(Entry<Long, Noeud> entry : G.getnodeMap().entrySet()) {
        //if(G.lol(G.getnodeMap(),entry.getValue())!=null) {
        //System.out.println(G.lol(G.getnodeMap(),entry.getValue()).getID());
        //}
        //System.out.println(entry.getValue().getID());

    }
}