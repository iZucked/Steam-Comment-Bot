/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2021
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.scenario.model;

import java.time.LocalDate;

import com.mmxlabs.models.lng.actuals.ActualsModel;
import com.mmxlabs.models.lng.adp.ADPModel;
import com.mmxlabs.models.lng.analytics.AnalyticsModel;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.nominations.NominationsModel;
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
 *   <li>{@link com.mmxlabs.models.lng.scenario.model.LNGScenarioModel#getActualsModel <em>Actuals Model</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.scenario.model.LNGScenarioModel#getPromptPeriodStart <em>Prompt Period Start</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.scenario.model.LNGScenarioModel#getPromptPeriodEnd <em>Prompt Period End</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.scenario.model.LNGScenarioModel#getSchedulingEndDate <em>Scheduling End Date</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.scenario.model.LNGScenarioModel#getReferenceModel <em>Reference Model</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.scenario.model.LNGScenarioModel#getUserSettings <em>User Settings</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.scenario.model.LNGScenarioModel#getAnalyticsModel <em>Analytics Model</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.scenario.model.LNGScenarioModel#getAdpModel <em>Adp Model</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.scenario.model.LNGScenarioModel#getNominationsModel <em>Nominations Model</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.scenario.model.LNGScenarioModel#isLongTerm <em>Long Term</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.scenario.model.LNGScenarioModel#isAnonymised <em>Anonymised</em>}</li>
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
	 * @see #setPromptPeriodStart(LocalDate)
	 * @see com.mmxlabs.models.lng.scenario.model.LNGScenarioPackage#getLNGScenarioModel_PromptPeriodStart()
	 * @model dataType="com.mmxlabs.models.datetime.LocalDate"
	 * @generated
	 */
	LocalDate getPromptPeriodStart();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.scenario.model.LNGScenarioModel#getPromptPeriodStart <em>Prompt Period Start</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Prompt Period Start</em>' attribute.
	 * @see #getPromptPeriodStart()
	 * @generated
	 */
	void setPromptPeriodStart(LocalDate value);

	/**
	 * Returns the value of the '<em><b>Prompt Period End</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Prompt Period End</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Prompt Period End</em>' attribute.
	 * @see #setPromptPeriodEnd(LocalDate)
	 * @see com.mmxlabs.models.lng.scenario.model.LNGScenarioPackage#getLNGScenarioModel_PromptPeriodEnd()
	 * @model dataType="com.mmxlabs.models.datetime.LocalDate"
	 * @generated
	 */
	LocalDate getPromptPeriodEnd();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.scenario.model.LNGScenarioModel#getPromptPeriodEnd <em>Prompt Period End</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Prompt Period End</em>' attribute.
	 * @see #getPromptPeriodEnd()
	 * @generated
	 */
	void setPromptPeriodEnd(LocalDate value);

	/**
	 * Returns the value of the '<em><b>Scheduling End Date</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Scheduling End Date</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Scheduling End Date</em>' attribute.
	 * @see #isSetSchedulingEndDate()
	 * @see #unsetSchedulingEndDate()
	 * @see #setSchedulingEndDate(LocalDate)
	 * @see com.mmxlabs.models.lng.scenario.model.LNGScenarioPackage#getLNGScenarioModel_SchedulingEndDate()
	 * @model unsettable="true" dataType="com.mmxlabs.models.datetime.LocalDate"
	 * @generated
	 */
	LocalDate getSchedulingEndDate();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.scenario.model.LNGScenarioModel#getSchedulingEndDate <em>Scheduling End Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Scheduling End Date</em>' attribute.
	 * @see #isSetSchedulingEndDate()
	 * @see #unsetSchedulingEndDate()
	 * @see #getSchedulingEndDate()
	 * @generated
	 */
	void setSchedulingEndDate(LocalDate value);

	/**
	 * Unsets the value of the '{@link com.mmxlabs.models.lng.scenario.model.LNGScenarioModel#getSchedulingEndDate <em>Scheduling End Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetSchedulingEndDate()
	 * @see #getSchedulingEndDate()
	 * @see #setSchedulingEndDate(LocalDate)
	 * @generated
	 */
	void unsetSchedulingEndDate();

	/**
	 * Returns whether the value of the '{@link com.mmxlabs.models.lng.scenario.model.LNGScenarioModel#getSchedulingEndDate <em>Scheduling End Date</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Scheduling End Date</em>' attribute is set.
	 * @see #unsetSchedulingEndDate()
	 * @see #getSchedulingEndDate()
	 * @see #setSchedulingEndDate(LocalDate)
	 * @generated
	 */
	boolean isSetSchedulingEndDate();

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

	/**
	 * Returns the value of the '<em><b>Analytics Model</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Analytics Model</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Analytics Model</em>' containment reference.
	 * @see #setAnalyticsModel(AnalyticsModel)
	 * @see com.mmxlabs.models.lng.scenario.model.LNGScenarioPackage#getLNGScenarioModel_AnalyticsModel()
	 * @model containment="true" resolveProxies="true"
	 * @generated
	 */
	AnalyticsModel getAnalyticsModel();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.scenario.model.LNGScenarioModel#getAnalyticsModel <em>Analytics Model</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Analytics Model</em>' containment reference.
	 * @see #getAnalyticsModel()
	 * @generated
	 */
	void setAnalyticsModel(AnalyticsModel value);

	/**
	 * Returns the value of the '<em><b>Adp Model</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Adp Model</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Adp Model</em>' containment reference.
	 * @see #setAdpModel(ADPModel)
	 * @see com.mmxlabs.models.lng.scenario.model.LNGScenarioPackage#getLNGScenarioModel_AdpModel()
	 * @model containment="true" resolveProxies="true"
	 * @generated
	 */
	ADPModel getAdpModel();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.scenario.model.LNGScenarioModel#getAdpModel <em>Adp Model</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Adp Model</em>' containment reference.
	 * @see #getAdpModel()
	 * @generated
	 */
	void setAdpModel(ADPModel value);

	/**
	 * Returns the value of the '<em><b>Nominations Model</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Nominations Model</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Nominations Model</em>' containment reference.
	 * @see #setNominationsModel(NominationsModel)
	 * @see com.mmxlabs.models.lng.scenario.model.LNGScenarioPackage#getLNGScenarioModel_NominationsModel()
	 * @model containment="true" resolveProxies="true"
	 * @generated
	 */
	NominationsModel getNominationsModel();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.scenario.model.LNGScenarioModel#getNominationsModel <em>Nominations Model</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Nominations Model</em>' containment reference.
	 * @see #getNominationsModel()
	 * @generated
	 */
	void setNominationsModel(NominationsModel value);

	/**
	 * Returns the value of the '<em><b>Long Term</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Long Term</em>' attribute.
	 * @see #setLongTerm(boolean)
	 * @see com.mmxlabs.models.lng.scenario.model.LNGScenarioPackage#getLNGScenarioModel_LongTerm()
	 * @model
	 * @generated
	 */
	boolean isLongTerm();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.scenario.model.LNGScenarioModel#isLongTerm <em>Long Term</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Long Term</em>' attribute.
	 * @see #isLongTerm()
	 * @generated
	 */
	void setLongTerm(boolean value);

	/**
	 * Returns the value of the '<em><b>Anonymised</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Anonymised</em>' attribute.
	 * @see #setAnonymised(boolean)
	 * @see com.mmxlabs.models.lng.scenario.model.LNGScenarioPackage#getLNGScenarioModel_Anonymised()
	 * @model
	 * @generated
	 */
	boolean isAnonymised();

	/**
	 * Sets the value of the '{@link com.mmxlabs.models.lng.scenario.model.LNGScenarioModel#isAnonymised <em>Anonymised</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Anonymised</em>' attribute.
	 * @see #isAnonymised()
	 * @generated
	 */
	void setAnonymised(boolean value);

} // LNGScenarioModel
