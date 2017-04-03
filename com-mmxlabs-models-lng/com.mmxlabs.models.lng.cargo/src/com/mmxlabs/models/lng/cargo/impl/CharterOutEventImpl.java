/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.cargo.impl;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.CharterOutEvent;
import com.mmxlabs.models.lng.cargo.EndHeelOptions;
import com.mmxlabs.models.lng.cargo.StartHeelOptions;
import com.mmxlabs.models.lng.port.Port;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Charter Out Event</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.CharterOutEventImpl#getRelocateTo <em>Relocate To</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.CharterOutEventImpl#getHireRate <em>Hire Rate</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.CharterOutEventImpl#getBallastBonus <em>Ballast Bonus</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.CharterOutEventImpl#getRepositioningFee <em>Repositioning Fee</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.CharterOutEventImpl#getRequiredHeel <em>Required Heel</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.CharterOutEventImpl#getAvailableHeel <em>Available Heel</em>}</li>
 * </ul>
 *
 * @generated
 */
public class CharterOutEventImpl extends VesselEventImpl implements CharterOutEvent {
	/**
	 * The cached value of the '{@link #getRelocateTo() <em>Relocate To</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRelocateTo()
	 * @generated
	 * @ordered
	 */
	protected Port relocateTo;

	/**
	 * This is true if the Relocate To reference has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean relocateToESet;

	/**
	 * The default value of the '{@link #getHireRate() <em>Hire Rate</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getHireRate()
	 * @generated
	 * @ordered
	 */
	protected static final int HIRE_RATE_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getHireRate() <em>Hire Rate</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getHireRate()
	 * @generated
	 * @ordered
	 */
	protected int hireRate = HIRE_RATE_EDEFAULT;

	/**
	 * The default value of the '{@link #getBallastBonus() <em>Ballast Bonus</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBallastBonus()
	 * @generated
	 * @ordered
	 */
	protected static final int BALLAST_BONUS_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getBallastBonus() <em>Ballast Bonus</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBallastBonus()
	 * @generated
	 * @ordered
	 */
	protected int ballastBonus = BALLAST_BONUS_EDEFAULT;

	/**
	 * The default value of the '{@link #getRepositioningFee() <em>Repositioning Fee</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRepositioningFee()
	 * @generated
	 * @ordered
	 */
	protected static final int REPOSITIONING_FEE_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getRepositioningFee() <em>Repositioning Fee</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRepositioningFee()
	 * @generated
	 * @ordered
	 */
	protected int repositioningFee = REPOSITIONING_FEE_EDEFAULT;

	/**
	 * The cached value of the '{@link #getRequiredHeel() <em>Required Heel</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRequiredHeel()
	 * @generated
	 * @ordered
	 */
	protected EndHeelOptions requiredHeel;

	/**
	 * The cached value of the '{@link #getAvailableHeel() <em>Available Heel</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAvailableHeel()
	 * @generated
	 * @ordered
	 */
	protected StartHeelOptions availableHeel;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected CharterOutEventImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return CargoPackage.Literals.CHARTER_OUT_EVENT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Port getRelocateTo() {
		if (relocateTo != null && relocateTo.eIsProxy()) {
			InternalEObject oldRelocateTo = (InternalEObject)relocateTo;
			relocateTo = (Port)eResolveProxy(oldRelocateTo);
			if (relocateTo != oldRelocateTo) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, CargoPackage.CHARTER_OUT_EVENT__RELOCATE_TO, oldRelocateTo, relocateTo));
			}
		}
		return relocateTo;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Port basicGetRelocateTo() {
		return relocateTo;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setRelocateTo(Port newRelocateTo) {
		Port oldRelocateTo = relocateTo;
		relocateTo = newRelocateTo;
		boolean oldRelocateToESet = relocateToESet;
		relocateToESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.CHARTER_OUT_EVENT__RELOCATE_TO, oldRelocateTo, relocateTo, !oldRelocateToESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetRelocateTo() {
		Port oldRelocateTo = relocateTo;
		boolean oldRelocateToESet = relocateToESet;
		relocateTo = null;
		relocateToESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, CargoPackage.CHARTER_OUT_EVENT__RELOCATE_TO, oldRelocateTo, null, oldRelocateToESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetRelocateTo() {
		return relocateToESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getRepositioningFee() {
		return repositioningFee;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setRepositioningFee(int newRepositioningFee) {
		int oldRepositioningFee = repositioningFee;
		repositioningFee = newRepositioningFee;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.CHARTER_OUT_EVENT__REPOSITIONING_FEE, oldRepositioningFee, repositioningFee));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EndHeelOptions getRequiredHeel() {
		if (requiredHeel != null && requiredHeel.eIsProxy()) {
			InternalEObject oldRequiredHeel = (InternalEObject)requiredHeel;
			requiredHeel = (EndHeelOptions)eResolveProxy(oldRequiredHeel);
			if (requiredHeel != oldRequiredHeel) {
				InternalEObject newRequiredHeel = (InternalEObject)requiredHeel;
				NotificationChain msgs = oldRequiredHeel.eInverseRemove(this, EOPPOSITE_FEATURE_BASE - CargoPackage.CHARTER_OUT_EVENT__REQUIRED_HEEL, null, null);
				if (newRequiredHeel.eInternalContainer() == null) {
					msgs = newRequiredHeel.eInverseAdd(this, EOPPOSITE_FEATURE_BASE - CargoPackage.CHARTER_OUT_EVENT__REQUIRED_HEEL, null, msgs);
				}
				if (msgs != null) msgs.dispatch();
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, CargoPackage.CHARTER_OUT_EVENT__REQUIRED_HEEL, oldRequiredHeel, requiredHeel));
			}
		}
		return requiredHeel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EndHeelOptions basicGetRequiredHeel() {
		return requiredHeel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetRequiredHeel(EndHeelOptions newRequiredHeel, NotificationChain msgs) {
		EndHeelOptions oldRequiredHeel = requiredHeel;
		requiredHeel = newRequiredHeel;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, CargoPackage.CHARTER_OUT_EVENT__REQUIRED_HEEL, oldRequiredHeel, newRequiredHeel);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setRequiredHeel(EndHeelOptions newRequiredHeel) {
		if (newRequiredHeel != requiredHeel) {
			NotificationChain msgs = null;
			if (requiredHeel != null)
				msgs = ((InternalEObject)requiredHeel).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - CargoPackage.CHARTER_OUT_EVENT__REQUIRED_HEEL, null, msgs);
			if (newRequiredHeel != null)
				msgs = ((InternalEObject)newRequiredHeel).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - CargoPackage.CHARTER_OUT_EVENT__REQUIRED_HEEL, null, msgs);
			msgs = basicSetRequiredHeel(newRequiredHeel, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.CHARTER_OUT_EVENT__REQUIRED_HEEL, newRequiredHeel, newRequiredHeel));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public StartHeelOptions getAvailableHeel() {
		if (availableHeel != null && availableHeel.eIsProxy()) {
			InternalEObject oldAvailableHeel = (InternalEObject)availableHeel;
			availableHeel = (StartHeelOptions)eResolveProxy(oldAvailableHeel);
			if (availableHeel != oldAvailableHeel) {
				InternalEObject newAvailableHeel = (InternalEObject)availableHeel;
				NotificationChain msgs = oldAvailableHeel.eInverseRemove(this, EOPPOSITE_FEATURE_BASE - CargoPackage.CHARTER_OUT_EVENT__AVAILABLE_HEEL, null, null);
				if (newAvailableHeel.eInternalContainer() == null) {
					msgs = newAvailableHeel.eInverseAdd(this, EOPPOSITE_FEATURE_BASE - CargoPackage.CHARTER_OUT_EVENT__AVAILABLE_HEEL, null, msgs);
				}
				if (msgs != null) msgs.dispatch();
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, CargoPackage.CHARTER_OUT_EVENT__AVAILABLE_HEEL, oldAvailableHeel, availableHeel));
			}
		}
		return availableHeel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public StartHeelOptions basicGetAvailableHeel() {
		return availableHeel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetAvailableHeel(StartHeelOptions newAvailableHeel, NotificationChain msgs) {
		StartHeelOptions oldAvailableHeel = availableHeel;
		availableHeel = newAvailableHeel;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, CargoPackage.CHARTER_OUT_EVENT__AVAILABLE_HEEL, oldAvailableHeel, newAvailableHeel);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setAvailableHeel(StartHeelOptions newAvailableHeel) {
		if (newAvailableHeel != availableHeel) {
			NotificationChain msgs = null;
			if (availableHeel != null)
				msgs = ((InternalEObject)availableHeel).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - CargoPackage.CHARTER_OUT_EVENT__AVAILABLE_HEEL, null, msgs);
			if (newAvailableHeel != null)
				msgs = ((InternalEObject)newAvailableHeel).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - CargoPackage.CHARTER_OUT_EVENT__AVAILABLE_HEEL, null, msgs);
			msgs = basicSetAvailableHeel(newAvailableHeel, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.CHARTER_OUT_EVENT__AVAILABLE_HEEL, newAvailableHeel, newAvailableHeel));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getHireRate() {
		return hireRate;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setHireRate(int newHireRate) {
		int oldHireRate = hireRate;
		hireRate = newHireRate;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.CHARTER_OUT_EVENT__HIRE_RATE, oldHireRate, hireRate));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getBallastBonus() {
		return ballastBonus;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setBallastBonus(int newBallastBonus) {
		int oldBallastBonus = ballastBonus;
		ballastBonus = newBallastBonus;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.CHARTER_OUT_EVENT__BALLAST_BONUS, oldBallastBonus, ballastBonus));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public Port getEndPort() {
		if (isSetRelocateTo()) return getRelocateTo();
		else return getPort();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case CargoPackage.CHARTER_OUT_EVENT__REQUIRED_HEEL:
				return basicSetRequiredHeel(null, msgs);
			case CargoPackage.CHARTER_OUT_EVENT__AVAILABLE_HEEL:
				return basicSetAvailableHeel(null, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case CargoPackage.CHARTER_OUT_EVENT__RELOCATE_TO:
				if (resolve) return getRelocateTo();
				return basicGetRelocateTo();
			case CargoPackage.CHARTER_OUT_EVENT__HIRE_RATE:
				return getHireRate();
			case CargoPackage.CHARTER_OUT_EVENT__BALLAST_BONUS:
				return getBallastBonus();
			case CargoPackage.CHARTER_OUT_EVENT__REPOSITIONING_FEE:
				return getRepositioningFee();
			case CargoPackage.CHARTER_OUT_EVENT__REQUIRED_HEEL:
				if (resolve) return getRequiredHeel();
				return basicGetRequiredHeel();
			case CargoPackage.CHARTER_OUT_EVENT__AVAILABLE_HEEL:
				if (resolve) return getAvailableHeel();
				return basicGetAvailableHeel();
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
			case CargoPackage.CHARTER_OUT_EVENT__RELOCATE_TO:
				setRelocateTo((Port)newValue);
				return;
			case CargoPackage.CHARTER_OUT_EVENT__HIRE_RATE:
				setHireRate((Integer)newValue);
				return;
			case CargoPackage.CHARTER_OUT_EVENT__BALLAST_BONUS:
				setBallastBonus((Integer)newValue);
				return;
			case CargoPackage.CHARTER_OUT_EVENT__REPOSITIONING_FEE:
				setRepositioningFee((Integer)newValue);
				return;
			case CargoPackage.CHARTER_OUT_EVENT__REQUIRED_HEEL:
				setRequiredHeel((EndHeelOptions)newValue);
				return;
			case CargoPackage.CHARTER_OUT_EVENT__AVAILABLE_HEEL:
				setAvailableHeel((StartHeelOptions)newValue);
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
			case CargoPackage.CHARTER_OUT_EVENT__RELOCATE_TO:
				unsetRelocateTo();
				return;
			case CargoPackage.CHARTER_OUT_EVENT__HIRE_RATE:
				setHireRate(HIRE_RATE_EDEFAULT);
				return;
			case CargoPackage.CHARTER_OUT_EVENT__BALLAST_BONUS:
				setBallastBonus(BALLAST_BONUS_EDEFAULT);
				return;
			case CargoPackage.CHARTER_OUT_EVENT__REPOSITIONING_FEE:
				setRepositioningFee(REPOSITIONING_FEE_EDEFAULT);
				return;
			case CargoPackage.CHARTER_OUT_EVENT__REQUIRED_HEEL:
				setRequiredHeel((EndHeelOptions)null);
				return;
			case CargoPackage.CHARTER_OUT_EVENT__AVAILABLE_HEEL:
				setAvailableHeel((StartHeelOptions)null);
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
			case CargoPackage.CHARTER_OUT_EVENT__RELOCATE_TO:
				return isSetRelocateTo();
			case CargoPackage.CHARTER_OUT_EVENT__HIRE_RATE:
				return hireRate != HIRE_RATE_EDEFAULT;
			case CargoPackage.CHARTER_OUT_EVENT__BALLAST_BONUS:
				return ballastBonus != BALLAST_BONUS_EDEFAULT;
			case CargoPackage.CHARTER_OUT_EVENT__REPOSITIONING_FEE:
				return repositioningFee != REPOSITIONING_FEE_EDEFAULT;
			case CargoPackage.CHARTER_OUT_EVENT__REQUIRED_HEEL:
				return requiredHeel != null;
			case CargoPackage.CHARTER_OUT_EVENT__AVAILABLE_HEEL:
				return availableHeel != null;
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
			case CargoPackage.CHARTER_OUT_EVENT___GET_END_PORT:
				return getEndPort();
		}
		return super.eInvoke(operationID, arguments);
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
		result.append(" (hireRate: ");
		result.append(hireRate);
		result.append(", ballastBonus: ");
		result.append(ballastBonus);
		result.append(", repositioningFee: ");
		result.append(repositioningFee);
		result.append(')');
		return result.toString();
	}

} //CharterOutEventImpl
