/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing;
import com.mmxlabs.models.lng.fleet.BaseFuel;
import com.mmxlabs.models.mmxcore.MMXObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Base Fuel Cost</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.pricing.BaseFuelCost#getFuel <em>Fuel</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.BaseFuelCost#getIndex <em>Index</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getBaseFuelCost()
 * @model
 * @generated
 */
public interface BaseFuelCost extends MMXObject {
	/**
	 * Returns the value of the '<em><b>Fuel</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Fuel</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Fuel</em>' reference.
	 * @see #setFuel(BaseFuel)
	 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getBaseFuelCost_Fuel()
	 * @model required="true"
	 * @generated
	 */
	BaseFuel getFuel();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.pricing.BaseFuelCost#getFuel <em>Fuel</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Fuel</em>' reference.
	 * @see #getFuel()
	 * @generated
	 */
	void setFuel(BaseFuel value);

	/**
	 * Returns the value of the '<em><b>Index</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Index</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Index</em>' reference.
	 * @see #setIndex(BaseFuelIndex)
	 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getBaseFuelCost_Index()
	 * @model required="true"
	 * @generated
	 */
	BaseFuelIndex getIndex();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.pricing.BaseFuelCost#getIndex <em>Index</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Index</em>' reference.
	 * @see #getIndex()
	 * @generated
	 */
	void setIndex(BaseFuelIndex value);

} // end of  BaseFuelCost

// finish type fixing
