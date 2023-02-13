/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.pricing;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Suez Canal Tariff</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.pricing.SuezCanalTariff#getBands <em>Bands</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.SuezCanalTariff#getTugBands <em>Tug Bands</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.SuezCanalTariff#getRouteRebates <em>Route Rebates</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.SuezCanalTariff#getTugCost <em>Tug Cost</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.SuezCanalTariff#getFixedCosts <em>Fixed Costs</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.SuezCanalTariff#getDiscountFactor <em>Discount Factor</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.SuezCanalTariff#getSdrToUSD <em>Sdr To USD</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getSuezCanalTariff()
 * @model
 * @generated
 */
public interface SuezCanalTariff extends EObject {
	/**
	 * Returns the value of the '<em><b>Bands</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.pricing.SuezCanalTariffBand}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Bands</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Bands</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getSuezCanalTariff_Bands()
	 * @model containment="true"
	 * @generated
	 */
	EList<SuezCanalTariffBand> getBands();

	/**
	 * Returns the value of the '<em><b>Tug Bands</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.pricing.SuezCanalTugBand}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Tug Bands</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Tug Bands</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getSuezCanalTariff_TugBands()
	 * @model containment="true"
	 * @generated
	 */
	EList<SuezCanalTugBand> getTugBands();

	/**
	 * Returns the value of the '<em><b>Route Rebates</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.pricing.SuezCanalRouteRebate}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Route Rebates</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getSuezCanalTariff_RouteRebates()
	 * @model containment="true"
	 * @generated
	 */
	EList<SuezCanalRouteRebate> getRouteRebates();

	/**
	 * Returns the value of the '<em><b>Tug Cost</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Tug Cost</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Tug Cost</em>' attribute.
	 * @see #setTugCost(double)
	 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getSuezCanalTariff_TugCost()
	 * @model annotation="http://www.mmxlabs.com/models/ui/numberFormat unitPrefix='$' formatString='##,###,##0'"
	 * @generated
	 */
	double getTugCost();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.pricing.SuezCanalTariff#getTugCost <em>Tug Cost</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Tug Cost</em>' attribute.
	 * @see #getTugCost()
	 * @generated
	 */
	void setTugCost(double value);

	/**
	 * Returns the value of the '<em><b>Fixed Costs</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Fixed Costs</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Fixed Costs</em>' attribute.
	 * @see #setFixedCosts(double)
	 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getSuezCanalTariff_FixedCosts()
	 * @model annotation="http://www.mmxlabs.com/models/ui/numberFormat unitPrefix='$' formatString='##,###,##0'"
	 * @generated
	 */
	double getFixedCosts();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.pricing.SuezCanalTariff#getFixedCosts <em>Fixed Costs</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Fixed Costs</em>' attribute.
	 * @see #getFixedCosts()
	 * @generated
	 */
	void setFixedCosts(double value);

	/**
	 * Returns the value of the '<em><b>Discount Factor</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Discount Factor</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Discount Factor</em>' attribute.
	 * @see #setDiscountFactor(double)
	 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getSuezCanalTariff_DiscountFactor()
	 * @model annotation="http://www.mmxlabs.com/models/ui/numberFormat scale='100' formatString='##0.#' unit='%'"
	 * @generated
	 */
	double getDiscountFactor();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.pricing.SuezCanalTariff#getDiscountFactor <em>Discount Factor</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Discount Factor</em>' attribute.
	 * @see #getDiscountFactor()
	 * @generated
	 */
	void setDiscountFactor(double value);

	/**
	 * Returns the value of the '<em><b>Sdr To USD</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Sdr To USD</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Sdr To USD</em>' attribute.
	 * @see #setSdrToUSD(String)
	 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getSuezCanalTariff_SdrToUSD()
	 * @model annotation="http://www.mmxlabs.com/models/pricing/expressionType type='currency'"
	 * @generated
	 */
	String getSdrToUSD();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.pricing.SuezCanalTariff#getSdrToUSD <em>Sdr To USD</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Sdr To USD</em>' attribute.
	 * @see #getSdrToUSD()
	 * @generated
	 */
	void setSdrToUSD(String value);

} // SuezCanalTariff
