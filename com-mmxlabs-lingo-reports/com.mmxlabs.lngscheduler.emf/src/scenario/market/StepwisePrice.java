/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package scenario.market;

import java.util.Date;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Stepwise Price</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link scenario.market.StepwisePrice#getDate <em>Date</em>}</li>
 *   <li>{@link scenario.market.StepwisePrice#getPriceFromDate <em>Price From Date</em>}</li>
 * </ul>
 * </p>
 *
 * @see scenario.market.MarketPackage#getStepwisePrice()
 * @model
 * @generated
 */
public interface StepwisePrice extends EObject {
	/**
	 * Returns the value of the '<em><b>Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Date</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Date</em>' attribute.
	 * @see #setDate(Date)
	 * @see scenario.market.MarketPackage#getStepwisePrice_Date()
	 * @model required="true"
	 * @generated
	 */
	Date getDate();

	/**
	 * Sets the value of the '{@link scenario.market.StepwisePrice#getDate <em>Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Date</em>' attribute.
	 * @see #getDate()
	 * @generated
	 */
	void setDate(Date value);

	/**
	 * Returns the value of the '<em><b>Price From Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Price From Date</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Price From Date</em>' attribute.
	 * @see #setPriceFromDate(float)
	 * @see scenario.market.MarketPackage#getStepwisePrice_PriceFromDate()
	 * @model required="true"
	 * @generated
	 */
	float getPriceFromDate();

	/**
	 * Sets the value of the '{@link scenario.market.StepwisePrice#getPriceFromDate <em>Price From Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Price From Date</em>' attribute.
	 * @see #getPriceFromDate()
	 * @generated
	 */
	void setPriceFromDate(float value);

} // StepwisePrice
