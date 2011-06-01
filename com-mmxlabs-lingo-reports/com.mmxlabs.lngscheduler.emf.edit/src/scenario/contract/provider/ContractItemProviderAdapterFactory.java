/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package scenario.contract.provider;

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

import scenario.contract.util.ContractAdapterFactory;

/**
 * This is the factory that is used to provide the interfaces needed to support Viewers.
 * The adapters generated by this factory convert EMF adapter notifications into calls to {@link #fireNotifyChanged fireNotifyChanged}.
 * The adapters also support Eclipse property sheets.
 * Note that most of the adapters are shared among multiple instances.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class ContractItemProviderAdapterFactory extends ContractAdapterFactory implements ComposeableAdapterFactory, IChangeNotifier, IDisposable {
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
	public ContractItemProviderAdapterFactory() {
		supportedTypes.add(IEditingDomainItemProvider.class);
		supportedTypes.add(IStructuredItemContentProvider.class);
		supportedTypes.add(ITreeItemContentProvider.class);
		supportedTypes.add(IItemLabelProvider.class);
		supportedTypes.add(IItemPropertySource.class);
	}

	/**
	 * This keeps track of the one adapter used for all {@link scenario.contract.ContractModel} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ContractModelItemProvider contractModelItemProvider;

	/**
	 * This creates an adapter for a {@link scenario.contract.ContractModel}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createContractModelAdapter() {
		if (contractModelItemProvider == null) {
			contractModelItemProvider = new ContractModelItemProvider(this);
		}

		return contractModelItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link scenario.contract.SalesContract} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected SalesContractItemProvider salesContractItemProvider;

	/**
	 * This creates an adapter for a {@link scenario.contract.SalesContract}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createSalesContractAdapter() {
		if (salesContractItemProvider == null) {
			salesContractItemProvider = new SalesContractItemProvider(this);
		}

		return salesContractItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link scenario.contract.TotalVolumeLimit} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected TotalVolumeLimitItemProvider totalVolumeLimitItemProvider;

	/**
	 * This creates an adapter for a {@link scenario.contract.TotalVolumeLimit}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createTotalVolumeLimitAdapter() {
		if (totalVolumeLimitItemProvider == null) {
			totalVolumeLimitItemProvider = new TotalVolumeLimitItemProvider(this);
		}

		return totalVolumeLimitItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link scenario.contract.Entity} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EntityItemProvider entityItemProvider;

	/**
	 * This creates an adapter for a {@link scenario.contract.Entity}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createEntityAdapter() {
		if (entityItemProvider == null) {
			entityItemProvider = new EntityItemProvider(this);
		}

		return entityItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link scenario.contract.FixedPricePurchaseContract} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected FixedPricePurchaseContractItemProvider fixedPricePurchaseContractItemProvider;

	/**
	 * This creates an adapter for a {@link scenario.contract.FixedPricePurchaseContract}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createFixedPricePurchaseContractAdapter() {
		if (fixedPricePurchaseContractItemProvider == null) {
			fixedPricePurchaseContractItemProvider = new FixedPricePurchaseContractItemProvider(this);
		}

		return fixedPricePurchaseContractItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link scenario.contract.IndexPricePurchaseContract} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected IndexPricePurchaseContractItemProvider indexPricePurchaseContractItemProvider;

	/**
	 * This creates an adapter for a {@link scenario.contract.IndexPricePurchaseContract}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createIndexPricePurchaseContractAdapter() {
		if (indexPricePurchaseContractItemProvider == null) {
			indexPricePurchaseContractItemProvider = new IndexPricePurchaseContractItemProvider(this);
		}

		return indexPricePurchaseContractItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link scenario.contract.NetbackPurchaseContract} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected NetbackPurchaseContractItemProvider netbackPurchaseContractItemProvider;

	/**
	 * This creates an adapter for a {@link scenario.contract.NetbackPurchaseContract}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createNetbackPurchaseContractAdapter() {
		if (netbackPurchaseContractItemProvider == null) {
			netbackPurchaseContractItemProvider = new NetbackPurchaseContractItemProvider(this);
		}

		return netbackPurchaseContractItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link scenario.contract.ProfitSharingPurchaseContract} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ProfitSharingPurchaseContractItemProvider profitSharingPurchaseContractItemProvider;

	/**
	 * This creates an adapter for a {@link scenario.contract.ProfitSharingPurchaseContract}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createProfitSharingPurchaseContractAdapter() {
		if (profitSharingPurchaseContractItemProvider == null) {
			profitSharingPurchaseContractItemProvider = new ProfitSharingPurchaseContractItemProvider(this);
		}

		return profitSharingPurchaseContractItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link scenario.contract.Contract} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ContractItemProvider contractItemProvider;

	/**
	 * This creates an adapter for a {@link scenario.contract.Contract}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createContractAdapter() {
		if (contractItemProvider == null) {
			contractItemProvider = new ContractItemProvider(this);
		}

		return contractItemProvider;
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
		if (contractModelItemProvider != null) contractModelItemProvider.dispose();
		if (salesContractItemProvider != null) salesContractItemProvider.dispose();
		if (totalVolumeLimitItemProvider != null) totalVolumeLimitItemProvider.dispose();
		if (entityItemProvider != null) entityItemProvider.dispose();
		if (fixedPricePurchaseContractItemProvider != null) fixedPricePurchaseContractItemProvider.dispose();
		if (indexPricePurchaseContractItemProvider != null) indexPricePurchaseContractItemProvider.dispose();
		if (netbackPurchaseContractItemProvider != null) netbackPurchaseContractItemProvider.dispose();
		if (profitSharingPurchaseContractItemProvider != null) profitSharingPurchaseContractItemProvider.dispose();
		if (contractItemProvider != null) contractItemProvider.dispose();
	}

}
