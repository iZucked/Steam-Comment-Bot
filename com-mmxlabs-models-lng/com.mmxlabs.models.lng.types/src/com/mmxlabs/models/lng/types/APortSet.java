/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.types;

import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.models.mmxcore.UUIDObject;
import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>APort Set</b></em>'.
 * <!-- end-user-doc -->
 *
 *
 * @see com.mmxlabs.models.lng.types.TypesPackage#getAPortSet()
 * @model abstract="true"
 * @generated
 */
public interface APortSet extends UUIDObject, NamedObject {

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model markedMany="true"
	 * @generated
	 */
	EList<APort> collect(EList<APortSet> marked);
} // APortSet
