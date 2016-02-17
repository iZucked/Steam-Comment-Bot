/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.scenario.model.impl;

import java.time.LocalDate;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

import com.mmxlabs.models.lng.actuals.ActualsModel;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.parameters.OptimiserSettings;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.scenario.model.LNGReferenceModel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioPackage;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.mmxcore.impl.MMXRootObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Model</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.scenario.model.impl.LNGScenarioModelImpl#getCargoModel <em>Cargo Model</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.scenario.model.impl.LNGScenarioModelImpl#getScheduleModel <em>Schedule Model</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.scenario.model.impl.LNGScenarioModelImpl#getParameters <em>Parameters</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.scenario.model.impl.LNGScenarioModelImpl#getActualsModel <em>Actuals Model</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.scenario.model.impl.LNGScenarioModelImpl#getPromptPeriodStart <em>Prompt Period Start</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.scenario.model.impl.LNGScenarioModelImpl#getPromptPeriodEnd <em>Prompt Period End</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.scenario.model.impl.LNGScenarioModelImpl#getReferenceModel <em>Reference Model</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.scenario.model.impl.LNGScenarioModelImpl#getUserSettings <em>User Settings</em>}</li>
 * </ul>
 *
 * @generated
 */
public class LNGScenarioModelImpl extends MMXRootObjectImpl implements LNGScenarioModel {
	/**
	 * The cached value of the '{@link #getCargoModel() <em>Cargo Model</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCargoModel()
	 * @generated
	 * @ordered
	 */
	protected CargoModel cargoModel;

	/**
	 * The cached value of the '{@link #getScheduleModel() <em>Schedule Model</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getScheduleModel()
	 * @generated
	 * @ordered
	 */
	protected ScheduleModel scheduleModel;

	/**
	 * The cached value of the '{@link #getParameters() <em>Parameters</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getParameters()
	 * @generated
	 * @ordered
	 */
	protected OptimiserSettings parameters;

	/**
	 * The cached value of the '{@link #getActualsModel() <em>Actuals Model</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getActualsModel()
	 * @generated
	 * @ordered
	 */
	protected ActualsModel actualsModel;

	/**
	 * The default value of the '{@link #getPromptPeriodStart() <em>Prompt Period Start</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPromptPeriodStart()
	 * @generated
	 * @ordered
	 */
	protected static final LocalDate PROMPT_PERIOD_START_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getPromptPeriodStart() <em>Prompt Period Start</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPromptPeriodStart()
	 * @generated
	 * @ordered
	 */
	protected LocalDate promptPeriodStart = PROMPT_PERIOD_START_EDEFAULT;

	/**
	 * This is true if the Prompt Period Start attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean promptPeriodStartESet;

	/**
	 * The default value of the '{@link #getPromptPeriodEnd() <em>Prompt Period End</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPromptPeriodEnd()
	 * @generated
	 * @ordered
	 */
	protected static final LocalDate PROMPT_PERIOD_END_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getPromptPeriodEnd() <em>Prompt Period End</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPromptPeriodEnd()
	 * @generated
	 * @ordered
	 */
	protected LocalDate promptPeriodEnd = PROMPT_PERIOD_END_EDEFAULT;

	/**
	 * This is true if the Prompt Period End attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean promptPeriodEndESet;

	/**
	 * The cached value of the '{@link #getReferenceModel() <em>Reference Model</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getReferenceModel()
	 * @generated
	 * @ordered
	 */
	protected LNGReferenceModel referenceModel;

	/**
	 * The cached value of the '{@link #getUserSettings() <em>User Settings</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getUserSettings()
	 * @generated
	 * @ordered
	 */
	protected UserSettings userSettings;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected LNGScenarioModelImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return LNGScenarioPackage.eINSTANCE.getLNGScenarioModel();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CargoModel getCargoModel() {
		if (cargoModel != null && cargoModel.eIsProxy()) {
			InternalEObject oldCargoModel = (InternalEObject)cargoModel;
			cargoModel = (CargoModel)eResolveProxy(oldCargoModel);
			if (cargoModel != oldCargoModel) {
				InternalEObject newCargoModel = (InternalEObject)cargoModel;
				NotificationChain msgs = oldCargoModel.eInverseRemove(this, EOPPOSITE_FEATURE_BASE - LNGScenarioPackage.LNG_SCENARIO_MODEL__CARGO_MODEL, null, null);
				if (newCargoModel.eInternalContainer() == null) {
					msgs = newCargoModel.eInverseAdd(this, EOPPOSITE_FEATURE_BASE - LNGScenarioPackage.LNG_SCENARIO_MODEL__CARGO_MODEL, null, msgs);
				}
				if (msgs != null) msgs.dispatch();
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, LNGScenarioPackage.LNG_SCENARIO_MODEL__CARGO_MODEL, oldCargoModel, cargoModel));
			}
		}
		return cargoModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CargoModel basicGetCargoModel() {
		return cargoModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetCargoModel(CargoModel newCargoModel, NotificationChain msgs) {
		CargoModel oldCargoModel = cargoModel;
		cargoModel = newCargoModel;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, LNGScenarioPackage.LNG_SCENARIO_MODEL__CARGO_MODEL, oldCargoModel, newCargoModel);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setCargoModel(CargoModel newCargoModel) {
		if (newCargoModel != cargoModel) {
			NotificationChain msgs = null;
			if (cargoModel != null)
				msgs = ((InternalEObject)cargoModel).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - LNGScenarioPackage.LNG_SCENARIO_MODEL__CARGO_MODEL, null, msgs);
			if (newCargoModel != null)
				msgs = ((InternalEObject)newCargoModel).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - LNGScenarioPackage.LNG_SCENARIO_MODEL__CARGO_MODEL, null, msgs);
			msgs = basicSetCargoModel(newCargoModel, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, LNGScenarioPackage.LNG_SCENARIO_MODEL__CARGO_MODEL, newCargoModel, newCargoModel));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ScheduleModel getScheduleModel() {
		if (scheduleModel != null && scheduleModel.eIsProxy()) {
			InternalEObject oldScheduleModel = (InternalEObject)scheduleModel;
			scheduleModel = (ScheduleModel)eResolveProxy(oldScheduleModel);
			if (scheduleModel != oldScheduleModel) {
				InternalEObject newScheduleModel = (InternalEObject)scheduleModel;
				NotificationChain msgs = oldScheduleModel.eInverseRemove(this, EOPPOSITE_FEATURE_BASE - LNGScenarioPackage.LNG_SCENARIO_MODEL__SCHEDULE_MODEL, null, null);
				if (newScheduleModel.eInternalContainer() == null) {
					msgs = newScheduleModel.eInverseAdd(this, EOPPOSITE_FEATURE_BASE - LNGScenarioPackage.LNG_SCENARIO_MODEL__SCHEDULE_MODEL, null, msgs);
				}
				if (msgs != null) msgs.dispatch();
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, LNGScenarioPackage.LNG_SCENARIO_MODEL__SCHEDULE_MODEL, oldScheduleModel, scheduleModel));
			}
		}
		return scheduleModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ScheduleModel basicGetScheduleModel() {
		return scheduleModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetScheduleModel(ScheduleModel newScheduleModel, NotificationChain msgs) {
		ScheduleModel oldScheduleModel = scheduleModel;
		scheduleModel = newScheduleModel;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, LNGScenarioPackage.LNG_SCENARIO_MODEL__SCHEDULE_MODEL, oldScheduleModel, newScheduleModel);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setScheduleModel(ScheduleModel newScheduleModel) {
		if (newScheduleModel != scheduleModel) {
			NotificationChain msgs = null;
			if (scheduleModel != null)
				msgs = ((InternalEObject)scheduleModel).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - LNGScenarioPackage.LNG_SCENARIO_MODEL__SCHEDULE_MODEL, null, msgs);
			if (newScheduleModel != null)
				msgs = ((InternalEObject)newScheduleModel).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - LNGScenarioPackage.LNG_SCENARIO_MODEL__SCHEDULE_MODEL, null, msgs);
			msgs = basicSetScheduleModel(newScheduleModel, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, LNGScenarioPackage.LNG_SCENARIO_MODEL__SCHEDULE_MODEL, newScheduleModel, newScheduleModel));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public OptimiserSettings getParameters() {
		if (parameters != null && parameters.eIsProxy()) {
			InternalEObject oldParameters = (InternalEObject)parameters;
			parameters = (OptimiserSettings)eResolveProxy(oldParameters);
			if (parameters != oldParameters) {
				InternalEObject newParameters = (InternalEObject)parameters;
				NotificationChain msgs = oldParameters.eInverseRemove(this, EOPPOSITE_FEATURE_BASE - LNGScenarioPackage.LNG_SCENARIO_MODEL__PARAMETERS, null, null);
				if (newParameters.eInternalContainer() == null) {
					msgs = newParameters.eInverseAdd(this, EOPPOSITE_FEATURE_BASE - LNGScenarioPackage.LNG_SCENARIO_MODEL__PARAMETERS, null, msgs);
				}
				if (msgs != null) msgs.dispatch();
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, LNGScenarioPackage.LNG_SCENARIO_MODEL__PARAMETERS, oldParameters, parameters));
			}
		}
		return parameters;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public OptimiserSettings basicGetParameters() {
		return parameters;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetParameters(OptimiserSettings newParameters, NotificationChain msgs) {
		OptimiserSettings oldParameters = parameters;
		parameters = newParameters;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, LNGScenarioPackage.LNG_SCENARIO_MODEL__PARAMETERS, oldParameters, newParameters);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setParameters(OptimiserSettings newParameters) {
		if (newParameters != parameters) {
			NotificationChain msgs = null;
			if (parameters != null)
				msgs = ((InternalEObject)parameters).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - LNGScenarioPackage.LNG_SCENARIO_MODEL__PARAMETERS, null, msgs);
			if (newParameters != null)
				msgs = ((InternalEObject)newParameters).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - LNGScenarioPackage.LNG_SCENARIO_MODEL__PARAMETERS, null, msgs);
			msgs = basicSetParameters(newParameters, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, LNGScenarioPackage.LNG_SCENARIO_MODEL__PARAMETERS, newParameters, newParameters));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ActualsModel getActualsModel() {
		if (actualsModel != null && actualsModel.eIsProxy()) {
			InternalEObject oldActualsModel = (InternalEObject)actualsModel;
			actualsModel = (ActualsModel)eResolveProxy(oldActualsModel);
			if (actualsModel != oldActualsModel) {
				InternalEObject newActualsModel = (InternalEObject)actualsModel;
				NotificationChain msgs = oldActualsModel.eInverseRemove(this, EOPPOSITE_FEATURE_BASE - LNGScenarioPackage.LNG_SCENARIO_MODEL__ACTUALS_MODEL, null, null);
				if (newActualsModel.eInternalContainer() == null) {
					msgs = newActualsModel.eInverseAdd(this, EOPPOSITE_FEATURE_BASE - LNGScenarioPackage.LNG_SCENARIO_MODEL__ACTUALS_MODEL, null, msgs);
				}
				if (msgs != null) msgs.dispatch();
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, LNGScenarioPackage.LNG_SCENARIO_MODEL__ACTUALS_MODEL, oldActualsModel, actualsModel));
			}
		}
		return actualsModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ActualsModel basicGetActualsModel() {
		return actualsModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetActualsModel(ActualsModel newActualsModel, NotificationChain msgs) {
		ActualsModel oldActualsModel = actualsModel;
		actualsModel = newActualsModel;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, LNGScenarioPackage.LNG_SCENARIO_MODEL__ACTUALS_MODEL, oldActualsModel, newActualsModel);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setActualsModel(ActualsModel newActualsModel) {
		if (newActualsModel != actualsModel) {
			NotificationChain msgs = null;
			if (actualsModel != null)
				msgs = ((InternalEObject)actualsModel).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - LNGScenarioPackage.LNG_SCENARIO_MODEL__ACTUALS_MODEL, null, msgs);
			if (newActualsModel != null)
				msgs = ((InternalEObject)newActualsModel).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - LNGScenarioPackage.LNG_SCENARIO_MODEL__ACTUALS_MODEL, null, msgs);
			msgs = basicSetActualsModel(newActualsModel, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, LNGScenarioPackage.LNG_SCENARIO_MODEL__ACTUALS_MODEL, newActualsModel, newActualsModel));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public LocalDate getPromptPeriodStart() {
		return promptPeriodStart;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setPromptPeriodStart(LocalDate newPromptPeriodStart) {
		LocalDate oldPromptPeriodStart = promptPeriodStart;
		promptPeriodStart = newPromptPeriodStart;
		boolean oldPromptPeriodStartESet = promptPeriodStartESet;
		promptPeriodStartESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, LNGScenarioPackage.LNG_SCENARIO_MODEL__PROMPT_PERIOD_START, oldPromptPeriodStart, promptPeriodStart, !oldPromptPeriodStartESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetPromptPeriodStart() {
		LocalDate oldPromptPeriodStart = promptPeriodStart;
		boolean oldPromptPeriodStartESet = promptPeriodStartESet;
		promptPeriodStart = PROMPT_PERIOD_START_EDEFAULT;
		promptPeriodStartESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, LNGScenarioPackage.LNG_SCENARIO_MODEL__PROMPT_PERIOD_START, oldPromptPeriodStart, PROMPT_PERIOD_START_EDEFAULT, oldPromptPeriodStartESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetPromptPeriodStart() {
		return promptPeriodStartESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public LocalDate getPromptPeriodEnd() {
		return promptPeriodEnd;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setPromptPeriodEnd(LocalDate newPromptPeriodEnd) {
		LocalDate oldPromptPeriodEnd = promptPeriodEnd;
		promptPeriodEnd = newPromptPeriodEnd;
		boolean oldPromptPeriodEndESet = promptPeriodEndESet;
		promptPeriodEndESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, LNGScenarioPackage.LNG_SCENARIO_MODEL__PROMPT_PERIOD_END, oldPromptPeriodEnd, promptPeriodEnd, !oldPromptPeriodEndESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetPromptPeriodEnd() {
		LocalDate oldPromptPeriodEnd = promptPeriodEnd;
		boolean oldPromptPeriodEndESet = promptPeriodEndESet;
		promptPeriodEnd = PROMPT_PERIOD_END_EDEFAULT;
		promptPeriodEndESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, LNGScenarioPackage.LNG_SCENARIO_MODEL__PROMPT_PERIOD_END, oldPromptPeriodEnd, PROMPT_PERIOD_END_EDEFAULT, oldPromptPeriodEndESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetPromptPeriodEnd() {
		return promptPeriodEndESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public LNGReferenceModel getReferenceModel() {
		if (referenceModel != null && referenceModel.eIsProxy()) {
			InternalEObject oldReferenceModel = (InternalEObject)referenceModel;
			referenceModel = (LNGReferenceModel)eResolveProxy(oldReferenceModel);
			if (referenceModel != oldReferenceModel) {
				InternalEObject newReferenceModel = (InternalEObject)referenceModel;
				NotificationChain msgs = oldReferenceModel.eInverseRemove(this, EOPPOSITE_FEATURE_BASE - LNGScenarioPackage.LNG_SCENARIO_MODEL__REFERENCE_MODEL, null, null);
				if (newReferenceModel.eInternalContainer() == null) {
					msgs = newReferenceModel.eInverseAdd(this, EOPPOSITE_FEATURE_BASE - LNGScenarioPackage.LNG_SCENARIO_MODEL__REFERENCE_MODEL, null, msgs);
				}
				if (msgs != null) msgs.dispatch();
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, LNGScenarioPackage.LNG_SCENARIO_MODEL__REFERENCE_MODEL, oldReferenceModel, referenceModel));
			}
		}
		return referenceModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public LNGReferenceModel basicGetReferenceModel() {
		return referenceModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetReferenceModel(LNGReferenceModel newReferenceModel, NotificationChain msgs) {
		LNGReferenceModel oldReferenceModel = referenceModel;
		referenceModel = newReferenceModel;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, LNGScenarioPackage.LNG_SCENARIO_MODEL__REFERENCE_MODEL, oldReferenceModel, newReferenceModel);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setReferenceModel(LNGReferenceModel newReferenceModel) {
		if (newReferenceModel != referenceModel) {
			NotificationChain msgs = null;
			if (referenceModel != null)
				msgs = ((InternalEObject)referenceModel).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - LNGScenarioPackage.LNG_SCENARIO_MODEL__REFERENCE_MODEL, null, msgs);
			if (newReferenceModel != null)
				msgs = ((InternalEObject)newReferenceModel).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - LNGScenarioPackage.LNG_SCENARIO_MODEL__REFERENCE_MODEL, null, msgs);
			msgs = basicSetReferenceModel(newReferenceModel, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, LNGScenarioPackage.LNG_SCENARIO_MODEL__REFERENCE_MODEL, newReferenceModel, newReferenceModel));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public UserSettings getUserSettings() {
		if (userSettings != null && userSettings.eIsProxy()) {
			InternalEObject oldUserSettings = (InternalEObject)userSettings;
			userSettings = (UserSettings)eResolveProxy(oldUserSettings);
			if (userSettings != oldUserSettings) {
				InternalEObject newUserSettings = (InternalEObject)userSettings;
				NotificationChain msgs = oldUserSettings.eInverseRemove(this, EOPPOSITE_FEATURE_BASE - LNGScenarioPackage.LNG_SCENARIO_MODEL__USER_SETTINGS, null, null);
				if (newUserSettings.eInternalContainer() == null) {
					msgs = newUserSettings.eInverseAdd(this, EOPPOSITE_FEATURE_BASE - LNGScenarioPackage.LNG_SCENARIO_MODEL__USER_SETTINGS, null, msgs);
				}
				if (msgs != null) msgs.dispatch();
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, LNGScenarioPackage.LNG_SCENARIO_MODEL__USER_SETTINGS, oldUserSettings, userSettings));
			}
		}
		return userSettings;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public UserSettings basicGetUserSettings() {
		return userSettings;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetUserSettings(UserSettings newUserSettings, NotificationChain msgs) {
		UserSettings oldUserSettings = userSettings;
		userSettings = newUserSettings;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, LNGScenarioPackage.LNG_SCENARIO_MODEL__USER_SETTINGS, oldUserSettings, newUserSettings);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setUserSettings(UserSettings newUserSettings) {
		if (newUserSettings != userSettings) {
			NotificationChain msgs = null;
			if (userSettings != null)
				msgs = ((InternalEObject)userSettings).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - LNGScenarioPackage.LNG_SCENARIO_MODEL__USER_SETTINGS, null, msgs);
			if (newUserSettings != null)
				msgs = ((InternalEObject)newUserSettings).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - LNGScenarioPackage.LNG_SCENARIO_MODEL__USER_SETTINGS, null, msgs);
			msgs = basicSetUserSettings(newUserSettings, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, LNGScenarioPackage.LNG_SCENARIO_MODEL__USER_SETTINGS, newUserSettings, newUserSettings));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case LNGScenarioPackage.LNG_SCENARIO_MODEL__CARGO_MODEL:
				return basicSetCargoModel(null, msgs);
			case LNGScenarioPackage.LNG_SCENARIO_MODEL__SCHEDULE_MODEL:
				return basicSetScheduleModel(null, msgs);
			case LNGScenarioPackage.LNG_SCENARIO_MODEL__PARAMETERS:
				return basicSetParameters(null, msgs);
			case LNGScenarioPackage.LNG_SCENARIO_MODEL__ACTUALS_MODEL:
				return basicSetActualsModel(null, msgs);
			case LNGScenarioPackage.LNG_SCENARIO_MODEL__REFERENCE_MODEL:
				return basicSetReferenceModel(null, msgs);
			case LNGScenarioPackage.LNG_SCENARIO_MODEL__USER_SETTINGS:
				return basicSetUserSettings(null, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case LNGScenarioPackage.LNG_SCENARIO_MODEL__CARGO_MODEL:
				if (resolve) return getCargoModel();
				return basicGetCargoModel();
			case LNGScenarioPackage.LNG_SCENARIO_MODEL__SCHEDULE_MODEL:
				if (resolve) return getScheduleModel();
				return basicGetScheduleModel();
			case LNGScenarioPackage.LNG_SCENARIO_MODEL__PARAMETERS:
				if (resolve) return getParameters();
				return basicGetParameters();
			case LNGScenarioPackage.LNG_SCENARIO_MODEL__ACTUALS_MODEL:
				if (resolve) return getActualsModel();
				return basicGetActualsModel();
			case LNGScenarioPackage.LNG_SCENARIO_MODEL__PROMPT_PERIOD_START:
				return getPromptPeriodStart();
			case LNGScenarioPackage.LNG_SCENARIO_MODEL__PROMPT_PERIOD_END:
				return getPromptPeriodEnd();
			case LNGScenarioPackage.LNG_SCENARIO_MODEL__REFERENCE_MODEL:
				if (resolve) return getReferenceModel();
				return basicGetReferenceModel();
			case LNGScenarioPackage.LNG_SCENARIO_MODEL__USER_SETTINGS:
				if (resolve) return getUserSettings();
				return basicGetUserSettings();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case LNGScenarioPackage.LNG_SCENARIO_MODEL__CARGO_MODEL:
				setCargoModel((CargoModel)newValue);
				return;
			case LNGScenarioPackage.LNG_SCENARIO_MODEL__SCHEDULE_MODEL:
				setScheduleModel((ScheduleModel)newValue);
				return;
			case LNGScenarioPackage.LNG_SCENARIO_MODEL__PARAMETERS:
				setParameters((OptimiserSettings)newValue);
				return;
			case LNGScenarioPackage.LNG_SCENARIO_MODEL__ACTUALS_MODEL:
				setActualsModel((ActualsModel)newValue);
				return;
			case LNGScenarioPackage.LNG_SCENARIO_MODEL__PROMPT_PERIOD_START:
				setPromptPeriodStart((LocalDate)newValue);
				return;
			case LNGScenarioPackage.LNG_SCENARIO_MODEL__PROMPT_PERIOD_END:
				setPromptPeriodEnd((LocalDate)newValue);
				return;
			case LNGScenarioPackage.LNG_SCENARIO_MODEL__REFERENCE_MODEL:
				setReferenceModel((LNGReferenceModel)newValue);
				return;
			case LNGScenarioPackage.LNG_SCENARIO_MODEL__USER_SETTINGS:
				setUserSettings((UserSettings)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case LNGScenarioPackage.LNG_SCENARIO_MODEL__CARGO_MODEL:
				setCargoModel((CargoModel)null);
				return;
			case LNGScenarioPackage.LNG_SCENARIO_MODEL__SCHEDULE_MODEL:
				setScheduleModel((ScheduleModel)null);
				return;
			case LNGScenarioPackage.LNG_SCENARIO_MODEL__PARAMETERS:
				setParameters((OptimiserSettings)null);
				return;
			case LNGScenarioPackage.LNG_SCENARIO_MODEL__ACTUALS_MODEL:
				setActualsModel((ActualsModel)null);
				return;
			case LNGScenarioPackage.LNG_SCENARIO_MODEL__PROMPT_PERIOD_START:
				unsetPromptPeriodStart();
				return;
			case LNGScenarioPackage.LNG_SCENARIO_MODEL__PROMPT_PERIOD_END:
				unsetPromptPeriodEnd();
				return;
			case LNGScenarioPackage.LNG_SCENARIO_MODEL__REFERENCE_MODEL:
				setReferenceModel((LNGReferenceModel)null);
				return;
			case LNGScenarioPackage.LNG_SCENARIO_MODEL__USER_SETTINGS:
				setUserSettings((UserSettings)null);
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case LNGScenarioPackage.LNG_SCENARIO_MODEL__CARGO_MODEL:
				return cargoModel != null;
			case LNGScenarioPackage.LNG_SCENARIO_MODEL__SCHEDULE_MODEL:
				return scheduleModel != null;
			case LNGScenarioPackage.LNG_SCENARIO_MODEL__PARAMETERS:
				return parameters != null;
			case LNGScenarioPackage.LNG_SCENARIO_MODEL__ACTUALS_MODEL:
				return actualsModel != null;
			case LNGScenarioPackage.LNG_SCENARIO_MODEL__PROMPT_PERIOD_START:
				return isSetPromptPeriodStart();
			case LNGScenarioPackage.LNG_SCENARIO_MODEL__PROMPT_PERIOD_END:
				return isSetPromptPeriodEnd();
			case LNGScenarioPackage.LNG_SCENARIO_MODEL__REFERENCE_MODEL:
				return referenceModel != null;
			case LNGScenarioPackage.LNG_SCENARIO_MODEL__USER_SETTINGS:
				return userSettings != null;
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (promptPeriodStart: ");
		if (promptPeriodStartESet) result.append(promptPeriodStart); else result.append("<unset>");
		result.append(", promptPeriodEnd: ");
		if (promptPeriodEndESet) result.append(promptPeriodEnd); else result.append("<unset>");
		result.append(')');
		return result.toString();
	}

} //LNGScenarioModelImpl
