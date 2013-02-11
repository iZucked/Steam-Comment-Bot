

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
 * A representation of the model object '<em><b>Redirection Price Parameters</b></em>'.
 * @since 3.0
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.commercial.RedirectionPriceParameters#getBaseSalesMarketPort <em>Base Sales Market Port</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.RedirectionPriceParameters#getBaseSalesPriceExpression <em>Base Sales Price Expression</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.RedirectionPriceParameters#getBasePurchasePriceExpression <em>Base Purchase Price Expression</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.RedirectionPriceParameters#getNotionalSpeed <em>Notional Speed</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.RedirectionPriceParameters#getDesPurchasePort <em>Des Purchase Port</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.RedirectionPriceParameters#getSourcePurchasePort <em>Source Purchase Port</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.RedirectionPriceParameters#getProfitShare <em>Profit Share</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.RedirectionPriceParameters#getVesselClass <em>Vessel Class</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.RedirectionPriceParameters#getHireCost <em>Hire Cost</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.commercial.RedirectionPriceParameters#getDaysFromSource <em>Days From Source</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getRedirectionPriceParameters()
 * @model
 * @generated
 */
public interface RedirectionPriceParameters extends LNGPriceCalculatorParameters {
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
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getRedirectionPriceParameters_BaseSalesMarketPort()
	 * @model required="true"
	 * @generated
	 */
	Port getBaseSalesMarketPort();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.commercial.RedirectionPriceParameters#getBaseSalesMarketPort <em>Base Sales Market Port</em>}' reference.
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
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getRedirectionPriceParameters_BaseSalesPriceExpression()
	 * @model
	 * @generated
	 */
	String getBaseSalesPriceExpression();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.commercial.RedirectionPriceParameters#getBaseSalesPriceExpression <em>Base Sales Price Expression</em>}' attribute.
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
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getRedirectionPriceParameters_BasePurchasePriceExpression()
	 * @model
	 * @generated
	 */
	String getBasePurchasePriceExpression();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.commercial.RedirectionPriceParameters#getBasePurchasePriceExpression <em>Base Purchase Price Expression</em>}' attribute.
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
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getRedirectionPriceParameters_NotionalSpeed()
	 * @model
	 * @generated
	 */
	double getNotionalSpeed();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.commercial.RedirectionPriceParameters#getNotionalSpeed <em>Notional Speed</em>}' attribute.
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
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Des Purchase Port</em>' reference.
	 * @see #setDesPurchasePort(Port)
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getRedirectionPriceParameters_DesPurchasePort()
	 * @model required="true"
	 * @generated
	 */
	Port getDesPurchasePort();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.commercial.RedirectionPriceParameters#getDesPurchasePort <em>Des Purchase Port</em>}' reference.
	 * <!-- begin-user-doc -->
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
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getRedirectionPriceParameters_SourcePurchasePort()
	 * @model required="true"
	 * @generated
	 */
	Port getSourcePurchasePort();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.commercial.RedirectionPriceParameters#getSourcePurchasePort <em>Source Purchase Port</em>}' reference.
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
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getRedirectionPriceParameters_ProfitShare()
	 * @model
	 * @generated
	 */
	double getProfitShare();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.commercial.RedirectionPriceParameters#getProfitShare <em>Profit Share</em>}' attribute.
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
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getRedirectionPriceParameters_VesselClass()
	 * @model
	 * @generated
	 */
	AVesselClass getVesselClass();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.commercial.RedirectionPriceParameters#getVesselClass <em>Vessel Class</em>}' reference.
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
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getRedirectionPriceParameters_HireCost()
	 * @model
	 * @generated
	 */
	int getHireCost();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.commercial.RedirectionPriceParameters#getHireCost <em>Hire Cost</em>}' attribute.
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
	 * @see com.mmxlabs.models.lng.commercial.CommercialPackage#getRedirectionPriceParameters_DaysFromSource()
	 * @model
	 * @generated
	 */
	int getDaysFromSource();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.commercial.RedirectionPriceParameters#getDaysFromSource <em>Days From Source</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Days From Source</em>' attribute.
	 * @see #getDaysFromSource()
	 * @generated
	 */
	void setDaysFromSource(int value);

} // end of  RedirectionPriceParameters

// finish type fixing
