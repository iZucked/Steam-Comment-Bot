/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.adp.impl;

import com.mmxlabs.models.lng.adp.ADPPackage;
import com.mmxlabs.models.lng.adp.DeliverToProfileFlow;
import com.mmxlabs.models.lng.adp.SalesContractProfile;
import com.mmxlabs.models.lng.adp.SubContractProfile;

import com.mmxlabs.models.lng.cargo.DischargeSlot;

import com.mmxlabs.models.lng.commercial.SalesContract;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Deliver To Profile Flow</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.adp.impl.DeliverToProfileFlowImpl#getProfile <em>Profile</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.impl.DeliverToProfileFlowImpl#getSubProfile <em>Sub Profile</em>}</li>
 * </ul>
 *
 * @generated
 */
public class DeliverToProfileFlowImpl extends DeliverToFlowImpl implements DeliverToProfileFlow {
	/**
	 * The cached value of the '{@link #getProfile() <em>Profile</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getProfile()
	 * @generated
	 * @ordered
	 */
	protected SalesContractProfile profile;

	/**
	 * The cached value of the '{@link #getSubProfile() <em>Sub Profile</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSubProfile()
	 * @generated
	 * @ordered
	 */
	protected SubContractProfile<DischargeSlot, SalesContract> subProfile;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected DeliverToProfileFlowImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ADPPackage.Literals.DELIVER_TO_PROFILE_FLOW;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SalesContractProfile getProfile() {
		if (profile != null && profile.eIsProxy()) {
			InternalEObject oldProfile = (InternalEObject)profile;
			profile = (SalesContractProfile)eResolveProxy(oldProfile);
			if (profile != oldProfile) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ADPPackage.DELIVER_TO_PROFILE_FLOW__PROFILE, oldProfile, profile));
			}
		}
		return profile;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SalesContractProfile basicGetProfile() {
		return profile;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setProfile(SalesContractProfile newProfile) {
		SalesContractProfile oldProfile = profile;
		profile = newProfile;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ADPPackage.DELIVER_TO_PROFILE_FLOW__PROFILE, oldProfile, profile));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	public SubContractProfile<DischargeSlot, SalesContract> getSubProfile() {
		if (subProfile != null && subProfile.eIsProxy()) {
			InternalEObject oldSubProfile = (InternalEObject)subProfile;
			subProfile = (SubContractProfile<DischargeSlot, SalesContract>)eResolveProxy(oldSubProfile);
			if (subProfile != oldSubProfile) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ADPPackage.DELIVER_TO_PROFILE_FLOW__SUB_PROFILE, oldSubProfile, subProfile));
			}
		}
		return subProfile;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SubContractProfile<DischargeSlot, SalesContract> basicGetSubProfile() {
		return subProfile;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSubProfile(SubContractProfile<DischargeSlot, SalesContract> newSubProfile) {
		SubContractProfile<DischargeSlot, SalesContract> oldSubProfile = subProfile;
		subProfile = newSubProfile;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ADPPackage.DELIVER_TO_PROFILE_FLOW__SUB_PROFILE, oldSubProfile, subProfile));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case ADPPackage.DELIVER_TO_PROFILE_FLOW__PROFILE:
				if (resolve) return getProfile();
				return basicGetProfile();
			case ADPPackage.DELIVER_TO_PROFILE_FLOW__SUB_PROFILE:
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
			case ADPPackage.DELIVER_TO_PROFILE_FLOW__PROFILE:
				setProfile((SalesContractProfile)newValue);
				return;
			case ADPPackage.DELIVER_TO_PROFILE_FLOW__SUB_PROFILE:
				setSubProfile((SubContractProfile<DischargeSlot, SalesContract>)newValue);
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
			case ADPPackage.DELIVER_TO_PROFILE_FLOW__PROFILE:
				setProfile((SalesContractProfile)null);
				return;
			case ADPPackage.DELIVER_TO_PROFILE_FLOW__SUB_PROFILE:
				setSubProfile((SubContractProfile<DischargeSlot, SalesContract>)null);
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
			case ADPPackage.DELIVER_TO_PROFILE_FLOW__PROFILE:
				return profile != null;
			case ADPPackage.DELIVER_TO_PROFILE_FLOW__SUB_PROFILE:
				return subProfile != null;
		}
		return super.eIsSet(featureID);
	}

} //DeliverToProfileFlowImpl
