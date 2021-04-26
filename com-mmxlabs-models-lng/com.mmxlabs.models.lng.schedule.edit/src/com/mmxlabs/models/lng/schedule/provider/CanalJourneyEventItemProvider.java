/**
 */
package com.mmxlabs.models.lng.schedule.provider;


import com.mmxlabs.models.lng.schedule.CanalJourneyEvent;
import com.mmxlabs.models.lng.schedule.SchedulePackage;

import java.time.ZonedDateTime;

import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ViewerNotification;

/**
 * This is the item provider adapter for a {@link com.mmxlabs.models.lng.schedule.CanalJourneyEvent} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class CanalJourneyEventItemProvider extends EventItemProvider {
	/**
	 * This constructs an instance from a factory and a notifier.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CanalJourneyEventItemProvider(AdapterFactory adapterFactory) {
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

			addLinkedSequencePropertyDescriptor(object);
			addLinkedJourneyPropertyDescriptor(object);
			addPanamaWaitingTimeHoursPropertyDescriptor(object);
			addMaxAvailablePanamaWaitingTimeHoursPropertyDescriptor(object);
		}
		return itemPropertyDescriptors;
	}

	/**
	 * This adds a property descriptor for the Linked Sequence feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addLinkedSequencePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_CanalJourneyEvent_linkedSequence_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_CanalJourneyEvent_linkedSequence_feature", "_UI_CanalJourneyEvent_type"),
				 SchedulePackage.Literals.CANAL_JOURNEY_EVENT__LINKED_SEQUENCE,
				 true,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Linked Journey feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addLinkedJourneyPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_CanalJourneyEvent_linkedJourney_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_CanalJourneyEvent_linkedJourney_feature", "_UI_CanalJourneyEvent_type"),
				 SchedulePackage.Literals.CANAL_JOURNEY_EVENT__LINKED_JOURNEY,
				 true,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Panama Waiting Time Hours feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addPanamaWaitingTimeHoursPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_CanalJourneyEvent_panamaWaitingTimeHours_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_CanalJourneyEvent_panamaWaitingTimeHours_feature", "_UI_CanalJourneyEvent_type"),
				 SchedulePackage.Literals.CANAL_JOURNEY_EVENT__PANAMA_WAITING_TIME_HOURS,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Max Available Panama Waiting Time Hours feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addMaxAvailablePanamaWaitingTimeHoursPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_CanalJourneyEvent_maxAvailablePanamaWaitingTimeHours_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_CanalJourneyEvent_maxAvailablePanamaWaitingTimeHours_feature", "_UI_CanalJourneyEvent_type"),
				 SchedulePackage.Literals.CANAL_JOURNEY_EVENT__MAX_AVAILABLE_PANAMA_WAITING_TIME_HOURS,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This returns CanalJourneyEvent.gif.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object getImage(Object object) {
		return overlayImage(object, getResourceLocator().getImage("full/obj16/CanalJourneyEvent"));
	}

	/**
	 * This returns the label text for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getText(Object object) {
		ZonedDateTime labelValue = ((CanalJourneyEvent)object).getStart();
		String label = labelValue == null ? null : labelValue.toString();
		return label == null || label.length() == 0 ?
			getString("_UI_CanalJourneyEvent_type") :
			getString("_UI_CanalJourneyEvent_type") + " " + label;
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

		switch (notification.getFeatureID(CanalJourneyEvent.class)) {
			case SchedulePackage.CANAL_JOURNEY_EVENT__PANAMA_WAITING_TIME_HOURS:
			case SchedulePackage.CANAL_JOURNEY_EVENT__MAX_AVAILABLE_PANAMA_WAITING_TIME_HOURS:
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
