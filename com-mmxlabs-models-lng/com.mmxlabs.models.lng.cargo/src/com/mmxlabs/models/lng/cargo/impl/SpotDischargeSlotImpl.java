/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.impl;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.SpotDischargeSlot;
import com.mmxlabs.models.lng.cargo.SpotSlot;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.commercial.PricingEvent;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortPackage;
import com.mmxlabs.models.lng.spotmarkets.DESSalesMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage;
import com.mmxlabs.models.lng.types.APortSet;
import com.mmxlabs.models.lng.types.AVesselSet;
import com.mmxlabs.models.lng.types.TimePeriod;
import com.mmxlabs.models.lng.types.VolumeUnits;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Spot Discharge Slot</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.SpotDischargeSlotImpl#getMarket <em>Market</em>}</li>
 * </ul>
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
	@Override
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
	@Override
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
			case CargoPackage.SPOT_DISCHARGE_SLOT__RESTRICTED_VESSELS:
				if (resolve) return (Object) getVesselRestrictions();
			case CargoPackage.SPOT_DISCHARGE_SLOT__RESTRICTED_CONTRACTS:
				if (resolve) return (Object) getContractRestrictions();
			case CargoPackage.SPOT_DISCHARGE_SLOT__RESTRICTED_PORTS:
				if (resolve) return (Object) getPortRestrictions();
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
		} else if (feature == CargoPackage.Literals.SLOT__VOLUME_LIMITS_UNIT) {
			return new DelegateInformation(null, null, null) {
				public boolean delegatesTo(final Object changedFeature) {
					return (changedFeature == CargoPackage.Literals.SPOT_SLOT__MARKET);
				}

				public Object getValue(final EObject object) {
					Object result = null;
					SpotMarket spotMarket = getMarket();
					if (result == null && spotMarket != null) {
						result = spotMarket.eGetWithDefault(SpotMarketsPackage.Literals.SPOT_MARKET__VOLUME_LIMITS_UNIT);
					}
					if (result == null) {
						result = VolumeUnits.M3;
					}
					return result;
				}
			};
		} else if (feature == CargoPackage.Literals.SLOT__PRICING_EVENT) {
			return new DelegateInformation(CargoPackage.eINSTANCE.getSpotSlot_Market(), SpotMarketsPackage.Literals.SPOT_MARKET__PRICING_EVENT, PricingEvent.START_DISCHARGE);
		} else if (CargoPackage.Literals.SLOT__WINDOW_SIZE == feature) {
			return new DelegateInformation(null, null, null) {
				public boolean delegatesTo(final Object changedFeature) {
					return (changedFeature == CargoPackage.Literals.SPOT_SLOT__MARKET);
				}

				public Object getValue(final EObject object) {
					return 1;
				}
			};
		} else if (CargoPackage.Literals.SLOT__WINDOW_SIZE_UNITS == feature) {
			return new DelegateInformation(CargoPackage.eINSTANCE.getSlot_Port(), PortPackage.eINSTANCE.getPort_DefaultWindowSizeUnits(), TimePeriod.MONTHS);
		} else if (feature == CargoPackage.Literals.SLOT__RESTRICTED_CONTRACTS) {
			return new DelegateInformation(CargoPackage.Literals.SPOT_SLOT__MARKET, SpotMarketsPackage.Literals.SPOT_MARKET__RESTRICTED_CONTRACTS, null);
		} else if (feature == CargoPackage.Literals.SLOT__RESTRICTED_PORTS) {
			return new DelegateInformation(CargoPackage.Literals.SPOT_SLOT__MARKET, SpotMarketsPackage.Literals.SPOT_MARKET__RESTRICTED_PORTS, null);
		} else if (feature == CargoPackage.Literals.SLOT__RESTRICTED_VESSELS) {
			return new DelegateInformation(CargoPackage.Literals.SPOT_SLOT__MARKET, SpotMarketsPackage.Literals.SPOT_MARKET__RESTRICTED_VESSELS, null);
		} else if (feature == CargoPackage.Literals.SLOT__RESTRICTED_CONTRACTS_ARE_PERMISSIVE) {
			return new DelegateInformation(CargoPackage.Literals.SPOT_SLOT__MARKET, SpotMarketsPackage.Literals.SPOT_MARKET__RESTRICTED_CONTRACTS_ARE_PERMISSIVE, Boolean.FALSE);
		} else if (feature == CargoPackage.Literals.SLOT__RESTRICTED_PORTS_ARE_PERMISSIVE) {
			return new DelegateInformation(CargoPackage.Literals.SPOT_SLOT__MARKET, SpotMarketsPackage.Literals.SPOT_MARKET__RESTRICTED_PORTS_ARE_PERMISSIVE, Boolean.FALSE);
		} else if (feature == CargoPackage.Literals.SLOT__RESTRICTED_VESSELS_ARE_PERMISSIVE) {
			return new DelegateInformation(CargoPackage.Literals.SPOT_SLOT__MARKET, SpotMarketsPackage.Literals.SPOT_MARKET__RESTRICTED_VESSELS_ARE_PERMISSIVE, Boolean.FALSE);
		}
		return super.getUnsetValueOrDelegate(feature);
	}
	
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public EList<AVesselSet<Vessel>> getSlotOrDelegateVesselRestrictions() {
		return getVesselRestrictions();
	}
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public EList<AVesselSet<Vessel>> getVesselRestrictions() {
		if (getMarket() == null) return super.getRestrictedVessels();
		return getMarket().getRestrictedVessels();
	}
	
	//return getSlotOrDelegatePortRestrictions();
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public EList<Contract> getSlotOrDelegateContractRestrictions() {
		return getContractRestrictions();
	}
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public EList<Contract> getContractRestrictions() {
		if (getMarket() == null) return super.getRestrictedContracts();
		return getMarket().getRestrictedContracts();
	}
	
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public EList<APortSet<Port>> getSlotOrDelegatePortRestrictions() {
		return getPortRestrictions();
	}
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public EList<APortSet<Port>> getPortRestrictions() {
		if (getMarket() == null) return super.getRestrictedPorts();
		return getMarket().getRestrictedPorts();
	}
	
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT.
	 */
	@Override
	public int getSlotOrDelegateDaysBuffer() {
		if (this.getMarket() instanceof DESSalesMarket) {
			return ((DESSalesMarket)this.getMarket()).getDaysBuffer();
		}
		return super.getSlotOrDelegateDaysBuffer();
	}
} // end of SpotDischargeSlotImpl

// finish type fixing
