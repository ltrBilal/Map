package fr.maps;

import java.util.ArrayList;
import java.util.List;

public class WaterData {
    public double IdWay;
    public double minLat;
    public double maxLat;
    public double minLon;
    public double maxLon;


    public   List lat = new ArrayList<Double>();
    public   List lon = new ArrayList<Double>();

    public List info = new ArrayList<String>();






    public void setLat(double lat){
        this.lat.add(lat);
    }

    public void setLon(double lon){
        this.lon.add(lon);
    }

    public void setInfo(String info ){
        this.info.add(info);
    }



    public void setIdWay(double idWay) {
        this.IdWay = idWay;
    }

    public void setMinLat(double minLan){
        this.minLat=minLan;
    }

    public void setMaxLat(double maxLan){
        this.maxLat=maxLan;
    }

    public void setMinLon(double minLon){
        this.minLon=minLon;
    }

    public void setMaxLon(double maxLon){
        this.maxLon=maxLon;
    }

    public double getIdWay(){
        return this.IdWay;
    }

    public double getMinLat(){
        return this.minLat;
    }
    public double getMinLon(){
        return this.minLon;
    }
    public double getMaxLat(){
        return this.maxLat;
    }
    public double getMaxLon(){
        return this.maxLon;
    }

    public List getLat(){
        return  this.lat;
    }
    public List getLon(){
        return  this.lon;
    }

    public List getinfo(){
        return  this.info;
    }



}