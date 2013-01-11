/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet;
import com.mmxlabs.models.lng.types.ABaseFuel;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Base Fuel</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.fleet.BaseFuel#getEquivalenceFactor <em>Equivalence Factor</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getBaseFuel()
 * @model
 * @generated
 */
public interface BaseFuel extends ABaseFuel {

	/**
	 * Returns the value of the '<em><b>Equivalence Factor</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Equivalence Factor</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Equivalence Factor</em>' attribute.
	 * @see #setEquivalenceFactor(double)
	 * @see com.mmxlabs.models.lng.fleet.FleetPackage#getBaseFuel_EquivalenceFactor()
	 * @model required="true"
	 * @generated
	 */
	double getEquivalenceFactor();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.fleet.BaseFuel#getEquivalenceFactor <em>Equivalence Factor</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Equivalence Factor</em>' attribute.
	 * @see #getEquivalenceFactor()
	 * @generated
	 */
	void setEquivalenceFactor(double value);
} // end of  BaseFuel

// finish type fixing
