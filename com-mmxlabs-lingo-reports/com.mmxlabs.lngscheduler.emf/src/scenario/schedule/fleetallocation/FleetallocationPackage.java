/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package scenario.schedule.fleetallocation;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each operation of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see scenario.schedule.fleetallocation.FleetallocationFactory
 * @model kind="package"
 * @generated
 */
public interface FleetallocationPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "fleetallocation";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://com.mmxlabs.lng.emf/schedule/fleetallocation";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "com.mmxlabs.lng.emf.schedule.fleetallocation";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	FleetallocationPackage eINSTANCE = scenario.schedule.fleetallocation.impl.FleetallocationPackageImpl.init();

	/**
	 * The meta object id for the '{@link scenario.schedule.fleetallocation.impl.AllocatedVesselImpl <em>Allocated Vessel</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see scenario.schedule.fleetallocation.impl.AllocatedVesselImpl
	 * @see scenario.schedule.fleetallocation.impl.FleetallocationPackageImpl#getAllocatedVessel()
	 * @generated
	 */
	int ALLOCATED_VESSEL = 0;

	/**
	 * The number of structural features of the '<em>Allocated Vessel</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ALLOCATED_VESSEL_FEATURE_COUNT = 0;

	/**
	 * The operation id for the '<em>Get Hourly Charter Price</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ALLOCATED_VESSEL___GET_HOURLY_CHARTER_PRICE = 0;

	/**
	 * The operation id for the '<em>Get Name</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ALLOCATED_VESSEL___GET_NAME = 1;

	/**
	 * The number of operations of the '<em>Allocated Vessel</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ALLOCATED_VESSEL_OPERATION_COUNT = 2;

	/**
	 * The meta object id for the '{@link scenario.schedule.fleetallocation.impl.FleetVesselImpl <em>Fleet Vessel</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see scenario.schedule.fleetallocation.impl.FleetVesselImpl
	 * @see scenario.schedule.fleetallocation.impl.FleetallocationPackageImpl#getFleetVessel()
	 * @generated
	 */
	int FLEET_VESSEL = 1;

	/**
	 * The feature id for the '<em><b>Vessel</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FLEET_VESSEL__VESSEL = ALLOCATED_VESSEL_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Fleet Vessel</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FLEET_VESSEL_FEATURE_COUNT = ALLOCATED_VESSEL_FEATURE_COUNT + 1;

	/**
	 * The operation id for the '<em>Get Hourly Charter Price</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FLEET_VESSEL___GET_HOURLY_CHARTER_PRICE = ALLOCATED_VESSEL___GET_HOURLY_CHARTER_PRICE;

	/**
	 * The operation id for the '<em>Get Name</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FLEET_VESSEL___GET_NAME = ALLOCATED_VESSEL_OPERATION_COUNT + 0;

	/**
	 * The number of operations of the '<em>Fleet Vessel</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FLEET_VESSEL_OPERATION_COUNT = ALLOCATED_VESSEL_OPERATION_COUNT + 1;

	/**
	 * The meta object id for the '{@link scenario.schedule.fleetallocation.impl.SpotVesselImpl <em>Spot Vessel</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see scenario.schedule.fleetallocation.impl.SpotVesselImpl
	 * @see scenario.schedule.fleetallocation.impl.FleetallocationPackageImpl#getSpotVessel()
	 * @generated
	 */
	int SPOT_VESSEL = 2;

	/**
	 * The feature id for the '<em><b>Index</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_VESSEL__INDEX = ALLOCATED_VESSEL_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Vessel Class</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_VESSEL__VESSEL_CLASS = ALLOCATED_VESSEL_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Spot Vessel</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_VESSEL_FEATURE_COUNT = ALLOCATED_VESSEL_FEATURE_COUNT + 2;

	/**
	 * The operation id for the '<em>Get Hourly Charter Price</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_VESSEL___GET_HOURLY_CHARTER_PRICE = ALLOCATED_VESSEL_OPERATION_COUNT + 0;

	/**
	 * The operation id for the '<em>Get Name</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_VESSEL___GET_NAME = ALLOCATED_VESSEL_OPERATION_COUNT + 1;

	/**
	 * The number of operations of the '<em>Spot Vessel</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPOT_VESSEL_OPERATION_COUNT = ALLOCATED_VESSEL_OPERATION_COUNT + 2;


	/**
	 * Returns the meta object for class '{@link scenario.schedule.fleetallocation.AllocatedVessel <em>Allocated Vessel</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Allocated Vessel</em>'.
	 * @see scenario.schedule.fleetallocation.AllocatedVessel
	 * @generated
	 */
	EClass getAllocatedVessel();

	/**
	 * Returns the meta object for the '{@link scenario.schedule.fleetallocation.AllocatedVessel#getHourlyCharterPrice() <em>Get Hourly Charter Price</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Hourly Charter Price</em>' operation.
	 * @see scenario.schedule.fleetallocation.AllocatedVessel#getHourlyCharterPrice()
	 * @generated
	 */
	EOperation getAllocatedVessel__GetHourlyCharterPrice();

	/**
	 * Returns the meta object for the '{@link scenario.schedule.fleetallocation.AllocatedVessel#getName() <em>Get Name</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Name</em>' operation.
	 * @see scenario.schedule.fleetallocation.AllocatedVessel#getName()
	 * @generated
	 */
	EOperation getAllocatedVessel__GetName();

	/**
	 * Returns the meta object for class '{@link scenario.schedule.fleetallocation.FleetVessel <em>Fleet Vessel</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Fleet Vessel</em>'.
	 * @see scenario.schedule.fleetallocation.FleetVessel
	 * @generated
	 */
	EClass getFleetVessel();

	/**
	 * Returns the meta object for the reference '{@link scenario.schedule.fleetallocation.FleetVessel#getVessel <em>Vessel</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Vessel</em>'.
	 * @see scenario.schedule.fleetallocation.FleetVessel#getVessel()
	 * @see #getFleetVessel()
	 * @generated
	 */
	EReference getFleetVessel_Vessel();

	/**
	 * Returns the meta object for the '{@link scenario.schedule.fleetallocation.FleetVessel#getName() <em>Get Name</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Name</em>' operation.
	 * @see scenario.schedule.fleetallocation.FleetVessel#getName()
	 * @generated
	 */
	EOperation getFleetVessel__GetName();

	/**
	 * Returns the meta object for class '{@link scenario.schedule.fleetallocation.SpotVessel <em>Spot Vessel</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Spot Vessel</em>'.
	 * @see scenario.schedule.fleetallocation.SpotVessel
	 * @generated
	 */
	EClass getSpotVessel();

	/**
	 * Returns the meta object for the attribute '{@link scenario.schedule.fleetallocation.SpotVessel#getIndex <em>Index</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Index</em>'.
	 * @see scenario.schedule.fleetallocation.SpotVessel#getIndex()
	 * @see #getSpotVessel()
	 * @generated
	 */
	EAttribute getSpotVessel_Index();

	/**
	 * Returns the meta object for the reference '{@link scenario.schedule.fleetallocation.SpotVessel#getVesselClass <em>Vessel Class</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Vessel Class</em>'.
	 * @see scenario.schedule.fleetallocation.SpotVessel#getVesselClass()
	 * @see #getSpotVessel()
	 * @generated
	 */
	EReference getSpotVessel_VesselClass();

	/**
	 * Returns the meta object for the '{@link scenario.schedule.fleetallocation.SpotVessel#getHourlyCharterPrice() <em>Get Hourly Charter Price</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Hourly Charter Price</em>' operation.
	 * @see scenario.schedule.fleetallocation.SpotVessel#getHourlyCharterPrice()
	 * @generated
	 */
	EOperation getSpotVessel__GetHourlyCharterPrice();

	/**
	 * Returns the meta object for the '{@link scenario.schedule.fleetallocation.SpotVessel#getName() <em>Get Name</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Get Name</em>' operation.
	 * @see scenario.schedule.fleetallocation.SpotVessel#getName()
	 * @generated
	 */
	EOperation getSpotVessel__GetName();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	FleetallocationFactory getFleetallocationFactory();

	/**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 *   <li>each class,</li>
	 *   <li>each feature of each class,</li>
	 *   <li>each operation of each class,</li>
	 *   <li>each enum,</li>
	 *   <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the '{@link scenario.schedule.fleetallocation.impl.AllocatedVesselImpl <em>Allocated Vessel</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see scenario.schedule.fleetallocation.impl.AllocatedVesselImpl
		 * @see scenario.schedule.fleetallocation.impl.FleetallocationPackageImpl#getAllocatedVessel()
		 * @generated
		 */
		EClass ALLOCATED_VESSEL = eINSTANCE.getAllocatedVessel();

		/**
		 * The meta object literal for the '<em><b>Get Hourly Charter Price</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation ALLOCATED_VESSEL___GET_HOURLY_CHARTER_PRICE = eINSTANCE.getAllocatedVessel__GetHourlyCharterPrice();

		/**
		 * The meta object literal for the '<em><b>Get Name</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation ALLOCATED_VESSEL___GET_NAME = eINSTANCE.getAllocatedVessel__GetName();

		/**
		 * The meta object literal for the '{@link scenario.schedule.fleetallocation.impl.FleetVesselImpl <em>Fleet Vessel</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see scenario.schedule.fleetallocation.impl.FleetVesselImpl
		 * @see scenario.schedule.fleetallocation.impl.FleetallocationPackageImpl#getFleetVessel()
		 * @generated
		 */
		EClass FLEET_VESSEL = eINSTANCE.getFleetVessel();

		/**
		 * The meta object literal for the '<em><b>Vessel</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference FLEET_VESSEL__VESSEL = eINSTANCE.getFleetVessel_Vessel();

		/**
		 * The meta object literal for the '<em><b>Get Name</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation FLEET_VESSEL___GET_NAME = eINSTANCE.getFleetVessel__GetName();

		/**
		 * The meta object literal for the '{@link scenario.schedule.fleetallocation.impl.SpotVesselImpl <em>Spot Vessel</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see scenario.schedule.fleetallocation.impl.SpotVesselImpl
		 * @see scenario.schedule.fleetallocation.impl.FleetallocationPackageImpl#getSpotVessel()
		 * @generated
		 */
		EClass SPOT_VESSEL = eINSTANCE.getSpotVessel();

		/**
		 * The meta object literal for the '<em><b>Index</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SPOT_VESSEL__INDEX = eINSTANCE.getSpotVessel_Index();

		/**
		 * The meta object literal for the '<em><b>Vessel Class</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SPOT_VESSEL__VESSEL_CLASS = eINSTANCE.getSpotVessel_VesselClass();

		/**
		 * The meta object literal for the '<em><b>Get Hourly Charter Price</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation SPOT_VESSEL___GET_HOURLY_CHARTER_PRICE = eINSTANCE.getSpotVessel__GetHourlyCharterPrice();

		/**
		 * The meta object literal for the '<em><b>Get Name</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation SPOT_VESSEL___GET_NAME = eINSTANCE.getSpotVessel__GetName();

	}

} //FleetallocationPackage
