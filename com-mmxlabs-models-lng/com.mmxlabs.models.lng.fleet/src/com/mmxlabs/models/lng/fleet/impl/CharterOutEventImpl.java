

/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package com.mmxlabs.models.lng.fleet.impl;
import com.mmxlabs.models.lng.fleet.CharterOutEvent;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.fleet.HeelOptions;

import com.mmxlabs.models.lng.port.Port;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Charter Out Event</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.CharterOutEventImpl#getRelocateTo <em>Relocate To</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.CharterOutEventImpl#getHeelOptions <em>Heel Options</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class CharterOutEventImpl extends VesselEventImpl implements CharterOutEvent {
	/**
	 * The cached value of the '{@link #getRelocateTo() <em>Relocate To</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRelocateTo()
	 * @generated
	 * @ordered
	 */
	protected Port relocateTo;

	/**
	 * This is true if the Relocate To reference has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean relocateToESet;

	/**
	 * The cached value of the '{@link #getHeelOptions() <em>Heel Options</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getHeelOptions()
	 * @generated
	 * @ordered
	 */
	protected HeelOptions heelOptions;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected CharterOutEventImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return FleetPackage.Literals.CHARTER_OUT_EVENT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Port getRelocateTo() {
		if (relocateTo != null && relocateTo.eIsProxy()) {
			InternalEObject oldRelocateTo = (InternalEObject)relocateTo;
			relocateTo = (Port)eResolveProxy(oldRelocateTo);
			if (relocateTo != oldRelocateTo) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, FleetPackage.CHARTER_OUT_EVENT__RELOCATE_TO, oldRelocateTo, relocateTo));
			}
		}
		return relocateTo;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Port basicGetRelocateTo() {
		return relocateTo;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setRelocateTo(Port newRelocateTo) {
		Port oldRelocateTo = relocateTo;
		relocateTo = newRelocateTo;
		boolean oldRelocateToESet = relocateToESet;
		relocateToESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.CHARTER_OUT_EVENT__RELOCATE_TO, oldRelocateTo, relocateTo, !oldRelocateToESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetRelocateTo() {
		Port oldRelocateTo = relocateTo;
		boolean oldRelocateToESet = relocateToESet;
		relocateTo = null;
		relocateToESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, FleetPackage.CHARTER_OUT_EVENT__RELOCATE_TO, oldRelocateTo, null, oldRelocateToESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetRelocateTo() {
		return relocateToESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public HeelOptions getHeelOptions() {
		return heelOptions;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetHeelOptions(HeelOptions newHeelOptions, NotificationChain msgs) {
		HeelOptions oldHeelOptions = heelOptions;
		heelOptions = newHeelOptions;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, FleetPackage.CHARTER_OUT_EVENT__HEEL_OPTIONS, oldHeelOptions, newHeelOptions);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setHeelOptions(HeelOptions newHeelOptions) {
		if (newHeelOptions != heelOptions) {
			NotificationChain msgs = null;
			if (heelOptions != null)
				msgs = ((InternalEObject)heelOptions).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - FleetPackage.CHARTER_OUT_EVENT__HEEL_OPTIONS, null, msgs);
			if (newHeelOptions != null)
				msgs = ((InternalEObject)newHeelOptions).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - FleetPackage.CHARTER_OUT_EVENT__HEEL_OPTIONS, null, msgs);
			msgs = basicSetHeelOptions(newHeelOptions, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.CHARTER_OUT_EVENT__HEEL_OPTIONS, newHeelOptions, newHeelOptions));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case FleetPackage.CHARTER_OUT_EVENT__HEEL_OPTIONS:
				return basicSetHeelOptions(null, msgs);
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
			case FleetPackage.CHARTER_OUT_EVENT__RELOCATE_TO:
				if (resolve) return getRelocateTo();
				return basicGetRelocateTo();
			case FleetPackage.CHARTER_OUT_EVENT__HEEL_OPTIONS:
				return getHeelOptions();
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
			case FleetPackage.CHARTER_OUT_EVENT__RELOCATE_TO:
				setRelocateTo((Port)newValue);
				return;
			case FleetPackage.CHARTER_OUT_EVENT__HEEL_OPTIONS:
				setHeelOptions((HeelOptions)newValue);
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
			case FleetPackage.CHARTER_OUT_EVENT__RELOCATE_TO:
				unsetRelocateTo();
				return;
			case FleetPackage.CHARTER_OUT_EVENT__HEEL_OPTIONS:
				setHeelOptions((HeelOptions)null);
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
			case FleetPackage.CHARTER_OUT_EVENT__RELOCATE_TO:
				return isSetRelocateTo();
			case FleetPackage.CHARTER_OUT_EVENT__HEEL_OPTIONS:
				return heelOptions != null;
		}
		return super.eIsSet(featureID);
	}

} // end of CharterOutEventImpl

// finish type fixing
