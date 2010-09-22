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
 *   <li>{@link scenario.fleet.FleetModel#getCharterOuts <em>Charter Outs</em>}</li>
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
	 * If the meaning of the '<em>Vessel Classes</em>' containment reference isn't clear,
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
	 * Returns the value of the '<em><b>Charter Outs</b></em>' containment reference list.
	 * The list contents are of type {@link scenario.fleet.CharterOut}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Charter Outs</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Charter Outs</em>' containment reference list.
	 * @see scenario.fleet.FleetPackage#getFleetModel_CharterOuts()
	 * @model containment="true"
	 * @generated
	 */
	EList<CharterOut> getCharterOuts();

} // FleetModel
