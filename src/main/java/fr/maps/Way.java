package fr.maps;
import java.util.ArrayList;
import java.util.List;

public class Way {

    private List<Noeud> nList;
    private long id;
    private String type;
    private String nom;
    private String value;

    public Way(long id) {
        this.nList = new ArrayList<Noeud>();
        this.id = id;
        this.type = null;
        this.nom = null;
        this.value=null;
    }
    public void setValue(String value) {
        this.value = value;
    }
    public void setType(String nom) {
        this.type = nom;
    }
    public void setNom(String name) {
        this.nom = name;
    }
    public String getNom() {
        return this.nom;
    }
    public String getValue() {
        return this.value;
    }
    public String getType() {
        return this.type;
    }
    public long getID() {
        return this.id;
    }
    public List<Noeud> getlist(){
        return this.nList;
    }
    public void addNode(Noeud n) {
        this.nList.add(n);
    }
}
