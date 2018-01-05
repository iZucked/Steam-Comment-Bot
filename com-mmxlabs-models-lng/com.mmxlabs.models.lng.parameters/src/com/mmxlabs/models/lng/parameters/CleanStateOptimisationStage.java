/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.parameters;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Clean State Optimisation Stage</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.parameters.CleanStateOptimisationStage#getSeed <em>Seed</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.parameters.CleanStateOptimisationStage#getAnnealingSettings <em>Annealing Settings</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.parameters.ParametersPackage#getCleanStateOptimisationStage()
 * @model
 * @generated
 */
public interface CleanStateOptimisationStage extends ParallisableOptimisationStage, ConstraintsAndFitnessSettingsStage {
	/**
	 * Returns the value of the '<em><b>Seed</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Seed</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Seed</em>' attribute.
	 * @see #setSeed(int)
	 * @see com.mmxlabs.models.lng.parameters.ParametersPackage#getCleanStateOptimisationStage_Seed()
	 * @model required="true"
	 * @generated
	 */
	int getSeed();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.parameters.CleanStateOptimisationStage#getSeed <em>Seed</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Seed</em>' attribute.
	 * @see #getSeed()
	 * @generated
	 */
	void setSeed(int value);

	/**
	 * Returns the value of the '<em><b>Annealing Settings</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Annealing Settings</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Annealing Settings</em>' containment reference.
	 * @see #setAnnealingSettings(AnnealingSettings)
	 * @see com.mmxlabs.models.lng.parameters.ParametersPackage#getCleanStateOptimisationStage_AnnealingSettings()
	 * @model containment="true" required="true"
	 * @generated
	 */
	AnnealingSettings getAnnealingSettings();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.parameters.CleanStateOptimisationStage#getAnnealingSettings <em>Annealing Settings</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Annealing Settings</em>' containment reference.
	 * @see #getAnnealingSettings()
	 * @generated
	 */
	void setAnnealingSettings(AnnealingSettings value);

} // CleanStateOptimisationStage
