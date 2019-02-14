/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.analytics;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Open Slot Change</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.analytics.OpenSlotChange#getSlotDescriptor <em>Slot Descriptor</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getOpenSlotChange()
 * @model
 * @generated
 */
public interface OpenSlotChange extends Change {
	/**
	 * Returns the value of the '<em><b>Slot Descriptor</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Slot Descriptor</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Slot Descriptor</em>' containment reference.
	 * @see #setSlotDescriptor(SlotDescriptor)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getOpenSlotChange_SlotDescriptor()
	 * @model containment="true"
	 * @generated
	 */
	SlotDescriptor getSlotDescriptor();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.OpenSlotChange#getSlotDescriptor <em>Slot Descriptor</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Slot Descriptor</em>' containment reference.
	 * @see #getSlotDescriptor()
	 * @generated
	 */
	void setSlotDescriptor(SlotDescriptor value);

} // OpenSlotChange
