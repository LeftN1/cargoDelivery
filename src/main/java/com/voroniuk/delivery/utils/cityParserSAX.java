package com.voroniuk.delivery.utils;


import com.voroniuk.delivery.db.entity.SiteLocale;
import com.voroniuk.delivery.utils.constant.Constants;
import com.voroniuk.delivery.utils.constant.XML;
import com.voroniuk.delivery.db.entity.City;
import com.voroniuk.delivery.db.entity.Country;
import com.voroniuk.delivery.db.entity.Region;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;


public class cityParserSAX extends DefaultHandler {

    private static final SAXParserFactory FACTORY = SAXParserFactory.newInstance();


    private String xmlFileName;

    // current element name holder
    private String currentElement;

    // main container
    private Country country;

    private Region region;

    private City city;


    public cityParserSAX(String xmlFileName) {
        this.xmlFileName = xmlFileName;
    }

    /**
     * Parses XML document.
     *
     * @param validate If true validate XML document against its XML schema. With
     *                 this parameter it is possible make parser validating.
     */
    public void parse(boolean validate)
            throws ParserConfigurationException, SAXException, IOException {

        // XML document contains namespaces
        FACTORY.setNamespaceAware(true);

        // set validation
        if (validate) {
            FACTORY.setFeature(Constants.FEATURE_TURN_VALIDATION_ON, true);
            FACTORY.setFeature(Constants.FEATURE_TURN_SCHEMA_VALIDATION_ON, true);
        }

        SAXParser parser = FACTORY.newSAXParser();
        parser.parse(xmlFileName, this);
    }

    // ///////////////////////////////////////////////////////////
    // ERROR HANDLER IMPLEMENTATION
    // ///////////////////////////////////////////////////////////

    @Override
    public void error(org.xml.sax.SAXParseException e) throws SAXException {
        // if XML document not valid just throw exception
        throw e;
    }

    public Country getCountry() {
        return country;
    }

    // ///////////////////////////////////////////////////////////
    // CONTENT HANDLER IMPLEMENTATION
    // ///////////////////////////////////////////////////////////


    @Override
    public void startElement(String uri, String localName, String qName,
                             Attributes attributes) throws SAXException {

        currentElement = localName;

        if (XML.COUNTRY.equalsTo(currentElement)) {
            country = new Country();
            if (attributes.getValue(uri,XML.EN.value())!=null) {
                country.addName(SiteLocale.EN.getLocale(), attributes.getValue(uri, XML.EN.value()));
            }
            if (attributes.getValue(uri,XML.RU.value())!=null) {
                country.addName(SiteLocale.RU.getLocale(), attributes.getValue(uri, XML.RU.value()));
            }
            if (attributes.getValue(uri,XML.UK.value())!=null) {
                country.addName(SiteLocale.UA.getLocale(), attributes.getValue(uri, XML.UK.value()));
            }
            return;
        }

        if (XML.REGION.equalsTo(currentElement)) {
            region = new Region();
            region.setId(0);
            region.setCountry(getCountry());
            if (attributes.getValue(uri,XML.EN.value())!=null) {
                region.addName(SiteLocale.EN.getLocale(), attributes.getValue(uri, XML.EN.value()));
            }
            if (attributes.getValue(uri,XML.RU.value())!=null) {
                region.addName(SiteLocale.RU.getLocale(), attributes.getValue(uri, XML.RU.value()));
            }
            if (attributes.getValue(uri,XML.UK.value())!=null) {
                region.addName(SiteLocale.UA.getLocale(), attributes.getValue(uri, XML.UK.value()));
            }
            return;
        }

        if (XML.CITY.equalsTo(currentElement)) {
            city = new City();
            city.setId(0);
            city.setRegion(region);
            city.setLatitude(Double.parseDouble(attributes.getValue(uri, XML.LAT.value())));
			city.setLongitude(Double.parseDouble(attributes.getValue(uri, XML.LON.value())));

			if (attributes.getValue(uri,XML.EN.value())!=null) {
                city.addName(SiteLocale.EN.getLocale(), attributes.getValue(uri, XML.EN.value()));
            }
            if (attributes.getValue(uri,XML.RU.value())!=null) {
                city.addName(SiteLocale.RU.getLocale(), attributes.getValue(uri, XML.RU.value()));
            }
            if (attributes.getValue(uri,XML.UK.value())!=null) {
                city.addName(SiteLocale.UA.getLocale(), attributes.getValue(uri, XML.UK.value()));
            }
			return;
        }

    }

    @Override
    public void characters(char[] ch, int start, int length)
            throws SAXException {

        String elementText = new String(ch, start, length).trim();

        // return if content is empty
        if (elementText.isEmpty()) {
            return;
        }


    }

    @Override
    public void endElement(String uri, String localName, String qName)
            throws SAXException {

        if (XML.REGION.equalsTo(localName)) {
            country.addRegion(region);
            return;
        }

		if (XML.CITY.equalsTo(localName)) {
			region.addCity(city);
			return;
		}

    }

}