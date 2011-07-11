/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package scenario.fleet.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import scenario.fleet.CharterOut;
import scenario.fleet.FleetPackage;
import scenario.port.Port;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Charter Out</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link scenario.fleet.impl.CharterOutImpl#getMaxHeelOut <em>Max Heel Out</em>}</li>
 *   <li>{@link scenario.fleet.impl.CharterOutImpl#getHeelCVValue <em>Heel CV Value</em>}</li>
 *   <li>{@link scenario.fleet.impl.CharterOutImpl#getEndPort <em>End Port</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class CharterOutImpl extends VesselEventImpl implements CharterOut {
	/**
	 * The default value of the '{@link #getMaxHeelOut() <em>Max Heel Out</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMaxHeelOut()
	 * @generated
	 * @ordered
	 */
	protected static final int MAX_HEEL_OUT_EDEFAULT = 2147483647;
	/**
	 * The cached value of the '{@link #getMaxHeelOut() <em>Max Heel Out</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMaxHeelOut()
	 * @generated
	 * @ordered
	 */
	protected int maxHeelOut = MAX_HEEL_OUT_EDEFAULT;

	/**
	 * The default value of the '{@link #getHeelCVValue() <em>Heel CV Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getHeelCVValue()
	 * @generated
	 * @ordered
	 */
	protected static final float HEEL_CV_VALUE_EDEFAULT = 22.8F;
	/**
	 * The cached value of the '{@link #getHeelCVValue() <em>Heel CV Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getHeelCVValue()
	 * @generated
	 * @ordered
	 */
	protected float heelCVValue = HEEL_CV_VALUE_EDEFAULT;
	/**
	 * The cached value of the '{@link #getEndPort() <em>End Port</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEndPort()
	 * @generated
	 * @ordered
	 */
	protected Port endPort;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected CharterOutImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return FleetPackage.Literals.CHARTER_OUT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Port getEndPort() {
		if (endPort != null && endPort.eIsProxy()) {
			InternalEObject oldEndPort = (InternalEObject)endPort;
			endPort = (Port)eResolveProxy(oldEndPort);
			if (endPort != oldEndPort) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, FleetPackage.CHARTER_OUT__END_PORT, oldEndPort, endPort));
			}
		}
		return endPort;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Port basicGetEndPort() {
		return endPort;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setEndPort(Port newEndPort) {
		Port oldEndPort = endPort;
		endPort = newEndPort;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.CHARTER_OUT__END_PORT, oldEndPort, endPort));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getMaxHeelOut() {
		return maxHeelOut;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMaxHeelOut(int newMaxHeelOut) {
		int oldMaxHeelOut = maxHeelOut;
		maxHeelOut = newMaxHeelOut;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.CHARTER_OUT__MAX_HEEL_OUT, oldMaxHeelOut, maxHeelOut));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public float getHeelCVValue() {
		return heelCVValue;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setHeelCVValue(float newHeelCVValue) {
		float oldHeelCVValue = heelCVValue;
		heelCVValue = newHeelCVValue;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.CHARTER_OUT__HEEL_CV_VALUE, oldHeelCVValue, heelCVValue));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case FleetPackage.CHARTER_OUT__MAX_HEEL_OUT:
				return getMaxHeelOut();
			case FleetPackage.CHARTER_OUT__HEEL_CV_VALUE:
				return getHeelCVValue();
			case FleetPackage.CHARTER_OUT__END_PORT:
				if (resolve) return getEndPort();
				return basicGetEndPort();
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
			case FleetPackage.CHARTER_OUT__MAX_HEEL_OUT:
				setMaxHeelOut((Integer)newValue);
				return;
			case FleetPackage.CHARTER_OUT__HEEL_CV_VALUE:
				setHeelCVValue((Float)newValue);
				return;
			case FleetPackage.CHARTER_OUT__END_PORT:
				setEndPort((Port)newValue);
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
			case FleetPackage.CHARTER_OUT__MAX_HEEL_OUT:
				setMaxHeelOut(MAX_HEEL_OUT_EDEFAULT);
				return;
			case FleetPackage.CHARTER_OUT__HEEL_CV_VALUE:
				setHeelCVValue(HEEL_CV_VALUE_EDEFAULT);
				return;
			case FleetPackage.CHARTER_OUT__END_PORT:
				setEndPort((Port)null);
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
			case FleetPackage.CHARTER_OUT__MAX_HEEL_OUT:
				return maxHeelOut != MAX_HEEL_OUT_EDEFAULT;
			case FleetPackage.CHARTER_OUT__HEEL_CV_VALUE:
				return heelCVValue != HEEL_CV_VALUE_EDEFAULT;
			case FleetPackage.CHARTER_OUT__END_PORT:
				return endPort != null;
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
		result.append(" (maxHeelOut: ");
		result.append(maxHeelOut);
		result.append(", heelCVValue: ");
		result.append(heelCVValue);
		result.append(')');
		return result.toString();
	}

} //CharterOutImpl
