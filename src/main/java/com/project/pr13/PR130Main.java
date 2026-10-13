package com.project.pr13;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.project.pr13.format.PersonaFormatter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Classe principal que gestiona la lectura i el processament de fitxers XML per obtenir dades de persones.
 * 
 * Aquesta classe s'encarrega de llegir un fitxer XML que conté informació de persones,
 * processar-lo i mostrar les dades formatades per consola.
 */
public class PR130Main {

    private final File dataDir;

    /**
     * Constructor de la classe PR130Main.
     * 
     * @param dataDir Directori on es troben els fitxers de dades.
     */
    public PR130Main(File dataDir) {
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

        PR130Main app = new PR130Main(dataDir);
        app.processarFitxerXML("persones.xml");
    }

    /**
     * Processa un fitxer XML per obtenir la informació de les persones i imprimir-la.
     * 
     * @param filename Nom del fitxer XML a processar.
     */
    public void processarFitxerXML(String filename) {
        File inputFile = new File(dataDir, filename);
        Document doc = parseXML(inputFile);
        if (doc != null) {
            NodeList persones = doc.getElementsByTagName("persona");
            imprimirCapçaleres();
            imprimirDadesPersones(persones);
        }
    }

    /**
     * Llegeix un fitxer XML i el converteix en un objecte Document.
     * 
     * @param inputFile Fitxer XML a llegir.
     * @return Document XML carregat o null si hi ha hagut un error en la lectura.
     */
    public static Document parseXML(File inputFile) {
        // *************** CODI PRÀCTICA **********************/
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();

        // Crea un constructor de documents
        DocumentBuilder dBuilder;

        // Analitza el fitxer XML
        try {
            dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            return doc;
        } catch (ParserConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SAXException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Imprimeix per consola les capçaleres de la taula de persones.
     * Cal fer servir PersonaFormatter.getCapçaleres().
     */
    public static void imprimirCapçaleres() {
        // *************** CODI PRÀCTICA **********************/
        System.out.println(PersonaFormatter.getCapçaleres());
    }

    /**
     * Imprimeix per consola una línia per a cada persona del NodeList,
     * amb el format de PersonaFormatter.formatarPersona().
     *
     * @param persones NodeList amb els elements "persona" del document XML.
     */
    public static void imprimirDadesPersones(NodeList persones) {
        // *************** CODI PRÀCTICA **********************/
        for (int cnt = 0; cnt < persones.getLength(); cnt++) {
            // Obté l'estudiant actual
            Node nodeEstudiant = persones.item(cnt);
            // Comprova si l'estudiant actual és un element
            if (nodeEstudiant.getNodeType() == Node.ELEMENT_NODE) {
                // Converteix l'estudiant actual a un element
                Element elm = (Element) nodeEstudiant;
                // **Obté l'identificador de l'estudiant**
                NodeList elementNom = elm.getElementsByTagName("nom");
                NodeList elementCognom = elm.getElementsByTagName("cognom");
                NodeList elementEdat = elm.getElementsByTagName("edat");
                NodeList elementCiutat = elm.getElementsByTagName("ciutat");
                String nom = elementNom.item(0).getTextContent();
                String cognom = elementCognom.item(0).getTextContent();
                String edat = elementEdat.item(0).getTextContent();
                String ciutat = elementCiutat.item(0).getTextContent();
                System.out.println(PersonaFormatter.formatarPersona(nom, cognom, edat, ciutat));
            }
        }
    }
}
