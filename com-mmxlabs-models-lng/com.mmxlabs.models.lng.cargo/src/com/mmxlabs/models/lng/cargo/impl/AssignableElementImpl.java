/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.cargo.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import com.mmxlabs.models.lng.cargo.AssignableElement;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.types.VesselAssignmentType;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Assignable Element</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.AssignableElementImpl#getSequenceHint <em>Sequence Hint</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.AssignableElementImpl#getVesselAssignmentType <em>Vessel Assignment Type</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.AssignableElementImpl#getSpotIndex <em>Spot Index</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.AssignableElementImpl#isLocked <em>Locked</em>}</li>
 * </ul>
 *
 * @generated
 */
public abstract class AssignableElementImpl extends EObjectImpl implements AssignableElement {
	/**
	 * The default value of the '{@link #getSequenceHint() <em>Sequence Hint</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSequenceHint()
	 * @generated
	 * @ordered
	 */
	protected static final int SEQUENCE_HINT_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getSequenceHint() <em>Sequence Hint</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSequenceHint()
	 * @generated
	 * @ordered
	 */
	protected int sequenceHint = SEQUENCE_HINT_EDEFAULT;

	/**
	 * The cached value of the '{@link #getVesselAssignmentType() <em>Vessel Assignment Type</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVesselAssignmentType()
	 * @generated
	 * @ordered
	 */
	protected VesselAssignmentType vesselAssignmentType;

	/**
	 * The default value of the '{@link #getSpotIndex() <em>Spot Index</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSpotIndex()
	 * @generated
	 * @ordered
	 */
	protected static final int SPOT_INDEX_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getSpotIndex() <em>Spot Index</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSpotIndex()
	 * @generated
	 * @ordered
	 */
	protected int spotIndex = SPOT_INDEX_EDEFAULT;

	/**
	 * The default value of the '{@link #isLocked() <em>Locked</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isLocked()
	 * @generated
	 * @ordered
	 */
	protected static final boolean LOCKED_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isLocked() <em>Locked</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isLocked()
	 * @generated
	 * @ordered
	 */
	protected boolean locked = LOCKED_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected AssignableElementImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return CargoPackage.Literals.ASSIGNABLE_ELEMENT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getSpotIndex() {
		return spotIndex;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSpotIndex(int newSpotIndex) {
		int oldSpotIndex = spotIndex;
		spotIndex = newSpotIndex;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.ASSIGNABLE_ELEMENT__SPOT_INDEX, oldSpotIndex, spotIndex));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getSequenceHint() {
		return sequenceHint;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSequenceHint(int newSequenceHint) {
		int oldSequenceHint = sequenceHint;
		sequenceHint = newSequenceHint;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.ASSIGNABLE_ELEMENT__SEQUENCE_HINT, oldSequenceHint, sequenceHint));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isLocked() {
		return locked;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setLocked(boolean newLocked) {
		boolean oldLocked = locked;
		locked = newLocked;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.ASSIGNABLE_ELEMENT__LOCKED, oldLocked, locked));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public VesselAssignmentType getVesselAssignmentType() {
		if (vesselAssignmentType != null && vesselAssignmentType.eIsProxy()) {
			InternalEObject oldVesselAssignmentType = (InternalEObject)vesselAssignmentType;
			vesselAssignmentType = (VesselAssignmentType)eResolveProxy(oldVesselAssignmentType);
			if (vesselAssignmentType != oldVesselAssignmentType) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, CargoPackage.ASSIGNABLE_ELEMENT__VESSEL_ASSIGNMENT_TYPE, oldVesselAssignmentType, vesselAssignmentType));
			}
		}
		return vesselAssignmentType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public VesselAssignmentType basicGetVesselAssignmentType() {
		return vesselAssignmentType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setVesselAssignmentType(VesselAssignmentType newVesselAssignmentType) {
		VesselAssignmentType oldVesselAssignmentType = vesselAssignmentType;
		vesselAssignmentType = newVesselAssignmentType;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.ASSIGNABLE_ELEMENT__VESSEL_ASSIGNMENT_TYPE, oldVesselAssignmentType, vesselAssignmentType));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case CargoPackage.ASSIGNABLE_ELEMENT__SEQUENCE_HINT:
				return getSequenceHint();
			case CargoPackage.ASSIGNABLE_ELEMENT__VESSEL_ASSIGNMENT_TYPE:
				if (resolve) return getVesselAssignmentType();
				return basicGetVesselAssignmentType();
			case CargoPackage.ASSIGNABLE_ELEMENT__SPOT_INDEX:
				return getSpotIndex();
			case CargoPackage.ASSIGNABLE_ELEMENT__LOCKED:
				return isLocked();
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
			case CargoPackage.ASSIGNABLE_ELEMENT__SEQUENCE_HINT:
				setSequenceHint((Integer)newValue);
				return;
			case CargoPackage.ASSIGNABLE_ELEMENT__VESSEL_ASSIGNMENT_TYPE:
				setVesselAssignmentType((VesselAssignmentType)newValue);
				return;
			case CargoPackage.ASSIGNABLE_ELEMENT__SPOT_INDEX:
				setSpotIndex((Integer)newValue);
				return;
			case CargoPackage.ASSIGNABLE_ELEMENT__LOCKED:
				setLocked((Boolean)newValue);
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
			case CargoPackage.ASSIGNABLE_ELEMENT__SEQUENCE_HINT:
				setSequenceHint(SEQUENCE_HINT_EDEFAULT);
				return;
			case CargoPackage.ASSIGNABLE_ELEMENT__VESSEL_ASSIGNMENT_TYPE:
				setVesselAssignmentType((VesselAssignmentType)null);
				return;
			case CargoPackage.ASSIGNABLE_ELEMENT__SPOT_INDEX:
				setSpotIndex(SPOT_INDEX_EDEFAULT);
				return;
			case CargoPackage.ASSIGNABLE_ELEMENT__LOCKED:
				setLocked(LOCKED_EDEFAULT);
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
			case CargoPackage.ASSIGNABLE_ELEMENT__SEQUENCE_HINT:
				return sequenceHint != SEQUENCE_HINT_EDEFAULT;
			case CargoPackage.ASSIGNABLE_ELEMENT__VESSEL_ASSIGNMENT_TYPE:
				return vesselAssignmentType != null;
			case CargoPackage.ASSIGNABLE_ELEMENT__SPOT_INDEX:
				return spotIndex != SPOT_INDEX_EDEFAULT;
			case CargoPackage.ASSIGNABLE_ELEMENT__LOCKED:
				return locked != LOCKED_EDEFAULT;
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
		result.append(" (sequenceHint: ");
		result.append(sequenceHint);
		result.append(", spotIndex: ");
		result.append(spotIndex);
		result.append(", locked: ");
		result.append(locked);
		result.append(')');
		return result.toString();
	}

} //AssignableElementImpl
