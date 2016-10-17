/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.lingo.reports.views.schedule.model;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Diff Options</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.lingo.reports.views.schedule.model.DiffOptions#isFilterSelectedElements <em>Filter Selected Elements</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.schedule.model.DiffOptions#isFilterSelectedSequences <em>Filter Selected Sequences</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.lingo.reports.views.schedule.model.ScheduleReportPackage#getDiffOptions()
 * @model
 * @generated
 */
public interface DiffOptions extends EObject {
	/**
	 * Returns the value of the '<em><b>Filter Selected Elements</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Filter Selected Elements</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Filter Selected Elements</em>' attribute.
	 * @see #setFilterSelectedElements(boolean)
	 * @see com.mmxlabs.lingo.reports.views.schedule.model.ScheduleReportPackage#getDiffOptions_FilterSelectedElements()
	 * @model
	 * @generated
	 */
	boolean isFilterSelectedElements();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.schedule.model.DiffOptions#isFilterSelectedElements <em>Filter Selected Elements</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Filter Selected Elements</em>' attribute.
	 * @see #isFilterSelectedElements()
	 * @generated
	 */
	void setFilterSelectedElements(boolean value);

	/**
	 * Returns the value of the '<em><b>Filter Selected Sequences</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Filter Selected Sequences</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Filter Selected Sequences</em>' attribute.
	 * @see #setFilterSelectedSequences(boolean)
	 * @see com.mmxlabs.lingo.reports.views.schedule.model.ScheduleReportPackage#getDiffOptions_FilterSelectedSequences()
	 * @model
	 * @generated
	 */
	boolean isFilterSelectedSequences();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.schedule.model.DiffOptions#isFilterSelectedSequences <em>Filter Selected Sequences</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Filter Selected Sequences</em>' attribute.
	 * @see #isFilterSelectedSequences()
	 * @generated
	 */
	void setFilterSelectedSequences(boolean value);

} // DiffOptions
