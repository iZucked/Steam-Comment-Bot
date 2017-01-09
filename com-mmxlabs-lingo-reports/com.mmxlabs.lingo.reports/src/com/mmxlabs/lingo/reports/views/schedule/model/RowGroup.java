/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.lingo.reports.views.schedule.model;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Row Group</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.lingo.reports.views.schedule.model.RowGroup#getRows <em>Rows</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.lingo.reports.views.schedule.model.ScheduleReportPackage#getRowGroup()
 * @model
 * @generated
 */
public interface RowGroup extends EObject {
	/**
	 * Returns the value of the '<em><b>Rows</b></em>' reference list.
	 * The list contents are of type {@link com.mmxlabs.lingo.reports.views.schedule.model.Row}.
	 * It is bidirectional and its opposite is '{@link com.mmxlabs.lingo.reports.views.schedule.model.Row#getRowGroup <em>Row Group</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Rows</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Rows</em>' reference list.
	 * @see com.mmxlabs.lingo.reports.views.schedule.model.ScheduleReportPackage#getRowGroup_Rows()
	 * @see com.mmxlabs.lingo.reports.views.schedule.model.Row#getRowGroup
	 * @model opposite="rowGroup"
	 * @generated
	 */
	EList<Row> getRows();

} // RowGroup
