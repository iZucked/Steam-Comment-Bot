/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.models.lng.commercial.provider;

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
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.util.CommercialAdapterFactory;

/**
 * This is the factory that is used to provide the interfaces needed to support Viewers.
 * The adapters generated by this factory convert EMF adapter notifications into calls to {@link #fireNotifyChanged fireNotifyChanged}.
 * The adapters also support Eclipse property sheets.
 * Note that most of the adapters are shared among multiple instances.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class CommercialItemProviderAdapterFactory extends CommercialAdapterFactory implements ComposeableAdapterFactory, IChangeNotifier, IDisposable, IChildCreationExtender {
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
	protected ChildCreationExtenderManager childCreationExtenderManager = new ChildCreationExtenderManager(CommercialEditPlugin.INSTANCE, CommercialPackage.eNS_URI);

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
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.commercial.SalesContract} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected SalesContractItemProvider salesContractItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.commercial.SalesContract}.
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
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.commercial.PurchaseContract} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected PurchaseContractItemProvider purchaseContractItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.commercial.PurchaseContract}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createPurchaseContractAdapter() {
		if (purchaseContractItemProvider == null) {
			purchaseContractItemProvider = new PurchaseContractItemProvider(this);
		}

		return purchaseContractItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.commercial.TaxRate} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected TaxRateItemProvider taxRateItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.commercial.TaxRate}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createTaxRateAdapter() {
		if (taxRateItemProvider == null) {
			taxRateItemProvider = new TaxRateItemProvider(this);
		}

		return taxRateItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.commercial.ExpressionPriceParameters} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ExpressionPriceParametersItemProvider expressionPriceParametersItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.commercial.ExpressionPriceParameters}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createExpressionPriceParametersAdapter() {
		if (expressionPriceParametersItemProvider == null) {
			expressionPriceParametersItemProvider = new ExpressionPriceParametersItemProvider(this);
		}

		return expressionPriceParametersItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.commercial.ContractExpressionMapEntry} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ContractExpressionMapEntryItemProvider contractExpressionMapEntryItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.commercial.ContractExpressionMapEntry}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createContractExpressionMapEntryAdapter() {
		if (contractExpressionMapEntryItemProvider == null) {
			contractExpressionMapEntryItemProvider = new ContractExpressionMapEntryItemProvider(this);
		}

		return contractExpressionMapEntryItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.commercial.SimpleEntityBook} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected SimpleEntityBookItemProvider simpleEntityBookItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.commercial.SimpleEntityBook}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createSimpleEntityBookAdapter() {
		if (simpleEntityBookItemProvider == null) {
			simpleEntityBookItemProvider = new SimpleEntityBookItemProvider(this);
		}

		return simpleEntityBookItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.commercial.DateShiftExpressionPriceParameters} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected DateShiftExpressionPriceParametersItemProvider dateShiftExpressionPriceParametersItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.commercial.DateShiftExpressionPriceParameters}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createDateShiftExpressionPriceParametersAdapter() {
		if (dateShiftExpressionPriceParametersItemProvider == null) {
			dateShiftExpressionPriceParametersItemProvider = new DateShiftExpressionPriceParametersItemProvider(this);
		}

		return dateShiftExpressionPriceParametersItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.commercial.GenericCharterContract} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected GenericCharterContractItemProvider genericCharterContractItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.commercial.GenericCharterContract}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createGenericCharterContractAdapter() {
		if (genericCharterContractItemProvider == null) {
			genericCharterContractItemProvider = new GenericCharterContractItemProvider(this);
		}

		return genericCharterContractItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.commercial.SimpleRepositioningFeeContainer} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected SimpleRepositioningFeeContainerItemProvider simpleRepositioningFeeContainerItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.commercial.SimpleRepositioningFeeContainer}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createSimpleRepositioningFeeContainerAdapter() {
		if (simpleRepositioningFeeContainerItemProvider == null) {
			simpleRepositioningFeeContainerItemProvider = new SimpleRepositioningFeeContainerItemProvider(this);
		}

		return simpleRepositioningFeeContainerItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.commercial.SimpleBallastBonusContainer} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected SimpleBallastBonusContainerItemProvider simpleBallastBonusContainerItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.commercial.SimpleBallastBonusContainer}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createSimpleBallastBonusContainerAdapter() {
		if (simpleBallastBonusContainerItemProvider == null) {
			simpleBallastBonusContainerItemProvider = new SimpleBallastBonusContainerItemProvider(this);
		}

		return simpleBallastBonusContainerItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.commercial.MonthlyBallastBonusContainer} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected MonthlyBallastBonusContainerItemProvider monthlyBallastBonusContainerItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.commercial.MonthlyBallastBonusContainer}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createMonthlyBallastBonusContainerAdapter() {
		if (monthlyBallastBonusContainerItemProvider == null) {
			monthlyBallastBonusContainerItemProvider = new MonthlyBallastBonusContainerItemProvider(this);
		}

		return monthlyBallastBonusContainerItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.commercial.BallastBonusTerm} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected BallastBonusTermItemProvider ballastBonusTermItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.commercial.BallastBonusTerm}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createBallastBonusTermAdapter() {
		if (ballastBonusTermItemProvider == null) {
			ballastBonusTermItemProvider = new BallastBonusTermItemProvider(this);
		}

		return ballastBonusTermItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.commercial.LumpSumBallastBonusTerm} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected LumpSumBallastBonusTermItemProvider lumpSumBallastBonusTermItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.commercial.LumpSumBallastBonusTerm}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createLumpSumBallastBonusTermAdapter() {
		if (lumpSumBallastBonusTermItemProvider == null) {
			lumpSumBallastBonusTermItemProvider = new LumpSumBallastBonusTermItemProvider(this);
		}

		return lumpSumBallastBonusTermItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.commercial.NotionalJourneyBallastBonusTerm} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected NotionalJourneyBallastBonusTermItemProvider notionalJourneyBallastBonusTermItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.commercial.NotionalJourneyBallastBonusTerm}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createNotionalJourneyBallastBonusTermAdapter() {
		if (notionalJourneyBallastBonusTermItemProvider == null) {
			notionalJourneyBallastBonusTermItemProvider = new NotionalJourneyBallastBonusTermItemProvider(this);
		}

		return notionalJourneyBallastBonusTermItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.commercial.MonthlyBallastBonusTerm} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected MonthlyBallastBonusTermItemProvider monthlyBallastBonusTermItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.commercial.MonthlyBallastBonusTerm}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createMonthlyBallastBonusTermAdapter() {
		if (monthlyBallastBonusTermItemProvider == null) {
			monthlyBallastBonusTermItemProvider = new MonthlyBallastBonusTermItemProvider(this);
		}

		return monthlyBallastBonusTermItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.commercial.RepositioningFeeTerm} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected RepositioningFeeTermItemProvider repositioningFeeTermItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.commercial.RepositioningFeeTerm}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createRepositioningFeeTermAdapter() {
		if (repositioningFeeTermItemProvider == null) {
			repositioningFeeTermItemProvider = new RepositioningFeeTermItemProvider(this);
		}

		return repositioningFeeTermItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.commercial.LumpSumRepositioningFeeTerm} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected LumpSumRepositioningFeeTermItemProvider lumpSumRepositioningFeeTermItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.commercial.LumpSumRepositioningFeeTerm}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createLumpSumRepositioningFeeTermAdapter() {
		if (lumpSumRepositioningFeeTermItemProvider == null) {
			lumpSumRepositioningFeeTermItemProvider = new LumpSumRepositioningFeeTermItemProvider(this);
		}

		return lumpSumRepositioningFeeTermItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.commercial.OriginPortRepositioningFeeTerm} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected OriginPortRepositioningFeeTermItemProvider originPortRepositioningFeeTermItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.commercial.OriginPortRepositioningFeeTerm}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createOriginPortRepositioningFeeTermAdapter() {
		if (originPortRepositioningFeeTermItemProvider == null) {
			originPortRepositioningFeeTermItemProvider = new OriginPortRepositioningFeeTermItemProvider(this);
		}

		return originPortRepositioningFeeTermItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.commercial.EndHeelOptions} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EndHeelOptionsItemProvider endHeelOptionsItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.commercial.EndHeelOptions}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createEndHeelOptionsAdapter() {
		if (endHeelOptionsItemProvider == null) {
			endHeelOptionsItemProvider = new EndHeelOptionsItemProvider(this);
		}

		return endHeelOptionsItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.commercial.StartHeelOptions} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected StartHeelOptionsItemProvider startHeelOptionsItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.commercial.StartHeelOptions}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createStartHeelOptionsAdapter() {
		if (startHeelOptionsItemProvider == null) {
			startHeelOptionsItemProvider = new StartHeelOptionsItemProvider(this);
		}

		return startHeelOptionsItemProvider;
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
		if (commercialModelItemProvider != null) commercialModelItemProvider.dispose();
		if (legalEntityItemProvider != null) legalEntityItemProvider.dispose();
		if (contractItemProvider != null) contractItemProvider.dispose();
		if (salesContractItemProvider != null) salesContractItemProvider.dispose();
		if (purchaseContractItemProvider != null) purchaseContractItemProvider.dispose();
		if (taxRateItemProvider != null) taxRateItemProvider.dispose();
		if (expressionPriceParametersItemProvider != null) expressionPriceParametersItemProvider.dispose();
		if (contractExpressionMapEntryItemProvider != null) contractExpressionMapEntryItemProvider.dispose();
		if (simpleEntityBookItemProvider != null) simpleEntityBookItemProvider.dispose();
		if (dateShiftExpressionPriceParametersItemProvider != null) dateShiftExpressionPriceParametersItemProvider.dispose();
		if (genericCharterContractItemProvider != null) genericCharterContractItemProvider.dispose();
		if (simpleRepositioningFeeContainerItemProvider != null) simpleRepositioningFeeContainerItemProvider.dispose();
		if (simpleBallastBonusContainerItemProvider != null) simpleBallastBonusContainerItemProvider.dispose();
		if (monthlyBallastBonusContainerItemProvider != null) monthlyBallastBonusContainerItemProvider.dispose();
		if (ballastBonusTermItemProvider != null) ballastBonusTermItemProvider.dispose();
		if (lumpSumBallastBonusTermItemProvider != null) lumpSumBallastBonusTermItemProvider.dispose();
		if (notionalJourneyBallastBonusTermItemProvider != null) notionalJourneyBallastBonusTermItemProvider.dispose();
		if (monthlyBallastBonusTermItemProvider != null) monthlyBallastBonusTermItemProvider.dispose();
		if (repositioningFeeTermItemProvider != null) repositioningFeeTermItemProvider.dispose();
		if (lumpSumRepositioningFeeTermItemProvider != null) lumpSumRepositioningFeeTermItemProvider.dispose();
		if (originPortRepositioningFeeTermItemProvider != null) originPortRepositioningFeeTermItemProvider.dispose();
		if (endHeelOptionsItemProvider != null) endHeelOptionsItemProvider.dispose();
		if (startHeelOptionsItemProvider != null) startHeelOptionsItemProvider.dispose();
	}

}
