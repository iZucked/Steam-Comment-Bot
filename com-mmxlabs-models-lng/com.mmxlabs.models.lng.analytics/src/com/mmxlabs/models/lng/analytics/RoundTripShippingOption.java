/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.analytics;

import com.mmxlabs.models.lng.commercial.BaseLegalEntity;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.mmxcore.UUIDObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Round Trip Shipping Option</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.analytics.RoundTripShippingOption#getVessel <em>Vessel</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.RoundTripShippingOption#getHireCost <em>Hire Cost</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.RoundTripShippingOption#getEntity <em>Entity</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getRoundTripShippingOption()
 * @model
 * @generated
 */
public interface RoundTripShippingOption extends UUIDObject, ShippingOption {
	/**
	 * Returns the value of the '<em><b>Vessel</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Vessel</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Vessel</em>' reference.
	 * @see #setVessel(Vessel)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getRoundTripShippingOption_Vessel()
	 * @model
	 * @generated
	 */
	Vessel getVessel();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.RoundTripShippingOption#getVessel <em>Vessel</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Vessel</em>' reference.
	 * @see #getVessel()
	 * @generated
	 */
	void setVessel(Vessel value);

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

	/**
	 * Returns the value of the '<em><b>Entity</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Entity</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Entity</em>' reference.
	 * @see #setEntity(BaseLegalEntity)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getRoundTripShippingOption_Entity()
	 * @model
	 * @generated
	 */
	BaseLegalEntity getEntity();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.RoundTripShippingOption#getEntity <em>Entity</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Entity</em>' reference.
	 * @see #getEntity()
	 * @generated
	 */
	void setEntity(BaseLegalEntity value);

} // RoundTripShippingOption
