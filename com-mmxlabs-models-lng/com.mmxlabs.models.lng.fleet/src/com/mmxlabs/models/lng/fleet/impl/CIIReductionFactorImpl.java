/**
 */
package com.mmxlabs.models.lng.fleet.impl;

import com.mmxlabs.models.lng.fleet.CIIReductionFactor;
import com.mmxlabs.models.lng.fleet.FleetPackage;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>CII Reduction Factor</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.CIIReductionFactorImpl#getYear <em>Year</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.CIIReductionFactorImpl#getPercentage <em>Percentage</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.CIIReductionFactorImpl#getRemark <em>Remark</em>}</li>
 * </ul>
 *
 * @generated
 */
public class CIIReductionFactorImpl extends EObjectImpl implements CIIReductionFactor {
	/**
	 * The default value of the '{@link #getYear() <em>Year</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getYear()
	 * @generated
	 * @ordered
	 */
	protected static final int YEAR_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getYear() <em>Year</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getYear()
	 * @generated
	 * @ordered
	 */
	protected int year = YEAR_EDEFAULT;

	/**
	 * The default value of the '{@link #getPercentage() <em>Percentage</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPercentage()
	 * @generated
	 * @ordered
	 */
	protected static final int PERCENTAGE_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getPercentage() <em>Percentage</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPercentage()
	 * @generated
	 * @ordered
	 */
	protected int percentage = PERCENTAGE_EDEFAULT;

	/**
	 * The default value of the '{@link #getRemark() <em>Remark</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRemark()
	 * @generated
	 * @ordered
	 */
	protected static final String REMARK_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getRemark() <em>Remark</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRemark()
	 * @generated
	 * @ordered
	 */
	protected String remark = REMARK_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected CIIReductionFactorImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return FleetPackage.Literals.CII_REDUCTION_FACTOR;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getYear() {
		return year;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setYear(int newYear) {
		int oldYear = year;
		year = newYear;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.CII_REDUCTION_FACTOR__YEAR, oldYear, year));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getPercentage() {
		return percentage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setPercentage(int newPercentage) {
		int oldPercentage = percentage;
		percentage = newPercentage;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.CII_REDUCTION_FACTOR__PERCENTAGE, oldPercentage, percentage));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getRemark() {
		return remark;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setRemark(String newRemark) {
		String oldRemark = remark;
		remark = newRemark;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.CII_REDUCTION_FACTOR__REMARK, oldRemark, remark));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case FleetPackage.CII_REDUCTION_FACTOR__YEAR:
				return getYear();
			case FleetPackage.CII_REDUCTION_FACTOR__PERCENTAGE:
				return getPercentage();
			case FleetPackage.CII_REDUCTION_FACTOR__REMARK:
				return getRemark();
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
			case FleetPackage.CII_REDUCTION_FACTOR__YEAR:
				setYear((Integer)newValue);
				return;
			case FleetPackage.CII_REDUCTION_FACTOR__PERCENTAGE:
				setPercentage((Integer)newValue);
				return;
			case FleetPackage.CII_REDUCTION_FACTOR__REMARK:
				setRemark((String)newValue);
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
			case FleetPackage.CII_REDUCTION_FACTOR__YEAR:
				setYear(YEAR_EDEFAULT);
				return;
			case FleetPackage.CII_REDUCTION_FACTOR__PERCENTAGE:
				setPercentage(PERCENTAGE_EDEFAULT);
				return;
			case FleetPackage.CII_REDUCTION_FACTOR__REMARK:
				setRemark(REMARK_EDEFAULT);
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
			case FleetPackage.CII_REDUCTION_FACTOR__YEAR:
				return year != YEAR_EDEFAULT;
			case FleetPackage.CII_REDUCTION_FACTOR__PERCENTAGE:
				return percentage != PERCENTAGE_EDEFAULT;
			case FleetPackage.CII_REDUCTION_FACTOR__REMARK:
				return REMARK_EDEFAULT == null ? remark != null : !REMARK_EDEFAULT.equals(remark);
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
		result.append(" (year: ");
		result.append(year);
		result.append(", percentage: ");
		result.append(percentage);
		result.append(", remark: ");
		result.append(remark);
		result.append(')');
		return result.toString();
	}

} //CIIReductionFactorImpl
