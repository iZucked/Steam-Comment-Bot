/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2018
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.parameters.provider;


import com.mmxlabs.models.lng.parameters.ParallelOptimisationStage;
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
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ItemProviderAdapter;
import org.eclipse.emf.edit.provider.ViewerNotification;

/**
 * This is the item provider adapter for a {@link com.mmxlabs.models.lng.parameters.ParallelOptimisationStage} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class ParallelOptimisationStageItemProvider 
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
	public ParallelOptimisationStageItemProvider(AdapterFactory adapterFactory) {
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
			addJobCountPropertyDescriptor(object);
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
				 getString("_UI_OptimisationStage_name_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_OptimisationStage_name_feature", "_UI_OptimisationStage_type"),
				 ParametersPackage.Literals.OPTIMISATION_STAGE__NAME,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Job Count feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addJobCountPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_ParallelOptimisationStage_jobCount_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_ParallelOptimisationStage_jobCount_feature", "_UI_ParallelOptimisationStage_type"),
				 ParametersPackage.Literals.PARALLEL_OPTIMISATION_STAGE__JOB_COUNT,
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
			childrenFeatures.add(ParametersPackage.Literals.PARALLEL_OPTIMISATION_STAGE__TEMPLATE);
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
	 * This returns ParallelOptimisationStage.gif.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object getImage(Object object) {
		return overlayImage(object, getResourceLocator().getImage("full/obj16/ParallelOptimisationStage"));
	}

	/**
	 * This returns the label text for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getText(Object object) {
		String label = ((ParallelOptimisationStage<?>)object).getName();
		return label == null || label.length() == 0 ?
			getString("_UI_ParallelOptimisationStage_type") :
			getString("_UI_ParallelOptimisationStage_type") + " " + label;
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

		switch (notification.getFeatureID(ParallelOptimisationStage.class)) {
			case ParametersPackage.PARALLEL_OPTIMISATION_STAGE__NAME:
			case ParametersPackage.PARALLEL_OPTIMISATION_STAGE__JOB_COUNT:
				fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), false, true));
				return;
			case ParametersPackage.PARALLEL_OPTIMISATION_STAGE__TEMPLATE:
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
				(ParametersPackage.Literals.PARALLEL_OPTIMISATION_STAGE__TEMPLATE,
				 ParametersFactory.eINSTANCE.createParallisableOptimisationStage()));

		newChildDescriptors.add
			(createChildParameter
				(ParametersPackage.Literals.PARALLEL_OPTIMISATION_STAGE__TEMPLATE,
				 ParametersFactory.eINSTANCE.createCleanStateOptimisationStage()));

		newChildDescriptors.add
			(createChildParameter
				(ParametersPackage.Literals.PARALLEL_OPTIMISATION_STAGE__TEMPLATE,
				 ParametersFactory.eINSTANCE.createLocalSearchOptimisationStage()));

		newChildDescriptors.add
			(createChildParameter
				(ParametersPackage.Literals.PARALLEL_OPTIMISATION_STAGE__TEMPLATE,
				 ParametersFactory.eINSTANCE.createHillClimbOptimisationStage()));

		newChildDescriptors.add
			(createChildParameter
				(ParametersPackage.Literals.PARALLEL_OPTIMISATION_STAGE__TEMPLATE,
				 ParametersFactory.eINSTANCE.createMultipleSolutionSimilarityOptimisationStage()));

		newChildDescriptors.add
			(createChildParameter
				(ParametersPackage.Literals.PARALLEL_OPTIMISATION_STAGE__TEMPLATE,
				 ParametersFactory.eINSTANCE.createParallelMultipleSolutionSimilarityOptimisationStage()));

		newChildDescriptors.add
			(createChildParameter
				(ParametersPackage.Literals.PARALLEL_OPTIMISATION_STAGE__TEMPLATE,
				 ParametersFactory.eINSTANCE.createParallelHillClimbOptimisationStage()));

		newChildDescriptors.add
			(createChildParameter
				(ParametersPackage.Literals.PARALLEL_OPTIMISATION_STAGE__TEMPLATE,
				 ParametersFactory.eINSTANCE.createParallelLocalSearchOptimisationStage()));

		newChildDescriptors.add
			(createChildParameter
				(ParametersPackage.Literals.PARALLEL_OPTIMISATION_STAGE__TEMPLATE,
				 ParametersFactory.eINSTANCE.createMultiobjectiveSimilarityOptimisationStage()));

		newChildDescriptors.add
			(createChildParameter
				(ParametersPackage.Literals.PARALLEL_OPTIMISATION_STAGE__TEMPLATE,
				 ParametersFactory.eINSTANCE.createParallelMultiobjectiveSimilarityOptimisationStage()));
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
