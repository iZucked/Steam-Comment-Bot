/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.impl;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.CargoType;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.types.AVesselSet;
import com.mmxlabs.models.lng.types.impl.ACargoImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Cargo</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.CargoImpl#getLoadSlot <em>Load Slot</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.CargoImpl#getDischargeSlot <em>Discharge Slot</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.CargoImpl#isAllowRewiring <em>Allow Rewiring</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.CargoImpl#getAllowedVessels <em>Allowed Vessels</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class CargoImpl extends ACargoImpl implements Cargo {
	/**
	 * The cached value of the '{@link #getLoadSlot() <em>Load Slot</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLoadSlot()
	 * @generated
	 * @ordered
	 */
	protected LoadSlot loadSlot;

	/**
	 * The cached value of the '{@link #getDischargeSlot() <em>Discharge Slot</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDischargeSlot()
	 * @generated
	 * @ordered
	 */
	protected DischargeSlot dischargeSlot;

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
	public LoadSlot getLoadSlot() {
		if (loadSlot != null && loadSlot.eIsProxy()) {
			InternalEObject oldLoadSlot = (InternalEObject)loadSlot;
			loadSlot = (LoadSlot)eResolveProxy(oldLoadSlot);
			if (loadSlot != oldLoadSlot) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, CargoPackage.CARGO__LOAD_SLOT, oldLoadSlot, loadSlot));
			}
		}
		return loadSlot;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public LoadSlot basicGetLoadSlot() {
		return loadSlot;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetLoadSlot(LoadSlot newLoadSlot, NotificationChain msgs) {
		LoadSlot oldLoadSlot = loadSlot;
		loadSlot = newLoadSlot;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, CargoPackage.CARGO__LOAD_SLOT, oldLoadSlot, newLoadSlot);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setLoadSlot(LoadSlot newLoadSlot) {
		if (newLoadSlot != loadSlot) {
			NotificationChain msgs = null;
			if (loadSlot != null)
				msgs = ((InternalEObject)loadSlot).eInverseRemove(this, CargoPackage.LOAD_SLOT__CARGO, LoadSlot.class, msgs);
			if (newLoadSlot != null)
				msgs = ((InternalEObject)newLoadSlot).eInverseAdd(this, CargoPackage.LOAD_SLOT__CARGO, LoadSlot.class, msgs);
			msgs = basicSetLoadSlot(newLoadSlot, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.CARGO__LOAD_SLOT, newLoadSlot, newLoadSlot));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DischargeSlot getDischargeSlot() {
		if (dischargeSlot != null && dischargeSlot.eIsProxy()) {
			InternalEObject oldDischargeSlot = (InternalEObject)dischargeSlot;
			dischargeSlot = (DischargeSlot)eResolveProxy(oldDischargeSlot);
			if (dischargeSlot != oldDischargeSlot) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, CargoPackage.CARGO__DISCHARGE_SLOT, oldDischargeSlot, dischargeSlot));
			}
		}
		return dischargeSlot;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DischargeSlot basicGetDischargeSlot() {
		return dischargeSlot;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetDischargeSlot(DischargeSlot newDischargeSlot, NotificationChain msgs) {
		DischargeSlot oldDischargeSlot = dischargeSlot;
		dischargeSlot = newDischargeSlot;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, CargoPackage.CARGO__DISCHARGE_SLOT, oldDischargeSlot, newDischargeSlot);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDischargeSlot(DischargeSlot newDischargeSlot) {
		if (newDischargeSlot != dischargeSlot) {
			NotificationChain msgs = null;
			if (dischargeSlot != null)
				msgs = ((InternalEObject)dischargeSlot).eInverseRemove(this, CargoPackage.DISCHARGE_SLOT__CARGO, DischargeSlot.class, msgs);
			if (newDischargeSlot != null)
				msgs = ((InternalEObject)newDischargeSlot).eInverseAdd(this, CargoPackage.DISCHARGE_SLOT__CARGO, DischargeSlot.class, msgs);
			msgs = basicSetDischargeSlot(newDischargeSlot, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.CARGO__DISCHARGE_SLOT, newDischargeSlot, newDischargeSlot));
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
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public CargoType getCargoType() {

		if (getLoadSlot() == null || getLoadSlot().isDESPurchase()) {
			return CargoType.DES;
		}
		if (getDischargeSlot() == null || getDischargeSlot().isFOBSale()) {
			return CargoType.FOB;
		}
		return CargoType.FLEET;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case CargoPackage.CARGO__LOAD_SLOT:
				if (loadSlot != null)
					msgs = ((InternalEObject)loadSlot).eInverseRemove(this, CargoPackage.LOAD_SLOT__CARGO, LoadSlot.class, msgs);
				return basicSetLoadSlot((LoadSlot)otherEnd, msgs);
			case CargoPackage.CARGO__DISCHARGE_SLOT:
				if (dischargeSlot != null)
					msgs = ((InternalEObject)dischargeSlot).eInverseRemove(this, CargoPackage.DISCHARGE_SLOT__CARGO, DischargeSlot.class, msgs);
				return basicSetDischargeSlot((DischargeSlot)otherEnd, msgs);
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
			case CargoPackage.CARGO__LOAD_SLOT:
				return basicSetLoadSlot(null, msgs);
			case CargoPackage.CARGO__DISCHARGE_SLOT:
				return basicSetDischargeSlot(null, msgs);
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
			case CargoPackage.CARGO__LOAD_SLOT:
				if (resolve) return getLoadSlot();
				return basicGetLoadSlot();
			case CargoPackage.CARGO__DISCHARGE_SLOT:
				if (resolve) return getDischargeSlot();
				return basicGetDischargeSlot();
			case CargoPackage.CARGO__ALLOW_REWIRING:
				return isAllowRewiring();
			case CargoPackage.CARGO__ALLOWED_VESSELS:
				return getAllowedVessels();
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
			case CargoPackage.CARGO__LOAD_SLOT:
				setLoadSlot((LoadSlot)newValue);
				return;
			case CargoPackage.CARGO__DISCHARGE_SLOT:
				setDischargeSlot((DischargeSlot)newValue);
				return;
			case CargoPackage.CARGO__ALLOW_REWIRING:
				setAllowRewiring((Boolean)newValue);
				return;
			case CargoPackage.CARGO__ALLOWED_VESSELS:
				getAllowedVessels().clear();
				getAllowedVessels().addAll((Collection<? extends AVesselSet>)newValue);
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
			case CargoPackage.CARGO__LOAD_SLOT:
				setLoadSlot((LoadSlot)null);
				return;
			case CargoPackage.CARGO__DISCHARGE_SLOT:
				setDischargeSlot((DischargeSlot)null);
				return;
			case CargoPackage.CARGO__ALLOW_REWIRING:
				unsetAllowRewiring();
				return;
			case CargoPackage.CARGO__ALLOWED_VESSELS:
				getAllowedVessels().clear();
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
			case CargoPackage.CARGO__LOAD_SLOT:
				return loadSlot != null;
			case CargoPackage.CARGO__DISCHARGE_SLOT:
				return dischargeSlot != null;
			case CargoPackage.CARGO__ALLOW_REWIRING:
				return isSetAllowRewiring();
			case CargoPackage.CARGO__ALLOWED_VESSELS:
				return allowedVessels != null && !allowedVessels.isEmpty();
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
