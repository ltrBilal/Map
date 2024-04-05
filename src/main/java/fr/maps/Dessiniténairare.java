package fr.maps;

import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polyline;

import java.util.List;

public class Dessiniténairare {

    public void dessiniteniraire(List<Noeud> noeudList, Converter converter, Pane pane){

        double coords[] = new double[2 * noeudList.size()];

        for (int k = 0; k < noeudList.size(); k++) {
            Point2D point = converter.project(noeudList.get(k).getLat(), noeudList.get(k).getLon());
            coords[2 * k] = point.getX();
            coords[2 * k + 1] = point.getY();

            Polyline polyline = new Polyline(coords);
            // Configurer les propriétés visuelles du Polygon
            //polyline.setFill(Color.DARKGRAY);
            polyline.setStroke(Color.BLUE);
            polyline.setStrokeWidth(5.0);
            pane.getChildren().add(polyline);

        }
    }
}
