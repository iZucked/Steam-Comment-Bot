/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.provider;


import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ViewerNotification;

import com.mmxlabs.models.lng.analytics.AnalyticsFactory;
import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.ProvisionalCargo;
import com.mmxlabs.models.lng.cargo.CargoFactory;
import com.mmxlabs.models.lng.commercial.CommercialFactory;
import com.mmxlabs.models.lng.fleet.FleetFactory;
import com.mmxlabs.models.lng.port.PortFactory;
import com.mmxlabs.models.lng.pricing.PricingFactory;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsFactory;
import com.mmxlabs.models.mmxcore.MMXCoreFactory;
import com.mmxlabs.models.mmxcore.provider.MMXObjectItemProvider;

/**
 * This is the item provider adapter for a {@link com.mmxlabs.models.lng.analytics.ProvisionalCargo} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class ProvisionalCargoItemProvider
	extends MMXObjectItemProvider {
	/**
	 * This constructs an instance from a factory and a notifier.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ProvisionalCargoItemProvider(AdapterFactory adapterFactory) {
		super(adapterFactory);
	}

	/**
	 * This returns the property descriptors for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public List<IItemPropertyDescriptor> getPropertyDescriptors(Object object) {
		if (itemPropertyDescriptors == null) {
			super.getPropertyDescriptors(object);

			addVesselPropertyDescriptor(object);
		}
		return itemPropertyDescriptors;
	}

	/**
	 * This adds a property descriptor for the Vessel feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addVesselPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_ProvisionalCargo_vessel_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_ProvisionalCargo_vessel_feature", "_UI_ProvisionalCargo_type"),
				 AnalyticsPackage.Literals.PROVISIONAL_CARGO__VESSEL,
				 true,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This specifies how to implement {@link #getChildren} and is used to deduce an appropriate feature for an
	 * {@link org.eclipse.emf.edit.command.AddCommand}, {@link org.eclipse.emf.edit.command.RemoveCommand} or
	 * {@link org.eclipse.emf.edit.command.MoveCommand} in {@link #createCommand}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Collection<? extends EStructuralFeature> getChildrenFeatures(Object object) {
		if (childrenFeatures == null) {
			super.getChildrenFeatures(object);
			childrenFeatures.add(AnalyticsPackage.Literals.PROVISIONAL_CARGO__BUY);
			childrenFeatures.add(AnalyticsPackage.Literals.PROVISIONAL_CARGO__SELL);
			childrenFeatures.add(AnalyticsPackage.Literals.PROVISIONAL_CARGO__PORTFOLIO_MODEL);
		}
		return childrenFeatures;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EStructuralFeature getChildFeature(Object object, Object child) {
		// Check the type of the specified child object and return the proper feature to use for
		// adding (see {@link AddCommand}) it as a child.

		return super.getChildFeature(object, child);
	}

	/**
	 * This returns ProvisionalCargo.gif.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object getImage(Object object) {
		return overlayImage(object, getResourceLocator().getImage("full/obj16/ProvisionalCargo"));
	}

	/**
	 * This returns the label text for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getText(Object object) {
		return getString("_UI_ProvisionalCargo_type");
	}

	/**
	 * This handles model notifications by calling {@link #updateChildren} to update any cached
	 * children and by creating a viewer notification, which it passes to {@link #fireNotifyChanged}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void notifyChanged(Notification notification) {
		updateChildren(notification);

		switch (notification.getFeatureID(ProvisionalCargo.class)) {
			case AnalyticsPackage.PROVISIONAL_CARGO__BUY:
			case AnalyticsPackage.PROVISIONAL_CARGO__SELL:
			case AnalyticsPackage.PROVISIONAL_CARGO__PORTFOLIO_MODEL:
				fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), true, false));
				return;
		}
		super.notifyChanged(notification);
	}

	/**
	 * This adds {@link org.eclipse.emf.edit.command.CommandParameter}s describing the children
	 * that can be created under this object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected void collectNewChildDescriptors(Collection<Object> newChildDescriptors, Object object) {
		super.collectNewChildDescriptors(newChildDescriptors, object);

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.PROVISIONAL_CARGO__BUY,
				 AnalyticsFactory.eINSTANCE.createBuyOpportunity()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.PROVISIONAL_CARGO__SELL,
				 AnalyticsFactory.eINSTANCE.createSellOpportunity()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.PROVISIONAL_CARGO__PORTFOLIO_MODEL,
				 AnalyticsFactory.eINSTANCE.createAnalyticsModel()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.PROVISIONAL_CARGO__PORTFOLIO_MODEL,
				 AnalyticsFactory.eINSTANCE.createUnitCostMatrix()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.PROVISIONAL_CARGO__PORTFOLIO_MODEL,
				 AnalyticsFactory.eINSTANCE.createUnitCostLine()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.PROVISIONAL_CARGO__PORTFOLIO_MODEL,
				 AnalyticsFactory.eINSTANCE.createCostComponent()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.PROVISIONAL_CARGO__PORTFOLIO_MODEL,
				 AnalyticsFactory.eINSTANCE.createVoyage()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.PROVISIONAL_CARGO__PORTFOLIO_MODEL,
				 AnalyticsFactory.eINSTANCE.createVisit()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.PROVISIONAL_CARGO__PORTFOLIO_MODEL,
				 AnalyticsFactory.eINSTANCE.createFuelCost()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.PROVISIONAL_CARGO__PORTFOLIO_MODEL,
				 AnalyticsFactory.eINSTANCE.createJourney()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.PROVISIONAL_CARGO__PORTFOLIO_MODEL,
				 AnalyticsFactory.eINSTANCE.createShippingCostPlan()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.PROVISIONAL_CARGO__PORTFOLIO_MODEL,
				 AnalyticsFactory.eINSTANCE.createShippingCostRow()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.PROVISIONAL_CARGO__PORTFOLIO_MODEL,
				 AnalyticsFactory.eINSTANCE.createCargoSandbox()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.PROVISIONAL_CARGO__PORTFOLIO_MODEL,
				 AnalyticsFactory.eINSTANCE.createProvisionalCargo()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.PROVISIONAL_CARGO__PORTFOLIO_MODEL,
				 AnalyticsFactory.eINSTANCE.createBuyOpportunity()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.PROVISIONAL_CARGO__PORTFOLIO_MODEL,
				 AnalyticsFactory.eINSTANCE.createSellOpportunity()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.PROVISIONAL_CARGO__PORTFOLIO_MODEL,
				 AnalyticsFactory.eINSTANCE.createBuyMarket()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.PROVISIONAL_CARGO__PORTFOLIO_MODEL,
				 AnalyticsFactory.eINSTANCE.createSellMarket()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.PROVISIONAL_CARGO__PORTFOLIO_MODEL,
				 AnalyticsFactory.eINSTANCE.createBuyReference()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.PROVISIONAL_CARGO__PORTFOLIO_MODEL,
				 AnalyticsFactory.eINSTANCE.createSellReference()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.PROVISIONAL_CARGO__PORTFOLIO_MODEL,
				 AnalyticsFactory.eINSTANCE.createBaseCaseRow()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.PROVISIONAL_CARGO__PORTFOLIO_MODEL,
				 AnalyticsFactory.eINSTANCE.createPartialCaseRow()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.PROVISIONAL_CARGO__PORTFOLIO_MODEL,
				 AnalyticsFactory.eINSTANCE.createShippingOption()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.PROVISIONAL_CARGO__PORTFOLIO_MODEL,
				 AnalyticsFactory.eINSTANCE.createFleetShippingOption()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.PROVISIONAL_CARGO__PORTFOLIO_MODEL,
				 AnalyticsFactory.eINSTANCE.createRoundTripShippingOption()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.PROVISIONAL_CARGO__PORTFOLIO_MODEL,
				 AnalyticsFactory.eINSTANCE.createNominatedShippingOption()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.PROVISIONAL_CARGO__PORTFOLIO_MODEL,
				 AnalyticsFactory.eINSTANCE.createAnalysisResultRow()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.PROVISIONAL_CARGO__PORTFOLIO_MODEL,
				 AnalyticsFactory.eINSTANCE.createAnalysisResultDetail()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.PROVISIONAL_CARGO__PORTFOLIO_MODEL,
				 AnalyticsFactory.eINSTANCE.createProfitAndLossResult()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.PROVISIONAL_CARGO__PORTFOLIO_MODEL,
				 AnalyticsFactory.eINSTANCE.createBreakEvenResult()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.PROVISIONAL_CARGO__PORTFOLIO_MODEL,
				 AnalyticsFactory.eINSTANCE.createModeOptionRule()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.PROVISIONAL_CARGO__PORTFOLIO_MODEL,
				 AnalyticsFactory.eINSTANCE.createOptionAnalysisModel()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.PROVISIONAL_CARGO__PORTFOLIO_MODEL,
				 AnalyticsFactory.eINSTANCE.createResultSet()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.PROVISIONAL_CARGO__PORTFOLIO_MODEL,
				 AnalyticsFactory.eINSTANCE.createBaseCase()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.PROVISIONAL_CARGO__PORTFOLIO_MODEL,
				 AnalyticsFactory.eINSTANCE.createPartialCase()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.PROVISIONAL_CARGO__PORTFOLIO_MODEL,
				 CargoFactory.eINSTANCE.createCargoModel()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.PROVISIONAL_CARGO__PORTFOLIO_MODEL,
				 CargoFactory.eINSTANCE.createCargo()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.PROVISIONAL_CARGO__PORTFOLIO_MODEL,
				 CargoFactory.eINSTANCE.createLoadSlot()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.PROVISIONAL_CARGO__PORTFOLIO_MODEL,
				 CargoFactory.eINSTANCE.createDischargeSlot()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.PROVISIONAL_CARGO__PORTFOLIO_MODEL,
				 CargoFactory.eINSTANCE.createSpotLoadSlot()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.PROVISIONAL_CARGO__PORTFOLIO_MODEL,
				 CargoFactory.eINSTANCE.createSpotDischargeSlot()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.PROVISIONAL_CARGO__PORTFOLIO_MODEL,
				 CargoFactory.eINSTANCE.createCargoGroup()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.PROVISIONAL_CARGO__PORTFOLIO_MODEL,
				 CargoFactory.eINSTANCE.createVesselAvailability()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.PROVISIONAL_CARGO__PORTFOLIO_MODEL,
				 CargoFactory.eINSTANCE.createMaintenanceEvent()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.PROVISIONAL_CARGO__PORTFOLIO_MODEL,
				 CargoFactory.eINSTANCE.createDryDockEvent()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.PROVISIONAL_CARGO__PORTFOLIO_MODEL,
				 CargoFactory.eINSTANCE.createCharterOutEvent()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.PROVISIONAL_CARGO__PORTFOLIO_MODEL,
				 CargoFactory.eINSTANCE.createVesselTypeGroup()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.PROVISIONAL_CARGO__PORTFOLIO_MODEL,
				 CargoFactory.eINSTANCE.createEndHeelOptions()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.PROVISIONAL_CARGO__PORTFOLIO_MODEL,
				 CommercialFactory.eINSTANCE.createCommercialModel()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.PROVISIONAL_CARGO__PORTFOLIO_MODEL,
				 CommercialFactory.eINSTANCE.createLegalEntity()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.PROVISIONAL_CARGO__PORTFOLIO_MODEL,
				 CommercialFactory.eINSTANCE.createContract()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.PROVISIONAL_CARGO__PORTFOLIO_MODEL,
				 CommercialFactory.eINSTANCE.createSalesContract()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.PROVISIONAL_CARGO__PORTFOLIO_MODEL,
				 CommercialFactory.eINSTANCE.createPurchaseContract()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.PROVISIONAL_CARGO__PORTFOLIO_MODEL,
				 CommercialFactory.eINSTANCE.createTaxRate()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.PROVISIONAL_CARGO__PORTFOLIO_MODEL,
				 CommercialFactory.eINSTANCE.createExpressionPriceParameters()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.PROVISIONAL_CARGO__PORTFOLIO_MODEL,
				 CommercialFactory.eINSTANCE.createContractExpressionMapEntry()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.PROVISIONAL_CARGO__PORTFOLIO_MODEL,
				 CommercialFactory.eINSTANCE.createSimpleEntityBook()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.PROVISIONAL_CARGO__PORTFOLIO_MODEL,
				 FleetFactory.eINSTANCE.createFleetModel()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.PROVISIONAL_CARGO__PORTFOLIO_MODEL,
				 FleetFactory.eINSTANCE.createBaseFuel()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.PROVISIONAL_CARGO__PORTFOLIO_MODEL,
				 FleetFactory.eINSTANCE.createVessel()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.PROVISIONAL_CARGO__PORTFOLIO_MODEL,
				 FleetFactory.eINSTANCE.createVesselClass()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.PROVISIONAL_CARGO__PORTFOLIO_MODEL,
				 FleetFactory.eINSTANCE.createVesselGroup()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.PROVISIONAL_CARGO__PORTFOLIO_MODEL,
				 FleetFactory.eINSTANCE.createHeelOptions()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.PROVISIONAL_CARGO__PORTFOLIO_MODEL,
				 FleetFactory.eINSTANCE.createVesselStateAttributes()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.PROVISIONAL_CARGO__PORTFOLIO_MODEL,
				 FleetFactory.eINSTANCE.createFuelConsumption()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.PROVISIONAL_CARGO__PORTFOLIO_MODEL,
				 FleetFactory.eINSTANCE.createVesselClassRouteParameters()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.PROVISIONAL_CARGO__PORTFOLIO_MODEL,
				 MMXCoreFactory.eINSTANCE.createNamedObject()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.PROVISIONAL_CARGO__PORTFOLIO_MODEL,
				 MMXCoreFactory.eINSTANCE.createOtherNamesObject()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.PROVISIONAL_CARGO__PORTFOLIO_MODEL,
				 MMXCoreFactory.eINSTANCE.createUUIDObject()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.PROVISIONAL_CARGO__PORTFOLIO_MODEL,
				 MMXCoreFactory.eINSTANCE.createMMXRootObject()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.PROVISIONAL_CARGO__PORTFOLIO_MODEL,
				 PortFactory.eINSTANCE.createPort()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.PROVISIONAL_CARGO__PORTFOLIO_MODEL,
				 PortFactory.eINSTANCE.createRoute()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.PROVISIONAL_CARGO__PORTFOLIO_MODEL,
				 PortFactory.eINSTANCE.createPortGroup()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.PROVISIONAL_CARGO__PORTFOLIO_MODEL,
				 PortFactory.eINSTANCE.createRouteLine()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.PROVISIONAL_CARGO__PORTFOLIO_MODEL,
				 PortFactory.eINSTANCE.createPortModel()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.PROVISIONAL_CARGO__PORTFOLIO_MODEL,
				 PortFactory.eINSTANCE.createCapabilityGroup()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.PROVISIONAL_CARGO__PORTFOLIO_MODEL,
				 PortFactory.eINSTANCE.createLocation()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.PROVISIONAL_CARGO__PORTFOLIO_MODEL,
				 PricingFactory.eINSTANCE.createPricingModel()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.PROVISIONAL_CARGO__PORTFOLIO_MODEL,
				 PricingFactory.eINSTANCE.createDataIndex()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.PROVISIONAL_CARGO__PORTFOLIO_MODEL,
				 PricingFactory.eINSTANCE.createDerivedIndex()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.PROVISIONAL_CARGO__PORTFOLIO_MODEL,
				 PricingFactory.eINSTANCE.createIndexPoint()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.PROVISIONAL_CARGO__PORTFOLIO_MODEL,
				 PricingFactory.eINSTANCE.createNamedIndexContainer()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.PROVISIONAL_CARGO__PORTFOLIO_MODEL,
				 PricingFactory.eINSTANCE.createCurrencyIndex()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.PROVISIONAL_CARGO__PORTFOLIO_MODEL,
				 PricingFactory.eINSTANCE.createCommodityIndex()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.PROVISIONAL_CARGO__PORTFOLIO_MODEL,
				 PricingFactory.eINSTANCE.createCharterIndex()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.PROVISIONAL_CARGO__PORTFOLIO_MODEL,
				 PricingFactory.eINSTANCE.createBaseFuelIndex()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.PROVISIONAL_CARGO__PORTFOLIO_MODEL,
				 PricingFactory.eINSTANCE.createCostModel()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.PROVISIONAL_CARGO__PORTFOLIO_MODEL,
				 PricingFactory.eINSTANCE.createRouteCost()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.PROVISIONAL_CARGO__PORTFOLIO_MODEL,
				 PricingFactory.eINSTANCE.createBaseFuelCost()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.PROVISIONAL_CARGO__PORTFOLIO_MODEL,
				 PricingFactory.eINSTANCE.createPortCost()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.PROVISIONAL_CARGO__PORTFOLIO_MODEL,
				 PricingFactory.eINSTANCE.createPortCostEntry()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.PROVISIONAL_CARGO__PORTFOLIO_MODEL,
				 PricingFactory.eINSTANCE.createPortsExpressionMap()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.PROVISIONAL_CARGO__PORTFOLIO_MODEL,
				 PricingFactory.eINSTANCE.createCooldownPrice()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.PROVISIONAL_CARGO__PORTFOLIO_MODEL,
				 PricingFactory.eINSTANCE.createPortsPriceMap()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.PROVISIONAL_CARGO__PORTFOLIO_MODEL,
				 PricingFactory.eINSTANCE.createPortsSplitPriceMap()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.PROVISIONAL_CARGO__PORTFOLIO_MODEL,
				 PricingFactory.eINSTANCE.createPortsSplitExpressionMap()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.PROVISIONAL_CARGO__PORTFOLIO_MODEL,
				 PricingFactory.eINSTANCE.createPanamaCanalTariff()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.PROVISIONAL_CARGO__PORTFOLIO_MODEL,
				 PricingFactory.eINSTANCE.createPanamaCanalTariffBand()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.PROVISIONAL_CARGO__PORTFOLIO_MODEL,
				 PricingFactory.eINSTANCE.createUnitConversion()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.PROVISIONAL_CARGO__PORTFOLIO_MODEL,
				 SpotMarketsFactory.eINSTANCE.createSpotMarketsModel()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.PROVISIONAL_CARGO__PORTFOLIO_MODEL,
				 SpotMarketsFactory.eINSTANCE.createSpotMarketGroup()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.PROVISIONAL_CARGO__PORTFOLIO_MODEL,
				 SpotMarketsFactory.eINSTANCE.createDESPurchaseMarket()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.PROVISIONAL_CARGO__PORTFOLIO_MODEL,
				 SpotMarketsFactory.eINSTANCE.createDESSalesMarket()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.PROVISIONAL_CARGO__PORTFOLIO_MODEL,
				 SpotMarketsFactory.eINSTANCE.createFOBPurchasesMarket()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.PROVISIONAL_CARGO__PORTFOLIO_MODEL,
				 SpotMarketsFactory.eINSTANCE.createFOBSalesMarket()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.PROVISIONAL_CARGO__PORTFOLIO_MODEL,
				 SpotMarketsFactory.eINSTANCE.createSpotAvailability()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.PROVISIONAL_CARGO__PORTFOLIO_MODEL,
				 SpotMarketsFactory.eINSTANCE.createCharterOutStartDate()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.PROVISIONAL_CARGO__PORTFOLIO_MODEL,
				 SpotMarketsFactory.eINSTANCE.createCharterOutMarket()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.PROVISIONAL_CARGO__PORTFOLIO_MODEL,
				 SpotMarketsFactory.eINSTANCE.createCharterInMarket()));
	}

	/**
	 * This returns the label text for {@link org.eclipse.emf.edit.command.CreateChildCommand}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getCreateChildText(Object owner, Object feature, Object child, Collection<?> selection) {
		Object childFeature = feature;
		Object childObject = child;

		boolean qualify =
			childFeature == AnalyticsPackage.Literals.PROVISIONAL_CARGO__BUY ||
			childFeature == AnalyticsPackage.Literals.PROVISIONAL_CARGO__PORTFOLIO_MODEL ||
			childFeature == AnalyticsPackage.Literals.PROVISIONAL_CARGO__SELL;

		if (qualify) {
			return getString
				("_UI_CreateChild_text2",
				 new Object[] { getTypeText(childObject), getFeatureText(childFeature), getTypeText(owner) });
		}
		return super.getCreateChildText(owner, feature, child, selection);
	}

}
