/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.spotmarkets;

import com.mmxlabs.models.lng.pricing.CharterIndex;
import com.mmxlabs.models.lng.types.VesselAssignmentType;
import com.mmxlabs.models.mmxcore.NamedObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Charter In Market</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.spotmarkets.CharterInMarket#getCharterInPrice <em>Charter In Price</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.spotmarkets.CharterInMarket#getSpotCharterCount <em>Spot Charter Count</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage#getCharterInMarket()
 * @model
 * @generated
 */
public interface CharterInMarket extends SpotCharterMarket, VesselAssignmentType, NamedObject {
	/**
	 * Returns the value of the '<em><b>Charter In Price</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Charter In Price</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Charter In Price</em>' reference.
	 * @see #setCharterInPrice(CharterIndex)
	 * @see com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage#getCharterInMarket_CharterInPrice()
	 * @model required="true"
	 * @generated
	 */
	CharterIndex getCharterInPrice();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.spotmarkets.CharterInMarket#getCharterInPrice <em>Charter In Price</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Charter In Price</em>' reference.
	 * @see #getCharterInPrice()
	 * @generated
	 */
	void setCharterInPrice(CharterIndex value);

	/**
	 * Returns the value of the '<em><b>Spot Charter Count</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Spot Charter Count</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Spot Charter Count</em>' attribute.
	 * @see #setSpotCharterCount(int)
	 * @see com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage#getCharterInMarket_SpotCharterCount()
	 * @model required="true"
	 * @generated
	 */
	int getSpotCharterCount();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.spotmarkets.CharterInMarket#getSpotCharterCount <em>Spot Charter Count</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Spot Charter Count</em>' attribute.
	 * @see #getSpotCharterCount()
	 * @generated
	 */
	void setSpotCharterCount(int value);

} // CharterInMarket
