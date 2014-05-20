/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.impl;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.SpotDischargeSlot;
import com.mmxlabs.models.lng.cargo.SpotSlot;
import com.mmxlabs.models.lng.spotmarkets.SpotMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Spot Discharge Slot</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.SpotDischargeSlotImpl#getMarket <em>Market</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class SpotDischargeSlotImpl extends DischargeSlotImpl implements SpotDischargeSlot {
	/**
	 * The cached value of the '{@link #getMarket() <em>Market</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMarket()
	 * @generated
	 * @ordered
	 */
	protected SpotMarket market;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected SpotDischargeSlotImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return CargoPackage.Literals.SPOT_DISCHARGE_SLOT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SpotMarket getMarket() {
		if (market != null && market.eIsProxy()) {
			InternalEObject oldMarket = (InternalEObject)market;
			market = (SpotMarket)eResolveProxy(oldMarket);
			if (market != oldMarket) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, CargoPackage.SPOT_DISCHARGE_SLOT__MARKET, oldMarket, market));
			}
		}
		return market;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SpotMarket basicGetMarket() {
		return market;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMarket(SpotMarket newMarket) {
		SpotMarket oldMarket = market;
		market = newMarket;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.SPOT_DISCHARGE_SLOT__MARKET, oldMarket, market));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case CargoPackage.SPOT_DISCHARGE_SLOT__MARKET:
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
			case CargoPackage.SPOT_DISCHARGE_SLOT__MARKET:
				setMarket((SpotMarket)newValue);
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
			case CargoPackage.SPOT_DISCHARGE_SLOT__MARKET:
				setMarket((SpotMarket)null);
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
			case CargoPackage.SPOT_DISCHARGE_SLOT__MARKET:
				return market != null;
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
		if (baseClass == SpotSlot.class) {
			switch (derivedFeatureID) {
				case CargoPackage.SPOT_DISCHARGE_SLOT__MARKET: return CargoPackage.SPOT_SLOT__MARKET;
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
		if (baseClass == SpotSlot.class) {
			switch (baseFeatureID) {
				case CargoPackage.SPOT_SLOT__MARKET: return CargoPackage.SPOT_DISCHARGE_SLOT__MARKET;
				default: return -1;
			}
		}
		return super.eDerivedStructuralFeatureID(baseFeatureID, baseClass);
	}

	/**
	 * @generated NOT
	 */
	@Override
	public DelegateInformation getUnsetValueOrDelegate(EStructuralFeature feature) {
		if (feature == CargoPackage.Literals.SLOT__ENTITY) {
			// delegate entity information to the spot market, not the contract
			return new DelegateInformation(CargoPackage.Literals.SPOT_SLOT__MARKET, SpotMarketsPackage.Literals.SPOT_MARKET__ENTITY, null);
		} else if (feature == CargoPackage.Literals.SLOT__MIN_QUANTITY) {
			return new DelegateInformation(null, null, null) {
				public boolean delegatesTo(final Object changedFeature) {
					return (changedFeature == CargoPackage.Literals.SPOT_SLOT__MARKET);
				}

				public Object getValue(final EObject object) {
					Object result = null;
					SpotMarket spotMarket = getMarket();
					if (result == null && spotMarket != null) {
						result = spotMarket.eGetWithDefault(SpotMarketsPackage.Literals.SPOT_MARKET__MIN_QUANTITY);
					}
					if (result == null) {
						result = Integer.valueOf(0);
					}
					return result;

				}
			};
		} else if (feature == CargoPackage.Literals.SLOT__MAX_QUANTITY) {
			return new DelegateInformation(null, null, null) {
				public boolean delegatesTo(final Object changedFeature) {
					return (changedFeature == CargoPackage.Literals.SPOT_SLOT__MARKET);
				}
				
				public Object getValue(final EObject object) {
					Object result = null;
					SpotMarket spotMarket = getMarket();
					if (result == null && spotMarket != null) {
						result = spotMarket.eGetWithDefault(SpotMarketsPackage.Literals.SPOT_MARKET__MAX_QUANTITY);
					}
					if (result == null || result.equals(0)) {
						result = Integer.valueOf(140000);
					}
					return result;
					
				}
			};
		}
		return super.getUnsetValueOrDelegate(feature);
	}	
} // end of SpotDischargeSlotImpl

// finish type fixing
