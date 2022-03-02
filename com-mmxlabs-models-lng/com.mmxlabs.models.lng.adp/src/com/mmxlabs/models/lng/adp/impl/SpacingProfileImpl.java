/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.adp.impl;

import com.mmxlabs.models.lng.adp.ADPPackage;
import com.mmxlabs.models.lng.adp.DesSpacingAllocation;
import com.mmxlabs.models.lng.adp.FobSpacingAllocation;
import com.mmxlabs.models.lng.adp.SpacingProfile;

import com.mmxlabs.models.lng.port.Port;

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
 * An implementation of the model object '<em><b>Spacing Profile</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.adp.impl.SpacingProfileImpl#getDefaultPort <em>Default Port</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.impl.SpacingProfileImpl#getFobSpacingAllocations <em>Fob Spacing Allocations</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.impl.SpacingProfileImpl#getDesSpacingAllocations <em>Des Spacing Allocations</em>}</li>
 * </ul>
 *
 * @generated
 */
public class SpacingProfileImpl extends EObjectImpl implements SpacingProfile {
	/**
	 * The cached value of the '{@link #getDefaultPort() <em>Default Port</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDefaultPort()
	 * @generated
	 * @ordered
	 */
	protected Port defaultPort;

	/**
	 * The cached value of the '{@link #getFobSpacingAllocations() <em>Fob Spacing Allocations</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFobSpacingAllocations()
	 * @generated
	 * @ordered
	 */
	protected EList<FobSpacingAllocation> fobSpacingAllocations;

	/**
	 * The cached value of the '{@link #getDesSpacingAllocations() <em>Des Spacing Allocations</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDesSpacingAllocations()
	 * @generated
	 * @ordered
	 */
	protected EList<DesSpacingAllocation> desSpacingAllocations;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected SpacingProfileImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ADPPackage.Literals.SPACING_PROFILE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Port getDefaultPort() {
		if (defaultPort != null && defaultPort.eIsProxy()) {
			InternalEObject oldDefaultPort = (InternalEObject)defaultPort;
			defaultPort = (Port)eResolveProxy(oldDefaultPort);
			if (defaultPort != oldDefaultPort) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ADPPackage.SPACING_PROFILE__DEFAULT_PORT, oldDefaultPort, defaultPort));
			}
		}
		return defaultPort;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Port basicGetDefaultPort() {
		return defaultPort;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setDefaultPort(Port newDefaultPort) {
		Port oldDefaultPort = defaultPort;
		defaultPort = newDefaultPort;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ADPPackage.SPACING_PROFILE__DEFAULT_PORT, oldDefaultPort, defaultPort));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<FobSpacingAllocation> getFobSpacingAllocations() {
		if (fobSpacingAllocations == null) {
			fobSpacingAllocations = new EObjectContainmentEList.Resolving<FobSpacingAllocation>(FobSpacingAllocation.class, this, ADPPackage.SPACING_PROFILE__FOB_SPACING_ALLOCATIONS);
		}
		return fobSpacingAllocations;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<DesSpacingAllocation> getDesSpacingAllocations() {
		if (desSpacingAllocations == null) {
			desSpacingAllocations = new EObjectContainmentEList.Resolving<DesSpacingAllocation>(DesSpacingAllocation.class, this, ADPPackage.SPACING_PROFILE__DES_SPACING_ALLOCATIONS);
		}
		return desSpacingAllocations;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case ADPPackage.SPACING_PROFILE__FOB_SPACING_ALLOCATIONS:
				return ((InternalEList<?>)getFobSpacingAllocations()).basicRemove(otherEnd, msgs);
			case ADPPackage.SPACING_PROFILE__DES_SPACING_ALLOCATIONS:
				return ((InternalEList<?>)getDesSpacingAllocations()).basicRemove(otherEnd, msgs);
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
			case ADPPackage.SPACING_PROFILE__DEFAULT_PORT:
				if (resolve) return getDefaultPort();
				return basicGetDefaultPort();
			case ADPPackage.SPACING_PROFILE__FOB_SPACING_ALLOCATIONS:
				return getFobSpacingAllocations();
			case ADPPackage.SPACING_PROFILE__DES_SPACING_ALLOCATIONS:
				return getDesSpacingAllocations();
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
			case ADPPackage.SPACING_PROFILE__DEFAULT_PORT:
				setDefaultPort((Port)newValue);
				return;
			case ADPPackage.SPACING_PROFILE__FOB_SPACING_ALLOCATIONS:
				getFobSpacingAllocations().clear();
				getFobSpacingAllocations().addAll((Collection<? extends FobSpacingAllocation>)newValue);
				return;
			case ADPPackage.SPACING_PROFILE__DES_SPACING_ALLOCATIONS:
				getDesSpacingAllocations().clear();
				getDesSpacingAllocations().addAll((Collection<? extends DesSpacingAllocation>)newValue);
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
			case ADPPackage.SPACING_PROFILE__DEFAULT_PORT:
				setDefaultPort((Port)null);
				return;
			case ADPPackage.SPACING_PROFILE__FOB_SPACING_ALLOCATIONS:
				getFobSpacingAllocations().clear();
				return;
			case ADPPackage.SPACING_PROFILE__DES_SPACING_ALLOCATIONS:
				getDesSpacingAllocations().clear();
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
			case ADPPackage.SPACING_PROFILE__DEFAULT_PORT:
				return defaultPort != null;
			case ADPPackage.SPACING_PROFILE__FOB_SPACING_ALLOCATIONS:
				return fobSpacingAllocations != null && !fobSpacingAllocations.isEmpty();
			case ADPPackage.SPACING_PROFILE__DES_SPACING_ALLOCATIONS:
				return desSpacingAllocations != null && !desSpacingAllocations.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //SpacingProfileImpl
