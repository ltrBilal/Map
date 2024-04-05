package fr.maps;

public enum TransportType {
    CAR(1),
    BUS(2),
    BICYCLE(3),
    PEDESTRIAN(4);


    private final int averageSpeed;

    private TransportType(int speed) {
        this.averageSpeed = speed;
    }


    public int getAverageSpeed() {
        return this.averageSpeed;
    }


}
