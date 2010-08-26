/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package scenario.optimiser.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

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
	 * The cached value of the '{@link #getAllSettings() <em>All Settings</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAllSettings()
	 * @generated
	 * @ordered
	 */
	protected OptimisationSettings allSettings;

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
	public OptimisationSettings getAllSettings() {
		return allSettings;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetAllSettings(OptimisationSettings newAllSettings, NotificationChain msgs) {
		OptimisationSettings oldAllSettings = allSettings;
		allSettings = newAllSettings;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, OptimiserPackage.OPTIMISATION__ALL_SETTINGS, oldAllSettings, newAllSettings);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setAllSettings(OptimisationSettings newAllSettings) {
		if (newAllSettings != allSettings) {
			NotificationChain msgs = null;
			if (allSettings != null)
				msgs = ((InternalEObject)allSettings).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - OptimiserPackage.OPTIMISATION__ALL_SETTINGS, null, msgs);
			if (newAllSettings != null)
				msgs = ((InternalEObject)newAllSettings).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - OptimiserPackage.OPTIMISATION__ALL_SETTINGS, null, msgs);
			msgs = basicSetAllSettings(newAllSettings, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, OptimiserPackage.OPTIMISATION__ALL_SETTINGS, newAllSettings, newAllSettings));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
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
				return basicSetAllSettings(null, msgs);
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
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case OptimiserPackage.OPTIMISATION__ALL_SETTINGS:
				setAllSettings((OptimisationSettings)newValue);
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
				setAllSettings((OptimisationSettings)null);
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
				return allSettings != null;
			case OptimiserPackage.OPTIMISATION__CURRENT_SETTINGS:
				return currentSettings != null;
		}
		return super.eIsSet(featureID);
	}

} //OptimisationImpl
