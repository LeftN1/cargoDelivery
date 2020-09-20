package com.voroniuk.delivery.utils;

import com.voroniuk.delivery.dao.DBManager;
import com.voroniuk.delivery.dao.DBManagerImpl;
import com.voroniuk.delivery.entity.Country;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class AddCitiesFromXMLtoDB {

    public static void main(String[] args) throws IOException, SAXException, ParserConfigurationException {

        String xmlFileName = "xml/cities-ua.xml";

        cityParserSAX parser = new cityParserSAX(xmlFileName);
        parser.parse(false);

        Country Ukraine = parser.getCountry();

        DBManager manager = DBManagerImpl.getInstance();

        manager.addCountry(Ukraine);

    }

}
