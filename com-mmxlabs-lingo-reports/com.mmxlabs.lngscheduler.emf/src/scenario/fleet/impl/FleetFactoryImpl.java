/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package scenario.fleet.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;

import scenario.fleet.CharterOut;
import scenario.fleet.Drydock;
import scenario.fleet.FleetFactory;
import scenario.fleet.FleetModel;
import scenario.fleet.FleetPackage;
import scenario.fleet.FuelConsumptionLine;
import scenario.fleet.HeelOptions;
import scenario.fleet.PortAndTime;
import scenario.fleet.PortExclusion;
import scenario.fleet.PortTimeAndHeel;
import scenario.fleet.Vessel;
import scenario.fleet.VesselClass;
import scenario.fleet.VesselClassCost;
import scenario.fleet.VesselFuel;
import scenario.fleet.VesselState;
import scenario.fleet.VesselStateAttributes;

/**
 * <!-- begin-user-doc --> An implementation of the model <b>Factory</b>. <!-- end-user-doc -->
 * 
 * @generated
 */
public class FleetFactoryImpl extends EFactoryImpl implements FleetFactory {
	/**
	 * Creates the default factory implementation. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public static FleetFactory init() {
		try {
			final FleetFactory theFleetFactory = (FleetFactory) EPackage.Registry.INSTANCE.getEFactory("http://com.mmxlabs.lng.emf2/fleet");
			if (theFleetFactory != null) {
				return theFleetFactory;
			}
		} catch (final Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new FleetFactoryImpl();
	}

	/**
	 * Creates an instance of the factory. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public FleetFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public EObject create(final EClass eClass) {
		switch (eClass.getClassifierID()) {
		case FleetPackage.FLEET_MODEL:
			return createFleetModel();
		case FleetPackage.VESSEL:
			return createVessel();
		case FleetPackage.VESSEL_CLASS:
			return createVesselClass();
		case FleetPackage.FUEL_CONSUMPTION_LINE:
			return createFuelConsumptionLine();
		case FleetPackage.VESSEL_STATE_ATTRIBUTES:
			return createVesselStateAttributes();
		case FleetPackage.PORT_AND_TIME:
			return createPortAndTime();
		case FleetPackage.CHARTER_OUT:
			return createCharterOut();
		case FleetPackage.DRYDOCK:
			return createDrydock();
		case FleetPackage.VESSEL_FUEL:
			return createVesselFuel();
		case FleetPackage.PORT_EXCLUSION:
			return createPortExclusion();
		case FleetPackage.VESSEL_CLASS_COST:
			return createVesselClassCost();
		case FleetPackage.PORT_TIME_AND_HEEL:
			return createPortTimeAndHeel();
		case FleetPackage.HEEL_OPTIONS:
			return createHeelOptions();
		default:
			throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public Object createFromString(final EDataType eDataType, final String initialValue) {
		switch (eDataType.getClassifierID()) {
		case FleetPackage.VESSEL_STATE:
			return createVesselStateFromString(eDataType, initialValue);
		default:
			throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public String convertToString(final EDataType eDataType, final Object instanceValue) {
		switch (eDataType.getClassifierID()) {
		case FleetPackage.VESSEL_STATE:
			return convertVesselStateToString(eDataType, instanceValue);
		default:
			throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public FleetModel createFleetModel() {
		final FleetModelImpl fleetModel = new FleetModelImpl();
		return fleetModel;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public Vessel createVessel() {
		final VesselImpl vessel = new VesselImpl();
		return vessel;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public VesselClass createVesselClass() {
		final VesselClassImpl vesselClass = new VesselClassImpl();
		return vesselClass;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public FuelConsumptionLine createFuelConsumptionLine() {
		final FuelConsumptionLineImpl fuelConsumptionLine = new FuelConsumptionLineImpl();
		return fuelConsumptionLine;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public VesselStateAttributes createVesselStateAttributes() {
		final VesselStateAttributesImpl vesselStateAttributes = new VesselStateAttributesImpl();
		return vesselStateAttributes;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public PortAndTime createPortAndTime() {
		final PortAndTimeImpl portAndTime = new PortAndTimeImpl();
		return portAndTime;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public CharterOut createCharterOut() {
		final CharterOutImpl charterOut = new CharterOutImpl();
		return charterOut;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public Drydock createDrydock() {
		final DrydockImpl drydock = new DrydockImpl();
		return drydock;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public VesselFuel createVesselFuel() {
		final VesselFuelImpl vesselFuel = new VesselFuelImpl();
		return vesselFuel;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public PortExclusion createPortExclusion() {
		final PortExclusionImpl portExclusion = new PortExclusionImpl();
		return portExclusion;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public VesselClassCost createVesselClassCost() {
		final VesselClassCostImpl vesselClassCost = new VesselClassCostImpl();
		return vesselClassCost;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public PortTimeAndHeel createPortTimeAndHeel() {
		final PortTimeAndHeelImpl portTimeAndHeel = new PortTimeAndHeelImpl();
		return portTimeAndHeel;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public HeelOptions createHeelOptions() {
		final HeelOptionsImpl heelOptions = new HeelOptionsImpl();
		return heelOptions;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public VesselState createVesselStateFromString(final EDataType eDataType, final String initialValue) {
		final VesselState result = VesselState.get(initialValue);
		if (result == null) {
			throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		}
		return result;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String convertVesselStateToString(final EDataType eDataType, final Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public FleetPackage getFleetPackage() {
		return (FleetPackage) getEPackage();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static FleetPackage getPackage() {
		return FleetPackage.eINSTANCE;
	}

} // FleetFactoryImpl
