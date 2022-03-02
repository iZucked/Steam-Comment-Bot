/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.adp.impl;

import com.mmxlabs.models.lng.adp.ADPPackage;
import com.mmxlabs.models.lng.adp.MullCargoWrapper;
import com.mmxlabs.models.lng.adp.MullProfile;
import com.mmxlabs.models.lng.adp.MullSubprofile;

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
 * An implementation of the model object '<em><b>Mull Profile</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.adp.impl.MullProfileImpl#getWindowSize <em>Window Size</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.impl.MullProfileImpl#getVolumeFlex <em>Volume Flex</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.impl.MullProfileImpl#getInventories <em>Inventories</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.impl.MullProfileImpl#getFullCargoLotValue <em>Full Cargo Lot Value</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.adp.impl.MullProfileImpl#getCargoesToKeep <em>Cargoes To Keep</em>}</li>
 * </ul>
 *
 * @generated
 */
public class MullProfileImpl extends EObjectImpl implements MullProfile {
	/**
	 * The default value of the '{@link #getWindowSize() <em>Window Size</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getWindowSize()
	 * @generated
	 * @ordered
	 */
	protected static final int WINDOW_SIZE_EDEFAULT = 0;

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
	 * The default value of the '{@link #getVolumeFlex() <em>Volume Flex</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVolumeFlex()
	 * @generated
	 * @ordered
	 */
	protected static final int VOLUME_FLEX_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getVolumeFlex() <em>Volume Flex</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVolumeFlex()
	 * @generated
	 * @ordered
	 */
	protected int volumeFlex = VOLUME_FLEX_EDEFAULT;

	/**
	 * The cached value of the '{@link #getInventories() <em>Inventories</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getInventories()
	 * @generated
	 * @ordered
	 */
	protected EList<MullSubprofile> inventories;

	/**
	 * The default value of the '{@link #getFullCargoLotValue() <em>Full Cargo Lot Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFullCargoLotValue()
	 * @generated
	 * @ordered
	 */
	protected static final int FULL_CARGO_LOT_VALUE_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getFullCargoLotValue() <em>Full Cargo Lot Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFullCargoLotValue()
	 * @generated
	 * @ordered
	 */
	protected int fullCargoLotValue = FULL_CARGO_LOT_VALUE_EDEFAULT;

	/**
	 * The cached value of the '{@link #getCargoesToKeep() <em>Cargoes To Keep</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCargoesToKeep()
	 * @generated
	 * @ordered
	 */
	protected EList<MullCargoWrapper> cargoesToKeep;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected MullProfileImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ADPPackage.Literals.MULL_PROFILE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getWindowSize() {
		return windowSize;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setWindowSize(int newWindowSize) {
		int oldWindowSize = windowSize;
		windowSize = newWindowSize;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ADPPackage.MULL_PROFILE__WINDOW_SIZE, oldWindowSize, windowSize));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getVolumeFlex() {
		return volumeFlex;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setVolumeFlex(int newVolumeFlex) {
		int oldVolumeFlex = volumeFlex;
		volumeFlex = newVolumeFlex;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ADPPackage.MULL_PROFILE__VOLUME_FLEX, oldVolumeFlex, volumeFlex));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<MullSubprofile> getInventories() {
		if (inventories == null) {
			inventories = new EObjectContainmentEList.Resolving<MullSubprofile>(MullSubprofile.class, this, ADPPackage.MULL_PROFILE__INVENTORIES);
		}
		return inventories;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getFullCargoLotValue() {
		return fullCargoLotValue;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setFullCargoLotValue(int newFullCargoLotValue) {
		int oldFullCargoLotValue = fullCargoLotValue;
		fullCargoLotValue = newFullCargoLotValue;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ADPPackage.MULL_PROFILE__FULL_CARGO_LOT_VALUE, oldFullCargoLotValue, fullCargoLotValue));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<MullCargoWrapper> getCargoesToKeep() {
		if (cargoesToKeep == null) {
			cargoesToKeep = new EObjectContainmentEList.Resolving<MullCargoWrapper>(MullCargoWrapper.class, this, ADPPackage.MULL_PROFILE__CARGOES_TO_KEEP);
		}
		return cargoesToKeep;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case ADPPackage.MULL_PROFILE__INVENTORIES:
				return ((InternalEList<?>)getInventories()).basicRemove(otherEnd, msgs);
			case ADPPackage.MULL_PROFILE__CARGOES_TO_KEEP:
				return ((InternalEList<?>)getCargoesToKeep()).basicRemove(otherEnd, msgs);
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
			case ADPPackage.MULL_PROFILE__WINDOW_SIZE:
				return getWindowSize();
			case ADPPackage.MULL_PROFILE__VOLUME_FLEX:
				return getVolumeFlex();
			case ADPPackage.MULL_PROFILE__INVENTORIES:
				return getInventories();
			case ADPPackage.MULL_PROFILE__FULL_CARGO_LOT_VALUE:
				return getFullCargoLotValue();
			case ADPPackage.MULL_PROFILE__CARGOES_TO_KEEP:
				return getCargoesToKeep();
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
			case ADPPackage.MULL_PROFILE__WINDOW_SIZE:
				setWindowSize((Integer)newValue);
				return;
			case ADPPackage.MULL_PROFILE__VOLUME_FLEX:
				setVolumeFlex((Integer)newValue);
				return;
			case ADPPackage.MULL_PROFILE__INVENTORIES:
				getInventories().clear();
				getInventories().addAll((Collection<? extends MullSubprofile>)newValue);
				return;
			case ADPPackage.MULL_PROFILE__FULL_CARGO_LOT_VALUE:
				setFullCargoLotValue((Integer)newValue);
				return;
			case ADPPackage.MULL_PROFILE__CARGOES_TO_KEEP:
				getCargoesToKeep().clear();
				getCargoesToKeep().addAll((Collection<? extends MullCargoWrapper>)newValue);
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
			case ADPPackage.MULL_PROFILE__WINDOW_SIZE:
				setWindowSize(WINDOW_SIZE_EDEFAULT);
				return;
			case ADPPackage.MULL_PROFILE__VOLUME_FLEX:
				setVolumeFlex(VOLUME_FLEX_EDEFAULT);
				return;
			case ADPPackage.MULL_PROFILE__INVENTORIES:
				getInventories().clear();
				return;
			case ADPPackage.MULL_PROFILE__FULL_CARGO_LOT_VALUE:
				setFullCargoLotValue(FULL_CARGO_LOT_VALUE_EDEFAULT);
				return;
			case ADPPackage.MULL_PROFILE__CARGOES_TO_KEEP:
				getCargoesToKeep().clear();
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
			case ADPPackage.MULL_PROFILE__WINDOW_SIZE:
				return windowSize != WINDOW_SIZE_EDEFAULT;
			case ADPPackage.MULL_PROFILE__VOLUME_FLEX:
				return volumeFlex != VOLUME_FLEX_EDEFAULT;
			case ADPPackage.MULL_PROFILE__INVENTORIES:
				return inventories != null && !inventories.isEmpty();
			case ADPPackage.MULL_PROFILE__FULL_CARGO_LOT_VALUE:
				return fullCargoLotValue != FULL_CARGO_LOT_VALUE_EDEFAULT;
			case ADPPackage.MULL_PROFILE__CARGOES_TO_KEEP:
				return cargoesToKeep != null && !cargoesToKeep.isEmpty();
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
		result.append(" (windowSize: ");
		result.append(windowSize);
		result.append(", volumeFlex: ");
		result.append(volumeFlex);
		result.append(", fullCargoLotValue: ");
		result.append(fullCargoLotValue);
		result.append(')');
		return result.toString();
	}

} //MullProfileImpl
