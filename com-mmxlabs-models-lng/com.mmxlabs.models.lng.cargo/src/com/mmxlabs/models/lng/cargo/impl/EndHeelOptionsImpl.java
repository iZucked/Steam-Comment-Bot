/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.cargo.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.EndHeelOptions;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>End Heel Options</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.EndHeelOptionsImpl#getTargetEndHeel <em>Target End Heel</em>}</li>
 * </ul>
 *
 * @generated
 */
public class EndHeelOptionsImpl extends EObjectImpl implements EndHeelOptions {
	/**
	 * The default value of the '{@link #getTargetEndHeel() <em>Target End Heel</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTargetEndHeel()
	 * @generated
	 * @ordered
	 */
	protected static final int TARGET_END_HEEL_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getTargetEndHeel() <em>Target End Heel</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTargetEndHeel()
	 * @generated
	 * @ordered
	 */
	protected int targetEndHeel = TARGET_END_HEEL_EDEFAULT;

	/**
	 * This is true if the Target End Heel attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean targetEndHeelESet;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EndHeelOptionsImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return CargoPackage.Literals.END_HEEL_OPTIONS;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getTargetEndHeel() {
		return targetEndHeel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setTargetEndHeel(int newTargetEndHeel) {
		int oldTargetEndHeel = targetEndHeel;
		targetEndHeel = newTargetEndHeel;
		boolean oldTargetEndHeelESet = targetEndHeelESet;
		targetEndHeelESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.END_HEEL_OPTIONS__TARGET_END_HEEL, oldTargetEndHeel, targetEndHeel, !oldTargetEndHeelESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetTargetEndHeel() {
		int oldTargetEndHeel = targetEndHeel;
		boolean oldTargetEndHeelESet = targetEndHeelESet;
		targetEndHeel = TARGET_END_HEEL_EDEFAULT;
		targetEndHeelESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, CargoPackage.END_HEEL_OPTIONS__TARGET_END_HEEL, oldTargetEndHeel, TARGET_END_HEEL_EDEFAULT, oldTargetEndHeelESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetTargetEndHeel() {
		return targetEndHeelESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case CargoPackage.END_HEEL_OPTIONS__TARGET_END_HEEL:
				return getTargetEndHeel();
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
			case CargoPackage.END_HEEL_OPTIONS__TARGET_END_HEEL:
				setTargetEndHeel((Integer)newValue);
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
			case CargoPackage.END_HEEL_OPTIONS__TARGET_END_HEEL:
				unsetTargetEndHeel();
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
			case CargoPackage.END_HEEL_OPTIONS__TARGET_END_HEEL:
				return isSetTargetEndHeel();
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
		result.append(" (targetEndHeel: ");
		if (targetEndHeelESet) result.append(targetEndHeel); else result.append("<unset>");
		result.append(')');
		return result.toString();
	}

} //EndHeelOptionsImpl
