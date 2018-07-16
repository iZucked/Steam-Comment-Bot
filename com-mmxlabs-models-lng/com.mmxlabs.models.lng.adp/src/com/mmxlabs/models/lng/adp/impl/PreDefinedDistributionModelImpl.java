/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.adp.impl;

import com.mmxlabs.models.lng.adp.ADPPackage;
import com.mmxlabs.models.lng.adp.LNGVolumeUnit;
import com.mmxlabs.models.lng.adp.PreDefinedDate;
import com.mmxlabs.models.lng.adp.PreDefinedDistributionModel;

import com.mmxlabs.models.lng.types.TimePeriod;
import java.time.LocalDate;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EDataTypeUniqueEList;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Pre Defined Distribution Model</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.adp.impl.PreDefinedDistributionModelImpl#getVolumePerCargo <em>Volume Per Cargo</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.impl.PreDefinedDistributionModelImpl#getVolumeUnit <em>Volume Unit</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.impl.PreDefinedDistributionModelImpl#getDates <em>Dates</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.impl.PreDefinedDistributionModelImpl#getWindowSize <em>Window Size</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.impl.PreDefinedDistributionModelImpl#getWindowSizeUnits <em>Window Size Units</em>}</li>
 * </ul>
 *
 * @generated
 */
public class PreDefinedDistributionModelImpl extends EObjectImpl implements PreDefinedDistributionModel {
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
	 * The cached value of the '{@link #getDates() <em>Dates</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDates()
	 * @generated
	 * @ordered
	 */
	protected EList<PreDefinedDate> dates;

	/**
	 * The default value of the '{@link #getWindowSize() <em>Window Size</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getWindowSize()
	 * @generated
	 * @ordered
	 */
	protected static final int WINDOW_SIZE_EDEFAULT = 1;
	/**
	 * The cached value of the '{@link #getWindowSize() <em>Window Size</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getWindowSize()
	 * @generated
	 * @ordered
	 */
	protected int windowSize = WINDOW_SIZE_EDEFAULT;
	/**
	 * The default value of the '{@link #getWindowSizeUnits() <em>Window Size Units</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getWindowSizeUnits()
	 * @generated
	 * @ordered
	 */
	protected static final TimePeriod WINDOW_SIZE_UNITS_EDEFAULT = TimePeriod.MONTHS;
	/**
	 * The cached value of the '{@link #getWindowSizeUnits() <em>Window Size Units</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getWindowSizeUnits()
	 * @generated
	 * @ordered
	 */
	protected TimePeriod windowSizeUnits = WINDOW_SIZE_UNITS_EDEFAULT;
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected PreDefinedDistributionModelImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ADPPackage.Literals.PRE_DEFINED_DISTRIBUTION_MODEL;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<PreDefinedDate> getDates() {
		if (dates == null) {
			dates = new EObjectContainmentEList.Resolving<PreDefinedDate>(PreDefinedDate.class, this, ADPPackage.PRE_DEFINED_DISTRIBUTION_MODEL__DATES);
		}
		return dates;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getWindowSize() {
		return windowSize;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setWindowSize(int newWindowSize) {
		int oldWindowSize = windowSize;
		windowSize = newWindowSize;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ADPPackage.PRE_DEFINED_DISTRIBUTION_MODEL__WINDOW_SIZE, oldWindowSize, windowSize));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public TimePeriod getWindowSizeUnits() {
		return windowSizeUnits;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setWindowSizeUnits(TimePeriod newWindowSizeUnits) {
		TimePeriod oldWindowSizeUnits = windowSizeUnits;
		windowSizeUnits = newWindowSizeUnits == null ? WINDOW_SIZE_UNITS_EDEFAULT : newWindowSizeUnits;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ADPPackage.PRE_DEFINED_DISTRIBUTION_MODEL__WINDOW_SIZE_UNITS, oldWindowSizeUnits, windowSizeUnits));
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
			eNotify(new ENotificationImpl(this, Notification.SET, ADPPackage.PRE_DEFINED_DISTRIBUTION_MODEL__VOLUME_PER_CARGO, oldVolumePerCargo, volumePerCargo));
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
			eNotify(new ENotificationImpl(this, Notification.SET, ADPPackage.PRE_DEFINED_DISTRIBUTION_MODEL__VOLUME_UNIT, oldVolumeUnit, volumeUnit));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case ADPPackage.PRE_DEFINED_DISTRIBUTION_MODEL__DATES:
				return ((InternalEList<?>)getDates()).basicRemove(otherEnd, msgs);
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
			case ADPPackage.PRE_DEFINED_DISTRIBUTION_MODEL__VOLUME_PER_CARGO:
				return getVolumePerCargo();
			case ADPPackage.PRE_DEFINED_DISTRIBUTION_MODEL__VOLUME_UNIT:
				return getVolumeUnit();
			case ADPPackage.PRE_DEFINED_DISTRIBUTION_MODEL__DATES:
				return getDates();
			case ADPPackage.PRE_DEFINED_DISTRIBUTION_MODEL__WINDOW_SIZE:
				return getWindowSize();
			case ADPPackage.PRE_DEFINED_DISTRIBUTION_MODEL__WINDOW_SIZE_UNITS:
				return getWindowSizeUnits();
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
			case ADPPackage.PRE_DEFINED_DISTRIBUTION_MODEL__VOLUME_PER_CARGO:
				setVolumePerCargo((Double)newValue);
				return;
			case ADPPackage.PRE_DEFINED_DISTRIBUTION_MODEL__VOLUME_UNIT:
				setVolumeUnit((LNGVolumeUnit)newValue);
				return;
			case ADPPackage.PRE_DEFINED_DISTRIBUTION_MODEL__DATES:
				getDates().clear();
				getDates().addAll((Collection<? extends PreDefinedDate>)newValue);
				return;
			case ADPPackage.PRE_DEFINED_DISTRIBUTION_MODEL__WINDOW_SIZE:
				setWindowSize((Integer)newValue);
				return;
			case ADPPackage.PRE_DEFINED_DISTRIBUTION_MODEL__WINDOW_SIZE_UNITS:
				setWindowSizeUnits((TimePeriod)newValue);
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
			case ADPPackage.PRE_DEFINED_DISTRIBUTION_MODEL__VOLUME_PER_CARGO:
				setVolumePerCargo(VOLUME_PER_CARGO_EDEFAULT);
				return;
			case ADPPackage.PRE_DEFINED_DISTRIBUTION_MODEL__VOLUME_UNIT:
				setVolumeUnit(VOLUME_UNIT_EDEFAULT);
				return;
			case ADPPackage.PRE_DEFINED_DISTRIBUTION_MODEL__DATES:
				getDates().clear();
				return;
			case ADPPackage.PRE_DEFINED_DISTRIBUTION_MODEL__WINDOW_SIZE:
				setWindowSize(WINDOW_SIZE_EDEFAULT);
				return;
			case ADPPackage.PRE_DEFINED_DISTRIBUTION_MODEL__WINDOW_SIZE_UNITS:
				setWindowSizeUnits(WINDOW_SIZE_UNITS_EDEFAULT);
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
			case ADPPackage.PRE_DEFINED_DISTRIBUTION_MODEL__VOLUME_PER_CARGO:
				return volumePerCargo != VOLUME_PER_CARGO_EDEFAULT;
			case ADPPackage.PRE_DEFINED_DISTRIBUTION_MODEL__VOLUME_UNIT:
				return volumeUnit != VOLUME_UNIT_EDEFAULT;
			case ADPPackage.PRE_DEFINED_DISTRIBUTION_MODEL__DATES:
				return dates != null && !dates.isEmpty();
			case ADPPackage.PRE_DEFINED_DISTRIBUTION_MODEL__WINDOW_SIZE:
				return windowSize != WINDOW_SIZE_EDEFAULT;
			case ADPPackage.PRE_DEFINED_DISTRIBUTION_MODEL__WINDOW_SIZE_UNITS:
				return windowSizeUnits != WINDOW_SIZE_UNITS_EDEFAULT;
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
		result.append(", windowSize: ");
		result.append(windowSize);
		result.append(", windowSizeUnits: ");
		result.append(windowSizeUnits);
		result.append(')');
		return result.toString();
	}

} //PreDefinedDistributionModelImpl
