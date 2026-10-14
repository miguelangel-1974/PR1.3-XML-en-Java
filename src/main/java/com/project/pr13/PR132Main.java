package com.project.pr13;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.project.pr13.format.AsciiTablePrinter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Classe principal que permet gestionar un fitxer XML de cursos amb opcions per llistar, afegir i eliminar alumnes, 
 * així com mostrar informació dels cursos i mòduls.
 * 
 * Aquesta classe inclou funcionalitats per interactuar amb un fitxer XML, executar operacions de consulta,
 * i realitzar modificacions en el contingut del fitxer.
 */
public class PR132Main {

    private final Path xmlFilePath;
    private static final Scanner scanner = new Scanner(System.in);

    /**
     * Constructor de la classe PR132Main.
     * 
     * @param xmlFilePath Ruta al fitxer XML que conté la informació dels cursos.
     */
    public PR132Main(Path xmlFilePath) {
        this.xmlFilePath = xmlFilePath;
    }

    /**
     * Mètode principal que inicia l'execució del programa.
     * 
     * @param args Arguments passats a la línia de comandament (no s'utilitzen en aquest programa).
     */
    public static void main(String[] args) {
        String userDir = System.getProperty("user.dir");
        Path xmlFilePath = Paths.get(userDir, "data", "pr13", "cursos.xml");

        PR132Main app = new PR132Main(xmlFilePath);
        app.executar();
    }

    /**
     * Executa el menú principal del programa fins que l'usuari decideixi sortir.
     */
    public void executar() {
        boolean exit = false;
        while (!exit) {
            mostrarMenu();
            System.out.print("Escull una opció: ");
            int opcio = scanner.nextInt();
            scanner.nextLine(); // Netegem el buffer del scanner
            exit = processarOpcio(opcio);
        }
    }

    /**
     * Processa l'opció seleccionada per l'usuari.
     * 
     * @param opcio Opció seleccionada al menú.
     * @return True si l'usuari decideix sortir del programa, false en cas contrari.
     */
    public boolean processarOpcio(int opcio) {
        String cursId;
        String nomAlumne;
        switch (opcio) {
            case 1:
                List<List<String>> cursos = llistarCursos();
                imprimirTaulaCursos(cursos);
                return false;
            case 2:
                System.out.print("Introdueix l'ID del curs per veure els seus mòduls: ");
                cursId = scanner.nextLine();
                List<List<String>> moduls = mostrarModuls(cursId);
                imprimirTaulaModuls(moduls);
                return false;
            case 3:
                System.out.print("Introdueix l'ID del curs per veure la llista d'alumnes: ");
                cursId = scanner.nextLine();
                List<String> alumnes = llistarAlumnes(cursId);
                imprimirLlistaAlumnes(alumnes);
                return false;
            case 4:
                System.out.print("Introdueix l'ID del curs on vols afegir l'alumne: ");
                cursId = scanner.nextLine();
                System.out.print("Introdueix el nom complet de l'alumne a afegir: ");
                nomAlumne = scanner.nextLine();
                afegirAlumne(cursId, nomAlumne);
                return false;
            case 5:
                System.out.print("Introdueix l'ID del curs on vols eliminar l'alumne: ");
                cursId = scanner.nextLine();
                System.out.print("Introdueix el nom complet de l'alumne a eliminar: ");
                nomAlumne = scanner.nextLine();
                eliminarAlumne(cursId, nomAlumne);
                return false;
            case 6:
                System.out.println("Sortint del programa...");
                return true;
            default:
                System.out.println("Opció no reconeguda. Si us plau, prova de nou.");
                return false;
        }
    }

    /**
     * Mostra el menú principal amb les opcions disponibles.
     */
    private void mostrarMenu() {
        System.out.println("\nMENÚ PRINCIPAL");
        System.out.println("1. Llistar IDs de cursos i tutors");
        System.out.println("2. Mostrar IDs i títols dels mòduls d'un curs");
        System.out.println("3. Llistar alumnes d’un curs");
        System.out.println("4. Afegir un alumne a un curs");
        System.out.println("5. Eliminar un alumne d'un curs");
        System.out.println("6. Sortir");
    }

    /**
     * Llegeix el fitxer XML i llista tots els cursos amb el seu tutor i nombre d'alumnes.
     * 
     * @return Llista amb la informació dels cursos (ID, tutor, nombre d'alumnes).
     */
    public List<List<String>> llistarCursos() {
        // *************** CODI PRÀCTICA **********************/
        List<List<String>> cursos = new ArrayList<>();
        try {
            // Carreguem el document amb el mètode que ja ens dona el professor
            Document doc = carregarDocumentXML(xmlFilePath);

            // Crea un objecte XPath
            XPath xPath = XPathFactory.newInstance().newXPath();

            // NODESET: tots els cursos del document
            NodeList llistaCursos = (NodeList) xPath.evaluate(
                    "/cursos/curs", doc, XPathConstants.NODESET);

            // Recorrem cada curs
            for (int i = 0; i < llistaCursos.getLength(); i++) {
                Node curs = llistaCursos.item(i);

                // Expressions relatives: s'avaluen des del curs actual (sense "/" inicial)
                String id = xPath.evaluate("@id", curs);
                String tutor = xPath.evaluate("tutor", curs);

                // NUMBER: count() retorna un Double
                Double total = (Double) xPath.evaluate(
                        "count(alumnes/alumne)", curs, XPathConstants.NUMBER);

                // Cada curs és una fila: [id, tutor, total d'alumnes]
                cursos.add(List.of(id, tutor, String.valueOf(total.intValue())));
            }
        } catch (XPathExpressionException e) {
            // Expressió XPath incorrecta (diapositiva 17)
            System.err.println("Expressió XPath incorrecta: " + e.getMessage());
        }
        return cursos;
    }

    /**
     * Imprimeix per consola una taula amb la informació dels cursos.
     * 
     * @param cursos Llista amb la informació dels cursos.
     */
    public void imprimirTaulaCursos(List<List<String>> cursos) {
        List<String> capçaleres = List.of("ID", "Tutor", "Total Alumnes");
        AsciiTablePrinter.imprimirTaula(capçaleres, cursos);
    }

    /**
     * Mostra els mòduls d'un curs especificat pel seu ID.
     * 
     * @param idCurs ID del curs del qual es volen veure els mòduls.
     * @return Llista amb la informació dels mòduls (ID, títol).
     */
    public List<List<String>> mostrarModuls(String idCurs) {
        // *************** CODI PRÀCTICA **********************/
        List<List<String>> moduls = new ArrayList<>();
        try {
            // Carreguem el document amb el mètode que ja ens dona el professor
            Document doc = carregarDocumentXML(xmlFilePath);

            // Crea un objecte XPath
            XPath xPath = XPathFactory.newInstance().newXPath();

            // NODESET: tots els mòduls del curs amb l'id indicat
            NodeList llistaModuls = (NodeList) xPath.evaluate(
                    "/cursos/curs[@id='" + idCurs + "']/moduls/modul",
                    doc, XPathConstants.NODESET);

            // Recorrem cada mòdul
            for (int i = 0; i < llistaModuls.getLength(); i++) {
                Node modul = llistaModuls.item(i);

                // Expressions relatives: s'avaluen des del mòdul actual (sense "/" inicial)
                String id = xPath.evaluate("@id", modul);
                String titol = xPath.evaluate("titol", modul);

                // Cada mòdul és una fila: [id, títol]
                moduls.add(List.of(id, titol));
            }
        } catch (XPathExpressionException e) {
            // Expressió XPath incorrecta (diapositiva 17)
            System.err.println("Expressió XPath incorrecta: " + e.getMessage());
        }
        return moduls;
    }

    /**
     * Imprimeix per consola una taula amb la informació dels mòduls.
     * 
     * @param moduls Llista amb la informació dels mòduls.
     */
    public void imprimirTaulaModuls(List<List<String>> moduls) {
        List<String> capçaleres = List.of("ID Mòdul", "Títol");
        AsciiTablePrinter.imprimirTaula(capçaleres, moduls);
    }

    /**
     * Llista els alumnes inscrits en un curs especificat pel seu ID.
     * 
     * @param idCurs ID del curs del qual es volen veure els alumnes.
     * @return Llista amb els noms dels alumnes.
     */
    public List<String> llistarAlumnes(String idCurs) {
        // *************** CODI PRÀCTICA **********************/
        List<String> alumnes = new ArrayList<>();
        try {
            // Carreguem el document amb el mètode que ja ens dona el professor
            Document doc = carregarDocumentXML(xmlFilePath);

            // Crea un objecte XPath
            XPath xPath = XPathFactory.newInstance().newXPath();

            // Expressió: tots els <alumne> del curs amb l'id indicat
            String expression = "/cursos/curs[@id='" + idCurs + "']/alumnes/alumne";

            // NODESET perquè esperem diversos nodes
            NodeList llistaAlumnes = (NodeList) xPath.evaluate(expression, doc, XPathConstants.NODESET);

            // Recorrem la llista i guardem el text de cada alumne
            for (int i = 0; i < llistaAlumnes.getLength(); i++) {
                alumnes.add(llistaAlumnes.item(i).getTextContent());
            }
        } catch (XPathExpressionException e) {
            // Expressió XPath incorrecta (diapositiva 17)
            System.err.println("Expressió XPath incorrecta: " + e.getMessage());
        }
        return alumnes;
    }

    /**
     * Imprimeix per consola la llista d'alumnes d'un curs.
     * 
     * @param alumnes Llista d'alumnes a imprimir.
     */
    public void imprimirLlistaAlumnes(List<String> alumnes) {
        System.out.println("Alumnes:");
        alumnes.forEach(alumne -> System.out.println("- " + alumne));
    }

    /**
     * Afegeix un alumne a un curs especificat pel seu ID.
     * 
     * @param idCurs ID del curs on es vol afegir l'alumne.
     * @param nomAlumne Nom de l'alumne a afegir.
     */
    public void afegirAlumne(String idCurs, String nomAlumne) {
        // *************** CODI PRÀCTICA **********************/
        try {
            // Carreguem el document amb el mètode que ja ens dona el professor
            Document doc = carregarDocumentXML(xmlFilePath);

            // Crea un objecte XPath
            XPath xPath = XPathFactory.newInstance().newXPath();

            // NODE: un sol node, o null si no n'hi ha cap (diapositiva 11)
            Node alumnes = (Node) xPath.evaluate(
                    "/cursos/curs[@id='" + idCurs + "']/alumnes",
                    doc, XPathConstants.NODE);

            // Si el curs no existeix, avisem i sortim
            if (alumnes == null) {
                System.out.println("No existeix cap curs amb l'ID " + idCurs);
                return;
            }

            // Crea l'element <alumne> amb el nom com a text (diapositiva 13)
            Element nou = doc.createElement("alumne");
            nou.setTextContent(nomAlumne);

            // L'afegeix com a fill de <alumnes>
            alumnes.appendChild(nou);

            // Els canvis són només en memòria: cal tornar a escriure el fitxer
            guardarDocumentXML(doc);
        } catch (XPathExpressionException e) {
            // Expressió XPath incorrecta (diapositiva 17)
            System.err.println("Expressió XPath incorrecta: " + e.getMessage());
        }
    }

    /**
     * Elimina un alumne d'un curs especificat pel seu ID.
     * 
     * @param idCurs ID del curs d'on es vol eliminar l'alumne.
     * @param nomAlumne Nom de l'alumne a eliminar.
     */
    public void eliminarAlumne(String idCurs, String nomAlumne) {
        // *************** CODI PRÀCTICA **********************/
            try {
            // Carreguem el document amb el mètode que ja ens dona el professor
            Document doc = carregarDocumentXML(xmlFilePath);

            // Crea un objecte XPath
            XPath xPath = XPathFactory.newInstance().newXPath();

            // NODE: l'alumne concret dins del curs, o null si no existeix (diapositiva 11)
            Node alumne = (Node) xPath.evaluate(
                    "/cursos/curs[@id='" + idCurs + "']/alumnes/alumne[text()='" + nomAlumne + "']",
                    doc, XPathConstants.NODE);

            // Si no es troba, avisem i sortim
            if (alumne == null) {
                System.out.println("No s'ha trobat l'alumne " + nomAlumne + " al curs " + idCurs);
                return;
            }

            // Un node s'esborra sempre des del seu pare (diapositiva 13)
            alumne.getParentNode().removeChild(alumne);

            // Els canvis són només en memòria: cal tornar a escriure el fitxer
            guardarDocumentXML(doc);
        } catch (XPathExpressionException e) {
            // Expressió XPath incorrecta (diapositiva 17)
            System.err.println("Expressió XPath incorrecta: " + e.getMessage());
        }
    }

    /**
     * Carrega el document XML des de la ruta especificada.
     * 
     * @param pathToXml Ruta del fitxer XML a carregar.
     * @return Document XML carregat.
     */
    private Document carregarDocumentXML(Path pathToXml) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            return builder.parse(pathToXml.toFile());
        } catch (Exception e) {
            throw new RuntimeException("Error en carregar el document XML.", e);
        }
    }

    /**
     * Guarda el document XML proporcionat en la ruta del fitxer original.
     * 
     * @param document Document XML a guardar.
     */
    private void guardarDocumentXML(Document document) {
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

            // Sense això, l'espai en blanc original es manté i el fitxer s'omple de línies buides
            eliminarEspaisEnBlanc(document);

            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(xmlFilePath.toFile());
            transformer.transform(source, result);
            System.out.println("El fitxer XML ha estat guardat amb èxit.");
        } catch (TransformerException e) {
            System.out.println("Error en guardar el fitxer XML.");
            e.printStackTrace();
        }
    }

    /**
     * Elimina, de manera recursiva, els nodes de text que només contenen espais, tabuladors
     * o salts de línia (l'indentació del fitxer original). Si no s'eliminen, en tornar a
     * escriure el document amb INDENT = "yes" el fitxer s'omple de línies en blanc.
     * És el mateix mètode trimWhitespace que s'explica a la teoria.
     *
     * @param node Node a partir del qual es fa la neteja (normalment, el Document).
     */
    private static void eliminarEspaisEnBlanc(Node node) {
        NodeList fills = node.getChildNodes();
        // Es recorre de darrere cap endavant perquè getChildNodes() és una llista «viva»
        for (int i = fills.getLength() - 1; i >= 0; i--) {
            Node fill = fills.item(i);
            if (fill.getNodeType() == Node.TEXT_NODE && fill.getTextContent().isBlank()) {
                node.removeChild(fill);
            } else {
                eliminarEspaisEnBlanc(fill);
            }
        }
    }
}
