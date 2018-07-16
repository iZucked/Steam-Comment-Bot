/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.adp.impl;

import com.mmxlabs.models.lng.adp.ADPPackage;
import com.mmxlabs.models.lng.adp.CargoNumberDistributionModel;
import com.mmxlabs.models.lng.adp.LNGVolumeUnit;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Cargo Number Distribution Model</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.adp.impl.CargoNumberDistributionModelImpl#getVolumePerCargo <em>Volume Per Cargo</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.impl.CargoNumberDistributionModelImpl#getVolumeUnit <em>Volume Unit</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.impl.CargoNumberDistributionModelImpl#getNumberOfCargoes <em>Number Of Cargoes</em>}</li>
 * </ul>
 *
 * @generated
 */
public class CargoNumberDistributionModelImpl extends EObjectImpl implements CargoNumberDistributionModel {
	/**
	 * The default value of the '{@link #getVolumePerCargo() <em>Volume Per Cargo</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVolumePerCargo()
	 * @generated
	 * @ordered
	 */
	protected static final double VOLUME_PER_CARGO_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getVolumePerCargo() <em>Volume Per Cargo</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVolumePerCargo()
	 * @generated
	 * @ordered
	 */
	protected double volumePerCargo = VOLUME_PER_CARGO_EDEFAULT;

	/**
	 * The default value of the '{@link #getVolumeUnit() <em>Volume Unit</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVolumeUnit()
	 * @generated
	 * @ordered
	 */
	protected static final LNGVolumeUnit VOLUME_UNIT_EDEFAULT = LNGVolumeUnit.M3;

	/**
	 * The cached value of the '{@link #getVolumeUnit() <em>Volume Unit</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVolumeUnit()
	 * @generated
	 * @ordered
	 */
	protected LNGVolumeUnit volumeUnit = VOLUME_UNIT_EDEFAULT;

	/**
	 * The default value of the '{@link #getNumberOfCargoes() <em>Number Of Cargoes</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNumberOfCargoes()
	 * @generated
	 * @ordered
	 */
	protected static final int NUMBER_OF_CARGOES_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getNumberOfCargoes() <em>Number Of Cargoes</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNumberOfCargoes()
	 * @generated
	 * @ordered
	 */
	protected int numberOfCargoes = NUMBER_OF_CARGOES_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected CargoNumberDistributionModelImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ADPPackage.Literals.CARGO_NUMBER_DISTRIBUTION_MODEL;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getNumberOfCargoes() {
		return numberOfCargoes;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setNumberOfCargoes(int newNumberOfCargoes) {
		int oldNumberOfCargoes = numberOfCargoes;
		numberOfCargoes = newNumberOfCargoes;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ADPPackage.CARGO_NUMBER_DISTRIBUTION_MODEL__NUMBER_OF_CARGOES, oldNumberOfCargoes, numberOfCargoes));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public double getVolumePerCargo() {
		return volumePerCargo;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setVolumePerCargo(double newVolumePerCargo) {
		double oldVolumePerCargo = volumePerCargo;
		volumePerCargo = newVolumePerCargo;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ADPPackage.CARGO_NUMBER_DISTRIBUTION_MODEL__VOLUME_PER_CARGO, oldVolumePerCargo, volumePerCargo));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public LNGVolumeUnit getVolumeUnit() {
		return volumeUnit;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setVolumeUnit(LNGVolumeUnit newVolumeUnit) {
		LNGVolumeUnit oldVolumeUnit = volumeUnit;
		volumeUnit = newVolumeUnit == null ? VOLUME_UNIT_EDEFAULT : newVolumeUnit;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ADPPackage.CARGO_NUMBER_DISTRIBUTION_MODEL__VOLUME_UNIT, oldVolumeUnit, volumeUnit));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case ADPPackage.CARGO_NUMBER_DISTRIBUTION_MODEL__VOLUME_PER_CARGO:
				return getVolumePerCargo();
			case ADPPackage.CARGO_NUMBER_DISTRIBUTION_MODEL__VOLUME_UNIT:
				return getVolumeUnit();
			case ADPPackage.CARGO_NUMBER_DISTRIBUTION_MODEL__NUMBER_OF_CARGOES:
				return getNumberOfCargoes();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case ADPPackage.CARGO_NUMBER_DISTRIBUTION_MODEL__VOLUME_PER_CARGO:
				setVolumePerCargo((Double)newValue);
				return;
			case ADPPackage.CARGO_NUMBER_DISTRIBUTION_MODEL__VOLUME_UNIT:
				setVolumeUnit((LNGVolumeUnit)newValue);
				return;
			case ADPPackage.CARGO_NUMBER_DISTRIBUTION_MODEL__NUMBER_OF_CARGOES:
				setNumberOfCargoes((Integer)newValue);
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
			case ADPPackage.CARGO_NUMBER_DISTRIBUTION_MODEL__VOLUME_PER_CARGO:
				setVolumePerCargo(VOLUME_PER_CARGO_EDEFAULT);
				return;
			case ADPPackage.CARGO_NUMBER_DISTRIBUTION_MODEL__VOLUME_UNIT:
				setVolumeUnit(VOLUME_UNIT_EDEFAULT);
				return;
			case ADPPackage.CARGO_NUMBER_DISTRIBUTION_MODEL__NUMBER_OF_CARGOES:
				setNumberOfCargoes(NUMBER_OF_CARGOES_EDEFAULT);
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
			case ADPPackage.CARGO_NUMBER_DISTRIBUTION_MODEL__VOLUME_PER_CARGO:
				return volumePerCargo != VOLUME_PER_CARGO_EDEFAULT;
			case ADPPackage.CARGO_NUMBER_DISTRIBUTION_MODEL__VOLUME_UNIT:
				return volumeUnit != VOLUME_UNIT_EDEFAULT;
			case ADPPackage.CARGO_NUMBER_DISTRIBUTION_MODEL__NUMBER_OF_CARGOES:
				return numberOfCargoes != NUMBER_OF_CARGOES_EDEFAULT;
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

		StringBuilder result = new StringBuilder(super.toString());
		result.append(" (volumePerCargo: ");
		result.append(volumePerCargo);
		result.append(", volumeUnit: ");
		result.append(volumeUnit);
		result.append(", numberOfCargoes: ");
		result.append(numberOfCargoes);
		result.append(')');
		return result.toString();
	}

} //CargoNumberDistributionModelImpl
