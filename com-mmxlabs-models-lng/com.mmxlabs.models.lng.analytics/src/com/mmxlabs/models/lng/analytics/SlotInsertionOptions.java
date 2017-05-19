/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.analytics;

import com.mmxlabs.models.lng.cargo.Slot;

import com.mmxlabs.models.mmxcore.UUIDObject;
import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Slot Insertion Options</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.analytics.SlotInsertionOptions#getSlotsInserted <em>Slots Inserted</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.SlotInsertionOptions#getInsertionOptions <em>Insertion Options</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.SlotInsertionOptions#getExtraSlots <em>Extra Slots</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getSlotInsertionOptions()
 * @model annotation="http://www.mmxlabs.com/models/mmxcore/validation/NamedObject nonUniqueChildren='true'"
 * @generated
 */
public interface SlotInsertionOptions extends UUIDObject {
	/**
	 * Returns the value of the '<em><b>Slots Inserted</b></em>' reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.cargo.Slot}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Slots Inserted</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Slots Inserted</em>' reference list.
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getSlotInsertionOptions_SlotsInserted()
	 * @model
	 * @generated
	 */
	EList<Slot> getSlotsInserted();

	/**
	 * Returns the value of the '<em><b>Insertion Options</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.analytics.SlotInsertionOption}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Insertion Options</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Insertion Options</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getSlotInsertionOptions_InsertionOptions()
	 * @model containment="true"
	 * @generated
	 */
	EList<SlotInsertionOption> getInsertionOptions();

	/**
	 * Returns the value of the '<em><b>Extra Slots</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.cargo.Slot}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Extra Slots</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Extra Slots</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getSlotInsertionOptions_ExtraSlots()
	 * @model containment="true"
	 * @generated
	 */
	EList<Slot> getExtraSlots();

} // SlotInsertionOptions
