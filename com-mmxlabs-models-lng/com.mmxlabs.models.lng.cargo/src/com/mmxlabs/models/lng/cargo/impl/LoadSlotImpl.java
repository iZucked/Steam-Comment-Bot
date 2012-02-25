

/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package com.mmxlabs.models.lng.cargo.impl;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.LoadSlot;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Load Slot</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.LoadSlotImpl#getCargoCV <em>Cargo CV</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.LoadSlotImpl#isArriveCold <em>Arrive Cold</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class LoadSlotImpl extends SlotImpl implements LoadSlot {
	/**
	 * The default value of the '{@link #getCargoCV() <em>Cargo CV</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCargoCV()
	 * @generated
	 * @ordered
	 */
	protected static final double CARGO_CV_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getCargoCV() <em>Cargo CV</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCargoCV()
	 * @generated
	 * @ordered
	 */
	protected double cargoCV = CARGO_CV_EDEFAULT;

	/**
	 * This is true if the Cargo CV attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean cargoCVESet;

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
	public double getCargoCV() {
		return cargoCV;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setCargoCV(double newCargoCV) {
		double oldCargoCV = cargoCV;
		cargoCV = newCargoCV;
		boolean oldCargoCVESet = cargoCVESet;
		cargoCVESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.LOAD_SLOT__CARGO_CV, oldCargoCV, cargoCV, !oldCargoCVESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetCargoCV() {
		double oldCargoCV = cargoCV;
		boolean oldCargoCVESet = cargoCVESet;
		cargoCV = CARGO_CV_EDEFAULT;
		cargoCVESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, CargoPackage.LOAD_SLOT__CARGO_CV, oldCargoCV, CARGO_CV_EDEFAULT, oldCargoCVESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetCargoCV() {
		return cargoCVESet;
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
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case CargoPackage.LOAD_SLOT__CARGO_CV:
				return getCargoCV();
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
			case CargoPackage.LOAD_SLOT__CARGO_CV:
				setCargoCV((Double)newValue);
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
			case CargoPackage.LOAD_SLOT__CARGO_CV:
				unsetCargoCV();
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
			case CargoPackage.LOAD_SLOT__CARGO_CV:
				return isSetCargoCV();
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
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (cargoCV: ");
		if (cargoCVESet) result.append(cargoCV); else result.append("<unset>");
		result.append(", arriveCold: ");
		if (arriveColdESet) result.append(arriveCold); else result.append("<unset>");
		result.append(')');
		return result.toString();
	}

	@Override
	public Object getUnsetValue(EStructuralFeature feature) {
		if (feature == CargoPackage.Literals.SLOT__DURATION) {
			return getPort().getLoadDuration();
		}
		return super.getUnsetValue(feature);
	}

	
} // end of LoadSlotImpl

// finish type fixing
