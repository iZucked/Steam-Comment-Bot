/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package scenario.contract;

import scenario.market.Index;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Profit Sharing Purchase Contract</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link scenario.contract.ProfitSharingPurchaseContract#getIndex <em>Index</em>}</li>
 *   <li>{@link scenario.contract.ProfitSharingPurchaseContract#getReferenceIndex <em>Reference Index</em>}</li>
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
	 * Returns the value of the '<em><b>Index</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Index</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Index</em>' reference.
	 * @see #setIndex(Index)
	 * @see scenario.contract.ContractPackage#getProfitSharingPurchaseContract_Index()
	 * @model required="true"
	 * @generated
	 */
	Index getIndex();

	/**
	 * Sets the value of the '{@link scenario.contract.ProfitSharingPurchaseContract#getIndex <em>Index</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Index</em>' reference.
	 * @see #getIndex()
	 * @generated
	 */
	void setIndex(Index value);

	/**
	 * Returns the value of the '<em><b>Reference Index</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Reference Index</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Reference Index</em>' reference.
	 * @see #setReferenceIndex(Index)
	 * @see scenario.contract.ContractPackage#getProfitSharingPurchaseContract_ReferenceIndex()
	 * @model required="true"
	 * @generated
	 */
	Index getReferenceIndex();

	/**
	 * Sets the value of the '{@link scenario.contract.ProfitSharingPurchaseContract#getReferenceIndex <em>Reference Index</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Reference Index</em>' reference.
	 * @see #getReferenceIndex()
	 * @generated
	 */
	void setReferenceIndex(Index value);

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
