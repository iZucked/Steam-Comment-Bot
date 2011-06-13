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
 * A representation of the model object '<em><b>Vessel State Attributes</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link scenario.fleet.VesselStateAttributes#getVesselState <em>Vessel State</em>}</li>
 *   <li>{@link scenario.fleet.VesselStateAttributes#getNboRate <em>Nbo Rate</em>}</li>
 *   <li>{@link scenario.fleet.VesselStateAttributes#getIdleNBORate <em>Idle NBO Rate</em>}</li>
 *   <li>{@link scenario.fleet.VesselStateAttributes#getIdleConsumptionRate <em>Idle Consumption Rate</em>}</li>
 *   <li>{@link scenario.fleet.VesselStateAttributes#getFuelConsumptionCurve <em>Fuel Consumption Curve</em>}</li>
 * </ul>
 * </p>
 *
 * @see scenario.fleet.FleetPackage#getVesselStateAttributes()
 * @model
 * @generated
 */
public interface VesselStateAttributes extends EObject {
	/**
	 * Returns the value of the '<em><b>Vessel State</b></em>' attribute.
	 * The literals are from the enumeration {@link scenario.fleet.VesselState}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Vessel State</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Vessel State</em>' attribute.
	 * @see scenario.fleet.VesselState
	 * @see #setVesselState(VesselState)
	 * @see scenario.fleet.FleetPackage#getVesselStateAttributes_VesselState()
	 * @model
	 * @generated
	 */
	VesselState getVesselState();

	/**
	 * Sets the value of the '{@link scenario.fleet.VesselStateAttributes#getVesselState <em>Vessel State</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Vessel State</em>' attribute.
	 * @see scenario.fleet.VesselState
	 * @see #getVesselState()
	 * @generated
	 */
	void setVesselState(VesselState value);

	/**
	 * Returns the value of the '<em><b>Nbo Rate</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Nbo Rate</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Nbo Rate</em>' attribute.
	 * @see #setNboRate(float)
	 * @see scenario.fleet.FleetPackage#getVesselStateAttributes_NboRate()
	 * @model
	 * @generated
	 */
	float getNboRate();

	/**
	 * Sets the value of the '{@link scenario.fleet.VesselStateAttributes#getNboRate <em>Nbo Rate</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Nbo Rate</em>' attribute.
	 * @see #getNboRate()
	 * @generated
	 */
	void setNboRate(float value);

	/**
	 * Returns the value of the '<em><b>Idle NBO Rate</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Idle NBO Rate</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Idle NBO Rate</em>' attribute.
	 * @see #setIdleNBORate(float)
	 * @see scenario.fleet.FleetPackage#getVesselStateAttributes_IdleNBORate()
	 * @model
	 * @generated
	 */
	float getIdleNBORate();

	/**
	 * Sets the value of the '{@link scenario.fleet.VesselStateAttributes#getIdleNBORate <em>Idle NBO Rate</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Idle NBO Rate</em>' attribute.
	 * @see #getIdleNBORate()
	 * @generated
	 */
	void setIdleNBORate(float value);

	/**
	 * Returns the value of the '<em><b>Idle Consumption Rate</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Idle Consumption Rate</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Idle Consumption Rate</em>' attribute.
	 * @see #setIdleConsumptionRate(float)
	 * @see scenario.fleet.FleetPackage#getVesselStateAttributes_IdleConsumptionRate()
	 * @model
	 * @generated
	 */
	float getIdleConsumptionRate();

	/**
	 * Sets the value of the '{@link scenario.fleet.VesselStateAttributes#getIdleConsumptionRate <em>Idle Consumption Rate</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Idle Consumption Rate</em>' attribute.
	 * @see #getIdleConsumptionRate()
	 * @generated
	 */
	void setIdleConsumptionRate(float value);

	/**
	 * Returns the value of the '<em><b>Fuel Consumption Curve</b></em>' containment reference list.
	 * The list contents are of type {@link scenario.fleet.FuelConsumptionLine}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Fuel Consumption Curve</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Fuel Consumption Curve</em>' containment reference list.
	 * @see scenario.fleet.FleetPackage#getVesselStateAttributes_FuelConsumptionCurve()
	 * @model containment="true"
	 * @generated
	 */
	EList<FuelConsumptionLine> getFuelConsumptionCurve();

} // VesselStateAttributes
