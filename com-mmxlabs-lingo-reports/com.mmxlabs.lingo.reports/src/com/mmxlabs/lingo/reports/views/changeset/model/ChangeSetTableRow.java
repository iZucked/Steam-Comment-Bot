/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.lingo.reports.views.changeset.model;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Change Set Table Row</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#getLhsBefore <em>Lhs Before</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#getLhsAfter <em>Lhs After</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#isLhsValid <em>Lhs Valid</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#getCurrentRhsBefore <em>Current Rhs Before</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#getCurrentRhsAfter <em>Current Rhs After</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#getPreviousRhsBefore <em>Previous Rhs Before</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#getPreviousRhsAfter <em>Previous Rhs After</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#isCurrentRhsValid <em>Current Rhs Valid</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#isWiringChange <em>Wiring Change</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#isVesselChange <em>Vessel Change</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#isDateChange <em>Date Change</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#getWiringGroup <em>Wiring Group</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#getTableGroup <em>Table Group</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getChangeSetTableRow()
 * @model
 * @generated
 */
public interface ChangeSetTableRow extends EObject {
	/**
	 * Returns the value of the '<em><b>Lhs Before</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Lhs Before</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Lhs Before</em>' reference.
	 * @see #setLhsBefore(ChangeSetRowData)
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getChangeSetTableRow_LhsBefore()
	 * @model
	 * @generated
	 */
	ChangeSetRowData getLhsBefore();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#getLhsBefore <em>Lhs Before</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Lhs Before</em>' reference.
	 * @see #getLhsBefore()
	 * @generated
	 */
	void setLhsBefore(ChangeSetRowData value);

	/**
	 * Returns the value of the '<em><b>Lhs After</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Lhs After</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Lhs After</em>' reference.
	 * @see #setLhsAfter(ChangeSetRowData)
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getChangeSetTableRow_LhsAfter()
	 * @model
	 * @generated
	 */
	ChangeSetRowData getLhsAfter();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#getLhsAfter <em>Lhs After</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Lhs After</em>' reference.
	 * @see #getLhsAfter()
	 * @generated
	 */
	void setLhsAfter(ChangeSetRowData value);

	/**
	 * Returns the value of the '<em><b>Wiring Change</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Wiring Change</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Wiring Change</em>' attribute.
	 * @see #setWiringChange(boolean)
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getChangeSetTableRow_WiringChange()
	 * @model
	 * @generated
	 */
	boolean isWiringChange();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#isWiringChange <em>Wiring Change</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Wiring Change</em>' attribute.
	 * @see #isWiringChange()
	 * @generated
	 */
	void setWiringChange(boolean value);

	/**
	 * Returns the value of the '<em><b>Vessel Change</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Vessel Change</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Vessel Change</em>' attribute.
	 * @see #setVesselChange(boolean)
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getChangeSetTableRow_VesselChange()
	 * @model
	 * @generated
	 */
	boolean isVesselChange();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#isVesselChange <em>Vessel Change</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Vessel Change</em>' attribute.
	 * @see #isVesselChange()
	 * @generated
	 */
	void setVesselChange(boolean value);

	/**
	 * Returns the value of the '<em><b>Date Change</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Date Change</em>' attribute.
	 * @see #setDateChange(boolean)
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getChangeSetTableRow_DateChange()
	 * @model
	 * @generated
	 */
	boolean isDateChange();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#isDateChange <em>Date Change</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Date Change</em>' attribute.
	 * @see #isDateChange()
	 * @generated
	 */
	void setDateChange(boolean value);

	/**
	 * Returns the value of the '<em><b>Lhs Valid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Lhs Valid</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Lhs Valid</em>' attribute.
	 * @see #setLhsValid(boolean)
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getChangeSetTableRow_LhsValid()
	 * @model
	 * @generated
	 */
	boolean isLhsValid();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#isLhsValid <em>Lhs Valid</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Lhs Valid</em>' attribute.
	 * @see #isLhsValid()
	 * @generated
	 */
	void setLhsValid(boolean value);

	/**
	 * Returns the value of the '<em><b>Current Rhs Before</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Current Rhs Before</em>' reference.
	 * @see #setCurrentRhsBefore(ChangeSetRowData)
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getChangeSetTableRow_CurrentRhsBefore()
	 * @model
	 * @generated
	 */
	ChangeSetRowData getCurrentRhsBefore();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#getCurrentRhsBefore <em>Current Rhs Before</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Current Rhs Before</em>' reference.
	 * @see #getCurrentRhsBefore()
	 * @generated
	 */
	void setCurrentRhsBefore(ChangeSetRowData value);

	/**
	 * Returns the value of the '<em><b>Current Rhs After</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Current Rhs After</em>' reference.
	 * @see #setCurrentRhsAfter(ChangeSetRowData)
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getChangeSetTableRow_CurrentRhsAfter()
	 * @model
	 * @generated
	 */
	ChangeSetRowData getCurrentRhsAfter();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#getCurrentRhsAfter <em>Current Rhs After</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Current Rhs After</em>' reference.
	 * @see #getCurrentRhsAfter()
	 * @generated
	 */
	void setCurrentRhsAfter(ChangeSetRowData value);

	/**
	 * Returns the value of the '<em><b>Previous Rhs Before</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Previous Rhs Before</em>' reference.
	 * @see #setPreviousRhsBefore(ChangeSetRowData)
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getChangeSetTableRow_PreviousRhsBefore()
	 * @model
	 * @generated
	 */
	ChangeSetRowData getPreviousRhsBefore();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#getPreviousRhsBefore <em>Previous Rhs Before</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Previous Rhs Before</em>' reference.
	 * @see #getPreviousRhsBefore()
	 * @generated
	 */
	void setPreviousRhsBefore(ChangeSetRowData value);

	/**
	 * Returns the value of the '<em><b>Previous Rhs After</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Previous Rhs After</em>' reference.
	 * @see #setPreviousRhsAfter(ChangeSetRowData)
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getChangeSetTableRow_PreviousRhsAfter()
	 * @model
	 * @generated
	 */
	ChangeSetRowData getPreviousRhsAfter();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#getPreviousRhsAfter <em>Previous Rhs After</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Previous Rhs After</em>' reference.
	 * @see #getPreviousRhsAfter()
	 * @generated
	 */
	void setPreviousRhsAfter(ChangeSetRowData value);

	/**
	 * Returns the value of the '<em><b>Current Rhs Valid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Current Rhs Valid</em>' attribute.
	 * @see #setCurrentRhsValid(boolean)
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getChangeSetTableRow_CurrentRhsValid()
	 * @model
	 * @generated
	 */
	boolean isCurrentRhsValid();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#isCurrentRhsValid <em>Current Rhs Valid</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Current Rhs Valid</em>' attribute.
	 * @see #isCurrentRhsValid()
	 * @generated
	 */
	void setCurrentRhsValid(boolean value);

	/**
	 * Returns the value of the '<em><b>Wiring Group</b></em>' reference.
	 * It is bidirectional and its opposite is '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetWiringGroup#getRows <em>Rows</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Wiring Group</em>' reference.
	 * @see #setWiringGroup(ChangeSetWiringGroup)
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getChangeSetTableRow_WiringGroup()
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetWiringGroup#getRows
	 * @model opposite="rows"
	 * @generated
	 */
	ChangeSetWiringGroup getWiringGroup();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#getWiringGroup <em>Wiring Group</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Wiring Group</em>' reference.
	 * @see #getWiringGroup()
	 * @generated
	 */
	void setWiringGroup(ChangeSetWiringGroup value);

	/**
	 * Returns the value of the '<em><b>Table Group</b></em>' reference.
	 * It is bidirectional and its opposite is '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableGroup#getRows <em>Rows</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Table Group</em>' reference.
	 * @see #setTableGroup(ChangeSetTableGroup)
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getChangeSetTableRow_TableGroup()
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableGroup#getRows
	 * @model opposite="rows"
	 * @generated
	 */
	ChangeSetTableGroup getTableGroup();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#getTableGroup <em>Table Group</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Table Group</em>' reference.
	 * @see #getTableGroup()
	 * @generated
	 */
	void setTableGroup(ChangeSetTableGroup value);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation"
	 * @generated
	 */
	boolean isMajorChange();
	
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation"
	 * @generated
	 */
	ChangeSetRowData getLHSAfterOrBefore();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation"
	 * @generated
	 */
	ChangeSetRowData getCurrentRHSAfterOrBefore();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation"
	 * @generated
	 */
	String getLhsName();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation"
	 * @generated
	 */
	String getCurrentRhsName();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation"
	 * @generated
	 */
	int getBeforeVesselCharterNumber();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation"
	 * @generated
	 */
	String getBeforeVesselName();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation"
	 * @generated
	 */
	String getBeforeVesselShortName();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation"
	 * @generated
	 */
	ChangeSetVesselType getBeforeVesselType();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation"
	 * @generated
	 */
	int getAfterVesselCharterNumber();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation"
	 * @generated
	 */
	String getAfterVesselName();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation"
	 * @generated
	 */
	String getAfterVesselShortName();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation"
	 * @generated
	 */
	ChangeSetVesselType getAfterVesselType();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation"
	 * @generated
	 */
	boolean isLhsSlot();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation"
	 * @generated
	 */
	boolean isCurrentRhsSlot();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation"
	 * @generated
	 */
	boolean isLhsSpot();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation"
	 * @generated
	 */
	boolean isCurrentRhsOptional();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation"
	 * @generated
	 */
	boolean isLhsOptional();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation"
	 * @generated
	 */
	boolean isCurrentRhsSpot();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation"
	 * @generated
	 */
	boolean isLhsNonShipped();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation"
	 * @generated
	 */
	boolean isCurrentRhsNonShipped();

	boolean isWiringOrVesselChange();

} // ChangeSetTableRow
