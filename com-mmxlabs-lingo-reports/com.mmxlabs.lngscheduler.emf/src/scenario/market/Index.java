/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package scenario.market;

import scenario.NamedObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Index</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link scenario.market.Index#getPriceCurve <em>Price Curve</em>}</li>
 * </ul>
 * </p>
 *
 * @see scenario.market.MarketPackage#getIndex()
 * @model
 * @generated
 */
public interface Index extends NamedObject {
	/**
	 * Returns the value of the '<em><b>Price Curve</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Price Curve</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Price Curve</em>' containment reference.
	 * @see #setPriceCurve(StepwisePriceCurve)
	 * @see scenario.market.MarketPackage#getIndex_PriceCurve()
	 * @model containment="true" resolveProxies="true" required="true"
	 * @generated
	 */
	StepwisePriceCurve getPriceCurve();

	/**
	 * Sets the value of the '{@link scenario.market.Index#getPriceCurve <em>Price Curve</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Price Curve</em>' containment reference.
	 * @see #getPriceCurve()
	 * @generated
	 */
	void setPriceCurve(StepwisePriceCurve value);

} // Index
