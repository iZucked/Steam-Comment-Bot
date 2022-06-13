/**
 */
package com.mmxlabs.models.lng.adp.impl;

import com.mmxlabs.models.lng.adp.ADPPackage;
import com.mmxlabs.models.lng.adp.AllowedArrivalTimeRecord;

import java.time.LocalDate;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EDataTypeUniqueEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Allowed Arrival Time Record</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.adp.impl.AllowedArrivalTimeRecordImpl#getPeriodStart <em>Period Start</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.impl.AllowedArrivalTimeRecordImpl#getAllowedTimes <em>Allowed Times</em>}</li>
 * </ul>
 *
 * @generated
 */
public class AllowedArrivalTimeRecordImpl extends EObjectImpl implements AllowedArrivalTimeRecord {
	/**
	 * The default value of the '{@link #getPeriodStart() <em>Period Start</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPeriodStart()
	 * @generated
	 * @ordered
	 */
	protected static final LocalDate PERIOD_START_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getPeriodStart() <em>Period Start</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPeriodStart()
	 * @generated
	 * @ordered
	 */
	protected LocalDate periodStart = PERIOD_START_EDEFAULT;

	/**
	 * The cached value of the '{@link #getAllowedTimes() <em>Allowed Times</em>}' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAllowedTimes()
	 * @generated
	 * @ordered
	 */
	protected EList<Integer> allowedTimes;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected AllowedArrivalTimeRecordImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ADPPackage.Literals.ALLOWED_ARRIVAL_TIME_RECORD;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public LocalDate getPeriodStart() {
		return periodStart;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setPeriodStart(LocalDate newPeriodStart) {
		LocalDate oldPeriodStart = periodStart;
		periodStart = newPeriodStart;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ADPPackage.ALLOWED_ARRIVAL_TIME_RECORD__PERIOD_START, oldPeriodStart, periodStart));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<Integer> getAllowedTimes() {
		if (allowedTimes == null) {
			allowedTimes = new EDataTypeUniqueEList<Integer>(Integer.class, this, ADPPackage.ALLOWED_ARRIVAL_TIME_RECORD__ALLOWED_TIMES);
		}
		return allowedTimes;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case ADPPackage.ALLOWED_ARRIVAL_TIME_RECORD__PERIOD_START:
				return getPeriodStart();
			case ADPPackage.ALLOWED_ARRIVAL_TIME_RECORD__ALLOWED_TIMES:
				return getAllowedTimes();
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
			case ADPPackage.ALLOWED_ARRIVAL_TIME_RECORD__PERIOD_START:
				setPeriodStart((LocalDate)newValue);
				return;
			case ADPPackage.ALLOWED_ARRIVAL_TIME_RECORD__ALLOWED_TIMES:
				getAllowedTimes().clear();
				getAllowedTimes().addAll((Collection<? extends Integer>)newValue);
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
			case ADPPackage.ALLOWED_ARRIVAL_TIME_RECORD__PERIOD_START:
				setPeriodStart(PERIOD_START_EDEFAULT);
				return;
			case ADPPackage.ALLOWED_ARRIVAL_TIME_RECORD__ALLOWED_TIMES:
				getAllowedTimes().clear();
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
			case ADPPackage.ALLOWED_ARRIVAL_TIME_RECORD__PERIOD_START:
				return PERIOD_START_EDEFAULT == null ? periodStart != null : !PERIOD_START_EDEFAULT.equals(periodStart);
			case ADPPackage.ALLOWED_ARRIVAL_TIME_RECORD__ALLOWED_TIMES:
				return allowedTimes != null && !allowedTimes.isEmpty();
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuilder result = new StringBuilder(super.toString());
		result.append(" (periodStart: ");
		result.append(periodStart);
		result.append(", allowedTimes: ");
		result.append(allowedTimes);
		result.append(')');
		return result.toString();
	}

} //AllowedArrivalTimeRecordImpl
