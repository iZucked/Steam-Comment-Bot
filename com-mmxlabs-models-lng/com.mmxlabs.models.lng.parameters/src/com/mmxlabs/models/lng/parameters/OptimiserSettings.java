/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.parameters;
import org.eclipse.emf.common.util.EList;

import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.models.mmxcore.UUIDObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Settings</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.parameters.OptimiserSettings#getObjectives <em>Objectives</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.parameters.OptimiserSettings#getConstraints <em>Constraints</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.parameters.OptimiserSettings#getRange <em>Range</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.parameters.OptimiserSettings#getAnnealingSettings <em>Annealing Settings</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.parameters.OptimiserSettings#getSeed <em>Seed</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.parameters.OptimiserSettings#getArguments <em>Arguments</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.parameters.OptimiserSettings#isRewire <em>Rewire</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.mmxlabs.models.lng.parameters.ParametersPackage#getOptimiserSettings()
 * @model
 * @generated
 */
public interface OptimiserSettings extends UUIDObject, NamedObject {
	/**
	 * Returns the value of the '<em><b>Objectives</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.parameters.Objective}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Objectives</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Objectives</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.parameters.ParametersPackage#getOptimiserSettings_Objectives()
	 * @model containment="true"
	 * @generated
	 */
	EList<Objective> getObjectives();

	/**
	 * Returns the value of the '<em><b>Constraints</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.parameters.Constraint}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Constraints</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Constraints</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.parameters.ParametersPackage#getOptimiserSettings_Constraints()
	 * @model containment="true"
	 * @generated
	 */
	EList<Constraint> getConstraints();

	/**
	 * Returns the value of the '<em><b>Range</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Range</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Range</em>' containment reference.
	 * @see #setRange(OptimisationRange)
	 * @see com.mmxlabs.models.lng.parameters.ParametersPackage#getOptimiserSettings_Range()
	 * @model containment="true" required="true"
	 * @generated
	 */
	OptimisationRange getRange();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.parameters.OptimiserSettings#getRange <em>Range</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Range</em>' containment reference.
	 * @see #getRange()
	 * @generated
	 */
	void setRange(OptimisationRange value);

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
	 * @see com.mmxlabs.models.lng.parameters.ParametersPackage#getOptimiserSettings_AnnealingSettings()
	 * @model containment="true" required="true"
	 * @generated
	 */
	AnnealingSettings getAnnealingSettings();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.parameters.OptimiserSettings#getAnnealingSettings <em>Annealing Settings</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Annealing Settings</em>' containment reference.
	 * @see #getAnnealingSettings()
	 * @generated
	 */
	void setAnnealingSettings(AnnealingSettings value);

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
	 * @see com.mmxlabs.models.lng.parameters.ParametersPackage#getOptimiserSettings_Seed()
	 * @model required="true"
	 * @generated
	 */
	int getSeed();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.parameters.OptimiserSettings#getSeed <em>Seed</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Seed</em>' attribute.
	 * @see #getSeed()
	 * @generated
	 */
	void setSeed(int value);

	/**
	 * Returns the value of the '<em><b>Arguments</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.parameters.Argument}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Arguments</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Arguments</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.parameters.ParametersPackage#getOptimiserSettings_Arguments()
	 * @model containment="true"
	 * @generated
	 */
	EList<Argument> getArguments();

	/**
	 * Returns the value of the '<em><b>Rewire</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Rewire</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Rewire</em>' attribute.
	 * @see #setRewire(boolean)
	 * @see com.mmxlabs.models.lng.parameters.ParametersPackage#getOptimiserSettings_Rewire()
	 * @model required="true"
	 * @generated
	 */
	boolean isRewire();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.parameters.OptimiserSettings#isRewire <em>Rewire</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Rewire</em>' attribute.
	 * @see #isRewire()
	 * @generated
	 */
	void setRewire(boolean value);

} // end of  OptimiserSettings

// finish type fixing
