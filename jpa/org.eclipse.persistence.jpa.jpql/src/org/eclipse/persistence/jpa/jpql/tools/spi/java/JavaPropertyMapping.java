/*******************************************************************************
 * Copyright (c) 2011, 2013 Oracle and/or its affiliates. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 and Eclipse Distribution License v. 1.0
 * which accompanies this distribution.
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 * and the Eclipse Distribution License is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * Contributors:
 *     Oracle - initial API and implementation
 *
 ******************************************************************************/
package org.eclipse.persistence.jpa.jpql.tools.spi.java;

import java.lang.reflect.Method;
import org.eclipse.persistence.jpa.jpql.tools.spi.IManagedType;

/**
 * The concrete implementation of {@link org.eclipse.persistence.jpa.jpql.tools.spi.IMapping IMapping}
 * that is wrapping the runtime representation of a property.
 *
 * @version 2.4
 * @since 2.4
 * @author Pascal Filion
 */
public class JavaPropertyMapping extends AbstractMethodMapping {

	/**
	 * Creates a new <code>JavaPropertyMapping</code>.
	 *
	 * @param parent The parent of this mapping
	 * @param method The Java {@link Method} wrapped by this mapping
	 */
	public JavaPropertyMapping(IManagedType parent, Method method) {
		super(parent, method);
	}
}