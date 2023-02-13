/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2023
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
import com.mmxlabs.models.lng.adp.ADPModel;
import com.mmxlabs.models.lng.analytics.AnalyticsModel;
import com.mmxlabs.models.lng.analytics.SensitivityModel;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.nominations.NominationsModel;
import com.mmxlabs.models.lng.parameters.UserSettings;
import com.mmxlabs.models.lng.scenario.model.LNGReferenceModel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioPackage;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.lng.transfers.TransferModel;
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
 *   <li>{@link com.mmxlabs.models.lng.scenario.model.impl.LNGScenarioModelImpl#getActualsModel <em>Actuals Model</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.scenario.model.impl.LNGScenarioModelImpl#getPromptPeriodStart <em>Prompt Period Start</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.scenario.model.impl.LNGScenarioModelImpl#getPromptPeriodEnd <em>Prompt Period End</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.scenario.model.impl.LNGScenarioModelImpl#getSchedulingEndDate <em>Scheduling End Date</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.scenario.model.impl.LNGScenarioModelImpl#getReferenceModel <em>Reference Model</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.scenario.model.impl.LNGScenarioModelImpl#getUserSettings <em>User Settings</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.scenario.model.impl.LNGScenarioModelImpl#getAnalyticsModel <em>Analytics Model</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.scenario.model.impl.LNGScenarioModelImpl#getAdpModel <em>Adp Model</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.scenario.model.impl.LNGScenarioModelImpl#getNominationsModel <em>Nominations Model</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.scenario.model.impl.LNGScenarioModelImpl#isLongTerm <em>Long Term</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.scenario.model.impl.LNGScenarioModelImpl#isAnonymised <em>Anonymised</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.scenario.model.impl.LNGScenarioModelImpl#getSensitivityModel <em>Sensitivity Model</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.scenario.model.impl.LNGScenarioModelImpl#getTransferModel <em>Transfer Model</em>}</li>
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
	 * The default value of the '{@link #getSchedulingEndDate() <em>Scheduling End Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSchedulingEndDate()
	 * @generated
	 * @ordered
	 */
	protected static final LocalDate SCHEDULING_END_DATE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getSchedulingEndDate() <em>Scheduling End Date</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSchedulingEndDate()
	 * @generated
	 * @ordered
	 */
	protected LocalDate schedulingEndDate = SCHEDULING_END_DATE_EDEFAULT;

	/**
	 * This is true if the Scheduling End Date attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean schedulingEndDateESet;

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
	 * The cached value of the '{@link #getAnalyticsModel() <em>Analytics Model</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAnalyticsModel()
	 * @generated
	 * @ordered
	 */
	protected AnalyticsModel analyticsModel;

	/**
	 * The cached value of the '{@link #getAdpModel() <em>Adp Model</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAdpModel()
	 * @generated
	 * @ordered
	 */
	protected ADPModel adpModel;

	/**
	 * The cached value of the '{@link #getNominationsModel() <em>Nominations Model</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNominationsModel()
	 * @generated
	 * @ordered
	 */
	protected NominationsModel nominationsModel;

	/**
	 * The default value of the '{@link #isLongTerm() <em>Long Term</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isLongTerm()
	 * @generated
	 * @ordered
	 */
	protected static final boolean LONG_TERM_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isLongTerm() <em>Long Term</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isLongTerm()
	 * @generated
	 * @ordered
	 */
	protected boolean longTerm = LONG_TERM_EDEFAULT;

	/**
	 * The default value of the '{@link #isAnonymised() <em>Anonymised</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isAnonymised()
	 * @generated
	 * @ordered
	 */
	protected static final boolean ANONYMISED_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isAnonymised() <em>Anonymised</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isAnonymised()
	 * @generated
	 * @ordered
	 */
	protected boolean anonymised = ANONYMISED_EDEFAULT;

	/**
	 * The cached value of the '{@link #getSensitivityModel() <em>Sensitivity Model</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSensitivityModel()
	 * @generated
	 * @ordered
	 */
	protected SensitivityModel sensitivityModel;

	/**
	 * The cached value of the '{@link #getTransferModel() <em>Transfer Model</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTransferModel()
	 * @generated
	 * @ordered
	 */
	protected TransferModel transferModel;

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
	@Override
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
	@Override
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
	@Override
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
	@Override
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
	@Override
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
	@Override
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
	@Override
	public LocalDate getPromptPeriodStart() {
		return promptPeriodStart;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setPromptPeriodStart(LocalDate newPromptPeriodStart) {
		LocalDate oldPromptPeriodStart = promptPeriodStart;
		promptPeriodStart = newPromptPeriodStart;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, LNGScenarioPackage.LNG_SCENARIO_MODEL__PROMPT_PERIOD_START, oldPromptPeriodStart, promptPeriodStart));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public LocalDate getPromptPeriodEnd() {
		return promptPeriodEnd;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setPromptPeriodEnd(LocalDate newPromptPeriodEnd) {
		LocalDate oldPromptPeriodEnd = promptPeriodEnd;
		promptPeriodEnd = newPromptPeriodEnd;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, LNGScenarioPackage.LNG_SCENARIO_MODEL__PROMPT_PERIOD_END, oldPromptPeriodEnd, promptPeriodEnd));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public LocalDate getSchedulingEndDate() {
		return schedulingEndDate;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setSchedulingEndDate(LocalDate newSchedulingEndDate) {
		LocalDate oldSchedulingEndDate = schedulingEndDate;
		schedulingEndDate = newSchedulingEndDate;
		boolean oldSchedulingEndDateESet = schedulingEndDateESet;
		schedulingEndDateESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, LNGScenarioPackage.LNG_SCENARIO_MODEL__SCHEDULING_END_DATE, oldSchedulingEndDate, schedulingEndDate, !oldSchedulingEndDateESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void unsetSchedulingEndDate() {
		LocalDate oldSchedulingEndDate = schedulingEndDate;
		boolean oldSchedulingEndDateESet = schedulingEndDateESet;
		schedulingEndDate = SCHEDULING_END_DATE_EDEFAULT;
		schedulingEndDateESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, LNGScenarioPackage.LNG_SCENARIO_MODEL__SCHEDULING_END_DATE, oldSchedulingEndDate, SCHEDULING_END_DATE_EDEFAULT, oldSchedulingEndDateESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isSetSchedulingEndDate() {
		return schedulingEndDateESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
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
	@Override
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
	@Override
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
	@Override
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
	public AnalyticsModel getAnalyticsModel() {
		if (analyticsModel != null && analyticsModel.eIsProxy()) {
			InternalEObject oldAnalyticsModel = (InternalEObject)analyticsModel;
			analyticsModel = (AnalyticsModel)eResolveProxy(oldAnalyticsModel);
			if (analyticsModel != oldAnalyticsModel) {
				InternalEObject newAnalyticsModel = (InternalEObject)analyticsModel;
				NotificationChain msgs = oldAnalyticsModel.eInverseRemove(this, EOPPOSITE_FEATURE_BASE - LNGScenarioPackage.LNG_SCENARIO_MODEL__ANALYTICS_MODEL, null, null);
				if (newAnalyticsModel.eInternalContainer() == null) {
					msgs = newAnalyticsModel.eInverseAdd(this, EOPPOSITE_FEATURE_BASE - LNGScenarioPackage.LNG_SCENARIO_MODEL__ANALYTICS_MODEL, null, msgs);
				}
				if (msgs != null) msgs.dispatch();
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, LNGScenarioPackage.LNG_SCENARIO_MODEL__ANALYTICS_MODEL, oldAnalyticsModel, analyticsModel));
			}
		}
		return analyticsModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AnalyticsModel basicGetAnalyticsModel() {
		return analyticsModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetAnalyticsModel(AnalyticsModel newAnalyticsModel, NotificationChain msgs) {
		AnalyticsModel oldAnalyticsModel = analyticsModel;
		analyticsModel = newAnalyticsModel;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, LNGScenarioPackage.LNG_SCENARIO_MODEL__ANALYTICS_MODEL, oldAnalyticsModel, newAnalyticsModel);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setAnalyticsModel(AnalyticsModel newAnalyticsModel) {
		if (newAnalyticsModel != analyticsModel) {
			NotificationChain msgs = null;
			if (analyticsModel != null)
				msgs = ((InternalEObject)analyticsModel).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - LNGScenarioPackage.LNG_SCENARIO_MODEL__ANALYTICS_MODEL, null, msgs);
			if (newAnalyticsModel != null)
				msgs = ((InternalEObject)newAnalyticsModel).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - LNGScenarioPackage.LNG_SCENARIO_MODEL__ANALYTICS_MODEL, null, msgs);
			msgs = basicSetAnalyticsModel(newAnalyticsModel, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, LNGScenarioPackage.LNG_SCENARIO_MODEL__ANALYTICS_MODEL, newAnalyticsModel, newAnalyticsModel));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ADPModel getAdpModel() {
		if (adpModel != null && adpModel.eIsProxy()) {
			InternalEObject oldAdpModel = (InternalEObject)adpModel;
			adpModel = (ADPModel)eResolveProxy(oldAdpModel);
			if (adpModel != oldAdpModel) {
				InternalEObject newAdpModel = (InternalEObject)adpModel;
				NotificationChain msgs = oldAdpModel.eInverseRemove(this, EOPPOSITE_FEATURE_BASE - LNGScenarioPackage.LNG_SCENARIO_MODEL__ADP_MODEL, null, null);
				if (newAdpModel.eInternalContainer() == null) {
					msgs = newAdpModel.eInverseAdd(this, EOPPOSITE_FEATURE_BASE - LNGScenarioPackage.LNG_SCENARIO_MODEL__ADP_MODEL, null, msgs);
				}
				if (msgs != null) msgs.dispatch();
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, LNGScenarioPackage.LNG_SCENARIO_MODEL__ADP_MODEL, oldAdpModel, adpModel));
			}
		}
		return adpModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ADPModel basicGetAdpModel() {
		return adpModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetAdpModel(ADPModel newAdpModel, NotificationChain msgs) {
		ADPModel oldAdpModel = adpModel;
		adpModel = newAdpModel;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, LNGScenarioPackage.LNG_SCENARIO_MODEL__ADP_MODEL, oldAdpModel, newAdpModel);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setAdpModel(ADPModel newAdpModel) {
		if (newAdpModel != adpModel) {
			NotificationChain msgs = null;
			if (adpModel != null)
				msgs = ((InternalEObject)adpModel).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - LNGScenarioPackage.LNG_SCENARIO_MODEL__ADP_MODEL, null, msgs);
			if (newAdpModel != null)
				msgs = ((InternalEObject)newAdpModel).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - LNGScenarioPackage.LNG_SCENARIO_MODEL__ADP_MODEL, null, msgs);
			msgs = basicSetAdpModel(newAdpModel, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, LNGScenarioPackage.LNG_SCENARIO_MODEL__ADP_MODEL, newAdpModel, newAdpModel));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NominationsModel getNominationsModel() {
		if (nominationsModel != null && nominationsModel.eIsProxy()) {
			InternalEObject oldNominationsModel = (InternalEObject)nominationsModel;
			nominationsModel = (NominationsModel)eResolveProxy(oldNominationsModel);
			if (nominationsModel != oldNominationsModel) {
				InternalEObject newNominationsModel = (InternalEObject)nominationsModel;
				NotificationChain msgs = oldNominationsModel.eInverseRemove(this, EOPPOSITE_FEATURE_BASE - LNGScenarioPackage.LNG_SCENARIO_MODEL__NOMINATIONS_MODEL, null, null);
				if (newNominationsModel.eInternalContainer() == null) {
					msgs = newNominationsModel.eInverseAdd(this, EOPPOSITE_FEATURE_BASE - LNGScenarioPackage.LNG_SCENARIO_MODEL__NOMINATIONS_MODEL, null, msgs);
				}
				if (msgs != null) msgs.dispatch();
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, LNGScenarioPackage.LNG_SCENARIO_MODEL__NOMINATIONS_MODEL, oldNominationsModel, nominationsModel));
			}
		}
		return nominationsModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NominationsModel basicGetNominationsModel() {
		return nominationsModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetNominationsModel(NominationsModel newNominationsModel, NotificationChain msgs) {
		NominationsModel oldNominationsModel = nominationsModel;
		nominationsModel = newNominationsModel;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, LNGScenarioPackage.LNG_SCENARIO_MODEL__NOMINATIONS_MODEL, oldNominationsModel, newNominationsModel);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setNominationsModel(NominationsModel newNominationsModel) {
		if (newNominationsModel != nominationsModel) {
			NotificationChain msgs = null;
			if (nominationsModel != null)
				msgs = ((InternalEObject)nominationsModel).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - LNGScenarioPackage.LNG_SCENARIO_MODEL__NOMINATIONS_MODEL, null, msgs);
			if (newNominationsModel != null)
				msgs = ((InternalEObject)newNominationsModel).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - LNGScenarioPackage.LNG_SCENARIO_MODEL__NOMINATIONS_MODEL, null, msgs);
			msgs = basicSetNominationsModel(newNominationsModel, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, LNGScenarioPackage.LNG_SCENARIO_MODEL__NOMINATIONS_MODEL, newNominationsModel, newNominationsModel));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isLongTerm() {
		return longTerm;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setLongTerm(boolean newLongTerm) {
		boolean oldLongTerm = longTerm;
		longTerm = newLongTerm;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, LNGScenarioPackage.LNG_SCENARIO_MODEL__LONG_TERM, oldLongTerm, longTerm));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isAnonymised() {
		return anonymised;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setAnonymised(boolean newAnonymised) {
		boolean oldAnonymised = anonymised;
		anonymised = newAnonymised;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, LNGScenarioPackage.LNG_SCENARIO_MODEL__ANONYMISED, oldAnonymised, anonymised));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public SensitivityModel getSensitivityModel() {
		if (sensitivityModel != null && sensitivityModel.eIsProxy()) {
			InternalEObject oldSensitivityModel = (InternalEObject)sensitivityModel;
			sensitivityModel = (SensitivityModel)eResolveProxy(oldSensitivityModel);
			if (sensitivityModel != oldSensitivityModel) {
				InternalEObject newSensitivityModel = (InternalEObject)sensitivityModel;
				NotificationChain msgs = oldSensitivityModel.eInverseRemove(this, EOPPOSITE_FEATURE_BASE - LNGScenarioPackage.LNG_SCENARIO_MODEL__SENSITIVITY_MODEL, null, null);
				if (newSensitivityModel.eInternalContainer() == null) {
					msgs = newSensitivityModel.eInverseAdd(this, EOPPOSITE_FEATURE_BASE - LNGScenarioPackage.LNG_SCENARIO_MODEL__SENSITIVITY_MODEL, null, msgs);
				}
				if (msgs != null) msgs.dispatch();
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, LNGScenarioPackage.LNG_SCENARIO_MODEL__SENSITIVITY_MODEL, oldSensitivityModel, sensitivityModel));
			}
		}
		return sensitivityModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SensitivityModel basicGetSensitivityModel() {
		return sensitivityModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetSensitivityModel(SensitivityModel newSensitivityModel, NotificationChain msgs) {
		SensitivityModel oldSensitivityModel = sensitivityModel;
		sensitivityModel = newSensitivityModel;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, LNGScenarioPackage.LNG_SCENARIO_MODEL__SENSITIVITY_MODEL, oldSensitivityModel, newSensitivityModel);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setSensitivityModel(SensitivityModel newSensitivityModel) {
		if (newSensitivityModel != sensitivityModel) {
			NotificationChain msgs = null;
			if (sensitivityModel != null)
				msgs = ((InternalEObject)sensitivityModel).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - LNGScenarioPackage.LNG_SCENARIO_MODEL__SENSITIVITY_MODEL, null, msgs);
			if (newSensitivityModel != null)
				msgs = ((InternalEObject)newSensitivityModel).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - LNGScenarioPackage.LNG_SCENARIO_MODEL__SENSITIVITY_MODEL, null, msgs);
			msgs = basicSetSensitivityModel(newSensitivityModel, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, LNGScenarioPackage.LNG_SCENARIO_MODEL__SENSITIVITY_MODEL, newSensitivityModel, newSensitivityModel));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public TransferModel getTransferModel() {
		if (transferModel != null && transferModel.eIsProxy()) {
			InternalEObject oldTransferModel = (InternalEObject)transferModel;
			transferModel = (TransferModel)eResolveProxy(oldTransferModel);
			if (transferModel != oldTransferModel) {
				InternalEObject newTransferModel = (InternalEObject)transferModel;
				NotificationChain msgs = oldTransferModel.eInverseRemove(this, EOPPOSITE_FEATURE_BASE - LNGScenarioPackage.LNG_SCENARIO_MODEL__TRANSFER_MODEL, null, null);
				if (newTransferModel.eInternalContainer() == null) {
					msgs = newTransferModel.eInverseAdd(this, EOPPOSITE_FEATURE_BASE - LNGScenarioPackage.LNG_SCENARIO_MODEL__TRANSFER_MODEL, null, msgs);
				}
				if (msgs != null) msgs.dispatch();
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, LNGScenarioPackage.LNG_SCENARIO_MODEL__TRANSFER_MODEL, oldTransferModel, transferModel));
			}
		}
		return transferModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public TransferModel basicGetTransferModel() {
		return transferModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetTransferModel(TransferModel newTransferModel, NotificationChain msgs) {
		TransferModel oldTransferModel = transferModel;
		transferModel = newTransferModel;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, LNGScenarioPackage.LNG_SCENARIO_MODEL__TRANSFER_MODEL, oldTransferModel, newTransferModel);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setTransferModel(TransferModel newTransferModel) {
		if (newTransferModel != transferModel) {
			NotificationChain msgs = null;
			if (transferModel != null)
				msgs = ((InternalEObject)transferModel).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - LNGScenarioPackage.LNG_SCENARIO_MODEL__TRANSFER_MODEL, null, msgs);
			if (newTransferModel != null)
				msgs = ((InternalEObject)newTransferModel).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - LNGScenarioPackage.LNG_SCENARIO_MODEL__TRANSFER_MODEL, null, msgs);
			msgs = basicSetTransferModel(newTransferModel, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, LNGScenarioPackage.LNG_SCENARIO_MODEL__TRANSFER_MODEL, newTransferModel, newTransferModel));
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
			case LNGScenarioPackage.LNG_SCENARIO_MODEL__ACTUALS_MODEL:
				return basicSetActualsModel(null, msgs);
			case LNGScenarioPackage.LNG_SCENARIO_MODEL__REFERENCE_MODEL:
				return basicSetReferenceModel(null, msgs);
			case LNGScenarioPackage.LNG_SCENARIO_MODEL__USER_SETTINGS:
				return basicSetUserSettings(null, msgs);
			case LNGScenarioPackage.LNG_SCENARIO_MODEL__ANALYTICS_MODEL:
				return basicSetAnalyticsModel(null, msgs);
			case LNGScenarioPackage.LNG_SCENARIO_MODEL__ADP_MODEL:
				return basicSetAdpModel(null, msgs);
			case LNGScenarioPackage.LNG_SCENARIO_MODEL__NOMINATIONS_MODEL:
				return basicSetNominationsModel(null, msgs);
			case LNGScenarioPackage.LNG_SCENARIO_MODEL__SENSITIVITY_MODEL:
				return basicSetSensitivityModel(null, msgs);
			case LNGScenarioPackage.LNG_SCENARIO_MODEL__TRANSFER_MODEL:
				return basicSetTransferModel(null, msgs);
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
			case LNGScenarioPackage.LNG_SCENARIO_MODEL__ACTUALS_MODEL:
				if (resolve) return getActualsModel();
				return basicGetActualsModel();
			case LNGScenarioPackage.LNG_SCENARIO_MODEL__PROMPT_PERIOD_START:
				return getPromptPeriodStart();
			case LNGScenarioPackage.LNG_SCENARIO_MODEL__PROMPT_PERIOD_END:
				return getPromptPeriodEnd();
			case LNGScenarioPackage.LNG_SCENARIO_MODEL__SCHEDULING_END_DATE:
				return getSchedulingEndDate();
			case LNGScenarioPackage.LNG_SCENARIO_MODEL__REFERENCE_MODEL:
				if (resolve) return getReferenceModel();
				return basicGetReferenceModel();
			case LNGScenarioPackage.LNG_SCENARIO_MODEL__USER_SETTINGS:
				if (resolve) return getUserSettings();
				return basicGetUserSettings();
			case LNGScenarioPackage.LNG_SCENARIO_MODEL__ANALYTICS_MODEL:
				if (resolve) return getAnalyticsModel();
				return basicGetAnalyticsModel();
			case LNGScenarioPackage.LNG_SCENARIO_MODEL__ADP_MODEL:
				if (resolve) return getAdpModel();
				return basicGetAdpModel();
			case LNGScenarioPackage.LNG_SCENARIO_MODEL__NOMINATIONS_MODEL:
				if (resolve) return getNominationsModel();
				return basicGetNominationsModel();
			case LNGScenarioPackage.LNG_SCENARIO_MODEL__LONG_TERM:
				return isLongTerm();
			case LNGScenarioPackage.LNG_SCENARIO_MODEL__ANONYMISED:
				return isAnonymised();
			case LNGScenarioPackage.LNG_SCENARIO_MODEL__SENSITIVITY_MODEL:
				if (resolve) return getSensitivityModel();
				return basicGetSensitivityModel();
			case LNGScenarioPackage.LNG_SCENARIO_MODEL__TRANSFER_MODEL:
				if (resolve) return getTransferModel();
				return basicGetTransferModel();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case LNGScenarioPackage.LNG_SCENARIO_MODEL__CARGO_MODEL:
				setCargoModel((CargoModel)newValue);
				return;
			case LNGScenarioPackage.LNG_SCENARIO_MODEL__SCHEDULE_MODEL:
				setScheduleModel((ScheduleModel)newValue);
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
			case LNGScenarioPackage.LNG_SCENARIO_MODEL__SCHEDULING_END_DATE:
				setSchedulingEndDate((LocalDate)newValue);
				return;
			case LNGScenarioPackage.LNG_SCENARIO_MODEL__REFERENCE_MODEL:
				setReferenceModel((LNGReferenceModel)newValue);
				return;
			case LNGScenarioPackage.LNG_SCENARIO_MODEL__USER_SETTINGS:
				setUserSettings((UserSettings)newValue);
				return;
			case LNGScenarioPackage.LNG_SCENARIO_MODEL__ANALYTICS_MODEL:
				setAnalyticsModel((AnalyticsModel)newValue);
				return;
			case LNGScenarioPackage.LNG_SCENARIO_MODEL__ADP_MODEL:
				setAdpModel((ADPModel)newValue);
				return;
			case LNGScenarioPackage.LNG_SCENARIO_MODEL__NOMINATIONS_MODEL:
				setNominationsModel((NominationsModel)newValue);
				return;
			case LNGScenarioPackage.LNG_SCENARIO_MODEL__LONG_TERM:
				setLongTerm((Boolean)newValue);
				return;
			case LNGScenarioPackage.LNG_SCENARIO_MODEL__ANONYMISED:
				setAnonymised((Boolean)newValue);
				return;
			case LNGScenarioPackage.LNG_SCENARIO_MODEL__SENSITIVITY_MODEL:
				setSensitivityModel((SensitivityModel)newValue);
				return;
			case LNGScenarioPackage.LNG_SCENARIO_MODEL__TRANSFER_MODEL:
				setTransferModel((TransferModel)newValue);
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
			case LNGScenarioPackage.LNG_SCENARIO_MODEL__ACTUALS_MODEL:
				setActualsModel((ActualsModel)null);
				return;
			case LNGScenarioPackage.LNG_SCENARIO_MODEL__PROMPT_PERIOD_START:
				setPromptPeriodStart(PROMPT_PERIOD_START_EDEFAULT);
				return;
			case LNGScenarioPackage.LNG_SCENARIO_MODEL__PROMPT_PERIOD_END:
				setPromptPeriodEnd(PROMPT_PERIOD_END_EDEFAULT);
				return;
			case LNGScenarioPackage.LNG_SCENARIO_MODEL__SCHEDULING_END_DATE:
				unsetSchedulingEndDate();
				return;
			case LNGScenarioPackage.LNG_SCENARIO_MODEL__REFERENCE_MODEL:
				setReferenceModel((LNGReferenceModel)null);
				return;
			case LNGScenarioPackage.LNG_SCENARIO_MODEL__USER_SETTINGS:
				setUserSettings((UserSettings)null);
				return;
			case LNGScenarioPackage.LNG_SCENARIO_MODEL__ANALYTICS_MODEL:
				setAnalyticsModel((AnalyticsModel)null);
				return;
			case LNGScenarioPackage.LNG_SCENARIO_MODEL__ADP_MODEL:
				setAdpModel((ADPModel)null);
				return;
			case LNGScenarioPackage.LNG_SCENARIO_MODEL__NOMINATIONS_MODEL:
				setNominationsModel((NominationsModel)null);
				return;
			case LNGScenarioPackage.LNG_SCENARIO_MODEL__LONG_TERM:
				setLongTerm(LONG_TERM_EDEFAULT);
				return;
			case LNGScenarioPackage.LNG_SCENARIO_MODEL__ANONYMISED:
				setAnonymised(ANONYMISED_EDEFAULT);
				return;
			case LNGScenarioPackage.LNG_SCENARIO_MODEL__SENSITIVITY_MODEL:
				setSensitivityModel((SensitivityModel)null);
				return;
			case LNGScenarioPackage.LNG_SCENARIO_MODEL__TRANSFER_MODEL:
				setTransferModel((TransferModel)null);
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
			case LNGScenarioPackage.LNG_SCENARIO_MODEL__ACTUALS_MODEL:
				return actualsModel != null;
			case LNGScenarioPackage.LNG_SCENARIO_MODEL__PROMPT_PERIOD_START:
				return PROMPT_PERIOD_START_EDEFAULT == null ? promptPeriodStart != null : !PROMPT_PERIOD_START_EDEFAULT.equals(promptPeriodStart);
			case LNGScenarioPackage.LNG_SCENARIO_MODEL__PROMPT_PERIOD_END:
				return PROMPT_PERIOD_END_EDEFAULT == null ? promptPeriodEnd != null : !PROMPT_PERIOD_END_EDEFAULT.equals(promptPeriodEnd);
			case LNGScenarioPackage.LNG_SCENARIO_MODEL__SCHEDULING_END_DATE:
				return isSetSchedulingEndDate();
			case LNGScenarioPackage.LNG_SCENARIO_MODEL__REFERENCE_MODEL:
				return referenceModel != null;
			case LNGScenarioPackage.LNG_SCENARIO_MODEL__USER_SETTINGS:
				return userSettings != null;
			case LNGScenarioPackage.LNG_SCENARIO_MODEL__ANALYTICS_MODEL:
				return analyticsModel != null;
			case LNGScenarioPackage.LNG_SCENARIO_MODEL__ADP_MODEL:
				return adpModel != null;
			case LNGScenarioPackage.LNG_SCENARIO_MODEL__NOMINATIONS_MODEL:
				return nominationsModel != null;
			case LNGScenarioPackage.LNG_SCENARIO_MODEL__LONG_TERM:
				return longTerm != LONG_TERM_EDEFAULT;
			case LNGScenarioPackage.LNG_SCENARIO_MODEL__ANONYMISED:
				return anonymised != ANONYMISED_EDEFAULT;
			case LNGScenarioPackage.LNG_SCENARIO_MODEL__SENSITIVITY_MODEL:
				return sensitivityModel != null;
			case LNGScenarioPackage.LNG_SCENARIO_MODEL__TRANSFER_MODEL:
				return transferModel != null;
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

		StringBuilder result = new StringBuilder(super.toString());
		result.append(" (promptPeriodStart: ");
		result.append(promptPeriodStart);
		result.append(", promptPeriodEnd: ");
		result.append(promptPeriodEnd);
		result.append(", schedulingEndDate: ");
		if (schedulingEndDateESet) result.append(schedulingEndDate); else result.append("<unset>");
		result.append(", longTerm: ");
		result.append(longTerm);
		result.append(", anonymised: ");
		result.append(anonymised);
		result.append(')');
		return result.toString();
	}

} //LNGScenarioModelImpl
