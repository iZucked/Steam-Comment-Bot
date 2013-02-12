/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.schedule.provider;

import com.mmxlabs.models.lng.schedule.util.ScheduleAdapterFactory;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;

import org.eclipse.emf.edit.provider.ChangeNotifier;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.IChangeNotifier;
import org.eclipse.emf.edit.provider.IDisposable;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.INotifyChangedListener;
import org.eclipse.emf.edit.provider.IStructuredItemContentProvider;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;

/**
 * This is the factory that is used to provide the interfaces needed to support Viewers.
 * The adapters generated by this factory convert EMF adapter notifications into calls to {@link #fireNotifyChanged fireNotifyChanged}.
 * The adapters also support Eclipse property sheets.
 * Note that most of the adapters are shared among multiple instances.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class ScheduleItemProviderAdapterFactory extends ScheduleAdapterFactory implements ComposeableAdapterFactory, IChangeNotifier, IDisposable {
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
	public ScheduleItemProviderAdapterFactory() {
		supportedTypes.add(IEditingDomainItemProvider.class);
		supportedTypes.add(IStructuredItemContentProvider.class);
		supportedTypes.add(ITreeItemContentProvider.class);
		supportedTypes.add(IItemLabelProvider.class);
		supportedTypes.add(IItemPropertySource.class);
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.schedule.ScheduleModel} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ScheduleModelItemProvider scheduleModelItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.schedule.ScheduleModel}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createScheduleModelAdapter() {
		if (scheduleModelItemProvider == null) {
			scheduleModelItemProvider = new ScheduleModelItemProvider(this);
		}

		return scheduleModelItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.schedule.Schedule} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ScheduleItemProvider scheduleItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.schedule.Schedule}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createScheduleAdapter() {
		if (scheduleItemProvider == null) {
			scheduleItemProvider = new ScheduleItemProvider(this);
		}

		return scheduleItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.schedule.Sequence} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected SequenceItemProvider sequenceItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.schedule.Sequence}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createSequenceAdapter() {
		if (sequenceItemProvider == null) {
			sequenceItemProvider = new SequenceItemProvider(this);
		}

		return sequenceItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.schedule.Event} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EventItemProvider eventItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.schedule.Event}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createEventAdapter() {
		if (eventItemProvider == null) {
			eventItemProvider = new EventItemProvider(this);
		}

		return eventItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.schedule.SlotVisit} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected SlotVisitItemProvider slotVisitItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.schedule.SlotVisit}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createSlotVisitAdapter() {
		if (slotVisitItemProvider == null) {
			slotVisitItemProvider = new SlotVisitItemProvider(this);
		}

		return slotVisitItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.schedule.VesselEventVisit} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected VesselEventVisitItemProvider vesselEventVisitItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.schedule.VesselEventVisit}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createVesselEventVisitAdapter() {
		if (vesselEventVisitItemProvider == null) {
			vesselEventVisitItemProvider = new VesselEventVisitItemProvider(this);
		}

		return vesselEventVisitItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.schedule.Journey} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected JourneyItemProvider journeyItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.schedule.Journey}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createJourneyAdapter() {
		if (journeyItemProvider == null) {
			journeyItemProvider = new JourneyItemProvider(this);
		}

		return journeyItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.schedule.Idle} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected IdleItemProvider idleItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.schedule.Idle}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createIdleAdapter() {
		if (idleItemProvider == null) {
			idleItemProvider = new IdleItemProvider(this);
		}

		return idleItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.schedule.GeneratedCharterOut} instances.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected GeneratedCharterOutItemProvider generatedCharterOutItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.schedule.GeneratedCharterOut}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createGeneratedCharterOutAdapter() {
		if (generatedCharterOutItemProvider == null) {
			generatedCharterOutItemProvider = new GeneratedCharterOutItemProvider(this);
		}

		return generatedCharterOutItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.schedule.FuelUsage} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected FuelUsageItemProvider fuelUsageItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.schedule.FuelUsage}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createFuelUsageAdapter() {
		if (fuelUsageItemProvider == null) {
			fuelUsageItemProvider = new FuelUsageItemProvider(this);
		}

		return fuelUsageItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.schedule.FuelQuantity} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected FuelQuantityItemProvider fuelQuantityItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.schedule.FuelQuantity}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createFuelQuantityAdapter() {
		if (fuelQuantityItemProvider == null) {
			fuelQuantityItemProvider = new FuelQuantityItemProvider(this);
		}

		return fuelQuantityItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.schedule.Cooldown} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected CooldownItemProvider cooldownItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.schedule.Cooldown}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createCooldownAdapter() {
		if (cooldownItemProvider == null) {
			cooldownItemProvider = new CooldownItemProvider(this);
		}

		return cooldownItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.schedule.CargoAllocation} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected CargoAllocationItemProvider cargoAllocationItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.schedule.CargoAllocation}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createCargoAllocationAdapter() {
		if (cargoAllocationItemProvider == null) {
			cargoAllocationItemProvider = new CargoAllocationItemProvider(this);
		}

		return cargoAllocationItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.schedule.SlotAllocation} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected SlotAllocationItemProvider slotAllocationItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.schedule.SlotAllocation}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createSlotAllocationAdapter() {
		if (slotAllocationItemProvider == null) {
			slotAllocationItemProvider = new SlotAllocationItemProvider(this);
		}

		return slotAllocationItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.schedule.FuelAmount} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected FuelAmountItemProvider fuelAmountItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.schedule.FuelAmount}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createFuelAmountAdapter() {
		if (fuelAmountItemProvider == null) {
			fuelAmountItemProvider = new FuelAmountItemProvider(this);
		}

		return fuelAmountItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.schedule.Fitness} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected FitnessItemProvider fitnessItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.schedule.Fitness}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createFitnessAdapter() {
		if (fitnessItemProvider == null) {
			fitnessItemProvider = new FitnessItemProvider(this);
		}

		return fitnessItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.schedule.PortVisit} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected PortVisitItemProvider portVisitItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.schedule.PortVisit}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createPortVisitAdapter() {
		if (portVisitItemProvider == null) {
			portVisitItemProvider = new PortVisitItemProvider(this);
		}

		return portVisitItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.schedule.StartEvent} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected StartEventItemProvider startEventItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.schedule.StartEvent}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createStartEventAdapter() {
		if (startEventItemProvider == null) {
			startEventItemProvider = new StartEventItemProvider(this);
		}

		return startEventItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.schedule.EndEvent} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EndEventItemProvider endEventItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.schedule.EndEvent}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createEndEventAdapter() {
		if (endEventItemProvider == null) {
			endEventItemProvider = new EndEventItemProvider(this);
		}

		return endEventItemProvider;
	}

	/**
	 * This returns the root adapter factory that contains this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ComposeableAdapterFactory getRootAdapterFactory() {
		return parentAdapterFactory == null ? this : parentAdapterFactory.getRootAdapterFactory();
	}

	/**
	 * This sets the composed adapter factory that contains this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
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
	 * This adds a listener.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void addListener(INotifyChangedListener notifyChangedListener) {
		changeNotifier.addListener(notifyChangedListener);
	}

	/**
	 * This removes a listener.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void removeListener(INotifyChangedListener notifyChangedListener) {
		changeNotifier.removeListener(notifyChangedListener);
	}

	/**
	 * This delegates to {@link #changeNotifier} and to {@link #parentAdapterFactory}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
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
	public void dispose() {
		if (scheduleModelItemProvider != null) scheduleModelItemProvider.dispose();
		if (scheduleItemProvider != null) scheduleItemProvider.dispose();
		if (sequenceItemProvider != null) sequenceItemProvider.dispose();
		if (eventItemProvider != null) eventItemProvider.dispose();
		if (slotVisitItemProvider != null) slotVisitItemProvider.dispose();
		if (vesselEventVisitItemProvider != null) vesselEventVisitItemProvider.dispose();
		if (journeyItemProvider != null) journeyItemProvider.dispose();
		if (idleItemProvider != null) idleItemProvider.dispose();
		if (generatedCharterOutItemProvider != null) generatedCharterOutItemProvider.dispose();
		if (fuelUsageItemProvider != null) fuelUsageItemProvider.dispose();
		if (fuelQuantityItemProvider != null) fuelQuantityItemProvider.dispose();
		if (cooldownItemProvider != null) cooldownItemProvider.dispose();
		if (cargoAllocationItemProvider != null) cargoAllocationItemProvider.dispose();
		if (slotAllocationItemProvider != null) slotAllocationItemProvider.dispose();
		if (fuelAmountItemProvider != null) fuelAmountItemProvider.dispose();
		if (fitnessItemProvider != null) fitnessItemProvider.dispose();
		if (portVisitItemProvider != null) portVisitItemProvider.dispose();
		if (startEventItemProvider != null) startEventItemProvider.dispose();
		if (endEventItemProvider != null) endEventItemProvider.dispose();
	}

}
