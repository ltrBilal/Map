package fr.maps;

import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;

public class DessinAll {

    private static final double LON0 = 4.77;
    private static final double LAT0 = 43.91;
     static double SCALE =7.0;



    public static Label legendLabel;

    public Text text ;

    public void showLegend2( List<String> description) {
        int i =0;
        legendLabel.setText(description.get(i));
        i++;
        while(i< description.size()){
            legendLabel.setText(legendLabel.getText() + "\n"+description.get(i));
            i++;
        }
        //  Text text = new Text(description.toString());
        //   textFlow.getChildre(n().add(text);
    }

    public Pane dessin(String ville){
        // création de notre Pane principale
        Pane pane = new Pane();
        // Initialisation de la projection Mercator centrée sur la ville d'Avignon
        ParseXml parseXml = new ParseXml();
        final Circle[] selectedCircle = {null}; // le cercle sélectionné
        // Création du canvas et du contexte graphique
        Converter converter = new Converter(LON0, LAT0, SCALE);

        /************************ le block pour déssiner les water ****************/

        if(HelloController.eau == true){
            List<WaterData> W = new ArrayList<WaterData>();

            W = parseXml.parserWater(ville);
            // Création du canvas et du contexte graphique


            DessinWater dessinWater = new DessinWater();
            dessinWater.dessinerWater(parseXml,pane, (ArrayList<WaterData>) W,ville,selectedCircle,converter);
        }
        if(HelloController.park == true){
            /************************ le block pour déssiner les park ****************/

            List<ParkData> LL = new ArrayList<ParkData>();

            LL = parseXml.parserPark(ville);
            // Création du canvas et du contexte graphique


            DessinPark dessinPark = new DessinPark();
            dessinPark.dessinerPark(parseXml,pane, (ArrayList<ParkData>) LL,ville,selectedCircle,converter);

        }
        if(HelloController.foret == true){
            /************************ le block pour déssiner les foret  ****************/

            List<ForetData> F = new ArrayList<ForetData>();

            F = parseXml.parserforet(ville);
            // Création du canvas et du contexte graphique


            DessinForet dessinForet = new DessinForet();
            dessinForet.dessinerForet(parseXml,pane, (ArrayList<ForetData>) F,ville,selectedCircle,converter);

        }
        if(HelloController.route == true){
            /************************ le block pour déssiner les Road ****************/

            List<RoadsData> R = new ArrayList<RoadsData>();

            R = parseXml.parserRoads(ville);
            // Création du canvas et du contexte graphique


            DessinRoad road = new DessinRoad();
            road.dessinerRoad(parseXml,pane, (ArrayList<RoadsData>) R,ville,selectedCircle,converter);

        }
        if(HelloController.batiment == true){
            /************************ le block pour déssiner les Batiment ****************/

            List<BatimentData> B = new ArrayList<BatimentData>();

            B = parseXml.parserBatiment(ville);
            // Création du canvas et du contexte graphique


            DessinBatiment batiment = new DessinBatiment();
            batiment.dessinerBatiment(parseXml,pane, (ArrayList<BatimentData>) B,ville,selectedCircle,converter);

        }
        if (HelloController.point == true){
            /************************ le block pour déssiner les Point ****************/

            List<PointDintereData> P = new ArrayList<PointDintereData>();

            P = parseXml.parserPoint(ville);
            // Création du canvas et du contexte graphique


            DessinPointD dessinPointD = new DessinPointD();
            dessinPointD.dessinerPoint(parseXml,pane, (ArrayList<PointDintereData>) P,ville,selectedCircle,converter);

        }














        /************************ le block pour déssiner l'iténiraire ****************/






        return pane;
    }

    public Pane iténiraire(String depart,String arrivee){
        Pane pane = new Pane();
        Dessiniténairare dessiniténairare = new Dessiniténairare();
        Graphe G = new Graphe(43.9492493, 4.8059012);

        Noeud source = G.rueToNode(depart);
        Noeud destination = G.rueToNode(arrivee);//"cours jean jaur�s");
        List<Noeud> noeudList = G.dijkstra(source, destination,TypeTransportController.Typetrans,true,0);
        G.printPath(noeudList);
        Converter converter = new Converter(LON0, LAT0, SCALE);
        dessiniténairare.dessiniteniraire(noeudList,converter,pane);
        return pane;
    }

}
