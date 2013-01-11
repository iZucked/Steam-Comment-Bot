/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.schedule.impl;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import com.mmxlabs.models.lng.schedule.EndEvent;
import com.mmxlabs.models.lng.schedule.FuelQuantity;
import com.mmxlabs.models.lng.schedule.FuelUsage;
import com.mmxlabs.models.lng.schedule.PortVisit;
import com.mmxlabs.models.lng.schedule.SchedulePackage;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.types.ExtraData;
import com.mmxlabs.models.lng.types.ExtraDataContainer;
import com.mmxlabs.models.lng.types.ExtraDataFormatType;
import com.mmxlabs.models.lng.types.TypesFactory;
import com.mmxlabs.models.lng.types.TypesPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>End Event</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.EndEventImpl#getFuels <em>Fuels</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.EndEventImpl#getPortCost <em>Port Cost</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.EndEventImpl#getExtraData <em>Extra Data</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.EndEventImpl#getSlotAllocation <em>Slot Allocation</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class EndEventImpl extends EventImpl implements EndEvent {
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
	 * The cached value of the '{@link #getExtraData() <em>Extra Data</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getExtraData()
	 * @generated
	 * @ordered
	 */
	protected EList<ExtraData> extraData;

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
	protected EndEventImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return SchedulePackage.Literals.END_EVENT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<FuelQuantity> getFuels() {
		if (fuels == null) {
			fuels = new EObjectContainmentEList<FuelQuantity>(FuelQuantity.class, this, SchedulePackage.END_EVENT__FUELS);
		}
		return fuels;
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
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.END_EVENT__PORT_COST, oldPortCost, portCost));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<ExtraData> getExtraData() {
		if (extraData == null) {
			extraData = new EObjectContainmentEList<ExtraData>(ExtraData.class, this, SchedulePackage.END_EVENT__EXTRA_DATA);
		}
		return extraData;
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
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, SchedulePackage.END_EVENT__SLOT_ALLOCATION, oldSlotAllocation, slotAllocation));
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
	public void setSlotAllocation(SlotAllocation newSlotAllocation) {
		SlotAllocation oldSlotAllocation = slotAllocation;
		slotAllocation = newSlotAllocation;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.END_EVENT__SLOT_ALLOCATION, oldSlotAllocation, slotAllocation));
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
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case SchedulePackage.END_EVENT__FUELS:
				return ((InternalEList<?>)getFuels()).basicRemove(otherEnd, msgs);
			case SchedulePackage.END_EVENT__EXTRA_DATA:
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
			case SchedulePackage.END_EVENT__FUELS:
				return getFuels();
			case SchedulePackage.END_EVENT__PORT_COST:
				return getPortCost();
			case SchedulePackage.END_EVENT__EXTRA_DATA:
				return getExtraData();
			case SchedulePackage.END_EVENT__SLOT_ALLOCATION:
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
			case SchedulePackage.END_EVENT__FUELS:
				getFuels().clear();
				getFuels().addAll((Collection<? extends FuelQuantity>)newValue);
				return;
			case SchedulePackage.END_EVENT__PORT_COST:
				setPortCost((Integer)newValue);
				return;
			case SchedulePackage.END_EVENT__EXTRA_DATA:
				getExtraData().clear();
				getExtraData().addAll((Collection<? extends ExtraData>)newValue);
				return;
			case SchedulePackage.END_EVENT__SLOT_ALLOCATION:
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
			case SchedulePackage.END_EVENT__FUELS:
				getFuels().clear();
				return;
			case SchedulePackage.END_EVENT__PORT_COST:
				setPortCost(PORT_COST_EDEFAULT);
				return;
			case SchedulePackage.END_EVENT__EXTRA_DATA:
				getExtraData().clear();
				return;
			case SchedulePackage.END_EVENT__SLOT_ALLOCATION:
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
			case SchedulePackage.END_EVENT__FUELS:
				return fuels != null && !fuels.isEmpty();
			case SchedulePackage.END_EVENT__PORT_COST:
				return portCost != PORT_COST_EDEFAULT;
			case SchedulePackage.END_EVENT__EXTRA_DATA:
				return extraData != null && !extraData.isEmpty();
			case SchedulePackage.END_EVENT__SLOT_ALLOCATION:
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
				case SchedulePackage.END_EVENT__FUELS: return SchedulePackage.FUEL_USAGE__FUELS;
				default: return -1;
			}
		}
		if (baseClass == PortVisit.class) {
			switch (derivedFeatureID) {
				case SchedulePackage.END_EVENT__PORT_COST: return SchedulePackage.PORT_VISIT__PORT_COST;
				default: return -1;
			}
		}
		if (baseClass == ExtraDataContainer.class) {
			switch (derivedFeatureID) {
				case SchedulePackage.END_EVENT__EXTRA_DATA: return TypesPackage.EXTRA_DATA_CONTAINER__EXTRA_DATA;
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
				case SchedulePackage.FUEL_USAGE__FUELS: return SchedulePackage.END_EVENT__FUELS;
				default: return -1;
			}
		}
		if (baseClass == PortVisit.class) {
			switch (baseFeatureID) {
				case SchedulePackage.PORT_VISIT__PORT_COST: return SchedulePackage.END_EVENT__PORT_COST;
				default: return -1;
			}
		}
		if (baseClass == ExtraDataContainer.class) {
			switch (baseFeatureID) {
				case TypesPackage.EXTRA_DATA_CONTAINER__EXTRA_DATA: return SchedulePackage.END_EVENT__EXTRA_DATA;
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
				case SchedulePackage.FUEL_USAGE___GET_FUEL_COST: return SchedulePackage.END_EVENT___GET_FUEL_COST;
				default: return -1;
			}
		}
		if (baseClass == PortVisit.class) {
			switch (baseOperationID) {
				default: return -1;
			}
		}
		if (baseClass == ExtraDataContainer.class) {
			switch (baseOperationID) {
				case TypesPackage.EXTRA_DATA_CONTAINER___GET_DATA_WITH_PATH__ITERABLE: return SchedulePackage.END_EVENT___GET_DATA_WITH_PATH__ITERABLE;
				case TypesPackage.EXTRA_DATA_CONTAINER___GET_DATA_WITH_KEY__STRING: return SchedulePackage.END_EVENT___GET_DATA_WITH_KEY__STRING;
				case TypesPackage.EXTRA_DATA_CONTAINER___ADD_EXTRA_DATA__STRING_STRING: return SchedulePackage.END_EVENT___ADD_EXTRA_DATA__STRING_STRING;
				case TypesPackage.EXTRA_DATA_CONTAINER___ADD_EXTRA_DATA__STRING_STRING_SERIALIZABLE_EXTRADATAFORMATTYPE: return SchedulePackage.END_EVENT___ADD_EXTRA_DATA__STRING_STRING_SERIALIZABLE_EXTRADATAFORMATTYPE;
				case TypesPackage.EXTRA_DATA_CONTAINER___GET_VALUE_WITH_PATH_AS__ITERABLE_CLASS_OBJECT: return SchedulePackage.END_EVENT___GET_VALUE_WITH_PATH_AS__ITERABLE_CLASS_OBJECT;
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
	@SuppressWarnings("unchecked")
	public Object eInvoke(int operationID, EList<?> arguments) throws InvocationTargetException {
		switch (operationID) {
			case SchedulePackage.END_EVENT___GET_DATA_WITH_PATH__ITERABLE:
				return getDataWithPath((Iterable<String>)arguments.get(0));
			case SchedulePackage.END_EVENT___GET_DATA_WITH_KEY__STRING:
				return getDataWithKey((String)arguments.get(0));
			case SchedulePackage.END_EVENT___ADD_EXTRA_DATA__STRING_STRING:
				return addExtraData((String)arguments.get(0), (String)arguments.get(1));
			case SchedulePackage.END_EVENT___ADD_EXTRA_DATA__STRING_STRING_SERIALIZABLE_EXTRADATAFORMATTYPE:
				return addExtraData((String)arguments.get(0), (String)arguments.get(1), (Serializable)arguments.get(2), (ExtraDataFormatType)arguments.get(3));
			case SchedulePackage.END_EVENT___GET_VALUE_WITH_PATH_AS__ITERABLE_CLASS_OBJECT:
				return getValueWithPathAs((Iterable<String>)arguments.get(0), (Class)arguments.get(1), arguments.get(2));
			case SchedulePackage.END_EVENT___GET_FUEL_COST:
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

} // end of EndEventImpl

// finish type fixing
