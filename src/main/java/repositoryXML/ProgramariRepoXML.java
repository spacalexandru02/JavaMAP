package repositoryXML;

import entity.Pacient;
import entity.Programare;
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
import java.io.IOException;

public class ProgramariRepoXML extends AbstractRepo<Programare> {

    protected String filePath;

    public ProgramariRepoXML(String filePath) throws IOException {
        this.filePath = filePath;
        loadData();
    }

    private void loadData() {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(filePath);
            doc.getDocumentElement().normalize();
            NodeList nodeList = doc.getElementsByTagName("Programare");
            for (int itr = 0; itr < nodeList.getLength(); itr++)
            {
                Node node = nodeList.item(itr);
                if (node.getNodeType() == Node.ELEMENT_NODE)
                {
                    Element eElement = (Element) node;
                    String uniqueID = eElement.getElementsByTagName("uniqueID").item(0).getTextContent();
                    String dataProgramare = eElement.getElementsByTagName("data").item(0).getTextContent();
                    String scopulProgramarii = eElement.getElementsByTagName("scopulProgramarii").item(0).getTextContent();
                    String pacientID = eElement.getElementsByTagName("pacientID").item(0).getTextContent();
                    Integer ora = Integer.parseInt(eElement.getElementsByTagName("ora").item(0).getTextContent());
                    Programare item = new Programare(dataProgramare, ora, scopulProgramarii, pacientID, uniqueID);
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
            for(Programare programare: data) {
                Element programareElement = document.createElement("Programare");

                Element idElement = document.createElement("uniqueID");
                idElement.appendChild(document.createTextNode(String.valueOf(programare.getUniqueID())));
                programareElement.appendChild(idElement);

                Element dataElement = document.createElement("data");
                dataElement.appendChild(document.createTextNode(String.valueOf(programare.getData())));
                programareElement.appendChild(dataElement);

                Element oraElement = document.createElement("ora");
                oraElement.appendChild(document.createTextNode(String.valueOf(programare.getOra())));
                programareElement.appendChild(oraElement);

                Element scopulElement = document.createElement("scopulProgramarii");
                scopulElement.appendChild(document.createTextNode(String.valueOf(programare.getScopulProgramarii())));
                programareElement.appendChild(scopulElement);

                Element pacientIDElement = document.createElement("pacientID");
                pacientIDElement.appendChild(document.createTextNode(String.valueOf(programare.getPacientID())));
                programareElement.appendChild(pacientIDElement);

                rootElement.appendChild(programareElement);
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

    public void add(Programare item) throws IOException {
        super.add(item);
        saveData();
    }
    public void update(Programare newItem, String id) throws IOException {
        super.update(newItem,id);
        saveData();
    }
    public void delete(String id) throws IOException {
        super.delete(id);
        saveData();
    }
}

