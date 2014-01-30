/**
 */
package com.mmxlabs.models.lng.actuals;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Discharge Actuals</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.actuals.DischargeActuals#getDeliveryType <em>Delivery Type</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.mmxlabs.models.lng.actuals.ActualsPackage#getDischargeActuals()
 * @model
 * @generated
 */
public interface DischargeActuals extends SlotActuals {

	/**
	 * Returns the value of the '<em><b>Delivery Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Delivery Type</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Delivery Type</em>' attribute.
	 * @see #setDeliveryType(String)
	 * @see com.mmxlabs.models.lng.actuals.ActualsPackage#getDischargeActuals_DeliveryType()
	 * @model
	 * @generated
	 */
	String getDeliveryType();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.actuals.DischargeActuals#getDeliveryType <em>Delivery Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Delivery Type</em>' attribute.
	 * @see #getDeliveryType()
	 * @generated
	 */
	void setDeliveryType(String value);
} // DischargeActuals
