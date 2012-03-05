

/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package com.mmxlabs.models.lng.optimiser.impl;
import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import com.mmxlabs.models.lng.optimiser.OptimiserModel;
import com.mmxlabs.models.lng.optimiser.OptimiserPackage;
import com.mmxlabs.models.lng.optimiser.OptimiserSettings;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.models.mmxcore.impl.UUIDObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Model</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.optimiser.impl.OptimiserModelImpl#getName <em>Name</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.optimiser.impl.OptimiserModelImpl#getSettings <em>Settings</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.optimiser.impl.OptimiserModelImpl#getActiveSetting <em>Active Setting</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class OptimiserModelImpl extends UUIDObjectImpl implements OptimiserModel {
	/**
	 * The default value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected static final String NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected String name = NAME_EDEFAULT;

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
	protected OptimiserModelImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return OptimiserPackage.Literals.OPTIMISER_MODEL;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getName() {
		return name;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setName(String newName) {
		String oldName = name;
		name = newName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, OptimiserPackage.OPTIMISER_MODEL__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<OptimiserSettings> getSettings() {
		if (settings == null) {
			settings = new EObjectContainmentEList<OptimiserSettings>(OptimiserSettings.class, this, OptimiserPackage.OPTIMISER_MODEL__SETTINGS);
		}
		return settings;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public OptimiserSettings getActiveSetting() {
		if (activeSetting != null && activeSetting.eIsProxy()) {
			InternalEObject oldActiveSetting = (InternalEObject)activeSetting;
			activeSetting = (OptimiserSettings)eResolveProxy(oldActiveSetting);
			if (activeSetting != oldActiveSetting) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, OptimiserPackage.OPTIMISER_MODEL__ACTIVE_SETTING, oldActiveSetting, activeSetting));
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
	public void setActiveSetting(OptimiserSettings newActiveSetting) {
		OptimiserSettings oldActiveSetting = activeSetting;
		activeSetting = newActiveSetting;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, OptimiserPackage.OPTIMISER_MODEL__ACTIVE_SETTING, oldActiveSetting, activeSetting));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case OptimiserPackage.OPTIMISER_MODEL__SETTINGS:
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
			case OptimiserPackage.OPTIMISER_MODEL__NAME:
				return getName();
			case OptimiserPackage.OPTIMISER_MODEL__SETTINGS:
				return getSettings();
			case OptimiserPackage.OPTIMISER_MODEL__ACTIVE_SETTING:
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
			case OptimiserPackage.OPTIMISER_MODEL__NAME:
				setName((String)newValue);
				return;
			case OptimiserPackage.OPTIMISER_MODEL__SETTINGS:
				getSettings().clear();
				getSettings().addAll((Collection<? extends OptimiserSettings>)newValue);
				return;
			case OptimiserPackage.OPTIMISER_MODEL__ACTIVE_SETTING:
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
			case OptimiserPackage.OPTIMISER_MODEL__NAME:
				setName(NAME_EDEFAULT);
				return;
			case OptimiserPackage.OPTIMISER_MODEL__SETTINGS:
				getSettings().clear();
				return;
			case OptimiserPackage.OPTIMISER_MODEL__ACTIVE_SETTING:
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
			case OptimiserPackage.OPTIMISER_MODEL__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case OptimiserPackage.OPTIMISER_MODEL__SETTINGS:
				return settings != null && !settings.isEmpty();
			case OptimiserPackage.OPTIMISER_MODEL__ACTIVE_SETTING:
				return activeSetting != null;
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
		if (baseClass == NamedObject.class) {
			switch (derivedFeatureID) {
				case OptimiserPackage.OPTIMISER_MODEL__NAME: return MMXCorePackage.NAMED_OBJECT__NAME;
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
		if (baseClass == NamedObject.class) {
			switch (baseFeatureID) {
				case MMXCorePackage.NAMED_OBJECT__NAME: return OptimiserPackage.OPTIMISER_MODEL__NAME;
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
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (name: ");
		result.append(name);
		result.append(')');
		return result.toString();
	}

} // end of OptimiserModelImpl

// finish type fixing
