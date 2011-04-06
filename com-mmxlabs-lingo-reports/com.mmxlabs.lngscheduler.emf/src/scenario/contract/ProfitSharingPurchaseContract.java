/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package scenario.contract;

import scenario.market.Market;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Profit Sharing Purchase Contract</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link scenario.contract.ProfitSharingPurchaseContract#getMarket <em>Market</em>}</li>
 *   <li>{@link scenario.contract.ProfitSharingPurchaseContract#getReferenceMarket <em>Reference Market</em>}</li>
 *   <li>{@link scenario.contract.ProfitSharingPurchaseContract#getAlpha <em>Alpha</em>}</li>
 *   <li>{@link scenario.contract.ProfitSharingPurchaseContract#getBeta <em>Beta</em>}</li>
 *   <li>{@link scenario.contract.ProfitSharingPurchaseContract#getGamma <em>Gamma</em>}</li>
 * </ul>
 * </p>
 *
 * @see scenario.contract.ContractPackage#getProfitSharingPurchaseContract()
 * @model
 * @generated
 */
public interface ProfitSharingPurchaseContract extends PurchaseContract {

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
	 * @see scenario.contract.ContractPackage#getProfitSharingPurchaseContract_Market()
	 * @model required="true"
	 * @generated
	 */
	Market getMarket();

	/**
	 * Sets the value of the '{@link scenario.contract.ProfitSharingPurchaseContract#getMarket <em>Market</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Market</em>' reference.
	 * @see #getMarket()
	 * @generated
	 */
	void setMarket(Market value);

	/**
	 * Returns the value of the '<em><b>Reference Market</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Reference Market</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Reference Market</em>' reference.
	 * @see #setReferenceMarket(Market)
	 * @see scenario.contract.ContractPackage#getProfitSharingPurchaseContract_ReferenceMarket()
	 * @model required="true"
	 * @generated
	 */
	Market getReferenceMarket();

	/**
	 * Sets the value of the '{@link scenario.contract.ProfitSharingPurchaseContract#getReferenceMarket <em>Reference Market</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Reference Market</em>' reference.
	 * @see #getReferenceMarket()
	 * @generated
	 */
	void setReferenceMarket(Market value);

	/**
	 * Returns the value of the '<em><b>Alpha</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Alpha</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Alpha</em>' attribute.
	 * @see #setAlpha(float)
	 * @see scenario.contract.ContractPackage#getProfitSharingPurchaseContract_Alpha()
	 * @model required="true"
	 * @generated
	 */
	float getAlpha();

	/**
	 * Sets the value of the '{@link scenario.contract.ProfitSharingPurchaseContract#getAlpha <em>Alpha</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Alpha</em>' attribute.
	 * @see #getAlpha()
	 * @generated
	 */
	void setAlpha(float value);

	/**
	 * Returns the value of the '<em><b>Beta</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Beta</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Beta</em>' attribute.
	 * @see #setBeta(float)
	 * @see scenario.contract.ContractPackage#getProfitSharingPurchaseContract_Beta()
	 * @model required="true"
	 * @generated
	 */
	float getBeta();

	/**
	 * Sets the value of the '{@link scenario.contract.ProfitSharingPurchaseContract#getBeta <em>Beta</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Beta</em>' attribute.
	 * @see #getBeta()
	 * @generated
	 */
	void setBeta(float value);

	/**
	 * Returns the value of the '<em><b>Gamma</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Gamma</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Gamma</em>' attribute.
	 * @see #setGamma(float)
	 * @see scenario.contract.ContractPackage#getProfitSharingPurchaseContract_Gamma()
	 * @model required="true"
	 * @generated
	 */
	float getGamma();

	/**
	 * Sets the value of the '{@link scenario.contract.ProfitSharingPurchaseContract#getGamma <em>Gamma</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Gamma</em>' attribute.
	 * @see #getGamma()
	 * @generated
	 */
	void setGamma(float value);
} // ProfitSharingPurchaseContract
