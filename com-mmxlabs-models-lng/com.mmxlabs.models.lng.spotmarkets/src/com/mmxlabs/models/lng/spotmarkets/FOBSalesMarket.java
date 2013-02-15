

/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package com.mmxlabs.models.lng.spotmarkets;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.commercial.SalesContract;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>FOB Sales Market</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.spotmarkets.FOBSalesMarket#getLoadPort <em>Load Port</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.spotmarkets.FOBSalesMarket#getContract <em>Contract</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage#getFOBSalesMarket()
 * @model
 * @generated
 */
public interface FOBSalesMarket extends SpotMarket {
	/**
	 * Returns the value of the '<em><b>Load Port</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Load Port</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Load Port</em>' reference.
	 * @see #setLoadPort(Port)
	 * @see com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage#getFOBSalesMarket_LoadPort()
	 * @model required="true"
	 * @generated
	 */
	Port getLoadPort();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.spotmarkets.FOBSalesMarket#getLoadPort <em>Load Port</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Load Port</em>' reference.
	 * @see #getLoadPort()
	 * @generated
	 */
	void setLoadPort(Port value);

	/**
	 * Returns the value of the '<em><b>Contract</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Contract</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Contract</em>' reference.
	 * @see #setContract(SalesContract)
	 * @see com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage#getFOBSalesMarket_Contract()
	 * @model required="true"
	 * @generated
	 */
	SalesContract getContract();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.spotmarkets.FOBSalesMarket#getContract <em>Contract</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Contract</em>' reference.
	 * @see #getContract()
	 * @generated
	 */
	void setContract(SalesContract value);

} // end of  FOBSalesMarket

// finish type fixing
