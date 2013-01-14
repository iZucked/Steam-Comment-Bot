/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.optimiser.provider;


import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.IStructuredItemContentProvider;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ViewerNotification;

import com.mmxlabs.models.lng.optimiser.OptimiserFactory;
import com.mmxlabs.models.lng.optimiser.OptimiserPackage;
import com.mmxlabs.models.lng.optimiser.OptimiserSettings;
import com.mmxlabs.models.lng.types.provider.AOptimisationSettingsItemProvider;

/**
 * This is the item provider adapter for a {@link com.mmxlabs.models.lng.optimiser.OptimiserSettings} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class OptimiserSettingsItemProvider
	extends AOptimisationSettingsItemProvider
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

			addSeedPropertyDescriptor(object);
			addRewirePropertyDescriptor(object);
		}
		return itemPropertyDescriptors;
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
				 OptimiserPackage.Literals.OPTIMISER_SETTINGS__SEED,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Rewire feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addRewirePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_OptimiserSettings_rewire_feature"),
				 getString("_UI_OptimiserSettings_rewire_description"),
				 OptimiserPackage.Literals.OPTIMISER_SETTINGS__REWIRE,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE,
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
			childrenFeatures.add(OptimiserPackage.Literals.OPTIMISER_SETTINGS__OBJECTIVES);
			childrenFeatures.add(OptimiserPackage.Literals.OPTIMISER_SETTINGS__CONSTRAINTS);
			childrenFeatures.add(OptimiserPackage.Literals.OPTIMISER_SETTINGS__RANGE);
			childrenFeatures.add(OptimiserPackage.Literals.OPTIMISER_SETTINGS__ANNEALING_SETTINGS);
			childrenFeatures.add(OptimiserPackage.Literals.OPTIMISER_SETTINGS__ARGUMENTS);
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
			case OptimiserPackage.OPTIMISER_SETTINGS__SEED:
			case OptimiserPackage.OPTIMISER_SETTINGS__REWIRE:
				fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), false, true));
				return;
			case OptimiserPackage.OPTIMISER_SETTINGS__OBJECTIVES:
			case OptimiserPackage.OPTIMISER_SETTINGS__CONSTRAINTS:
			case OptimiserPackage.OPTIMISER_SETTINGS__RANGE:
			case OptimiserPackage.OPTIMISER_SETTINGS__ANNEALING_SETTINGS:
			case OptimiserPackage.OPTIMISER_SETTINGS__ARGUMENTS:
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
				(OptimiserPackage.Literals.OPTIMISER_SETTINGS__OBJECTIVES,
				 OptimiserFactory.eINSTANCE.createObjective()));

		newChildDescriptors.add
			(createChildParameter
				(OptimiserPackage.Literals.OPTIMISER_SETTINGS__CONSTRAINTS,
				 OptimiserFactory.eINSTANCE.createConstraint()));

		newChildDescriptors.add
			(createChildParameter
				(OptimiserPackage.Literals.OPTIMISER_SETTINGS__RANGE,
				 OptimiserFactory.eINSTANCE.createOptimisationRange()));

		newChildDescriptors.add
			(createChildParameter
				(OptimiserPackage.Literals.OPTIMISER_SETTINGS__ANNEALING_SETTINGS,
				 OptimiserFactory.eINSTANCE.createAnnealingSettings()));

		newChildDescriptors.add
			(createChildParameter
				(OptimiserPackage.Literals.OPTIMISER_SETTINGS__ARGUMENTS,
				 OptimiserFactory.eINSTANCE.createArgument()));
	}

	/**
	 * Return the resource locator for this item provider's resources.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ResourceLocator getResourceLocator() {
		return OptimiserEditPlugin.INSTANCE;
	}

}
