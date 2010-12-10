/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package scenario.schedule.fleet;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see scenario.schedule.fleet.FleetPackage
 * @generated
 */
public interface FleetFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	FleetFactory eINSTANCE = scenario.schedule.fleet.impl.FleetFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>Allocated Vessel</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Allocated Vessel</em>'.
	 * @generated
	 */
	AllocatedVessel createAllocatedVessel();

	/**
	 * Returns a new object of class '<em>Vessel</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Vessel</em>'.
	 * @generated
	 */
	FleetVessel createFleetVessel();

	/**
	 * Returns a new object of class '<em>Spot Vessel</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Spot Vessel</em>'.
	 * @generated
	 */
	SpotVessel createSpotVessel();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	FleetPackage getFleetPackage();

} //FleetFactory
