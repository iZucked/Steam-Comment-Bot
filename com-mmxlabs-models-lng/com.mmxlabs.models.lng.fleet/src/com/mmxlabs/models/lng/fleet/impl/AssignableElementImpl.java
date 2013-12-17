/**
 */
package com.mmxlabs.models.lng.fleet.impl;

import com.mmxlabs.models.lng.fleet.AssignableElement;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.types.AVesselSet;
import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Assignable Element</b></em>'.
 * @since 8.0
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.AssignableElementImpl#getAssignment <em>Assignment</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.AssignableElementImpl#getSpotIndex <em>Spot Index</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.AssignableElementImpl#getSequenceHint <em>Sequence Hint</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.AssignableElementImpl#isLocked <em>Locked</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public abstract class AssignableElementImpl extends EObjectImpl implements AssignableElement {
	/**
	 * The cached value of the '{@link #getAssignment() <em>Assignment</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAssignment()
	 * @generated
	 * @ordered
	 */
	protected AVesselSet<? extends Vessel> assignment;

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
		return FleetPackage.Literals.ASSIGNABLE_ELEMENT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	public AVesselSet<? extends Vessel> getAssignment() {
		if (assignment != null && assignment.eIsProxy()) {
			InternalEObject oldAssignment = (InternalEObject)assignment;
			assignment = (AVesselSet<? extends Vessel>)eResolveProxy(oldAssignment);
			if (assignment != oldAssignment) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, FleetPackage.ASSIGNABLE_ELEMENT__ASSIGNMENT, oldAssignment, assignment));
			}
		}
		return assignment;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AVesselSet<? extends Vessel> basicGetAssignment() {
		return assignment;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setAssignment(AVesselSet<? extends Vessel> newAssignment) {
		AVesselSet<? extends Vessel> oldAssignment = assignment;
		assignment = newAssignment;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.ASSIGNABLE_ELEMENT__ASSIGNMENT, oldAssignment, assignment));
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
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.ASSIGNABLE_ELEMENT__SPOT_INDEX, oldSpotIndex, spotIndex));
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
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.ASSIGNABLE_ELEMENT__SEQUENCE_HINT, oldSequenceHint, sequenceHint));
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
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.ASSIGNABLE_ELEMENT__LOCKED, oldLocked, locked));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case FleetPackage.ASSIGNABLE_ELEMENT__ASSIGNMENT:
				if (resolve) return getAssignment();
				return basicGetAssignment();
			case FleetPackage.ASSIGNABLE_ELEMENT__SPOT_INDEX:
				return getSpotIndex();
			case FleetPackage.ASSIGNABLE_ELEMENT__SEQUENCE_HINT:
				return getSequenceHint();
			case FleetPackage.ASSIGNABLE_ELEMENT__LOCKED:
				return isLocked();
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
			case FleetPackage.ASSIGNABLE_ELEMENT__ASSIGNMENT:
				setAssignment((AVesselSet<? extends Vessel>)newValue);
				return;
			case FleetPackage.ASSIGNABLE_ELEMENT__SPOT_INDEX:
				setSpotIndex((Integer)newValue);
				return;
			case FleetPackage.ASSIGNABLE_ELEMENT__SEQUENCE_HINT:
				setSequenceHint((Integer)newValue);
				return;
			case FleetPackage.ASSIGNABLE_ELEMENT__LOCKED:
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
			case FleetPackage.ASSIGNABLE_ELEMENT__ASSIGNMENT:
				setAssignment((AVesselSet<? extends Vessel>)null);
				return;
			case FleetPackage.ASSIGNABLE_ELEMENT__SPOT_INDEX:
				setSpotIndex(SPOT_INDEX_EDEFAULT);
				return;
			case FleetPackage.ASSIGNABLE_ELEMENT__SEQUENCE_HINT:
				setSequenceHint(SEQUENCE_HINT_EDEFAULT);
				return;
			case FleetPackage.ASSIGNABLE_ELEMENT__LOCKED:
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
			case FleetPackage.ASSIGNABLE_ELEMENT__ASSIGNMENT:
				return assignment != null;
			case FleetPackage.ASSIGNABLE_ELEMENT__SPOT_INDEX:
				return spotIndex != SPOT_INDEX_EDEFAULT;
			case FleetPackage.ASSIGNABLE_ELEMENT__SEQUENCE_HINT:
				return sequenceHint != SEQUENCE_HINT_EDEFAULT;
			case FleetPackage.ASSIGNABLE_ELEMENT__LOCKED:
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
		result.append(" (spotIndex: ");
		result.append(spotIndex);
		result.append(", sequenceHint: ");
		result.append(sequenceHint);
		result.append(", locked: ");
		result.append(locked);
		result.append(')');
		return result.toString();
	}

} //AssignableElementImpl
