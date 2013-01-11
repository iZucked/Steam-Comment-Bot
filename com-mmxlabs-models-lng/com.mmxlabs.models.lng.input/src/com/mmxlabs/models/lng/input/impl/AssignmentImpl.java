/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.input.impl;
import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;

import com.mmxlabs.models.lng.input.Assignment;
import com.mmxlabs.models.lng.input.InputPackage;
import com.mmxlabs.models.lng.types.AVesselSet;
import com.mmxlabs.models.mmxcore.UUIDObject;
import com.mmxlabs.models.mmxcore.impl.MMXObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Assignment</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.input.impl.AssignmentImpl#getVessels <em>Vessels</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.input.impl.AssignmentImpl#isAssignToSpot <em>Assign To Spot</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.input.impl.AssignmentImpl#getAssignedObjects <em>Assigned Objects</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class AssignmentImpl extends MMXObjectImpl implements Assignment {
	/**
	 * The cached value of the '{@link #getVessels() <em>Vessels</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVessels()
	 * @generated
	 * @ordered
	 */
	protected EList<AVesselSet> vessels;

	/**
	 * The default value of the '{@link #isAssignToSpot() <em>Assign To Spot</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isAssignToSpot()
	 * @generated
	 * @ordered
	 */
	protected static final boolean ASSIGN_TO_SPOT_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isAssignToSpot() <em>Assign To Spot</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isAssignToSpot()
	 * @generated
	 * @ordered
	 */
	protected boolean assignToSpot = ASSIGN_TO_SPOT_EDEFAULT;

	/**
	 * The cached value of the '{@link #getAssignedObjects() <em>Assigned Objects</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAssignedObjects()
	 * @generated
	 * @ordered
	 */
	protected EList<UUIDObject> assignedObjects;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected AssignmentImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return InputPackage.Literals.ASSIGNMENT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<AVesselSet> getVessels() {
		if (vessels == null) {
			vessels = new EObjectResolvingEList<AVesselSet>(AVesselSet.class, this, InputPackage.ASSIGNMENT__VESSELS);
		}
		return vessels;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isAssignToSpot() {
		return assignToSpot;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setAssignToSpot(boolean newAssignToSpot) {
		boolean oldAssignToSpot = assignToSpot;
		assignToSpot = newAssignToSpot;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, InputPackage.ASSIGNMENT__ASSIGN_TO_SPOT, oldAssignToSpot, assignToSpot));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<UUIDObject> getAssignedObjects() {
		if (assignedObjects == null) {
			assignedObjects = new EObjectResolvingEList<UUIDObject>(UUIDObject.class, this, InputPackage.ASSIGNMENT__ASSIGNED_OBJECTS);
		}
		return assignedObjects;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case InputPackage.ASSIGNMENT__VESSELS:
				return getVessels();
			case InputPackage.ASSIGNMENT__ASSIGN_TO_SPOT:
				return isAssignToSpot();
			case InputPackage.ASSIGNMENT__ASSIGNED_OBJECTS:
				return getAssignedObjects();
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
			case InputPackage.ASSIGNMENT__VESSELS:
				getVessels().clear();
				getVessels().addAll((Collection<? extends AVesselSet>)newValue);
				return;
			case InputPackage.ASSIGNMENT__ASSIGN_TO_SPOT:
				setAssignToSpot((Boolean)newValue);
				return;
			case InputPackage.ASSIGNMENT__ASSIGNED_OBJECTS:
				getAssignedObjects().clear();
				getAssignedObjects().addAll((Collection<? extends UUIDObject>)newValue);
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
			case InputPackage.ASSIGNMENT__VESSELS:
				getVessels().clear();
				return;
			case InputPackage.ASSIGNMENT__ASSIGN_TO_SPOT:
				setAssignToSpot(ASSIGN_TO_SPOT_EDEFAULT);
				return;
			case InputPackage.ASSIGNMENT__ASSIGNED_OBJECTS:
				getAssignedObjects().clear();
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
			case InputPackage.ASSIGNMENT__VESSELS:
				return vessels != null && !vessels.isEmpty();
			case InputPackage.ASSIGNMENT__ASSIGN_TO_SPOT:
				return assignToSpot != ASSIGN_TO_SPOT_EDEFAULT;
			case InputPackage.ASSIGNMENT__ASSIGNED_OBJECTS:
				return assignedObjects != null && !assignedObjects.isEmpty();
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
		result.append(" (assignToSpot: ");
		result.append(assignToSpot);
		result.append(')');
		return result.toString();
	}

} // end of AssignmentImpl

// finish type fixing
