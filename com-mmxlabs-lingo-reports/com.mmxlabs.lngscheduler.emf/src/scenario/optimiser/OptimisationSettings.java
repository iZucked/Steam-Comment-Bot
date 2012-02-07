/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package scenario.optimiser;

import java.util.Date;

import org.eclipse.emf.common.util.EList;

import scenario.NamedObject;
import scenario.schedule.Schedule;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Optimisation Settings</b></em>'. <!-- end-user-doc -->
 * 
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link scenario.optimiser.OptimisationSettings#getRandomSeed <em>Random Seed</em>}</li>
 * <li>{@link scenario.optimiser.OptimisationSettings#getInitialSchedule <em>Initial Schedule</em>}</li>
 * <li>{@link scenario.optimiser.OptimisationSettings#getFreezeDaysFromStart <em>Freeze Days From Start</em>}</li>
 * <li>{@link scenario.optimiser.OptimisationSettings#getIgnoreElementsAfter <em>Ignore Elements After</em>}</li>
 * <li>{@link scenario.optimiser.OptimisationSettings#getConstraints <em>Constraints</em>}</li>
 * <li>{@link scenario.optimiser.OptimisationSettings#getObjectives <em>Objectives</em>}</li>
 * <li>{@link scenario.optimiser.OptimisationSettings#getDefaultDiscountCurve <em>Default Discount Curve</em>}</li>
 * <li>{@link scenario.optimiser.OptimisationSettings#isAllowRewiringByDefault <em>Allow Rewiring By Default</em>}</li>
 * </ul>
 * </p>
 * 
 * @see scenario.optimiser.OptimiserPackage#getOptimisationSettings()
 * @model
 * @generated
 */
public interface OptimisationSettings extends NamedObject {
	/**
	 * Returns the value of the '<em><b>Random Seed</b></em>' attribute. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Random Seed</em>' attribute isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Random Seed</em>' attribute.
	 * @see #setRandomSeed(long)
	 * @see scenario.optimiser.OptimiserPackage#getOptimisationSettings_RandomSeed()
	 * @model
	 * @generated
	 */
	long getRandomSeed();

	/**
	 * Sets the value of the '{@link scenario.optimiser.OptimisationSettings#getRandomSeed <em>Random Seed</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Random Seed</em>' attribute.
	 * @see #getRandomSeed()
	 * @generated
	 */
	void setRandomSeed(long value);

	/**
	 * Returns the value of the '<em><b>Constraints</b></em>' containment reference list. The list contents are of type {@link scenario.optimiser.Constraint}. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Constraints</em>' containment reference list isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Constraints</em>' containment reference list.
	 * @see scenario.optimiser.OptimiserPackage#getOptimisationSettings_Constraints()
	 * @model containment="true" resolveProxies="true"
	 * @generated
	 */
	EList<Constraint> getConstraints();

	/**
	 * Returns the value of the '<em><b>Objectives</b></em>' containment reference list. The list contents are of type {@link scenario.optimiser.Objective}. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Objectives</em>' containment reference list isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Objectives</em>' containment reference list.
	 * @see scenario.optimiser.OptimiserPackage#getOptimisationSettings_Objectives()
	 * @model containment="true" resolveProxies="true"
	 * @generated
	 */
	EList<Objective> getObjectives();

	/**
	 * Returns the value of the '<em><b>Initial Schedule</b></em>' reference. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Initial Schedule</em>' reference isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Initial Schedule</em>' reference.
	 * @see #setInitialSchedule(Schedule)
	 * @see scenario.optimiser.OptimiserPackage#getOptimisationSettings_InitialSchedule()
	 * @model
	 * @generated
	 */
	Schedule getInitialSchedule();

	/**
	 * Sets the value of the '{@link scenario.optimiser.OptimisationSettings#getInitialSchedule <em>Initial Schedule</em>}' reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Initial Schedule</em>' reference.
	 * @see #getInitialSchedule()
	 * @generated
	 */
	void setInitialSchedule(Schedule value);

	/**
	 * Returns the value of the '<em><b>Default Discount Curve</b></em>' containment reference. <!-- begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc --> A discount curve which will be
	 * applied to all this optimisation's objectives which don't themselves have a discount curve set. If unset, no discounts will be applied. <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Default Discount Curve</em>' containment reference.
	 * @see #isSetDefaultDiscountCurve()
	 * @see #unsetDefaultDiscountCurve()
	 * @see #setDefaultDiscountCurve(DiscountCurve)
	 * @see scenario.optimiser.OptimiserPackage#getOptimisationSettings_DefaultDiscountCurve()
	 * @model containment="true" resolveProxies="true" unsettable="true" required="true"
	 * @generated
	 */
	DiscountCurve getDefaultDiscountCurve();

	/**
	 * Sets the value of the '{@link scenario.optimiser.OptimisationSettings#getDefaultDiscountCurve <em>Default Discount Curve</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @param value
	 *            the new value of the '<em>Default Discount Curve</em>' containment reference.
	 * @see #isSetDefaultDiscountCurve()
	 * @see #unsetDefaultDiscountCurve()
	 * @see #getDefaultDiscountCurve()
	 * @generated
	 */
	void setDefaultDiscountCurve(DiscountCurve value);

	/**
	 * Unsets the value of the '{@link scenario.optimiser.OptimisationSettings#getDefaultDiscountCurve <em>Default Discount Curve</em>}' containment reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #isSetDefaultDiscountCurve()
	 * @see #getDefaultDiscountCurve()
	 * @see #setDefaultDiscountCurve(DiscountCurve)
	 * @generated
	 */
	void unsetDefaultDiscountCurve();

	/**
	 * Returns whether the value of the '{@link scenario.optimiser.OptimisationSettings#getDefaultDiscountCurve <em>Default Discount Curve</em>}' containment reference is set. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return whether the value of the '<em>Default Discount Curve</em>' containment reference is set.
	 * @see #unsetDefaultDiscountCurve()
	 * @see #getDefaultDiscountCurve()
	 * @see #setDefaultDiscountCurve(DiscountCurve)
	 * @generated
	 */
	boolean isSetDefaultDiscountCurve();

	/**
	 * Returns the value of the '<em><b>Allow Rewiring By Default</b></em>' attribute. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Allow Rewiring By Default</em>' attribute isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Allow Rewiring By Default</em>' attribute.
	 * @see #setAllowRewiringByDefault(boolean)
	 * @see scenario.optimiser.OptimiserPackage#getOptimisationSettings_AllowRewiringByDefault()
	 * @model required="true"
	 * @generated
	 */
	boolean isAllowRewiringByDefault();

	/**
	 * Sets the value of the '{@link scenario.optimiser.OptimisationSettings#isAllowRewiringByDefault <em>Allow Rewiring By Default</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Allow Rewiring By Default</em>' attribute.
	 * @see #isAllowRewiringByDefault()
	 * @generated
	 */
	void setAllowRewiringByDefault(boolean value);

	/**
	 * Returns the value of the '<em><b>Freeze Days From Start</b></em>' attribute. The default value is <code>"0"</code>. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Freeze Days From Start</em>' attribute isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Freeze Days From Start</em>' attribute.
	 * @see #setFreezeDaysFromStart(int)
	 * @see scenario.optimiser.OptimiserPackage#getOptimisationSettings_FreezeDaysFromStart()
	 * @model default="0" required="true"
	 * @generated
	 */
	int getFreezeDaysFromStart();

	/**
	 * Sets the value of the '{@link scenario.optimiser.OptimisationSettings#getFreezeDaysFromStart <em>Freeze Days From Start</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Freeze Days From Start</em>' attribute.
	 * @see #getFreezeDaysFromStart()
	 * @generated
	 */
	void setFreezeDaysFromStart(int value);

	/**
	 * Returns the value of the '<em><b>Ignore Elements After</b></em>' attribute. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Ignore Elements After</em>' attribute isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Ignore Elements After</em>' attribute.
	 * @see #isSetIgnoreElementsAfter()
	 * @see #unsetIgnoreElementsAfter()
	 * @see #setIgnoreElementsAfter(Date)
	 * @see scenario.optimiser.OptimiserPackage#getOptimisationSettings_IgnoreElementsAfter()
	 * @model unsettable="true" required="true"
	 * @generated
	 */
	Date getIgnoreElementsAfter();

	/**
	 * Sets the value of the '{@link scenario.optimiser.OptimisationSettings#getIgnoreElementsAfter <em>Ignore Elements After</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Ignore Elements After</em>' attribute.
	 * @see #isSetIgnoreElementsAfter()
	 * @see #unsetIgnoreElementsAfter()
	 * @see #getIgnoreElementsAfter()
	 * @generated
	 */
	void setIgnoreElementsAfter(Date value);

	/**
	 * Unsets the value of the '{@link scenario.optimiser.OptimisationSettings#getIgnoreElementsAfter <em>Ignore Elements After</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #isSetIgnoreElementsAfter()
	 * @see #getIgnoreElementsAfter()
	 * @see #setIgnoreElementsAfter(Date)
	 * @generated
	 */
	void unsetIgnoreElementsAfter();

	/**
	 * Returns whether the value of the '{@link scenario.optimiser.OptimisationSettings#getIgnoreElementsAfter <em>Ignore Elements After</em>}' attribute is set. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @return whether the value of the '<em>Ignore Elements After</em>' attribute is set.
	 * @see #unsetIgnoreElementsAfter()
	 * @see #getIgnoreElementsAfter()
	 * @see #setIgnoreElementsAfter(Date)
	 * @generated
	 */
	boolean isSetIgnoreElementsAfter();

} // OptimisationSettings
