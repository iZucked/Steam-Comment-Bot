/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package scenario.contract;

import scenario.market.Index;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Sales Contract</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link scenario.contract.SalesContract#getIndex <em>Index</em>}</li>
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
	 * Returns the value of the '<em><b>Index</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Index</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Index</em>' reference.
	 * @see #setIndex(Index)
	 * @see scenario.contract.ContractPackage#getSalesContract_Index()
	 * @model required="true"
	 * @generated
	 */
	Index getIndex();

	/**
	 * Sets the value of the '{@link scenario.contract.SalesContract#getIndex <em>Index</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Index</em>' reference.
	 * @see #getIndex()
	 * @generated
	 */
	void setIndex(Index value);

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
