/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
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
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.commercial.RuleBasedBallastBonusContract} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected RuleBasedBallastBonusContractItemProvider ruleBasedBallastBonusContractItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.commercial.RuleBasedBallastBonusContract}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createRuleBasedBallastBonusContractAdapter() {
		if (ruleBasedBallastBonusContractItemProvider == null) {
			ruleBasedBallastBonusContractItemProvider = new RuleBasedBallastBonusContractItemProvider(this);
		}

		return ruleBasedBallastBonusContractItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.commercial.LumpSumBallastBonusContractLine} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected LumpSumBallastBonusContractLineItemProvider lumpSumBallastBonusContractLineItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.commercial.LumpSumBallastBonusContractLine}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createLumpSumBallastBonusContractLineAdapter() {
		if (lumpSumBallastBonusContractLineItemProvider == null) {
			lumpSumBallastBonusContractLineItemProvider = new LumpSumBallastBonusContractLineItemProvider(this);
		}

		return lumpSumBallastBonusContractLineItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.commercial.NotionalJourneyBallastBonusContractLine} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected NotionalJourneyBallastBonusContractLineItemProvider notionalJourneyBallastBonusContractLineItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.commercial.NotionalJourneyBallastBonusContractLine}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createNotionalJourneyBallastBonusContractLineAdapter() {
		if (notionalJourneyBallastBonusContractLineItemProvider == null) {
			notionalJourneyBallastBonusContractLineItemProvider = new NotionalJourneyBallastBonusContractLineItemProvider(this);
		}

		return notionalJourneyBallastBonusContractLineItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.commercial.SimpleCharterContract} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected SimpleCharterContractItemProvider simpleCharterContractItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.commercial.SimpleCharterContract}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createSimpleCharterContractAdapter() {
		if (simpleCharterContractItemProvider == null) {
			simpleCharterContractItemProvider = new SimpleCharterContractItemProvider(this);
		}

		return simpleCharterContractItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.commercial.SimpleBallastBonusCharterContract} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected SimpleBallastBonusCharterContractItemProvider simpleBallastBonusCharterContractItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.commercial.SimpleBallastBonusCharterContract}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createSimpleBallastBonusCharterContractAdapter() {
		if (simpleBallastBonusCharterContractItemProvider == null) {
			simpleBallastBonusCharterContractItemProvider = new SimpleBallastBonusCharterContractItemProvider(this);
		}

		return simpleBallastBonusCharterContractItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.commercial.MonthlyBallastBonusContractLine} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected MonthlyBallastBonusContractLineItemProvider monthlyBallastBonusContractLineItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.commercial.MonthlyBallastBonusContractLine}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createMonthlyBallastBonusContractLineAdapter() {
		if (monthlyBallastBonusContractLineItemProvider == null) {
			monthlyBallastBonusContractLineItemProvider = new MonthlyBallastBonusContractLineItemProvider(this);
		}

		return monthlyBallastBonusContractLineItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.commercial.MonthlyBallastBonusContract} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected MonthlyBallastBonusContractItemProvider monthlyBallastBonusContractItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.commercial.MonthlyBallastBonusContract}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createMonthlyBallastBonusContractAdapter() {
		if (monthlyBallastBonusContractItemProvider == null) {
			monthlyBallastBonusContractItemProvider = new MonthlyBallastBonusContractItemProvider(this);
		}

		return monthlyBallastBonusContractItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.commercial.MonthlyBallastBonusCharterContract} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected MonthlyBallastBonusCharterContractItemProvider monthlyBallastBonusCharterContractItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.commercial.MonthlyBallastBonusCharterContract}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createMonthlyBallastBonusCharterContractAdapter() {
		if (monthlyBallastBonusCharterContractItemProvider == null) {
			monthlyBallastBonusCharterContractItemProvider = new MonthlyBallastBonusCharterContractItemProvider(this);
		}

		return monthlyBallastBonusCharterContractItemProvider;
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
		if (ruleBasedBallastBonusContractItemProvider != null) ruleBasedBallastBonusContractItemProvider.dispose();
		if (lumpSumBallastBonusContractLineItemProvider != null) lumpSumBallastBonusContractLineItemProvider.dispose();
		if (notionalJourneyBallastBonusContractLineItemProvider != null) notionalJourneyBallastBonusContractLineItemProvider.dispose();
		if (simpleCharterContractItemProvider != null) simpleCharterContractItemProvider.dispose();
		if (simpleBallastBonusCharterContractItemProvider != null) simpleBallastBonusCharterContractItemProvider.dispose();
		if (monthlyBallastBonusContractLineItemProvider != null) monthlyBallastBonusContractLineItemProvider.dispose();
		if (monthlyBallastBonusContractItemProvider != null) monthlyBallastBonusContractItemProvider.dispose();
		if (monthlyBallastBonusCharterContractItemProvider != null) monthlyBallastBonusCharterContractItemProvider.dispose();
	}

}
