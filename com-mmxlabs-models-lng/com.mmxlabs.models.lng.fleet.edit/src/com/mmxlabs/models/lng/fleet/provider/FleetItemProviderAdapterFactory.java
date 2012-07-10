/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet.provider;

import com.mmxlabs.models.lng.fleet.util.FleetAdapterFactory;

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
public class FleetItemProviderAdapterFactory extends FleetAdapterFactory implements ComposeableAdapterFactory, IChangeNotifier, IDisposable {
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
	public FleetItemProviderAdapterFactory() {
		supportedTypes.add(IEditingDomainItemProvider.class);
		supportedTypes.add(IStructuredItemContentProvider.class);
		supportedTypes.add(ITreeItemContentProvider.class);
		supportedTypes.add(IItemLabelProvider.class);
		supportedTypes.add(IItemPropertySource.class);
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.fleet.Vessel} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected VesselItemProvider vesselItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.fleet.Vessel}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createVesselAdapter() {
		if (vesselItemProvider == null) {
			vesselItemProvider = new VesselItemProvider(this);
		}

		return vesselItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.fleet.VesselClass} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected VesselClassItemProvider vesselClassItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.fleet.VesselClass}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createVesselClassAdapter() {
		if (vesselClassItemProvider == null) {
			vesselClassItemProvider = new VesselClassItemProvider(this);
		}

		return vesselClassItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.fleet.FleetModel} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected FleetModelItemProvider fleetModelItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.fleet.FleetModel}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createFleetModelAdapter() {
		if (fleetModelItemProvider == null) {
			fleetModelItemProvider = new FleetModelItemProvider(this);
		}

		return fleetModelItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.fleet.BaseFuel} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected BaseFuelItemProvider baseFuelItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.fleet.BaseFuel}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createBaseFuelAdapter() {
		if (baseFuelItemProvider == null) {
			baseFuelItemProvider = new BaseFuelItemProvider(this);
		}

		return baseFuelItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.fleet.DryDockEvent} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected DryDockEventItemProvider dryDockEventItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.fleet.DryDockEvent}.
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
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.fleet.CharterOutEvent} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected CharterOutEventItemProvider charterOutEventItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.fleet.CharterOutEvent}.
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
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.fleet.HeelOptions} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected HeelOptionsItemProvider heelOptionsItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.fleet.HeelOptions}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createHeelOptionsAdapter() {
		if (heelOptionsItemProvider == null) {
			heelOptionsItemProvider = new HeelOptionsItemProvider(this);
		}

		return heelOptionsItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.fleet.VesselStateAttributes} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected VesselStateAttributesItemProvider vesselStateAttributesItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.fleet.VesselStateAttributes}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createVesselStateAttributesAdapter() {
		if (vesselStateAttributesItemProvider == null) {
			vesselStateAttributesItemProvider = new VesselStateAttributesItemProvider(this);
		}

		return vesselStateAttributesItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.fleet.VesselAvailability} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected VesselAvailabilityItemProvider vesselAvailabilityItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.fleet.VesselAvailability}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createVesselAvailabilityAdapter() {
		if (vesselAvailabilityItemProvider == null) {
			vesselAvailabilityItemProvider = new VesselAvailabilityItemProvider(this);
		}

		return vesselAvailabilityItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.fleet.FuelConsumption} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected FuelConsumptionItemProvider fuelConsumptionItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.fleet.FuelConsumption}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createFuelConsumptionAdapter() {
		if (fuelConsumptionItemProvider == null) {
			fuelConsumptionItemProvider = new FuelConsumptionItemProvider(this);
		}

		return fuelConsumptionItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.fleet.MaintenanceEvent} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected MaintenanceEventItemProvider maintenanceEventItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.fleet.MaintenanceEvent}.
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
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.fleet.VesselClassRouteParameters} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected VesselClassRouteParametersItemProvider vesselClassRouteParametersItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.fleet.VesselClassRouteParameters}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createVesselClassRouteParametersAdapter() {
		if (vesselClassRouteParametersItemProvider == null) {
			vesselClassRouteParametersItemProvider = new VesselClassRouteParametersItemProvider(this);
		}

		return vesselClassRouteParametersItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.fleet.VesselGroup} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected VesselGroupItemProvider vesselGroupItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.fleet.VesselGroup}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createVesselGroupAdapter() {
		if (vesselGroupItemProvider == null) {
			vesselGroupItemProvider = new VesselGroupItemProvider(this);
		}

		return vesselGroupItemProvider;
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
		if (vesselItemProvider != null) vesselItemProvider.dispose();
		if (vesselClassItemProvider != null) vesselClassItemProvider.dispose();
		if (fleetModelItemProvider != null) fleetModelItemProvider.dispose();
		if (baseFuelItemProvider != null) baseFuelItemProvider.dispose();
		if (dryDockEventItemProvider != null) dryDockEventItemProvider.dispose();
		if (charterOutEventItemProvider != null) charterOutEventItemProvider.dispose();
		if (heelOptionsItemProvider != null) heelOptionsItemProvider.dispose();
		if (vesselStateAttributesItemProvider != null) vesselStateAttributesItemProvider.dispose();
		if (vesselAvailabilityItemProvider != null) vesselAvailabilityItemProvider.dispose();
		if (fuelConsumptionItemProvider != null) fuelConsumptionItemProvider.dispose();
		if (maintenanceEventItemProvider != null) maintenanceEventItemProvider.dispose();
		if (vesselClassRouteParametersItemProvider != null) vesselClassRouteParametersItemProvider.dispose();
		if (vesselGroupItemProvider != null) vesselGroupItemProvider.dispose();
	}

}
