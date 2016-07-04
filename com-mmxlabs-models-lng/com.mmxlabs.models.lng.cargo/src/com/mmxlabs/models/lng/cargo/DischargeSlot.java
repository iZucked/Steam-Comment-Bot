/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
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
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.cargo.DischargeSlot#isFOBSale <em>FOB Sale</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.DischargeSlot#getPurchaseDeliveryType <em>Purchase Delivery Type</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.DischargeSlot#getTransferTo <em>Transfer To</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.DischargeSlot#getMinCvValue <em>Min Cv Value</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.DischargeSlot#getMaxCvValue <em>Max Cv Value</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getDischargeSlot()
 * @model
 * @generated
 */
public interface DischargeSlot extends Slot {

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
	 * The default value is <code>"Any"</code>.
	 * The literals are from the enumeration {@link com.mmxlabs.models.lng.types.CargoDeliveryType}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Purchase Delivery Type</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Purchase Delivery Type</em>' attribute.
	 * @see com.mmxlabs.models.lng.types.CargoDeliveryType
	 * @see #isSetPurchaseDeliveryType()
	 * @see #unsetPurchaseDeliveryType()
	 * @see #setPurchaseDeliveryType(CargoDeliveryType)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getDischargeSlot_PurchaseDeliveryType()
	 * @model default="Any" unsettable="true"
	 * @generated
	 */
	CargoDeliveryType getPurchaseDeliveryType();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.DischargeSlot#getPurchaseDeliveryType <em>Purchase Delivery Type</em>}' attribute.
	 * <!-- begin-user-doc -->
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
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Purchase Delivery Type</em>' attribute is set.
	 * @see #unsetPurchaseDeliveryType()
	 * @see #getPurchaseDeliveryType()
	 * @see #setPurchaseDeliveryType(CargoDeliveryType)
	 * @generated
	 */
	boolean isSetPurchaseDeliveryType();

	/**
	 * Returns the value of the '<em><b>Transfer To</b></em>' reference.
	 * It is bidirectional and its opposite is '{@link com.mmxlabs.models.lng.cargo.LoadSlot#getTransferFrom <em>Transfer From</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Transfer To</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Transfer To</em>' reference.
	 * @see #setTransferTo(LoadSlot)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getDischargeSlot_TransferTo()
	 * @see com.mmxlabs.models.lng.cargo.LoadSlot#getTransferFrom
	 * @model opposite="transferFrom"
	 * @generated
	 */
	LoadSlot getTransferTo();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.DischargeSlot#getTransferTo <em>Transfer To</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Transfer To</em>' reference.
	 * @see #getTransferTo()
	 * @generated
	 */
	void setTransferTo(LoadSlot value);

	/**
	 * Returns the value of the '<em><b>Min Cv Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Min Cv Value</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Min Cv Value</em>' attribute.
	 * @see #isSetMinCvValue()
	 * @see #unsetMinCvValue()
	 * @see #setMinCvValue(double)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getDischargeSlot_MinCvValue()
	 * @model unsettable="true"
	 *        annotation="http://www.mmxlabs.com/models/ui/numberFormat formatString='#0.###'"
	 * @generated
	 */
	double getMinCvValue();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.DischargeSlot#getMinCvValue <em>Min Cv Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Min Cv Value</em>' attribute.
	 * @see #isSetMinCvValue()
	 * @see #unsetMinCvValue()
	 * @see #getMinCvValue()
	 * @generated
	 */
	void setMinCvValue(double value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.cargo.DischargeSlot#getMinCvValue <em>Min Cv Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetMinCvValue()
	 * @see #getMinCvValue()
	 * @see #setMinCvValue(double)
	 * @generated
	 */
	void unsetMinCvValue();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.cargo.DischargeSlot#getMinCvValue <em>Min Cv Value</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Min Cv Value</em>' attribute is set.
	 * @see #unsetMinCvValue()
	 * @see #getMinCvValue()
	 * @see #setMinCvValue(double)
	 * @generated
	 */
	boolean isSetMinCvValue();

	/**
	 * Returns the value of the '<em><b>Max Cv Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Max Cv Value</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Max Cv Value</em>' attribute.
	 * @see #isSetMaxCvValue()
	 * @see #unsetMaxCvValue()
	 * @see #setMaxCvValue(double)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getDischargeSlot_MaxCvValue()
	 * @model unsettable="true"
	 *        annotation="http://www.mmxlabs.com/models/ui/numberFormat formatString='#0.###'"
	 * @generated
	 */
	double getMaxCvValue();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.DischargeSlot#getMaxCvValue <em>Max Cv Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Max Cv Value</em>' attribute.
	 * @see #isSetMaxCvValue()
	 * @see #unsetMaxCvValue()
	 * @see #getMaxCvValue()
	 * @generated
	 */
	void setMaxCvValue(double value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.cargo.DischargeSlot#getMaxCvValue <em>Max Cv Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetMaxCvValue()
	 * @see #getMaxCvValue()
	 * @see #setMaxCvValue(double)
	 * @generated
	 */
	void unsetMaxCvValue();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.cargo.DischargeSlot#getMaxCvValue <em>Max Cv Value</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Max Cv Value</em>' attribute is set.
	 * @see #unsetMaxCvValue()
	 * @see #getMaxCvValue()
	 * @see #setMaxCvValue(double)
	 * @generated
	 */
	boolean isSetMaxCvValue();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation"
	 * @generated
	 */
	double getSlotOrContractMinCv();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation"
	 * @generated
	 */
	double getSlotOrContractMaxCv();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation"
	 * @generated
	 */
	CargoDeliveryType getSlotOrContractDeliveryType();
} // end of  DischargeSlot

// finish type fixing
