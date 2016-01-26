/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.commercial;

import com.mmxlabs.models.lng.types.CargoDeliveryType;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Purchase Contract</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.commercial.PurchaseContract#getCargoCV <em>Cargo CV</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.PurchaseContract#getSalesDeliveryType <em>Sales Delivery Type</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getPurchaseContract()
 * @model
 * @generated
 */
public interface PurchaseContract extends Contract {

	/**
	 * Returns the value of the '<em><b>Cargo CV</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Cargo CV</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Cargo CV</em>' attribute.
	 * @see #isSetCargoCV()
	 * @see #unsetCargoCV()
	 * @see #setCargoCV(double)
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getPurchaseContract_CargoCV()
	 * @model unsettable="true"
	 *        annotation="http://www.mmxlabs.com/models/ui/numberFormat unit='mmBtu/m\263' formatString='#0.###'"
	 * @generated
	 */
	double getCargoCV();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.commercial.PurchaseContract#getCargoCV <em>Cargo CV</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Cargo CV</em>' attribute.
	 * @see #isSetCargoCV()
	 * @see #unsetCargoCV()
	 * @see #getCargoCV()
	 * @generated
	 */
	void setCargoCV(double value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.commercial.PurchaseContract#getCargoCV <em>Cargo CV</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetCargoCV()
	 * @see #getCargoCV()
	 * @see #setCargoCV(double)
	 * @generated
	 */
	void unsetCargoCV();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.commercial.PurchaseContract#getCargoCV <em>Cargo CV</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Cargo CV</em>' attribute is set.
	 * @see #unsetCargoCV()
	 * @see #getCargoCV()
	 * @see #setCargoCV(double)
	 * @generated
	 */
	boolean isSetCargoCV();

	/**
	 * Returns the value of the '<em><b>Sales Delivery Type</b></em>' attribute.
	 * The default value is <code>"Any"</code>.
	 * The literals are from the enumeration {@link com.mmxlabs.models.lng.types.CargoDeliveryType}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Sales Delivery Type</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Sales Delivery Type</em>' attribute.
	 * @see com.mmxlabs.models.lng.types.CargoDeliveryType
	 * @see #setSalesDeliveryType(CargoDeliveryType)
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getPurchaseContract_SalesDeliveryType()
	 * @model default="Any" required="true"
	 * @generated
	 */
	CargoDeliveryType getSalesDeliveryType();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.commercial.PurchaseContract#getSalesDeliveryType <em>Sales Delivery Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Sales Delivery Type</em>' attribute.
	 * @see com.mmxlabs.models.lng.types.CargoDeliveryType
	 * @see #getSalesDeliveryType()
	 * @generated
	 */
	void setSalesDeliveryType(CargoDeliveryType value);
} // end of  PurchaseContract

// finish type fixing
