/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.spotmarkets;
import org.eclipse.emf.common.util.EList;

import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.types.APortSet;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>FOB Purchases Market</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.spotmarkets.FOBPurchasesMarket#getNotionalPort <em>Notional Port</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.spotmarkets.FOBPurchasesMarket#getCv <em>Cv</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.spotmarkets.FOBPurchasesMarket#getMarketPorts <em>Market Ports</em>}</li>
 * </ul>
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
	 * @model annotation="http://www.mmxlabs.com/models/ui/numberFormat unit='mmBtu/m\263' formatString='#0.###'"
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

	/**
	 * Returns the value of the '<em><b>Market Ports</b></em>' reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.types.APortSet}&lt;com.mmxlabs.models.lng.port.Port>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Market Ports</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Market Ports</em>' reference list.
	 * @see com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage#getFOBPurchasesMarket_MarketPorts()
	 * @model
	 * @generated
	 */
	EList<APortSet<Port>> getMarketPorts();

} // end of  FOBPurchasesMarket

// finish type fixing
