/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.parameters.provider;


import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ViewerNotification;

import com.mmxlabs.models.lng.parameters.OptimiserSettings;
import com.mmxlabs.models.lng.parameters.ParametersFactory;
import com.mmxlabs.models.lng.parameters.ParametersPackage;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.mmxcore.provider.UUIDObjectItemProvider;

/**
 * This is the item provider adapter for a {@link com.mmxlabs.models.lng.parameters.OptimiserSettings} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class OptimiserSettingsItemProvider
	extends UUIDObjectItemProvider {
	/**
	 * This constructs an instance from a factory and a notifier.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public OptimiserSettingsItemProvider(AdapterFactory adapterFactory) {
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

			addNamePropertyDescriptor(object);
			addSeedPropertyDescriptor(object);
			addGenerateCharterOutsPropertyDescriptor(object);
			addShippingOnlyPropertyDescriptor(object);
			addBuildActionSetsPropertyDescriptor(object);
			addFloatingDaysLimitPropertyDescriptor(object);
		}
		return itemPropertyDescriptors;
	}

	/**
	 * This adds a property descriptor for the Name feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addNamePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_NamedObject_name_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_NamedObject_name_feature", "_UI_NamedObject_type"),
				 MMXCorePackage.Literals.NAMED_OBJECT__NAME,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Seed feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addSeedPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_OptimiserSettings_seed_feature"),
				 getString("_UI_OptimiserSettings_seed_description"),
				 ParametersPackage.Literals.OPTIMISER_SETTINGS__SEED,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
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
				 getString("_UI_OptimiserSettings_generateCharterOuts_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_OptimiserSettings_generateCharterOuts_feature", "_UI_OptimiserSettings_type"),
				 ParametersPackage.Literals.OPTIMISER_SETTINGS__GENERATE_CHARTER_OUTS,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE,
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
				 getString("_UI_OptimiserSettings_shippingOnly_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_OptimiserSettings_shippingOnly_feature", "_UI_OptimiserSettings_type"),
				 ParametersPackage.Literals.OPTIMISER_SETTINGS__SHIPPING_ONLY,
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
				 getString("_UI_OptimiserSettings_buildActionSets_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_OptimiserSettings_buildActionSets_feature", "_UI_OptimiserSettings_type"),
				 ParametersPackage.Literals.OPTIMISER_SETTINGS__BUILD_ACTION_SETS,
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
				 getString("_UI_OptimiserSettings_floatingDaysLimit_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_OptimiserSettings_floatingDaysLimit_feature", "_UI_OptimiserSettings_type"),
				 ParametersPackage.Literals.OPTIMISER_SETTINGS__FLOATING_DAYS_LIMIT,
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
			childrenFeatures.add(ParametersPackage.Literals.OPTIMISER_SETTINGS__OBJECTIVES);
			childrenFeatures.add(ParametersPackage.Literals.OPTIMISER_SETTINGS__CONSTRAINTS);
			childrenFeatures.add(ParametersPackage.Literals.OPTIMISER_SETTINGS__RANGE);
			childrenFeatures.add(ParametersPackage.Literals.OPTIMISER_SETTINGS__ANNEALING_SETTINGS);
			childrenFeatures.add(ParametersPackage.Literals.OPTIMISER_SETTINGS__ARGUMENTS);
			childrenFeatures.add(ParametersPackage.Literals.OPTIMISER_SETTINGS__SIMILARITY_SETTINGS);
			childrenFeatures.add(ParametersPackage.Literals.OPTIMISER_SETTINGS__SOLUTION_IMPROVEMENT_SETTINGS);
			childrenFeatures.add(ParametersPackage.Literals.OPTIMISER_SETTINGS__ACTION_PLAN_SETTINGS);
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
	 * This returns OptimiserSettings.gif.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object getImage(Object object) {
		return overlayImage(object, getResourceLocator().getImage("full/obj16/OptimiserSettings"));
	}

	/**
	 * This returns the label text for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getText(Object object) {
		String label = ((OptimiserSettings)object).getName();
		return label == null || label.length() == 0 ?
			getString("_UI_OptimiserSettings_type") :
			getString("_UI_OptimiserSettings_type") + " " + label;
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

		switch (notification.getFeatureID(OptimiserSettings.class)) {
			case ParametersPackage.OPTIMISER_SETTINGS__NAME:
			case ParametersPackage.OPTIMISER_SETTINGS__SEED:
			case ParametersPackage.OPTIMISER_SETTINGS__GENERATE_CHARTER_OUTS:
			case ParametersPackage.OPTIMISER_SETTINGS__SHIPPING_ONLY:
			case ParametersPackage.OPTIMISER_SETTINGS__BUILD_ACTION_SETS:
			case ParametersPackage.OPTIMISER_SETTINGS__FLOATING_DAYS_LIMIT:
				fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), false, true));
				return;
			case ParametersPackage.OPTIMISER_SETTINGS__OBJECTIVES:
			case ParametersPackage.OPTIMISER_SETTINGS__CONSTRAINTS:
			case ParametersPackage.OPTIMISER_SETTINGS__RANGE:
			case ParametersPackage.OPTIMISER_SETTINGS__ANNEALING_SETTINGS:
			case ParametersPackage.OPTIMISER_SETTINGS__ARGUMENTS:
			case ParametersPackage.OPTIMISER_SETTINGS__SIMILARITY_SETTINGS:
			case ParametersPackage.OPTIMISER_SETTINGS__SOLUTION_IMPROVEMENT_SETTINGS:
			case ParametersPackage.OPTIMISER_SETTINGS__ACTION_PLAN_SETTINGS:
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
				(ParametersPackage.Literals.OPTIMISER_SETTINGS__OBJECTIVES,
				 ParametersFactory.eINSTANCE.createObjective()));

		newChildDescriptors.add
			(createChildParameter
				(ParametersPackage.Literals.OPTIMISER_SETTINGS__CONSTRAINTS,
				 ParametersFactory.eINSTANCE.createConstraint()));

		newChildDescriptors.add
			(createChildParameter
				(ParametersPackage.Literals.OPTIMISER_SETTINGS__RANGE,
				 ParametersFactory.eINSTANCE.createOptimisationRange()));

		newChildDescriptors.add
			(createChildParameter
				(ParametersPackage.Literals.OPTIMISER_SETTINGS__ANNEALING_SETTINGS,
				 ParametersFactory.eINSTANCE.createAnnealingSettings()));

		newChildDescriptors.add
			(createChildParameter
				(ParametersPackage.Literals.OPTIMISER_SETTINGS__ARGUMENTS,
				 ParametersFactory.eINSTANCE.createArgument()));

		newChildDescriptors.add
			(createChildParameter
				(ParametersPackage.Literals.OPTIMISER_SETTINGS__SIMILARITY_SETTINGS,
				 ParametersFactory.eINSTANCE.createSimilaritySettings()));

		newChildDescriptors.add
			(createChildParameter
				(ParametersPackage.Literals.OPTIMISER_SETTINGS__SOLUTION_IMPROVEMENT_SETTINGS,
				 ParametersFactory.eINSTANCE.createIndividualSolutionImprovementSettings()));

		newChildDescriptors.add
			(createChildParameter
				(ParametersPackage.Literals.OPTIMISER_SETTINGS__ACTION_PLAN_SETTINGS,
				 ParametersFactory.eINSTANCE.createActionPlanSettings()));
	}

}
