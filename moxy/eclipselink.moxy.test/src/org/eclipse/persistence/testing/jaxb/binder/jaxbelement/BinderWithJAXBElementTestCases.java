/*******************************************************************************
 * Copyright (c) 2011, 2012 Oracle and/or its affiliates. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 and Eclipse Distribution License v. 1.0
 * which accompanies this distribution.
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * Contributors:
 *     Matt MacIvor - 2.3
 ******************************************************************************/
package org.eclipse.persistence.testing.jaxb.binder.jaxbelement;

import java.io.File;
import java.io.StringReader;

import javax.xml.bind.Binder;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;

import junit.framework.TestCase;

import org.eclipse.persistence.jaxb.JAXBContextFactory;
import org.eclipse.persistence.platform.xml.XMLParser;
import org.eclipse.persistence.platform.xml.XMLPlatform;
import org.eclipse.persistence.platform.xml.XMLPlatformFactory;
import org.eclipse.persistence.testing.jaxb.JAXBXMLComparer;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class BinderWithJAXBElementTestCases extends TestCase {
    private XMLParser parser;

    public BinderWithJAXBElementTestCases() {
        XMLPlatform platform = XMLPlatformFactory.getInstance().getXMLPlatform();
        parser = platform.newXMLParser();
    }
    
    public void testAbsentNode() throws Exception {
        String xml = "<employee><id>123</id><!-- Comment 1 --><name>Matt</name></employee>";
        String controlSource = "org/eclipse/persistence/testing/jaxb/binder/jaxbelement/employee.xml";
        Document controlDocument = parser.parse(new File(controlSource));
        
        JAXBContext ctx = JAXBContextFactory.createContext(new Class[]{Employee.class}, null);
        
        Binder binder = ctx.createBinder();
        
        JAXBElement emp = binder.unmarshal(parser.parse(new StringReader(xml)), Employee.class);
        ((Employee)emp.getValue()).id = 456;
        binder.updateXML(emp);
        
        JAXBXMLComparer comparer = new JAXBXMLComparer();
        assertTrue("Marshalled document does not match the control document.", comparer.isNodeEqual(controlDocument, ((Node)binder.getXMLNode(emp)).getOwnerDocument()));
    }
}