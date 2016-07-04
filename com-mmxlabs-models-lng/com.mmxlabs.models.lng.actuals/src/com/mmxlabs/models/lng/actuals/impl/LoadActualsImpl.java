/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.actuals.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

import com.mmxlabs.models.lng.actuals.ActualsPackage;
import com.mmxlabs.models.lng.actuals.LoadActuals;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Load Actuals</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.actuals.impl.LoadActualsImpl#getContractType <em>Contract Type</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.actuals.impl.LoadActualsImpl#getStartingHeelM3 <em>Starting Heel M3</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.actuals.impl.LoadActualsImpl#getStartingHeelMMBTu <em>Starting Heel MMB Tu</em>}</li>
 * </ul>
 *
 * @generated
 */
public class LoadActualsImpl extends SlotActualsImpl implements LoadActuals {
	/**
	 * The default value of the '{@link #getContractType() <em>Contract Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getContractType()
	 * @generated
	 * @ordered
	 */
	protected static final String CONTRACT_TYPE_EDEFAULT = null;
	/**
	 * The cached value of the '{@link #getContractType() <em>Contract Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getContractType()
	 * @generated
	 * @ordered
	 */
	protected String contractType = CONTRACT_TYPE_EDEFAULT;

	/**
	 * The default value of the '{@link #getStartingHeelM3() <em>Starting Heel M3</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStartingHeelM3()
	 * @generated
	 * @ordered
	 */
	protected static final double STARTING_HEEL_M3_EDEFAULT = 0.0;
	/**
	 * The cached value of the '{@link #getStartingHeelM3() <em>Starting Heel M3</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStartingHeelM3()
	 * @generated
	 * @ordered
	 */
	protected double startingHeelM3 = STARTING_HEEL_M3_EDEFAULT;
	/**
	 * The default value of the '{@link #getStartingHeelMMBTu() <em>Starting Heel MMB Tu</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStartingHeelMMBTu()
	 * @generated
	 * @ordered
	 */
	protected static final double STARTING_HEEL_MMB_TU_EDEFAULT = 0.0;
	/**
	 * The cached value of the '{@link #getStartingHeelMMBTu() <em>Starting Heel MMB Tu</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStartingHeelMMBTu()
	 * @generated
	 * @ordered
	 */
	protected double startingHeelMMBTu = STARTING_HEEL_MMB_TU_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected LoadActualsImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ActualsPackage.Literals.LOAD_ACTUALS;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getContractType() {
		return contractType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setContractType(String newContractType) {
		String oldContractType = contractType;
		contractType = newContractType;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ActualsPackage.LOAD_ACTUALS__CONTRACT_TYPE, oldContractType, contractType));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public double getStartingHeelM3() {
		return startingHeelM3;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setStartingHeelM3(double newStartingHeelM3) {
		double oldStartingHeelM3 = startingHeelM3;
		startingHeelM3 = newStartingHeelM3;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ActualsPackage.LOAD_ACTUALS__STARTING_HEEL_M3, oldStartingHeelM3, startingHeelM3));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public double getStartingHeelMMBTu() {
		return startingHeelMMBTu;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setStartingHeelMMBTu(double newStartingHeelMMBTu) {
		double oldStartingHeelMMBTu = startingHeelMMBTu;
		startingHeelMMBTu = newStartingHeelMMBTu;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ActualsPackage.LOAD_ACTUALS__STARTING_HEEL_MMB_TU, oldStartingHeelMMBTu, startingHeelMMBTu));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case ActualsPackage.LOAD_ACTUALS__CONTRACT_TYPE:
				return getContractType();
			case ActualsPackage.LOAD_ACTUALS__STARTING_HEEL_M3:
				return getStartingHeelM3();
			case ActualsPackage.LOAD_ACTUALS__STARTING_HEEL_MMB_TU:
				return getStartingHeelMMBTu();
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
			case ActualsPackage.LOAD_ACTUALS__CONTRACT_TYPE:
				setContractType((String)newValue);
				return;
			case ActualsPackage.LOAD_ACTUALS__STARTING_HEEL_M3:
				setStartingHeelM3((Double)newValue);
				return;
			case ActualsPackage.LOAD_ACTUALS__STARTING_HEEL_MMB_TU:
				setStartingHeelMMBTu((Double)newValue);
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
			case ActualsPackage.LOAD_ACTUALS__CONTRACT_TYPE:
				setContractType(CONTRACT_TYPE_EDEFAULT);
				return;
			case ActualsPackage.LOAD_ACTUALS__STARTING_HEEL_M3:
				setStartingHeelM3(STARTING_HEEL_M3_EDEFAULT);
				return;
			case ActualsPackage.LOAD_ACTUALS__STARTING_HEEL_MMB_TU:
				setStartingHeelMMBTu(STARTING_HEEL_MMB_TU_EDEFAULT);
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
			case ActualsPackage.LOAD_ACTUALS__CONTRACT_TYPE:
				return CONTRACT_TYPE_EDEFAULT == null ? contractType != null : !CONTRACT_TYPE_EDEFAULT.equals(contractType);
			case ActualsPackage.LOAD_ACTUALS__STARTING_HEEL_M3:
				return startingHeelM3 != STARTING_HEEL_M3_EDEFAULT;
			case ActualsPackage.LOAD_ACTUALS__STARTING_HEEL_MMB_TU:
				return startingHeelMMBTu != STARTING_HEEL_MMB_TU_EDEFAULT;
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
		result.append(" (contractType: ");
		result.append(contractType);
		result.append(", startingHeelM3: ");
		result.append(startingHeelM3);
		result.append(", startingHeelMMBTu: ");
		result.append(startingHeelMMBTu);
		result.append(')');
		return result.toString();
	}

} //LoadActualsImpl
