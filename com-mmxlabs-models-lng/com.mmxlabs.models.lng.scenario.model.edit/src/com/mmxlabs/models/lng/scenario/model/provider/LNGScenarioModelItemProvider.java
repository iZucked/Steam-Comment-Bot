/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.scenario.model.provider;


import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ViewerNotification;

import com.mmxlabs.models.lng.actuals.ActualsFactory;
import com.mmxlabs.models.lng.analytics.AnalyticsFactory;
import com.mmxlabs.models.lng.cargo.CargoFactory;
import com.mmxlabs.models.lng.parameters.ParametersFactory;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioFactory;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioPackage;
import com.mmxlabs.models.lng.schedule.ScheduleFactory;
import com.mmxlabs.models.mmxcore.provider.MMXRootObjectItemProvider;

/**
 * This is the item provider adapter for a {@link com.mmxlabs.models.lng.scenario.model.LNGScenarioModel} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class LNGScenarioModelItemProvider
	extends MMXRootObjectItemProvider {
	/**
	 * This constructs an instance from a factory and a notifier.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public LNGScenarioModelItemProvider(AdapterFactory adapterFactory) {
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

			addPromptPeriodStartPropertyDescriptor(object);
			addPromptPeriodEndPropertyDescriptor(object);
		}
		return itemPropertyDescriptors;
	}

	/**
	 * This adds a property descriptor for the Prompt Period Start feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addPromptPeriodStartPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_LNGScenarioModel_promptPeriodStart_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_LNGScenarioModel_promptPeriodStart_feature", "_UI_LNGScenarioModel_type"),
				 LNGScenarioPackage.eINSTANCE.getLNGScenarioModel_PromptPeriodStart(),
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Prompt Period End feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addPromptPeriodEndPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_LNGScenarioModel_promptPeriodEnd_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_LNGScenarioModel_promptPeriodEnd_feature", "_UI_LNGScenarioModel_type"),
				 LNGScenarioPackage.eINSTANCE.getLNGScenarioModel_PromptPeriodEnd(),
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
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
			childrenFeatures.add(LNGScenarioPackage.eINSTANCE.getLNGScenarioModel_CargoModel());
			childrenFeatures.add(LNGScenarioPackage.eINSTANCE.getLNGScenarioModel_ScheduleModel());
			childrenFeatures.add(LNGScenarioPackage.eINSTANCE.getLNGScenarioModel_ActualsModel());
			childrenFeatures.add(LNGScenarioPackage.eINSTANCE.getLNGScenarioModel_ReferenceModel());
			childrenFeatures.add(LNGScenarioPackage.eINSTANCE.getLNGScenarioModel_UserSettings());
			childrenFeatures.add(LNGScenarioPackage.eINSTANCE.getLNGScenarioModel_OptionModels());
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
	 * This returns LNGScenarioModel.gif.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object getImage(Object object) {
		return overlayImage(object, getResourceLocator().getImage("full/obj16/LNGScenarioModel"));
	}

	/**
	 * This returns the label text for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getText(Object object) {
		String label = ((LNGScenarioModel)object).getUuid();
		return label == null || label.length() == 0 ?
			getString("_UI_LNGScenarioModel_type") :
			getString("_UI_LNGScenarioModel_type") + " " + label;
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

		switch (notification.getFeatureID(LNGScenarioModel.class)) {
			case LNGScenarioPackage.LNG_SCENARIO_MODEL__PROMPT_PERIOD_START:
			case LNGScenarioPackage.LNG_SCENARIO_MODEL__PROMPT_PERIOD_END:
				fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), false, true));
				return;
			case LNGScenarioPackage.LNG_SCENARIO_MODEL__CARGO_MODEL:
			case LNGScenarioPackage.LNG_SCENARIO_MODEL__SCHEDULE_MODEL:
			case LNGScenarioPackage.LNG_SCENARIO_MODEL__ACTUALS_MODEL:
			case LNGScenarioPackage.LNG_SCENARIO_MODEL__REFERENCE_MODEL:
			case LNGScenarioPackage.LNG_SCENARIO_MODEL__USER_SETTINGS:
			case LNGScenarioPackage.LNG_SCENARIO_MODEL__OPTION_MODELS:
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
				(LNGScenarioPackage.eINSTANCE.getLNGScenarioModel_CargoModel(),
				 CargoFactory.eINSTANCE.createCargoModel()));

		newChildDescriptors.add
			(createChildParameter
				(LNGScenarioPackage.eINSTANCE.getLNGScenarioModel_ScheduleModel(),
				 ScheduleFactory.eINSTANCE.createScheduleModel()));

		newChildDescriptors.add
			(createChildParameter
				(LNGScenarioPackage.eINSTANCE.getLNGScenarioModel_ActualsModel(),
				 ActualsFactory.eINSTANCE.createActualsModel()));

		newChildDescriptors.add
			(createChildParameter
				(LNGScenarioPackage.eINSTANCE.getLNGScenarioModel_ReferenceModel(),
				 LNGScenarioFactory.eINSTANCE.createLNGReferenceModel()));

		newChildDescriptors.add
			(createChildParameter
				(LNGScenarioPackage.eINSTANCE.getLNGScenarioModel_UserSettings(),
				 ParametersFactory.eINSTANCE.createUserSettings()));

		newChildDescriptors.add
			(createChildParameter
				(LNGScenarioPackage.eINSTANCE.getLNGScenarioModel_OptionModels(),
				 AnalyticsFactory.eINSTANCE.createOptionAnalysisModel()));
	}

}
