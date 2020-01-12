/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.analytics.impl;

import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.OpenSlotChange;
import com.mmxlabs.models.lng.analytics.SlotDescriptor;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Open Slot Change</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.OpenSlotChangeImpl#getSlotDescriptor <em>Slot Descriptor</em>}</li>
 * </ul>
 *
 * @generated
 */
public class OpenSlotChangeImpl extends ChangeImpl implements OpenSlotChange {
	/**
	 * The cached value of the '{@link #getSlotDescriptor() <em>Slot Descriptor</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSlotDescriptor()
	 * @generated
	 * @ordered
	 */
	protected SlotDescriptor slotDescriptor;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected OpenSlotChangeImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return AnalyticsPackage.Literals.OPEN_SLOT_CHANGE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public SlotDescriptor getSlotDescriptor() {
		return slotDescriptor;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetSlotDescriptor(SlotDescriptor newSlotDescriptor, NotificationChain msgs) {
		SlotDescriptor oldSlotDescriptor = slotDescriptor;
		slotDescriptor = newSlotDescriptor;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, AnalyticsPackage.OPEN_SLOT_CHANGE__SLOT_DESCRIPTOR, oldSlotDescriptor, newSlotDescriptor);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setSlotDescriptor(SlotDescriptor newSlotDescriptor) {
		if (newSlotDescriptor != slotDescriptor) {
			NotificationChain msgs = null;
			if (slotDescriptor != null)
				msgs = ((InternalEObject)slotDescriptor).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - AnalyticsPackage.OPEN_SLOT_CHANGE__SLOT_DESCRIPTOR, null, msgs);
			if (newSlotDescriptor != null)
				msgs = ((InternalEObject)newSlotDescriptor).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - AnalyticsPackage.OPEN_SLOT_CHANGE__SLOT_DESCRIPTOR, null, msgs);
			msgs = basicSetSlotDescriptor(newSlotDescriptor, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.OPEN_SLOT_CHANGE__SLOT_DESCRIPTOR, newSlotDescriptor, newSlotDescriptor));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case AnalyticsPackage.OPEN_SLOT_CHANGE__SLOT_DESCRIPTOR:
				return basicSetSlotDescriptor(null, msgs);
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
			case AnalyticsPackage.OPEN_SLOT_CHANGE__SLOT_DESCRIPTOR:
				return getSlotDescriptor();
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
			case AnalyticsPackage.OPEN_SLOT_CHANGE__SLOT_DESCRIPTOR:
				setSlotDescriptor((SlotDescriptor)newValue);
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
			case AnalyticsPackage.OPEN_SLOT_CHANGE__SLOT_DESCRIPTOR:
				setSlotDescriptor((SlotDescriptor)null);
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
			case AnalyticsPackage.OPEN_SLOT_CHANGE__SLOT_DESCRIPTOR:
				return slotDescriptor != null;
		}
		return super.eIsSet(featureID);
	}

} //OpenSlotChangeImpl
