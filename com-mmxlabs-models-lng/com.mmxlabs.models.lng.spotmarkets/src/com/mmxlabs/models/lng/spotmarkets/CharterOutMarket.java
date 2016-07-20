/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.spotmarkets;

import org.eclipse.emf.common.util.EList;

import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.types.APortSet;
import com.mmxlabs.models.mmxcore.NamedObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Charter Out Market</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.spotmarkets.CharterOutMarket#getCharterOutRate <em>Charter Out Rate</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.spotmarkets.CharterOutMarket#getMinCharterOutDuration <em>Min Charter Out Duration</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.spotmarkets.CharterOutMarket#getAvailablePorts <em>Available Ports</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage#getCharterOutMarket()
 * @model
 * @generated
 */
public interface CharterOutMarket extends SpotCharterMarket, NamedObject {
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
	 * @see com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage#getCharterOutMarket_CharterOutPrice()
	 * @model required="true"
	 * @generated
	 */
//	CharterIndex getCharterOutPrice();

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
	 * @see com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage#getCharterOutMarket_MinCharterOutDuration()
	 * @model required="true"
	 * @generated
	 */
	int getMinCharterOutDuration();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.spotmarkets.CharterOutMarket#getMinCharterOutDuration <em>Min Charter Out Duration</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Min Charter Out Duration</em>' attribute.
	 * @see #getMinCharterOutDuration()
	 * @generated
	 */
	void setMinCharterOutDuration(int value);

	/**
	 * Returns the value of the '<em><b>Available Ports</b></em>' reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.types.APortSet}&lt;com.mmxlabs.models.lng.port.Port>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Available Ports</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Available Ports</em>' reference list.
	 * @see com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage#getCharterOutMarket_AvailablePorts()
	 * @model
	 * @generated
	 */
	EList<APortSet<Port>> getAvailablePorts();

	/**
	 * Returns the value of the '<em><b>Charter Out Rate</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Charter Out Rate</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Charter Out Rate</em>' attribute.
	 * @see #setCharterOutRate(String)
	 * @see com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage#getCharterOutMarket_CharterOutRate()
	 * @model annotation="http://www.mmxlabs.com/models/ui/numberFormat unit='$/day'"
	 *        annotation="http://www.mmxlabs.com/models/pricing/expressionType type='charter'"
	 * @generated
	 */
	String getCharterOutRate();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.spotmarkets.CharterOutMarket#getCharterOutRate <em>Charter Out Rate</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Charter Out Rate</em>' attribute.
	 * @see #getCharterOutRate()
	 * @generated
	 */
	void setCharterOutRate(String value);

} // CharterOutMarket
