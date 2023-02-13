/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.adp.impl;

import com.mmxlabs.models.lng.adp.ADPPackage;
import com.mmxlabs.models.lng.adp.ContractProfile;
import com.mmxlabs.models.lng.adp.ProfileConstraint;
import com.mmxlabs.models.lng.adp.LNGVolumeUnit;
import com.mmxlabs.models.lng.adp.SubContractProfile;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.commercial.Contract;

import java.util.Collection;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Contract Profile</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.adp.impl.ContractProfileImpl#getContract <em>Contract</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.impl.ContractProfileImpl#getContractCode <em>Contract Code</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.impl.ContractProfileImpl#isCustom <em>Custom</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.impl.ContractProfileImpl#isEnabled <em>Enabled</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.impl.ContractProfileImpl#getTotalVolume <em>Total Volume</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.impl.ContractProfileImpl#getVolumeUnit <em>Volume Unit</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.impl.ContractProfileImpl#getSubProfiles <em>Sub Profiles</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.impl.ContractProfileImpl#getConstraints <em>Constraints</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ContractProfileImpl<T extends Slot<U>, U extends Contract> extends EObjectImpl implements ContractProfile<T, U> {
	/**
	 * The cached value of the '{@link #getContract() <em>Contract</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getContract()
	 * @generated
	 * @ordered
	 */
	protected U contract;

	/**
	 * The default value of the '{@link #getContractCode() <em>Contract Code</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getContractCode()
	 * @generated
	 * @ordered
	 */
	protected static final String CONTRACT_CODE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getContractCode() <em>Contract Code</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getContractCode()
	 * @generated
	 * @ordered
	 */
	protected String contractCode = CONTRACT_CODE_EDEFAULT;

	/**
	 * The default value of the '{@link #isCustom() <em>Custom</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isCustom()
	 * @generated
	 * @ordered
	 */
	protected static final boolean CUSTOM_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isCustom() <em>Custom</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isCustom()
	 * @generated
	 * @ordered
	 */
	protected boolean custom = CUSTOM_EDEFAULT;

	/**
	 * The default value of the '{@link #isEnabled() <em>Enabled</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isEnabled()
	 * @generated
	 * @ordered
	 */
	protected static final boolean ENABLED_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isEnabled() <em>Enabled</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isEnabled()
	 * @generated
	 * @ordered
	 */
	protected boolean enabled = ENABLED_EDEFAULT;

	/**
	 * The default value of the '{@link #getTotalVolume() <em>Total Volume</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTotalVolume()
	 * @generated
	 * @ordered
	 */
	protected static final double TOTAL_VOLUME_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getTotalVolume() <em>Total Volume</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTotalVolume()
	 * @generated
	 * @ordered
	 */
	protected double totalVolume = TOTAL_VOLUME_EDEFAULT;

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
	 * The cached value of the '{@link #getSubProfiles() <em>Sub Profiles</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSubProfiles()
	 * @generated
	 * @ordered
	 */
	protected EList<SubContractProfile<T, U>> subProfiles;

	/**
	 * The cached value of the '{@link #getConstraints() <em>Constraints</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getConstraints()
	 * @generated
	 * @ordered
	 */
	protected EList<ProfileConstraint> constraints;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ContractProfileImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ADPPackage.Literals.CONTRACT_PROFILE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public U getContract() {
		if (contract != null && contract.eIsProxy()) {
			InternalEObject oldContract = (InternalEObject)contract;
			contract = (U)eResolveProxy(oldContract);
			if (contract != oldContract) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ADPPackage.CONTRACT_PROFILE__CONTRACT, oldContract, contract));
			}
		}
		return contract;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public U basicGetContract() {
		return contract;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setContract(U newContract) {
		U oldContract = contract;
		contract = newContract;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ADPPackage.CONTRACT_PROFILE__CONTRACT, oldContract, contract));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getContractCode() {
		return contractCode;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setContractCode(String newContractCode) {
		String oldContractCode = contractCode;
		contractCode = newContractCode;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ADPPackage.CONTRACT_PROFILE__CONTRACT_CODE, oldContractCode, contractCode));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isCustom() {
		return custom;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setCustom(boolean newCustom) {
		boolean oldCustom = custom;
		custom = newCustom;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ADPPackage.CONTRACT_PROFILE__CUSTOM, oldCustom, custom));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setEnabled(boolean newEnabled) {
		boolean oldEnabled = enabled;
		enabled = newEnabled;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ADPPackage.CONTRACT_PROFILE__ENABLED, oldEnabled, enabled));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public double getTotalVolume() {
		return totalVolume;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setTotalVolume(double newTotalVolume) {
		double oldTotalVolume = totalVolume;
		totalVolume = newTotalVolume;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ADPPackage.CONTRACT_PROFILE__TOTAL_VOLUME, oldTotalVolume, totalVolume));
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
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ADPPackage.CONTRACT_PROFILE__VOLUME_UNIT, oldVolumeUnit, volumeUnit));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<SubContractProfile<T, U>> getSubProfiles() {
		if (subProfiles == null) {
			subProfiles = new EObjectContainmentEList.Resolving<SubContractProfile<T, U>>(SubContractProfile.class, this, ADPPackage.CONTRACT_PROFILE__SUB_PROFILES);
		}
		return subProfiles;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<ProfileConstraint> getConstraints() {
		if (constraints == null) {
			constraints = new EObjectContainmentEList.Resolving<ProfileConstraint>(ProfileConstraint.class, this, ADPPackage.CONTRACT_PROFILE__CONSTRAINTS);
		}
		return constraints;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case ADPPackage.CONTRACT_PROFILE__SUB_PROFILES:
				return ((InternalEList<?>)getSubProfiles()).basicRemove(otherEnd, msgs);
			case ADPPackage.CONTRACT_PROFILE__CONSTRAINTS:
				return ((InternalEList<?>)getConstraints()).basicRemove(otherEnd, msgs);
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
			case ADPPackage.CONTRACT_PROFILE__CONTRACT:
				if (resolve) return getContract();
				return basicGetContract();
			case ADPPackage.CONTRACT_PROFILE__CONTRACT_CODE:
				return getContractCode();
			case ADPPackage.CONTRACT_PROFILE__CUSTOM:
				return isCustom();
			case ADPPackage.CONTRACT_PROFILE__ENABLED:
				return isEnabled();
			case ADPPackage.CONTRACT_PROFILE__TOTAL_VOLUME:
				return getTotalVolume();
			case ADPPackage.CONTRACT_PROFILE__VOLUME_UNIT:
				return getVolumeUnit();
			case ADPPackage.CONTRACT_PROFILE__SUB_PROFILES:
				return getSubProfiles();
			case ADPPackage.CONTRACT_PROFILE__CONSTRAINTS:
				return getConstraints();
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
			case ADPPackage.CONTRACT_PROFILE__CONTRACT:
				setContract((U)newValue);
				return;
			case ADPPackage.CONTRACT_PROFILE__CONTRACT_CODE:
				setContractCode((String)newValue);
				return;
			case ADPPackage.CONTRACT_PROFILE__CUSTOM:
				setCustom((Boolean)newValue);
				return;
			case ADPPackage.CONTRACT_PROFILE__ENABLED:
				setEnabled((Boolean)newValue);
				return;
			case ADPPackage.CONTRACT_PROFILE__TOTAL_VOLUME:
				setTotalVolume((Double)newValue);
				return;
			case ADPPackage.CONTRACT_PROFILE__VOLUME_UNIT:
				setVolumeUnit((LNGVolumeUnit)newValue);
				return;
			case ADPPackage.CONTRACT_PROFILE__SUB_PROFILES:
				getSubProfiles().clear();
				getSubProfiles().addAll((Collection<? extends SubContractProfile<T, U>>)newValue);
				return;
			case ADPPackage.CONTRACT_PROFILE__CONSTRAINTS:
				getConstraints().clear();
				getConstraints().addAll((Collection<? extends ProfileConstraint>)newValue);
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
			case ADPPackage.CONTRACT_PROFILE__CONTRACT:
				setContract((U)null);
				return;
			case ADPPackage.CONTRACT_PROFILE__CONTRACT_CODE:
				setContractCode(CONTRACT_CODE_EDEFAULT);
				return;
			case ADPPackage.CONTRACT_PROFILE__CUSTOM:
				setCustom(CUSTOM_EDEFAULT);
				return;
			case ADPPackage.CONTRACT_PROFILE__ENABLED:
				setEnabled(ENABLED_EDEFAULT);
				return;
			case ADPPackage.CONTRACT_PROFILE__TOTAL_VOLUME:
				setTotalVolume(TOTAL_VOLUME_EDEFAULT);
				return;
			case ADPPackage.CONTRACT_PROFILE__VOLUME_UNIT:
				setVolumeUnit(VOLUME_UNIT_EDEFAULT);
				return;
			case ADPPackage.CONTRACT_PROFILE__SUB_PROFILES:
				getSubProfiles().clear();
				return;
			case ADPPackage.CONTRACT_PROFILE__CONSTRAINTS:
				getConstraints().clear();
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
			case ADPPackage.CONTRACT_PROFILE__CONTRACT:
				return contract != null;
			case ADPPackage.CONTRACT_PROFILE__CONTRACT_CODE:
				return CONTRACT_CODE_EDEFAULT == null ? contractCode != null : !CONTRACT_CODE_EDEFAULT.equals(contractCode);
			case ADPPackage.CONTRACT_PROFILE__CUSTOM:
				return custom != CUSTOM_EDEFAULT;
			case ADPPackage.CONTRACT_PROFILE__ENABLED:
				return enabled != ENABLED_EDEFAULT;
			case ADPPackage.CONTRACT_PROFILE__TOTAL_VOLUME:
				return totalVolume != TOTAL_VOLUME_EDEFAULT;
			case ADPPackage.CONTRACT_PROFILE__VOLUME_UNIT:
				return volumeUnit != VOLUME_UNIT_EDEFAULT;
			case ADPPackage.CONTRACT_PROFILE__SUB_PROFILES:
				return subProfiles != null && !subProfiles.isEmpty();
			case ADPPackage.CONTRACT_PROFILE__CONSTRAINTS:
				return constraints != null && !constraints.isEmpty();
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
		result.append(" (contractCode: ");
		result.append(contractCode);
		result.append(", custom: ");
		result.append(custom);
		result.append(", enabled: ");
		result.append(enabled);
		result.append(", totalVolume: ");
		result.append(totalVolume);
		result.append(", volumeUnit: ");
		result.append(volumeUnit);
		result.append(')');
		return result.toString();
	}

} //ContractProfileImpl
