/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.analytics.provider;


import com.mmxlabs.models.lng.analytics.AnalyticsFactory;
import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.MTMResult;

import java.time.LocalDate;

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
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ItemProviderAdapter;
import org.eclipse.emf.edit.provider.ViewerNotification;

/**
 * This is the item provider adapter for a {@link com.mmxlabs.models.lng.analytics.MTMResult} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class MTMResultItemProvider 
	extends ItemProviderAdapter
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
	public MTMResultItemProvider(AdapterFactory adapterFactory) {
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

			addTargetPropertyDescriptor(object);
			addEarliestETAPropertyDescriptor(object);
			addEarliestVolumePropertyDescriptor(object);
			addEarliestPricePropertyDescriptor(object);
			addShippingCostPropertyDescriptor(object);
			addOriginalVolumePropertyDescriptor(object);
			addOriginalPricePropertyDescriptor(object);
			addTotalShippingCostPropertyDescriptor(object);
		}
		return itemPropertyDescriptors;
	}

	/**
	 * This adds a property descriptor for the Target feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addTargetPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_MTMResult_target_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_MTMResult_target_feature", "_UI_MTMResult_type"),
				 AnalyticsPackage.Literals.MTM_RESULT__TARGET,
				 true,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Earliest ETA feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addEarliestETAPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_MTMResult_earliestETA_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_MTMResult_earliestETA_feature", "_UI_MTMResult_type"),
				 AnalyticsPackage.Literals.MTM_RESULT__EARLIEST_ETA,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Earliest Volume feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addEarliestVolumePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_MTMResult_earliestVolume_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_MTMResult_earliestVolume_feature", "_UI_MTMResult_type"),
				 AnalyticsPackage.Literals.MTM_RESULT__EARLIEST_VOLUME,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Earliest Price feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addEarliestPricePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_MTMResult_earliestPrice_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_MTMResult_earliestPrice_feature", "_UI_MTMResult_type"),
				 AnalyticsPackage.Literals.MTM_RESULT__EARLIEST_PRICE,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.REAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Shipping Cost feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addShippingCostPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_MTMResult_shippingCost_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_MTMResult_shippingCost_feature", "_UI_MTMResult_type"),
				 AnalyticsPackage.Literals.MTM_RESULT__SHIPPING_COST,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.REAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Original Volume feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addOriginalVolumePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_MTMResult_originalVolume_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_MTMResult_originalVolume_feature", "_UI_MTMResult_type"),
				 AnalyticsPackage.Literals.MTM_RESULT__ORIGINAL_VOLUME,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Original Price feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addOriginalPricePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_MTMResult_originalPrice_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_MTMResult_originalPrice_feature", "_UI_MTMResult_type"),
				 AnalyticsPackage.Literals.MTM_RESULT__ORIGINAL_PRICE,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.REAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Total Shipping Cost feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addTotalShippingCostPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_MTMResult_totalShippingCost_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_MTMResult_totalShippingCost_feature", "_UI_MTMResult_type"),
				 AnalyticsPackage.Literals.MTM_RESULT__TOTAL_SHIPPING_COST,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
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
			childrenFeatures.add(AnalyticsPackage.Literals.MTM_RESULT__SHIPPING);
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
	 * This returns MTMResult.gif.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object getImage(Object object) {
		return overlayImage(object, getResourceLocator().getImage("full/obj16/MTMResult"));
	}

	/**
	 * This returns the label text for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getText(Object object) {
		LocalDate labelValue = ((MTMResult)object).getEarliestETA();
		String label = labelValue == null ? null : labelValue.toString();
		return label == null || label.length() == 0 ?
			getString("_UI_MTMResult_type") :
			getString("_UI_MTMResult_type") + " " + label;
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

		switch (notification.getFeatureID(MTMResult.class)) {
			case AnalyticsPackage.MTM_RESULT__EARLIEST_ETA:
			case AnalyticsPackage.MTM_RESULT__EARLIEST_VOLUME:
			case AnalyticsPackage.MTM_RESULT__EARLIEST_PRICE:
			case AnalyticsPackage.MTM_RESULT__SHIPPING_COST:
			case AnalyticsPackage.MTM_RESULT__ORIGINAL_VOLUME:
			case AnalyticsPackage.MTM_RESULT__ORIGINAL_PRICE:
			case AnalyticsPackage.MTM_RESULT__TOTAL_SHIPPING_COST:
				fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), false, true));
				return;
			case AnalyticsPackage.MTM_RESULT__SHIPPING:
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
				(AnalyticsPackage.Literals.MTM_RESULT__SHIPPING,
				 AnalyticsFactory.eINSTANCE.createShippingOption()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.MTM_RESULT__SHIPPING,
				 AnalyticsFactory.eINSTANCE.createSimpleVesselCharterOption()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.MTM_RESULT__SHIPPING,
				 AnalyticsFactory.eINSTANCE.createOptionalSimpleVesselCharterOption()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.MTM_RESULT__SHIPPING,
				 AnalyticsFactory.eINSTANCE.createRoundTripShippingOption()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.MTM_RESULT__SHIPPING,
				 AnalyticsFactory.eINSTANCE.createNominatedShippingOption()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.MTM_RESULT__SHIPPING,
				 AnalyticsFactory.eINSTANCE.createFullVesselCharterOption()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.MTM_RESULT__SHIPPING,
				 AnalyticsFactory.eINSTANCE.createExistingVesselCharterOption()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.MTM_RESULT__SHIPPING,
				 AnalyticsFactory.eINSTANCE.createExistingCharterMarketOption()));
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
