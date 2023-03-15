/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.lingo.reports.views.changeset.model;

import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.EventGrouping;
import com.mmxlabs.models.lng.schedule.OpenSlotAllocation;
import com.mmxlabs.models.lng.schedule.PaperDealAllocation;
import com.mmxlabs.models.lng.schedule.ProfitAndLossContainer;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Change Set Row Data</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowData#isPrimaryRecord <em>Primary Record</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowData#getRowDataGroup <em>Row Data Group</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowData#getEventGrouping <em>Event Grouping</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowData#getVesselName <em>Vessel Name</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowData#getVesselShortName <em>Vessel Short Name</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowData#getVesselType <em>Vessel Type</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowData#getVesselCharterNumber <em>Vessel Charter Number</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowData#getLhsName <em>Lhs Name</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowData#getRhsName <em>Rhs Name</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowData#getLhsLink <em>Lhs Link</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowData#getRhsLink <em>Rhs Link</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowData#isLhsSlot <em>Lhs Slot</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowData#isLhsSpot <em>Lhs Spot</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowData#isLhsOptional <em>Lhs Optional</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowData#isRhsSlot <em>Rhs Slot</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowData#isRhsSpot <em>Rhs Spot</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowData#isRhsOptional <em>Rhs Optional</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowData#isLhsNonShipped <em>Lhs Non Shipped</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowData#isRhsNonShipped <em>Rhs Non Shipped</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowData#getLoadSlot <em>Load Slot</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowData#getDischargeSlot <em>Discharge Slot</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowData#getLoadAllocation <em>Load Allocation</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowData#getDischargeAllocation <em>Discharge Allocation</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowData#getOpenLoadAllocation <em>Open Load Allocation</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowData#getOpenDischargeAllocation <em>Open Discharge Allocation</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowData#getLhsEvent <em>Lhs Event</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowData#getRhsEvent <em>Rhs Event</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowData#getLhsGroupProfitAndLoss <em>Lhs Group Profit And Loss</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowData#getRhsGroupProfitAndLoss <em>Rhs Group Profit And Loss</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowData#getPaperDealAllocation <em>Paper Deal Allocation</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getChangeSetRowData()
 * @model
 * @generated
 */
public interface ChangeSetRowData extends EObject {
	/**
	 * Returns the value of the '<em><b>Primary Record</b></em>' attribute.
	 * The default value is <code>"true"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Primary Record</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Primary Record</em>' attribute.
	 * @see #setPrimaryRecord(boolean)
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getChangeSetRowData_PrimaryRecord()
	 * @model default="true"
	 * @generated
	 */
	boolean isPrimaryRecord();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowData#isPrimaryRecord <em>Primary Record</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Primary Record</em>' attribute.
	 * @see #isPrimaryRecord()
	 * @generated
	 */
	void setPrimaryRecord(boolean value);

	/**
	 * Returns the value of the '<em><b>Row Data Group</b></em>' container reference.
	 * It is bidirectional and its opposite is '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowDataGroup#getMembers <em>Members</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Row Data Group</em>' container reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Row Data Group</em>' container reference.
	 * @see #setRowDataGroup(ChangeSetRowDataGroup)
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getChangeSetRowData_RowDataGroup()
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowDataGroup#getMembers
	 * @model opposite="members" transient="false"
	 * @generated
	 */
	ChangeSetRowDataGroup getRowDataGroup();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowData#getRowDataGroup <em>Row Data Group</em>}' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Row Data Group</em>' container reference.
	 * @see #getRowDataGroup()
	 * @generated
	 */
	void setRowDataGroup(ChangeSetRowDataGroup value);

	/**
	 * Returns the value of the '<em><b>Event Grouping</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Event Grouping</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Event Grouping</em>' reference.
	 * @see #setEventGrouping(EventGrouping)
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getChangeSetRowData_EventGrouping()
	 * @model
	 * @generated
	 */
	EventGrouping getEventGrouping();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowData#getEventGrouping <em>Event Grouping</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Event Grouping</em>' reference.
	 * @see #getEventGrouping()
	 * @generated
	 */
	void setEventGrouping(EventGrouping value);

	/**
	 * Returns the value of the '<em><b>Vessel Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Vessel Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Vessel Name</em>' attribute.
	 * @see #setVesselName(String)
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getChangeSetRowData_VesselName()
	 * @model
	 * @generated
	 */
	String getVesselName();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowData#getVesselName <em>Vessel Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Vessel Name</em>' attribute.
	 * @see #getVesselName()
	 * @generated
	 */
	void setVesselName(String value);

	/**
	 * Returns the value of the '<em><b>Vessel Short Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Vessel Short Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Vessel Short Name</em>' attribute.
	 * @see #setVesselShortName(String)
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getChangeSetRowData_VesselShortName()
	 * @model
	 * @generated
	 */
	String getVesselShortName();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowData#getVesselShortName <em>Vessel Short Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Vessel Short Name</em>' attribute.
	 * @see #getVesselShortName()
	 * @generated
	 */
	void setVesselShortName(String value);

	/**
	 * Returns the value of the '<em><b>Vessel Type</b></em>' attribute.
	 * The literals are from the enumeration {@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetVesselType}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Vessel Type</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Vessel Type</em>' attribute.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetVesselType
	 * @see #setVesselType(ChangeSetVesselType)
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getChangeSetRowData_VesselType()
	 * @model
	 * @generated
	 */
	ChangeSetVesselType getVesselType();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowData#getVesselType <em>Vessel Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Vessel Type</em>' attribute.
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetVesselType
	 * @see #getVesselType()
	 * @generated
	 */
	void setVesselType(ChangeSetVesselType value);

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
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getChangeSetRowData_LhsName()
	 * @model
	 * @generated
	 */
	String getLhsName();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowData#getLhsName <em>Lhs Name</em>}' attribute.
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
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getChangeSetRowData_RhsName()
	 * @model
	 * @generated
	 */
	String getRhsName();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowData#getRhsName <em>Rhs Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Rhs Name</em>' attribute.
	 * @see #getRhsName()
	 * @generated
	 */
	void setRhsName(String value);

	/**
	 * Returns the value of the '<em><b>Lhs Link</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Lhs Link</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Lhs Link</em>' reference.
	 * @see #setLhsLink(ChangeSetRowData)
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getChangeSetRowData_LhsLink()
	 * @model
	 * @generated
	 */
	ChangeSetRowData getLhsLink();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowData#getLhsLink <em>Lhs Link</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Lhs Link</em>' reference.
	 * @see #getLhsLink()
	 * @generated
	 */
	void setLhsLink(ChangeSetRowData value);

	/**
	 * Returns the value of the '<em><b>Rhs Link</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Rhs Link</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Rhs Link</em>' reference.
	 * @see #setRhsLink(ChangeSetRowData)
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getChangeSetRowData_RhsLink()
	 * @model
	 * @generated
	 */
	ChangeSetRowData getRhsLink();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowData#getRhsLink <em>Rhs Link</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Rhs Link</em>' reference.
	 * @see #getRhsLink()
	 * @generated
	 */
	void setRhsLink(ChangeSetRowData value);

	/**
	 * Returns the value of the '<em><b>Lhs Slot</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Lhs Slot</em>' attribute.
	 * @see #setLhsSlot(boolean)
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getChangeSetRowData_LhsSlot()
	 * @model
	 * @generated
	 */
	boolean isLhsSlot();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowData#isLhsSlot <em>Lhs Slot</em>}' attribute.
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
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Lhs Spot</em>' attribute.
	 * @see #setLhsSpot(boolean)
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getChangeSetRowData_LhsSpot()
	 * @model
	 * @generated
	 */
	boolean isLhsSpot();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowData#isLhsSpot <em>Lhs Spot</em>}' attribute.
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
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Lhs Optional</em>' attribute.
	 * @see #setLhsOptional(boolean)
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getChangeSetRowData_LhsOptional()
	 * @model
	 * @generated
	 */
	boolean isLhsOptional();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowData#isLhsOptional <em>Lhs Optional</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Lhs Optional</em>' attribute.
	 * @see #isLhsOptional()
	 * @generated
	 */
	void setLhsOptional(boolean value);

	/**
	 * Returns the value of the '<em><b>Rhs Slot</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Rhs Slot</em>' attribute.
	 * @see #setRhsSlot(boolean)
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getChangeSetRowData_RhsSlot()
	 * @model
	 * @generated
	 */
	boolean isRhsSlot();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowData#isRhsSlot <em>Rhs Slot</em>}' attribute.
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
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Rhs Spot</em>' attribute.
	 * @see #setRhsSpot(boolean)
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getChangeSetRowData_RhsSpot()
	 * @model
	 * @generated
	 */
	boolean isRhsSpot();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowData#isRhsSpot <em>Rhs Spot</em>}' attribute.
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
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Rhs Optional</em>' attribute.
	 * @see #setRhsOptional(boolean)
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getChangeSetRowData_RhsOptional()
	 * @model
	 * @generated
	 */
	boolean isRhsOptional();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowData#isRhsOptional <em>Rhs Optional</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Rhs Optional</em>' attribute.
	 * @see #isRhsOptional()
	 * @generated
	 */
	void setRhsOptional(boolean value);

	/**
	 * Returns the value of the '<em><b>Lhs Non Shipped</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Lhs Non Shipped</em>' attribute.
	 * @see #setLhsNonShipped(boolean)
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getChangeSetRowData_LhsNonShipped()
	 * @model
	 * @generated
	 */
	boolean isLhsNonShipped();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowData#isLhsNonShipped <em>Lhs Non Shipped</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Lhs Non Shipped</em>' attribute.
	 * @see #isLhsNonShipped()
	 * @generated
	 */
	void setLhsNonShipped(boolean value);

	/**
	 * Returns the value of the '<em><b>Rhs Non Shipped</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Rhs Non Shipped</em>' attribute.
	 * @see #setRhsNonShipped(boolean)
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getChangeSetRowData_RhsNonShipped()
	 * @model
	 * @generated
	 */
	boolean isRhsNonShipped();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowData#isRhsNonShipped <em>Rhs Non Shipped</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Rhs Non Shipped</em>' attribute.
	 * @see #isRhsNonShipped()
	 * @generated
	 */
	void setRhsNonShipped(boolean value);

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
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getChangeSetRowData_LoadSlot()
	 * @model
	 * @generated
	 */
	LoadSlot getLoadSlot();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowData#getLoadSlot <em>Load Slot</em>}' reference.
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
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getChangeSetRowData_DischargeSlot()
	 * @model
	 * @generated
	 */
	DischargeSlot getDischargeSlot();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowData#getDischargeSlot <em>Discharge Slot</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Discharge Slot</em>' reference.
	 * @see #getDischargeSlot()
	 * @generated
	 */
	void setDischargeSlot(DischargeSlot value);

	/**
	 * Returns the value of the '<em><b>Load Allocation</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Load Allocation</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Load Allocation</em>' reference.
	 * @see #setLoadAllocation(SlotAllocation)
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getChangeSetRowData_LoadAllocation()
	 * @model
	 * @generated
	 */
	SlotAllocation getLoadAllocation();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowData#getLoadAllocation <em>Load Allocation</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Load Allocation</em>' reference.
	 * @see #getLoadAllocation()
	 * @generated
	 */
	void setLoadAllocation(SlotAllocation value);

	/**
	 * Returns the value of the '<em><b>Discharge Allocation</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Discharge Allocation</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Discharge Allocation</em>' reference.
	 * @see #setDischargeAllocation(SlotAllocation)
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getChangeSetRowData_DischargeAllocation()
	 * @model
	 * @generated
	 */
	SlotAllocation getDischargeAllocation();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowData#getDischargeAllocation <em>Discharge Allocation</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Discharge Allocation</em>' reference.
	 * @see #getDischargeAllocation()
	 * @generated
	 */
	void setDischargeAllocation(SlotAllocation value);

	/**
	 * Returns the value of the '<em><b>Open Load Allocation</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Open Load Allocation</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Open Load Allocation</em>' reference.
	 * @see #setOpenLoadAllocation(OpenSlotAllocation)
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getChangeSetRowData_OpenLoadAllocation()
	 * @model
	 * @generated
	 */
	OpenSlotAllocation getOpenLoadAllocation();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowData#getOpenLoadAllocation <em>Open Load Allocation</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Open Load Allocation</em>' reference.
	 * @see #getOpenLoadAllocation()
	 * @generated
	 */
	void setOpenLoadAllocation(OpenSlotAllocation value);

	/**
	 * Returns the value of the '<em><b>Open Discharge Allocation</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Open Discharge Allocation</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Open Discharge Allocation</em>' reference.
	 * @see #setOpenDischargeAllocation(OpenSlotAllocation)
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getChangeSetRowData_OpenDischargeAllocation()
	 * @model
	 * @generated
	 */
	OpenSlotAllocation getOpenDischargeAllocation();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowData#getOpenDischargeAllocation <em>Open Discharge Allocation</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Open Discharge Allocation</em>' reference.
	 * @see #getOpenDischargeAllocation()
	 * @generated
	 */
	void setOpenDischargeAllocation(OpenSlotAllocation value);

	/**
	 * Returns the value of the '<em><b>Lhs Event</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Lhs Event</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Lhs Event</em>' reference.
	 * @see #setLhsEvent(Event)
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getChangeSetRowData_LhsEvent()
	 * @model
	 * @generated
	 */
	Event getLhsEvent();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowData#getLhsEvent <em>Lhs Event</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Lhs Event</em>' reference.
	 * @see #getLhsEvent()
	 * @generated
	 */
	void setLhsEvent(Event value);

	/**
	 * Returns the value of the '<em><b>Rhs Event</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Rhs Event</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Rhs Event</em>' reference.
	 * @see #setRhsEvent(Event)
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getChangeSetRowData_RhsEvent()
	 * @model
	 * @generated
	 */
	Event getRhsEvent();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowData#getRhsEvent <em>Rhs Event</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Rhs Event</em>' reference.
	 * @see #getRhsEvent()
	 * @generated
	 */
	void setRhsEvent(Event value);

	/**
	 * Returns the value of the '<em><b>Lhs Group Profit And Loss</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Lhs Group Profit And Loss</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Lhs Group Profit And Loss</em>' reference.
	 * @see #setLhsGroupProfitAndLoss(ProfitAndLossContainer)
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getChangeSetRowData_LhsGroupProfitAndLoss()
	 * @model
	 * @generated
	 */
	ProfitAndLossContainer getLhsGroupProfitAndLoss();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowData#getLhsGroupProfitAndLoss <em>Lhs Group Profit And Loss</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Lhs Group Profit And Loss</em>' reference.
	 * @see #getLhsGroupProfitAndLoss()
	 * @generated
	 */
	void setLhsGroupProfitAndLoss(ProfitAndLossContainer value);

	/**
	 * Returns the value of the '<em><b>Rhs Group Profit And Loss</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Rhs Group Profit And Loss</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Rhs Group Profit And Loss</em>' reference.
	 * @see #setRhsGroupProfitAndLoss(ProfitAndLossContainer)
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getChangeSetRowData_RhsGroupProfitAndLoss()
	 * @model
	 * @generated
	 */
	ProfitAndLossContainer getRhsGroupProfitAndLoss();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowData#getRhsGroupProfitAndLoss <em>Rhs Group Profit And Loss</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Rhs Group Profit And Loss</em>' reference.
	 * @see #getRhsGroupProfitAndLoss()
	 * @generated
	 */
	void setRhsGroupProfitAndLoss(ProfitAndLossContainer value);

	/**
	 * Returns the value of the '<em><b>Paper Deal Allocation</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Paper Deal Allocation</em>' reference.
	 * @see #setPaperDealAllocation(PaperDealAllocation)
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getChangeSetRowData_PaperDealAllocation()
	 * @model
	 * @generated
	 */
	PaperDealAllocation getPaperDealAllocation();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowData#getPaperDealAllocation <em>Paper Deal Allocation</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Paper Deal Allocation</em>' reference.
	 * @see #getPaperDealAllocation()
	 * @generated
	 */
	void setPaperDealAllocation(PaperDealAllocation value);

	/**
	 * Returns the value of the '<em><b>Vessel Charter Number</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Vessel Charter Number</em>' attribute.
	 * @see #setVesselCharterNumber(int)
	 * @see com.mmxlabs.lingo.reports.views.changeset.model.ChangesetPackage#getChangeSetRowData_VesselCharterNumber()
	 * @model
	 * @generated
	 */
	int getVesselCharterNumber();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.changeset.model.ChangeSetRowData#getVesselCharterNumber <em>Vessel Charter Number</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Vessel Charter Number</em>' attribute.
	 * @see #getVesselCharterNumber()
	 * @generated
	 */
	void setVesselCharterNumber(int value);

} // ChangeSetRowData
