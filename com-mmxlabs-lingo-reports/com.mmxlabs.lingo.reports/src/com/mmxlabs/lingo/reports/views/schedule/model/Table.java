/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.lingo.reports.views.schedule.model;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Table</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.mmxlabs.lingo.reports.views.schedule.model.Table#getRows <em>Rows</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.schedule.model.Table#getCycleGroups <em>Cycle Groups</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.schedule.model.Table#getRowGroups <em>Row Groups</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.schedule.model.Table#getScenarios <em>Scenarios</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.schedule.model.Table#getPinnedScenario <em>Pinned Scenario</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.mmxlabs.lingo.reports.views.schedule.model.ScheduleReportPackage#getTable()
 * @model
 * @generated
 */
public interface Table extends EObject {
	/**
	 * Returns the value of the '<em><b>Rows</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.lingo.reports.views.schedule.model.Row}.
	 * It is bidirectional and its opposite is '{@link com.mmxlabs.lingo.reports.views.schedule.model.Row#getTable <em>Table</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Rows</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Rows</em>' containment reference list.
	 * @see com.mmxlabs.lingo.reports.views.schedule.model.ScheduleReportPackage#getTable_Rows()
	 * @see com.mmxlabs.lingo.reports.views.schedule.model.Row#getTable
	 * @model opposite="table" containment="true"
	 * @generated
	 */
	EList<Row> getRows();

	/**
	 * Returns the value of the '<em><b>Cycle Groups</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.lingo.reports.views.schedule.model.CycleGroup}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Cycle Groups</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Cycle Groups</em>' containment reference list.
	 * @see com.mmxlabs.lingo.reports.views.schedule.model.ScheduleReportPackage#getTable_CycleGroups()
	 * @model containment="true"
	 * @generated
	 */
	EList<CycleGroup> getCycleGroups();

	/**
	 * Returns the value of the '<em><b>Row Groups</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.lingo.reports.views.schedule.model.RowGroup}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Row Groups</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Row Groups</em>' containment reference list.
	 * @see com.mmxlabs.lingo.reports.views.schedule.model.ScheduleReportPackage#getTable_RowGroups()
	 * @model containment="true"
	 * @generated
	 */
	EList<RowGroup> getRowGroups();

	/**
	 * Returns the value of the '<em><b>Scenarios</b></em>' reference list.
	 * The list contents are of type {@link org.eclipse.emf.ecore.EObject}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Scenarios</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Scenarios</em>' reference list.
	 * @see com.mmxlabs.lingo.reports.views.schedule.model.ScheduleReportPackage#getTable_Scenarios()
	 * @model
	 * @generated
	 */
	EList<EObject> getScenarios();

	/**
	 * Returns the value of the '<em><b>Pinned Scenario</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Pinned Scenario</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Pinned Scenario</em>' reference.
	 * @see #setPinnedScenario(EObject)
	 * @see com.mmxlabs.lingo.reports.views.schedule.model.ScheduleReportPackage#getTable_PinnedScenario()
	 * @model
	 * @generated
	 */
	EObject getPinnedScenario();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.schedule.model.Table#getPinnedScenario <em>Pinned Scenario</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Pinned Scenario</em>' reference.
	 * @see #getPinnedScenario()
	 * @generated
	 */
	void setPinnedScenario(EObject value);

} // Table
