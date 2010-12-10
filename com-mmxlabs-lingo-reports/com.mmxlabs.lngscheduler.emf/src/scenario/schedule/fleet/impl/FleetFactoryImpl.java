/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package scenario.schedule.fleet.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

import scenario.schedule.fleet.*;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class FleetFactoryImpl extends EFactoryImpl implements FleetFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static FleetFactory init() {
		try {
			FleetFactory theFleetFactory = (FleetFactory)EPackage.Registry.INSTANCE.getEFactory("http://com.mmxlabs.lng.emf/schedule/fleet"); 
			if (theFleetFactory != null) {
				return theFleetFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new FleetFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public FleetFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
			case FleetPackage.ALLOCATED_VESSEL: return createAllocatedVessel();
			case FleetPackage.FLEET_VESSEL: return createFleetVessel();
			case FleetPackage.SPOT_VESSEL: return createSpotVessel();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AllocatedVessel createAllocatedVessel() {
		AllocatedVesselImpl allocatedVessel = new AllocatedVesselImpl();
		return allocatedVessel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public FleetVessel createFleetVessel() {
		FleetVesselImpl fleetVessel = new FleetVesselImpl();
		return fleetVessel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SpotVessel createSpotVessel() {
		SpotVesselImpl spotVessel = new SpotVesselImpl();
		return spotVessel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public FleetPackage getFleetPackage() {
		return (FleetPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static FleetPackage getPackage() {
		return FleetPackage.eINSTANCE;
	}

} //FleetFactoryImpl
