/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package scenario.optimiser.lso;

import scenario.optimiser.OptimisationSettings;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>LSO Settings</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link scenario.optimiser.lso.LSOSettings#getNumberOfSteps <em>Number Of Steps</em>}</li>
 *   <li>{@link scenario.optimiser.lso.LSOSettings#getThresholderSettings <em>Thresholder Settings</em>}</li>
 *   <li>{@link scenario.optimiser.lso.LSOSettings#getMoveGeneratorSettings <em>Move Generator Settings</em>}</li>
 * </ul>
 * </p>
 *
 * @see scenario.optimiser.lso.LsoPackage#getLSOSettings()
 * @model
 * @generated
 */
public interface LSOSettings extends OptimisationSettings {
	/**
	 * Returns the value of the '<em><b>Number Of Steps</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Number Of Steps</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Number Of Steps</em>' attribute.
	 * @see #setNumberOfSteps(int)
	 * @see scenario.optimiser.lso.LsoPackage#getLSOSettings_NumberOfSteps()
	 * @model
	 * @generated
	 */
	int getNumberOfSteps();

	/**
	 * Sets the value of the '{@link scenario.optimiser.lso.LSOSettings#getNumberOfSteps <em>Number Of Steps</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Number Of Steps</em>' attribute.
	 * @see #getNumberOfSteps()
	 * @generated
	 */
	void setNumberOfSteps(int value);

	/**
	 * Returns the value of the '<em><b>Thresholder Settings</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Thresholder Settings</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Thresholder Settings</em>' containment reference.
	 * @see #setThresholderSettings(ThresholderSettings)
	 * @see scenario.optimiser.lso.LsoPackage#getLSOSettings_ThresholderSettings()
	 * @model containment="true" required="true"
	 * @generated
	 */
	ThresholderSettings getThresholderSettings();

	/**
	 * Sets the value of the '{@link scenario.optimiser.lso.LSOSettings#getThresholderSettings <em>Thresholder Settings</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Thresholder Settings</em>' containment reference.
	 * @see #getThresholderSettings()
	 * @generated
	 */
	void setThresholderSettings(ThresholderSettings value);

	/**
	 * Returns the value of the '<em><b>Move Generator Settings</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Move Generator Settings</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Move Generator Settings</em>' containment reference.
	 * @see #setMoveGeneratorSettings(MoveGeneratorSettings)
	 * @see scenario.optimiser.lso.LsoPackage#getLSOSettings_MoveGeneratorSettings()
	 * @model containment="true"
	 * @generated
	 */
	MoveGeneratorSettings getMoveGeneratorSettings();

	/**
	 * Sets the value of the '{@link scenario.optimiser.lso.LSOSettings#getMoveGeneratorSettings <em>Move Generator Settings</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Move Generator Settings</em>' containment reference.
	 * @see #getMoveGeneratorSettings()
	 * @generated
	 */
	void setMoveGeneratorSettings(MoveGeneratorSettings value);

} // LSOSettings
