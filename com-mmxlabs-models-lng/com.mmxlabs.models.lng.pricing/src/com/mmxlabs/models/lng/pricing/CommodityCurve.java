/**
 */
package com.mmxlabs.models.lng.pricing;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Commodity Curve</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.pricing.CommodityCurve#getMarketIndex <em>Market Index</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getCommodityCurve()
 * @model
 * @generated
 */
public interface CommodityCurve extends AbstractYearMonthCurve {

	/**
	 * Returns the value of the '<em><b>Market Index</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Market Index</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Market Index</em>' reference.
	 * @see #setMarketIndex(MarketIndex)
	 * @see com.mmxlabs.models.lng.pricing.PricingPackage#getCommodityCurve_MarketIndex()
	 * @model
	 * @generated
	 */
	MarketIndex getMarketIndex();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.pricing.CommodityCurve#getMarketIndex <em>Market Index</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Market Index</em>' reference.
	 * @see #getMarketIndex()
	 * @generated
	 */
	void setMarketIndex(MarketIndex value);
} // CommodityCurve
