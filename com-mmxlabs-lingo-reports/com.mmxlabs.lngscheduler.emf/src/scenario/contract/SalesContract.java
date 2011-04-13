/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package scenario.contract;

import scenario.market.Market;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Sales Contract</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link scenario.contract.SalesContract#getMarket <em>Market</em>}</li>
 *   <li>{@link scenario.contract.SalesContract#getMarkup <em>Markup</em>}</li>
 * </ul>
 * </p>
 *
 * @see scenario.contract.ContractPackage#getSalesContract()
 * @model
 * @generated
 */
public interface SalesContract extends Contract {

	/**
	 * Returns the value of the '<em><b>Market</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Market</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Market</em>' reference.
	 * @see #setMarket(Market)
	 * @see scenario.contract.ContractPackage#getSalesContract_Market()
	 * @model required="true"
	 * @generated
	 */
	Market getMarket();

	/**
	 * Sets the value of the '{@link scenario.contract.SalesContract#getMarket <em>Market</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Market</em>' reference.
	 * @see #getMarket()
	 * @generated
	 */
	void setMarket(Market value);

	/**
	 * Returns the value of the '<em><b>Markup</b></em>' attribute.
	 * The default value is <code>"1.05"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Markup</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Markup</em>' attribute.
	 * @see #setMarkup(float)
	 * @see scenario.contract.ContractPackage#getSalesContract_Markup()
	 * @model default="1.05" required="true"
	 * @generated
	 */
	float getMarkup();

	/**
	 * Sets the value of the '{@link scenario.contract.SalesContract#getMarkup <em>Markup</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Markup</em>' attribute.
	 * @see #getMarkup()
	 * @generated
	 */
	void setMarkup(float value);
} // SalesContract
