/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.fleet.provider;


import com.mmxlabs.models.lng.fleet.CIIGradeBoundary;
import com.mmxlabs.models.lng.fleet.FleetPackage;

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
 * This is the item provider adapter for a {@link com.mmxlabs.models.lng.fleet.CIIGradeBoundary} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class CIIGradeBoundaryItemProvider 
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
	public CIIGradeBoundaryItemProvider(AdapterFactory adapterFactory) {
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

			addDwtUpperLimitPropertyDescriptor(object);
			addGradePropertyDescriptor(object);
			addUpperLimitPropertyDescriptor(object);
			addGradeAPropertyDescriptor(object);
			addGradeAValuePropertyDescriptor(object);
			addGradeBPropertyDescriptor(object);
			addGradeBValuePropertyDescriptor(object);
			addGradeCPropertyDescriptor(object);
			addGradeCValuePropertyDescriptor(object);
			addGradeDPropertyDescriptor(object);
			addGradeDValuePropertyDescriptor(object);
		}
		return itemPropertyDescriptors;
	}

	/**
	 * This adds a property descriptor for the Dwt Upper Limit feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addDwtUpperLimitPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_CIIGradeBoundary_dwtUpperLimit_feature"),
				 getString("_UI_CIIGradeBoundary_dwtUpperLimit_description"),
				 FleetPackage.Literals.CII_GRADE_BOUNDARY__DWT_UPPER_LIMIT,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Grade feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addGradePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_CIIGradeBoundary_grade_feature"),
				 getString("_UI_CIIGradeBoundary_grade_description"),
				 FleetPackage.Literals.CII_GRADE_BOUNDARY__GRADE,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Upper Limit feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addUpperLimitPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_CIIGradeBoundary_upperLimit_feature"),
				 getString("_UI_CIIGradeBoundary_upperLimit_description"),
				 FleetPackage.Literals.CII_GRADE_BOUNDARY__UPPER_LIMIT,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.REAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Grade A feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addGradeAPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_CIIGradeBoundary_gradeA_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_CIIGradeBoundary_gradeA_feature", "_UI_CIIGradeBoundary_type"),
				 FleetPackage.Literals.CII_GRADE_BOUNDARY__GRADE_A,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Grade AValue feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addGradeAValuePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_CIIGradeBoundary_gradeAValue_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_CIIGradeBoundary_gradeAValue_feature", "_UI_CIIGradeBoundary_type"),
				 FleetPackage.Literals.CII_GRADE_BOUNDARY__GRADE_AVALUE,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.REAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Grade B feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addGradeBPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_CIIGradeBoundary_gradeB_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_CIIGradeBoundary_gradeB_feature", "_UI_CIIGradeBoundary_type"),
				 FleetPackage.Literals.CII_GRADE_BOUNDARY__GRADE_B,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Grade BValue feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addGradeBValuePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_CIIGradeBoundary_gradeBValue_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_CIIGradeBoundary_gradeBValue_feature", "_UI_CIIGradeBoundary_type"),
				 FleetPackage.Literals.CII_GRADE_BOUNDARY__GRADE_BVALUE,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.REAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Grade C feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addGradeCPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_CIIGradeBoundary_gradeC_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_CIIGradeBoundary_gradeC_feature", "_UI_CIIGradeBoundary_type"),
				 FleetPackage.Literals.CII_GRADE_BOUNDARY__GRADE_C,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Grade CValue feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addGradeCValuePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_CIIGradeBoundary_gradeCValue_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_CIIGradeBoundary_gradeCValue_feature", "_UI_CIIGradeBoundary_type"),
				 FleetPackage.Literals.CII_GRADE_BOUNDARY__GRADE_CVALUE,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.REAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Grade D feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addGradeDPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_CIIGradeBoundary_gradeD_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_CIIGradeBoundary_gradeD_feature", "_UI_CIIGradeBoundary_type"),
				 FleetPackage.Literals.CII_GRADE_BOUNDARY__GRADE_D,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Grade DValue feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addGradeDValuePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_CIIGradeBoundary_gradeDValue_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_CIIGradeBoundary_gradeDValue_feature", "_UI_CIIGradeBoundary_type"),
				 FleetPackage.Literals.CII_GRADE_BOUNDARY__GRADE_DVALUE,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.REAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This returns CIIGradeBoundary.gif.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object getImage(Object object) {
		return overlayImage(object, getResourceLocator().getImage("full/obj16/CIIGradeBoundary"));
	}

	/**
	 * This returns the label text for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getText(Object object) {
		CIIGradeBoundary ciiGradeBoundary = (CIIGradeBoundary)object;
		return getString("_UI_CIIGradeBoundary_type") + " " + ciiGradeBoundary.getDwtUpperLimit();
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

		switch (notification.getFeatureID(CIIGradeBoundary.class)) {
			case FleetPackage.CII_GRADE_BOUNDARY__DWT_UPPER_LIMIT:
			case FleetPackage.CII_GRADE_BOUNDARY__GRADE:
			case FleetPackage.CII_GRADE_BOUNDARY__UPPER_LIMIT:
			case FleetPackage.CII_GRADE_BOUNDARY__GRADE_A:
			case FleetPackage.CII_GRADE_BOUNDARY__GRADE_AVALUE:
			case FleetPackage.CII_GRADE_BOUNDARY__GRADE_B:
			case FleetPackage.CII_GRADE_BOUNDARY__GRADE_BVALUE:
			case FleetPackage.CII_GRADE_BOUNDARY__GRADE_C:
			case FleetPackage.CII_GRADE_BOUNDARY__GRADE_CVALUE:
			case FleetPackage.CII_GRADE_BOUNDARY__GRADE_D:
			case FleetPackage.CII_GRADE_BOUNDARY__GRADE_DVALUE:
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
