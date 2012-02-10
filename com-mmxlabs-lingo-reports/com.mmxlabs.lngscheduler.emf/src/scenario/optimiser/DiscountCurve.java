/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package scenario.optimiser;

import java.util.Date;

import org.eclipse.emf.common.util.EList;

import scenario.NamedObject;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Discount Curve</b></em>'. <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * A collection of discounts together with a base-time from which the discount curve should apply.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link scenario.optimiser.DiscountCurve#getStartDate <em>Start Date</em>}</li>
 *   <li>{@link scenario.optimiser.DiscountCurve#getDiscounts <em>Discounts</em>}</li>
 * </ul>
 * </p>
 *
 * @see scenario.optimiser.OptimiserPackage#getDiscountCurve()
 * @model
 * @generated
 */
public interface DiscountCurve extends NamedObject {
	/**
	 * Returns the value of the '<em><b>Discounts</b></em>' containment reference list.
	 * The list contents are of type {@link scenario.optimiser.Discount}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Discounts</em>' containment reference list isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Discounts</em>' containment reference list.
	 * @see scenario.optimiser.OptimiserPackage#getDiscountCurve_Discounts()
	 * @model containment="true" resolveProxies="true"
	 * @generated
	 */
	EList<Discount> getDiscounts();

	/**
	 * Returns the value of the '<em><b>Start Date</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc --> The earliest date from which the discount curve should
	 * apply. If this is not set, the discount curve applies from the optimisation's start. <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Start Date</em>' attribute.
	 * @see #isSetStartDate()
	 * @see #unsetStartDate()
	 * @see #setStartDate(Date)
	 * @see scenario.optimiser.OptimiserPackage#getDiscountCurve_StartDate()
	 * @model unsettable="true" required="true"
	 * @generated
	 */
	Date getStartDate();

	/**
	 * Sets the value of the '{@link scenario.optimiser.DiscountCurve#getStartDate <em>Start Date</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @param value the new value of the '<em>Start Date</em>' attribute.
	 * @see #isSetStartDate()
	 * @see #unsetStartDate()
	 * @see #getStartDate()
	 * @generated
	 */
	void setStartDate(Date value);

	/**
	 * Unsets the value of the '{@link scenario.optimiser.DiscountCurve#getStartDate <em>Start Date</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #isSetStartDate()
	 * @see #getStartDate()
	 * @see #setStartDate(Date)
	 * @generated
	 */
	void unsetStartDate();

	/**
	 * Returns whether the value of the '{@link scenario.optimiser.DiscountCurve#getStartDate <em>Start Date</em>}' attribute is set.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @return whether the value of the '<em>Start Date</em>' attribute is set.
	 * @see #unsetStartDate()
	 * @see #getStartDate()
	 * @see #setStartDate(Date)
	 * @generated
	 */
	boolean isSetStartDate();

} // DiscountCurve
