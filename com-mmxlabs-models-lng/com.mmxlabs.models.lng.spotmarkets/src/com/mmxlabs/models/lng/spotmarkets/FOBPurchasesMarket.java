/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.spotmarkets;
import com.mmxlabs.models.lng.port.Port;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>FOB Purchases Market</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.spotmarkets.FOBPurchasesMarket#getNotionalPort <em>Notional Port</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.spotmarkets.FOBPurchasesMarket#getCv <em>Cv</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage#getFOBPurchasesMarket()
 * @model
 * @generated
 */
public interface FOBPurchasesMarket extends SpotMarket {
	/**
	 * Returns the value of the '<em><b>Notional Port</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Notional Port</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Notional Port</em>' reference.
	 * @see #setNotionalPort(Port)
	 * @see com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage#getFOBPurchasesMarket_NotionalPort()
	 * @model required="true"
	 * @generated
	 */
	Port getNotionalPort();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.spotmarkets.FOBPurchasesMarket#getNotionalPort <em>Notional Port</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Notional Port</em>' reference.
	 * @see #getNotionalPort()
	 * @generated
	 */
	void setNotionalPort(Port value);

	/**
	 * Returns the value of the '<em><b>Cv</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Cv</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Cv</em>' attribute.
	 * @see #setCv(double)
	 * @see com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage#getFOBPurchasesMarket_Cv()
	 * @model
	 * @generated
	 */
	double getCv();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.spotmarkets.FOBPurchasesMarket#getCv <em>Cv</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Cv</em>' attribute.
	 * @see #getCv()
	 * @generated
	 */
	void setCv(double value);

} // end of  FOBPurchasesMarket

// finish type fixing
