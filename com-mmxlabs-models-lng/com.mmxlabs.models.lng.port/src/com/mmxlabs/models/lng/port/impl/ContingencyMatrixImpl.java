/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.port.impl;

import com.mmxlabs.models.lng.port.ContingencyMatrix;
import com.mmxlabs.models.lng.port.ContingencyMatrixEntry;
import com.mmxlabs.models.lng.port.PortPackage;

import com.mmxlabs.models.lng.types.TimePeriod;

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
 * An implementation of the model object '<em><b>Contingency Matrix</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.port.impl.ContingencyMatrixImpl#getEntries <em>Entries</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.port.impl.ContingencyMatrixImpl#getDefaultDuration <em>Default Duration</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ContingencyMatrixImpl extends EObjectImpl implements ContingencyMatrix {
	/**
	 * The cached value of the '{@link #getEntries() <em>Entries</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEntries()
	 * @generated
	 * @ordered
	 */
	protected EList<ContingencyMatrixEntry> entries;

	/**
	 * The default value of the '{@link #getDefaultDuration() <em>Default Duration</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDefaultDuration()
	 * @generated
	 * @ordered
	 */
	protected static final int DEFAULT_DURATION_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getDefaultDuration() <em>Default Duration</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDefaultDuration()
	 * @generated
	 * @ordered
	 */
	protected int defaultDuration = DEFAULT_DURATION_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ContingencyMatrixImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return PortPackage.Literals.CONTINGENCY_MATRIX;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<ContingencyMatrixEntry> getEntries() {
		if (entries == null) {
			entries = new EObjectContainmentEList<ContingencyMatrixEntry>(ContingencyMatrixEntry.class, this, PortPackage.CONTINGENCY_MATRIX__ENTRIES);
		}
		return entries;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getDefaultDuration() {
		return defaultDuration;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setDefaultDuration(int newDefaultDuration) {
		int oldDefaultDuration = defaultDuration;
		defaultDuration = newDefaultDuration;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PortPackage.CONTINGENCY_MATRIX__DEFAULT_DURATION, oldDefaultDuration, defaultDuration));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case PortPackage.CONTINGENCY_MATRIX__ENTRIES:
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
			case PortPackage.CONTINGENCY_MATRIX__ENTRIES:
				return getEntries();
			case PortPackage.CONTINGENCY_MATRIX__DEFAULT_DURATION:
				return getDefaultDuration();
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
			case PortPackage.CONTINGENCY_MATRIX__ENTRIES:
				getEntries().clear();
				getEntries().addAll((Collection<? extends ContingencyMatrixEntry>)newValue);
				return;
			case PortPackage.CONTINGENCY_MATRIX__DEFAULT_DURATION:
				setDefaultDuration((Integer)newValue);
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
			case PortPackage.CONTINGENCY_MATRIX__ENTRIES:
				getEntries().clear();
				return;
			case PortPackage.CONTINGENCY_MATRIX__DEFAULT_DURATION:
				setDefaultDuration(DEFAULT_DURATION_EDEFAULT);
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
			case PortPackage.CONTINGENCY_MATRIX__ENTRIES:
				return entries != null && !entries.isEmpty();
			case PortPackage.CONTINGENCY_MATRIX__DEFAULT_DURATION:
				return defaultDuration != DEFAULT_DURATION_EDEFAULT;
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
		result.append(" (defaultDuration: ");
		result.append(defaultDuration);
		result.append(')');
		return result.toString();
	}

} //ContingencyMatrixImpl
