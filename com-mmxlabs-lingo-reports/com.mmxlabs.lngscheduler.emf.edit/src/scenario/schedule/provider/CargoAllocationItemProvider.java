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

import scenario.schedule.CargoAllocation;
import scenario.schedule.SchedulePackage;

/**
 * This is the item provider adapter for a {@link scenario.schedule.CargoAllocation} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class CargoAllocationItemProvider extends ItemProviderAdapter implements
		IEditingDomainItemProvider, IStructuredItemContentProvider,
		ITreeItemContentProvider, IItemLabelProvider, IItemPropertySource {
	/**
	 * This constructs an instance from a factory and a notifier. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public CargoAllocationItemProvider(AdapterFactory adapterFactory) {
		super(adapterFactory);
	}

	/**
	 * This returns the property descriptors for the adapted class. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public List<IItemPropertyDescriptor> getPropertyDescriptors(Object object) {
		if (itemPropertyDescriptors == null) {
			super.getPropertyDescriptors(object);

			addLoadSlotPropertyDescriptor(object);
			addDischargeSlotPropertyDescriptor(object);
			addFuelVolumePropertyDescriptor(object);
			addDischargeVolumePropertyDescriptor(object);
			addLoadDatePropertyDescriptor(object);
			addDischargeDatePropertyDescriptor(object);
			addLoadPriceM3PropertyDescriptor(object);
			addDischargePriceM3PropertyDescriptor(object);
			addVesselPropertyDescriptor(object);
			addLadenLegPropertyDescriptor(object);
			addBallastLegPropertyDescriptor(object);
			addLadenIdlePropertyDescriptor(object);
			addBallastIdlePropertyDescriptor(object);
			addLoadRevenuePropertyDescriptor(object);
			addShippingRevenuePropertyDescriptor(object);
			addDischargeRevenuePropertyDescriptor(object);
			addCargoTypePropertyDescriptor(object);
			addLoadSlotVisitPropertyDescriptor(object);
			addDischargeSlotVisitPropertyDescriptor(object);
		}
		return itemPropertyDescriptors;
	}

	/**
	 * This adds a property descriptor for the Load Slot feature. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected void addLoadSlotPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_CargoAllocation_loadSlot_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_CargoAllocation_loadSlot_feature", "_UI_CargoAllocation_type"),
				 SchedulePackage.Literals.CARGO_ALLOCATION__LOAD_SLOT,
				 true,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Discharge Slot feature. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected void addDischargeSlotPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_CargoAllocation_dischargeSlot_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_CargoAllocation_dischargeSlot_feature", "_UI_CargoAllocation_type"),
				 SchedulePackage.Literals.CARGO_ALLOCATION__DISCHARGE_SLOT,
				 true,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Fuel Volume feature. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected void addFuelVolumePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_CargoAllocation_fuelVolume_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_CargoAllocation_fuelVolume_feature", "_UI_CargoAllocation_type"),
				 SchedulePackage.Literals.CARGO_ALLOCATION__FUEL_VOLUME,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Discharge Volume feature. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected void addDischargeVolumePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_CargoAllocation_dischargeVolume_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_CargoAllocation_dischargeVolume_feature", "_UI_CargoAllocation_type"),
				 SchedulePackage.Literals.CARGO_ALLOCATION__DISCHARGE_VOLUME,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Load Date feature. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected void addLoadDatePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_CargoAllocation_loadDate_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_CargoAllocation_loadDate_feature", "_UI_CargoAllocation_type"),
				 SchedulePackage.Literals.CARGO_ALLOCATION__LOAD_DATE,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Discharge Date feature. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected void addDischargeDatePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_CargoAllocation_dischargeDate_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_CargoAllocation_dischargeDate_feature", "_UI_CargoAllocation_type"),
				 SchedulePackage.Literals.CARGO_ALLOCATION__DISCHARGE_DATE,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Load Price M3 feature. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected void addLoadPriceM3PropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_CargoAllocation_loadPriceM3_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_CargoAllocation_loadPriceM3_feature", "_UI_CargoAllocation_type"),
				 SchedulePackage.Literals.CARGO_ALLOCATION__LOAD_PRICE_M3,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Discharge Price M3 feature. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected void addDischargePriceM3PropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_CargoAllocation_dischargePriceM3_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_CargoAllocation_dischargePriceM3_feature", "_UI_CargoAllocation_type"),
				 SchedulePackage.Literals.CARGO_ALLOCATION__DISCHARGE_PRICE_M3,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Vessel feature. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected void addVesselPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_CargoAllocation_vessel_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_CargoAllocation_vessel_feature", "_UI_CargoAllocation_type"),
				 SchedulePackage.Literals.CARGO_ALLOCATION__VESSEL,
				 true,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Laden Leg feature. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected void addLadenLegPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_CargoAllocation_ladenLeg_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_CargoAllocation_ladenLeg_feature", "_UI_CargoAllocation_type"),
				 SchedulePackage.Literals.CARGO_ALLOCATION__LADEN_LEG,
				 true,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Ballast Leg feature. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected void addBallastLegPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_CargoAllocation_ballastLeg_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_CargoAllocation_ballastLeg_feature", "_UI_CargoAllocation_type"),
				 SchedulePackage.Literals.CARGO_ALLOCATION__BALLAST_LEG,
				 true,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Laden Idle feature. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected void addLadenIdlePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_CargoAllocation_ladenIdle_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_CargoAllocation_ladenIdle_feature", "_UI_CargoAllocation_type"),
				 SchedulePackage.Literals.CARGO_ALLOCATION__LADEN_IDLE,
				 true,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Ballast Idle feature. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected void addBallastIdlePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_CargoAllocation_ballastIdle_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_CargoAllocation_ballastIdle_feature", "_UI_CargoAllocation_type"),
				 SchedulePackage.Literals.CARGO_ALLOCATION__BALLAST_IDLE,
				 true,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Load Revenue feature. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected void addLoadRevenuePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_CargoAllocation_loadRevenue_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_CargoAllocation_loadRevenue_feature", "_UI_CargoAllocation_type"),
				 SchedulePackage.Literals.CARGO_ALLOCATION__LOAD_REVENUE,
				 true,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Shipping Revenue feature. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected void addShippingRevenuePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_CargoAllocation_shippingRevenue_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_CargoAllocation_shippingRevenue_feature", "_UI_CargoAllocation_type"),
				 SchedulePackage.Literals.CARGO_ALLOCATION__SHIPPING_REVENUE,
				 true,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Discharge Revenue feature. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected void addDischargeRevenuePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_CargoAllocation_dischargeRevenue_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_CargoAllocation_dischargeRevenue_feature", "_UI_CargoAllocation_type"),
				 SchedulePackage.Literals.CARGO_ALLOCATION__DISCHARGE_REVENUE,
				 true,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Cargo Type feature. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected void addCargoTypePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_CargoAllocation_cargoType_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_CargoAllocation_cargoType_feature", "_UI_CargoAllocation_type"),
				 SchedulePackage.Literals.CARGO_ALLOCATION__CARGO_TYPE,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Load Slot Visit feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addLoadSlotVisitPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_CargoAllocation_loadSlotVisit_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_CargoAllocation_loadSlotVisit_feature", "_UI_CargoAllocation_type"),
				 SchedulePackage.Literals.CARGO_ALLOCATION__LOAD_SLOT_VISIT,
				 true,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Discharge Slot Visit feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addDischargeSlotVisitPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_CargoAllocation_dischargeSlotVisit_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_CargoAllocation_dischargeSlotVisit_feature", "_UI_CargoAllocation_type"),
				 SchedulePackage.Literals.CARGO_ALLOCATION__DISCHARGE_SLOT_VISIT,
				 true,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This returns CargoAllocation.gif.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 */
	@Override
	public Object getImage(Object object) {
		return overlayImage(object, getResourceLocator().getImage("full/obj16/CargoAllocation"));
	}

	/**
	 * This returns the label text for the adapted class. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated NO
	 */
	@Override
	public String getText(Object object) {
		CargoAllocation cargoAllocation = (CargoAllocation) object;
		return getString("_UI_CargoAllocation_type")
				+ " "
				+ (cargoAllocation.getLoadSlot() != null ? cargoAllocation
						.getLoadSlot().getId() : "no slot")
				+ " to "
				+ (cargoAllocation.getDischargeSlot() != null ? cargoAllocation
						.getDischargeSlot().getId() : "no slot");
	}

	/**
	 * This handles model notifications by calling {@link #updateChildren} to update any cached
	 * children and by creating a viewer notification, which it passes to {@link #fireNotifyChanged}.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 */
	@Override
	public void notifyChanged(Notification notification) {
		updateChildren(notification);

		switch (notification.getFeatureID(CargoAllocation.class)) {
			case SchedulePackage.CARGO_ALLOCATION__FUEL_VOLUME:
			case SchedulePackage.CARGO_ALLOCATION__DISCHARGE_VOLUME:
			case SchedulePackage.CARGO_ALLOCATION__LOAD_DATE:
			case SchedulePackage.CARGO_ALLOCATION__DISCHARGE_DATE:
			case SchedulePackage.CARGO_ALLOCATION__LOAD_PRICE_M3:
			case SchedulePackage.CARGO_ALLOCATION__DISCHARGE_PRICE_M3:
			case SchedulePackage.CARGO_ALLOCATION__CARGO_TYPE:
				fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), false, true));
				return;
		}
		super.notifyChanged(notification);
	}

	/**
	 * This adds {@link org.eclipse.emf.edit.command.CommandParameter}s
	 * describing the children that can be created under this object. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected void collectNewChildDescriptors(
			Collection<Object> newChildDescriptors, Object object) {
		super.collectNewChildDescriptors(newChildDescriptors, object);
	}

	/**
	 * Return the resource locator for this item provider's resources. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public ResourceLocator getResourceLocator() {
		return LngEditPlugin.INSTANCE;
	}

}
