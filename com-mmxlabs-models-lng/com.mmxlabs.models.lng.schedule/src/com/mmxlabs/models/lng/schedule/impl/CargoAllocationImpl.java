/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.schedule.impl;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.SchedulePackage;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.types.ExtraData;
import com.mmxlabs.models.lng.types.ExtraDataContainer;
import com.mmxlabs.models.lng.types.ExtraDataFormatType;
import com.mmxlabs.models.lng.types.TypesPackage;
import com.mmxlabs.models.mmxcore.impl.MMXObjectImpl;
import java.io.Serializable;
import java.lang.Iterable;
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
import org.eclipse.emf.ecore.util.InternalEList;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.SchedulePackage;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.types.ExtraData;
import com.mmxlabs.models.lng.types.ExtraDataContainer;
import com.mmxlabs.models.lng.types.ExtraDataFormatType;
import com.mmxlabs.models.lng.types.TypesPackage;
import com.mmxlabs.models.mmxcore.impl.MMXObjectImpl;
import java.io.Serializable;
import java.lang.Iterable;
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
import org.eclipse.emf.ecore.util.InternalEList;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.SchedulePackage;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.types.ExtraData;
import com.mmxlabs.models.lng.types.ExtraDataContainer;
import com.mmxlabs.models.lng.types.ExtraDataFormatType;
import com.mmxlabs.models.lng.types.TypesPackage;
import com.mmxlabs.models.mmxcore.impl.MMXObjectImpl;
import java.io.Serializable;
import java.lang.Iterable;
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
import org.eclipse.emf.ecore.util.InternalEList;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.SchedulePackage;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.types.ExtraData;
import com.mmxlabs.models.lng.types.ExtraDataContainer;
import com.mmxlabs.models.lng.types.ExtraDataFormatType;
import com.mmxlabs.models.lng.types.TypesPackage;
import com.mmxlabs.models.mmxcore.impl.MMXObjectImpl;
import java.io.Serializable;
import java.lang.Iterable;
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
import org.eclipse.emf.ecore.util.InternalEList;
import java.io.Serializable;
import java.lang.Iterable;
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
import org.eclipse.emf.ecore.util.InternalEList;

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.SchedulePackage;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.types.ExtraData;
import com.mmxlabs.models.lng.types.ExtraDataContainer;
import com.mmxlabs.models.lng.types.ExtraDataFormatType;
import com.mmxlabs.models.lng.types.TypesFactory;
import com.mmxlabs.models.lng.types.TypesPackage;
import com.mmxlabs.models.mmxcore.impl.MMXObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Cargo Allocation</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.CargoAllocationImpl#getExtraData <em>Extra Data</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.CargoAllocationImpl#getSlotAllocations <em>Slot Allocations</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.CargoAllocationImpl#getInputCargo <em>Input Cargo</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.CargoAllocationImpl#getEvents <em>Events</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.CargoAllocationImpl#getSequence <em>Sequence</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class CargoAllocationImpl extends MMXObjectImpl implements CargoAllocation {
	/**
	 * The cached value of the '{@link #getExtraData() <em>Extra Data</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getExtraData()
	 * @generated
	 * @ordered
	 */
	protected EList<ExtraData> extraData;

	/**
	 * The cached value of the '{@link #getSlotAllocations() <em>Slot Allocations</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * @since 3.0
	 * <!-- end-user-doc -->
	 * @see #getSlotAllocations()
	 * @generated
	 * @ordered
	 */
	protected EList<SlotAllocation> slotAllocations;

	/**
	 * The cached value of the '{@link #getInputCargo() <em>Input Cargo</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getInputCargo()
	 * @generated
	 * @ordered
	 */
	protected Cargo inputCargo;

	/**
	 * This is true if the Input Cargo reference has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean inputCargoESet;

	/**
	 * The cached value of the '{@link #getEvents() <em>Events</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * @since 3.0
	 * <!-- end-user-doc -->
	 * @see #getEvents()
	 * @generated
	 * @ordered
	 */
	protected EList<Event> events;

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
	public Cargo getInputCargo() {
		if (inputCargo != null && inputCargo.eIsProxy()) {
			InternalEObject oldInputCargo = (InternalEObject)inputCargo;
			inputCargo = (Cargo)eResolveProxy(oldInputCargo);
			if (inputCargo != oldInputCargo) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, SchedulePackage.CARGO_ALLOCATION__INPUT_CARGO, oldInputCargo, inputCargo));
			}
		}
		return inputCargo;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Cargo basicGetInputCargo() {
		return inputCargo;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setInputCargo(Cargo newInputCargo) {
		Cargo oldInputCargo = inputCargo;
		inputCargo = newInputCargo;
		boolean oldInputCargoESet = inputCargoESet;
		inputCargoESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.CARGO_ALLOCATION__INPUT_CARGO, oldInputCargo, inputCargo, !oldInputCargoESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetInputCargo() {
		Cargo oldInputCargo = inputCargo;
		boolean oldInputCargoESet = inputCargoESet;
		inputCargo = null;
		inputCargoESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, SchedulePackage.CARGO_ALLOCATION__INPUT_CARGO, oldInputCargo, null, oldInputCargoESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetInputCargo() {
		return inputCargoESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 3.0
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
	public EList<ExtraData> getExtraData() {
		if (extraData == null) {
			extraData = new EObjectContainmentEList<ExtraData>(ExtraData.class, this, SchedulePackage.CARGO_ALLOCATION__EXTRA_DATA);
		}
		return extraData;
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 3.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<SlotAllocation> getSlotAllocations() {
		if (slotAllocations == null) {
			slotAllocations = new EObjectResolvingEList<SlotAllocation>(SlotAllocation.class, this, SchedulePackage.CARGO_ALLOCATION__SLOT_ALLOCATIONS);
		}
		return slotAllocations;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public String getName() {
		if (isSetInputCargo()) {
			return getInputCargo().getName();
		} else {
			for (SlotAllocation slotAllocation : getSlotAllocations()) {
				// TODO: First load allocation?
				return slotAllocation.getName();
			}
		}
		return "";
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ExtraData getDataWithPath(Iterable<String> keys) {
		java.util.Iterator<String> iterator = keys.iterator();
				if (iterator.hasNext() == false) return null;
				ExtraData edc = getDataWithKey(iterator.next());
				while (edc != null && iterator.hasNext()) {
					edc = edc.getDataWithKey(iterator.next());
				}
				return edc;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ExtraData getDataWithKey(String key) {
		for (final ExtraData e : getExtraData()) {
			if (e.getKey().equals(key)) return e;
		}
		return null;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ExtraData addExtraData(String key, String name) {
		final ExtraData result = TypesFactory.eINSTANCE.createExtraData();
		result.setKey(key);
		result.setName(name);
		getExtraData().add(result);
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ExtraData addExtraData(String key, String name, Serializable value, ExtraDataFormatType format) {
		final ExtraData result = addExtraData(key, name);
		result.setValue(value);
		result.setFormatType(format);
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public <T> T getValueWithPathAs(Iterable<String> path, Class<T> clazz, T defaultValue) {
		final ExtraData ed = getDataWithPath(path);
		if (ed == null) return defaultValue;
		final T value = ed.getValueAs(clazz);
		if (value == null) return defaultValue;
		return value;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case SchedulePackage.CARGO_ALLOCATION__EXTRA_DATA:
				return ((InternalEList<?>)getExtraData()).basicRemove(otherEnd, msgs);
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
			case SchedulePackage.CARGO_ALLOCATION__EXTRA_DATA:
				return getExtraData();
			case SchedulePackage.CARGO_ALLOCATION__SLOT_ALLOCATIONS:
				return getSlotAllocations();
			case SchedulePackage.CARGO_ALLOCATION__INPUT_CARGO:
				if (resolve) return getInputCargo();
				return basicGetInputCargo();
			case SchedulePackage.CARGO_ALLOCATION__EVENTS:
				return getEvents();
			case SchedulePackage.CARGO_ALLOCATION__SEQUENCE:
				if (resolve) return getSequence();
				return basicGetSequence();
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
			case SchedulePackage.CARGO_ALLOCATION__EXTRA_DATA:
				getExtraData().clear();
				getExtraData().addAll((Collection<? extends ExtraData>)newValue);
				return;
			case SchedulePackage.CARGO_ALLOCATION__SLOT_ALLOCATIONS:
				getSlotAllocations().clear();
				getSlotAllocations().addAll((Collection<? extends SlotAllocation>)newValue);
				return;
			case SchedulePackage.CARGO_ALLOCATION__INPUT_CARGO:
				setInputCargo((Cargo)newValue);
				return;
			case SchedulePackage.CARGO_ALLOCATION__EVENTS:
				getEvents().clear();
				getEvents().addAll((Collection<? extends Event>)newValue);
				return;
			case SchedulePackage.CARGO_ALLOCATION__SEQUENCE:
				setSequence((Sequence)newValue);
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
			case SchedulePackage.CARGO_ALLOCATION__EXTRA_DATA:
				getExtraData().clear();
				return;
			case SchedulePackage.CARGO_ALLOCATION__SLOT_ALLOCATIONS:
				getSlotAllocations().clear();
				return;
			case SchedulePackage.CARGO_ALLOCATION__INPUT_CARGO:
				unsetInputCargo();
				return;
			case SchedulePackage.CARGO_ALLOCATION__EVENTS:
				getEvents().clear();
				return;
			case SchedulePackage.CARGO_ALLOCATION__SEQUENCE:
				unsetSequence();
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
			case SchedulePackage.CARGO_ALLOCATION__EXTRA_DATA:
				return extraData != null && !extraData.isEmpty();
			case SchedulePackage.CARGO_ALLOCATION__SLOT_ALLOCATIONS:
				return slotAllocations != null && !slotAllocations.isEmpty();
			case SchedulePackage.CARGO_ALLOCATION__INPUT_CARGO:
				return isSetInputCargo();
			case SchedulePackage.CARGO_ALLOCATION__EVENTS:
				return events != null && !events.isEmpty();
			case SchedulePackage.CARGO_ALLOCATION__SEQUENCE:
				return isSetSequence();
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
		if (baseClass == ExtraDataContainer.class) {
			switch (derivedFeatureID) {
				case SchedulePackage.CARGO_ALLOCATION__EXTRA_DATA: return TypesPackage.EXTRA_DATA_CONTAINER__EXTRA_DATA;
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
		if (baseClass == ExtraDataContainer.class) {
			switch (baseFeatureID) {
				case TypesPackage.EXTRA_DATA_CONTAINER__EXTRA_DATA: return SchedulePackage.CARGO_ALLOCATION__EXTRA_DATA;
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
		if (baseClass == ExtraDataContainer.class) {
			switch (baseOperationID) {
				case TypesPackage.EXTRA_DATA_CONTAINER___GET_DATA_WITH_PATH__ITERABLE: return SchedulePackage.CARGO_ALLOCATION___GET_DATA_WITH_PATH__ITERABLE;
				case TypesPackage.EXTRA_DATA_CONTAINER___GET_DATA_WITH_KEY__STRING: return SchedulePackage.CARGO_ALLOCATION___GET_DATA_WITH_KEY__STRING;
				case TypesPackage.EXTRA_DATA_CONTAINER___ADD_EXTRA_DATA__STRING_STRING: return SchedulePackage.CARGO_ALLOCATION___ADD_EXTRA_DATA__STRING_STRING;
				case TypesPackage.EXTRA_DATA_CONTAINER___ADD_EXTRA_DATA__STRING_STRING_SERIALIZABLE_EXTRADATAFORMATTYPE: return SchedulePackage.CARGO_ALLOCATION___ADD_EXTRA_DATA__STRING_STRING_SERIALIZABLE_EXTRADATAFORMATTYPE;
				case TypesPackage.EXTRA_DATA_CONTAINER___GET_VALUE_WITH_PATH_AS__ITERABLE_CLASS_OBJECT: return SchedulePackage.CARGO_ALLOCATION___GET_VALUE_WITH_PATH_AS__ITERABLE_CLASS_OBJECT;
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
	@SuppressWarnings({"rawtypes", "unchecked" })
	public Object eInvoke(int operationID, EList<?> arguments) throws InvocationTargetException {
		switch (operationID) {
			case SchedulePackage.CARGO_ALLOCATION___GET_NAME:
				return getName();
			case SchedulePackage.CARGO_ALLOCATION___GET_DATA_WITH_PATH__ITERABLE:
				return getDataWithPath((Iterable<String>)arguments.get(0));
			case SchedulePackage.CARGO_ALLOCATION___GET_DATA_WITH_KEY__STRING:
				return getDataWithKey((String)arguments.get(0));
			case SchedulePackage.CARGO_ALLOCATION___ADD_EXTRA_DATA__STRING_STRING:
				return addExtraData((String)arguments.get(0), (String)arguments.get(1));
			case SchedulePackage.CARGO_ALLOCATION___ADD_EXTRA_DATA__STRING_STRING_SERIALIZABLE_EXTRADATAFORMATTYPE:
				return addExtraData((String)arguments.get(0), (String)arguments.get(1), (Serializable)arguments.get(2), (ExtraDataFormatType)arguments.get(3));
			case SchedulePackage.CARGO_ALLOCATION___GET_VALUE_WITH_PATH_AS__ITERABLE_CLASS_OBJECT:
				return getValueWithPathAs((Iterable<String>)arguments.get(0), (Class)arguments.get(1), arguments.get(2));
		}
		return super.eInvoke(operationID, arguments);
	}

} // end of CargoAllocationImpl

// finish type fixing
