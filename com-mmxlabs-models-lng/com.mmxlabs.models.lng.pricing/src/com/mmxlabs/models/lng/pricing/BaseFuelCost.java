/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing;
import com.mmxlabs.models.lng.fleet.BaseFuel;

import com.mmxlabs.models.mmxcore.MMXObject;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Base Fuel Cost</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.pricing.BaseFuelCost#getFuels <em>Fuels</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.BaseFuelCost#getPrice <em>Price</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getBaseFuelCost()
 * @model
 * @generated
 */
public interface BaseFuelCost extends MMXObject {
	/**
	 * Returns the value of the '<em><b>Fuels</b></em>' reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.types.BaseFuel}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Fuels</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Fuels</em>' reference list.
	 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getBaseFuelCost_Fuels()
	 * @model
	 * @generated
	 */
	EList<BaseFuel> getFuels();

	/**
	 * Returns the value of the '<em><b>Price</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Price</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Price</em>' reference.
	 * @see #setPrice(Index)
	 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getBaseFuelCost_Price()
	 * @model type="com.mmxlabs.models.lng.pricing.Index<org.eclipse.emf.ecore.EDoubleObject>" required="true"
	 * @generated
	 */
	Index<Double> getPrice();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.pricing.BaseFuelCost#getPrice <em>Price</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Price</em>' reference.
	 * @see #getPrice()
	 * @generated
	 */
	void setPrice(Index<Double> value);

} // end of  BaseFuelCost

// finish type fixing
