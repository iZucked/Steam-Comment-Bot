/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.lingo.reports.views.schedule.model;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>User Group</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.mmxlabs.lingo.reports.views.schedule.model.UserGroup#getComment <em>Comment</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.schedule.model.UserGroup#getGroups <em>Groups</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.schedule.model.UserGroup#getDelta <em>Delta</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.mmxlabs.lingo.reports.views.schedule.model.ScheduleReportPackage#getUserGroup()
 * @model
 * @generated
 */
public interface UserGroup extends EObject {
	/**
	 * Returns the value of the '<em><b>Comment</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Comment</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Comment</em>' attribute.
	 * @see #setComment(String)
	 * @see com.mmxlabs.lingo.reports.views.schedule.model.ScheduleReportPackage#getUserGroup_Comment()
	 * @model
	 * @generated
	 */
	String getComment();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.schedule.model.UserGroup#getComment <em>Comment</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Comment</em>' attribute.
	 * @see #getComment()
	 * @generated
	 */
	void setComment(String value);

	/**
	 * Returns the value of the '<em><b>Groups</b></em>' containment reference list.
	 * The list contents are of type {@link com.mmxlabs.lingo.reports.views.schedule.model.CycleGroup}.
	 * It is bidirectional and its opposite is '{@link com.mmxlabs.lingo.reports.views.schedule.model.CycleGroup#getUserGroup <em>User Group</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Groups</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Groups</em>' containment reference list.
	 * @see com.mmxlabs.lingo.reports.views.schedule.model.ScheduleReportPackage#getUserGroup_Groups()
	 * @see com.mmxlabs.lingo.reports.views.schedule.model.CycleGroup#getUserGroup
	 * @model opposite="userGroup" containment="true"
	 * @generated
	 */
	EList<CycleGroup> getGroups();

	/**
	 * Returns the value of the '<em><b>Delta</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Delta</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Delta</em>' attribute.
	 * @see #setDelta(int)
	 * @see com.mmxlabs.lingo.reports.views.schedule.model.ScheduleReportPackage#getUserGroup_Delta()
	 * @model
	 * @generated
	 */
	int getDelta();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.schedule.model.UserGroup#getDelta <em>Delta</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Delta</em>' attribute.
	 * @see #getDelta()
	 * @generated
	 */
	void setDelta(int value);

} // UserGroup
