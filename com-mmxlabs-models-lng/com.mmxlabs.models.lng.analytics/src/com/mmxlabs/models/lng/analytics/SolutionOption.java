/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.analytics;

import com.mmxlabs.models.lng.cargo.ScheduleSpecification;
import com.mmxlabs.models.lng.schedule.ScheduleModel;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Solution Option</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.analytics.SolutionOption#getChangeDescription <em>Change Description</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.SolutionOption#getScheduleSpecification <em>Schedule Specification</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.SolutionOption#getScheduleModel <em>Schedule Model</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getSolutionOption()
 * @model
 * @generated
 */
public interface SolutionOption extends EObject {
	/**
	 * Returns the value of the '<em><b>Change Description</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Change Description</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Change Description</em>' containment reference.
	 * @see #setChangeDescription(ChangeDescription)
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getSolutionOption_ChangeDescription()
	 * @model containment="true"
	 * @generated
	 */
	ChangeDescription getChangeDescription();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.SolutionOption#getChangeDescription <em>Change Description</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Change Description</em>' containment reference.
	 * @see #getChangeDescription()
	 * @generated
	 */
	void setChangeDescription(ChangeDescription value);

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
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getSolutionOption_ScheduleSpecification()
	 * @model containment="true"
	 * @generated
	 */
	ScheduleSpecification getScheduleSpecification();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.SolutionOption#getScheduleSpecification <em>Schedule Specification</em>}' containment reference.
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
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#getSolutionOption_ScheduleModel()
	 * @model containment="true"
	 * @generated
	 */
	ScheduleModel getScheduleModel();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.analytics.SolutionOption#getScheduleModel <em>Schedule Model</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Schedule Model</em>' containment reference.
	 * @see #getScheduleModel()
	 * @generated
	 */
	void setScheduleModel(ScheduleModel value);

} // SolutionOption
