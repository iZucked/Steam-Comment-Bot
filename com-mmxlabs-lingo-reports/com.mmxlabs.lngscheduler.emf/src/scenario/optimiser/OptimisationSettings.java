/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package scenario.optimiser;

import org.eclipse.emf.common.util.EList;

import scenario.NamedObject;

import scenario.schedule.Schedule;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Optimisation Settings</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link scenario.optimiser.OptimisationSettings#getRandomSeed <em>Random Seed</em>}</li>
 *   <li>{@link scenario.optimiser.OptimisationSettings#getConstraints <em>Constraints</em>}</li>
 *   <li>{@link scenario.optimiser.OptimisationSettings#getObjectives <em>Objectives</em>}</li>
 *   <li>{@link scenario.optimiser.OptimisationSettings#getInitialSchedule <em>Initial Schedule</em>}</li>
 *   <li>{@link scenario.optimiser.OptimisationSettings#getDefaultDiscountCurve <em>Default Discount Curve</em>}</li>
 * </ul>
 * </p>
 *
 * @see scenario.optimiser.OptimiserPackage#getOptimisationSettings()
 * @model
 * @generated
 */
public interface OptimisationSettings extends NamedObject {
	/**
	 * Returns the value of the '<em><b>Random Seed</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Random Seed</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Random Seed</em>' attribute.
	 * @see #setRandomSeed(long)
	 * @see scenario.optimiser.OptimiserPackage#getOptimisationSettings_RandomSeed()
	 * @model
	 * @generated
	 */
	long getRandomSeed();

	/**
	 * Sets the value of the '{@link scenario.optimiser.OptimisationSettings#getRandomSeed <em>Random Seed</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Random Seed</em>' attribute.
	 * @see #getRandomSeed()
	 * @generated
	 */
	void setRandomSeed(long value);

	/**
	 * Returns the value of the '<em><b>Constraints</b></em>' containment reference list.
	 * The list contents are of type {@link scenario.optimiser.Constraint}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Constraints</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Constraints</em>' containment reference list.
	 * @see scenario.optimiser.OptimiserPackage#getOptimisationSettings_Constraints()
	 * @model containment="true"
	 * @generated
	 */
	EList<Constraint> getConstraints();

	/**
	 * Returns the value of the '<em><b>Objectives</b></em>' containment reference list.
	 * The list contents are of type {@link scenario.optimiser.Objective}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Objectives</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Objectives</em>' containment reference list.
	 * @see scenario.optimiser.OptimiserPackage#getOptimisationSettings_Objectives()
	 * @model containment="true"
	 * @generated
	 */
	EList<Objective> getObjectives();

	/**
	 * Returns the value of the '<em><b>Initial Schedule</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Initial Schedule</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Initial Schedule</em>' reference.
	 * @see #setInitialSchedule(Schedule)
	 * @see scenario.optimiser.OptimiserPackage#getOptimisationSettings_InitialSchedule()
	 * @model
	 * @generated
	 */
	Schedule getInitialSchedule();

	/**
	 * Sets the value of the '{@link scenario.optimiser.OptimisationSettings#getInitialSchedule <em>Initial Schedule</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Initial Schedule</em>' reference.
	 * @see #getInitialSchedule()
	 * @generated
	 */
	void setInitialSchedule(Schedule value);

	/**
	 * Returns the value of the '<em><b>Default Discount Curve</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * A discount curve which will be applied to all this optimisation's objectives which don't themselves have a discount curve set. If unset, no discounts will be applied.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Default Discount Curve</em>' reference.
	 * @see #isSetDefaultDiscountCurve()
	 * @see #unsetDefaultDiscountCurve()
	 * @see #setDefaultDiscountCurve(DiscountCurve)
	 * @see scenario.optimiser.OptimiserPackage#getOptimisationSettings_DefaultDiscountCurve()
	 * @model unsettable="true" required="true"
	 * @generated
	 */
	DiscountCurve getDefaultDiscountCurve();

	/**
	 * Sets the value of the '{@link scenario.optimiser.OptimisationSettings#getDefaultDiscountCurve <em>Default Discount Curve</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Default Discount Curve</em>' reference.
	 * @see #isSetDefaultDiscountCurve()
	 * @see #unsetDefaultDiscountCurve()
	 * @see #getDefaultDiscountCurve()
	 * @generated
	 */
	void setDefaultDiscountCurve(DiscountCurve value);

	/**
	 * Unsets the value of the '{@link scenario.optimiser.OptimisationSettings#getDefaultDiscountCurve <em>Default Discount Curve</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetDefaultDiscountCurve()
	 * @see #getDefaultDiscountCurve()
	 * @see #setDefaultDiscountCurve(DiscountCurve)
	 * @generated
	 */
	void unsetDefaultDiscountCurve();

	/**
	 * Returns whether the value of the '{@link scenario.optimiser.OptimisationSettings#getDefaultDiscountCurve <em>Default Discount Curve</em>}' reference is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Default Discount Curve</em>' reference is set.
	 * @see #unsetDefaultDiscountCurve()
	 * @see #getDefaultDiscountCurve()
	 * @see #setDefaultDiscountCurve(DiscountCurve)
	 * @generated
	 */
	boolean isSetDefaultDiscountCurve();

} // OptimisationSettings
