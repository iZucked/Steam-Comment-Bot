/**
 */
package com.mmxlabs.models.lng.commercial.provider;


import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.MonthlyBallastBonusTerm;

import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ViewerNotification;

/**
 * This is the item provider adapter for a {@link com.mmxlabs.models.lng.commercial.MonthlyBallastBonusTerm} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class MonthlyBallastBonusTermItemProvider extends NotionalJourneyBallastBonusTermItemProvider {
	/**
	 * This constructs an instance from a factory and a notifier.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public MonthlyBallastBonusTermItemProvider(AdapterFactory adapterFactory) {
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

			addMonthPropertyDescriptor(object);
			addBallastBonusToPropertyDescriptor(object);
			addBallastBonusPctFuelPropertyDescriptor(object);
			addBallastBonusPctCharterPropertyDescriptor(object);
		}
		return itemPropertyDescriptors;
	}

	/**
	 * This adds a property descriptor for the Month feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addMonthPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_MonthlyBallastBonusTerm_month_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_MonthlyBallastBonusTerm_month_feature", "_UI_MonthlyBallastBonusTerm_type"),
				 CommercialPackage.Literals.MONTHLY_BALLAST_BONUS_TERM__MONTH,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Ballast Bonus To feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addBallastBonusToPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_MonthlyBallastBonusTerm_ballastBonusTo_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_MonthlyBallastBonusTerm_ballastBonusTo_feature", "_UI_MonthlyBallastBonusTerm_type"),
				 CommercialPackage.Literals.MONTHLY_BALLAST_BONUS_TERM__BALLAST_BONUS_TO,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Ballast Bonus Pct Fuel feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addBallastBonusPctFuelPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_MonthlyBallastBonusTerm_ballastBonusPctFuel_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_MonthlyBallastBonusTerm_ballastBonusPctFuel_feature", "_UI_MonthlyBallastBonusTerm_type"),
				 CommercialPackage.Literals.MONTHLY_BALLAST_BONUS_TERM__BALLAST_BONUS_PCT_FUEL,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Ballast Bonus Pct Charter feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addBallastBonusPctCharterPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_MonthlyBallastBonusTerm_ballastBonusPctCharter_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_MonthlyBallastBonusTerm_ballastBonusPctCharter_feature", "_UI_MonthlyBallastBonusTerm_type"),
				 CommercialPackage.Literals.MONTHLY_BALLAST_BONUS_TERM__BALLAST_BONUS_PCT_CHARTER,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This returns MonthlyBallastBonusTerm.gif.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object getImage(Object object) {
		return overlayImage(object, getResourceLocator().getImage("full/obj16/MonthlyBallastBonusTerm"));
	}

	/**
	 * This returns the label text for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getText(Object object) {
		MonthlyBallastBonusTerm monthlyBallastBonusTerm = (MonthlyBallastBonusTerm)object;
		return getString("_UI_MonthlyBallastBonusTerm_type") + " " + monthlyBallastBonusTerm.getSpeed();
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

		switch (notification.getFeatureID(MonthlyBallastBonusTerm.class)) {
			case CommercialPackage.MONTHLY_BALLAST_BONUS_TERM__MONTH:
			case CommercialPackage.MONTHLY_BALLAST_BONUS_TERM__BALLAST_BONUS_TO:
			case CommercialPackage.MONTHLY_BALLAST_BONUS_TERM__BALLAST_BONUS_PCT_FUEL:
			case CommercialPackage.MONTHLY_BALLAST_BONUS_TERM__BALLAST_BONUS_PCT_CHARTER:
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
