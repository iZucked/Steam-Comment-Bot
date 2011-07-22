/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package scenario.fleet.util;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;
import org.eclipse.emf.ecore.EObject;

import scenario.NamedObject;
import scenario.ScenarioObject;
import scenario.fleet.CharterOut;
import scenario.fleet.Drydock;
import scenario.fleet.FleetModel;
import scenario.fleet.FleetPackage;
import scenario.fleet.FuelConsumptionLine;
import scenario.fleet.PortAndTime;
import scenario.fleet.PortExclusion;
import scenario.fleet.Vessel;
import scenario.fleet.VesselClass;
import scenario.fleet.VesselClassCost;
import scenario.fleet.VesselEvent;
import scenario.fleet.VesselFuel;
import scenario.fleet.VesselStateAttributes;

/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see scenario.fleet.FleetPackage
 * @generated
 */
public class FleetAdapterFactory extends AdapterFactoryImpl {
	/**
	 * The cached model package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected static FleetPackage modelPackage;

	/**
	 * Creates an instance of the adapter factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public FleetAdapterFactory() {
		if (modelPackage == null) {
			modelPackage = FleetPackage.eINSTANCE;
		}
	}

	/**
	 * Returns whether this factory is applicable for the type of the object.
	 * <!-- begin-user-doc -->
	 * This implementation returns <code>true</code> if the object is either the model's package or is an instance object of the model.
	 * <!-- end-user-doc -->
	 * @return whether this factory is applicable for the type of the object.
	 * @generated
	 */
	@Override
	public boolean isFactoryForType(Object object) {
		if (object == modelPackage) {
			return true;
		}
		if (object instanceof EObject) {
			return ((EObject)object).eClass().getEPackage() == modelPackage;
		}
		return false;
	}

	/**
	 * The switch that delegates to the <code>createXXX</code> methods.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected FleetSwitch<Adapter> modelSwitch =
		new FleetSwitch<Adapter>() {
			@Override
			public Adapter caseFleetModel(FleetModel object) {
				return createFleetModelAdapter();
			}
			@Override
			public Adapter caseVessel(Vessel object) {
				return createVesselAdapter();
			}
			@Override
			public Adapter caseVesselClass(VesselClass object) {
				return createVesselClassAdapter();
			}
			@Override
			public Adapter caseFuelConsumptionLine(FuelConsumptionLine object) {
				return createFuelConsumptionLineAdapter();
			}
			@Override
			public Adapter caseVesselStateAttributes(VesselStateAttributes object) {
				return createVesselStateAttributesAdapter();
			}
			@Override
			public Adapter casePortAndTime(PortAndTime object) {
				return createPortAndTimeAdapter();
			}
			@Override
			public Adapter caseVesselEvent(VesselEvent object) {
				return createVesselEventAdapter();
			}
			@Override
			public Adapter caseCharterOut(CharterOut object) {
				return createCharterOutAdapter();
			}
			@Override
			public Adapter caseDrydock(Drydock object) {
				return createDrydockAdapter();
			}
			@Override
			public Adapter caseVesselFuel(VesselFuel object) {
				return createVesselFuelAdapter();
			}
			@Override
			public Adapter casePortExclusion(PortExclusion object) {
				return createPortExclusionAdapter();
			}
			@Override
			public Adapter caseVesselClassCost(VesselClassCost object) {
				return createVesselClassCostAdapter();
			}
			@Override
			public Adapter caseScenarioObject(ScenarioObject object) {
				return createScenarioObjectAdapter();
			}
			@Override
			public Adapter caseNamedObject(NamedObject object) {
				return createNamedObjectAdapter();
			}
			@Override
			public Adapter defaultCase(EObject object) {
				return createEObjectAdapter();
			}
		};

	/**
	 * Creates an adapter for the <code>target</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param target the object to adapt.
	 * @return the adapter for the <code>target</code>.
	 * @generated
	 */
	@Override
	public Adapter createAdapter(Notifier target) {
		return modelSwitch.doSwitch((EObject)target);
	}


	/**
	 * Creates a new adapter for an object of class '{@link scenario.fleet.FleetModel <em>Model</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see scenario.fleet.FleetModel
	 * @generated
	 */
	public Adapter createFleetModelAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link scenario.fleet.Vessel <em>Vessel</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see scenario.fleet.Vessel
	 * @generated
	 */
	public Adapter createVesselAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link scenario.fleet.VesselClass <em>Vessel Class</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see scenario.fleet.VesselClass
	 * @generated
	 */
	public Adapter createVesselClassAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link scenario.fleet.FuelConsumptionLine <em>Fuel Consumption Line</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see scenario.fleet.FuelConsumptionLine
	 * @generated
	 */
	public Adapter createFuelConsumptionLineAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link scenario.fleet.VesselStateAttributes <em>Vessel State Attributes</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see scenario.fleet.VesselStateAttributes
	 * @generated
	 */
	public Adapter createVesselStateAttributesAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link scenario.fleet.PortAndTime <em>Port And Time</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see scenario.fleet.PortAndTime
	 * @generated
	 */
	public Adapter createPortAndTimeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link scenario.fleet.VesselEvent <em>Vessel Event</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see scenario.fleet.VesselEvent
	 * @generated
	 */
	public Adapter createVesselEventAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link scenario.fleet.CharterOut <em>Charter Out</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see scenario.fleet.CharterOut
	 * @generated
	 */
	public Adapter createCharterOutAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link scenario.fleet.Drydock <em>Drydock</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see scenario.fleet.Drydock
	 * @generated
	 */
	public Adapter createDrydockAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link scenario.fleet.VesselFuel <em>Vessel Fuel</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see scenario.fleet.VesselFuel
	 * @generated
	 */
	public Adapter createVesselFuelAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link scenario.fleet.PortExclusion <em>Port Exclusion</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see scenario.fleet.PortExclusion
	 * @generated
	 */
	public Adapter createPortExclusionAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link scenario.fleet.VesselClassCost <em>Vessel Class Cost</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see scenario.fleet.VesselClassCost
	 * @generated
	 */
	public Adapter createVesselClassCostAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link scenario.ScenarioObject <em>Object</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see scenario.ScenarioObject
	 * @generated
	 */
	public Adapter createScenarioObjectAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link scenario.NamedObject <em>Named Object</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see scenario.NamedObject
	 * @generated
	 */
	public Adapter createNamedObjectAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for the default case.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @generated
	 */
	public Adapter createEObjectAdapter() {
		return null;
	}

} //FleetAdapterFactory
