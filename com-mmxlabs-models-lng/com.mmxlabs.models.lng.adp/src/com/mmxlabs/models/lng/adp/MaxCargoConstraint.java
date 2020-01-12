/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.adp;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Max Cargo Constraint</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.adp.MaxCargoConstraint#getMaxCargoes <em>Max Cargoes</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.MaxCargoConstraint#getIntervalType <em>Interval Type</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.adp.ADPPackage#getMaxCargoConstraint()
 * @model
 * @generated
 */
public interface MaxCargoConstraint extends ProfileConstraint {
	/**
	 * Returns the value of the '<em><b>Max Cargoes</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Max Cargoes</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Max Cargoes</em>' attribute.
	 * @see #setMaxCargoes(int)
	 * @see com.mmxlabs.models.lng.adp.ADPPackage#getMaxCargoConstraint_MaxCargoes()
	 * @model
	 * @generated
	 */
	int getMaxCargoes();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.adp.MaxCargoConstraint#getMaxCargoes <em>Max Cargoes</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Max Cargoes</em>' attribute.
	 * @see #getMaxCargoes()
	 * @generated
	 */
	void setMaxCargoes(int value);

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
	 * @see com.mmxlabs.models.lng.adp.ADPPackage#getMaxCargoConstraint_IntervalType()
	 * @model default="YEARLY"
	 * @generated
	 */
	IntervalType getIntervalType();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.adp.MaxCargoConstraint#getIntervalType <em>Interval Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Interval Type</em>' attribute.
	 * @see com.mmxlabs.models.lng.adp.IntervalType
	 * @see #getIntervalType()
	 * @generated
	 */
	void setIntervalType(IntervalType value);

} // MaxCargoConstraint
