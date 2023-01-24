/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.parameters;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Strategic Local Search Optimisation Stage</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.parameters.StrategicLocalSearchOptimisationStage#getCount <em>Count</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.parameters.ParametersPackage#getStrategicLocalSearchOptimisationStage()
 * @model
 * @generated
 */
public interface StrategicLocalSearchOptimisationStage extends LocalSearchOptimisationStage {
	/**
	 * Returns the value of the '<em><b>Count</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Count</em>' attribute.
	 * @see #setCount(int)
	 * @see com.mmxlabs.models.lng.parameters.ParametersPackage#getStrategicLocalSearchOptimisationStage_Count()
	 * @model required="true"
	 * @generated
	 */
	int getCount();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.parameters.StrategicLocalSearchOptimisationStage#getCount <em>Count</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Count</em>' attribute.
	 * @see #getCount()
	 * @generated
	 */
	void setCount(int value);

} // StrategicLocalSearchOptimisationStage
