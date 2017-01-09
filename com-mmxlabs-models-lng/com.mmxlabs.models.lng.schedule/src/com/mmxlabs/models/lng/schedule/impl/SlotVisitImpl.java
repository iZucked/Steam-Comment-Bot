/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.schedule.impl;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.EcoreEMap;
import org.eclipse.emf.ecore.util.InternalEList;

import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.schedule.CapacityViolationType;
import com.mmxlabs.models.lng.schedule.CapacityViolationsHolder;
import com.mmxlabs.models.lng.schedule.FuelQuantity;
import com.mmxlabs.models.lng.schedule.FuelUsage;
import com.mmxlabs.models.lng.schedule.PortVisit;
import com.mmxlabs.models.lng.schedule.PortVisitLateness;
import com.mmxlabs.models.lng.schedule.SchedulePackage;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotVisit;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Slot Visit</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.SlotVisitImpl#getFuels <em>Fuels</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.SlotVisitImpl#getViolations <em>Violations</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.SlotVisitImpl#getPortCost <em>Port Cost</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.SlotVisitImpl#getLateness <em>Lateness</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.SlotVisitImpl#getSlotAllocation <em>Slot Allocation</em>}</li>
 * </ul>
 *
 * @generated
 */
public class SlotVisitImpl extends EventImpl implements SlotVisit {
	/**
	 * The cached value of the '{@link #getFuels() <em>Fuels</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFuels()
	 * @generated
	 * @ordered
	 */
	protected EList<FuelQuantity> fuels;
	/**
	 * The cached value of the '{@link #getViolations() <em>Violations</em>}' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getViolations()
	 * @generated
	 * @ordered
	 */
	protected EMap<CapacityViolationType, Long> violations;
	/**
	 * The default value of the '{@link #getPortCost() <em>Port Cost</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPortCost()
	 * @generated
	 * @ordered
	 */
	protected static final int PORT_COST_EDEFAULT = 0;
	/**
	 * The cached value of the '{@link #getPortCost() <em>Port Cost</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPortCost()
	 * @generated
	 * @ordered
	 */
	protected int portCost = PORT_COST_EDEFAULT;
	/**
	 * The cached value of the '{@link #getLateness() <em>Lateness</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLateness()
	 * @generated
	 * @ordered
	 */
	protected PortVisitLateness lateness;
	/**
	 * The cached value of the '{@link #getSlotAllocation() <em>Slot Allocation</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSlotAllocation()
	 * @generated
	 * @ordered
	 */
	protected SlotAllocation slotAllocation;
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected SlotVisitImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return SchedulePackage.Literals.SLOT_VISIT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<FuelQuantity> getFuels() {
		if (fuels == null) {
			fuels = new EObjectContainmentEList<FuelQuantity>(FuelQuantity.class, this, SchedulePackage.SLOT_VISIT__FUELS);
		}
		return fuels;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EMap<CapacityViolationType, Long> getViolations() {
		if (violations == null) {
			violations = new EcoreEMap<CapacityViolationType,Long>(SchedulePackage.Literals.CAPACITY_MAP_ENTRY, CapacityMapEntryImpl.class, this, SchedulePackage.SLOT_VISIT__VIOLATIONS);
		}
		return violations;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getPortCost() {
		return portCost;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setPortCost(int newPortCost) {
		int oldPortCost = portCost;
		portCost = newPortCost;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.SLOT_VISIT__PORT_COST, oldPortCost, portCost));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public PortVisitLateness getLateness() {
		return lateness;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetLateness(PortVisitLateness newLateness, NotificationChain msgs) {
		PortVisitLateness oldLateness = lateness;
		lateness = newLateness;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, SchedulePackage.SLOT_VISIT__LATENESS, oldLateness, newLateness);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setLateness(PortVisitLateness newLateness) {
		if (newLateness != lateness) {
			NotificationChain msgs = null;
			if (lateness != null)
				msgs = ((InternalEObject)lateness).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - SchedulePackage.SLOT_VISIT__LATENESS, null, msgs);
			if (newLateness != null)
				msgs = ((InternalEObject)newLateness).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - SchedulePackage.SLOT_VISIT__LATENESS, null, msgs);
			msgs = basicSetLateness(newLateness, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.SLOT_VISIT__LATENESS, newLateness, newLateness));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SlotAllocation getSlotAllocation() {
		if (slotAllocation != null && slotAllocation.eIsProxy()) {
			InternalEObject oldSlotAllocation = (InternalEObject)slotAllocation;
			slotAllocation = (SlotAllocation)eResolveProxy(oldSlotAllocation);
			if (slotAllocation != oldSlotAllocation) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, SchedulePackage.SLOT_VISIT__SLOT_ALLOCATION, oldSlotAllocation, slotAllocation));
			}
		}
		return slotAllocation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SlotAllocation basicGetSlotAllocation() {
		return slotAllocation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetSlotAllocation(SlotAllocation newSlotAllocation, NotificationChain msgs) {
		SlotAllocation oldSlotAllocation = slotAllocation;
		slotAllocation = newSlotAllocation;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, SchedulePackage.SLOT_VISIT__SLOT_ALLOCATION, oldSlotAllocation, newSlotAllocation);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSlotAllocation(SlotAllocation newSlotAllocation) {
		if (newSlotAllocation != slotAllocation) {
			NotificationChain msgs = null;
			if (slotAllocation != null)
				msgs = ((InternalEObject)slotAllocation).eInverseRemove(this, SchedulePackage.SLOT_ALLOCATION__SLOT_VISIT, SlotAllocation.class, msgs);
			if (newSlotAllocation != null)
				msgs = ((InternalEObject)newSlotAllocation).eInverseAdd(this, SchedulePackage.SLOT_ALLOCATION__SLOT_VISIT, SlotAllocation.class, msgs);
			msgs = basicSetSlotAllocation(newSlotAllocation, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.SLOT_VISIT__SLOT_ALLOCATION, newSlotAllocation, newSlotAllocation));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getFuelCost() {
		int sum = 0;
		for (final FuelQuantity fq : getFuels()) {
			sum += fq.getCost();
		}
		return sum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case SchedulePackage.SLOT_VISIT__SLOT_ALLOCATION:
				if (slotAllocation != null)
					msgs = ((InternalEObject)slotAllocation).eInverseRemove(this, SchedulePackage.SLOT_ALLOCATION__SLOT_VISIT, SlotAllocation.class, msgs);
				return basicSetSlotAllocation((SlotAllocation)otherEnd, msgs);
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
			case SchedulePackage.SLOT_VISIT__FUELS:
				return ((InternalEList<?>)getFuels()).basicRemove(otherEnd, msgs);
			case SchedulePackage.SLOT_VISIT__VIOLATIONS:
				return ((InternalEList<?>)getViolations()).basicRemove(otherEnd, msgs);
			case SchedulePackage.SLOT_VISIT__LATENESS:
				return basicSetLateness(null, msgs);
			case SchedulePackage.SLOT_VISIT__SLOT_ALLOCATION:
				return basicSetSlotAllocation(null, msgs);
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
			case SchedulePackage.SLOT_VISIT__FUELS:
				return getFuels();
			case SchedulePackage.SLOT_VISIT__VIOLATIONS:
				if (coreType) return getViolations();
				else return getViolations().map();
			case SchedulePackage.SLOT_VISIT__PORT_COST:
				return getPortCost();
			case SchedulePackage.SLOT_VISIT__LATENESS:
				return getLateness();
			case SchedulePackage.SLOT_VISIT__SLOT_ALLOCATION:
				if (resolve) return getSlotAllocation();
				return basicGetSlotAllocation();
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
			case SchedulePackage.SLOT_VISIT__FUELS:
				getFuels().clear();
				getFuels().addAll((Collection<? extends FuelQuantity>)newValue);
				return;
			case SchedulePackage.SLOT_VISIT__VIOLATIONS:
				((EStructuralFeature.Setting)getViolations()).set(newValue);
				return;
			case SchedulePackage.SLOT_VISIT__PORT_COST:
				setPortCost((Integer)newValue);
				return;
			case SchedulePackage.SLOT_VISIT__LATENESS:
				setLateness((PortVisitLateness)newValue);
				return;
			case SchedulePackage.SLOT_VISIT__SLOT_ALLOCATION:
				setSlotAllocation((SlotAllocation)newValue);
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
			case SchedulePackage.SLOT_VISIT__FUELS:
				getFuels().clear();
				return;
			case SchedulePackage.SLOT_VISIT__VIOLATIONS:
				getViolations().clear();
				return;
			case SchedulePackage.SLOT_VISIT__PORT_COST:
				setPortCost(PORT_COST_EDEFAULT);
				return;
			case SchedulePackage.SLOT_VISIT__LATENESS:
				setLateness((PortVisitLateness)null);
				return;
			case SchedulePackage.SLOT_VISIT__SLOT_ALLOCATION:
				setSlotAllocation((SlotAllocation)null);
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
			case SchedulePackage.SLOT_VISIT__FUELS:
				return fuels != null && !fuels.isEmpty();
			case SchedulePackage.SLOT_VISIT__VIOLATIONS:
				return violations != null && !violations.isEmpty();
			case SchedulePackage.SLOT_VISIT__PORT_COST:
				return portCost != PORT_COST_EDEFAULT;
			case SchedulePackage.SLOT_VISIT__LATENESS:
				return lateness != null;
			case SchedulePackage.SLOT_VISIT__SLOT_ALLOCATION:
				return slotAllocation != null;
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int eBaseStructuralFeatureID(int derivedFeatureID, Class<?> baseClass) {
		if (baseClass == FuelUsage.class) {
			switch (derivedFeatureID) {
				case SchedulePackage.SLOT_VISIT__FUELS: return SchedulePackage.FUEL_USAGE__FUELS;
				default: return -1;
			}
		}
		if (baseClass == CapacityViolationsHolder.class) {
			switch (derivedFeatureID) {
				case SchedulePackage.SLOT_VISIT__VIOLATIONS: return SchedulePackage.CAPACITY_VIOLATIONS_HOLDER__VIOLATIONS;
				default: return -1;
			}
		}
		if (baseClass == PortVisit.class) {
			switch (derivedFeatureID) {
				case SchedulePackage.SLOT_VISIT__PORT_COST: return SchedulePackage.PORT_VISIT__PORT_COST;
				case SchedulePackage.SLOT_VISIT__LATENESS: return SchedulePackage.PORT_VISIT__LATENESS;
				default: return -1;
			}
		}
		return super.eBaseStructuralFeatureID(derivedFeatureID, baseClass);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int eDerivedStructuralFeatureID(int baseFeatureID, Class<?> baseClass) {
		if (baseClass == FuelUsage.class) {
			switch (baseFeatureID) {
				case SchedulePackage.FUEL_USAGE__FUELS: return SchedulePackage.SLOT_VISIT__FUELS;
				default: return -1;
			}
		}
		if (baseClass == CapacityViolationsHolder.class) {
			switch (baseFeatureID) {
				case SchedulePackage.CAPACITY_VIOLATIONS_HOLDER__VIOLATIONS: return SchedulePackage.SLOT_VISIT__VIOLATIONS;
				default: return -1;
			}
		}
		if (baseClass == PortVisit.class) {
			switch (baseFeatureID) {
				case SchedulePackage.PORT_VISIT__PORT_COST: return SchedulePackage.SLOT_VISIT__PORT_COST;
				case SchedulePackage.PORT_VISIT__LATENESS: return SchedulePackage.SLOT_VISIT__LATENESS;
				default: return -1;
			}
		}
		return super.eDerivedStructuralFeatureID(baseFeatureID, baseClass);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int eDerivedOperationID(int baseOperationID, Class<?> baseClass) {
		if (baseClass == FuelUsage.class) {
			switch (baseOperationID) {
				case SchedulePackage.FUEL_USAGE___GET_FUEL_COST: return SchedulePackage.SLOT_VISIT___GET_FUEL_COST;
				default: return -1;
			}
		}
		if (baseClass == CapacityViolationsHolder.class) {
			switch (baseOperationID) {
				default: return -1;
			}
		}
		if (baseClass == PortVisit.class) {
			switch (baseOperationID) {
				default: return -1;
			}
		}
		return super.eDerivedOperationID(baseOperationID, baseClass);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eInvoke(int operationID, EList<?> arguments) throws InvocationTargetException {
		switch (operationID) {
			case SchedulePackage.SLOT_VISIT___GET_FUEL_COST:
				return getFuelCost();
		}
		return super.eInvoke(operationID, arguments);
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
		result.append(" (portCost: ");
		result.append(portCost);
		result.append(')');
		return result.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public String type() {
		
		final SlotAllocation slotAllocation = getSlotAllocation();
		final Slot slot = slotAllocation.getSlot();
		if (slot instanceof LoadSlot) {
			return "Load";
		} else if (slot instanceof DischargeSlot) {
			return "Discharge";
		}
		return "Unknown";
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public String name() {
		final SlotAllocation slotAllocation = getSlotAllocation();
		final Slot slot = slotAllocation.getSlot();
		if (slot != null) {
//
//			// Show cargo ID rather than slot ID.
//			// TODO: Perhaps this should be a UI configurable option
//			if (slot instanceof LoadSlot) {
//				final Cargo cargo = ((LoadSlot) slot).getCargo();
//				if (cargo != null) {
//					return cargo.getLoadName();
//				}
//			} else if (slot instanceof DischargeSlot) {
//				final Cargo cargo = ((DischargeSlot) slot).getCargo();
//				if (cargo != null) {
//					return cargo.getLoadName();
//				}
//			}

			return slot.getName();
		}
		return "";
	}
	
} // end of SlotVisitImpl

// finish type fixing
