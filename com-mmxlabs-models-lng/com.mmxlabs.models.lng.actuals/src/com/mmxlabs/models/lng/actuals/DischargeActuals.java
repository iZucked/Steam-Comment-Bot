/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
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
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.actuals.DischargeActuals#getDeliveryType <em>Delivery Type</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.actuals.DischargeActuals#getEndHeelM3 <em>End Heel M3</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.actuals.DischargeActuals#getEndHeelMMBTu <em>End Heel MMB Tu</em>}</li>
 * </ul>
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

	/**
	 * Returns the value of the '<em><b>End Heel M3</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>End Heel M3</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>End Heel M3</em>' attribute.
	 * @see #setEndHeelM3(double)
	 * @see com.mmxlabs.models.lng.actuals.ActualsPackage#getDischargeActuals_EndHeelM3()
	 * @model annotation="http://www.mmxlabs.com/models/ui/numberFormat unit='m\263' formatString='###,##0.###'"
	 * @generated
	 */
	double getEndHeelM3();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.actuals.DischargeActuals#getEndHeelM3 <em>End Heel M3</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>End Heel M3</em>' attribute.
	 * @see #getEndHeelM3()
	 * @generated
	 */
	void setEndHeelM3(double value);

	/**
	 * Returns the value of the '<em><b>End Heel MMB Tu</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>End Heel MMB Tu</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>End Heel MMB Tu</em>' attribute.
	 * @see #setEndHeelMMBTu(double)
	 * @see com.mmxlabs.models.lng.actuals.ActualsPackage#getDischargeActuals_EndHeelMMBTu()
	 * @model annotation="http://www.mmxlabs.com/models/ui/numberFormat unit='mmBtu' formatString='##,###,##0.###'"
	 * @generated
	 */
	double getEndHeelMMBTu();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.actuals.DischargeActuals#getEndHeelMMBTu <em>End Heel MMB Tu</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>End Heel MMB Tu</em>' attribute.
	 * @see #getEndHeelMMBTu()
	 * @generated
	 */
	void setEndHeelMMBTu(double value);
} // DischargeActuals
