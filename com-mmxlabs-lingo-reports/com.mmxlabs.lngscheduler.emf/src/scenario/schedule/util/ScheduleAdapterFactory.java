/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package scenario.schedule.util;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;
import org.eclipse.emf.ecore.EObject;

import scenario.ScenarioObject;
import scenario.schedule.*;
import scenario.schedule.BookedRevenue;
import scenario.schedule.CargoAllocation;
import scenario.schedule.CargoRevenue;
import scenario.schedule.Schedule;
import scenario.schedule.ScheduleFitness;
import scenario.schedule.ScheduleModel;
import scenario.schedule.SchedulePackage;
import scenario.schedule.Sequence;
import scenario.schedule.VesselEventRevenue;

/**
 * <!-- begin-user-doc --> The <b>Adapter Factory</b> for the model. It provides an adapter <code>createXXX</code> method for each class of the model. <!-- end-user-doc -->
 * @see scenario.schedule.SchedulePackage
 * @generated
 */
public class ScheduleAdapterFactory extends AdapterFactoryImpl {
	/**
	 * The cached model package.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	protected static SchedulePackage modelPackage;

	/**
	 * Creates an instance of the adapter factory.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public ScheduleAdapterFactory() {
		if (modelPackage == null) {
			modelPackage = SchedulePackage.eINSTANCE;
		}
	}

	/**
	 * Returns whether this factory is applicable for the type of the object.
	 * <!-- begin-user-doc --> This implementation returns <code>true</code> if the object is either the model's package or is an
	 * instance object of the model. <!-- end-user-doc -->
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
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	protected ScheduleSwitch<Adapter> modelSwitch = new ScheduleSwitch<Adapter>() {
			@Override
			public Adapter caseScheduleModel(ScheduleModel object) {
				return createScheduleModelAdapter();
			}
			@Override
			public Adapter caseSchedule(Schedule object) {
				return createScheduleAdapter();
			}
			@Override
			public Adapter caseSequence(Sequence object) {
				return createSequenceAdapter();
			}
			@Override
			public Adapter caseCargoAllocation(CargoAllocation object) {
				return createCargoAllocationAdapter();
			}
			@Override
			public Adapter caseScheduleFitness(ScheduleFitness object) {
				return createScheduleFitnessAdapter();
			}
			@Override
			public Adapter caseBookedRevenue(BookedRevenue object) {
				return createBookedRevenueAdapter();
			}
			@Override
			public Adapter caseCargoRevenue(CargoRevenue object) {
				return createCargoRevenueAdapter();
			}
			@Override
			public Adapter caseVesselEventRevenue(VesselEventRevenue object) {
				return createVesselEventRevenueAdapter();
			}
			@Override
			public Adapter caseScenarioObject(ScenarioObject object) {
				return createScenarioObjectAdapter();
			}
			@Override
			public Adapter defaultCase(EObject object) {
				return createEObjectAdapter();
			}
		};

	/**
	 * Creates an adapter for the <code>target</code>.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @param target the object to adapt.
	 * @return the adapter for the <code>target</code>.
	 * @generated
	 */
	@Override
	public Adapter createAdapter(Notifier target) {
		return modelSwitch.doSwitch((EObject)target);
	}

	/**
	 * Creates a new adapter for an object of class '{@link scenario.schedule.ScheduleModel <em>Model</em>}'.
	 * <!-- begin-user-doc --> This default implementation returns null so that we can easily
	 * ignore cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see scenario.schedule.ScheduleModel
	 * @generated
	 */
	public Adapter createScheduleModelAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link scenario.schedule.Schedule <em>Schedule</em>}'.
	 * <!-- begin-user-doc --> This default implementation returns null so that we can easily
	 * ignore cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see scenario.schedule.Schedule
	 * @generated
	 */
	public Adapter createScheduleAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link scenario.schedule.Sequence <em>Sequence</em>}'.
	 * <!-- begin-user-doc --> This default implementation returns null so that we can easily
	 * ignore cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see scenario.schedule.Sequence
	 * @generated
	 */
	public Adapter createSequenceAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link scenario.schedule.CargoAllocation <em>Cargo Allocation</em>}'.
	 * <!-- begin-user-doc --> This default implementation returns null so that we
	 * can easily ignore cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see scenario.schedule.CargoAllocation
	 * @generated
	 */
	public Adapter createCargoAllocationAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link scenario.schedule.ScheduleFitness <em>Fitness</em>}'.
	 * <!-- begin-user-doc --> This default implementation returns null so that we can easily
	 * ignore cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see scenario.schedule.ScheduleFitness
	 * @generated
	 */
	public Adapter createScheduleFitnessAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link scenario.schedule.BookedRevenue <em>Booked Revenue</em>}'.
	 * <!-- begin-user-doc --> This default implementation returns null so that we can
	 * easily ignore cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see scenario.schedule.BookedRevenue
	 * @generated
	 */
	public Adapter createBookedRevenueAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link scenario.schedule.CargoRevenue <em>Cargo Revenue</em>}'.
	 * <!-- begin-user-doc --> This default implementation returns null so that we can
	 * easily ignore cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see scenario.schedule.CargoRevenue
	 * @generated
	 */
	public Adapter createCargoRevenueAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link scenario.schedule.VesselEventRevenue <em>Vessel Event Revenue</em>}'.
	 * <!-- begin-user-doc --> This default implementation returns null so
	 * that we can easily ignore cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see scenario.schedule.VesselEventRevenue
	 * @generated
	 */
	public Adapter createVesselEventRevenueAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link scenario.ScenarioObject <em>Object</em>}'.
	 * <!-- begin-user-doc --> This default implementation returns null so that we can easily ignore
	 * cases; it's useful to ignore a case when inheritance will catch all the cases anyway. <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see scenario.ScenarioObject
	 * @generated
	 */
	public Adapter createScenarioObjectAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for the default case.
	 * <!-- begin-user-doc --> This default implementation returns null. <!-- end-user-doc -->
	 * @return the new adapter.
	 * @generated
	 */
	public Adapter createEObjectAdapter() {
		return null;
	}

} // ScheduleAdapterFactory
