

/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package com.mmxlabs.models.lng.schedule.impl;
import com.mmxlabs.models.lng.schedule.AdditionalData;
import com.mmxlabs.models.lng.schedule.AdditionalDataHolder;
import com.mmxlabs.models.lng.schedule.SchedulePackage;

import com.mmxlabs.models.mmxcore.impl.NamedObjectImpl;

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

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Additional Data</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.AdditionalDataImpl#getAdditionalData <em>Additional Data</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.AdditionalDataImpl#getKey <em>Key</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.AdditionalDataImpl#getIntegerValue <em>Integer Value</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class AdditionalDataImpl extends NamedObjectImpl implements AdditionalData {
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
	 * The default value of the '{@link #getKey() <em>Key</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getKey()
	 * @generated
	 * @ordered
	 */
	protected static final String KEY_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getKey() <em>Key</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getKey()
	 * @generated
	 * @ordered
	 */
	protected String key = KEY_EDEFAULT;

	/**
	 * The default value of the '{@link #getIntegerValue() <em>Integer Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getIntegerValue()
	 * @generated
	 * @ordered
	 */
	protected static final int INTEGER_VALUE_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getIntegerValue() <em>Integer Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getIntegerValue()
	 * @generated
	 * @ordered
	 */
	protected int integerValue = INTEGER_VALUE_EDEFAULT;

	/**
	 * This is true if the Integer Value attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean integerValueESet;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected AdditionalDataImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return SchedulePackage.Literals.ADDITIONAL_DATA;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<AdditionalData> getAdditionalData() {
		if (additionalData == null) {
			additionalData = new EObjectContainmentEList<AdditionalData>(AdditionalData.class, this, SchedulePackage.ADDITIONAL_DATA__ADDITIONAL_DATA);
		}
		return additionalData;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getKey() {
		return key;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setKey(String newKey) {
		String oldKey = key;
		key = newKey;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.ADDITIONAL_DATA__KEY, oldKey, key));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getIntegerValue() {
		return integerValue;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setIntegerValue(int newIntegerValue) {
		int oldIntegerValue = integerValue;
		integerValue = newIntegerValue;
		boolean oldIntegerValueESet = integerValueESet;
		integerValueESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.ADDITIONAL_DATA__INTEGER_VALUE, oldIntegerValue, integerValue, !oldIntegerValueESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetIntegerValue() {
		int oldIntegerValue = integerValue;
		boolean oldIntegerValueESet = integerValueESet;
		integerValue = INTEGER_VALUE_EDEFAULT;
		integerValueESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, SchedulePackage.ADDITIONAL_DATA__INTEGER_VALUE, oldIntegerValue, INTEGER_VALUE_EDEFAULT, oldIntegerValueESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetIntegerValue() {
		return integerValueESet;
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
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case SchedulePackage.ADDITIONAL_DATA__ADDITIONAL_DATA:
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
			case SchedulePackage.ADDITIONAL_DATA__ADDITIONAL_DATA:
				return getAdditionalData();
			case SchedulePackage.ADDITIONAL_DATA__KEY:
				return getKey();
			case SchedulePackage.ADDITIONAL_DATA__INTEGER_VALUE:
				return getIntegerValue();
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
			case SchedulePackage.ADDITIONAL_DATA__ADDITIONAL_DATA:
				getAdditionalData().clear();
				getAdditionalData().addAll((Collection<? extends AdditionalData>)newValue);
				return;
			case SchedulePackage.ADDITIONAL_DATA__KEY:
				setKey((String)newValue);
				return;
			case SchedulePackage.ADDITIONAL_DATA__INTEGER_VALUE:
				setIntegerValue((Integer)newValue);
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
			case SchedulePackage.ADDITIONAL_DATA__ADDITIONAL_DATA:
				getAdditionalData().clear();
				return;
			case SchedulePackage.ADDITIONAL_DATA__KEY:
				setKey(KEY_EDEFAULT);
				return;
			case SchedulePackage.ADDITIONAL_DATA__INTEGER_VALUE:
				unsetIntegerValue();
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
			case SchedulePackage.ADDITIONAL_DATA__ADDITIONAL_DATA:
				return additionalData != null && !additionalData.isEmpty();
			case SchedulePackage.ADDITIONAL_DATA__KEY:
				return KEY_EDEFAULT == null ? key != null : !KEY_EDEFAULT.equals(key);
			case SchedulePackage.ADDITIONAL_DATA__INTEGER_VALUE:
				return isSetIntegerValue();
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
		if (baseClass == AdditionalDataHolder.class) {
			switch (derivedFeatureID) {
				case SchedulePackage.ADDITIONAL_DATA__ADDITIONAL_DATA: return SchedulePackage.ADDITIONAL_DATA_HOLDER__ADDITIONAL_DATA;
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
		if (baseClass == AdditionalDataHolder.class) {
			switch (baseFeatureID) {
				case SchedulePackage.ADDITIONAL_DATA_HOLDER__ADDITIONAL_DATA: return SchedulePackage.ADDITIONAL_DATA__ADDITIONAL_DATA;
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
		if (baseClass == AdditionalDataHolder.class) {
			switch (baseOperationID) {
				case SchedulePackage.ADDITIONAL_DATA_HOLDER___GET_ADDITIONAL_DATA_WITH_KEY__STRING: return SchedulePackage.ADDITIONAL_DATA___GET_ADDITIONAL_DATA_WITH_KEY__STRING;
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
	public Object eInvoke(int operationID, EList<?> arguments) throws InvocationTargetException {
		switch (operationID) {
			case SchedulePackage.ADDITIONAL_DATA___GET_ADDITIONAL_DATA_WITH_KEY__STRING:
				return getAdditionalDataWithKey((String)arguments.get(0));
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
		result.append(" (key: ");
		result.append(key);
		result.append(", integerValue: ");
		if (integerValueESet) result.append(integerValue); else result.append("<unset>");
		result.append(')');
		return result.toString();
	}

} // end of AdditionalDataImpl

// finish type fixing
