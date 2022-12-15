/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.commercial.provider;


import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.NotionalJourneyBallastBonusTerm;

import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ViewerNotification;

/**
 * This is the item provider adapter for a {@link com.mmxlabs.models.lng.commercial.NotionalJourneyBallastBonusTerm} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class NotionalJourneyBallastBonusTermItemProvider extends BallastBonusTermItemProvider {
	/**
	 * This constructs an instance from a factory and a notifier.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotionalJourneyBallastBonusTermItemProvider(AdapterFactory adapterFactory) {
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

			addSpeedPropertyDescriptor(object);
			addFuelPriceExpressionPropertyDescriptor(object);
			addHirePriceExpressionPropertyDescriptor(object);
			addIncludeCanalPropertyDescriptor(object);
			addIncludeCanalTimePropertyDescriptor(object);
			addLumpSumPriceExpressionPropertyDescriptor(object);
			addPriceOnLastLNGPricePropertyDescriptor(object);
			addReturnPortsPropertyDescriptor(object);
		}
		return itemPropertyDescriptors;
	}

	/**
	 * This adds a property descriptor for the Speed feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addSpeedPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_NotionalJourneyTerm_speed_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_NotionalJourneyTerm_speed_feature", "_UI_NotionalJourneyTerm_type"),
				 CommercialPackage.Literals.NOTIONAL_JOURNEY_TERM__SPEED,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.REAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Fuel Price Expression feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addFuelPriceExpressionPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_NotionalJourneyTerm_fuelPriceExpression_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_NotionalJourneyTerm_fuelPriceExpression_feature", "_UI_NotionalJourneyTerm_type"),
				 CommercialPackage.Literals.NOTIONAL_JOURNEY_TERM__FUEL_PRICE_EXPRESSION,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Hire Price Expression feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addHirePriceExpressionPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_NotionalJourneyTerm_hirePriceExpression_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_NotionalJourneyTerm_hirePriceExpression_feature", "_UI_NotionalJourneyTerm_type"),
				 CommercialPackage.Literals.NOTIONAL_JOURNEY_TERM__HIRE_PRICE_EXPRESSION,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Include Canal feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addIncludeCanalPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_NotionalJourneyTerm_includeCanal_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_NotionalJourneyTerm_includeCanal_feature", "_UI_NotionalJourneyTerm_type"),
				 CommercialPackage.Literals.NOTIONAL_JOURNEY_TERM__INCLUDE_CANAL,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Include Canal Time feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addIncludeCanalTimePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_NotionalJourneyTerm_includeCanalTime_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_NotionalJourneyTerm_includeCanalTime_feature", "_UI_NotionalJourneyTerm_type"),
				 CommercialPackage.Literals.NOTIONAL_JOURNEY_TERM__INCLUDE_CANAL_TIME,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Lump Sum Price Expression feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addLumpSumPriceExpressionPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_NotionalJourneyTerm_lumpSumPriceExpression_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_NotionalJourneyTerm_lumpSumPriceExpression_feature", "_UI_NotionalJourneyTerm_type"),
				 CommercialPackage.Literals.NOTIONAL_JOURNEY_TERM__LUMP_SUM_PRICE_EXPRESSION,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Price On Last LNG Price feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addPriceOnLastLNGPricePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_NotionalJourneyTerm_priceOnLastLNGPrice_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_NotionalJourneyTerm_priceOnLastLNGPrice_feature", "_UI_NotionalJourneyTerm_type"),
				 CommercialPackage.Literals.NOTIONAL_JOURNEY_TERM__PRICE_ON_LAST_LNG_PRICE,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Return Ports feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addReturnPortsPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_NotionalJourneyBallastBonusTerm_returnPorts_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_NotionalJourneyBallastBonusTerm_returnPorts_feature", "_UI_NotionalJourneyBallastBonusTerm_type"),
				 CommercialPackage.Literals.NOTIONAL_JOURNEY_BALLAST_BONUS_TERM__RETURN_PORTS,
				 true,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This returns NotionalJourneyBallastBonusTerm.gif.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object getImage(Object object) {
		return overlayImage(object, getResourceLocator().getImage("full/obj16/NotionalJourneyBallastBonusTerm"));
	}

	/**
	 * This returns the label text for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getText(Object object) {
		NotionalJourneyBallastBonusTerm notionalJourneyBallastBonusTerm = (NotionalJourneyBallastBonusTerm)object;
		return getString("_UI_NotionalJourneyBallastBonusTerm_type") + " " + notionalJourneyBallastBonusTerm.getSpeed();
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

		switch (notification.getFeatureID(NotionalJourneyBallastBonusTerm.class)) {
			case CommercialPackage.NOTIONAL_JOURNEY_BALLAST_BONUS_TERM__SPEED:
			case CommercialPackage.NOTIONAL_JOURNEY_BALLAST_BONUS_TERM__FUEL_PRICE_EXPRESSION:
			case CommercialPackage.NOTIONAL_JOURNEY_BALLAST_BONUS_TERM__HIRE_PRICE_EXPRESSION:
			case CommercialPackage.NOTIONAL_JOURNEY_BALLAST_BONUS_TERM__INCLUDE_CANAL:
			case CommercialPackage.NOTIONAL_JOURNEY_BALLAST_BONUS_TERM__INCLUDE_CANAL_TIME:
			case CommercialPackage.NOTIONAL_JOURNEY_BALLAST_BONUS_TERM__LUMP_SUM_PRICE_EXPRESSION:
			case CommercialPackage.NOTIONAL_JOURNEY_BALLAST_BONUS_TERM__PRICE_ON_LAST_LNG_PRICE:
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
