/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
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
import com.mmxlabs.models.lng.cargo.VesselCharter;
import com.mmxlabs.models.lng.commercial.CommercialFactory;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.mmxcore.provider.UUIDObjectItemProvider;

/**
 * This is the item provider adapter for a {@link com.mmxlabs.models.lng.cargo.VesselCharter} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class VesselCharterItemProvider
	extends UUIDObjectItemProvider {
	/**
	 * This constructs an instance from a factory and a notifier.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public VesselCharterItemProvider(AdapterFactory adapterFactory) {
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
			addForceHireCostOnlyEndRulePropertyDescriptor(object);
			addMinDurationPropertyDescriptor(object);
			addMaxDurationPropertyDescriptor(object);
			addCharterContractOverridePropertyDescriptor(object);
			addGenericCharterContractPropertyDescriptor(object);
		}
		return itemPropertyDescriptors;
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
				 getString("_UI_VesselCharter_vessel_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_VesselCharter_vessel_feature", "_UI_VesselCharter_type"),
				 CargoPackage.Literals.VESSEL_CHARTER__VESSEL,
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
				 getString("_UI_VesselCharter_timeCharterRate_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_VesselCharter_timeCharterRate_feature", "_UI_VesselCharter_type"),
				 CargoPackage.Literals.VESSEL_CHARTER__TIME_CHARTER_RATE,
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
				 getString("_UI_VesselCharter_startAt_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_VesselCharter_startAt_feature", "_UI_VesselCharter_type"),
				 CargoPackage.Literals.VESSEL_CHARTER__START_AT,
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
				 getString("_UI_VesselCharter_startAfter_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_VesselCharter_startAfter_feature", "_UI_VesselCharter_type"),
				 CargoPackage.Literals.VESSEL_CHARTER__START_AFTER,
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
				 getString("_UI_VesselCharter_startBy_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_VesselCharter_startBy_feature", "_UI_VesselCharter_type"),
				 CargoPackage.Literals.VESSEL_CHARTER__START_BY,
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
				 getString("_UI_VesselCharter_endAt_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_VesselCharter_endAt_feature", "_UI_VesselCharter_type"),
				 CargoPackage.Literals.VESSEL_CHARTER__END_AT,
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
				 getString("_UI_VesselCharter_endAfter_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_VesselCharter_endAfter_feature", "_UI_VesselCharter_type"),
				 CargoPackage.Literals.VESSEL_CHARTER__END_AFTER,
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
				 getString("_UI_VesselCharter_endBy_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_VesselCharter_endBy_feature", "_UI_VesselCharter_type"),
				 CargoPackage.Literals.VESSEL_CHARTER__END_BY,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
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
				 getString("_UI_VesselCharter_forceHireCostOnlyEndRule_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_VesselCharter_forceHireCostOnlyEndRule_feature", "_UI_VesselCharter_type"),
				 CargoPackage.Literals.VESSEL_CHARTER__FORCE_HIRE_COST_ONLY_END_RULE,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Generic Charter Contract feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addGenericCharterContractPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_VesselCharter_genericCharterContract_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_VesselCharter_genericCharterContract_feature", "_UI_VesselCharter_type"),
				 CargoPackage.Literals.VESSEL_CHARTER__GENERIC_CHARTER_CONTRACT,
				 true,
				 false,
				 true,
				 null,
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
				 getString("_UI_VesselCharter_optional_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_VesselCharter_optional_feature", "_UI_VesselCharter_type"),
				 CargoPackage.Literals.VESSEL_CHARTER__OPTIONAL,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE,
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
				 getString("_UI_VesselCharter_minDuration_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_VesselCharter_minDuration_feature", "_UI_VesselCharter_type"),
				 CargoPackage.Literals.VESSEL_CHARTER__MIN_DURATION,
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
				 getString("_UI_VesselCharter_maxDuration_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_VesselCharter_maxDuration_feature", "_UI_VesselCharter_type"),
				 CargoPackage.Literals.VESSEL_CHARTER__MAX_DURATION,
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
				 getString("_UI_VesselCharter_charterContractOverride_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_VesselCharter_charterContractOverride_feature", "_UI_VesselCharter_type"),
				 CargoPackage.Literals.VESSEL_CHARTER__CHARTER_CONTRACT_OVERRIDE,
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
				 getString("_UI_VesselCharter_charterNumber_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_VesselCharter_charterNumber_feature", "_UI_VesselCharter_type"),
				 CargoPackage.Literals.VESSEL_CHARTER__CHARTER_NUMBER,
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
				 getString("_UI_VesselCharter_entity_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_VesselCharter_entity_feature", "_UI_VesselCharter_type"),
				 CargoPackage.Literals.VESSEL_CHARTER__ENTITY,
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
			childrenFeatures.add(CargoPackage.Literals.VESSEL_CHARTER__START_HEEL);
			childrenFeatures.add(CargoPackage.Literals.VESSEL_CHARTER__END_HEEL);
			childrenFeatures.add(CargoPackage.Literals.VESSEL_CHARTER__CONTAINED_CHARTER_CONTRACT);
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
	 * This returns VesselCharter.gif.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object getImage(Object object) {
		return overlayImage(object, getResourceLocator().getImage("full/obj16/VesselCharter"));
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
		Vessel v = ((VesselCharter) object).getVessel();
		uniqueAvailability((VesselCharter) object);
		if(v != null){
			label = v.getName();		
		} else{
			label = "<unspecified>";			
		}
		return label;
	}
	
	private boolean uniqueAvailability(VesselCharter va) {
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

		switch (notification.getFeatureID(VesselCharter.class)) {
			case CargoPackage.VESSEL_CHARTER__OPTIONAL:
			case CargoPackage.VESSEL_CHARTER__CHARTER_NUMBER:
			case CargoPackage.VESSEL_CHARTER__TIME_CHARTER_RATE:
			case CargoPackage.VESSEL_CHARTER__START_AFTER:
			case CargoPackage.VESSEL_CHARTER__START_BY:
			case CargoPackage.VESSEL_CHARTER__END_AFTER:
			case CargoPackage.VESSEL_CHARTER__END_BY:
			case CargoPackage.VESSEL_CHARTER__FORCE_HIRE_COST_ONLY_END_RULE:
			case CargoPackage.VESSEL_CHARTER__MIN_DURATION:
			case CargoPackage.VESSEL_CHARTER__MAX_DURATION:
			case CargoPackage.VESSEL_CHARTER__CHARTER_CONTRACT_OVERRIDE:
				fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), false, true));
				return;
			case CargoPackage.VESSEL_CHARTER__START_HEEL:
			case CargoPackage.VESSEL_CHARTER__END_HEEL:
			case CargoPackage.VESSEL_CHARTER__CONTAINED_CHARTER_CONTRACT:
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
				(CargoPackage.Literals.VESSEL_CHARTER__START_HEEL,
				 CommercialFactory.eINSTANCE.createStartHeelOptions()));

		newChildDescriptors.add
			(createChildParameter
				(CargoPackage.Literals.VESSEL_CHARTER__END_HEEL,
				 CommercialFactory.eINSTANCE.createEndHeelOptions()));

		newChildDescriptors.add
			(createChildParameter
				(CargoPackage.Literals.VESSEL_CHARTER__CONTAINED_CHARTER_CONTRACT,
				 CommercialFactory.eINSTANCE.createGenericCharterContract()));
	}

}
