/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package scenario.optimiser;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Optimisation</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link scenario.optimiser.Optimisation#getAllSettings <em>All Settings</em>}</li>
 *   <li>{@link scenario.optimiser.Optimisation#getCurrentSettings <em>Current Settings</em>}</li>
 * </ul>
 * </p>
 *
 * @see scenario.optimiser.OptimiserPackage#getOptimisation()
 * @model
 * @generated
 */
public interface Optimisation extends EObject {
	/**
	 * Returns the value of the '<em><b>All Settings</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>All Settings</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>All Settings</em>' containment reference.
	 * @see #setAllSettings(OptimisationSettings)
	 * @see scenario.optimiser.OptimiserPackage#getOptimisation_AllSettings()
	 * @model containment="true"
	 * @generated
	 */
	OptimisationSettings getAllSettings();

	/**
	 * Sets the value of the '{@link scenario.optimiser.Optimisation#getAllSettings <em>All Settings</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>All Settings</em>' containment reference.
	 * @see #getAllSettings()
	 * @generated
	 */
	void setAllSettings(OptimisationSettings value);

	/**
	 * Returns the value of the '<em><b>Current Settings</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Current Settings</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Current Settings</em>' reference.
	 * @see #setCurrentSettings(OptimisationSettings)
	 * @see scenario.optimiser.OptimiserPackage#getOptimisation_CurrentSettings()
	 * @model
	 * @generated
	 */
	OptimisationSettings getCurrentSettings();

	/**
	 * Sets the value of the '{@link scenario.optimiser.Optimisation#getCurrentSettings <em>Current Settings</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Current Settings</em>' reference.
	 * @see #getCurrentSettings()
	 * @generated
	 */
	void setCurrentSettings(OptimisationSettings value);

} // Optimisation
