/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.acesinc.ats.model.converter.candidate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import net.acesinc.ats.model.candidate.Candidate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 *
 * @author andrewserff
 */
public abstract class BaseXMLConverter extends CandidateModelConverter {
    private static final Logger log = LoggerFactory.getLogger(BaseXMLConverter.class);
    
    public static String preconditionXMLData(String data) {
        return data.replaceAll("&(?![#a-zA-Z0-9]+;)", "&amp;");
    }

    @Override
    public Candidate getModelFromData(String data) throws IOException {
        try {
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = builderFactory.newDocumentBuilder();
        
            //We need to make sure there are no & in the xml so we replace them all. 
            data = preconditionXMLData(data);
            
            Document doc = builder.parse(new InputSource(new java.io.StringReader(data)));
            if (doc != null) {
                Candidate c = getCandidateForDocument(doc);
                return c;
            } else {
                log.debug("Did not get a Document from parser");
            }
        } catch (SAXException ex) {
            throw new IOException("Exception parsing XML", ex);
        } catch (ParserConfigurationException ex) {
            throw new IOException("Exception setting up XML Parser", ex);
        }
        return null;
    }
    
    public abstract Candidate getCandidateForDocument(Document doc);
    
    protected Node getNode(String tagName, NodeList nodes) {
        for (int x = 0; x < nodes.getLength(); x++) {
            Node node = nodes.item(x);
            if (node.getNodeName().equalsIgnoreCase(tagName)) {
                return node;
            }
        }

        return null;
    }
    protected List<Node> getNodes(String tagName, NodeList nodes) {
        List<Node> matchingNodes = new ArrayList<>();
        for (int x = 0; x < nodes.getLength(); x++) {
            Node node = nodes.item(x);
            if (node.getNodeName().equalsIgnoreCase(tagName)) {
                matchingNodes.add(node);
            }
        }
        
        if (matchingNodes.isEmpty()) {
            return null;
        } else {
            return matchingNodes;
        }
    }

    protected String getNodeValue(Node node) {
        NodeList childNodes = node.getChildNodes();
        for (int x = 0; x < childNodes.getLength(); x++) {
            Node data = childNodes.item(x);
            if (data.getNodeType() == Node.TEXT_NODE) {
                return data.getNodeValue().trim();
            }
        }
        return null;
    }

    protected String getNodeValue(String tagName, NodeList nodes) {
        for (int x = 0; x < nodes.getLength(); x++) {
            Node node = nodes.item(x);
            if (node.getNodeName().equalsIgnoreCase(tagName)) {
                NodeList childNodes = node.getChildNodes();
                for (int y = 0; y < childNodes.getLength(); y++) {
                    Node data = childNodes.item(y);
                    if (data.getNodeType() == Node.TEXT_NODE) {
                        return data.getNodeValue().trim();
                    }
                }
            }
        }
        return null;
    }

    protected String getNodeAttr(String attrName, Node node) {
        NamedNodeMap attrs = node.getAttributes();
        for (int y = 0; y < attrs.getLength(); y++) {
            Node attr = attrs.item(y);
            if (attr.getNodeName().equalsIgnoreCase(attrName)) {
                return attr.getNodeValue().trim();
            }
        }
        return null;
    }

    protected String getNodeAttr(String tagName, String attrName, NodeList nodes) {
        for (int x = 0; x < nodes.getLength(); x++) {
            Node node = nodes.item(x);
            if (node.getNodeName().equalsIgnoreCase(tagName)) {
                NodeList childNodes = node.getChildNodes();
                for (int y = 0; y < childNodes.getLength(); y++) {
                    Node data = childNodes.item(y);
                    if (data.getNodeType() == Node.ATTRIBUTE_NODE) {
                        if (data.getNodeName().equalsIgnoreCase(attrName)) {
                            return data.getNodeValue().trim();
                        }
                    }
                }
            }
        }

        return null;
    }
}
