/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package scenario.schedule;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see scenario.schedule.SchedulePackage
 * @generated
 */
public interface ScheduleFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	ScheduleFactory eINSTANCE = scenario.schedule.impl.ScheduleFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>Model</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Model</em>'.
	 * @generated
	 */
	ScheduleModel createScheduleModel();

	/**
	 * Returns a new object of class '<em>Schedule</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Schedule</em>'.
	 * @generated
	 */
	Schedule createSchedule();

	/**
	 * Returns a new object of class '<em>Sequence</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Sequence</em>'.
	 * @generated
	 */
	Sequence createSequence();

	/**
	 * Returns a new object of class '<em>Cargo Allocation</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Cargo Allocation</em>'.
	 * @generated
	 */
	CargoAllocation createCargoAllocation();

	/**
	 * Returns a new object of class '<em>Fitness</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Fitness</em>'.
	 * @generated
	 */
	ScheduleFitness createScheduleFitness();

	/**
	 * Returns a new object of class '<em>Booked Revenue</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Booked Revenue</em>'.
	 * @generated
	 */
	BookedRevenue createBookedRevenue();

	/**
	 * Returns a new object of class '<em>Cargo Revenue</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Cargo Revenue</em>'.
	 * @generated
	 */
	CargoRevenue createCargoRevenue();

	/**
	 * Returns a new object of class '<em>Vessel Event Revenue</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Vessel Event Revenue</em>'.
	 * @generated
	 */
	VesselEventRevenue createVesselEventRevenue();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	SchedulePackage getSchedulePackage();

} //ScheduleFactory
