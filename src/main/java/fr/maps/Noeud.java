package fr.maps;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Noeud {


        private long id;
        private double lon;
        private double lat;
        private String nom;
        private List<Noeud> adj = new ArrayList<Noeud>();

        private boolean bus;
        private boolean busStop;
        private boolean pedestrian;
        private boolean bicycle;
        private boolean motorizedVehicle;

        private double maxMotorizedVehicleSpeed = 0.0;

        private double distance = Double.MAX_VALUE;             //Distance de la source  ce noeud (Calcul par l'algorithme de Dijkstra)
        private double time = Double.MAX_VALUE;
        private List<Noeud> shortestPath = new LinkedList<>();     //Chemin parcouru de la source jusqu' ce noeud (quivalent d'un tableau des prdecesseurs) (List rempli par l'algorithme de Dijkstra)


        public Noeud(long id, double lat, double lon, String nom, boolean bus, boolean pedestrian, boolean bicycle){
            this.id = id;
            this.lat = lat;
            this.lon = lon;
            this.setNom(nom);

            this.setBus(bus);
            this.setPedestrian(pedestrian);
            this.setBicycle(bicycle);
        }

        public Noeud(long id, double lat, double lon, String nom){
            this.id = id;
            this.lat = lat;
            this.lon = lon;
            this.setNom(nom);
        }

        //Calcul de la distance entre 2 noeuds  partir de leur latitudes et longitudes
        public double calculateDistance(Noeud n2) {
            return 6371000 * Math.acos(Math.sin(Math.toRadians(this.lat)) * Math.sin(Math.toRadians(n2.lat)) + Math.cos(Math.toRadians(this.lat)) * Math.cos(Math.toRadians(n2.lat)) * Math.cos(Math.toRadians(n2.lon - this.lon)));
        }

        //Calcul de la distance entre 2 noeuds  partir de leur latitudes et longitudes
        public double calculateTime(double distance, TransportType transportType) {

            if(transportType == TransportType.CAR || transportType == TransportType.BUS) {
                if(this.maxMotorizedVehicleSpeed > 0.0) {
                    return distance / this.maxMotorizedVehicleSpeed;
                }

            }

            //this.maxMotorizedVehicleSpeed = transportType.getAverageSpeed();
            return distance / transportType.getAverageSpeed();

        }



        //Getters and Setters
        public long getID() {
            return this.id;
        }

        public double getLon() {
            return this.lon;
        }

        public double getLat() {
            return this.lat;
        }


        public String getNom() {
            return nom;
        }

        public void setNom(String nom) {
            this.nom = nom;
        }

        public double getDistance() {
            return distance;
        }

        public void setDistance(double distance) {
            this.distance = distance;
        }

        public LinkedList<Noeud> getShortestPath() {
            return (LinkedList<Noeud>) shortestPath;
        }

        public void setShortestPath(LinkedList<Noeud> shortestPath) {
            this.shortestPath = shortestPath;

        }

        public List<Noeud> getAdj() {
            return adj;
        }

        public double getTime() {
            return time;
        }

        public void setTime(double time) {
            this.time = time;
        }

        public void setTime(double distance, TransportType transportType) {
            double averageSpeed = 0.0;
            if(transportType == TransportType.CAR || transportType == TransportType.BUS) {

                if(this.maxMotorizedVehicleSpeed == 0.0) {
                    averageSpeed += transportType.getAverageSpeed();
                }
                else {
                    averageSpeed += this.maxMotorizedVehicleSpeed;
                }
            }

            this.time = distance / averageSpeed;

        }

        public String convertTimeInTimeFormat() {
            double seconds = this.time;
            double minutes = 0.0;
            double hours = 0.0;

            if(seconds >= 60.0) {
                minutes = seconds / 60.0;
                seconds = seconds - (((int) minutes) * 60.0);
            }

            if(minutes >= 60.0) {
                hours = minutes / 60.0;
                minutes = minutes - (((int) hours) * 60.0);
            }

            String h = String.valueOf(Math.round(hours));
            String m = String.valueOf(Math.round(minutes));
            String s = String.valueOf(Math.round(seconds));

            if(Long.parseLong(h) < 10) { h = "0"+h; }
            if(Long.parseLong(m) < 10) { m = "0"+m; }
            if(Long.parseLong(s) < 10) { s = "0"+s; }

            return h + ":" +  m + ":"+ s;
        }

        public boolean isBus() {
            return bus;
        }
        public void setBus(boolean bus) {
            this.bus = bus;
        }

        public boolean isPedestrian() {
            return pedestrian;
        }

        public void setPedestrian(boolean pedestrian) {
            this.pedestrian = pedestrian;
        }

        public boolean isBicycle() {
            return bicycle;
        }

        public void setBicycle(boolean bicycle) {
            this.bicycle = bicycle;
        }

        public void setMotorizedVehicle(boolean motorizedVehicle) {
            this.motorizedVehicle = motorizedVehicle;
        }

        public boolean isMotorizedVehicle() {
            return motorizedVehicle;
        }

        public double getMaxMotorizedVehicleSpeed() {
            return maxMotorizedVehicleSpeed;
        }

        public void setMaxMotorizedVehicleSpeed(double maxMotorizedVehicleSpeed) {
            this.maxMotorizedVehicleSpeed = maxMotorizedVehicleSpeed;
        }

        public boolean isBusStop() {
            return busStop;
        }

        public void setBusStop(boolean busStop) {
            this.busStop = busStop;
        }


}
