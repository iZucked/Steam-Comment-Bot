/**
 * Copyright (C) Minimax Labs Ltd., 2010
 * All rights reserved.
 */

package scenario.fleet.provider;


import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemColorProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.IStructuredItemContentProvider;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ItemProviderAdapter;
import org.eclipse.emf.edit.provider.ViewerNotification;

import scenario.fleet.FleetFactory;
import scenario.fleet.FleetPackage;
import scenario.fleet.VesselClass;
import scenario.provider.LngEditPlugin;

/**
 * This is the item provider adapter for a {@link scenario.fleet.VesselClass} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class VesselClassItemProvider
	extends ItemProviderAdapter
	implements
		IEditingDomainItemProvider, IStructuredItemContentProvider, ITreeItemContentProvider, IItemLabelProvider, IItemPropertySource, IItemColorProvider {
	/**
	 * This constructs an instance from a factory and a notifier.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public VesselClassItemProvider(AdapterFactory adapterFactory) {
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

			addNamePropertyDescriptor(object);
			addCapacityPropertyDescriptor(object);
			addMinSpeedPropertyDescriptor(object);
			addMaxSpeedPropertyDescriptor(object);
			addBaseFuelUnitPricePropertyDescriptor(object);
			addMinHeelVolumePropertyDescriptor(object);
			addFillCapacityPropertyDescriptor(object);
			addDailyCharterPricePropertyDescriptor(object);
			addSpotCharterCountPropertyDescriptor(object);
			addBaseFuelEquivalenceFactorPropertyDescriptor(object);
			addInaccessiblePortsPropertyDescriptor(object);
		}
		return itemPropertyDescriptors;
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
				 getString("_UI_VesselClass_name_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_VesselClass_name_feature", "_UI_VesselClass_type"),
				 FleetPackage.Literals.VESSEL_CLASS__NAME,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Capacity feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addCapacityPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_VesselClass_capacity_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_VesselClass_capacity_feature", "_UI_VesselClass_type"),
				 FleetPackage.Literals.VESSEL_CLASS__CAPACITY,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Min Speed feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addMinSpeedPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_VesselClass_minSpeed_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_VesselClass_minSpeed_feature", "_UI_VesselClass_type"),
				 FleetPackage.Literals.VESSEL_CLASS__MIN_SPEED,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.REAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Max Speed feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addMaxSpeedPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_VesselClass_maxSpeed_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_VesselClass_maxSpeed_feature", "_UI_VesselClass_type"),
				 FleetPackage.Literals.VESSEL_CLASS__MAX_SPEED,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.REAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Base Fuel Unit Price feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addBaseFuelUnitPricePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_VesselClass_baseFuelUnitPrice_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_VesselClass_baseFuelUnitPrice_feature", "_UI_VesselClass_type"),
				 FleetPackage.Literals.VESSEL_CLASS__BASE_FUEL_UNIT_PRICE,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Min Heel Volume feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addMinHeelVolumePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_VesselClass_minHeelVolume_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_VesselClass_minHeelVolume_feature", "_UI_VesselClass_type"),
				 FleetPackage.Literals.VESSEL_CLASS__MIN_HEEL_VOLUME,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Fill Capacity feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addFillCapacityPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_VesselClass_fillCapacity_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_VesselClass_fillCapacity_feature", "_UI_VesselClass_type"),
				 FleetPackage.Literals.VESSEL_CLASS__FILL_CAPACITY,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.REAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Daily Charter Price feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addDailyCharterPricePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_VesselClass_dailyCharterPrice_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_VesselClass_dailyCharterPrice_feature", "_UI_VesselClass_type"),
				 FleetPackage.Literals.VESSEL_CLASS__DAILY_CHARTER_PRICE,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Spot Charter Count feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addSpotCharterCountPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_VesselClass_spotCharterCount_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_VesselClass_spotCharterCount_feature", "_UI_VesselClass_type"),
				 FleetPackage.Literals.VESSEL_CLASS__SPOT_CHARTER_COUNT,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Base Fuel Equivalence Factor feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addBaseFuelEquivalenceFactorPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_VesselClass_baseFuelEquivalenceFactor_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_VesselClass_baseFuelEquivalenceFactor_feature", "_UI_VesselClass_type"),
				 FleetPackage.Literals.VESSEL_CLASS__BASE_FUEL_EQUIVALENCE_FACTOR,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.REAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Inaccessible Ports feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addInaccessiblePortsPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_VesselClass_inaccessiblePorts_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_VesselClass_inaccessiblePorts_feature", "_UI_VesselClass_type"),
				 FleetPackage.Literals.VESSEL_CLASS__INACCESSIBLE_PORTS,
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
			childrenFeatures.add(FleetPackage.Literals.VESSEL_CLASS__LADEN_ATTRIBUTES);
			childrenFeatures.add(FleetPackage.Literals.VESSEL_CLASS__BALLAST_ATTRIBUTES);
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
	 * This returns VesselClass.gif.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object getImage(Object object) {
		return overlayImage(object, getResourceLocator().getImage("full/obj16/VesselClass"));
	}

	/**
	 * This returns the label text for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getText(Object object) {
		String label = ((VesselClass)object).getName();
		return label == null || label.length() == 0 ?
			getString("_UI_VesselClass_type") :
			getString("_UI_VesselClass_type") + " " + label;
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

		switch (notification.getFeatureID(VesselClass.class)) {
			case FleetPackage.VESSEL_CLASS__NAME:
			case FleetPackage.VESSEL_CLASS__CAPACITY:
			case FleetPackage.VESSEL_CLASS__MIN_SPEED:
			case FleetPackage.VESSEL_CLASS__MAX_SPEED:
			case FleetPackage.VESSEL_CLASS__BASE_FUEL_UNIT_PRICE:
			case FleetPackage.VESSEL_CLASS__MIN_HEEL_VOLUME:
			case FleetPackage.VESSEL_CLASS__FILL_CAPACITY:
			case FleetPackage.VESSEL_CLASS__DAILY_CHARTER_PRICE:
			case FleetPackage.VESSEL_CLASS__SPOT_CHARTER_COUNT:
			case FleetPackage.VESSEL_CLASS__BASE_FUEL_EQUIVALENCE_FACTOR:
				fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), false, true));
				return;
			case FleetPackage.VESSEL_CLASS__LADEN_ATTRIBUTES:
			case FleetPackage.VESSEL_CLASS__BALLAST_ATTRIBUTES:
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
				(FleetPackage.Literals.VESSEL_CLASS__LADEN_ATTRIBUTES,
				 FleetFactory.eINSTANCE.createVesselStateAttributes()));

		newChildDescriptors.add
			(createChildParameter
				(FleetPackage.Literals.VESSEL_CLASS__BALLAST_ATTRIBUTES,
				 FleetFactory.eINSTANCE.createVesselStateAttributes()));
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
			childFeature == FleetPackage.Literals.VESSEL_CLASS__LADEN_ATTRIBUTES ||
			childFeature == FleetPackage.Literals.VESSEL_CLASS__BALLAST_ATTRIBUTES;

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
		return LngEditPlugin.INSTANCE;
	}

}
