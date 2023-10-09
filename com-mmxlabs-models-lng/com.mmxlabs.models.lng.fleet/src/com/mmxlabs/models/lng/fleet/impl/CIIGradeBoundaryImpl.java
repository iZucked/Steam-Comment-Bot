/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
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
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.CIIGradeBoundaryImpl#getGradeA <em>Grade A</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.CIIGradeBoundaryImpl#getGradeAValue <em>Grade AValue</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.CIIGradeBoundaryImpl#getGradeB <em>Grade B</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.CIIGradeBoundaryImpl#getGradeBValue <em>Grade BValue</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.CIIGradeBoundaryImpl#getGradeC <em>Grade C</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.CIIGradeBoundaryImpl#getGradeCValue <em>Grade CValue</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.CIIGradeBoundaryImpl#getGradeD <em>Grade D</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.CIIGradeBoundaryImpl#getGradeDValue <em>Grade DValue</em>}</li>
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
	protected static final long DWT_UPPER_LIMIT_EDEFAULT = 0L;

	/**
	 * The cached value of the '{@link #getDwtUpperLimit() <em>Dwt Upper Limit</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDwtUpperLimit()
	 * @generated
	 * @ordered
	 */
	protected long dwtUpperLimit = DWT_UPPER_LIMIT_EDEFAULT;

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
	 * The default value of the '{@link #getGradeA() <em>Grade A</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getGradeA()
	 * @generated
	 * @ordered
	 */
	protected static final String GRADE_A_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getGradeA() <em>Grade A</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getGradeA()
	 * @generated
	 * @ordered
	 */
	protected String gradeA = GRADE_A_EDEFAULT;

	/**
	 * The default value of the '{@link #getGradeAValue() <em>Grade AValue</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getGradeAValue()
	 * @generated
	 * @ordered
	 */
	protected static final double GRADE_AVALUE_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getGradeAValue() <em>Grade AValue</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getGradeAValue()
	 * @generated
	 * @ordered
	 */
	protected double gradeAValue = GRADE_AVALUE_EDEFAULT;

	/**
	 * The default value of the '{@link #getGradeB() <em>Grade B</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getGradeB()
	 * @generated
	 * @ordered
	 */
	protected static final String GRADE_B_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getGradeB() <em>Grade B</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getGradeB()
	 * @generated
	 * @ordered
	 */
	protected String gradeB = GRADE_B_EDEFAULT;

	/**
	 * The default value of the '{@link #getGradeBValue() <em>Grade BValue</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getGradeBValue()
	 * @generated
	 * @ordered
	 */
	protected static final double GRADE_BVALUE_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getGradeBValue() <em>Grade BValue</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getGradeBValue()
	 * @generated
	 * @ordered
	 */
	protected double gradeBValue = GRADE_BVALUE_EDEFAULT;

	/**
	 * The default value of the '{@link #getGradeC() <em>Grade C</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getGradeC()
	 * @generated
	 * @ordered
	 */
	protected static final String GRADE_C_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getGradeC() <em>Grade C</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getGradeC()
	 * @generated
	 * @ordered
	 */
	protected String gradeC = GRADE_C_EDEFAULT;

	/**
	 * The default value of the '{@link #getGradeCValue() <em>Grade CValue</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getGradeCValue()
	 * @generated
	 * @ordered
	 */
	protected static final double GRADE_CVALUE_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getGradeCValue() <em>Grade CValue</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getGradeCValue()
	 * @generated
	 * @ordered
	 */
	protected double gradeCValue = GRADE_CVALUE_EDEFAULT;

	/**
	 * The default value of the '{@link #getGradeD() <em>Grade D</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getGradeD()
	 * @generated
	 * @ordered
	 */
	protected static final String GRADE_D_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getGradeD() <em>Grade D</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getGradeD()
	 * @generated
	 * @ordered
	 */
	protected String gradeD = GRADE_D_EDEFAULT;

	/**
	 * The default value of the '{@link #getGradeDValue() <em>Grade DValue</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getGradeDValue()
	 * @generated
	 * @ordered
	 */
	protected static final double GRADE_DVALUE_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getGradeDValue() <em>Grade DValue</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getGradeDValue()
	 * @generated
	 * @ordered
	 */
	protected double gradeDValue = GRADE_DVALUE_EDEFAULT;

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
	public long getDwtUpperLimit() {
		return dwtUpperLimit;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setDwtUpperLimit(long newDwtUpperLimit) {
		long oldDwtUpperLimit = dwtUpperLimit;
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
	public String getGradeA() {
		return gradeA;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setGradeA(String newGradeA) {
		String oldGradeA = gradeA;
		gradeA = newGradeA;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.CII_GRADE_BOUNDARY__GRADE_A, oldGradeA, gradeA));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public double getGradeAValue() {
		return gradeAValue;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setGradeAValue(double newGradeAValue) {
		double oldGradeAValue = gradeAValue;
		gradeAValue = newGradeAValue;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.CII_GRADE_BOUNDARY__GRADE_AVALUE, oldGradeAValue, gradeAValue));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getGradeB() {
		return gradeB;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setGradeB(String newGradeB) {
		String oldGradeB = gradeB;
		gradeB = newGradeB;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.CII_GRADE_BOUNDARY__GRADE_B, oldGradeB, gradeB));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public double getGradeBValue() {
		return gradeBValue;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setGradeBValue(double newGradeBValue) {
		double oldGradeBValue = gradeBValue;
		gradeBValue = newGradeBValue;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.CII_GRADE_BOUNDARY__GRADE_BVALUE, oldGradeBValue, gradeBValue));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getGradeC() {
		return gradeC;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setGradeC(String newGradeC) {
		String oldGradeC = gradeC;
		gradeC = newGradeC;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.CII_GRADE_BOUNDARY__GRADE_C, oldGradeC, gradeC));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public double getGradeCValue() {
		return gradeCValue;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setGradeCValue(double newGradeCValue) {
		double oldGradeCValue = gradeCValue;
		gradeCValue = newGradeCValue;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.CII_GRADE_BOUNDARY__GRADE_CVALUE, oldGradeCValue, gradeCValue));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getGradeD() {
		return gradeD;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setGradeD(String newGradeD) {
		String oldGradeD = gradeD;
		gradeD = newGradeD;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.CII_GRADE_BOUNDARY__GRADE_D, oldGradeD, gradeD));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public double getGradeDValue() {
		return gradeDValue;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setGradeDValue(double newGradeDValue) {
		double oldGradeDValue = gradeDValue;
		gradeDValue = newGradeDValue;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.CII_GRADE_BOUNDARY__GRADE_DVALUE, oldGradeDValue, gradeDValue));
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
			case FleetPackage.CII_GRADE_BOUNDARY__GRADE_A:
				return getGradeA();
			case FleetPackage.CII_GRADE_BOUNDARY__GRADE_AVALUE:
				return getGradeAValue();
			case FleetPackage.CII_GRADE_BOUNDARY__GRADE_B:
				return getGradeB();
			case FleetPackage.CII_GRADE_BOUNDARY__GRADE_BVALUE:
				return getGradeBValue();
			case FleetPackage.CII_GRADE_BOUNDARY__GRADE_C:
				return getGradeC();
			case FleetPackage.CII_GRADE_BOUNDARY__GRADE_CVALUE:
				return getGradeCValue();
			case FleetPackage.CII_GRADE_BOUNDARY__GRADE_D:
				return getGradeD();
			case FleetPackage.CII_GRADE_BOUNDARY__GRADE_DVALUE:
				return getGradeDValue();
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
				setDwtUpperLimit((Long)newValue);
				return;
			case FleetPackage.CII_GRADE_BOUNDARY__GRADE:
				setGrade((String)newValue);
				return;
			case FleetPackage.CII_GRADE_BOUNDARY__UPPER_LIMIT:
				setUpperLimit((Double)newValue);
				return;
			case FleetPackage.CII_GRADE_BOUNDARY__GRADE_A:
				setGradeA((String)newValue);
				return;
			case FleetPackage.CII_GRADE_BOUNDARY__GRADE_AVALUE:
				setGradeAValue((Double)newValue);
				return;
			case FleetPackage.CII_GRADE_BOUNDARY__GRADE_B:
				setGradeB((String)newValue);
				return;
			case FleetPackage.CII_GRADE_BOUNDARY__GRADE_BVALUE:
				setGradeBValue((Double)newValue);
				return;
			case FleetPackage.CII_GRADE_BOUNDARY__GRADE_C:
				setGradeC((String)newValue);
				return;
			case FleetPackage.CII_GRADE_BOUNDARY__GRADE_CVALUE:
				setGradeCValue((Double)newValue);
				return;
			case FleetPackage.CII_GRADE_BOUNDARY__GRADE_D:
				setGradeD((String)newValue);
				return;
			case FleetPackage.CII_GRADE_BOUNDARY__GRADE_DVALUE:
				setGradeDValue((Double)newValue);
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
			case FleetPackage.CII_GRADE_BOUNDARY__GRADE_A:
				setGradeA(GRADE_A_EDEFAULT);
				return;
			case FleetPackage.CII_GRADE_BOUNDARY__GRADE_AVALUE:
				setGradeAValue(GRADE_AVALUE_EDEFAULT);
				return;
			case FleetPackage.CII_GRADE_BOUNDARY__GRADE_B:
				setGradeB(GRADE_B_EDEFAULT);
				return;
			case FleetPackage.CII_GRADE_BOUNDARY__GRADE_BVALUE:
				setGradeBValue(GRADE_BVALUE_EDEFAULT);
				return;
			case FleetPackage.CII_GRADE_BOUNDARY__GRADE_C:
				setGradeC(GRADE_C_EDEFAULT);
				return;
			case FleetPackage.CII_GRADE_BOUNDARY__GRADE_CVALUE:
				setGradeCValue(GRADE_CVALUE_EDEFAULT);
				return;
			case FleetPackage.CII_GRADE_BOUNDARY__GRADE_D:
				setGradeD(GRADE_D_EDEFAULT);
				return;
			case FleetPackage.CII_GRADE_BOUNDARY__GRADE_DVALUE:
				setGradeDValue(GRADE_DVALUE_EDEFAULT);
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
			case FleetPackage.CII_GRADE_BOUNDARY__GRADE_A:
				return GRADE_A_EDEFAULT == null ? gradeA != null : !GRADE_A_EDEFAULT.equals(gradeA);
			case FleetPackage.CII_GRADE_BOUNDARY__GRADE_AVALUE:
				return gradeAValue != GRADE_AVALUE_EDEFAULT;
			case FleetPackage.CII_GRADE_BOUNDARY__GRADE_B:
				return GRADE_B_EDEFAULT == null ? gradeB != null : !GRADE_B_EDEFAULT.equals(gradeB);
			case FleetPackage.CII_GRADE_BOUNDARY__GRADE_BVALUE:
				return gradeBValue != GRADE_BVALUE_EDEFAULT;
			case FleetPackage.CII_GRADE_BOUNDARY__GRADE_C:
				return GRADE_C_EDEFAULT == null ? gradeC != null : !GRADE_C_EDEFAULT.equals(gradeC);
			case FleetPackage.CII_GRADE_BOUNDARY__GRADE_CVALUE:
				return gradeCValue != GRADE_CVALUE_EDEFAULT;
			case FleetPackage.CII_GRADE_BOUNDARY__GRADE_D:
				return GRADE_D_EDEFAULT == null ? gradeD != null : !GRADE_D_EDEFAULT.equals(gradeD);
			case FleetPackage.CII_GRADE_BOUNDARY__GRADE_DVALUE:
				return gradeDValue != GRADE_DVALUE_EDEFAULT;
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
		result.append(", gradeA: ");
		result.append(gradeA);
		result.append(", gradeAValue: ");
		result.append(gradeAValue);
		result.append(", gradeB: ");
		result.append(gradeB);
		result.append(", gradeBValue: ");
		result.append(gradeBValue);
		result.append(", gradeC: ");
		result.append(gradeC);
		result.append(", gradeCValue: ");
		result.append(gradeCValue);
		result.append(", gradeD: ");
		result.append(gradeD);
		result.append(", gradeDValue: ");
		result.append(gradeDValue);
		result.append(')');
		return result.toString();
	}

} //CIIGradeBoundaryImpl
