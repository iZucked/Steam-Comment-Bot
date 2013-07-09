/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.spotmarkets;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.types.APortSet;
import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>DES Sales Market</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.spotmarkets.DESSalesMarket#getNotionalPort <em>Notional Port</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.spotmarkets.DESSalesMarket#getMarketPorts <em>Market Ports</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage#getDESSalesMarket()
 * @model
 * @generated
 */
public interface DESSalesMarket extends SpotMarket {
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
	 * @see com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage#getDESSalesMarket_NotionalPort()
	 * @model required="true"
	 * @generated
	 */
	Port getNotionalPort();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.spotmarkets.DESSalesMarket#getNotionalPort <em>Notional Port</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Notional Port</em>' reference.
	 * @see #getNotionalPort()
	 * @generated
	 */
	void setNotionalPort(Port value);

	/**
	 * Returns the value of the '<em><b>Market Ports</b></em>' reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.types.APortSet}&lt;com.mmxlabs.models.lng.port.Port>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Market Ports</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * @since 5.0
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Market Ports</em>' reference list.
	 * @see com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage#getDESSalesMarket_MarketPorts()
	 * @model
	 * @generated
	 */
	EList<APortSet<Port>> getMarketPorts();

} // end of  DESSalesMarket

// finish type fixing
