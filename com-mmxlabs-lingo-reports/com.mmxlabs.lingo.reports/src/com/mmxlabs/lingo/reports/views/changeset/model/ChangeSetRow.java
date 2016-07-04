/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.lingo.reports.views.changeset.model;

import org.eclipse.emf.ecore.EObject;

import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.schedule.EventGrouping;
import com.mmxlabs.models.lng.schedule.ProfitAndLossContainer;
import com.mmxlabs.models.lng.schedule.SlotAllocation;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Change Set Row</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow#getLhsName <em>Lhs Name</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow#getRhsName <em>Rhs Name</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow#getOriginalVesselName <em>Original Vessel Name</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow#getNewVesselName <em>New Vessel Name</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow#getLhsWiringLink <em>Lhs Wiring Link</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow#getRhsWiringLink <em>Rhs Wiring Link</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow#getLoadSlot <em>Load Slot</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow#getDischargeSlot <em>Discharge Slot</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow#getOriginalLoadAllocation <em>Original Load Allocation</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow#getNewLoadAllocation <em>New Load Allocation</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow#getOriginalDischargeAllocation <em>Original Discharge Allocation</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow#getNewDischargeAllocation <em>New Discharge Allocation</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow#isWiringChange <em>Wiring Change</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow#isVesselChange <em>Vessel Change</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow#getOriginalGroupProfitAndLoss <em>Original Group Profit And Loss</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow#getNewGroupProfitAndLoss <em>New Group Profit And Loss</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow#getOriginalEventGrouping <em>Original Event Grouping</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow#getNewEventGrouping <em>New Event Grouping</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow#getOriginalVesselShortName <em>Original Vessel Short Name</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow#getNewVesselShortName <em>New Vessel Short Name</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getChangeSetRow()
 * @model
 * @generated
 */
public interface ChangeSetRow extends EObject {
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
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getChangeSetRow_LhsName()
	 * @model
	 * @generated
	 */
	String getLhsName();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow#getLhsName <em>Lhs Name</em>}' attribute.
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
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getChangeSetRow_RhsName()
	 * @model
	 * @generated
	 */
	String getRhsName();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow#getRhsName <em>Rhs Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Rhs Name</em>' attribute.
	 * @see #getRhsName()
	 * @generated
	 */
	void setRhsName(String value);

	/**
	 * Returns the value of the '<em><b>Original Vessel Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Original Vessel Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Original Vessel Name</em>' attribute.
	 * @see #setOriginalVesselName(String)
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getChangeSetRow_OriginalVesselName()
	 * @model
	 * @generated
	 */
	String getOriginalVesselName();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow#getOriginalVesselName <em>Original Vessel Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Original Vessel Name</em>' attribute.
	 * @see #getOriginalVesselName()
	 * @generated
	 */
	void setOriginalVesselName(String value);

	/**
	 * Returns the value of the '<em><b>New Vessel Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>New Vessel Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>New Vessel Name</em>' attribute.
	 * @see #setNewVesselName(String)
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getChangeSetRow_NewVesselName()
	 * @model
	 * @generated
	 */
	String getNewVesselName();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow#getNewVesselName <em>New Vessel Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>New Vessel Name</em>' attribute.
	 * @see #getNewVesselName()
	 * @generated
	 */
	void setNewVesselName(String value);

	/**
	 * Returns the value of the '<em><b>Lhs Wiring Link</b></em>' reference.
	 * It is bidirectional and its opposite is '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow#getRhsWiringLink <em>Rhs Wiring Link</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Lhs Wiring Link</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Lhs Wiring Link</em>' reference.
	 * @see #setLhsWiringLink(ChangeSetRow)
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getChangeSetRow_LhsWiringLink()
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow#getRhsWiringLink
	 * @model opposite="rhsWiringLink"
	 * @generated
	 */
	ChangeSetRow getLhsWiringLink();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow#getLhsWiringLink <em>Lhs Wiring Link</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Lhs Wiring Link</em>' reference.
	 * @see #getLhsWiringLink()
	 * @generated
	 */
	void setLhsWiringLink(ChangeSetRow value);

	/**
	 * Returns the value of the '<em><b>Rhs Wiring Link</b></em>' reference.
	 * It is bidirectional and its opposite is '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow#getLhsWiringLink <em>Lhs Wiring Link</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Rhs Wiring Link</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Rhs Wiring Link</em>' reference.
	 * @see #setRhsWiringLink(ChangeSetRow)
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getChangeSetRow_RhsWiringLink()
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow#getLhsWiringLink
	 * @model opposite="lhsWiringLink"
	 * @generated
	 */
	ChangeSetRow getRhsWiringLink();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow#getRhsWiringLink <em>Rhs Wiring Link</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Rhs Wiring Link</em>' reference.
	 * @see #getRhsWiringLink()
	 * @generated
	 */
	void setRhsWiringLink(ChangeSetRow value);

	/**
	 * Returns the value of the '<em><b>Load Slot</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Load Slot</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Load Slot</em>' reference.
	 * @see #setLoadSlot(LoadSlot)
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getChangeSetRow_LoadSlot()
	 * @model
	 * @generated
	 */
	LoadSlot getLoadSlot();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow#getLoadSlot <em>Load Slot</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Load Slot</em>' reference.
	 * @see #getLoadSlot()
	 * @generated
	 */
	void setLoadSlot(LoadSlot value);

	/**
	 * Returns the value of the '<em><b>Discharge Slot</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Discharge Slot</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Discharge Slot</em>' reference.
	 * @see #setDischargeSlot(DischargeSlot)
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getChangeSetRow_DischargeSlot()
	 * @model
	 * @generated
	 */
	DischargeSlot getDischargeSlot();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow#getDischargeSlot <em>Discharge Slot</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Discharge Slot</em>' reference.
	 * @see #getDischargeSlot()
	 * @generated
	 */
	void setDischargeSlot(DischargeSlot value);

	/**
	 * Returns the value of the '<em><b>Original Load Allocation</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Original Load Allocation</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Original Load Allocation</em>' reference.
	 * @see #setOriginalLoadAllocation(SlotAllocation)
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getChangeSetRow_OriginalLoadAllocation()
	 * @model
	 * @generated
	 */
	SlotAllocation getOriginalLoadAllocation();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow#getOriginalLoadAllocation <em>Original Load Allocation</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Original Load Allocation</em>' reference.
	 * @see #getOriginalLoadAllocation()
	 * @generated
	 */
	void setOriginalLoadAllocation(SlotAllocation value);

	/**
	 * Returns the value of the '<em><b>New Load Allocation</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>New Load Allocation</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>New Load Allocation</em>' reference.
	 * @see #setNewLoadAllocation(SlotAllocation)
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getChangeSetRow_NewLoadAllocation()
	 * @model
	 * @generated
	 */
	SlotAllocation getNewLoadAllocation();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow#getNewLoadAllocation <em>New Load Allocation</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>New Load Allocation</em>' reference.
	 * @see #getNewLoadAllocation()
	 * @generated
	 */
	void setNewLoadAllocation(SlotAllocation value);

	/**
	 * Returns the value of the '<em><b>Original Discharge Allocation</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Original Discharge Allocation</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Original Discharge Allocation</em>' reference.
	 * @see #setOriginalDischargeAllocation(SlotAllocation)
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getChangeSetRow_OriginalDischargeAllocation()
	 * @model
	 * @generated
	 */
	SlotAllocation getOriginalDischargeAllocation();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow#getOriginalDischargeAllocation <em>Original Discharge Allocation</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Original Discharge Allocation</em>' reference.
	 * @see #getOriginalDischargeAllocation()
	 * @generated
	 */
	void setOriginalDischargeAllocation(SlotAllocation value);

	/**
	 * Returns the value of the '<em><b>New Discharge Allocation</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>New Discharge Allocation</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>New Discharge Allocation</em>' reference.
	 * @see #setNewDischargeAllocation(SlotAllocation)
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getChangeSetRow_NewDischargeAllocation()
	 * @model
	 * @generated
	 */
	SlotAllocation getNewDischargeAllocation();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow#getNewDischargeAllocation <em>New Discharge Allocation</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>New Discharge Allocation</em>' reference.
	 * @see #getNewDischargeAllocation()
	 * @generated
	 */
	void setNewDischargeAllocation(SlotAllocation value);

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
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getChangeSetRow_WiringChange()
	 * @model
	 * @generated
	 */
	boolean isWiringChange();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow#isWiringChange <em>Wiring Change</em>}' attribute.
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
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getChangeSetRow_VesselChange()
	 * @model
	 * @generated
	 */
	boolean isVesselChange();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow#isVesselChange <em>Vessel Change</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Vessel Change</em>' attribute.
	 * @see #isVesselChange()
	 * @generated
	 */
	void setVesselChange(boolean value);

	/**
	 * Returns the value of the '<em><b>Original Group Profit And Loss</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Original Group Profit And Loss</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Original Group Profit And Loss</em>' reference.
	 * @see #setOriginalGroupProfitAndLoss(ProfitAndLossContainer)
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getChangeSetRow_OriginalGroupProfitAndLoss()
	 * @model
	 * @generated
	 */
	ProfitAndLossContainer getOriginalGroupProfitAndLoss();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow#getOriginalGroupProfitAndLoss <em>Original Group Profit And Loss</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Original Group Profit And Loss</em>' reference.
	 * @see #getOriginalGroupProfitAndLoss()
	 * @generated
	 */
	void setOriginalGroupProfitAndLoss(ProfitAndLossContainer value);

	/**
	 * Returns the value of the '<em><b>New Group Profit And Loss</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>New Group Profit And Loss</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>New Group Profit And Loss</em>' reference.
	 * @see #setNewGroupProfitAndLoss(ProfitAndLossContainer)
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getChangeSetRow_NewGroupProfitAndLoss()
	 * @model
	 * @generated
	 */
	ProfitAndLossContainer getNewGroupProfitAndLoss();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow#getNewGroupProfitAndLoss <em>New Group Profit And Loss</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>New Group Profit And Loss</em>' reference.
	 * @see #getNewGroupProfitAndLoss()
	 * @generated
	 */
	void setNewGroupProfitAndLoss(ProfitAndLossContainer value);

	/**
	 * Returns the value of the '<em><b>Original Event Grouping</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Original Event Grouping</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Original Event Grouping</em>' reference.
	 * @see #setOriginalEventGrouping(EventGrouping)
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getChangeSetRow_OriginalEventGrouping()
	 * @model
	 * @generated
	 */
	EventGrouping getOriginalEventGrouping();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow#getOriginalEventGrouping <em>Original Event Grouping</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Original Event Grouping</em>' reference.
	 * @see #getOriginalEventGrouping()
	 * @generated
	 */
	void setOriginalEventGrouping(EventGrouping value);

	/**
	 * Returns the value of the '<em><b>New Event Grouping</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>New Event Grouping</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>New Event Grouping</em>' reference.
	 * @see #setNewEventGrouping(EventGrouping)
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getChangeSetRow_NewEventGrouping()
	 * @model
	 * @generated
	 */
	EventGrouping getNewEventGrouping();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow#getNewEventGrouping <em>New Event Grouping</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>New Event Grouping</em>' reference.
	 * @see #getNewEventGrouping()
	 * @generated
	 */
	void setNewEventGrouping(EventGrouping value);

	/**
	 * Returns the value of the '<em><b>Original Vessel Short Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Original Vessel Short Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Original Vessel Short Name</em>' attribute.
	 * @see #setOriginalVesselShortName(String)
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getChangeSetRow_OriginalVesselShortName()
	 * @model
	 * @generated
	 */
	String getOriginalVesselShortName();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow#getOriginalVesselShortName <em>Original Vessel Short Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Original Vessel Short Name</em>' attribute.
	 * @see #getOriginalVesselShortName()
	 * @generated
	 */
	void setOriginalVesselShortName(String value);

	/**
	 * Returns the value of the '<em><b>New Vessel Short Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>New Vessel Short Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>New Vessel Short Name</em>' attribute.
	 * @see #setNewVesselShortName(String)
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getChangeSetRow_NewVesselShortName()
	 * @model
	 * @generated
	 */
	String getNewVesselShortName();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRow#getNewVesselShortName <em>New Vessel Short Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>New Vessel Short Name</em>' attribute.
	 * @see #getNewVesselShortName()
	 * @generated
	 */
	void setNewVesselShortName(String value);

} // ChangeSetRow
