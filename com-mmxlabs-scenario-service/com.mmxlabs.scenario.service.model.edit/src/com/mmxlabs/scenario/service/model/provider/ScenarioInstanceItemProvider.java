/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.model.provider;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ViewerNotification;

import com.mmxlabs.rcp.icons.lingo.CommonImages;
import com.mmxlabs.rcp.icons.lingo.CommonImages.IconPaths;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.ScenarioServiceFactory;
import com.mmxlabs.scenario.service.model.ScenarioServicePackage;

/**
 * This is the item provider adapter for a {@link com.mmxlabs.scenario.service.model.ScenarioInstance} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class ScenarioInstanceItemProvider extends ContainerItemProvider {
	/**
	 * This constructs an instance from a factory and a notifier.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ScenarioInstanceItemProvider(AdapterFactory adapterFactory) {
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

			addUuidPropertyDescriptor(object);
			addRootObjectURIPropertyDescriptor(object);
			addScenarioVersionPropertyDescriptor(object);
			addVersionContextPropertyDescriptor(object);
			addClientScenarioVersionPropertyDescriptor(object);
			addClientVersionContextPropertyDescriptor(object);
			addReadonlyPropertyDescriptor(object);
			addCloudLockedPropertyDescriptor(object);
			addValidationStatusCodePropertyDescriptor(object);
			addManifestPropertyDescriptor(object);
			addExternalIDPropertyDescriptor(object);
		}
		return itemPropertyDescriptors;
	}

	/**
	 * This adds a property descriptor for the Uuid feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addUuidPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(), getResourceLocator(),
				getString("_UI_ScenarioInstance_uuid_feature"), getString("_UI_PropertyDescriptor_description", "_UI_ScenarioInstance_uuid_feature", "_UI_ScenarioInstance_type"),
				ScenarioServicePackage.eINSTANCE.getScenarioInstance_Uuid(), true, false, false, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
	}

	/**
	 * This adds a property descriptor for the Root Object URI feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addRootObjectURIPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(), getResourceLocator(),
				getString("_UI_ScenarioInstance_rootObjectURI_feature"), getString("_UI_PropertyDescriptor_description", "_UI_ScenarioInstance_rootObjectURI_feature", "_UI_ScenarioInstance_type"),
				ScenarioServicePackage.eINSTANCE.getScenarioInstance_RootObjectURI(), true, false, false, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
	}

	/**
	 * This adds a property descriptor for the Scenario Version feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addScenarioVersionPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(), getResourceLocator(),
				getString("_UI_ScenarioInstance_scenarioVersion_feature"), getString("_UI_ScenarioInstance_scenarioVersion_description"),
				ScenarioServicePackage.eINSTANCE.getScenarioInstance_ScenarioVersion(), true, false, false, ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE, null, null));
	}

	/**
	 * This adds a property descriptor for the Version Context feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addVersionContextPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(), getResourceLocator(),
				getString("_UI_ScenarioInstance_versionContext_feature"), getString("_UI_ScenarioInstance_versionContext_description"),
				ScenarioServicePackage.eINSTANCE.getScenarioInstance_VersionContext(), true, false, false, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
	}

	/**
	 * This adds a property descriptor for the Readonly feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addReadonlyPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(), getResourceLocator(),
				getString("_UI_ScenarioInstance_readonly_feature"), getString("_UI_PropertyDescriptor_description", "_UI_ScenarioInstance_readonly_feature", "_UI_ScenarioInstance_type"),
				ScenarioServicePackage.eINSTANCE.getScenarioInstance_Readonly(), true, false, false, ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE, null, null));
	}

	/**
	 * This adds a property descriptor for the Cloud Locked feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addCloudLockedPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(), getResourceLocator(),
				getString("_UI_ScenarioInstance_cloudLocked_feature"), getString("_UI_PropertyDescriptor_description", "_UI_ScenarioInstance_cloudLocked_feature", "_UI_ScenarioInstance_type"),
				ScenarioServicePackage.eINSTANCE.getScenarioInstance_CloudLocked(), true, false, false, ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE, null, null));
	}

	/**
	 * This adds a property descriptor for the Validation Status Code feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addValidationStatusCodePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(), getResourceLocator(),
				getString("_UI_ScenarioInstance_validationStatusCode_feature"), getString("_UI_ScenarioInstance_validationStatusCode_description"),
				ScenarioServicePackage.eINSTANCE.getScenarioInstance_ValidationStatusCode(), false, false, false, ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE, null, null));
	}

	/**
	 * This adds a property descriptor for the Manifest feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addManifestPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(), getResourceLocator(),
				getString("_UI_ScenarioInstance_manifest_feature"), getString("_UI_PropertyDescriptor_description", "_UI_ScenarioInstance_manifest_feature", "_UI_ScenarioInstance_type"),
				ScenarioServicePackage.eINSTANCE.getScenarioInstance_Manifest(), true, false, true, null, null, null));
	}

	/**
	 * This adds a property descriptor for the External ID feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addExternalIDPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(), getResourceLocator(),
				getString("_UI_ScenarioInstance_externalID_feature"), getString("_UI_PropertyDescriptor_description", "_UI_ScenarioInstance_externalID_feature", "_UI_ScenarioInstance_type"),
				ScenarioServicePackage.eINSTANCE.getScenarioInstance_ExternalID(), true, false, false, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
	}

	/**
	 * This adds a property descriptor for the Client Scenario Version feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addClientScenarioVersionPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(), getResourceLocator(),
				getString("_UI_ScenarioInstance_clientScenarioVersion_feature"),
				getString("_UI_PropertyDescriptor_description", "_UI_ScenarioInstance_clientScenarioVersion_feature", "_UI_ScenarioInstance_type"),
				ScenarioServicePackage.eINSTANCE.getScenarioInstance_ClientScenarioVersion(), true, false, false, ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE, null, null));
	}

	/**
	 * This adds a property descriptor for the Client Version Context feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addClientVersionContextPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add(
				createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(), getResourceLocator(), getString("_UI_ScenarioInstance_clientVersionContext_feature"),
						getString("_UI_PropertyDescriptor_description", "_UI_ScenarioInstance_clientVersionContext_feature", "_UI_ScenarioInstance_type"),
						ScenarioServicePackage.eINSTANCE.getScenarioInstance_ClientVersionContext(), true, false, false, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
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
			childrenFeatures.add(ScenarioServicePackage.eINSTANCE.getScenarioInstance_Metadata());
			childrenFeatures.add(ScenarioServicePackage.eINSTANCE.getScenarioInstance_Fragments());
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
	 * This returns ScenarioInstance.png.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public Object getImage(Object object) {
		return overlayImage(object, CommonImages.getImageURL(IconPaths.Scenario));
	}

	/**
	 * This returns the label text for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public String getText(Object object) {
		String label = ((ScenarioInstance) object).getName();
		return label == null ? "(untitled)" : label;
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

		switch (notification.getFeatureID(ScenarioInstance.class)) {
		case ScenarioServicePackage.SCENARIO_INSTANCE__UUID:
		case ScenarioServicePackage.SCENARIO_INSTANCE__ROOT_OBJECT_URI:
		case ScenarioServicePackage.SCENARIO_INSTANCE__SCENARIO_VERSION:
		case ScenarioServicePackage.SCENARIO_INSTANCE__VERSION_CONTEXT:
		case ScenarioServicePackage.SCENARIO_INSTANCE__CLIENT_SCENARIO_VERSION:
		case ScenarioServicePackage.SCENARIO_INSTANCE__CLIENT_VERSION_CONTEXT:
		case ScenarioServicePackage.SCENARIO_INSTANCE__READONLY:
		case ScenarioServicePackage.SCENARIO_INSTANCE__CLOUD_LOCKED:
		case ScenarioServicePackage.SCENARIO_INSTANCE__VALIDATION_STATUS_CODE:
		case ScenarioServicePackage.SCENARIO_INSTANCE__EXTERNAL_ID:
			fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), false, true));
			return;
		case ScenarioServicePackage.SCENARIO_INSTANCE__METADATA:
		case ScenarioServicePackage.SCENARIO_INSTANCE__FRAGMENTS:
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

		newChildDescriptors.add(createChildParameter(ScenarioServicePackage.eINSTANCE.getScenarioInstance_Metadata(), ScenarioServiceFactory.eINSTANCE.createMetadata()));

		newChildDescriptors.add(createChildParameter(ScenarioServicePackage.eINSTANCE.getScenarioInstance_Fragments(), ScenarioServiceFactory.eINSTANCE.createScenarioFragment()));
	}

}
