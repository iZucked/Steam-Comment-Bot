/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.adp.provider;


import com.mmxlabs.models.lng.adp.ADPPackage;

import com.mmxlabs.models.lng.adp.util.ADPAdapterFactory;

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

/**
 * This is the factory that is used to provide the interfaces needed to support Viewers.
 * The adapters generated by this factory convert EMF adapter notifications into calls to {@link #fireNotifyChanged fireNotifyChanged}.
 * The adapters also support Eclipse property sheets.
 * Note that most of the adapters are shared among multiple instances.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class ADPItemProviderAdapterFactory extends ADPAdapterFactory implements ComposeableAdapterFactory, IChangeNotifier, IDisposable, IChildCreationExtender {
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
	protected ChildCreationExtenderManager childCreationExtenderManager = new ChildCreationExtenderManager(ADPEditPlugin.INSTANCE, ADPPackage.eNS_URI);

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
	public ADPItemProviderAdapterFactory() {
		supportedTypes.add(IEditingDomainItemProvider.class);
		supportedTypes.add(IStructuredItemContentProvider.class);
		supportedTypes.add(ITreeItemContentProvider.class);
		supportedTypes.add(IItemLabelProvider.class);
		supportedTypes.add(IItemPropertySource.class);
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.adp.ADPModel} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ADPModelItemProvider adpModelItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.adp.ADPModel}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createADPModelAdapter() {
		if (adpModelItemProvider == null) {
			adpModelItemProvider = new ADPModelItemProvider(this);
		}

		return adpModelItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.adp.FleetProfile} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected FleetProfileItemProvider fleetProfileItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.adp.FleetProfile}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createFleetProfileAdapter() {
		if (fleetProfileItemProvider == null) {
			fleetProfileItemProvider = new FleetProfileItemProvider(this);
		}

		return fleetProfileItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.adp.ContractProfile} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ContractProfileItemProvider contractProfileItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.adp.ContractProfile}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createContractProfileAdapter() {
		if (contractProfileItemProvider == null) {
			contractProfileItemProvider = new ContractProfileItemProvider(this);
		}

		return contractProfileItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.adp.SpotMarketsProfile} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected SpotMarketsProfileItemProvider spotMarketsProfileItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.adp.SpotMarketsProfile}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createSpotMarketsProfileAdapter() {
		if (spotMarketsProfileItemProvider == null) {
			spotMarketsProfileItemProvider = new SpotMarketsProfileItemProvider(this);
		}

		return spotMarketsProfileItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.adp.CargoSizeDistributionModel} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected CargoSizeDistributionModelItemProvider cargoSizeDistributionModelItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.adp.CargoSizeDistributionModel}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createCargoSizeDistributionModelAdapter() {
		if (cargoSizeDistributionModelItemProvider == null) {
			cargoSizeDistributionModelItemProvider = new CargoSizeDistributionModelItemProvider(this);
		}

		return cargoSizeDistributionModelItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.adp.CargoNumberDistributionModel} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected CargoNumberDistributionModelItemProvider cargoNumberDistributionModelItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.adp.CargoNumberDistributionModel}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createCargoNumberDistributionModelAdapter() {
		if (cargoNumberDistributionModelItemProvider == null) {
			cargoNumberDistributionModelItemProvider = new CargoNumberDistributionModelItemProvider(this);
		}

		return cargoNumberDistributionModelItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.adp.PurchaseContractProfile} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected PurchaseContractProfileItemProvider purchaseContractProfileItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.adp.PurchaseContractProfile}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createPurchaseContractProfileAdapter() {
		if (purchaseContractProfileItemProvider == null) {
			purchaseContractProfileItemProvider = new PurchaseContractProfileItemProvider(this);
		}

		return purchaseContractProfileItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.adp.SalesContractProfile} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected SalesContractProfileItemProvider salesContractProfileItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.adp.SalesContractProfile}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createSalesContractProfileAdapter() {
		if (salesContractProfileItemProvider == null) {
			salesContractProfileItemProvider = new SalesContractProfileItemProvider(this);
		}

		return salesContractProfileItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.adp.SubContractProfile} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected SubContractProfileItemProvider subContractProfileItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.adp.SubContractProfile}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createSubContractProfileAdapter() {
		if (subContractProfileItemProvider == null) {
			subContractProfileItemProvider = new SubContractProfileItemProvider(this);
		}

		return subContractProfileItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.adp.CargoByQuarterDistributionModel} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected CargoByQuarterDistributionModelItemProvider cargoByQuarterDistributionModelItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.adp.CargoByQuarterDistributionModel}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createCargoByQuarterDistributionModelAdapter() {
		if (cargoByQuarterDistributionModelItemProvider == null) {
			cargoByQuarterDistributionModelItemProvider = new CargoByQuarterDistributionModelItemProvider(this);
		}

		return cargoByQuarterDistributionModelItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.adp.CargoIntervalDistributionModel} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected CargoIntervalDistributionModelItemProvider cargoIntervalDistributionModelItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.adp.CargoIntervalDistributionModel}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createCargoIntervalDistributionModelAdapter() {
		if (cargoIntervalDistributionModelItemProvider == null) {
			cargoIntervalDistributionModelItemProvider = new CargoIntervalDistributionModelItemProvider(this);
		}

		return cargoIntervalDistributionModelItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.adp.PreDefinedDistributionModel} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected PreDefinedDistributionModelItemProvider preDefinedDistributionModelItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.adp.PreDefinedDistributionModel}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createPreDefinedDistributionModelAdapter() {
		if (preDefinedDistributionModelItemProvider == null) {
			preDefinedDistributionModelItemProvider = new PreDefinedDistributionModelItemProvider(this);
		}

		return preDefinedDistributionModelItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.adp.PreDefinedDate} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected PreDefinedDateItemProvider preDefinedDateItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.adp.PreDefinedDate}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createPreDefinedDateAdapter() {
		if (preDefinedDateItemProvider == null) {
			preDefinedDateItemProvider = new PreDefinedDateItemProvider(this);
		}

		return preDefinedDateItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.adp.FlowType} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected FlowTypeItemProvider flowTypeItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.adp.FlowType}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createFlowTypeAdapter() {
		if (flowTypeItemProvider == null) {
			flowTypeItemProvider = new FlowTypeItemProvider(this);
		}

		return flowTypeItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.adp.SupplyFromFlow} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected SupplyFromFlowItemProvider supplyFromFlowItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.adp.SupplyFromFlow}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createSupplyFromFlowAdapter() {
		if (supplyFromFlowItemProvider == null) {
			supplyFromFlowItemProvider = new SupplyFromFlowItemProvider(this);
		}

		return supplyFromFlowItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.adp.DeliverToFlow} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected DeliverToFlowItemProvider deliverToFlowItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.adp.DeliverToFlow}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createDeliverToFlowAdapter() {
		if (deliverToFlowItemProvider == null) {
			deliverToFlowItemProvider = new DeliverToFlowItemProvider(this);
		}

		return deliverToFlowItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.adp.SupplyFromProfileFlow} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected SupplyFromProfileFlowItemProvider supplyFromProfileFlowItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.adp.SupplyFromProfileFlow}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createSupplyFromProfileFlowAdapter() {
		if (supplyFromProfileFlowItemProvider == null) {
			supplyFromProfileFlowItemProvider = new SupplyFromProfileFlowItemProvider(this);
		}

		return supplyFromProfileFlowItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.adp.DeliverToProfileFlow} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected DeliverToProfileFlowItemProvider deliverToProfileFlowItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.adp.DeliverToProfileFlow}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createDeliverToProfileFlowAdapter() {
		if (deliverToProfileFlowItemProvider == null) {
			deliverToProfileFlowItemProvider = new DeliverToProfileFlowItemProvider(this);
		}

		return deliverToProfileFlowItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.adp.SupplyFromSpotFlow} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected SupplyFromSpotFlowItemProvider supplyFromSpotFlowItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.adp.SupplyFromSpotFlow}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createSupplyFromSpotFlowAdapter() {
		if (supplyFromSpotFlowItemProvider == null) {
			supplyFromSpotFlowItemProvider = new SupplyFromSpotFlowItemProvider(this);
		}

		return supplyFromSpotFlowItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.adp.DeliverToSpotFlow} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected DeliverToSpotFlowItemProvider deliverToSpotFlowItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.adp.DeliverToSpotFlow}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createDeliverToSpotFlowAdapter() {
		if (deliverToSpotFlowItemProvider == null) {
			deliverToSpotFlowItemProvider = new DeliverToSpotFlowItemProvider(this);
		}

		return deliverToSpotFlowItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.adp.ProfileVesselRestriction} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ProfileVesselRestrictionItemProvider profileVesselRestrictionItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.adp.ProfileVesselRestriction}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createProfileVesselRestrictionAdapter() {
		if (profileVesselRestrictionItemProvider == null) {
			profileVesselRestrictionItemProvider = new ProfileVesselRestrictionItemProvider(this);
		}

		return profileVesselRestrictionItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.adp.ShippingOption} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ShippingOptionItemProvider shippingOptionItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.adp.ShippingOption}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createShippingOptionAdapter() {
		if (shippingOptionItemProvider == null) {
			shippingOptionItemProvider = new ShippingOptionItemProvider(this);
		}

		return shippingOptionItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.adp.MinCargoConstraint} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected MinCargoConstraintItemProvider minCargoConstraintItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.adp.MinCargoConstraint}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createMinCargoConstraintAdapter() {
		if (minCargoConstraintItemProvider == null) {
			minCargoConstraintItemProvider = new MinCargoConstraintItemProvider(this);
		}

		return minCargoConstraintItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.adp.MaxCargoConstraint} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected MaxCargoConstraintItemProvider maxCargoConstraintItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.adp.MaxCargoConstraint}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createMaxCargoConstraintAdapter() {
		if (maxCargoConstraintItemProvider == null) {
			maxCargoConstraintItemProvider = new MaxCargoConstraintItemProvider(this);
		}

		return maxCargoConstraintItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.adp.ADPModelResult} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ADPModelResultItemProvider adpModelResultItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.adp.ADPModelResult}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createADPModelResultAdapter() {
		if (adpModelResultItemProvider == null) {
			adpModelResultItemProvider = new ADPModelResultItemProvider(this);
		}

		return adpModelResultItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.adp.TargetCargoesOnVesselConstraint} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected TargetCargoesOnVesselConstraintItemProvider targetCargoesOnVesselConstraintItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.adp.TargetCargoesOnVesselConstraint}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createTargetCargoesOnVesselConstraintAdapter() {
		if (targetCargoesOnVesselConstraintItemProvider == null) {
			targetCargoesOnVesselConstraintItemProvider = new TargetCargoesOnVesselConstraintItemProvider(this);
		}

		return targetCargoesOnVesselConstraintItemProvider;
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
		if (adpModelItemProvider != null) adpModelItemProvider.dispose();
		if (fleetProfileItemProvider != null) fleetProfileItemProvider.dispose();
		if (contractProfileItemProvider != null) contractProfileItemProvider.dispose();
		if (spotMarketsProfileItemProvider != null) spotMarketsProfileItemProvider.dispose();
		if (purchaseContractProfileItemProvider != null) purchaseContractProfileItemProvider.dispose();
		if (salesContractProfileItemProvider != null) salesContractProfileItemProvider.dispose();
		if (subContractProfileItemProvider != null) subContractProfileItemProvider.dispose();
		if (cargoSizeDistributionModelItemProvider != null) cargoSizeDistributionModelItemProvider.dispose();
		if (cargoNumberDistributionModelItemProvider != null) cargoNumberDistributionModelItemProvider.dispose();
		if (cargoByQuarterDistributionModelItemProvider != null) cargoByQuarterDistributionModelItemProvider.dispose();
		if (cargoIntervalDistributionModelItemProvider != null) cargoIntervalDistributionModelItemProvider.dispose();
		if (preDefinedDistributionModelItemProvider != null) preDefinedDistributionModelItemProvider.dispose();
		if (preDefinedDateItemProvider != null) preDefinedDateItemProvider.dispose();
		if (flowTypeItemProvider != null) flowTypeItemProvider.dispose();
		if (supplyFromFlowItemProvider != null) supplyFromFlowItemProvider.dispose();
		if (deliverToFlowItemProvider != null) deliverToFlowItemProvider.dispose();
		if (supplyFromProfileFlowItemProvider != null) supplyFromProfileFlowItemProvider.dispose();
		if (deliverToProfileFlowItemProvider != null) deliverToProfileFlowItemProvider.dispose();
		if (supplyFromSpotFlowItemProvider != null) supplyFromSpotFlowItemProvider.dispose();
		if (deliverToSpotFlowItemProvider != null) deliverToSpotFlowItemProvider.dispose();
		if (profileVesselRestrictionItemProvider != null) profileVesselRestrictionItemProvider.dispose();
		if (shippingOptionItemProvider != null) shippingOptionItemProvider.dispose();
		if (minCargoConstraintItemProvider != null) minCargoConstraintItemProvider.dispose();
		if (maxCargoConstraintItemProvider != null) maxCargoConstraintItemProvider.dispose();
		if (adpModelResultItemProvider != null) adpModelResultItemProvider.dispose();
		if (targetCargoesOnVesselConstraintItemProvider != null) targetCargoesOnVesselConstraintItemProvider.dispose();
	}

}
