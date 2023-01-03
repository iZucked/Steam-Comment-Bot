/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.analytics;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Slot Descriptor</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.analytics.SlotDescriptor#getSlotType <em>Slot Type</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getSlotDescriptor()
 * @model abstract="true"
 * @generated
 */
public interface SlotDescriptor extends EObject {
	/**
	 * Returns the value of the '<em><b>Slot Type</b></em>' attribute.
	 * The literals are from the enumeration {@link com.mmxlabs.models.lng.analytics.SlotType}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Slot Type</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Slot Type</em>' attribute.
	 * @see com.mmxlabs.models.lng.analytics.SlotType
	 * @see #setSlotType(SlotType)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getSlotDescriptor_SlotType()
	 * @model
	 * @generated
	 */
	SlotType getSlotType();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.SlotDescriptor#getSlotType <em>Slot Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Slot Type</em>' attribute.
	 * @see com.mmxlabs.models.lng.analytics.SlotType
	 * @see #getSlotType()
	 * @generated
	 */
	void setSlotType(SlotType value);

} // SlotDescriptor
