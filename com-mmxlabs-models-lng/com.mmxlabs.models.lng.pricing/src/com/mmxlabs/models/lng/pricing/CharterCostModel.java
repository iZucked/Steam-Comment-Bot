/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing;
import com.mmxlabs.models.lng.types.AVesselSet;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Charter Cost Model</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.pricing.CharterCostModel#getVessels <em>Vessels</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.CharterCostModel#getCharterInPrice <em>Charter In Price</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.CharterCostModel#getCharterOutPrice <em>Charter Out Price</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.CharterCostModel#getSpotCharterCount <em>Spot Charter Count</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getCharterCostModel()
 * @model
 * @generated
 */
public interface CharterCostModel extends EObject {
	/**
	 * Returns the value of the '<em><b>Vessels</b></em>' reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.types.AVesselSet}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Vessels</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Vessels</em>' reference list.
	 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getCharterCostModel_Vessels()
	 * @model
	 * @generated
	 */
	EList<AVesselSet> getVessels();

	/**
	 * Returns the value of the '<em><b>Charter In Price</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Charter In Price</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Charter In Price</em>' reference.
	 * @see #setCharterInPrice(Index)
	 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getCharterCostModel_CharterInPrice()
	 * @model type="com.mmxlabs.models.lng.pricing.Index<org.eclipse.emf.ecore.EIntegerObject>" required="true"
	 * @generated
	 */
	Index<Integer> getCharterInPrice();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.pricing.CharterCostModel#getCharterInPrice <em>Charter In Price</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Charter In Price</em>' reference.
	 * @see #getCharterInPrice()
	 * @generated
	 */
	void setCharterInPrice(Index<Integer> value);

	/**
	 * Returns the value of the '<em><b>Charter Out Price</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Charter Out Price</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Charter Out Price</em>' reference.
	 * @see #setCharterOutPrice(Index)
	 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getCharterCostModel_CharterOutPrice()
	 * @model type="com.mmxlabs.models.lng.pricing.Index<org.eclipse.emf.ecore.EIntegerObject>" required="true"
	 * @generated
	 */
	Index<Integer> getCharterOutPrice();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.pricing.CharterCostModel#getCharterOutPrice <em>Charter Out Price</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Charter Out Price</em>' reference.
	 * @see #getCharterOutPrice()
	 * @generated
	 */
	void setCharterOutPrice(Index<Integer> value);

	/**
	 * Returns the value of the '<em><b>Spot Charter Count</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Spot Charter Count</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Spot Charter Count</em>' reference.
	 * @see #setSpotCharterCount(Index)
	 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getCharterCostModel_SpotCharterCount()
	 * @model type="com.mmxlabs.models.lng.pricing.Index<org.eclipse.emf.ecore.EIntegerObject>" required="true"
	 * @generated
	 */
	Index<Integer> getSpotCharterCount();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.pricing.CharterCostModel#getSpotCharterCount <em>Spot Charter Count</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Spot Charter Count</em>' reference.
	 * @see #getSpotCharterCount()
	 * @generated
	 */
	void setSpotCharterCount(Index<Integer> value);

} // end of  CharterCostModel

// finish type fixing
