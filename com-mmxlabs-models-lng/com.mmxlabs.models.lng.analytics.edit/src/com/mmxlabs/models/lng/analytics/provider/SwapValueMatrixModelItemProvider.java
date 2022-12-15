/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.analytics.provider;


import com.mmxlabs.models.lng.analytics.AnalyticsFactory;
import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.SwapValueMatrixModel;

import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EStructuralFeature;

import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ViewerNotification;

/**
 * This is the item provider adapter for a {@link com.mmxlabs.models.lng.analytics.SwapValueMatrixModel} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class SwapValueMatrixModelItemProvider extends AbstractAnalysisModelItemProvider {
	/**
	 * This constructs an instance from a factory and a notifier.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SwapValueMatrixModelItemProvider(AdapterFactory adapterFactory) {
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

			addBaseDischargeMinPricePropertyDescriptor(object);
			addBaseDischargeMaxPricePropertyDescriptor(object);
			addBaseDischargeStepSizePropertyDescriptor(object);
			addMarketMinPricePropertyDescriptor(object);
			addMarketMaxPricePropertyDescriptor(object);
			addMarketStepSizePropertyDescriptor(object);
			addSwapFeePropertyDescriptor(object);
		}
		return itemPropertyDescriptors;
	}

	/**
	 * This adds a property descriptor for the Base Discharge Min Price feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addBaseDischargeMinPricePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_SwapValueMatrixModel_baseDischargeMinPrice_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_SwapValueMatrixModel_baseDischargeMinPrice_feature", "_UI_SwapValueMatrixModel_type"),
				 AnalyticsPackage.Literals.SWAP_VALUE_MATRIX_MODEL__BASE_DISCHARGE_MIN_PRICE,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Base Discharge Max Price feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addBaseDischargeMaxPricePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_SwapValueMatrixModel_baseDischargeMaxPrice_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_SwapValueMatrixModel_baseDischargeMaxPrice_feature", "_UI_SwapValueMatrixModel_type"),
				 AnalyticsPackage.Literals.SWAP_VALUE_MATRIX_MODEL__BASE_DISCHARGE_MAX_PRICE,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Base Discharge Step Size feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addBaseDischargeStepSizePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_SwapValueMatrixModel_baseDischargeStepSize_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_SwapValueMatrixModel_baseDischargeStepSize_feature", "_UI_SwapValueMatrixModel_type"),
				 AnalyticsPackage.Literals.SWAP_VALUE_MATRIX_MODEL__BASE_DISCHARGE_STEP_SIZE,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Market Min Price feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addMarketMinPricePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_SwapValueMatrixModel_marketMinPrice_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_SwapValueMatrixModel_marketMinPrice_feature", "_UI_SwapValueMatrixModel_type"),
				 AnalyticsPackage.Literals.SWAP_VALUE_MATRIX_MODEL__MARKET_MIN_PRICE,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Market Max Price feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addMarketMaxPricePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_SwapValueMatrixModel_marketMaxPrice_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_SwapValueMatrixModel_marketMaxPrice_feature", "_UI_SwapValueMatrixModel_type"),
				 AnalyticsPackage.Literals.SWAP_VALUE_MATRIX_MODEL__MARKET_MAX_PRICE,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Market Step Size feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addMarketStepSizePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_SwapValueMatrixModel_marketStepSize_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_SwapValueMatrixModel_marketStepSize_feature", "_UI_SwapValueMatrixModel_type"),
				 AnalyticsPackage.Literals.SWAP_VALUE_MATRIX_MODEL__MARKET_STEP_SIZE,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Swap Fee feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addSwapFeePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_SwapValueMatrixModel_swapFee_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_SwapValueMatrixModel_swapFee_feature", "_UI_SwapValueMatrixModel_type"),
				 AnalyticsPackage.Literals.SWAP_VALUE_MATRIX_MODEL__SWAP_FEE,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.REAL_VALUE_IMAGE,
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
			childrenFeatures.add(AnalyticsPackage.Literals.SWAP_VALUE_MATRIX_MODEL__BASE_LOAD);
			childrenFeatures.add(AnalyticsPackage.Literals.SWAP_VALUE_MATRIX_MODEL__BASE_DISCHARGE);
			childrenFeatures.add(AnalyticsPackage.Literals.SWAP_VALUE_MATRIX_MODEL__BASE_VESSEL_CHARTER);
			childrenFeatures.add(AnalyticsPackage.Literals.SWAP_VALUE_MATRIX_MODEL__SWAP_LOAD_MARKET);
			childrenFeatures.add(AnalyticsPackage.Literals.SWAP_VALUE_MATRIX_MODEL__SWAP_DISCHARGE_MARKET);
			childrenFeatures.add(AnalyticsPackage.Literals.SWAP_VALUE_MATRIX_MODEL__SWAP_VALUE_MATRIX_RESULT);
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
	 * This returns SwapValueMatrixModel.gif.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object getImage(Object object) {
		return overlayImage(object, getResourceLocator().getImage("full/obj16/SwapValueMatrixModel"));
	}

	/**
	 * This returns the label text for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getText(Object object) {
		String label = ((SwapValueMatrixModel)object).getName();
		return label == null || label.length() == 0 ?
			getString("_UI_SwapValueMatrixModel_type") :
			getString("_UI_SwapValueMatrixModel_type") + " " + label;
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

		switch (notification.getFeatureID(SwapValueMatrixModel.class)) {
			case AnalyticsPackage.SWAP_VALUE_MATRIX_MODEL__BASE_DISCHARGE_MIN_PRICE:
			case AnalyticsPackage.SWAP_VALUE_MATRIX_MODEL__BASE_DISCHARGE_MAX_PRICE:
			case AnalyticsPackage.SWAP_VALUE_MATRIX_MODEL__BASE_DISCHARGE_STEP_SIZE:
			case AnalyticsPackage.SWAP_VALUE_MATRIX_MODEL__MARKET_MIN_PRICE:
			case AnalyticsPackage.SWAP_VALUE_MATRIX_MODEL__MARKET_MAX_PRICE:
			case AnalyticsPackage.SWAP_VALUE_MATRIX_MODEL__MARKET_STEP_SIZE:
			case AnalyticsPackage.SWAP_VALUE_MATRIX_MODEL__SWAP_FEE:
				fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), false, true));
				return;
			case AnalyticsPackage.SWAP_VALUE_MATRIX_MODEL__BASE_LOAD:
			case AnalyticsPackage.SWAP_VALUE_MATRIX_MODEL__BASE_DISCHARGE:
			case AnalyticsPackage.SWAP_VALUE_MATRIX_MODEL__BASE_VESSEL_CHARTER:
			case AnalyticsPackage.SWAP_VALUE_MATRIX_MODEL__SWAP_LOAD_MARKET:
			case AnalyticsPackage.SWAP_VALUE_MATRIX_MODEL__SWAP_DISCHARGE_MARKET:
			case AnalyticsPackage.SWAP_VALUE_MATRIX_MODEL__SWAP_VALUE_MATRIX_RESULT:
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
				(AnalyticsPackage.Literals.SWAP_VALUE_MATRIX_MODEL__BASE_LOAD,
				 AnalyticsFactory.eINSTANCE.createBuyReference()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.SWAP_VALUE_MATRIX_MODEL__BASE_DISCHARGE,
				 AnalyticsFactory.eINSTANCE.createSellReference()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.SWAP_VALUE_MATRIX_MODEL__BASE_VESSEL_CHARTER,
				 AnalyticsFactory.eINSTANCE.createExistingVesselCharterOption()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.SWAP_VALUE_MATRIX_MODEL__SWAP_LOAD_MARKET,
				 AnalyticsFactory.eINSTANCE.createBuyMarket()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.SWAP_VALUE_MATRIX_MODEL__SWAP_DISCHARGE_MARKET,
				 AnalyticsFactory.eINSTANCE.createSellMarket()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.SWAP_VALUE_MATRIX_MODEL__SWAP_VALUE_MATRIX_RESULT,
				 AnalyticsFactory.eINSTANCE.createSwapValueMatrixResultSet()));
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
			childFeature == AnalyticsPackage.Literals.ABSTRACT_ANALYSIS_MODEL__BUYS ||
			childFeature == AnalyticsPackage.Literals.SWAP_VALUE_MATRIX_MODEL__SWAP_LOAD_MARKET ||
			childFeature == AnalyticsPackage.Literals.SWAP_VALUE_MATRIX_MODEL__BASE_LOAD ||
			childFeature == AnalyticsPackage.Literals.ABSTRACT_ANALYSIS_MODEL__SELLS ||
			childFeature == AnalyticsPackage.Literals.SWAP_VALUE_MATRIX_MODEL__SWAP_DISCHARGE_MARKET ||
			childFeature == AnalyticsPackage.Literals.SWAP_VALUE_MATRIX_MODEL__BASE_DISCHARGE ||
			childFeature == AnalyticsPackage.Literals.ABSTRACT_ANALYSIS_MODEL__SHIPPING_TEMPLATES ||
			childFeature == AnalyticsPackage.Literals.SWAP_VALUE_MATRIX_MODEL__BASE_VESSEL_CHARTER;

		if (qualify) {
			return getString
				("_UI_CreateChild_text2",
				 new Object[] { getTypeText(childObject), getFeatureText(childFeature), getTypeText(owner) });
		}
		return super.getCreateChildText(owner, feature, child, selection);
	}

}
