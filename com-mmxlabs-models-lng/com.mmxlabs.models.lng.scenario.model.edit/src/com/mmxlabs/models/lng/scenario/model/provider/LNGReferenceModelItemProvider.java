/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.scenario.model.provider;


import com.mmxlabs.models.lng.analytics.AnalyticsFactory;

import com.mmxlabs.models.lng.commercial.CommercialFactory;

import com.mmxlabs.models.lng.fleet.FleetFactory;

import com.mmxlabs.models.lng.port.PortFactory;

import com.mmxlabs.models.lng.pricing.PricingFactory;

import com.mmxlabs.models.lng.scenario.model.LNGReferenceModel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioPackage;

import com.mmxlabs.models.lng.spotmarkets.SpotMarketsFactory;

import com.mmxlabs.models.mmxcore.provider.UUIDObjectItemProvider;

import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EStructuralFeature;

import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ViewerNotification;

/**
 * This is the item provider adapter for a {@link com.mmxlabs.models.lng.scenario.model.LNGReferenceModel} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class LNGReferenceModelItemProvider extends UUIDObjectItemProvider {
	/**
	 * This constructs an instance from a factory and a notifier.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public LNGReferenceModelItemProvider(AdapterFactory adapterFactory) {
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

		}
		return itemPropertyDescriptors;
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
			childrenFeatures.add(LNGScenarioPackage.eINSTANCE.getLNGReferenceModel_PortModel());
			childrenFeatures.add(LNGScenarioPackage.eINSTANCE.getLNGReferenceModel_FleetModel());
			childrenFeatures.add(LNGScenarioPackage.eINSTANCE.getLNGReferenceModel_PricingModel());
			childrenFeatures.add(LNGScenarioPackage.eINSTANCE.getLNGReferenceModel_CommercialModel());
			childrenFeatures.add(LNGScenarioPackage.eINSTANCE.getLNGReferenceModel_SpotMarketsModel());
			childrenFeatures.add(LNGScenarioPackage.eINSTANCE.getLNGReferenceModel_AnalyticsModel());
			childrenFeatures.add(LNGScenarioPackage.eINSTANCE.getLNGReferenceModel_CostModel());
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
	 * This returns LNGReferenceModel.gif.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object getImage(Object object) {
		return overlayImage(object, getResourceLocator().getImage("full/obj16/LNGReferenceModel"));
	}

	/**
	 * This returns the label text for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getText(Object object) {
		String label = ((LNGReferenceModel)object).getUuid();
		return label == null || label.length() == 0 ?
			getString("_UI_LNGReferenceModel_type") :
			getString("_UI_LNGReferenceModel_type") + " " + label;
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

		switch (notification.getFeatureID(LNGReferenceModel.class)) {
			case LNGScenarioPackage.LNG_REFERENCE_MODEL__PORT_MODEL:
			case LNGScenarioPackage.LNG_REFERENCE_MODEL__FLEET_MODEL:
			case LNGScenarioPackage.LNG_REFERENCE_MODEL__PRICING_MODEL:
			case LNGScenarioPackage.LNG_REFERENCE_MODEL__COMMERCIAL_MODEL:
			case LNGScenarioPackage.LNG_REFERENCE_MODEL__SPOT_MARKETS_MODEL:
			case LNGScenarioPackage.LNG_REFERENCE_MODEL__ANALYTICS_MODEL:
			case LNGScenarioPackage.LNG_REFERENCE_MODEL__COST_MODEL:
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
				(LNGScenarioPackage.eINSTANCE.getLNGReferenceModel_PortModel(),
				 PortFactory.eINSTANCE.createPortModel()));

		newChildDescriptors.add
			(createChildParameter
				(LNGScenarioPackage.eINSTANCE.getLNGReferenceModel_FleetModel(),
				 FleetFactory.eINSTANCE.createFleetModel()));

		newChildDescriptors.add
			(createChildParameter
				(LNGScenarioPackage.eINSTANCE.getLNGReferenceModel_PricingModel(),
				 PricingFactory.eINSTANCE.createPricingModel()));

		newChildDescriptors.add
			(createChildParameter
				(LNGScenarioPackage.eINSTANCE.getLNGReferenceModel_CommercialModel(),
				 CommercialFactory.eINSTANCE.createCommercialModel()));

		newChildDescriptors.add
			(createChildParameter
				(LNGScenarioPackage.eINSTANCE.getLNGReferenceModel_SpotMarketsModel(),
				 SpotMarketsFactory.eINSTANCE.createSpotMarketsModel()));

		newChildDescriptors.add
			(createChildParameter
				(LNGScenarioPackage.eINSTANCE.getLNGReferenceModel_AnalyticsModel(),
				 AnalyticsFactory.eINSTANCE.createAnalyticsModel()));

		newChildDescriptors.add
			(createChildParameter
				(LNGScenarioPackage.eINSTANCE.getLNGReferenceModel_CostModel(),
				 PricingFactory.eINSTANCE.createCostModel()));
	}

}
