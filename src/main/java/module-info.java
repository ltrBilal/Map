module fr.maps {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.xml;
    requires java.desktop;
    requires org.jdom2;


    opens fr.maps to javafx.fxml;
    exports fr.maps;
}