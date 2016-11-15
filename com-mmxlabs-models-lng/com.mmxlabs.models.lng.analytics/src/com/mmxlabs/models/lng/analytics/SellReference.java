/**
 */
package com.mmxlabs.models.lng.analytics;

import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Sell Reference</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.analytics.SellReference#getSlot <em>Slot</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getSellReference()
 * @model
 * @generated
 */
public interface SellReference extends SellOption {
	/**
	 * Returns the value of the '<em><b>Slot</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Slot</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Slot</em>' reference.
	 * @see #setSlot(DischargeSlot)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getSellReference_Slot()
	 * @model
	 * @generated
	 */
	DischargeSlot getSlot();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.SellReference#getSlot <em>Slot</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Slot</em>' reference.
	 * @see #getSlot()
	 * @generated
	 */
	void setSlot(DischargeSlot value);

} // SellReference
