/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.input.impl;
import com.mmxlabs.models.lng.input.ElementAssignment;
import com.mmxlabs.models.lng.input.InputPackage;

import com.mmxlabs.models.lng.types.AVesselSet;

import com.mmxlabs.models.mmxcore.UUIDObject;

import com.mmxlabs.models.mmxcore.impl.MMXObjectImpl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Element Assignment</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.input.impl.ElementAssignmentImpl#getAssignedObject <em>Assigned Object</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.input.impl.ElementAssignmentImpl#getAssignment <em>Assignment</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.input.impl.ElementAssignmentImpl#isLocked <em>Locked</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.input.impl.ElementAssignmentImpl#getSequence <em>Sequence</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.input.impl.ElementAssignmentImpl#getSpotIndex <em>Spot Index</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ElementAssignmentImpl extends MMXObjectImpl implements ElementAssignment {
	/**
	 * The cached value of the '{@link #getAssignedObject() <em>Assigned Object</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAssignedObject()
	 * @generated
	 * @ordered
	 */
	protected UUIDObject assignedObject;

	/**
	 * The cached value of the '{@link #getAssignment() <em>Assignment</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAssignment()
	 * @generated
	 * @ordered
	 */
	protected AVesselSet assignment;

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
	 * The default value of the '{@link #getSequence() <em>Sequence</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSequence()
	 * @generated
	 * @ordered
	 */
	protected static final int SEQUENCE_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getSequence() <em>Sequence</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSequence()
	 * @generated
	 * @ordered
	 */
	protected int sequence = SEQUENCE_EDEFAULT;

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
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ElementAssignmentImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return InputPackage.Literals.ELEMENT_ASSIGNMENT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public UUIDObject getAssignedObject() {
		if (assignedObject != null && assignedObject.eIsProxy()) {
			InternalEObject oldAssignedObject = (InternalEObject)assignedObject;
			assignedObject = (UUIDObject)eResolveProxy(oldAssignedObject);
			if (assignedObject != oldAssignedObject) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, InputPackage.ELEMENT_ASSIGNMENT__ASSIGNED_OBJECT, oldAssignedObject, assignedObject));
			}
		}
		return assignedObject;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public UUIDObject basicGetAssignedObject() {
		return assignedObject;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setAssignedObject(UUIDObject newAssignedObject) {
		UUIDObject oldAssignedObject = assignedObject;
		assignedObject = newAssignedObject;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, InputPackage.ELEMENT_ASSIGNMENT__ASSIGNED_OBJECT, oldAssignedObject, assignedObject));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AVesselSet getAssignment() {
		if (assignment != null && assignment.eIsProxy()) {
			InternalEObject oldAssignment = (InternalEObject)assignment;
			assignment = (AVesselSet)eResolveProxy(oldAssignment);
			if (assignment != oldAssignment) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, InputPackage.ELEMENT_ASSIGNMENT__ASSIGNMENT, oldAssignment, assignment));
			}
		}
		return assignment;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AVesselSet basicGetAssignment() {
		return assignment;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setAssignment(AVesselSet newAssignment) {
		AVesselSet oldAssignment = assignment;
		assignment = newAssignment;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, InputPackage.ELEMENT_ASSIGNMENT__ASSIGNMENT, oldAssignment, assignment));
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
			eNotify(new ENotificationImpl(this, Notification.SET, InputPackage.ELEMENT_ASSIGNMENT__LOCKED, oldLocked, locked));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getSequence() {
		return sequence;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSequence(int newSequence) {
		int oldSequence = sequence;
		sequence = newSequence;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, InputPackage.ELEMENT_ASSIGNMENT__SEQUENCE, oldSequence, sequence));
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
			eNotify(new ENotificationImpl(this, Notification.SET, InputPackage.ELEMENT_ASSIGNMENT__SPOT_INDEX, oldSpotIndex, spotIndex));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case InputPackage.ELEMENT_ASSIGNMENT__ASSIGNED_OBJECT:
				if (resolve) return getAssignedObject();
				return basicGetAssignedObject();
			case InputPackage.ELEMENT_ASSIGNMENT__ASSIGNMENT:
				if (resolve) return getAssignment();
				return basicGetAssignment();
			case InputPackage.ELEMENT_ASSIGNMENT__LOCKED:
				return isLocked();
			case InputPackage.ELEMENT_ASSIGNMENT__SEQUENCE:
				return getSequence();
			case InputPackage.ELEMENT_ASSIGNMENT__SPOT_INDEX:
				return getSpotIndex();
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
			case InputPackage.ELEMENT_ASSIGNMENT__ASSIGNED_OBJECT:
				setAssignedObject((UUIDObject)newValue);
				return;
			case InputPackage.ELEMENT_ASSIGNMENT__ASSIGNMENT:
				setAssignment((AVesselSet)newValue);
				return;
			case InputPackage.ELEMENT_ASSIGNMENT__LOCKED:
				setLocked((Boolean)newValue);
				return;
			case InputPackage.ELEMENT_ASSIGNMENT__SEQUENCE:
				setSequence((Integer)newValue);
				return;
			case InputPackage.ELEMENT_ASSIGNMENT__SPOT_INDEX:
				setSpotIndex((Integer)newValue);
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
			case InputPackage.ELEMENT_ASSIGNMENT__ASSIGNED_OBJECT:
				setAssignedObject((UUIDObject)null);
				return;
			case InputPackage.ELEMENT_ASSIGNMENT__ASSIGNMENT:
				setAssignment((AVesselSet)null);
				return;
			case InputPackage.ELEMENT_ASSIGNMENT__LOCKED:
				setLocked(LOCKED_EDEFAULT);
				return;
			case InputPackage.ELEMENT_ASSIGNMENT__SEQUENCE:
				setSequence(SEQUENCE_EDEFAULT);
				return;
			case InputPackage.ELEMENT_ASSIGNMENT__SPOT_INDEX:
				setSpotIndex(SPOT_INDEX_EDEFAULT);
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
			case InputPackage.ELEMENT_ASSIGNMENT__ASSIGNED_OBJECT:
				return assignedObject != null;
			case InputPackage.ELEMENT_ASSIGNMENT__ASSIGNMENT:
				return assignment != null;
			case InputPackage.ELEMENT_ASSIGNMENT__LOCKED:
				return locked != LOCKED_EDEFAULT;
			case InputPackage.ELEMENT_ASSIGNMENT__SEQUENCE:
				return sequence != SEQUENCE_EDEFAULT;
			case InputPackage.ELEMENT_ASSIGNMENT__SPOT_INDEX:
				return spotIndex != SPOT_INDEX_EDEFAULT;
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
		result.append(" (locked: ");
		result.append(locked);
		result.append(", sequence: ");
		result.append(sequence);
		result.append(", spotIndex: ");
		result.append(spotIndex);
		result.append(')');
		return result.toString();
	}

} // end of ElementAssignmentImpl

// finish type fixing
