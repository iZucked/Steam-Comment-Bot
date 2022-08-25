/**
 */
package com.mmxlabs.models.lng.transfers.provider;


import com.mmxlabs.models.lng.transfers.TransferRecord;
import com.mmxlabs.models.lng.transfers.TransfersPackage;

import com.mmxlabs.models.mmxcore.MMXCorePackage;

import com.mmxlabs.models.mmxcore.provider.NamedObjectItemProvider;

import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ViewerNotification;

/**
 * This is the item provider adapter for a {@link com.mmxlabs.models.lng.transfers.TransferRecord} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class TransferRecordItemProvider extends NamedObjectItemProvider {
	/**
	 * This constructs an instance from a factory and a notifier.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public TransferRecordItemProvider(AdapterFactory adapterFactory) {
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

			addUuidPropertyDescriptor(object);
			addTransferAgreementPropertyDescriptor(object);
			addLhsPropertyDescriptor(object);
			addRhsPropertyDescriptor(object);
			addCargoReleaseDatePropertyDescriptor(object);
			addPriceExpressionPropertyDescriptor(object);
			addPricingDatePropertyDescriptor(object);
			addIncotermPropertyDescriptor(object);
			addStatusPropertyDescriptor(object);
			addNotesPropertyDescriptor(object);
			addStalePropertyDescriptor(object);
			addPricingBasisPropertyDescriptor(object);
		}
		return itemPropertyDescriptors;
	}

	/**
	 * This adds a property descriptor for the Uuid feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addUuidPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_UUIDObject_uuid_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_UUIDObject_uuid_feature", "_UI_UUIDObject_type"),
				 MMXCorePackage.Literals.UUID_OBJECT__UUID,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Transfer Agreement feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addTransferAgreementPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_TransferRecord_transferAgreement_feature"),
				 getString("_UI_TransferRecord_transferAgreement_description"),
				 TransfersPackage.Literals.TRANSFER_RECORD__TRANSFER_AGREEMENT,
				 true,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Lhs feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addLhsPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_TransferRecord_lhs_feature"),
				 getString("_UI_TransferRecord_lhs_description"),
				 TransfersPackage.Literals.TRANSFER_RECORD__LHS,
				 true,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Rhs feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addRhsPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_TransferRecord_rhs_feature"),
				 getString("_UI_TransferRecord_rhs_description"),
				 TransfersPackage.Literals.TRANSFER_RECORD__RHS,
				 true,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Cargo Release Date feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addCargoReleaseDatePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_TransferRecord_cargoReleaseDate_feature"),
				 getString("_UI_TransferRecord_cargoReleaseDate_description"),
				 TransfersPackage.Literals.TRANSFER_RECORD__CARGO_RELEASE_DATE,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Price Expression feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addPriceExpressionPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_TransferRecord_priceExpression_feature"),
				 getString("_UI_TransferRecord_priceExpression_description"),
				 TransfersPackage.Literals.TRANSFER_RECORD__PRICE_EXPRESSION,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Pricing Date feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addPricingDatePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_TransferRecord_pricingDate_feature"),
				 getString("_UI_TransferRecord_pricingDate_description"),
				 TransfersPackage.Literals.TRANSFER_RECORD__PRICING_DATE,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Incoterm feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addIncotermPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_TransferRecord_incoterm_feature"),
				 getString("_UI_TransferRecord_incoterm_description"),
				 TransfersPackage.Literals.TRANSFER_RECORD__INCOTERM,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Status feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addStatusPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_TransferRecord_status_feature"),
				 getString("_UI_TransferRecord_status_description"),
				 TransfersPackage.Literals.TRANSFER_RECORD__STATUS,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Notes feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addNotesPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_TransferRecord_notes_feature"),
				 getString("_UI_TransferRecord_notes_description"),
				 TransfersPackage.Literals.TRANSFER_RECORD__NOTES,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Stale feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addStalePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_TransferRecord_stale_feature"),
				 getString("_UI_TransferRecord_stale_description"),
				 TransfersPackage.Literals.TRANSFER_RECORD__STALE,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Pricing Basis feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addPricingBasisPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_TransferRecord_pricingBasis_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_TransferRecord_pricingBasis_feature", "_UI_TransferRecord_type"),
				 TransfersPackage.Literals.TRANSFER_RECORD__PRICING_BASIS,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This returns TransferRecord.gif.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object getImage(Object object) {
		return overlayImage(object, getResourceLocator().getImage("full/obj16/TransferRecord"));
	}

	/**
	 * This returns the label text for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getText(Object object) {
		String label = ((TransferRecord)object).getName();
		return label == null || label.length() == 0 ?
			getString("_UI_TransferRecord_type") :
			getString("_UI_TransferRecord_type") + " " + label;
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

		switch (notification.getFeatureID(TransferRecord.class)) {
			case TransfersPackage.TRANSFER_RECORD__UUID:
			case TransfersPackage.TRANSFER_RECORD__CARGO_RELEASE_DATE:
			case TransfersPackage.TRANSFER_RECORD__PRICE_EXPRESSION:
			case TransfersPackage.TRANSFER_RECORD__PRICING_DATE:
			case TransfersPackage.TRANSFER_RECORD__INCOTERM:
			case TransfersPackage.TRANSFER_RECORD__STATUS:
			case TransfersPackage.TRANSFER_RECORD__NOTES:
			case TransfersPackage.TRANSFER_RECORD__STALE:
			case TransfersPackage.TRANSFER_RECORD__PRICING_BASIS:
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
