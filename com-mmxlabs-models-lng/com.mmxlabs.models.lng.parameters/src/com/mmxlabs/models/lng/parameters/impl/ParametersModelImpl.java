/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.parameters.impl;
import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import com.mmxlabs.models.lng.parameters.OptimiserSettings;
import com.mmxlabs.models.lng.parameters.ParametersModel;
import com.mmxlabs.models.lng.parameters.ParametersPackage;
import com.mmxlabs.models.mmxcore.impl.UUIDObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Parameters Model</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.parameters.impl.ParametersModelImpl#getSettings <em>Settings</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.parameters.impl.ParametersModelImpl#getActiveSetting <em>Active Setting</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ParametersModelImpl extends UUIDObjectImpl implements ParametersModel {
	/**
	 * The cached value of the '{@link #getSettings() <em>Settings</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSettings()
	 * @generated
	 * @ordered
	 */
	protected EList<OptimiserSettings> settings;

	/**
	 * The cached value of the '{@link #getActiveSetting() <em>Active Setting</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getActiveSetting()
	 * @generated
	 * @ordered
	 */
	protected OptimiserSettings activeSetting;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ParametersModelImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ParametersPackage.Literals.PARAMETERS_MODEL;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<OptimiserSettings> getSettings() {
		if (settings == null) {
			settings = new EObjectContainmentEList<OptimiserSettings>(OptimiserSettings.class, this, ParametersPackage.PARAMETERS_MODEL__SETTINGS);
		}
		return settings;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public OptimiserSettings getActiveSetting() {
		if (activeSetting != null && activeSetting.eIsProxy()) {
			InternalEObject oldActiveSetting = (InternalEObject)activeSetting;
			activeSetting = (OptimiserSettings)eResolveProxy(oldActiveSetting);
			if (activeSetting != oldActiveSetting) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ParametersPackage.PARAMETERS_MODEL__ACTIVE_SETTING, oldActiveSetting, activeSetting));
			}
		}
		return activeSetting;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public OptimiserSettings basicGetActiveSetting() {
		return activeSetting;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setActiveSetting(OptimiserSettings newActiveSetting) {
		OptimiserSettings oldActiveSetting = activeSetting;
		activeSetting = newActiveSetting;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ParametersPackage.PARAMETERS_MODEL__ACTIVE_SETTING, oldActiveSetting, activeSetting));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case ParametersPackage.PARAMETERS_MODEL__SETTINGS:
				return ((InternalEList<?>)getSettings()).basicRemove(otherEnd, msgs);
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
			case ParametersPackage.PARAMETERS_MODEL__SETTINGS:
				return getSettings();
			case ParametersPackage.PARAMETERS_MODEL__ACTIVE_SETTING:
				if (resolve) return getActiveSetting();
				return basicGetActiveSetting();
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
			case ParametersPackage.PARAMETERS_MODEL__SETTINGS:
				getSettings().clear();
				getSettings().addAll((Collection<? extends OptimiserSettings>)newValue);
				return;
			case ParametersPackage.PARAMETERS_MODEL__ACTIVE_SETTING:
				setActiveSetting((OptimiserSettings)newValue);
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
			case ParametersPackage.PARAMETERS_MODEL__SETTINGS:
				getSettings().clear();
				return;
			case ParametersPackage.PARAMETERS_MODEL__ACTIVE_SETTING:
				setActiveSetting((OptimiserSettings)null);
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
			case ParametersPackage.PARAMETERS_MODEL__SETTINGS:
				return settings != null && !settings.isEmpty();
			case ParametersPackage.PARAMETERS_MODEL__ACTIVE_SETTING:
				return activeSetting != null;
		}
		return super.eIsSet(featureID);
	}

} //ParametersModelImpl

// finish type fixing

