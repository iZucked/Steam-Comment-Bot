/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo;

import com.mmxlabs.models.lng.commercial.PurchaseContract;
import com.mmxlabs.models.lng.types.CargoDeliveryType;
import com.mmxlabs.models.lng.types.DESPurchaseDealType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Load Slot</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.cargo.LoadSlot#getCargoCV <em>Cargo CV</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.LoadSlot#isSchedulePurge <em>Schedule Purge</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.LoadSlot#isArriveCold <em>Arrive Cold</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.LoadSlot#isDESPurchase <em>DES Purchase</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.LoadSlot#getTransferFrom <em>Transfer From</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.LoadSlot#getSalesDeliveryType <em>Sales Delivery Type</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.LoadSlot#getDesPurchaseDealType <em>Des Purchase Deal Type</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.LoadSlot#isVolumeCounterParty <em>Volume Counter Party</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getLoadSlot()
 * @model annotation="http://www.mmxlabs.com/models/featureOverride"
 * @generated
 */
public interface LoadSlot extends Slot<PurchaseContract> {
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
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getLoadSlot_CargoCV()
	 * @model unsettable="true" required="true"
	 *        annotation="http://www.mmxlabs.com/models/ui/numberFormat formatString='#0.###'"
	 * @generated
	 */
	double getCargoCV();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.LoadSlot#getCargoCV <em>Cargo CV</em>}' attribute.
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
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.cargo.LoadSlot#getCargoCV <em>Cargo CV</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetCargoCV()
	 * @see #getCargoCV()
	 * @see #setCargoCV(double)
	 * @generated
	 */
	void unsetCargoCV();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.cargo.LoadSlot#getCargoCV <em>Cargo CV</em>}' attribute is set.
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
	 * Returns the value of the '<em><b>Schedule Purge</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Schedule Purge</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Schedule Purge</em>' attribute.
	 * @see #setSchedulePurge(boolean)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getLoadSlot_SchedulePurge()
	 * @model required="true"
	 *        annotation="http://www.mmxlabs.com/models/ui/featureEnablement feature='purge'"
	 * @generated
	 */
	boolean isSchedulePurge();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.LoadSlot#isSchedulePurge <em>Schedule Purge</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Schedule Purge</em>' attribute.
	 * @see #isSchedulePurge()
	 * @generated
	 */
	void setSchedulePurge(boolean value);

	/**
	 * Returns the value of the '<em><b>Arrive Cold</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Arrive Cold</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Arrive Cold</em>' attribute.
	 * @see #isSetArriveCold()
	 * @see #unsetArriveCold()
	 * @see #setArriveCold(boolean)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getLoadSlot_ArriveCold()
	 * @model unsettable="true" required="true"
	 * @generated
	 */
	boolean isArriveCold();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.LoadSlot#isArriveCold <em>Arrive Cold</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Arrive Cold</em>' attribute.
	 * @see #isSetArriveCold()
	 * @see #unsetArriveCold()
	 * @see #isArriveCold()
	 * @generated
	 */
	void setArriveCold(boolean value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.cargo.LoadSlot#isArriveCold <em>Arrive Cold</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetArriveCold()
	 * @see #isArriveCold()
	 * @see #setArriveCold(boolean)
	 * @generated
	 */
	void unsetArriveCold();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.cargo.LoadSlot#isArriveCold <em>Arrive Cold</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Arrive Cold</em>' attribute is set.
	 * @see #unsetArriveCold()
	 * @see #isArriveCold()
	 * @see #setArriveCold(boolean)
	 * @generated
	 */
	boolean isSetArriveCold();

	/**
	 * Returns the value of the '<em><b>DES Purchase</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>DES Purchase</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>DES Purchase</em>' attribute.
	 * @see #setDESPurchase(boolean)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getLoadSlot_DESPurchase()
	 * @model
	 * @generated
	 */
	boolean isDESPurchase();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.LoadSlot#isDESPurchase <em>DES Purchase</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>DES Purchase</em>' attribute.
	 * @see #isDESPurchase()
	 * @generated
	 */
	void setDESPurchase(boolean value);

	/**
	 * Returns the value of the '<em><b>Transfer From</b></em>' reference.
	 * It is bidirectional and its opposite is '{@link com.mmxlabs.models.lng.cargo.DischargeSlot#getTransferTo <em>Transfer To</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Transfer From</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Transfer From</em>' reference.
	 * @see #setTransferFrom(DischargeSlot)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getLoadSlot_TransferFrom()
	 * @see com.mmxlabs.models.lng.cargo.DischargeSlot#getTransferTo
	 * @model opposite="transferTo"
	 * @generated
	 */
	DischargeSlot getTransferFrom();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.LoadSlot#getTransferFrom <em>Transfer From</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Transfer From</em>' reference.
	 * @see #getTransferFrom()
	 * @generated
	 */
	void setTransferFrom(DischargeSlot value);

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
	 * @see #isSetSalesDeliveryType()
	 * @see #unsetSalesDeliveryType()
	 * @see #setSalesDeliveryType(CargoDeliveryType)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getLoadSlot_SalesDeliveryType()
	 * @model default="Any" unsettable="true"
	 * @generated
	 */
	CargoDeliveryType getSalesDeliveryType();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.LoadSlot#getSalesDeliveryType <em>Sales Delivery Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Sales Delivery Type</em>' attribute.
	 * @see com.mmxlabs.models.lng.types.CargoDeliveryType
	 * @see #isSetSalesDeliveryType()
	 * @see #unsetSalesDeliveryType()
	 * @see #getSalesDeliveryType()
	 * @generated
	 */
	void setSalesDeliveryType(CargoDeliveryType value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.cargo.LoadSlot#getSalesDeliveryType <em>Sales Delivery Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetSalesDeliveryType()
	 * @see #getSalesDeliveryType()
	 * @see #setSalesDeliveryType(CargoDeliveryType)
	 * @generated
	 */
	void unsetSalesDeliveryType();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.cargo.LoadSlot#getSalesDeliveryType <em>Sales Delivery Type</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Sales Delivery Type</em>' attribute is set.
	 * @see #unsetSalesDeliveryType()
	 * @see #getSalesDeliveryType()
	 * @see #setSalesDeliveryType(CargoDeliveryType)
	 * @generated
	 */
	boolean isSetSalesDeliveryType();

	/**
	 * Returns the value of the '<em><b>Des Purchase Deal Type</b></em>' attribute.
	 * The literals are from the enumeration {@link com.mmxlabs.models.lng.types.DESPurchaseDealType}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Des Purchase Deal Type</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Des Purchase Deal Type</em>' attribute.
	 * @see com.mmxlabs.models.lng.types.DESPurchaseDealType
	 * @see #isSetDesPurchaseDealType()
	 * @see #unsetDesPurchaseDealType()
	 * @see #setDesPurchaseDealType(DESPurchaseDealType)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getLoadSlot_DesPurchaseDealType()
	 * @model unsettable="true"
	 * @generated
	 */
	DESPurchaseDealType getDesPurchaseDealType();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.LoadSlot#getDesPurchaseDealType <em>Des Purchase Deal Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Des Purchase Deal Type</em>' attribute.
	 * @see com.mmxlabs.models.lng.types.DESPurchaseDealType
	 * @see #isSetDesPurchaseDealType()
	 * @see #unsetDesPurchaseDealType()
	 * @see #getDesPurchaseDealType()
	 * @generated
	 */
	void setDesPurchaseDealType(DESPurchaseDealType value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.cargo.LoadSlot#getDesPurchaseDealType <em>Des Purchase Deal Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetDesPurchaseDealType()
	 * @see #getDesPurchaseDealType()
	 * @see #setDesPurchaseDealType(DESPurchaseDealType)
	 * @generated
	 */
	void unsetDesPurchaseDealType();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.cargo.LoadSlot#getDesPurchaseDealType <em>Des Purchase Deal Type</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Des Purchase Deal Type</em>' attribute is set.
	 * @see #unsetDesPurchaseDealType()
	 * @see #getDesPurchaseDealType()
	 * @see #setDesPurchaseDealType(DESPurchaseDealType)
	 * @generated
	 */
	boolean isSetDesPurchaseDealType();

	/**
	 * Returns the value of the '<em><b>Volume Counter Party</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Volume Counter Party</em>' attribute.
	 * @see #setVolumeCounterParty(boolean)
	 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getLoadSlot_VolumeCounterParty()
	 * @model
	 * @generated
	 */
	boolean isVolumeCounterParty();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.cargo.LoadSlot#isVolumeCounterParty <em>Volume Counter Party</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Volume Counter Party</em>' attribute.
	 * @see #isVolumeCounterParty()
	 * @generated
	 */
	void setVolumeCounterParty(boolean value);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation" required="true"
	 * @generated
	 */
	double getSlotOrDelegateCV();

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
	DESPurchaseDealType getSlotOrDelegateDESPurchaseDealType();

} // end of  LoadSlot

// finish type fixing
