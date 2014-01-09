/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.provider;

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

import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.lng.pricing.util.PricingAdapterFactory;

/**
 * This is the factory that is used to provide the interfaces needed to support Viewers.
 * The adapters generated by this factory convert EMF adapter notifications into calls to {@link #fireNotifyChanged fireNotifyChanged}.
 * The adapters also support Eclipse property sheets.
 * Note that most of the adapters are shared among multiple instances.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class PricingItemProviderAdapterFactory extends PricingAdapterFactory implements ComposeableAdapterFactory, IChangeNotifier, IDisposable, IChildCreationExtender {
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
	 * @since 4.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ChildCreationExtenderManager childCreationExtenderManager = new ChildCreationExtenderManager(PricingEditPlugin.INSTANCE, PricingPackage.eNS_URI);

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
	public PricingItemProviderAdapterFactory() {
		supportedTypes.add(IEditingDomainItemProvider.class);
		supportedTypes.add(IStructuredItemContentProvider.class);
		supportedTypes.add(ITreeItemContentProvider.class);
		supportedTypes.add(IItemLabelProvider.class);
		supportedTypes.add(IItemPropertySource.class);
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.pricing.PricingModel} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected PricingModelItemProvider pricingModelItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.pricing.PricingModel}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createPricingModelAdapter() {
		if (pricingModelItemProvider == null) {
			pricingModelItemProvider = new PricingModelItemProvider(this);
		}

		return pricingModelItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.pricing.DataIndex} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected DataIndexItemProvider dataIndexItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.pricing.DataIndex}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createDataIndexAdapter() {
		if (dataIndexItemProvider == null) {
			dataIndexItemProvider = new DataIndexItemProvider(this);
		}

		return dataIndexItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.pricing.DerivedIndex} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected DerivedIndexItemProvider derivedIndexItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.pricing.DerivedIndex}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createDerivedIndexAdapter() {
		if (derivedIndexItemProvider == null) {
			derivedIndexItemProvider = new DerivedIndexItemProvider(this);
		}

		return derivedIndexItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.pricing.IndexPoint} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected IndexPointItemProvider indexPointItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.pricing.IndexPoint}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createIndexPointAdapter() {
		if (indexPointItemProvider == null) {
			indexPointItemProvider = new IndexPointItemProvider(this);
		}

		return indexPointItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.pricing.FleetCostModel} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected FleetCostModelItemProvider fleetCostModelItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.pricing.FleetCostModel}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createFleetCostModelAdapter() {
		if (fleetCostModelItemProvider == null) {
			fleetCostModelItemProvider = new FleetCostModelItemProvider(this);
		}

		return fleetCostModelItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.pricing.RouteCost} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected RouteCostItemProvider routeCostItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.pricing.RouteCost}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createRouteCostAdapter() {
		if (routeCostItemProvider == null) {
			routeCostItemProvider = new RouteCostItemProvider(this);
		}

		return routeCostItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.pricing.BaseFuelCost} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected BaseFuelCostItemProvider baseFuelCostItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.pricing.BaseFuelCost}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createBaseFuelCostAdapter() {
		if (baseFuelCostItemProvider == null) {
			baseFuelCostItemProvider = new BaseFuelCostItemProvider(this);
		}

		return baseFuelCostItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.pricing.PortCost} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected PortCostItemProvider portCostItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.pricing.PortCost}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createPortCostAdapter() {
		if (portCostItemProvider == null) {
			portCostItemProvider = new PortCostItemProvider(this);
		}

		return portCostItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.pricing.PortCostEntry} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected PortCostEntryItemProvider portCostEntryItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.pricing.PortCostEntry}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createPortCostEntryAdapter() {
		if (portCostEntryItemProvider == null) {
			portCostEntryItemProvider = new PortCostEntryItemProvider(this);
		}

		return portCostEntryItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.pricing.CooldownPrice} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected CooldownPriceItemProvider cooldownPriceItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.pricing.CooldownPrice}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createCooldownPriceAdapter() {
		if (cooldownPriceItemProvider == null) {
			cooldownPriceItemProvider = new CooldownPriceItemProvider(this);
		}

		return cooldownPriceItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.pricing.CommodityIndex} instances.
	 * <!-- begin-user-doc -->
	 * @since 5.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected CommodityIndexItemProvider commodityIndexItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.pricing.CommodityIndex}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createCommodityIndexAdapter() {
		if (commodityIndexItemProvider == null) {
			commodityIndexItemProvider = new CommodityIndexItemProvider(this);
		}

		return commodityIndexItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.pricing.CharterIndex} instances.
	 * <!-- begin-user-doc -->
	 * @since 5.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected CharterIndexItemProvider charterIndexItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.pricing.CharterIndex}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createCharterIndexAdapter() {
		if (charterIndexItemProvider == null) {
			charterIndexItemProvider = new CharterIndexItemProvider(this);
		}

		return charterIndexItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.pricing.BaseFuelIndex} instances.
	 * <!-- begin-user-doc -->
	 * @since 5.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected BaseFuelIndexItemProvider baseFuelIndexItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.pricing.BaseFuelIndex}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createBaseFuelIndexAdapter() {
		if (baseFuelIndexItemProvider == null) {
			baseFuelIndexItemProvider = new BaseFuelIndexItemProvider(this);
		}

		return baseFuelIndexItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.pricing.NamedIndexContainer} instances.
	 * <!-- begin-user-doc -->
	 * @since 5.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected NamedIndexContainerItemProvider namedIndexContainerItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.pricing.NamedIndexContainer}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createNamedIndexContainerAdapter() {
		if (namedIndexContainerItemProvider == null) {
			namedIndexContainerItemProvider = new NamedIndexContainerItemProvider(this);
		}

		return namedIndexContainerItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.pricing.PortsPriceMap} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected PortsPriceMapItemProvider portsPriceMapItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.pricing.PortsPriceMap}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createPortsPriceMapAdapter() {
		if (portsPriceMapItemProvider == null) {
			portsPriceMapItemProvider = new PortsPriceMapItemProvider(this);
		}

		return portsPriceMapItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.pricing.PortsExpressionMap} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected PortsExpressionMapItemProvider portsExpressionMapItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.pricing.PortsExpressionMap}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createPortsExpressionMapAdapter() {
		if (portsExpressionMapItemProvider == null) {
			portsExpressionMapItemProvider = new PortsExpressionMapItemProvider(this);
		}

		return portsExpressionMapItemProvider;
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
	 * @since 4.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public List<IChildCreationExtender> getChildCreationExtenders() {
		return childCreationExtenderManager.getChildCreationExtenders();
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 4.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Collection<?> getNewChildDescriptors(Object object, EditingDomain editingDomain) {
		return childCreationExtenderManager.getNewChildDescriptors(object, editingDomain);
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 4.0
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
		if (pricingModelItemProvider != null) pricingModelItemProvider.dispose();
		if (dataIndexItemProvider != null) dataIndexItemProvider.dispose();
		if (derivedIndexItemProvider != null) derivedIndexItemProvider.dispose();
		if (indexPointItemProvider != null) indexPointItemProvider.dispose();
		if (fleetCostModelItemProvider != null) fleetCostModelItemProvider.dispose();
		if (routeCostItemProvider != null) routeCostItemProvider.dispose();
		if (baseFuelCostItemProvider != null) baseFuelCostItemProvider.dispose();
		if (portCostItemProvider != null) portCostItemProvider.dispose();
		if (portCostEntryItemProvider != null) portCostEntryItemProvider.dispose();
		if (cooldownPriceItemProvider != null) cooldownPriceItemProvider.dispose();
		if (commodityIndexItemProvider != null) commodityIndexItemProvider.dispose();
		if (charterIndexItemProvider != null) charterIndexItemProvider.dispose();
		if (baseFuelIndexItemProvider != null) baseFuelIndexItemProvider.dispose();
		if (namedIndexContainerItemProvider != null) namedIndexContainerItemProvider.dispose();
		if (portsPriceMapItemProvider != null) portsPriceMapItemProvider.dispose();
		if (portsExpressionMapItemProvider != null) portsExpressionMapItemProvider.dispose();
	}

}
