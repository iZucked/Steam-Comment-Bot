/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.lingo.reports.views.schedule.model;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.OpenSlotAllocation;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.scenario.service.model.manager.IScenarioDataProvider;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Row</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.lingo.reports.views.schedule.model.Row#getScenarioName <em>Scenario Name</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.schedule.model.Row#isVisible <em>Visible</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.schedule.model.Row#getInputEquivalents <em>Input Equivalents</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.schedule.model.Row#getName <em>Name</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.schedule.model.Row#getName2 <em>Name2</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.schedule.model.Row#getTarget <em>Target</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.schedule.model.Row#getSchedule <em>Schedule</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.schedule.model.Row#getSequence <em>Sequence</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.schedule.model.Row#getCargoAllocation <em>Cargo Allocation</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.schedule.model.Row#getLoadAllocation <em>Load Allocation</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.schedule.model.Row#getDischargeAllocation <em>Discharge Allocation</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.schedule.model.Row#getOpenLoadSlotAllocation <em>Open Load Slot Allocation</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.schedule.model.Row#getOpenDischargeSlotAllocation <em>Open Discharge Slot Allocation</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.schedule.model.Row#isReference <em>Reference</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.schedule.model.Row#getLhsLink <em>Lhs Link</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.schedule.model.Row#getRhsLink <em>Rhs Link</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.schedule.model.Row#getRowGroup <em>Row Group</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.schedule.model.Row#getScenarioDataProvider <em>Scenario Data Provider</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.lingo.reports.views.schedule.model.ScheduleReportPackage#getRow()
 * @model
 * @generated
 */
public interface Row extends EObject {
	/**
	 * Returns the value of the '<em><b>Scenario Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Scenario Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Scenario Name</em>' attribute.
	 * @see #setScenarioName(String)
	 * @see com.mmxlabs.lingo.reports.views.schedule.model.ScheduleReportPackage#getRow_ScenarioName()
	 * @model
	 * @generated
	 */
	String getScenarioName();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.schedule.model.Row#getScenarioName <em>Scenario Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Scenario Name</em>' attribute.
	 * @see #getScenarioName()
	 * @generated
	 */
	void setScenarioName(String value);

	/**
	 * Returns the value of the '<em><b>Visible</b></em>' attribute.
	 * The default value is <code>"true"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Visible</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Visible</em>' attribute.
	 * @see #setVisible(boolean)
	 * @see com.mmxlabs.lingo.reports.views.schedule.model.ScheduleReportPackage#getRow_Visible()
	 * @model default="true"
	 * @generated
	 */
	boolean isVisible();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.schedule.model.Row#isVisible <em>Visible</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Visible</em>' attribute.
	 * @see #isVisible()
	 * @generated
	 */
	void setVisible(boolean value);

	/**
	 * Returns the value of the '<em><b>Input Equivalents</b></em>' reference list.
	 * The list contents are of type {@link org.eclipse.emf.ecore.EObject}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Input Equivalents</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Input Equivalents</em>' reference list.
	 * @see com.mmxlabs.lingo.reports.views.schedule.model.ScheduleReportPackage#getRow_InputEquivalents()
	 * @model
	 * @generated
	 */
	EList<EObject> getInputEquivalents();

	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see com.mmxlabs.lingo.reports.views.schedule.model.ScheduleReportPackage#getRow_Name()
	 * @model
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.schedule.model.Row#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Name2</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Name2</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Name2</em>' attribute.
	 * @see #setName2(String)
	 * @see com.mmxlabs.lingo.reports.views.schedule.model.ScheduleReportPackage#getRow_Name2()
	 * @model
	 * @generated
	 */
	String getName2();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.schedule.model.Row#getName2 <em>Name2</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name2</em>' attribute.
	 * @see #getName2()
	 * @generated
	 */
	void setName2(String value);

	/**
	 * Returns the value of the '<em><b>Target</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Target</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Target</em>' reference.
	 * @see #setTarget(EObject)
	 * @see com.mmxlabs.lingo.reports.views.schedule.model.ScheduleReportPackage#getRow_Target()
	 * @model
	 * @generated
	 */
	EObject getTarget();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.schedule.model.Row#getTarget <em>Target</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Target</em>' reference.
	 * @see #getTarget()
	 * @generated
	 */
	void setTarget(EObject value);

	/**
	 * Returns the value of the '<em><b>Schedule</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Schedule</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Schedule</em>' reference.
	 * @see #setSchedule(Schedule)
	 * @see com.mmxlabs.lingo.reports.views.schedule.model.ScheduleReportPackage#getRow_Schedule()
	 * @model
	 * @generated
	 */
	Schedule getSchedule();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.schedule.model.Row#getSchedule <em>Schedule</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Schedule</em>' reference.
	 * @see #getSchedule()
	 * @generated
	 */
	void setSchedule(Schedule value);

	/**
	 * Returns the value of the '<em><b>Sequence</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Sequence</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Sequence</em>' reference.
	 * @see #setSequence(Sequence)
	 * @see com.mmxlabs.lingo.reports.views.schedule.model.ScheduleReportPackage#getRow_Sequence()
	 * @model
	 * @generated
	 */
	Sequence getSequence();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.schedule.model.Row#getSequence <em>Sequence</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Sequence</em>' reference.
	 * @see #getSequence()
	 * @generated
	 */
	void setSequence(Sequence value);

	/**
	 * Returns the value of the '<em><b>Cargo Allocation</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Cargo Allocation</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Cargo Allocation</em>' reference.
	 * @see #setCargoAllocation(CargoAllocation)
	 * @see com.mmxlabs.lingo.reports.views.schedule.model.ScheduleReportPackage#getRow_CargoAllocation()
	 * @model
	 * @generated
	 */
	CargoAllocation getCargoAllocation();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.schedule.model.Row#getCargoAllocation <em>Cargo Allocation</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Cargo Allocation</em>' reference.
	 * @see #getCargoAllocation()
	 * @generated
	 */
	void setCargoAllocation(CargoAllocation value);

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
	 * @see com.mmxlabs.lingo.reports.views.schedule.model.ScheduleReportPackage#getRow_LoadAllocation()
	 * @model
	 * @generated
	 */
	SlotAllocation getLoadAllocation();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.schedule.model.Row#getLoadAllocation <em>Load Allocation</em>}' reference.
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
	 * @see com.mmxlabs.lingo.reports.views.schedule.model.ScheduleReportPackage#getRow_DischargeAllocation()
	 * @model
	 * @generated
	 */
	SlotAllocation getDischargeAllocation();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.schedule.model.Row#getDischargeAllocation <em>Discharge Allocation</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Discharge Allocation</em>' reference.
	 * @see #getDischargeAllocation()
	 * @generated
	 */
	void setDischargeAllocation(SlotAllocation value);

	/**
	 * Returns the value of the '<em><b>Open Load Slot Allocation</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Open Load Slot Allocation</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Open Load Slot Allocation</em>' reference.
	 * @see #setOpenLoadSlotAllocation(OpenSlotAllocation)
	 * @see com.mmxlabs.lingo.reports.views.schedule.model.ScheduleReportPackage#getRow_OpenLoadSlotAllocation()
	 * @model
	 * @generated
	 */
	OpenSlotAllocation getOpenLoadSlotAllocation();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.schedule.model.Row#getOpenLoadSlotAllocation <em>Open Load Slot Allocation</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Open Load Slot Allocation</em>' reference.
	 * @see #getOpenLoadSlotAllocation()
	 * @generated
	 */
	void setOpenLoadSlotAllocation(OpenSlotAllocation value);

	/**
	 * Returns the value of the '<em><b>Open Discharge Slot Allocation</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Open Discharge Slot Allocation</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Open Discharge Slot Allocation</em>' reference.
	 * @see #setOpenDischargeSlotAllocation(OpenSlotAllocation)
	 * @see com.mmxlabs.lingo.reports.views.schedule.model.ScheduleReportPackage#getRow_OpenDischargeSlotAllocation()
	 * @model
	 * @generated
	 */
	OpenSlotAllocation getOpenDischargeSlotAllocation();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.schedule.model.Row#getOpenDischargeSlotAllocation <em>Open Discharge Slot Allocation</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Open Discharge Slot Allocation</em>' reference.
	 * @see #getOpenDischargeSlotAllocation()
	 * @generated
	 */
	void setOpenDischargeSlotAllocation(OpenSlotAllocation value);

	/**
	 * Returns the value of the '<em><b>Reference</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Reference</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Reference</em>' attribute.
	 * @see #setReference(boolean)
	 * @see com.mmxlabs.lingo.reports.views.schedule.model.ScheduleReportPackage#getRow_Reference()
	 * @model
	 * @generated
	 */
	boolean isReference();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.schedule.model.Row#isReference <em>Reference</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Reference</em>' attribute.
	 * @see #isReference()
	 * @generated
	 */
	void setReference(boolean value);

	/**
	 * Returns the value of the '<em><b>Lhs Link</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Lhs Link</em>' reference.
	 * @see #setLhsLink(Row)
	 * @see com.mmxlabs.lingo.reports.views.schedule.model.ScheduleReportPackage#getRow_LhsLink()
	 * @model
	 * @generated
	 */
	Row getLhsLink();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.schedule.model.Row#getLhsLink <em>Lhs Link</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Lhs Link</em>' reference.
	 * @see #getLhsLink()
	 * @generated
	 */
	void setLhsLink(Row value);

	/**
	 * Returns the value of the '<em><b>Rhs Link</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Rhs Link</em>' reference.
	 * @see #setRhsLink(Row)
	 * @see com.mmxlabs.lingo.reports.views.schedule.model.ScheduleReportPackage#getRow_RhsLink()
	 * @model
	 * @generated
	 */
	Row getRhsLink();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.schedule.model.Row#getRhsLink <em>Rhs Link</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Rhs Link</em>' reference.
	 * @see #getRhsLink()
	 * @generated
	 */
	void setRhsLink(Row value);

	/**
	 * Returns the value of the '<em><b>Row Group</b></em>' reference.
	 * It is bidirectional and its opposite is '{@link com.mmxlabs.lingo.reports.views.schedule.model.RowGroup#getRows <em>Rows</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Row Group</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Row Group</em>' reference.
	 * @see #setRowGroup(RowGroup)
	 * @see com.mmxlabs.lingo.reports.views.schedule.model.ScheduleReportPackage#getRow_RowGroup()
	 * @see com.mmxlabs.lingo.reports.views.schedule.model.RowGroup#getRows
	 * @model opposite="rows"
	 * @generated
	 */
	RowGroup getRowGroup();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.schedule.model.Row#getRowGroup <em>Row Group</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Row Group</em>' reference.
	 * @see #getRowGroup()
	 * @generated
	 */
	void setRowGroup(RowGroup value);

	/**
	 * Returns the value of the '<em><b>Scenario Data Provider</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Scenario Data Provider</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Scenario Data Provider</em>' attribute.
	 * @see #setScenarioDataProvider(IScenarioDataProvider)
	 * @see com.mmxlabs.lingo.reports.views.schedule.model.ScheduleReportPackage#getRow_ScenarioDataProvider()
	 * @model dataType="com.mmxlabs.lingo.reports.views.schedule.model.IScenarioDataProvider" transient="true"
	 * @generated
	 */
	IScenarioDataProvider getScenarioDataProvider();

	/**
	 * Sets the value of the '{@link com.mmxlabs.lingo.reports.views.schedule.model.Row#getScenarioDataProvider <em>Scenario Data Provider</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Scenario Data Provider</em>' attribute.
	 * @see #getScenarioDataProvider()
	 * @generated
	 */
	void setScenarioDataProvider(IScenarioDataProvider value);

} // Row
