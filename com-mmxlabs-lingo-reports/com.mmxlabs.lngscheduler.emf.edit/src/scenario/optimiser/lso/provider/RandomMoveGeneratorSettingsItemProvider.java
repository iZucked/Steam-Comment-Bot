/**
 * Copyright (C) Minimax Labs Ltd., 2010
 * All rights reserved.
 */

package scenario.optimiser.lso.provider;


import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemColorProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.IStructuredItemContentProvider;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ViewerNotification;

import scenario.optimiser.lso.LsoPackage;
import scenario.optimiser.lso.RandomMoveGeneratorSettings;

/**
 * This is the item provider adapter for a {@link scenario.optimiser.lso.RandomMoveGeneratorSettings} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class RandomMoveGeneratorSettingsItemProvider
	extends MoveGeneratorSettingsItemProvider
	implements
		IEditingDomainItemProvider, IStructuredItemContentProvider, ITreeItemContentProvider, IItemLabelProvider, IItemPropertySource, IItemColorProvider {
	/**
	 * This constructs an instance from a factory and a notifier.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public RandomMoveGeneratorSettingsItemProvider(AdapterFactory adapterFactory) {
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

			addUsing2over2PropertyDescriptor(object);
			addUsing3over2PropertyDescriptor(object);
			addUsing4over1PropertyDescriptor(object);
			addUsing4over2PropertyDescriptor(object);
		}
		return itemPropertyDescriptors;
	}

	/**
	 * This adds a property descriptor for the Using2over2 feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addUsing2over2PropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_RandomMoveGeneratorSettings_using2over2_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_RandomMoveGeneratorSettings_using2over2_feature", "_UI_RandomMoveGeneratorSettings_type"),
				 LsoPackage.Literals.RANDOM_MOVE_GENERATOR_SETTINGS__USING2OVER2,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Using3over2 feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addUsing3over2PropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_RandomMoveGeneratorSettings_using3over2_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_RandomMoveGeneratorSettings_using3over2_feature", "_UI_RandomMoveGeneratorSettings_type"),
				 LsoPackage.Literals.RANDOM_MOVE_GENERATOR_SETTINGS__USING3OVER2,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Using4over1 feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addUsing4over1PropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_RandomMoveGeneratorSettings_using4over1_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_RandomMoveGeneratorSettings_using4over1_feature", "_UI_RandomMoveGeneratorSettings_type"),
				 LsoPackage.Literals.RANDOM_MOVE_GENERATOR_SETTINGS__USING4OVER1,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Using4over2 feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addUsing4over2PropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_RandomMoveGeneratorSettings_using4over2_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_RandomMoveGeneratorSettings_using4over2_feature", "_UI_RandomMoveGeneratorSettings_type"),
				 LsoPackage.Literals.RANDOM_MOVE_GENERATOR_SETTINGS__USING4OVER2,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This returns RandomMoveGeneratorSettings.gif.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object getImage(Object object) {
		return overlayImage(object, getResourceLocator().getImage("full/obj16/RandomMoveGeneratorSettings"));
	}

	/**
	 * This returns the label text for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getText(Object object) {
		RandomMoveGeneratorSettings randomMoveGeneratorSettings = (RandomMoveGeneratorSettings)object;
		return getString("_UI_RandomMoveGeneratorSettings_type") + " " + randomMoveGeneratorSettings.isUsing2over2();
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

		switch (notification.getFeatureID(RandomMoveGeneratorSettings.class)) {
			case LsoPackage.RANDOM_MOVE_GENERATOR_SETTINGS__USING2OVER2:
			case LsoPackage.RANDOM_MOVE_GENERATOR_SETTINGS__USING3OVER2:
			case LsoPackage.RANDOM_MOVE_GENERATOR_SETTINGS__USING4OVER1:
			case LsoPackage.RANDOM_MOVE_GENERATOR_SETTINGS__USING4OVER2:
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
