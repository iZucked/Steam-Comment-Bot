/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package scenario.schedule.impl;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

import scenario.schedule.CharterOutRevenue;
import scenario.schedule.SchedulePackage;
import scenario.schedule.events.CharterOutVisit;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Charter Out Revenue</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link scenario.schedule.impl.CharterOutRevenueImpl#getCharterOut <em>Charter Out</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class CharterOutRevenueImpl extends BookedRevenueImpl implements CharterOutRevenue {
	/**
	 * The cached value of the '{@link #getCharterOut() <em>Charter Out</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCharterOut()
	 * @generated
	 * @ordered
	 */
	protected CharterOutVisit charterOut;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected CharterOutRevenueImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return SchedulePackage.Literals.CHARTER_OUT_REVENUE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CharterOutVisit getCharterOut() {
		if (charterOut != null && charterOut.eIsProxy()) {
			InternalEObject oldCharterOut = (InternalEObject)charterOut;
			charterOut = (CharterOutVisit)eResolveProxy(oldCharterOut);
			if (charterOut != oldCharterOut) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, SchedulePackage.CHARTER_OUT_REVENUE__CHARTER_OUT, oldCharterOut, charterOut));
			}
		}
		return charterOut;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CharterOutVisit basicGetCharterOut() {
		return charterOut;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setCharterOut(CharterOutVisit newCharterOut) {
		CharterOutVisit oldCharterOut = charterOut;
		charterOut = newCharterOut;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.CHARTER_OUT_REVENUE__CHARTER_OUT, oldCharterOut, charterOut));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getName() {
		return getCharterOut().getCharterOut().getId();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case SchedulePackage.CHARTER_OUT_REVENUE__CHARTER_OUT:
				if (resolve) return getCharterOut();
				return basicGetCharterOut();
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
			case SchedulePackage.CHARTER_OUT_REVENUE__CHARTER_OUT:
				setCharterOut((CharterOutVisit)newValue);
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
			case SchedulePackage.CHARTER_OUT_REVENUE__CHARTER_OUT:
				setCharterOut((CharterOutVisit)null);
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
			case SchedulePackage.CHARTER_OUT_REVENUE__CHARTER_OUT:
				return charterOut != null;
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eInvoke(int operationID, EList<?> arguments) throws InvocationTargetException {
		switch (operationID) {
			case SchedulePackage.CHARTER_OUT_REVENUE___GET_NAME:
				return getName();
		}
		return super.eInvoke(operationID, arguments);
	}

} //CharterOutRevenueImpl
