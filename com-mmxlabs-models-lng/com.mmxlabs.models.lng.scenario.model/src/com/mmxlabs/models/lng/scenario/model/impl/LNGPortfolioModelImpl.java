/**
 */
package com.mmxlabs.models.lng.scenario.model.impl;

import com.mmxlabs.models.lng.assignment.AssignmentModel;

import com.mmxlabs.models.lng.cargo.CargoModel;

import com.mmxlabs.models.lng.fleet.ScenarioFleetModel;

import com.mmxlabs.models.lng.scenario.model.LNGPortfolioModel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioPackage;

import com.mmxlabs.models.lng.schedule.ScheduleModel;

import com.mmxlabs.models.mmxcore.impl.UUIDObjectImpl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>LNG Portfolio Model</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.scenario.model.impl.LNGPortfolioModelImpl#getScenarioFleetModel <em>Scenario Fleet Model</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.scenario.model.impl.LNGPortfolioModelImpl#getCargoModel <em>Cargo Model</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.scenario.model.impl.LNGPortfolioModelImpl#getAssignmentModel <em>Assignment Model</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.scenario.model.impl.LNGPortfolioModelImpl#getScheduleModel <em>Schedule Model</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class LNGPortfolioModelImpl extends UUIDObjectImpl implements LNGPortfolioModel {
	/**
	 * The cached value of the '{@link #getScenarioFleetModel() <em>Scenario Fleet Model</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getScenarioFleetModel()
	 * @generated
	 * @ordered
	 */
	protected ScenarioFleetModel scenarioFleetModel;

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
	 * The cached value of the '{@link #getAssignmentModel() <em>Assignment Model</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAssignmentModel()
	 * @generated
	 * @ordered
	 */
	protected AssignmentModel assignmentModel;

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
	public ScenarioFleetModel getScenarioFleetModel() {
		if (scenarioFleetModel != null && scenarioFleetModel.eIsProxy()) {
			InternalEObject oldScenarioFleetModel = (InternalEObject)scenarioFleetModel;
			scenarioFleetModel = (ScenarioFleetModel)eResolveProxy(oldScenarioFleetModel);
			if (scenarioFleetModel != oldScenarioFleetModel) {
				InternalEObject newScenarioFleetModel = (InternalEObject)scenarioFleetModel;
				NotificationChain msgs = oldScenarioFleetModel.eInverseRemove(this, EOPPOSITE_FEATURE_BASE - LNGScenarioPackage.LNG_PORTFOLIO_MODEL__SCENARIO_FLEET_MODEL, null, null);
				if (newScenarioFleetModel.eInternalContainer() == null) {
					msgs = newScenarioFleetModel.eInverseAdd(this, EOPPOSITE_FEATURE_BASE - LNGScenarioPackage.LNG_PORTFOLIO_MODEL__SCENARIO_FLEET_MODEL, null, msgs);
				}
				if (msgs != null) msgs.dispatch();
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, LNGScenarioPackage.LNG_PORTFOLIO_MODEL__SCENARIO_FLEET_MODEL, oldScenarioFleetModel, scenarioFleetModel));
			}
		}
		return scenarioFleetModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ScenarioFleetModel basicGetScenarioFleetModel() {
		return scenarioFleetModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetScenarioFleetModel(ScenarioFleetModel newScenarioFleetModel, NotificationChain msgs) {
		ScenarioFleetModel oldScenarioFleetModel = scenarioFleetModel;
		scenarioFleetModel = newScenarioFleetModel;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, LNGScenarioPackage.LNG_PORTFOLIO_MODEL__SCENARIO_FLEET_MODEL, oldScenarioFleetModel, newScenarioFleetModel);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setScenarioFleetModel(ScenarioFleetModel newScenarioFleetModel) {
		if (newScenarioFleetModel != scenarioFleetModel) {
			NotificationChain msgs = null;
			if (scenarioFleetModel != null)
				msgs = ((InternalEObject)scenarioFleetModel).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - LNGScenarioPackage.LNG_PORTFOLIO_MODEL__SCENARIO_FLEET_MODEL, null, msgs);
			if (newScenarioFleetModel != null)
				msgs = ((InternalEObject)newScenarioFleetModel).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - LNGScenarioPackage.LNG_PORTFOLIO_MODEL__SCENARIO_FLEET_MODEL, null, msgs);
			msgs = basicSetScenarioFleetModel(newScenarioFleetModel, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, LNGScenarioPackage.LNG_PORTFOLIO_MODEL__SCENARIO_FLEET_MODEL, newScenarioFleetModel, newScenarioFleetModel));
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
	public AssignmentModel getAssignmentModel() {
		if (assignmentModel != null && assignmentModel.eIsProxy()) {
			InternalEObject oldAssignmentModel = (InternalEObject)assignmentModel;
			assignmentModel = (AssignmentModel)eResolveProxy(oldAssignmentModel);
			if (assignmentModel != oldAssignmentModel) {
				InternalEObject newAssignmentModel = (InternalEObject)assignmentModel;
				NotificationChain msgs = oldAssignmentModel.eInverseRemove(this, EOPPOSITE_FEATURE_BASE - LNGScenarioPackage.LNG_PORTFOLIO_MODEL__ASSIGNMENT_MODEL, null, null);
				if (newAssignmentModel.eInternalContainer() == null) {
					msgs = newAssignmentModel.eInverseAdd(this, EOPPOSITE_FEATURE_BASE - LNGScenarioPackage.LNG_PORTFOLIO_MODEL__ASSIGNMENT_MODEL, null, msgs);
				}
				if (msgs != null) msgs.dispatch();
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, LNGScenarioPackage.LNG_PORTFOLIO_MODEL__ASSIGNMENT_MODEL, oldAssignmentModel, assignmentModel));
			}
		}
		return assignmentModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AssignmentModel basicGetAssignmentModel() {
		return assignmentModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetAssignmentModel(AssignmentModel newAssignmentModel, NotificationChain msgs) {
		AssignmentModel oldAssignmentModel = assignmentModel;
		assignmentModel = newAssignmentModel;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, LNGScenarioPackage.LNG_PORTFOLIO_MODEL__ASSIGNMENT_MODEL, oldAssignmentModel, newAssignmentModel);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setAssignmentModel(AssignmentModel newAssignmentModel) {
		if (newAssignmentModel != assignmentModel) {
			NotificationChain msgs = null;
			if (assignmentModel != null)
				msgs = ((InternalEObject)assignmentModel).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - LNGScenarioPackage.LNG_PORTFOLIO_MODEL__ASSIGNMENT_MODEL, null, msgs);
			if (newAssignmentModel != null)
				msgs = ((InternalEObject)newAssignmentModel).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - LNGScenarioPackage.LNG_PORTFOLIO_MODEL__ASSIGNMENT_MODEL, null, msgs);
			msgs = basicSetAssignmentModel(newAssignmentModel, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, LNGScenarioPackage.LNG_PORTFOLIO_MODEL__ASSIGNMENT_MODEL, newAssignmentModel, newAssignmentModel));
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
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case LNGScenarioPackage.LNG_PORTFOLIO_MODEL__SCENARIO_FLEET_MODEL:
				return basicSetScenarioFleetModel(null, msgs);
			case LNGScenarioPackage.LNG_PORTFOLIO_MODEL__CARGO_MODEL:
				return basicSetCargoModel(null, msgs);
			case LNGScenarioPackage.LNG_PORTFOLIO_MODEL__ASSIGNMENT_MODEL:
				return basicSetAssignmentModel(null, msgs);
			case LNGScenarioPackage.LNG_PORTFOLIO_MODEL__SCHEDULE_MODEL:
				return basicSetScheduleModel(null, msgs);
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
			case LNGScenarioPackage.LNG_PORTFOLIO_MODEL__SCENARIO_FLEET_MODEL:
				if (resolve) return getScenarioFleetModel();
				return basicGetScenarioFleetModel();
			case LNGScenarioPackage.LNG_PORTFOLIO_MODEL__CARGO_MODEL:
				if (resolve) return getCargoModel();
				return basicGetCargoModel();
			case LNGScenarioPackage.LNG_PORTFOLIO_MODEL__ASSIGNMENT_MODEL:
				if (resolve) return getAssignmentModel();
				return basicGetAssignmentModel();
			case LNGScenarioPackage.LNG_PORTFOLIO_MODEL__SCHEDULE_MODEL:
				if (resolve) return getScheduleModel();
				return basicGetScheduleModel();
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
			case LNGScenarioPackage.LNG_PORTFOLIO_MODEL__SCENARIO_FLEET_MODEL:
				setScenarioFleetModel((ScenarioFleetModel)newValue);
				return;
			case LNGScenarioPackage.LNG_PORTFOLIO_MODEL__CARGO_MODEL:
				setCargoModel((CargoModel)newValue);
				return;
			case LNGScenarioPackage.LNG_PORTFOLIO_MODEL__ASSIGNMENT_MODEL:
				setAssignmentModel((AssignmentModel)newValue);
				return;
			case LNGScenarioPackage.LNG_PORTFOLIO_MODEL__SCHEDULE_MODEL:
				setScheduleModel((ScheduleModel)newValue);
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
			case LNGScenarioPackage.LNG_PORTFOLIO_MODEL__SCENARIO_FLEET_MODEL:
				setScenarioFleetModel((ScenarioFleetModel)null);
				return;
			case LNGScenarioPackage.LNG_PORTFOLIO_MODEL__CARGO_MODEL:
				setCargoModel((CargoModel)null);
				return;
			case LNGScenarioPackage.LNG_PORTFOLIO_MODEL__ASSIGNMENT_MODEL:
				setAssignmentModel((AssignmentModel)null);
				return;
			case LNGScenarioPackage.LNG_PORTFOLIO_MODEL__SCHEDULE_MODEL:
				setScheduleModel((ScheduleModel)null);
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
			case LNGScenarioPackage.LNG_PORTFOLIO_MODEL__SCENARIO_FLEET_MODEL:
				return scenarioFleetModel != null;
			case LNGScenarioPackage.LNG_PORTFOLIO_MODEL__CARGO_MODEL:
				return cargoModel != null;
			case LNGScenarioPackage.LNG_PORTFOLIO_MODEL__ASSIGNMENT_MODEL:
				return assignmentModel != null;
			case LNGScenarioPackage.LNG_PORTFOLIO_MODEL__SCHEDULE_MODEL:
				return scheduleModel != null;
		}
		return super.eIsSet(featureID);
	}

} //LNGPortfolioModelImpl
