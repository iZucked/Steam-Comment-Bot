/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.lingo.reports.views.schedule.model;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Composite Row</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.lingo.reports.views.schedule.model.CompositeRow#getPreviousRow <em>Previous Row</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.schedule.model.CompositeRow#getPinnedRow <em>Pinned Row</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.lingo.reports.views.schedule.model.ScheduleReportPackage#getCompositeRow()
 * @model
 * @generated
 */
public interface CompositeRow extends EObject {
	/**
	 * Returns the value of the '<em><b>Previous Row</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Previous Row</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Previous Row</em>' reference.
	 * @see #setPreviousRow(Row)
	 * @see com.mmxlabs.lingo.reports.views.schedule.model.ScheduleReportPackage#getCompositeRow_PreviousRow()
	 * @model
	 * @generated
	 */
	Row getPreviousRow();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.schedule.model.CompositeRow#getPreviousRow <em>Previous Row</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Previous Row</em>' reference.
	 * @see #getPreviousRow()
	 * @generated
	 */
	void setPreviousRow(Row value);

	/**
	 * Returns the value of the '<em><b>Pinned Row</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Pinned Row</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Pinned Row</em>' reference.
	 * @see #setPinnedRow(Row)
	 * @see com.mmxlabs.lingo.reports.views.schedule.model.ScheduleReportPackage#getCompositeRow_PinnedRow()
	 * @model
	 * @generated
	 */
	Row getPinnedRow();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.schedule.model.CompositeRow#getPinnedRow <em>Pinned Row</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Pinned Row</em>' reference.
	 * @see #getPinnedRow()
	 * @generated
	 */
	void setPinnedRow(Row value);

} // CompositeRow
