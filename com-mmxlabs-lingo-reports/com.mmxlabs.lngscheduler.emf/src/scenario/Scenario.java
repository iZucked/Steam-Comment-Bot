/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package scenario;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

import scenario.cargo.CargoModel;
import scenario.contract.ContractModel;
import scenario.fleet.FleetModel;
import scenario.market.MarketModel;
import scenario.optimiser.Optimisation;
import scenario.port.CanalModel;
import scenario.port.DistanceModel;
import scenario.port.PortModel;
import scenario.schedule.ScheduleModel;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Scenario</b></em>'. <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link scenario.Scenario#getVersion <em>Version</em>}</li>
 *   <li>{@link scenario.Scenario#getName <em>Name</em>}</li>
 *   <li>{@link scenario.Scenario#getFleetModel <em>Fleet Model</em>}</li>
 *   <li>{@link scenario.Scenario#getScheduleModel <em>Schedule Model</em>}</li>
 *   <li>{@link scenario.Scenario#getPortModel <em>Port Model</em>}</li>
 *   <li>{@link scenario.Scenario#getDistanceModel <em>Distance Model</em>}</li>
 *   <li>{@link scenario.Scenario#getCanalModel <em>Canal Model</em>}</li>
 *   <li>{@link scenario.Scenario#getCargoModel <em>Cargo Model</em>}</li>
 *   <li>{@link scenario.Scenario#getContractModel <em>Contract Model</em>}</li>
 *   <li>{@link scenario.Scenario#getMarketModel <em>Market Model</em>}</li>
 *   <li>{@link scenario.Scenario#getOptimisation <em>Optimisation</em>}</li>
 *   <li>{@link scenario.Scenario#getContainedModels <em>Contained Models</em>}</li>
 * </ul>
 * </p>
 *
 * @see scenario.ScenarioPackage#getScenario()
 * @model
 * @generated
 */
public interface Scenario extends AnnotatedObject {
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
	 * @model containment="true" resolveProxies="true"
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
	 * Returns the value of the '<em><b>Port Model</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Port Model</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Port Model</em>' reference.
	 * @see #setPortModel(PortModel)
	 * @see scenario.ScenarioPackage#getScenario_PortModel()
	 * @model
	 * @generated
	 */
	PortModel getPortModel();

	/**
	 * Sets the value of the '{@link scenario.Scenario#getPortModel <em>Port Model</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Port Model</em>' reference.
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
	 * @model containment="true" resolveProxies="true"
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
	 * @model containment="true" resolveProxies="true"
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
	 * @model containment="true" resolveProxies="true"
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
	 * @model containment="true" resolveProxies="true"
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
	 * Returns the value of the '<em><b>Distance Model</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Distance Model</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Distance Model</em>' reference.
	 * @see #setDistanceModel(DistanceModel)
	 * @see scenario.ScenarioPackage#getScenario_DistanceModel()
	 * @model
	 * @generated
	 */
	DistanceModel getDistanceModel();

	/**
	 * Sets the value of the '{@link scenario.Scenario#getDistanceModel <em>Distance Model</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Distance Model</em>' reference.
	 * @see #getDistanceModel()
	 * @generated
	 */
	void setDistanceModel(DistanceModel value);

	/**
	 * Returns the value of the '<em><b>Canal Model</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Canal Model</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Canal Model</em>' reference.
	 * @see #setCanalModel(CanalModel)
	 * @see scenario.ScenarioPackage#getScenario_CanalModel()
	 * @model
	 * @generated
	 */
	CanalModel getCanalModel();

	/**
	 * Sets the value of the '{@link scenario.Scenario#getCanalModel <em>Canal Model</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Canal Model</em>' reference.
	 * @see #getCanalModel()
	 * @generated
	 */
	void setCanalModel(CanalModel value);

	/**
	 * Returns the value of the '<em><b>Optimisation</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Optimisation</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Optimisation</em>' containment reference.
	 * @see #setOptimisation(Optimisation)
	 * @see scenario.ScenarioPackage#getScenario_Optimisation()
	 * @model containment="true" resolveProxies="true"
	 * @generated
	 */
	Optimisation getOptimisation();

	/**
	 * Sets the value of the '{@link scenario.Scenario#getOptimisation <em>Optimisation</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Optimisation</em>' containment reference.
	 * @see #getOptimisation()
	 * @generated
	 */
	void setOptimisation(Optimisation value);

	/**
	 * Returns the value of the '<em><b>Contained Models</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.emf.ecore.EObject}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Contained Models</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Contained Models</em>' containment reference list.
	 * @see scenario.ScenarioPackage#getScenario_ContainedModels()
	 * @model containment="true" resolveProxies="true"
	 * @generated
	 */
	EList<EObject> getContainedModels();

	/**
	 * Returns the value of the '<em><b>Version</b></em>' attribute.
	 * The default value is <code>"2"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Version</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Version</em>' attribute.
	 * @see #setVersion(int)
	 * @see scenario.ScenarioPackage#getScenario_Version()
	 * @model default="2"
	 * @generated
	 */
	int getVersion();

	/**
	 * Sets the value of the '{@link scenario.Scenario#getVersion <em>Version</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Version</em>' attribute.
	 * @see #getVersion()
	 * @generated
	 */
	void setVersion(int value);

	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * The default value is <code>"Default name"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see scenario.ScenarioPackage#getScenario_Name()
	 * @model default="Default name" required="true"
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link scenario.Scenario#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation" required="true"
	 *        annotation="http://www.eclipse.org/emf/2002/GenModel body='if (getFleetModel() == null)\n\t\t\tsetFleetModel(scenario.fleet.FleetFactory.eINSTANCE.createFleetModel());\n\t\t\n\t\treturn getFleetModel();'"
	 * @generated
	 */
	FleetModel getOrCreateFleetModel();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation" required="true"
	 *        annotation="http://www.eclipse.org/emf/2002/GenModel body='if (getScheduleModel() == null)\n\t\t\tsetScheduleModel(scenario.schedule.ScheduleFactory.eINSTANCE.createScheduleModel());\n\t\treturn getScheduleModel();'"
	 * @generated
	 */
	ScheduleModel getOrCreateScheduleModel();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model annotation="http://www.eclipse.org/emf/2002/GenModel body='if (getPortModel() == null) {\r\n\tsetPortModel(scenario.port.PortFactory.eINSTANCE.createPortModel());\r\n\tgetContainedModels().add(getPortModel());\r\n}\r\n\r\nif (getDistanceModel() == null) {\r\n\tsetDistanceModel(scenario.port.PortFactory.eINSTANCE.createDistanceModel());\r\n\tgetContainedModels().add(getDistanceModel());\r\n}\r\n\r\nif (getCanalModel() == null) {\r\n\tsetCanalModel(scenario.port.PortFactory.eINSTANCE.createCanalModel());\r\n\tgetContainedModels().add(getCanalModel());\r\n}\r\n\r\nif (getFleetModel() == null)\r\n\tsetFleetModel(scenario.fleet.FleetFactory.eINSTANCE.createFleetModel());\r\n\r\nif (getCargoModel() == null)\r\n\tsetCargoModel(scenario.cargo.CargoFactory.eINSTANCE.createCargoModel());\r\n\r\nif (getContractModel() == null)\r\n\tsetContractModel(scenario.contract.ContractFactory.eINSTANCE.createContractModel());\r\n\r\nif (getScheduleModel() == null)\r\n\tsetScheduleModel(scenario.schedule.ScheduleFactory.eINSTANCE.createScheduleModel());\r\n\r\nif (getMarketModel() == null)\r\n\tsetMarketModel(scenario.market.MarketFactory.eINSTANCE.createMarketModel());\r\n\r\nif (getOptimisation() == null)\r\n\t\t\tsetOptimisation(scenario.optimiser.OptimiserFactory.eINSTANCE.createOptimisation());'"
	 * @generated
	 */
	void createMissingModels();

	/**
	 * @return
	 */
	Scenario getSelfContainedCopy();

} // Scenario
