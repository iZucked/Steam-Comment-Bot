/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package scenario.fleet;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see scenario.fleet.FleetPackage
 * @generated
 */
public interface FleetFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	FleetFactory eINSTANCE = scenario.fleet.impl.FleetFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>Model</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Model</em>'.
	 * @generated
	 */
	FleetModel createFleetModel();

	/**
	 * Returns a new object of class '<em>Vessel</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Vessel</em>'.
	 * @generated
	 */
	Vessel createVessel();

	/**
	 * Returns a new object of class '<em>Vessel Class</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Vessel Class</em>'.
	 * @generated
	 */
	VesselClass createVesselClass();

	/**
	 * Returns a new object of class '<em>Fuel Consumption Line</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Fuel Consumption Line</em>'.
	 * @generated
	 */
	FuelConsumptionLine createFuelConsumptionLine();

	/**
	 * Returns a new object of class '<em>Vessel State Attributes</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Vessel State Attributes</em>'.
	 * @generated
	 */
	VesselStateAttributes createVesselStateAttributes();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	FleetPackage getFleetPackage();

} //FleetFactory
