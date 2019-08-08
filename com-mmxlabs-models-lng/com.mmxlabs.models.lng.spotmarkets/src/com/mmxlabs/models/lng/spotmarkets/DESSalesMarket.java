/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.spotmarkets;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.types.AVesselSet;
import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>DES Sales Market</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.spotmarkets.DESSalesMarket#getNotionalPort <em>Notional Port</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.spotmarkets.DESSalesMarket#getDaysPadding <em>Days Padding</em>}</li>
 * </ul>
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
	 * Returns the value of the '<em><b>Days Padding</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Days Padding</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Days Padding</em>' attribute.
	 * @see #setDaysPadding(int)
	 * @see com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage#getDESSalesMarket_DaysPadding()
	 * @model
	 * @generated
	 */
	int getDaysPadding();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.spotmarkets.DESSalesMarket#getDaysPadding <em>Days Padding</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Days Padding</em>' attribute.
	 * @see #getDaysPadding()
	 * @generated
	 */
	void setDaysPadding(int value);

} // end of  DESSalesMarket

// finish type fixing
