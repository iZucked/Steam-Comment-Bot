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
 * A representation of the model object '<em><b>Discount</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * A discount value, which gives a time from the start of its containing curve and a discount factor for fitnesses from that time up to the next specified time (if any)
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link scenario.optimiser.Discount#getTime <em>Time</em>}</li>
 *   <li>{@link scenario.optimiser.Discount#getDiscountFactor <em>Discount Factor</em>}</li>
 * </ul>
 * </p>
 *
 * @see scenario.optimiser.OptimiserPackage#getDiscount()
 * @model
 * @generated
 */
public interface Discount extends EObject {
	/**
	 * Returns the value of the '<em><b>Time</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The time offset from the beginning of the curve from which this discount factor applies, in hours.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Time</em>' attribute.
	 * @see #setTime(int)
	 * @see scenario.optimiser.OptimiserPackage#getDiscount_Time()
	 * @model required="true"
	 * @generated
	 */
	int getTime();

	/**
	 * Sets the value of the '{@link scenario.optimiser.Discount#getTime <em>Time</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Time</em>' attribute.
	 * @see #getTime()
	 * @generated
	 */
	void setTime(int value);

	/**
	 * Returns the value of the '<em><b>Discount Factor</b></em>' attribute.
	 * The default value is <code>"1"</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The discount factor itself, a floating point number indicating how much weight a fitness value should be given in this interval (0 means no weight, 1 means normal weight)
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Discount Factor</em>' attribute.
	 * @see #setDiscountFactor(float)
	 * @see scenario.optimiser.OptimiserPackage#getDiscount_DiscountFactor()
	 * @model default="1" required="true"
	 * @generated
	 */
	float getDiscountFactor();

	/**
	 * Sets the value of the '{@link scenario.optimiser.Discount#getDiscountFactor <em>Discount Factor</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Discount Factor</em>' attribute.
	 * @see #getDiscountFactor()
	 * @generated
	 */
	void setDiscountFactor(float value);

} // Discount
