/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.scenario.model.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.parameters.OptimiserSettings;
import com.mmxlabs.models.lng.scenario.model.LNGPortfolioModel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioPackage;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.mmxcore.impl.UUIDObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>LNG Portfolio Model</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.scenario.model.impl.LNGPortfolioModelImpl#getCargoModel <em>Cargo Model</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.scenario.model.impl.LNGPortfolioModelImpl#getScheduleModel <em>Schedule Model</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.scenario.model.impl.LNGPortfolioModelImpl#getParameters <em>Parameters</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.scenario.model.impl.LNGPortfolioModelImpl#getActualsModel <em>Actuals Model</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class LNGPortfolioModelImpl extends UUIDObjectImpl implements LNGPortfolioModel {
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
	protected com.mmxlabs.models.lng.actuals.ActualsModel actualsModel;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected LNGPortfolioModelImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return LNGScenarioPackage.eINSTANCE.getLNGPortfolioModel();
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
				NotificationChain msgs = oldCargoModel.eInverseRemove(this, EOPPOSITE_FEATURE_BASE - LNGScenarioPackage.LNG_PORTFOLIO_MODEL__CARGO_MODEL, null, null);
				if (newCargoModel.eInternalContainer() == null) {
					msgs = newCargoModel.eInverseAdd(this, EOPPOSITE_FEATURE_BASE - LNGScenarioPackage.LNG_PORTFOLIO_MODEL__CARGO_MODEL, null, msgs);
				}
				if (msgs != null) msgs.dispatch();
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, LNGScenarioPackage.LNG_PORTFOLIO_MODEL__CARGO_MODEL, oldCargoModel, cargoModel));
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
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, LNGScenarioPackage.LNG_PORTFOLIO_MODEL__CARGO_MODEL, oldCargoModel, newCargoModel);
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
				msgs = ((InternalEObject)cargoModel).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - LNGScenarioPackage.LNG_PORTFOLIO_MODEL__CARGO_MODEL, null, msgs);
			if (newCargoModel != null)
				msgs = ((InternalEObject)newCargoModel).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - LNGScenarioPackage.LNG_PORTFOLIO_MODEL__CARGO_MODEL, null, msgs);
			msgs = basicSetCargoModel(newCargoModel, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, LNGScenarioPackage.LNG_PORTFOLIO_MODEL__CARGO_MODEL, newCargoModel, newCargoModel));
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
				NotificationChain msgs = oldScheduleModel.eInverseRemove(this, EOPPOSITE_FEATURE_BASE - LNGScenarioPackage.LNG_PORTFOLIO_MODEL__SCHEDULE_MODEL, null, null);
				if (newScheduleModel.eInternalContainer() == null) {
					msgs = newScheduleModel.eInverseAdd(this, EOPPOSITE_FEATURE_BASE - LNGScenarioPackage.LNG_PORTFOLIO_MODEL__SCHEDULE_MODEL, null, msgs);
				}
				if (msgs != null) msgs.dispatch();
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, LNGScenarioPackage.LNG_PORTFOLIO_MODEL__SCHEDULE_MODEL, oldScheduleModel, scheduleModel));
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
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, LNGScenarioPackage.LNG_PORTFOLIO_MODEL__SCHEDULE_MODEL, oldScheduleModel, newScheduleModel);
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
				msgs = ((InternalEObject)scheduleModel).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - LNGScenarioPackage.LNG_PORTFOLIO_MODEL__SCHEDULE_MODEL, null, msgs);
			if (newScheduleModel != null)
				msgs = ((InternalEObject)newScheduleModel).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - LNGScenarioPackage.LNG_PORTFOLIO_MODEL__SCHEDULE_MODEL, null, msgs);
			msgs = basicSetScheduleModel(newScheduleModel, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, LNGScenarioPackage.LNG_PORTFOLIO_MODEL__SCHEDULE_MODEL, newScheduleModel, newScheduleModel));
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
				NotificationChain msgs = oldParameters.eInverseRemove(this, EOPPOSITE_FEATURE_BASE - LNGScenarioPackage.LNG_PORTFOLIO_MODEL__PARAMETERS, null, null);
				if (newParameters.eInternalContainer() == null) {
					msgs = newParameters.eInverseAdd(this, EOPPOSITE_FEATURE_BASE - LNGScenarioPackage.LNG_PORTFOLIO_MODEL__PARAMETERS, null, msgs);
				}
				if (msgs != null) msgs.dispatch();
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, LNGScenarioPackage.LNG_PORTFOLIO_MODEL__PARAMETERS, oldParameters, parameters));
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
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, LNGScenarioPackage.LNG_PORTFOLIO_MODEL__PARAMETERS, oldParameters, newParameters);
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
				msgs = ((InternalEObject)parameters).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - LNGScenarioPackage.LNG_PORTFOLIO_MODEL__PARAMETERS, null, msgs);
			if (newParameters != null)
				msgs = ((InternalEObject)newParameters).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - LNGScenarioPackage.LNG_PORTFOLIO_MODEL__PARAMETERS, null, msgs);
			msgs = basicSetParameters(newParameters, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, LNGScenarioPackage.LNG_PORTFOLIO_MODEL__PARAMETERS, newParameters, newParameters));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public com.mmxlabs.models.lng.actuals.ActualsModel getActualsModel() {
		if (actualsModel != null && actualsModel.eIsProxy()) {
			InternalEObject oldActualsModel = (InternalEObject)actualsModel;
			actualsModel = (com.mmxlabs.models.lng.actuals.ActualsModel)eResolveProxy(oldActualsModel);
			if (actualsModel != oldActualsModel) {
				InternalEObject newActualsModel = (InternalEObject)actualsModel;
				NotificationChain msgs = oldActualsModel.eInverseRemove(this, EOPPOSITE_FEATURE_BASE - LNGScenarioPackage.LNG_PORTFOLIO_MODEL__ACTUALS_MODEL, null, null);
				if (newActualsModel.eInternalContainer() == null) {
					msgs = newActualsModel.eInverseAdd(this, EOPPOSITE_FEATURE_BASE - LNGScenarioPackage.LNG_PORTFOLIO_MODEL__ACTUALS_MODEL, null, msgs);
				}
				if (msgs != null) msgs.dispatch();
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, LNGScenarioPackage.LNG_PORTFOLIO_MODEL__ACTUALS_MODEL, oldActualsModel, actualsModel));
			}
		}
		return actualsModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public com.mmxlabs.models.lng.actuals.ActualsModel basicGetActualsModel() {
		return actualsModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetActualsModel(com.mmxlabs.models.lng.actuals.ActualsModel newActualsModel, NotificationChain msgs) {
		com.mmxlabs.models.lng.actuals.ActualsModel oldActualsModel = actualsModel;
		actualsModel = newActualsModel;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, LNGScenarioPackage.LNG_PORTFOLIO_MODEL__ACTUALS_MODEL, oldActualsModel, newActualsModel);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setActualsModel(com.mmxlabs.models.lng.actuals.ActualsModel newActualsModel) {
		if (newActualsModel != actualsModel) {
			NotificationChain msgs = null;
			if (actualsModel != null)
				msgs = ((InternalEObject)actualsModel).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - LNGScenarioPackage.LNG_PORTFOLIO_MODEL__ACTUALS_MODEL, null, msgs);
			if (newActualsModel != null)
				msgs = ((InternalEObject)newActualsModel).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - LNGScenarioPackage.LNG_PORTFOLIO_MODEL__ACTUALS_MODEL, null, msgs);
			msgs = basicSetActualsModel(newActualsModel, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, LNGScenarioPackage.LNG_PORTFOLIO_MODEL__ACTUALS_MODEL, newActualsModel, newActualsModel));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case LNGScenarioPackage.LNG_PORTFOLIO_MODEL__CARGO_MODEL:
				return basicSetCargoModel(null, msgs);
			case LNGScenarioPackage.LNG_PORTFOLIO_MODEL__SCHEDULE_MODEL:
				return basicSetScheduleModel(null, msgs);
			case LNGScenarioPackage.LNG_PORTFOLIO_MODEL__PARAMETERS:
				return basicSetParameters(null, msgs);
			case LNGScenarioPackage.LNG_PORTFOLIO_MODEL__ACTUALS_MODEL:
				return basicSetActualsModel(null, msgs);
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
			case LNGScenarioPackage.LNG_PORTFOLIO_MODEL__CARGO_MODEL:
				if (resolve) return getCargoModel();
				return basicGetCargoModel();
			case LNGScenarioPackage.LNG_PORTFOLIO_MODEL__SCHEDULE_MODEL:
				if (resolve) return getScheduleModel();
				return basicGetScheduleModel();
			case LNGScenarioPackage.LNG_PORTFOLIO_MODEL__PARAMETERS:
				if (resolve) return getParameters();
				return basicGetParameters();
			case LNGScenarioPackage.LNG_PORTFOLIO_MODEL__ACTUALS_MODEL:
				if (resolve) return getActualsModel();
				return basicGetActualsModel();
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
			case LNGScenarioPackage.LNG_PORTFOLIO_MODEL__CARGO_MODEL:
				setCargoModel((CargoModel)newValue);
				return;
			case LNGScenarioPackage.LNG_PORTFOLIO_MODEL__SCHEDULE_MODEL:
				setScheduleModel((ScheduleModel)newValue);
				return;
			case LNGScenarioPackage.LNG_PORTFOLIO_MODEL__PARAMETERS:
				setParameters((OptimiserSettings)newValue);
				return;
			case LNGScenarioPackage.LNG_PORTFOLIO_MODEL__ACTUALS_MODEL:
				setActualsModel((com.mmxlabs.models.lng.actuals.ActualsModel)newValue);
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
			case LNGScenarioPackage.LNG_PORTFOLIO_MODEL__CARGO_MODEL:
				setCargoModel((CargoModel)null);
				return;
			case LNGScenarioPackage.LNG_PORTFOLIO_MODEL__SCHEDULE_MODEL:
				setScheduleModel((ScheduleModel)null);
				return;
			case LNGScenarioPackage.LNG_PORTFOLIO_MODEL__PARAMETERS:
				setParameters((OptimiserSettings)null);
				return;
			case LNGScenarioPackage.LNG_PORTFOLIO_MODEL__ACTUALS_MODEL:
				setActualsModel((com.mmxlabs.models.lng.actuals.ActualsModel)null);
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
			case LNGScenarioPackage.LNG_PORTFOLIO_MODEL__CARGO_MODEL:
				return cargoModel != null;
			case LNGScenarioPackage.LNG_PORTFOLIO_MODEL__SCHEDULE_MODEL:
				return scheduleModel != null;
			case LNGScenarioPackage.LNG_PORTFOLIO_MODEL__PARAMETERS:
				return parameters != null;
			case LNGScenarioPackage.LNG_PORTFOLIO_MODEL__ACTUALS_MODEL:
				return actualsModel != null;
		}
		return super.eIsSet(featureID);
	}

} //LNGPortfolioModelImpl
