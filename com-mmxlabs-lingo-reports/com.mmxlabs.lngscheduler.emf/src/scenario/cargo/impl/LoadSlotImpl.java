/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package scenario.cargo.impl;


import java.lang.reflect.InvocationTargetException;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

import scenario.cargo.CargoPackage;
import scenario.cargo.LoadSlot;
import scenario.cargo.Slot;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Load Slot</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link scenario.cargo.impl.LoadSlotImpl#getCargoCVvalue <em>Cargo CVvalue</em>}</li>
 *   <li>{@link scenario.cargo.impl.LoadSlotImpl#isArriveCold <em>Arrive Cold</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class LoadSlotImpl extends SlotImpl implements LoadSlot {
	/**
	 * The default value of the '{@link #getCargoCVvalue() <em>Cargo CVvalue</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCargoCVvalue()
	 * @generated
	 * @ordered
	 */
	protected static final float CARGO_CVVALUE_EDEFAULT = 0.0F;

	/**
	 * The cached value of the '{@link #getCargoCVvalue() <em>Cargo CVvalue</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCargoCVvalue()
	 * @generated
	 * @ordered
	 */
	protected float cargoCVvalue = CARGO_CVVALUE_EDEFAULT;

	/**
	 * This is true if the Cargo CVvalue attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean cargoCVvalueESet;

	/**
	 * The default value of the '{@link #isArriveCold() <em>Arrive Cold</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isArriveCold()
	 * @generated
	 * @ordered
	 */
	protected static final boolean ARRIVE_COLD_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isArriveCold() <em>Arrive Cold</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isArriveCold()
	 * @generated
	 * @ordered
	 */
	protected boolean arriveCold = ARRIVE_COLD_EDEFAULT;

	/**
	 * This is true if the Arrive Cold attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean arriveColdESet;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected LoadSlotImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return CargoPackage.Literals.LOAD_SLOT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public float getCargoCVvalue() {
		return cargoCVvalue;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setCargoCVvalue(float newCargoCVvalue) {
		float oldCargoCVvalue = cargoCVvalue;
		cargoCVvalue = newCargoCVvalue;
		boolean oldCargoCVvalueESet = cargoCVvalueESet;
		cargoCVvalueESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.LOAD_SLOT__CARGO_CVVALUE, oldCargoCVvalue, cargoCVvalue, !oldCargoCVvalueESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetCargoCVvalue() {
		float oldCargoCVvalue = cargoCVvalue;
		boolean oldCargoCVvalueESet = cargoCVvalueESet;
		cargoCVvalue = CARGO_CVVALUE_EDEFAULT;
		cargoCVvalueESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, CargoPackage.LOAD_SLOT__CARGO_CVVALUE, oldCargoCVvalue, CARGO_CVVALUE_EDEFAULT, oldCargoCVvalueESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetCargoCVvalue() {
		return cargoCVvalueESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isArriveCold() {
		return arriveCold;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setArriveCold(boolean newArriveCold) {
		boolean oldArriveCold = arriveCold;
		arriveCold = newArriveCold;
		boolean oldArriveColdESet = arriveColdESet;
		arriveColdESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.LOAD_SLOT__ARRIVE_COLD, oldArriveCold, arriveCold, !oldArriveColdESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetArriveCold() {
		boolean oldArriveCold = arriveCold;
		boolean oldArriveColdESet = arriveColdESet;
		arriveCold = ARRIVE_COLD_EDEFAULT;
		arriveColdESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, CargoPackage.LOAD_SLOT__ARRIVE_COLD, oldArriveCold, ARRIVE_COLD_EDEFAULT, oldArriveColdESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetArriveCold() {
		return arriveColdESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public float getCargoOrPortCVValue() {
		if (isSetCargoCVvalue()) 
			return getCargoCVvalue();
		else if (getPort()!=null)
			return getPort().getDefaultCVvalue();
		else
			return 0;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getSlotOrPortDuration() {
		if (isSetSlotDuration())
			return getSlotDuration();
		else
			return getPort().getDefaultLoadDuration();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case CargoPackage.LOAD_SLOT__CARGO_CVVALUE:
				return getCargoCVvalue();
			case CargoPackage.LOAD_SLOT__ARRIVE_COLD:
				return isArriveCold();
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
			case CargoPackage.LOAD_SLOT__CARGO_CVVALUE:
				setCargoCVvalue((Float)newValue);
				return;
			case CargoPackage.LOAD_SLOT__ARRIVE_COLD:
				setArriveCold((Boolean)newValue);
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
			case CargoPackage.LOAD_SLOT__CARGO_CVVALUE:
				unsetCargoCVvalue();
				return;
			case CargoPackage.LOAD_SLOT__ARRIVE_COLD:
				unsetArriveCold();
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
			case CargoPackage.LOAD_SLOT__CARGO_CVVALUE:
				return isSetCargoCVvalue();
			case CargoPackage.LOAD_SLOT__ARRIVE_COLD:
				return isSetArriveCold();
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int eDerivedOperationID(int baseOperationID, Class<?> baseClass) {
		if (baseClass == Slot.class) {
			switch (baseOperationID) {
				case CargoPackage.SLOT___GET_SLOT_OR_PORT_DURATION: return CargoPackage.LOAD_SLOT___GET_SLOT_OR_PORT_DURATION;
				default: return super.eDerivedOperationID(baseOperationID, baseClass);
			}
		}
		return super.eDerivedOperationID(baseOperationID, baseClass);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eInvoke(int operationID, EList<?> arguments) throws InvocationTargetException {
		switch (operationID) {
			case CargoPackage.LOAD_SLOT___GET_CARGO_OR_PORT_CV_VALUE:
				return getCargoOrPortCVValue();
			case CargoPackage.LOAD_SLOT___GET_SLOT_OR_PORT_DURATION:
				return getSlotOrPortDuration();
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
		result.append(" (cargoCVvalue: ");
		if (cargoCVvalueESet) result.append(cargoCVvalue); else result.append("<unset>");
		result.append(", arriveCold: ");
		if (arriveColdESet) result.append(arriveCold); else result.append("<unset>");
		result.append(')');
		return result.toString();
	}

} //LoadSlotImpl