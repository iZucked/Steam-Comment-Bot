/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.analytics.provider;


import com.mmxlabs.models.lng.analytics.AbstractAnalysisModel;
import com.mmxlabs.models.lng.analytics.AnalyticsFactory;
import com.mmxlabs.models.lng.analytics.AnalyticsPackage;

import com.mmxlabs.models.mmxcore.provider.NamedObjectItemProvider;

import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EStructuralFeature;

import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ViewerNotification;

/**
 * This is the item provider adapter for a {@link com.mmxlabs.models.lng.analytics.AbstractAnalysisModel} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class AbstractAnalysisModelItemProvider extends NamedObjectItemProvider {
	/**
	 * This constructs an instance from a factory and a notifier.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AbstractAnalysisModelItemProvider(AdapterFactory adapterFactory) {
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
			childrenFeatures.add(AnalyticsPackage.Literals.ABSTRACT_ANALYSIS_MODEL__BUYS);
			childrenFeatures.add(AnalyticsPackage.Literals.ABSTRACT_ANALYSIS_MODEL__SELLS);
			childrenFeatures.add(AnalyticsPackage.Literals.ABSTRACT_ANALYSIS_MODEL__VESSEL_EVENTS);
			childrenFeatures.add(AnalyticsPackage.Literals.ABSTRACT_ANALYSIS_MODEL__SHIPPING_TEMPLATES);
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
	 * This returns the label text for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getText(Object object) {
		String label = ((AbstractAnalysisModel)object).getName();
		return label == null || label.length() == 0 ?
			getString("_UI_AbstractAnalysisModel_type") :
			getString("_UI_AbstractAnalysisModel_type") + " " + label;
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

		switch (notification.getFeatureID(AbstractAnalysisModel.class)) {
			case AnalyticsPackage.ABSTRACT_ANALYSIS_MODEL__BUYS:
			case AnalyticsPackage.ABSTRACT_ANALYSIS_MODEL__SELLS:
			case AnalyticsPackage.ABSTRACT_ANALYSIS_MODEL__VESSEL_EVENTS:
			case AnalyticsPackage.ABSTRACT_ANALYSIS_MODEL__SHIPPING_TEMPLATES:
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
				(AnalyticsPackage.Literals.ABSTRACT_ANALYSIS_MODEL__BUYS,
				 AnalyticsFactory.eINSTANCE.createOpenBuy()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.ABSTRACT_ANALYSIS_MODEL__BUYS,
				 AnalyticsFactory.eINSTANCE.createBuyOpportunity()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.ABSTRACT_ANALYSIS_MODEL__BUYS,
				 AnalyticsFactory.eINSTANCE.createBuyMarket()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.ABSTRACT_ANALYSIS_MODEL__BUYS,
				 AnalyticsFactory.eINSTANCE.createBuyReference()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.ABSTRACT_ANALYSIS_MODEL__SELLS,
				 AnalyticsFactory.eINSTANCE.createOpenSell()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.ABSTRACT_ANALYSIS_MODEL__SELLS,
				 AnalyticsFactory.eINSTANCE.createSellOpportunity()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.ABSTRACT_ANALYSIS_MODEL__SELLS,
				 AnalyticsFactory.eINSTANCE.createSellMarket()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.ABSTRACT_ANALYSIS_MODEL__SELLS,
				 AnalyticsFactory.eINSTANCE.createSellReference()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.ABSTRACT_ANALYSIS_MODEL__VESSEL_EVENTS,
				 AnalyticsFactory.eINSTANCE.createVesselEventOption()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.ABSTRACT_ANALYSIS_MODEL__VESSEL_EVENTS,
				 AnalyticsFactory.eINSTANCE.createVesselEventReference()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.ABSTRACT_ANALYSIS_MODEL__VESSEL_EVENTS,
				 AnalyticsFactory.eINSTANCE.createCharterOutOpportunity()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.ABSTRACT_ANALYSIS_MODEL__SHIPPING_TEMPLATES,
				 AnalyticsFactory.eINSTANCE.createShippingOption()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.ABSTRACT_ANALYSIS_MODEL__SHIPPING_TEMPLATES,
				 AnalyticsFactory.eINSTANCE.createSimpleVesselCharterOption()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.ABSTRACT_ANALYSIS_MODEL__SHIPPING_TEMPLATES,
				 AnalyticsFactory.eINSTANCE.createOptionalSimpleVesselCharterOption()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.ABSTRACT_ANALYSIS_MODEL__SHIPPING_TEMPLATES,
				 AnalyticsFactory.eINSTANCE.createRoundTripShippingOption()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.ABSTRACT_ANALYSIS_MODEL__SHIPPING_TEMPLATES,
				 AnalyticsFactory.eINSTANCE.createNominatedShippingOption()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.ABSTRACT_ANALYSIS_MODEL__SHIPPING_TEMPLATES,
				 AnalyticsFactory.eINSTANCE.createFullVesselCharterOption()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.ABSTRACT_ANALYSIS_MODEL__SHIPPING_TEMPLATES,
				 AnalyticsFactory.eINSTANCE.createExistingVesselCharterOption()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.ABSTRACT_ANALYSIS_MODEL__SHIPPING_TEMPLATES,
				 AnalyticsFactory.eINSTANCE.createExistingCharterMarketOption()));
	}

}
