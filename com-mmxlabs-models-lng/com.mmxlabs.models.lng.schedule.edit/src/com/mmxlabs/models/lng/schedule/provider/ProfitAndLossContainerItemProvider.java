/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.schedule.provider;


import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ViewerNotification;

import com.mmxlabs.models.lng.schedule.ProfitAndLossContainer;
import com.mmxlabs.models.lng.schedule.ScheduleFactory;
import com.mmxlabs.models.lng.schedule.SchedulePackage;
import com.mmxlabs.models.mmxcore.provider.MMXObjectItemProvider;

/**
 * This is the item provider adapter for a {@link com.mmxlabs.models.lng.schedule.ProfitAndLossContainer} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class ProfitAndLossContainerItemProvider
	extends MMXObjectItemProvider {
	/**
	 * This constructs an instance from a factory and a notifier.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ProfitAndLossContainerItemProvider(AdapterFactory adapterFactory) {
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

			addGroupProfitAndLossPropertyDescriptor(object);
		}
		return itemPropertyDescriptors;
	}

	/**
	 * This adds a property descriptor for the Group Profit And Loss feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addGroupProfitAndLossPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_ProfitAndLossContainer_groupProfitAndLoss_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_ProfitAndLossContainer_groupProfitAndLoss_feature", "_UI_ProfitAndLossContainer_type"),
				 SchedulePackage.Literals.PROFIT_AND_LOSS_CONTAINER__GROUP_PROFIT_AND_LOSS,
				 false,
				 false,
				 false,
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
			childrenFeatures.add(SchedulePackage.Literals.PROFIT_AND_LOSS_CONTAINER__GROUP_PROFIT_AND_LOSS);
			childrenFeatures.add(SchedulePackage.Literals.PROFIT_AND_LOSS_CONTAINER__GENERAL_PNL_DETAILS);
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
	 * This returns ProfitAndLossContainer.gif.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object getImage(Object object) {
		return overlayImage(object, getResourceLocator().getImage("full/obj16/ProfitAndLossContainer"));
	}

	/**
	 * This returns the label text for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getText(Object object) {
		return getString("_UI_ProfitAndLossContainer_type");
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

		switch (notification.getFeatureID(ProfitAndLossContainer.class)) {
			case SchedulePackage.PROFIT_AND_LOSS_CONTAINER__GROUP_PROFIT_AND_LOSS:
			case SchedulePackage.PROFIT_AND_LOSS_CONTAINER__GENERAL_PNL_DETAILS:
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
				(SchedulePackage.Literals.PROFIT_AND_LOSS_CONTAINER__GROUP_PROFIT_AND_LOSS,
				 ScheduleFactory.eINSTANCE.createGroupProfitAndLoss()));

		newChildDescriptors.add
			(createChildParameter
				(SchedulePackage.Literals.PROFIT_AND_LOSS_CONTAINER__GENERAL_PNL_DETAILS,
				 ScheduleFactory.eINSTANCE.createEntityPNLDetails()));

		newChildDescriptors.add
			(createChildParameter
				(SchedulePackage.Literals.PROFIT_AND_LOSS_CONTAINER__GENERAL_PNL_DETAILS,
				 ScheduleFactory.eINSTANCE.createSlotPNLDetails()));

		newChildDescriptors.add
			(createChildParameter
				(SchedulePackage.Literals.PROFIT_AND_LOSS_CONTAINER__GENERAL_PNL_DETAILS,
				 ScheduleFactory.eINSTANCE.createBasicSlotPNLDetails()));
	}

}
