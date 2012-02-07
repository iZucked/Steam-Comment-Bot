/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package scenario.schedule.fleetallocation;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc --> The <b>Factory</b> for the model. It provides a create method for each non-abstract class of the model. <!-- end-user-doc -->
 * 
 * @see scenario.schedule.fleetallocation.FleetallocationPackage
 * @generated
 */
public interface FleetallocationFactory extends EFactory {
	/**
	 * The singleton instance of the factory. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	FleetallocationFactory eINSTANCE = scenario.schedule.fleetallocation.impl.FleetallocationFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>Allocated Vessel</em>'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Allocated Vessel</em>'.
	 * @generated
	 */
	AllocatedVessel createAllocatedVessel();

	/**
	 * Returns a new object of class '<em>Fleet Vessel</em>'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Fleet Vessel</em>'.
	 * @generated
	 */
	FleetVessel createFleetVessel();

	/**
	 * Returns a new object of class '<em>Spot Vessel</em>'. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Spot Vessel</em>'.
	 * @generated
	 */
	SpotVessel createSpotVessel();

	/**
	 * Returns the package supported by this factory. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the package supported by this factory.
	 * @generated
	 */
	FleetallocationPackage getFleetallocationPackage();

} // FleetallocationFactory
