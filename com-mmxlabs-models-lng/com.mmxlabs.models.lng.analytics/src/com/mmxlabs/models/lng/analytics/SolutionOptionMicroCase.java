/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.analytics;

import com.mmxlabs.models.lng.cargo.CharterInMarketOverride;
import com.mmxlabs.models.lng.cargo.ScheduleSpecification;
import com.mmxlabs.models.lng.cargo.VesselCharter;

import com.mmxlabs.models.lng.schedule.ScheduleModel;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Solution Option Micro Case</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.analytics.SolutionOptionMicroCase#getScheduleSpecification <em>Schedule Specification</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.SolutionOptionMicroCase#getScheduleModel <em>Schedule Model</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.SolutionOptionMicroCase#getExtraVesselCharters <em>Extra Vessel Charters</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.SolutionOptionMicroCase#getCharterInMarketOverrides <em>Charter In Market Overrides</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getSolutionOptionMicroCase()
 * @model
 * @generated
 */
public interface SolutionOptionMicroCase extends EObject {
	/**
	 * Returns the value of the '<em><b>Schedule Specification</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Schedule Specification</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Schedule Specification</em>' containment reference.
	 * @see #setScheduleSpecification(ScheduleSpecification)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getSolutionOptionMicroCase_ScheduleSpecification()
	 * @model containment="true"
	 * @generated
	 */
	ScheduleSpecification getScheduleSpecification();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.SolutionOptionMicroCase#getScheduleSpecification <em>Schedule Specification</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Schedule Specification</em>' containment reference.
	 * @see #getScheduleSpecification()
	 * @generated
	 */
	void setScheduleSpecification(ScheduleSpecification value);

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
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getSolutionOptionMicroCase_ScheduleModel()
	 * @model containment="true"
	 * @generated
	 */
	ScheduleModel getScheduleModel();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.SolutionOptionMicroCase#getScheduleModel <em>Schedule Model</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Schedule Model</em>' containment reference.
	 * @see #getScheduleModel()
	 * @generated
	 */
	void setScheduleModel(ScheduleModel value);

	/**
	 * Returns the value of the '<em><b>Extra Vessel Charters</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.cargo.VesselCharter}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Extra Vessel Charters</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getSolutionOptionMicroCase_ExtraVesselCharters()
	 * @model containment="true"
	 * @generated
	 */
	EList<VesselCharter> getExtraVesselCharters();

	/**
	 * Returns the value of the '<em><b>Charter In Market Overrides</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.models.lng.cargo.CharterInMarketOverride}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Charter In Market Overrides</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Charter In Market Overrides</em>' containment reference list.
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getSolutionOptionMicroCase_CharterInMarketOverrides()
	 * @model containment="true"
	 * @generated
	 */
	EList<CharterInMarketOverride> getCharterInMarketOverrides();

} // SolutionOptionMicroCase
