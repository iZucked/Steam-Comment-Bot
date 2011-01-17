/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package scenario.port;

import org.eclipse.emf.ecore.EObject;

import scenario.fleet.VesselClass;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Vessel Class Cost</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link scenario.port.PortVesselClassCost#getName <em>Name</em>}</li>
 *   <li>{@link scenario.port.PortVesselClassCost#getPortCostProfile <em>Port Cost Profile</em>}</li>
 *   <li>{@link scenario.port.PortVesselClassCost#getVesselClass <em>Vessel Class</em>}</li>
 *   <li>{@link scenario.port.PortVesselClassCost#getFixedCost <em>Fixed Cost</em>}</li>
 *   <li>{@link scenario.port.PortVesselClassCost#getHourlyCost <em>Hourly Cost</em>}</li>
 * </ul>
 * </p>
 *
 * @see scenario.port.PortPackage#getPortVesselClassCost()
 * @model
 * @generated
 */
public interface PortVesselClassCost extends EObject {
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
	 * @see scenario.port.PortPackage#getPortVesselClassCost_Name()
	 * @model
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link scenario.port.PortVesselClassCost#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Port Cost Profile</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Port Cost Profile</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Port Cost Profile</em>' reference.
	 * @see #setPortCostProfile(PortCostProfile)
	 * @see scenario.port.PortPackage#getPortVesselClassCost_PortCostProfile()
	 * @model required="true"
	 * @generated
	 */
	PortCostProfile getPortCostProfile();

	/**
	 * Sets the value of the '{@link scenario.port.PortVesselClassCost#getPortCostProfile <em>Port Cost Profile</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Port Cost Profile</em>' reference.
	 * @see #getPortCostProfile()
	 * @generated
	 */
	void setPortCostProfile(PortCostProfile value);

	/**
	 * Returns the value of the '<em><b>Vessel Class</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Vessel Class</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Vessel Class</em>' reference.
	 * @see #setVesselClass(VesselClass)
	 * @see scenario.port.PortPackage#getPortVesselClassCost_VesselClass()
	 * @model required="true"
	 * @generated
	 */
	VesselClass getVesselClass();

	/**
	 * Sets the value of the '{@link scenario.port.PortVesselClassCost#getVesselClass <em>Vessel Class</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Vessel Class</em>' reference.
	 * @see #getVesselClass()
	 * @generated
	 */
	void setVesselClass(VesselClass value);

	/**
	 * Returns the value of the '<em><b>Fixed Cost</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Fixed Cost</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Fixed Cost</em>' attribute.
	 * @see #setFixedCost(int)
	 * @see scenario.port.PortPackage#getPortVesselClassCost_FixedCost()
	 * @model
	 * @generated
	 */
	int getFixedCost();

	/**
	 * Sets the value of the '{@link scenario.port.PortVesselClassCost#getFixedCost <em>Fixed Cost</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Fixed Cost</em>' attribute.
	 * @see #getFixedCost()
	 * @generated
	 */
	void setFixedCost(int value);

	/**
	 * Returns the value of the '<em><b>Hourly Cost</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Hourly Cost</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Hourly Cost</em>' attribute.
	 * @see #setHourlyCost(int)
	 * @see scenario.port.PortPackage#getPortVesselClassCost_HourlyCost()
	 * @model
	 * @generated
	 */
	int getHourlyCost();

	/**
	 * Sets the value of the '{@link scenario.port.PortVesselClassCost#getHourlyCost <em>Hourly Cost</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Hourly Cost</em>' attribute.
	 * @see #getHourlyCost()
	 * @generated
	 */
	void setHourlyCost(int value);

} // PortVesselClassCost
