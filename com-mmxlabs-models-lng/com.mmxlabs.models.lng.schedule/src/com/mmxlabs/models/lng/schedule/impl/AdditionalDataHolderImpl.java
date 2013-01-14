/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.schedule.impl;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import com.mmxlabs.models.lng.schedule.AdditionalData;
import com.mmxlabs.models.lng.schedule.AdditionalDataHolder;
import com.mmxlabs.models.lng.schedule.SchedulePackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Additional Data Holder</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.AdditionalDataHolderImpl#getAdditionalData <em>Additional Data</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class AdditionalDataHolderImpl extends EObjectImpl implements AdditionalDataHolder {
	/**
	 * The cached value of the '{@link #getAdditionalData() <em>Additional Data</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAdditionalData()
	 * @generated
	 * @ordered
	 */
	protected EList<AdditionalData> additionalData;
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected AdditionalDataHolderImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return SchedulePackage.Literals.ADDITIONAL_DATA_HOLDER;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<AdditionalData> getAdditionalData() {
		if (additionalData == null) {
			additionalData = new EObjectContainmentEList<AdditionalData>(AdditionalData.class, this, SchedulePackage.ADDITIONAL_DATA_HOLDER__ADDITIONAL_DATA);
		}
		return additionalData;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AdditionalData getAdditionalDataWithKey(String key) {
		for (final AdditionalData ad : getAdditionalData()) {
		  if (ad.getKey().equals(key)) return ad;
		}
		
		return null;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AdditionalData getAdditionalDataWithPath(Iterable<String> keys) {
		AdditionalDataHolder adh = this;
		for (final String key : keys) {
			if (adh == null) return null;
			adh = adh.getAdditionalDataWithKey(key);
		}
		if (adh instanceof AdditionalData) return (AdditionalData) adh;
		return null;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case SchedulePackage.ADDITIONAL_DATA_HOLDER__ADDITIONAL_DATA:
				return ((InternalEList<?>)getAdditionalData()).basicRemove(otherEnd, msgs);
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
			case SchedulePackage.ADDITIONAL_DATA_HOLDER__ADDITIONAL_DATA:
				return getAdditionalData();
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
			case SchedulePackage.ADDITIONAL_DATA_HOLDER__ADDITIONAL_DATA:
				getAdditionalData().clear();
				getAdditionalData().addAll((Collection<? extends AdditionalData>)newValue);
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
			case SchedulePackage.ADDITIONAL_DATA_HOLDER__ADDITIONAL_DATA:
				getAdditionalData().clear();
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
			case SchedulePackage.ADDITIONAL_DATA_HOLDER__ADDITIONAL_DATA:
				return additionalData != null && !additionalData.isEmpty();
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
			case SchedulePackage.ADDITIONAL_DATA_HOLDER___GET_ADDITIONAL_DATA_WITH_KEY__STRING:
				return getAdditionalDataWithKey((String)arguments.get(0));
			case SchedulePackage.ADDITIONAL_DATA_HOLDER___GET_ADDITIONAL_DATA_WITH_PATH__ITERABLE:
				return getAdditionalDataWithPath((Iterable<String>)arguments.get(0));
		}
		return super.eInvoke(operationID, arguments);
	}

} // end of AdditionalDataHolderImpl

// finish type fixing
