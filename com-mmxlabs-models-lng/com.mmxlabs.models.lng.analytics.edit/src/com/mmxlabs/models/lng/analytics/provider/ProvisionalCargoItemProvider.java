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
import com.mmxlabs.models.lng.commercial.CommercialFactory;
import com.mmxlabs.models.lng.fleet.FleetFactory;
import com.mmxlabs.models.lng.port.PortFactory;
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
