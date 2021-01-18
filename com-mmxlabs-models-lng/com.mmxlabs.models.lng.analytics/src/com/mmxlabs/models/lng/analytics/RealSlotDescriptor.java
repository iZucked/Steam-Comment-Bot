/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.analytics;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Real Slot Descriptor</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.analytics.RealSlotDescriptor#getSlotName <em>Slot Name</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getRealSlotDescriptor()
 * @model
 * @generated
 */
public interface RealSlotDescriptor extends SlotDescriptor {
	/**
	 * Returns the value of the '<em><b>Slot Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Slot Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Slot Name</em>' attribute.
	 * @see #setSlotName(String)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getRealSlotDescriptor_SlotName()
	 * @model
	 * @generated
	 */
	String getSlotName();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.RealSlotDescriptor#getSlotName <em>Slot Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Slot Name</em>' attribute.
	 * @see #getSlotName()
	 * @generated
	 */
	void setSlotName(String value);

} // RealSlotDescriptor
