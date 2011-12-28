/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.model.provider;

import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.ScenarioServiceFactory;
import com.mmxlabs.scenario.service.model.ScenarioServicePackage;

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
import org.eclipse.emf.edit.provider.ItemProviderAdapter;
import org.eclipse.emf.edit.provider.ViewerNotification;

/**
 * This is the item provider adapter for a {@link com.mmxlabs.scenario.service.model.ScenarioInstance} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class ScenarioInstanceItemProvider extends ItemProviderAdapter implements IEditingDomainItemProvider, IStructuredItemContentProvider, ITreeItemContentProvider, IItemLabelProvider,
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

			addNamePropertyDescriptor(object);
			addUuidPropertyDescriptor(object);
			addUriPropertyDescriptor(object);
			addLockedPropertyDescriptor(object);
			addArchivedPropertyDescriptor(object);
		}
		return itemPropertyDescriptors;
	}

	/**
	 * This adds a property descriptor for the Uri feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addUriPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(), getResourceLocator(),
				getString("_UI_ScenarioInstance_uri_feature"), getString("_UI_PropertyDescriptor_description", "_UI_ScenarioInstance_uri_feature", "_UI_ScenarioInstance_type"),
				ScenarioServicePackage.Literals.SCENARIO_INSTANCE__URI, true, false, false, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
	}

	/**
	 * This adds a property descriptor for the Name feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addNamePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(), getResourceLocator(),
				getString("_UI_ScenarioInstance_name_feature"), getString("_UI_PropertyDescriptor_description", "_UI_ScenarioInstance_name_feature", "_UI_ScenarioInstance_type"),
				ScenarioServicePackage.Literals.SCENARIO_INSTANCE__NAME, true, false, false, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
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
	 * This adds a property descriptor for the Archived feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addArchivedPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(), getResourceLocator(),
				getString("_UI_ScenarioInstance_archived_feature"), getString("_UI_PropertyDescriptor_description", "_UI_ScenarioInstance_archived_feature", "_UI_ScenarioInstance_type"),
				ScenarioServicePackage.Literals.SCENARIO_INSTANCE__ARCHIVED, true, false, false, ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE, null, null));
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
			childrenFeatures.add(ScenarioServicePackage.Literals.SCENARIO_INSTANCE__INITIAL_SOLUTION);
			childrenFeatures.add(ScenarioServicePackage.Literals.SCENARIO_INSTANCE__VARIATIONS);
			childrenFeatures.add(ScenarioServicePackage.Literals.SCENARIO_INSTANCE__PARAMETER_SETS);
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
	 * @generated
	 */
	@Override
	public Object getImage(Object object) {
		return overlayImage(object, getResourceLocator().getImage("full/obj16/ScenarioInstance"));
	}

	/**
	 * This returns the label text for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getText(Object object) {
		String label = ((ScenarioInstance) object).getName();
		return label == null || label.length() == 0 ? getString("_UI_ScenarioInstance_type") : getString("_UI_ScenarioInstance_type") + " " + label;
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
		case ScenarioServicePackage.SCENARIO_INSTANCE__NAME:
		case ScenarioServicePackage.SCENARIO_INSTANCE__UUID:
		case ScenarioServicePackage.SCENARIO_INSTANCE__URI:
		case ScenarioServicePackage.SCENARIO_INSTANCE__LOCKED:
		case ScenarioServicePackage.SCENARIO_INSTANCE__ARCHIVED:
			fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), false, true));
			return;
		case ScenarioServicePackage.SCENARIO_INSTANCE__METADATA:
		case ScenarioServicePackage.SCENARIO_INSTANCE__INITIAL_SOLUTION:
		case ScenarioServicePackage.SCENARIO_INSTANCE__VARIATIONS:
		case ScenarioServicePackage.SCENARIO_INSTANCE__PARAMETER_SETS:
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

		newChildDescriptors.add(createChildParameter(ScenarioServicePackage.Literals.SCENARIO_INSTANCE__INITIAL_SOLUTION, ScenarioServiceFactory.eINSTANCE.createSolution()));

		newChildDescriptors.add(createChildParameter(ScenarioServicePackage.Literals.SCENARIO_INSTANCE__VARIATIONS, ScenarioServiceFactory.eINSTANCE.createScenarioInstance()));

		newChildDescriptors.add(createChildParameter(ScenarioServicePackage.Literals.SCENARIO_INSTANCE__PARAMETER_SETS, ScenarioServiceFactory.eINSTANCE.createParamSet()));
	}

	/**
	 * Return the resource locator for this item provider's resources.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ResourceLocator getResourceLocator() {
		return ScenarioEditPlugin.INSTANCE;
	}

}
