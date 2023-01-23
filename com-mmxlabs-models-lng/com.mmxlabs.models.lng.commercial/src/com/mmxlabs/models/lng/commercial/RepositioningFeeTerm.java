/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.commercial;

import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.types.APortSet;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Repositioning Fee Term</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.commercial.RepositioningFeeTerm#getStartPorts <em>Start Ports</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getRepositioningFeeTerm()
 * @model
 * @generated
 */
public interface RepositioningFeeTerm extends EObject {
	/**
	 * Returns the value of the '<em><b>Start Ports</b></em>' reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.types.APortSet}<code>&lt;com.mmxlabs.models.lng.port.Port&gt;</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Start Ports</em>' reference list.
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getRepositioningFeeTerm_StartPorts()
	 * @model
	 * @generated
	 */
	EList<APortSet<Port>> getStartPorts();

} // RepositioningFeeTerm
