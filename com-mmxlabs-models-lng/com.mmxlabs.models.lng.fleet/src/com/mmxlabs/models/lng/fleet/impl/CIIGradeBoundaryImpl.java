/**
 */
package com.mmxlabs.models.lng.fleet.impl;

import com.mmxlabs.models.lng.fleet.CIIGradeBoundary;
import com.mmxlabs.models.lng.fleet.FleetPackage;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>CII Grade Boundary</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.CIIGradeBoundaryImpl#getDwtUpperLimit <em>Dwt Upper Limit</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.CIIGradeBoundaryImpl#getGrade <em>Grade</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.CIIGradeBoundaryImpl#getUpperLimit <em>Upper Limit</em>}</li>
 * </ul>
 *
 * @generated
 */
public class CIIGradeBoundaryImpl extends EObjectImpl implements CIIGradeBoundary {
	/**
	 * The default value of the '{@link #getDwtUpperLimit() <em>Dwt Upper Limit</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDwtUpperLimit()
	 * @generated
	 * @ordered
	 */
	protected static final double DWT_UPPER_LIMIT_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getDwtUpperLimit() <em>Dwt Upper Limit</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDwtUpperLimit()
	 * @generated
	 * @ordered
	 */
	protected double dwtUpperLimit = DWT_UPPER_LIMIT_EDEFAULT;

	/**
	 * The default value of the '{@link #getGrade() <em>Grade</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getGrade()
	 * @generated
	 * @ordered
	 */
	protected static final String GRADE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getGrade() <em>Grade</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getGrade()
	 * @generated
	 * @ordered
	 */
	protected String grade = GRADE_EDEFAULT;

	/**
	 * The default value of the '{@link #getUpperLimit() <em>Upper Limit</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getUpperLimit()
	 * @generated
	 * @ordered
	 */
	protected static final double UPPER_LIMIT_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getUpperLimit() <em>Upper Limit</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getUpperLimit()
	 * @generated
	 * @ordered
	 */
	protected double upperLimit = UPPER_LIMIT_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected CIIGradeBoundaryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return FleetPackage.Literals.CII_GRADE_BOUNDARY;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public double getDwtUpperLimit() {
		return dwtUpperLimit;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setDwtUpperLimit(double newDwtUpperLimit) {
		double oldDwtUpperLimit = dwtUpperLimit;
		dwtUpperLimit = newDwtUpperLimit;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.CII_GRADE_BOUNDARY__DWT_UPPER_LIMIT, oldDwtUpperLimit, dwtUpperLimit));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getGrade() {
		return grade;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setGrade(String newGrade) {
		String oldGrade = grade;
		grade = newGrade;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.CII_GRADE_BOUNDARY__GRADE, oldGrade, grade));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public double getUpperLimit() {
		return upperLimit;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setUpperLimit(double newUpperLimit) {
		double oldUpperLimit = upperLimit;
		upperLimit = newUpperLimit;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.CII_GRADE_BOUNDARY__UPPER_LIMIT, oldUpperLimit, upperLimit));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case FleetPackage.CII_GRADE_BOUNDARY__DWT_UPPER_LIMIT:
				return getDwtUpperLimit();
			case FleetPackage.CII_GRADE_BOUNDARY__GRADE:
				return getGrade();
			case FleetPackage.CII_GRADE_BOUNDARY__UPPER_LIMIT:
				return getUpperLimit();
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
			case FleetPackage.CII_GRADE_BOUNDARY__DWT_UPPER_LIMIT:
				setDwtUpperLimit((Double)newValue);
				return;
			case FleetPackage.CII_GRADE_BOUNDARY__GRADE:
				setGrade((String)newValue);
				return;
			case FleetPackage.CII_GRADE_BOUNDARY__UPPER_LIMIT:
				setUpperLimit((Double)newValue);
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
			case FleetPackage.CII_GRADE_BOUNDARY__DWT_UPPER_LIMIT:
				setDwtUpperLimit(DWT_UPPER_LIMIT_EDEFAULT);
				return;
			case FleetPackage.CII_GRADE_BOUNDARY__GRADE:
				setGrade(GRADE_EDEFAULT);
				return;
			case FleetPackage.CII_GRADE_BOUNDARY__UPPER_LIMIT:
				setUpperLimit(UPPER_LIMIT_EDEFAULT);
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
			case FleetPackage.CII_GRADE_BOUNDARY__DWT_UPPER_LIMIT:
				return dwtUpperLimit != DWT_UPPER_LIMIT_EDEFAULT;
			case FleetPackage.CII_GRADE_BOUNDARY__GRADE:
				return GRADE_EDEFAULT == null ? grade != null : !GRADE_EDEFAULT.equals(grade);
			case FleetPackage.CII_GRADE_BOUNDARY__UPPER_LIMIT:
				return upperLimit != UPPER_LIMIT_EDEFAULT;
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
		result.append(" (dwtUpperLimit: ");
		result.append(dwtUpperLimit);
		result.append(", grade: ");
		result.append(grade);
		result.append(", upperLimit: ");
		result.append(upperLimit);
		result.append(')');
		return result.toString();
	}

} //CIIGradeBoundaryImpl
