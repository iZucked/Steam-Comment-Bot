/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.adp;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Min Cargo Constraint</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.adp.MinCargoConstraint#getMinCargoes <em>Min Cargoes</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.MinCargoConstraint#getIntervalType <em>Interval Type</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.adp.ADPPackage#getMinCargoConstraint()
 * @model
 * @generated
 */
public interface MinCargoConstraint extends ProfileConstraint {
	/**
	 * Returns the value of the '<em><b>Min Cargoes</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Min Cargoes</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Min Cargoes</em>' attribute.
	 * @see #setMinCargoes(int)
	 * @see com.mmxlabs.models.lng.adp.ADPPackage#getMinCargoConstraint_MinCargoes()
	 * @model
	 * @generated
	 */
	int getMinCargoes();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.adp.MinCargoConstraint#getMinCargoes <em>Min Cargoes</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Min Cargoes</em>' attribute.
	 * @see #getMinCargoes()
	 * @generated
	 */
	void setMinCargoes(int value);

	/**
	 * Returns the value of the '<em><b>Interval Type</b></em>' attribute.
	 * The default value is <code>"YEARLY"</code>.
	 * The literals are from the enumeration {@link com.mmxlabs.models.lng.adp.IntervalType}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Interval Type</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Interval Type</em>' attribute.
	 * @see com.mmxlabs.models.lng.adp.IntervalType
	 * @see #setIntervalType(IntervalType)
	 * @see com.mmxlabs.models.lng.adp.ADPPackage#getMinCargoConstraint_IntervalType()
	 * @model default="YEARLY"
	 * @generated
	 */
	IntervalType getIntervalType();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.adp.MinCargoConstraint#getIntervalType <em>Interval Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Interval Type</em>' attribute.
	 * @see com.mmxlabs.models.lng.adp.IntervalType
	 * @see #getIntervalType()
	 * @generated
	 */
	void setIntervalType(IntervalType value);

} // MinCargoConstraint
