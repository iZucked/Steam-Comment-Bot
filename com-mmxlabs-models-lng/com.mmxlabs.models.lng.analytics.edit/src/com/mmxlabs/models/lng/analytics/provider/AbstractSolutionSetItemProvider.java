/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.analytics.provider;


import com.mmxlabs.models.lng.analytics.AbstractSolutionSet;
import com.mmxlabs.models.lng.analytics.AnalyticsFactory;
import com.mmxlabs.models.lng.analytics.AnalyticsPackage;

import com.mmxlabs.models.lng.cargo.CargoFactory;

import com.mmxlabs.models.lng.parameters.ParametersFactory;
import com.mmxlabs.models.mmxcore.MMXCorePackage;

import com.mmxlabs.models.mmxcore.provider.UUIDObjectItemProvider;

import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EStructuralFeature;

import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ViewerNotification;

/**
 * This is the item provider adapter for a {@link com.mmxlabs.models.lng.analytics.AbstractSolutionSet} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class AbstractSolutionSetItemProvider extends UUIDObjectItemProvider {
	/**
	 * This constructs an instance from a factory and a notifier.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AbstractSolutionSetItemProvider(AdapterFactory adapterFactory) {
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
			addHasDualModeSolutionsPropertyDescriptor(object);
			addPortfolioBreakEvenModePropertyDescriptor(object);
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
				 getString("_UI_NamedObject_name_feature"),
				 getString("_UI_NamedObject_name_description"),
				 MMXCorePackage.Literals.NAMED_OBJECT__NAME,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Has Dual Mode Solutions feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addHasDualModeSolutionsPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_AbstractSolutionSet_hasDualModeSolutions_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_AbstractSolutionSet_hasDualModeSolutions_feature", "_UI_AbstractSolutionSet_type"),
				 AnalyticsPackage.Literals.ABSTRACT_SOLUTION_SET__HAS_DUAL_MODE_SOLUTIONS,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Portfolio Break Even Mode feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addPortfolioBreakEvenModePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_AbstractSolutionSet_portfolioBreakEvenMode_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_AbstractSolutionSet_portfolioBreakEvenMode_feature", "_UI_AbstractSolutionSet_type"),
				 AnalyticsPackage.Literals.ABSTRACT_SOLUTION_SET__PORTFOLIO_BREAK_EVEN_MODE,
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
			childrenFeatures.add(AnalyticsPackage.Literals.ABSTRACT_SOLUTION_SET__USER_SETTINGS);
			childrenFeatures.add(AnalyticsPackage.Literals.ABSTRACT_SOLUTION_SET__EXTRA_SLOTS);
			childrenFeatures.add(AnalyticsPackage.Literals.ABSTRACT_SOLUTION_SET__BASE_OPTION);
			childrenFeatures.add(AnalyticsPackage.Literals.ABSTRACT_SOLUTION_SET__OPTIONS);
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
	 * This returns AbstractSolutionSet.gif.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object getImage(Object object) {
		return overlayImage(object, getResourceLocator().getImage("full/obj16/AbstractSolutionSet"));
	}

	/**
	 * This returns the label text for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getText(Object object) {
		String label = ((AbstractSolutionSet)object).getName();
		return label == null || label.length() == 0 ?
			getString("_UI_AbstractSolutionSet_type") :
			getString("_UI_AbstractSolutionSet_type") + " " + label;
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

		switch (notification.getFeatureID(AbstractSolutionSet.class)) {
			case AnalyticsPackage.ABSTRACT_SOLUTION_SET__NAME:
			case AnalyticsPackage.ABSTRACT_SOLUTION_SET__HAS_DUAL_MODE_SOLUTIONS:
			case AnalyticsPackage.ABSTRACT_SOLUTION_SET__PORTFOLIO_BREAK_EVEN_MODE:
				fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), false, true));
				return;
			case AnalyticsPackage.ABSTRACT_SOLUTION_SET__USER_SETTINGS:
			case AnalyticsPackage.ABSTRACT_SOLUTION_SET__EXTRA_SLOTS:
			case AnalyticsPackage.ABSTRACT_SOLUTION_SET__BASE_OPTION:
			case AnalyticsPackage.ABSTRACT_SOLUTION_SET__OPTIONS:
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
				(AnalyticsPackage.Literals.ABSTRACT_SOLUTION_SET__USER_SETTINGS,
				 ParametersFactory.eINSTANCE.createUserSettings()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.ABSTRACT_SOLUTION_SET__EXTRA_SLOTS,
				 CargoFactory.eINSTANCE.createLoadSlot()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.ABSTRACT_SOLUTION_SET__EXTRA_SLOTS,
				 CargoFactory.eINSTANCE.createDischargeSlot()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.ABSTRACT_SOLUTION_SET__EXTRA_SLOTS,
				 CargoFactory.eINSTANCE.createSpotLoadSlot()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.ABSTRACT_SOLUTION_SET__EXTRA_SLOTS,
				 CargoFactory.eINSTANCE.createSpotDischargeSlot()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.ABSTRACT_SOLUTION_SET__BASE_OPTION,
				 AnalyticsFactory.eINSTANCE.createSolutionOption()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.ABSTRACT_SOLUTION_SET__BASE_OPTION,
				 AnalyticsFactory.eINSTANCE.createDualModeSolutionOption()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.ABSTRACT_SOLUTION_SET__OPTIONS,
				 AnalyticsFactory.eINSTANCE.createSolutionOption()));

		newChildDescriptors.add
			(createChildParameter
				(AnalyticsPackage.Literals.ABSTRACT_SOLUTION_SET__OPTIONS,
				 AnalyticsFactory.eINSTANCE.createDualModeSolutionOption()));
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
			childFeature == AnalyticsPackage.Literals.ABSTRACT_SOLUTION_SET__BASE_OPTION ||
			childFeature == AnalyticsPackage.Literals.ABSTRACT_SOLUTION_SET__OPTIONS;

		if (qualify) {
			return getString
				("_UI_CreateChild_text2",
				 new Object[] { getTypeText(childObject), getFeatureText(childFeature), getTypeText(owner) });
		}
		return super.getCreateChildText(owner, feature, child, selection);
	}

}
