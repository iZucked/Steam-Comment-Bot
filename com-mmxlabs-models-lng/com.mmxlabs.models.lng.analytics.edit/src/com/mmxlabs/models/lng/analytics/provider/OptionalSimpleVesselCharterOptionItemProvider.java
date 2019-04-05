/**
 */
package com.mmxlabs.models.lng.analytics.provider;


import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.OptionalSimpleVesselCharterOption;

import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ViewerNotification;

/**
 * This is the item provider adapter for a {@link com.mmxlabs.models.lng.analytics.OptionalSimpleVesselCharterOption} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class OptionalSimpleVesselCharterOptionItemProvider extends SimpleVesselCharterOptionItemProvider {
	/**
	 * This constructs an instance from a factory and a notifier.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public OptionalSimpleVesselCharterOptionItemProvider(AdapterFactory adapterFactory) {
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

			addBallastBonusPropertyDescriptor(object);
			addRepositioningFeePropertyDescriptor(object);
			addStartPropertyDescriptor(object);
			addEndPropertyDescriptor(object);
			addStartPortPropertyDescriptor(object);
			addEndPortPropertyDescriptor(object);
		}
		return itemPropertyDescriptors;
	}

	/**
	 * This adds a property descriptor for the Ballast Bonus feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addBallastBonusPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_OptionalSimpleVesselCharterOption_ballastBonus_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_OptionalSimpleVesselCharterOption_ballastBonus_feature", "_UI_OptionalSimpleVesselCharterOption_type"),
				 AnalyticsPackage.Literals.OPTIONAL_SIMPLE_VESSEL_CHARTER_OPTION__BALLAST_BONUS,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Repositioning Fee feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addRepositioningFeePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_OptionalSimpleVesselCharterOption_repositioningFee_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_OptionalSimpleVesselCharterOption_repositioningFee_feature", "_UI_OptionalSimpleVesselCharterOption_type"),
				 AnalyticsPackage.Literals.OPTIONAL_SIMPLE_VESSEL_CHARTER_OPTION__REPOSITIONING_FEE,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Start feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addStartPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_OptionalSimpleVesselCharterOption_start_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_OptionalSimpleVesselCharterOption_start_feature", "_UI_OptionalSimpleVesselCharterOption_type"),
				 AnalyticsPackage.Literals.OPTIONAL_SIMPLE_VESSEL_CHARTER_OPTION__START,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the End feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addEndPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_OptionalSimpleVesselCharterOption_end_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_OptionalSimpleVesselCharterOption_end_feature", "_UI_OptionalSimpleVesselCharterOption_type"),
				 AnalyticsPackage.Literals.OPTIONAL_SIMPLE_VESSEL_CHARTER_OPTION__END,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Start Port feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addStartPortPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_OptionalSimpleVesselCharterOption_startPort_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_OptionalSimpleVesselCharterOption_startPort_feature", "_UI_OptionalSimpleVesselCharterOption_type"),
				 AnalyticsPackage.Literals.OPTIONAL_SIMPLE_VESSEL_CHARTER_OPTION__START_PORT,
				 true,
				 false,
				 true,
				 null,
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
				 getString("_UI_OptionalSimpleVesselCharterOption_endPort_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_OptionalSimpleVesselCharterOption_endPort_feature", "_UI_OptionalSimpleVesselCharterOption_type"),
				 AnalyticsPackage.Literals.OPTIONAL_SIMPLE_VESSEL_CHARTER_OPTION__END_PORT,
				 true,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This returns OptionalSimpleVesselCharterOption.gif.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object getImage(Object object) {
		return overlayImage(object, getResourceLocator().getImage("full/obj16/OptionalSimpleVesselCharterOption"));
	}

	/**
	 * This returns the label text for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getText(Object object) {
		String label = ((OptionalSimpleVesselCharterOption)object).getUuid();
		return label == null || label.length() == 0 ?
			getString("_UI_OptionalSimpleVesselCharterOption_type") :
			getString("_UI_OptionalSimpleVesselCharterOption_type") + " " + label;
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

		switch (notification.getFeatureID(OptionalSimpleVesselCharterOption.class)) {
			case AnalyticsPackage.OPTIONAL_SIMPLE_VESSEL_CHARTER_OPTION__BALLAST_BONUS:
			case AnalyticsPackage.OPTIONAL_SIMPLE_VESSEL_CHARTER_OPTION__REPOSITIONING_FEE:
			case AnalyticsPackage.OPTIONAL_SIMPLE_VESSEL_CHARTER_OPTION__START:
			case AnalyticsPackage.OPTIONAL_SIMPLE_VESSEL_CHARTER_OPTION__END:
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
