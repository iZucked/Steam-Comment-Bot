/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.models.lng.commercial.provider;

import com.mmxlabs.models.lng.commercial.util.CommercialAdapterFactory;

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
public class CommercialItemProviderAdapterFactory extends CommercialAdapterFactory implements ComposeableAdapterFactory, IChangeNotifier, IDisposable {
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
	public CommercialItemProviderAdapterFactory() {
		supportedTypes.add(IEditingDomainItemProvider.class);
		supportedTypes.add(IStructuredItemContentProvider.class);
		supportedTypes.add(ITreeItemContentProvider.class);
		supportedTypes.add(IItemLabelProvider.class);
		supportedTypes.add(IItemPropertySource.class);
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.commercial.CommercialModel} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected CommercialModelItemProvider commercialModelItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.commercial.CommercialModel}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createCommercialModelAdapter() {
		if (commercialModelItemProvider == null) {
			commercialModelItemProvider = new CommercialModelItemProvider(this);
		}

		return commercialModelItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.commercial.LegalEntity} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected LegalEntityItemProvider legalEntityItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.commercial.LegalEntity}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createLegalEntityAdapter() {
		if (legalEntityItemProvider == null) {
			legalEntityItemProvider = new LegalEntityItemProvider(this);
		}

		return legalEntityItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.commercial.Contract} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ContractItemProvider contractItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.commercial.Contract}.
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
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.commercial.FixedPriceContract} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected FixedPriceContractItemProvider fixedPriceContractItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.commercial.FixedPriceContract}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createFixedPriceContractAdapter() {
		if (fixedPriceContractItemProvider == null) {
			fixedPriceContractItemProvider = new FixedPriceContractItemProvider(this);
		}

		return fixedPriceContractItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.commercial.IndexPriceContract} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected IndexPriceContractItemProvider indexPriceContractItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.commercial.IndexPriceContract}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createIndexPriceContractAdapter() {
		if (indexPriceContractItemProvider == null) {
			indexPriceContractItemProvider = new IndexPriceContractItemProvider(this);
		}

		return indexPriceContractItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.commercial.NetbackPurchaseContract} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected NetbackPurchaseContractItemProvider netbackPurchaseContractItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.commercial.NetbackPurchaseContract}.
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
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.commercial.ProfitSharePurchaseContract} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ProfitSharePurchaseContractItemProvider profitSharePurchaseContractItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.commercial.ProfitSharePurchaseContract}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createProfitSharePurchaseContractAdapter() {
		if (profitSharePurchaseContractItemProvider == null) {
			profitSharePurchaseContractItemProvider = new ProfitSharePurchaseContractItemProvider(this);
		}

		return profitSharePurchaseContractItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.commercial.NotionalBallastParameters} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected NotionalBallastParametersItemProvider notionalBallastParametersItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.commercial.NotionalBallastParameters}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createNotionalBallastParametersAdapter() {
		if (notionalBallastParametersItemProvider == null) {
			notionalBallastParametersItemProvider = new NotionalBallastParametersItemProvider(this);
		}

		return notionalBallastParametersItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.commercial.RedirectionPurchaseContract} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected RedirectionPurchaseContractItemProvider redirectionPurchaseContractItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.commercial.RedirectionPurchaseContract}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createRedirectionPurchaseContractAdapter() {
		if (redirectionPurchaseContractItemProvider == null) {
			redirectionPurchaseContractItemProvider = new RedirectionPurchaseContractItemProvider(this);
		}

		return redirectionPurchaseContractItemProvider;
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
		if (commercialModelItemProvider != null) commercialModelItemProvider.dispose();
		if (legalEntityItemProvider != null) legalEntityItemProvider.dispose();
		if (contractItemProvider != null) contractItemProvider.dispose();
		if (fixedPriceContractItemProvider != null) fixedPriceContractItemProvider.dispose();
		if (indexPriceContractItemProvider != null) indexPriceContractItemProvider.dispose();
		if (netbackPurchaseContractItemProvider != null) netbackPurchaseContractItemProvider.dispose();
		if (profitSharePurchaseContractItemProvider != null) profitSharePurchaseContractItemProvider.dispose();
		if (notionalBallastParametersItemProvider != null) notionalBallastParametersItemProvider.dispose();
		if (redirectionPurchaseContractItemProvider != null) redirectionPurchaseContractItemProvider.dispose();
	}

}
