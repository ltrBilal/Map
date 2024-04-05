package fr.maps;

import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Polyline;

import java.util.ArrayList;
import java.util.List;

public class DessinRoad {
    public void showLegend2( List<String> description) {
        //legendLabel = new Label("Cliquez sur un élément de la carte pour afficher sa légende");
        int i =0;
        DessinAll.legendLabel.setText(description.get(i));
        i++;
        while(i< description.size()){
            DessinAll.legendLabel.setText(DessinAll.legendLabel.getText() + "\n"+description.get(i));
            i++;
        }
        //  Text text = new Text(description.toString());
        //   textFlow.getChildren().add(text);
    }

    public void dessinerRoad(ParseXml parser,Pane pane, ArrayList<RoadsData> P, String viile ,Circle[] selectedCircle,Converter converter){
        P = parser.parserRoads(viile);

        for (int i = 0; i < P.size(); i++) {

            RoadsData roadsData = (RoadsData) P.get(i);
            List <Double>listlat = roadsData.getLat();
            List <Double>listlon = roadsData.getLon();


            double coords[] = new double[2 * listlat.size()];

            for (int k = 0; k < listlon.size(); k++) {
                Point2D point = converter.project(listlon.get(k), listlat.get(k));
                coords[2 * k] = point.getX();
                coords[2 * k + 1] = point.getY();
            }
            Polyline polyline = new Polyline(coords);
            // Configurer les propriétés visuelles du Polygon
            //polyline.setFill(Color.DARKGRAY);
            polyline.setStroke(Color.BLACK);
            polyline.setStrokeWidth(2.0);
            // Ajouter un événement pour gérer le clic sur le batiment
            polyline.setOnMouseClicked(event -> {
                // Supprimer le cercle sélectionné précédemment, s'il existe
                if (selectedCircle[0] != null) {
                    pane.getChildren().remove(selectedCircle[0]);
                }

                // Créer un nouveau cercle et l'ajouter au canvas
                Point2D lastPoint = converter.project(listlon.get(listlon.size() - 1), listlat.get(listlat.size() - 1));
                selectedCircle[0] = new Circle(lastPoint.getX(), lastPoint.getY(), 10, Color.RED);
                pane.getChildren().add(selectedCircle[0]);

                // Afficher la légende
                Double name = roadsData.getIdWay();
                Double description = roadsData.getIdWay();
                showLegend2(roadsData.getinfo());
            });
            pane.getChildren().add(polyline);

        }
    }
}



