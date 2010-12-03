/**
 * Copyright (C) Minimax Labs Ltd., 2010
 * All rights reserved.
 */

package scenario.port.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;

import scenario.port.*;
import scenario.port.DistanceLine;
import scenario.port.DistanceModel;
import scenario.port.Port;
import scenario.port.PortFactory;
import scenario.port.PortModel;
import scenario.port.PortPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class PortFactoryImpl extends EFactoryImpl implements PortFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static PortFactory init() {
		try {
			PortFactory thePortFactory = (PortFactory)EPackage.Registry.INSTANCE.getEFactory("http://com.mmxlabs.lng.emf/port"); 
			if (thePortFactory != null) {
				return thePortFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new PortFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public PortFactoryImpl() {
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
			case PortPackage.PORT_MODEL: return createPortModel();
			case PortPackage.PORT: return createPort();
			case PortPackage.DISTANCE_MODEL: return createDistanceModel();
			case PortPackage.DISTANCE_LINE: return createDistanceLine();
			case PortPackage.CANAL: return createCanal();
			case PortPackage.CANAL_MODEL: return createCanalModel();
			case PortPackage.VESSEL_CLASS_COST: return createVesselClassCost();
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
	public PortModel createPortModel() {
		PortModelImpl portModel = new PortModelImpl();
		return portModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Port createPort() {
		PortImpl port = new PortImpl();
		return port;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DistanceModel createDistanceModel() {
		DistanceModelImpl distanceModel = new DistanceModelImpl();
		return distanceModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DistanceLine createDistanceLine() {
		DistanceLineImpl distanceLine = new DistanceLineImpl();
		return distanceLine;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Canal createCanal() {
		CanalImpl canal = new CanalImpl();
		return canal;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CanalModel createCanalModel() {
		CanalModelImpl canalModel = new CanalModelImpl();
		return canalModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public VesselClassCost createVesselClassCost() {
		VesselClassCostImpl vesselClassCost = new VesselClassCostImpl();
		return vesselClassCost;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public PortPackage getPortPackage() {
		return (PortPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static PortPackage getPackage() {
		return PortPackage.eINSTANCE;
	}

} //PortFactoryImpl
