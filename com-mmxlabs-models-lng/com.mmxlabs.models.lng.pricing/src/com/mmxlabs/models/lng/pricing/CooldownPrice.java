/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Cooldown Price</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.pricing.CooldownPrice#isLumpsum <em>Lumpsum</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getCooldownPrice()
 * @model
 * @generated
 */
public interface CooldownPrice extends PortsExpressionMap {

	/**
	 * Returns the value of the '<em><b>Lumpsum</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Lumpsum</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Lumpsum</em>' attribute.
	 * @see #setLumpsum(boolean)
	 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getCooldownPrice_Lumpsum()
	 * @model
	 * @generated
	 */
	boolean isLumpsum();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.pricing.CooldownPrice#isLumpsum <em>Lumpsum</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Lumpsum</em>' attribute.
	 * @see #isLumpsum()
	 * @generated
	 */
	void setLumpsum(boolean value);

} // end of  CooldownPrice

// finish type fixing
