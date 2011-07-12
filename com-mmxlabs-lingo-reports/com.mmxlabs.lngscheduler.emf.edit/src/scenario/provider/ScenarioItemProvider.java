/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package scenario.provider;


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

import scenario.Scenario;
import scenario.ScenarioFactory;
import scenario.ScenarioPackage;

import scenario.cargo.CargoFactory;

import scenario.contract.ContractFactory;

import scenario.fleet.FleetFactory;

import scenario.market.MarketFactory;

import scenario.optimiser.OptimiserFactory;

import scenario.optimiser.lso.LsoFactory;
import scenario.port.PortFactory;

import scenario.schedule.ScheduleFactory;
import scenario.schedule.events.EventsFactory;
import scenario.schedule.fleetallocation.FleetallocationFactory;

/**
 * This is the item provider adapter for a {@link scenario.Scenario} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class ScenarioItemProvider
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
	public ScenarioItemProvider(AdapterFactory adapterFactory) {
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

			addVersionPropertyDescriptor(object);
			addNamePropertyDescriptor(object);
		}
		return itemPropertyDescriptors;
	}

	/**
	 * This adds a property descriptor for the Version feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addVersionPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_Scenario_version_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_Scenario_version_feature", "_UI_Scenario_type"),
				 ScenarioPackage.Literals.SCENARIO__VERSION,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
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
				 getString("_UI_Scenario_name_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_Scenario_name_feature", "_UI_Scenario_type"),
				 ScenarioPackage.Literals.SCENARIO__NAME,
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
			childrenFeatures.add(ScenarioPackage.Literals.SCENARIO__FLEET_MODEL);
			childrenFeatures.add(ScenarioPackage.Literals.SCENARIO__SCHEDULE_MODEL);
			childrenFeatures.add(ScenarioPackage.Literals.SCENARIO__PORT_MODEL);
			childrenFeatures.add(ScenarioPackage.Literals.SCENARIO__DISTANCE_MODEL);
			childrenFeatures.add(ScenarioPackage.Literals.SCENARIO__CANAL_MODEL);
			childrenFeatures.add(ScenarioPackage.Literals.SCENARIO__CARGO_MODEL);
			childrenFeatures.add(ScenarioPackage.Literals.SCENARIO__CONTRACT_MODEL);
			childrenFeatures.add(ScenarioPackage.Literals.SCENARIO__MARKET_MODEL);
			childrenFeatures.add(ScenarioPackage.Literals.SCENARIO__OPTIMISATION);
			childrenFeatures.add(ScenarioPackage.Literals.SCENARIO__CONTAINED_MODELS);
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
	 * This returns Scenario.gif.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object getImage(Object object) {
		return overlayImage(object, getResourceLocator().getImage("full/obj16/Scenario"));
	}

	/**
	 * This returns the label text for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getText(Object object) {
		String label = ((Scenario)object).getName();
		return label == null || label.length() == 0 ?
			getString("_UI_Scenario_type") :
			getString("_UI_Scenario_type") + " " + label;
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

		switch (notification.getFeatureID(Scenario.class)) {
			case ScenarioPackage.SCENARIO__VERSION:
			case ScenarioPackage.SCENARIO__NAME:
				fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), false, true));
				return;
			case ScenarioPackage.SCENARIO__FLEET_MODEL:
			case ScenarioPackage.SCENARIO__SCHEDULE_MODEL:
			case ScenarioPackage.SCENARIO__PORT_MODEL:
			case ScenarioPackage.SCENARIO__DISTANCE_MODEL:
			case ScenarioPackage.SCENARIO__CANAL_MODEL:
			case ScenarioPackage.SCENARIO__CARGO_MODEL:
			case ScenarioPackage.SCENARIO__CONTRACT_MODEL:
			case ScenarioPackage.SCENARIO__MARKET_MODEL:
			case ScenarioPackage.SCENARIO__OPTIMISATION:
			case ScenarioPackage.SCENARIO__CONTAINED_MODELS:
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
				(ScenarioPackage.Literals.SCENARIO__FLEET_MODEL,
				 FleetFactory.eINSTANCE.createFleetModel()));

		newChildDescriptors.add
			(createChildParameter
				(ScenarioPackage.Literals.SCENARIO__SCHEDULE_MODEL,
				 ScheduleFactory.eINSTANCE.createScheduleModel()));

		newChildDescriptors.add
			(createChildParameter
				(ScenarioPackage.Literals.SCENARIO__PORT_MODEL,
				 PortFactory.eINSTANCE.createPortModel()));

		newChildDescriptors.add
			(createChildParameter
				(ScenarioPackage.Literals.SCENARIO__DISTANCE_MODEL,
				 PortFactory.eINSTANCE.createDistanceModel()));

		newChildDescriptors.add
			(createChildParameter
				(ScenarioPackage.Literals.SCENARIO__CANAL_MODEL,
				 PortFactory.eINSTANCE.createCanalModel()));

		newChildDescriptors.add
			(createChildParameter
				(ScenarioPackage.Literals.SCENARIO__CARGO_MODEL,
				 CargoFactory.eINSTANCE.createCargoModel()));

		newChildDescriptors.add
			(createChildParameter
				(ScenarioPackage.Literals.SCENARIO__CONTRACT_MODEL,
				 ContractFactory.eINSTANCE.createContractModel()));

		newChildDescriptors.add
			(createChildParameter
				(ScenarioPackage.Literals.SCENARIO__MARKET_MODEL,
				 MarketFactory.eINSTANCE.createMarketModel()));

		newChildDescriptors.add
			(createChildParameter
				(ScenarioPackage.Literals.SCENARIO__OPTIMISATION,
				 OptimiserFactory.eINSTANCE.createOptimisation()));

		newChildDescriptors.add
			(createChildParameter
				(ScenarioPackage.Literals.SCENARIO__CONTAINED_MODELS,
				 ScenarioFactory.eINSTANCE.createScenario()));

		newChildDescriptors.add
			(createChildParameter
				(ScenarioPackage.Literals.SCENARIO__CONTAINED_MODELS,
				 FleetFactory.eINSTANCE.createFleetModel()));

		newChildDescriptors.add
			(createChildParameter
				(ScenarioPackage.Literals.SCENARIO__CONTAINED_MODELS,
				 FleetFactory.eINSTANCE.createVessel()));

		newChildDescriptors.add
			(createChildParameter
				(ScenarioPackage.Literals.SCENARIO__CONTAINED_MODELS,
				 FleetFactory.eINSTANCE.createVesselClass()));

		newChildDescriptors.add
			(createChildParameter
				(ScenarioPackage.Literals.SCENARIO__CONTAINED_MODELS,
				 FleetFactory.eINSTANCE.createFuelConsumptionLine()));

		newChildDescriptors.add
			(createChildParameter
				(ScenarioPackage.Literals.SCENARIO__CONTAINED_MODELS,
				 FleetFactory.eINSTANCE.createVesselStateAttributes()));

		newChildDescriptors.add
			(createChildParameter
				(ScenarioPackage.Literals.SCENARIO__CONTAINED_MODELS,
				 FleetFactory.eINSTANCE.createPortAndTime()));

		newChildDescriptors.add
			(createChildParameter
				(ScenarioPackage.Literals.SCENARIO__CONTAINED_MODELS,
				 FleetFactory.eINSTANCE.createCharterOut()));

		newChildDescriptors.add
			(createChildParameter
				(ScenarioPackage.Literals.SCENARIO__CONTAINED_MODELS,
				 FleetFactory.eINSTANCE.createDrydock()));

		newChildDescriptors.add
			(createChildParameter
				(ScenarioPackage.Literals.SCENARIO__CONTAINED_MODELS,
				 FleetFactory.eINSTANCE.createVesselFuel()));

		newChildDescriptors.add
			(createChildParameter
				(ScenarioPackage.Literals.SCENARIO__CONTAINED_MODELS,
				 FleetFactory.eINSTANCE.createPortExclusion()));

		newChildDescriptors.add
			(createChildParameter
				(ScenarioPackage.Literals.SCENARIO__CONTAINED_MODELS,
				 FleetFactory.eINSTANCE.createVesselClassCost()));

		newChildDescriptors.add
			(createChildParameter
				(ScenarioPackage.Literals.SCENARIO__CONTAINED_MODELS,
				 ScheduleFactory.eINSTANCE.createScheduleModel()));

		newChildDescriptors.add
			(createChildParameter
				(ScenarioPackage.Literals.SCENARIO__CONTAINED_MODELS,
				 ScheduleFactory.eINSTANCE.createSchedule()));

		newChildDescriptors.add
			(createChildParameter
				(ScenarioPackage.Literals.SCENARIO__CONTAINED_MODELS,
				 ScheduleFactory.eINSTANCE.createSequence()));

		newChildDescriptors.add
			(createChildParameter
				(ScenarioPackage.Literals.SCENARIO__CONTAINED_MODELS,
				 ScheduleFactory.eINSTANCE.createCargoAllocation()));

		newChildDescriptors.add
			(createChildParameter
				(ScenarioPackage.Literals.SCENARIO__CONTAINED_MODELS,
				 ScheduleFactory.eINSTANCE.createScheduleFitness()));

		newChildDescriptors.add
			(createChildParameter
				(ScenarioPackage.Literals.SCENARIO__CONTAINED_MODELS,
				 ScheduleFactory.eINSTANCE.createLineItem()));

		newChildDescriptors.add
			(createChildParameter
				(ScenarioPackage.Literals.SCENARIO__CONTAINED_MODELS,
				 ScheduleFactory.eINSTANCE.createBookedRevenue()));

		newChildDescriptors.add
			(createChildParameter
				(ScenarioPackage.Literals.SCENARIO__CONTAINED_MODELS,
				 ScheduleFactory.eINSTANCE.createCargoRevenue()));

		newChildDescriptors.add
			(createChildParameter
				(ScenarioPackage.Literals.SCENARIO__CONTAINED_MODELS,
				 ScheduleFactory.eINSTANCE.createCharterOutRevenue()));

		newChildDescriptors.add
			(createChildParameter
				(ScenarioPackage.Literals.SCENARIO__CONTAINED_MODELS,
				 EventsFactory.eINSTANCE.createFuelMixture()));

		newChildDescriptors.add
			(createChildParameter
				(ScenarioPackage.Literals.SCENARIO__CONTAINED_MODELS,
				 EventsFactory.eINSTANCE.createFuelQuantity()));

		newChildDescriptors.add
			(createChildParameter
				(ScenarioPackage.Literals.SCENARIO__CONTAINED_MODELS,
				 EventsFactory.eINSTANCE.createScheduledEvent()));

		newChildDescriptors.add
			(createChildParameter
				(ScenarioPackage.Literals.SCENARIO__CONTAINED_MODELS,
				 EventsFactory.eINSTANCE.createJourney()));

		newChildDescriptors.add
			(createChildParameter
				(ScenarioPackage.Literals.SCENARIO__CONTAINED_MODELS,
				 EventsFactory.eINSTANCE.createPortVisit()));

		newChildDescriptors.add
			(createChildParameter
				(ScenarioPackage.Literals.SCENARIO__CONTAINED_MODELS,
				 EventsFactory.eINSTANCE.createIdle()));

		newChildDescriptors.add
			(createChildParameter
				(ScenarioPackage.Literals.SCENARIO__CONTAINED_MODELS,
				 EventsFactory.eINSTANCE.createSlotVisit()));

		newChildDescriptors.add
			(createChildParameter
				(ScenarioPackage.Literals.SCENARIO__CONTAINED_MODELS,
				 EventsFactory.eINSTANCE.createVesselEventVisit()));

		newChildDescriptors.add
			(createChildParameter
				(ScenarioPackage.Literals.SCENARIO__CONTAINED_MODELS,
				 EventsFactory.eINSTANCE.createCharterOutVisit()));

		newChildDescriptors.add
			(createChildParameter
				(ScenarioPackage.Literals.SCENARIO__CONTAINED_MODELS,
				 FleetallocationFactory.eINSTANCE.createAllocatedVessel()));

		newChildDescriptors.add
			(createChildParameter
				(ScenarioPackage.Literals.SCENARIO__CONTAINED_MODELS,
				 FleetallocationFactory.eINSTANCE.createFleetVessel()));

		newChildDescriptors.add
			(createChildParameter
				(ScenarioPackage.Literals.SCENARIO__CONTAINED_MODELS,
				 FleetallocationFactory.eINSTANCE.createSpotVessel()));

		newChildDescriptors.add
			(createChildParameter
				(ScenarioPackage.Literals.SCENARIO__CONTAINED_MODELS,
				 PortFactory.eINSTANCE.createPortModel()));

		newChildDescriptors.add
			(createChildParameter
				(ScenarioPackage.Literals.SCENARIO__CONTAINED_MODELS,
				 PortFactory.eINSTANCE.createPort()));

		newChildDescriptors.add
			(createChildParameter
				(ScenarioPackage.Literals.SCENARIO__CONTAINED_MODELS,
				 PortFactory.eINSTANCE.createDistanceModel()));

		newChildDescriptors.add
			(createChildParameter
				(ScenarioPackage.Literals.SCENARIO__CONTAINED_MODELS,
				 PortFactory.eINSTANCE.createDistanceLine()));

		newChildDescriptors.add
			(createChildParameter
				(ScenarioPackage.Literals.SCENARIO__CONTAINED_MODELS,
				 PortFactory.eINSTANCE.createCanal()));

		newChildDescriptors.add
			(createChildParameter
				(ScenarioPackage.Literals.SCENARIO__CONTAINED_MODELS,
				 PortFactory.eINSTANCE.createCanalModel()));

		newChildDescriptors.add
			(createChildParameter
				(ScenarioPackage.Literals.SCENARIO__CONTAINED_MODELS,
				 CargoFactory.eINSTANCE.createCargoModel()));

		newChildDescriptors.add
			(createChildParameter
				(ScenarioPackage.Literals.SCENARIO__CONTAINED_MODELS,
				 CargoFactory.eINSTANCE.createCargo()));

		newChildDescriptors.add
			(createChildParameter
				(ScenarioPackage.Literals.SCENARIO__CONTAINED_MODELS,
				 CargoFactory.eINSTANCE.createSlot()));

		newChildDescriptors.add
			(createChildParameter
				(ScenarioPackage.Literals.SCENARIO__CONTAINED_MODELS,
				 CargoFactory.eINSTANCE.createLoadSlot()));

		newChildDescriptors.add
			(createChildParameter
				(ScenarioPackage.Literals.SCENARIO__CONTAINED_MODELS,
				 ContractFactory.eINSTANCE.createContractModel()));

		newChildDescriptors.add
			(createChildParameter
				(ScenarioPackage.Literals.SCENARIO__CONTAINED_MODELS,
				 ContractFactory.eINSTANCE.createTotalVolumeLimit()));

		newChildDescriptors.add
			(createChildParameter
				(ScenarioPackage.Literals.SCENARIO__CONTAINED_MODELS,
				 ContractFactory.eINSTANCE.createEntity()));

		newChildDescriptors.add
			(createChildParameter
				(ScenarioPackage.Literals.SCENARIO__CONTAINED_MODELS,
				 ContractFactory.eINSTANCE.createSalesContract()));

		newChildDescriptors.add
			(createChildParameter
				(ScenarioPackage.Literals.SCENARIO__CONTAINED_MODELS,
				 ContractFactory.eINSTANCE.createFixedPricePurchaseContract()));

		newChildDescriptors.add
			(createChildParameter
				(ScenarioPackage.Literals.SCENARIO__CONTAINED_MODELS,
				 ContractFactory.eINSTANCE.createIndexPricePurchaseContract()));

		newChildDescriptors.add
			(createChildParameter
				(ScenarioPackage.Literals.SCENARIO__CONTAINED_MODELS,
				 ContractFactory.eINSTANCE.createNetbackPurchaseContract()));

		newChildDescriptors.add
			(createChildParameter
				(ScenarioPackage.Literals.SCENARIO__CONTAINED_MODELS,
				 ContractFactory.eINSTANCE.createProfitSharingPurchaseContract()));

		newChildDescriptors.add
			(createChildParameter
				(ScenarioPackage.Literals.SCENARIO__CONTAINED_MODELS,
				 MarketFactory.eINSTANCE.createIndex()));

		newChildDescriptors.add
			(createChildParameter
				(ScenarioPackage.Literals.SCENARIO__CONTAINED_MODELS,
				 MarketFactory.eINSTANCE.createMarketModel()));

		newChildDescriptors.add
			(createChildParameter
				(ScenarioPackage.Literals.SCENARIO__CONTAINED_MODELS,
				 MarketFactory.eINSTANCE.createStepwisePriceCurve()));

		newChildDescriptors.add
			(createChildParameter
				(ScenarioPackage.Literals.SCENARIO__CONTAINED_MODELS,
				 MarketFactory.eINSTANCE.createStepwisePrice()));

		newChildDescriptors.add
			(createChildParameter
				(ScenarioPackage.Literals.SCENARIO__CONTAINED_MODELS,
				 OptimiserFactory.eINSTANCE.createOptimisationSettings()));

		newChildDescriptors.add
			(createChildParameter
				(ScenarioPackage.Literals.SCENARIO__CONTAINED_MODELS,
				 OptimiserFactory.eINSTANCE.createOptimisation()));

		newChildDescriptors.add
			(createChildParameter
				(ScenarioPackage.Literals.SCENARIO__CONTAINED_MODELS,
				 OptimiserFactory.eINSTANCE.createConstraint()));

		newChildDescriptors.add
			(createChildParameter
				(ScenarioPackage.Literals.SCENARIO__CONTAINED_MODELS,
				 OptimiserFactory.eINSTANCE.createObjective()));

		newChildDescriptors.add
			(createChildParameter
				(ScenarioPackage.Literals.SCENARIO__CONTAINED_MODELS,
				 OptimiserFactory.eINSTANCE.createDiscountCurve()));

		newChildDescriptors.add
			(createChildParameter
				(ScenarioPackage.Literals.SCENARIO__CONTAINED_MODELS,
				 OptimiserFactory.eINSTANCE.createDiscount()));

		newChildDescriptors.add
			(createChildParameter
				(ScenarioPackage.Literals.SCENARIO__CONTAINED_MODELS,
				 LsoFactory.eINSTANCE.createLSOSettings()));

		newChildDescriptors.add
			(createChildParameter
				(ScenarioPackage.Literals.SCENARIO__CONTAINED_MODELS,
				 LsoFactory.eINSTANCE.createThresholderSettings()));

		newChildDescriptors.add
			(createChildParameter
				(ScenarioPackage.Literals.SCENARIO__CONTAINED_MODELS,
				 LsoFactory.eINSTANCE.createMoveGeneratorSettings()));

		newChildDescriptors.add
			(createChildParameter
				(ScenarioPackage.Literals.SCENARIO__CONTAINED_MODELS,
				 LsoFactory.eINSTANCE.createRandomMoveGeneratorSettings()));

		newChildDescriptors.add
			(createChildParameter
				(ScenarioPackage.Literals.SCENARIO__CONTAINED_MODELS,
				 LsoFactory.eINSTANCE.createConstrainedMoveGeneratorSettings()));
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
			childFeature == ScenarioPackage.Literals.SCENARIO__FLEET_MODEL ||
			childFeature == ScenarioPackage.Literals.SCENARIO__CONTAINED_MODELS ||
			childFeature == ScenarioPackage.Literals.SCENARIO__SCHEDULE_MODEL ||
			childFeature == ScenarioPackage.Literals.SCENARIO__PORT_MODEL ||
			childFeature == ScenarioPackage.Literals.SCENARIO__DISTANCE_MODEL ||
			childFeature == ScenarioPackage.Literals.SCENARIO__CANAL_MODEL ||
			childFeature == ScenarioPackage.Literals.SCENARIO__CARGO_MODEL ||
			childFeature == ScenarioPackage.Literals.SCENARIO__CONTRACT_MODEL ||
			childFeature == ScenarioPackage.Literals.SCENARIO__MARKET_MODEL ||
			childFeature == ScenarioPackage.Literals.SCENARIO__OPTIMISATION;

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
