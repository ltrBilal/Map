package fr.maps;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class HelloController {

    private double xOffset = 0;
    private double yOffset = 0;

    @FXML
    CheckBox dessin_eau;
    @FXML
    CheckBox dessin_batiment;
    @FXML
    CheckBox dessin_route;
    @FXML
    CheckBox dessin_park;
    @FXML
    CheckBox dessin_point;
    @FXML
    CheckBox dessin_foret;

    static boolean batiment;
    static boolean foret;
    static boolean park;
    static boolean point;
    static boolean route;
    static boolean eau;
    @FXML
    TextField nameField;
    @FXML
    Button Zoom;
    @FXML
    Button Dezoom;
    @FXML
    ScrollPane scrolpanee;

    DessinAll dessinAll = new DessinAll();


    @FXML
    TextFlow textFlow;

    Group group = new Group();

    @FXML
    TextArea textFlow2;

    @FXML
    TextField depart;

    @FXML
    TextField arrivee;

    @FXML
    Button itebtn;
    @FXML
    Button itebtn1 ;

    @FXML
    public void disableMonBouton() {
        itebtn1.setDisable(false);
    }

    private Task<Void> task;



    @FXML
    public void ProposeTypeIte(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(TypeTransportController.class.getResource("TypeTransport-view.fxml"));
        Parent parent = loader.load();
        Stage stage = new Stage();
        Scene scene = new Scene(parent,800, 400);
        stage.setTitle("Type Transport");
        stage.setScene(scene);
        stage.show();



    }

    @FXML
    public void iteniraire (ActionEvent event){
        Graphe G = new Graphe(43.9492493, 4.8059012);

        Noeud source = G.rueToNode(depart.getText());
        Noeud destination = G.rueToNode(arrivee.getText());//"cours jean jaur�s");
        List<Noeud> noeudList = G.dijkstra(source, destination,TypeTransportController.Typetrans,true,0);
        G.printPathterminal(noeudList);
        group.getChildren().add(dessinAll.iténiraire(depart.getText(), arrivee.getText()));
        textFlow2.setText(Graphe.label.getText());

        exporterItineraireXML itineraireXML = new exporterItineraireXML();
        itineraireXML.exporterItineraireXML(noeudList,depart.getText()+"_"+arrivee.getText(),TypeTransportController.Typetrans);
    }

    @FXML
    private void Zoom(ActionEvent event) throws IOException {
        DessinAll.SCALE=DessinAll.SCALE + 2.0 ;
        group.getChildren().clear();
        dessinAll.legendLabel = new Label("Clickez sur un element pour avoir plus d info");
        //pour gérer l erreur de majuscule
        String  Nom_Ville = nameField.getText().substring(0, 1).toUpperCase() + nameField.getText().substring(1);
        group.getChildren().add(dessinAll.dessin(Nom_Ville));
        //group.getChildren().add(dessinAll.legendLabel);

        Text text1 = new Text(DessinAll.legendLabel.toString());
        textFlow.getChildren().add(dessinAll.legendLabel);
    }

    @FXML
    private void Dezoom(ActionEvent event) {
        DessinAll.SCALE=DessinAll.SCALE - 2.0;
        group.getChildren().clear();
        dessinAll.legendLabel = new Label("Clickez sur un element pour avoir plus d info");
        //pour gérer l erreur de majuscule
        String  Nom_Ville = nameField.getText().substring(0, 1).toUpperCase() + nameField.getText().substring(1);
        group.getChildren().add(dessinAll.dessin(Nom_Ville));
        //group.getChildren().add(dessinAll.legendLabel);

        Text text1 = new Text(DessinAll.legendLabel.toString());
        textFlow.getChildren().add(dessinAll.legendLabel);
    }
    public void initialize(){


        // Enlever la barre de défilement
        scrolpanee.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrolpanee.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        // Ajouter la fonctionnalité de défilement avec la souris
        group.setOnMousePressed(event -> {
            if (event.isPrimaryButtonDown()) {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            }
        });

        group.setOnMouseDragged(event -> {
            if (event.isPrimaryButtonDown()) {
                double deltaX = event.getSceneX() - xOffset;
                double deltaY = event.getSceneY() - yOffset;

                double newX = scrolpanee.getHvalue() - deltaX / group.getBoundsInParent().getWidth();
                double newY = scrolpanee.getVvalue() - deltaY / group.getBoundsInParent().getHeight();

                scrolpanee.setHvalue(clamp(newX, 0, 1));
                scrolpanee.setVvalue(clamp(newY, 0, 1));

                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            }
        });
    }
    public void execute() throws IOException {
        if( dessin_eau.isSelected()){
            eau=true;
        }else {
            eau=false;
        }

        if( dessin_batiment.isSelected()){
            batiment=true;
        }else {
            batiment=false;
        }
        if( dessin_route.isSelected()){
            route=true;
        }else {
            route=false;
        }
        if( dessin_park.isSelected()){
            park=true;
        }else {
            park=false;
        }
        if( dessin_point.isSelected()){
            point=true;
        }else {
            point=false;
        }
        if( dessin_foret.isSelected()){
            foret=true;
        }else {
            foret=false;
        }

        Main m = new Main();
        Text text = new Text("           Info !! ");
        textFlow.getChildren().add(text);
        if(m.getCoordonnee(nameField.getText())==" "){
            System.out.println("erreur");
        }

        task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                if(m.getCoordonnee(nameField.getText())!=" "){
                    Main.label = new Label("Terminal");
                    m.init(nameField.getText());

                }
                Platform.runLater(() -> {

                    Text text3 = new Text(" ");
                    textFlow2.setText(String.valueOf(text3));
                    textFlow2.setText(Main.label.getText());

                    final Circle[] selectedCircle = {null}; // le cercle sélectionné

                    dessinAll.legendLabel = new Label("Clickez sur un element pour avoir plus d info");
                    //pour gérer l erreur de majuscule
                    String  Nom_Ville = nameField.getText().substring(0, 1).toUpperCase() + nameField.getText().substring(1);
                    group.getChildren().clear();
                    group.getChildren().add(dessinAll.dessin(Nom_Ville));
                    //group.getChildren().add(dessinAll.legendLabel);

                    Text text1 = new Text(DessinAll.legendLabel.toString());
                    textFlow.getChildren().add(dessinAll.legendLabel);

                    scrolpanee.setContent(group);

                });

                return null;
            }

        };


        new Thread(task).start();


    }

    // Fonction utilitaire pour limiter une valeur entre un minimum et un maximum



    private double clamp(double value, double min, double max) {
        return Math.min(Math.max(value, min), max);

    }
}