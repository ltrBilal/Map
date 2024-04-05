package fr.maps;

import java.util.ArrayList;
import java.util.List;

public class Member {

    public String type;
    public long ref;
    public String role;

    public List lat = new ArrayList<Double>();
    public   List lon = new ArrayList<Double>();

    public Member(String type,long ref,String role){
        this.setType(type);
        this.setRef(ref);
        this.setRole(role);
    }

    public void setLat(double lat){
        this.lat.add(lat);
    }

    public void setLon(double lon){
        this.lon.add(lon);
    }

    public List getLat(){
        return  this.lat;
    }
    public List getLon(){
        return  this.lon;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getRef() {
        return ref;
    }

    public void setRef(long ref) {
        this.ref = ref;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
