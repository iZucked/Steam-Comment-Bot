/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package scenario.optimiser.impl;

import java.util.Collection;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;
import scenario.optimiser.Optimisation;
import scenario.optimiser.OptimisationSettings;
import scenario.optimiser.OptimiserPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Optimisation</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link scenario.optimiser.impl.OptimisationImpl#getAllSettings <em>All Settings</em>}</li>
 *   <li>{@link scenario.optimiser.impl.OptimisationImpl#getCurrentSettings <em>Current Settings</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class OptimisationImpl extends EObjectImpl implements Optimisation {
	/**
	 * The cached value of the '{@link #getAllSettings() <em>All Settings</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAllSettings()
	 * @generated
	 * @ordered
	 */
	protected EList<OptimisationSettings> allSettings;

	/**
	 * The cached value of the '{@link #getCurrentSettings() <em>Current Settings</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCurrentSettings()
	 * @generated
	 * @ordered
	 */
	protected OptimisationSettings currentSettings;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected OptimisationImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return OptimiserPackage.Literals.OPTIMISATION;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<OptimisationSettings> getAllSettings() {
		if (allSettings == null) {
			allSettings = new EObjectContainmentEList<OptimisationSettings>(OptimisationSettings.class, this, OptimiserPackage.OPTIMISATION__ALL_SETTINGS);
		}
		return allSettings;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public OptimisationSettings getCurrentSettings() {
		if (currentSettings != null && currentSettings.eIsProxy()) {
			InternalEObject oldCurrentSettings = (InternalEObject)currentSettings;
			currentSettings = (OptimisationSettings)eResolveProxy(oldCurrentSettings);
			if (currentSettings != oldCurrentSettings) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, OptimiserPackage.OPTIMISATION__CURRENT_SETTINGS, oldCurrentSettings, currentSettings));
			}
		}
		return currentSettings;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public OptimisationSettings basicGetCurrentSettings() {
		return currentSettings;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setCurrentSettings(OptimisationSettings newCurrentSettings) {
		OptimisationSettings oldCurrentSettings = currentSettings;
		currentSettings = newCurrentSettings;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, OptimiserPackage.OPTIMISATION__CURRENT_SETTINGS, oldCurrentSettings, currentSettings));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case OptimiserPackage.OPTIMISATION__ALL_SETTINGS:
				return ((InternalEList<?>)getAllSettings()).basicRemove(otherEnd, msgs);
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
			case OptimiserPackage.OPTIMISATION__ALL_SETTINGS:
				return getAllSettings();
			case OptimiserPackage.OPTIMISATION__CURRENT_SETTINGS:
				if (resolve) return getCurrentSettings();
				return basicGetCurrentSettings();
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
			case OptimiserPackage.OPTIMISATION__ALL_SETTINGS:
				getAllSettings().clear();
				getAllSettings().addAll((Collection<? extends OptimisationSettings>)newValue);
				return;
			case OptimiserPackage.OPTIMISATION__CURRENT_SETTINGS:
				setCurrentSettings((OptimisationSettings)newValue);
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
			case OptimiserPackage.OPTIMISATION__ALL_SETTINGS:
				getAllSettings().clear();
				return;
			case OptimiserPackage.OPTIMISATION__CURRENT_SETTINGS:
				setCurrentSettings((OptimisationSettings)null);
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
			case OptimiserPackage.OPTIMISATION__ALL_SETTINGS:
				return allSettings != null && !allSettings.isEmpty();
			case OptimiserPackage.OPTIMISATION__CURRENT_SETTINGS:
				return currentSettings != null;
		}
		return super.eIsSet(featureID);
	}

} //OptimisationImpl
