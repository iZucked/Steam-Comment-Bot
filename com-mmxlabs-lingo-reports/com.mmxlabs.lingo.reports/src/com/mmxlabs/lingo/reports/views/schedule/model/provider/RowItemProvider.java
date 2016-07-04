/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.lingo.reports.views.schedule.model.provider;


import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.IStructuredItemContentProvider;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ItemProviderAdapter;
import org.eclipse.emf.edit.provider.ViewerNotification;

import com.mmxlabs.lingo.reports.internal.Activator;
import com.mmxlabs.lingo.reports.views.schedule.model.Row;
import com.mmxlabs.lingo.reports.views.schedule.model.ScheduleReportPackage;

/**
 * This is the item provider adapter for a {@link com.mmxlabs.lingo.reports.views.schedule.model.Row} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class RowItemProvider
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
	public RowItemProvider(AdapterFactory adapterFactory) {
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

			addVisiblePropertyDescriptor(object);
			addCycleGroupPropertyDescriptor(object);
			addInputEquivalentsPropertyDescriptor(object);
			addNamePropertyDescriptor(object);
			addName2PropertyDescriptor(object);
			addTargetPropertyDescriptor(object);
			addSchedulePropertyDescriptor(object);
			addSequencePropertyDescriptor(object);
			addCargoAllocationPropertyDescriptor(object);
			addLoadAllocationPropertyDescriptor(object);
			addDischargeAllocationPropertyDescriptor(object);
			addOpenSlotAllocationPropertyDescriptor(object);
			addReferenceRowPropertyDescriptor(object);
			addReferringRowsPropertyDescriptor(object);
			addReferencePropertyDescriptor(object);
			addRowGroupPropertyDescriptor(object);
			addScenarioPropertyDescriptor(object);
		}
		return itemPropertyDescriptors;
	}

	/**
	 * This adds a property descriptor for the Visible feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addVisiblePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Row_visible_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_Row_visible_feature", "_UI_Row_type"),
				 ScheduleReportPackage.Literals.ROW__VISIBLE,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Cycle Group feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addCycleGroupPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Row_cycleGroup_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_Row_cycleGroup_feature", "_UI_Row_type"),
				 ScheduleReportPackage.Literals.ROW__CYCLE_GROUP,
				 true,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Input Equivalents feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addInputEquivalentsPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Row_inputEquivalents_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_Row_inputEquivalents_feature", "_UI_Row_type"),
				 ScheduleReportPackage.Literals.ROW__INPUT_EQUIVALENTS,
				 true,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Name feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addNamePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Row_name_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_Row_name_feature", "_UI_Row_type"),
				 ScheduleReportPackage.Literals.ROW__NAME,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Name2 feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addName2PropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Row_name2_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_Row_name2_feature", "_UI_Row_type"),
				 ScheduleReportPackage.Literals.ROW__NAME2,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Target feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addTargetPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Row_target_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_Row_target_feature", "_UI_Row_type"),
				 ScheduleReportPackage.Literals.ROW__TARGET,
				 true,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Schedule feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addSchedulePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Row_schedule_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_Row_schedule_feature", "_UI_Row_type"),
				 ScheduleReportPackage.Literals.ROW__SCHEDULE,
				 true,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Sequence feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addSequencePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Row_sequence_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_Row_sequence_feature", "_UI_Row_type"),
				 ScheduleReportPackage.Literals.ROW__SEQUENCE,
				 true,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Cargo Allocation feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addCargoAllocationPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Row_cargoAllocation_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_Row_cargoAllocation_feature", "_UI_Row_type"),
				 ScheduleReportPackage.Literals.ROW__CARGO_ALLOCATION,
				 true,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Load Allocation feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addLoadAllocationPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Row_loadAllocation_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_Row_loadAllocation_feature", "_UI_Row_type"),
				 ScheduleReportPackage.Literals.ROW__LOAD_ALLOCATION,
				 true,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Discharge Allocation feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addDischargeAllocationPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Row_dischargeAllocation_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_Row_dischargeAllocation_feature", "_UI_Row_type"),
				 ScheduleReportPackage.Literals.ROW__DISCHARGE_ALLOCATION,
				 true,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Open Slot Allocation feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addOpenSlotAllocationPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Row_openSlotAllocation_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_Row_openSlotAllocation_feature", "_UI_Row_type"),
				 ScheduleReportPackage.Literals.ROW__OPEN_SLOT_ALLOCATION,
				 true,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Reference Row feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addReferenceRowPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Row_referenceRow_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_Row_referenceRow_feature", "_UI_Row_type"),
				 ScheduleReportPackage.Literals.ROW__REFERENCE_ROW,
				 true,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Referring Rows feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addReferringRowsPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Row_referringRows_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_Row_referringRows_feature", "_UI_Row_type"),
				 ScheduleReportPackage.Literals.ROW__REFERRING_ROWS,
				 true,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Reference feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addReferencePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Row_reference_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_Row_reference_feature", "_UI_Row_type"),
				 ScheduleReportPackage.Literals.ROW__REFERENCE,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Row Group feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addRowGroupPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Row_rowGroup_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_Row_rowGroup_feature", "_UI_Row_type"),
				 ScheduleReportPackage.Literals.ROW__ROW_GROUP,
				 true,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Scenario feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addScenarioPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Row_scenario_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_Row_scenario_feature", "_UI_Row_type"),
				 ScheduleReportPackage.Literals.ROW__SCENARIO,
				 true,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This returns Row.gif.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public Object getImage(Object object) {
		if (object instanceof Row) {
			final Row row = (Row) object;
			if (row.isReference()) {
				return overlayImage(object, getResourceLocator().getImage("full/obj16/PinnedRow"));
			}
		}
		return null;
	}

	/**
	 * This returns the label text for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getText(Object object) {
		String label = ((Row)object).getName();
		return label == null || label.length() == 0 ?
			getString("_UI_Row_type") :
			getString("_UI_Row_type") + " " + label;
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

		switch (notification.getFeatureID(Row.class)) {
			case ScheduleReportPackage.ROW__VISIBLE:
			case ScheduleReportPackage.ROW__CYCLE_GROUP:
			case ScheduleReportPackage.ROW__INPUT_EQUIVALENTS:
			case ScheduleReportPackage.ROW__NAME:
			case ScheduleReportPackage.ROW__NAME2:
			case ScheduleReportPackage.ROW__TARGET:
			case ScheduleReportPackage.ROW__SCHEDULE:
			case ScheduleReportPackage.ROW__CARGO_ALLOCATION:
			case ScheduleReportPackage.ROW__LOAD_ALLOCATION:
			case ScheduleReportPackage.ROW__DISCHARGE_ALLOCATION:
			case ScheduleReportPackage.ROW__OPEN_SLOT_ALLOCATION:
			case ScheduleReportPackage.ROW__REFERENCE_ROW:
			case ScheduleReportPackage.ROW__REFERRING_ROWS:
			case ScheduleReportPackage.ROW__REFERENCE:
			case ScheduleReportPackage.ROW__ROW_GROUP:
			case ScheduleReportPackage.ROW__SCENARIO:
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
		return Activator.INSTANCE;
	}

}
