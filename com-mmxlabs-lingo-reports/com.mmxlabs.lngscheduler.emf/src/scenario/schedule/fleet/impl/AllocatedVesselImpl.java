/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package scenario.schedule.fleet.impl;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.EObjectImpl;

import scenario.schedule.fleet.AllocatedVessel;
import scenario.schedule.fleet.FleetPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Allocated Vessel</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * </p>
 *
 * @generated
 */
public class AllocatedVesselImpl extends EObjectImpl implements AllocatedVessel {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected AllocatedVesselImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return FleetPackage.Literals.ALLOCATED_VESSEL;
	}

	@Override
	public String getName() {
		throw new UnsupportedOperationException("This should never be called");
	}

} //AllocatedVesselImpl
