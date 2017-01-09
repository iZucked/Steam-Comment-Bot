/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.parameters.provider;


import com.mmxlabs.models.lng.parameters.OptimisationPlan;
import com.mmxlabs.models.lng.parameters.ParametersFactory;
import com.mmxlabs.models.lng.parameters.ParametersPackage;

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
import org.eclipse.emf.edit.provider.ItemProviderAdapter;
import org.eclipse.emf.edit.provider.ViewerNotification;

/**
 * This is the item provider adapter for a {@link com.mmxlabs.models.lng.parameters.OptimisationPlan} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class OptimisationPlanItemProvider 
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
	public OptimisationPlanItemProvider(AdapterFactory adapterFactory) {
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

			addUserSettingsPropertyDescriptor(object);
		}
		return itemPropertyDescriptors;
	}

	/**
	 * This adds a property descriptor for the User Settings feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addUserSettingsPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_OptimisationPlan_userSettings_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_OptimisationPlan_userSettings_feature", "_UI_OptimisationPlan_type"),
				 ParametersPackage.Literals.OPTIMISATION_PLAN__USER_SETTINGS,
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
			childrenFeatures.add(ParametersPackage.Literals.OPTIMISATION_PLAN__STAGES);
			childrenFeatures.add(ParametersPackage.Literals.OPTIMISATION_PLAN__SOLUTION_BUILDER_SETTINGS);
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
	 * This returns OptimisationPlan.gif.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object getImage(Object object) {
		return overlayImage(object, getResourceLocator().getImage("full/obj16/OptimisationPlan"));
	}

	/**
	 * This returns the label text for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getText(Object object) {
		return getString("_UI_OptimisationPlan_type");
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

		switch (notification.getFeatureID(OptimisationPlan.class)) {
			case ParametersPackage.OPTIMISATION_PLAN__STAGES:
			case ParametersPackage.OPTIMISATION_PLAN__SOLUTION_BUILDER_SETTINGS:
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
				(ParametersPackage.Literals.OPTIMISATION_PLAN__STAGES,
				 ParametersFactory.eINSTANCE.createParallisableOptimisationStage()));

		newChildDescriptors.add
			(createChildParameter
				(ParametersPackage.Literals.OPTIMISATION_PLAN__STAGES,
				 ParametersFactory.eINSTANCE.createParallelOptimisationStage()));

		newChildDescriptors.add
			(createChildParameter
				(ParametersPackage.Literals.OPTIMISATION_PLAN__STAGES,
				 ParametersFactory.eINSTANCE.createCleanStateOptimisationStage()));

		newChildDescriptors.add
			(createChildParameter
				(ParametersPackage.Literals.OPTIMISATION_PLAN__STAGES,
				 ParametersFactory.eINSTANCE.createLocalSearchOptimisationStage()));

		newChildDescriptors.add
			(createChildParameter
				(ParametersPackage.Literals.OPTIMISATION_PLAN__STAGES,
				 ParametersFactory.eINSTANCE.createHillClimbOptimisationStage()));

		newChildDescriptors.add
			(createChildParameter
				(ParametersPackage.Literals.OPTIMISATION_PLAN__STAGES,
				 ParametersFactory.eINSTANCE.createActionPlanOptimisationStage()));

		newChildDescriptors.add
			(createChildParameter
				(ParametersPackage.Literals.OPTIMISATION_PLAN__STAGES,
				 ParametersFactory.eINSTANCE.createResetInitialSequencesStage()));

		newChildDescriptors.add
			(createChildParameter
				(ParametersPackage.Literals.OPTIMISATION_PLAN__STAGES,
				 ParametersFactory.eINSTANCE.createBreakEvenOptimisationStage()));

		newChildDescriptors.add
			(createChildParameter
				(ParametersPackage.Literals.OPTIMISATION_PLAN__SOLUTION_BUILDER_SETTINGS,
				 ParametersFactory.eINSTANCE.createSolutionBuilderSettings()));
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
