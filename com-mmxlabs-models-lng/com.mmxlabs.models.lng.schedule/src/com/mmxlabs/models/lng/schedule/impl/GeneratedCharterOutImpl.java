/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.schedule.impl;
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
import org.eclipse.emf.ecore.util.InternalEList;

import com.mmxlabs.models.lng.schedule.GeneratedCharterOut;
import com.mmxlabs.models.lng.schedule.SchedulePackage;
import com.mmxlabs.models.lng.types.ExtraData;
import com.mmxlabs.models.lng.types.ExtraDataContainer;
import com.mmxlabs.models.lng.types.ExtraDataFormatType;
import com.mmxlabs.models.lng.types.TypesFactory;
import com.mmxlabs.models.lng.types.TypesPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Generated Charter Out</b></em>'.
 * @since 2.0
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.GeneratedCharterOutImpl#getExtraData <em>Extra Data</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.GeneratedCharterOutImpl#getRevenue <em>Revenue</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class GeneratedCharterOutImpl extends EventImpl implements GeneratedCharterOut {
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
	 * The default value of the '{@link #getRevenue() <em>Revenue</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRevenue()
	 * @generated
	 * @ordered
	 */
	protected static final int REVENUE_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getRevenue() <em>Revenue</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRevenue()
	 * @generated
	 * @ordered
	 */
	protected int revenue = REVENUE_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected GeneratedCharterOutImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return SchedulePackage.Literals.GENERATED_CHARTER_OUT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<ExtraData> getExtraData() {
		if (extraData == null) {
			extraData = new EObjectContainmentEList<ExtraData>(ExtraData.class, this, SchedulePackage.GENERATED_CHARTER_OUT__EXTRA_DATA);
		}
		return extraData;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getRevenue() {
		return revenue;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setRevenue(int newRevenue) {
		int oldRevenue = revenue;
		revenue = newRevenue;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.GENERATED_CHARTER_OUT__REVENUE, oldRevenue, revenue));
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
			case SchedulePackage.GENERATED_CHARTER_OUT__EXTRA_DATA:
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
			case SchedulePackage.GENERATED_CHARTER_OUT__EXTRA_DATA:
				return getExtraData();
			case SchedulePackage.GENERATED_CHARTER_OUT__REVENUE:
				return getRevenue();
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
			case SchedulePackage.GENERATED_CHARTER_OUT__EXTRA_DATA:
				getExtraData().clear();
				getExtraData().addAll((Collection<? extends ExtraData>)newValue);
				return;
			case SchedulePackage.GENERATED_CHARTER_OUT__REVENUE:
				setRevenue((Integer)newValue);
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
			case SchedulePackage.GENERATED_CHARTER_OUT__EXTRA_DATA:
				getExtraData().clear();
				return;
			case SchedulePackage.GENERATED_CHARTER_OUT__REVENUE:
				setRevenue(REVENUE_EDEFAULT);
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
			case SchedulePackage.GENERATED_CHARTER_OUT__EXTRA_DATA:
				return extraData != null && !extraData.isEmpty();
			case SchedulePackage.GENERATED_CHARTER_OUT__REVENUE:
				return revenue != REVENUE_EDEFAULT;
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
				case SchedulePackage.GENERATED_CHARTER_OUT__EXTRA_DATA: return TypesPackage.EXTRA_DATA_CONTAINER__EXTRA_DATA;
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
				case TypesPackage.EXTRA_DATA_CONTAINER__EXTRA_DATA: return SchedulePackage.GENERATED_CHARTER_OUT__EXTRA_DATA;
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
				case TypesPackage.EXTRA_DATA_CONTAINER___GET_DATA_WITH_PATH__ITERABLE: return SchedulePackage.GENERATED_CHARTER_OUT___GET_DATA_WITH_PATH__ITERABLE;
				case TypesPackage.EXTRA_DATA_CONTAINER___GET_DATA_WITH_KEY__STRING: return SchedulePackage.GENERATED_CHARTER_OUT___GET_DATA_WITH_KEY__STRING;
				case TypesPackage.EXTRA_DATA_CONTAINER___ADD_EXTRA_DATA__STRING_STRING: return SchedulePackage.GENERATED_CHARTER_OUT___ADD_EXTRA_DATA__STRING_STRING;
				case TypesPackage.EXTRA_DATA_CONTAINER___ADD_EXTRA_DATA__STRING_STRING_SERIALIZABLE_EXTRADATAFORMATTYPE: return SchedulePackage.GENERATED_CHARTER_OUT___ADD_EXTRA_DATA__STRING_STRING_SERIALIZABLE_EXTRADATAFORMATTYPE;
				case TypesPackage.EXTRA_DATA_CONTAINER___GET_VALUE_WITH_PATH_AS__ITERABLE_CLASS_OBJECT: return SchedulePackage.GENERATED_CHARTER_OUT___GET_VALUE_WITH_PATH_AS__ITERABLE_CLASS_OBJECT;
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
			case SchedulePackage.GENERATED_CHARTER_OUT___GET_DATA_WITH_PATH__ITERABLE:
				return getDataWithPath((Iterable<String>)arguments.get(0));
			case SchedulePackage.GENERATED_CHARTER_OUT___GET_DATA_WITH_KEY__STRING:
				return getDataWithKey((String)arguments.get(0));
			case SchedulePackage.GENERATED_CHARTER_OUT___ADD_EXTRA_DATA__STRING_STRING:
				return addExtraData((String)arguments.get(0), (String)arguments.get(1));
			case SchedulePackage.GENERATED_CHARTER_OUT___ADD_EXTRA_DATA__STRING_STRING_SERIALIZABLE_EXTRADATAFORMATTYPE:
				return addExtraData((String)arguments.get(0), (String)arguments.get(1), (Serializable)arguments.get(2), (ExtraDataFormatType)arguments.get(3));
			case SchedulePackage.GENERATED_CHARTER_OUT___GET_VALUE_WITH_PATH_AS__ITERABLE_CLASS_OBJECT:
				return getValueWithPathAs((Iterable<String>)arguments.get(0), (Class)arguments.get(1), arguments.get(2));
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
		result.append(" (revenue: ");
		result.append(revenue);
		result.append(')');
		return result.toString();
	}

	/**
	 * @generated NOT
	 */
	@Override
	public String name() {
		return "Charter Out";
	}
	
} // end of GeneratedCharterOutImpl

// finish type fixing
