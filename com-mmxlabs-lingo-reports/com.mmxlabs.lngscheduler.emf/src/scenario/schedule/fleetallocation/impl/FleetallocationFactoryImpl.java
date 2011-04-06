/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package scenario.schedule.fleetallocation.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

import scenario.schedule.fleetallocation.*;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class FleetallocationFactoryImpl extends EFactoryImpl implements FleetallocationFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static FleetallocationFactory init() {
		try {
			FleetallocationFactory theFleetallocationFactory = (FleetallocationFactory)EPackage.Registry.INSTANCE.getEFactory("http://com.mmxlabs.lng.emf/schedule/fleetallocation"); 
			if (theFleetallocationFactory != null) {
				return theFleetallocationFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new FleetallocationFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public FleetallocationFactoryImpl() {
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
			case FleetallocationPackage.ALLOCATED_VESSEL: return createAllocatedVessel();
			case FleetallocationPackage.FLEET_VESSEL: return createFleetVessel();
			case FleetallocationPackage.SPOT_VESSEL: return createSpotVessel();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public AllocatedVessel createAllocatedVessel() {
		AllocatedVesselImpl allocatedVessel = new AllocatedVesselImpl();
		return allocatedVessel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public FleetVessel createFleetVessel() {
		FleetVesselImpl fleetVessel = new FleetVesselImpl();
		return fleetVessel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public SpotVessel createSpotVessel() {
		SpotVesselImpl spotVessel = new SpotVesselImpl();
		return spotVessel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public FleetallocationPackage getFleetallocationPackage() {
		return (FleetallocationPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static FleetallocationPackage getPackage() {
		return FleetallocationPackage.eINSTANCE;
	}

} //FleetallocationFactoryImpl
