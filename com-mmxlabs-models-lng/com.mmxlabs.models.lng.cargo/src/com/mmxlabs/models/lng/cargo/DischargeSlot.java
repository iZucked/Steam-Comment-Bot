/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo;

import com.mmxlabs.models.lng.commercial.SalesContract;
import com.mmxlabs.models.lng.types.CargoDeliveryType;
import com.mmxlabs.models.lng.types.FOBSaleDealType;

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
 *   <li>{@link com.mmxlabs.models.lng.cargo.DischargeSlot#getFobSaleDealType <em>Fob Sale Deal Type</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.DischargeSlot#isHeelCarry <em>Heel Carry</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getDischargeSlot()
 * @model annotation="http://www.mmxlabs.com/models/featureOverride"
 * @generated
 */
public interface DischargeSlot extends Slot<SalesContract> {

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
	 * Returns the value of the '<em><b>Fob Sale Deal Type</b></em>' attribute.
	 * The literals are from the enumeration {@link com.mmxlabs.models.lng.types.FOBSaleDealType}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Fob Sale Deal Type</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Fob Sale Deal Type</em>' attribute.
	 * @see com.mmxlabs.models.lng.types.FOBSaleDealType
	 * @see #isSetFobSaleDealType()
	 * @see #unsetFobSaleDealType()
	 * @see #setFobSaleDealType(FOBSaleDealType)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getDischargeSlot_FobSaleDealType()
	 * @model unsettable="true"
	 * @generated
	 */
	FOBSaleDealType getFobSaleDealType();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.DischargeSlot#getFobSaleDealType <em>Fob Sale Deal Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Fob Sale Deal Type</em>' attribute.
	 * @see com.mmxlabs.models.lng.types.FOBSaleDealType
	 * @see #isSetFobSaleDealType()
	 * @see #unsetFobSaleDealType()
	 * @see #getFobSaleDealType()
	 * @generated
	 */
	void setFobSaleDealType(FOBSaleDealType value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.cargo.DischargeSlot#getFobSaleDealType <em>Fob Sale Deal Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetFobSaleDealType()
	 * @see #getFobSaleDealType()
	 * @see #setFobSaleDealType(FOBSaleDealType)
	 * @generated
	 */
	void unsetFobSaleDealType();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.cargo.DischargeSlot#getFobSaleDealType <em>Fob Sale Deal Type</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Fob Sale Deal Type</em>' attribute is set.
	 * @see #unsetFobSaleDealType()
	 * @see #getFobSaleDealType()
	 * @see #setFobSaleDealType(FOBSaleDealType)
	 * @generated
	 */
	boolean isSetFobSaleDealType();

	/**
	 * Returns the value of the '<em><b>Heel Carry</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Heel Carry</em>' attribute.
	 * @see #setHeelCarry(boolean)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getDischargeSlot_HeelCarry()
	 * @model
	 * @generated
	 */
	boolean isHeelCarry();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.DischargeSlot#isHeelCarry <em>Heel Carry</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Heel Carry</em>' attribute.
	 * @see #isHeelCarry()
	 * @generated
	 */
	void setHeelCarry(boolean value);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation"
	 * @generated
	 */
	double getSlotOrDelegateMinCv();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation"
	 * @generated
	 */
	double getSlotOrDelegateMaxCv();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation"
	 * @generated
	 */
	CargoDeliveryType getSlotOrDelegateDeliveryType();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation"
	 * @generated
	 */
	FOBSaleDealType getSlotOrDelegateFOBSaleDealType();
} // end of  DischargeSlot

// finish type fixing
