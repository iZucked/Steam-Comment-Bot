/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.schedule.impl;
import java.lang.reflect.InvocationTargetException;

import java.util.Collection;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.commercial.Contract;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.schedule.CargoAllocation;
import com.mmxlabs.models.lng.schedule.ExposureDetail;
import com.mmxlabs.models.lng.schedule.MarketAllocation;
import com.mmxlabs.models.lng.schedule.SchedulePackage;
import com.mmxlabs.models.lng.schedule.SlotAllocation;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.spotmarkets.FOBPurchasesMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarket;
import com.mmxlabs.models.mmxcore.impl.MMXObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Slot Allocation</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.SlotAllocationImpl#getSlot <em>Slot</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.SlotAllocationImpl#getSpotMarket <em>Spot Market</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.SlotAllocationImpl#getCargoAllocation <em>Cargo Allocation</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.SlotAllocationImpl#getMarketAllocation <em>Market Allocation</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.SlotAllocationImpl#getSlotVisit <em>Slot Visit</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.SlotAllocationImpl#getPrice <em>Price</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.SlotAllocationImpl#getVolumeTransferred <em>Volume Transferred</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.SlotAllocationImpl#getEnergyTransferred <em>Energy Transferred</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.SlotAllocationImpl#getCv <em>Cv</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.SlotAllocationImpl#getVolumeValue <em>Volume Value</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.SlotAllocationImpl#getExposures <em>Exposures</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.SlotAllocationImpl#getPhysicalVolumeTransferred <em>Physical Volume Transferred</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.SlotAllocationImpl#getPhysicalEnergyTransferred <em>Physical Energy Transferred</em>}</li>
 * </ul>
 *
 * @generated
 */
public class SlotAllocationImpl extends MMXObjectImpl implements SlotAllocation {
	/**
	 * The cached value of the '{@link #getSlot() <em>Slot</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSlot()
	 * @generated
	 * @ordered
	 */
	protected Slot slot;

	/**
	 * This is true if the Slot reference has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean slotESet;

	/**
	 * The cached value of the '{@link #getSpotMarket() <em>Spot Market</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSpotMarket()
	 * @generated
	 * @ordered
	 */
	protected SpotMarket spotMarket;

	/**
	 * This is true if the Spot Market reference has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean spotMarketESet;

	/**
	 * The cached value of the '{@link #getCargoAllocation() <em>Cargo Allocation</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCargoAllocation()
	 * @generated
	 * @ordered
	 */
	protected CargoAllocation cargoAllocation;

	/**
	 * The cached value of the '{@link #getMarketAllocation() <em>Market Allocation</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMarketAllocation()
	 * @generated
	 * @ordered
	 */
	protected MarketAllocation marketAllocation;

	/**
	 * The cached value of the '{@link #getSlotVisit() <em>Slot Visit</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSlotVisit()
	 * @generated
	 * @ordered
	 */
	protected SlotVisit slotVisit;

	/**
	 * This is true if the Slot Visit reference has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean slotVisitESet;

	/**
	 * The default value of the '{@link #getPrice() <em>Price</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPrice()
	 * @generated
	 * @ordered
	 */
	protected static final double PRICE_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getPrice() <em>Price</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPrice()
	 * @generated
	 * @ordered
	 */
	protected double price = PRICE_EDEFAULT;

	/**
	 * The default value of the '{@link #getVolumeTransferred() <em>Volume Transferred</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVolumeTransferred()
	 * @generated
	 * @ordered
	 */
	protected static final int VOLUME_TRANSFERRED_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getVolumeTransferred() <em>Volume Transferred</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVolumeTransferred()
	 * @generated
	 * @ordered
	 */
	protected int volumeTransferred = VOLUME_TRANSFERRED_EDEFAULT;

	/**
	 * The default value of the '{@link #getEnergyTransferred() <em>Energy Transferred</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEnergyTransferred()
	 * @generated
	 * @ordered
	 */
	protected static final int ENERGY_TRANSFERRED_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getEnergyTransferred() <em>Energy Transferred</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEnergyTransferred()
	 * @generated
	 * @ordered
	 */
	protected int energyTransferred = ENERGY_TRANSFERRED_EDEFAULT;

	/**
	 * The default value of the '{@link #getCv() <em>Cv</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCv()
	 * @generated
	 * @ordered
	 */
	protected static final double CV_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getCv() <em>Cv</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCv()
	 * @generated
	 * @ordered
	 */
	protected double cv = CV_EDEFAULT;

	/**
	 * The default value of the '{@link #getVolumeValue() <em>Volume Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVolumeValue()
	 * @generated
	 * @ordered
	 */
	protected static final int VOLUME_VALUE_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getVolumeValue() <em>Volume Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVolumeValue()
	 * @generated
	 * @ordered
	 */
	protected int volumeValue = VOLUME_VALUE_EDEFAULT;

	/**
	 * The cached value of the '{@link #getExposures() <em>Exposures</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getExposures()
	 * @generated
	 * @ordered
	 */
	protected EList<ExposureDetail> exposures;

	/**
	 * The default value of the '{@link #getPhysicalVolumeTransferred() <em>Physical Volume Transferred</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPhysicalVolumeTransferred()
	 * @generated
	 * @ordered
	 */
	protected static final int PHYSICAL_VOLUME_TRANSFERRED_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getPhysicalVolumeTransferred() <em>Physical Volume Transferred</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPhysicalVolumeTransferred()
	 * @generated
	 * @ordered
	 */
	protected int physicalVolumeTransferred = PHYSICAL_VOLUME_TRANSFERRED_EDEFAULT;

	/**
	 * The default value of the '{@link #getPhysicalEnergyTransferred() <em>Physical Energy Transferred</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPhysicalEnergyTransferred()
	 * @generated
	 * @ordered
	 */
	protected static final int PHYSICAL_ENERGY_TRANSFERRED_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getPhysicalEnergyTransferred() <em>Physical Energy Transferred</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPhysicalEnergyTransferred()
	 * @generated
	 * @ordered
	 */
	protected int physicalEnergyTransferred = PHYSICAL_ENERGY_TRANSFERRED_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected SlotAllocationImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return SchedulePackage.Literals.SLOT_ALLOCATION;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Slot getSlot() {
		if (slot != null && slot.eIsProxy()) {
			InternalEObject oldSlot = (InternalEObject)slot;
			slot = (Slot)eResolveProxy(oldSlot);
			if (slot != oldSlot) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, SchedulePackage.SLOT_ALLOCATION__SLOT, oldSlot, slot));
			}
		}
		return slot;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Slot basicGetSlot() {
		return slot;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSlot(Slot newSlot) {
		Slot oldSlot = slot;
		slot = newSlot;
		boolean oldSlotESet = slotESet;
		slotESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.SLOT_ALLOCATION__SLOT, oldSlot, slot, !oldSlotESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetSlot() {
		Slot oldSlot = slot;
		boolean oldSlotESet = slotESet;
		slot = null;
		slotESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, SchedulePackage.SLOT_ALLOCATION__SLOT, oldSlot, null, oldSlotESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetSlot() {
		return slotESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SpotMarket getSpotMarket() {
		if (spotMarket != null && spotMarket.eIsProxy()) {
			InternalEObject oldSpotMarket = (InternalEObject)spotMarket;
			spotMarket = (SpotMarket)eResolveProxy(oldSpotMarket);
			if (spotMarket != oldSpotMarket) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, SchedulePackage.SLOT_ALLOCATION__SPOT_MARKET, oldSpotMarket, spotMarket));
			}
		}
		return spotMarket;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SpotMarket basicGetSpotMarket() {
		return spotMarket;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSpotMarket(SpotMarket newSpotMarket) {
		SpotMarket oldSpotMarket = spotMarket;
		spotMarket = newSpotMarket;
		boolean oldSpotMarketESet = spotMarketESet;
		spotMarketESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.SLOT_ALLOCATION__SPOT_MARKET, oldSpotMarket, spotMarket, !oldSpotMarketESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetSpotMarket() {
		SpotMarket oldSpotMarket = spotMarket;
		boolean oldSpotMarketESet = spotMarketESet;
		spotMarket = null;
		spotMarketESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, SchedulePackage.SLOT_ALLOCATION__SPOT_MARKET, oldSpotMarket, null, oldSpotMarketESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetSpotMarket() {
		return spotMarketESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CargoAllocation getCargoAllocation() {
		if (cargoAllocation != null && cargoAllocation.eIsProxy()) {
			InternalEObject oldCargoAllocation = (InternalEObject)cargoAllocation;
			cargoAllocation = (CargoAllocation)eResolveProxy(oldCargoAllocation);
			if (cargoAllocation != oldCargoAllocation) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, SchedulePackage.SLOT_ALLOCATION__CARGO_ALLOCATION, oldCargoAllocation, cargoAllocation));
			}
		}
		return cargoAllocation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CargoAllocation basicGetCargoAllocation() {
		return cargoAllocation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetCargoAllocation(CargoAllocation newCargoAllocation, NotificationChain msgs) {
		CargoAllocation oldCargoAllocation = cargoAllocation;
		cargoAllocation = newCargoAllocation;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, SchedulePackage.SLOT_ALLOCATION__CARGO_ALLOCATION, oldCargoAllocation, newCargoAllocation);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setCargoAllocation(CargoAllocation newCargoAllocation) {
		if (newCargoAllocation != cargoAllocation) {
			NotificationChain msgs = null;
			if (cargoAllocation != null)
				msgs = ((InternalEObject)cargoAllocation).eInverseRemove(this, SchedulePackage.CARGO_ALLOCATION__SLOT_ALLOCATIONS, CargoAllocation.class, msgs);
			if (newCargoAllocation != null)
				msgs = ((InternalEObject)newCargoAllocation).eInverseAdd(this, SchedulePackage.CARGO_ALLOCATION__SLOT_ALLOCATIONS, CargoAllocation.class, msgs);
			msgs = basicSetCargoAllocation(newCargoAllocation, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.SLOT_ALLOCATION__CARGO_ALLOCATION, newCargoAllocation, newCargoAllocation));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public MarketAllocation getMarketAllocation() {
		if (marketAllocation != null && marketAllocation.eIsProxy()) {
			InternalEObject oldMarketAllocation = (InternalEObject)marketAllocation;
			marketAllocation = (MarketAllocation)eResolveProxy(oldMarketAllocation);
			if (marketAllocation != oldMarketAllocation) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, SchedulePackage.SLOT_ALLOCATION__MARKET_ALLOCATION, oldMarketAllocation, marketAllocation));
			}
		}
		return marketAllocation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public MarketAllocation basicGetMarketAllocation() {
		return marketAllocation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetMarketAllocation(MarketAllocation newMarketAllocation, NotificationChain msgs) {
		MarketAllocation oldMarketAllocation = marketAllocation;
		marketAllocation = newMarketAllocation;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, SchedulePackage.SLOT_ALLOCATION__MARKET_ALLOCATION, oldMarketAllocation, newMarketAllocation);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMarketAllocation(MarketAllocation newMarketAllocation) {
		if (newMarketAllocation != marketAllocation) {
			NotificationChain msgs = null;
			if (marketAllocation != null)
				msgs = ((InternalEObject)marketAllocation).eInverseRemove(this, SchedulePackage.MARKET_ALLOCATION__SLOT_ALLOCATION, MarketAllocation.class, msgs);
			if (newMarketAllocation != null)
				msgs = ((InternalEObject)newMarketAllocation).eInverseAdd(this, SchedulePackage.MARKET_ALLOCATION__SLOT_ALLOCATION, MarketAllocation.class, msgs);
			msgs = basicSetMarketAllocation(newMarketAllocation, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.SLOT_ALLOCATION__MARKET_ALLOCATION, newMarketAllocation, newMarketAllocation));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SlotVisit getSlotVisit() {
		if (slotVisit != null && slotVisit.eIsProxy()) {
			InternalEObject oldSlotVisit = (InternalEObject)slotVisit;
			slotVisit = (SlotVisit)eResolveProxy(oldSlotVisit);
			if (slotVisit != oldSlotVisit) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, SchedulePackage.SLOT_ALLOCATION__SLOT_VISIT, oldSlotVisit, slotVisit));
			}
		}
		return slotVisit;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SlotVisit basicGetSlotVisit() {
		return slotVisit;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetSlotVisit(SlotVisit newSlotVisit, NotificationChain msgs) {
		SlotVisit oldSlotVisit = slotVisit;
		slotVisit = newSlotVisit;
		boolean oldSlotVisitESet = slotVisitESet;
		slotVisitESet = true;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, SchedulePackage.SLOT_ALLOCATION__SLOT_VISIT, oldSlotVisit, newSlotVisit, !oldSlotVisitESet);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSlotVisit(SlotVisit newSlotVisit) {
		if (newSlotVisit != slotVisit) {
			NotificationChain msgs = null;
			if (slotVisit != null)
				msgs = ((InternalEObject)slotVisit).eInverseRemove(this, SchedulePackage.SLOT_VISIT__SLOT_ALLOCATION, SlotVisit.class, msgs);
			if (newSlotVisit != null)
				msgs = ((InternalEObject)newSlotVisit).eInverseAdd(this, SchedulePackage.SLOT_VISIT__SLOT_ALLOCATION, SlotVisit.class, msgs);
			msgs = basicSetSlotVisit(newSlotVisit, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else {
			boolean oldSlotVisitESet = slotVisitESet;
			slotVisitESet = true;
			if (eNotificationRequired())
				eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.SLOT_ALLOCATION__SLOT_VISIT, newSlotVisit, newSlotVisit, !oldSlotVisitESet));
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicUnsetSlotVisit(NotificationChain msgs) {
		SlotVisit oldSlotVisit = slotVisit;
		slotVisit = null;
		boolean oldSlotVisitESet = slotVisitESet;
		slotVisitESet = false;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.UNSET, SchedulePackage.SLOT_ALLOCATION__SLOT_VISIT, oldSlotVisit, null, oldSlotVisitESet);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetSlotVisit() {
		if (slotVisit != null) {
			NotificationChain msgs = null;
			msgs = ((InternalEObject)slotVisit).eInverseRemove(this, SchedulePackage.SLOT_VISIT__SLOT_ALLOCATION, SlotVisit.class, msgs);
			msgs = basicUnsetSlotVisit(msgs);
			if (msgs != null) msgs.dispatch();
		}
		else {
			boolean oldSlotVisitESet = slotVisitESet;
			slotVisitESet = false;
			if (eNotificationRequired())
				eNotify(new ENotificationImpl(this, Notification.UNSET, SchedulePackage.SLOT_ALLOCATION__SLOT_VISIT, null, null, oldSlotVisitESet));
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetSlotVisit() {
		return slotVisitESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public double getPrice() {
		return price;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setPrice(double newPrice) {
		double oldPrice = price;
		price = newPrice;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.SLOT_ALLOCATION__PRICE, oldPrice, price));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getVolumeTransferred() {
		return volumeTransferred;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setVolumeTransferred(int newVolumeTransferred) {
		int oldVolumeTransferred = volumeTransferred;
		volumeTransferred = newVolumeTransferred;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.SLOT_ALLOCATION__VOLUME_TRANSFERRED, oldVolumeTransferred, volumeTransferred));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getEnergyTransferred() {
		return energyTransferred;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setEnergyTransferred(int newEnergyTransferred) {
		int oldEnergyTransferred = energyTransferred;
		energyTransferred = newEnergyTransferred;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.SLOT_ALLOCATION__ENERGY_TRANSFERRED, oldEnergyTransferred, energyTransferred));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public double getCv() {
		return cv;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setCv(double newCv) {
		double oldCv = cv;
		cv = newCv;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.SLOT_ALLOCATION__CV, oldCv, cv));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getVolumeValue() {
		return volumeValue;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setVolumeValue(int newVolumeValue) {
		int oldVolumeValue = volumeValue;
		volumeValue = newVolumeValue;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.SLOT_ALLOCATION__VOLUME_VALUE, oldVolumeValue, volumeValue));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<ExposureDetail> getExposures() {
		if (exposures == null) {
			exposures = new EObjectContainmentEList<ExposureDetail>(ExposureDetail.class, this, SchedulePackage.SLOT_ALLOCATION__EXPOSURES);
		}
		return exposures;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getPhysicalVolumeTransferred() {
		return physicalVolumeTransferred;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setPhysicalVolumeTransferred(int newPhysicalVolumeTransferred) {
		int oldPhysicalVolumeTransferred = physicalVolumeTransferred;
		physicalVolumeTransferred = newPhysicalVolumeTransferred;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.SLOT_ALLOCATION__PHYSICAL_VOLUME_TRANSFERRED, oldPhysicalVolumeTransferred, physicalVolumeTransferred));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getPhysicalEnergyTransferred() {
		return physicalEnergyTransferred;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setPhysicalEnergyTransferred(int newPhysicalEnergyTransferred) {
		int oldPhysicalEnergyTransferred = physicalEnergyTransferred;
		physicalEnergyTransferred = newPhysicalEnergyTransferred;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.SLOT_ALLOCATION__PHYSICAL_ENERGY_TRANSFERRED, oldPhysicalEnergyTransferred, physicalEnergyTransferred));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public Port getPort() {
		if (isSetSlot()) {
			return getSlot().getPort();
		} else if (isSetSlotVisit()) {
			return getSlotVisit().getPort();
		} else if (isSetSpotMarket()) {
			final SpotMarket market = getSpotMarket();
			if (market instanceof FOBPurchasesMarket) {
				return ((FOBPurchasesMarket) market).getNotionalPort();
			}
		}
		return null;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public Contract getContract() {
		if (isSetSlot()) {
			return slot.getContract();
//		} else if (isSetSpotMarket()) {
//			return ((SpotMarket)getSpotMarket()).getContract();
		}
		return null;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public String getName() {
		if (isSetSlot()) {
			return getSlot().getName();
		} else if (isSetSpotMarket()) {
			return getSpotMarket().getName();
		} else {
			return "";
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case SchedulePackage.SLOT_ALLOCATION__CARGO_ALLOCATION:
				if (cargoAllocation != null)
					msgs = ((InternalEObject)cargoAllocation).eInverseRemove(this, SchedulePackage.CARGO_ALLOCATION__SLOT_ALLOCATIONS, CargoAllocation.class, msgs);
				return basicSetCargoAllocation((CargoAllocation)otherEnd, msgs);
			case SchedulePackage.SLOT_ALLOCATION__MARKET_ALLOCATION:
				if (marketAllocation != null)
					msgs = ((InternalEObject)marketAllocation).eInverseRemove(this, SchedulePackage.MARKET_ALLOCATION__SLOT_ALLOCATION, MarketAllocation.class, msgs);
				return basicSetMarketAllocation((MarketAllocation)otherEnd, msgs);
			case SchedulePackage.SLOT_ALLOCATION__SLOT_VISIT:
				if (slotVisit != null)
					msgs = ((InternalEObject)slotVisit).eInverseRemove(this, SchedulePackage.SLOT_VISIT__SLOT_ALLOCATION, SlotVisit.class, msgs);
				return basicSetSlotVisit((SlotVisit)otherEnd, msgs);
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
			case SchedulePackage.SLOT_ALLOCATION__CARGO_ALLOCATION:
				return basicSetCargoAllocation(null, msgs);
			case SchedulePackage.SLOT_ALLOCATION__MARKET_ALLOCATION:
				return basicSetMarketAllocation(null, msgs);
			case SchedulePackage.SLOT_ALLOCATION__SLOT_VISIT:
				return basicUnsetSlotVisit(msgs);
			case SchedulePackage.SLOT_ALLOCATION__EXPOSURES:
				return ((InternalEList<?>)getExposures()).basicRemove(otherEnd, msgs);
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
			case SchedulePackage.SLOT_ALLOCATION__SLOT:
				if (resolve) return getSlot();
				return basicGetSlot();
			case SchedulePackage.SLOT_ALLOCATION__SPOT_MARKET:
				if (resolve) return getSpotMarket();
				return basicGetSpotMarket();
			case SchedulePackage.SLOT_ALLOCATION__CARGO_ALLOCATION:
				if (resolve) return getCargoAllocation();
				return basicGetCargoAllocation();
			case SchedulePackage.SLOT_ALLOCATION__MARKET_ALLOCATION:
				if (resolve) return getMarketAllocation();
				return basicGetMarketAllocation();
			case SchedulePackage.SLOT_ALLOCATION__SLOT_VISIT:
				if (resolve) return getSlotVisit();
				return basicGetSlotVisit();
			case SchedulePackage.SLOT_ALLOCATION__PRICE:
				return getPrice();
			case SchedulePackage.SLOT_ALLOCATION__VOLUME_TRANSFERRED:
				return getVolumeTransferred();
			case SchedulePackage.SLOT_ALLOCATION__ENERGY_TRANSFERRED:
				return getEnergyTransferred();
			case SchedulePackage.SLOT_ALLOCATION__CV:
				return getCv();
			case SchedulePackage.SLOT_ALLOCATION__VOLUME_VALUE:
				return getVolumeValue();
			case SchedulePackage.SLOT_ALLOCATION__EXPOSURES:
				return getExposures();
			case SchedulePackage.SLOT_ALLOCATION__PHYSICAL_VOLUME_TRANSFERRED:
				return getPhysicalVolumeTransferred();
			case SchedulePackage.SLOT_ALLOCATION__PHYSICAL_ENERGY_TRANSFERRED:
				return getPhysicalEnergyTransferred();
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
			case SchedulePackage.SLOT_ALLOCATION__SLOT:
				setSlot((Slot)newValue);
				return;
			case SchedulePackage.SLOT_ALLOCATION__SPOT_MARKET:
				setSpotMarket((SpotMarket)newValue);
				return;
			case SchedulePackage.SLOT_ALLOCATION__CARGO_ALLOCATION:
				setCargoAllocation((CargoAllocation)newValue);
				return;
			case SchedulePackage.SLOT_ALLOCATION__MARKET_ALLOCATION:
				setMarketAllocation((MarketAllocation)newValue);
				return;
			case SchedulePackage.SLOT_ALLOCATION__SLOT_VISIT:
				setSlotVisit((SlotVisit)newValue);
				return;
			case SchedulePackage.SLOT_ALLOCATION__PRICE:
				setPrice((Double)newValue);
				return;
			case SchedulePackage.SLOT_ALLOCATION__VOLUME_TRANSFERRED:
				setVolumeTransferred((Integer)newValue);
				return;
			case SchedulePackage.SLOT_ALLOCATION__ENERGY_TRANSFERRED:
				setEnergyTransferred((Integer)newValue);
				return;
			case SchedulePackage.SLOT_ALLOCATION__CV:
				setCv((Double)newValue);
				return;
			case SchedulePackage.SLOT_ALLOCATION__VOLUME_VALUE:
				setVolumeValue((Integer)newValue);
				return;
			case SchedulePackage.SLOT_ALLOCATION__EXPOSURES:
				getExposures().clear();
				getExposures().addAll((Collection<? extends ExposureDetail>)newValue);
				return;
			case SchedulePackage.SLOT_ALLOCATION__PHYSICAL_VOLUME_TRANSFERRED:
				setPhysicalVolumeTransferred((Integer)newValue);
				return;
			case SchedulePackage.SLOT_ALLOCATION__PHYSICAL_ENERGY_TRANSFERRED:
				setPhysicalEnergyTransferred((Integer)newValue);
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
			case SchedulePackage.SLOT_ALLOCATION__SLOT:
				unsetSlot();
				return;
			case SchedulePackage.SLOT_ALLOCATION__SPOT_MARKET:
				unsetSpotMarket();
				return;
			case SchedulePackage.SLOT_ALLOCATION__CARGO_ALLOCATION:
				setCargoAllocation((CargoAllocation)null);
				return;
			case SchedulePackage.SLOT_ALLOCATION__MARKET_ALLOCATION:
				setMarketAllocation((MarketAllocation)null);
				return;
			case SchedulePackage.SLOT_ALLOCATION__SLOT_VISIT:
				unsetSlotVisit();
				return;
			case SchedulePackage.SLOT_ALLOCATION__PRICE:
				setPrice(PRICE_EDEFAULT);
				return;
			case SchedulePackage.SLOT_ALLOCATION__VOLUME_TRANSFERRED:
				setVolumeTransferred(VOLUME_TRANSFERRED_EDEFAULT);
				return;
			case SchedulePackage.SLOT_ALLOCATION__ENERGY_TRANSFERRED:
				setEnergyTransferred(ENERGY_TRANSFERRED_EDEFAULT);
				return;
			case SchedulePackage.SLOT_ALLOCATION__CV:
				setCv(CV_EDEFAULT);
				return;
			case SchedulePackage.SLOT_ALLOCATION__VOLUME_VALUE:
				setVolumeValue(VOLUME_VALUE_EDEFAULT);
				return;
			case SchedulePackage.SLOT_ALLOCATION__EXPOSURES:
				getExposures().clear();
				return;
			case SchedulePackage.SLOT_ALLOCATION__PHYSICAL_VOLUME_TRANSFERRED:
				setPhysicalVolumeTransferred(PHYSICAL_VOLUME_TRANSFERRED_EDEFAULT);
				return;
			case SchedulePackage.SLOT_ALLOCATION__PHYSICAL_ENERGY_TRANSFERRED:
				setPhysicalEnergyTransferred(PHYSICAL_ENERGY_TRANSFERRED_EDEFAULT);
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
			case SchedulePackage.SLOT_ALLOCATION__SLOT:
				return isSetSlot();
			case SchedulePackage.SLOT_ALLOCATION__SPOT_MARKET:
				return isSetSpotMarket();
			case SchedulePackage.SLOT_ALLOCATION__CARGO_ALLOCATION:
				return cargoAllocation != null;
			case SchedulePackage.SLOT_ALLOCATION__MARKET_ALLOCATION:
				return marketAllocation != null;
			case SchedulePackage.SLOT_ALLOCATION__SLOT_VISIT:
				return isSetSlotVisit();
			case SchedulePackage.SLOT_ALLOCATION__PRICE:
				return price != PRICE_EDEFAULT;
			case SchedulePackage.SLOT_ALLOCATION__VOLUME_TRANSFERRED:
				return volumeTransferred != VOLUME_TRANSFERRED_EDEFAULT;
			case SchedulePackage.SLOT_ALLOCATION__ENERGY_TRANSFERRED:
				return energyTransferred != ENERGY_TRANSFERRED_EDEFAULT;
			case SchedulePackage.SLOT_ALLOCATION__CV:
				return cv != CV_EDEFAULT;
			case SchedulePackage.SLOT_ALLOCATION__VOLUME_VALUE:
				return volumeValue != VOLUME_VALUE_EDEFAULT;
			case SchedulePackage.SLOT_ALLOCATION__EXPOSURES:
				return exposures != null && !exposures.isEmpty();
			case SchedulePackage.SLOT_ALLOCATION__PHYSICAL_VOLUME_TRANSFERRED:
				return physicalVolumeTransferred != PHYSICAL_VOLUME_TRANSFERRED_EDEFAULT;
			case SchedulePackage.SLOT_ALLOCATION__PHYSICAL_ENERGY_TRANSFERRED:
				return physicalEnergyTransferred != PHYSICAL_ENERGY_TRANSFERRED_EDEFAULT;
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
			case SchedulePackage.SLOT_ALLOCATION___GET_PORT:
				return getPort();
			case SchedulePackage.SLOT_ALLOCATION___GET_CONTRACT:
				return getContract();
			case SchedulePackage.SLOT_ALLOCATION___GET_NAME:
				return getName();
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
		result.append(" (price: ");
		result.append(price);
		result.append(", volumeTransferred: ");
		result.append(volumeTransferred);
		result.append(", energyTransferred: ");
		result.append(energyTransferred);
		result.append(", cv: ");
		result.append(cv);
		result.append(", volumeValue: ");
		result.append(volumeValue);
		result.append(", physicalVolumeTransferred: ");
		result.append(physicalVolumeTransferred);
		result.append(", physicalEnergyTransferred: ");
		result.append(physicalEnergyTransferred);
		result.append(')');
		return result.toString();
	}

} // end of SlotAllocationImpl

// finish type fixing
