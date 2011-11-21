/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package scenario.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.InternalEList;

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
 *   <li>{@link scenario.impl.ScenarioImpl#getVersion <em>Version</em>}</li>
 *   <li>{@link scenario.impl.ScenarioImpl#getName <em>Name</em>}</li>
 *   <li>{@link scenario.impl.ScenarioImpl#getFleetModel <em>Fleet Model</em>}</li>
 *   <li>{@link scenario.impl.ScenarioImpl#getScheduleModel <em>Schedule Model</em>}</li>
 *   <li>{@link scenario.impl.ScenarioImpl#getPortModel <em>Port Model</em>}</li>
 *   <li>{@link scenario.impl.ScenarioImpl#getDistanceModel <em>Distance Model</em>}</li>
 *   <li>{@link scenario.impl.ScenarioImpl#getCanalModel <em>Canal Model</em>}</li>
 *   <li>{@link scenario.impl.ScenarioImpl#getCargoModel <em>Cargo Model</em>}</li>
 *   <li>{@link scenario.impl.ScenarioImpl#getContractModel <em>Contract Model</em>}</li>
 *   <li>{@link scenario.impl.ScenarioImpl#getMarketModel <em>Market Model</em>}</li>
 *   <li>{@link scenario.impl.ScenarioImpl#getOptimisation <em>Optimisation</em>}</li>
 *   <li>{@link scenario.impl.ScenarioImpl#getContainedModels <em>Contained Models</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ScenarioImpl extends AnnotatedObjectImpl implements Scenario {
	/**
	 * The default value of the '{@link #getVersion() <em>Version</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVersion()
	 * @generated
	 * @ordered
	 */
	protected static final int VERSION_EDEFAULT = 2;

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
	 * The default value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected static final String NAME_EDEFAULT = "Default name";

	/**
	 * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected String name = NAME_EDEFAULT;

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
	 * The cached value of the '{@link #getScheduleModel() <em>Schedule Model</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getScheduleModel()
	 * @generated
	 * @ordered
	 */
	protected ScheduleModel scheduleModel;

	/**
	 * The cached value of the '{@link #getPortModel() <em>Port Model</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPortModel()
	 * @generated
	 * @ordered
	 */
	protected PortModel portModel;

	/**
	 * The cached value of the '{@link #getDistanceModel() <em>Distance Model</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDistanceModel()
	 * @generated
	 * @ordered
	 */
	protected DistanceModel distanceModel;

	/**
	 * The cached value of the '{@link #getCanalModel() <em>Canal Model</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCanalModel()
	 * @generated
	 * @ordered
	 */
	protected CanalModel canalModel;

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
	 * The cached value of the '{@link #getMarketModel() <em>Market Model</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMarketModel()
	 * @generated
	 * @ordered
	 */
	protected MarketModel marketModel;

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
	 * The cached value of the '{@link #getContainedModels() <em>Contained Models</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getContainedModels()
	 * @generated
	 * @ordered
	 */
	protected EList<EObject> containedModels;

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
		if (fleetModel != null && fleetModel.eIsProxy()) {
			InternalEObject oldFleetModel = (InternalEObject)fleetModel;
			fleetModel = (FleetModel)eResolveProxy(oldFleetModel);
			if (fleetModel != oldFleetModel) {
				InternalEObject newFleetModel = (InternalEObject)fleetModel;
				NotificationChain msgs = oldFleetModel.eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ScenarioPackage.SCENARIO__FLEET_MODEL, null, null);
				if (newFleetModel.eInternalContainer() == null) {
					msgs = newFleetModel.eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ScenarioPackage.SCENARIO__FLEET_MODEL, null, msgs);
				}
				if (msgs != null) msgs.dispatch();
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ScenarioPackage.SCENARIO__FLEET_MODEL, oldFleetModel, fleetModel));
			}
		}
		return fleetModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public FleetModel basicGetFleetModel() {
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
		if (portModel != null && portModel.eIsProxy()) {
			InternalEObject oldPortModel = (InternalEObject)portModel;
			portModel = (PortModel)eResolveProxy(oldPortModel);
			if (portModel != oldPortModel) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ScenarioPackage.SCENARIO__PORT_MODEL, oldPortModel, portModel));
			}
		}
		return portModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public PortModel basicGetPortModel() {
		return portModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setPortModel(PortModel newPortModel) {
		PortModel oldPortModel = portModel;
		portModel = newPortModel;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ScenarioPackage.SCENARIO__PORT_MODEL, oldPortModel, portModel));
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
				NotificationChain msgs = oldCargoModel.eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ScenarioPackage.SCENARIO__CARGO_MODEL, null, null);
				if (newCargoModel.eInternalContainer() == null) {
					msgs = newCargoModel.eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ScenarioPackage.SCENARIO__CARGO_MODEL, null, msgs);
				}
				if (msgs != null) msgs.dispatch();
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ScenarioPackage.SCENARIO__CARGO_MODEL, oldCargoModel, cargoModel));
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
		if (contractModel != null && contractModel.eIsProxy()) {
			InternalEObject oldContractModel = (InternalEObject)contractModel;
			contractModel = (ContractModel)eResolveProxy(oldContractModel);
			if (contractModel != oldContractModel) {
				InternalEObject newContractModel = (InternalEObject)contractModel;
				NotificationChain msgs = oldContractModel.eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ScenarioPackage.SCENARIO__CONTRACT_MODEL, null, null);
				if (newContractModel.eInternalContainer() == null) {
					msgs = newContractModel.eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ScenarioPackage.SCENARIO__CONTRACT_MODEL, null, msgs);
				}
				if (msgs != null) msgs.dispatch();
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ScenarioPackage.SCENARIO__CONTRACT_MODEL, oldContractModel, contractModel));
			}
		}
		return contractModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ContractModel basicGetContractModel() {
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
		if (scheduleModel != null && scheduleModel.eIsProxy()) {
			InternalEObject oldScheduleModel = (InternalEObject)scheduleModel;
			scheduleModel = (ScheduleModel)eResolveProxy(oldScheduleModel);
			if (scheduleModel != oldScheduleModel) {
				InternalEObject newScheduleModel = (InternalEObject)scheduleModel;
				NotificationChain msgs = oldScheduleModel.eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ScenarioPackage.SCENARIO__SCHEDULE_MODEL, null, null);
				if (newScheduleModel.eInternalContainer() == null) {
					msgs = newScheduleModel.eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ScenarioPackage.SCENARIO__SCHEDULE_MODEL, null, msgs);
				}
				if (msgs != null) msgs.dispatch();
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ScenarioPackage.SCENARIO__SCHEDULE_MODEL, oldScheduleModel, scheduleModel));
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
	@Override
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
		if (marketModel != null && marketModel.eIsProxy()) {
			InternalEObject oldMarketModel = (InternalEObject)marketModel;
			marketModel = (MarketModel)eResolveProxy(oldMarketModel);
			if (marketModel != oldMarketModel) {
				InternalEObject newMarketModel = (InternalEObject)marketModel;
				NotificationChain msgs = oldMarketModel.eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ScenarioPackage.SCENARIO__MARKET_MODEL, null, null);
				if (newMarketModel.eInternalContainer() == null) {
					msgs = newMarketModel.eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ScenarioPackage.SCENARIO__MARKET_MODEL, null, msgs);
				}
				if (msgs != null) msgs.dispatch();
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ScenarioPackage.SCENARIO__MARKET_MODEL, oldMarketModel, marketModel));
			}
		}
		return marketModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public MarketModel basicGetMarketModel() {
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
	@Override
	public DistanceModel getDistanceModel() {
		if (distanceModel != null && distanceModel.eIsProxy()) {
			InternalEObject oldDistanceModel = (InternalEObject)distanceModel;
			distanceModel = (DistanceModel)eResolveProxy(oldDistanceModel);
			if (distanceModel != oldDistanceModel) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ScenarioPackage.SCENARIO__DISTANCE_MODEL, oldDistanceModel, distanceModel));
			}
		}
		return distanceModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DistanceModel basicGetDistanceModel() {
		return distanceModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setDistanceModel(DistanceModel newDistanceModel) {
		DistanceModel oldDistanceModel = distanceModel;
		distanceModel = newDistanceModel;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ScenarioPackage.SCENARIO__DISTANCE_MODEL, oldDistanceModel, distanceModel));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public CanalModel getCanalModel() {
		if (canalModel != null && canalModel.eIsProxy()) {
			InternalEObject oldCanalModel = (InternalEObject)canalModel;
			canalModel = (CanalModel)eResolveProxy(oldCanalModel);
			if (canalModel != oldCanalModel) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ScenarioPackage.SCENARIO__CANAL_MODEL, oldCanalModel, canalModel));
			}
		}
		return canalModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CanalModel basicGetCanalModel() {
		return canalModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setCanalModel(CanalModel newCanalModel) {
		CanalModel oldCanalModel = canalModel;
		canalModel = newCanalModel;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ScenarioPackage.SCENARIO__CANAL_MODEL, oldCanalModel, canalModel));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Optimisation getOptimisation() {
		if (optimisation != null && optimisation.eIsProxy()) {
			InternalEObject oldOptimisation = (InternalEObject)optimisation;
			optimisation = (Optimisation)eResolveProxy(oldOptimisation);
			if (optimisation != oldOptimisation) {
				InternalEObject newOptimisation = (InternalEObject)optimisation;
				NotificationChain msgs = oldOptimisation.eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ScenarioPackage.SCENARIO__OPTIMISATION, null, null);
				if (newOptimisation.eInternalContainer() == null) {
					msgs = newOptimisation.eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ScenarioPackage.SCENARIO__OPTIMISATION, null, msgs);
				}
				if (msgs != null) msgs.dispatch();
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ScenarioPackage.SCENARIO__OPTIMISATION, oldOptimisation, optimisation));
			}
		}
		return optimisation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Optimisation basicGetOptimisation() {
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
	@Override
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
	@Override
	public EList<EObject> getContainedModels() {
		if (containedModels == null) {
			containedModels = new EObjectContainmentEList.Resolving<EObject>(EObject.class, this, ScenarioPackage.SCENARIO__CONTAINED_MODELS);
		}
		return containedModels;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getVersion() {
		return version;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
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
	public String getName() {
		return name;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setName(String newName) {
		String oldName = name;
		name = newName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ScenarioPackage.SCENARIO__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public FleetModel getOrCreateFleetModel() {
		if (getFleetModel() == null)
					setFleetModel(scenario.fleet.FleetFactory.eINSTANCE.createFleetModel());
				
				return getFleetModel();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ScheduleModel getOrCreateScheduleModel() {
		if (getScheduleModel() == null)
					setScheduleModel(scenario.schedule.ScheduleFactory.eINSTANCE.createScheduleModel());
				return getScheduleModel();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void createMissingModels() {
		if (getPortModel() == null) {
			setPortModel(scenario.port.PortFactory.eINSTANCE.createPortModel());
			getContainedModels().add(getPortModel());
		}
		
		if (getDistanceModel() == null) {
			setDistanceModel(scenario.port.PortFactory.eINSTANCE.createDistanceModel());
			getContainedModels().add(getDistanceModel());
		}
		
		if (getCanalModel() == null) {
			setCanalModel(scenario.port.PortFactory.eINSTANCE.createCanalModel());
			getContainedModels().add(getCanalModel());
		}
		
		if (getFleetModel() == null)
			setFleetModel(scenario.fleet.FleetFactory.eINSTANCE.createFleetModel());
		
		if (getCargoModel() == null)
			setCargoModel(scenario.cargo.CargoFactory.eINSTANCE.createCargoModel());
		
		if (getContractModel() == null)
			setContractModel(scenario.contract.ContractFactory.eINSTANCE.createContractModel());
		
		if (getScheduleModel() == null)
			setScheduleModel(scenario.schedule.ScheduleFactory.eINSTANCE.createScheduleModel());
		
		if (getMarketModel() == null)
			setMarketModel(scenario.market.MarketFactory.eINSTANCE.createMarketModel());
		
		if (getOptimisation() == null)
					setOptimisation(scenario.optimiser.OptimiserFactory.eINSTANCE.createOptimisation());
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
			case ScenarioPackage.SCENARIO__SCHEDULE_MODEL:
				return basicSetScheduleModel(null, msgs);
			case ScenarioPackage.SCENARIO__CARGO_MODEL:
				return basicSetCargoModel(null, msgs);
			case ScenarioPackage.SCENARIO__CONTRACT_MODEL:
				return basicSetContractModel(null, msgs);
			case ScenarioPackage.SCENARIO__MARKET_MODEL:
				return basicSetMarketModel(null, msgs);
			case ScenarioPackage.SCENARIO__OPTIMISATION:
				return basicSetOptimisation(null, msgs);
			case ScenarioPackage.SCENARIO__CONTAINED_MODELS:
				return ((InternalEList<?>)getContainedModels()).basicRemove(otherEnd, msgs);
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
			case ScenarioPackage.SCENARIO__VERSION:
				return getVersion();
			case ScenarioPackage.SCENARIO__NAME:
				return getName();
			case ScenarioPackage.SCENARIO__FLEET_MODEL:
				if (resolve) return getFleetModel();
				return basicGetFleetModel();
			case ScenarioPackage.SCENARIO__SCHEDULE_MODEL:
				if (resolve) return getScheduleModel();
				return basicGetScheduleModel();
			case ScenarioPackage.SCENARIO__PORT_MODEL:
				if (resolve) return getPortModel();
				return basicGetPortModel();
			case ScenarioPackage.SCENARIO__DISTANCE_MODEL:
				if (resolve) return getDistanceModel();
				return basicGetDistanceModel();
			case ScenarioPackage.SCENARIO__CANAL_MODEL:
				if (resolve) return getCanalModel();
				return basicGetCanalModel();
			case ScenarioPackage.SCENARIO__CARGO_MODEL:
				if (resolve) return getCargoModel();
				return basicGetCargoModel();
			case ScenarioPackage.SCENARIO__CONTRACT_MODEL:
				if (resolve) return getContractModel();
				return basicGetContractModel();
			case ScenarioPackage.SCENARIO__MARKET_MODEL:
				if (resolve) return getMarketModel();
				return basicGetMarketModel();
			case ScenarioPackage.SCENARIO__OPTIMISATION:
				if (resolve) return getOptimisation();
				return basicGetOptimisation();
			case ScenarioPackage.SCENARIO__CONTAINED_MODELS:
				return getContainedModels();
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
			case ScenarioPackage.SCENARIO__VERSION:
				setVersion((Integer)newValue);
				return;
			case ScenarioPackage.SCENARIO__NAME:
				setName((String)newValue);
				return;
			case ScenarioPackage.SCENARIO__FLEET_MODEL:
				setFleetModel((FleetModel)newValue);
				return;
			case ScenarioPackage.SCENARIO__SCHEDULE_MODEL:
				setScheduleModel((ScheduleModel)newValue);
				return;
			case ScenarioPackage.SCENARIO__PORT_MODEL:
				setPortModel((PortModel)newValue);
				return;
			case ScenarioPackage.SCENARIO__DISTANCE_MODEL:
				setDistanceModel((DistanceModel)newValue);
				return;
			case ScenarioPackage.SCENARIO__CANAL_MODEL:
				setCanalModel((CanalModel)newValue);
				return;
			case ScenarioPackage.SCENARIO__CARGO_MODEL:
				setCargoModel((CargoModel)newValue);
				return;
			case ScenarioPackage.SCENARIO__CONTRACT_MODEL:
				setContractModel((ContractModel)newValue);
				return;
			case ScenarioPackage.SCENARIO__MARKET_MODEL:
				setMarketModel((MarketModel)newValue);
				return;
			case ScenarioPackage.SCENARIO__OPTIMISATION:
				setOptimisation((Optimisation)newValue);
				return;
			case ScenarioPackage.SCENARIO__CONTAINED_MODELS:
				getContainedModels().clear();
				getContainedModels().addAll((Collection<? extends EObject>)newValue);
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
			case ScenarioPackage.SCENARIO__VERSION:
				setVersion(VERSION_EDEFAULT);
				return;
			case ScenarioPackage.SCENARIO__NAME:
				setName(NAME_EDEFAULT);
				return;
			case ScenarioPackage.SCENARIO__FLEET_MODEL:
				setFleetModel((FleetModel)null);
				return;
			case ScenarioPackage.SCENARIO__SCHEDULE_MODEL:
				setScheduleModel((ScheduleModel)null);
				return;
			case ScenarioPackage.SCENARIO__PORT_MODEL:
				setPortModel((PortModel)null);
				return;
			case ScenarioPackage.SCENARIO__DISTANCE_MODEL:
				setDistanceModel((DistanceModel)null);
				return;
			case ScenarioPackage.SCENARIO__CANAL_MODEL:
				setCanalModel((CanalModel)null);
				return;
			case ScenarioPackage.SCENARIO__CARGO_MODEL:
				setCargoModel((CargoModel)null);
				return;
			case ScenarioPackage.SCENARIO__CONTRACT_MODEL:
				setContractModel((ContractModel)null);
				return;
			case ScenarioPackage.SCENARIO__MARKET_MODEL:
				setMarketModel((MarketModel)null);
				return;
			case ScenarioPackage.SCENARIO__OPTIMISATION:
				setOptimisation((Optimisation)null);
				return;
			case ScenarioPackage.SCENARIO__CONTAINED_MODELS:
				getContainedModels().clear();
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
			case ScenarioPackage.SCENARIO__VERSION:
				return version != VERSION_EDEFAULT;
			case ScenarioPackage.SCENARIO__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case ScenarioPackage.SCENARIO__FLEET_MODEL:
				return fleetModel != null;
			case ScenarioPackage.SCENARIO__SCHEDULE_MODEL:
				return scheduleModel != null;
			case ScenarioPackage.SCENARIO__PORT_MODEL:
				return portModel != null;
			case ScenarioPackage.SCENARIO__DISTANCE_MODEL:
				return distanceModel != null;
			case ScenarioPackage.SCENARIO__CANAL_MODEL:
				return canalModel != null;
			case ScenarioPackage.SCENARIO__CARGO_MODEL:
				return cargoModel != null;
			case ScenarioPackage.SCENARIO__CONTRACT_MODEL:
				return contractModel != null;
			case ScenarioPackage.SCENARIO__MARKET_MODEL:
				return marketModel != null;
			case ScenarioPackage.SCENARIO__OPTIMISATION:
				return optimisation != null;
			case ScenarioPackage.SCENARIO__CONTAINED_MODELS:
				return containedModels != null && !containedModels.isEmpty();
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eInvoke(int operationID, EList<?> arguments) throws InvocationTargetException {
		switch (operationID) {
			case ScenarioPackage.SCENARIO___CREATE_MISSING_MODELS:
				createMissingModels();
				return null;
			case ScenarioPackage.SCENARIO___GET_OR_CREATE_FLEET_MODEL:
				return getOrCreateFleetModel();
			case ScenarioPackage.SCENARIO___GET_OR_CREATE_SCHEDULE_MODEL:
				return getOrCreateScheduleModel();
		}
		return super.eInvoke(operationID, arguments);
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
		result.append(", name: ");
		result.append(name);
		result.append(')');
		return result.toString();
	}



	@Override
	public Scenario getSelfContainedCopy() {
		final Scenario scenario = this;
		final List<EObject> toCopy = new ArrayList<EObject>();
		toCopy.add(scenario);
		for (final EReference reference : scenario.eClass().getEAllReferences()) {
			if (reference.isContainment() == false) {
				if (reference.isMany()) {
					final EList<EObject> values = (EList<EObject>) scenario.eGet(reference);
					for (final EObject value : values) {
						if (value != null && value.eContainer() != scenario)
							toCopy.add(value);
					}
				} else {
					final EObject value = (EObject) scenario.eGet(reference);
					if (value != null && value.eContainer() != scenario)
						toCopy.add(value);
				}
			}
		}

		final Collection<EObject> copies = EcoreUtil.copyAll(toCopy);
		Scenario copy = null;
		for (final EObject object : copies) {
			if (object instanceof Scenario)
				copy = (Scenario) object;
			break;
		}
		if (copy != null) {
			for (final EObject object : copies) {
				if (!(object instanceof Scenario)) {
					if (copy.getContainedModels().contains(object) == false)
						copy.getContainedModels().add(object);
				}
			}
		}
		return copy;
	}
} //ScenarioImpl
