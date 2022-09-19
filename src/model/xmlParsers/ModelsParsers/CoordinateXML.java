package model.xmlParsers.ModelsParsers;

import model.xmlParsers.XMLParser;
import model.Coordinates;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public final class CoordinateXML extends XMLParser<Coordinates> {
    private static final String path = "src\\xmlFiles\\Places.xml";
    private static final String TAG = "coordinates";
    private static final String ROOT_TAG = "Places";
    public CoordinateXML() {
        super(path);
    }

    @Override
    /** A HashMap for all Coordinates in Places.xml file. The HashMap is build with the id and the Coordinates indexed
     * for easier look up.
     * @return HashMap<id, Coordinate>
     * @throws SAXException
     * @throws TransformerException
     */
    public HashMap<String, Coordinates> getObjectsHashMap() throws ParserConfigurationException, IOException,
            SAXException, TransformerException {
        HashMap<String, Coordinates> coordinates = new HashMap<>();

        try {
            doc = getDocument();
        }catch (Exception e){
            File file;
            JFileChooser file_chooser = new JFileChooser();
            int i=file_chooser.showOpenDialog(new JFrame());
            if(i==JFileChooser.APPROVE_OPTION) {
                file = file_chooser.getSelectedFile();
                doc = getDocument(file);
                e.printStackTrace();
            }else{
                doc = builder.newDocument();
                createXMLFile(doc, ROOT_TAG, "Places.xml");
            }
        }
        NodeList nodeList = doc.getElementsByTagName(TAG);
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            Element elem = (Element) node;
            // Get the value of the ID attribute.
            String id = elem.getAttributes().getNamedItem("id").getNodeValue();
            Coordinates coord = new Coordinates(id);
            coordinates.put(id, getElementData(elem, coord));
        }
        return coordinates;
    }

    @Override
    /**
     * A Coordinate depending on the key sent by parameter or null if the Coordinate does not exists in XML File.
     * @param key
     * @return Coordinate
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     */
    public Coordinates getObject(String key) throws ParserConfigurationException, IOException, SAXException {
        Coordinates coordinate;
        doc = getDocument();

        NodeList nodeList = doc.getElementsByTagName(TAG);
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            Element elem = (Element) node;
            // Get the value of the ID attribute.
            String id = elem.getAttributes().getNamedItem("id").getNodeValue();
            if(id.equals(key)){
                coordinate = new Coordinates(id);
                coordinate = getElementData(elem, coordinate);

                return coordinate;
            }
        }
        return null;
    }

    @Override
    protected Coordinates getElementData(Element elem, Coordinates coordinate) {
        // Get the value of all sub-elements.
        String x = elem.getElementsByTagName("x").item(0).getChildNodes().item(0).getNodeValue();
        String y = elem.getElementsByTagName("y").item(0).getChildNodes().item(0).getNodeValue();
        coordinate.setX(Integer.parseInt(x));
        coordinate.setY(Integer.parseInt(y));

        return coordinate;
    }

    @Override
    public void insertElement(Coordinates coord) throws ParserConfigurationException, IOException, SAXException,
            TransformerException {
        doc = getDocument();
        Element root = (Element) doc.getFirstChild();//Busco el primer tag del file

        root.appendChild(setElementData(doc, coord));//Inserto el objeto en el tag

        //Elimino los espacios en blanco del elemento agregado
        removeEmptyText(root);
        //Creo el transformer
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        //Le doy indentado a la configuracion del transformer
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        //Creo la fuente DOM e inserto el DOM al file.
        DOMSource source = new DOMSource(doc);
        StreamResult consoleResult = new StreamResult(new File(path));
        transformer.transform(source, consoleResult);
    }

    @Override
    public void eraseElement(String key) {

    }
    @Override
    protected Node setElementData(Document doc, Coordinates coord) {
        Element coordinate = doc.createElement(TAG);//Creo un tag para el objeto
        coordinate.setAttribute("id", coord.getId());//Asigno el atributo principal.

        //Asigno los subnodos y valores del objeto
        coordinate.appendChild(createSubElements(doc, "x", String.valueOf(coord.getX())));
        coordinate.appendChild(createSubElements(doc, "y", String.valueOf(coord.getY())));

        return coordinate;
    }
    @Override
    protected Node createSubElements(Document doc, String nodeName, String value){
        Element subElement = doc.createElement(nodeName);//Creo el tag de cada subnodo del Objeto

        subElement.appendChild(doc.createTextNode(value));//Le doy el valor al subnodo.

        return subElement;
    }

}
