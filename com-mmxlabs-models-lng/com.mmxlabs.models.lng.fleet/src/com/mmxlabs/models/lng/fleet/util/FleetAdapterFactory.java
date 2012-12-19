/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet.util;

import com.mmxlabs.models.lng.fleet.*;

import com.mmxlabs.models.lng.types.ABaseFuel;
import com.mmxlabs.models.lng.types.AVessel;
import com.mmxlabs.models.lng.types.AVesselClass;
import com.mmxlabs.models.lng.types.AVesselEvent;
import com.mmxlabs.models.lng.types.AVesselSet;

import com.mmxlabs.models.lng.types.ITimezoneProvider;
import com.mmxlabs.models.mmxcore.MMXObject;
import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.models.mmxcore.UUIDObject;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;

import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see com.mmxlabs.models.lng.fleet.FleetPackage
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
			public Adapter caseVessel(Vessel object) {
				return createVesselAdapter();
			}
			@Override
			public Adapter caseVesselClass(VesselClass object) {
				return createVesselClassAdapter();
			}
			@Override
			public Adapter caseVesselEvent(VesselEvent object) {
				return createVesselEventAdapter();
			}
			@Override
			public Adapter caseFleetModel(FleetModel object) {
				return createFleetModelAdapter();
			}
			@Override
			public Adapter caseBaseFuel(BaseFuel object) {
				return createBaseFuelAdapter();
			}
			@Override
			public Adapter caseDryDockEvent(DryDockEvent object) {
				return createDryDockEventAdapter();
			}
			@Override
			public Adapter caseCharterOutEvent(CharterOutEvent object) {
				return createCharterOutEventAdapter();
			}
			@Override
			public Adapter caseHeelOptions(HeelOptions object) {
				return createHeelOptionsAdapter();
			}
			@Override
			public Adapter caseVesselStateAttributes(VesselStateAttributes object) {
				return createVesselStateAttributesAdapter();
			}
			@Override
			public Adapter caseVesselAvailability(VesselAvailability object) {
				return createVesselAvailabilityAdapter();
			}
			@Override
			public Adapter caseFuelConsumption(FuelConsumption object) {
				return createFuelConsumptionAdapter();
			}
			@Override
			public Adapter caseMaintenanceEvent(MaintenanceEvent object) {
				return createMaintenanceEventAdapter();
			}
			@Override
			public Adapter caseVesselClassRouteParameters(VesselClassRouteParameters object) {
				return createVesselClassRouteParametersAdapter();
			}
			@Override
			public Adapter caseVesselGroup(VesselGroup object) {
				return createVesselGroupAdapter();
			}
			@Override
			public Adapter caseVesselTypeGroup(VesselTypeGroup object) {
				return createVesselTypeGroupAdapter();
			}
			@Override
			public Adapter caseMMXObject(MMXObject object) {
				return createMMXObjectAdapter();
			}
			@Override
			public Adapter caseUUIDObject(UUIDObject object) {
				return createUUIDObjectAdapter();
			}
			@Override
			public Adapter caseNamedObject(NamedObject object) {
				return createNamedObjectAdapter();
			}
			@Override
			public Adapter caseAVesselSet(AVesselSet object) {
				return createAVesselSetAdapter();
			}
			@Override
			public Adapter caseAVessel(AVessel object) {
				return createAVesselAdapter();
			}
			@Override
			public Adapter caseAVesselClass(AVesselClass object) {
				return createAVesselClassAdapter();
			}
			@Override
			public Adapter caseAVesselEvent(AVesselEvent object) {
				return createAVesselEventAdapter();
			}
			@Override
			public Adapter caseITimezoneProvider(ITimezoneProvider object) {
				return createITimezoneProviderAdapter();
			}
			@Override
			public Adapter caseABaseFuel(ABaseFuel object) {
				return createABaseFuelAdapter();
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
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.fleet.Vessel <em>Vessel</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.fleet.Vessel
	 * @generated
	 */
	public Adapter createVesselAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.fleet.VesselClass <em>Vessel Class</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.fleet.VesselClass
	 * @generated
	 */
	public Adapter createVesselClassAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.fleet.VesselEvent <em>Vessel Event</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.fleet.VesselEvent
	 * @generated
	 */
	public Adapter createVesselEventAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.fleet.FleetModel <em>Model</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.fleet.FleetModel
	 * @generated
	 */
	public Adapter createFleetModelAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.fleet.BaseFuel <em>Base Fuel</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.fleet.BaseFuel
	 * @generated
	 */
	public Adapter createBaseFuelAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.fleet.DryDockEvent <em>Dry Dock Event</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.fleet.DryDockEvent
	 * @generated
	 */
	public Adapter createDryDockEventAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.fleet.CharterOutEvent <em>Charter Out Event</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.fleet.CharterOutEvent
	 * @generated
	 */
	public Adapter createCharterOutEventAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.fleet.HeelOptions <em>Heel Options</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.fleet.HeelOptions
	 * @generated
	 */
	public Adapter createHeelOptionsAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.fleet.VesselStateAttributes <em>Vessel State Attributes</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.fleet.VesselStateAttributes
	 * @generated
	 */
	public Adapter createVesselStateAttributesAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.fleet.VesselAvailability <em>Vessel Availability</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.fleet.VesselAvailability
	 * @generated
	 */
	public Adapter createVesselAvailabilityAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.fleet.FuelConsumption <em>Fuel Consumption</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.fleet.FuelConsumption
	 * @generated
	 */
	public Adapter createFuelConsumptionAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.fleet.MaintenanceEvent <em>Maintenance Event</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.fleet.MaintenanceEvent
	 * @generated
	 */
	public Adapter createMaintenanceEventAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.fleet.VesselClassRouteParameters <em>Vessel Class Route Parameters</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.fleet.VesselClassRouteParameters
	 * @generated
	 */
	public Adapter createVesselClassRouteParametersAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.fleet.VesselGroup <em>Vessel Group</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.fleet.VesselGroup
	 * @generated
	 */
	public Adapter createVesselGroupAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.fleet.VesselTypeGroup <em>Vessel Type Group</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.fleet.VesselTypeGroup
	 * @generated
	 */
	public Adapter createVesselTypeGroupAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.mmxcore.MMXObject <em>MMX Object</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.mmxcore.MMXObject
	 * @generated
	 */
	public Adapter createMMXObjectAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.mmxcore.UUIDObject <em>UUID Object</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.mmxcore.UUIDObject
	 * @generated
	 */
	public Adapter createUUIDObjectAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.mmxcore.NamedObject <em>Named Object</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.mmxcore.NamedObject
	 * @generated
	 */
	public Adapter createNamedObjectAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.types.AVesselSet <em>AVessel Set</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.types.AVesselSet
	 * @generated
	 */
	public Adapter createAVesselSetAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.types.AVessel <em>AVessel</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.types.AVessel
	 * @generated
	 */
	public Adapter createAVesselAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.types.AVesselClass <em>AVessel Class</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.types.AVesselClass
	 * @generated
	 */
	public Adapter createAVesselClassAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.types.AVesselEvent <em>AVessel Event</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.types.AVesselEvent
	 * @generated
	 */
	public Adapter createAVesselEventAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.types.ITimezoneProvider <em>ITimezone Provider</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.types.ITimezoneProvider
	 * @generated
	 */
	public Adapter createITimezoneProviderAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link com.mmxlabs.models.lng.types.ABaseFuel <em>ABase Fuel</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see com.mmxlabs.models.lng.types.ABaseFuel
	 * @generated
	 */
	public Adapter createABaseFuelAdapter() {
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
