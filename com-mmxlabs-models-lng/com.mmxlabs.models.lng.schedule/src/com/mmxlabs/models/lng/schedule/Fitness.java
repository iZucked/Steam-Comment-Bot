/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.schedule;
import com.mmxlabs.models.mmxcore.NamedObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Fitness</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.schedule.Fitness#getFitnessValue <em>Fitness Value</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getFitness()
 * @model
 * @generated
 */
public interface Fitness extends NamedObject {
	/**
	 * Returns the value of the '<em><b>Fitness Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Fitness Value</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Fitness Value</em>' attribute.
	 * @see #setFitnessValue(long)
	 * @see com.mmxlabs.models.lng.schedule.SchedulePackage#getFitness_FitnessValue()
	 * @model required="true"
	 * @generated
	 */
	long getFitnessValue();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.schedule.Fitness#getFitnessValue <em>Fitness Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Fitness Value</em>' attribute.
	 * @see #getFitnessValue()
	 * @generated
	 */
	void setFitnessValue(long value);

} // end of  Fitness

// finish type fixing
