/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.analytics;

import com.mmxlabs.models.lng.schedule.Schedule;
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
 *   <li>{@link com.mmxlabs.models.lng.analytics.MarketabilityModel#getVesselSpeed <em>Vessel Speed</em>}</li>
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

	/**
	 * Returns the value of the '<em><b>Vessel Speed</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Vessel Speed</em>' attribute.
	 * @see #isSetVesselSpeed()
	 * @see #unsetVesselSpeed()
	 * @see #setVesselSpeed(double)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getMarketabilityModel_VesselSpeed()
	 * @model unsettable="true"
	 * @generated
	 */
	double getVesselSpeed();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.MarketabilityModel#getVesselSpeed <em>Vessel Speed</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Vessel Speed</em>' attribute.
	 * @see #isSetVesselSpeed()
	 * @see #unsetVesselSpeed()
	 * @see #getVesselSpeed()
	 * @generated
	 */
	void setVesselSpeed(double value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.analytics.MarketabilityModel#getVesselSpeed <em>Vessel Speed</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetVesselSpeed()
	 * @see #getVesselSpeed()
	 * @see #setVesselSpeed(double)
	 * @generated
	 */
	void unsetVesselSpeed();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.analytics.MarketabilityModel#getVesselSpeed <em>Vessel Speed</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Vessel Speed</em>' attribute is set.
	 * @see #unsetVesselSpeed()
	 * @see #getVesselSpeed()
	 * @see #setVesselSpeed(double)
	 * @generated
	 */
	boolean isSetVesselSpeed();

} // MarketabilityModel
