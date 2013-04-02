/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.impl;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.CargoType;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.types.AVesselSet;
import com.mmxlabs.models.lng.types.impl.ACargoImpl;
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
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.types.AVesselSet;
import com.mmxlabs.models.lng.types.impl.ACargoImpl;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
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
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.types.AVesselSet;
import com.mmxlabs.models.lng.types.impl.ACargoImpl;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;
import org.eclipse.emf.ecore.util.EObjectWithInverseResolvingEList;
import org.eclipse.emf.ecore.util.InternalEList;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.util.CargoSlotSorter;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Cargo</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.CargoImpl#isAllowRewiring <em>Allow Rewiring</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.CargoImpl#getAllowedVessels <em>Allowed Vessels</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.CargoImpl#getSlots <em>Slots</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class CargoImpl extends ACargoImpl implements Cargo {
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
	 * This is true if the Allow Rewiring attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean allowRewiringESet;

	/**
	 * The cached value of the '{@link #getAllowedVessels() <em>Allowed Vessels</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAllowedVessels()
	 * @generated
	 * @ordered
	 */
	protected EList<AVesselSet> allowedVessels;

	/**
	 * The cached value of the '{@link #getSlots() <em>Slots</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * @since 3.0
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
		boolean oldAllowRewiringESet = allowRewiringESet;
		allowRewiringESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.CARGO__ALLOW_REWIRING, oldAllowRewiring, allowRewiring, !oldAllowRewiringESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetAllowRewiring() {
		boolean oldAllowRewiring = allowRewiring;
		boolean oldAllowRewiringESet = allowRewiringESet;
		allowRewiring = ALLOW_REWIRING_EDEFAULT;
		allowRewiringESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, CargoPackage.CARGO__ALLOW_REWIRING, oldAllowRewiring, ALLOW_REWIRING_EDEFAULT, oldAllowRewiringESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetAllowRewiring() {
		return allowRewiringESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<AVesselSet> getAllowedVessels() {
		if (allowedVessels == null) {
			allowedVessels = new EObjectResolvingEList<AVesselSet>(AVesselSet.class, this, CargoPackage.CARGO__ALLOWED_VESSELS);
		}
		return allowedVessels;
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 3.0
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
	 * @since 3.0
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
			case CargoPackage.CARGO__ALLOW_REWIRING:
				setAllowRewiring((Boolean)newValue);
				return;
			case CargoPackage.CARGO__ALLOWED_VESSELS:
				getAllowedVessels().clear();
				getAllowedVessels().addAll((Collection<? extends AVesselSet>)newValue);
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
			case CargoPackage.CARGO__ALLOW_REWIRING:
				unsetAllowRewiring();
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
			case CargoPackage.CARGO__ALLOW_REWIRING:
				return isSetAllowRewiring();
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
		result.append(" (allowRewiring: ");
		if (allowRewiringESet) result.append(allowRewiring); else result.append("<unset>");
		result.append(')');
		return result.toString();
	}

} // end of CargoImpl

// finish type fixing
