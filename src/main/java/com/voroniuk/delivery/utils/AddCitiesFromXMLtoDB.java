package com.voroniuk.delivery.utils;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class AddCitiesFromXMLtoDB {

    public static void main(String[] args) throws IOException, SAXException, ParserConfigurationException {

        String xmlFileName = "xml/cities-uaNEW.xml";

        cityParserSAX parser = new cityParserSAX(xmlFileName);
        parser.addToDb(false);
    }

}
