/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.provider;


import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IChildCreationExtender;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.IStructuredItemContentProvider;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;
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
	extends UUIDObjectItemProvider
	implements
		IEditingDomainItemProvider,
		IStructuredItemContentProvider,
		ITreeItemContentProvider,
		IItemLabelProvider,
		IItemPropertySource {
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

			addPortCostsPropertyDescriptor(object);
		}
		return itemPropertyDescriptors;
	}

	/**
	 * This adds a property descriptor for the Port Costs feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addPortCostsPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_PricingModel_portCosts_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_PricingModel_portCosts_feature", "_UI_PricingModel_type"),
				 PricingPackage.Literals.PRICING_MODEL__PORT_COSTS,
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
			childrenFeatures.add(PricingPackage.Literals.PRICING_MODEL__COMMODITY_INDICES);
			childrenFeatures.add(PricingPackage.Literals.PRICING_MODEL__CHARTER_INDICES);
			childrenFeatures.add(PricingPackage.Literals.PRICING_MODEL__FLEET_COST);
			childrenFeatures.add(PricingPackage.Literals.PRICING_MODEL__ROUTE_COSTS);
			childrenFeatures.add(PricingPackage.Literals.PRICING_MODEL__PORT_COSTS);
			childrenFeatures.add(PricingPackage.Literals.PRICING_MODEL__COOLDOWN_PRICES);
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
			case PricingPackage.PRICING_MODEL__COMMODITY_INDICES:
			case PricingPackage.PRICING_MODEL__CHARTER_INDICES:
			case PricingPackage.PRICING_MODEL__FLEET_COST:
			case PricingPackage.PRICING_MODEL__ROUTE_COSTS:
			case PricingPackage.PRICING_MODEL__PORT_COSTS:
			case PricingPackage.PRICING_MODEL__COOLDOWN_PRICES:
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
				(PricingPackage.Literals.PRICING_MODEL__COMMODITY_INDICES,
				 PricingFactory.eINSTANCE.createDataIndex()));

		newChildDescriptors.add
			(createChildParameter
				(PricingPackage.Literals.PRICING_MODEL__COMMODITY_INDICES,
				 PricingFactory.eINSTANCE.createDerivedIndex()));

		newChildDescriptors.add
			(createChildParameter
				(PricingPackage.Literals.PRICING_MODEL__CHARTER_INDICES,
				 PricingFactory.eINSTANCE.createDataIndex()));

		newChildDescriptors.add
			(createChildParameter
				(PricingPackage.Literals.PRICING_MODEL__CHARTER_INDICES,
				 PricingFactory.eINSTANCE.createDerivedIndex()));

		newChildDescriptors.add
			(createChildParameter
				(PricingPackage.Literals.PRICING_MODEL__FLEET_COST,
				 PricingFactory.eINSTANCE.createFleetCostModel()));

		newChildDescriptors.add
			(createChildParameter
				(PricingPackage.Literals.PRICING_MODEL__ROUTE_COSTS,
				 PricingFactory.eINSTANCE.createRouteCost()));

		newChildDescriptors.add
			(createChildParameter
				(PricingPackage.Literals.PRICING_MODEL__PORT_COSTS,
				 PricingFactory.eINSTANCE.createPortCost()));

		newChildDescriptors.add
			(createChildParameter
				(PricingPackage.Literals.PRICING_MODEL__COOLDOWN_PRICES,
				 PricingFactory.eINSTANCE.createCooldownPrice()));
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
			childFeature == PricingPackage.Literals.PRICING_MODEL__COMMODITY_INDICES ||
			childFeature == PricingPackage.Literals.PRICING_MODEL__CHARTER_INDICES;

		if (qualify) {
			return getString
				("_UI_CreateChild_text2",
				 new Object[] { getTypeText(childObject), getFeatureText(childFeature), getTypeText(owner) });
		}
		return super.getCreateChildText(owner, feature, child, selection);
	}

	/**
	 * Return the resource locator for this item provider's resources.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ResourceLocator getResourceLocator() {
		return ((IChildCreationExtender)adapterFactory).getResourceLocator();
	}

}
