/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo;

import com.mmxlabs.models.lng.types.CargoDeliveryType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Discharge Slot</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.cargo.DischargeSlot#getCargo <em>Cargo</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.DischargeSlot#isFOBSale <em>FOB Sale</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.DischargeSlot#getPurchaseDeliveryType <em>Purchase Delivery Type</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getDischargeSlot()
 * @model
 * @generated
 */
public interface DischargeSlot extends Slot {

	/**
	 * Returns the value of the '<em><b>Cargo</b></em>' reference.
	 * It is bidirectional and its opposite is '{@link com.mmxlabs.models.lng.cargo.Cargo#getDischargeSlot <em>Discharge Slot</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Cargo</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Cargo</em>' reference.
	 * @see #setCargo(Cargo)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getDischargeSlot_Cargo()
	 * @see com.mmxlabs.models.lng.cargo.Cargo#getDischargeSlot
	 * @model opposite="dischargeSlot"
	 * @generated
	 */
	Cargo getCargo();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.DischargeSlot#getCargo <em>Cargo</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Cargo</em>' reference.
	 * @see #getCargo()
	 * @generated
	 */
	void setCargo(Cargo value);

	/**
	 * Returns the value of the '<em><b>FOB Sale</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>FOB Sale</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>FOB Sale</em>' attribute.
	 * @see #setFOBSale(boolean)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getDischargeSlot_FOBSale()
	 * @model
	 * @generated
	 */
	boolean isFOBSale();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.DischargeSlot#isFOBSale <em>FOB Sale</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>FOB Sale</em>' attribute.
	 * @see #isFOBSale()
	 * @generated
	 */
	void setFOBSale(boolean value);

	/**
	 * Returns the value of the '<em><b>Purchase Delivery Type</b></em>' attribute.
	 * The default value is <code>"false"</code>.
	 * The literals are from the enumeration {@link com.mmxlabs.models.lng.types.CargoDeliveryType}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Purchase Delivery Type</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Purchase Delivery Type</em>' attribute.
	 * @see com.mmxlabs.models.lng.types.CargoDeliveryType
	 * @see #isSetPurchaseDeliveryType()
	 * @see #unsetPurchaseDeliveryType()
	 * @see #setPurchaseDeliveryType(CargoDeliveryType)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getDischargeSlot_PurchaseDeliveryType()
	 * @model default="false" unsettable="true"
	 * @generated
	 */
	CargoDeliveryType getPurchaseDeliveryType();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.DischargeSlot#getPurchaseDeliveryType <em>Purchase Delivery Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Purchase Delivery Type</em>' attribute.
	 * @see com.mmxlabs.models.lng.types.CargoDeliveryType
	 * @see #isSetPurchaseDeliveryType()
	 * @see #unsetPurchaseDeliveryType()
	 * @see #getPurchaseDeliveryType()
	 * @generated
	 */
	void setPurchaseDeliveryType(CargoDeliveryType value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.cargo.DischargeSlot#getPurchaseDeliveryType <em>Purchase Delivery Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @see #isSetPurchaseDeliveryType()
	 * @see #getPurchaseDeliveryType()
	 * @see #setPurchaseDeliveryType(CargoDeliveryType)
	 * @generated
	 */
	void unsetPurchaseDeliveryType();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.cargo.DischargeSlot#getPurchaseDeliveryType <em>Purchase Delivery Type</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Purchase Delivery Type</em>' attribute is set.
	 * @see #unsetPurchaseDeliveryType()
	 * @see #getPurchaseDeliveryType()
	 * @see #setPurchaseDeliveryType(CargoDeliveryType)
	 * @generated
	 */
	boolean isSetPurchaseDeliveryType();
} // end of  DischargeSlot

// finish type fixing
