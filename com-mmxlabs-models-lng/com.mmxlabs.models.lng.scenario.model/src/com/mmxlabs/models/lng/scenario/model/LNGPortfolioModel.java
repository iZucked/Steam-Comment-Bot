/**
 */
package com.mmxlabs.models.lng.scenario.model;

import com.mmxlabs.models.lng.assignment.AssignmentModel;

import com.mmxlabs.models.lng.cargo.CargoModel;

import com.mmxlabs.models.lng.fleet.ScenarioFleetModel;

import com.mmxlabs.models.lng.schedule.ScheduleModel;

import com.mmxlabs.models.mmxcore.UUIDObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>LNG Portfolio Model</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.scenario.model.LNGPortfolioModel#getScenarioFleetModel <em>Scenario Fleet Model</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.scenario.model.LNGPortfolioModel#getCargoModel <em>Cargo Model</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.scenario.model.LNGPortfolioModel#getAssignmentModel <em>Assignment Model</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.scenario.model.LNGPortfolioModel#getScheduleModel <em>Schedule Model</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.mmxlabs.models.lng.scenario.model.LNGScenarioPackage#getLNGPortfolioModel()
 * @model
 * @generated
 */
public interface LNGPortfolioModel extends UUIDObject {
	/**
	 * Returns the value of the '<em><b>Scenario Fleet Model</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Scenario Fleet Model</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Scenario Fleet Model</em>' containment reference.
	 * @see #setScenarioFleetModel(ScenarioFleetModel)
	 * @see com.mmxlabs.models.lng.scenario.model.LNGScenarioPackage#getLNGPortfolioModel_ScenarioFleetModel()
	 * @model containment="true" resolveProxies="true"
	 * @generated
	 */
	ScenarioFleetModel getScenarioFleetModel();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.scenario.model.LNGPortfolioModel#getScenarioFleetModel <em>Scenario Fleet Model</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Scenario Fleet Model</em>' containment reference.
	 * @see #getScenarioFleetModel()
	 * @generated
	 */
	void setScenarioFleetModel(ScenarioFleetModel value);

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
	 * @see com.mmxlabs.models.lng.scenario.model.LNGScenarioPackage#getLNGPortfolioModel_CargoModel()
	 * @model containment="true" resolveProxies="true"
	 * @generated
	 */
	CargoModel getCargoModel();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.scenario.model.LNGPortfolioModel#getCargoModel <em>Cargo Model</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Cargo Model</em>' containment reference.
	 * @see #getCargoModel()
	 * @generated
	 */
	void setCargoModel(CargoModel value);

	/**
	 * Returns the value of the '<em><b>Assignment Model</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Assignment Model</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Assignment Model</em>' containment reference.
	 * @see #setAssignmentModel(AssignmentModel)
	 * @see com.mmxlabs.models.lng.scenario.model.LNGScenarioPackage#getLNGPortfolioModel_AssignmentModel()
	 * @model containment="true" resolveProxies="true"
	 * @generated
	 */
	AssignmentModel getAssignmentModel();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.scenario.model.LNGPortfolioModel#getAssignmentModel <em>Assignment Model</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Assignment Model</em>' containment reference.
	 * @see #getAssignmentModel()
	 * @generated
	 */
	void setAssignmentModel(AssignmentModel value);

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
	 * @see com.mmxlabs.models.lng.scenario.model.LNGScenarioPackage#getLNGPortfolioModel_ScheduleModel()
	 * @model containment="true" resolveProxies="true"
	 * @generated
	 */
	ScheduleModel getScheduleModel();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.scenario.model.LNGPortfolioModel#getScheduleModel <em>Schedule Model</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Schedule Model</em>' containment reference.
	 * @see #getScheduleModel()
	 * @generated
	 */
	void setScheduleModel(ScheduleModel value);

} // LNGPortfolioModel
