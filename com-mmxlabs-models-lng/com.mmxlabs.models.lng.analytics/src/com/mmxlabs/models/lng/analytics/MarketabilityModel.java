/**
 */
package com.mmxlabs.models.lng.analytics;

import com.mmxlabs.models.lng.spotmarkets.SpotMarket;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Marketability Model</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.analytics.MarketabilityModel#getRows <em>Rows</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.MarketabilityModel#getMarkets <em>Markets</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getMarketabilityModel()
 * @model
 * @generated
 */
public interface MarketabilityModel extends AbstractAnalysisModel {
	/**
	 * Returns the value of the '<em><b>Rows</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.analytics.MarketabilityRow}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Rows</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getMarketabilityModel_Rows()
	 * @model containment="true"
	 * @generated
	 */
	EList<MarketabilityRow> getRows();

	/**
	 * Returns the value of the '<em><b>Markets</b></em>' reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.spotmarkets.SpotMarket}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Markets</em>' reference list.
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getMarketabilityModel_Markets()
	 * @model
	 * @generated
	 */
	EList<SpotMarket> getMarkets();

} // MarketabilityModel
