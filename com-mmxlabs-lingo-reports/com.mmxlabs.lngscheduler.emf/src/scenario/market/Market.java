/**
 * Copyright (C) Minimax Labs Ltd., 2010
 * All rights reserved.
 */

package scenario.market;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Market</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link scenario.market.Market#getName <em>Name</em>}</li>
 *   <li>{@link scenario.market.Market#getPriceCurve <em>Price Curve</em>}</li>
 * </ul>
 * </p>
 *
 * @see scenario.market.MarketPackage#getMarket()
 * @model
 * @generated
 */
public interface Market extends EObject {
	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see scenario.market.MarketPackage#getMarket_Name()
	 * @model
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link scenario.market.Market#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

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
	 * @see scenario.market.MarketPackage#getMarket_PriceCurve()
	 * @model containment="true" required="true"
	 * @generated
	 */
	StepwisePriceCurve getPriceCurve();

	/**
	 * Sets the value of the '{@link scenario.market.Market#getPriceCurve <em>Price Curve</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Price Curve</em>' containment reference.
	 * @see #getPriceCurve()
	 * @generated
	 */
	void setPriceCurve(StepwisePriceCurve value);

} // Market
