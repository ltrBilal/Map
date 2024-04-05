package fr.maps;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

import java.awt.event.ActionEvent;
import java.io.IOException;

public class TypeTransportController {
    static TransportType Typetrans;
    @FXML
    ToggleGroup group;

    @FXML
    Button chercher;

    public HelloController helloController = new HelloController();




@FXML
public void Chercher(javafx.event.ActionEvent event) throws IOException {
        RadioButton radioButton = (RadioButton) group.getSelectedToggle();
        if(radioButton.isSelected()){
            switch (radioButton.getText()) {
                case "BUS":
                    Typetrans = TransportType.BUS;
                    break;
                case "CAR":
                    Typetrans = TransportType.CAR;
                    break;
                case "BICYCLE":
                    Typetrans = TransportType.BICYCLE;
                    break;
                case"PEDESTRIAN":
                    Typetrans = TransportType.PEDESTRIAN;
                    break;
                // Autres cas possibles...
                default:
                    Typetrans = TransportType.BUS;
            }

            System.out.println(Typetrans);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
            Parent root = loader.load();
            HelloController deuxiemeFenetreController = loader.getController();
            deuxiemeFenetreController.disableMonBouton();

            // Fermer la fenêtre actuelle
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();

        }
    }
}
