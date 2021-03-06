/*******************************************************************************
 * Copyright (c) 2011, 2012 Oracle and/or its affiliates. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 and Eclipse Distribution License v. 1.0
 * which accompanies this distribution.
 *
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * Contributors:
 *  - rbarkhouse - 08 November 2011 - 2.4 - Initial implementation
 ******************************************************************************/
package org.eclipse.persistence.testing.jaxb.externalizedmetadata.xmltype.factory;

import javax.xml.bind.annotation.XmlTransient;

public class EmployeeWithNonStaticFactoryMethod {

    public int id = -1;
    public String firstName = null;
    public String lastName = null;

    @XmlTransient
    public int fromFactoryMethod = -1;

    public EmployeeWithNonStaticFactoryMethod(boolean flag) {
        if (flag) {
            id = -2;
        }
    }

    public boolean equals(Object o) {
        EmployeeWithNonStaticFactoryMethod obj;
        try {
            obj = (EmployeeWithNonStaticFactoryMethod) o;
        } catch (ClassCastException cce) {
            return false;
        }
        if (id != obj.id) {
            return false;
        }
        if (!firstName.equals(obj.firstName)) {
            return false;
        }
        if (!lastName.equals(obj.lastName)) {
            return false;
        }
        if (fromFactoryMethod != obj.fromFactoryMethod) {
            return false;
        }
        return true;
    }

    public String toString() {
        String isFromFactoryMethod = fromFactoryMethod > 0 ? "Factory" : "--";
        return getClass().getSimpleName()
                + "@" + Integer.toHexString(hashCode())
                + " #" + id + "|" + firstName + "|" + lastName + "|" + isFromFactoryMethod;
    }

    public EmployeeWithNonStaticFactoryMethod getNewEmployee() {
        EmployeeWithNonStaticFactoryMethod emp = new EmployeeWithNonStaticFactoryMethod(false);
        emp.fromFactoryMethod = 1;
        return emp;
    }

}