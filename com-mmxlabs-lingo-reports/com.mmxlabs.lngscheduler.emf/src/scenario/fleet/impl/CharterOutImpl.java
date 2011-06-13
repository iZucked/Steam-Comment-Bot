/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package scenario.fleet.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import scenario.fleet.CharterOut;
import scenario.fleet.FleetPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Charter Out</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link scenario.fleet.impl.CharterOutImpl#getMinHeelOut <em>Min Heel Out</em>}</li>
 *   <li>{@link scenario.fleet.impl.CharterOutImpl#getMaxHeelOut <em>Max Heel Out</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class CharterOutImpl extends VesselEventImpl implements CharterOut {
	/**
	 * The default value of the '{@link #getMinHeelOut() <em>Min Heel Out</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMinHeelOut()
	 * @generated
	 * @ordered
	 */
	protected static final int MIN_HEEL_OUT_EDEFAULT = 0;
	/**
	 * The cached value of the '{@link #getMinHeelOut() <em>Min Heel Out</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMinHeelOut()
	 * @generated
	 * @ordered
	 */
	protected int minHeelOut = MIN_HEEL_OUT_EDEFAULT;
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
	public int getMinHeelOut() {
		return minHeelOut;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMinHeelOut(int newMinHeelOut) {
		int oldMinHeelOut = minHeelOut;
		minHeelOut = newMinHeelOut;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.CHARTER_OUT__MIN_HEEL_OUT, oldMinHeelOut, minHeelOut));
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
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case FleetPackage.CHARTER_OUT__MIN_HEEL_OUT:
				return getMinHeelOut();
			case FleetPackage.CHARTER_OUT__MAX_HEEL_OUT:
				return getMaxHeelOut();
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
			case FleetPackage.CHARTER_OUT__MIN_HEEL_OUT:
				setMinHeelOut((Integer)newValue);
				return;
			case FleetPackage.CHARTER_OUT__MAX_HEEL_OUT:
				setMaxHeelOut((Integer)newValue);
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
			case FleetPackage.CHARTER_OUT__MIN_HEEL_OUT:
				setMinHeelOut(MIN_HEEL_OUT_EDEFAULT);
				return;
			case FleetPackage.CHARTER_OUT__MAX_HEEL_OUT:
				setMaxHeelOut(MAX_HEEL_OUT_EDEFAULT);
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
			case FleetPackage.CHARTER_OUT__MIN_HEEL_OUT:
				return minHeelOut != MIN_HEEL_OUT_EDEFAULT;
			case FleetPackage.CHARTER_OUT__MAX_HEEL_OUT:
				return maxHeelOut != MAX_HEEL_OUT_EDEFAULT;
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
		result.append(" (minHeelOut: ");
		result.append(minHeelOut);
		result.append(", maxHeelOut: ");
		result.append(maxHeelOut);
		result.append(')');
		return result.toString();
	}

} //CharterOutImpl
