/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package scenario.contract;

import scenario.market.Index;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Index Price Purchase Contract</b></em>'. <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link scenario.contract.IndexPricePurchaseContract#getIndex <em>Index</em>}</li>
 * </ul>
 * </p>
 *
 * @see scenario.contract.ContractPackage#getIndexPricePurchaseContract()
 * @model
 * @generated
 */
public interface IndexPricePurchaseContract extends SimplePurchaseContract {
	/**
	 * Returns the value of the '<em><b>Index</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Index</em>' reference isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Index</em>' reference.
	 * @see #setIndex(Index)
	 * @see scenario.contract.ContractPackage#getIndexPricePurchaseContract_Index()
	 * @model required="true"
	 * @generated
	 */
	Index getIndex();

	/**
	 * Sets the value of the '{@link scenario.contract.IndexPricePurchaseContract#getIndex <em>Index</em>}' reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @param value the new value of the '<em>Index</em>' reference.
	 * @see #getIndex()
	 * @generated
	 */
	void setIndex(Index value);

} // IndexPricePurchaseContract
