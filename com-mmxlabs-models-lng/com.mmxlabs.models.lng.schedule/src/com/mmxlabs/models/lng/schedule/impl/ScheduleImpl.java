/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.schedule.impl;
import java.util.Collection;

import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;
import org.eclipse.emf.ecore.util.InternalEList;

import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Fitness;
import com.mmxlabs.models.lng.schedule.MarketAllocation;
import com.mmxlabs.models.lng.schedule.OpenSlotAllocation;
import com.mmxlabs.models.lng.schedule.Schedule;
import com.mmxlabs.models.lng.schedule.SchedulePackage;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.mmxcore.impl.MMXObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Schedule</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.ScheduleImpl#getSequences <em>Sequences</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.ScheduleImpl#getCargoAllocations <em>Cargo Allocations</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.ScheduleImpl#getOpenSlotAllocations <em>Open Slot Allocations</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.ScheduleImpl#getMarketAllocations <em>Market Allocations</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.ScheduleImpl#getSlotAllocations <em>Slot Allocations</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.ScheduleImpl#getFitnesses <em>Fitnesses</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.ScheduleImpl#getUnusedElements <em>Unused Elements</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ScheduleImpl extends MMXObjectImpl implements Schedule {
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
	 * The cached value of the '{@link #getCargoAllocations() <em>Cargo Allocations</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCargoAllocations()
	 * @generated
	 * @ordered
	 */
	protected EList<CargoAllocation> cargoAllocations;

	/**
	 * The cached value of the '{@link #getOpenSlotAllocations() <em>Open Slot Allocations</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOpenSlotAllocations()
	 * @generated
	 * @ordered
	 */
	protected EList<OpenSlotAllocation> openSlotAllocations;

	/**
	 * The cached value of the '{@link #getMarketAllocations() <em>Market Allocations</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMarketAllocations()
	 * @generated
	 * @ordered
	 */
	protected EList<MarketAllocation> marketAllocations;

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
	 * The cached value of the '{@link #getUnusedElements() <em>Unused Elements</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getUnusedElements()
	 * @generated
	 * @ordered
	 */
	protected EList<EObject> unusedElements;

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
	public EList<CargoAllocation> getCargoAllocations() {
		if (cargoAllocations == null) {
			cargoAllocations = new EObjectContainmentEList<CargoAllocation>(CargoAllocation.class, this, SchedulePackage.SCHEDULE__CARGO_ALLOCATIONS);
		}
		return cargoAllocations;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<OpenSlotAllocation> getOpenSlotAllocations() {
		if (openSlotAllocations == null) {
			openSlotAllocations = new EObjectContainmentEList<OpenSlotAllocation>(OpenSlotAllocation.class, this, SchedulePackage.SCHEDULE__OPEN_SLOT_ALLOCATIONS);
		}
		return openSlotAllocations;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<MarketAllocation> getMarketAllocations() {
		if (marketAllocations == null) {
			marketAllocations = new EObjectContainmentEList<MarketAllocation>(MarketAllocation.class, this, SchedulePackage.SCHEDULE__MARKET_ALLOCATIONS);
		}
		return marketAllocations;
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
	public EList<EObject> getUnusedElements() {
		if (unusedElements == null) {
			unusedElements = new EObjectResolvingEList<EObject>(EObject.class, this, SchedulePackage.SCHEDULE__UNUSED_ELEMENTS);
		}
		return unusedElements;
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
			case SchedulePackage.SCHEDULE__CARGO_ALLOCATIONS:
				return ((InternalEList<?>)getCargoAllocations()).basicRemove(otherEnd, msgs);
			case SchedulePackage.SCHEDULE__OPEN_SLOT_ALLOCATIONS:
				return ((InternalEList<?>)getOpenSlotAllocations()).basicRemove(otherEnd, msgs);
			case SchedulePackage.SCHEDULE__MARKET_ALLOCATIONS:
				return ((InternalEList<?>)getMarketAllocations()).basicRemove(otherEnd, msgs);
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
			case SchedulePackage.SCHEDULE__SEQUENCES:
				return getSequences();
			case SchedulePackage.SCHEDULE__CARGO_ALLOCATIONS:
				return getCargoAllocations();
			case SchedulePackage.SCHEDULE__OPEN_SLOT_ALLOCATIONS:
				return getOpenSlotAllocations();
			case SchedulePackage.SCHEDULE__MARKET_ALLOCATIONS:
				return getMarketAllocations();
			case SchedulePackage.SCHEDULE__SLOT_ALLOCATIONS:
				return getSlotAllocations();
			case SchedulePackage.SCHEDULE__FITNESSES:
				return getFitnesses();
			case SchedulePackage.SCHEDULE__UNUSED_ELEMENTS:
				return getUnusedElements();
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
			case SchedulePackage.SCHEDULE__SEQUENCES:
				getSequences().clear();
				getSequences().addAll((Collection<? extends Sequence>)newValue);
				return;
			case SchedulePackage.SCHEDULE__CARGO_ALLOCATIONS:
				getCargoAllocations().clear();
				getCargoAllocations().addAll((Collection<? extends CargoAllocation>)newValue);
				return;
			case SchedulePackage.SCHEDULE__OPEN_SLOT_ALLOCATIONS:
				getOpenSlotAllocations().clear();
				getOpenSlotAllocations().addAll((Collection<? extends OpenSlotAllocation>)newValue);
				return;
			case SchedulePackage.SCHEDULE__MARKET_ALLOCATIONS:
				getMarketAllocations().clear();
				getMarketAllocations().addAll((Collection<? extends MarketAllocation>)newValue);
				return;
			case SchedulePackage.SCHEDULE__SLOT_ALLOCATIONS:
				getSlotAllocations().clear();
				getSlotAllocations().addAll((Collection<? extends SlotAllocation>)newValue);
				return;
			case SchedulePackage.SCHEDULE__FITNESSES:
				getFitnesses().clear();
				getFitnesses().addAll((Collection<? extends Fitness>)newValue);
				return;
			case SchedulePackage.SCHEDULE__UNUSED_ELEMENTS:
				getUnusedElements().clear();
				getUnusedElements().addAll((Collection<? extends EObject>)newValue);
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
			case SchedulePackage.SCHEDULE__SEQUENCES:
				getSequences().clear();
				return;
			case SchedulePackage.SCHEDULE__CARGO_ALLOCATIONS:
				getCargoAllocations().clear();
				return;
			case SchedulePackage.SCHEDULE__OPEN_SLOT_ALLOCATIONS:
				getOpenSlotAllocations().clear();
				return;
			case SchedulePackage.SCHEDULE__MARKET_ALLOCATIONS:
				getMarketAllocations().clear();
				return;
			case SchedulePackage.SCHEDULE__SLOT_ALLOCATIONS:
				getSlotAllocations().clear();
				return;
			case SchedulePackage.SCHEDULE__FITNESSES:
				getFitnesses().clear();
				return;
			case SchedulePackage.SCHEDULE__UNUSED_ELEMENTS:
				getUnusedElements().clear();
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
			case SchedulePackage.SCHEDULE__SEQUENCES:
				return sequences != null && !sequences.isEmpty();
			case SchedulePackage.SCHEDULE__CARGO_ALLOCATIONS:
				return cargoAllocations != null && !cargoAllocations.isEmpty();
			case SchedulePackage.SCHEDULE__OPEN_SLOT_ALLOCATIONS:
				return openSlotAllocations != null && !openSlotAllocations.isEmpty();
			case SchedulePackage.SCHEDULE__MARKET_ALLOCATIONS:
				return marketAllocations != null && !marketAllocations.isEmpty();
			case SchedulePackage.SCHEDULE__SLOT_ALLOCATIONS:
				return slotAllocations != null && !slotAllocations.isEmpty();
			case SchedulePackage.SCHEDULE__FITNESSES:
				return fitnesses != null && !fitnesses.isEmpty();
			case SchedulePackage.SCHEDULE__UNUSED_ELEMENTS:
				return unusedElements != null && !unusedElements.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} // end of ScheduleImpl

// finish type fixing
