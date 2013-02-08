/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.models.lng.types;

import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.models.mmxcore.UUIDObject;
import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>AVessel Set</b></em>'.
 * <!-- end-user-doc -->
 *
 *
 * @see com.mmxlabs.models.lng.types.TypesPackage#getAVesselSet()
 * @model abstract="true"
 * @generated
 */
public interface AVesselSet extends UUIDObject, NamedObject {

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model markedMany="true"
	 * @generated
	 */
	EList<AVessel> collect(EList<AVesselSet> marked);
} // AVesselSet
