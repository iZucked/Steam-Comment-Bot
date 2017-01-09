/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet.impl;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.fleet.HeelOptions;
import com.mmxlabs.models.mmxcore.impl.MMXObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Heel Options</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.HeelOptionsImpl#getVolumeAvailable <em>Volume Available</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.HeelOptionsImpl#getCvValue <em>Cv Value</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.HeelOptionsImpl#getPricePerMMBTU <em>Price Per MMBTU</em>}</li>
 * </ul>
 *
 * @generated
 */
public class HeelOptionsImpl extends MMXObjectImpl implements HeelOptions {
	/**
	 * The default value of the '{@link #getVolumeAvailable() <em>Volume Available</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVolumeAvailable()
	 * @generated
	 * @ordered
	 */
	protected static final double VOLUME_AVAILABLE_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getVolumeAvailable() <em>Volume Available</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVolumeAvailable()
	 * @generated
	 * @ordered
	 */
	protected double volumeAvailable = VOLUME_AVAILABLE_EDEFAULT;

	/**
	 * This is true if the Volume Available attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean volumeAvailableESet;

	/**
	 * The default value of the '{@link #getCvValue() <em>Cv Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCvValue()
	 * @generated
	 * @ordered
	 */
	protected static final double CV_VALUE_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getCvValue() <em>Cv Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCvValue()
	 * @generated
	 * @ordered
	 */
	protected double cvValue = CV_VALUE_EDEFAULT;

	/**
	 * The default value of the '{@link #getPricePerMMBTU() <em>Price Per MMBTU</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPricePerMMBTU()
	 * @generated
	 * @ordered
	 */
	protected static final double PRICE_PER_MMBTU_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getPricePerMMBTU() <em>Price Per MMBTU</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPricePerMMBTU()
	 * @generated
	 * @ordered
	 */
	protected double pricePerMMBTU = PRICE_PER_MMBTU_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected HeelOptionsImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return FleetPackage.Literals.HEEL_OPTIONS;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public double getVolumeAvailable() {
		return volumeAvailable;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setVolumeAvailable(double newVolumeAvailable) {
		double oldVolumeAvailable = volumeAvailable;
		volumeAvailable = newVolumeAvailable;
		boolean oldVolumeAvailableESet = volumeAvailableESet;
		volumeAvailableESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.HEEL_OPTIONS__VOLUME_AVAILABLE, oldVolumeAvailable, volumeAvailable, !oldVolumeAvailableESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetVolumeAvailable() {
		double oldVolumeAvailable = volumeAvailable;
		boolean oldVolumeAvailableESet = volumeAvailableESet;
		volumeAvailable = VOLUME_AVAILABLE_EDEFAULT;
		volumeAvailableESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, FleetPackage.HEEL_OPTIONS__VOLUME_AVAILABLE, oldVolumeAvailable, VOLUME_AVAILABLE_EDEFAULT, oldVolumeAvailableESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetVolumeAvailable() {
		return volumeAvailableESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public double getCvValue() {
		return cvValue;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setCvValue(double newCvValue) {
		double oldCvValue = cvValue;
		cvValue = newCvValue;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.HEEL_OPTIONS__CV_VALUE, oldCvValue, cvValue));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public double getPricePerMMBTU() {
		return pricePerMMBTU;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setPricePerMMBTU(double newPricePerMMBTU) {
		double oldPricePerMMBTU = pricePerMMBTU;
		pricePerMMBTU = newPricePerMMBTU;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.HEEL_OPTIONS__PRICE_PER_MMBTU, oldPricePerMMBTU, pricePerMMBTU));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case FleetPackage.HEEL_OPTIONS__VOLUME_AVAILABLE:
				return getVolumeAvailable();
			case FleetPackage.HEEL_OPTIONS__CV_VALUE:
				return getCvValue();
			case FleetPackage.HEEL_OPTIONS__PRICE_PER_MMBTU:
				return getPricePerMMBTU();
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
			case FleetPackage.HEEL_OPTIONS__VOLUME_AVAILABLE:
				setVolumeAvailable((Double)newValue);
				return;
			case FleetPackage.HEEL_OPTIONS__CV_VALUE:
				setCvValue((Double)newValue);
				return;
			case FleetPackage.HEEL_OPTIONS__PRICE_PER_MMBTU:
				setPricePerMMBTU((Double)newValue);
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
			case FleetPackage.HEEL_OPTIONS__VOLUME_AVAILABLE:
				unsetVolumeAvailable();
				return;
			case FleetPackage.HEEL_OPTIONS__CV_VALUE:
				setCvValue(CV_VALUE_EDEFAULT);
				return;
			case FleetPackage.HEEL_OPTIONS__PRICE_PER_MMBTU:
				setPricePerMMBTU(PRICE_PER_MMBTU_EDEFAULT);
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
			case FleetPackage.HEEL_OPTIONS__VOLUME_AVAILABLE:
				return isSetVolumeAvailable();
			case FleetPackage.HEEL_OPTIONS__CV_VALUE:
				return cvValue != CV_VALUE_EDEFAULT;
			case FleetPackage.HEEL_OPTIONS__PRICE_PER_MMBTU:
				return pricePerMMBTU != PRICE_PER_MMBTU_EDEFAULT;
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
		result.append(" (volumeAvailable: ");
		if (volumeAvailableESet) result.append(volumeAvailable); else result.append("<unset>");
		result.append(", cvValue: ");
		result.append(cvValue);
		result.append(", pricePerMMBTU: ");
		result.append(pricePerMMBTU);
		result.append(')');
		return result.toString();
	}

} // end of HeelOptionsImpl

// finish type fixing
