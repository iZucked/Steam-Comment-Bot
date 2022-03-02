/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.adp;

import com.mmxlabs.models.lng.fleet.Vessel;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Target Cargoes On Vessel Constraint</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.adp.TargetCargoesOnVesselConstraint#getVessel <em>Vessel</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.TargetCargoesOnVesselConstraint#getTargetNumberOfCargoes <em>Target Number Of Cargoes</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.TargetCargoesOnVesselConstraint#getIntervalType <em>Interval Type</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.TargetCargoesOnVesselConstraint#getWeight <em>Weight</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.adp.ADPPackage#getTargetCargoesOnVesselConstraint()
 * @model
 * @generated
 */
public interface TargetCargoesOnVesselConstraint extends FleetConstraint {
	/**
	 * Returns the value of the '<em><b>Vessel</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Vessel</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Vessel</em>' reference.
	 * @see #setVessel(Vessel)
	 * @see com.mmxlabs.models.lng.adp.ADPPackage#getTargetCargoesOnVesselConstraint_Vessel()
	 * @model
	 * @generated
	 */
	Vessel getVessel();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.adp.TargetCargoesOnVesselConstraint#getVessel <em>Vessel</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Vessel</em>' reference.
	 * @see #getVessel()
	 * @generated
	 */
	void setVessel(Vessel value);

	/**
	 * Returns the value of the '<em><b>Target Number Of Cargoes</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Target Number Of Cargoes</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Target Number Of Cargoes</em>' attribute.
	 * @see #setTargetNumberOfCargoes(int)
	 * @see com.mmxlabs.models.lng.adp.ADPPackage#getTargetCargoesOnVesselConstraint_TargetNumberOfCargoes()
	 * @model
	 * @generated
	 */
	int getTargetNumberOfCargoes();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.adp.TargetCargoesOnVesselConstraint#getTargetNumberOfCargoes <em>Target Number Of Cargoes</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Target Number Of Cargoes</em>' attribute.
	 * @see #getTargetNumberOfCargoes()
	 * @generated
	 */
	void setTargetNumberOfCargoes(int value);

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
	 * @see com.mmxlabs.models.lng.adp.ADPPackage#getTargetCargoesOnVesselConstraint_IntervalType()
	 * @model default="YEARLY"
	 * @generated
	 */
	IntervalType getIntervalType();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.adp.TargetCargoesOnVesselConstraint#getIntervalType <em>Interval Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Interval Type</em>' attribute.
	 * @see com.mmxlabs.models.lng.adp.IntervalType
	 * @see #getIntervalType()
	 * @generated
	 */
	void setIntervalType(IntervalType value);

	/**
	 * Returns the value of the '<em><b>Weight</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Weight</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Weight</em>' attribute.
	 * @see #setWeight(int)
	 * @see com.mmxlabs.models.lng.adp.ADPPackage#getTargetCargoesOnVesselConstraint_Weight()
	 * @model
	 * @generated
	 */
	int getWeight();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.adp.TargetCargoesOnVesselConstraint#getWeight <em>Weight</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Weight</em>' attribute.
	 * @see #getWeight()
	 * @generated
	 */
	void setWeight(int value);

} // TargetCargoesOnVesselConstraint
