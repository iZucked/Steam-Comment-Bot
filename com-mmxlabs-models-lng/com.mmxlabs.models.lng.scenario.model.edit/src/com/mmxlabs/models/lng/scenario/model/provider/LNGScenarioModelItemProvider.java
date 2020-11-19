/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2020
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
import com.mmxlabs.models.lng.adp.ADPFactory;
import com.mmxlabs.models.lng.analytics.AnalyticsFactory;
import com.mmxlabs.models.lng.cargo.CargoFactory;
import com.mmxlabs.models.lng.nominations.NominationsFactory;
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
			addSchedulingEndDatePropertyDescriptor(object);
			addLongTermPropertyDescriptor(object);
			addAnonymisedPropertyDescriptor(object);
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
	 * This adds a property descriptor for the Scheduling End Date feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addSchedulingEndDatePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_LNGScenarioModel_schedulingEndDate_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_LNGScenarioModel_schedulingEndDate_feature", "_UI_LNGScenarioModel_type"),
				 LNGScenarioPackage.eINSTANCE.getLNGScenarioModel_SchedulingEndDate(),
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Long Term feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addLongTermPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_LNGScenarioModel_longTerm_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_LNGScenarioModel_longTerm_feature", "_UI_LNGScenarioModel_type"),
				 LNGScenarioPackage.eINSTANCE.getLNGScenarioModel_LongTerm(),
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Anonymised feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addAnonymisedPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_LNGScenarioModel_anonymised_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_LNGScenarioModel_anonymised_feature", "_UI_LNGScenarioModel_type"),
				 LNGScenarioPackage.eINSTANCE.getLNGScenarioModel_Anonymised(),
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
			childrenFeatures.add(LNGScenarioPackage.eINSTANCE.getLNGScenarioModel_CargoModel());
			childrenFeatures.add(LNGScenarioPackage.eINSTANCE.getLNGScenarioModel_ScheduleModel());
			childrenFeatures.add(LNGScenarioPackage.eINSTANCE.getLNGScenarioModel_ActualsModel());
			childrenFeatures.add(LNGScenarioPackage.eINSTANCE.getLNGScenarioModel_ReferenceModel());
			childrenFeatures.add(LNGScenarioPackage.eINSTANCE.getLNGScenarioModel_UserSettings());
			childrenFeatures.add(LNGScenarioPackage.eINSTANCE.getLNGScenarioModel_AnalyticsModel());
			childrenFeatures.add(LNGScenarioPackage.eINSTANCE.getLNGScenarioModel_AdpModel());
			childrenFeatures.add(LNGScenarioPackage.eINSTANCE.getLNGScenarioModel_AdpModels());
			childrenFeatures.add(LNGScenarioPackage.eINSTANCE.getLNGScenarioModel_NominationsModel());
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
			case LNGScenarioPackage.LNG_SCENARIO_MODEL__SCHEDULING_END_DATE:
			case LNGScenarioPackage.LNG_SCENARIO_MODEL__LONG_TERM:
			case LNGScenarioPackage.LNG_SCENARIO_MODEL__ANONYMISED:
				fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), false, true));
				return;
			case LNGScenarioPackage.LNG_SCENARIO_MODEL__CARGO_MODEL:
			case LNGScenarioPackage.LNG_SCENARIO_MODEL__SCHEDULE_MODEL:
			case LNGScenarioPackage.LNG_SCENARIO_MODEL__ACTUALS_MODEL:
			case LNGScenarioPackage.LNG_SCENARIO_MODEL__REFERENCE_MODEL:
			case LNGScenarioPackage.LNG_SCENARIO_MODEL__USER_SETTINGS:
			case LNGScenarioPackage.LNG_SCENARIO_MODEL__ANALYTICS_MODEL:
			case LNGScenarioPackage.LNG_SCENARIO_MODEL__ADP_MODEL:
			case LNGScenarioPackage.LNG_SCENARIO_MODEL__ADP_MODELS:
			case LNGScenarioPackage.LNG_SCENARIO_MODEL__NOMINATIONS_MODEL:
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
				(LNGScenarioPackage.eINSTANCE.getLNGScenarioModel_AnalyticsModel(),
				 AnalyticsFactory.eINSTANCE.createAnalyticsModel()));

		newChildDescriptors.add
			(createChildParameter
				(LNGScenarioPackage.eINSTANCE.getLNGScenarioModel_AdpModel(),
				 ADPFactory.eINSTANCE.createADPModel()));

		newChildDescriptors.add
			(createChildParameter
				(LNGScenarioPackage.eINSTANCE.getLNGScenarioModel_AdpModels(),
				 ADPFactory.eINSTANCE.createADPModel()));

		newChildDescriptors.add
			(createChildParameter
				(LNGScenarioPackage.eINSTANCE.getLNGScenarioModel_NominationsModel(),
				 NominationsFactory.eINSTANCE.createNominationsModel()));
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
			childFeature == LNGScenarioPackage.eINSTANCE.getLNGScenarioModel_AdpModel() ||
			childFeature == LNGScenarioPackage.eINSTANCE.getLNGScenarioModel_AdpModels();

		if (qualify) {
			return getString
				("_UI_CreateChild_text2",
				 new Object[] { getTypeText(childObject), getFeatureText(childFeature), getTypeText(owner) });
		}
		return super.getCreateChildText(owner, feature, child, selection);
	}

}
