/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.analytics;

import com.mmxlabs.models.lng.cargo.CharterInMarketOverride;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.lng.spotmarkets.CharterInMarket;
import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Result Set</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.analytics.ResultSet#getRows <em>Rows</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.ResultSet#getProfitAndLoss <em>Profit And Loss</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.ResultSet#getScheduleModel <em>Schedule Model</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getResultSet()
 * @model
 * @generated
 */
public interface ResultSet extends EObject {
	/**
	 * Returns the value of the '<em><b>Rows</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.analytics.AnalysisResultRow}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Rows</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Rows</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getResultSet_Rows()
	 * @model containment="true"
	 * @generated
	 */
	EList<AnalysisResultRow> getRows();

	/**
	 * Returns the value of the '<em><b>Profit And Loss</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Profit And Loss</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Profit And Loss</em>' attribute.
	 * @see #setProfitAndLoss(long)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getResultSet_ProfitAndLoss()
	 * @model
	 * @generated
	 */
	long getProfitAndLoss();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.ResultSet#getProfitAndLoss <em>Profit And Loss</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Profit And Loss</em>' attribute.
	 * @see #getProfitAndLoss()
	 * @generated
	 */
	void setProfitAndLoss(long value);

	/**
	 * Returns the value of the '<em><b>Schedule Model</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Schedule Model</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Schedule Model</em>' containment reference.
	 * @see #setScheduleModel(ScheduleModel)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getResultSet_ScheduleModel()
	 * @model containment="true"
	 * @generated
	 */
	ScheduleModel getScheduleModel();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.ResultSet#getScheduleModel <em>Schedule Model</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Schedule Model</em>' containment reference.
	 * @see #getScheduleModel()
	 * @generated
	 */
	void setScheduleModel(ScheduleModel value);

} // ResultSet
