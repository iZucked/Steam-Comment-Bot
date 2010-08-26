/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package scenario.optimiser;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>LSO Settings</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link scenario.optimiser.LSOSettings#getNumberOfSteps <em>Number Of Steps</em>}</li>
 *   <li>{@link scenario.optimiser.LSOSettings#getStepSize <em>Step Size</em>}</li>
 *   <li>{@link scenario.optimiser.LSOSettings#getInitialThreshold <em>Initial Threshold</em>}</li>
 * </ul>
 * </p>
 *
 * @see scenario.optimiser.OptimiserPackage#getLSOSettings()
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
	 * @see scenario.optimiser.OptimiserPackage#getLSOSettings_NumberOfSteps()
	 * @model
	 * @generated
	 */
	int getNumberOfSteps();

	/**
	 * Sets the value of the '{@link scenario.optimiser.LSOSettings#getNumberOfSteps <em>Number Of Steps</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Number Of Steps</em>' attribute.
	 * @see #getNumberOfSteps()
	 * @generated
	 */
	void setNumberOfSteps(int value);

	/**
	 * Returns the value of the '<em><b>Step Size</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Step Size</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Step Size</em>' attribute.
	 * @see #setStepSize(int)
	 * @see scenario.optimiser.OptimiserPackage#getLSOSettings_StepSize()
	 * @model
	 * @generated
	 */
	int getStepSize();

	/**
	 * Sets the value of the '{@link scenario.optimiser.LSOSettings#getStepSize <em>Step Size</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Step Size</em>' attribute.
	 * @see #getStepSize()
	 * @generated
	 */
	void setStepSize(int value);

	/**
	 * Returns the value of the '<em><b>Initial Threshold</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Initial Threshold</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Initial Threshold</em>' attribute.
	 * @see #setInitialThreshold(int)
	 * @see scenario.optimiser.OptimiserPackage#getLSOSettings_InitialThreshold()
	 * @model
	 * @generated
	 */
	int getInitialThreshold();

	/**
	 * Sets the value of the '{@link scenario.optimiser.LSOSettings#getInitialThreshold <em>Initial Threshold</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Initial Threshold</em>' attribute.
	 * @see #getInitialThreshold()
	 * @generated
	 */
	void setInitialThreshold(int value);

} // LSOSettings
