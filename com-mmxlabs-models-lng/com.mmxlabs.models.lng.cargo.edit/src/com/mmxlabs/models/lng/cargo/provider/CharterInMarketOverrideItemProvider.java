/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.cargo.provider;


import com.mmxlabs.models.lng.cargo.CargoFactory;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.CharterInMarketOverride;

import com.mmxlabs.models.lng.commercial.CommercialFactory;
import com.mmxlabs.models.mmxcore.provider.MMXObjectItemProvider;
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
 * This is the item provider adapter for a {@link com.mmxlabs.models.lng.cargo.CharterInMarketOverride} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class CharterInMarketOverrideItemProvider 
	extends MMXObjectItemProvider {
	/**
	 * This constructs an instance from a factory and a notifier.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CharterInMarketOverrideItemProvider(AdapterFactory adapterFactory) {
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

			addCharterInMarketPropertyDescriptor(object);
			addSpotIndexPropertyDescriptor(object);
			addStartDatePropertyDescriptor(object);
			addEndPortPropertyDescriptor(object);
			addEndDatePropertyDescriptor(object);
			addIncludeBallastBonusPropertyDescriptor(object);
			addMinDurationPropertyDescriptor(object);
			addMaxDurationPropertyDescriptor(object);
		}
		return itemPropertyDescriptors;
	}

	/**
	 * This adds a property descriptor for the Charter In Market feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addCharterInMarketPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_CharterInMarketOverride_charterInMarket_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_CharterInMarketOverride_charterInMarket_feature", "_UI_CharterInMarketOverride_type"),
				 CargoPackage.Literals.CHARTER_IN_MARKET_OVERRIDE__CHARTER_IN_MARKET,
				 true,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Spot Index feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addSpotIndexPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_CharterInMarketOverride_spotIndex_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_CharterInMarketOverride_spotIndex_feature", "_UI_CharterInMarketOverride_type"),
				 CargoPackage.Literals.CHARTER_IN_MARKET_OVERRIDE__SPOT_INDEX,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Start Date feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addStartDatePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_CharterInMarketOverride_startDate_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_CharterInMarketOverride_startDate_feature", "_UI_CharterInMarketOverride_type"),
				 CargoPackage.Literals.CHARTER_IN_MARKET_OVERRIDE__START_DATE,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the End Port feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addEndPortPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_CharterInMarketOverride_endPort_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_CharterInMarketOverride_endPort_feature", "_UI_CharterInMarketOverride_type"),
				 CargoPackage.Literals.CHARTER_IN_MARKET_OVERRIDE__END_PORT,
				 true,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the End Date feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addEndDatePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_CharterInMarketOverride_endDate_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_CharterInMarketOverride_endDate_feature", "_UI_CharterInMarketOverride_type"),
				 CargoPackage.Literals.CHARTER_IN_MARKET_OVERRIDE__END_DATE,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Include Ballast Bonus feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addIncludeBallastBonusPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_CharterInMarketOverride_includeBallastBonus_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_CharterInMarketOverride_includeBallastBonus_feature", "_UI_CharterInMarketOverride_type"),
				 CargoPackage.Literals.CHARTER_IN_MARKET_OVERRIDE__INCLUDE_BALLAST_BONUS,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Min Duration feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addMinDurationPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_CharterInMarketOverride_minDuration_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_CharterInMarketOverride_minDuration_feature", "_UI_CharterInMarketOverride_type"),
				 CargoPackage.Literals.CHARTER_IN_MARKET_OVERRIDE__MIN_DURATION,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Max Duration feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addMaxDurationPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_CharterInMarketOverride_maxDuration_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_CharterInMarketOverride_maxDuration_feature", "_UI_CharterInMarketOverride_type"),
				 CargoPackage.Literals.CHARTER_IN_MARKET_OVERRIDE__MAX_DURATION,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
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
			childrenFeatures.add(CargoPackage.Literals.CHARTER_IN_MARKET_OVERRIDE__START_HEEL);
			childrenFeatures.add(CargoPackage.Literals.CHARTER_IN_MARKET_OVERRIDE__END_HEEL);
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
	 * This returns CharterInMarketOverride.gif.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object getImage(Object object) {
		return overlayImage(object, getResourceLocator().getImage("full/obj16/CharterInMarketOverride"));
	}

	/**
	 * This returns the label text for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getText(Object object) {
		CharterInMarketOverride charterInMarketOverride = (CharterInMarketOverride)object;
		return getString("_UI_CharterInMarketOverride_type") + " " + charterInMarketOverride.getSpotIndex();
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

		switch (notification.getFeatureID(CharterInMarketOverride.class)) {
			case CargoPackage.CHARTER_IN_MARKET_OVERRIDE__SPOT_INDEX:
			case CargoPackage.CHARTER_IN_MARKET_OVERRIDE__START_DATE:
			case CargoPackage.CHARTER_IN_MARKET_OVERRIDE__END_DATE:
			case CargoPackage.CHARTER_IN_MARKET_OVERRIDE__INCLUDE_BALLAST_BONUS:
			case CargoPackage.CHARTER_IN_MARKET_OVERRIDE__MIN_DURATION:
			case CargoPackage.CHARTER_IN_MARKET_OVERRIDE__MAX_DURATION:
				fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), false, true));
				return;
			case CargoPackage.CHARTER_IN_MARKET_OVERRIDE__START_HEEL:
			case CargoPackage.CHARTER_IN_MARKET_OVERRIDE__END_HEEL:
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
				(CargoPackage.Literals.CHARTER_IN_MARKET_OVERRIDE__START_HEEL,
				 CommercialFactory.eINSTANCE.createStartHeelOptions()));

		newChildDescriptors.add
			(createChildParameter
				(CargoPackage.Literals.CHARTER_IN_MARKET_OVERRIDE__END_HEEL,
				 CommercialFactory.eINSTANCE.createEndHeelOptions()));
	}

}
