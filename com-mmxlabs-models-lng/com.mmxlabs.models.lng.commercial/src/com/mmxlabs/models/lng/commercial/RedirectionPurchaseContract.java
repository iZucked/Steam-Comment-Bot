

/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package com.mmxlabs.models.lng.commercial;
import com.mmxlabs.models.lng.port.Port;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Redirection Purchase Contract</b></em>'.
 * @since 2.0
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.commercial.RedirectionPurchaseContract#getBaseSalesMarketPort <em>Base Sales Market Port</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.RedirectionPurchaseContract#getBaseSalesPriceExpression <em>Base Sales Price Expression</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.RedirectionPurchaseContract#getBasePurchasePriceExpression <em>Base Purchase Price Expression</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.RedirectionPurchaseContract#getNotionalSpeed <em>Notional Speed</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getRedirectionPurchaseContract()
 * @model
 * @generated
 */
public interface RedirectionPurchaseContract extends PurchaseContract {
	/**
	 * Returns the value of the '<em><b>Base Sales Market Port</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Base Sales Market Port</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Base Sales Market Port</em>' reference.
	 * @see #setBaseSalesMarketPort(Port)
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getRedirectionPurchaseContract_BaseSalesMarketPort()
	 * @model required="true"
	 * @generated
	 */
	Port getBaseSalesMarketPort();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.commercial.RedirectionPurchaseContract#getBaseSalesMarketPort <em>Base Sales Market Port</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Base Sales Market Port</em>' reference.
	 * @see #getBaseSalesMarketPort()
	 * @generated
	 */
	void setBaseSalesMarketPort(Port value);

	/**
	 * Returns the value of the '<em><b>Base Sales Price Expression</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Base Sales Price Expression</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Base Sales Price Expression</em>' attribute.
	 * @see #setBaseSalesPriceExpression(String)
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getRedirectionPurchaseContract_BaseSalesPriceExpression()
	 * @model
	 * @generated
	 */
	String getBaseSalesPriceExpression();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.commercial.RedirectionPurchaseContract#getBaseSalesPriceExpression <em>Base Sales Price Expression</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Base Sales Price Expression</em>' attribute.
	 * @see #getBaseSalesPriceExpression()
	 * @generated
	 */
	void setBaseSalesPriceExpression(String value);

	/**
	 * Returns the value of the '<em><b>Base Purchase Price Expression</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Base Purchase Price Expression</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Base Purchase Price Expression</em>' attribute.
	 * @see #setBasePurchasePriceExpression(String)
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getRedirectionPurchaseContract_BasePurchasePriceExpression()
	 * @model
	 * @generated
	 */
	String getBasePurchasePriceExpression();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.commercial.RedirectionPurchaseContract#getBasePurchasePriceExpression <em>Base Purchase Price Expression</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Base Purchase Price Expression</em>' attribute.
	 * @see #getBasePurchasePriceExpression()
	 * @generated
	 */
	void setBasePurchasePriceExpression(String value);

	/**
	 * Returns the value of the '<em><b>Notional Speed</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Notional Speed</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Notional Speed</em>' attribute.
	 * @see #setNotionalSpeed(double)
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getRedirectionPurchaseContract_NotionalSpeed()
	 * @model
	 * @generated
	 */
	double getNotionalSpeed();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.commercial.RedirectionPurchaseContract#getNotionalSpeed <em>Notional Speed</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Notional Speed</em>' attribute.
	 * @see #getNotionalSpeed()
	 * @generated
	 */
	void setNotionalSpeed(double value);

} // end of  RedirectionPurchaseContract

// finish type fixing
