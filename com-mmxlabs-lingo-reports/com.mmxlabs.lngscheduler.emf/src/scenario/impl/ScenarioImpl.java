/**
 * Copyright (C) Minimax Labs Ltd., 2010
 * All rights reserved.
 */

package scenario.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import scenario.Scenario;
import scenario.ScenarioPackage;
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
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Scenario</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link scenario.impl.ScenarioImpl#getFleetModel <em>Fleet Model</em>}</li>
 *   <li>{@link scenario.impl.ScenarioImpl#getPortModel <em>Port Model</em>}</li>
 *   <li>{@link scenario.impl.ScenarioImpl#getCargoModel <em>Cargo Model</em>}</li>
 *   <li>{@link scenario.impl.ScenarioImpl#getContractModel <em>Contract Model</em>}</li>
 *   <li>{@link scenario.impl.ScenarioImpl#getScheduleModel <em>Schedule Model</em>}</li>
 *   <li>{@link scenario.impl.ScenarioImpl#getMarketModel <em>Market Model</em>}</li>
 *   <li>{@link scenario.impl.ScenarioImpl#getDistanceModel <em>Distance Model</em>}</li>
 *   <li>{@link scenario.impl.ScenarioImpl#getCanalModel <em>Canal Model</em>}</li>
 *   <li>{@link scenario.impl.ScenarioImpl#getOptimisation <em>Optimisation</em>}</li>
 *   <li>{@link scenario.impl.ScenarioImpl#getVersion <em>Version</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ScenarioImpl extends EObjectImpl implements Scenario {
	/**
	 * The cached value of the '{@link #getFleetModel() <em>Fleet Model</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFleetModel()
	 * @generated
	 * @ordered
	 */
	protected FleetModel fleetModel;

	/**
	 * The cached value of the '{@link #getPortModel() <em>Port Model</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPortModel()
	 * @generated
	 * @ordered
	 */
	protected PortModel portModel;

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
	 * The cached value of the '{@link #getContractModel() <em>Contract Model</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getContractModel()
	 * @generated
	 * @ordered
	 */
	protected ContractModel contractModel;

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
	 * The cached value of the '{@link #getMarketModel() <em>Market Model</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMarketModel()
	 * @generated
	 * @ordered
	 */
	protected MarketModel marketModel;

	/**
	 * The cached value of the '{@link #getDistanceModel() <em>Distance Model</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDistanceModel()
	 * @generated
	 * @ordered
	 */
	protected DistanceModel distanceModel;

	/**
	 * The cached value of the '{@link #getCanalModel() <em>Canal Model</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCanalModel()
	 * @generated
	 * @ordered
	 */
	protected CanalModel canalModel;

	/**
	 * The cached value of the '{@link #getOptimisation() <em>Optimisation</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOptimisation()
	 * @generated
	 * @ordered
	 */
	protected Optimisation optimisation;

	/**
	 * The default value of the '{@link #getVersion() <em>Version</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVersion()
	 * @generated
	 * @ordered
	 */
	protected static final int VERSION_EDEFAULT = 1;

	/**
	 * The cached value of the '{@link #getVersion() <em>Version</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVersion()
	 * @generated
	 * @ordered
	 */
	protected int version = VERSION_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ScenarioImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ScenarioPackage.Literals.SCENARIO;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public FleetModel getFleetModel() {
		return fleetModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetFleetModel(FleetModel newFleetModel, NotificationChain msgs) {
		FleetModel oldFleetModel = fleetModel;
		fleetModel = newFleetModel;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ScenarioPackage.SCENARIO__FLEET_MODEL, oldFleetModel, newFleetModel);
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
	public void setFleetModel(FleetModel newFleetModel) {
		if (newFleetModel != fleetModel) {
			NotificationChain msgs = null;
			if (fleetModel != null)
				msgs = ((InternalEObject)fleetModel).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ScenarioPackage.SCENARIO__FLEET_MODEL, null, msgs);
			if (newFleetModel != null)
				msgs = ((InternalEObject)newFleetModel).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ScenarioPackage.SCENARIO__FLEET_MODEL, null, msgs);
			msgs = basicSetFleetModel(newFleetModel, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ScenarioPackage.SCENARIO__FLEET_MODEL, newFleetModel, newFleetModel));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public PortModel getPortModel() {
		return portModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetPortModel(PortModel newPortModel, NotificationChain msgs) {
		PortModel oldPortModel = portModel;
		portModel = newPortModel;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ScenarioPackage.SCENARIO__PORT_MODEL, oldPortModel, newPortModel);
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
	public void setPortModel(PortModel newPortModel) {
		if (newPortModel != portModel) {
			NotificationChain msgs = null;
			if (portModel != null)
				msgs = ((InternalEObject)portModel).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ScenarioPackage.SCENARIO__PORT_MODEL, null, msgs);
			if (newPortModel != null)
				msgs = ((InternalEObject)newPortModel).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ScenarioPackage.SCENARIO__PORT_MODEL, null, msgs);
			msgs = basicSetPortModel(newPortModel, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ScenarioPackage.SCENARIO__PORT_MODEL, newPortModel, newPortModel));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public CargoModel getCargoModel() {
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
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ScenarioPackage.SCENARIO__CARGO_MODEL, oldCargoModel, newCargoModel);
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
				msgs = ((InternalEObject)cargoModel).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ScenarioPackage.SCENARIO__CARGO_MODEL, null, msgs);
			if (newCargoModel != null)
				msgs = ((InternalEObject)newCargoModel).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ScenarioPackage.SCENARIO__CARGO_MODEL, null, msgs);
			msgs = basicSetCargoModel(newCargoModel, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ScenarioPackage.SCENARIO__CARGO_MODEL, newCargoModel, newCargoModel));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ContractModel getContractModel() {
		return contractModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetContractModel(ContractModel newContractModel, NotificationChain msgs) {
		ContractModel oldContractModel = contractModel;
		contractModel = newContractModel;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ScenarioPackage.SCENARIO__CONTRACT_MODEL, oldContractModel, newContractModel);
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
	public void setContractModel(ContractModel newContractModel) {
		if (newContractModel != contractModel) {
			NotificationChain msgs = null;
			if (contractModel != null)
				msgs = ((InternalEObject)contractModel).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ScenarioPackage.SCENARIO__CONTRACT_MODEL, null, msgs);
			if (newContractModel != null)
				msgs = ((InternalEObject)newContractModel).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ScenarioPackage.SCENARIO__CONTRACT_MODEL, null, msgs);
			msgs = basicSetContractModel(newContractModel, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ScenarioPackage.SCENARIO__CONTRACT_MODEL, newContractModel, newContractModel));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ScheduleModel getScheduleModel() {
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
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ScenarioPackage.SCENARIO__SCHEDULE_MODEL, oldScheduleModel, newScheduleModel);
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
				msgs = ((InternalEObject)scheduleModel).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ScenarioPackage.SCENARIO__SCHEDULE_MODEL, null, msgs);
			if (newScheduleModel != null)
				msgs = ((InternalEObject)newScheduleModel).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ScenarioPackage.SCENARIO__SCHEDULE_MODEL, null, msgs);
			msgs = basicSetScheduleModel(newScheduleModel, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ScenarioPackage.SCENARIO__SCHEDULE_MODEL, newScheduleModel, newScheduleModel));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public MarketModel getMarketModel() {
		return marketModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetMarketModel(MarketModel newMarketModel, NotificationChain msgs) {
		MarketModel oldMarketModel = marketModel;
		marketModel = newMarketModel;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ScenarioPackage.SCENARIO__MARKET_MODEL, oldMarketModel, newMarketModel);
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
	public void setMarketModel(MarketModel newMarketModel) {
		if (newMarketModel != marketModel) {
			NotificationChain msgs = null;
			if (marketModel != null)
				msgs = ((InternalEObject)marketModel).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ScenarioPackage.SCENARIO__MARKET_MODEL, null, msgs);
			if (newMarketModel != null)
				msgs = ((InternalEObject)newMarketModel).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ScenarioPackage.SCENARIO__MARKET_MODEL, null, msgs);
			msgs = basicSetMarketModel(newMarketModel, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ScenarioPackage.SCENARIO__MARKET_MODEL, newMarketModel, newMarketModel));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DistanceModel getDistanceModel() {
		return distanceModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetDistanceModel(DistanceModel newDistanceModel, NotificationChain msgs) {
		DistanceModel oldDistanceModel = distanceModel;
		distanceModel = newDistanceModel;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ScenarioPackage.SCENARIO__DISTANCE_MODEL, oldDistanceModel, newDistanceModel);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDistanceModel(DistanceModel newDistanceModel) {
		if (newDistanceModel != distanceModel) {
			NotificationChain msgs = null;
			if (distanceModel != null)
				msgs = ((InternalEObject)distanceModel).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ScenarioPackage.SCENARIO__DISTANCE_MODEL, null, msgs);
			if (newDistanceModel != null)
				msgs = ((InternalEObject)newDistanceModel).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ScenarioPackage.SCENARIO__DISTANCE_MODEL, null, msgs);
			msgs = basicSetDistanceModel(newDistanceModel, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ScenarioPackage.SCENARIO__DISTANCE_MODEL, newDistanceModel, newDistanceModel));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CanalModel getCanalModel() {
		return canalModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetCanalModel(CanalModel newCanalModel, NotificationChain msgs) {
		CanalModel oldCanalModel = canalModel;
		canalModel = newCanalModel;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ScenarioPackage.SCENARIO__CANAL_MODEL, oldCanalModel, newCanalModel);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setCanalModel(CanalModel newCanalModel) {
		if (newCanalModel != canalModel) {
			NotificationChain msgs = null;
			if (canalModel != null)
				msgs = ((InternalEObject)canalModel).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ScenarioPackage.SCENARIO__CANAL_MODEL, null, msgs);
			if (newCanalModel != null)
				msgs = ((InternalEObject)newCanalModel).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ScenarioPackage.SCENARIO__CANAL_MODEL, null, msgs);
			msgs = basicSetCanalModel(newCanalModel, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ScenarioPackage.SCENARIO__CANAL_MODEL, newCanalModel, newCanalModel));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Optimisation getOptimisation() {
		return optimisation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetOptimisation(Optimisation newOptimisation, NotificationChain msgs) {
		Optimisation oldOptimisation = optimisation;
		optimisation = newOptimisation;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ScenarioPackage.SCENARIO__OPTIMISATION, oldOptimisation, newOptimisation);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setOptimisation(Optimisation newOptimisation) {
		if (newOptimisation != optimisation) {
			NotificationChain msgs = null;
			if (optimisation != null)
				msgs = ((InternalEObject)optimisation).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ScenarioPackage.SCENARIO__OPTIMISATION, null, msgs);
			if (newOptimisation != null)
				msgs = ((InternalEObject)newOptimisation).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ScenarioPackage.SCENARIO__OPTIMISATION, null, msgs);
			msgs = basicSetOptimisation(newOptimisation, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ScenarioPackage.SCENARIO__OPTIMISATION, newOptimisation, newOptimisation));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getVersion() {
		return version;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setVersion(int newVersion) {
		int oldVersion = version;
		version = newVersion;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ScenarioPackage.SCENARIO__VERSION, oldVersion, version));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case ScenarioPackage.SCENARIO__FLEET_MODEL:
				return basicSetFleetModel(null, msgs);
			case ScenarioPackage.SCENARIO__PORT_MODEL:
				return basicSetPortModel(null, msgs);
			case ScenarioPackage.SCENARIO__CARGO_MODEL:
				return basicSetCargoModel(null, msgs);
			case ScenarioPackage.SCENARIO__CONTRACT_MODEL:
				return basicSetContractModel(null, msgs);
			case ScenarioPackage.SCENARIO__SCHEDULE_MODEL:
				return basicSetScheduleModel(null, msgs);
			case ScenarioPackage.SCENARIO__MARKET_MODEL:
				return basicSetMarketModel(null, msgs);
			case ScenarioPackage.SCENARIO__DISTANCE_MODEL:
				return basicSetDistanceModel(null, msgs);
			case ScenarioPackage.SCENARIO__CANAL_MODEL:
				return basicSetCanalModel(null, msgs);
			case ScenarioPackage.SCENARIO__OPTIMISATION:
				return basicSetOptimisation(null, msgs);
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
			case ScenarioPackage.SCENARIO__FLEET_MODEL:
				return getFleetModel();
			case ScenarioPackage.SCENARIO__PORT_MODEL:
				return getPortModel();
			case ScenarioPackage.SCENARIO__CARGO_MODEL:
				return getCargoModel();
			case ScenarioPackage.SCENARIO__CONTRACT_MODEL:
				return getContractModel();
			case ScenarioPackage.SCENARIO__SCHEDULE_MODEL:
				return getScheduleModel();
			case ScenarioPackage.SCENARIO__MARKET_MODEL:
				return getMarketModel();
			case ScenarioPackage.SCENARIO__DISTANCE_MODEL:
				return getDistanceModel();
			case ScenarioPackage.SCENARIO__CANAL_MODEL:
				return getCanalModel();
			case ScenarioPackage.SCENARIO__OPTIMISATION:
				return getOptimisation();
			case ScenarioPackage.SCENARIO__VERSION:
				return getVersion();
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
			case ScenarioPackage.SCENARIO__FLEET_MODEL:
				setFleetModel((FleetModel)newValue);
				return;
			case ScenarioPackage.SCENARIO__PORT_MODEL:
				setPortModel((PortModel)newValue);
				return;
			case ScenarioPackage.SCENARIO__CARGO_MODEL:
				setCargoModel((CargoModel)newValue);
				return;
			case ScenarioPackage.SCENARIO__CONTRACT_MODEL:
				setContractModel((ContractModel)newValue);
				return;
			case ScenarioPackage.SCENARIO__SCHEDULE_MODEL:
				setScheduleModel((ScheduleModel)newValue);
				return;
			case ScenarioPackage.SCENARIO__MARKET_MODEL:
				setMarketModel((MarketModel)newValue);
				return;
			case ScenarioPackage.SCENARIO__DISTANCE_MODEL:
				setDistanceModel((DistanceModel)newValue);
				return;
			case ScenarioPackage.SCENARIO__CANAL_MODEL:
				setCanalModel((CanalModel)newValue);
				return;
			case ScenarioPackage.SCENARIO__OPTIMISATION:
				setOptimisation((Optimisation)newValue);
				return;
			case ScenarioPackage.SCENARIO__VERSION:
				setVersion((Integer)newValue);
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
			case ScenarioPackage.SCENARIO__FLEET_MODEL:
				setFleetModel((FleetModel)null);
				return;
			case ScenarioPackage.SCENARIO__PORT_MODEL:
				setPortModel((PortModel)null);
				return;
			case ScenarioPackage.SCENARIO__CARGO_MODEL:
				setCargoModel((CargoModel)null);
				return;
			case ScenarioPackage.SCENARIO__CONTRACT_MODEL:
				setContractModel((ContractModel)null);
				return;
			case ScenarioPackage.SCENARIO__SCHEDULE_MODEL:
				setScheduleModel((ScheduleModel)null);
				return;
			case ScenarioPackage.SCENARIO__MARKET_MODEL:
				setMarketModel((MarketModel)null);
				return;
			case ScenarioPackage.SCENARIO__DISTANCE_MODEL:
				setDistanceModel((DistanceModel)null);
				return;
			case ScenarioPackage.SCENARIO__CANAL_MODEL:
				setCanalModel((CanalModel)null);
				return;
			case ScenarioPackage.SCENARIO__OPTIMISATION:
				setOptimisation((Optimisation)null);
				return;
			case ScenarioPackage.SCENARIO__VERSION:
				setVersion(VERSION_EDEFAULT);
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
			case ScenarioPackage.SCENARIO__FLEET_MODEL:
				return fleetModel != null;
			case ScenarioPackage.SCENARIO__PORT_MODEL:
				return portModel != null;
			case ScenarioPackage.SCENARIO__CARGO_MODEL:
				return cargoModel != null;
			case ScenarioPackage.SCENARIO__CONTRACT_MODEL:
				return contractModel != null;
			case ScenarioPackage.SCENARIO__SCHEDULE_MODEL:
				return scheduleModel != null;
			case ScenarioPackage.SCENARIO__MARKET_MODEL:
				return marketModel != null;
			case ScenarioPackage.SCENARIO__DISTANCE_MODEL:
				return distanceModel != null;
			case ScenarioPackage.SCENARIO__CANAL_MODEL:
				return canalModel != null;
			case ScenarioPackage.SCENARIO__OPTIMISATION:
				return optimisation != null;
			case ScenarioPackage.SCENARIO__VERSION:
				return version != VERSION_EDEFAULT;
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
		result.append(" (version: ");
		result.append(version);
		result.append(')');
		return result.toString();
	}

} //ScenarioImpl
