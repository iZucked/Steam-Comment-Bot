/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.commercial;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Netback Purchase Contract</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.commercial.NetbackPurchaseContract#getNotionalBallastParameters <em>Notional Ballast Parameters</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.NetbackPurchaseContract#getMargin <em>Margin</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.NetbackPurchaseContract#getFloorPrice <em>Floor Price</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getNetbackPurchaseContract()
 * @model
 * @generated
 */
public interface NetbackPurchaseContract extends PurchaseContract {
	/**
	 * Returns the value of the '<em><b>Notional Ballast Parameters</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.commercial.NotionalBallastParameters}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Notional Ballast Parameters</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Notional Ballast Parameters</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getNetbackPurchaseContract_NotionalBallastParameters()
	 * @model containment="true" required="true"
	 * @generated
	 */
	EList<NotionalBallastParameters> getNotionalBallastParameters();

	/**
	 * Returns the value of the '<em><b>Margin</b></em>' attribute.
	 * The default value is <code>"0"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Margin</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Margin</em>' attribute.
	 * @see #setMargin(double)
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getNetbackPurchaseContract_Margin()
	 * @model default="0" required="true"
	 * @generated
	 */
	double getMargin();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.commercial.NetbackPurchaseContract#getMargin <em>Margin</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Margin</em>' attribute.
	 * @see #getMargin()
	 * @generated
	 */
	void setMargin(double value);

	/**
	 * Returns the value of the '<em><b>Floor Price</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Floor Price</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Floor Price</em>' attribute.
	 * @see #setFloorPrice(double)
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getNetbackPurchaseContract_FloorPrice()
	 * @model
	 * @generated
	 */
	double getFloorPrice();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.commercial.NetbackPurchaseContract#getFloorPrice <em>Floor Price</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Floor Price</em>' attribute.
	 * @see #getFloorPrice()
	 * @generated
	 */
	void setFloorPrice(double value);

} // end of  NetbackPurchaseContract

// finish type fixing
