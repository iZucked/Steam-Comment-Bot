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
 * A representation of the model object '<em><b>Cycle Group</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.lingo.reports.views.schedule.model.CycleGroup#getDescription <em>Description</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.schedule.model.CycleGroup#getRows <em>Rows</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.schedule.model.CycleGroup#getIndex <em>Index</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.schedule.model.CycleGroup#getUserGroup <em>User Group</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.schedule.model.CycleGroup#getDelta <em>Delta</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.schedule.model.CycleGroup#getChangeType <em>Change Type</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.lingo.reports.views.schedule.model.ScheduleReportPackage#getCycleGroup()
 * @model
 * @generated
 */
public interface CycleGroup extends EObject {
	/**
	 * Returns the value of the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Description</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Description</em>' attribute.
	 * @see #setDescription(String)
	 * @see com.mmxlabs.lingo.reports.views.schedule.model.ScheduleReportPackage#getCycleGroup_Description()
	 * @model
	 * @generated
	 */
	String getDescription();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.schedule.model.CycleGroup#getDescription <em>Description</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Description</em>' attribute.
	 * @see #getDescription()
	 * @generated
	 */
	void setDescription(String value);

	/**
	 * Returns the value of the '<em><b>Rows</b></em>' reference list.
	 * The list contents are of type {@link com.mmxlabs.lingo.reports.views.schedule.model.Row}.
	 * It is bidirectional and its opposite is '{@link com.mmxlabs.lingo.reports.views.schedule.model.Row#getCycleGroup <em>Cycle Group</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Rows</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Rows</em>' reference list.
	 * @see com.mmxlabs.lingo.reports.views.schedule.model.ScheduleReportPackage#getCycleGroup_Rows()
	 * @see com.mmxlabs.lingo.reports.views.schedule.model.Row#getCycleGroup
	 * @model opposite="cycleGroup"
	 * @generated
	 */
	EList<Row> getRows();

	/**
	 * Returns the value of the '<em><b>Index</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Index</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Index</em>' attribute.
	 * @see #isSetIndex()
	 * @see #unsetIndex()
	 * @see #setIndex(int)
	 * @see com.mmxlabs.lingo.reports.views.schedule.model.ScheduleReportPackage#getCycleGroup_Index()
	 * @model unsettable="true"
	 * @generated
	 */
	int getIndex();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.schedule.model.CycleGroup#getIndex <em>Index</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Index</em>' attribute.
	 * @see #isSetIndex()
	 * @see #unsetIndex()
	 * @see #getIndex()
	 * @generated
	 */
	void setIndex(int value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.lingo.reports.views.schedule.model.CycleGroup#getIndex <em>Index</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetIndex()
	 * @see #getIndex()
	 * @see #setIndex(int)
	 * @generated
	 */
	void unsetIndex();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.lingo.reports.views.schedule.model.CycleGroup#getIndex <em>Index</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Index</em>' attribute is set.
	 * @see #unsetIndex()
	 * @see #getIndex()
	 * @see #setIndex(int)
	 * @generated
	 */
	boolean isSetIndex();

	/**
	 * Returns the value of the '<em><b>User Group</b></em>' container reference.
	 * It is bidirectional and its opposite is '{@link com.mmxlabs.lingo.reports.views.schedule.model.UserGroup#getGroups <em>Groups</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>User Group</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>User Group</em>' container reference.
	 * @see #setUserGroup(UserGroup)
	 * @see com.mmxlabs.lingo.reports.views.schedule.model.ScheduleReportPackage#getCycleGroup_UserGroup()
	 * @see com.mmxlabs.lingo.reports.views.schedule.model.UserGroup#getGroups
	 * @model opposite="groups" transient="false"
	 * @generated
	 */
	UserGroup getUserGroup();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.schedule.model.CycleGroup#getUserGroup <em>User Group</em>}' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>User Group</em>' container reference.
	 * @see #getUserGroup()
	 * @generated
	 */
	void setUserGroup(UserGroup value);

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
	 * @see com.mmxlabs.lingo.reports.views.schedule.model.ScheduleReportPackage#getCycleGroup_Delta()
	 * @model
	 * @generated
	 */
	int getDelta();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.schedule.model.CycleGroup#getDelta <em>Delta</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Delta</em>' attribute.
	 * @see #getDelta()
	 * @generated
	 */
	void setDelta(int value);

	/**
	 * Returns the value of the '<em><b>Change Type</b></em>' attribute.
	 * The default value is <code>"PNL"</code>.
	 * The literals are from the enumeration {@link com.mmxlabs.lingo.reports.views.schedule.model.ChangeType}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Change Type</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Change Type</em>' attribute.
	 * @see com.mmxlabs.lingo.reports.views.schedule.model.ChangeType
	 * @see #setChangeType(ChangeType)
	 * @see com.mmxlabs.lingo.reports.views.schedule.model.ScheduleReportPackage#getCycleGroup_ChangeType()
	 * @model default="PNL"
	 * @generated
	 */
	ChangeType getChangeType();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.schedule.model.CycleGroup#getChangeType <em>Change Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Change Type</em>' attribute.
	 * @see com.mmxlabs.lingo.reports.views.schedule.model.ChangeType
	 * @see #getChangeType()
	 * @generated
	 */
	void setChangeType(ChangeType value);

} // CycleGroup
