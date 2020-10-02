package com.voroniuk.delivery.utils;

import com.voroniuk.delivery.db.dao.CityDAO;
import com.voroniuk.delivery.db.entity.Country;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class AddCitiesFromXMLtoDB {

    public static void main(String[] args) throws IOException, SAXException, ParserConfigurationException {

        String xmlFileName = "xml/cities-ua.xml";

        cityParserSAX parser = new cityParserSAX(xmlFileName);
        parser.parse(false);

        Country Ukraine = parser.getCountry();

        CityDAO cityDAO = new CityDAO();


        cityDAO.addCountry(Ukraine);

    }

}
