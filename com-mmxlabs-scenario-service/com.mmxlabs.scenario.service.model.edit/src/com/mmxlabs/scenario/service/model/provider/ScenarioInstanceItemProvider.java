/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.model.provider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.ComposedImage;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.IStructuredItemContentProvider;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ViewerNotification;

import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.ScenarioServiceFactory;
import com.mmxlabs.scenario.service.model.ScenarioServicePackage;

/**
 * This is the item provider adapter for a {@link com.mmxlabs.scenario.service.model.ScenarioInstance} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class ScenarioInstanceItemProvider extends ContainerItemProvider implements IEditingDomainItemProvider, IStructuredItemContentProvider, ITreeItemContentProvider, IItemLabelProvider,
		IItemPropertySource {
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
			addLockedPropertyDescriptor(object);
			addAdaptersPropertyDescriptor(object);
			addRootObjectURIPropertyDescriptor(object);
			addDirtyPropertyDescriptor(object);
			addValidationStatusCodePropertyDescriptor(object);
			addScenarioVersionPropertyDescriptor(object);
			addVersionContextPropertyDescriptor(object);
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
				ScenarioServicePackage.Literals.SCENARIO_INSTANCE__UUID, true, false, false, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
	}

	/**
	 * This adds a property descriptor for the Locked feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addLockedPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(), getResourceLocator(),
				getString("_UI_ScenarioInstance_locked_feature"), getString("_UI_PropertyDescriptor_description", "_UI_ScenarioInstance_locked_feature", "_UI_ScenarioInstance_type"),
				ScenarioServicePackage.Literals.SCENARIO_INSTANCE__LOCKED, true, false, false, ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE, null, null));
	}

	/**
	 * This adds a property descriptor for the Adapters feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addAdaptersPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(), getResourceLocator(),
				getString("_UI_ScenarioInstance_adapters_feature"), getString("_UI_ScenarioInstance_adapters_description"), ScenarioServicePackage.Literals.SCENARIO_INSTANCE__ADAPTERS, true, false,
				false, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
	}

	/**
	 * This adds a property descriptor for the Root Object URI feature.
	 * <!-- begin-user-doc -->
	 * @since 4.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addRootObjectURIPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(), getResourceLocator(),
				getString("_UI_ScenarioInstance_rootObjectURI_feature"), getString("_UI_PropertyDescriptor_description", "_UI_ScenarioInstance_rootObjectURI_feature", "_UI_ScenarioInstance_type"),
				ScenarioServicePackage.Literals.SCENARIO_INSTANCE__ROOT_OBJECT_URI, true, false, false, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
	}

	/**
	 * This adds a property descriptor for the Scenario Version feature.
	 * <!-- begin-user-doc -->
	 * @since 3.1
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addScenarioVersionPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(), getResourceLocator(),
				getString("_UI_ScenarioInstance_scenarioVersion_feature"), getString("_UI_ScenarioInstance_scenarioVersion_description"),
				ScenarioServicePackage.Literals.SCENARIO_INSTANCE__SCENARIO_VERSION, true, false, false, ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE, null, null));
	}

	/**
	 * This adds a property descriptor for the Version Context feature.
	 * <!-- begin-user-doc -->
	 * @since 3.1
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addVersionContextPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(), getResourceLocator(),
				getString("_UI_ScenarioInstance_versionContext_feature"), getString("_UI_ScenarioInstance_versionContext_description"),
				ScenarioServicePackage.Literals.SCENARIO_INSTANCE__VERSION_CONTEXT, true, false, false, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
	}

	/**
	 * This adds a property descriptor for the Dirty feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addDirtyPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(), getResourceLocator(),
				getString("_UI_ScenarioInstance_dirty_feature"), getString("_UI_PropertyDescriptor_description", "_UI_ScenarioInstance_dirty_feature", "_UI_ScenarioInstance_type"),
				ScenarioServicePackage.Literals.SCENARIO_INSTANCE__DIRTY, true, false, false, ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE, null, null));
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
				ScenarioServicePackage.Literals.SCENARIO_INSTANCE__VALIDATION_STATUS_CODE, false, false, false, ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE, null, null));
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
			childrenFeatures.add(ScenarioServicePackage.Literals.SCENARIO_INSTANCE__METADATA);
			childrenFeatures.add(ScenarioServicePackage.Literals.SCENARIO_INSTANCE__INSTANCE);
			childrenFeatures.add(ScenarioServicePackage.Literals.SCENARIO_INSTANCE__LOCKS);
			childrenFeatures.add(ScenarioServicePackage.Literals.SCENARIO_INSTANCE__FRAGMENTS);
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
	 * This returns ScenarioInstance.gif.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public Object getImage(Object object) {
		boolean locked = ((ScenarioInstance) object).isLocked();

		final List<Object> images = new ArrayList<Object>(3);
		images.add(getResourceLocator().getImage("full/obj16/ScenarioInstance"));
		if (locked) {
			images.add(getResourceLocator().getImage("overlays/lock"));

		}
		//		boolean archived = ((ScenarioInstance) object).isLocked();
		//		if (archived) {
		//			images.add(getResourceLocator().getImage("overlays/archive"));
		//		}
		return new ComposedImage(images);
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
		case ScenarioServicePackage.SCENARIO_INSTANCE__LOCKED:
		case ScenarioServicePackage.SCENARIO_INSTANCE__ADAPTERS:
		case ScenarioServicePackage.SCENARIO_INSTANCE__ROOT_OBJECT_URI:
		case ScenarioServicePackage.SCENARIO_INSTANCE__DIRTY:
		case ScenarioServicePackage.SCENARIO_INSTANCE__VALIDATION_STATUS_CODE:
		case ScenarioServicePackage.SCENARIO_INSTANCE__SCENARIO_VERSION:
		case ScenarioServicePackage.SCENARIO_INSTANCE__VERSION_CONTEXT:
			fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), false, true));
			return;
		case ScenarioServicePackage.SCENARIO_INSTANCE__METADATA:
		case ScenarioServicePackage.SCENARIO_INSTANCE__INSTANCE:
		case ScenarioServicePackage.SCENARIO_INSTANCE__LOCKS:
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

		newChildDescriptors.add(createChildParameter(ScenarioServicePackage.Literals.SCENARIO_INSTANCE__METADATA, ScenarioServiceFactory.eINSTANCE.createMetadata()));

		newChildDescriptors.add(createChildParameter(ScenarioServicePackage.Literals.SCENARIO_INSTANCE__LOCKS, ScenarioServiceFactory.eINSTANCE.createScenarioLock()));

		newChildDescriptors.add(createChildParameter(ScenarioServicePackage.Literals.SCENARIO_INSTANCE__FRAGMENTS, ScenarioServiceFactory.eINSTANCE.createScenarioFragment()));
	}

}
