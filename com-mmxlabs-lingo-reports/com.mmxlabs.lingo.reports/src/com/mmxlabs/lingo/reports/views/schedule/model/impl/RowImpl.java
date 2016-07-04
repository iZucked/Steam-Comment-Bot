/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.lingo.reports.views.schedule.model.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;
import org.eclipse.emf.ecore.util.EObjectWithInverseResolvingEList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.InternalEList;

import com.mmxlabs.lingo.reports.views.schedule.model.CycleGroup;
import com.mmxlabs.lingo.reports.views.schedule.model.Row;
import com.mmxlabs.lingo.reports.views.schedule.model.RowGroup;
import com.mmxlabs.lingo.reports.views.schedule.model.ScheduleReportPackage;
import com.mmxlabs.lingo.reports.views.schedule.model.Table;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.OpenSlotAllocation;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SlotAllocation;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Row</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.mmxlabs.lingo.reports.views.schedule.model.impl.RowImpl#isVisible <em>Visible</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.schedule.model.impl.RowImpl#getCycleGroup <em>Cycle Group</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.schedule.model.impl.RowImpl#getInputEquivalents <em>Input Equivalents</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.schedule.model.impl.RowImpl#getName <em>Name</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.schedule.model.impl.RowImpl#getName2 <em>Name2</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.schedule.model.impl.RowImpl#getTarget <em>Target</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.schedule.model.impl.RowImpl#getSchedule <em>Schedule</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.schedule.model.impl.RowImpl#getSequence <em>Sequence</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.schedule.model.impl.RowImpl#getCargoAllocation <em>Cargo Allocation</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.schedule.model.impl.RowImpl#getLoadAllocation <em>Load Allocation</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.schedule.model.impl.RowImpl#getDischargeAllocation <em>Discharge Allocation</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.schedule.model.impl.RowImpl#getOpenSlotAllocation <em>Open Slot Allocation</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.schedule.model.impl.RowImpl#getReferenceRow <em>Reference Row</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.schedule.model.impl.RowImpl#getReferringRows <em>Referring Rows</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.schedule.model.impl.RowImpl#isReference <em>Reference</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.schedule.model.impl.RowImpl#getRowGroup <em>Row Group</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.schedule.model.impl.RowImpl#getScenario <em>Scenario</em>}</li>
 *   <li>{@link com.mmxlabs.lingo.reports.views.schedule.model.impl.RowImpl#getTable <em>Table</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class RowImpl extends MinimalEObjectImpl.Container implements Row {
	/**
	 * The default value of the '{@link #isVisible() <em>Visible</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isVisible()
	 * @generated
	 * @ordered
	 */
	protected static final boolean VISIBLE_EDEFAULT = true;

	/**
	 * The cached value of the '{@link #isVisible() <em>Visible</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isVisible()
	 * @generated
	 * @ordered
	 */
	protected boolean visible = VISIBLE_EDEFAULT;

	/**
	 * The cached value of the '{@link #getCycleGroup() <em>Cycle Group</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCycleGroup()
	 * @generated
	 * @ordered
	 */
	protected CycleGroup cycleGroup;

	/**
	 * The cached value of the '{@link #getInputEquivalents() <em>Input Equivalents</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getInputEquivalents()
	 * @generated
	 * @ordered
	 */
	protected EList<EObject> inputEquivalents;

	/**
	 * The default value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected static final String NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected String name = NAME_EDEFAULT;

	/**
	 * The default value of the '{@link #getName2() <em>Name2</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName2()
	 * @generated
	 * @ordered
	 */
	protected static final String NAME2_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getName2() <em>Name2</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName2()
	 * @generated
	 * @ordered
	 */
	protected String name2 = NAME2_EDEFAULT;

	/**
	 * The cached value of the '{@link #getTarget() <em>Target</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTarget()
	 * @generated
	 * @ordered
	 */
	protected EObject target;

	/**
	 * The cached value of the '{@link #getSchedule() <em>Schedule</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSchedule()
	 * @generated
	 * @ordered
	 */
	protected Schedule schedule;

	/**
	 * The cached value of the '{@link #getSequence() <em>Sequence</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSequence()
	 * @generated
	 * @ordered
	 */
	protected Sequence sequence;

	/**
	 * The cached value of the '{@link #getCargoAllocation() <em>Cargo Allocation</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCargoAllocation()
	 * @generated
	 * @ordered
	 */
	protected CargoAllocation cargoAllocation;

	/**
	 * The cached value of the '{@link #getLoadAllocation() <em>Load Allocation</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLoadAllocation()
	 * @generated
	 * @ordered
	 */
	protected SlotAllocation loadAllocation;

	/**
	 * The cached value of the '{@link #getDischargeAllocation() <em>Discharge Allocation</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDischargeAllocation()
	 * @generated
	 * @ordered
	 */
	protected SlotAllocation dischargeAllocation;

	/**
	 * The cached value of the '{@link #getOpenSlotAllocation() <em>Open Slot Allocation</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOpenSlotAllocation()
	 * @generated
	 * @ordered
	 */
	protected OpenSlotAllocation openSlotAllocation;

	/**
	 * The cached value of the '{@link #getReferenceRow() <em>Reference Row</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getReferenceRow()
	 * @generated
	 * @ordered
	 */
	protected Row referenceRow;

	/**
	 * The cached value of the '{@link #getReferringRows() <em>Referring Rows</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getReferringRows()
	 * @generated
	 * @ordered
	 */
	protected EList<Row> referringRows;

	/**
	 * The default value of the '{@link #isReference() <em>Reference</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isReference()
	 * @generated
	 * @ordered
	 */
	protected static final boolean REFERENCE_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isReference() <em>Reference</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isReference()
	 * @generated
	 * @ordered
	 */
	protected boolean reference = REFERENCE_EDEFAULT;

	/**
	 * The cached value of the '{@link #getRowGroup() <em>Row Group</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRowGroup()
	 * @generated
	 * @ordered
	 */
	protected RowGroup rowGroup;

	/**
	 * The cached value of the '{@link #getScenario() <em>Scenario</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getScenario()
	 * @generated
	 * @ordered
	 */
	protected EObject scenario;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected RowImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ScheduleReportPackage.Literals.ROW;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isVisible() {
		return visible;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setVisible(boolean newVisible) {
		boolean oldVisible = visible;
		visible = newVisible;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ScheduleReportPackage.ROW__VISIBLE, oldVisible, visible));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public CycleGroup getCycleGroup() {
		if (cycleGroup != null && cycleGroup.eIsProxy()) {
			InternalEObject oldCycleGroup = (InternalEObject)cycleGroup;
			cycleGroup = (CycleGroup)eResolveProxy(oldCycleGroup);
			if (cycleGroup != oldCycleGroup) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ScheduleReportPackage.ROW__CYCLE_GROUP, oldCycleGroup, cycleGroup));
			}
		}
		return cycleGroup;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CycleGroup basicGetCycleGroup() {
		return cycleGroup;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetCycleGroup(CycleGroup newCycleGroup, NotificationChain msgs) {
		CycleGroup oldCycleGroup = cycleGroup;
		cycleGroup = newCycleGroup;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ScheduleReportPackage.ROW__CYCLE_GROUP, oldCycleGroup, newCycleGroup);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setCycleGroup(CycleGroup newCycleGroup) {
		if (newCycleGroup != cycleGroup) {
			NotificationChain msgs = null;
			if (cycleGroup != null)
				msgs = ((InternalEObject)cycleGroup).eInverseRemove(this, ScheduleReportPackage.CYCLE_GROUP__ROWS, CycleGroup.class, msgs);
			if (newCycleGroup != null)
				msgs = ((InternalEObject)newCycleGroup).eInverseAdd(this, ScheduleReportPackage.CYCLE_GROUP__ROWS, CycleGroup.class, msgs);
			msgs = basicSetCycleGroup(newCycleGroup, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ScheduleReportPackage.ROW__CYCLE_GROUP, newCycleGroup, newCycleGroup));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<EObject> getInputEquivalents() {
		if (inputEquivalents == null) {
			inputEquivalents = new EObjectResolvingEList<EObject>(EObject.class, this, ScheduleReportPackage.ROW__INPUT_EQUIVALENTS);
		}
		return inputEquivalents;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setName(String newName) {
		String oldName = name;
		name = newName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ScheduleReportPackage.ROW__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getName2() {
		return name2;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setName2(String newName2) {
		String oldName2 = name2;
		name2 = newName2;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ScheduleReportPackage.ROW__NAME2, oldName2, name2));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EObject getTarget() {
		if (target != null && target.eIsProxy()) {
			InternalEObject oldTarget = (InternalEObject)target;
			target = eResolveProxy(oldTarget);
			if (target != oldTarget) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ScheduleReportPackage.ROW__TARGET, oldTarget, target));
			}
		}
		return target;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EObject basicGetTarget() {
		return target;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setTarget(EObject newTarget) {
		EObject oldTarget = target;
		target = newTarget;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ScheduleReportPackage.ROW__TARGET, oldTarget, target));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Schedule getSchedule() {
		if (schedule != null && schedule.eIsProxy()) {
			InternalEObject oldSchedule = (InternalEObject)schedule;
			schedule = (Schedule)eResolveProxy(oldSchedule);
			if (schedule != oldSchedule) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ScheduleReportPackage.ROW__SCHEDULE, oldSchedule, schedule));
			}
		}
		return schedule;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Schedule basicGetSchedule() {
		return schedule;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setSchedule(Schedule newSchedule) {
		Schedule oldSchedule = schedule;
		schedule = newSchedule;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ScheduleReportPackage.ROW__SCHEDULE, oldSchedule, schedule));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Sequence getSequence() {
		if (sequence != null && sequence.eIsProxy()) {
			InternalEObject oldSequence = (InternalEObject)sequence;
			sequence = (Sequence)eResolveProxy(oldSequence);
			if (sequence != oldSequence) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ScheduleReportPackage.ROW__SEQUENCE, oldSequence, sequence));
			}
		}
		return sequence;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Sequence basicGetSequence() {
		return sequence;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSequence(Sequence newSequence) {
		Sequence oldSequence = sequence;
		sequence = newSequence;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ScheduleReportPackage.ROW__SEQUENCE, oldSequence, sequence));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public CargoAllocation getCargoAllocation() {
		if (cargoAllocation != null && cargoAllocation.eIsProxy()) {
			InternalEObject oldCargoAllocation = (InternalEObject)cargoAllocation;
			cargoAllocation = (CargoAllocation)eResolveProxy(oldCargoAllocation);
			if (cargoAllocation != oldCargoAllocation) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ScheduleReportPackage.ROW__CARGO_ALLOCATION, oldCargoAllocation, cargoAllocation));
			}
		}
		return cargoAllocation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CargoAllocation basicGetCargoAllocation() {
		return cargoAllocation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setCargoAllocation(CargoAllocation newCargoAllocation) {
		CargoAllocation oldCargoAllocation = cargoAllocation;
		cargoAllocation = newCargoAllocation;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ScheduleReportPackage.ROW__CARGO_ALLOCATION, oldCargoAllocation, cargoAllocation));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public SlotAllocation getLoadAllocation() {
		if (loadAllocation != null && loadAllocation.eIsProxy()) {
			InternalEObject oldLoadAllocation = (InternalEObject)loadAllocation;
			loadAllocation = (SlotAllocation)eResolveProxy(oldLoadAllocation);
			if (loadAllocation != oldLoadAllocation) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ScheduleReportPackage.ROW__LOAD_ALLOCATION, oldLoadAllocation, loadAllocation));
			}
		}
		return loadAllocation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SlotAllocation basicGetLoadAllocation() {
		return loadAllocation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setLoadAllocation(SlotAllocation newLoadAllocation) {
		SlotAllocation oldLoadAllocation = loadAllocation;
		loadAllocation = newLoadAllocation;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ScheduleReportPackage.ROW__LOAD_ALLOCATION, oldLoadAllocation, loadAllocation));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public SlotAllocation getDischargeAllocation() {
		if (dischargeAllocation != null && dischargeAllocation.eIsProxy()) {
			InternalEObject oldDischargeAllocation = (InternalEObject)dischargeAllocation;
			dischargeAllocation = (SlotAllocation)eResolveProxy(oldDischargeAllocation);
			if (dischargeAllocation != oldDischargeAllocation) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ScheduleReportPackage.ROW__DISCHARGE_ALLOCATION, oldDischargeAllocation, dischargeAllocation));
			}
		}
		return dischargeAllocation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SlotAllocation basicGetDischargeAllocation() {
		return dischargeAllocation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setDischargeAllocation(SlotAllocation newDischargeAllocation) {
		SlotAllocation oldDischargeAllocation = dischargeAllocation;
		dischargeAllocation = newDischargeAllocation;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ScheduleReportPackage.ROW__DISCHARGE_ALLOCATION, oldDischargeAllocation, dischargeAllocation));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public OpenSlotAllocation getOpenSlotAllocation() {
		if (openSlotAllocation != null && openSlotAllocation.eIsProxy()) {
			InternalEObject oldOpenSlotAllocation = (InternalEObject)openSlotAllocation;
			openSlotAllocation = (OpenSlotAllocation)eResolveProxy(oldOpenSlotAllocation);
			if (openSlotAllocation != oldOpenSlotAllocation) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ScheduleReportPackage.ROW__OPEN_SLOT_ALLOCATION, oldOpenSlotAllocation, openSlotAllocation));
			}
		}
		return openSlotAllocation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public OpenSlotAllocation basicGetOpenSlotAllocation() {
		return openSlotAllocation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setOpenSlotAllocation(OpenSlotAllocation newOpenSlotAllocation) {
		OpenSlotAllocation oldOpenSlotAllocation = openSlotAllocation;
		openSlotAllocation = newOpenSlotAllocation;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ScheduleReportPackage.ROW__OPEN_SLOT_ALLOCATION, oldOpenSlotAllocation, openSlotAllocation));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Row getReferenceRow() {
		if (referenceRow != null && referenceRow.eIsProxy()) {
			InternalEObject oldReferenceRow = (InternalEObject)referenceRow;
			referenceRow = (Row)eResolveProxy(oldReferenceRow);
			if (referenceRow != oldReferenceRow) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ScheduleReportPackage.ROW__REFERENCE_ROW, oldReferenceRow, referenceRow));
			}
		}
		return referenceRow;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Row basicGetReferenceRow() {
		return referenceRow;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetReferenceRow(Row newReferenceRow, NotificationChain msgs) {
		Row oldReferenceRow = referenceRow;
		referenceRow = newReferenceRow;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ScheduleReportPackage.ROW__REFERENCE_ROW, oldReferenceRow, newReferenceRow);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setReferenceRow(Row newReferenceRow) {
		if (newReferenceRow != referenceRow) {
			NotificationChain msgs = null;
			if (referenceRow != null)
				msgs = ((InternalEObject)referenceRow).eInverseRemove(this, ScheduleReportPackage.ROW__REFERRING_ROWS, Row.class, msgs);
			if (newReferenceRow != null)
				msgs = ((InternalEObject)newReferenceRow).eInverseAdd(this, ScheduleReportPackage.ROW__REFERRING_ROWS, Row.class, msgs);
			msgs = basicSetReferenceRow(newReferenceRow, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ScheduleReportPackage.ROW__REFERENCE_ROW, newReferenceRow, newReferenceRow));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<Row> getReferringRows() {
		if (referringRows == null) {
			referringRows = new EObjectWithInverseResolvingEList<Row>(Row.class, this, ScheduleReportPackage.ROW__REFERRING_ROWS, ScheduleReportPackage.ROW__REFERENCE_ROW);
		}
		return referringRows;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isReference() {
		return reference;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setReference(boolean newReference) {
		boolean oldReference = reference;
		reference = newReference;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ScheduleReportPackage.ROW__REFERENCE, oldReference, reference));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public RowGroup getRowGroup() {
		if (rowGroup != null && rowGroup.eIsProxy()) {
			InternalEObject oldRowGroup = (InternalEObject)rowGroup;
			rowGroup = (RowGroup)eResolveProxy(oldRowGroup);
			if (rowGroup != oldRowGroup) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ScheduleReportPackage.ROW__ROW_GROUP, oldRowGroup, rowGroup));
			}
		}
		return rowGroup;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public RowGroup basicGetRowGroup() {
		return rowGroup;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetRowGroup(RowGroup newRowGroup, NotificationChain msgs) {
		RowGroup oldRowGroup = rowGroup;
		rowGroup = newRowGroup;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ScheduleReportPackage.ROW__ROW_GROUP, oldRowGroup, newRowGroup);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setRowGroup(RowGroup newRowGroup) {
		if (newRowGroup != rowGroup) {
			NotificationChain msgs = null;
			if (rowGroup != null)
				msgs = ((InternalEObject)rowGroup).eInverseRemove(this, ScheduleReportPackage.ROW_GROUP__ROWS, RowGroup.class, msgs);
			if (newRowGroup != null)
				msgs = ((InternalEObject)newRowGroup).eInverseAdd(this, ScheduleReportPackage.ROW_GROUP__ROWS, RowGroup.class, msgs);
			msgs = basicSetRowGroup(newRowGroup, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ScheduleReportPackage.ROW__ROW_GROUP, newRowGroup, newRowGroup));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EObject getScenario() {
		if (scenario != null && scenario.eIsProxy()) {
			InternalEObject oldScenario = (InternalEObject)scenario;
			scenario = eResolveProxy(oldScenario);
			if (scenario != oldScenario) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ScheduleReportPackage.ROW__SCENARIO, oldScenario, scenario));
			}
		}
		return scenario;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EObject basicGetScenario() {
		return scenario;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setScenario(EObject newScenario) {
		EObject oldScenario = scenario;
		scenario = newScenario;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ScheduleReportPackage.ROW__SCENARIO, oldScenario, scenario));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Table getTable() {
		if (eContainerFeatureID() != ScheduleReportPackage.ROW__TABLE) return null;
		return (Table)eInternalContainer();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetTable(Table newTable, NotificationChain msgs) {
		msgs = eBasicSetContainer((InternalEObject)newTable, ScheduleReportPackage.ROW__TABLE, msgs);
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setTable(Table newTable) {
		if (newTable != eInternalContainer() || (eContainerFeatureID() != ScheduleReportPackage.ROW__TABLE && newTable != null)) {
			if (EcoreUtil.isAncestor(this, newTable))
				throw new IllegalArgumentException("Recursive containment not allowed for " + toString());
			NotificationChain msgs = null;
			if (eInternalContainer() != null)
				msgs = eBasicRemoveFromContainer(msgs);
			if (newTable != null)
				msgs = ((InternalEObject)newTable).eInverseAdd(this, ScheduleReportPackage.TABLE__ROWS, Table.class, msgs);
			msgs = basicSetTable(newTable, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ScheduleReportPackage.ROW__TABLE, newTable, newTable));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case ScheduleReportPackage.ROW__CYCLE_GROUP:
				if (cycleGroup != null)
					msgs = ((InternalEObject)cycleGroup).eInverseRemove(this, ScheduleReportPackage.CYCLE_GROUP__ROWS, CycleGroup.class, msgs);
				return basicSetCycleGroup((CycleGroup)otherEnd, msgs);
			case ScheduleReportPackage.ROW__REFERENCE_ROW:
				if (referenceRow != null)
					msgs = ((InternalEObject)referenceRow).eInverseRemove(this, ScheduleReportPackage.ROW__REFERRING_ROWS, Row.class, msgs);
				return basicSetReferenceRow((Row)otherEnd, msgs);
			case ScheduleReportPackage.ROW__REFERRING_ROWS:
				return ((InternalEList<InternalEObject>)(InternalEList<?>)getReferringRows()).basicAdd(otherEnd, msgs);
			case ScheduleReportPackage.ROW__ROW_GROUP:
				if (rowGroup != null)
					msgs = ((InternalEObject)rowGroup).eInverseRemove(this, ScheduleReportPackage.ROW_GROUP__ROWS, RowGroup.class, msgs);
				return basicSetRowGroup((RowGroup)otherEnd, msgs);
			case ScheduleReportPackage.ROW__TABLE:
				if (eInternalContainer() != null)
					msgs = eBasicRemoveFromContainer(msgs);
				return basicSetTable((Table)otherEnd, msgs);
		}
		return super.eInverseAdd(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case ScheduleReportPackage.ROW__CYCLE_GROUP:
				return basicSetCycleGroup(null, msgs);
			case ScheduleReportPackage.ROW__REFERENCE_ROW:
				return basicSetReferenceRow(null, msgs);
			case ScheduleReportPackage.ROW__REFERRING_ROWS:
				return ((InternalEList<?>)getReferringRows()).basicRemove(otherEnd, msgs);
			case ScheduleReportPackage.ROW__ROW_GROUP:
				return basicSetRowGroup(null, msgs);
			case ScheduleReportPackage.ROW__TABLE:
				return basicSetTable(null, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eBasicRemoveFromContainerFeature(NotificationChain msgs) {
		switch (eContainerFeatureID()) {
			case ScheduleReportPackage.ROW__TABLE:
				return eInternalContainer().eInverseRemove(this, ScheduleReportPackage.TABLE__ROWS, Table.class, msgs);
		}
		return super.eBasicRemoveFromContainerFeature(msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case ScheduleReportPackage.ROW__VISIBLE:
				return isVisible();
			case ScheduleReportPackage.ROW__CYCLE_GROUP:
				if (resolve) return getCycleGroup();
				return basicGetCycleGroup();
			case ScheduleReportPackage.ROW__INPUT_EQUIVALENTS:
				return getInputEquivalents();
			case ScheduleReportPackage.ROW__NAME:
				return getName();
			case ScheduleReportPackage.ROW__NAME2:
				return getName2();
			case ScheduleReportPackage.ROW__TARGET:
				if (resolve) return getTarget();
				return basicGetTarget();
			case ScheduleReportPackage.ROW__SCHEDULE:
				if (resolve) return getSchedule();
				return basicGetSchedule();
			case ScheduleReportPackage.ROW__SEQUENCE:
				if (resolve) return getSequence();
				return basicGetSequence();
			case ScheduleReportPackage.ROW__CARGO_ALLOCATION:
				if (resolve) return getCargoAllocation();
				return basicGetCargoAllocation();
			case ScheduleReportPackage.ROW__LOAD_ALLOCATION:
				if (resolve) return getLoadAllocation();
				return basicGetLoadAllocation();
			case ScheduleReportPackage.ROW__DISCHARGE_ALLOCATION:
				if (resolve) return getDischargeAllocation();
				return basicGetDischargeAllocation();
			case ScheduleReportPackage.ROW__OPEN_SLOT_ALLOCATION:
				if (resolve) return getOpenSlotAllocation();
				return basicGetOpenSlotAllocation();
			case ScheduleReportPackage.ROW__REFERENCE_ROW:
				if (resolve) return getReferenceRow();
				return basicGetReferenceRow();
			case ScheduleReportPackage.ROW__REFERRING_ROWS:
				return getReferringRows();
			case ScheduleReportPackage.ROW__REFERENCE:
				return isReference();
			case ScheduleReportPackage.ROW__ROW_GROUP:
				if (resolve) return getRowGroup();
				return basicGetRowGroup();
			case ScheduleReportPackage.ROW__SCENARIO:
				if (resolve) return getScenario();
				return basicGetScenario();
			case ScheduleReportPackage.ROW__TABLE:
				return getTable();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case ScheduleReportPackage.ROW__VISIBLE:
				setVisible((Boolean)newValue);
				return;
			case ScheduleReportPackage.ROW__CYCLE_GROUP:
				setCycleGroup((CycleGroup)newValue);
				return;
			case ScheduleReportPackage.ROW__INPUT_EQUIVALENTS:
				getInputEquivalents().clear();
				getInputEquivalents().addAll((Collection<? extends EObject>)newValue);
				return;
			case ScheduleReportPackage.ROW__NAME:
				setName((String)newValue);
				return;
			case ScheduleReportPackage.ROW__NAME2:
				setName2((String)newValue);
				return;
			case ScheduleReportPackage.ROW__TARGET:
				setTarget((EObject)newValue);
				return;
			case ScheduleReportPackage.ROW__SCHEDULE:
				setSchedule((Schedule)newValue);
				return;
			case ScheduleReportPackage.ROW__SEQUENCE:
				setSequence((Sequence)newValue);
				return;
			case ScheduleReportPackage.ROW__CARGO_ALLOCATION:
				setCargoAllocation((CargoAllocation)newValue);
				return;
			case ScheduleReportPackage.ROW__LOAD_ALLOCATION:
				setLoadAllocation((SlotAllocation)newValue);
				return;
			case ScheduleReportPackage.ROW__DISCHARGE_ALLOCATION:
				setDischargeAllocation((SlotAllocation)newValue);
				return;
			case ScheduleReportPackage.ROW__OPEN_SLOT_ALLOCATION:
				setOpenSlotAllocation((OpenSlotAllocation)newValue);
				return;
			case ScheduleReportPackage.ROW__REFERENCE_ROW:
				setReferenceRow((Row)newValue);
				return;
			case ScheduleReportPackage.ROW__REFERRING_ROWS:
				getReferringRows().clear();
				getReferringRows().addAll((Collection<? extends Row>)newValue);
				return;
			case ScheduleReportPackage.ROW__REFERENCE:
				setReference((Boolean)newValue);
				return;
			case ScheduleReportPackage.ROW__ROW_GROUP:
				setRowGroup((RowGroup)newValue);
				return;
			case ScheduleReportPackage.ROW__SCENARIO:
				setScenario((EObject)newValue);
				return;
			case ScheduleReportPackage.ROW__TABLE:
				setTable((Table)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case ScheduleReportPackage.ROW__VISIBLE:
				setVisible(VISIBLE_EDEFAULT);
				return;
			case ScheduleReportPackage.ROW__CYCLE_GROUP:
				setCycleGroup((CycleGroup)null);
				return;
			case ScheduleReportPackage.ROW__INPUT_EQUIVALENTS:
				getInputEquivalents().clear();
				return;
			case ScheduleReportPackage.ROW__NAME:
				setName(NAME_EDEFAULT);
				return;
			case ScheduleReportPackage.ROW__NAME2:
				setName2(NAME2_EDEFAULT);
				return;
			case ScheduleReportPackage.ROW__TARGET:
				setTarget((EObject)null);
				return;
			case ScheduleReportPackage.ROW__SCHEDULE:
				setSchedule((Schedule)null);
				return;
			case ScheduleReportPackage.ROW__SEQUENCE:
				setSequence((Sequence)null);
				return;
			case ScheduleReportPackage.ROW__CARGO_ALLOCATION:
				setCargoAllocation((CargoAllocation)null);
				return;
			case ScheduleReportPackage.ROW__LOAD_ALLOCATION:
				setLoadAllocation((SlotAllocation)null);
				return;
			case ScheduleReportPackage.ROW__DISCHARGE_ALLOCATION:
				setDischargeAllocation((SlotAllocation)null);
				return;
			case ScheduleReportPackage.ROW__OPEN_SLOT_ALLOCATION:
				setOpenSlotAllocation((OpenSlotAllocation)null);
				return;
			case ScheduleReportPackage.ROW__REFERENCE_ROW:
				setReferenceRow((Row)null);
				return;
			case ScheduleReportPackage.ROW__REFERRING_ROWS:
				getReferringRows().clear();
				return;
			case ScheduleReportPackage.ROW__REFERENCE:
				setReference(REFERENCE_EDEFAULT);
				return;
			case ScheduleReportPackage.ROW__ROW_GROUP:
				setRowGroup((RowGroup)null);
				return;
			case ScheduleReportPackage.ROW__SCENARIO:
				setScenario((EObject)null);
				return;
			case ScheduleReportPackage.ROW__TABLE:
				setTable((Table)null);
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case ScheduleReportPackage.ROW__VISIBLE:
				return visible != VISIBLE_EDEFAULT;
			case ScheduleReportPackage.ROW__CYCLE_GROUP:
				return cycleGroup != null;
			case ScheduleReportPackage.ROW__INPUT_EQUIVALENTS:
				return inputEquivalents != null && !inputEquivalents.isEmpty();
			case ScheduleReportPackage.ROW__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case ScheduleReportPackage.ROW__NAME2:
				return NAME2_EDEFAULT == null ? name2 != null : !NAME2_EDEFAULT.equals(name2);
			case ScheduleReportPackage.ROW__TARGET:
				return target != null;
			case ScheduleReportPackage.ROW__SCHEDULE:
				return schedule != null;
			case ScheduleReportPackage.ROW__SEQUENCE:
				return sequence != null;
			case ScheduleReportPackage.ROW__CARGO_ALLOCATION:
				return cargoAllocation != null;
			case ScheduleReportPackage.ROW__LOAD_ALLOCATION:
				return loadAllocation != null;
			case ScheduleReportPackage.ROW__DISCHARGE_ALLOCATION:
				return dischargeAllocation != null;
			case ScheduleReportPackage.ROW__OPEN_SLOT_ALLOCATION:
				return openSlotAllocation != null;
			case ScheduleReportPackage.ROW__REFERENCE_ROW:
				return referenceRow != null;
			case ScheduleReportPackage.ROW__REFERRING_ROWS:
				return referringRows != null && !referringRows.isEmpty();
			case ScheduleReportPackage.ROW__REFERENCE:
				return reference != REFERENCE_EDEFAULT;
			case ScheduleReportPackage.ROW__ROW_GROUP:
				return rowGroup != null;
			case ScheduleReportPackage.ROW__SCENARIO:
				return scenario != null;
			case ScheduleReportPackage.ROW__TABLE:
				return getTable() != null;
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (visible: ");
		result.append(visible);
		result.append(", name: ");
		result.append(name);
		result.append(", name2: ");
		result.append(name2);
		result.append(", reference: ");
		result.append(reference);
		result.append(')');
		return result.toString();
	}

} //RowImpl
