/**
 */
package com.mmxlabs.models.lng.analytics.provider;


import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.SwapValueMatrixCargoResult;

import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.util.ResourceLocator;

import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IChildCreationExtender;
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
 * This is the item provider adapter for a {@link com.mmxlabs.models.lng.analytics.SwapValueMatrixCargoResult} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class SwapValueMatrixCargoResultItemProvider 
	extends ItemProviderAdapter
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
	public SwapValueMatrixCargoResultItemProvider(AdapterFactory adapterFactory) {
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

			addLoadPricePropertyDescriptor(object);
			addDischargePricePropertyDescriptor(object);
			addPurchaseCostPropertyDescriptor(object);
			addSalesRevenuePropertyDescriptor(object);
			addAdditionalPnlPropertyDescriptor(object);
			addTotalPnlPropertyDescriptor(object);
		}
		return itemPropertyDescriptors;
	}

	/**
	 * This adds a property descriptor for the Load Price feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addLoadPricePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_SwapValueMatrixCargoResult_loadPrice_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_SwapValueMatrixCargoResult_loadPrice_feature", "_UI_SwapValueMatrixCargoResult_type"),
				 AnalyticsPackage.Literals.SWAP_VALUE_MATRIX_CARGO_RESULT__LOAD_PRICE,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.REAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Discharge Price feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addDischargePricePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_SwapValueMatrixCargoResult_dischargePrice_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_SwapValueMatrixCargoResult_dischargePrice_feature", "_UI_SwapValueMatrixCargoResult_type"),
				 AnalyticsPackage.Literals.SWAP_VALUE_MATRIX_CARGO_RESULT__DISCHARGE_PRICE,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.REAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Purchase Cost feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addPurchaseCostPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_SwapValueMatrixCargoResult_purchaseCost_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_SwapValueMatrixCargoResult_purchaseCost_feature", "_UI_SwapValueMatrixCargoResult_type"),
				 AnalyticsPackage.Literals.SWAP_VALUE_MATRIX_CARGO_RESULT__PURCHASE_COST,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Sales Revenue feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addSalesRevenuePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_SwapValueMatrixCargoResult_salesRevenue_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_SwapValueMatrixCargoResult_salesRevenue_feature", "_UI_SwapValueMatrixCargoResult_type"),
				 AnalyticsPackage.Literals.SWAP_VALUE_MATRIX_CARGO_RESULT__SALES_REVENUE,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Additional Pnl feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addAdditionalPnlPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_SwapValueMatrixCargoResult_additionalPnl_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_SwapValueMatrixCargoResult_additionalPnl_feature", "_UI_SwapValueMatrixCargoResult_type"),
				 AnalyticsPackage.Literals.SWAP_VALUE_MATRIX_CARGO_RESULT__ADDITIONAL_PNL,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Total Pnl feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addTotalPnlPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_SwapValueMatrixCargoResult_totalPnl_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_SwapValueMatrixCargoResult_totalPnl_feature", "_UI_SwapValueMatrixCargoResult_type"),
				 AnalyticsPackage.Literals.SWAP_VALUE_MATRIX_CARGO_RESULT__TOTAL_PNL,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This returns the label text for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getText(Object object) {
		SwapValueMatrixCargoResult swapValueMatrixCargoResult = (SwapValueMatrixCargoResult)object;
		return getString("_UI_SwapValueMatrixCargoResult_type") + " " + swapValueMatrixCargoResult.getLoadPrice();
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

		switch (notification.getFeatureID(SwapValueMatrixCargoResult.class)) {
			case AnalyticsPackage.SWAP_VALUE_MATRIX_CARGO_RESULT__LOAD_PRICE:
			case AnalyticsPackage.SWAP_VALUE_MATRIX_CARGO_RESULT__DISCHARGE_PRICE:
			case AnalyticsPackage.SWAP_VALUE_MATRIX_CARGO_RESULT__PURCHASE_COST:
			case AnalyticsPackage.SWAP_VALUE_MATRIX_CARGO_RESULT__SALES_REVENUE:
			case AnalyticsPackage.SWAP_VALUE_MATRIX_CARGO_RESULT__ADDITIONAL_PNL:
			case AnalyticsPackage.SWAP_VALUE_MATRIX_CARGO_RESULT__TOTAL_PNL:
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
		return ((IChildCreationExtender)adapterFactory).getResourceLocator();
	}

}
