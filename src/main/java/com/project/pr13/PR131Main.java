package com.project.pr13;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;

/**
 * Classe principal que crea un document XML amb informació de llibres i el guarda en un fitxer.
 * 
 * Aquesta classe permet construir un document XML, afegir elements i guardar-lo en un directori
 * especificat per l'usuari.
 */
public class PR131Main {

    private File dataDir;

    /**
     * Constructor de la classe PR131Main.
     * 
     * @param dataDir Directori on es guardaran els fitxers de sortida.
     */
    public PR131Main(File dataDir) {
        this.dataDir = dataDir;
    }

    /**
     * Retorna el directori de dades actual.
     * 
     * @return Directori de dades.
     */
    public File getDataDir() {
        return dataDir;
    }

    /**
     * Actualitza el directori de dades.
     * 
     * @param dataDir Nou directori de dades.
     */
    public void setDataDir(File dataDir) {
        this.dataDir = dataDir;
    }

    /**
     * Mètode principal que inicia l'execució del programa.
     * 
     * @param args Arguments passats a la línia de comandament (no s'utilitzen en aquest programa).
     */
    public static void main(String[] args) {
        String userDir = System.getProperty("user.dir");
        File dataDir = new File(userDir, "data" + File.separator + "pr13");

        PR131Main app = new PR131Main(dataDir);
        app.processarFitxerXML("biblioteca.xml");
    }

    /**
     * Processa el document XML creant-lo, guardant-lo en un fitxer i comprovant el directori de sortida.
     * 
     * @param filename Nom del fitxer XML a guardar.
     */
    public void processarFitxerXML(String filename) {
        if (comprovarIDirCrearDirectori(dataDir)) {
            Document doc = construirDocument();
            File fitxerSortida = new File(dataDir, filename);
            guardarDocument(doc, fitxerSortida);
        }
    }

    /**
     * Comprova si el directori existeix i, si no és així, el crea.
     * 
     * @param directori Directori a comprovar o crear.
     * @return True si el directori ja existeix o s'ha creat amb èxit, false en cas contrari.
     */
    private boolean comprovarIDirCrearDirectori(File directori) {
        if (!directori.exists()) {
            return directori.mkdirs();
        }
        return true;
    }

    /**
     * Crea un document XML amb l'estructura d'una biblioteca i afegeix un llibre amb els seus detalls.
     * Cal construir-lo amb DocumentBuilder. No és privat perquè els tests el puguin comprovar.
     * 
     * @return Document XML creat o null en cas d'error.
     */
    static Document construirDocument() {
        // *************** CODI PRÀCTICA **********************/
        try {
            // Crea una factoria de constructors de documents
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        
            // Crea un constructor de documents
            DocumentBuilder db = dbf.newDocumentBuilder();
        
            // Crea un nou document XML
            Document doc = db.newDocument();

            // Crea l'element root del document XML
            Element elmRoot = doc.createElement("biblioteca");
            // Afegeix l'element root al document XML
            doc.appendChild(elmRoot);

            // Crea l'element <llibre>, que sera fill de <biblioteca>
            Element elmLlibre = doc.createElement("llibre");
            elmRoot.appendChild(elmLlibre);
            
            // Crea un element i un node de text per a cada camp
            Element elmTitol = doc.createElement("titol");
            Text nodeTextTitol = doc.createTextNode("El viatge dels venturons");
            Element elmAutor = doc.createElement("autor");
            Text nodeTextAutor = doc.createTextNode("Joan Pla");
            Element elmAnyPublicacio = doc.createElement("anyPublicacio");
            Text nodeTextAnyPublicacio = doc.createTextNode("1998");
            Element elmEditorial = doc.createElement("editorial");
            Text nodeTextEditorial = doc.createTextNode("Edicions Mar");
            Element elmGenere = doc.createElement("genere");
            Text nodeTextGenere = doc.createTextNode("Aventura");
            Element elmPagines = doc.createElement("pagines");
            Text nodeTextPagines = doc.createTextNode("320");
            Element elmDisponible = doc.createElement("disponible");
            Text nodeTextDisponible = doc.createTextNode("true");
            
            // Afegeix el node de text a cada element
            elmTitol.appendChild(nodeTextTitol);
            elmAutor.appendChild(nodeTextAutor);
            elmAnyPublicacio.appendChild(nodeTextAnyPublicacio);
            elmEditorial.appendChild(nodeTextEditorial);
            elmGenere.appendChild(nodeTextGenere);
            elmPagines.appendChild(nodeTextPagines);
            elmDisponible.appendChild(nodeTextDisponible);
            
            // Afegeix els elements a <llibre> (no a l'arrel)
            elmLlibre.appendChild(elmTitol);
            elmLlibre.appendChild(elmAutor);
            elmLlibre.appendChild(elmAnyPublicacio);
            elmLlibre.appendChild(elmEditorial);
            elmLlibre.appendChild(elmGenere);
            elmLlibre.appendChild(elmPagines);
            elmLlibre.appendChild(elmDisponible);

            // Crea un atribut "id"
            Attr attrId = doc.createAttribute("id");
            // Estableix el valor de l'atribut "id"
            attrId.setValue("001");
            // Afegeix l'atribut "id" a l'element "to"
            elmLlibre.setAttributeNode(attrId);

            return doc;
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
       return null;
    }

    /**
     * Guarda el document XML proporcionat en el fitxer especificat.
     * 
     * @param doc Document XML a guardar.
     * @param fitxerSortida Fitxer de sortida on es guardarà el document.
     */
    private static void guardarDocument(Document doc, File fitxerSortida) {
        // *************** CODI PRÀCTICA **********************/
        try {
            // Crea una factoria de transformadors XSLT
            TransformerFactory transformerFactory = TransformerFactory.newInstance();

            // Crea un transformador XSLT
            Transformer transformer = transformerFactory.newTransformer();

            // Estableix la propietat OMIT_XML_DECLARATION a "no" per no ometre la declaració XML
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");

            // Estableix la propietat INDENT a "yes" per indentar el document XML resultant
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            // Opcional: nombre d'espais per nivell d'indentació
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

            // Crea una instància de DOMSource a partir del document XML
            DOMSource source = new DOMSource(doc);

            // Crea una instància de StreamResult a partir del fitxer de sortida
            StreamResult result = new StreamResult(fitxerSortida);

            // Transforma el document XML i escriu el resultat al fitxer
            transformer.transform(source, result);
        } catch (TransformerException e) {
            // Error en escriure el document (diapositiva 17: capturar l'excepció concreta)
            System.err.println("No es pot escriure " + fitxerSortida + ": " + e.getMessage());
        }
    }
}
