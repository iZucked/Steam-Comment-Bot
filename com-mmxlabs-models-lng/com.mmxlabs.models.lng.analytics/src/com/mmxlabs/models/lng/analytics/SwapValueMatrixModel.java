/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.analytics;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Swap Value Matrix Model</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixModel#getBaseLoad <em>Base Load</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixModel#getBaseDischarge <em>Base Discharge</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixModel#getBaseVesselCharter <em>Base Vessel Charter</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixModel#getSwapLoadMarket <em>Swap Load Market</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixModel#getSwapDischargeMarket <em>Swap Discharge Market</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixModel#getBaseDischargeMinPrice <em>Base Discharge Min Price</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixModel#getBaseDischargeMaxPrice <em>Base Discharge Max Price</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixModel#getBaseDischargeStepSize <em>Base Discharge Step Size</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixModel#getMarketMinPrice <em>Market Min Price</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixModel#getMarketMaxPrice <em>Market Max Price</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixModel#getMarketStepSize <em>Market Step Size</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixModel#getSwapFee <em>Swap Fee</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixModel#getSwapValueMatrixResult <em>Swap Value Matrix Result</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getSwapValueMatrixModel()
 * @model
 * @generated
 */
public interface SwapValueMatrixModel extends AbstractAnalysisModel {
	/**
	 * Returns the value of the '<em><b>Base Load</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Base Load</em>' containment reference.
	 * @see #setBaseLoad(BuyReference)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getSwapValueMatrixModel_BaseLoad()
	 * @model containment="true"
	 * @generated
	 */
	BuyReference getBaseLoad();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixModel#getBaseLoad <em>Base Load</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Base Load</em>' containment reference.
	 * @see #getBaseLoad()
	 * @generated
	 */
	void setBaseLoad(BuyReference value);

	/**
	 * Returns the value of the '<em><b>Base Discharge</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Base Discharge</em>' containment reference.
	 * @see #setBaseDischarge(SellReference)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getSwapValueMatrixModel_BaseDischarge()
	 * @model containment="true"
	 * @generated
	 */
	SellReference getBaseDischarge();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixModel#getBaseDischarge <em>Base Discharge</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Base Discharge</em>' containment reference.
	 * @see #getBaseDischarge()
	 * @generated
	 */
	void setBaseDischarge(SellReference value);

	/**
	 * Returns the value of the '<em><b>Base Vessel Charter</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Base Vessel Charter</em>' containment reference.
	 * @see #setBaseVesselCharter(ExistingVesselCharterOption)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getSwapValueMatrixModel_BaseVesselCharter()
	 * @model containment="true"
	 * @generated
	 */
	ExistingVesselCharterOption getBaseVesselCharter();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixModel#getBaseVesselCharter <em>Base Vessel Charter</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Base Vessel Charter</em>' containment reference.
	 * @see #getBaseVesselCharter()
	 * @generated
	 */
	void setBaseVesselCharter(ExistingVesselCharterOption value);

	/**
	 * Returns the value of the '<em><b>Swap Load Market</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Swap Load Market</em>' containment reference.
	 * @see #setSwapLoadMarket(BuyMarket)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getSwapValueMatrixModel_SwapLoadMarket()
	 * @model containment="true"
	 * @generated
	 */
	BuyMarket getSwapLoadMarket();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixModel#getSwapLoadMarket <em>Swap Load Market</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Swap Load Market</em>' containment reference.
	 * @see #getSwapLoadMarket()
	 * @generated
	 */
	void setSwapLoadMarket(BuyMarket value);

	/**
	 * Returns the value of the '<em><b>Swap Discharge Market</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Swap Discharge Market</em>' containment reference.
	 * @see #setSwapDischargeMarket(SellMarket)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getSwapValueMatrixModel_SwapDischargeMarket()
	 * @model containment="true"
	 * @generated
	 */
	SellMarket getSwapDischargeMarket();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixModel#getSwapDischargeMarket <em>Swap Discharge Market</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Swap Discharge Market</em>' containment reference.
	 * @see #getSwapDischargeMarket()
	 * @generated
	 */
	void setSwapDischargeMarket(SellMarket value);

	/**
	 * Returns the value of the '<em><b>Base Discharge Min Price</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Base Discharge Min Price</em>' attribute.
	 * @see #setBaseDischargeMinPrice(int)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getSwapValueMatrixModel_BaseDischargeMinPrice()
	 * @model
	 * @generated
	 */
	int getBaseDischargeMinPrice();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixModel#getBaseDischargeMinPrice <em>Base Discharge Min Price</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Base Discharge Min Price</em>' attribute.
	 * @see #getBaseDischargeMinPrice()
	 * @generated
	 */
	void setBaseDischargeMinPrice(int value);

	/**
	 * Returns the value of the '<em><b>Base Discharge Max Price</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Base Discharge Max Price</em>' attribute.
	 * @see #setBaseDischargeMaxPrice(int)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getSwapValueMatrixModel_BaseDischargeMaxPrice()
	 * @model
	 * @generated
	 */
	int getBaseDischargeMaxPrice();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixModel#getBaseDischargeMaxPrice <em>Base Discharge Max Price</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Base Discharge Max Price</em>' attribute.
	 * @see #getBaseDischargeMaxPrice()
	 * @generated
	 */
	void setBaseDischargeMaxPrice(int value);

	/**
	 * Returns the value of the '<em><b>Base Discharge Step Size</b></em>' attribute.
	 * The default value is <code>"1"</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Base Discharge Step Size</em>' attribute.
	 * @see #setBaseDischargeStepSize(int)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getSwapValueMatrixModel_BaseDischargeStepSize()
	 * @model default="1"
	 * @generated
	 */
	int getBaseDischargeStepSize();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixModel#getBaseDischargeStepSize <em>Base Discharge Step Size</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Base Discharge Step Size</em>' attribute.
	 * @see #getBaseDischargeStepSize()
	 * @generated
	 */
	void setBaseDischargeStepSize(int value);

	/**
	 * Returns the value of the '<em><b>Market Min Price</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Market Min Price</em>' attribute.
	 * @see #setMarketMinPrice(int)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getSwapValueMatrixModel_MarketMinPrice()
	 * @model
	 * @generated
	 */
	int getMarketMinPrice();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixModel#getMarketMinPrice <em>Market Min Price</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Market Min Price</em>' attribute.
	 * @see #getMarketMinPrice()
	 * @generated
	 */
	void setMarketMinPrice(int value);

	/**
	 * Returns the value of the '<em><b>Market Max Price</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Market Max Price</em>' attribute.
	 * @see #setMarketMaxPrice(int)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getSwapValueMatrixModel_MarketMaxPrice()
	 * @model
	 * @generated
	 */
	int getMarketMaxPrice();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixModel#getMarketMaxPrice <em>Market Max Price</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Market Max Price</em>' attribute.
	 * @see #getMarketMaxPrice()
	 * @generated
	 */
	void setMarketMaxPrice(int value);

	/**
	 * Returns the value of the '<em><b>Market Step Size</b></em>' attribute.
	 * The default value is <code>"1"</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Market Step Size</em>' attribute.
	 * @see #setMarketStepSize(int)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getSwapValueMatrixModel_MarketStepSize()
	 * @model default="1"
	 * @generated
	 */
	int getMarketStepSize();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixModel#getMarketStepSize <em>Market Step Size</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Market Step Size</em>' attribute.
	 * @see #getMarketStepSize()
	 * @generated
	 */
	void setMarketStepSize(int value);

	/**
	 * Returns the value of the '<em><b>Swap Fee</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Swap Fee</em>' attribute.
	 * @see #setSwapFee(double)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getSwapValueMatrixModel_SwapFee()
	 * @model annotation="http://www.mmxlabs.com/models/ui/numberFormat formatString='##.##'"
	 * @generated
	 */
	double getSwapFee();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixModel#getSwapFee <em>Swap Fee</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Swap Fee</em>' attribute.
	 * @see #getSwapFee()
	 * @generated
	 */
	void setSwapFee(double value);

	/**
	 * Returns the value of the '<em><b>Swap Value Matrix Result</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Swap Value Matrix Result</em>' containment reference.
	 * @see #setSwapValueMatrixResult(SwapValueMatrixResultSet)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getSwapValueMatrixModel_SwapValueMatrixResult()
	 * @model containment="true"
	 * @generated
	 */
	SwapValueMatrixResultSet getSwapValueMatrixResult();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixModel#getSwapValueMatrixResult <em>Swap Value Matrix Result</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Swap Value Matrix Result</em>' containment reference.
	 * @see #getSwapValueMatrixResult()
	 * @generated
	 */
	void setSwapValueMatrixResult(SwapValueMatrixResultSet value);

} // SwapValueMatrixModel
