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
 *     Blaise Doughan - 2.2 - initial implementation
 ******************************************************************************/
package org.eclipse.persistence.testing.jaxb.xmlmarshaller.locator;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;

public class TestValidationEventHandler implements ValidationEventHandler {

    private List<ValidationEvent> validationEvents;

    public TestValidationEventHandler() {
        validationEvents = new ArrayList<ValidationEvent>();
    }

    public List<ValidationEvent> getValidationEvents() {
        return validationEvents;
    }

    public boolean handleEvent(ValidationEvent ve) {
        validationEvents.add(ve);
        return true;
    }

}