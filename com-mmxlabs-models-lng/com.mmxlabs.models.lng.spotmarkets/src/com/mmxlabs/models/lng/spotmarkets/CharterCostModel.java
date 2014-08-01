/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.models.lng.spotmarkets;
import org.eclipse.emf.common.util.EList;

import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.pricing.CharterIndex;
import com.mmxlabs.models.mmxcore.MMXObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Charter Cost Model</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.spotmarkets.CharterCostModel#isEnabled <em>Enabled</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.spotmarkets.CharterCostModel#getVesselClasses <em>Vessel Classes</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.spotmarkets.CharterCostModel#getCharterInPrice <em>Charter In Price</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.spotmarkets.CharterCostModel#getCharterOutPrice <em>Charter Out Price</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.spotmarkets.CharterCostModel#getSpotCharterCount <em>Spot Charter Count</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.spotmarkets.CharterCostModel#getMinCharterOutDuration <em>Min Charter Out Duration</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage#getCharterCostModel()
 * @model
 * @generated
 */
public interface CharterCostModel extends MMXObject {
	/**
	 * Returns the value of the '<em><b>Vessel Classes</b></em>' reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.fleet.VesselClass}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Vessel Classes</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Vessel Classes</em>' reference list.
	 * @see com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage#getCharterCostModel_VesselClasses()
	 * @model
	 * @generated
	 */
	EList<VesselClass> getVesselClasses();

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
	 * @see com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage#getCharterCostModel_CharterInPrice()
	 * @model required="true"
	 * @generated
	 */
	CharterIndex getCharterInPrice();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.spotmarkets.CharterCostModel#getCharterInPrice <em>Charter In Price</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Charter In Price</em>' reference.
	 * @see #getCharterInPrice()
	 * @generated
	 */
	void setCharterInPrice(CharterIndex value);

	/**
	 * Returns the value of the '<em><b>Charter Out Price</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Charter Out Price</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Charter Out Price</em>' reference.
	 * @see #setCharterOutPrice(CharterIndex)
	 * @see com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage#getCharterCostModel_CharterOutPrice()
	 * @model required="true"
	 * @generated
	 */
	CharterIndex getCharterOutPrice();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.spotmarkets.CharterCostModel#getCharterOutPrice <em>Charter Out Price</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Charter Out Price</em>' reference.
	 * @see #getCharterOutPrice()
	 * @generated
	 */
	void setCharterOutPrice(CharterIndex value);

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
	 * @see com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage#getCharterCostModel_SpotCharterCount()
	 * @model required="true"
	 * @generated
	 */
	int getSpotCharterCount();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.spotmarkets.CharterCostModel#getSpotCharterCount <em>Spot Charter Count</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Spot Charter Count</em>' attribute.
	 * @see #getSpotCharterCount()
	 * @generated
	 */
	void setSpotCharterCount(int value);

	/**
	 * Returns the value of the '<em><b>Min Charter Out Duration</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Min Charter Out Duration</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Min Charter Out Duration</em>' attribute.
	 * @see #setMinCharterOutDuration(int)
	 * @see com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage#getCharterCostModel_MinCharterOutDuration()
	 * @model required="true"
	 * @generated
	 */
	int getMinCharterOutDuration();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.spotmarkets.CharterCostModel#getMinCharterOutDuration <em>Min Charter Out Duration</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Min Charter Out Duration</em>' attribute.
	 * @see #getMinCharterOutDuration()
	 * @generated
	 */
	void setMinCharterOutDuration(int value);

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
	 * @see com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage#getCharterCostModel_Enabled()
	 * @model default="true"
	 * @generated
	 */
	boolean isEnabled();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.spotmarkets.CharterCostModel#isEnabled <em>Enabled</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Enabled</em>' attribute.
	 * @see #isEnabled()
	 * @generated
	 */
	void setEnabled(boolean value);

} // end of  CharterCostModel

// finish type fixing
