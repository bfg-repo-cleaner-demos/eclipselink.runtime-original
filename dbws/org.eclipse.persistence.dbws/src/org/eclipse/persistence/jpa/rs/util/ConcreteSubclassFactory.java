/*******************************************************************************
 * Copyright (c) 2012 Oracle and/or its affiliates. All rights reserved.
 * This program and the accompanying materials are made available under the 
 * terms of the Eclipse Public License v1.0 and Eclipse Distribution License v. 1.0 
 * which accompanies this distribution. 
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at 
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 *  Contributors:
 *      gonural - initial 
 ******************************************************************************/
package org.eclipse.persistence.jpa.rs.util;

/**
 * Factory class to create concrete subclasses of abstract classes. 
 * 
 * This class is used in JPA-RS JAXB mappings to create InstantiationPolicy for abstract classes
 * @see PreLoginMappingAdapter
 * 
 * @author gonural
 *
 */
public class ConcreteSubclassFactory {

    @SuppressWarnings("rawtypes")
    private Class clazz = null;

    /**
     * Instantiates a new concrete subclass factory.
     *
     * @param clazz the clazz
     */
    @SuppressWarnings("rawtypes")
    public ConcreteSubclassFactory(Class clazz) {
        super();
        this.clazz = clazz;
    }

    /**
     * Creates a new ConcreteSubclass object.
     *
     * @return the object
     * @throws InstantiationException the instantiation exception
     * @throws IllegalAccessException the illegal access exception
     */
    public Object createConcreteSubclass() throws InstantiationException, IllegalAccessException {
        return clazz.newInstance();
    }
}
