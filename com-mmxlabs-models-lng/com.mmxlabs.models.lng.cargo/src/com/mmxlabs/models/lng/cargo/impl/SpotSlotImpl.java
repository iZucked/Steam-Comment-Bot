/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.impl;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.SpotSlot;
import com.mmxlabs.models.lng.types.ASpotMarket;
import com.mmxlabs.models.mmxcore.impl.MMXObjectImpl;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Spot Slot</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.SpotSlotImpl#getMarket <em>Market</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class SpotSlotImpl extends MMXObjectImpl implements SpotSlot {
	/**
	 * The cached value of the '{@link #getMarket() <em>Market</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMarket()
	 * @generated
	 * @ordered
	 */
	protected ASpotMarket market;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected SpotSlotImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return CargoPackage.Literals.SPOT_SLOT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ASpotMarket getMarket() {
		if (market != null && market.eIsProxy()) {
			InternalEObject oldMarket = (InternalEObject)market;
			market = (ASpotMarket)eResolveProxy(oldMarket);
			if (market != oldMarket) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, CargoPackage.SPOT_SLOT__MARKET, oldMarket, market));
			}
		}
		return market;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ASpotMarket basicGetMarket() {
		return market;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMarket(ASpotMarket newMarket) {
		ASpotMarket oldMarket = market;
		market = newMarket;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.SPOT_SLOT__MARKET, oldMarket, market));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case CargoPackage.SPOT_SLOT__MARKET:
				if (resolve) return getMarket();
				return basicGetMarket();
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
			case CargoPackage.SPOT_SLOT__MARKET:
				setMarket((ASpotMarket)newValue);
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
			case CargoPackage.SPOT_SLOT__MARKET:
				setMarket((ASpotMarket)null);
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
			case CargoPackage.SPOT_SLOT__MARKET:
				return market != null;
		}
		return super.eIsSet(featureID);
	}

} // end of SpotSlotImpl

// finish type fixing
