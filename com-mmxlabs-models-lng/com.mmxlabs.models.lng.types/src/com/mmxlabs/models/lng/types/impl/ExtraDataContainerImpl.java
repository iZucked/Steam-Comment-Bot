/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.models.lng.types.impl;

import java.io.Serializable;
import java.lang.Iterable;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import com.mmxlabs.models.lng.types.ExtraData;
import com.mmxlabs.models.lng.types.ExtraDataContainer;
import com.mmxlabs.models.lng.types.ExtraDataFormatType;
import com.mmxlabs.models.lng.types.TypesFactory;
import com.mmxlabs.models.lng.types.TypesPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Extra Data Container</b></em>'.
 * @since 2.0
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.types.impl.ExtraDataContainerImpl#getExtraData <em>Extra Data</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ExtraDataContainerImpl extends EObjectImpl implements ExtraDataContainer {
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
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ExtraDataContainerImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return TypesPackage.Literals.EXTRA_DATA_CONTAINER;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<ExtraData> getExtraData() {
		if (extraData == null) {
			extraData = new EObjectContainmentEList<ExtraData>(ExtraData.class, this, TypesPackage.EXTRA_DATA_CONTAINER__EXTRA_DATA);
		}
		return extraData;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ExtraData getDataWithPath(Iterable<String> keys) {
		java.util.Iterator<String> iterator = keys.iterator();
		if (iterator.hasNext() == false)
			return null;
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
			if (e.getKey().equals(key))
				return e;
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
		if (ed == null)
			return defaultValue;
		final T value = ed.getValueAs(clazz);
		if (value == null)
			return defaultValue;
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
		case TypesPackage.EXTRA_DATA_CONTAINER__EXTRA_DATA:
			return ((InternalEList<?>) getExtraData()).basicRemove(otherEnd, msgs);
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
		case TypesPackage.EXTRA_DATA_CONTAINER__EXTRA_DATA:
			return getExtraData();
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
		case TypesPackage.EXTRA_DATA_CONTAINER__EXTRA_DATA:
			getExtraData().clear();
			getExtraData().addAll((Collection<? extends ExtraData>) newValue);
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
		case TypesPackage.EXTRA_DATA_CONTAINER__EXTRA_DATA:
			getExtraData().clear();
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
		case TypesPackage.EXTRA_DATA_CONTAINER__EXTRA_DATA:
			return extraData != null && !extraData.isEmpty();
		}
		return super.eIsSet(featureID);
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
		case TypesPackage.EXTRA_DATA_CONTAINER___GET_DATA_WITH_PATH__ITERABLE:
			return getDataWithPath((Iterable<String>) arguments.get(0));
		case TypesPackage.EXTRA_DATA_CONTAINER___GET_DATA_WITH_KEY__STRING:
			return getDataWithKey((String) arguments.get(0));
		case TypesPackage.EXTRA_DATA_CONTAINER___ADD_EXTRA_DATA__STRING_STRING:
			return addExtraData((String) arguments.get(0), (String) arguments.get(1));
		case TypesPackage.EXTRA_DATA_CONTAINER___ADD_EXTRA_DATA__STRING_STRING_SERIALIZABLE_EXTRADATAFORMATTYPE:
			return addExtraData((String) arguments.get(0), (String) arguments.get(1), (Serializable) arguments.get(2), (ExtraDataFormatType) arguments.get(3));
		case TypesPackage.EXTRA_DATA_CONTAINER___GET_VALUE_WITH_PATH_AS__ITERABLE_CLASS_OBJECT:
			return getValueWithPathAs((Iterable<String>) arguments.get(0), (Class) arguments.get(1), arguments.get(2));
		}
		return super.eInvoke(operationID, arguments);
	}

} // end of ExtraDataContainerImpl

// finish type fixing
