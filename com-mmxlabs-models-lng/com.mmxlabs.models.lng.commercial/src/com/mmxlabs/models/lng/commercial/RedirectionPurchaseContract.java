

/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package com.mmxlabs.models.lng.commercial;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.types.AVesselClass;

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
 *   <li>{@link com.mmxlabs.models.lng.commercial.RedirectionPurchaseContract#getDesPurchasePort <em>Des Purchase Port</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.RedirectionPurchaseContract#getSourcePurchasePort <em>Source Purchase Port</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.RedirectionPurchaseContract#getProfitShare <em>Profit Share</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.RedirectionPurchaseContract#getVesselClass <em>Vessel Class</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.RedirectionPurchaseContract#getHireCost <em>Hire Cost</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.RedirectionPurchaseContract#getDaysFromSource <em>Days From Source</em>}</li>
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

	/**
	 * Returns the value of the '<em><b>Des Purchase Port</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Des Purchase Port</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * @deprecated
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Des Purchase Port</em>' reference.
	 * @see #setDesPurchasePort(Port)
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getRedirectionPurchaseContract_DesPurchasePort()
	 * @model required="true"
	 * @generated
	 */
	Port getDesPurchasePort();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.commercial.RedirectionPurchaseContract#getDesPurchasePort <em>Des Purchase Port</em>}' reference.
	 * <!-- begin-user-doc -->
	 * @deprecated
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Des Purchase Port</em>' reference.
	 * @see #getDesPurchasePort()
	 * @generated
	 */
	void setDesPurchasePort(Port value);

	/**
	 * Returns the value of the '<em><b>Source Purchase Port</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Source Purchase Port</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Source Purchase Port</em>' reference.
	 * @see #setSourcePurchasePort(Port)
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getRedirectionPurchaseContract_SourcePurchasePort()
	 * @model required="true"
	 * @generated
	 */
	Port getSourcePurchasePort();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.commercial.RedirectionPurchaseContract#getSourcePurchasePort <em>Source Purchase Port</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Source Purchase Port</em>' reference.
	 * @see #getSourcePurchasePort()
	 * @generated
	 */
	void setSourcePurchasePort(Port value);

	/**
	 * Returns the value of the '<em><b>Profit Share</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Profit Share</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Profit Share</em>' attribute.
	 * @see #setProfitShare(double)
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getRedirectionPurchaseContract_ProfitShare()
	 * @model
	 * @generated
	 */
	double getProfitShare();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.commercial.RedirectionPurchaseContract#getProfitShare <em>Profit Share</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Profit Share</em>' attribute.
	 * @see #getProfitShare()
	 * @generated
	 */
	void setProfitShare(double value);

	/**
	 * Returns the value of the '<em><b>Vessel Class</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Vessel Class</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Vessel Class</em>' reference.
	 * @see #setVesselClass(VesselClass)
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getRedirectionPurchaseContract_VesselClass()
	 * @model
	 * @generated
	 */
	AVesselClass getVesselClass();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.commercial.RedirectionPurchaseContract#getVesselClass <em>Vessel Class</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Vessel Class</em>' reference.
	 * @see #getVesselClass()
	 * @generated
	 */
	void setVesselClass(AVesselClass value);

	/**
	 * Returns the value of the '<em><b>Hire Cost</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Hire Cost</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Hire Cost</em>' attribute.
	 * @see #setHireCost(int)
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getRedirectionPurchaseContract_HireCost()
	 * @model
	 * @generated
	 */
	int getHireCost();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.commercial.RedirectionPurchaseContract#getHireCost <em>Hire Cost</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Hire Cost</em>' attribute.
	 * @see #getHireCost()
	 * @generated
	 */
	void setHireCost(int value);

	/**
	 * Returns the value of the '<em><b>Days From Source</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Days From Source</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Days From Source</em>' attribute.
	 * @see #setDaysFromSource(int)
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getRedirectionPurchaseContract_DaysFromSource()
	 * @model
	 * @generated
	 */
	int getDaysFromSource();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.commercial.RedirectionPurchaseContract#getDaysFromSource <em>Days From Source</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Days From Source</em>' attribute.
	 * @see #getDaysFromSource()
	 * @generated
	 */
	void setDaysFromSource(int value);

} // end of  RedirectionPurchaseContract

// finish type fixing
