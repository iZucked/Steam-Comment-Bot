/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.commercial;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Purchase Contract</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.commercial.PurchaseContract#getCargoCV <em>Cargo CV</em>}</li>
 * </ul>
 * </p>
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
} // end of  PurchaseContract

// finish type fixing
