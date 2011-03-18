/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package scenario.contract;

import scenario.market.Market;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Market Price Purchase Contract</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link scenario.contract.MarketPricePurchaseContract#getMarket <em>Market</em>}</li>
 * </ul>
 * </p>
 *
 * @see scenario.contract.ContractPackage#getMarketPricePurchaseContract()
 * @model
 * @generated
 */
public interface MarketPricePurchaseContract extends PurchaseContract {
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
	 * @see scenario.contract.ContractPackage#getMarketPricePurchaseContract_Market()
	 * @model required="true"
	 * @generated
	 */
	Market getMarket();

	/**
	 * Sets the value of the '{@link scenario.contract.MarketPricePurchaseContract#getMarket <em>Market</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Market</em>' reference.
	 * @see #getMarket()
	 * @generated
	 */
	void setMarket(Market value);

} // MarketPricePurchaseContract
