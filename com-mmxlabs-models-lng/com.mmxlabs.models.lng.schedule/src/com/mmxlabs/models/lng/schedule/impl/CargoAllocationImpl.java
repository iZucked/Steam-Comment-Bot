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
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;
import org.eclipse.emf.ecore.util.EObjectWithInverseResolvingEList;
import org.eclipse.emf.ecore.util.InternalEList;

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoType;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.EventGrouping;
import com.mmxlabs.models.lng.schedule.GeneralPNLDetails;
import com.mmxlabs.models.lng.schedule.GroupProfitAndLoss;
import com.mmxlabs.models.lng.schedule.ProfitAndLossContainer;
import com.mmxlabs.models.lng.schedule.SchedulePackage;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.mmxcore.impl.MMXObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Cargo Allocation</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.CargoAllocationImpl#getGroupProfitAndLoss <em>Group Profit And Loss</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.CargoAllocationImpl#getGeneralPNLDetails <em>General PNL Details</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.CargoAllocationImpl#getEvents <em>Events</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.CargoAllocationImpl#getSlotAllocations <em>Slot Allocations</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.CargoAllocationImpl#getSequence <em>Sequence</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.CargoAllocationImpl#getCargoType <em>Cargo Type</em>}</li>
 * </ul>
 *
 * @generated
 */
public class CargoAllocationImpl extends MMXObjectImpl implements CargoAllocation {
	/**
	 * The cached value of the '{@link #getGroupProfitAndLoss() <em>Group Profit And Loss</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * @see #getGroupProfitAndLoss()
	 * @generated
	 * @ordered
	 */
	protected GroupProfitAndLoss groupProfitAndLoss;

	/**
	 * The cached value of the '{@link #getGeneralPNLDetails() <em>General PNL Details</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getGeneralPNLDetails()
	 * @generated
	 * @ordered
	 */
	protected EList<GeneralPNLDetails> generalPNLDetails;

	/**
	 * The cached value of the '{@link #getEvents() <em>Events</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEvents()
	 * @generated
	 * @ordered
	 */
	protected EList<Event> events;

	/**
	 * The cached value of the '{@link #getSlotAllocations() <em>Slot Allocations</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSlotAllocations()
	 * @generated
	 * @ordered
	 */
	protected EList<SlotAllocation> slotAllocations;

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
	 * This is true if the Sequence reference has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean sequenceESet;

	/**
	 * The default value of the '{@link #getCargoType() <em>Cargo Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCargoType()
	 * @generated
	 * @ordered
	 */
	protected static final CargoType CARGO_TYPE_EDEFAULT = CargoType.FLEET;

	/**
	 * The cached value of the '{@link #getCargoType() <em>Cargo Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCargoType()
	 * @generated
	 * @ordered
	 */
	protected CargoType cargoType = CARGO_TYPE_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected CargoAllocationImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return SchedulePackage.Literals.CARGO_ALLOCATION;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public GroupProfitAndLoss getGroupProfitAndLoss() {
		return groupProfitAndLoss;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetGroupProfitAndLoss(GroupProfitAndLoss newGroupProfitAndLoss, NotificationChain msgs) {
		GroupProfitAndLoss oldGroupProfitAndLoss = groupProfitAndLoss;
		groupProfitAndLoss = newGroupProfitAndLoss;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, SchedulePackage.CARGO_ALLOCATION__GROUP_PROFIT_AND_LOSS, oldGroupProfitAndLoss, newGroupProfitAndLoss);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setGroupProfitAndLoss(GroupProfitAndLoss newGroupProfitAndLoss) {
		if (newGroupProfitAndLoss != groupProfitAndLoss) {
			NotificationChain msgs = null;
			if (groupProfitAndLoss != null)
				msgs = ((InternalEObject)groupProfitAndLoss).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - SchedulePackage.CARGO_ALLOCATION__GROUP_PROFIT_AND_LOSS, null, msgs);
			if (newGroupProfitAndLoss != null)
				msgs = ((InternalEObject)newGroupProfitAndLoss).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - SchedulePackage.CARGO_ALLOCATION__GROUP_PROFIT_AND_LOSS, null, msgs);
			msgs = basicSetGroupProfitAndLoss(newGroupProfitAndLoss, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.CARGO_ALLOCATION__GROUP_PROFIT_AND_LOSS, newGroupProfitAndLoss, newGroupProfitAndLoss));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<GeneralPNLDetails> getGeneralPNLDetails() {
		if (generalPNLDetails == null) {
			generalPNLDetails = new EObjectContainmentEList<GeneralPNLDetails>(GeneralPNLDetails.class, this, SchedulePackage.CARGO_ALLOCATION__GENERAL_PNL_DETAILS);
		}
		return generalPNLDetails;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Event> getEvents() {
		if (events == null) {
			events = new EObjectResolvingEList<Event>(Event.class, this, SchedulePackage.CARGO_ALLOCATION__EVENTS);
		}
		return events;
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
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, SchedulePackage.CARGO_ALLOCATION__SEQUENCE, oldSequence, sequence));
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
		boolean oldSequenceESet = sequenceESet;
		sequenceESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.CARGO_ALLOCATION__SEQUENCE, oldSequence, sequence, !oldSequenceESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetSequence() {
		Sequence oldSequence = sequence;
		boolean oldSequenceESet = sequenceESet;
		sequence = null;
		sequenceESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, SchedulePackage.CARGO_ALLOCATION__SEQUENCE, oldSequence, null, oldSequenceESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetSequence() {
		return sequenceESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CargoType getCargoType() {
		return cargoType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setCargoType(CargoType newCargoType) {
		CargoType oldCargoType = cargoType;
		cargoType = newCargoType == null ? CARGO_TYPE_EDEFAULT : newCargoType;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.CARGO_ALLOCATION__CARGO_TYPE, oldCargoType, cargoType));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<SlotAllocation> getSlotAllocations() {
		if (slotAllocations == null) {
			slotAllocations = new EObjectWithInverseResolvingEList<SlotAllocation>(SlotAllocation.class, this, SchedulePackage.CARGO_ALLOCATION__SLOT_ALLOCATIONS, SchedulePackage.SLOT_ALLOCATION__CARGO_ALLOCATION);
		}
		return slotAllocations;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public String getName() {
//		if (isSetInputCargo()) {
//			return getInputCargo().getLoadName();
//		} else {
			for (SlotAllocation slotAllocation : getSlotAllocations()) {
				// TODO: First load allocation?
				return slotAllocation.getName();
			}
//		}
		return "";
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
			case SchedulePackage.CARGO_ALLOCATION__SLOT_ALLOCATIONS:
				return ((InternalEList<InternalEObject>)(InternalEList<?>)getSlotAllocations()).basicAdd(otherEnd, msgs);
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
			case SchedulePackage.CARGO_ALLOCATION__GROUP_PROFIT_AND_LOSS:
				return basicSetGroupProfitAndLoss(null, msgs);
			case SchedulePackage.CARGO_ALLOCATION__GENERAL_PNL_DETAILS:
				return ((InternalEList<?>)getGeneralPNLDetails()).basicRemove(otherEnd, msgs);
			case SchedulePackage.CARGO_ALLOCATION__SLOT_ALLOCATIONS:
				return ((InternalEList<?>)getSlotAllocations()).basicRemove(otherEnd, msgs);
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
			case SchedulePackage.CARGO_ALLOCATION__GROUP_PROFIT_AND_LOSS:
				return getGroupProfitAndLoss();
			case SchedulePackage.CARGO_ALLOCATION__GENERAL_PNL_DETAILS:
				return getGeneralPNLDetails();
			case SchedulePackage.CARGO_ALLOCATION__EVENTS:
				return getEvents();
			case SchedulePackage.CARGO_ALLOCATION__SLOT_ALLOCATIONS:
				return getSlotAllocations();
			case SchedulePackage.CARGO_ALLOCATION__SEQUENCE:
				if (resolve) return getSequence();
				return basicGetSequence();
			case SchedulePackage.CARGO_ALLOCATION__CARGO_TYPE:
				return getCargoType();
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
			case SchedulePackage.CARGO_ALLOCATION__GROUP_PROFIT_AND_LOSS:
				setGroupProfitAndLoss((GroupProfitAndLoss)newValue);
				return;
			case SchedulePackage.CARGO_ALLOCATION__GENERAL_PNL_DETAILS:
				getGeneralPNLDetails().clear();
				getGeneralPNLDetails().addAll((Collection<? extends GeneralPNLDetails>)newValue);
				return;
			case SchedulePackage.CARGO_ALLOCATION__EVENTS:
				getEvents().clear();
				getEvents().addAll((Collection<? extends Event>)newValue);
				return;
			case SchedulePackage.CARGO_ALLOCATION__SLOT_ALLOCATIONS:
				getSlotAllocations().clear();
				getSlotAllocations().addAll((Collection<? extends SlotAllocation>)newValue);
				return;
			case SchedulePackage.CARGO_ALLOCATION__SEQUENCE:
				setSequence((Sequence)newValue);
				return;
			case SchedulePackage.CARGO_ALLOCATION__CARGO_TYPE:
				setCargoType((CargoType)newValue);
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
			case SchedulePackage.CARGO_ALLOCATION__GROUP_PROFIT_AND_LOSS:
				setGroupProfitAndLoss((GroupProfitAndLoss)null);
				return;
			case SchedulePackage.CARGO_ALLOCATION__GENERAL_PNL_DETAILS:
				getGeneralPNLDetails().clear();
				return;
			case SchedulePackage.CARGO_ALLOCATION__EVENTS:
				getEvents().clear();
				return;
			case SchedulePackage.CARGO_ALLOCATION__SLOT_ALLOCATIONS:
				getSlotAllocations().clear();
				return;
			case SchedulePackage.CARGO_ALLOCATION__SEQUENCE:
				unsetSequence();
				return;
			case SchedulePackage.CARGO_ALLOCATION__CARGO_TYPE:
				setCargoType(CARGO_TYPE_EDEFAULT);
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
			case SchedulePackage.CARGO_ALLOCATION__GROUP_PROFIT_AND_LOSS:
				return groupProfitAndLoss != null;
			case SchedulePackage.CARGO_ALLOCATION__GENERAL_PNL_DETAILS:
				return generalPNLDetails != null && !generalPNLDetails.isEmpty();
			case SchedulePackage.CARGO_ALLOCATION__EVENTS:
				return events != null && !events.isEmpty();
			case SchedulePackage.CARGO_ALLOCATION__SLOT_ALLOCATIONS:
				return slotAllocations != null && !slotAllocations.isEmpty();
			case SchedulePackage.CARGO_ALLOCATION__SEQUENCE:
				return isSetSequence();
			case SchedulePackage.CARGO_ALLOCATION__CARGO_TYPE:
				return cargoType != CARGO_TYPE_EDEFAULT;
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
		if (baseClass == ProfitAndLossContainer.class) {
			switch (derivedFeatureID) {
				case SchedulePackage.CARGO_ALLOCATION__GROUP_PROFIT_AND_LOSS: return SchedulePackage.PROFIT_AND_LOSS_CONTAINER__GROUP_PROFIT_AND_LOSS;
				case SchedulePackage.CARGO_ALLOCATION__GENERAL_PNL_DETAILS: return SchedulePackage.PROFIT_AND_LOSS_CONTAINER__GENERAL_PNL_DETAILS;
				default: return -1;
			}
		}
		if (baseClass == EventGrouping.class) {
			switch (derivedFeatureID) {
				case SchedulePackage.CARGO_ALLOCATION__EVENTS: return SchedulePackage.EVENT_GROUPING__EVENTS;
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
		if (baseClass == ProfitAndLossContainer.class) {
			switch (baseFeatureID) {
				case SchedulePackage.PROFIT_AND_LOSS_CONTAINER__GROUP_PROFIT_AND_LOSS: return SchedulePackage.CARGO_ALLOCATION__GROUP_PROFIT_AND_LOSS;
				case SchedulePackage.PROFIT_AND_LOSS_CONTAINER__GENERAL_PNL_DETAILS: return SchedulePackage.CARGO_ALLOCATION__GENERAL_PNL_DETAILS;
				default: return -1;
			}
		}
		if (baseClass == EventGrouping.class) {
			switch (baseFeatureID) {
				case SchedulePackage.EVENT_GROUPING__EVENTS: return SchedulePackage.CARGO_ALLOCATION__EVENTS;
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
	@SuppressWarnings({"rawtypes", "unchecked" })
	public Object eInvoke(int operationID, EList<?> arguments) throws InvocationTargetException {
		switch (operationID) {
			case SchedulePackage.CARGO_ALLOCATION___GET_NAME:
				return getName();
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
		result.append(" (cargoType: ");
		result.append(cargoType);
		result.append(')');
		return result.toString();
	}

} // end of CargoAllocationImpl

// finish type fixing
