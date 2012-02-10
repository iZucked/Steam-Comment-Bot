/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package scenario.optimiser;

import scenario.NamedObject;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Objective</b></em>'. <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link scenario.optimiser.Objective#getWeight <em>Weight</em>}</li>
 *   <li>{@link scenario.optimiser.Objective#getDiscountCurve <em>Discount Curve</em>}</li>
 * </ul>
 * </p>
 *
 * @see scenario.optimiser.OptimiserPackage#getObjective()
 * @model
 * @generated
 */
public interface Objective extends NamedObject {
	/**
	 * Returns the value of the '<em><b>Weight</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Weight</em>' attribute isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Weight</em>' attribute.
	 * @see #setWeight(double)
	 * @see scenario.optimiser.OptimiserPackage#getObjective_Weight()
	 * @model
	 * @generated
	 */
	double getWeight();

	/**
	 * Sets the value of the '{@link scenario.optimiser.Objective#getWeight <em>Weight</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @param value the new value of the '<em>Weight</em>' attribute.
	 * @see #getWeight()
	 * @generated
	 */
	void setWeight(double value);

	/**
	 * Returns the value of the '<em><b>Discount Curve</b></em>' containment reference. <!-- begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc --> A discount curve to be used for weighting
	 * this objective. If this field is unset, the defaultDiscountCurve in the containing OptimisationSettings will be used instead. <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Discount Curve</em>' containment reference.
	 * @see #isSetDiscountCurve()
	 * @see #unsetDiscountCurve()
	 * @see #setDiscountCurve(DiscountCurve)
	 * @see scenario.optimiser.OptimiserPackage#getObjective_DiscountCurve()
	 * @model containment="true" resolveProxies="true" unsettable="true" required="true"
	 * @generated
	 */
	DiscountCurve getDiscountCurve();

	/**
	 * Sets the value of the '{@link scenario.optimiser.Objective#getDiscountCurve <em>Discount Curve</em>}' containment reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @param value the new value of the '<em>Discount Curve</em>' containment reference.
	 * @see #isSetDiscountCurve()
	 * @see #unsetDiscountCurve()
	 * @see #getDiscountCurve()
	 * @generated
	 */
	void setDiscountCurve(DiscountCurve value);

	/**
	 * Unsets the value of the '{@link scenario.optimiser.Objective#getDiscountCurve <em>Discount Curve</em>}' containment reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #isSetDiscountCurve()
	 * @see #getDiscountCurve()
	 * @see #setDiscountCurve(DiscountCurve)
	 * @generated
	 */
	void unsetDiscountCurve();

	/**
	 * Returns whether the value of the '{@link scenario.optimiser.Objective#getDiscountCurve <em>Discount Curve</em>}' containment reference is set.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return whether the value of the '<em>Discount Curve</em>' containment reference is set.
	 * @see #unsetDiscountCurve()
	 * @see #getDiscountCurve()
	 * @see #setDiscountCurve(DiscountCurve)
	 * @generated
	 */
	boolean isSetDiscountCurve();

} // Objective
