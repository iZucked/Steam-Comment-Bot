/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package scenario.fleet;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Model</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link scenario.fleet.FleetModel#getFleet <em>Fleet</em>}</li>
 *   <li>{@link scenario.fleet.FleetModel#getVesselClasses <em>Vessel Classes</em>}</li>
 *   <li>{@link scenario.fleet.FleetModel#getVesselEvents <em>Vessel Events</em>}</li>
 *   <li>{@link scenario.fleet.FleetModel#getFuels <em>Fuels</em>}</li>
 * </ul>
 * </p>
 *
 * @see scenario.fleet.FleetPackage#getFleetModel()
 * @model
 * @generated
 */
public interface FleetModel extends EObject {
	/**
	 * Returns the value of the '<em><b>Fleet</b></em>' containment reference list.
	 * The list contents are of type {@link scenario.fleet.Vessel}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Fleet</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Fleet</em>' containment reference list.
	 * @see scenario.fleet.FleetPackage#getFleetModel_Fleet()
	 * @model containment="true"
	 * @generated
	 */
	EList<Vessel> getFleet();

	/**
	 * Returns the value of the '<em><b>Vessel Classes</b></em>' containment reference list.
	 * The list contents are of type {@link scenario.fleet.VesselClass}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Vessel Classes</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Vessel Classes</em>' containment reference list.
	 * @see scenario.fleet.FleetPackage#getFleetModel_VesselClasses()
	 * @model containment="true"
	 * @generated
	 */
	EList<VesselClass> getVesselClasses();

	/**
	 * Returns the value of the '<em><b>Vessel Events</b></em>' containment reference list.
	 * The list contents are of type {@link scenario.fleet.VesselEvent}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Vessel Events</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Vessel Events</em>' containment reference list.
	 * @see scenario.fleet.FleetPackage#getFleetModel_VesselEvents()
	 * @model containment="true"
	 * @generated
	 */
	EList<VesselEvent> getVesselEvents();

	/**
	 * Returns the value of the '<em><b>Fuels</b></em>' containment reference list.
	 * The list contents are of type {@link scenario.fleet.VesselFuel}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Fuels</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Fuels</em>' containment reference list.
	 * @see scenario.fleet.FleetPackage#getFleetModel_Fuels()
	 * @model containment="true"
	 * @generated
	 */
	EList<VesselFuel> getFuels();

} // FleetModel
