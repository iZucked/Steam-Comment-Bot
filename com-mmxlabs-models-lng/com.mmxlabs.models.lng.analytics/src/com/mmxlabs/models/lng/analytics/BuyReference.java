/**
 */
package com.mmxlabs.models.lng.analytics;

import com.mmxlabs.models.lng.cargo.LoadSlot;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Buy Reference</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.analytics.BuyReference#getSlot <em>Slot</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getBuyReference()
 * @model
 * @generated
 */
public interface BuyReference extends BuyOption {
	/**
	 * Returns the value of the '<em><b>Slot</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Slot</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Slot</em>' reference.
	 * @see #setSlot(LoadSlot)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getBuyReference_Slot()
	 * @model
	 * @generated
	 */
	LoadSlot getSlot();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.BuyReference#getSlot <em>Slot</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Slot</em>' reference.
	 * @see #getSlot()
	 * @generated
	 */
	void setSlot(LoadSlot value);

} // BuyReference
