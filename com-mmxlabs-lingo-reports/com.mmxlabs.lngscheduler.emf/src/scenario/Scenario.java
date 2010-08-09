/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package scenario;

import org.eclipse.emf.ecore.EObject;

import scenario.cargo.CargoModel;
import scenario.contract.ContractModel;
import scenario.fleet.FleetModel;
import scenario.market.MarketModel;
import scenario.port.DistanceModel;
import scenario.port.PortModel;
import scenario.schedule.ScheduleModel;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Scenario</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link scenario.Scenario#getFleetModel <em>Fleet Model</em>}</li>
 *   <li>{@link scenario.Scenario#getPortModel <em>Port Model</em>}</li>
 *   <li>{@link scenario.Scenario#getCargoModel <em>Cargo Model</em>}</li>
 *   <li>{@link scenario.Scenario#getContractModel <em>Contract Model</em>}</li>
 *   <li>{@link scenario.Scenario#getScheduleModel <em>Schedule Model</em>}</li>
 *   <li>{@link scenario.Scenario#getMarketModel <em>Market Model</em>}</li>
 *   <li>{@link scenario.Scenario#getDistanceModel <em>Distance Model</em>}</li>
 * </ul>
 * </p>
 *
 * @see scenario.ScenarioPackage#getScenario()
 * @model
 * @generated
 */
public interface Scenario extends EObject {
	/**
	 * Returns the value of the '<em><b>Fleet Model</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Fleet Model</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Fleet Model</em>' containment reference.
	 * @see #setFleetModel(FleetModel)
	 * @see scenario.ScenarioPackage#getScenario_FleetModel()
	 * @model containment="true"
	 * @generated
	 */
	FleetModel getFleetModel();

	/**
	 * Sets the value of the '{@link scenario.Scenario#getFleetModel <em>Fleet Model</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Fleet Model</em>' containment reference.
	 * @see #getFleetModel()
	 * @generated
	 */
	void setFleetModel(FleetModel value);

	/**
	 * Returns the value of the '<em><b>Port Model</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Port Model</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Port Model</em>' containment reference.
	 * @see #setPortModel(PortModel)
	 * @see scenario.ScenarioPackage#getScenario_PortModel()
	 * @model containment="true"
	 * @generated
	 */
	PortModel getPortModel();

	/**
	 * Sets the value of the '{@link scenario.Scenario#getPortModel <em>Port Model</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Port Model</em>' containment reference.
	 * @see #getPortModel()
	 * @generated
	 */
	void setPortModel(PortModel value);

	/**
	 * Returns the value of the '<em><b>Cargo Model</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Cargo Model</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Cargo Model</em>' containment reference.
	 * @see #setCargoModel(CargoModel)
	 * @see scenario.ScenarioPackage#getScenario_CargoModel()
	 * @model containment="true"
	 * @generated
	 */
	CargoModel getCargoModel();

	/**
	 * Sets the value of the '{@link scenario.Scenario#getCargoModel <em>Cargo Model</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Cargo Model</em>' containment reference.
	 * @see #getCargoModel()
	 * @generated
	 */
	void setCargoModel(CargoModel value);

	/**
	 * Returns the value of the '<em><b>Contract Model</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Contract Model</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Contract Model</em>' containment reference.
	 * @see #setContractModel(ContractModel)
	 * @see scenario.ScenarioPackage#getScenario_ContractModel()
	 * @model containment="true"
	 * @generated
	 */
	ContractModel getContractModel();

	/**
	 * Sets the value of the '{@link scenario.Scenario#getContractModel <em>Contract Model</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Contract Model</em>' containment reference.
	 * @see #getContractModel()
	 * @generated
	 */
	void setContractModel(ContractModel value);

	/**
	 * Returns the value of the '<em><b>Schedule Model</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Schedule Model</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Schedule Model</em>' containment reference.
	 * @see #setScheduleModel(ScheduleModel)
	 * @see scenario.ScenarioPackage#getScenario_ScheduleModel()
	 * @model containment="true"
	 * @generated
	 */
	ScheduleModel getScheduleModel();

	/**
	 * Sets the value of the '{@link scenario.Scenario#getScheduleModel <em>Schedule Model</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Schedule Model</em>' containment reference.
	 * @see #getScheduleModel()
	 * @generated
	 */
	void setScheduleModel(ScheduleModel value);

	/**
	 * Returns the value of the '<em><b>Market Model</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Market Model</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Market Model</em>' containment reference.
	 * @see #setMarketModel(MarketModel)
	 * @see scenario.ScenarioPackage#getScenario_MarketModel()
	 * @model containment="true"
	 * @generated
	 */
	MarketModel getMarketModel();

	/**
	 * Sets the value of the '{@link scenario.Scenario#getMarketModel <em>Market Model</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Market Model</em>' containment reference.
	 * @see #getMarketModel()
	 * @generated
	 */
	void setMarketModel(MarketModel value);

	/**
	 * Returns the value of the '<em><b>Distance Model</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Distance Model</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Distance Model</em>' containment reference.
	 * @see #setDistanceModel(DistanceModel)
	 * @see scenario.ScenarioPackage#getScenario_DistanceModel()
	 * @model containment="true"
	 * @generated
	 */
	DistanceModel getDistanceModel();

	/**
	 * Sets the value of the '{@link scenario.Scenario#getDistanceModel <em>Distance Model</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Distance Model</em>' containment reference.
	 * @see #getDistanceModel()
	 * @generated
	 */
	void setDistanceModel(DistanceModel value);

} // Scenario
