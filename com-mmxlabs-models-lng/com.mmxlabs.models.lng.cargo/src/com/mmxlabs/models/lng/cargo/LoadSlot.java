/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Load Slot</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.cargo.LoadSlot#getCargoCV <em>Cargo CV</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.LoadSlot#isArriveCold <em>Arrive Cold</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.LoadSlot#isDESPurchase <em>DES Purchase</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.mmxlabs.models.lng.cargo.CargoPackage#getLoadSlot()
 * @model
 * @generated
 */
public interface LoadSlot extends Slot {
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
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation" required="true"
	 * @generated
	 */
	double getSlotOrPortCV();

} // end of  LoadSlot

// finish type fixing
