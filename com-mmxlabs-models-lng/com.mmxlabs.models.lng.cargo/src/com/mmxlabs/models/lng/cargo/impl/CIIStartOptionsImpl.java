/**
 */
package com.mmxlabs.models.lng.cargo.impl;

import com.mmxlabs.models.lng.cargo.CIIStartOptions;
import com.mmxlabs.models.lng.cargo.CargoPackage;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>CII Start Options</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.CIIStartOptionsImpl#getYearTodayEmissions <em>Year Today Emissions</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.CIIStartOptionsImpl#getYearTodayDistance <em>Year Today Distance</em>}</li>
 * </ul>
 *
 * @generated
 */
public class CIIStartOptionsImpl extends EObjectImpl implements CIIStartOptions {
	/**
	 * The default value of the '{@link #getYearTodayEmissions() <em>Year Today Emissions</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getYearTodayEmissions()
	 * @generated
	 * @ordered
	 */
	protected static final int YEAR_TODAY_EMISSIONS_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getYearTodayEmissions() <em>Year Today Emissions</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getYearTodayEmissions()
	 * @generated
	 * @ordered
	 */
	protected int yearTodayEmissions = YEAR_TODAY_EMISSIONS_EDEFAULT;

	/**
	 * The default value of the '{@link #getYearTodayDistance() <em>Year Today Distance</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getYearTodayDistance()
	 * @generated
	 * @ordered
	 */
	protected static final int YEAR_TODAY_DISTANCE_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getYearTodayDistance() <em>Year Today Distance</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getYearTodayDistance()
	 * @generated
	 * @ordered
	 */
	protected int yearTodayDistance = YEAR_TODAY_DISTANCE_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected CIIStartOptionsImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return CargoPackage.Literals.CII_START_OPTIONS;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getYearTodayEmissions() {
		return yearTodayEmissions;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setYearTodayEmissions(int newYearTodayEmissions) {
		int oldYearTodayEmissions = yearTodayEmissions;
		yearTodayEmissions = newYearTodayEmissions;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.CII_START_OPTIONS__YEAR_TODAY_EMISSIONS, oldYearTodayEmissions, yearTodayEmissions));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getYearTodayDistance() {
		return yearTodayDistance;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setYearTodayDistance(int newYearTodayDistance) {
		int oldYearTodayDistance = yearTodayDistance;
		yearTodayDistance = newYearTodayDistance;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.CII_START_OPTIONS__YEAR_TODAY_DISTANCE, oldYearTodayDistance, yearTodayDistance));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case CargoPackage.CII_START_OPTIONS__YEAR_TODAY_EMISSIONS:
				return getYearTodayEmissions();
			case CargoPackage.CII_START_OPTIONS__YEAR_TODAY_DISTANCE:
				return getYearTodayDistance();
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
			case CargoPackage.CII_START_OPTIONS__YEAR_TODAY_EMISSIONS:
				setYearTodayEmissions((Integer)newValue);
				return;
			case CargoPackage.CII_START_OPTIONS__YEAR_TODAY_DISTANCE:
				setYearTodayDistance((Integer)newValue);
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
			case CargoPackage.CII_START_OPTIONS__YEAR_TODAY_EMISSIONS:
				setYearTodayEmissions(YEAR_TODAY_EMISSIONS_EDEFAULT);
				return;
			case CargoPackage.CII_START_OPTIONS__YEAR_TODAY_DISTANCE:
				setYearTodayDistance(YEAR_TODAY_DISTANCE_EDEFAULT);
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
			case CargoPackage.CII_START_OPTIONS__YEAR_TODAY_EMISSIONS:
				return yearTodayEmissions != YEAR_TODAY_EMISSIONS_EDEFAULT;
			case CargoPackage.CII_START_OPTIONS__YEAR_TODAY_DISTANCE:
				return yearTodayDistance != YEAR_TODAY_DISTANCE_EDEFAULT;
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
		result.append(" (yearTodayEmissions: ");
		result.append(yearTodayEmissions);
		result.append(", yearTodayDistance: ");
		result.append(yearTodayDistance);
		result.append(')');
		return result.toString();
	}

} //CIIStartOptionsImpl
