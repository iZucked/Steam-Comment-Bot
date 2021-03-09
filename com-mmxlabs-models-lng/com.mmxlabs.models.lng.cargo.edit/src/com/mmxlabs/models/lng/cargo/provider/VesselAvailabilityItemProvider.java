/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.cargo.provider;


import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ViewerNotification;

import com.mmxlabs.models.lng.cargo.CargoFactory;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.VesselAvailability;
import com.mmxlabs.models.lng.commercial.CommercialFactory;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.mmxcore.provider.UUIDObjectItemProvider;

/**
 * This is the item provider adapter for a {@link com.mmxlabs.models.lng.cargo.VesselAvailability} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class VesselAvailabilityItemProvider
	extends UUIDObjectItemProvider {
	/**
	 * This constructs an instance from a factory and a notifier.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public VesselAvailabilityItemProvider(AdapterFactory adapterFactory) {
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

			addFleetPropertyDescriptor(object);
			addOptionalPropertyDescriptor(object);
			addVesselPropertyDescriptor(object);
			addCharterNumberPropertyDescriptor(object);
			addEntityPropertyDescriptor(object);
			addTimeCharterRatePropertyDescriptor(object);
			addStartAtPropertyDescriptor(object);
			addStartAfterPropertyDescriptor(object);
			addStartByPropertyDescriptor(object);
			addEndAtPropertyDescriptor(object);
			addEndAfterPropertyDescriptor(object);
			addEndByPropertyDescriptor(object);
			addStartHeelPropertyDescriptor(object);
			addEndHeelPropertyDescriptor(object);
			addForceHireCostOnlyEndRulePropertyDescriptor(object);
			addCharterContractPropertyDescriptor(object);
			addMinDurationPropertyDescriptor(object);
			addMaxDurationPropertyDescriptor(object);
			addCharterContractOverridePropertyDescriptor(object);
		}
		return itemPropertyDescriptors;
	}

	/**
	 * This adds a property descriptor for the Fleet feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addFleetPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_VesselAvailability_fleet_feature"),
				 getString("_UI_VesselAvailability_fleet_description"),
				 CargoPackage.Literals.VESSEL_AVAILABILITY__FLEET,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Vessel feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addVesselPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_VesselAvailability_vessel_feature"),
				 getString("_UI_VesselAvailability_vessel_description"),
				 CargoPackage.Literals.VESSEL_AVAILABILITY__VESSEL,
				 true,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Time Charter Rate feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addTimeCharterRatePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_VesselAvailability_timeCharterRate_feature"),
				 getString("_UI_VesselAvailability_timeCharterRate_description"),
				 CargoPackage.Literals.VESSEL_AVAILABILITY__TIME_CHARTER_RATE,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Start At feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addStartAtPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_VesselAvailability_startAt_feature"),
				 getString("_UI_VesselAvailability_startAt_description"),
				 CargoPackage.Literals.VESSEL_AVAILABILITY__START_AT,
				 true,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Start After feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addStartAfterPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_VesselAvailability_startAfter_feature"),
				 getString("_UI_VesselAvailability_startAfter_description"),
				 CargoPackage.Literals.VESSEL_AVAILABILITY__START_AFTER,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Start By feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addStartByPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_VesselAvailability_startBy_feature"),
				 getString("_UI_VesselAvailability_startBy_description"),
				 CargoPackage.Literals.VESSEL_AVAILABILITY__START_BY,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the End At feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addEndAtPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_VesselAvailability_endAt_feature"),
				 getString("_UI_VesselAvailability_endAt_description"),
				 CargoPackage.Literals.VESSEL_AVAILABILITY__END_AT,
				 true,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the End After feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addEndAfterPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_VesselAvailability_endAfter_feature"),
				 getString("_UI_VesselAvailability_endAfter_description"),
				 CargoPackage.Literals.VESSEL_AVAILABILITY__END_AFTER,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the End By feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addEndByPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_VesselAvailability_endBy_feature"),
				 getString("_UI_VesselAvailability_endBy_description"),
				 CargoPackage.Literals.VESSEL_AVAILABILITY__END_BY,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Start Heel feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addStartHeelPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_VesselAvailability_startHeel_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_VesselAvailability_startHeel_feature", "_UI_VesselAvailability_type"),
				 CargoPackage.Literals.VESSEL_AVAILABILITY__START_HEEL,
				 false,
				 false,
				 false,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the End Heel feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addEndHeelPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_VesselAvailability_endHeel_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_VesselAvailability_endHeel_feature", "_UI_VesselAvailability_type"),
				 CargoPackage.Literals.VESSEL_AVAILABILITY__END_HEEL,
				 false,
				 false,
				 false,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Force Hire Cost Only End Rule feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addForceHireCostOnlyEndRulePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_VesselAvailability_forceHireCostOnlyEndRule_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_VesselAvailability_forceHireCostOnlyEndRule_feature", "_UI_VesselAvailability_type"),
				 CargoPackage.Literals.VESSEL_AVAILABILITY__FORCE_HIRE_COST_ONLY_END_RULE,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Optional feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addOptionalPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_VesselAvailability_optional_feature"),
				 getString("_UI_VesselAvailability_optional_description"),
				 CargoPackage.Literals.VESSEL_AVAILABILITY__OPTIONAL,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Charter Contract feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addCharterContractPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_VesselAvailability_charterContract_feature"),
				 getString("_UI_VesselAvailability_charterContract_description"),
				 CargoPackage.Literals.VESSEL_AVAILABILITY__CHARTER_CONTRACT,
				 true,
				 false,
				 true,
				 null,
				 null,
				 null));
	}
	/**
	 * This adds a property descriptor for the Min Duration feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addMinDurationPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_VesselAvailability_minDuration_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_VesselAvailability_minDuration_feature", "_UI_VesselAvailability_type"),
				 CargoPackage.Literals.VESSEL_AVAILABILITY__MIN_DURATION,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Max Duration feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addMaxDurationPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_VesselAvailability_maxDuration_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_VesselAvailability_maxDuration_feature", "_UI_VesselAvailability_type"),
				 CargoPackage.Literals.VESSEL_AVAILABILITY__MAX_DURATION,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Charter Contract Override feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addCharterContractOverridePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_VesselAvailability_charterContractOverride_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_VesselAvailability_charterContractOverride_feature", "_UI_VesselAvailability_type"),
				 CargoPackage.Literals.VESSEL_AVAILABILITY__CHARTER_CONTRACT_OVERRIDE,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Charter Number feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addCharterNumberPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_VesselAvailability_charterNumber_feature"),
				 getString("_UI_VesselAvailability_charterNumber_description"),
				 CargoPackage.Literals.VESSEL_AVAILABILITY__CHARTER_NUMBER,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Entity feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addEntityPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_VesselAvailability_entity_feature"),
				 getString("_UI_VesselAvailability_entity_description"),
				 CargoPackage.Literals.VESSEL_AVAILABILITY__ENTITY,
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
			childrenFeatures.add(CargoPackage.Literals.VESSEL_AVAILABILITY__START_HEEL);
			childrenFeatures.add(CargoPackage.Literals.VESSEL_AVAILABILITY__END_HEEL);
			childrenFeatures.add(CargoPackage.Literals.VESSEL_AVAILABILITY__CONTAINED_CHARTER_CONTRACT);
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
	 * This returns VesselAvailability.gif.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object getImage(Object object) {
		return overlayImage(object, getResourceLocator().getImage("full/obj16/VesselAvailability"));
	}

	/**
	 * This returns the label text for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	@Override
	public String getText(Object object) {
		String label = "";
		Vessel v = ((VesselAvailability) object).getVessel();
		uniqueAvailability((VesselAvailability) object);
		if(v != null){
			label = v.getName();		
		} else{
			label = "<unspecified>";			
		}
		return label;
	}
	
	private boolean uniqueAvailability(VesselAvailability va) {
		EObject eContainer = va.eContainer();
		EStructuralFeature eContainingFeature = va.eContainingFeature();
		EReference eContainmentFeature = va.eContainmentFeature();
		return false;
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

		switch (notification.getFeatureID(VesselAvailability.class)) {
			case CargoPackage.VESSEL_AVAILABILITY__FLEET:
			case CargoPackage.VESSEL_AVAILABILITY__OPTIONAL:
			case CargoPackage.VESSEL_AVAILABILITY__CHARTER_NUMBER:
			case CargoPackage.VESSEL_AVAILABILITY__TIME_CHARTER_RATE:
			case CargoPackage.VESSEL_AVAILABILITY__START_AFTER:
			case CargoPackage.VESSEL_AVAILABILITY__START_BY:
			case CargoPackage.VESSEL_AVAILABILITY__END_AFTER:
			case CargoPackage.VESSEL_AVAILABILITY__END_BY:
			case CargoPackage.VESSEL_AVAILABILITY__FORCE_HIRE_COST_ONLY_END_RULE:
			case CargoPackage.VESSEL_AVAILABILITY__MIN_DURATION:
			case CargoPackage.VESSEL_AVAILABILITY__MAX_DURATION:
			case CargoPackage.VESSEL_AVAILABILITY__CHARTER_CONTRACT_OVERRIDE:
				fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), false, true));
				return;
			case CargoPackage.VESSEL_AVAILABILITY__START_HEEL:
			case CargoPackage.VESSEL_AVAILABILITY__END_HEEL:
			case CargoPackage.VESSEL_AVAILABILITY__CONTAINED_CHARTER_CONTRACT:
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
				(CargoPackage.Literals.VESSEL_AVAILABILITY__START_HEEL,
				 CommercialFactory.eINSTANCE.createStartHeelOptions()));

		newChildDescriptors.add
			(createChildParameter
				(CargoPackage.Literals.VESSEL_AVAILABILITY__END_HEEL,
				 CommercialFactory.eINSTANCE.createEndHeelOptions()));

		newChildDescriptors.add
			(createChildParameter
				(CargoPackage.Literals.VESSEL_AVAILABILITY__CONTAINED_CHARTER_CONTRACT,
				 CommercialFactory.eINSTANCE.createGenericCharterContract()));
	}

}
