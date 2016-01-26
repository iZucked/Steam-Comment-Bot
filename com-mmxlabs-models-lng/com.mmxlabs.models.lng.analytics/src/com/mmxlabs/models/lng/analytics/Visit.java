/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Visit</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.analytics.Visit#getPortCost <em>Port Cost</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getVisit()
 * @model
 * @generated
 */
public interface Visit extends CostComponent {
	/**
	 * Returns the value of the '<em><b>Port Cost</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Port Cost</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Port Cost</em>' attribute.
	 * @see #setPortCost(int)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getVisit_PortCost()
	 * @model required="true"
	 * @generated
	 */
	int getPortCost();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.Visit#getPortCost <em>Port Cost</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Port Cost</em>' attribute.
	 * @see #getPortCost()
	 * @generated
	 */
	void setPortCost(int value);

} // end of  Visit

// finish type fixing
