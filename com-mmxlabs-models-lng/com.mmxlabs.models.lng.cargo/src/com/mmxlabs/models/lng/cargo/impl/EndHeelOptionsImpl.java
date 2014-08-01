/**
 */
package com.mmxlabs.models.lng.cargo.impl;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.EndHeelOptions;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>End Heel Options</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.EndHeelOptionsImpl#isEndCold <em>End Cold</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.EndHeelOptionsImpl#getTargetEndHeel <em>Target End Heel</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class EndHeelOptionsImpl extends EObjectImpl implements EndHeelOptions {
	/**
	 * The default value of the '{@link #isEndCold() <em>End Cold</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isEndCold()
	 * @generated
	 * @ordered
	 */
	protected static final boolean END_COLD_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isEndCold() <em>End Cold</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isEndCold()
	 * @generated
	 * @ordered
	 */
	protected boolean endCold = END_COLD_EDEFAULT;

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
	public boolean isEndCold() {
		return endCold;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setEndCold(boolean newEndCold) {
		boolean oldEndCold = endCold;
		endCold = newEndCold;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.END_HEEL_OPTIONS__END_COLD, oldEndCold, endCold));
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
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.END_HEEL_OPTIONS__TARGET_END_HEEL, oldTargetEndHeel, targetEndHeel));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case CargoPackage.END_HEEL_OPTIONS__END_COLD:
				return isEndCold();
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
			case CargoPackage.END_HEEL_OPTIONS__END_COLD:
				setEndCold((Boolean)newValue);
				return;
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
			case CargoPackage.END_HEEL_OPTIONS__END_COLD:
				setEndCold(END_COLD_EDEFAULT);
				return;
			case CargoPackage.END_HEEL_OPTIONS__TARGET_END_HEEL:
				setTargetEndHeel(TARGET_END_HEEL_EDEFAULT);
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
			case CargoPackage.END_HEEL_OPTIONS__END_COLD:
				return endCold != END_COLD_EDEFAULT;
			case CargoPackage.END_HEEL_OPTIONS__TARGET_END_HEEL:
				return targetEndHeel != TARGET_END_HEEL_EDEFAULT;
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
		result.append(" (endCold: ");
		result.append(endCold);
		result.append(", targetEndHeel: ");
		result.append(targetEndHeel);
		result.append(')');
		return result.toString();
	}

} //EndHeelOptionsImpl
