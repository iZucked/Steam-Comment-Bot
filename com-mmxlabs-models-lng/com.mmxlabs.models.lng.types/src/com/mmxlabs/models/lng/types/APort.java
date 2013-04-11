/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.types;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>APort</b></em>'.
 * <!-- end-user-doc -->
 *
 *
 * @see com.mmxlabs.models.lng.types.TypesPackage#getAPort()
 * @model abstract="true"
 *        annotation="http://www.mmxlabs.com/mmxcore/1/MMXCore generatedType='com.mmxlabs.models.lng.port.Port'"
 * @generated
 */
public interface APort extends APortSet {

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model markedMany="true"
	 *        annotation="http://www.eclipse.org/emf/2002/GenModel body='if (marked.contains(this)) {\n\treturn org.eclipse.emf.common.util.ECollections.emptyEList();\n} else {\n\tmarked.add(this);\n\treturn (EList<APort>) org.eclipse.emf.common.util.ECollections.singletonEList((APort)this);\n}\n'"
	 * @generated
	 */
	EList<APort> collect(EList<APortSet> marked);
} // APort
