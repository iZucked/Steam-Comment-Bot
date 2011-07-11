/**
 * <copyright>
 * </copyright>
 *
 * $Id$
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
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ItemProviderAdapter;
import org.eclipse.emf.edit.provider.ViewerNotification;

import scenario.provider.LngEditPlugin;

import scenario.schedule.Schedule;
import scenario.schedule.ScheduleFactory;
import scenario.schedule.SchedulePackage;

import scenario.schedule.fleetallocation.FleetallocationFactory;

/**
 * This is the item provider adapter for a {@link scenario.schedule.Schedule} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class ScheduleItemProvider
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
	public ScheduleItemProvider(AdapterFactory adapterFactory) {
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
				 getString("_UI_Schedule_name_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_Schedule_name_feature", "_UI_Schedule_type"),
				 SchedulePackage.Literals.SCHEDULE__NAME,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
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
			childrenFeatures.add(SchedulePackage.Literals.SCHEDULE__SEQUENCES);
			childrenFeatures.add(SchedulePackage.Literals.SCHEDULE__CARGO_ALLOCATIONS);
			childrenFeatures.add(SchedulePackage.Literals.SCHEDULE__FITNESS);
			childrenFeatures.add(SchedulePackage.Literals.SCHEDULE__REVENUE);
			childrenFeatures.add(SchedulePackage.Literals.SCHEDULE__FLEET);
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
	 * This returns Schedule.gif.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object getImage(Object object) {
		return overlayImage(object, getResourceLocator().getImage("full/obj16/Schedule"));
	}

	/**
	 * This returns the label text for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getText(Object object) {
		String label = ((Schedule)object).getName();
		return label == null || label.length() == 0 ?
			getString("_UI_Schedule_type") :
			getString("_UI_Schedule_type") + " " + label;
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

		switch (notification.getFeatureID(Schedule.class)) {
			case SchedulePackage.SCHEDULE__NAME:
				fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), false, true));
				return;
			case SchedulePackage.SCHEDULE__SEQUENCES:
			case SchedulePackage.SCHEDULE__CARGO_ALLOCATIONS:
			case SchedulePackage.SCHEDULE__FITNESS:
			case SchedulePackage.SCHEDULE__REVENUE:
			case SchedulePackage.SCHEDULE__FLEET:
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
				(SchedulePackage.Literals.SCHEDULE__SEQUENCES,
				 ScheduleFactory.eINSTANCE.createSequence()));

		newChildDescriptors.add
			(createChildParameter
				(SchedulePackage.Literals.SCHEDULE__CARGO_ALLOCATIONS,
				 ScheduleFactory.eINSTANCE.createCargoAllocation()));

		newChildDescriptors.add
			(createChildParameter
				(SchedulePackage.Literals.SCHEDULE__FITNESS,
				 ScheduleFactory.eINSTANCE.createScheduleFitness()));

		newChildDescriptors.add
			(createChildParameter
				(SchedulePackage.Literals.SCHEDULE__REVENUE,
				 ScheduleFactory.eINSTANCE.createBookedRevenue()));

		newChildDescriptors.add
			(createChildParameter
				(SchedulePackage.Literals.SCHEDULE__REVENUE,
				 ScheduleFactory.eINSTANCE.createCargoRevenue()));

		newChildDescriptors.add
			(createChildParameter
				(SchedulePackage.Literals.SCHEDULE__REVENUE,
				 ScheduleFactory.eINSTANCE.createCharterOutRevenue()));

		newChildDescriptors.add
			(createChildParameter
				(SchedulePackage.Literals.SCHEDULE__FLEET,
				 FleetallocationFactory.eINSTANCE.createAllocatedVessel()));

		newChildDescriptors.add
			(createChildParameter
				(SchedulePackage.Literals.SCHEDULE__FLEET,
				 FleetallocationFactory.eINSTANCE.createFleetVessel()));

		newChildDescriptors.add
			(createChildParameter
				(SchedulePackage.Literals.SCHEDULE__FLEET,
				 FleetallocationFactory.eINSTANCE.createSpotVessel()));
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
