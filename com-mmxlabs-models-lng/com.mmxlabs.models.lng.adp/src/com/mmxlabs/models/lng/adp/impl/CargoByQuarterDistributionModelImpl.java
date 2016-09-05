/**
 */
package com.mmxlabs.models.lng.adp.impl;

import com.mmxlabs.models.lng.adp.ADPPackage;
import com.mmxlabs.models.lng.adp.CargoByQuarterDistributionModel;
import com.mmxlabs.models.lng.adp.ContractProfile;

import com.mmxlabs.models.lng.cargo.Slot;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Cargo By Quarter Distribution Model</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.adp.impl.CargoByQuarterDistributionModelImpl#getQ1 <em>Q1</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.impl.CargoByQuarterDistributionModelImpl#getQ2 <em>Q2</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.impl.CargoByQuarterDistributionModelImpl#getQ3 <em>Q3</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.impl.CargoByQuarterDistributionModelImpl#getQ4 <em>Q4</em>}</li>
 * </ul>
 *
 * @generated
 */
public class CargoByQuarterDistributionModelImpl extends EObjectImpl implements CargoByQuarterDistributionModel {
	/**
	 * The default value of the '{@link #getQ1() <em>Q1</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getQ1()
	 * @generated
	 * @ordered
	 */
	protected static final int Q1_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getQ1() <em>Q1</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getQ1()
	 * @generated
	 * @ordered
	 */
	protected int q1 = Q1_EDEFAULT;

	/**
	 * The default value of the '{@link #getQ2() <em>Q2</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getQ2()
	 * @generated
	 * @ordered
	 */
	protected static final int Q2_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getQ2() <em>Q2</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getQ2()
	 * @generated
	 * @ordered
	 */
	protected int q2 = Q2_EDEFAULT;

	/**
	 * The default value of the '{@link #getQ3() <em>Q3</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getQ3()
	 * @generated
	 * @ordered
	 */
	protected static final int Q3_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getQ3() <em>Q3</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getQ3()
	 * @generated
	 * @ordered
	 */
	protected int q3 = Q3_EDEFAULT;

	/**
	 * The default value of the '{@link #getQ4() <em>Q4</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getQ4()
	 * @generated
	 * @ordered
	 */
	protected static final int Q4_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getQ4() <em>Q4</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getQ4()
	 * @generated
	 * @ordered
	 */
	protected int q4 = Q4_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected CargoByQuarterDistributionModelImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ADPPackage.Literals.CARGO_BY_QUARTER_DISTRIBUTION_MODEL;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getQ1() {
		return q1;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setQ1(int newQ1) {
		int oldQ1 = q1;
		q1 = newQ1;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ADPPackage.CARGO_BY_QUARTER_DISTRIBUTION_MODEL__Q1, oldQ1, q1));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getQ2() {
		return q2;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setQ2(int newQ2) {
		int oldQ2 = q2;
		q2 = newQ2;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ADPPackage.CARGO_BY_QUARTER_DISTRIBUTION_MODEL__Q2, oldQ2, q2));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getQ3() {
		return q3;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setQ3(int newQ3) {
		int oldQ3 = q3;
		q3 = newQ3;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ADPPackage.CARGO_BY_QUARTER_DISTRIBUTION_MODEL__Q3, oldQ3, q3));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getQ4() {
		return q4;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setQ4(int newQ4) {
		int oldQ4 = q4;
		q4 = newQ4;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ADPPackage.CARGO_BY_QUARTER_DISTRIBUTION_MODEL__Q4, oldQ4, q4));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case ADPPackage.CARGO_BY_QUARTER_DISTRIBUTION_MODEL__Q1:
				return getQ1();
			case ADPPackage.CARGO_BY_QUARTER_DISTRIBUTION_MODEL__Q2:
				return getQ2();
			case ADPPackage.CARGO_BY_QUARTER_DISTRIBUTION_MODEL__Q3:
				return getQ3();
			case ADPPackage.CARGO_BY_QUARTER_DISTRIBUTION_MODEL__Q4:
				return getQ4();
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
			case ADPPackage.CARGO_BY_QUARTER_DISTRIBUTION_MODEL__Q1:
				setQ1((Integer)newValue);
				return;
			case ADPPackage.CARGO_BY_QUARTER_DISTRIBUTION_MODEL__Q2:
				setQ2((Integer)newValue);
				return;
			case ADPPackage.CARGO_BY_QUARTER_DISTRIBUTION_MODEL__Q3:
				setQ3((Integer)newValue);
				return;
			case ADPPackage.CARGO_BY_QUARTER_DISTRIBUTION_MODEL__Q4:
				setQ4((Integer)newValue);
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
			case ADPPackage.CARGO_BY_QUARTER_DISTRIBUTION_MODEL__Q1:
				setQ1(Q1_EDEFAULT);
				return;
			case ADPPackage.CARGO_BY_QUARTER_DISTRIBUTION_MODEL__Q2:
				setQ2(Q2_EDEFAULT);
				return;
			case ADPPackage.CARGO_BY_QUARTER_DISTRIBUTION_MODEL__Q3:
				setQ3(Q3_EDEFAULT);
				return;
			case ADPPackage.CARGO_BY_QUARTER_DISTRIBUTION_MODEL__Q4:
				setQ4(Q4_EDEFAULT);
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
			case ADPPackage.CARGO_BY_QUARTER_DISTRIBUTION_MODEL__Q1:
				return q1 != Q1_EDEFAULT;
			case ADPPackage.CARGO_BY_QUARTER_DISTRIBUTION_MODEL__Q2:
				return q2 != Q2_EDEFAULT;
			case ADPPackage.CARGO_BY_QUARTER_DISTRIBUTION_MODEL__Q3:
				return q3 != Q3_EDEFAULT;
			case ADPPackage.CARGO_BY_QUARTER_DISTRIBUTION_MODEL__Q4:
				return q4 != Q4_EDEFAULT;
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

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (q1: ");
		result.append(q1);
		result.append(", q2: ");
		result.append(q2);
		result.append(", q3: ");
		result.append(q3);
		result.append(", q4: ");
		result.append(q4);
		result.append(')');
		return result.toString();
	}

} //CargoByQuarterDistributionModelImpl
