/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing;
import org.eclipse.emf.common.util.EList;

import com.mmxlabs.models.mmxcore.MMXObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Fleet Cost Model</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.pricing.FleetCostModel#getCharterCosts <em>Charter Costs</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.FleetCostModel#getBaseFuelPrices <em>Base Fuel Prices</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getFleetCostModel()
 * @model
 * @generated
 */
public interface FleetCostModel extends MMXObject {
	/**
	 * Returns the value of the '<em><b>Charter Costs</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.pricing.CharterCostModel}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Charter Costs</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Charter Costs</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getFleetCostModel_CharterCosts()
	 * @model containment="true"
	 * @generated
	 */
	EList<CharterCostModel> getCharterCosts();

	/**
	 * Returns the value of the '<em><b>Base Fuel Prices</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.pricing.BaseFuelCost}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Base Fuel Prices</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Base Fuel Prices</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getFleetCostModel_BaseFuelPrices()
	 * @model containment="true"
	 * @generated
	 */
	EList<BaseFuelCost> getBaseFuelPrices();

} // end of  FleetCostModel

// finish type fixing
