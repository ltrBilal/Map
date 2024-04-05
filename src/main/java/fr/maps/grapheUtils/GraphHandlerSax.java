package fr.maps.grapheUtils;

import org.xml.sax.helpers.DefaultHandler;

import fr.maps.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class GraphHandlerSax extends DefaultHandler {
	
	private StringBuilder currentValue = new StringBuilder();
	
	private Noeud currentNode;
	private Way currentWay;
	private Relation currentRel;
	
	private static ArrayList<String> tagList;
	
	private NavigableMap<Long, Noeud> nodeMap;
	private List<Way> wayList;
	private List<Relation> relList;
	private Map<String, Double> bounds = new HashMap<>();
	private Map<String, Boolean> type = new HashMap<>();
	
	private static final String BOUNDS = "bounds";
	private static final String REL = "relation";
	private static final String MBR = "member";
    private static final String NODE = "node";
    private static final String TAG = "tag";
    private static final String WAY = "way";
    private static final String ND = "nd";
    
    public NavigableMap<Long, Noeud> getNodeMap(){
    	return this.nodeMap;
    }
    
    
    public List<Way> getWayList(){
    	return this.wayList;
    }
    
    public List<Relation> getRelList(){
    	return this.relList;
    }
    
    public Map<String, Double> getBounds(){
    	return this.bounds;
    }
    
	@Override
	public void startDocument() {
		nodeMap = new TreeMap<Long, Noeud>();
		wayList = new ArrayList<Way>();
		relList = new ArrayList<>();
		addNomToList();
	}
    
	public void addNomToList() {
		this.tagList= new ArrayList<String>();
		tagList.add("AERIALWAY");tagList.add("AEROWAY");tagList.add("AMENITY");tagList.add("BARRIER");
		tagList.add("BOUNDARY");tagList.add("BUILDING");tagList.add("CRAFT");tagList.add("EMERGENCY");
		tagList.add("GEOLOGICAL");tagList.add("HEALTHCARE");tagList.add("HIGHWAY");tagList.add("HISTORIC");
		tagList.add("LANDUSE");tagList.add("LEISURE");tagList.add("MAN_MADE");tagList.add("MILITARY");
		tagList.add("NATURAL");tagList.add("OFFICE");tagList.add("PLACE");tagList.add("POWER");tagList.add("PUBLIC TRANSPORT");
		tagList.add("RAILWAY");tagList.add("ROUTE");tagList.add("SHOP");tagList.add("SPORT");tagList.add("TELECOM");
		tagList.add("TOURISM");tagList.add("WATER");tagList.add("WATERWAY");
	}
	
	@Override
	public void startElement(String uri, String lName, String qName, Attributes attr) throws SAXException {
		currentValue.setLength(0);
		boolean found = false;
				
		switch(qName) {
			case BOUNDS:
				this.bounds.put("minlat", Double.parseDouble(attr.getValue("minlat")));
				this.bounds.put("minlon", Double.parseDouble(attr.getValue("minlon")));
				this.bounds.put("maxlat", Double.parseDouble(attr.getValue("maxlat")));
				this.bounds.put("maxlon", Double.parseDouble(attr.getValue("maxlon")));
				break;
				
			case NODE:
				long id = Long.parseLong(attr.getValue("id"));
				double lat = Double.parseDouble(attr.getValue("lat"));
				double lon = Double.parseDouble(attr.getValue("lon"));
			
				currentNode = new Noeud(id, lat, lon, null);
				break;
			
			case WAY:
				currentNode = null;
				long wId = Long.parseLong(attr.getValue("id"));
				currentWay = new Way(wId);
				break;		
				
			case TAG:	
				if(attr.getValue("k").toUpperCase().equals("NAME")) {
					String nom = attr.getValue("v").toLowerCase();
					if(currentNode != null) {
						currentNode.setNom(nom);
					}
					else {
						currentWay.setNom(nom);	
						for(Noeud n : currentWay.getlist()){
                        	n.setNom(nom);
                        }
					}
				}
				
				//BUS
				if((attr.getValue("k").equals("highway"))) {
					String b = attr.getValue("v");
					boolean bus = b.equals("bus_stop");
					
					if(currentNode != null  && !currentNode.isBusStop()) {
						currentNode.setBusStop(bus);
					}
					/*
					else {
						for(Noeud n : currentWay.getlist()) {
							if(!n.isBus()) {
								n.setBusStop(bus);
							}
						}
							
					}
					*/
						
				}
				
				//PEDESTRIAN
				 if(attr.getValue("k").equals("highway")) {
	                 String b = attr.getValue("v");
	                 boolean pedestrian = 
	                         b.equals("footway") || b.equals("pedestrian") || b.equals("living_street") || b.equals("residential")
	                         || b.equals("path") || b.equals("track") || b.equals("steps") || b.equals("crossing");
	                 
	                 if(currentNode != null) {
	                	 currentNode.setPedestrian(pedestrian);
						}
						else {
							for(Noeud n : currentWay.getlist()) {
								 n.setPedestrian(pedestrian);
							}
					 }
	                 
	                	
	             }
				 
				//MOTORIZED VEHICLE
				 if(attr.getValue("k").equals("highway")) {
	                 String b = attr.getValue("v");
	                 boolean motorizedVehicle = 
	                         b.equals("living_street") || b.equals("track")  ||  b.equals("residential")
	                         || b.equals("tertiary")  || b.equals("secondary") || b.equals("primary");
	                 
	                 
	                 if(currentNode != null && !currentNode.isMotorizedVehicle()) {
	                	 if(b.equals("living_street")) { currentNode.setMaxMotorizedVehicleSpeed(8.0); }
	                	 currentNode.setMotorizedVehicle(motorizedVehicle);
					 }
					 else {
					
							for(Noeud n : currentWay.getlist()) {
								if(!n.isMotorizedVehicle()) {
									 if(b.equals("living_street")) { n.setMaxMotorizedVehicleSpeed(8.0); }
									 n.setMotorizedVehicle(motorizedVehicle);
								}
							}
					 }
	                 
	                	
	             }
				 
				 //BICYCLE
                 boolean cycleway = false,highway = false , bicycle= false;
                 if(attr.getValue("k").contains("cycleway")) {
                     String b = attr.getValue("v");
                     cycleway = b.equals("cycleway") || b.equals("track") || b.equals("lane") || b.equals("residential")
                             || b.equals("opposite_lane") || b.equals("opposite") || b.equals("shared") || b.equals("crossing");
                     
                 }
                 
                 
                 else if(attr.getValue("k").equals("highway")) {
                    highway = true;
                 }
                 
                 else if(attr.getValue("k").contains("bicycle")) {
                     bicycle = !attr.getValue("v").equals("no");
                 }
             
                 if(highway || cycleway || (highway && bicycle)) {
                	 if(currentNode != null) {
                		 currentNode.setBicycle(true);
						}
						else {
							for(Noeud n : currentWay.getlist()) {
								 n.setBicycle(true);
							}
					 }
                    
                 }
         				
				//si le tag appartient a un way on recup sa value et son type
				if(currentNode == null) {
					for(String tag : tagList) {				
						if((attr.getValue("k").toUpperCase().equals(tag)) && !found){
							if(tag.equals("BUILDING")) {
								found = true;
							}
							currentWay.setType(tag);
							currentWay.setValue(attr.getValue("v").toUpperCase());
						}
					}
				}
				break;

				
			case ND:
				long nRef = Long.parseLong(attr.getValue("ref"));
				Noeud n = nodeMap.get(nRef);
				if(n != null)
					currentWay.addNode(n);		
				break;
				
			case REL:
				long relId = Long.parseLong(attr.getValue("id"));
				Relation rel = new Relation(relId);
				currentRel = rel;
				break;
				
			case MBR:
				String type = attr.getValue("type");
				Long mRef = Long.parseLong(attr.getValue("ref"));
				String role = attr.getValue("role");

				Member member = new Member(type, mRef, role);
				currentRel.getmember().add(member);
				
				if(member.getType().equals("way")) {
					
					for(Way way : wayList) {
						if(way.getID() == member.getRef()) {
							
							for(Noeud noeud : way.getlist()) {
								noeud.setBus(true);
							}
						}
					}
					
					Noeud noeud = nodeMap.get(mRef);
					
					if(noeud != null) {
						if(member.getRole() == "stop") {
							noeud.setBusStop(true);
						}
					}
					
				}
				
				
				break;
				
		}
	}
	
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		
		switch(qName) {
			case NODE:
				nodeMap.put(currentNode.getID(), currentNode);
				break;
			
			case WAY:
				wayList.add(currentWay);
				List<Noeud> list = currentWay.getlist();
                int size = list.size();
               
                for(int i = 0; i<size - 1; i++) {
                    Noeud n1 = list.get(i);
                    Noeud n2 = list.get(i+1);
                    n1.getAdj().add(n2);
                    n2.getAdj().add(n1);
                }
				break;
				
			case REL:
				relList.add(currentRel);
				break;
		}
	}
	
	@Override
    public void characters(char[] ch, int start, int length) throws SAXException {
      	currentValue.append(ch, start, length);
    }
	
	public ArrayList<String> getNomTag() {
		return tagList;
	}

	public void setNomTag(ArrayList<String> nomTag) {
		this.tagList = nomTag;
	}
}
