/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.shiplingo.platform.models.manifest.manifest.impl;

import com.mmxlabs.shiplingo.platform.models.manifest.manifest.Entry;
import com.mmxlabs.shiplingo.platform.models.manifest.manifest.Manifest;
import com.mmxlabs.shiplingo.platform.models.manifest.manifest.ManifestPackage;

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
 * An implementation of the model object '<em><b>Manifest</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.mmxlabs.shiplingo.platform.models.manifest.manifest.impl.ManifestImpl#getEntries <em>Entries</em>}</li>
 *   <li>{@link com.mmxlabs.shiplingo.platform.models.manifest.manifest.impl.ManifestImpl#getCurrentVersion <em>Current Version</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ManifestImpl extends EObjectImpl implements Manifest {
	/**
	 * The cached value of the '{@link #getEntries() <em>Entries</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEntries()
	 * @generated
	 * @ordered
	 */
	protected EList<Entry> entries;

	/**
	 * The default value of the '{@link #getCurrentVersion() <em>Current Version</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCurrentVersion()
	 * @generated
	 * @ordered
	 */
	protected static final int CURRENT_VERSION_EDEFAULT = 0;
	/**
	 * The cached value of the '{@link #getCurrentVersion() <em>Current Version</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCurrentVersion()
	 * @generated
	 * @ordered
	 */
	protected int currentVersion = CURRENT_VERSION_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ManifestImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ManifestPackage.Literals.MANIFEST;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<Entry> getEntries() {
		if (entries == null) {
			entries = new EObjectContainmentEList<Entry>(Entry.class, this, ManifestPackage.MANIFEST__ENTRIES);
		}
		return entries;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getCurrentVersion() {
		return currentVersion;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setCurrentVersion(int newCurrentVersion) {
		int oldCurrentVersion = currentVersion;
		currentVersion = newCurrentVersion;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ManifestPackage.MANIFEST__CURRENT_VERSION, oldCurrentVersion, currentVersion));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case ManifestPackage.MANIFEST__ENTRIES:
				return ((InternalEList<?>)getEntries()).basicRemove(otherEnd, msgs);
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
			case ManifestPackage.MANIFEST__ENTRIES:
				return getEntries();
			case ManifestPackage.MANIFEST__CURRENT_VERSION:
				return getCurrentVersion();
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
			case ManifestPackage.MANIFEST__ENTRIES:
				getEntries().clear();
				getEntries().addAll((Collection<? extends Entry>)newValue);
				return;
			case ManifestPackage.MANIFEST__CURRENT_VERSION:
				setCurrentVersion((Integer)newValue);
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
			case ManifestPackage.MANIFEST__ENTRIES:
				getEntries().clear();
				return;
			case ManifestPackage.MANIFEST__CURRENT_VERSION:
				setCurrentVersion(CURRENT_VERSION_EDEFAULT);
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
			case ManifestPackage.MANIFEST__ENTRIES:
				return entries != null && !entries.isEmpty();
			case ManifestPackage.MANIFEST__CURRENT_VERSION:
				return currentVersion != CURRENT_VERSION_EDEFAULT;
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
		result.append(" (currentVersion: ");
		result.append(currentVersion);
		result.append(')');
		return result.toString();
	}

} //ManifestImpl
