/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.adp.impl;

import com.mmxlabs.models.lng.adp.ADPPackage;
import com.mmxlabs.models.lng.adp.PurchaseContractProfile;
import com.mmxlabs.models.lng.adp.SubContractProfile;
import com.mmxlabs.models.lng.adp.SupplyFromProfileFlow;

import com.mmxlabs.models.lng.cargo.LoadSlot;

import com.mmxlabs.models.lng.commercial.PurchaseContract;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Supply From Profile Flow</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.adp.impl.SupplyFromProfileFlowImpl#getProfile <em>Profile</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.impl.SupplyFromProfileFlowImpl#getSubProfile <em>Sub Profile</em>}</li>
 * </ul>
 *
 * @generated
 */
public class SupplyFromProfileFlowImpl extends SupplyFromFlowImpl implements SupplyFromProfileFlow {
	/**
	 * The cached value of the '{@link #getProfile() <em>Profile</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getProfile()
	 * @generated
	 * @ordered
	 */
	protected PurchaseContractProfile profile;

	/**
	 * The cached value of the '{@link #getSubProfile() <em>Sub Profile</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSubProfile()
	 * @generated
	 * @ordered
	 */
	protected SubContractProfile<LoadSlot, PurchaseContract> subProfile;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected SupplyFromProfileFlowImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ADPPackage.Literals.SUPPLY_FROM_PROFILE_FLOW;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public PurchaseContractProfile getProfile() {
		if (profile != null && profile.eIsProxy()) {
			InternalEObject oldProfile = (InternalEObject)profile;
			profile = (PurchaseContractProfile)eResolveProxy(oldProfile);
			if (profile != oldProfile) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ADPPackage.SUPPLY_FROM_PROFILE_FLOW__PROFILE, oldProfile, profile));
			}
		}
		return profile;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public PurchaseContractProfile basicGetProfile() {
		return profile;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setProfile(PurchaseContractProfile newProfile) {
		PurchaseContractProfile oldProfile = profile;
		profile = newProfile;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ADPPackage.SUPPLY_FROM_PROFILE_FLOW__PROFILE, oldProfile, profile));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public SubContractProfile<LoadSlot, PurchaseContract> getSubProfile() {
		if (subProfile != null && subProfile.eIsProxy()) {
			InternalEObject oldSubProfile = (InternalEObject)subProfile;
			subProfile = (SubContractProfile<LoadSlot, PurchaseContract>)eResolveProxy(oldSubProfile);
			if (subProfile != oldSubProfile) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ADPPackage.SUPPLY_FROM_PROFILE_FLOW__SUB_PROFILE, oldSubProfile, subProfile));
			}
		}
		return subProfile;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SubContractProfile<LoadSlot, PurchaseContract> basicGetSubProfile() {
		return subProfile;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setSubProfile(SubContractProfile<LoadSlot, PurchaseContract> newSubProfile) {
		SubContractProfile<LoadSlot, PurchaseContract> oldSubProfile = subProfile;
		subProfile = newSubProfile;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ADPPackage.SUPPLY_FROM_PROFILE_FLOW__SUB_PROFILE, oldSubProfile, subProfile));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case ADPPackage.SUPPLY_FROM_PROFILE_FLOW__PROFILE:
				if (resolve) return getProfile();
				return basicGetProfile();
			case ADPPackage.SUPPLY_FROM_PROFILE_FLOW__SUB_PROFILE:
				if (resolve) return getSubProfile();
				return basicGetSubProfile();
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
			case ADPPackage.SUPPLY_FROM_PROFILE_FLOW__PROFILE:
				setProfile((PurchaseContractProfile)newValue);
				return;
			case ADPPackage.SUPPLY_FROM_PROFILE_FLOW__SUB_PROFILE:
				setSubProfile((SubContractProfile<LoadSlot, PurchaseContract>)newValue);
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
			case ADPPackage.SUPPLY_FROM_PROFILE_FLOW__PROFILE:
				setProfile((PurchaseContractProfile)null);
				return;
			case ADPPackage.SUPPLY_FROM_PROFILE_FLOW__SUB_PROFILE:
				setSubProfile((SubContractProfile<LoadSlot, PurchaseContract>)null);
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
			case ADPPackage.SUPPLY_FROM_PROFILE_FLOW__PROFILE:
				return profile != null;
			case ADPPackage.SUPPLY_FROM_PROFILE_FLOW__SUB_PROFILE:
				return subProfile != null;
		}
		return super.eIsSet(featureID);
	}

} //SupplyFromProfileFlowImpl
