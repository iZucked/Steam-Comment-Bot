/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
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
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#getLhsName <em>Lhs Name</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#getRhsName <em>Rhs Name</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#getLhsBefore <em>Lhs Before</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#getLhsAfter <em>Lhs After</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#getRhsBefore <em>Rhs Before</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#getRhsAfter <em>Rhs After</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#getBeforeVesselName <em>Before Vessel Name</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#getBeforeVesselShortName <em>Before Vessel Short Name</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#getAfterVesselName <em>After Vessel Name</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#getAfterVesselShortName <em>After Vessel Short Name</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#isWiringChange <em>Wiring Change</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#isVesselChange <em>Vessel Change</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#getPreviousRHS <em>Previous RHS</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#getNextLHS <em>Next LHS</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#isLhsSlot <em>Lhs Slot</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#isLhsSpot <em>Lhs Spot</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#isLhsOptional <em>Lhs Optional</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#isLhsValid <em>Lhs Valid</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#isLhsNonShipped <em>Lhs Non Shipped</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#isRhsSlot <em>Rhs Slot</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#isRhsSpot <em>Rhs Spot</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#isRhsOptional <em>Rhs Optional</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#isRhsValid <em>Rhs Valid</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#isRhsNonShipped <em>Rhs Non Shipped</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getChangeSetTableRow()
 * @model
 * @generated
 */
public interface ChangeSetTableRow extends EObject {
	/**
	 * Returns the value of the '<em><b>Lhs Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Lhs Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Lhs Name</em>' attribute.
	 * @see #setLhsName(String)
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getChangeSetTableRow_LhsName()
	 * @model
	 * @generated
	 */
	String getLhsName();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#getLhsName <em>Lhs Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Lhs Name</em>' attribute.
	 * @see #getLhsName()
	 * @generated
	 */
	void setLhsName(String value);

	/**
	 * Returns the value of the '<em><b>Rhs Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Rhs Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Rhs Name</em>' attribute.
	 * @see #setRhsName(String)
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getChangeSetTableRow_RhsName()
	 * @model
	 * @generated
	 */
	String getRhsName();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#getRhsName <em>Rhs Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Rhs Name</em>' attribute.
	 * @see #getRhsName()
	 * @generated
	 */
	void setRhsName(String value);

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
	 * Returns the value of the '<em><b>Rhs Before</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Rhs Before</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Rhs Before</em>' reference.
	 * @see #setRhsBefore(ChangeSetRowData)
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getChangeSetTableRow_RhsBefore()
	 * @model
	 * @generated
	 */
	ChangeSetRowData getRhsBefore();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#getRhsBefore <em>Rhs Before</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Rhs Before</em>' reference.
	 * @see #getRhsBefore()
	 * @generated
	 */
	void setRhsBefore(ChangeSetRowData value);

	/**
	 * Returns the value of the '<em><b>Rhs After</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Rhs After</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Rhs After</em>' reference.
	 * @see #setRhsAfter(ChangeSetRowData)
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getChangeSetTableRow_RhsAfter()
	 * @model
	 * @generated
	 */
	ChangeSetRowData getRhsAfter();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#getRhsAfter <em>Rhs After</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Rhs After</em>' reference.
	 * @see #getRhsAfter()
	 * @generated
	 */
	void setRhsAfter(ChangeSetRowData value);

	/**
	 * Returns the value of the '<em><b>Before Vessel Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Before Vessel Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Before Vessel Name</em>' attribute.
	 * @see #setBeforeVesselName(String)
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getChangeSetTableRow_BeforeVesselName()
	 * @model
	 * @generated
	 */
	String getBeforeVesselName();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#getBeforeVesselName <em>Before Vessel Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Before Vessel Name</em>' attribute.
	 * @see #getBeforeVesselName()
	 * @generated
	 */
	void setBeforeVesselName(String value);

	/**
	 * Returns the value of the '<em><b>Before Vessel Short Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Before Vessel Short Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Before Vessel Short Name</em>' attribute.
	 * @see #setBeforeVesselShortName(String)
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getChangeSetTableRow_BeforeVesselShortName()
	 * @model
	 * @generated
	 */
	String getBeforeVesselShortName();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#getBeforeVesselShortName <em>Before Vessel Short Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Before Vessel Short Name</em>' attribute.
	 * @see #getBeforeVesselShortName()
	 * @generated
	 */
	void setBeforeVesselShortName(String value);

	/**
	 * Returns the value of the '<em><b>After Vessel Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>After Vessel Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>After Vessel Name</em>' attribute.
	 * @see #setAfterVesselName(String)
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getChangeSetTableRow_AfterVesselName()
	 * @model
	 * @generated
	 */
	String getAfterVesselName();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#getAfterVesselName <em>After Vessel Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>After Vessel Name</em>' attribute.
	 * @see #getAfterVesselName()
	 * @generated
	 */
	void setAfterVesselName(String value);

	/**
	 * Returns the value of the '<em><b>After Vessel Short Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>After Vessel Short Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>After Vessel Short Name</em>' attribute.
	 * @see #setAfterVesselShortName(String)
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getChangeSetTableRow_AfterVesselShortName()
	 * @model
	 * @generated
	 */
	String getAfterVesselShortName();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#getAfterVesselShortName <em>After Vessel Short Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>After Vessel Short Name</em>' attribute.
	 * @see #getAfterVesselShortName()
	 * @generated
	 */
	void setAfterVesselShortName(String value);

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
	 * Returns the value of the '<em><b>Previous RHS</b></em>' reference.
	 * It is bidirectional and its opposite is '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#getNextLHS <em>Next LHS</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Previous RHS</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Previous RHS</em>' reference.
	 * @see #setPreviousRHS(ChangeSetTableRow)
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getChangeSetTableRow_PreviousRHS()
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#getNextLHS
	 * @model opposite="nextLHS"
	 * @generated
	 */
	ChangeSetTableRow getPreviousRHS();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#getPreviousRHS <em>Previous RHS</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Previous RHS</em>' reference.
	 * @see #getPreviousRHS()
	 * @generated
	 */
	void setPreviousRHS(ChangeSetTableRow value);

	/**
	 * Returns the value of the '<em><b>Next LHS</b></em>' reference.
	 * It is bidirectional and its opposite is '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#getPreviousRHS <em>Previous RHS</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Next LHS</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Next LHS</em>' reference.
	 * @see #setNextLHS(ChangeSetTableRow)
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getChangeSetTableRow_NextLHS()
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#getPreviousRHS
	 * @model opposite="previousRHS"
	 * @generated
	 */
	ChangeSetTableRow getNextLHS();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#getNextLHS <em>Next LHS</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Next LHS</em>' reference.
	 * @see #getNextLHS()
	 * @generated
	 */
	void setNextLHS(ChangeSetTableRow value);

	/**
	 * Returns the value of the '<em><b>Lhs Slot</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Lhs Slot</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Lhs Slot</em>' attribute.
	 * @see #setLhsSlot(boolean)
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getChangeSetTableRow_LhsSlot()
	 * @model
	 * @generated
	 */
	boolean isLhsSlot();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#isLhsSlot <em>Lhs Slot</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Lhs Slot</em>' attribute.
	 * @see #isLhsSlot()
	 * @generated
	 */
	void setLhsSlot(boolean value);

	/**
	 * Returns the value of the '<em><b>Lhs Spot</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Lhs Spot</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Lhs Spot</em>' attribute.
	 * @see #setLhsSpot(boolean)
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getChangeSetTableRow_LhsSpot()
	 * @model
	 * @generated
	 */
	boolean isLhsSpot();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#isLhsSpot <em>Lhs Spot</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Lhs Spot</em>' attribute.
	 * @see #isLhsSpot()
	 * @generated
	 */
	void setLhsSpot(boolean value);

	/**
	 * Returns the value of the '<em><b>Lhs Optional</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Lhs Optional</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Lhs Optional</em>' attribute.
	 * @see #setLhsOptional(boolean)
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getChangeSetTableRow_LhsOptional()
	 * @model
	 * @generated
	 */
	boolean isLhsOptional();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#isLhsOptional <em>Lhs Optional</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Lhs Optional</em>' attribute.
	 * @see #isLhsOptional()
	 * @generated
	 */
	void setLhsOptional(boolean value);

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
	 * Returns the value of the '<em><b>Lhs Non Shipped</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Lhs Non Shipped</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Lhs Non Shipped</em>' attribute.
	 * @see #setLhsNonShipped(boolean)
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getChangeSetTableRow_LhsNonShipped()
	 * @model
	 * @generated
	 */
	boolean isLhsNonShipped();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#isLhsNonShipped <em>Lhs Non Shipped</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Lhs Non Shipped</em>' attribute.
	 * @see #isLhsNonShipped()
	 * @generated
	 */
	void setLhsNonShipped(boolean value);

	/**
	 * Returns the value of the '<em><b>Rhs Slot</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Rhs Slot</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Rhs Slot</em>' attribute.
	 * @see #setRhsSlot(boolean)
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getChangeSetTableRow_RhsSlot()
	 * @model
	 * @generated
	 */
	boolean isRhsSlot();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#isRhsSlot <em>Rhs Slot</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Rhs Slot</em>' attribute.
	 * @see #isRhsSlot()
	 * @generated
	 */
	void setRhsSlot(boolean value);

	/**
	 * Returns the value of the '<em><b>Rhs Spot</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Rhs Spot</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Rhs Spot</em>' attribute.
	 * @see #setRhsSpot(boolean)
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getChangeSetTableRow_RhsSpot()
	 * @model
	 * @generated
	 */
	boolean isRhsSpot();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#isRhsSpot <em>Rhs Spot</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Rhs Spot</em>' attribute.
	 * @see #isRhsSpot()
	 * @generated
	 */
	void setRhsSpot(boolean value);

	/**
	 * Returns the value of the '<em><b>Rhs Optional</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Rhs Optional</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Rhs Optional</em>' attribute.
	 * @see #setRhsOptional(boolean)
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getChangeSetTableRow_RhsOptional()
	 * @model
	 * @generated
	 */
	boolean isRhsOptional();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#isRhsOptional <em>Rhs Optional</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Rhs Optional</em>' attribute.
	 * @see #isRhsOptional()
	 * @generated
	 */
	void setRhsOptional(boolean value);

	/**
	 * Returns the value of the '<em><b>Rhs Valid</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Rhs Valid</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Rhs Valid</em>' attribute.
	 * @see #setRhsValid(boolean)
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getChangeSetTableRow_RhsValid()
	 * @model
	 * @generated
	 */
	boolean isRhsValid();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#isRhsValid <em>Rhs Valid</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Rhs Valid</em>' attribute.
	 * @see #isRhsValid()
	 * @generated
	 */
	void setRhsValid(boolean value);

	/**
	 * Returns the value of the '<em><b>Rhs Non Shipped</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Rhs Non Shipped</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Rhs Non Shipped</em>' attribute.
	 * @see #setRhsNonShipped(boolean)
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getChangeSetTableRow_RhsNonShipped()
	 * @model
	 * @generated
	 */
	boolean isRhsNonShipped();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetTableRow#isRhsNonShipped <em>Rhs Non Shipped</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Rhs Non Shipped</em>' attribute.
	 * @see #isRhsNonShipped()
	 * @generated
	 */
	void setRhsNonShipped(boolean value);

} // ChangeSetTableRow
