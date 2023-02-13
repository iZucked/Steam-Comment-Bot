/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.analytics.provider;


import com.mmxlabs.models.lng.analytics.AbstractSensitivityResult;
import com.mmxlabs.models.lng.analytics.AnalyticsPackage;

import com.mmxlabs.models.mmxcore.provider.UUIDObjectItemProvider;

import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ViewerNotification;

/**
 * This is the item provider adapter for a {@link com.mmxlabs.models.lng.analytics.AbstractSensitivityResult} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class AbstractSensitivityResultItemProvider extends UUIDObjectItemProvider {
	/**
	 * This constructs an instance from a factory and a notifier.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AbstractSensitivityResultItemProvider(AdapterFactory adapterFactory) {
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

			addMinPnLPropertyDescriptor(object);
			addMaxPnLPropertyDescriptor(object);
			addAveragePnLPropertyDescriptor(object);
			addVariancePropertyDescriptor(object);
		}
		return itemPropertyDescriptors;
	}

	/**
	 * This adds a property descriptor for the Min Pn L feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addMinPnLPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_AbstractSensitivityResult_minPnL_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_AbstractSensitivityResult_minPnL_feature", "_UI_AbstractSensitivityResult_type"),
				 AnalyticsPackage.Literals.ABSTRACT_SENSITIVITY_RESULT__MIN_PN_L,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Max Pn L feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addMaxPnLPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_AbstractSensitivityResult_maxPnL_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_AbstractSensitivityResult_maxPnL_feature", "_UI_AbstractSensitivityResult_type"),
				 AnalyticsPackage.Literals.ABSTRACT_SENSITIVITY_RESULT__MAX_PN_L,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Average Pn L feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addAveragePnLPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_AbstractSensitivityResult_averagePnL_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_AbstractSensitivityResult_averagePnL_feature", "_UI_AbstractSensitivityResult_type"),
				 AnalyticsPackage.Literals.ABSTRACT_SENSITIVITY_RESULT__AVERAGE_PN_L,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Variance feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addVariancePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_AbstractSensitivityResult_variance_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_AbstractSensitivityResult_variance_feature", "_UI_AbstractSensitivityResult_type"),
				 AnalyticsPackage.Literals.ABSTRACT_SENSITIVITY_RESULT__VARIANCE,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.REAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This returns the label text for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getText(Object object) {
		String label = ((AbstractSensitivityResult)object).getUuid();
		return label == null || label.length() == 0 ?
			getString("_UI_AbstractSensitivityResult_type") :
			getString("_UI_AbstractSensitivityResult_type") + " " + label;
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

		switch (notification.getFeatureID(AbstractSensitivityResult.class)) {
			case AnalyticsPackage.ABSTRACT_SENSITIVITY_RESULT__MIN_PN_L:
			case AnalyticsPackage.ABSTRACT_SENSITIVITY_RESULT__MAX_PN_L:
			case AnalyticsPackage.ABSTRACT_SENSITIVITY_RESULT__AVERAGE_PN_L:
			case AnalyticsPackage.ABSTRACT_SENSITIVITY_RESULT__VARIANCE:
				fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), false, true));
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
	}

}
