package fr.maps;

import java.util.ArrayList;
import java.util.List;

public class Relation {
    public long id;
    public double minLon;
    public double maxLon;

    public double minLat;
    public double maxLat;
    public List<Member> Listmember;

    public Relation(long id){
        this.id = id;
        this.Listmember = new ArrayList<>();
    }
    public void setId(long id){
        this.id=id;
    }
    public void setmember(List member){
        Listmember = member;
    }

    public List<Member> getmember(){
        return Listmember;
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
}
