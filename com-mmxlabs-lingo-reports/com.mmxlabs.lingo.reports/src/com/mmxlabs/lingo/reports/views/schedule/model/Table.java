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
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Rows</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Rows</em>' containment reference list.
	 * @see com.mmxlabs.lingo.reports.views.schedule.model.ScheduleReportPackage#getTable_Rows()
	 * @model containment="true"
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

} // Table
