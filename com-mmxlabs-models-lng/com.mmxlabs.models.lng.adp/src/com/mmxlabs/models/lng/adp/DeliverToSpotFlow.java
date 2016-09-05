/**
 */
package com.mmxlabs.models.lng.adp;

import com.mmxlabs.models.lng.spotmarkets.SpotMarket;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Deliver To Spot Flow</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.adp.DeliverToSpotFlow#getMarket <em>Market</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.adp.ADPPackage#getDeliverToSpotFlow()
 * @model
 * @generated
 */
public interface DeliverToSpotFlow extends DeliverToFlow {
	/**
	 * Returns the value of the '<em><b>Market</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Market</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Market</em>' reference.
	 * @see #setMarket(SpotMarket)
	 * @see com.mmxlabs.models.lng.adp.ADPPackage#getDeliverToSpotFlow_Market()
	 * @model
	 * @generated
	 */
	SpotMarket getMarket();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.adp.DeliverToSpotFlow#getMarket <em>Market</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Market</em>' reference.
	 * @see #getMarket()
	 * @generated
	 */
	void setMarket(SpotMarket value);

} // DeliverToSpotFlow
