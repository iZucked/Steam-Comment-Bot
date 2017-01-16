/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.analytics;

import com.mmxlabs.models.lng.fleet.VesselClass;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Round Trip Shipping Option</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.analytics.RoundTripShippingOption#getVesselClass <em>Vessel Class</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.RoundTripShippingOption#getHireCost <em>Hire Cost</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getRoundTripShippingOption()
 * @model
 * @generated
 */
public interface RoundTripShippingOption extends ShippingOption {
	/**
	 * Returns the value of the '<em><b>Vessel Class</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Vessel Class</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Vessel Class</em>' reference.
	 * @see #setVesselClass(VesselClass)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getRoundTripShippingOption_VesselClass()
	 * @model
	 * @generated
	 */
	VesselClass getVesselClass();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.RoundTripShippingOption#getVesselClass <em>Vessel Class</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Vessel Class</em>' reference.
	 * @see #getVesselClass()
	 * @generated
	 */
	void setVesselClass(VesselClass value);

	/**
	 * Returns the value of the '<em><b>Hire Cost</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Hire Cost</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Hire Cost</em>' attribute.
	 * @see #setHireCost(String)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getRoundTripShippingOption_HireCost()
	 * @model annotation="http://www.mmxlabs.com/models/pricing/expressionType type='charter'"
	 * @generated
	 */
	String getHireCost();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.RoundTripShippingOption#getHireCost <em>Hire Cost</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Hire Cost</em>' attribute.
	 * @see #getHireCost()
	 * @generated
	 */
	void setHireCost(String value);

} // RoundTripShippingOption
