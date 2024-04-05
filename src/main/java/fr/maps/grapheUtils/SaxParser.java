package fr.maps.grapheUtils;

import java.io.IOException;
import java.io.InputStream;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import fr.maps.Graphe;
import org.xml.sax.SAXException;


public class SaxParser {
	
	private static GraphHandlerSax handler = new GraphHandlerSax();
		
	private static void parseInputStream(InputStream is) {

        SAXParserFactory factory = SAXParserFactory.newInstance();

        try { 
            SAXParser saxParser = factory.newSAXParser();
            saxParser.parse(is, handler);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        } 
    }
	
	public static void transfertInfo(Graphe g, InputStream is) {
		parseInputStream(is);
		g.setNodeMap(handler.getNodeMap());
		g.setWayList(handler.getWayList());
		//for(Way w: handler.getWayList()) {
		//	System.out.println("way -> "+w.getNom());
		//};
		g.setRelList(handler.getRelList());
		g.setMinLat((Double)handler.getBounds().get("minlat"));
		g.setMinLon((Double)handler.getBounds().get("minlon"));
		g.setMaxLat((Double)handler.getBounds().get("maxlat"));
		g.setMaxLon((Double)handler.getBounds().get("maxlon"));
	}
}