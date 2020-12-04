/**
 */
package com.mmxlabs.models.lng.commercial.provider;


import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.MontlyBallastBonusContractLine;

import java.time.YearMonth;

import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ViewerNotification;

/**
 * This is the item provider adapter for a {@link com.mmxlabs.models.lng.commercial.MontlyBallastBonusContractLine} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class MontlyBallastBonusContractLineItemProvider extends BallastBonusContractLineItemProvider {
	/**
	 * This constructs an instance from a factory and a notifier.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public MontlyBallastBonusContractLineItemProvider(AdapterFactory adapterFactory) {
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
				 getString("_UI_MontlyBallastBonusContractLine_month_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_MontlyBallastBonusContractLine_month_feature", "_UI_MontlyBallastBonusContractLine_type"),
				 CommercialPackage.Literals.MONTLY_BALLAST_BONUS_CONTRACT_LINE__MONTH,
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
				 getString("_UI_MontlyBallastBonusContractLine_ballastBonusTo_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_MontlyBallastBonusContractLine_ballastBonusTo_feature", "_UI_MontlyBallastBonusContractLine_type"),
				 CommercialPackage.Literals.MONTLY_BALLAST_BONUS_CONTRACT_LINE__BALLAST_BONUS_TO,
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
				 getString("_UI_MontlyBallastBonusContractLine_ballastBonusPctFuel_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_MontlyBallastBonusContractLine_ballastBonusPctFuel_feature", "_UI_MontlyBallastBonusContractLine_type"),
				 CommercialPackage.Literals.MONTLY_BALLAST_BONUS_CONTRACT_LINE__BALLAST_BONUS_PCT_FUEL,
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
				 getString("_UI_MontlyBallastBonusContractLine_ballastBonusPctCharter_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_MontlyBallastBonusContractLine_ballastBonusPctCharter_feature", "_UI_MontlyBallastBonusContractLine_type"),
				 CommercialPackage.Literals.MONTLY_BALLAST_BONUS_CONTRACT_LINE__BALLAST_BONUS_PCT_CHARTER,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This returns MontlyBallastBonusContractLine.gif.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object getImage(Object object) {
		return overlayImage(object, getResourceLocator().getImage("full/obj16/MontlyBallastBonusContractLine"));
	}

	/**
	 * This returns the label text for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getText(Object object) {
		YearMonth labelValue = ((MontlyBallastBonusContractLine)object).getMonth();
		String label = labelValue == null ? null : labelValue.toString();
		return label == null || label.length() == 0 ?
			getString("_UI_MontlyBallastBonusContractLine_type") :
			getString("_UI_MontlyBallastBonusContractLine_type") + " " + label;
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

		switch (notification.getFeatureID(MontlyBallastBonusContractLine.class)) {
			case CommercialPackage.MONTLY_BALLAST_BONUS_CONTRACT_LINE__MONTH:
			case CommercialPackage.MONTLY_BALLAST_BONUS_CONTRACT_LINE__BALLAST_BONUS_TO:
			case CommercialPackage.MONTLY_BALLAST_BONUS_CONTRACT_LINE__BALLAST_BONUS_PCT_FUEL:
			case CommercialPackage.MONTLY_BALLAST_BONUS_CONTRACT_LINE__BALLAST_BONUS_PCT_CHARTER:
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
