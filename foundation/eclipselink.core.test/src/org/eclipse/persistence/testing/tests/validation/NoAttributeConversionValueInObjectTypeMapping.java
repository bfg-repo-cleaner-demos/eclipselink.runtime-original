/*******************************************************************************
 * Copyright (c) 1998, 2013 Oracle and/or its affiliates. All rights reserved.
 * This program and the accompanying materials are made available under the 
 * terms of the Eclipse Public License v1.0 and Eclipse Distribution License v. 1.0 
 * which accompanies this distribution. 
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at 
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * Contributors:
 *     Oracle - initial API and implementation from Oracle TopLink
 ******************************************************************************/  
package org.eclipse.persistence.testing.tests.validation;

import org.eclipse.persistence.exceptions.DescriptorException;
import org.eclipse.persistence.exceptions.EclipseLinkException;


public class NoAttributeConversionValueInObjectTypeMapping extends ExceptionTest {
    protected void setup() {
        getSession().getIdentityMapAccessor().initializeIdentityMaps();
        expectedException = DescriptorException.noAttributeValueConversionToFieldValueProvided(null, null);
    }

    protected void test() {
        try {
            getAbstractSession().writeObject(org.eclipse.persistence.testing.models.mapping.Employee.errorExample1());
        } catch (EclipseLinkException exception) {
            this.caughtException = exception;
        }
    }
}
