/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package scenario.market;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Stepwise Price Curve</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link scenario.market.StepwisePriceCurve#getPrices <em>Prices</em>}</li>
 *   <li>{@link scenario.market.StepwisePriceCurve#getDefaultValue <em>Default Value</em>}</li>
 * </ul>
 * </p>
 *
 * @see scenario.market.MarketPackage#getStepwisePriceCurve()
 * @model
 * @generated
 */
public interface StepwisePriceCurve extends EObject {
	/**
	 * Returns the value of the '<em><b>Prices</b></em>' containment reference list.
	 * The list contents are of type {@link scenario.market.StepwisePrice}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Prices</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Prices</em>' containment reference list.
	 * @see scenario.market.MarketPackage#getStepwisePriceCurve_Prices()
	 * @model containment="true"
	 * @generated
	 */
	EList<StepwisePrice> getPrices();

	/**
	 * Returns the value of the '<em><b>Default Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Default Value</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Default Value</em>' attribute.
	 * @see #setDefaultValue(int)
	 * @see scenario.market.MarketPackage#getStepwisePriceCurve_DefaultValue()
	 * @model
	 * @generated
	 */
	int getDefaultValue();

	/**
	 * Sets the value of the '{@link scenario.market.StepwisePriceCurve#getDefaultValue <em>Default Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Default Value</em>' attribute.
	 * @see #getDefaultValue()
	 * @generated
	 */
	void setDefaultValue(int value);

} // StepwisePriceCurve
