/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.pricing.provider;


import com.mmxlabs.models.lng.pricing.CostModel;
import com.mmxlabs.models.lng.pricing.PricingFactory;
import com.mmxlabs.models.lng.pricing.PricingPackage;

import com.mmxlabs.models.mmxcore.provider.UUIDObjectItemProvider;

import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EStructuralFeature;

import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ViewerNotification;

/**
 * This is the item provider adapter for a {@link com.mmxlabs.models.lng.pricing.CostModel} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class CostModelItemProvider extends UUIDObjectItemProvider {
	/**
	 * This constructs an instance from a factory and a notifier.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CostModelItemProvider(AdapterFactory adapterFactory) {
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
			childrenFeatures.add(PricingPackage.Literals.COST_MODEL__ROUTE_COSTS);
			childrenFeatures.add(PricingPackage.Literals.COST_MODEL__PORT_COSTS);
			childrenFeatures.add(PricingPackage.Literals.COST_MODEL__COOLDOWN_COSTS);
			childrenFeatures.add(PricingPackage.Literals.COST_MODEL__BASE_FUEL_COSTS);
			childrenFeatures.add(PricingPackage.Literals.COST_MODEL__PANAMA_CANAL_TARIFF);
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
	 * This returns CostModel.gif.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object getImage(Object object) {
		return overlayImage(object, getResourceLocator().getImage("full/obj16/CostModel"));
	}

	/**
	 * This returns the label text for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getText(Object object) {
		String label = ((CostModel)object).getUuid();
		return label == null || label.length() == 0 ?
			getString("_UI_CostModel_type") :
			getString("_UI_CostModel_type") + " " + label;
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

		switch (notification.getFeatureID(CostModel.class)) {
			case PricingPackage.COST_MODEL__ROUTE_COSTS:
			case PricingPackage.COST_MODEL__PORT_COSTS:
			case PricingPackage.COST_MODEL__COOLDOWN_COSTS:
			case PricingPackage.COST_MODEL__BASE_FUEL_COSTS:
			case PricingPackage.COST_MODEL__PANAMA_CANAL_TARIFF:
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
				(PricingPackage.Literals.COST_MODEL__ROUTE_COSTS,
				 PricingFactory.eINSTANCE.createRouteCost()));

		newChildDescriptors.add
			(createChildParameter
				(PricingPackage.Literals.COST_MODEL__PORT_COSTS,
				 PricingFactory.eINSTANCE.createPortCost()));

		newChildDescriptors.add
			(createChildParameter
				(PricingPackage.Literals.COST_MODEL__COOLDOWN_COSTS,
				 PricingFactory.eINSTANCE.createCooldownPrice()));

		newChildDescriptors.add
			(createChildParameter
				(PricingPackage.Literals.COST_MODEL__BASE_FUEL_COSTS,
				 PricingFactory.eINSTANCE.createBaseFuelCost()));

		newChildDescriptors.add
			(createChildParameter
				(PricingPackage.Literals.COST_MODEL__PANAMA_CANAL_TARIFF,
				 PricingFactory.eINSTANCE.createPanamaCanalTariff()));
	}

}
