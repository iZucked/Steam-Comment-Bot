/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.scenario.model;

import java.time.LocalDate;

import com.mmxlabs.models.lng.actuals.ActualsModel;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.parameters.OptimiserSettings;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.mmxcore.MMXRootObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Model</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.scenario.model.LNGScenarioModel#getCargoModel <em>Cargo Model</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.scenario.model.LNGScenarioModel#getScheduleModel <em>Schedule Model</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.scenario.model.LNGScenarioModel#getParameters <em>Parameters</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.scenario.model.LNGScenarioModel#getActualsModel <em>Actuals Model</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.scenario.model.LNGScenarioModel#getPromptPeriodStart <em>Prompt Period Start</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.scenario.model.LNGScenarioModel#getPromptPeriodEnd <em>Prompt Period End</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.scenario.model.LNGScenarioModel#getReferenceModel <em>Reference Model</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.scenario.model.LNGScenarioModel#getUserSettings <em>User Settings</em>}</li>
 * </ul>
 *
 * @see com.mmxlabs.models.lng.scenario.model.LNGScenarioPackage#getLNGScenarioModel()
 * @model
 * @generated
 */
public interface LNGScenarioModel extends MMXRootObject {
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
	 * @see com.mmxlabs.models.lng.scenario.model.LNGScenarioPackage#getLNGScenarioModel_CargoModel()
	 * @model containment="true" resolveProxies="true"
	 * @generated
	 */
	CargoModel getCargoModel();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.scenario.model.LNGScenarioModel#getCargoModel <em>Cargo Model</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Cargo Model</em>' containment reference.
	 * @see #getCargoModel()
	 * @generated
	 */
	void setCargoModel(CargoModel value);

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
	 * @see com.mmxlabs.models.lng.scenario.model.LNGScenarioPackage#getLNGScenarioModel_ScheduleModel()
	 * @model containment="true" resolveProxies="true"
	 * @generated
	 */
	ScheduleModel getScheduleModel();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.scenario.model.LNGScenarioModel#getScheduleModel <em>Schedule Model</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Schedule Model</em>' containment reference.
	 * @see #getScheduleModel()
	 * @generated
	 */
	void setScheduleModel(ScheduleModel value);

	/**
	 * Returns the value of the '<em><b>Parameters</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Parameters</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Parameters</em>' containment reference.
	 * @see #setParameters(OptimiserSettings)
	 * @see com.mmxlabs.models.lng.scenario.model.LNGScenarioPackage#getLNGScenarioModel_Parameters()
	 * @model containment="true" resolveProxies="true"
	 * @generated
	 */
	OptimiserSettings getParameters();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.scenario.model.LNGScenarioModel#getParameters <em>Parameters</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Parameters</em>' containment reference.
	 * @see #getParameters()
	 * @generated
	 */
	void setParameters(OptimiserSettings value);

	/**
	 * Returns the value of the '<em><b>Actuals Model</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Actuals Model</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Actuals Model</em>' containment reference.
	 * @see #setActualsModel(ActualsModel)
	 * @see com.mmxlabs.models.lng.scenario.model.LNGScenarioPackage#getLNGScenarioModel_ActualsModel()
	 * @model containment="true" resolveProxies="true"
	 * @generated
	 */
	ActualsModel getActualsModel();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.scenario.model.LNGScenarioModel#getActualsModel <em>Actuals Model</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Actuals Model</em>' containment reference.
	 * @see #getActualsModel()
	 * @generated
	 */
	void setActualsModel(ActualsModel value);

	/**
	 * Returns the value of the '<em><b>Prompt Period Start</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Prompt Period Start</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Prompt Period Start</em>' attribute.
	 * @see #isSetPromptPeriodStart()
	 * @see #unsetPromptPeriodStart()
	 * @see #setPromptPeriodStart(LocalDate)
	 * @see com.mmxlabs.models.lng.scenario.model.LNGScenarioPackage#getLNGScenarioModel_PromptPeriodStart()
	 * @model unsettable="true" dataType="com.mmxlabs.models.datetime.LocalDate"
	 * @generated
	 */
	LocalDate getPromptPeriodStart();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.scenario.model.LNGScenarioModel#getPromptPeriodStart <em>Prompt Period Start</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Prompt Period Start</em>' attribute.
	 * @see #isSetPromptPeriodStart()
	 * @see #unsetPromptPeriodStart()
	 * @see #getPromptPeriodStart()
	 * @generated
	 */
	void setPromptPeriodStart(LocalDate value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.scenario.model.LNGScenarioModel#getPromptPeriodStart <em>Prompt Period Start</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetPromptPeriodStart()
	 * @see #getPromptPeriodStart()
	 * @see #setPromptPeriodStart(LocalDate)
	 * @generated
	 */
	void unsetPromptPeriodStart();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.scenario.model.LNGScenarioModel#getPromptPeriodStart <em>Prompt Period Start</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Prompt Period Start</em>' attribute is set.
	 * @see #unsetPromptPeriodStart()
	 * @see #getPromptPeriodStart()
	 * @see #setPromptPeriodStart(LocalDate)
	 * @generated
	 */
	boolean isSetPromptPeriodStart();

	/**
	 * Returns the value of the '<em><b>Prompt Period End</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Prompt Period End</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Prompt Period End</em>' attribute.
	 * @see #isSetPromptPeriodEnd()
	 * @see #unsetPromptPeriodEnd()
	 * @see #setPromptPeriodEnd(LocalDate)
	 * @see com.mmxlabs.models.lng.scenario.model.LNGScenarioPackage#getLNGScenarioModel_PromptPeriodEnd()
	 * @model unsettable="true" dataType="com.mmxlabs.models.datetime.LocalDate"
	 * @generated
	 */
	LocalDate getPromptPeriodEnd();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.scenario.model.LNGScenarioModel#getPromptPeriodEnd <em>Prompt Period End</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Prompt Period End</em>' attribute.
	 * @see #isSetPromptPeriodEnd()
	 * @see #unsetPromptPeriodEnd()
	 * @see #getPromptPeriodEnd()
	 * @generated
	 */
	void setPromptPeriodEnd(LocalDate value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.scenario.model.LNGScenarioModel#getPromptPeriodEnd <em>Prompt Period End</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetPromptPeriodEnd()
	 * @see #getPromptPeriodEnd()
	 * @see #setPromptPeriodEnd(LocalDate)
	 * @generated
	 */
	void unsetPromptPeriodEnd();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.scenario.model.LNGScenarioModel#getPromptPeriodEnd <em>Prompt Period End</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Prompt Period End</em>' attribute is set.
	 * @see #unsetPromptPeriodEnd()
	 * @see #getPromptPeriodEnd()
	 * @see #setPromptPeriodEnd(LocalDate)
	 * @generated
	 */
	boolean isSetPromptPeriodEnd();

	/**
	 * Returns the value of the '<em><b>Reference Model</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Reference Model</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Reference Model</em>' containment reference.
	 * @see #setReferenceModel(LNGReferenceModel)
	 * @see com.mmxlabs.models.lng.scenario.model.LNGScenarioPackage#getLNGScenarioModel_ReferenceModel()
	 * @model containment="true" resolveProxies="true"
	 * @generated
	 */
	LNGReferenceModel getReferenceModel();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.scenario.model.LNGScenarioModel#getReferenceModel <em>Reference Model</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Reference Model</em>' containment reference.
	 * @see #getReferenceModel()
	 * @generated
	 */
	void setReferenceModel(LNGReferenceModel value);

	/**
	 * Returns the value of the '<em><b>User Settings</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>User Settings</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>User Settings</em>' containment reference.
	 * @see #setUserSettings(UserSettings)
	 * @see com.mmxlabs.models.lng.scenario.model.LNGScenarioPackage#getLNGScenarioModel_UserSettings()
	 * @model containment="true" resolveProxies="true"
	 * @generated
	 */
	UserSettings getUserSettings();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.scenario.model.LNGScenarioModel#getUserSettings <em>User Settings</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>User Settings</em>' containment reference.
	 * @see #getUserSettings()
	 * @generated
	 */
	void setUserSettings(UserSettings value);

} // LNGScenarioModel
