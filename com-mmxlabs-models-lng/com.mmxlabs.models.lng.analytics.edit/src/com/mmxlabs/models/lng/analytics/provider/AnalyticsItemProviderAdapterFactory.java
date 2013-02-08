/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.provider;

import com.mmxlabs.models.lng.analytics.util.AnalyticsAdapterFactory;

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
public class AnalyticsItemProviderAdapterFactory extends AnalyticsAdapterFactory implements ComposeableAdapterFactory, IChangeNotifier, IDisposable {
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
	public AnalyticsItemProviderAdapterFactory() {
		supportedTypes.add(IEditingDomainItemProvider.class);
		supportedTypes.add(IStructuredItemContentProvider.class);
		supportedTypes.add(ITreeItemContentProvider.class);
		supportedTypes.add(IItemLabelProvider.class);
		supportedTypes.add(IItemPropertySource.class);
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.analytics.AnalyticsModel} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected AnalyticsModelItemProvider analyticsModelItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.analytics.AnalyticsModel}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createAnalyticsModelAdapter() {
		if (analyticsModelItemProvider == null) {
			analyticsModelItemProvider = new AnalyticsModelItemProvider(this);
		}

		return analyticsModelItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.analytics.UnitCostMatrix} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected UnitCostMatrixItemProvider unitCostMatrixItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.analytics.UnitCostMatrix}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createUnitCostMatrixAdapter() {
		if (unitCostMatrixItemProvider == null) {
			unitCostMatrixItemProvider = new UnitCostMatrixItemProvider(this);
		}

		return unitCostMatrixItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.analytics.UnitCostLine} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected UnitCostLineItemProvider unitCostLineItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.analytics.UnitCostLine}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createUnitCostLineAdapter() {
		if (unitCostLineItemProvider == null) {
			unitCostLineItemProvider = new UnitCostLineItemProvider(this);
		}

		return unitCostLineItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.analytics.Voyage} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected VoyageItemProvider voyageItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.analytics.Voyage}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createVoyageAdapter() {
		if (voyageItemProvider == null) {
			voyageItemProvider = new VoyageItemProvider(this);
		}

		return voyageItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.analytics.Visit} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected VisitItemProvider visitItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.analytics.Visit}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createVisitAdapter() {
		if (visitItemProvider == null) {
			visitItemProvider = new VisitItemProvider(this);
		}

		return visitItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.analytics.CostComponent} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected CostComponentItemProvider costComponentItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.analytics.CostComponent}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createCostComponentAdapter() {
		if (costComponentItemProvider == null) {
			costComponentItemProvider = new CostComponentItemProvider(this);
		}

		return costComponentItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.analytics.FuelCost} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected FuelCostItemProvider fuelCostItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.analytics.FuelCost}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createFuelCostAdapter() {
		if (fuelCostItemProvider == null) {
			fuelCostItemProvider = new FuelCostItemProvider(this);
		}

		return fuelCostItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.analytics.Journey} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected JourneyItemProvider journeyItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.analytics.Journey}.
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
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.analytics.ShippingCostPlan} instances.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ShippingCostPlanItemProvider shippingCostPlanItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.analytics.ShippingCostPlan}.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createShippingCostPlanAdapter() {
		if (shippingCostPlanItemProvider == null) {
			shippingCostPlanItemProvider = new ShippingCostPlanItemProvider(this);
		}

		return shippingCostPlanItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.analytics.ShippingCostRow} instances.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ShippingCostRowItemProvider shippingCostRowItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.analytics.ShippingCostRow}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createShippingCostRowAdapter() {
		if (shippingCostRowItemProvider == null) {
			shippingCostRowItemProvider = new ShippingCostRowItemProvider(this);
		}

		return shippingCostRowItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.analytics.CargoSandbox} instances.
	 * <!-- begin-user-doc -->
	 * @since 3.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected CargoSandboxItemProvider cargoSandboxItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.analytics.CargoSandbox}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createCargoSandboxAdapter() {
		if (cargoSandboxItemProvider == null) {
			cargoSandboxItemProvider = new CargoSandboxItemProvider(this);
		}

		return cargoSandboxItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.analytics.ProvisionalCargo} instances.
	 * <!-- begin-user-doc -->
	 * @since 3.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ProvisionalCargoItemProvider provisionalCargoItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.analytics.ProvisionalCargo}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createProvisionalCargoAdapter() {
		if (provisionalCargoItemProvider == null) {
			provisionalCargoItemProvider = new ProvisionalCargoItemProvider(this);
		}

		return provisionalCargoItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.analytics.BuyOpportunity} instances.
	 * <!-- begin-user-doc -->
	 * @since 3.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected BuyOpportunityItemProvider buyOpportunityItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.analytics.BuyOpportunity}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createBuyOpportunityAdapter() {
		if (buyOpportunityItemProvider == null) {
			buyOpportunityItemProvider = new BuyOpportunityItemProvider(this);
		}

		return buyOpportunityItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.analytics.SellOpportunity} instances.
	 * <!-- begin-user-doc -->
	 * @since 3.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected SellOpportunityItemProvider sellOpportunityItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.analytics.SellOpportunity}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createSellOpportunityAdapter() {
		if (sellOpportunityItemProvider == null) {
			sellOpportunityItemProvider = new SellOpportunityItemProvider(this);
		}

		return sellOpportunityItemProvider;
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
		if (analyticsModelItemProvider != null) analyticsModelItemProvider.dispose();
		if (unitCostMatrixItemProvider != null) unitCostMatrixItemProvider.dispose();
		if (unitCostLineItemProvider != null) unitCostLineItemProvider.dispose();
		if (voyageItemProvider != null) voyageItemProvider.dispose();
		if (visitItemProvider != null) visitItemProvider.dispose();
		if (costComponentItemProvider != null) costComponentItemProvider.dispose();
		if (fuelCostItemProvider != null) fuelCostItemProvider.dispose();
		if (journeyItemProvider != null) journeyItemProvider.dispose();
		if (shippingCostPlanItemProvider != null) shippingCostPlanItemProvider.dispose();
		if (shippingCostRowItemProvider != null) shippingCostRowItemProvider.dispose();
		if (cargoSandboxItemProvider != null) cargoSandboxItemProvider.dispose();
		if (provisionalCargoItemProvider != null) provisionalCargoItemProvider.dispose();
		if (buyOpportunityItemProvider != null) buyOpportunityItemProvider.dispose();
		if (sellOpportunityItemProvider != null) sellOpportunityItemProvider.dispose();
	}

}
