/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.mmxcore.impl;

import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.mmxcore.MMXProxy;
import com.mmxlabs.models.mmxcore.UUIDObject;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>MMX Proxy</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.mmxlabs.models.mmxcore.impl.MMXProxyImpl#getReferentID <em>Referent ID</em>}</li>
 *   <li>{@link com.mmxlabs.models.mmxcore.impl.MMXProxyImpl#getResolvedReferent <em>Resolved Referent</em>}</li>
 *   <li>{@link com.mmxlabs.models.mmxcore.impl.MMXProxyImpl#getReference <em>Reference</em>}</li>
 *   <li>{@link com.mmxlabs.models.mmxcore.impl.MMXProxyImpl#getReferentOwner <em>Referent Owner</em>}</li>
 *   <li>{@link com.mmxlabs.models.mmxcore.impl.MMXProxyImpl#getIndex <em>Index</em>}</li>
 *   <li>{@link com.mmxlabs.models.mmxcore.impl.MMXProxyImpl#getReferentName <em>Referent Name</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class MMXProxyImpl extends EObjectImpl implements MMXProxy {
	/**
	 * The default value of the '{@link #getReferentID() <em>Referent ID</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getReferentID()
	 * @generated
	 * @ordered
	 */
	protected static final String REFERENT_ID_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getReferentID() <em>Referent ID</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getReferentID()
	 * @generated
	 * @ordered
	 */
	protected String referentID = REFERENT_ID_EDEFAULT;

	/**
	 * The cached value of the '{@link #getResolvedReferent() <em>Resolved Referent</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getResolvedReferent()
	 * @generated
	 * @ordered
	 */
	protected UUIDObject resolvedReferent;

	/**
	 * The cached value of the '{@link #getReference() <em>Reference</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getReference()
	 * @generated
	 * @ordered
	 */
	protected EReference reference;

	/**
	 * The default value of the '{@link #getReferentOwner() <em>Referent Owner</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getReferentOwner()
	 * @generated
	 * @ordered
	 */
	protected static final String REFERENT_OWNER_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getReferentOwner() <em>Referent Owner</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getReferentOwner()
	 * @generated
	 * @ordered
	 */
	protected String referentOwner = REFERENT_OWNER_EDEFAULT;

	/**
	 * The default value of the '{@link #getIndex() <em>Index</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getIndex()
	 * @generated
	 * @ordered
	 */
	protected static final int INDEX_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getIndex() <em>Index</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getIndex()
	 * @generated
	 * @ordered
	 */
	protected int index = INDEX_EDEFAULT;

	/**
	 * The default value of the '{@link #getReferentName() <em>Referent Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getReferentName()
	 * @generated
	 * @ordered
	 */
	protected static final String REFERENT_NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getReferentName() <em>Referent Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getReferentName()
	 * @generated
	 * @ordered
	 */
	protected String referentName = REFERENT_NAME_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected MMXProxyImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return MMXCorePackage.Literals.MMX_PROXY;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getReferentID() {
		return referentID;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setReferentID(String newReferentID) {
		String oldReferentID = referentID;
		referentID = newReferentID;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MMXCorePackage.MMX_PROXY__REFERENT_ID, oldReferentID, referentID));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public UUIDObject getResolvedReferent() {
		if (resolvedReferent != null && resolvedReferent.eIsProxy()) {
			InternalEObject oldResolvedReferent = (InternalEObject)resolvedReferent;
			resolvedReferent = (UUIDObject)eResolveProxy(oldResolvedReferent);
			if (resolvedReferent != oldResolvedReferent) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, MMXCorePackage.MMX_PROXY__RESOLVED_REFERENT, oldResolvedReferent, resolvedReferent));
			}
		}
		return resolvedReferent;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public UUIDObject basicGetResolvedReferent() {
		return resolvedReferent;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setResolvedReferent(UUIDObject newResolvedReferent) {
		UUIDObject oldResolvedReferent = resolvedReferent;
		resolvedReferent = newResolvedReferent;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MMXCorePackage.MMX_PROXY__RESOLVED_REFERENT, oldResolvedReferent, resolvedReferent));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getReference() {
		if (reference != null && reference.eIsProxy()) {
			InternalEObject oldReference = (InternalEObject)reference;
			reference = (EReference)eResolveProxy(oldReference);
			if (reference != oldReference) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, MMXCorePackage.MMX_PROXY__REFERENCE, oldReference, reference));
			}
		}
		return reference;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference basicGetReference() {
		return reference;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setReference(EReference newReference) {
		EReference oldReference = reference;
		reference = newReference;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MMXCorePackage.MMX_PROXY__REFERENCE, oldReference, reference));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getReferentOwner() {
		return referentOwner;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setReferentOwner(String newReferentOwner) {
		String oldReferentOwner = referentOwner;
		referentOwner = newReferentOwner;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MMXCorePackage.MMX_PROXY__REFERENT_OWNER, oldReferentOwner, referentOwner));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setIndex(int newIndex) {
		int oldIndex = index;
		index = newIndex;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MMXCorePackage.MMX_PROXY__INDEX, oldIndex, index));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getReferentName() {
		return referentName;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setReferentName(String newReferentName) {
		String oldReferentName = referentName;
		referentName = newReferentName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MMXCorePackage.MMX_PROXY__REFERENT_NAME, oldReferentName, referentName));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case MMXCorePackage.MMX_PROXY__REFERENT_ID:
				return getReferentID();
			case MMXCorePackage.MMX_PROXY__RESOLVED_REFERENT:
				if (resolve) return getResolvedReferent();
				return basicGetResolvedReferent();
			case MMXCorePackage.MMX_PROXY__REFERENCE:
				if (resolve) return getReference();
				return basicGetReference();
			case MMXCorePackage.MMX_PROXY__REFERENT_OWNER:
				return getReferentOwner();
			case MMXCorePackage.MMX_PROXY__INDEX:
				return getIndex();
			case MMXCorePackage.MMX_PROXY__REFERENT_NAME:
				return getReferentName();
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
			case MMXCorePackage.MMX_PROXY__REFERENT_ID:
				setReferentID((String)newValue);
				return;
			case MMXCorePackage.MMX_PROXY__RESOLVED_REFERENT:
				setResolvedReferent((UUIDObject)newValue);
				return;
			case MMXCorePackage.MMX_PROXY__REFERENCE:
				setReference((EReference)newValue);
				return;
			case MMXCorePackage.MMX_PROXY__REFERENT_OWNER:
				setReferentOwner((String)newValue);
				return;
			case MMXCorePackage.MMX_PROXY__INDEX:
				setIndex((Integer)newValue);
				return;
			case MMXCorePackage.MMX_PROXY__REFERENT_NAME:
				setReferentName((String)newValue);
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
			case MMXCorePackage.MMX_PROXY__REFERENT_ID:
				setReferentID(REFERENT_ID_EDEFAULT);
				return;
			case MMXCorePackage.MMX_PROXY__RESOLVED_REFERENT:
				setResolvedReferent((UUIDObject)null);
				return;
			case MMXCorePackage.MMX_PROXY__REFERENCE:
				setReference((EReference)null);
				return;
			case MMXCorePackage.MMX_PROXY__REFERENT_OWNER:
				setReferentOwner(REFERENT_OWNER_EDEFAULT);
				return;
			case MMXCorePackage.MMX_PROXY__INDEX:
				setIndex(INDEX_EDEFAULT);
				return;
			case MMXCorePackage.MMX_PROXY__REFERENT_NAME:
				setReferentName(REFERENT_NAME_EDEFAULT);
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
			case MMXCorePackage.MMX_PROXY__REFERENT_ID:
				return REFERENT_ID_EDEFAULT == null ? referentID != null : !REFERENT_ID_EDEFAULT.equals(referentID);
			case MMXCorePackage.MMX_PROXY__RESOLVED_REFERENT:
				return resolvedReferent != null;
			case MMXCorePackage.MMX_PROXY__REFERENCE:
				return reference != null;
			case MMXCorePackage.MMX_PROXY__REFERENT_OWNER:
				return REFERENT_OWNER_EDEFAULT == null ? referentOwner != null : !REFERENT_OWNER_EDEFAULT.equals(referentOwner);
			case MMXCorePackage.MMX_PROXY__INDEX:
				return index != INDEX_EDEFAULT;
			case MMXCorePackage.MMX_PROXY__REFERENT_NAME:
				return REFERENT_NAME_EDEFAULT == null ? referentName != null : !REFERENT_NAME_EDEFAULT.equals(referentName);
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
		result.append(" (referentID: ");
		result.append(referentID);
		result.append(", referentOwner: ");
		result.append(referentOwner);
		result.append(", index: ");
		result.append(index);
		result.append(", referentName: ");
		result.append(referentName);
		result.append(')');
		return result.toString();
	}

} //MMXProxyImpl
