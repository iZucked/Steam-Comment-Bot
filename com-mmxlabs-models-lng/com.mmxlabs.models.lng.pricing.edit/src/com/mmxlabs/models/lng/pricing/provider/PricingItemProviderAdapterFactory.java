/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
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

import org.eclipse.jdt.annotation.Nullable;
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
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.pricing.CostModel} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected CostModelItemProvider costModelItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.pricing.CostModel}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createCostModelAdapter() {
		if (costModelItemProvider == null) {
			costModelItemProvider = new CostModelItemProvider(this);
		}

		return costModelItemProvider;
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
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.pricing.CooldownPriceEntry} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected CooldownPriceEntryItemProvider cooldownPriceEntryItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.pricing.CooldownPriceEntry}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createCooldownPriceEntryAdapter() {
		if (cooldownPriceEntryItemProvider == null) {
			cooldownPriceEntryItemProvider = new CooldownPriceEntryItemProvider(this);
		}

		return cooldownPriceEntryItemProvider;
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
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.pricing.PortsSplitExpressionMap} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected PortsSplitExpressionMapItemProvider portsSplitExpressionMapItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.pricing.PortsSplitExpressionMap}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createPortsSplitExpressionMapAdapter() {
		if (portsSplitExpressionMapItemProvider == null) {
			portsSplitExpressionMapItemProvider = new PortsSplitExpressionMapItemProvider(this);
		}

		return portsSplitExpressionMapItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.pricing.PanamaCanalTariff} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected PanamaCanalTariffItemProvider panamaCanalTariffItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.pricing.PanamaCanalTariff}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createPanamaCanalTariffAdapter() {
		if (panamaCanalTariffItemProvider == null) {
			panamaCanalTariffItemProvider = new PanamaCanalTariffItemProvider(this);
		}

		return panamaCanalTariffItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.pricing.PanamaCanalTariffBand} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected PanamaCanalTariffBandItemProvider panamaCanalTariffBandItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.pricing.PanamaCanalTariffBand}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createPanamaCanalTariffBandAdapter() {
		if (panamaCanalTariffBandItemProvider == null) {
			panamaCanalTariffBandItemProvider = new PanamaCanalTariffBandItemProvider(this);
		}

		return panamaCanalTariffBandItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.pricing.PanamaTariffV2} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected PanamaTariffV2ItemProvider panamaTariffV2ItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.pricing.PanamaTariffV2}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createPanamaTariffV2Adapter() {
		if (panamaTariffV2ItemProvider == null) {
			panamaTariffV2ItemProvider = new PanamaTariffV2ItemProvider(this);
		}

		return panamaTariffV2ItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.pricing.SuezCanalTugBand} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected SuezCanalTugBandItemProvider suezCanalTugBandItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.pricing.SuezCanalTugBand}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createSuezCanalTugBandAdapter() {
		if (suezCanalTugBandItemProvider == null) {
			suezCanalTugBandItemProvider = new SuezCanalTugBandItemProvider(this);
		}

		return suezCanalTugBandItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.pricing.SuezCanalTariff} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected SuezCanalTariffItemProvider suezCanalTariffItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.pricing.SuezCanalTariff}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createSuezCanalTariffAdapter() {
		if (suezCanalTariffItemProvider == null) {
			suezCanalTariffItemProvider = new SuezCanalTariffItemProvider(this);
		}

		return suezCanalTariffItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.pricing.SuezCanalTariffBand} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected SuezCanalTariffBandItemProvider suezCanalTariffBandItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.pricing.SuezCanalTariffBand}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createSuezCanalTariffBandAdapter() {
		if (suezCanalTariffBandItemProvider == null) {
			suezCanalTariffBandItemProvider = new SuezCanalTariffBandItemProvider(this);
		}

		return suezCanalTariffBandItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.pricing.SuezCanalRouteRebate} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected SuezCanalRouteRebateItemProvider suezCanalRouteRebateItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.pricing.SuezCanalRouteRebate}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createSuezCanalRouteRebateAdapter() {
		if (suezCanalRouteRebateItemProvider == null) {
			suezCanalRouteRebateItemProvider = new SuezCanalRouteRebateItemProvider(this);
		}

		return suezCanalRouteRebateItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.pricing.UnitConversion} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected UnitConversionItemProvider unitConversionItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.pricing.UnitConversion}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createUnitConversionAdapter() {
		if (unitConversionItemProvider == null) {
			unitConversionItemProvider = new UnitConversionItemProvider(this);
		}

		return unitConversionItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.pricing.DatePointContainer} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected DatePointContainerItemProvider datePointContainerItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.pricing.DatePointContainer}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createDatePointContainerAdapter() {
		if (datePointContainerItemProvider == null) {
			datePointContainerItemProvider = new DatePointContainerItemProvider(this);
		}

		return datePointContainerItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.pricing.DatePoint} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected DatePointItemProvider datePointItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.pricing.DatePoint}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createDatePointAdapter() {
		if (datePointItemProvider == null) {
			datePointItemProvider = new DatePointItemProvider(this);
		}

		return datePointItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.pricing.YearMonthPointContainer} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected YearMonthPointContainerItemProvider yearMonthPointContainerItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.pricing.YearMonthPointContainer}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createYearMonthPointContainerAdapter() {
		if (yearMonthPointContainerItemProvider == null) {
			yearMonthPointContainerItemProvider = new YearMonthPointContainerItemProvider(this);
		}

		return yearMonthPointContainerItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.pricing.YearMonthPoint} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected YearMonthPointItemProvider yearMonthPointItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.pricing.YearMonthPoint}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createYearMonthPointAdapter() {
		if (yearMonthPointItemProvider == null) {
			yearMonthPointItemProvider = new YearMonthPointItemProvider(this);
		}

		return yearMonthPointItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.pricing.CommodityCurve} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected CommodityCurveItemProvider commodityCurveItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.pricing.CommodityCurve}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createCommodityCurveAdapter() {
		if (commodityCurveItemProvider == null) {
			commodityCurveItemProvider = new CommodityCurveItemProvider(this);
		}

		return commodityCurveItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.pricing.CharterCurve} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected CharterCurveItemProvider charterCurveItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.pricing.CharterCurve}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createCharterCurveAdapter() {
		if (charterCurveItemProvider == null) {
			charterCurveItemProvider = new CharterCurveItemProvider(this);
		}

		return charterCurveItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.pricing.BunkerFuelCurve} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected BunkerFuelCurveItemProvider bunkerFuelCurveItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.pricing.BunkerFuelCurve}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createBunkerFuelCurveAdapter() {
		if (bunkerFuelCurveItemProvider == null) {
			bunkerFuelCurveItemProvider = new BunkerFuelCurveItemProvider(this);
		}

		return bunkerFuelCurveItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.pricing.CurrencyCurve} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected CurrencyCurveItemProvider currencyCurveItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.pricing.CurrencyCurve}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createCurrencyCurveAdapter() {
		if (currencyCurveItemProvider == null) {
			currencyCurveItemProvider = new CurrencyCurveItemProvider(this);
		}

		return currencyCurveItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.pricing.MarketIndex} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected MarketIndexItemProvider marketIndexItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.pricing.MarketIndex}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createMarketIndexAdapter() {
		if (marketIndexItemProvider == null) {
			marketIndexItemProvider = new MarketIndexItemProvider(this);
		}

		return marketIndexItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.pricing.PricingCalendarEntry} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected PricingCalendarEntryItemProvider pricingCalendarEntryItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.pricing.PricingCalendarEntry}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createPricingCalendarEntryAdapter() {
		if (pricingCalendarEntryItemProvider == null) {
			pricingCalendarEntryItemProvider = new PricingCalendarEntryItemProvider(this);
		}

		return pricingCalendarEntryItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.pricing.PricingCalendar} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected PricingCalendarItemProvider pricingCalendarItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.pricing.PricingCalendar}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createPricingCalendarAdapter() {
		if (pricingCalendarItemProvider == null) {
			pricingCalendarItemProvider = new PricingCalendarItemProvider(this);
		}

		return pricingCalendarItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.pricing.HolidayCalendarEntry} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected HolidayCalendarEntryItemProvider holidayCalendarEntryItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.pricing.HolidayCalendarEntry}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createHolidayCalendarEntryAdapter() {
		if (holidayCalendarEntryItemProvider == null) {
			holidayCalendarEntryItemProvider = new HolidayCalendarEntryItemProvider(this);
		}

		return holidayCalendarEntryItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.pricing.HolidayCalendar} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected HolidayCalendarItemProvider holidayCalendarItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.pricing.HolidayCalendar}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createHolidayCalendarAdapter() {
		if (holidayCalendarItemProvider == null) {
			holidayCalendarItemProvider = new HolidayCalendarItemProvider(this);
		}

		return holidayCalendarItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link com.mmxlabs.models.lng.pricing.SettleStrategy} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected SettleStrategyItemProvider settleStrategyItemProvider;

	/**
	 * This creates an adapter for a {@link com.mmxlabs.models.lng.pricing.SettleStrategy}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createSettleStrategyAdapter() {
		if (settleStrategyItemProvider == null) {
			settleStrategyItemProvider = new SettleStrategyItemProvider(this);
		}

		return settleStrategyItemProvider;
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
		if (pricingModelItemProvider != null) pricingModelItemProvider.dispose();
		if (dataIndexItemProvider != null) dataIndexItemProvider.dispose();
		if (derivedIndexItemProvider != null) derivedIndexItemProvider.dispose();
		if (indexPointItemProvider != null) indexPointItemProvider.dispose();
		if (costModelItemProvider != null) costModelItemProvider.dispose();
		if (routeCostItemProvider != null) routeCostItemProvider.dispose();
		if (baseFuelCostItemProvider != null) baseFuelCostItemProvider.dispose();
		if (portCostItemProvider != null) portCostItemProvider.dispose();
		if (portCostEntryItemProvider != null) portCostEntryItemProvider.dispose();
		if (cooldownPriceEntryItemProvider != null) cooldownPriceEntryItemProvider.dispose();
		if (cooldownPriceItemProvider != null) cooldownPriceItemProvider.dispose();
		if (portsExpressionMapItemProvider != null) portsExpressionMapItemProvider.dispose();
		if (portsSplitExpressionMapItemProvider != null) portsSplitExpressionMapItemProvider.dispose();
		if (panamaCanalTariffItemProvider != null) panamaCanalTariffItemProvider.dispose();
		if (panamaCanalTariffBandItemProvider != null) panamaCanalTariffBandItemProvider.dispose();
		if (panamaTariffV2ItemProvider != null) panamaTariffV2ItemProvider.dispose();
		if (suezCanalTugBandItemProvider != null) suezCanalTugBandItemProvider.dispose();
		if (suezCanalTariffItemProvider != null) suezCanalTariffItemProvider.dispose();
		if (suezCanalTariffBandItemProvider != null) suezCanalTariffBandItemProvider.dispose();
		if (suezCanalRouteRebateItemProvider != null) suezCanalRouteRebateItemProvider.dispose();
		if (unitConversionItemProvider != null) unitConversionItemProvider.dispose();
		if (datePointContainerItemProvider != null) datePointContainerItemProvider.dispose();
		if (datePointItemProvider != null) datePointItemProvider.dispose();
		if (yearMonthPointContainerItemProvider != null) yearMonthPointContainerItemProvider.dispose();
		if (yearMonthPointItemProvider != null) yearMonthPointItemProvider.dispose();
		if (commodityCurveItemProvider != null) commodityCurveItemProvider.dispose();
		if (charterCurveItemProvider != null) charterCurveItemProvider.dispose();
		if (bunkerFuelCurveItemProvider != null) bunkerFuelCurveItemProvider.dispose();
		if (currencyCurveItemProvider != null) currencyCurveItemProvider.dispose();
		if (marketIndexItemProvider != null) marketIndexItemProvider.dispose();
		if (pricingCalendarEntryItemProvider != null) pricingCalendarEntryItemProvider.dispose();
		if (pricingCalendarItemProvider != null) pricingCalendarItemProvider.dispose();
		if (holidayCalendarEntryItemProvider != null) holidayCalendarEntryItemProvider.dispose();
		if (holidayCalendarItemProvider != null) holidayCalendarItemProvider.dispose();
		if (settleStrategyItemProvider != null) settleStrategyItemProvider.dispose();
	}

}
