/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.provider;


import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ViewerNotification;

import com.mmxlabs.models.lng.pricing.PricingFactory;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.mmxcore.provider.UUIDObjectItemProvider;

/**
 * This is the item provider adapter for a {@link com.mmxlabs.models.lng.pricing.PricingModel} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class PricingModelItemProvider
	extends UUIDObjectItemProvider {
	/**
	 * This constructs an instance from a factory and a notifier.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public PricingModelItemProvider(AdapterFactory adapterFactory) {
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
			childrenFeatures.add(PricingPackage.Literals.PRICING_MODEL__CURRENCY_INDICES);
			childrenFeatures.add(PricingPackage.Literals.PRICING_MODEL__COMMODITY_INDICES);
			childrenFeatures.add(PricingPackage.Literals.PRICING_MODEL__CHARTER_INDICES);
			childrenFeatures.add(PricingPackage.Literals.PRICING_MODEL__BASE_FUEL_PRICES);
			childrenFeatures.add(PricingPackage.Literals.PRICING_MODEL__CONVERSION_FACTORS);
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
	 * This returns PricingModel.gif.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object getImage(Object object) {
		return overlayImage(object, getResourceLocator().getImage("full/obj16/PricingModel"));
	}

	/**
	 * This returns the label text for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getText(Object object) {
		String label = ((PricingModel)object).getUuid();
		return label == null || label.length() == 0 ?
			getString("_UI_PricingModel_type") :
			getString("_UI_PricingModel_type") + " " + label;
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

		switch (notification.getFeatureID(PricingModel.class)) {
			case PricingPackage.PRICING_MODEL__CURRENCY_INDICES:
			case PricingPackage.PRICING_MODEL__COMMODITY_INDICES:
			case PricingPackage.PRICING_MODEL__CHARTER_INDICES:
			case PricingPackage.PRICING_MODEL__BASE_FUEL_PRICES:
			case PricingPackage.PRICING_MODEL__CONVERSION_FACTORS:
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
				(PricingPackage.Literals.PRICING_MODEL__CURRENCY_INDICES,
				 PricingFactory.eINSTANCE.createCurrencyIndex()));

		newChildDescriptors.add
			(createChildParameter
				(PricingPackage.Literals.PRICING_MODEL__COMMODITY_INDICES,
				 PricingFactory.eINSTANCE.createCommodityIndex()));

		newChildDescriptors.add
			(createChildParameter
				(PricingPackage.Literals.PRICING_MODEL__CHARTER_INDICES,
				 PricingFactory.eINSTANCE.createCharterIndex()));

		newChildDescriptors.add
			(createChildParameter
				(PricingPackage.Literals.PRICING_MODEL__BASE_FUEL_PRICES,
				 PricingFactory.eINSTANCE.createBaseFuelIndex()));

		newChildDescriptors.add
			(createChildParameter
				(PricingPackage.Literals.PRICING_MODEL__CONVERSION_FACTORS,
				 PricingFactory.eINSTANCE.createUnitConversion()));
	}

}
