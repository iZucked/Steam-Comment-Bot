/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package com.mmxlabs.models.lng.commercial.provider;


import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.RedirectionPriceParameters;

import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.IStructuredItemContentProvider;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ViewerNotification;

/**
 * This is the item provider adapter for a {@link com.mmxlabs.models.lng.commercial.RedirectionPriceParameters} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class RedirectionPriceParametersItemProvider
	extends LNGPriceCalculatorParametersItemProvider
	implements
		IEditingDomainItemProvider,
		IStructuredItemContentProvider,
		ITreeItemContentProvider,
		IItemLabelProvider,
		IItemPropertySource {
	/**
	 * This constructs an instance from a factory and a notifier.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public RedirectionPriceParametersItemProvider(AdapterFactory adapterFactory) {
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

			addBaseSalesMarketPortPropertyDescriptor(object);
			addBaseSalesPriceExpressionPropertyDescriptor(object);
			addBasePurchasePriceExpressionPropertyDescriptor(object);
			addNotionalSpeedPropertyDescriptor(object);
			addDesPurchasePortPropertyDescriptor(object);
			addSourcePurchasePortPropertyDescriptor(object);
			addProfitSharePropertyDescriptor(object);
			addVesselClassPropertyDescriptor(object);
			addHireCostPropertyDescriptor(object);
			addDaysFromSourcePropertyDescriptor(object);
		}
		return itemPropertyDescriptors;
	}

	/**
	 * This adds a property descriptor for the Base Sales Market Port feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addBaseSalesMarketPortPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_RedirectionPriceParameters_baseSalesMarketPort_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_RedirectionPriceParameters_baseSalesMarketPort_feature", "_UI_RedirectionPriceParameters_type"),
				 CommercialPackage.Literals.REDIRECTION_PRICE_PARAMETERS__BASE_SALES_MARKET_PORT,
				 true,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Base Sales Price Expression feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addBaseSalesPriceExpressionPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_RedirectionPriceParameters_baseSalesPriceExpression_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_RedirectionPriceParameters_baseSalesPriceExpression_feature", "_UI_RedirectionPriceParameters_type"),
				 CommercialPackage.Literals.REDIRECTION_PRICE_PARAMETERS__BASE_SALES_PRICE_EXPRESSION,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Base Purchase Price Expression feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addBasePurchasePriceExpressionPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_RedirectionPriceParameters_basePurchasePriceExpression_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_RedirectionPriceParameters_basePurchasePriceExpression_feature", "_UI_RedirectionPriceParameters_type"),
				 CommercialPackage.Literals.REDIRECTION_PRICE_PARAMETERS__BASE_PURCHASE_PRICE_EXPRESSION,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Notional Speed feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addNotionalSpeedPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_RedirectionPriceParameters_notionalSpeed_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_RedirectionPriceParameters_notionalSpeed_feature", "_UI_RedirectionPriceParameters_type"),
				 CommercialPackage.Literals.REDIRECTION_PRICE_PARAMETERS__NOTIONAL_SPEED,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.REAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Des Purchase Port feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addDesPurchasePortPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_RedirectionPriceParameters_desPurchasePort_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_RedirectionPriceParameters_desPurchasePort_feature", "_UI_RedirectionPriceParameters_type"),
				 CommercialPackage.Literals.REDIRECTION_PRICE_PARAMETERS__DES_PURCHASE_PORT,
				 true,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Source Purchase Port feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addSourcePurchasePortPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_RedirectionPriceParameters_sourcePurchasePort_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_RedirectionPriceParameters_sourcePurchasePort_feature", "_UI_RedirectionPriceParameters_type"),
				 CommercialPackage.Literals.REDIRECTION_PRICE_PARAMETERS__SOURCE_PURCHASE_PORT,
				 true,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Profit Share feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addProfitSharePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_RedirectionPriceParameters_profitShare_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_RedirectionPriceParameters_profitShare_feature", "_UI_RedirectionPriceParameters_type"),
				 CommercialPackage.Literals.REDIRECTION_PRICE_PARAMETERS__PROFIT_SHARE,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.REAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Vessel Class feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addVesselClassPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_RedirectionPriceParameters_vesselClass_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_RedirectionPriceParameters_vesselClass_feature", "_UI_RedirectionPriceParameters_type"),
				 CommercialPackage.Literals.REDIRECTION_PRICE_PARAMETERS__VESSEL_CLASS,
				 true,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Hire Cost feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addHireCostPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_RedirectionPriceParameters_hireCost_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_RedirectionPriceParameters_hireCost_feature", "_UI_RedirectionPriceParameters_type"),
				 CommercialPackage.Literals.REDIRECTION_PRICE_PARAMETERS__HIRE_COST,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Days From Source feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addDaysFromSourcePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_RedirectionPriceParameters_daysFromSource_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_RedirectionPriceParameters_daysFromSource_feature", "_UI_RedirectionPriceParameters_type"),
				 CommercialPackage.Literals.REDIRECTION_PRICE_PARAMETERS__DAYS_FROM_SOURCE,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This returns RedirectionPriceParameters.gif.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object getImage(Object object) {
		return overlayImage(object, getResourceLocator().getImage("full/obj16/RedirectionPriceParameters"));
	}

	/**
	 * This returns the label text for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getText(Object object) {
		String label = ((RedirectionPriceParameters)object).getUuid();
		return label == null || label.length() == 0 ?
			getString("_UI_RedirectionPriceParameters_type") :
			getString("_UI_RedirectionPriceParameters_type") + " " + label;
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

		switch (notification.getFeatureID(RedirectionPriceParameters.class)) {
			case CommercialPackage.REDIRECTION_PRICE_PARAMETERS__BASE_SALES_PRICE_EXPRESSION:
			case CommercialPackage.REDIRECTION_PRICE_PARAMETERS__BASE_PURCHASE_PRICE_EXPRESSION:
			case CommercialPackage.REDIRECTION_PRICE_PARAMETERS__NOTIONAL_SPEED:
			case CommercialPackage.REDIRECTION_PRICE_PARAMETERS__PROFIT_SHARE:
			case CommercialPackage.REDIRECTION_PRICE_PARAMETERS__HIRE_COST:
			case CommercialPackage.REDIRECTION_PRICE_PARAMETERS__DAYS_FROM_SOURCE:
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
