package repositoryXML;

import entity.Pacient;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import repository.AbstractRepo;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;

public class PacientiRepoXML extends AbstractRepo<Pacient> {

    protected String filePath;

    public PacientiRepoXML(String filePath) throws IOException {
        this.filePath = filePath;
        loadData();
    }

    private void loadData() {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(filePath);
            doc.getDocumentElement().normalize();
            NodeList nodeList = doc.getElementsByTagName("Pacient");
            for (int itr = 0; itr < nodeList.getLength(); itr++)
            {
                Node node = nodeList.item(itr);
                if (node.getNodeType() == Node.ELEMENT_NODE)
                {
                    Element eElement = (Element) node;
                    String uniqueID = eElement.getElementsByTagName("uniqueID").item(0).getTextContent();
                    String nume = eElement.getElementsByTagName("nume").item(0).getTextContent();
                    String prenume = eElement.getElementsByTagName("prenume").item(0).getTextContent();
                    Integer varsta = Integer.parseInt(eElement.getElementsByTagName("varsta").item(0).getTextContent());
                    Pacient item = new Pacient(nume, prenume, varsta, uniqueID);
                    if (item != null) {
                        data.add(item);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        }
    }

    private void saveData() {
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();

            // Create root element
            Element rootElement = document.createElement("data");
            document.appendChild(rootElement);
            for(Pacient pacient: data) {
                Element pacientElement = document.createElement("Pacient");

                Element idElement = document.createElement("uniqueID");
                idElement.appendChild(document.createTextNode(String.valueOf(pacient.getUniqueID())));
                pacientElement.appendChild(idElement);

                Element numeElement = document.createElement("nume");
                numeElement.appendChild(document.createTextNode(String.valueOf(pacient.getNume())));
                pacientElement.appendChild(numeElement);

                Element prenumeElement = document.createElement("prenume");
                prenumeElement.appendChild(document.createTextNode(String.valueOf(pacient.getPrenume())));
                pacientElement.appendChild(prenumeElement);

                Element varstaElement = document.createElement("varsta");
                varstaElement.appendChild(document.createTextNode(String.valueOf(pacient.getVarsta())));
                pacientElement.appendChild(varstaElement);

                rootElement.appendChild(pacientElement);
            }
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(filePath);

            transformer.transform(source, result);
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        } catch (TransformerConfigurationException e) {
            throw new RuntimeException(e);
        } catch (TransformerException e) {
            throw new RuntimeException(e);
        }
    }

    public void add(Pacient item) throws IOException {
        super.add(item);
        saveData();
    }
    public void update(Pacient newItem, String id) throws IOException {
        super.update(newItem,id);
        saveData();
    }
    public void delete(String id) throws IOException {
        super.delete(id);
        saveData();
    }
}
