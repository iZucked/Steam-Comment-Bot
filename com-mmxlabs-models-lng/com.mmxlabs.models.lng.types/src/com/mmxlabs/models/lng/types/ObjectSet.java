/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.types;

import org.eclipse.emf.common.util.EList;

import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.models.mmxcore.UUIDObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Object Set</b></em>'.
 * <!-- end-user-doc -->
 *
 *
 * @see com.mmxlabs.models.lng.types.TypesPackage#getObjectSet()
 * @model abstract="true"
 * @generated
 */
public interface ObjectSet<T extends ObjectSet<T, U>, U> extends UUIDObject, NamedObject {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model markedMany="true"
	 * @generated
	 */
	EList<U> collect(EList<T> marked);

} // ObjectSet
