/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package scenario.cargo.impl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import scenario.cargo.CargoPackage;
import scenario.cargo.LoadSlot;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Load Slot</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link scenario.cargo.impl.LoadSlotImpl#getCargoCVvalue <em>Cargo CVvalue</em>}</li>
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
	protected static final int CARGO_CVVALUE_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getCargoCVvalue() <em>Cargo CVvalue</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCargoCVvalue()
	 * @generated
	 * @ordered
	 */
	protected int cargoCVvalue = CARGO_CVVALUE_EDEFAULT;

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
	public int getCargoCVvalue() {
		return cargoCVvalue;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setCargoCVvalue(int newCargoCVvalue) {
		int oldCargoCVvalue = cargoCVvalue;
		cargoCVvalue = newCargoCVvalue;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.LOAD_SLOT__CARGO_CVVALUE, oldCargoCVvalue, cargoCVvalue));
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
				setCargoCVvalue((Integer)newValue);
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
				setCargoCVvalue(CARGO_CVVALUE_EDEFAULT);
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
				return cargoCVvalue != CARGO_CVVALUE_EDEFAULT;
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
		result.append(" (cargoCVvalue: ");
		result.append(cargoCVvalue);
		result.append(')');
		return result.toString();
	}

} //LoadSlotImpl
