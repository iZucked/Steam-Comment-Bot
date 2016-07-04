/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.spotmarkets.provider;


import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ViewerNotification;

import com.mmxlabs.models.lng.spotmarkets.SpotMarketsFactory;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsModel;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage;
import com.mmxlabs.models.mmxcore.provider.UUIDObjectItemProvider;

/**
 * This is the item provider adapter for a {@link com.mmxlabs.models.lng.spotmarkets.SpotMarketsModel} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class SpotMarketsModelItemProvider
	extends UUIDObjectItemProvider {
	/**
	 * This constructs an instance from a factory and a notifier.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SpotMarketsModelItemProvider(AdapterFactory adapterFactory) {
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
			childrenFeatures.add(SpotMarketsPackage.Literals.SPOT_MARKETS_MODEL__DES_PURCHASE_SPOT_MARKET);
			childrenFeatures.add(SpotMarketsPackage.Literals.SPOT_MARKETS_MODEL__DES_SALES_SPOT_MARKET);
			childrenFeatures.add(SpotMarketsPackage.Literals.SPOT_MARKETS_MODEL__FOB_PURCHASES_SPOT_MARKET);
			childrenFeatures.add(SpotMarketsPackage.Literals.SPOT_MARKETS_MODEL__FOB_SALES_SPOT_MARKET);
			childrenFeatures.add(SpotMarketsPackage.Literals.SPOT_MARKETS_MODEL__CHARTER_OUT_START_DATE);
			childrenFeatures.add(SpotMarketsPackage.Literals.SPOT_MARKETS_MODEL__CHARTER_IN_MARKETS);
			childrenFeatures.add(SpotMarketsPackage.Literals.SPOT_MARKETS_MODEL__CHARTER_OUT_MARKETS);
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
	 * This returns SpotMarketsModel.gif.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object getImage(Object object) {
		return overlayImage(object, getResourceLocator().getImage("full/obj16/SpotMarketsModel"));
	}

	/**
	 * This returns the label text for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getText(Object object) {
		String label = ((SpotMarketsModel)object).getUuid();
		return label == null || label.length() == 0 ?
			getString("_UI_SpotMarketsModel_type") :
			getString("_UI_SpotMarketsModel_type") + " " + label;
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

		switch (notification.getFeatureID(SpotMarketsModel.class)) {
			case SpotMarketsPackage.SPOT_MARKETS_MODEL__DES_PURCHASE_SPOT_MARKET:
			case SpotMarketsPackage.SPOT_MARKETS_MODEL__DES_SALES_SPOT_MARKET:
			case SpotMarketsPackage.SPOT_MARKETS_MODEL__FOB_PURCHASES_SPOT_MARKET:
			case SpotMarketsPackage.SPOT_MARKETS_MODEL__FOB_SALES_SPOT_MARKET:
			case SpotMarketsPackage.SPOT_MARKETS_MODEL__CHARTER_OUT_START_DATE:
			case SpotMarketsPackage.SPOT_MARKETS_MODEL__CHARTER_IN_MARKETS:
			case SpotMarketsPackage.SPOT_MARKETS_MODEL__CHARTER_OUT_MARKETS:
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
				(SpotMarketsPackage.Literals.SPOT_MARKETS_MODEL__DES_PURCHASE_SPOT_MARKET,
				 SpotMarketsFactory.eINSTANCE.createSpotMarketGroup()));

		newChildDescriptors.add
			(createChildParameter
				(SpotMarketsPackage.Literals.SPOT_MARKETS_MODEL__DES_SALES_SPOT_MARKET,
				 SpotMarketsFactory.eINSTANCE.createSpotMarketGroup()));

		newChildDescriptors.add
			(createChildParameter
				(SpotMarketsPackage.Literals.SPOT_MARKETS_MODEL__FOB_PURCHASES_SPOT_MARKET,
				 SpotMarketsFactory.eINSTANCE.createSpotMarketGroup()));

		newChildDescriptors.add
			(createChildParameter
				(SpotMarketsPackage.Literals.SPOT_MARKETS_MODEL__FOB_SALES_SPOT_MARKET,
				 SpotMarketsFactory.eINSTANCE.createSpotMarketGroup()));

		newChildDescriptors.add
			(createChildParameter
				(SpotMarketsPackage.Literals.SPOT_MARKETS_MODEL__CHARTER_OUT_START_DATE,
				 SpotMarketsFactory.eINSTANCE.createCharterOutStartDate()));

		newChildDescriptors.add
			(createChildParameter
				(SpotMarketsPackage.Literals.SPOT_MARKETS_MODEL__CHARTER_IN_MARKETS,
				 SpotMarketsFactory.eINSTANCE.createCharterInMarket()));

		newChildDescriptors.add
			(createChildParameter
				(SpotMarketsPackage.Literals.SPOT_MARKETS_MODEL__CHARTER_OUT_MARKETS,
				 SpotMarketsFactory.eINSTANCE.createCharterOutMarket()));
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
			childFeature == SpotMarketsPackage.Literals.SPOT_MARKETS_MODEL__DES_PURCHASE_SPOT_MARKET ||
			childFeature == SpotMarketsPackage.Literals.SPOT_MARKETS_MODEL__DES_SALES_SPOT_MARKET ||
			childFeature == SpotMarketsPackage.Literals.SPOT_MARKETS_MODEL__FOB_PURCHASES_SPOT_MARKET ||
			childFeature == SpotMarketsPackage.Literals.SPOT_MARKETS_MODEL__FOB_SALES_SPOT_MARKET;

		if (qualify) {
			return getString
				("_UI_CreateChild_text2",
				 new Object[] { getTypeText(childObject), getFeatureText(childFeature), getTypeText(owner) });
		}
		return super.getCreateChildText(owner, feature, child, selection);
	}

}
