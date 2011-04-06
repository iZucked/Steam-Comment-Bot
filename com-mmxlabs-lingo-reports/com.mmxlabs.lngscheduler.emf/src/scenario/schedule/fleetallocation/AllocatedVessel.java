/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package scenario.schedule.fleetallocation;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Allocated Vessel</b></em>'.
 * <!-- end-user-doc -->
 *
 *
 * @see scenario.schedule.fleetallocation.FleetallocationPackage#getAllocatedVessel()
 * @model
 * @generated
 */
public interface AllocatedVessel extends EObject {

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation" required="true"
	 *        annotation="http://www.eclipse.org/emf/2002/GenModel body='return 0;'"
	 * @generated
	 */
	double getHourlyCharterPrice();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation" required="true"
	 * @generated
	 */
	String getName();
} // AllocatedVessel
