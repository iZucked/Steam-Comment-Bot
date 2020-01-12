/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.spotmarkets;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Spot Charter Market</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.spotmarkets.SpotCharterMarket#isEnabled <em>Enabled</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage#getSpotCharterMarket()
 * @model abstract="true"
 * @generated
 */
public interface SpotCharterMarket extends EObject {

	/**
	 * Returns the value of the '<em><b>Enabled</b></em>' attribute.
	 * The default value is <code>"true"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Enabled</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Enabled</em>' attribute.
	 * @see #setEnabled(boolean)
	 * @see com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage#getSpotCharterMarket_Enabled()
	 * @model default="true"
	 * @generated
	 */
	boolean isEnabled();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.spotmarkets.SpotCharterMarket#isEnabled <em>Enabled</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Enabled</em>' attribute.
	 * @see #isEnabled()
	 * @generated
	 */
	void setEnabled(boolean value);
} // SpotCharterMarket
