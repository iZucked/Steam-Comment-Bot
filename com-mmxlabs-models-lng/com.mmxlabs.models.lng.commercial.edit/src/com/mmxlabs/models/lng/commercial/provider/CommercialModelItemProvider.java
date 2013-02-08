/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.commercial.provider;


import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.IStructuredItemContentProvider;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;
import org.eclipse.emf.edit.provider.ViewerNotification;

import com.mmxlabs.models.lng.commercial.CommercialFactory;
import com.mmxlabs.models.lng.commercial.CommercialModel;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.types.TypesFactory;
import com.mmxlabs.models.mmxcore.MMXCoreFactory;
import com.mmxlabs.models.mmxcore.provider.UUIDObjectItemProvider;

/**
 * This is the item provider adapter for a {@link com.mmxlabs.models.lng.commercial.CommercialModel} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class CommercialModelItemProvider
	extends UUIDObjectItemProvider
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
	public CommercialModelItemProvider(AdapterFactory adapterFactory) {
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

			addShippingEntityPropertyDescriptor(object);
		}
		return itemPropertyDescriptors;
	}

	/**
	 * This adds a property descriptor for the Shipping Entity feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addShippingEntityPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_CommercialModel_shippingEntity_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_CommercialModel_shippingEntity_feature", "_UI_CommercialModel_type"),
				 CommercialPackage.Literals.COMMERCIAL_MODEL__SHIPPING_ENTITY,
				 true,
				 false,
				 true,
				 null,
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
			childrenFeatures.add(CommercialPackage.Literals.COMMERCIAL_MODEL__ENTITIES);
			childrenFeatures.add(CommercialPackage.Literals.COMMERCIAL_MODEL__SALES_CONTRACTS);
			childrenFeatures.add(CommercialPackage.Literals.COMMERCIAL_MODEL__PURCHASE_CONTRACTS);
			childrenFeatures.add(CommercialPackage.Literals.COMMERCIAL_MODEL__CONTRACT_SLOT_EXTENSIONS);
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
	 * This returns CommercialModel.gif.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object getImage(Object object) {
		return overlayImage(object, getResourceLocator().getImage("full/obj16/CommercialModel"));
	}

	/**
	 * This returns the label text for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getText(Object object) {
		String label = ((CommercialModel)object).getUuid();
		return label == null || label.length() == 0 ?
			getString("_UI_CommercialModel_type") :
			getString("_UI_CommercialModel_type") + " " + label;
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

		switch (notification.getFeatureID(CommercialModel.class)) {
			case CommercialPackage.COMMERCIAL_MODEL__ENTITIES:
			case CommercialPackage.COMMERCIAL_MODEL__SALES_CONTRACTS:
			case CommercialPackage.COMMERCIAL_MODEL__PURCHASE_CONTRACTS:
			case CommercialPackage.COMMERCIAL_MODEL__CONTRACT_SLOT_EXTENSIONS:
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
				(CommercialPackage.Literals.COMMERCIAL_MODEL__ENTITIES,
				 CommercialFactory.eINSTANCE.createLegalEntity()));

		newChildDescriptors.add
			(createChildParameter
				(CommercialPackage.Literals.COMMERCIAL_MODEL__SALES_CONTRACTS,
				 CommercialFactory.eINSTANCE.createSalesContract()));

		newChildDescriptors.add
			(createChildParameter
				(CommercialPackage.Literals.COMMERCIAL_MODEL__SALES_CONTRACTS,
				 CommercialFactory.eINSTANCE.createFixedPriceContract()));

		newChildDescriptors.add
			(createChildParameter
				(CommercialPackage.Literals.COMMERCIAL_MODEL__SALES_CONTRACTS,
				 CommercialFactory.eINSTANCE.createIndexPriceContract()));

		newChildDescriptors.add
			(createChildParameter
				(CommercialPackage.Literals.COMMERCIAL_MODEL__SALES_CONTRACTS,
				 CommercialFactory.eINSTANCE.createPriceExpressionContract()));

		newChildDescriptors.add
			(createChildParameter
				(CommercialPackage.Literals.COMMERCIAL_MODEL__PURCHASE_CONTRACTS,
				 CommercialFactory.eINSTANCE.createPurchaseContract()));

		newChildDescriptors.add
			(createChildParameter
				(CommercialPackage.Literals.COMMERCIAL_MODEL__PURCHASE_CONTRACTS,
				 CommercialFactory.eINSTANCE.createFixedPriceContract()));

		newChildDescriptors.add
			(createChildParameter
				(CommercialPackage.Literals.COMMERCIAL_MODEL__PURCHASE_CONTRACTS,
				 CommercialFactory.eINSTANCE.createIndexPriceContract()));

		newChildDescriptors.add
			(createChildParameter
				(CommercialPackage.Literals.COMMERCIAL_MODEL__PURCHASE_CONTRACTS,
				 CommercialFactory.eINSTANCE.createNetbackPurchaseContract()));

		newChildDescriptors.add
			(createChildParameter
				(CommercialPackage.Literals.COMMERCIAL_MODEL__PURCHASE_CONTRACTS,
				 CommercialFactory.eINSTANCE.createProfitSharePurchaseContract()));

		newChildDescriptors.add
			(createChildParameter
				(CommercialPackage.Literals.COMMERCIAL_MODEL__PURCHASE_CONTRACTS,
				 CommercialFactory.eINSTANCE.createRedirectionPurchaseContract()));

		newChildDescriptors.add
			(createChildParameter
				(CommercialPackage.Literals.COMMERCIAL_MODEL__PURCHASE_CONTRACTS,
				 CommercialFactory.eINSTANCE.createPriceExpressionContract()));

		newChildDescriptors.add
			(createChildParameter
				(CommercialPackage.Literals.COMMERCIAL_MODEL__CONTRACT_SLOT_EXTENSIONS,
				 CommercialFactory.eINSTANCE.createCommercialModel()));

		newChildDescriptors.add
			(createChildParameter
				(CommercialPackage.Literals.COMMERCIAL_MODEL__CONTRACT_SLOT_EXTENSIONS,
				 CommercialFactory.eINSTANCE.createLegalEntity()));

		newChildDescriptors.add
			(createChildParameter
				(CommercialPackage.Literals.COMMERCIAL_MODEL__CONTRACT_SLOT_EXTENSIONS,
				 CommercialFactory.eINSTANCE.createContract()));

		newChildDescriptors.add
			(createChildParameter
				(CommercialPackage.Literals.COMMERCIAL_MODEL__CONTRACT_SLOT_EXTENSIONS,
				 CommercialFactory.eINSTANCE.createSalesContract()));

		newChildDescriptors.add
			(createChildParameter
				(CommercialPackage.Literals.COMMERCIAL_MODEL__CONTRACT_SLOT_EXTENSIONS,
				 CommercialFactory.eINSTANCE.createPurchaseContract()));

		newChildDescriptors.add
			(createChildParameter
				(CommercialPackage.Literals.COMMERCIAL_MODEL__CONTRACT_SLOT_EXTENSIONS,
				 CommercialFactory.eINSTANCE.createFixedPriceContract()));

		newChildDescriptors.add
			(createChildParameter
				(CommercialPackage.Literals.COMMERCIAL_MODEL__CONTRACT_SLOT_EXTENSIONS,
				 CommercialFactory.eINSTANCE.createIndexPriceContract()));

		newChildDescriptors.add
			(createChildParameter
				(CommercialPackage.Literals.COMMERCIAL_MODEL__CONTRACT_SLOT_EXTENSIONS,
				 CommercialFactory.eINSTANCE.createNetbackPurchaseContract()));

		newChildDescriptors.add
			(createChildParameter
				(CommercialPackage.Literals.COMMERCIAL_MODEL__CONTRACT_SLOT_EXTENSIONS,
				 CommercialFactory.eINSTANCE.createProfitSharePurchaseContract()));

		newChildDescriptors.add
			(createChildParameter
				(CommercialPackage.Literals.COMMERCIAL_MODEL__CONTRACT_SLOT_EXTENSIONS,
				 CommercialFactory.eINSTANCE.createRedirectionPurchaseContract()));

		newChildDescriptors.add
			(createChildParameter
				(CommercialPackage.Literals.COMMERCIAL_MODEL__CONTRACT_SLOT_EXTENSIONS,
				 CommercialFactory.eINSTANCE.createPriceExpressionContract()));

		newChildDescriptors.add
			(createChildParameter
				(CommercialPackage.Literals.COMMERCIAL_MODEL__CONTRACT_SLOT_EXTENSIONS,
				 CommercialFactory.eINSTANCE.createRedirectionContractOriginalDate()));

		newChildDescriptors.add
			(createChildParameter
				(CommercialPackage.Literals.COMMERCIAL_MODEL__CONTRACT_SLOT_EXTENSIONS,
				 CommercialFactory.eINSTANCE.createFixedPriceParameters()));

		newChildDescriptors.add
			(createChildParameter
				(CommercialPackage.Literals.COMMERCIAL_MODEL__CONTRACT_SLOT_EXTENSIONS,
				 CommercialFactory.eINSTANCE.createIndexPriceParameters()));

		newChildDescriptors.add
			(createChildParameter
				(CommercialPackage.Literals.COMMERCIAL_MODEL__CONTRACT_SLOT_EXTENSIONS,
				 CommercialFactory.eINSTANCE.createExpressionPriceParameters()));

		newChildDescriptors.add
			(createChildParameter
				(CommercialPackage.Literals.COMMERCIAL_MODEL__CONTRACT_SLOT_EXTENSIONS,
				 CommercialFactory.eINSTANCE.createNetbackPriceParameters()));

		newChildDescriptors.add
			(createChildParameter
				(CommercialPackage.Literals.COMMERCIAL_MODEL__CONTRACT_SLOT_EXTENSIONS,
				 CommercialFactory.eINSTANCE.createProfitSharePriceParameters()));

		newChildDescriptors.add
			(createChildParameter
				(CommercialPackage.Literals.COMMERCIAL_MODEL__CONTRACT_SLOT_EXTENSIONS,
				 CommercialFactory.eINSTANCE.createRedirectionPriceParameters()));

		newChildDescriptors.add
			(createChildParameter
				(CommercialPackage.Literals.COMMERCIAL_MODEL__CONTRACT_SLOT_EXTENSIONS,
				 MMXCoreFactory.eINSTANCE.createUUIDObject()));

		newChildDescriptors.add
			(createChildParameter
				(CommercialPackage.Literals.COMMERCIAL_MODEL__CONTRACT_SLOT_EXTENSIONS,
				 MMXCoreFactory.eINSTANCE.createMMXRootObject()));
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
			childFeature == CommercialPackage.Literals.COMMERCIAL_MODEL__ENTITIES ||
			childFeature == CommercialPackage.Literals.COMMERCIAL_MODEL__CONTRACT_SLOT_EXTENSIONS ||
			childFeature == CommercialPackage.Literals.COMMERCIAL_MODEL__SALES_CONTRACTS ||
			childFeature == CommercialPackage.Literals.COMMERCIAL_MODEL__PURCHASE_CONTRACTS;

		if (qualify) {
			return getString
				("_UI_CreateChild_text2",
				 new Object[] { getTypeText(childObject), getFeatureText(childFeature), getTypeText(owner) });
		}
		return super.getCreateChildText(owner, feature, child, selection);
	}

	/**
	 * Return the resource locator for this item provider's resources.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ResourceLocator getResourceLocator() {
		return CommercialEditPlugin.INSTANCE;
	}

}
