/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo;
import com.mmxlabs.models.lng.types.ASpotMarket;
import com.mmxlabs.models.mmxcore.MMXObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Spot Slot</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.cargo.SpotSlot#getMarket <em>Market</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getSpotSlot()
 * @model
 * @generated
 */
public interface SpotSlot extends MMXObject {
	/**
	 * Returns the value of the '<em><b>Market</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Market</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Market</em>' reference.
	 * @see #setMarket(ASpotMarket)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getSpotSlot_Market()
	 * @model required="true"
	 * @generated
	 */
	ASpotMarket getMarket();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.SpotSlot#getMarket <em>Market</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Market</em>' reference.
	 * @see #getMarket()
	 * @generated
	 */
	void setMarket(ASpotMarket value);

} // end of  SpotSlot

// finish type fixing
