/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.analytics;

import com.mmxlabs.models.lng.cargo.VesselCharter;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Swap Value Matrix Parameters</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixParameters#getBaseLoad <em>Base Load</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixParameters#getBaseDischarge <em>Base Discharge</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixParameters#getBaseVesselCharter <em>Base Vessel Charter</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixParameters#getBasePriceRange <em>Base Price Range</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixParameters#getSwapLoadMarket <em>Swap Load Market</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixParameters#getSwapDischargeMarket <em>Swap Discharge Market</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixParameters#getSwapFee <em>Swap Fee</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixParameters#getSwapPriceRange <em>Swap Price Range</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getSwapValueMatrixParameters()
 * @model
 * @generated
 */
public interface SwapValueMatrixParameters extends EObject {
	/**
	 * Returns the value of the '<em><b>Base Load</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Base Load</em>' containment reference.
	 * @see #setBaseLoad(BuyReference)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getSwapValueMatrixParameters_BaseLoad()
	 * @model containment="true"
	 * @generated
	 */
	BuyReference getBaseLoad();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixParameters#getBaseLoad <em>Base Load</em>}' containment reference.
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
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getSwapValueMatrixParameters_BaseDischarge()
	 * @model containment="true"
	 * @generated
	 */
	SellReference getBaseDischarge();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixParameters#getBaseDischarge <em>Base Discharge</em>}' containment reference.
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
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getSwapValueMatrixParameters_BaseVesselCharter()
	 * @model containment="true"
	 * @generated
	 */
	ExistingVesselCharterOption getBaseVesselCharter();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixParameters#getBaseVesselCharter <em>Base Vessel Charter</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Base Vessel Charter</em>' containment reference.
	 * @see #getBaseVesselCharter()
	 * @generated
	 */
	void setBaseVesselCharter(ExistingVesselCharterOption value);

	/**
	 * Returns the value of the '<em><b>Base Price Range</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Base Price Range</em>' containment reference.
	 * @see #setBasePriceRange(Range)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getSwapValueMatrixParameters_BasePriceRange()
	 * @model containment="true"
	 * @generated
	 */
	Range getBasePriceRange();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixParameters#getBasePriceRange <em>Base Price Range</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Base Price Range</em>' containment reference.
	 * @see #getBasePriceRange()
	 * @generated
	 */
	void setBasePriceRange(Range value);

	/**
	 * Returns the value of the '<em><b>Swap Load Market</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Swap Load Market</em>' containment reference.
	 * @see #setSwapLoadMarket(BuyMarket)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getSwapValueMatrixParameters_SwapLoadMarket()
	 * @model containment="true"
	 * @generated
	 */
	BuyMarket getSwapLoadMarket();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixParameters#getSwapLoadMarket <em>Swap Load Market</em>}' containment reference.
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
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getSwapValueMatrixParameters_SwapDischargeMarket()
	 * @model containment="true"
	 * @generated
	 */
	SellMarket getSwapDischargeMarket();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixParameters#getSwapDischargeMarket <em>Swap Discharge Market</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Swap Discharge Market</em>' containment reference.
	 * @see #getSwapDischargeMarket()
	 * @generated
	 */
	void setSwapDischargeMarket(SellMarket value);

	/**
	 * Returns the value of the '<em><b>Swap Fee</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Swap Fee</em>' attribute.
	 * @see #setSwapFee(double)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getSwapValueMatrixParameters_SwapFee()
	 * @model
	 * @generated
	 */
	double getSwapFee();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixParameters#getSwapFee <em>Swap Fee</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Swap Fee</em>' attribute.
	 * @see #getSwapFee()
	 * @generated
	 */
	void setSwapFee(double value);

	/**
	 * Returns the value of the '<em><b>Swap Price Range</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Swap Price Range</em>' containment reference.
	 * @see #setSwapPriceRange(Range)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getSwapValueMatrixParameters_SwapPriceRange()
	 * @model containment="true"
	 * @generated
	 */
	Range getSwapPriceRange();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.SwapValueMatrixParameters#getSwapPriceRange <em>Swap Price Range</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Swap Price Range</em>' containment reference.
	 * @see #getSwapPriceRange()
	 * @generated
	 */
	void setSwapPriceRange(Range value);

} // SwapValueMatrixParameters
