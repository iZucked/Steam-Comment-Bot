/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package scenario.schedule;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

import scenario.schedule.fleetallocation.AllocatedVessel;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Schedule</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link scenario.schedule.Schedule#getSequences <em>Sequences</em>}</li>
 *   <li>{@link scenario.schedule.Schedule#getName <em>Name</em>}</li>
 *   <li>{@link scenario.schedule.Schedule#getCargoAllocations <em>Cargo Allocations</em>}</li>
 *   <li>{@link scenario.schedule.Schedule#getFleet <em>Fleet</em>}</li>
 *   <li>{@link scenario.schedule.Schedule#getFitness <em>Fitness</em>}</li>
 *   <li>{@link scenario.schedule.Schedule#getRevenue <em>Revenue</em>}</li>
 * </ul>
 * </p>
 *
 * @see scenario.schedule.SchedulePackage#getSchedule()
 * @model
 * @generated
 */
public interface Schedule extends EObject {
	/**
	 * Returns the value of the '<em><b>Sequences</b></em>' containment reference list.
	 * The list contents are of type {@link scenario.schedule.Sequence}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Sequences</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Sequences</em>' containment reference list.
	 * @see scenario.schedule.SchedulePackage#getSchedule_Sequences()
	 * @model containment="true"
	 * @generated
	 */
	EList<Sequence> getSequences();

	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see scenario.schedule.SchedulePackage#getSchedule_Name()
	 * @model required="true"
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link scenario.schedule.Schedule#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Cargo Allocations</b></em>' containment reference list.
	 * The list contents are of type {@link scenario.schedule.CargoAllocation}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Cargo Allocations</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Cargo Allocations</em>' containment reference list.
	 * @see scenario.schedule.SchedulePackage#getSchedule_CargoAllocations()
	 * @model containment="true"
	 * @generated
	 */
	EList<CargoAllocation> getCargoAllocations();

	/**
	 * Returns the value of the '<em><b>Fleet</b></em>' containment reference list.
	 * The list contents are of type {@link scenario.schedule.fleetallocation.AllocatedVessel}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Fleet</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Fleet</em>' containment reference list.
	 * @see scenario.schedule.SchedulePackage#getSchedule_Fleet()
	 * @model containment="true"
	 * @generated
	 */
	EList<AllocatedVessel> getFleet();

	/**
	 * Returns the value of the '<em><b>Fitness</b></em>' containment reference list.
	 * The list contents are of type {@link scenario.schedule.ScheduleFitness}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Fitness</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Fitness</em>' containment reference list.
	 * @see scenario.schedule.SchedulePackage#getSchedule_Fitness()
	 * @model containment="true"
	 * @generated
	 */
	EList<ScheduleFitness> getFitness();

	/**
	 * Returns the value of the '<em><b>Revenue</b></em>' containment reference list.
	 * The list contents are of type {@link scenario.schedule.BookedRevenue}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Revenue</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Revenue</em>' containment reference list.
	 * @see scenario.schedule.SchedulePackage#getSchedule_Revenue()
	 * @model containment="true" required="true"
	 * @generated
	 */
	EList<BookedRevenue> getRevenue();

} // Schedule
