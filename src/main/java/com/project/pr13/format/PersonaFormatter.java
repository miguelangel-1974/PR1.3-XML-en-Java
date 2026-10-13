package com.project.pr13.format;

import javax.swing.text.Document;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Dona format de columnes alineades a les dades de les persones (exercici 0).
 *
 * Les columnes (nom, cognom, edat i ciutat) tenen una amplada de 8, 14, 5 i 9 caràcters
 * i se separen amb un espai, tal com es veu a la sortida esperada de l'enunciat.
 */
public class PersonaFormatter {

    /**
     * Retorna les dues línies de la capçalera, separades per un salt de línia (\n):
     * els noms de les columnes i, a sota, una línia de guions per a cada columna.
     *
     * @return Capçalera de la taula de persones.
     */
    public static String getCapçaleres() {
        // *************** CODI PRÀCTICA **********************/
        return String.format("%-9s%-15s%-6s%-9s%n", "Nom", "Cognom", "Edat", "Ciutat") 
            + String.format("%-9s%-15s%-6s%-9s", "-".repeat(8), "-".repeat(14), "-".repeat(5), "-".repeat(9));
    }

    /**
     * Retorna una línia amb les dades d'una persona, alineades sota la seva capçalera.
     *
     * @param nom Nom de la persona.
     * @param cognom Cognom de la persona.
     * @param edat Edat de la persona.
     * @param ciutat Ciutat de la persona.
     * @return Línia amb les dades formatades en columnes.
     */
    public static String formatarPersona(String nom, String cognom, String edat, String ciutat) {
        // *************** CODI PRÀCTICA **********************/
        return String.format("%-9s%-15s%-6s%-9s", nom, cognom, edat, ciutat); // Substitueix pel teu
    }
}
