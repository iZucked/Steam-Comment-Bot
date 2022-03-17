/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.model.provider;

import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EPackage.Registry;
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

import com.mmxlabs.scenario.service.model.ScenarioFragment;
import com.mmxlabs.scenario.service.model.ScenarioServicePackage;

/**
 * This is the item provider adapter for a {@link com.mmxlabs.scenario.service.model.ScenarioFragment} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class ScenarioFragmentItemProvider extends ItemProviderAdapter
		implements IEditingDomainItemProvider, IStructuredItemContentProvider, ITreeItemContentProvider, IItemLabelProvider, IItemPropertySource {
	/**
	 * This constructs an instance from a factory and a notifier.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ScenarioFragmentItemProvider(AdapterFactory adapterFactory) {
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
			addFragmentPropertyDescriptor(object);
			addContentTypePropertyDescriptor(object);
			addUseCommandStackPropertyDescriptor(object);
			addTypeHintPropertyDescriptor(object);
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
		itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(), getResourceLocator(),
				getString("_UI_ScenarioFragment_name_feature"), getString("_UI_PropertyDescriptor_description", "_UI_ScenarioFragment_name_feature", "_UI_ScenarioFragment_type"),
				ScenarioServicePackage.eINSTANCE.getScenarioFragment_Name(), true, false, false, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
	}

	/**
	 * This adds a property descriptor for the Fragment feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addFragmentPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(), getResourceLocator(),
				getString("_UI_ScenarioFragment_fragment_feature"), getString("_UI_PropertyDescriptor_description", "_UI_ScenarioFragment_fragment_feature", "_UI_ScenarioFragment_type"),
				ScenarioServicePackage.eINSTANCE.getScenarioFragment_Fragment(), true, false, true, null, null, null));
	}

	/**
	 * This adds a property descriptor for the Content Type feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addContentTypePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(), getResourceLocator(),
				getString("_UI_ScenarioFragment_contentType_feature"), getString("_UI_PropertyDescriptor_description", "_UI_ScenarioFragment_contentType_feature", "_UI_ScenarioFragment_type"),
				ScenarioServicePackage.eINSTANCE.getScenarioFragment_ContentType(), true, false, false, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
	}

	/**
	 * This adds a property descriptor for the Use Command Stack feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addUseCommandStackPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(), getResourceLocator(),
				getString("_UI_ScenarioFragment_useCommandStack_feature"), getString("_UI_PropertyDescriptor_description", "_UI_ScenarioFragment_useCommandStack_feature", "_UI_ScenarioFragment_type"),
				ScenarioServicePackage.eINSTANCE.getScenarioFragment_UseCommandStack(), true, false, false, ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE, null, null));
	}

	/**
	 * This adds a property descriptor for the Type Hint feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addTypeHintPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(), getResourceLocator(),
				getString("_UI_ScenarioFragment_typeHint_feature"), getString("_UI_PropertyDescriptor_description", "_UI_ScenarioFragment_typeHint_feature", "_UI_ScenarioFragment_type"),
				ScenarioServicePackage.eINSTANCE.getScenarioFragment_TypeHint(), true, false, false, ItemPropertyDescriptor.GENERIC_VALUE_IMAGE, null, null));
	}

	/**
	 * This returns ScenarioFragment.gif.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public Object getImage(Object object) {

		// Delegate to the fragment's item provider to get an image if possible.
		final EObject fragment = ((ScenarioFragment) object).getFragment();
		if (fragment != null) {
			final IItemLabelProvider lp = (IItemLabelProvider) ((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory().adapt(fragment, IItemLabelProvider.class);
			if (lp != null) {
				return lp.getImage(fragment);
			}
		} else {
			String name = ((ScenarioFragment) object).getTypeHint();
			if (name != null) {
				EClass eClass = null;
				// All registry packages...
				LOOP_OUTER: for (final Object obj : Registry.INSTANCE.values()) {
					if (obj instanceof EPackage) {
						EPackage ePackage = (EPackage) obj;
						for (final EClassifier e : ePackage.getEClassifiers()) {
							if (e.getInstanceClass() != null) {
								if (e.getInstanceClass().getCanonicalName().equals(name)) {
									eClass = (EClass) e;
									break LOOP_OUTER;
								}
							}
						}
					}
				}
				if (eClass != null) {
					EObject instance = eClass.getEPackage().getEFactoryInstance().create(eClass);
					if (instance != null) {
						final IItemLabelProvider lp = (IItemLabelProvider) ((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory().adapt(instance, IItemLabelProvider.class);
						if (lp != null) {
							return lp.getImage(instance);
						}
					}
				}
			}
		}

		return overlayImage(object, getResourceLocator().getImage("full/obj16/ScenarioFragment.png"));
	}

	/**
	 * This returns the label text for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public String getText(Object object) {
		String label = ((ScenarioFragment) object).getName();
		return label == null || label.length() == 0 ? "(No name)" : label;
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

		switch (notification.getFeatureID(ScenarioFragment.class)) {
		case ScenarioServicePackage.SCENARIO_FRAGMENT__NAME:
		case ScenarioServicePackage.SCENARIO_FRAGMENT__CONTENT_TYPE:
		case ScenarioServicePackage.SCENARIO_FRAGMENT__USE_COMMAND_STACK:
		case ScenarioServicePackage.SCENARIO_FRAGMENT__TYPE_HINT:
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
