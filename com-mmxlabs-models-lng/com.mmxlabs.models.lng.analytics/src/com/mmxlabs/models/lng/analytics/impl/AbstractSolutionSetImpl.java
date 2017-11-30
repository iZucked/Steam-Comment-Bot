/**
 */
package com.mmxlabs.models.lng.analytics.impl;

import com.mmxlabs.models.lng.analytics.AbstractSolutionSet;
import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.SolutionOption;

import com.mmxlabs.models.lng.cargo.Slot;

import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.mmxcore.NamedObject;

import com.mmxlabs.models.mmxcore.impl.UUIDObjectImpl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Abstract Solution Set</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.AbstractSolutionSetImpl#getName <em>Name</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.AbstractSolutionSetImpl#getUserSettings <em>User Settings</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.AbstractSolutionSetImpl#getOptions <em>Options</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.AbstractSolutionSetImpl#getExtraSlots <em>Extra Slots</em>}</li>
 * </ul>
 *
 * @generated
 */
public abstract class AbstractSolutionSetImpl extends UUIDObjectImpl implements AbstractSolutionSet {
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
	 * The cached value of the '{@link #getUserSettings() <em>User Settings</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getUserSettings()
	 * @generated
	 * @ordered
	 */
	protected UserSettings userSettings;

	/**
	 * The cached value of the '{@link #getOptions() <em>Options</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOptions()
	 * @generated
	 * @ordered
	 */
	protected EList<SolutionOption> options;

	/**
	 * The cached value of the '{@link #getExtraSlots() <em>Extra Slots</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getExtraSlots()
	 * @generated
	 * @ordered
	 */
	protected EList<Slot> extraSlots;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected AbstractSolutionSetImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return AnalyticsPackage.Literals.ABSTRACT_SOLUTION_SET;
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
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.ABSTRACT_SOLUTION_SET__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public UserSettings getUserSettings() {
		return userSettings;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetUserSettings(UserSettings newUserSettings, NotificationChain msgs) {
		UserSettings oldUserSettings = userSettings;
		userSettings = newUserSettings;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, AnalyticsPackage.ABSTRACT_SOLUTION_SET__USER_SETTINGS, oldUserSettings, newUserSettings);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setUserSettings(UserSettings newUserSettings) {
		if (newUserSettings != userSettings) {
			NotificationChain msgs = null;
			if (userSettings != null)
				msgs = ((InternalEObject)userSettings).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - AnalyticsPackage.ABSTRACT_SOLUTION_SET__USER_SETTINGS, null, msgs);
			if (newUserSettings != null)
				msgs = ((InternalEObject)newUserSettings).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - AnalyticsPackage.ABSTRACT_SOLUTION_SET__USER_SETTINGS, null, msgs);
			msgs = basicSetUserSettings(newUserSettings, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.ABSTRACT_SOLUTION_SET__USER_SETTINGS, newUserSettings, newUserSettings));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<SolutionOption> getOptions() {
		if (options == null) {
			options = new EObjectContainmentEList<SolutionOption>(SolutionOption.class, this, AnalyticsPackage.ABSTRACT_SOLUTION_SET__OPTIONS);
		}
		return options;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Slot> getExtraSlots() {
		if (extraSlots == null) {
			extraSlots = new EObjectContainmentEList<Slot>(Slot.class, this, AnalyticsPackage.ABSTRACT_SOLUTION_SET__EXTRA_SLOTS);
		}
		return extraSlots;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case AnalyticsPackage.ABSTRACT_SOLUTION_SET__USER_SETTINGS:
				return basicSetUserSettings(null, msgs);
			case AnalyticsPackage.ABSTRACT_SOLUTION_SET__OPTIONS:
				return ((InternalEList<?>)getOptions()).basicRemove(otherEnd, msgs);
			case AnalyticsPackage.ABSTRACT_SOLUTION_SET__EXTRA_SLOTS:
				return ((InternalEList<?>)getExtraSlots()).basicRemove(otherEnd, msgs);
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
			case AnalyticsPackage.ABSTRACT_SOLUTION_SET__NAME:
				return getName();
			case AnalyticsPackage.ABSTRACT_SOLUTION_SET__USER_SETTINGS:
				return getUserSettings();
			case AnalyticsPackage.ABSTRACT_SOLUTION_SET__OPTIONS:
				return getOptions();
			case AnalyticsPackage.ABSTRACT_SOLUTION_SET__EXTRA_SLOTS:
				return getExtraSlots();
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
			case AnalyticsPackage.ABSTRACT_SOLUTION_SET__NAME:
				setName((String)newValue);
				return;
			case AnalyticsPackage.ABSTRACT_SOLUTION_SET__USER_SETTINGS:
				setUserSettings((UserSettings)newValue);
				return;
			case AnalyticsPackage.ABSTRACT_SOLUTION_SET__OPTIONS:
				getOptions().clear();
				getOptions().addAll((Collection<? extends SolutionOption>)newValue);
				return;
			case AnalyticsPackage.ABSTRACT_SOLUTION_SET__EXTRA_SLOTS:
				getExtraSlots().clear();
				getExtraSlots().addAll((Collection<? extends Slot>)newValue);
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
			case AnalyticsPackage.ABSTRACT_SOLUTION_SET__NAME:
				setName(NAME_EDEFAULT);
				return;
			case AnalyticsPackage.ABSTRACT_SOLUTION_SET__USER_SETTINGS:
				setUserSettings((UserSettings)null);
				return;
			case AnalyticsPackage.ABSTRACT_SOLUTION_SET__OPTIONS:
				getOptions().clear();
				return;
			case AnalyticsPackage.ABSTRACT_SOLUTION_SET__EXTRA_SLOTS:
				getExtraSlots().clear();
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
			case AnalyticsPackage.ABSTRACT_SOLUTION_SET__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case AnalyticsPackage.ABSTRACT_SOLUTION_SET__USER_SETTINGS:
				return userSettings != null;
			case AnalyticsPackage.ABSTRACT_SOLUTION_SET__OPTIONS:
				return options != null && !options.isEmpty();
			case AnalyticsPackage.ABSTRACT_SOLUTION_SET__EXTRA_SLOTS:
				return extraSlots != null && !extraSlots.isEmpty();
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
				case AnalyticsPackage.ABSTRACT_SOLUTION_SET__NAME: return MMXCorePackage.NAMED_OBJECT__NAME;
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
				case MMXCorePackage.NAMED_OBJECT__NAME: return AnalyticsPackage.ABSTRACT_SOLUTION_SET__NAME;
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

} //AbstractSolutionSetImpl
