package org.acme.service;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;

public class DmnParser {
    private String nameSpace;
    private String name;

    public DmnParser(String dmnXml) {

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new ByteArrayInputStream(dmnXml.getBytes("UTF-8")));

            // Get the document element (dmn:definitions)
            Element root = doc.getDocumentElement();

            // Extract attributes
            String name = root.getAttribute("name");
            String namespace = root.getAttribute("namespace");

            this.name = name;
            this.nameSpace = namespace;
            System.out.println("Name: " + name);
            System.out.println("Namespace: " + namespace);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getName() {
        return name;
    }
    public String getNameSpace(){
        return nameSpace;
    }
}
