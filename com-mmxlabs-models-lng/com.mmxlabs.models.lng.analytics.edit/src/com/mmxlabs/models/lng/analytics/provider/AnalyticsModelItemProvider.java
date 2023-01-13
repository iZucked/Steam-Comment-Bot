/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.provider;


import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ViewerNotification;

import com.mmxlabs.models.lng.analytics.AnalyticsFactory;
import com.mmxlabs.models.lng.analytics.AnalyticsModel;
import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.mmxcore.provider.UUIDObjectItemProvider;

/**
 * This is the item provider adapter for a {@link com.mmxlabs.models.lng.analytics.AnalyticsModel} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class AnalyticsModelItemProvider
	extends UUIDObjectItemProvider {
	/**
	 * This constructs an instance from a factory and a notifier.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AnalyticsModelItemProvider(AdapterFactory adapterFactory) {
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

			addViabilityModelPropertyDescriptor(object);
		}
		return itemPropertyDescriptors;
	}

	/**
	 * This adds a property descriptor for the Viability Model feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addViabilityModelPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_AnalyticsModel_viabilityModel_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_AnalyticsModel_viabilityModel_feature", "_UI_AnalyticsModel_type"),
				 AnalyticsPackage.Literals.ANALYTICS_MODEL__VIABILITY_MODEL,
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
			childrenFeatures.add(AnalyticsPackage.Literals.ANALYTICS_MODEL__OPTION_MODELS);
			childrenFeatures.add(AnalyticsPackage.Literals.ANALYTICS_MODEL__OPTIMISATIONS);
			childrenFeatures.add(AnalyticsPackage.Literals.ANALYTICS_MODEL__VIABILITY_MODEL);
			childrenFeatures.add(AnalyticsPackage.Literals.ANALYTICS_MODEL__MTM_MODEL);
			childrenFeatures.add(AnalyticsPackage.Literals.ANALYTICS_MODEL__BREAKEVEN_MODELS);
			childrenFeatures.add(AnalyticsPackage.Literals.ANALYTICS_MODEL__SWAP_VALUE_MATRIX_MODELS);
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
	 * This returns AnalyticsModel.gif.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object getImage(Object object) {
		return overlayImage(object, getResourceLocator().getImage("full/obj16/AnalyticsModel"));
	}

	/**
	 * This returns the label text for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getText(Object object) {
		String label = ((AnalyticsModel)object).getUuid();
		return label == null || label.length() == 0 ?
			getString("_UI_AnalyticsModel_type") :
			getString("_UI_AnalyticsModel_type") + " " + label;
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

		switch (notification.getFeatureID(AnalyticsModel.class)) {
			case AnalyticsPackage.ANALYTICS_MODEL__OPTION_MODELS:
			case AnalyticsPackage.ANALYTICS_MODEL__OPTIMISATIONS:
			case AnalyticsPackage.ANALYTICS_MODEL__VIABILITY_MODEL:
			case AnalyticsPackage.ANALYTICS_MODEL__MTM_MODEL:
			case AnalyticsPackage.ANALYTICS_MODEL__BREAKEVEN_MODELS:
			case AnalyticsPackage.ANALYTICS_MODEL__SWAP_VALUE_MATRIX_MODELS:
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
				(AnalyticsPackage.Literals.ANALYTICS_MODEL__OPTION_MODELS,
				 AnalyticsFactory.eINSTANCE.createOptionAnalysisModel()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.ANALYTICS_MODEL__OPTIMISATIONS,
				 AnalyticsFactory.eINSTANCE.createSandboxResult()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.ANALYTICS_MODEL__OPTIMISATIONS,
				 AnalyticsFactory.eINSTANCE.createActionableSetPlan()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.ANALYTICS_MODEL__OPTIMISATIONS,
				 AnalyticsFactory.eINSTANCE.createSlotInsertionOptions()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.ANALYTICS_MODEL__OPTIMISATIONS,
				 AnalyticsFactory.eINSTANCE.createOptimisationResult()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.ANALYTICS_MODEL__OPTIMISATIONS,
				 AnalyticsFactory.eINSTANCE.createSensitivitySolutionSet()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.ANALYTICS_MODEL__VIABILITY_MODEL,
				 AnalyticsFactory.eINSTANCE.createViabilityModel()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.ANALYTICS_MODEL__MTM_MODEL,
				 AnalyticsFactory.eINSTANCE.createMTMModel()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.ANALYTICS_MODEL__BREAKEVEN_MODELS,
				 AnalyticsFactory.eINSTANCE.createBreakEvenAnalysisModel()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.ANALYTICS_MODEL__SWAP_VALUE_MATRIX_MODELS,
				 AnalyticsFactory.eINSTANCE.createSwapValueMatrixModel()));
	}

}
