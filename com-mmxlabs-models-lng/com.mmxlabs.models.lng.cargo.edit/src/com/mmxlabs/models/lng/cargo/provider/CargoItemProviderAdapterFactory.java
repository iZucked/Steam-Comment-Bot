/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.cargo.provider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.ChangeNotifier;
import org.eclipse.emf.edit.provider.ChildCreationExtenderManager;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.IChangeNotifier;
import org.eclipse.emf.edit.provider.IChildCreationExtender;
import org.eclipse.emf.edit.provider.IDisposable;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.INotifyChangedListener;
import org.eclipse.emf.edit.provider.IStructuredItemContentProvider;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;

import org.eclipse.jdt.annotation.Nullable;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.util.CargoAdapterFactory;

/**
 * This is the factory that is used to provide the interfaces needed to support Viewers.
 * The adapters generated by this factory convert EMF adapter notifications into calls to {@link #fireNotifyChanged fireNotifyChanged}.
 * The adapters also support Eclipse property sheets.
 * Note that most of the adapters are shared among multiple instances.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class CargoItemProviderAdapterFactory extends CargoAdapterFactory implements ComposeableAdapterFactory, IChangeNotifier, IDisposable, IChildCreationExtender {
	/**
	 * This keeps track of the root adapter factory that delegates to this adapter factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ComposedAdapterFactory parentAdapterFactory;

	/**
	 * This is used to implement {@link org.eclipse.emf.edit.provider.IChangeNotifier}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected IChangeNotifier changeNotifier = new ChangeNotifier();

	/**
	 * This helps manage the child creation extenders.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ChildCreationExtenderManager childCreationExtenderManager = new ChildCreationExtenderManager(CargoEditPlugin.INSTANCE, CargoPackage.eNS_URI);

	/**
	 * This keeps track of all the supported types checked by {@link #isFactoryForType isFactoryForType}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected Collection<Object> supportedTypes = new ArrayList<Object>();

	/**
	 * This constructs an instance.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CargoItemProviderAdapterFactory() {
		supportedTypes.add(IEditingDomainItemProvider.class);
		supportedTypes.add(IStructuredItemContentProvider.class);
		supportedTypes.add(ITreeItemContentProvider.class);
		supportedTypes.add(IItemLabelProvider.class);
		supportedTypes.add(IItemPropertySource.class);
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.cargo.Cargo} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected CargoItemProvider cargoItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.cargo.Cargo}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createCargoAdapter() {
		if (cargoItemProvider == null) {
			cargoItemProvider = new CargoItemProvider(this);
		}

		return cargoItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.cargo.LoadSlot} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected LoadSlotItemProvider loadSlotItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.cargo.LoadSlot}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createLoadSlotAdapter() {
		if (loadSlotItemProvider == null) {
			loadSlotItemProvider = new LoadSlotItemProvider(this);
		}

		return loadSlotItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.cargo.DischargeSlot} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected DischargeSlotItemProvider dischargeSlotItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.cargo.DischargeSlot}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createDischargeSlotAdapter() {
		if (dischargeSlotItemProvider == null) {
			dischargeSlotItemProvider = new DischargeSlotItemProvider(this);
		}

		return dischargeSlotItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.cargo.CargoModel} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected CargoModelItemProvider cargoModelItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.cargo.CargoModel}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createCargoModelAdapter() {
		if (cargoModelItemProvider == null) {
			cargoModelItemProvider = new CargoModelItemProvider(this);
		}

		return cargoModelItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.cargo.SpotLoadSlot} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected SpotLoadSlotItemProvider spotLoadSlotItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.cargo.SpotLoadSlot}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createSpotLoadSlotAdapter() {
		if (spotLoadSlotItemProvider == null) {
			spotLoadSlotItemProvider = new SpotLoadSlotItemProvider(this);
		}

		return spotLoadSlotItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.cargo.SpotDischargeSlot} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected SpotDischargeSlotItemProvider spotDischargeSlotItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.cargo.SpotDischargeSlot}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createSpotDischargeSlotAdapter() {
		if (spotDischargeSlotItemProvider == null) {
			spotDischargeSlotItemProvider = new SpotDischargeSlotItemProvider(this);
		}

		return spotDischargeSlotItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.cargo.CargoGroup} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected CargoGroupItemProvider cargoGroupItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.cargo.CargoGroup}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createCargoGroupAdapter() {
		if (cargoGroupItemProvider == null) {
			cargoGroupItemProvider = new CargoGroupItemProvider(this);
		}

		return cargoGroupItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.cargo.VesselCharter} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected VesselCharterItemProvider vesselCharterItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.cargo.VesselCharter}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createVesselCharterAdapter() {
		if (vesselCharterItemProvider == null) {
			vesselCharterItemProvider = new VesselCharterItemProvider(this);
		}

		return vesselCharterItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.cargo.MaintenanceEvent} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected MaintenanceEventItemProvider maintenanceEventItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.cargo.MaintenanceEvent}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createMaintenanceEventAdapter() {
		if (maintenanceEventItemProvider == null) {
			maintenanceEventItemProvider = new MaintenanceEventItemProvider(this);
		}

		return maintenanceEventItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.cargo.DryDockEvent} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected DryDockEventItemProvider dryDockEventItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.cargo.DryDockEvent}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createDryDockEventAdapter() {
		if (dryDockEventItemProvider == null) {
			dryDockEventItemProvider = new DryDockEventItemProvider(this);
		}

		return dryDockEventItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.cargo.CharterLengthEvent} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected CharterLengthEventItemProvider charterLengthEventItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.cargo.CharterLengthEvent}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createCharterLengthEventAdapter() {
		if (charterLengthEventItemProvider == null) {
			charterLengthEventItemProvider = new CharterLengthEventItemProvider(this);
		}

		return charterLengthEventItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.cargo.CharterOutEvent} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected CharterOutEventItemProvider charterOutEventItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.cargo.CharterOutEvent}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createCharterOutEventAdapter() {
		if (charterOutEventItemProvider == null) {
			charterOutEventItemProvider = new CharterOutEventItemProvider(this);
		}

		return charterOutEventItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.cargo.VesselTypeGroup} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected VesselTypeGroupItemProvider vesselTypeGroupItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.cargo.VesselTypeGroup}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createVesselTypeGroupAdapter() {
		if (vesselTypeGroupItemProvider == null) {
			vesselTypeGroupItemProvider = new VesselTypeGroupItemProvider(this);
		}

		return vesselTypeGroupItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.cargo.InventoryEventRow} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected InventoryEventRowItemProvider inventoryEventRowItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.cargo.InventoryEventRow}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createInventoryEventRowAdapter() {
		if (inventoryEventRowItemProvider == null) {
			inventoryEventRowItemProvider = new InventoryEventRowItemProvider(this);
		}

		return inventoryEventRowItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.cargo.InventoryCapacityRow} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected InventoryCapacityRowItemProvider inventoryCapacityRowItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.cargo.InventoryCapacityRow}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createInventoryCapacityRowAdapter() {
		if (inventoryCapacityRowItemProvider == null) {
			inventoryCapacityRowItemProvider = new InventoryCapacityRowItemProvider(this);
		}

		return inventoryCapacityRowItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.cargo.Inventory} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected InventoryItemProvider inventoryItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.cargo.Inventory}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createInventoryAdapter() {
		if (inventoryItemProvider == null) {
			inventoryItemProvider = new InventoryItemProvider(this);
		}

		return inventoryItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.cargo.CanalBookingSlot} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected CanalBookingSlotItemProvider canalBookingSlotItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.cargo.CanalBookingSlot}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createCanalBookingSlotAdapter() {
		if (canalBookingSlotItemProvider == null) {
			canalBookingSlotItemProvider = new CanalBookingSlotItemProvider(this);
		}

		return canalBookingSlotItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.cargo.CanalBookings} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected CanalBookingsItemProvider canalBookingsItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.cargo.CanalBookings}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createCanalBookingsAdapter() {
		if (canalBookingsItemProvider == null) {
			canalBookingsItemProvider = new CanalBookingsItemProvider(this);
		}

		return canalBookingsItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.cargo.ScheduleSpecification} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ScheduleSpecificationItemProvider scheduleSpecificationItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.cargo.ScheduleSpecification}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createScheduleSpecificationAdapter() {
		if (scheduleSpecificationItemProvider == null) {
			scheduleSpecificationItemProvider = new ScheduleSpecificationItemProvider(this);
		}

		return scheduleSpecificationItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.cargo.NonShippedCargoSpecification} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected NonShippedCargoSpecificationItemProvider nonShippedCargoSpecificationItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.cargo.NonShippedCargoSpecification}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createNonShippedCargoSpecificationAdapter() {
		if (nonShippedCargoSpecificationItemProvider == null) {
			nonShippedCargoSpecificationItemProvider = new NonShippedCargoSpecificationItemProvider(this);
		}

		return nonShippedCargoSpecificationItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.cargo.VesselScheduleSpecification} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected VesselScheduleSpecificationItemProvider vesselScheduleSpecificationItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.cargo.VesselScheduleSpecification}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createVesselScheduleSpecificationAdapter() {
		if (vesselScheduleSpecificationItemProvider == null) {
			vesselScheduleSpecificationItemProvider = new VesselScheduleSpecificationItemProvider(this);
		}

		return vesselScheduleSpecificationItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.cargo.ScheduleSpecificationEvent} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ScheduleSpecificationEventItemProvider scheduleSpecificationEventItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.cargo.ScheduleSpecificationEvent}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createScheduleSpecificationEventAdapter() {
		if (scheduleSpecificationEventItemProvider == null) {
			scheduleSpecificationEventItemProvider = new ScheduleSpecificationEventItemProvider(this);
		}

		return scheduleSpecificationEventItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.cargo.VesselEventSpecification} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected VesselEventSpecificationItemProvider vesselEventSpecificationItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.cargo.VesselEventSpecification}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createVesselEventSpecificationAdapter() {
		if (vesselEventSpecificationItemProvider == null) {
			vesselEventSpecificationItemProvider = new VesselEventSpecificationItemProvider(this);
		}

		return vesselEventSpecificationItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.cargo.VoyageSpecification} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected VoyageSpecificationItemProvider voyageSpecificationItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.cargo.VoyageSpecification}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createVoyageSpecificationAdapter() {
		if (voyageSpecificationItemProvider == null) {
			voyageSpecificationItemProvider = new VoyageSpecificationItemProvider(this);
		}

		return voyageSpecificationItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.cargo.SlotSpecification} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected SlotSpecificationItemProvider slotSpecificationItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.cargo.SlotSpecification}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createSlotSpecificationAdapter() {
		if (slotSpecificationItemProvider == null) {
			slotSpecificationItemProvider = new SlotSpecificationItemProvider(this);
		}

		return slotSpecificationItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.cargo.CharterInMarketOverride} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected CharterInMarketOverrideItemProvider charterInMarketOverrideItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.cargo.CharterInMarketOverride}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createCharterInMarketOverrideAdapter() {
		if (charterInMarketOverrideItemProvider == null) {
			charterInMarketOverrideItemProvider = new CharterInMarketOverrideItemProvider(this);
		}

		return charterInMarketOverrideItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.cargo.BuyPaperDeal} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected BuyPaperDealItemProvider buyPaperDealItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.cargo.BuyPaperDeal}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createBuyPaperDealAdapter() {
		if (buyPaperDealItemProvider == null) {
			buyPaperDealItemProvider = new BuyPaperDealItemProvider(this);
		}

		return buyPaperDealItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.cargo.SellPaperDeal} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected SellPaperDealItemProvider sellPaperDealItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.cargo.SellPaperDeal}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createSellPaperDealAdapter() {
		if (sellPaperDealItemProvider == null) {
			sellPaperDealItemProvider = new SellPaperDealItemProvider(this);
		}

		return sellPaperDealItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.cargo.DealSet} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected DealSetItemProvider dealSetItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.cargo.DealSet}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createDealSetAdapter() {
		if (dealSetItemProvider == null) {
			dealSetItemProvider = new DealSetItemProvider(this);
		}

		return dealSetItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.cargo.VesselGroupCanalParameters} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected VesselGroupCanalParametersItemProvider vesselGroupCanalParametersItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.cargo.VesselGroupCanalParameters}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createVesselGroupCanalParametersAdapter() {
		if (vesselGroupCanalParametersItemProvider == null) {
			vesselGroupCanalParametersItemProvider = new VesselGroupCanalParametersItemProvider(this);
		}

		return vesselGroupCanalParametersItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.cargo.PanamaSeasonalityRecord} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected PanamaSeasonalityRecordItemProvider panamaSeasonalityRecordItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.cargo.PanamaSeasonalityRecord}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createPanamaSeasonalityRecordAdapter() {
		if (panamaSeasonalityRecordItemProvider == null) {
			panamaSeasonalityRecordItemProvider = new PanamaSeasonalityRecordItemProvider(this);
		}

		return panamaSeasonalityRecordItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.cargo.GroupedSlotsConstraint} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected GroupedSlotsConstraintItemProvider groupedSlotsConstraintItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.cargo.GroupedSlotsConstraint}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createGroupedSlotsConstraintAdapter() {
		if (groupedSlotsConstraintItemProvider == null) {
			groupedSlotsConstraintItemProvider = new GroupedSlotsConstraintItemProvider(this);
		}

		return groupedSlotsConstraintItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.cargo.GroupedDischargeSlotsConstraint} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected GroupedDischargeSlotsConstraintItemProvider groupedDischargeSlotsConstraintItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.cargo.GroupedDischargeSlotsConstraint}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createGroupedDischargeSlotsConstraintAdapter() {
		if (groupedDischargeSlotsConstraintItemProvider == null) {
			groupedDischargeSlotsConstraintItemProvider = new GroupedDischargeSlotsConstraintItemProvider(this);
		}

		return groupedDischargeSlotsConstraintItemProvider;
	}

	/**
	 * This returns the root adapter factory that contains this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ComposeableAdapterFactory getRootAdapterFactory() {
		return parentAdapterFactory == null ? this : parentAdapterFactory.getRootAdapterFactory();
	}

	/**
	 * This sets the composed adapter factory that contains this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setParentAdapterFactory(ComposedAdapterFactory parentAdapterFactory) {
		this.parentAdapterFactory = parentAdapterFactory;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isFactoryForType(Object type) {
		return supportedTypes.contains(type) || super.isFactoryForType(type);
	}

	/**
	 * This implementation substitutes the factory itself as the key for the adapter.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter adapt(Notifier notifier, Object type) {
		return super.adapt(notifier, this);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object adapt(Object object, Object type) {
		if (isFactoryForType(type)) {
			Object adapter = super.adapt(object, type);
			if (!(type instanceof Class<?>) || (((Class<?>)type).isInstance(adapter))) {
				return adapter;
			}
		}

		return null;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public List<IChildCreationExtender> getChildCreationExtenders() {
		return childCreationExtenderManager.getChildCreationExtenders();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Collection<?> getNewChildDescriptors(Object object, EditingDomain editingDomain) {
		return childCreationExtenderManager.getNewChildDescriptors(object, editingDomain);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ResourceLocator getResourceLocator() {
		return childCreationExtenderManager;
	}

	/**
	 * This adds a listener.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void addListener(INotifyChangedListener notifyChangedListener) {
		changeNotifier.addListener(notifyChangedListener);
	}

	/**
	 * This removes a listener.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void removeListener(INotifyChangedListener notifyChangedListener) {
		changeNotifier.removeListener(notifyChangedListener);
	}

	/**
	 * This delegates to {@link #changeNotifier} and to {@link #parentAdapterFactory}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void fireNotifyChanged(Notification notification) {
		changeNotifier.fireNotifyChanged(notification);

		if (parentAdapterFactory != null) {
			parentAdapterFactory.fireNotifyChanged(notification);
		}
	}

	/**
	 * This disposes all of the item providers created by this factory. 
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void dispose() {
		if (cargoModelItemProvider != null) cargoModelItemProvider.dispose();
		if (cargoItemProvider != null) cargoItemProvider.dispose();
		if (loadSlotItemProvider != null) loadSlotItemProvider.dispose();
		if (dischargeSlotItemProvider != null) dischargeSlotItemProvider.dispose();
		if (spotLoadSlotItemProvider != null) spotLoadSlotItemProvider.dispose();
		if (spotDischargeSlotItemProvider != null) spotDischargeSlotItemProvider.dispose();
		if (cargoGroupItemProvider != null) cargoGroupItemProvider.dispose();
		if (vesselCharterItemProvider != null) vesselCharterItemProvider.dispose();
		if (maintenanceEventItemProvider != null) maintenanceEventItemProvider.dispose();
		if (dryDockEventItemProvider != null) dryDockEventItemProvider.dispose();
		if (charterLengthEventItemProvider != null) charterLengthEventItemProvider.dispose();
		if (charterOutEventItemProvider != null) charterOutEventItemProvider.dispose();
		if (vesselTypeGroupItemProvider != null) vesselTypeGroupItemProvider.dispose();
		if (inventoryEventRowItemProvider != null) inventoryEventRowItemProvider.dispose();
		if (inventoryCapacityRowItemProvider != null) inventoryCapacityRowItemProvider.dispose();
		if (inventoryItemProvider != null) inventoryItemProvider.dispose();
		if (canalBookingSlotItemProvider != null) canalBookingSlotItemProvider.dispose();
		if (canalBookingsItemProvider != null) canalBookingsItemProvider.dispose();
		if (scheduleSpecificationItemProvider != null) scheduleSpecificationItemProvider.dispose();
		if (nonShippedCargoSpecificationItemProvider != null) nonShippedCargoSpecificationItemProvider.dispose();
		if (vesselScheduleSpecificationItemProvider != null) vesselScheduleSpecificationItemProvider.dispose();
		if (scheduleSpecificationEventItemProvider != null) scheduleSpecificationEventItemProvider.dispose();
		if (vesselEventSpecificationItemProvider != null) vesselEventSpecificationItemProvider.dispose();
		if (voyageSpecificationItemProvider != null) voyageSpecificationItemProvider.dispose();
		if (slotSpecificationItemProvider != null) slotSpecificationItemProvider.dispose();
		if (charterInMarketOverrideItemProvider != null) charterInMarketOverrideItemProvider.dispose();
		if (buyPaperDealItemProvider != null) buyPaperDealItemProvider.dispose();
		if (sellPaperDealItemProvider != null) sellPaperDealItemProvider.dispose();
		if (dealSetItemProvider != null) dealSetItemProvider.dispose();
		if (vesselGroupCanalParametersItemProvider != null) vesselGroupCanalParametersItemProvider.dispose();
		if (panamaSeasonalityRecordItemProvider != null) panamaSeasonalityRecordItemProvider.dispose();
		if (groupedSlotsConstraintItemProvider != null) groupedSlotsConstraintItemProvider.dispose();
		if (groupedDischargeSlotsConstraintItemProvider != null) groupedDischargeSlotsConstraintItemProvider.dispose();
	}

}
