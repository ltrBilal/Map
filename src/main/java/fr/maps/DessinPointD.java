package fr.maps;

import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;

import java.util.ArrayList;
import java.util.List;

public class DessinPointD {
    public void showLegend2( List<String> description) {
        //legendLabel = new Label("Cliquez sur un élément de la carte pour afficher sa légende");
        int i =0;
        if(description.get(i)==null){
            DessinAll.legendLabel.setText("batimet");
        }else {
            DessinAll.legendLabel.setText(description.get(i));
            i++;
            while (i < description.size()) {
                DessinAll.legendLabel.setText(DessinAll.legendLabel.getText() + "\n" + description.get(i));
                i++;
            }
        }
        //  Text text = new Text(description.toString());
        //   textFlow.getChildren().add(text);
    }

    public void dessinerPoint(ParseXml parser,Pane pane, ArrayList<PointDintereData> P, String viile ,Circle[] selectedCircle,Converter converter){
        P = parser.parserPoint(viile);

        for (int i = 0; i < P.size(); i++) {

            PointDintereData pointDintereData = (PointDintereData) P.get(i);
            List <Double>listlat = pointDintereData.getLat();
            List <Double>listlon = pointDintereData.getLon();


            double coords[] = new double[2 * listlat.size()];

            for (int k = 0; k < listlon.size(); k++) {
                Point2D point = converter.project(listlon.get(k), listlat.get(k));
                coords[2 * k] = point.getX();
                coords[2 * k + 1] = point.getY();
            }
            Polygon polygon = new Polygon(coords);
            // Configurer les propriétés visuelles du Polygon
            polygon.setFill(Color.LIGHTYELLOW);
            polygon.setStrokeWidth(1.0);
            // Ajouter un événement pour gérer le clic sur le batiment
            polygon.setOnMouseClicked(event -> {
                // Supprimer le cercle sélectionné précédemment, s'il existe
                if (selectedCircle[0] != null) {
                    pane.getChildren().remove(selectedCircle[0]);
                }

                // Créer un nouveau cercle et l'ajouter au canvas
                Point2D lastPoint = converter.project(listlon.get(listlon.size() - 1), listlat.get(listlat.size() - 1));
                selectedCircle[0] = new Circle(lastPoint.getX(), lastPoint.getY(), 10, Color.RED);
                pane.getChildren().add(selectedCircle[0]);

                // Afficher la légende
                Double name = pointDintereData.getIdWay();
                Double description = pointDintereData.getIdWay();
                showLegend2(pointDintereData.getinfo());
            });
            pane.getChildren().add(polygon);

        }
    }
}




