/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing;
import org.eclipse.emf.common.util.EList;

import com.mmxlabs.models.mmxcore.UUIDObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Model</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.pricing.PricingModel#getCommodityIndices <em>Commodity Indices</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.PricingModel#getCharterIndices <em>Charter Indices</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.PricingModel#getBaseFuelPrices <em>Base Fuel Prices</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getPricingModel()
 * @model
 * @generated
 */
public interface PricingModel extends UUIDObject {
	/**
	 * Returns the value of the '<em><b>Commodity Indices</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.pricing.CommodityIndex}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Commodity Indices</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Commodity Indices</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getPricingModel_CommodityIndices()
	 * @model containment="true"
	 * @generated
	 */
	EList<CommodityIndex> getCommodityIndices();

	/**
	 * Returns the value of the '<em><b>Charter Indices</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.pricing.CharterIndex}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Charter Indices</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Charter Indices</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getPricingModel_CharterIndices()
	 * @model containment="true"
	 * @generated
	 */
	EList<CharterIndex> getCharterIndices();

	/**
	 * Returns the value of the '<em><b>Base Fuel Prices</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.pricing.BaseFuelIndex}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Base Fuel Prices</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Base Fuel Prices</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getPricingModel_BaseFuelPrices()
	 * @model containment="true"
	 * @generated
	 */
	EList<BaseFuelIndex> getBaseFuelPrices();

} // end of  PricingModel

// finish type fixing
