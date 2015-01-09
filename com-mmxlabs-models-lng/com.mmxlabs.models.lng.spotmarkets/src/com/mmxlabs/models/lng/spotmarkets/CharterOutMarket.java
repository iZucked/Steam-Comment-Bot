/**
 */
package com.mmxlabs.models.lng.spotmarkets;

import com.mmxlabs.models.lng.pricing.CharterIndex;
import com.mmxlabs.models.mmxcore.NamedObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Charter Out Market</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.spotmarkets.CharterOutMarket#getCharterOutPrice <em>Charter Out Price</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.spotmarkets.CharterOutMarket#getMinCharterOutDuration <em>Min Charter Out Duration</em>}</li>
 * </ul>
 * </p>
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
	CharterIndex getCharterOutPrice();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.spotmarkets.CharterOutMarket#getCharterOutPrice <em>Charter Out Price</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Charter Out Price</em>' reference.
	 * @see #getCharterOutPrice()
	 * @generated
	 */
	void setCharterOutPrice(CharterIndex value);

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

} // CharterOutMarket
