/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.parameters.provider;


import com.mmxlabs.models.lng.parameters.OptimisationMode;
import com.mmxlabs.models.lng.parameters.ParametersPackage;
import com.mmxlabs.models.lng.parameters.UserSettings;

import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.util.ResourceLocator;

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
 * This is the item provider adapter for a {@link com.mmxlabs.models.lng.parameters.UserSettings} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class UserSettingsItemProvider 
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
	public UserSettingsItemProvider(AdapterFactory adapterFactory) {
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

			addModePropertyDescriptor(object);
			addNominalOnlyPropertyDescriptor(object);
			addBuildActionSetsPropertyDescriptor(object);
			addPeriodStartDatePropertyDescriptor(object);
			addPeriodEndPropertyDescriptor(object);
			addDualModePropertyDescriptor(object);
			addSimilarityModePropertyDescriptor(object);
			addShippingOnlyPropertyDescriptor(object);
			addGenerateCharterOutsPropertyDescriptor(object);
			addWithCharterLengthPropertyDescriptor(object);
			addCharterLengthDaysPropertyDescriptor(object);
			addWithSpotCargoMarketsPropertyDescriptor(object);
			addFloatingDaysLimitPropertyDescriptor(object);
			addCleanSlateOptimisationPropertyDescriptor(object);
			addGeneratedPapersInPNLPropertyDescriptor(object);
		}
		return itemPropertyDescriptors;
	}

	/**
	 * This adds a property descriptor for the Mode feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addModePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_UserSettings_mode_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_UserSettings_mode_feature", "_UI_UserSettings_type"),
				 ParametersPackage.Literals.USER_SETTINGS__MODE,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Nominal Only feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addNominalOnlyPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_UserSettings_nominalOnly_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_UserSettings_nominalOnly_feature", "_UI_UserSettings_type"),
				 ParametersPackage.Literals.USER_SETTINGS__NOMINAL_ONLY,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Period Start Date feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addPeriodStartDatePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_UserSettings_periodStartDate_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_UserSettings_periodStartDate_feature", "_UI_UserSettings_type"),
				 ParametersPackage.Literals.USER_SETTINGS__PERIOD_START_DATE,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Period End feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addPeriodEndPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_UserSettings_periodEnd_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_UserSettings_periodEnd_feature", "_UI_UserSettings_type"),
				 ParametersPackage.Literals.USER_SETTINGS__PERIOD_END,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Shipping Only feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addShippingOnlyPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_UserSettings_shippingOnly_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_UserSettings_shippingOnly_feature", "_UI_UserSettings_type"),
				 ParametersPackage.Literals.USER_SETTINGS__SHIPPING_ONLY,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Generate Charter Outs feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addGenerateCharterOutsPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_UserSettings_generateCharterOuts_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_UserSettings_generateCharterOuts_feature", "_UI_UserSettings_type"),
				 ParametersPackage.Literals.USER_SETTINGS__GENERATE_CHARTER_OUTS,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the With Charter Length feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addWithCharterLengthPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_UserSettings_withCharterLength_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_UserSettings_withCharterLength_feature", "_UI_UserSettings_type"),
				 ParametersPackage.Literals.USER_SETTINGS__WITH_CHARTER_LENGTH,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Charter Length Days feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addCharterLengthDaysPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_UserSettings_charterLengthDays_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_UserSettings_charterLengthDays_feature", "_UI_UserSettings_type"),
				 ParametersPackage.Literals.USER_SETTINGS__CHARTER_LENGTH_DAYS,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the With Spot Cargo Markets feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addWithSpotCargoMarketsPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_UserSettings_withSpotCargoMarkets_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_UserSettings_withSpotCargoMarkets_feature", "_UI_UserSettings_type"),
				 ParametersPackage.Literals.USER_SETTINGS__WITH_SPOT_CARGO_MARKETS,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Build Action Sets feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addBuildActionSetsPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_UserSettings_buildActionSets_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_UserSettings_buildActionSets_feature", "_UI_UserSettings_type"),
				 ParametersPackage.Literals.USER_SETTINGS__BUILD_ACTION_SETS,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Floating Days Limit feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addFloatingDaysLimitPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_UserSettings_floatingDaysLimit_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_UserSettings_floatingDaysLimit_feature", "_UI_UserSettings_type"),
				 ParametersPackage.Literals.USER_SETTINGS__FLOATING_DAYS_LIMIT,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Clean Slate Optimisation feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addCleanSlateOptimisationPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_UserSettings_cleanSlateOptimisation_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_UserSettings_cleanSlateOptimisation_feature", "_UI_UserSettings_type"),
				 ParametersPackage.Literals.USER_SETTINGS__CLEAN_SLATE_OPTIMISATION,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Generated Papers In PNL feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addGeneratedPapersInPNLPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_UserSettings_generatedPapersInPNL_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_UserSettings_generatedPapersInPNL_feature", "_UI_UserSettings_type"),
				 ParametersPackage.Literals.USER_SETTINGS__GENERATED_PAPERS_IN_PNL,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Dual Mode feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addDualModePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_UserSettings_dualMode_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_UserSettings_dualMode_feature", "_UI_UserSettings_type"),
				 ParametersPackage.Literals.USER_SETTINGS__DUAL_MODE,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Similarity Mode feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addSimilarityModePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_UserSettings_similarityMode_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_UserSettings_similarityMode_feature", "_UI_UserSettings_type"),
				 ParametersPackage.Literals.USER_SETTINGS__SIMILARITY_MODE,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This returns UserSettings.gif.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object getImage(Object object) {
		return overlayImage(object, getResourceLocator().getImage("full/obj16/UserSettings"));
	}

	/**
	 * This returns the label text for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getText(Object object) {
		OptimisationMode labelValue = ((UserSettings)object).getMode();
		String label = labelValue == null ? null : labelValue.toString();
		return label == null || label.length() == 0 ?
			getString("_UI_UserSettings_type") :
			getString("_UI_UserSettings_type") + " " + label;
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

		switch (notification.getFeatureID(UserSettings.class)) {
			case ParametersPackage.USER_SETTINGS__MODE:
			case ParametersPackage.USER_SETTINGS__NOMINAL_ONLY:
			case ParametersPackage.USER_SETTINGS__BUILD_ACTION_SETS:
			case ParametersPackage.USER_SETTINGS__PERIOD_START_DATE:
			case ParametersPackage.USER_SETTINGS__PERIOD_END:
			case ParametersPackage.USER_SETTINGS__DUAL_MODE:
			case ParametersPackage.USER_SETTINGS__SIMILARITY_MODE:
			case ParametersPackage.USER_SETTINGS__SHIPPING_ONLY:
			case ParametersPackage.USER_SETTINGS__GENERATE_CHARTER_OUTS:
			case ParametersPackage.USER_SETTINGS__WITH_CHARTER_LENGTH:
			case ParametersPackage.USER_SETTINGS__CHARTER_LENGTH_DAYS:
			case ParametersPackage.USER_SETTINGS__WITH_SPOT_CARGO_MARKETS:
			case ParametersPackage.USER_SETTINGS__FLOATING_DAYS_LIMIT:
			case ParametersPackage.USER_SETTINGS__CLEAN_SLATE_OPTIMISATION:
			case ParametersPackage.USER_SETTINGS__GENERATED_PAPERS_IN_PNL:
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
