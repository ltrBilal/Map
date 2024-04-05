package fr.maps;

import javafx.scene.control.Label;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

public class Main {



    public  String Coordonnee;

    public static Label label;

    public void terminal ( String str) {
            label.setText(label.getText() + "\n"+str);
        }


    public String getCoordonnee(String ville){
        ville = ville.substring(0, 1).toUpperCase() + ville.substring(1);

        //récupération les cordoonnée de la ville saisie
        String[] Cord = new String[2];
        BufferedReader reader = null;
        try {
            String dir;
            dir = repertoireCourant();
            reader = new BufferedReader(new FileReader(dir+"/région de la france/villes.csv"));
            // Créer une boucle qui lit chaque ligne du fichier CSV
            String line;
            while ((line = reader.readLine()) != null) {
                // Vérifiez si la ligne egale la chaîne de recherche
                if (line.split("\\t")[0].equals(ville)) {
                    // Si oui, imprimez la ligne et terminez la boucle
                    //System.out.println(line);
                    Cord[0] = line.split("\\t")[1];
                    Cord[1] = line.split("\\t")[2];
                    break;
                }
            }
        } catch (IOException e) {
            //e.printStackTrace();
            System.out.println("la ville n existe pas ");
            terminal("la ville n existe pas ");
        } finally {
            // Fermez l'objet BufferedReader
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if(Cord[0]==null){
            return " ";
        }
        String temp = String.valueOf(Double.parseDouble(Cord[0]) - 0.03);
        String temp2 = String.valueOf(Double.parseDouble(Cord[1]) - 0.03);
        // System.out.println(temp);
        //System.out.println(temp2);

        this.Coordonnee = temp + "," + temp2 + "," + Cord[0] + "," + Cord[1] ;
        return  this.Coordonnee;
    }

    String repertoireCourant(){
        return System.getProperty("user.dir");
    }

    public void regionFrance() {
        // une requête Overpass QL qui réqupére toutes les régions de la France
        String query = "[out:csv(\"name\", ::lat, ::lon; false)];"
                + "area[name=\"France\"]->.country;"
                + "node[\"population\"](area.country);"
                + "out;";
        // pour récupérer le chemin
        String dir;
        dir = repertoireCourant();

        try {
            // Envoyez la requête à l'URL d'Overpass API
            URL url = new URL("http://overpass-api.de/api/interpreter");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            // Envoyez la requête dans le corps de la requête
            conn.getOutputStream().write(query.getBytes());

            // Récupérez la réponse
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            StringBuilder response = new StringBuilder();
            while ((line = rd.readLine()) != null) {
                //System.out.println(line);
                response.append(line);
                response.append("\n");
            }
            rd.close();

            FileWriter writer = new FileWriter(dir+"/région de la france/villes.csv");
            writer.write(response.toString());
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void waterXml(String Nom_Ville, String Coordonnee_ville) throws IOException {
        //pour gérer l erreur de majuscule
        Nom_Ville = Nom_Ville.substring(0, 1).toUpperCase() + Nom_Ville.substring(1);
        System.out.println("début de téléchargement les données water");
        terminal("début de téléchargement les données water");
        String queryWater = "[out:xml];"
                + "("
                + "  way[\"natural\"~\"^water|bay|strait|coastline$\"](" + Coordonnee_ville + ");"
                + "  way[\"landuse\"~\"^basin|reservoir$\"](" + Coordonnee_ville + ");"
                + "  way[\"leisure\"~\"^swimming_pool$\"](" + Coordonnee_ville + ");"
                + "  way[\"waterway\"](" + Coordonnee_ville + ");"
                + "  relation[\"natural\"~\"^water|bay|strait|coastline$\"](" + Coordonnee_ville + ");"
                + "  relation[\"landuse\"~\"^basin|reservoir$\"](" + Coordonnee_ville + ");"
                + "  relation[\"leisure\"~\"^swimming_pool$\"](" + Coordonnee_ville + ");"
                + "  relation[\"waterway\"](" + Coordonnee_ville + ");"
                + ");"
                + "out geom qt;";
        // pour récupérer le chemin
        String dir;
        dir = repertoireCourant();

        if(Coordonnee_ville == " "){
            System.out.println("erreur la ville existe pas ");
            terminal("erreur la ville existe pas !!!");
        }else {


            String encodedQuery = URLEncoder.encode(queryWater, "UTF-8");
            do {
                try {
                    // Envoyer la requête HTTP à l'API Overpass
                    URL url = new URL("https://overpass-api.de/api/interpreter?data=" + encodedQuery);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");

                    // Traiter les données de réponse
                    BufferedReader readerr = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder ParkXml = new StringBuilder();
                    String line;
                    while ((line = readerr.readLine()) != null) {
                        // Traiter chaque ligne de la réponse ici
                        // System.out.println(line);
                        ParkXml.append(line);
                        ParkXml.append("\n");
                    }
                    readerr.close();
                    FileWriter writer = new FileWriter(dir + "/région de la france/" + Nom_Ville + "/WaterXml.xml");
                    writer.write(ParkXml.toString());
                    writer.close();
                } catch (IOException ex) {
                    System.out.println("erreur serveur ... réessayer encore.");
                    terminal("erreur serveur ... réessayer encore.");
                }
            } while (!new File(dir + "/région de la france/" + Nom_Ville + "/WaterXml.xml").exists());
            System.out.println("fin de téléchargement avec success");
            terminal("fin du téléchargement avec success");
        }
    }
    public void roadxml(String Nom_Ville, String Coordonnee_ville) throws IOException {
        //pour gérer l erreur de majuscule
        Nom_Ville = Nom_Ville.substring(0, 1).toUpperCase() + Nom_Ville.substring(1);
        System.out.println("début de téléchargement les données roads");
        terminal("début de téléchargement les données roads");
        String queryRoad = "[out:xml];"
                + "("
                + "  way[\"highway\"](" + Coordonnee_ville + ");"
                + "  relation[\"highway\"](" + Coordonnee_ville + ");"
                + ");"
                + "out geom qt;";


        // pour récupérer le chemin
        String dir;
        dir = repertoireCourant();

        if(Coordonnee_ville == " "){
            System.out.println("erreur la ville existe pas ");
        }else {

            String encodedQuery = URLEncoder.encode(queryRoad, "UTF-8");

            do {
                try { // Envoyer la requête HTTP à l'API Overpass
                    URL url = new URL("https://overpass-api.de/api/interpreter?data=" + encodedQuery);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");

                    // Traiter les données de réponse
                    BufferedReader readerr = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder ParkXml = new StringBuilder();
                    String line;
                    while ((line = readerr.readLine()) != null) {
                        // Traiter chaque ligne de la réponse ici
                        // System.out.println(line);
                        ParkXml.append(line);
                        ParkXml.append("\n");
                    }
                    readerr.close();
                    FileWriter writer = new FileWriter(dir + "/région de la france/" + Nom_Ville + "/RoadXml.xml");
                    writer.write(ParkXml.toString());
                    writer.close();
                } catch (IOException ex) {
                    System.out.println("erreur serveur ... réessayer encore.");
                    terminal("erreur serveur ... réessayer encore.");
                }
            } while (!new File(dir + "/région de la france/" + Nom_Ville + "/RoadXml.xml").exists());
            System.out.println("fin de téléchargement avec success");
            terminal("fin du téléchargement avec success");
        }

    }


    public void parkXml(String Nom_Ville, String Coordonnee_ville) throws IOException {
        //pour gérer l erreur de majuscule
        Nom_Ville = Nom_Ville.substring(0, 1).toUpperCase() + Nom_Ville.substring(1);
        System.out.println("début de téléchargement les données park");
        terminal("début de téléchargement les données park");
        // Rédiger votre requête Overpass API ici en remplaçant les coordonnées de la ville
        //  String queryPark = "[out:xml]; (way[\"leisure\"=\"garden\"](43.9292493, 4.7859012, 43.9492493, 4.8059012); relation[\"leisure\"=\"garden\"](43.9292493, 4.7859012, 43.9492493, 4.8059012);); out body; >; out geom qt;";
        String queryPark = "[out:xml][timeout:25];"
                + "("
                + "way[\"leisure\"=\"garden\"](" + Coordonnee_ville + ");"
                + "way[\"leisure\"=\"park\"](" + Coordonnee_ville + ");"
                + "way[\"leisure\"=\"golf_course\"](" + Coordonnee_ville + ");"
                + "way[\"leisure\"=\"playground\"](" + Coordonnee_ville + ");"
                + "relation[\"leisure\"=\"garden\"](" + Coordonnee_ville + ");"
                + "relation[\"leisure\"=\"park\"](" + Coordonnee_ville + ");"
                + "relation[\"leisure\"=\"golf_course\"](" + Coordonnee_ville + ");"
                + "relation[\"leisure\"=\"playground\"](" + Coordonnee_ville + ");"
                + ");"
                + "out geom qt;";


        // pour récupérer le chemin
        String dir;
        dir = repertoireCourant();

        if(Coordonnee_ville == " "){
            System.out.println("erreur la ville existe pas ");
            terminal("erreur la ville existe pas ");
        }else {
            String encodedQuery = URLEncoder.encode(queryPark, "UTF-8");

            do {
                try { // Envoyer la requête HTTP à l'API Overpass
                    URL url = new URL("https://overpass-api.de/api/interpreter?data=" + encodedQuery);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");

                    // Traiter les données de réponse
                    BufferedReader readerr = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder ParkXml = new StringBuilder();
                    String line;
                    while ((line = readerr.readLine()) != null) {
                        // Traiter chaque ligne de la réponse ici
                        // System.out.println(line);
                        ParkXml.append(line);
                        ParkXml.append("\n");
                    }
                    readerr.close();
                    FileWriter writer = new FileWriter(dir + "/région de la france/" + Nom_Ville + "/ParkXml.xml");
                    writer.write(ParkXml.toString());
                    writer.close();
                } catch (IOException ex) {

                    System.out.println("erreur serveur ... réessayer encore.");
                    terminal("erreur serveur ... réessayer encore.");
                }
            } while (!new File(dir + "/région de la france/" + Nom_Ville + "/ParkXml.xml").exists());
            System.out.println("fin de téléchargement avec success");
            terminal("fin de téléchargement avec success");
        }
    }
    public void batimentXml(String Nom_Ville, String Coordonnee_ville) throws UnsupportedEncodingException {
        //pour gérer l erreur de majuscule
        Nom_Ville = Nom_Ville.substring(0, 1).toUpperCase() + Nom_Ville.substring(1);
        System.out.println("début de téléchargement les données batiments ");
        terminal("début de téléchargement les données batiments");
        String queryBatiment = "[out:xml][timeout:50];"
                + "("
                + "  way[\"building\"](" + Coordonnee_ville + ");"
                + "  relation[\"building\"](" + Coordonnee_ville + ");"
                + ");"
                + "out geom qt;";

        // pour récupérer le chemin
        String dir;
        dir = repertoireCourant();

        if(Coordonnee_ville == " "){
            System.out.println("erreur la ville existe pas ");
            terminal("erreur la ville existe pas ");
        }else {
            String encodedQuery = URLEncoder.encode(queryBatiment, "UTF-8");

            do {
                try { // Envoyer la requête HTTP à l'API Overpass
                    URL url = new URL("https://overpass-api.de/api/interpreter?data=" + encodedQuery);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");

                    // Traiter les données de réponse
                    BufferedReader readerr = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder ParkXml = new StringBuilder();
                    String line;
                    while ((line = readerr.readLine()) != null) {
                        // Traiter chaque ligne de la réponse ici
                        // System.out.println(line);
                        ParkXml.append(line);
                        ParkXml.append("\n");
                    }
                    readerr.close();
                    FileWriter writer = new FileWriter(dir + "/région de la france/" + Nom_Ville + "/batimentXml.xml");
                    writer.write(ParkXml.toString());
                    writer.close();
                } catch (IOException ex) {

                    System.out.println("erreur serveur ... réessayer encore.");
                    terminal("erreur serveur ... réessayer encore.");
                }
            } while (!new File(dir + "/région de la france/" + Nom_Ville + "/batimentXml.xml").exists());
            System.out.println("fin de téléchargement avec success");
            terminal("fin de téléchargement avec success");
        }
    }

    public void foretXml(String Nom_Ville, String Coordonnee_ville) throws IOException {
        //pour gérer l erreur de majuscule
        Nom_Ville = Nom_Ville.substring(0, 1).toUpperCase() + Nom_Ville.substring(1);
        System.out.println("début de téléchargement les données foret");
        terminal("début de téléchargement les données foret");
        // Rédiger votre requête Overpass API ici en remplaçant les coordonnées de la ville
        //  String queryPark = "[out:xml]; (way[\"leisure\"=\"garden\"](43.9292493, 4.7859012, 43.9492493, 4.8059012); relation[\"leisure\"=\"garden\"](43.9292493, 4.7859012, 43.9492493, 4.8059012);); out body; >; out geom qt;";
        String queryPark = "[out:xml][timeout:25];"
                + "("
                + "way[\"natural\"=\"wood\"](" + Coordonnee_ville + ");"
                + "relation[\"natural\"=\"wood\"](" + Coordonnee_ville + ");"
                + ");"
                + "out geom qt;";


        // pour récupérer le chemin
        String dir;
        dir = repertoireCourant();
        if(Coordonnee_ville == " "){
            System.out.println("erreur la ville existe pas ");
            terminal("erreur la ville existe pas ");
        }else {

            String encodedQuery = URLEncoder.encode(queryPark, "UTF-8");

            do {
                try { // Envoyer la requête HTTP à l'API Overpass
                    URL url = new URL("https://overpass-api.de/api/interpreter?data=" + encodedQuery);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");

                    // Traiter les données de réponse
                    BufferedReader readerr = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder ParkXml = new StringBuilder();
                    String line;
                    while ((line = readerr.readLine()) != null) {
                        // Traiter chaque ligne de la réponse ici
                        // System.out.println(line);
                        ParkXml.append(line);
                        ParkXml.append("\n");
                    }
                    readerr.close();
                    FileWriter writer = new FileWriter(dir + "/région de la france/" + Nom_Ville + "/woodXml.xml");
                    writer.write(ParkXml.toString());
                    writer.close();
                } catch (IOException ex) {

                    System.out.println("erreur serveur ... réessayer encore.");
                    terminal("erreur serveur ... réessayer encore.");
                }
            } while (!new File(dir + "/région de la france/" + Nom_Ville + "/woodXml.xml").exists());
            System.out.println("fin de téléchargement avec success");
            terminal("fin de téléchargement avec success");
        }
    }

    public void transportXml(String Nom_Ville, String Coordonnee_ville) throws IOException {
        //pour gérer l erreur de majuscule
        Nom_Ville = Nom_Ville.substring(0, 1).toUpperCase() + Nom_Ville.substring(1);
        System.out.println("début de téléchargement les données transport");
        terminal("début de téléchargement les données transport");
        // Rédiger votre requête Overpass API ici en remplaçant les coordonnées de la ville
        //  String queryPark = "[out:xml]; (way[\"leisure\"=\"garden\"](43.9292493, 4.7859012, 43.9492493, 4.8059012); relation[\"leisure\"=\"garden\"](43.9292493, 4.7859012, 43.9492493, 4.8059012);); out body; >; out geom qt;";
        String queryPark = "[out:xml][timeout:25];\n" +
                "// Récupère les nœuds des stations de métro\n" +
                "node[\"railway\"=\"station\"][\"station\"=\"subway\"]("+Coordonnee_ville+");\n" +
                "// Récupère les nœuds des arrêts de bus\n" +
                "node[\"highway\"=\"bus_stop\"]("+Coordonnee_ville+");\n" +
                "// Récupère les relations des lignes de tramway\n" +
                "relation[\"route\"=\"tram\"]("+Coordonnee_ville+");\n" +
                "// Récupère les membres des relations pour obtenir les nœuds des arrêts de tramway\n" +
                "out geom qt;\n";


        // pour récupérer le chemin
        String dir;
        dir = repertoireCourant();

        if(Coordonnee_ville == " "){
            System.out.println("erreur la ville existe pas ");
            terminal("erreur la ville existe pas");
        }else {
            String encodedQuery = URLEncoder.encode(queryPark, "UTF-8");

            do {
                try { // Envoyer la requête HTTP à l'API Overpass
                    URL url = new URL("https://overpass-api.de/api/interpreter?data=" + encodedQuery);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");

                    // Traiter les données de réponse
                    BufferedReader readerr = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder ParkXml = new StringBuilder();
                    String line;
                    while ((line = readerr.readLine()) != null) {
                        // Traiter chaque ligne de la réponse ici
                        // System.out.println(line);
                        ParkXml.append(line);
                        ParkXml.append("\n");
                    }
                    readerr.close();
                    FileWriter writer = new FileWriter(dir + "/région de la france/" + Nom_Ville + "/transportXml.xml");
                    writer.write(ParkXml.toString());
                    writer.close();
                } catch (IOException ex) {

                    System.out.println("erreur serveur ... réessayer encore.");
                    terminal("erreur serveur ... réessayer encore.");
                }
            } while (!new File(dir + "/région de la france/" + Nom_Ville + "/transportXml.xml").exists());
            System.out.println("fin de téléchargement avec success");
            terminal("fin de téléchargement avec success");
        }
    }



    public void PointInteret(String Nom_Ville, String Coordonnee_ville) throws IOException {
        //pour gérer l erreur de majuscule
        Nom_Ville = Nom_Ville.substring(0, 1).toUpperCase() + Nom_Ville.substring(1);
        System.out.println("début de téléchargement les données point d 'interet");
        terminal("début de téléchargement les données point d 'interet");
        // Rédiger votre requête Overpass API ici en remplaçant les coordonnées de la ville
        //  String queryPark = "[out:xml]; (way[\"leisure\"=\"garden\"](43.9292493, 4.7859012, 43.9492493, 4.8059012); relation[\"leisure\"=\"garden\"](43.9292493, 4.7859012, 43.9492493, 4.8059012);); out body; >; out geom qt;";
        String queryPark = "[out:xml][timeout:25];\n" +
                "(\n" +
                "  way[\"building\"][\"addr:street\"][\"addr:housenumber\"]("+Coordonnee_ville+");" +
                "  relation[\"building\"][\"addr:street\"][\"addr:housenumber\"]("+Coordonnee_ville+");" +
                ");\n" +
                "out geom qt;";


        // pour récupérer le chemin
        String dir;
        dir = repertoireCourant();
        if(Coordonnee_ville == " "){
            System.out.println("erreur la ville existe pas ");
            terminal("erreur la ville existe pas");
        }else {

            String encodedQuery = URLEncoder.encode(queryPark, "UTF-8");

            do {
                try { // Envoyer la requête HTTP à l'API Overpass
                    URL url = new URL("https://overpass-api.de/api/interpreter?data=" + encodedQuery);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");

                    // Traiter les données de réponse
                    BufferedReader readerr = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder ParkXml = new StringBuilder();
                    String line;
                    while ((line = readerr.readLine()) != null) {
                        // Traiter chaque ligne de la réponse ici
                        // System.out.println(line);
                        ParkXml.append(line);
                        ParkXml.append("\n");
                    }
                    readerr.close();
                    FileWriter writer = new FileWriter(dir + "/région de la france/" + Nom_Ville + "/PointInteretXml.xml");
                    writer.write(ParkXml.toString());
                    writer.close();
                } catch (IOException ex) {

                    System.out.println("erreur serveur ... réessayer encore.");
                    terminal("erreur serveur ... réessayer encore.");
                }
            } while (!new File(dir + "/région de la france/" + Nom_Ville + "/PointInteretXml.xml").exists());
            System.out.println("fin de téléchargement avec success");
            terminal("fin de téléchargement avec success");
        }
    }
    //----------------------------------------Trains------------------------------------------//
    public void PointTrain(String Nom_Ville, String Coordonnee_ville) throws IOException {
        Nom_Ville = Nom_Ville.substring(0, 1).toUpperCase() + Nom_Ville.substring(1);
        System.out.println("début de téléchargement les données Train");
        terminal("début de téléchargement les données Train");
        String queryTrain = "[out:xml];\n" +
                "area[name=\"Avignon\"]->.searchArea;\n" +
                "way(area.searchArea)[railway];\n" +
                "out center;";
        String dir;
        dir = repertoireCourant();
        if(Coordonnee_ville == " "){
            System.out.println("erreur la ville existe pas ");
            terminal("erreur la ville existe pas");
        }
        else {

            String encodedQuery = URLEncoder.encode(queryTrain, "UTF-8");

            do {
                try { // Envoyer la requête HTTP à l'API Overpass
                    URL url = new URL("https://overpass-api.de/api/interpreter?data=" + encodedQuery);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");

                    // Traiter les données de réponse
                    BufferedReader readerr = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder ParkXml = new StringBuilder();
                    String line;
                    while ((line = readerr.readLine()) != null) {
                        // Traiter chaque ligne de la réponse ici
                        // System.out.println(line);
                        ParkXml.append(line);
                        ParkXml.append("\n");
                    }
                    readerr.close();
                    FileWriter writer = new FileWriter(dir + "/région de la france/" + Nom_Ville + "/PointTrainXml.xml");
                    writer.write(ParkXml.toString());
                    writer.close();
                } catch (IOException ex) {

                    System.out.println("erreur serveur ... réessayer encore.");
                    terminal("erreur serveur ... réessayer encore.");
                }
            } while (!new File(dir + "/région de la france/" + Nom_Ville + "/PointTrainXml.xml").exists());
            System.out.println("fin de téléchargement avec success");
            terminal("fin de téléchargement avec success");
        }
    }

    //--------------------------------------------------------------------------------------------//
    public void init(String ville) throws IOException {
        label = new Label("terminal");
        String dir = this.repertoireCourant();
        File verif = new File(dir+"/région de la france");
        if (!verif.exists()) {
            verif.mkdir();
        }

        System.out.println("récupération des régions en cours ...");
        terminal("récupération des régions en cours ...");
        File verife = new File(dir+"/région de la france/villes.csv/");
        if (!verife.exists()) {
            this.regionFrance();
        }
        System.out.println(("données recuperéées avec succes"));
        terminal("données recuperéées avec succes");

        File directory = new File(dir+"/région de la france/" + ville);
        if (!directory.exists()) {
            directory.mkdir();
        }
        //obtenire toute les régions de la france
        this.regionFrance();
        //générie les fichiers xml

        if(this.getCoordonnee(ville) == " "){
            System.out.println("erreur la ville existe pas ");
            terminal("erreur la ville existe pas");
        }else {
            //this.transportXml(ville, this.getCoordonnee(ville));
            //this.PointTrain(ville,this.getCoordonnee(ville));

            if (HelloController.eau == true){
                this.waterXml(ville, this.getCoordonnee(ville));
            }


            if (HelloController.park == true){
                this.parkXml(ville, this.getCoordonnee(ville));
            }


            if (HelloController.foret==true) {
                this.foretXml(ville, this.getCoordonnee(ville));
            }

            if (HelloController.route == true) {
                this.roadxml(ville, this.getCoordonnee(ville));
            }


            if (HelloController.batiment == true) {
                this.batimentXml(ville, this.getCoordonnee(ville));
            }


            if (HelloController.point == true) {
                this.PointInteret(ville, this.getCoordonnee(ville));
            }
        }

    }
    public static  void main (String[] args){
        Main main = new Main();
        System.out.println(main.getCoordonnee("Avignon"));
    }
}