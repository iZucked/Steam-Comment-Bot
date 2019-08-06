/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.adp.impl;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

import com.mmxlabs.models.lng.adp.ADPPackage;
import com.mmxlabs.models.lng.adp.ContractProfile;
import com.mmxlabs.models.lng.adp.DistributionModel;
import com.mmxlabs.models.lng.adp.LNGVolumeUnit;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.types.VolumeUnits;
import com.mmxlabs.models.mmxcore.impl.MMXObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Distribution Model</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.adp.impl.DistributionModelImpl#getVolumePerCargo <em>Volume Per Cargo</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.impl.DistributionModelImpl#getVolumeUnit <em>Volume Unit</em>}</li>
 * </ul>
 *
 * @generated
 */
public abstract class DistributionModelImpl extends MMXObjectImpl implements DistributionModel {
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
	 * This is true if the Volume Per Cargo attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean volumePerCargoESet;

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
	 * This is true if the Volume Unit attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean volumeUnitESet;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected DistributionModelImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ADPPackage.Literals.DISTRIBUTION_MODEL;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public double getVolumePerCargo() {
		return volumePerCargo;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setVolumePerCargo(double newVolumePerCargo) {
		double oldVolumePerCargo = volumePerCargo;
		volumePerCargo = newVolumePerCargo;
		boolean oldVolumePerCargoESet = volumePerCargoESet;
		volumePerCargoESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ADPPackage.DISTRIBUTION_MODEL__VOLUME_PER_CARGO, oldVolumePerCargo, volumePerCargo, !oldVolumePerCargoESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void unsetVolumePerCargo() {
		double oldVolumePerCargo = volumePerCargo;
		boolean oldVolumePerCargoESet = volumePerCargoESet;
		volumePerCargo = VOLUME_PER_CARGO_EDEFAULT;
		volumePerCargoESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, ADPPackage.DISTRIBUTION_MODEL__VOLUME_PER_CARGO, oldVolumePerCargo, VOLUME_PER_CARGO_EDEFAULT, oldVolumePerCargoESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isSetVolumePerCargo() {
		return volumePerCargoESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public LNGVolumeUnit getVolumeUnit() {
		return volumeUnit;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setVolumeUnit(LNGVolumeUnit newVolumeUnit) {
		LNGVolumeUnit oldVolumeUnit = volumeUnit;
		volumeUnit = newVolumeUnit == null ? VOLUME_UNIT_EDEFAULT : newVolumeUnit;
		boolean oldVolumeUnitESet = volumeUnitESet;
		volumeUnitESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ADPPackage.DISTRIBUTION_MODEL__VOLUME_UNIT, oldVolumeUnit, volumeUnit, !oldVolumeUnitESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void unsetVolumeUnit() {
		LNGVolumeUnit oldVolumeUnit = volumeUnit;
		boolean oldVolumeUnitESet = volumeUnitESet;
		volumeUnit = VOLUME_UNIT_EDEFAULT;
		volumeUnitESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, ADPPackage.DISTRIBUTION_MODEL__VOLUME_UNIT, oldVolumeUnit, VOLUME_UNIT_EDEFAULT, oldVolumeUnitESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isSetVolumeUnit() {
		return volumeUnitESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public double getModelOrContractVolumePerCargo() {
		return (Double) eGetWithDefault(ADPPackage.Literals.DISTRIBUTION_MODEL__VOLUME_PER_CARGO);

	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public LNGVolumeUnit getModelOrContractVolumeUnit() {
		return (LNGVolumeUnit) eGetWithDefault(ADPPackage.Literals.DISTRIBUTION_MODEL__VOLUME_UNIT);

	}
	
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case ADPPackage.DISTRIBUTION_MODEL__VOLUME_PER_CARGO:
				return getVolumePerCargo();
			case ADPPackage.DISTRIBUTION_MODEL__VOLUME_UNIT:
				return getVolumeUnit();
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
			case ADPPackage.DISTRIBUTION_MODEL__VOLUME_PER_CARGO:
				setVolumePerCargo((Double)newValue);
				return;
			case ADPPackage.DISTRIBUTION_MODEL__VOLUME_UNIT:
				setVolumeUnit((LNGVolumeUnit)newValue);
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
			case ADPPackage.DISTRIBUTION_MODEL__VOLUME_PER_CARGO:
				unsetVolumePerCargo();
				return;
			case ADPPackage.DISTRIBUTION_MODEL__VOLUME_UNIT:
				unsetVolumeUnit();
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
			case ADPPackage.DISTRIBUTION_MODEL__VOLUME_PER_CARGO:
				return isSetVolumePerCargo();
			case ADPPackage.DISTRIBUTION_MODEL__VOLUME_UNIT:
				return isSetVolumeUnit();
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
			case ADPPackage.DISTRIBUTION_MODEL___GET_MODEL_OR_CONTRACT_VOLUME_PER_CARGO:
				return getModelOrContractVolumePerCargo();
			case ADPPackage.DISTRIBUTION_MODEL___GET_MODEL_OR_CONTRACT_VOLUME_UNIT:
				return getModelOrContractVolumeUnit();
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

		StringBuilder result = new StringBuilder(super.toString());
		result.append(" (volumePerCargo: ");
		if (volumePerCargoESet) result.append(volumePerCargo); else result.append("<unset>");
		result.append(", volumeUnit: ");
		if (volumeUnitESet) result.append(volumeUnit); else result.append("<unset>");
		result.append(')');
		return result.toString();
	}
	
	@Override
	public DelegateInformation getUnsetValueOrDelegate(EStructuralFeature feature) {
		 if (ADPPackage.eINSTANCE.getDistributionModel_VolumePerCargo() == feature) {
			final Contract c = getContract();
			if (c != null) {
				return new DelegateInformation(null, null, (double) c.getMaxQuantity());
			}
		} else if (ADPPackage.Literals.DISTRIBUTION_MODEL__VOLUME_UNIT == feature) {
			Contract c = getContract();
			if (c != null) {
				return new DelegateInformation(null, null, mapVolumeUnits(c.getVolumeLimitsUnit()));
			}
		} 
		return super.getUnsetValueOrDelegate(feature);
	}
	
	protected Contract getContract() {
		
		EObject p = eContainer();
		while (p != null) {
			if (p instanceof ContractProfile<?,?>) {
				ContractProfile<?,?> contractProfile = (ContractProfile<?,?>) p;
				return contractProfile.getContract();
			}
			p = p.eContainer();
		}
		return null;
	}
	
	protected LNGVolumeUnit mapVolumeUnits(VolumeUnits units) {
		switch (units) {
		case M3:
			return LNGVolumeUnit.M3;
		case MMBTU:
			return LNGVolumeUnit.MMBTU;
		default:
			throw new IllegalArgumentException();
		}
	}


} //DistributionModelImpl
