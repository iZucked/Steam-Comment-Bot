/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.model.provider;

import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ViewerNotification;

import com.mmxlabs.scenario.service.model.ScenarioService;
import com.mmxlabs.scenario.service.model.ScenarioServicePackage;

/**
 * This is the item provider adapter for a {@link com.mmxlabs.scenario.service.model.ScenarioService} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class ScenarioServiceItemProvider extends ContainerItemProvider {
	/**
	 * This constructs an instance from a factory and a notifier.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ScenarioServiceItemProvider(AdapterFactory adapterFactory) {
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

			addDescriptionPropertyDescriptor(object);
			addServiceRefPropertyDescriptor(object);
			addSupportsForkingPropertyDescriptor(object);
			addSupportsImportPropertyDescriptor(object);
			addScenarioModelPropertyDescriptor(object);
			addLocalPropertyDescriptor(object);
			addServiceIDPropertyDescriptor(object);
			addOfflinePropertyDescriptor(object);
			addLockedByPropertyDescriptor(object);
			addLockedPropertyDescriptor(object);
			addImagePropertyDescriptor(object);
		}
		return itemPropertyDescriptors;
	}

	/**
	 * This adds a property descriptor for the Description feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addDescriptionPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(), getResourceLocator(),
				getString("_UI_ScenarioService_description_feature"), getString("_UI_PropertyDescriptor_description", "_UI_ScenarioService_description_feature", "_UI_ScenarioService_type"),
				ScenarioServicePackage.eINSTANCE.getScenarioService_Description(), true, false, false, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
	}

	/**
	 * This adds a property descriptor for the Service Ref feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addServiceRefPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(), getResourceLocator(),
				getString("_UI_ScenarioService_serviceRef_feature"), getString("_UI_PropertyDescriptor_description", "_UI_ScenarioService_serviceRef_feature", "_UI_ScenarioService_type"),
				ScenarioServicePackage.eINSTANCE.getScenarioService_ServiceRef(), true, false, false, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
	}

	/**
	 * This adds a property descriptor for the Supports Forking feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addSupportsForkingPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(), getResourceLocator(),
				getString("_UI_ScenarioService_supportsForking_feature"), getString("_UI_PropertyDescriptor_description", "_UI_ScenarioService_supportsForking_feature", "_UI_ScenarioService_type"),
				ScenarioServicePackage.eINSTANCE.getScenarioService_SupportsForking(), true, false, false, ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE, null, null));
	}

	/**
	 * This adds a property descriptor for the Supports Import feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addSupportsImportPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(), getResourceLocator(),
				getString("_UI_ScenarioService_supportsImport_feature"), getString("_UI_PropertyDescriptor_description", "_UI_ScenarioService_supportsImport_feature", "_UI_ScenarioService_type"),
				ScenarioServicePackage.eINSTANCE.getScenarioService_SupportsImport(), true, false, false, ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE, null, null));
	}

	/**
	 * This adds a property descriptor for the Scenario Model feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addScenarioModelPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(), getResourceLocator(),
				getString("_UI_ScenarioService_scenarioModel_feature"), getString("_UI_PropertyDescriptor_description", "_UI_ScenarioService_scenarioModel_feature", "_UI_ScenarioService_type"),
				ScenarioServicePackage.eINSTANCE.getScenarioService_ScenarioModel(), true, false, true, null, null, null));
	}

	/**
	 * This adds a property descriptor for the Local feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addLocalPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(), getResourceLocator(),
				getString("_UI_ScenarioService_local_feature"), getString("_UI_ScenarioService_local_description"), ScenarioServicePackage.eINSTANCE.getScenarioService_Local(), true, false, false,
				ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE, null, null));
	}

	/**
	 * This adds a property descriptor for the Service ID feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addServiceIDPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(), getResourceLocator(),
				getString("_UI_ScenarioService_serviceID_feature"), getString("_UI_PropertyDescriptor_description", "_UI_ScenarioService_serviceID_feature", "_UI_ScenarioService_type"),
				ScenarioServicePackage.eINSTANCE.getScenarioService_ServiceID(), true, false, false, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
	}

	/**
	 * This adds a property descriptor for the Offline feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addOfflinePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(), getResourceLocator(),
				getString("_UI_ScenarioService_offline_feature"), getString("_UI_PropertyDescriptor_description", "_UI_ScenarioService_offline_feature", "_UI_ScenarioService_type"),
				ScenarioServicePackage.eINSTANCE.getScenarioService_Offline(), true, false, false, ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE, null, null));
	}

	/**
	 * This adds a property descriptor for the Locked By feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addLockedByPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(), getResourceLocator(),
				getString("_UI_ScenarioService_lockedBy_feature"), getString("_UI_PropertyDescriptor_description", "_UI_ScenarioService_lockedBy_feature", "_UI_ScenarioService_type"),
				ScenarioServicePackage.eINSTANCE.getScenarioService_LockedBy(), true, false, false, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
	}

	/**
	 * This adds a property descriptor for the Locked feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addLockedPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(), getResourceLocator(),
				getString("_UI_ScenarioService_locked_feature"), getString("_UI_PropertyDescriptor_description", "_UI_ScenarioService_locked_feature", "_UI_ScenarioService_type"),
				ScenarioServicePackage.eINSTANCE.getScenarioService_Locked(), true, false, false, ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE, null, null));
	}

	/**
	 * This adds a property descriptor for the Image feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addImagePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(), getResourceLocator(),
				getString("_UI_ScenarioService_image_feature"), getString("_UI_PropertyDescriptor_description", "_UI_ScenarioService_image_feature", "_UI_ScenarioService_type"),
				ScenarioServicePackage.eINSTANCE.getScenarioService_Image(), true, false, false, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
	}

	/**
	 * This returns ScenarioService.gif.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public Object getImage(Object object) {
		if (object instanceof ScenarioService) {
			ScenarioService scenarioService = (ScenarioService) object;
			Object img = scenarioService.getImage();
			if (img != null) {
				return overlayImage(object, img);
			}

			if (scenarioService.isOffline()) {
				return overlayImage(object, getResourceLocator().getImage("full/obj16/ScenarioServiceOffline"));
			}
		}
		return overlayImage(object, getResourceLocator().getImage("full/obj16/ScenarioService"));
	}

	/**
	 * This returns the label text for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public String getText(Object object) {
		ScenarioService scenarioService = (ScenarioService) object;
		String label = scenarioService.getName();
		String fullLabel = label == null ? "(untitled)" : label;
		if (scenarioService.isLocked()) {
			fullLabel += " (locked by " + scenarioService.getLockedBy() + ")";
		}
		return fullLabel;
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

		switch (notification.getFeatureID(ScenarioService.class)) {
		case ScenarioServicePackage.SCENARIO_SERVICE__DESCRIPTION:
		case ScenarioServicePackage.SCENARIO_SERVICE__SERVICE_REF:
		case ScenarioServicePackage.SCENARIO_SERVICE__SUPPORTS_FORKING:
		case ScenarioServicePackage.SCENARIO_SERVICE__SUPPORTS_IMPORT:
		case ScenarioServicePackage.SCENARIO_SERVICE__LOCAL:
		case ScenarioServicePackage.SCENARIO_SERVICE__SERVICE_ID:
		case ScenarioServicePackage.SCENARIO_SERVICE__OFFLINE:
		case ScenarioServicePackage.SCENARIO_SERVICE__LOCKED_BY:
		case ScenarioServicePackage.SCENARIO_SERVICE__LOCKED:
		case ScenarioServicePackage.SCENARIO_SERVICE__IMAGE:
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
