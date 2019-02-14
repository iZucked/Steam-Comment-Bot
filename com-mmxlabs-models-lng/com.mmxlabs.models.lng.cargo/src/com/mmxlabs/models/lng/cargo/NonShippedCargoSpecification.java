/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.cargo;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Non Shiped Cargo Specification</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.cargo.NonShippedCargoSpecification#getSlotSpecifications <em>Slot Specifications</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getNonShippedCargoSpecification()
 * @model
 * @generated
 */
public interface NonShippedCargoSpecification extends EObject {
	/**
	 * Returns the value of the '<em><b>Slot Specifications</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.cargo.SlotSpecification}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Slot Specifications</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Slot Specifications</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getNonShippedCargoSpecification_SlotSpecifications()
	 * @model containment="true" resolveProxies="true"
	 * @generated
	 */
	EList<SlotSpecification> getSlotSpecifications();

} // NonShipedCargoSpecification
