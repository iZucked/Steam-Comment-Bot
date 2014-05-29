/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
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
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.cargo.VesselAvailability} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected VesselAvailabilityItemProvider vesselAvailabilityItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.cargo.VesselAvailability}.
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
		if (cargoItemProvider != null) cargoItemProvider.dispose();
		if (loadSlotItemProvider != null) loadSlotItemProvider.dispose();
		if (dischargeSlotItemProvider != null) dischargeSlotItemProvider.dispose();
		if (cargoModelItemProvider != null) cargoModelItemProvider.dispose();
		if (spotLoadSlotItemProvider != null) spotLoadSlotItemProvider.dispose();
		if (spotDischargeSlotItemProvider != null) spotDischargeSlotItemProvider.dispose();
		if (cargoGroupItemProvider != null) cargoGroupItemProvider.dispose();
		if (vesselAvailabilityItemProvider != null) vesselAvailabilityItemProvider.dispose();
		if (maintenanceEventItemProvider != null) maintenanceEventItemProvider.dispose();
		if (dryDockEventItemProvider != null) dryDockEventItemProvider.dispose();
		if (charterOutEventItemProvider != null) charterOutEventItemProvider.dispose();
		if (vesselTypeGroupItemProvider != null) vesselTypeGroupItemProvider.dispose();
	}

}
