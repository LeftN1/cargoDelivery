package com.voroniuk.delivery.utils;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

/**
 * Add cities to database using prepared xml file. Must be run once after database creation.
 *
 * @author M. Voroniuk
 */

public class AddCitiesFromXMLtoDB {

    public static void main(String[] args) throws IOException, SAXException, ParserConfigurationException {

        String xmlFileName = "xml/cities-ua.xml";

        cityParserSAX parser = new cityParserSAX(xmlFileName);
        parser.addToDb(false);
    }

}
