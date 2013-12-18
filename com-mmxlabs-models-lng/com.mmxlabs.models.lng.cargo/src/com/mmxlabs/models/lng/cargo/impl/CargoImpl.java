/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.impl;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;
import org.eclipse.emf.ecore.util.EObjectWithInverseResolvingEList;
import org.eclipse.emf.ecore.util.InternalEList;

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.CargoType;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.cargo.util.CargoSlotSorter;
import com.mmxlabs.models.lng.fleet.AssignableElement;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.types.AVesselSet;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.models.mmxcore.impl.UUIDObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Cargo</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.CargoImpl#getName <em>Name</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.CargoImpl#getAssignment <em>Assignment</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.CargoImpl#getSpotIndex <em>Spot Index</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.CargoImpl#getSequenceHint <em>Sequence Hint</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.CargoImpl#isLocked <em>Locked</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.CargoImpl#isAllowRewiring <em>Allow Rewiring</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.CargoImpl#getAllowedVessels <em>Allowed Vessels</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.CargoImpl#getSlots <em>Slots</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class CargoImpl extends UUIDObjectImpl implements Cargo {
	/**
	 * The default value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * @since 4.0
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected static final String NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * @since 4.0
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected String name = NAME_EDEFAULT;

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
	 * This is true if the Spot Index attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean spotIndexESet;

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
	 * The default value of the '{@link #isAllowRewiring() <em>Allow Rewiring</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isAllowRewiring()
	 * @generated
	 * @ordered
	 */
	protected static final boolean ALLOW_REWIRING_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isAllowRewiring() <em>Allow Rewiring</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isAllowRewiring()
	 * @generated
	 * @ordered
	 */
	protected boolean allowRewiring = ALLOW_REWIRING_EDEFAULT;

	/**
	 * The cached value of the '{@link #getAllowedVessels() <em>Allowed Vessels</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAllowedVessels()
	 * @generated
	 * @ordered
	 */
	protected EList<AVesselSet<Vessel>> allowedVessels;

	/**
	 * The cached value of the '{@link #getSlots() <em>Slots</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * @since 4.0
	 * <!-- end-user-doc -->
	 * @see #getSlots()
	 * @generated
	 * @ordered
	 */
	protected EList<Slot> slots;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected CargoImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return CargoPackage.Literals.CARGO;
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 4.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getName() {
		return name;
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 4.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setName(String newName) {
		String oldName = name;
		name = newName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.CARGO__NAME, oldName, name));
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
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, CargoPackage.CARGO__ASSIGNMENT, oldAssignment, assignment));
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
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.CARGO__ASSIGNMENT, oldAssignment, assignment));
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
		boolean oldSpotIndexESet = spotIndexESet;
		spotIndexESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.CARGO__SPOT_INDEX, oldSpotIndex, spotIndex, !oldSpotIndexESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetSpotIndex() {
		int oldSpotIndex = spotIndex;
		boolean oldSpotIndexESet = spotIndexESet;
		spotIndex = SPOT_INDEX_EDEFAULT;
		spotIndexESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, CargoPackage.CARGO__SPOT_INDEX, oldSpotIndex, SPOT_INDEX_EDEFAULT, oldSpotIndexESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetSpotIndex() {
		return spotIndexESet;
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
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.CARGO__SEQUENCE_HINT, oldSequenceHint, sequenceHint));
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
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.CARGO__LOCKED, oldLocked, locked));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isAllowRewiring() {
		return allowRewiring;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setAllowRewiring(boolean newAllowRewiring) {
		boolean oldAllowRewiring = allowRewiring;
		allowRewiring = newAllowRewiring;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.CARGO__ALLOW_REWIRING, oldAllowRewiring, allowRewiring));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<AVesselSet<Vessel>> getAllowedVessels() {
		if (allowedVessels == null) {
			allowedVessels = new EObjectResolvingEList<AVesselSet<Vessel>>(AVesselSet.class, this, CargoPackage.CARGO__ALLOWED_VESSELS);
		}
		return allowedVessels;
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 4.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Slot> getSlots() {
		if (slots == null) {
			slots = new EObjectWithInverseResolvingEList<Slot>(Slot.class, this, CargoPackage.CARGO__SLOTS, CargoPackage.SLOT__CARGO);
		}
		return slots;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public CargoType getCargoType() {

		for (final Slot slot : getSlots()) {
			if (slot instanceof LoadSlot) {
				if (((LoadSlot) slot).isDESPurchase()) {
					return CargoType.DES;
				}
			} else if (slot instanceof DischargeSlot) {
				if (((DischargeSlot) slot).isFOBSale()) {
					return CargoType.FOB;
				}
			}
		}
		
		return CargoType.FLEET;
	}

	/**
	 * <!-- begin-user-doc -->
	 * Returns date sorted copy of the {@link #getSlots()} {@link List}.
	 * @since 4.0
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public EList<Slot> getSortedSlots() {
		return CargoSlotSorter.sortedSlots(getSlots());
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case CargoPackage.CARGO__SLOTS:
				return ((InternalEList<InternalEObject>)(InternalEList<?>)getSlots()).basicAdd(otherEnd, msgs);
		}
		return super.eInverseAdd(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case CargoPackage.CARGO__SLOTS:
				return ((InternalEList<?>)getSlots()).basicRemove(otherEnd, msgs);
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
			case CargoPackage.CARGO__NAME:
				return getName();
			case CargoPackage.CARGO__ASSIGNMENT:
				if (resolve) return getAssignment();
				return basicGetAssignment();
			case CargoPackage.CARGO__SPOT_INDEX:
				return getSpotIndex();
			case CargoPackage.CARGO__SEQUENCE_HINT:
				return getSequenceHint();
			case CargoPackage.CARGO__LOCKED:
				return isLocked();
			case CargoPackage.CARGO__ALLOW_REWIRING:
				return isAllowRewiring();
			case CargoPackage.CARGO__ALLOWED_VESSELS:
				return getAllowedVessels();
			case CargoPackage.CARGO__SLOTS:
				return getSlots();
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
			case CargoPackage.CARGO__NAME:
				setName((String)newValue);
				return;
			case CargoPackage.CARGO__ASSIGNMENT:
				setAssignment((AVesselSet<? extends Vessel>)newValue);
				return;
			case CargoPackage.CARGO__SPOT_INDEX:
				setSpotIndex((Integer)newValue);
				return;
			case CargoPackage.CARGO__SEQUENCE_HINT:
				setSequenceHint((Integer)newValue);
				return;
			case CargoPackage.CARGO__LOCKED:
				setLocked((Boolean)newValue);
				return;
			case CargoPackage.CARGO__ALLOW_REWIRING:
				setAllowRewiring((Boolean)newValue);
				return;
			case CargoPackage.CARGO__ALLOWED_VESSELS:
				getAllowedVessels().clear();
				getAllowedVessels().addAll((Collection<? extends AVesselSet<Vessel>>)newValue);
				return;
			case CargoPackage.CARGO__SLOTS:
				getSlots().clear();
				getSlots().addAll((Collection<? extends Slot>)newValue);
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
			case CargoPackage.CARGO__NAME:
				setName(NAME_EDEFAULT);
				return;
			case CargoPackage.CARGO__ASSIGNMENT:
				setAssignment((AVesselSet<? extends Vessel>)null);
				return;
			case CargoPackage.CARGO__SPOT_INDEX:
				unsetSpotIndex();
				return;
			case CargoPackage.CARGO__SEQUENCE_HINT:
				setSequenceHint(SEQUENCE_HINT_EDEFAULT);
				return;
			case CargoPackage.CARGO__LOCKED:
				setLocked(LOCKED_EDEFAULT);
				return;
			case CargoPackage.CARGO__ALLOW_REWIRING:
				setAllowRewiring(ALLOW_REWIRING_EDEFAULT);
				return;
			case CargoPackage.CARGO__ALLOWED_VESSELS:
				getAllowedVessels().clear();
				return;
			case CargoPackage.CARGO__SLOTS:
				getSlots().clear();
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
			case CargoPackage.CARGO__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case CargoPackage.CARGO__ASSIGNMENT:
				return assignment != null;
			case CargoPackage.CARGO__SPOT_INDEX:
				return isSetSpotIndex();
			case CargoPackage.CARGO__SEQUENCE_HINT:
				return sequenceHint != SEQUENCE_HINT_EDEFAULT;
			case CargoPackage.CARGO__LOCKED:
				return locked != LOCKED_EDEFAULT;
			case CargoPackage.CARGO__ALLOW_REWIRING:
				return allowRewiring != ALLOW_REWIRING_EDEFAULT;
			case CargoPackage.CARGO__ALLOWED_VESSELS:
				return allowedVessels != null && !allowedVessels.isEmpty();
			case CargoPackage.CARGO__SLOTS:
				return slots != null && !slots.isEmpty();
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int eBaseStructuralFeatureID(int derivedFeatureID, Class<?> baseClass) {
		if (baseClass == NamedObject.class) {
			switch (derivedFeatureID) {
				case CargoPackage.CARGO__NAME: return MMXCorePackage.NAMED_OBJECT__NAME;
				default: return -1;
			}
		}
		if (baseClass == AssignableElement.class) {
			switch (derivedFeatureID) {
				case CargoPackage.CARGO__ASSIGNMENT: return FleetPackage.ASSIGNABLE_ELEMENT__ASSIGNMENT;
				case CargoPackage.CARGO__SPOT_INDEX: return FleetPackage.ASSIGNABLE_ELEMENT__SPOT_INDEX;
				case CargoPackage.CARGO__SEQUENCE_HINT: return FleetPackage.ASSIGNABLE_ELEMENT__SEQUENCE_HINT;
				case CargoPackage.CARGO__LOCKED: return FleetPackage.ASSIGNABLE_ELEMENT__LOCKED;
				default: return -1;
			}
		}
		return super.eBaseStructuralFeatureID(derivedFeatureID, baseClass);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int eDerivedStructuralFeatureID(int baseFeatureID, Class<?> baseClass) {
		if (baseClass == NamedObject.class) {
			switch (baseFeatureID) {
				case MMXCorePackage.NAMED_OBJECT__NAME: return CargoPackage.CARGO__NAME;
				default: return -1;
			}
		}
		if (baseClass == AssignableElement.class) {
			switch (baseFeatureID) {
				case FleetPackage.ASSIGNABLE_ELEMENT__ASSIGNMENT: return CargoPackage.CARGO__ASSIGNMENT;
				case FleetPackage.ASSIGNABLE_ELEMENT__SPOT_INDEX: return CargoPackage.CARGO__SPOT_INDEX;
				case FleetPackage.ASSIGNABLE_ELEMENT__SEQUENCE_HINT: return CargoPackage.CARGO__SEQUENCE_HINT;
				case FleetPackage.ASSIGNABLE_ELEMENT__LOCKED: return CargoPackage.CARGO__LOCKED;
				default: return -1;
			}
		}
		return super.eDerivedStructuralFeatureID(baseFeatureID, baseClass);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eInvoke(int operationID, EList<?> arguments) throws InvocationTargetException {
		switch (operationID) {
			case CargoPackage.CARGO___GET_CARGO_TYPE:
				return getCargoType();
			case CargoPackage.CARGO___GET_SORTED_SLOTS:
				return getSortedSlots();
		}
		return super.eInvoke(operationID, arguments);
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
		result.append(" (name: ");
		result.append(name);
		result.append(", spotIndex: ");
		if (spotIndexESet) result.append(spotIndex); else result.append("<unset>");
		result.append(", sequenceHint: ");
		result.append(sequenceHint);
		result.append(", locked: ");
		result.append(locked);
		result.append(", allowRewiring: ");
		result.append(allowRewiring);
		result.append(')');
		return result.toString();
	}

} // end of CargoImpl

// finish type fixing
