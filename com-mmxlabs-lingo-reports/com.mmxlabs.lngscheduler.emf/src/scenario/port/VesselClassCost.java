/**
 * Copyright (C) Minimax Labs Ltd., 2010
 * All rights reserved.
 */

package scenario.port;

import org.eclipse.emf.ecore.EObject;

import scenario.fleet.VesselClass;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Vessel Class Cost</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Describes the various costs and delays associated with a given vessel class for a given canal.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link scenario.port.VesselClassCost#getVesselClass <em>Vessel Class</em>}</li>
 *   <li>{@link scenario.port.VesselClassCost#getLadenCost <em>Laden Cost</em>}</li>
 *   <li>{@link scenario.port.VesselClassCost#getUnladenCost <em>Unladen Cost</em>}</li>
 *   <li>{@link scenario.port.VesselClassCost#getTransitTime <em>Transit Time</em>}</li>
 *   <li>{@link scenario.port.VesselClassCost#getTransitFuel <em>Transit Fuel</em>}</li>
 * </ul>
 * </p>
 *
 * @see scenario.port.PortPackage#getVesselClassCost()
 * @model
 * @generated
 */
public interface VesselClassCost extends EObject {
	/**
	 * Returns the value of the '<em><b>Vessel Class</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Vessel Class</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The vessel class to which these costs apply, for this canal
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Vessel Class</em>' reference.
	 * @see #setVesselClass(VesselClass)
	 * @see scenario.port.PortPackage#getVesselClassCost_VesselClass()
	 * @model required="true"
	 * @generated
	 */
	VesselClass getVesselClass();

	/**
	 * Sets the value of the '{@link scenario.port.VesselClassCost#getVesselClass <em>Vessel Class</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Vessel Class</em>' reference.
	 * @see #getVesselClass()
	 * @generated
	 */
	void setVesselClass(VesselClass value);

	/**
	 * Returns the value of the '<em><b>Laden Cost</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Laden Cost</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The toll in dollars paid for letting a laden vessel of this class pass through this canal
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Laden Cost</em>' attribute.
	 * @see #setLadenCost(int)
	 * @see scenario.port.PortPackage#getVesselClassCost_LadenCost()
	 * @model
	 * @generated
	 */
	int getLadenCost();

	/**
	 * Sets the value of the '{@link scenario.port.VesselClassCost#getLadenCost <em>Laden Cost</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Laden Cost</em>' attribute.
	 * @see #getLadenCost()
	 * @generated
	 */
	void setLadenCost(int value);

	/**
	 * Returns the value of the '<em><b>Unladen Cost</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Unladen Cost</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The toll in dollars for an unladen vessel of this class to pass through this canal.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Unladen Cost</em>' attribute.
	 * @see #setUnladenCost(int)
	 * @see scenario.port.PortPackage#getVesselClassCost_UnladenCost()
	 * @model
	 * @generated
	 */
	int getUnladenCost();

	/**
	 * Sets the value of the '{@link scenario.port.VesselClassCost#getUnladenCost <em>Unladen Cost</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Unladen Cost</em>' attribute.
	 * @see #getUnladenCost()
	 * @generated
	 */
	void setUnladenCost(int value);

	/**
	 * Returns the value of the '<em><b>Transit Time</b></em>' attribute.
	 * The default value is <code>"24"</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The time in hours required for vessels of this class to pass through this canal
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Transit Time</em>' attribute.
	 * @see #setTransitTime(int)
	 * @see scenario.port.PortPackage#getVesselClassCost_TransitTime()
	 * @model default="24" required="true"
	 * @generated
	 */
	int getTransitTime();

	/**
	 * Sets the value of the '{@link scenario.port.VesselClassCost#getTransitTime <em>Transit Time</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Transit Time</em>' attribute.
	 * @see #getTransitTime()
	 * @generated
	 */
	void setTransitTime(int value);

	/**
	 * Returns the value of the '<em><b>Transit Fuel</b></em>' attribute.
	 * The default value is <code>"50"</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The amount of base fuel or base fuel equivalent, in metric tonnes per day (MT/day), consumed by vessels of this class during their passage through the canal. The total fuel consumption for a journey through the canal is then given by transitTime * (transitFuel / 24).
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Transit Fuel</em>' attribute.
	 * @see #setTransitFuel(float)
	 * @see scenario.port.PortPackage#getVesselClassCost_TransitFuel()
	 * @model default="50" required="true"
	 * @generated
	 */
	float getTransitFuel();

	/**
	 * Sets the value of the '{@link scenario.port.VesselClassCost#getTransitFuel <em>Transit Fuel</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Transit Fuel</em>' attribute.
	 * @see #getTransitFuel()
	 * @generated
	 */
	void setTransitFuel(float value);

} // VesselClassCost
