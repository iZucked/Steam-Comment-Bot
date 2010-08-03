/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package scenario.fleet;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
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
 * @see scenario.fleet.FleetFactory
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
	String eNS_URI = "http://com.mmxlabs.lng.emf/fleet";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "com.mmxlabs.lng.emf.fleet";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	FleetPackage eINSTANCE = scenario.fleet.impl.FleetPackageImpl.init();

	/**
	 * The meta object id for the '{@link scenario.fleet.impl.FleetModelImpl <em>Model</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see scenario.fleet.impl.FleetModelImpl
	 * @see scenario.fleet.impl.FleetPackageImpl#getFleetModel()
	 * @generated
	 */
	int FLEET_MODEL = 0;

	/**
	 * The feature id for the '<em><b>Fleet</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FLEET_MODEL__FLEET = 0;

	/**
	 * The feature id for the '<em><b>Vessel Classes</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FLEET_MODEL__VESSEL_CLASSES = 1;

	/**
	 * The number of structural features of the '<em>Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FLEET_MODEL_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the '{@link scenario.fleet.impl.VesselImpl <em>Vessel</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see scenario.fleet.impl.VesselImpl
	 * @see scenario.fleet.impl.FleetPackageImpl#getVessel()
	 * @generated
	 */
	int VESSEL = 1;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL__NAME = 0;

	/**
	 * The feature id for the '<em><b>Class</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL__CLASS = 1;

	/**
	 * The feature id for the '<em><b>Start Port</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL__START_PORT = 2;

	/**
	 * The feature id for the '<em><b>End Port</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL__END_PORT = 3;

	/**
	 * The number of structural features of the '<em>Vessel</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_FEATURE_COUNT = 4;

	/**
	 * The meta object id for the '{@link scenario.fleet.impl.VesselClassImpl <em>Vessel Class</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see scenario.fleet.impl.VesselClassImpl
	 * @see scenario.fleet.impl.FleetPackageImpl#getVesselClass()
	 * @generated
	 */
	int VESSEL_CLASS = 2;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_CLASS__NAME = 0;

	/**
	 * The feature id for the '<em><b>Capacity</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_CLASS__CAPACITY = 1;

	/**
	 * The feature id for the '<em><b>Min Speed</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_CLASS__MIN_SPEED = 2;

	/**
	 * The feature id for the '<em><b>Max Speed</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_CLASS__MAX_SPEED = 3;

	/**
	 * The feature id for the '<em><b>Base Fuel Unit Price</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_CLASS__BASE_FUEL_UNIT_PRICE = 4;

	/**
	 * The feature id for the '<em><b>Laden Attributes</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_CLASS__LADEN_ATTRIBUTES = 5;

	/**
	 * The feature id for the '<em><b>Ballast Attributes</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_CLASS__BALLAST_ATTRIBUTES = 6;

	/**
	 * The number of structural features of the '<em>Vessel Class</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_CLASS_FEATURE_COUNT = 7;

	/**
	 * The meta object id for the '{@link scenario.fleet.impl.FuelConsumptionLineImpl <em>Fuel Consumption Line</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see scenario.fleet.impl.FuelConsumptionLineImpl
	 * @see scenario.fleet.impl.FleetPackageImpl#getFuelConsumptionLine()
	 * @generated
	 */
	int FUEL_CONSUMPTION_LINE = 3;

	/**
	 * The feature id for the '<em><b>Speed</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FUEL_CONSUMPTION_LINE__SPEED = 0;

	/**
	 * The feature id for the '<em><b>Consumption</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FUEL_CONSUMPTION_LINE__CONSUMPTION = 1;

	/**
	 * The number of structural features of the '<em>Fuel Consumption Line</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FUEL_CONSUMPTION_LINE_FEATURE_COUNT = 2;


	/**
	 * The meta object id for the '{@link scenario.fleet.impl.VesselStateAttributesImpl <em>Vessel State Attributes</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see scenario.fleet.impl.VesselStateAttributesImpl
	 * @see scenario.fleet.impl.FleetPackageImpl#getVesselStateAttributes()
	 * @generated
	 */
	int VESSEL_STATE_ATTRIBUTES = 4;

	/**
	 * The feature id for the '<em><b>Vessel State</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_STATE_ATTRIBUTES__VESSEL_STATE = 0;

	/**
	 * The feature id for the '<em><b>Nbo Rate</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_STATE_ATTRIBUTES__NBO_RATE = 1;

	/**
	 * The feature id for the '<em><b>Idle NBO Rate</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_STATE_ATTRIBUTES__IDLE_NBO_RATE = 2;

	/**
	 * The feature id for the '<em><b>Idle Consumption Rate</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_STATE_ATTRIBUTES__IDLE_CONSUMPTION_RATE = 3;

	/**
	 * The feature id for the '<em><b>Fuel Consumption Curve</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_STATE_ATTRIBUTES__FUEL_CONSUMPTION_CURVE = 4;

	/**
	 * The number of structural features of the '<em>Vessel State Attributes</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VESSEL_STATE_ATTRIBUTES_FEATURE_COUNT = 5;

	/**
	 * The meta object id for the '{@link scenario.fleet.VesselState <em>Vessel State</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see scenario.fleet.VesselState
	 * @see scenario.fleet.impl.FleetPackageImpl#getVesselState()
	 * @generated
	 */
	int VESSEL_STATE = 5;


	/**
	 * Returns the meta object for class '{@link scenario.fleet.FleetModel <em>Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Model</em>'.
	 * @see scenario.fleet.FleetModel
	 * @generated
	 */
	EClass getFleetModel();

	/**
	 * Returns the meta object for the containment reference list '{@link scenario.fleet.FleetModel#getFleet <em>Fleet</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Fleet</em>'.
	 * @see scenario.fleet.FleetModel#getFleet()
	 * @see #getFleetModel()
	 * @generated
	 */
	EReference getFleetModel_Fleet();

	/**
	 * Returns the meta object for the containment reference list '{@link scenario.fleet.FleetModel#getVesselClasses <em>Vessel Classes</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Vessel Classes</em>'.
	 * @see scenario.fleet.FleetModel#getVesselClasses()
	 * @see #getFleetModel()
	 * @generated
	 */
	EReference getFleetModel_VesselClasses();

	/**
	 * Returns the meta object for class '{@link scenario.fleet.Vessel <em>Vessel</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Vessel</em>'.
	 * @see scenario.fleet.Vessel
	 * @generated
	 */
	EClass getVessel();

	/**
	 * Returns the meta object for the attribute '{@link scenario.fleet.Vessel#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see scenario.fleet.Vessel#getName()
	 * @see #getVessel()
	 * @generated
	 */
	EAttribute getVessel_Name();

	/**
	 * Returns the meta object for the reference '{@link scenario.fleet.Vessel#getClass_ <em>Class</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Class</em>'.
	 * @see scenario.fleet.Vessel#getClass_()
	 * @see #getVessel()
	 * @generated
	 */
	EReference getVessel_Class();

	/**
	 * Returns the meta object for the reference '{@link scenario.fleet.Vessel#getStartPort <em>Start Port</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Start Port</em>'.
	 * @see scenario.fleet.Vessel#getStartPort()
	 * @see #getVessel()
	 * @generated
	 */
	EReference getVessel_StartPort();

	/**
	 * Returns the meta object for the reference '{@link scenario.fleet.Vessel#getEndPort <em>End Port</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>End Port</em>'.
	 * @see scenario.fleet.Vessel#getEndPort()
	 * @see #getVessel()
	 * @generated
	 */
	EReference getVessel_EndPort();

	/**
	 * Returns the meta object for class '{@link scenario.fleet.VesselClass <em>Vessel Class</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Vessel Class</em>'.
	 * @see scenario.fleet.VesselClass
	 * @generated
	 */
	EClass getVesselClass();

	/**
	 * Returns the meta object for the attribute '{@link scenario.fleet.VesselClass#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see scenario.fleet.VesselClass#getName()
	 * @see #getVesselClass()
	 * @generated
	 */
	EAttribute getVesselClass_Name();

	/**
	 * Returns the meta object for the attribute '{@link scenario.fleet.VesselClass#getCapacity <em>Capacity</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Capacity</em>'.
	 * @see scenario.fleet.VesselClass#getCapacity()
	 * @see #getVesselClass()
	 * @generated
	 */
	EAttribute getVesselClass_Capacity();

	/**
	 * Returns the meta object for the attribute '{@link scenario.fleet.VesselClass#getMinSpeed <em>Min Speed</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Min Speed</em>'.
	 * @see scenario.fleet.VesselClass#getMinSpeed()
	 * @see #getVesselClass()
	 * @generated
	 */
	EAttribute getVesselClass_MinSpeed();

	/**
	 * Returns the meta object for the attribute '{@link scenario.fleet.VesselClass#getMaxSpeed <em>Max Speed</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Max Speed</em>'.
	 * @see scenario.fleet.VesselClass#getMaxSpeed()
	 * @see #getVesselClass()
	 * @generated
	 */
	EAttribute getVesselClass_MaxSpeed();

	/**
	 * Returns the meta object for the attribute '{@link scenario.fleet.VesselClass#getBaseFuelUnitPrice <em>Base Fuel Unit Price</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Base Fuel Unit Price</em>'.
	 * @see scenario.fleet.VesselClass#getBaseFuelUnitPrice()
	 * @see #getVesselClass()
	 * @generated
	 */
	EAttribute getVesselClass_BaseFuelUnitPrice();

	/**
	 * Returns the meta object for the containment reference '{@link scenario.fleet.VesselClass#getLadenAttributes <em>Laden Attributes</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Laden Attributes</em>'.
	 * @see scenario.fleet.VesselClass#getLadenAttributes()
	 * @see #getVesselClass()
	 * @generated
	 */
	EReference getVesselClass_LadenAttributes();

	/**
	 * Returns the meta object for the containment reference '{@link scenario.fleet.VesselClass#getBallastAttributes <em>Ballast Attributes</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Ballast Attributes</em>'.
	 * @see scenario.fleet.VesselClass#getBallastAttributes()
	 * @see #getVesselClass()
	 * @generated
	 */
	EReference getVesselClass_BallastAttributes();

	/**
	 * Returns the meta object for class '{@link scenario.fleet.FuelConsumptionLine <em>Fuel Consumption Line</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Fuel Consumption Line</em>'.
	 * @see scenario.fleet.FuelConsumptionLine
	 * @generated
	 */
	EClass getFuelConsumptionLine();

	/**
	 * Returns the meta object for the attribute '{@link scenario.fleet.FuelConsumptionLine#getSpeed <em>Speed</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Speed</em>'.
	 * @see scenario.fleet.FuelConsumptionLine#getSpeed()
	 * @see #getFuelConsumptionLine()
	 * @generated
	 */
	EAttribute getFuelConsumptionLine_Speed();

	/**
	 * Returns the meta object for the attribute '{@link scenario.fleet.FuelConsumptionLine#getConsumption <em>Consumption</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Consumption</em>'.
	 * @see scenario.fleet.FuelConsumptionLine#getConsumption()
	 * @see #getFuelConsumptionLine()
	 * @generated
	 */
	EAttribute getFuelConsumptionLine_Consumption();

	/**
	 * Returns the meta object for class '{@link scenario.fleet.VesselStateAttributes <em>Vessel State Attributes</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Vessel State Attributes</em>'.
	 * @see scenario.fleet.VesselStateAttributes
	 * @generated
	 */
	EClass getVesselStateAttributes();

	/**
	 * Returns the meta object for the attribute '{@link scenario.fleet.VesselStateAttributes#getVesselState <em>Vessel State</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Vessel State</em>'.
	 * @see scenario.fleet.VesselStateAttributes#getVesselState()
	 * @see #getVesselStateAttributes()
	 * @generated
	 */
	EAttribute getVesselStateAttributes_VesselState();

	/**
	 * Returns the meta object for the attribute '{@link scenario.fleet.VesselStateAttributes#getNboRate <em>Nbo Rate</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Nbo Rate</em>'.
	 * @see scenario.fleet.VesselStateAttributes#getNboRate()
	 * @see #getVesselStateAttributes()
	 * @generated
	 */
	EAttribute getVesselStateAttributes_NboRate();

	/**
	 * Returns the meta object for the attribute '{@link scenario.fleet.VesselStateAttributes#getIdleNBORate <em>Idle NBO Rate</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Idle NBO Rate</em>'.
	 * @see scenario.fleet.VesselStateAttributes#getIdleNBORate()
	 * @see #getVesselStateAttributes()
	 * @generated
	 */
	EAttribute getVesselStateAttributes_IdleNBORate();

	/**
	 * Returns the meta object for the attribute '{@link scenario.fleet.VesselStateAttributes#getIdleConsumptionRate <em>Idle Consumption Rate</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Idle Consumption Rate</em>'.
	 * @see scenario.fleet.VesselStateAttributes#getIdleConsumptionRate()
	 * @see #getVesselStateAttributes()
	 * @generated
	 */
	EAttribute getVesselStateAttributes_IdleConsumptionRate();

	/**
	 * Returns the meta object for the containment reference list '{@link scenario.fleet.VesselStateAttributes#getFuelConsumptionCurve <em>Fuel Consumption Curve</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Fuel Consumption Curve</em>'.
	 * @see scenario.fleet.VesselStateAttributes#getFuelConsumptionCurve()
	 * @see #getVesselStateAttributes()
	 * @generated
	 */
	EReference getVesselStateAttributes_FuelConsumptionCurve();

	/**
	 * Returns the meta object for enum '{@link scenario.fleet.VesselState <em>Vessel State</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Vessel State</em>'.
	 * @see scenario.fleet.VesselState
	 * @generated
	 */
	EEnum getVesselState();

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
		 * The meta object literal for the '{@link scenario.fleet.impl.FleetModelImpl <em>Model</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see scenario.fleet.impl.FleetModelImpl
		 * @see scenario.fleet.impl.FleetPackageImpl#getFleetModel()
		 * @generated
		 */
		EClass FLEET_MODEL = eINSTANCE.getFleetModel();

		/**
		 * The meta object literal for the '<em><b>Fleet</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference FLEET_MODEL__FLEET = eINSTANCE.getFleetModel_Fleet();

		/**
		 * The meta object literal for the '<em><b>Vessel Classes</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference FLEET_MODEL__VESSEL_CLASSES = eINSTANCE.getFleetModel_VesselClasses();

		/**
		 * The meta object literal for the '{@link scenario.fleet.impl.VesselImpl <em>Vessel</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see scenario.fleet.impl.VesselImpl
		 * @see scenario.fleet.impl.FleetPackageImpl#getVessel()
		 * @generated
		 */
		EClass VESSEL = eINSTANCE.getVessel();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VESSEL__NAME = eINSTANCE.getVessel_Name();

		/**
		 * The meta object literal for the '<em><b>Class</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference VESSEL__CLASS = eINSTANCE.getVessel_Class();

		/**
		 * The meta object literal for the '<em><b>Start Port</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference VESSEL__START_PORT = eINSTANCE.getVessel_StartPort();

		/**
		 * The meta object literal for the '<em><b>End Port</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference VESSEL__END_PORT = eINSTANCE.getVessel_EndPort();

		/**
		 * The meta object literal for the '{@link scenario.fleet.impl.VesselClassImpl <em>Vessel Class</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see scenario.fleet.impl.VesselClassImpl
		 * @see scenario.fleet.impl.FleetPackageImpl#getVesselClass()
		 * @generated
		 */
		EClass VESSEL_CLASS = eINSTANCE.getVesselClass();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VESSEL_CLASS__NAME = eINSTANCE.getVesselClass_Name();

		/**
		 * The meta object literal for the '<em><b>Capacity</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VESSEL_CLASS__CAPACITY = eINSTANCE.getVesselClass_Capacity();

		/**
		 * The meta object literal for the '<em><b>Min Speed</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VESSEL_CLASS__MIN_SPEED = eINSTANCE.getVesselClass_MinSpeed();

		/**
		 * The meta object literal for the '<em><b>Max Speed</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VESSEL_CLASS__MAX_SPEED = eINSTANCE.getVesselClass_MaxSpeed();

		/**
		 * The meta object literal for the '<em><b>Base Fuel Unit Price</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VESSEL_CLASS__BASE_FUEL_UNIT_PRICE = eINSTANCE.getVesselClass_BaseFuelUnitPrice();

		/**
		 * The meta object literal for the '<em><b>Laden Attributes</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference VESSEL_CLASS__LADEN_ATTRIBUTES = eINSTANCE.getVesselClass_LadenAttributes();

		/**
		 * The meta object literal for the '<em><b>Ballast Attributes</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference VESSEL_CLASS__BALLAST_ATTRIBUTES = eINSTANCE.getVesselClass_BallastAttributes();

		/**
		 * The meta object literal for the '{@link scenario.fleet.impl.FuelConsumptionLineImpl <em>Fuel Consumption Line</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see scenario.fleet.impl.FuelConsumptionLineImpl
		 * @see scenario.fleet.impl.FleetPackageImpl#getFuelConsumptionLine()
		 * @generated
		 */
		EClass FUEL_CONSUMPTION_LINE = eINSTANCE.getFuelConsumptionLine();

		/**
		 * The meta object literal for the '<em><b>Speed</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute FUEL_CONSUMPTION_LINE__SPEED = eINSTANCE.getFuelConsumptionLine_Speed();

		/**
		 * The meta object literal for the '<em><b>Consumption</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute FUEL_CONSUMPTION_LINE__CONSUMPTION = eINSTANCE.getFuelConsumptionLine_Consumption();

		/**
		 * The meta object literal for the '{@link scenario.fleet.impl.VesselStateAttributesImpl <em>Vessel State Attributes</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see scenario.fleet.impl.VesselStateAttributesImpl
		 * @see scenario.fleet.impl.FleetPackageImpl#getVesselStateAttributes()
		 * @generated
		 */
		EClass VESSEL_STATE_ATTRIBUTES = eINSTANCE.getVesselStateAttributes();

		/**
		 * The meta object literal for the '<em><b>Vessel State</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VESSEL_STATE_ATTRIBUTES__VESSEL_STATE = eINSTANCE.getVesselStateAttributes_VesselState();

		/**
		 * The meta object literal for the '<em><b>Nbo Rate</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VESSEL_STATE_ATTRIBUTES__NBO_RATE = eINSTANCE.getVesselStateAttributes_NboRate();

		/**
		 * The meta object literal for the '<em><b>Idle NBO Rate</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VESSEL_STATE_ATTRIBUTES__IDLE_NBO_RATE = eINSTANCE.getVesselStateAttributes_IdleNBORate();

		/**
		 * The meta object literal for the '<em><b>Idle Consumption Rate</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VESSEL_STATE_ATTRIBUTES__IDLE_CONSUMPTION_RATE = eINSTANCE.getVesselStateAttributes_IdleConsumptionRate();

		/**
		 * The meta object literal for the '<em><b>Fuel Consumption Curve</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference VESSEL_STATE_ATTRIBUTES__FUEL_CONSUMPTION_CURVE = eINSTANCE.getVesselStateAttributes_FuelConsumptionCurve();

		/**
		 * The meta object literal for the '{@link scenario.fleet.VesselState <em>Vessel State</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see scenario.fleet.VesselState
		 * @see scenario.fleet.impl.FleetPackageImpl#getVesselState()
		 * @generated
		 */
		EEnum VESSEL_STATE = eINSTANCE.getVesselState();

	}

} //FleetPackage
