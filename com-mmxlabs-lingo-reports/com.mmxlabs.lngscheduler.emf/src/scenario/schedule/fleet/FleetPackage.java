/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package scenario.schedule.fleet;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see scenario.schedule.fleet.FleetFactory
 * @model kind="package"
 * @generated
 */
public interface FleetPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "fleet";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://com.mmxlabs.lng.emf/schedule/fleet";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "com.mmxlabs.lng.emf.schedule.fleet";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	FleetPackage eINSTANCE = scenario.schedule.fleet.impl.FleetPackageImpl.init();

	/**
	 * The meta object id for the '{@link scenario.schedule.fleet.impl.AllocatedVesselImpl <em>Allocated Vessel</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see scenario.schedule.fleet.impl.AllocatedVesselImpl
	 * @see scenario.schedule.fleet.impl.FleetPackageImpl#getAllocatedVessel()
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
	 * The meta object id for the '{@link scenario.schedule.fleet.impl.FleetVesselImpl <em>Vessel</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see scenario.schedule.fleet.impl.FleetVesselImpl
	 * @see scenario.schedule.fleet.impl.FleetPackageImpl#getFleetVessel()
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
	 * The number of structural features of the '<em>Vessel</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FLEET_VESSEL_FEATURE_COUNT = ALLOCATED_VESSEL_FEATURE_COUNT + 1;

	/**
	 * The meta object id for the '{@link scenario.schedule.fleet.impl.SpotVesselImpl <em>Spot Vessel</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see scenario.schedule.fleet.impl.SpotVesselImpl
	 * @see scenario.schedule.fleet.impl.FleetPackageImpl#getSpotVessel()
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
	 * Returns the meta object for class '{@link scenario.schedule.fleet.AllocatedVessel <em>Allocated Vessel</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Allocated Vessel</em>'.
	 * @see scenario.schedule.fleet.AllocatedVessel
	 * @generated
	 */
	EClass getAllocatedVessel();

	/**
	 * Returns the meta object for class '{@link scenario.schedule.fleet.FleetVessel <em>Vessel</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Vessel</em>'.
	 * @see scenario.schedule.fleet.FleetVessel
	 * @generated
	 */
	EClass getFleetVessel();

	/**
	 * Returns the meta object for the reference '{@link scenario.schedule.fleet.FleetVessel#getVessel <em>Vessel</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Vessel</em>'.
	 * @see scenario.schedule.fleet.FleetVessel#getVessel()
	 * @see #getFleetVessel()
	 * @generated
	 */
	EReference getFleetVessel_Vessel();

	/**
	 * Returns the meta object for class '{@link scenario.schedule.fleet.SpotVessel <em>Spot Vessel</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Spot Vessel</em>'.
	 * @see scenario.schedule.fleet.SpotVessel
	 * @generated
	 */
	EClass getSpotVessel();

	/**
	 * Returns the meta object for the attribute '{@link scenario.schedule.fleet.SpotVessel#getIndex <em>Index</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Index</em>'.
	 * @see scenario.schedule.fleet.SpotVessel#getIndex()
	 * @see #getSpotVessel()
	 * @generated
	 */
	EAttribute getSpotVessel_Index();

	/**
	 * Returns the meta object for the reference '{@link scenario.schedule.fleet.SpotVessel#getVesselClass <em>Vessel Class</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Vessel Class</em>'.
	 * @see scenario.schedule.fleet.SpotVessel#getVesselClass()
	 * @see #getSpotVessel()
	 * @generated
	 */
	EReference getSpotVessel_VesselClass();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	FleetFactory getFleetFactory();

	/**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 *   <li>each class,</li>
	 *   <li>each feature of each class,</li>
	 *   <li>each enum,</li>
	 *   <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the '{@link scenario.schedule.fleet.impl.AllocatedVesselImpl <em>Allocated Vessel</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see scenario.schedule.fleet.impl.AllocatedVesselImpl
		 * @see scenario.schedule.fleet.impl.FleetPackageImpl#getAllocatedVessel()
		 * @generated
		 */
		EClass ALLOCATED_VESSEL = eINSTANCE.getAllocatedVessel();

		/**
		 * The meta object literal for the '{@link scenario.schedule.fleet.impl.FleetVesselImpl <em>Vessel</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see scenario.schedule.fleet.impl.FleetVesselImpl
		 * @see scenario.schedule.fleet.impl.FleetPackageImpl#getFleetVessel()
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
		 * The meta object literal for the '{@link scenario.schedule.fleet.impl.SpotVesselImpl <em>Spot Vessel</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see scenario.schedule.fleet.impl.SpotVesselImpl
		 * @see scenario.schedule.fleet.impl.FleetPackageImpl#getSpotVessel()
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

	}

} //FleetPackage
