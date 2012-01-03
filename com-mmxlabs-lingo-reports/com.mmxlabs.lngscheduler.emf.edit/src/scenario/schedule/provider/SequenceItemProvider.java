/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package scenario.schedule.provider;


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
import org.eclipse.emf.edit.provider.ItemProviderAdapter;
import org.eclipse.emf.edit.provider.ViewerNotification;

import scenario.provider.LngEditPlugin;

import scenario.schedule.ScheduleFactory;
import scenario.schedule.SchedulePackage;
import scenario.schedule.Sequence;

import scenario.schedule.events.EventsFactory;

/**
 * This is the item provider adapter for a {@link scenario.schedule.Sequence} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class SequenceItemProvider
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
	public SequenceItemProvider(AdapterFactory adapterFactory) {
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

			addVesselPropertyDescriptor(object);
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
				 getString("_UI_Sequence_vessel_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_Sequence_vessel_feature", "_UI_Sequence_type"),
				 SchedulePackage.Literals.SEQUENCE__VESSEL,
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
			childrenFeatures.add(SchedulePackage.Literals.SEQUENCE__FITNESS);
			childrenFeatures.add(SchedulePackage.Literals.SEQUENCE__EVENTS);
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
	 * This returns Sequence.gif.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object getImage(Object object) {
		return overlayImage(object, getResourceLocator().getImage("full/obj16/Sequence"));
	}

	/**
	 * This returns the label text for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NO
	 */
	@Override
	public String getText(Object object) {
		if (object instanceof Sequence) {
			return getString("_UI_Sequence_type") + " " + ((Sequence) object).getVessel().getName();
		}
		return getString("_UI_Sequence_type");
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

		switch (notification.getFeatureID(Sequence.class)) {
			case SchedulePackage.SEQUENCE__FITNESS:
			case SchedulePackage.SEQUENCE__EVENTS:
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
				(SchedulePackage.Literals.SEQUENCE__FITNESS,
				 ScheduleFactory.eINSTANCE.createScheduleFitness()));

		newChildDescriptors.add
			(createChildParameter
				(SchedulePackage.Literals.SEQUENCE__EVENTS,
				 EventsFactory.eINSTANCE.createScheduledEvent()));

		newChildDescriptors.add
			(createChildParameter
				(SchedulePackage.Literals.SEQUENCE__EVENTS,
				 EventsFactory.eINSTANCE.createJourney()));

		newChildDescriptors.add
			(createChildParameter
				(SchedulePackage.Literals.SEQUENCE__EVENTS,
				 EventsFactory.eINSTANCE.createPortVisit()));

		newChildDescriptors.add
			(createChildParameter
				(SchedulePackage.Literals.SEQUENCE__EVENTS,
				 EventsFactory.eINSTANCE.createIdle()));

		newChildDescriptors.add
			(createChildParameter
				(SchedulePackage.Literals.SEQUENCE__EVENTS,
				 EventsFactory.eINSTANCE.createSlotVisit()));

		newChildDescriptors.add
			(createChildParameter
				(SchedulePackage.Literals.SEQUENCE__EVENTS,
				 EventsFactory.eINSTANCE.createVesselEventVisit()));

		newChildDescriptors.add
			(createChildParameter
				(SchedulePackage.Literals.SEQUENCE__EVENTS,
				 EventsFactory.eINSTANCE.createCharterOutVisit()));
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
