package fr.maps;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/*
 *Talatizi Kamel
 * Kechadi farid
 */
public class ParseXml {

    //pour parser le fichier xml des batiments
    public ArrayList<BatimentData> parserBatiment(String nom_ville) {
        List ListBat = null;
        String dir;
        dir = repertoireCourant();
        try {
            File inputFile = new File(dir+"/région de la france/"+nom_ville+"/batimentXml.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();
            System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
            NodeList nList = doc.getElementsByTagName("way");

            ListBat = new ArrayList<BatimentData>();

            List maxlat = new ArrayList<Double>();
            List minlat = new ArrayList<Double>();

            List maxlon = new ArrayList<Double>();
            List minlon = new ArrayList<Double>();





            System.out.println("----------------------------");
            for (int temp = 0; temp < nList.getLength(); temp++) {
                BatimentData datBat = new BatimentData();
                Node nNode = nList.item(temp);
                System.out.println("\nCurrent Element :" + nNode.getNodeName());
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;


                    System.out.println("Way ID : "
                            + eElement.getAttribute("id"));
                    datBat.setIdWay(Double.parseDouble(eElement.getAttribute("id")));
                    System.out.println("MinLat" +
                            " : "
                            + eElement.getElementsByTagName("bounds")
                            .item(0)
                            .getAttributes()
                            .getNamedItem("minlat")
                            .getNodeValue());
                    minlat.add(Double.parseDouble(eElement.getElementsByTagName("bounds")
                            .item(0)
                            .getAttributes()
                            .getNamedItem("minlat")
                            .getNodeValue()));
                    datBat.setMinLat(Double.parseDouble(eElement.getElementsByTagName("bounds")
                            .item(0)
                            .getAttributes()
                            .getNamedItem("minlat")
                            .getNodeValue()));
                    minlon.add(Double.parseDouble(eElement.getElementsByTagName("bounds")
                            .item(0)
                            .getAttributes()
                            .getNamedItem("minlon")
                            .getNodeValue()));
                    System.out.println("MinLon" +
                            " : "
                            + eElement.getElementsByTagName("bounds")
                            .item(0)
                            .getAttributes()
                            .getNamedItem("minlon")
                            .getNodeValue());
                    datBat.setMaxLat((Double.parseDouble(eElement.getElementsByTagName("bounds")
                            .item(0)
                            .getAttributes()
                            .getNamedItem("maxlat")
                            .getNodeValue())));
                    System.out.println("MaxLat" +
                            " : "
                            + eElement.getElementsByTagName("bounds")
                            .item(0)
                            .getAttributes()
                            .getNamedItem("maxlat")
                            .getNodeValue());
                    maxlat.add(Double.parseDouble(eElement.getElementsByTagName("bounds")
                            .item(0)
                            .getAttributes()
                            .getNamedItem("maxlat")
                            .getNodeValue()));
                    datBat.setMinLon(Double.parseDouble(eElement.getElementsByTagName("bounds")
                            .item(0)
                            .getAttributes()
                            .getNamedItem("minlon")
                            .getNodeValue()));
                    System.out.println("MaxLon" +
                            " : "
                            + eElement.getElementsByTagName("bounds")
                            .item(0)
                            .getAttributes()
                            .getNamedItem("maxlon")
                            .getNodeValue());
                    datBat.setMinLon(Double.parseDouble(eElement.getElementsByTagName("bounds")
                            .item(0)
                            .getAttributes()
                            .getNamedItem("minlon")
                            .getNodeValue()));
                    maxlon.add(Double.parseDouble(eElement.getElementsByTagName("bounds")
                            .item(0)
                            .getAttributes()
                            .getNamedItem("maxlon")
                            .getNodeValue()));


                    NodeList Listnd = eElement.getElementsByTagName("nd");
                    for (int i = 0; i < Listnd.getLength(); i++) {
                        Node nNode1 = Listnd.item(i);
                        if (nNode1.getNodeType() == Node.ELEMENT_NODE) {
                            Element eElement2 = (Element) nNode1;
                            System.out.println("lat" + ":" + eElement2.getAttribute("lat"));
                            datBat.setLat(Double.parseDouble(eElement2.getAttribute("lat")));
                            System.out.println("lon" + ":" + eElement2.getAttribute("lon"));
                            datBat.setLon(Double.parseDouble(eElement2.getAttribute("lon")));
                        }
                    }

                    NodeList Listtag = eElement.getElementsByTagName("tag");
                    for (int i = 0; i < Listtag.getLength(); i++) {
                        Node nNode1 = Listtag.item(i);
                        if (nNode1.getNodeType() == Node.ELEMENT_NODE) {
                            Element eElement2 = (Element) nNode1;
                            System.out.println("lat" + ":" + eElement2.getAttribute("lat"));
                            datBat.setInfo(eElement2.getAttribute("k"));
                            //System.out.println("lon" + ":" + eElement2.getAttribute("lon"));
                            datBat.setInfo(eElement2.getAttribute("v"));
                        }
                    }


                }
                ListBat.add(datBat);
            }

            System.out.println("*********************");
            System.out.println("la                liste");

            for (int i = 0; i < ListBat.size(); i++) {
                BatimentData batiment = (BatimentData) ListBat.get(i);
                System.out.println("*****************************************************************");
                System.out.println(batiment.lat);
                System.out.println(batiment.lon);

            }


            double max = 0;
            for (Object element : maxlat) {
                if (max < (double) element) {
                    max = (double) element;
                }

            }
            double min = 43.9206992;

            for (Object element : minlat) {
                if (min > (double) element) {
                    min = (double) element;
                }

            }

            double maxlonn = 0;
            for (Object element : maxlon) {
                if (maxlonn < (double) element) {
                    maxlonn = (double) element;
                }

            }
            double minlonn = 43.9206992;

            for (Object element : minlon) {
                if (minlonn > (double) element) {
                    minlonn = (double) element;
                }

            }

            System.out.println(max);
            System.out.println(min);

            System.out.println(maxlonn);
            System.out.print(minlonn);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return (ArrayList<BatimentData>) ListBat;
    }

    //pour parser le fichier xml des batiments
    public ArrayList<RoadsData> parserRoads(String nom_ville) {
        List ListRoad = null;
        String dir;
        dir = repertoireCourant();
        try {
            File inputFile = new File(dir+"/région de la france/"+nom_ville+"/RoadXml.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();
            System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
            NodeList nList = doc.getElementsByTagName("way");

            ListRoad = new ArrayList<BatimentData>();

            List maxlat = new ArrayList<Double>();
            List minlat = new ArrayList<Double>();

            List maxlon = new ArrayList<Double>();
            List minlon = new ArrayList<Double>();





            System.out.println("----------------------------");
            for (int temp = 0; temp < nList.getLength(); temp++) {
                RoadsData dataRoad = new RoadsData();
                Node nNode = nList.item(temp);
                System.out.println("\nCurrent Element :" + nNode.getNodeName());
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;


                    System.out.println("Way ID : "
                            + eElement.getAttribute("id"));

                    dataRoad.setIdWay(Double.parseDouble(eElement.getAttribute("id")));
                    System.out.println("MinLat" +
                            " : "
                            + eElement.getElementsByTagName("bounds")
                            .item(0)
                            .getAttributes()
                            .getNamedItem("minlat")
                            .getNodeValue());
                    minlat.add(Double.parseDouble(eElement.getElementsByTagName("bounds")
                            .item(0)
                            .getAttributes()
                            .getNamedItem("minlat")
                            .getNodeValue()));
                    dataRoad.setMinLat(Double.parseDouble(eElement.getElementsByTagName("bounds")
                            .item(0)
                            .getAttributes()
                            .getNamedItem("minlat")
                            .getNodeValue()));
                    minlon.add(Double.parseDouble(eElement.getElementsByTagName("bounds")
                            .item(0)
                            .getAttributes()
                            .getNamedItem("minlon")
                            .getNodeValue()));
                    System.out.println("MinLon" +
                            " : "
                            + eElement.getElementsByTagName("bounds")
                            .item(0)
                            .getAttributes()
                            .getNamedItem("minlon")
                            .getNodeValue());
                    dataRoad.setMaxLat((Double.parseDouble(eElement.getElementsByTagName("bounds")
                            .item(0)
                            .getAttributes()
                            .getNamedItem("maxlat")
                            .getNodeValue())));
                    System.out.println("MaxLat" +
                            " : "
                            + eElement.getElementsByTagName("bounds")
                            .item(0)
                            .getAttributes()
                            .getNamedItem("maxlat")
                            .getNodeValue());
                    maxlat.add(Double.parseDouble(eElement.getElementsByTagName("bounds")
                            .item(0)
                            .getAttributes()
                            .getNamedItem("maxlat")
                            .getNodeValue()));
                    dataRoad.setMinLon(Double.parseDouble(eElement.getElementsByTagName("bounds")
                            .item(0)
                            .getAttributes()
                            .getNamedItem("minlon")
                            .getNodeValue()));
                    System.out.println("MaxLon" +
                            " : "
                            + eElement.getElementsByTagName("bounds")
                            .item(0)
                            .getAttributes()
                            .getNamedItem("maxlon")
                            .getNodeValue());
                    dataRoad.setMinLon(Double.parseDouble(eElement.getElementsByTagName("bounds")
                            .item(0)
                            .getAttributes()
                            .getNamedItem("minlon")
                            .getNodeValue()));
                    maxlon.add(Double.parseDouble(eElement.getElementsByTagName("bounds")
                            .item(0)
                            .getAttributes()
                            .getNamedItem("maxlon")
                            .getNodeValue()));


                    NodeList Listnd = eElement.getElementsByTagName("nd");
                    for (int i = 0; i < Listnd.getLength(); i++) {
                        Node nNode1 = Listnd.item(i);
                        if (nNode1.getNodeType() == Node.ELEMENT_NODE) {
                            Element eElement2 = (Element) nNode1;

                            System.out.println("lat" + ":" + eElement2.getAttribute("lat"));
                            dataRoad.setLat(Double.parseDouble(eElement2.getAttribute("lat")));
                            System.out.println("lon" + ":" + eElement2.getAttribute("lon"));
                            dataRoad.setLon(Double.parseDouble(eElement2.getAttribute("lon")));
                        }
                    }

                    NodeList Listtag = eElement.getElementsByTagName("tag");
                    for (int i = 0; i < Listtag.getLength(); i++) {
                        Node nNode1 = Listtag.item(i);
                        if (nNode1.getNodeType() == Node.ELEMENT_NODE) {
                            Element eElement2 = (Element) nNode1;
                            System.out.println("lat" + ":" + eElement2.getAttribute("lat"));
                            dataRoad.setInfo(eElement2.getAttribute("k"));
                            //System.out.println("lon" + ":" + eElement2.getAttribute("lon"));
                            dataRoad.setInfo(eElement2.getAttribute("v"));
                        }
                    }

                    ListRoad.add(dataRoad);
                }

            }

            System.out.println("*********************");
            System.out.println("la                liste");

            for (int i = 0; i < ListRoad.size(); i++) {
                RoadsData roadsData = (RoadsData) ListRoad.get(i);
                System.out.println("*****************************************************************");
                System.out.println(roadsData.getIdWay());
                System.out.println(roadsData.lat);
                System.out.println(roadsData.lon);

            }


            double max = 0;
            for (Object element : maxlat) {
                if (max < (double) element) {
                    max = (double) element;
                }

            }
            double min = 43.9206992;

            for (Object element : minlat) {
                if (min > (double) element) {
                    min = (double) element;
                }

            }

            double maxlonn = 0;
            for (Object element : maxlon) {
                if (maxlonn < (double) element) {
                    maxlonn = (double) element;
                }

            }
            double minlonn = 43.9206992;
            for (Object element : minlon) {
                if (minlonn > (double) element) {
                    minlonn = (double) element;
                }

            }

            System.out.println(max);
            System.out.println(min);

            System.out.println(maxlonn);
            System.out.print(minlonn);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return (ArrayList<RoadsData>) ListRoad;
    }
    //parseur Water
    public ArrayList<WaterData> parserWater(String nom_ville) {
        List ListBat = null;
        String dir;
        dir = repertoireCourant();
        try {
            File inputFile = new File(dir+"/région de la france/"+nom_ville+"/WaterXml.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();
            System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
            NodeList nList = doc.getElementsByTagName("way");

            ListBat = new ArrayList<BatimentData>();

            List maxlat = new ArrayList<Double>();
            List minlat = new ArrayList<Double>();

            List maxlon = new ArrayList<Double>();
            List minlon = new ArrayList<Double>();





            System.out.println("----------------------------");
            for (int temp = 0; temp < nList.getLength(); temp++) {
                WaterData datBat = new WaterData();
                Node nNode = nList.item(temp);
                System.out.println("\nCurrent Element :" + nNode.getNodeName());
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;


                    System.out.println("Way ID : "
                            + eElement.getAttribute("id"));
                    datBat.setIdWay(Double.parseDouble(eElement.getAttribute("id")));
                    System.out.println("MinLat" +
                            " : "
                            + eElement.getElementsByTagName("bounds")
                            .item(0)
                            .getAttributes()
                            .getNamedItem("minlat")
                            .getNodeValue());
                    minlat.add(Double.parseDouble(eElement.getElementsByTagName("bounds")
                            .item(0)
                            .getAttributes()
                            .getNamedItem("minlat")
                            .getNodeValue()));
                    datBat.setMinLat(Double.parseDouble(eElement.getElementsByTagName("bounds")
                            .item(0)
                            .getAttributes()
                            .getNamedItem("minlat")
                            .getNodeValue()));
                    minlon.add(Double.parseDouble(eElement.getElementsByTagName("bounds")
                            .item(0)
                            .getAttributes()
                            .getNamedItem("minlon")
                            .getNodeValue()));
                    System.out.println("MinLon" +
                            " : "
                            + eElement.getElementsByTagName("bounds")
                            .item(0)
                            .getAttributes()
                            .getNamedItem("minlon")
                            .getNodeValue());
                    datBat.setMaxLat((Double.parseDouble(eElement.getElementsByTagName("bounds")
                            .item(0)
                            .getAttributes()
                            .getNamedItem("maxlat")
                            .getNodeValue())));
                    System.out.println("MaxLat" +
                            " : "
                            + eElement.getElementsByTagName("bounds")
                            .item(0)
                            .getAttributes()
                            .getNamedItem("maxlat")
                            .getNodeValue());
                    maxlat.add(Double.parseDouble(eElement.getElementsByTagName("bounds")
                            .item(0)
                            .getAttributes()
                            .getNamedItem("maxlat")
                            .getNodeValue()));
                    datBat.setMinLon(Double.parseDouble(eElement.getElementsByTagName("bounds")
                            .item(0)
                            .getAttributes()
                            .getNamedItem("minlon")
                            .getNodeValue()));
                    System.out.println("MaxLon" +
                            " : "
                            + eElement.getElementsByTagName("bounds")
                            .item(0)
                            .getAttributes()
                            .getNamedItem("maxlon")
                            .getNodeValue());
                    datBat.setMinLon(Double.parseDouble(eElement.getElementsByTagName("bounds")
                            .item(0)
                            .getAttributes()
                            .getNamedItem("minlon")
                            .getNodeValue()));
                    maxlon.add(Double.parseDouble(eElement.getElementsByTagName("bounds")
                            .item(0)
                            .getAttributes()
                            .getNamedItem("maxlon")
                            .getNodeValue()));


                    NodeList Listnd = eElement.getElementsByTagName("nd");
                    for (int i = 0; i < Listnd.getLength(); i++) {
                        Node nNode1 = Listnd.item(i);
                        if (nNode1.getNodeType() == Node.ELEMENT_NODE) {
                            Element eElement2 = (Element) nNode1;
                            System.out.println("lat" + ":" + eElement2.getAttribute("lat"));
                            datBat.setLat(Double.parseDouble(eElement2.getAttribute("lat")));
                            System.out.println("lon" + ":" + eElement2.getAttribute("lon"));
                            datBat.setLon(Double.parseDouble(eElement2.getAttribute("lon")));
                        }
                    }

                    NodeList Listtag = eElement.getElementsByTagName("tag");
                    for (int i = 0; i < Listtag.getLength(); i++) {
                        Node nNode1 = Listtag.item(i);
                        if (nNode1.getNodeType() == Node.ELEMENT_NODE) {
                            Element eElement2 = (Element) nNode1;
                            System.out.println("lat" + ":" + eElement2.getAttribute("lat"));
                            datBat.setInfo(eElement2.getAttribute("k"));
                            //System.out.println("lon" + ":" + eElement2.getAttribute("lon"));
                            datBat.setInfo(eElement2.getAttribute("v"));
                        }
                    }


                }
                ListBat.add(datBat);
            }

            System.out.println("*********************");
            System.out.println("la                liste");

            for (int i = 0; i < ListBat.size(); i++) {
                WaterData batiment = (WaterData) ListBat.get(i);
                System.out.println("*****************************************************************");
                System.out.println(batiment.lat);
                System.out.println(batiment.lon);

            }


            double max = 0;
            for (Object element : maxlat) {
                if (max < (double) element) {
                    max = (double) element;
                }

            }
            double min = 43.9206992;

            for (Object element : minlat) {
                if (min > (double) element) {
                    min = (double) element;
                }

            }

            double maxlonn = 0;
            for (Object element : maxlon) {
                if (maxlonn < (double) element) {
                    maxlonn = (double) element;
                }

            }
            double minlonn = 43.9206992;

            for (Object element : minlon) {
                if (minlonn > (double) element) {
                    minlonn = (double) element;
                }

            }

            System.out.println(max);
            System.out.println(min);

            System.out.println(maxlonn);
            System.out.print(minlonn);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return (ArrayList<WaterData>) ListBat;
    }

    //parseur Water
    public ArrayList<ParkData> parserPark(String nom_ville) {
        List ListBat = null;
        String dir;
        dir = repertoireCourant();
        try {
            File inputFile = new File(dir+"/région de la france/"+nom_ville+"/ParkXml.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();
            System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
            NodeList nList = doc.getElementsByTagName("way");

            ListBat = new ArrayList<BatimentData>();

            List maxlat = new ArrayList<Double>();
            List minlat = new ArrayList<Double>();

            List maxlon = new ArrayList<Double>();
            List minlon = new ArrayList<Double>();





            System.out.println("----------------------------");
            for (int temp = 0; temp < nList.getLength(); temp++) {
                ParkData datBat = new ParkData();
                Node nNode = nList.item(temp);
                System.out.println("\nCurrent Element :" + nNode.getNodeName());
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;


                    System.out.println("Way ID : "
                            + eElement.getAttribute("id"));
                    datBat.setIdWay(Double.parseDouble(eElement.getAttribute("id")));
                    System.out.println("MinLat" +
                            " : "
                            + eElement.getElementsByTagName("bounds")
                            .item(0)
                            .getAttributes()
                            .getNamedItem("minlat")
                            .getNodeValue());
                    minlat.add(Double.parseDouble(eElement.getElementsByTagName("bounds")
                            .item(0)
                            .getAttributes()
                            .getNamedItem("minlat")
                            .getNodeValue()));
                    datBat.setMinLat(Double.parseDouble(eElement.getElementsByTagName("bounds")
                            .item(0)
                            .getAttributes()
                            .getNamedItem("minlat")
                            .getNodeValue()));
                    minlon.add(Double.parseDouble(eElement.getElementsByTagName("bounds")
                            .item(0)
                            .getAttributes()
                            .getNamedItem("minlon")
                            .getNodeValue()));
                    System.out.println("MinLon" +
                            " : "
                            + eElement.getElementsByTagName("bounds")
                            .item(0)
                            .getAttributes()
                            .getNamedItem("minlon")
                            .getNodeValue());
                    datBat.setMaxLat((Double.parseDouble(eElement.getElementsByTagName("bounds")
                            .item(0)
                            .getAttributes()
                            .getNamedItem("maxlat")
                            .getNodeValue())));
                    System.out.println("MaxLat" +
                            " : "
                            + eElement.getElementsByTagName("bounds")
                            .item(0)
                            .getAttributes()
                            .getNamedItem("maxlat")
                            .getNodeValue());
                    maxlat.add(Double.parseDouble(eElement.getElementsByTagName("bounds")
                            .item(0)
                            .getAttributes()
                            .getNamedItem("maxlat")
                            .getNodeValue()));
                    datBat.setMinLon(Double.parseDouble(eElement.getElementsByTagName("bounds")
                            .item(0)
                            .getAttributes()
                            .getNamedItem("minlon")
                            .getNodeValue()));
                    System.out.println("MaxLon" +
                            " : "
                            + eElement.getElementsByTagName("bounds")
                            .item(0)
                            .getAttributes()
                            .getNamedItem("maxlon")
                            .getNodeValue());
                    datBat.setMinLon(Double.parseDouble(eElement.getElementsByTagName("bounds")
                            .item(0)
                            .getAttributes()
                            .getNamedItem("minlon")
                            .getNodeValue()));
                    maxlon.add(Double.parseDouble(eElement.getElementsByTagName("bounds")
                            .item(0)
                            .getAttributes()
                            .getNamedItem("maxlon")
                            .getNodeValue()));


                    NodeList Listnd = eElement.getElementsByTagName("nd");
                    for (int i = 0; i < Listnd.getLength(); i++) {
                        Node nNode1 = Listnd.item(i);
                        if (nNode1.getNodeType() == Node.ELEMENT_NODE) {
                            Element eElement2 = (Element) nNode1;
                            System.out.println("lat" + ":" + eElement2.getAttribute("lat"));
                            datBat.setLat(Double.parseDouble(eElement2.getAttribute("lat")));
                            System.out.println("lon" + ":" + eElement2.getAttribute("lon"));
                            datBat.setLon(Double.parseDouble(eElement2.getAttribute("lon")));
                        }
                    }

                    NodeList Listtag = eElement.getElementsByTagName("tag");
                    for (int i = 0; i < Listtag.getLength(); i++) {
                        Node nNode1 = Listtag.item(i);
                        if (nNode1.getNodeType() == Node.ELEMENT_NODE) {
                            Element eElement2 = (Element) nNode1;
                            System.out.println("lat" + ":" + eElement2.getAttribute("lat"));
                            datBat.setInfo(eElement2.getAttribute("k"));
                            //System.out.println("lon" + ":" + eElement2.getAttribute("lon"));
                            datBat.setInfo(eElement2.getAttribute("v"));
                        }
                    }


                }
                ListBat.add(datBat);
            }

            System.out.println("*********************");
            System.out.println("la                liste");

            for (int i = 0; i < ListBat.size(); i++) {
                ParkData batiment = (ParkData) ListBat.get(i);
                System.out.println("*****************************************************************");
                System.out.println(batiment.lat);
                System.out.println(batiment.lon);

            }


            double max = 0;
            for (Object element : maxlat) {
                if (max < (double) element) {
                    max = (double) element;
                }

            }
            double min = 43.9206992;

            for (Object element : minlat) {
                if (min > (double) element) {
                    min = (double) element;
                }

            }

            double maxlonn = 0;
            for (Object element : maxlon) {
                if (maxlonn < (double) element) {
                    maxlonn = (double) element;
                }

            }
            double minlonn = 43.9206992;

            for (Object element : minlon) {
                if (minlonn > (double) element) {
                    minlonn = (double) element;
                }

            }

            System.out.println(max);
            System.out.println(min);

            System.out.println(maxlonn);
            System.out.print(minlonn);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return (ArrayList<ParkData>) ListBat;
    }

    public ArrayList<ForetData> parserforet(String nom_ville) {
        List ListBat = null;
        String dir;
        dir = repertoireCourant();
        try {
            File inputFile = new File(dir+"/région de la france/"+nom_ville+"/woodXml.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();
            System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
            NodeList nList = doc.getElementsByTagName("way");

            ListBat = new ArrayList<BatimentData>();

            List maxlat = new ArrayList<Double>();
            List minlat = new ArrayList<Double>();

            List maxlon = new ArrayList<Double>();
            List minlon = new ArrayList<Double>();





            System.out.println("----------------------------");
            for (int temp = 0; temp < nList.getLength(); temp++) {
                ForetData datBat = new ForetData();
                Node nNode = nList.item(temp);
                System.out.println("\nCurrent Element :" + nNode.getNodeName());
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;


                    System.out.println("Way ID : "
                            + eElement.getAttribute("id"));
                    datBat.setIdWay(Double.parseDouble(eElement.getAttribute("id")));
                    System.out.println("MinLat" +
                            " : "
                            + eElement.getElementsByTagName("bounds")
                            .item(0)
                            .getAttributes()
                            .getNamedItem("minlat")
                            .getNodeValue());
                    minlat.add(Double.parseDouble(eElement.getElementsByTagName("bounds")
                            .item(0)
                            .getAttributes()
                            .getNamedItem("minlat")
                            .getNodeValue()));
                    datBat.setMinLat(Double.parseDouble(eElement.getElementsByTagName("bounds")
                            .item(0)
                            .getAttributes()
                            .getNamedItem("minlat")
                            .getNodeValue()));
                    minlon.add(Double.parseDouble(eElement.getElementsByTagName("bounds")
                            .item(0)
                            .getAttributes()
                            .getNamedItem("minlon")
                            .getNodeValue()));
                    System.out.println("MinLon" +
                            " : "
                            + eElement.getElementsByTagName("bounds")
                            .item(0)
                            .getAttributes()
                            .getNamedItem("minlon")
                            .getNodeValue());
                    datBat.setMaxLat((Double.parseDouble(eElement.getElementsByTagName("bounds")
                            .item(0)
                            .getAttributes()
                            .getNamedItem("maxlat")
                            .getNodeValue())));
                    System.out.println("MaxLat" +
                            " : "
                            + eElement.getElementsByTagName("bounds")
                            .item(0)
                            .getAttributes()
                            .getNamedItem("maxlat")
                            .getNodeValue());
                    maxlat.add(Double.parseDouble(eElement.getElementsByTagName("bounds")
                            .item(0)
                            .getAttributes()
                            .getNamedItem("maxlat")
                            .getNodeValue()));
                    datBat.setMinLon(Double.parseDouble(eElement.getElementsByTagName("bounds")
                            .item(0)
                            .getAttributes()
                            .getNamedItem("minlon")
                            .getNodeValue()));
                    System.out.println("MaxLon" +
                            " : "
                            + eElement.getElementsByTagName("bounds")
                            .item(0)
                            .getAttributes()
                            .getNamedItem("maxlon")
                            .getNodeValue());
                    datBat.setMinLon(Double.parseDouble(eElement.getElementsByTagName("bounds")
                            .item(0)
                            .getAttributes()
                            .getNamedItem("minlon")
                            .getNodeValue()));
                    maxlon.add(Double.parseDouble(eElement.getElementsByTagName("bounds")
                            .item(0)
                            .getAttributes()
                            .getNamedItem("maxlon")
                            .getNodeValue()));


                    NodeList Listnd = eElement.getElementsByTagName("nd");
                    for (int i = 0; i < Listnd.getLength(); i++) {
                        Node nNode1 = Listnd.item(i);
                        if (nNode1.getNodeType() == Node.ELEMENT_NODE) {
                            Element eElement2 = (Element) nNode1;
                            System.out.println("lat" + ":" + eElement2.getAttribute("lat"));
                            datBat.setLat(Double.parseDouble(eElement2.getAttribute("lat")));
                            System.out.println("lon" + ":" + eElement2.getAttribute("lon"));
                            datBat.setLon(Double.parseDouble(eElement2.getAttribute("lon")));
                        }
                    }

                    NodeList Listtag = eElement.getElementsByTagName("tag");
                    for (int i = 0; i < Listtag.getLength(); i++) {
                        Node nNode1 = Listtag.item(i);
                        if (nNode1.getNodeType() == Node.ELEMENT_NODE) {
                            Element eElement2 = (Element) nNode1;
                            System.out.println("lat" + ":" + eElement2.getAttribute("lat"));
                            datBat.setInfo(eElement2.getAttribute("k"));
                            //System.out.println("lon" + ":" + eElement2.getAttribute("lon"));
                            datBat.setInfo(eElement2.getAttribute("v"));
                        }
                    }




                }
                ListBat.add(datBat);
            }

            System.out.println("*********************");
            System.out.println("la                liste");

            for (int i = 0; i < ListBat.size(); i++) {
                ForetData batiment = (ForetData) ListBat.get(i);
                System.out.println("*****************************************************************");
                System.out.println(batiment.lat);
                System.out.println(batiment.lon);

            }


            double max = 0;
            for (Object element : maxlat) {
                if (max < (double) element) {
                    max = (double) element;
                }

            }
            double min = 43.9206992;

            for (Object element : minlat) {
                if (min > (double) element) {
                    min = (double) element;
                }

            }

            double maxlonn = 0;
            for (Object element : maxlon) {
                if (maxlonn < (double) element) {
                    maxlonn = (double) element;
                }

            }
            double minlonn = 43.9206992;

            for (Object element : minlon) {
                if (minlonn > (double) element) {
                    minlonn = (double) element;
                }

            }

            System.out.println(max);
            System.out.println(min);

            System.out.println(maxlonn);
            System.out.print(minlonn);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return (ArrayList<ForetData>) ListBat;
    }

    public ArrayList<PointDintereData> parserPoint(String nom_ville) {
        List ListBat = null;
        String dir;
        dir = repertoireCourant();
        try {
            File inputFile = new File(dir+"/région de la france/"+nom_ville+"/PointInteretXml.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();
            System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
            NodeList nList = doc.getElementsByTagName("way");

            ListBat = new ArrayList<PointDintereData>();

            List maxlat = new ArrayList<Double>();
            List minlat = new ArrayList<Double>();

            List maxlon = new ArrayList<Double>();
            List minlon = new ArrayList<Double>();





            System.out.println("----------------------------");
            for (int temp = 0; temp < nList.getLength(); temp++) {
                PointDintereData datBat = new PointDintereData();
                Node nNode = nList.item(temp);
                System.out.println("\nCurrent Element :" + nNode.getNodeName());
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;


                    System.out.println("Way ID : "
                            + eElement.getAttribute("id"));
                    datBat.setIdWay(Double.parseDouble(eElement.getAttribute("id")));
                    System.out.println("MinLat" +
                            " : "
                            + eElement.getElementsByTagName("bounds")
                            .item(0)
                            .getAttributes()
                            .getNamedItem("minlat")
                            .getNodeValue());
                    minlat.add(Double.parseDouble(eElement.getElementsByTagName("bounds")
                            .item(0)
                            .getAttributes()
                            .getNamedItem("minlat")
                            .getNodeValue()));
                    datBat.setMinLat(Double.parseDouble(eElement.getElementsByTagName("bounds")
                            .item(0)
                            .getAttributes()
                            .getNamedItem("minlat")
                            .getNodeValue()));
                    minlon.add(Double.parseDouble(eElement.getElementsByTagName("bounds")
                            .item(0)
                            .getAttributes()
                            .getNamedItem("minlon")
                            .getNodeValue()));
                    System.out.println("MinLon" +
                            " : "
                            + eElement.getElementsByTagName("bounds")
                            .item(0)
                            .getAttributes()
                            .getNamedItem("minlon")
                            .getNodeValue());
                    datBat.setMaxLat((Double.parseDouble(eElement.getElementsByTagName("bounds")
                            .item(0)
                            .getAttributes()
                            .getNamedItem("maxlat")
                            .getNodeValue())));
                    System.out.println("MaxLat" +
                            " : "
                            + eElement.getElementsByTagName("bounds")
                            .item(0)
                            .getAttributes()
                            .getNamedItem("maxlat")
                            .getNodeValue());
                    maxlat.add(Double.parseDouble(eElement.getElementsByTagName("bounds")
                            .item(0)
                            .getAttributes()
                            .getNamedItem("maxlat")
                            .getNodeValue()));
                    datBat.setMinLon(Double.parseDouble(eElement.getElementsByTagName("bounds")
                            .item(0)
                            .getAttributes()
                            .getNamedItem("minlon")
                            .getNodeValue()));
                    System.out.println("MaxLon" +
                            " : "
                            + eElement.getElementsByTagName("bounds")
                            .item(0)
                            .getAttributes()
                            .getNamedItem("maxlon")
                            .getNodeValue());
                    datBat.setMinLon(Double.parseDouble(eElement.getElementsByTagName("bounds")
                            .item(0)
                            .getAttributes()
                            .getNamedItem("minlon")
                            .getNodeValue()));
                    maxlon.add(Double.parseDouble(eElement.getElementsByTagName("bounds")
                            .item(0)
                            .getAttributes()
                            .getNamedItem("maxlon")
                            .getNodeValue()));


                    NodeList Listnd = eElement.getElementsByTagName("nd");
                    for (int i = 0; i < Listnd.getLength(); i++) {
                        Node nNode1 = Listnd.item(i);
                        if (nNode1.getNodeType() == Node.ELEMENT_NODE) {
                            Element eElement2 = (Element) nNode1;
                            System.out.println("lat" + ":" + eElement2.getAttribute("lat"));
                            datBat.setLat(Double.parseDouble(eElement2.getAttribute("lat")));
                            System.out.println("lon" + ":" + eElement2.getAttribute("lon"));
                            datBat.setLon(Double.parseDouble(eElement2.getAttribute("lon")));
                        }
                    }

                    NodeList Listtag = eElement.getElementsByTagName("tag");
                    for (int i = 0; i < Listtag.getLength(); i++) {
                        Node nNode1 = Listtag.item(i);
                        if (nNode1.getNodeType() == Node.ELEMENT_NODE) {
                            Element eElement2 = (Element) nNode1;
                            System.out.println("lat" + ":" + eElement2.getAttribute("lat"));
                            datBat.setInfo(eElement2.getAttribute("k"));
                            //System.out.println("lon" + ":" + eElement2.getAttribute("lon"));
                            datBat.setInfo(eElement2.getAttribute("v"));
                        }
                    }


                }
                ListBat.add(datBat);
            }

            System.out.println("*********************");
            System.out.println("la                liste");

            for (int i = 0; i < ListBat.size(); i++) {
                PointDintereData batiment = (PointDintereData) ListBat.get(i);
                System.out.println("*****************************************************************");
                System.out.println(batiment.lat);
                System.out.println(batiment.lon);
                System.out.println(batiment.info);

            }


            double max = 0;
            for (Object element : maxlat) {
                if (max < (double) element) {
                    max = (double) element;
                }

            }
            double min = 43.9206992;

            for (Object element : minlat) {
                if (min > (double) element) {
                    min = (double) element;
                }

            }

            double maxlonn = 0;
            for (Object element : maxlon) {
                if (maxlonn < (double) element) {
                    maxlonn = (double) element;
                }

            }
            double minlonn = 43.9206992;

            for (Object element : minlon) {
                if (minlonn > (double) element) {
                    minlonn = (double) element;
                }

            }

            System.out.println(max);
            System.out.println(min);

            System.out.println(maxlonn);
            System.out.print(minlonn);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return (ArrayList<PointDintereData>) ListBat;
    }

    public ArrayList<Relation> parserMember(String nom_ville,String element) {
        List ListMemeber = null;
        List ListRelation = null;
        String dir;
        dir = repertoireCourant();
        try {
            File inputFile = new File(dir+"/région de la france/"+nom_ville+"/"+element);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();
            System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
            NodeList nList = doc.getElementsByTagName("relation");


            ListRelation = new ArrayList<Relation>();

            List maxlat = new ArrayList<Double>();
            List minlat = new ArrayList<Double>();

            List maxlon = new ArrayList<Double>();
            List minlon = new ArrayList<Double>();





            System.out.println("----------------------------");
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Relation relation = null;
                ListMemeber = new ArrayList<Member>();

                Node nNode = nList.item(temp);
                ListMemeber = new ArrayList<Member>();
                System.out.println("\nCurrent Element :" + nNode.getNodeName());
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;


                    System.out.println(eElement.getAttribute("id"));
                    relation = new Relation(Long.parseLong(eElement.getAttribute("id")));
                    relation.setId(Long.parseLong(eElement.getAttribute("id")));
                    relation.setMinLat(Double.parseDouble(eElement.getElementsByTagName("bounds")
                            .item(0)
                            .getAttributes()
                            .getNamedItem("minlat")
                            .getNodeValue()));

                    System.out.println("MinLat" +
                            " : "
                            + eElement.getElementsByTagName("bounds")
                            .item(0)
                            .getAttributes()
                            .getNamedItem("minlat")
                            .getNodeValue());

                    relation.setMinLon(Double.parseDouble(eElement.getElementsByTagName("bounds")
                            .item(0)
                            .getAttributes()
                            .getNamedItem("minlon")
                            .getNodeValue()));

                    relation.setMaxLat(Double.parseDouble(eElement.getElementsByTagName("bounds")
                            .item(0)
                            .getAttributes()
                            .getNamedItem("maxlat")
                            .getNodeValue()));

                    relation.setMaxLon(Double.parseDouble(eElement.getElementsByTagName("bounds")
                            .item(0)
                            .getAttributes()
                            .getNamedItem("maxlon")
                            .getNodeValue()));

                    NodeList Listnd = eElement.getElementsByTagName("member");
                    for (int i = 0; i < Listnd.getLength(); i++) {

                        Node nNode1 = Listnd.item(i);
                        if (nNode1.getNodeType() == Node.ELEMENT_NODE) {
                            Element eElement2 = (Element) nNode1;
                            Member member = new Member(eElement2.getAttribute("type"),Long.parseLong(eElement2.getAttribute("ref")),eElement2.getAttribute("role"));
                            System.out.println("type : "
                                    + eElement2.getAttribute("type"));

                            member.setType(eElement2.getAttribute("type"));

                            System.out.println("role : "
                                    + eElement2.getAttribute("role"));

                            member.setRole(eElement2.getAttribute("role"));

                            System.out.println("ref : "
                                    + eElement2.getAttribute("ref"));

                            member.setRef(Long.parseLong(eElement2.getAttribute("ref")));

                            NodeList Listnd1 = eElement2.getElementsByTagName("nd");
                            for (int j = 0; j < Listnd1.getLength(); j++) {
                                Node nNode2 = Listnd1.item(j);
                                if (nNode2.getNodeType() == Node.ELEMENT_NODE) {
                                    Element eElement3 = (Element) nNode2;
                                    System.out.print("lat" + ":" + eElement3.getAttribute("lat")+"/");
                                    member.setLat(Double.parseDouble(eElement3.getAttribute("lat")));
                                    //System.out.println("lon" + ":" + eElement3.getAttribute("lon"));
                                    member.setLon(Double.parseDouble(eElement3.getAttribute("lon")));
                                }
                            }
                            ListMemeber.add(member);

                        }
                    }




                    }


                relation.setmember(ListRelation);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (ArrayList<Relation>) ListRelation;
    }

    private String repertoireCourant(){
        return System.getProperty("user.dir");
    }

    public static void main(String[] args) {
        ParseXml pars = new ParseXml();
        List <Relation> L = new ArrayList<Relation>();

        L = pars.parserMember("Avignon","batimentXml.xml");


    }
}