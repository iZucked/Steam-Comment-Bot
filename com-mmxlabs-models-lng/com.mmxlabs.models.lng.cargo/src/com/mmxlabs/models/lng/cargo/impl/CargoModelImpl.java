/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.impl;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;
import org.eclipse.emf.ecore.util.InternalEList;

import com.mmxlabs.models.lng.cargo.CanalBookings;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoGroup;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.CharterInMarketOverride;
import com.mmxlabs.models.lng.cargo.DealSet;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.Inventory;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.PaperDeal;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.cargo.VesselTypeGroup;
import com.mmxlabs.models.mmxcore.impl.UUIDObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Model</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.CargoModelImpl#getLoadSlots <em>Load Slots</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.CargoModelImpl#getDischargeSlots <em>Discharge Slots</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.CargoModelImpl#getCargoes <em>Cargoes</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.CargoModelImpl#getCargoGroups <em>Cargo Groups</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.CargoModelImpl#getVesselAvailabilities <em>Vessel Availabilities</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.CargoModelImpl#getVesselEvents <em>Vessel Events</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.CargoModelImpl#getVesselTypeGroups <em>Vessel Type Groups</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.CargoModelImpl#getInventoryModels <em>Inventory Models</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.CargoModelImpl#getCanalBookings <em>Canal Bookings</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.CargoModelImpl#getCharterInMarketOverrides <em>Charter In Market Overrides</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.CargoModelImpl#getPaperDeals <em>Paper Deals</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.CargoModelImpl#getDealSets <em>Deal Sets</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.CargoModelImpl#getCargoesForExposures <em>Cargoes For Exposures</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.cargo.impl.CargoModelImpl#getCargoesForHedging <em>Cargoes For Hedging</em>}</li>
 * </ul>
 *
 * @generated
 */
public class CargoModelImpl extends UUIDObjectImpl implements CargoModel {
	/**
	 * The cached value of the '{@link #getLoadSlots() <em>Load Slots</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLoadSlots()
	 * @generated
	 * @ordered
	 */
	protected EList<LoadSlot> loadSlots;

	/**
	 * The cached value of the '{@link #getDischargeSlots() <em>Discharge Slots</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDischargeSlots()
	 * @generated
	 * @ordered
	 */
	protected EList<DischargeSlot> dischargeSlots;

	/**
	 * The cached value of the '{@link #getCargoes() <em>Cargoes</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCargoes()
	 * @generated
	 * @ordered
	 */
	protected EList<Cargo> cargoes;

	/**
	 * The cached value of the '{@link #getCargoGroups() <em>Cargo Groups</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCargoGroups()
	 * @generated
	 * @ordered
	 */
	protected EList<CargoGroup> cargoGroups;

	/**
	 * The cached value of the '{@link #getVesselAvailabilities() <em>Vessel Availabilities</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVesselAvailabilities()
	 * @generated
	 * @ordered
	 */
	protected EList<VesselAvailability> vesselAvailabilities;

	/**
	 * The cached value of the '{@link #getVesselEvents() <em>Vessel Events</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVesselEvents()
	 * @generated
	 * @ordered
	 */
	protected EList<VesselEvent> vesselEvents;

	/**
	 * The cached value of the '{@link #getVesselTypeGroups() <em>Vessel Type Groups</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVesselTypeGroups()
	 * @generated
	 * @ordered
	 */
	protected EList<VesselTypeGroup> vesselTypeGroups;

	/**
	 * The cached value of the '{@link #getInventoryModels() <em>Inventory Models</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getInventoryModels()
	 * @generated
	 * @ordered
	 */
	protected EList<Inventory> inventoryModels;

	/**
	 * The cached value of the '{@link #getCanalBookings() <em>Canal Bookings</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCanalBookings()
	 * @generated
	 * @ordered
	 */
	protected CanalBookings canalBookings;

	/**
	 * The cached value of the '{@link #getCharterInMarketOverrides() <em>Charter In Market Overrides</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCharterInMarketOverrides()
	 * @generated
	 * @ordered
	 */
	protected EList<CharterInMarketOverride> charterInMarketOverrides;

	/**
	 * The cached value of the '{@link #getPaperDeals() <em>Paper Deals</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPaperDeals()
	 * @generated
	 * @ordered
	 */
	protected EList<PaperDeal> paperDeals;

	/**
	 * The cached value of the '{@link #getDealSets() <em>Deal Sets</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDealSets()
	 * @generated
	 * @ordered
	 */
	protected EList<DealSet> dealSets;

	
	/**
	 * The cached value of the '{@link #getCargoesForExposures() <em>Cargoes For Exposures</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCargoesForExposures()
	 * @generated
	 * @ordered
	 */
	protected EList<Cargo> cargoesForExposures;

	/**
	 * The cached value of the '{@link #getCargoesForHedging() <em>Cargoes For Hedging</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCargoesForHedging()
	 * @generated
	 * @ordered
	 */
	protected EList<Cargo> cargoesForHedging;

	private Map< String, LoadSlot> lookupLoadSlotCache;
	private Map< String, DischargeSlot> lookupDischargeSlotCache;
	
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	protected CargoModelImpl() {
		super();
		eAdapters().add(new EContentAdapter() {
			@Override
			public void notifyChanged(Notification notification) {
				super.notifyChanged(notification);
				
				if (notification.isTouch()) {
					return;
				}
				if (lookupLoadSlotCache != null && notification.getNotifier() instanceof LoadSlot || notification.getFeature() == CargoPackage.Literals.CARGO_MODEL__LOAD_SLOTS) {
					lookupLoadSlotCache = null;
				}
				if (lookupDischargeSlotCache != null && notification.getNotifier() instanceof DischargeSlot || notification.getFeature() == CargoPackage.Literals.CARGO_MODEL__DISCHARGE_SLOTS) {
					lookupDischargeSlotCache = null;
				}
			}
		});
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return CargoPackage.Literals.CARGO_MODEL;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<LoadSlot> getLoadSlots() {
		if (loadSlots == null) {
			loadSlots = new EObjectContainmentEList.Resolving<LoadSlot>(LoadSlot.class, this, CargoPackage.CARGO_MODEL__LOAD_SLOTS);
		}
		return loadSlots;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<DischargeSlot> getDischargeSlots() {
		if (dischargeSlots == null) {
			dischargeSlots = new EObjectContainmentEList.Resolving<DischargeSlot>(DischargeSlot.class, this, CargoPackage.CARGO_MODEL__DISCHARGE_SLOTS);
		}
		return dischargeSlots;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<Cargo> getCargoes() {
		if (cargoes == null) {
			cargoes = new EObjectContainmentEList.Resolving<Cargo>(Cargo.class, this, CargoPackage.CARGO_MODEL__CARGOES);
		}
		return cargoes;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<CargoGroup> getCargoGroups() {
		if (cargoGroups == null) {
			cargoGroups = new EObjectContainmentEList.Resolving<CargoGroup>(CargoGroup.class, this, CargoPackage.CARGO_MODEL__CARGO_GROUPS);
		}
		return cargoGroups;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<VesselAvailability> getVesselAvailabilities() {
		if (vesselAvailabilities == null) {
			vesselAvailabilities = new EObjectContainmentEList.Resolving<VesselAvailability>(VesselAvailability.class, this, CargoPackage.CARGO_MODEL__VESSEL_AVAILABILITIES);
		}
		return vesselAvailabilities;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<VesselEvent> getVesselEvents() {
		if (vesselEvents == null) {
			vesselEvents = new EObjectContainmentEList.Resolving<VesselEvent>(VesselEvent.class, this, CargoPackage.CARGO_MODEL__VESSEL_EVENTS);
		}
		return vesselEvents;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<VesselTypeGroup> getVesselTypeGroups() {
		if (vesselTypeGroups == null) {
			vesselTypeGroups = new EObjectContainmentEList.Resolving<VesselTypeGroup>(VesselTypeGroup.class, this, CargoPackage.CARGO_MODEL__VESSEL_TYPE_GROUPS);
		}
		return vesselTypeGroups;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<Inventory> getInventoryModels() {
		if (inventoryModels == null) {
			inventoryModels = new EObjectContainmentEList.Resolving<Inventory>(Inventory.class, this, CargoPackage.CARGO_MODEL__INVENTORY_MODELS);
		}
		return inventoryModels;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public CanalBookings getCanalBookings() {
		if (canalBookings != null && canalBookings.eIsProxy()) {
			InternalEObject oldCanalBookings = (InternalEObject)canalBookings;
			canalBookings = (CanalBookings)eResolveProxy(oldCanalBookings);
			if (canalBookings != oldCanalBookings) {
				InternalEObject newCanalBookings = (InternalEObject)canalBookings;
				NotificationChain msgs = oldCanalBookings.eInverseRemove(this, EOPPOSITE_FEATURE_BASE - CargoPackage.CARGO_MODEL__CANAL_BOOKINGS, null, null);
				if (newCanalBookings.eInternalContainer() == null) {
					msgs = newCanalBookings.eInverseAdd(this, EOPPOSITE_FEATURE_BASE - CargoPackage.CARGO_MODEL__CANAL_BOOKINGS, null, msgs);
				}
				if (msgs != null) msgs.dispatch();
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, CargoPackage.CARGO_MODEL__CANAL_BOOKINGS, oldCanalBookings, canalBookings));
			}
		}
		return canalBookings;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CanalBookings basicGetCanalBookings() {
		return canalBookings;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetCanalBookings(CanalBookings newCanalBookings, NotificationChain msgs) {
		CanalBookings oldCanalBookings = canalBookings;
		canalBookings = newCanalBookings;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, CargoPackage.CARGO_MODEL__CANAL_BOOKINGS, oldCanalBookings, newCanalBookings);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setCanalBookings(CanalBookings newCanalBookings) {
		if (newCanalBookings != canalBookings) {
			NotificationChain msgs = null;
			if (canalBookings != null)
				msgs = ((InternalEObject)canalBookings).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - CargoPackage.CARGO_MODEL__CANAL_BOOKINGS, null, msgs);
			if (newCanalBookings != null)
				msgs = ((InternalEObject)newCanalBookings).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - CargoPackage.CARGO_MODEL__CANAL_BOOKINGS, null, msgs);
			msgs = basicSetCanalBookings(newCanalBookings, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CargoPackage.CARGO_MODEL__CANAL_BOOKINGS, newCanalBookings, newCanalBookings));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<CharterInMarketOverride> getCharterInMarketOverrides() {
		if (charterInMarketOverrides == null) {
			charterInMarketOverrides = new EObjectContainmentEList.Resolving<CharterInMarketOverride>(CharterInMarketOverride.class, this, CargoPackage.CARGO_MODEL__CHARTER_IN_MARKET_OVERRIDES);
		}
		return charterInMarketOverrides;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<PaperDeal> getPaperDeals() {
		if (paperDeals == null) {
			paperDeals = new EObjectContainmentEList.Resolving<PaperDeal>(PaperDeal.class, this, CargoPackage.CARGO_MODEL__PAPER_DEALS);
		}
		return paperDeals;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<DealSet> getDealSets() {
		if (dealSets == null) {
			dealSets = new EObjectContainmentEList.Resolving<DealSet>(DealSet.class, this, CargoPackage.CARGO_MODEL__DEAL_SETS);
		}
		return dealSets;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<Cargo> getCargoesForExposures() {
		if (cargoesForExposures == null) {
			cargoesForExposures = new EObjectResolvingEList<Cargo>(Cargo.class, this, CargoPackage.CARGO_MODEL__CARGOES_FOR_EXPOSURES);
		}
		return cargoesForExposures;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<Cargo> getCargoesForHedging() {
		if (cargoesForHedging == null) {
			cargoesForHedging = new EObjectResolvingEList<Cargo>(Cargo.class, this, CargoPackage.CARGO_MODEL__CARGOES_FOR_HEDGING);
		}
		return cargoesForHedging;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public LoadSlot getLoadSlotByName(String name) {
		Map<String, LoadSlot> pLookupLoadSlotCache = lookupLoadSlotCache;
		if (pLookupLoadSlotCache == null) {
			synchronized (this) {
				if (lookupLoadSlotCache == null) {
					pLookupLoadSlotCache = new HashMap<>();
					for (LoadSlot s : getLoadSlots()) {
						pLookupLoadSlotCache.put(s.getName(), s);
					}
					lookupLoadSlotCache = pLookupLoadSlotCache;
				}
			}
		}
		return pLookupLoadSlotCache.get(name);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public DischargeSlot getDischargeSlotByName(String name) {
		Map<String, DischargeSlot> pLookupDischargeSlotCache = lookupDischargeSlotCache;
		if (pLookupDischargeSlotCache == null) {
			synchronized (this) {
				if (lookupDischargeSlotCache == null) {
					pLookupDischargeSlotCache = new HashMap<>();
					for (DischargeSlot s : getDischargeSlots()) {
						pLookupDischargeSlotCache.put(s.getName(), s);
					}
					lookupDischargeSlotCache = pLookupDischargeSlotCache;
				}
			}
		}
		return pLookupDischargeSlotCache.get(name);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case CargoPackage.CARGO_MODEL__LOAD_SLOTS:
				return ((InternalEList<?>)getLoadSlots()).basicRemove(otherEnd, msgs);
			case CargoPackage.CARGO_MODEL__DISCHARGE_SLOTS:
				return ((InternalEList<?>)getDischargeSlots()).basicRemove(otherEnd, msgs);
			case CargoPackage.CARGO_MODEL__CARGOES:
				return ((InternalEList<?>)getCargoes()).basicRemove(otherEnd, msgs);
			case CargoPackage.CARGO_MODEL__CARGO_GROUPS:
				return ((InternalEList<?>)getCargoGroups()).basicRemove(otherEnd, msgs);
			case CargoPackage.CARGO_MODEL__VESSEL_AVAILABILITIES:
				return ((InternalEList<?>)getVesselAvailabilities()).basicRemove(otherEnd, msgs);
			case CargoPackage.CARGO_MODEL__VESSEL_EVENTS:
				return ((InternalEList<?>)getVesselEvents()).basicRemove(otherEnd, msgs);
			case CargoPackage.CARGO_MODEL__VESSEL_TYPE_GROUPS:
				return ((InternalEList<?>)getVesselTypeGroups()).basicRemove(otherEnd, msgs);
			case CargoPackage.CARGO_MODEL__INVENTORY_MODELS:
				return ((InternalEList<?>)getInventoryModels()).basicRemove(otherEnd, msgs);
			case CargoPackage.CARGO_MODEL__CANAL_BOOKINGS:
				return basicSetCanalBookings(null, msgs);
			case CargoPackage.CARGO_MODEL__CHARTER_IN_MARKET_OVERRIDES:
				return ((InternalEList<?>)getCharterInMarketOverrides()).basicRemove(otherEnd, msgs);
			case CargoPackage.CARGO_MODEL__PAPER_DEALS:
				return ((InternalEList<?>)getPaperDeals()).basicRemove(otherEnd, msgs);
			case CargoPackage.CARGO_MODEL__DEAL_SETS:
				return ((InternalEList<?>)getDealSets()).basicRemove(otherEnd, msgs);
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
			case CargoPackage.CARGO_MODEL__LOAD_SLOTS:
				return getLoadSlots();
			case CargoPackage.CARGO_MODEL__DISCHARGE_SLOTS:
				return getDischargeSlots();
			case CargoPackage.CARGO_MODEL__CARGOES:
				return getCargoes();
			case CargoPackage.CARGO_MODEL__CARGO_GROUPS:
				return getCargoGroups();
			case CargoPackage.CARGO_MODEL__VESSEL_AVAILABILITIES:
				return getVesselAvailabilities();
			case CargoPackage.CARGO_MODEL__VESSEL_EVENTS:
				return getVesselEvents();
			case CargoPackage.CARGO_MODEL__VESSEL_TYPE_GROUPS:
				return getVesselTypeGroups();
			case CargoPackage.CARGO_MODEL__INVENTORY_MODELS:
				return getInventoryModels();
			case CargoPackage.CARGO_MODEL__CANAL_BOOKINGS:
				if (resolve) return getCanalBookings();
				return basicGetCanalBookings();
			case CargoPackage.CARGO_MODEL__CHARTER_IN_MARKET_OVERRIDES:
				return getCharterInMarketOverrides();
			case CargoPackage.CARGO_MODEL__PAPER_DEALS:
				return getPaperDeals();
			case CargoPackage.CARGO_MODEL__DEAL_SETS:
				return getDealSets();
			case CargoPackage.CARGO_MODEL__CARGOES_FOR_EXPOSURES:
				return getCargoesForExposures();
			case CargoPackage.CARGO_MODEL__CARGOES_FOR_HEDGING:
				return getCargoesForHedging();
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
			case CargoPackage.CARGO_MODEL__LOAD_SLOTS:
				getLoadSlots().clear();
				getLoadSlots().addAll((Collection<? extends LoadSlot>)newValue);
				return;
			case CargoPackage.CARGO_MODEL__DISCHARGE_SLOTS:
				getDischargeSlots().clear();
				getDischargeSlots().addAll((Collection<? extends DischargeSlot>)newValue);
				return;
			case CargoPackage.CARGO_MODEL__CARGOES:
				getCargoes().clear();
				getCargoes().addAll((Collection<? extends Cargo>)newValue);
				return;
			case CargoPackage.CARGO_MODEL__CARGO_GROUPS:
				getCargoGroups().clear();
				getCargoGroups().addAll((Collection<? extends CargoGroup>)newValue);
				return;
			case CargoPackage.CARGO_MODEL__VESSEL_AVAILABILITIES:
				getVesselAvailabilities().clear();
				getVesselAvailabilities().addAll((Collection<? extends VesselAvailability>)newValue);
				return;
			case CargoPackage.CARGO_MODEL__VESSEL_EVENTS:
				getVesselEvents().clear();
				getVesselEvents().addAll((Collection<? extends VesselEvent>)newValue);
				return;
			case CargoPackage.CARGO_MODEL__VESSEL_TYPE_GROUPS:
				getVesselTypeGroups().clear();
				getVesselTypeGroups().addAll((Collection<? extends VesselTypeGroup>)newValue);
				return;
			case CargoPackage.CARGO_MODEL__INVENTORY_MODELS:
				getInventoryModels().clear();
				getInventoryModels().addAll((Collection<? extends Inventory>)newValue);
				return;
			case CargoPackage.CARGO_MODEL__CANAL_BOOKINGS:
				setCanalBookings((CanalBookings)newValue);
				return;
			case CargoPackage.CARGO_MODEL__CHARTER_IN_MARKET_OVERRIDES:
				getCharterInMarketOverrides().clear();
				getCharterInMarketOverrides().addAll((Collection<? extends CharterInMarketOverride>)newValue);
				return;
			case CargoPackage.CARGO_MODEL__PAPER_DEALS:
				getPaperDeals().clear();
				getPaperDeals().addAll((Collection<? extends PaperDeal>)newValue);
				return;
			case CargoPackage.CARGO_MODEL__DEAL_SETS:
				getDealSets().clear();
				getDealSets().addAll((Collection<? extends DealSet>)newValue);
				return;
			case CargoPackage.CARGO_MODEL__CARGOES_FOR_EXPOSURES:
				getCargoesForExposures().clear();
				getCargoesForExposures().addAll((Collection<? extends Cargo>)newValue);
				return;
			case CargoPackage.CARGO_MODEL__CARGOES_FOR_HEDGING:
				getCargoesForHedging().clear();
				getCargoesForHedging().addAll((Collection<? extends Cargo>)newValue);
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
			case CargoPackage.CARGO_MODEL__LOAD_SLOTS:
				getLoadSlots().clear();
				return;
			case CargoPackage.CARGO_MODEL__DISCHARGE_SLOTS:
				getDischargeSlots().clear();
				return;
			case CargoPackage.CARGO_MODEL__CARGOES:
				getCargoes().clear();
				return;
			case CargoPackage.CARGO_MODEL__CARGO_GROUPS:
				getCargoGroups().clear();
				return;
			case CargoPackage.CARGO_MODEL__VESSEL_AVAILABILITIES:
				getVesselAvailabilities().clear();
				return;
			case CargoPackage.CARGO_MODEL__VESSEL_EVENTS:
				getVesselEvents().clear();
				return;
			case CargoPackage.CARGO_MODEL__VESSEL_TYPE_GROUPS:
				getVesselTypeGroups().clear();
				return;
			case CargoPackage.CARGO_MODEL__INVENTORY_MODELS:
				getInventoryModels().clear();
				return;
			case CargoPackage.CARGO_MODEL__CANAL_BOOKINGS:
				setCanalBookings((CanalBookings)null);
				return;
			case CargoPackage.CARGO_MODEL__CHARTER_IN_MARKET_OVERRIDES:
				getCharterInMarketOverrides().clear();
				return;
			case CargoPackage.CARGO_MODEL__PAPER_DEALS:
				getPaperDeals().clear();
				return;
			case CargoPackage.CARGO_MODEL__DEAL_SETS:
				getDealSets().clear();
				return;
			case CargoPackage.CARGO_MODEL__CARGOES_FOR_EXPOSURES:
				getCargoesForExposures().clear();
				return;
			case CargoPackage.CARGO_MODEL__CARGOES_FOR_HEDGING:
				getCargoesForHedging().clear();
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
			case CargoPackage.CARGO_MODEL__LOAD_SLOTS:
				return loadSlots != null && !loadSlots.isEmpty();
			case CargoPackage.CARGO_MODEL__DISCHARGE_SLOTS:
				return dischargeSlots != null && !dischargeSlots.isEmpty();
			case CargoPackage.CARGO_MODEL__CARGOES:
				return cargoes != null && !cargoes.isEmpty();
			case CargoPackage.CARGO_MODEL__CARGO_GROUPS:
				return cargoGroups != null && !cargoGroups.isEmpty();
			case CargoPackage.CARGO_MODEL__VESSEL_AVAILABILITIES:
				return vesselAvailabilities != null && !vesselAvailabilities.isEmpty();
			case CargoPackage.CARGO_MODEL__VESSEL_EVENTS:
				return vesselEvents != null && !vesselEvents.isEmpty();
			case CargoPackage.CARGO_MODEL__VESSEL_TYPE_GROUPS:
				return vesselTypeGroups != null && !vesselTypeGroups.isEmpty();
			case CargoPackage.CARGO_MODEL__INVENTORY_MODELS:
				return inventoryModels != null && !inventoryModels.isEmpty();
			case CargoPackage.CARGO_MODEL__CANAL_BOOKINGS:
				return canalBookings != null;
			case CargoPackage.CARGO_MODEL__CHARTER_IN_MARKET_OVERRIDES:
				return charterInMarketOverrides != null && !charterInMarketOverrides.isEmpty();
			case CargoPackage.CARGO_MODEL__PAPER_DEALS:
				return paperDeals != null && !paperDeals.isEmpty();
			case CargoPackage.CARGO_MODEL__DEAL_SETS:
				return dealSets != null && !dealSets.isEmpty();
			case CargoPackage.CARGO_MODEL__CARGOES_FOR_EXPOSURES:
				return cargoesForExposures != null && !cargoesForExposures.isEmpty();
			case CargoPackage.CARGO_MODEL__CARGOES_FOR_HEDGING:
				return cargoesForHedging != null && !cargoesForHedging.isEmpty();
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
			case CargoPackage.CARGO_MODEL___GET_LOAD_SLOT_BY_NAME__STRING:
				return getLoadSlotByName((String)arguments.get(0));
			case CargoPackage.CARGO_MODEL___GET_DISCHARGE_SLOT_BY_NAME__STRING:
				return getDischargeSlotByName((String)arguments.get(0));
		}
		return super.eInvoke(operationID, arguments);
	}

} // end of CargoModelImpl

// finish type fixing
