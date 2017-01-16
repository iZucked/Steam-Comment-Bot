/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics;
import org.eclipse.emf.common.util.EList;

import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.mmxcore.NamedObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Shipping Cost Plan</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.analytics.ShippingCostPlan#getVessel <em>Vessel</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.ShippingCostPlan#getNotionalDayRate <em>Notional Day Rate</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.ShippingCostPlan#getBaseFuelPrice <em>Base Fuel Price</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.ShippingCostPlan#getRows <em>Rows</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getShippingCostPlan()
 * @model
 * @generated
 */
public interface ShippingCostPlan extends NamedObject {
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
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getShippingCostPlan_Vessel()
	 * @model required="true"
	 * @generated
	 */
	Vessel getVessel();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.ShippingCostPlan#getVessel <em>Vessel</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Vessel</em>' reference.
	 * @see #getVessel()
	 * @generated
	 */
	void setVessel(Vessel value);

	/**
	 * Returns the value of the '<em><b>Notional Day Rate</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Notional Day Rate</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Notional Day Rate</em>' attribute.
	 * @see #setNotionalDayRate(int)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getShippingCostPlan_NotionalDayRate()
	 * @model required="true"
	 *        annotation="http://www.mmxlabs.com/models/ui/numberFormat unit='$/day'"
	 * @generated
	 */
	int getNotionalDayRate();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.ShippingCostPlan#getNotionalDayRate <em>Notional Day Rate</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Notional Day Rate</em>' attribute.
	 * @see #getNotionalDayRate()
	 * @generated
	 */
	void setNotionalDayRate(int value);

	/**
	 * Returns the value of the '<em><b>Base Fuel Price</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Base Fuel Price</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Base Fuel Price</em>' attribute.
	 * @see #setBaseFuelPrice(double)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getShippingCostPlan_BaseFuelPrice()
	 * @model required="true"
	 *        annotation="http://www.mmxlabs.com/models/ui/numberFormat unit='$/MT'"
	 * @generated
	 */
	double getBaseFuelPrice();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.ShippingCostPlan#getBaseFuelPrice <em>Base Fuel Price</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Base Fuel Price</em>' attribute.
	 * @see #getBaseFuelPrice()
	 * @generated
	 */
	void setBaseFuelPrice(double value);

	/**
	 * Returns the value of the '<em><b>Rows</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.analytics.ShippingCostRow}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Rows</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Rows</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getShippingCostPlan_Rows()
	 * @model containment="true"
	 * @generated
	 */
	EList<ShippingCostRow> getRows();

} // end of  ShippingCostPlan

// finish type fixing
