/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.models.lng.schedule.impl;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Fitness;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.SchedulePackage;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.UnscheduledCargo;

import com.mmxlabs.models.mmxcore.impl.MMXObjectImpl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Schedule</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.ScheduleImpl#isComplete <em>Complete</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.ScheduleImpl#getSequences <em>Sequences</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.ScheduleImpl#getUnscheduledCargos <em>Unscheduled Cargos</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.ScheduleImpl#getAllocations <em>Allocations</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.ScheduleImpl#getSlotAllocations <em>Slot Allocations</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.ScheduleImpl#getFitnesses <em>Fitnesses</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ScheduleImpl extends MMXObjectImpl implements Schedule {
	/**
	 * The default value of the '{@link #isComplete() <em>Complete</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isComplete()
	 * @generated
	 * @ordered
	 */
	protected static final boolean COMPLETE_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isComplete() <em>Complete</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isComplete()
	 * @generated
	 * @ordered
	 */
	protected boolean complete = COMPLETE_EDEFAULT;

	/**
	 * The cached value of the '{@link #getSequences() <em>Sequences</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSequences()
	 * @generated
	 * @ordered
	 */
	protected EList<Sequence> sequences;

	/**
	 * The cached value of the '{@link #getUnscheduledCargos() <em>Unscheduled Cargos</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getUnscheduledCargos()
	 * @generated
	 * @ordered
	 */
	protected EList<UnscheduledCargo> unscheduledCargos;

	/**
	 * The cached value of the '{@link #getAllocations() <em>Allocations</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAllocations()
	 * @generated
	 * @ordered
	 */
	protected EList<CargoAllocation> allocations;

	/**
	 * The cached value of the '{@link #getSlotAllocations() <em>Slot Allocations</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSlotAllocations()
	 * @generated
	 * @ordered
	 */
	protected EList<SlotAllocation> slotAllocations;

	/**
	 * The cached value of the '{@link #getFitnesses() <em>Fitnesses</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFitnesses()
	 * @generated
	 * @ordered
	 */
	protected EList<Fitness> fitnesses;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ScheduleImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return SchedulePackage.Literals.SCHEDULE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isComplete() {
		return complete;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setComplete(boolean newComplete) {
		boolean oldComplete = complete;
		complete = newComplete;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.SCHEDULE__COMPLETE, oldComplete, complete));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Sequence> getSequences() {
		if (sequences == null) {
			sequences = new EObjectContainmentEList<Sequence>(Sequence.class, this, SchedulePackage.SCHEDULE__SEQUENCES);
		}
		return sequences;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<UnscheduledCargo> getUnscheduledCargos() {
		if (unscheduledCargos == null) {
			unscheduledCargos = new EObjectContainmentEList<UnscheduledCargo>(UnscheduledCargo.class, this, SchedulePackage.SCHEDULE__UNSCHEDULED_CARGOS);
		}
		return unscheduledCargos;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<CargoAllocation> getAllocations() {
		if (allocations == null) {
			allocations = new EObjectResolvingEList<CargoAllocation>(CargoAllocation.class, this, SchedulePackage.SCHEDULE__ALLOCATIONS);
		}
		return allocations;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<SlotAllocation> getSlotAllocations() {
		if (slotAllocations == null) {
			slotAllocations = new EObjectContainmentEList<SlotAllocation>(SlotAllocation.class, this, SchedulePackage.SCHEDULE__SLOT_ALLOCATIONS);
		}
		return slotAllocations;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Fitness> getFitnesses() {
		if (fitnesses == null) {
			fitnesses = new EObjectContainmentEList<Fitness>(Fitness.class, this, SchedulePackage.SCHEDULE__FITNESSES);
		}
		return fitnesses;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case SchedulePackage.SCHEDULE__SEQUENCES:
				return ((InternalEList<?>)getSequences()).basicRemove(otherEnd, msgs);
			case SchedulePackage.SCHEDULE__UNSCHEDULED_CARGOS:
				return ((InternalEList<?>)getUnscheduledCargos()).basicRemove(otherEnd, msgs);
			case SchedulePackage.SCHEDULE__SLOT_ALLOCATIONS:
				return ((InternalEList<?>)getSlotAllocations()).basicRemove(otherEnd, msgs);
			case SchedulePackage.SCHEDULE__FITNESSES:
				return ((InternalEList<?>)getFitnesses()).basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case SchedulePackage.SCHEDULE__COMPLETE:
				return isComplete();
			case SchedulePackage.SCHEDULE__SEQUENCES:
				return getSequences();
			case SchedulePackage.SCHEDULE__UNSCHEDULED_CARGOS:
				return getUnscheduledCargos();
			case SchedulePackage.SCHEDULE__ALLOCATIONS:
				return getAllocations();
			case SchedulePackage.SCHEDULE__SLOT_ALLOCATIONS:
				return getSlotAllocations();
			case SchedulePackage.SCHEDULE__FITNESSES:
				return getFitnesses();
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
			case SchedulePackage.SCHEDULE__COMPLETE:
				setComplete((Boolean)newValue);
				return;
			case SchedulePackage.SCHEDULE__SEQUENCES:
				getSequences().clear();
				getSequences().addAll((Collection<? extends Sequence>)newValue);
				return;
			case SchedulePackage.SCHEDULE__UNSCHEDULED_CARGOS:
				getUnscheduledCargos().clear();
				getUnscheduledCargos().addAll((Collection<? extends UnscheduledCargo>)newValue);
				return;
			case SchedulePackage.SCHEDULE__ALLOCATIONS:
				getAllocations().clear();
				getAllocations().addAll((Collection<? extends CargoAllocation>)newValue);
				return;
			case SchedulePackage.SCHEDULE__SLOT_ALLOCATIONS:
				getSlotAllocations().clear();
				getSlotAllocations().addAll((Collection<? extends SlotAllocation>)newValue);
				return;
			case SchedulePackage.SCHEDULE__FITNESSES:
				getFitnesses().clear();
				getFitnesses().addAll((Collection<? extends Fitness>)newValue);
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
			case SchedulePackage.SCHEDULE__COMPLETE:
				setComplete(COMPLETE_EDEFAULT);
				return;
			case SchedulePackage.SCHEDULE__SEQUENCES:
				getSequences().clear();
				return;
			case SchedulePackage.SCHEDULE__UNSCHEDULED_CARGOS:
				getUnscheduledCargos().clear();
				return;
			case SchedulePackage.SCHEDULE__ALLOCATIONS:
				getAllocations().clear();
				return;
			case SchedulePackage.SCHEDULE__SLOT_ALLOCATIONS:
				getSlotAllocations().clear();
				return;
			case SchedulePackage.SCHEDULE__FITNESSES:
				getFitnesses().clear();
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
			case SchedulePackage.SCHEDULE__COMPLETE:
				return complete != COMPLETE_EDEFAULT;
			case SchedulePackage.SCHEDULE__SEQUENCES:
				return sequences != null && !sequences.isEmpty();
			case SchedulePackage.SCHEDULE__UNSCHEDULED_CARGOS:
				return unscheduledCargos != null && !unscheduledCargos.isEmpty();
			case SchedulePackage.SCHEDULE__ALLOCATIONS:
				return allocations != null && !allocations.isEmpty();
			case SchedulePackage.SCHEDULE__SLOT_ALLOCATIONS:
				return slotAllocations != null && !slotAllocations.isEmpty();
			case SchedulePackage.SCHEDULE__FITNESSES:
				return fitnesses != null && !fitnesses.isEmpty();
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
		result.append(" (complete: ");
		result.append(complete);
		result.append(')');
		return result.toString();
	}

} // end of ScheduleImpl

// finish type fixing
