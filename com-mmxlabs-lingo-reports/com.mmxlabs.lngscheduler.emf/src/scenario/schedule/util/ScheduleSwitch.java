/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package scenario.schedule.util;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.util.Switch;

import scenario.ScenarioObject;
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
 * <!-- begin-user-doc --> The <b>Switch</b> for the model's inheritance hierarchy. It supports the call {@link #doSwitch(EObject) doSwitch(object)} to invoke the <code>caseXXX</code> method for each
 * class of the model, starting with the actual class of the object and proceeding up the inheritance hierarchy until a non-null result is returned, which is the result of the switch. <!--
 * end-user-doc -->
 * 
 * @see scenario.schedule.SchedulePackage
 * @generated
 */
public class ScheduleSwitch<T> extends Switch<T> {
	/**
	 * The cached model package <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected static SchedulePackage modelPackage;

	/**
	 * Creates an instance of the switch. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public ScheduleSwitch() {
		if (modelPackage == null) {
			modelPackage = SchedulePackage.eINSTANCE;
		}
	}

	/**
	 * Checks whether this is a switch for the given package. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @parameter ePackage the package in question.
	 * @return whether this is a switch for the given package.
	 * @generated
	 */
	@Override
	protected boolean isSwitchFor(final EPackage ePackage) {
		return ePackage == modelPackage;
	}

	/**
	 * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return the first non-null result returned by a <code>caseXXX</code> call.
	 * @generated
	 */
	@Override
	protected T doSwitch(final int classifierID, final EObject theEObject) {
		switch (classifierID) {
		case SchedulePackage.SCHEDULE_MODEL: {
			final ScheduleModel scheduleModel = (ScheduleModel) theEObject;
			T result = caseScheduleModel(scheduleModel);
			if (result == null) {
				result = defaultCase(theEObject);
			}
			return result;
		}
		case SchedulePackage.SCHEDULE: {
			final Schedule schedule = (Schedule) theEObject;
			T result = caseSchedule(schedule);
			if (result == null) {
				result = defaultCase(theEObject);
			}
			return result;
		}
		case SchedulePackage.SEQUENCE: {
			final Sequence sequence = (Sequence) theEObject;
			T result = caseSequence(sequence);
			if (result == null) {
				result = defaultCase(theEObject);
			}
			return result;
		}
		case SchedulePackage.CARGO_ALLOCATION: {
			final CargoAllocation cargoAllocation = (CargoAllocation) theEObject;
			T result = caseCargoAllocation(cargoAllocation);
			if (result == null) {
				result = defaultCase(theEObject);
			}
			return result;
		}
		case SchedulePackage.SCHEDULE_FITNESS: {
			final ScheduleFitness scheduleFitness = (ScheduleFitness) theEObject;
			T result = caseScheduleFitness(scheduleFitness);
			if (result == null) {
				result = defaultCase(theEObject);
			}
			return result;
		}
		case SchedulePackage.BOOKED_REVENUE: {
			final BookedRevenue bookedRevenue = (BookedRevenue) theEObject;
			T result = caseBookedRevenue(bookedRevenue);
			if (result == null) {
				result = caseScenarioObject(bookedRevenue);
			}
			if (result == null) {
				result = defaultCase(theEObject);
			}
			return result;
		}
		case SchedulePackage.CARGO_REVENUE: {
			final CargoRevenue cargoRevenue = (CargoRevenue) theEObject;
			T result = caseCargoRevenue(cargoRevenue);
			if (result == null) {
				result = caseBookedRevenue(cargoRevenue);
			}
			if (result == null) {
				result = caseScenarioObject(cargoRevenue);
			}
			if (result == null) {
				result = defaultCase(theEObject);
			}
			return result;
		}
		case SchedulePackage.VESSEL_EVENT_REVENUE: {
			final VesselEventRevenue vesselEventRevenue = (VesselEventRevenue) theEObject;
			T result = caseVesselEventRevenue(vesselEventRevenue);
			if (result == null) {
				result = caseBookedRevenue(vesselEventRevenue);
			}
			if (result == null) {
				result = caseScenarioObject(vesselEventRevenue);
			}
			if (result == null) {
				result = defaultCase(theEObject);
			}
			return result;
		}
		default:
			return defaultCase(theEObject);
		}
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Model</em>'. <!-- begin-user-doc --> This implementation returns null; returning a non-null result will terminate the
	 * switch. <!-- end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Model</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseScheduleModel(final ScheduleModel object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Schedule</em>'. <!-- begin-user-doc --> This implementation returns null; returning a non-null result will terminate the
	 * switch. <!-- end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Schedule</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseSchedule(final Schedule object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Sequence</em>'. <!-- begin-user-doc --> This implementation returns null; returning a non-null result will terminate the
	 * switch. <!-- end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Sequence</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseSequence(final Sequence object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Cargo Allocation</em>'. <!-- begin-user-doc --> This implementation returns null; returning a non-null result will terminate
	 * the switch. <!-- end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Cargo Allocation</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseCargoAllocation(final CargoAllocation object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Fitness</em>'. <!-- begin-user-doc --> This implementation returns null; returning a non-null result will terminate the
	 * switch. <!-- end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Fitness</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseScheduleFitness(final ScheduleFitness object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Booked Revenue</em>'. <!-- begin-user-doc --> This implementation returns null; returning a non-null result will terminate
	 * the switch. <!-- end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Booked Revenue</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseBookedRevenue(final BookedRevenue object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Cargo Revenue</em>'. <!-- begin-user-doc --> This implementation returns null; returning a non-null result will terminate
	 * the switch. <!-- end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Cargo Revenue</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseCargoRevenue(final CargoRevenue object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Vessel Event Revenue</em>'. <!-- begin-user-doc --> This implementation returns null; returning a non-null result will
	 * terminate the switch. <!-- end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Vessel Event Revenue</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseVesselEventRevenue(final VesselEventRevenue object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Object</em>'. <!-- begin-user-doc --> This implementation returns null; returning a non-null result will terminate the
	 * switch. <!-- end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Object</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseScenarioObject(final ScenarioObject object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>EObject</em>'. <!-- begin-user-doc --> This implementation returns null; returning a non-null result will terminate the
	 * switch, but this is the last case anyway. <!-- end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>EObject</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject)
	 * @generated
	 */
	@Override
	public T defaultCase(final EObject object) {
		return null;
	}

} // ScheduleSwitch
