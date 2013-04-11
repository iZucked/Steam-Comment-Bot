/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.types;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>AVessel</b></em>'.
 * <!-- end-user-doc -->
 *
 *
 * @see com.mmxlabs.models.lng.types.TypesPackage#getAVessel()
 * @model abstract="true"
 *        annotation="http://www.mmxlabs.com/mmxcore/1/MMXCore generatedType='com.mmxlabs.models.lng.fleet.Vessel'"
 * @generated
 */
public interface AVessel extends AVesselSet {

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model markedMany="true"
	 *        annotation="http://www.eclipse.org/emf/2002/GenModel body='if (marked.contains(this)) {\n\treturn org.eclipse.emf.common.util.ECollections.emptyEList();\n} else {\n\tmarked.add(this);\n\treturn (EList<AVessel>) org.eclipse.emf.common.util.ECollections.singletonEList((AVessel)this);\n}\n'"
	 * @generated
	 */
	EList<AVessel> collect(EList<AVesselSet> marked);
} // AVessel
