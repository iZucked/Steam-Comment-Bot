/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
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

import com.mmxlabs.models.lng.analytics.AnalyticsModel;
import com.mmxlabs.models.lng.commercial.CommercialModel;
import com.mmxlabs.models.lng.fleet.FleetModel;
import com.mmxlabs.models.lng.parameters.ParametersModel;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.scenario.model.LNGPortfolioModel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioModel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioPackage;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsModel;
import com.mmxlabs.models.mmxcore.impl.MMXRootObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Model</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.scenario.model.impl.LNGScenarioModelImpl#getPortModel <em>Port Model</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.scenario.model.impl.LNGScenarioModelImpl#getFleetModel <em>Fleet Model</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.scenario.model.impl.LNGScenarioModelImpl#getPricingModel <em>Pricing Model</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.scenario.model.impl.LNGScenarioModelImpl#getCommercialModel <em>Commercial Model</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.scenario.model.impl.LNGScenarioModelImpl#getSpotMarketsModel <em>Spot Markets Model</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.scenario.model.impl.LNGScenarioModelImpl#getParametersModel <em>Parameters Model</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.scenario.model.impl.LNGScenarioModelImpl#getAnalyticsModel <em>Analytics Model</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.scenario.model.impl.LNGScenarioModelImpl#getPortfolioModel <em>Portfolio Model</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class LNGScenarioModelImpl extends MMXRootObjectImpl implements LNGScenarioModel {
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
	 * The cached value of the '{@link #getFleetModel() <em>Fleet Model</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFleetModel()
	 * @generated
	 * @ordered
	 */
	protected FleetModel fleetModel;

	/**
	 * The cached value of the '{@link #getPricingModel() <em>Pricing Model</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPricingModel()
	 * @generated
	 * @ordered
	 */
	protected PricingModel pricingModel;

	/**
	 * The cached value of the '{@link #getCommercialModel() <em>Commercial Model</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCommercialModel()
	 * @generated
	 * @ordered
	 */
	protected CommercialModel commercialModel;

	/**
	 * The cached value of the '{@link #getSpotMarketsModel() <em>Spot Markets Model</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSpotMarketsModel()
	 * @generated
	 * @ordered
	 */
	protected SpotMarketsModel spotMarketsModel;

	/**
	 * The cached value of the '{@link #getParametersModel() <em>Parameters Model</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getParametersModel()
	 * @generated
	 * @ordered
	 */
	protected ParametersModel parametersModel;

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
	 * The cached value of the '{@link #getPortfolioModel() <em>Portfolio Model</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPortfolioModel()
	 * @generated
	 * @ordered
	 */
	protected LNGPortfolioModel portfolioModel;

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
	public PortModel getPortModel() {
		if (portModel != null && portModel.eIsProxy()) {
			InternalEObject oldPortModel = (InternalEObject)portModel;
			portModel = (PortModel)eResolveProxy(oldPortModel);
			if (portModel != oldPortModel) {
				InternalEObject newPortModel = (InternalEObject)portModel;
				NotificationChain msgs = oldPortModel.eInverseRemove(this, EOPPOSITE_FEATURE_BASE - LNGScenarioPackage.LNG_SCENARIO_MODEL__PORT_MODEL, null, null);
				if (newPortModel.eInternalContainer() == null) {
					msgs = newPortModel.eInverseAdd(this, EOPPOSITE_FEATURE_BASE - LNGScenarioPackage.LNG_SCENARIO_MODEL__PORT_MODEL, null, msgs);
				}
				if (msgs != null) msgs.dispatch();
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, LNGScenarioPackage.LNG_SCENARIO_MODEL__PORT_MODEL, oldPortModel, portModel));
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
	public NotificationChain basicSetPortModel(PortModel newPortModel, NotificationChain msgs) {
		PortModel oldPortModel = portModel;
		portModel = newPortModel;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, LNGScenarioPackage.LNG_SCENARIO_MODEL__PORT_MODEL, oldPortModel, newPortModel);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setPortModel(PortModel newPortModel) {
		if (newPortModel != portModel) {
			NotificationChain msgs = null;
			if (portModel != null)
				msgs = ((InternalEObject)portModel).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - LNGScenarioPackage.LNG_SCENARIO_MODEL__PORT_MODEL, null, msgs);
			if (newPortModel != null)
				msgs = ((InternalEObject)newPortModel).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - LNGScenarioPackage.LNG_SCENARIO_MODEL__PORT_MODEL, null, msgs);
			msgs = basicSetPortModel(newPortModel, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, LNGScenarioPackage.LNG_SCENARIO_MODEL__PORT_MODEL, newPortModel, newPortModel));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public FleetModel getFleetModel() {
		if (fleetModel != null && fleetModel.eIsProxy()) {
			InternalEObject oldFleetModel = (InternalEObject)fleetModel;
			fleetModel = (FleetModel)eResolveProxy(oldFleetModel);
			if (fleetModel != oldFleetModel) {
				InternalEObject newFleetModel = (InternalEObject)fleetModel;
				NotificationChain msgs = oldFleetModel.eInverseRemove(this, EOPPOSITE_FEATURE_BASE - LNGScenarioPackage.LNG_SCENARIO_MODEL__FLEET_MODEL, null, null);
				if (newFleetModel.eInternalContainer() == null) {
					msgs = newFleetModel.eInverseAdd(this, EOPPOSITE_FEATURE_BASE - LNGScenarioPackage.LNG_SCENARIO_MODEL__FLEET_MODEL, null, msgs);
				}
				if (msgs != null) msgs.dispatch();
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, LNGScenarioPackage.LNG_SCENARIO_MODEL__FLEET_MODEL, oldFleetModel, fleetModel));
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
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, LNGScenarioPackage.LNG_SCENARIO_MODEL__FLEET_MODEL, oldFleetModel, newFleetModel);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setFleetModel(FleetModel newFleetModel) {
		if (newFleetModel != fleetModel) {
			NotificationChain msgs = null;
			if (fleetModel != null)
				msgs = ((InternalEObject)fleetModel).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - LNGScenarioPackage.LNG_SCENARIO_MODEL__FLEET_MODEL, null, msgs);
			if (newFleetModel != null)
				msgs = ((InternalEObject)newFleetModel).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - LNGScenarioPackage.LNG_SCENARIO_MODEL__FLEET_MODEL, null, msgs);
			msgs = basicSetFleetModel(newFleetModel, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, LNGScenarioPackage.LNG_SCENARIO_MODEL__FLEET_MODEL, newFleetModel, newFleetModel));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public PricingModel getPricingModel() {
		if (pricingModel != null && pricingModel.eIsProxy()) {
			InternalEObject oldPricingModel = (InternalEObject)pricingModel;
			pricingModel = (PricingModel)eResolveProxy(oldPricingModel);
			if (pricingModel != oldPricingModel) {
				InternalEObject newPricingModel = (InternalEObject)pricingModel;
				NotificationChain msgs = oldPricingModel.eInverseRemove(this, EOPPOSITE_FEATURE_BASE - LNGScenarioPackage.LNG_SCENARIO_MODEL__PRICING_MODEL, null, null);
				if (newPricingModel.eInternalContainer() == null) {
					msgs = newPricingModel.eInverseAdd(this, EOPPOSITE_FEATURE_BASE - LNGScenarioPackage.LNG_SCENARIO_MODEL__PRICING_MODEL, null, msgs);
				}
				if (msgs != null) msgs.dispatch();
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, LNGScenarioPackage.LNG_SCENARIO_MODEL__PRICING_MODEL, oldPricingModel, pricingModel));
			}
		}
		return pricingModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public PricingModel basicGetPricingModel() {
		return pricingModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetPricingModel(PricingModel newPricingModel, NotificationChain msgs) {
		PricingModel oldPricingModel = pricingModel;
		pricingModel = newPricingModel;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, LNGScenarioPackage.LNG_SCENARIO_MODEL__PRICING_MODEL, oldPricingModel, newPricingModel);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setPricingModel(PricingModel newPricingModel) {
		if (newPricingModel != pricingModel) {
			NotificationChain msgs = null;
			if (pricingModel != null)
				msgs = ((InternalEObject)pricingModel).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - LNGScenarioPackage.LNG_SCENARIO_MODEL__PRICING_MODEL, null, msgs);
			if (newPricingModel != null)
				msgs = ((InternalEObject)newPricingModel).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - LNGScenarioPackage.LNG_SCENARIO_MODEL__PRICING_MODEL, null, msgs);
			msgs = basicSetPricingModel(newPricingModel, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, LNGScenarioPackage.LNG_SCENARIO_MODEL__PRICING_MODEL, newPricingModel, newPricingModel));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CommercialModel getCommercialModel() {
		if (commercialModel != null && commercialModel.eIsProxy()) {
			InternalEObject oldCommercialModel = (InternalEObject)commercialModel;
			commercialModel = (CommercialModel)eResolveProxy(oldCommercialModel);
			if (commercialModel != oldCommercialModel) {
				InternalEObject newCommercialModel = (InternalEObject)commercialModel;
				NotificationChain msgs = oldCommercialModel.eInverseRemove(this, EOPPOSITE_FEATURE_BASE - LNGScenarioPackage.LNG_SCENARIO_MODEL__COMMERCIAL_MODEL, null, null);
				if (newCommercialModel.eInternalContainer() == null) {
					msgs = newCommercialModel.eInverseAdd(this, EOPPOSITE_FEATURE_BASE - LNGScenarioPackage.LNG_SCENARIO_MODEL__COMMERCIAL_MODEL, null, msgs);
				}
				if (msgs != null) msgs.dispatch();
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, LNGScenarioPackage.LNG_SCENARIO_MODEL__COMMERCIAL_MODEL, oldCommercialModel, commercialModel));
			}
		}
		return commercialModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CommercialModel basicGetCommercialModel() {
		return commercialModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetCommercialModel(CommercialModel newCommercialModel, NotificationChain msgs) {
		CommercialModel oldCommercialModel = commercialModel;
		commercialModel = newCommercialModel;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, LNGScenarioPackage.LNG_SCENARIO_MODEL__COMMERCIAL_MODEL, oldCommercialModel, newCommercialModel);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setCommercialModel(CommercialModel newCommercialModel) {
		if (newCommercialModel != commercialModel) {
			NotificationChain msgs = null;
			if (commercialModel != null)
				msgs = ((InternalEObject)commercialModel).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - LNGScenarioPackage.LNG_SCENARIO_MODEL__COMMERCIAL_MODEL, null, msgs);
			if (newCommercialModel != null)
				msgs = ((InternalEObject)newCommercialModel).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - LNGScenarioPackage.LNG_SCENARIO_MODEL__COMMERCIAL_MODEL, null, msgs);
			msgs = basicSetCommercialModel(newCommercialModel, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, LNGScenarioPackage.LNG_SCENARIO_MODEL__COMMERCIAL_MODEL, newCommercialModel, newCommercialModel));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SpotMarketsModel getSpotMarketsModel() {
		if (spotMarketsModel != null && spotMarketsModel.eIsProxy()) {
			InternalEObject oldSpotMarketsModel = (InternalEObject)spotMarketsModel;
			spotMarketsModel = (SpotMarketsModel)eResolveProxy(oldSpotMarketsModel);
			if (spotMarketsModel != oldSpotMarketsModel) {
				InternalEObject newSpotMarketsModel = (InternalEObject)spotMarketsModel;
				NotificationChain msgs = oldSpotMarketsModel.eInverseRemove(this, EOPPOSITE_FEATURE_BASE - LNGScenarioPackage.LNG_SCENARIO_MODEL__SPOT_MARKETS_MODEL, null, null);
				if (newSpotMarketsModel.eInternalContainer() == null) {
					msgs = newSpotMarketsModel.eInverseAdd(this, EOPPOSITE_FEATURE_BASE - LNGScenarioPackage.LNG_SCENARIO_MODEL__SPOT_MARKETS_MODEL, null, msgs);
				}
				if (msgs != null) msgs.dispatch();
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, LNGScenarioPackage.LNG_SCENARIO_MODEL__SPOT_MARKETS_MODEL, oldSpotMarketsModel, spotMarketsModel));
			}
		}
		return spotMarketsModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SpotMarketsModel basicGetSpotMarketsModel() {
		return spotMarketsModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetSpotMarketsModel(SpotMarketsModel newSpotMarketsModel, NotificationChain msgs) {
		SpotMarketsModel oldSpotMarketsModel = spotMarketsModel;
		spotMarketsModel = newSpotMarketsModel;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, LNGScenarioPackage.LNG_SCENARIO_MODEL__SPOT_MARKETS_MODEL, oldSpotMarketsModel, newSpotMarketsModel);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSpotMarketsModel(SpotMarketsModel newSpotMarketsModel) {
		if (newSpotMarketsModel != spotMarketsModel) {
			NotificationChain msgs = null;
			if (spotMarketsModel != null)
				msgs = ((InternalEObject)spotMarketsModel).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - LNGScenarioPackage.LNG_SCENARIO_MODEL__SPOT_MARKETS_MODEL, null, msgs);
			if (newSpotMarketsModel != null)
				msgs = ((InternalEObject)newSpotMarketsModel).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - LNGScenarioPackage.LNG_SCENARIO_MODEL__SPOT_MARKETS_MODEL, null, msgs);
			msgs = basicSetSpotMarketsModel(newSpotMarketsModel, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, LNGScenarioPackage.LNG_SCENARIO_MODEL__SPOT_MARKETS_MODEL, newSpotMarketsModel, newSpotMarketsModel));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ParametersModel getParametersModel() {
		if (parametersModel != null && parametersModel.eIsProxy()) {
			InternalEObject oldParametersModel = (InternalEObject)parametersModel;
			parametersModel = (ParametersModel)eResolveProxy(oldParametersModel);
			if (parametersModel != oldParametersModel) {
				InternalEObject newParametersModel = (InternalEObject)parametersModel;
				NotificationChain msgs = oldParametersModel.eInverseRemove(this, EOPPOSITE_FEATURE_BASE - LNGScenarioPackage.LNG_SCENARIO_MODEL__PARAMETERS_MODEL, null, null);
				if (newParametersModel.eInternalContainer() == null) {
					msgs = newParametersModel.eInverseAdd(this, EOPPOSITE_FEATURE_BASE - LNGScenarioPackage.LNG_SCENARIO_MODEL__PARAMETERS_MODEL, null, msgs);
				}
				if (msgs != null) msgs.dispatch();
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, LNGScenarioPackage.LNG_SCENARIO_MODEL__PARAMETERS_MODEL, oldParametersModel, parametersModel));
			}
		}
		return parametersModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ParametersModel basicGetParametersModel() {
		return parametersModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetParametersModel(ParametersModel newParametersModel, NotificationChain msgs) {
		ParametersModel oldParametersModel = parametersModel;
		parametersModel = newParametersModel;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, LNGScenarioPackage.LNG_SCENARIO_MODEL__PARAMETERS_MODEL, oldParametersModel, newParametersModel);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setParametersModel(ParametersModel newParametersModel) {
		if (newParametersModel != parametersModel) {
			NotificationChain msgs = null;
			if (parametersModel != null)
				msgs = ((InternalEObject)parametersModel).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - LNGScenarioPackage.LNG_SCENARIO_MODEL__PARAMETERS_MODEL, null, msgs);
			if (newParametersModel != null)
				msgs = ((InternalEObject)newParametersModel).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - LNGScenarioPackage.LNG_SCENARIO_MODEL__PARAMETERS_MODEL, null, msgs);
			msgs = basicSetParametersModel(newParametersModel, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, LNGScenarioPackage.LNG_SCENARIO_MODEL__PARAMETERS_MODEL, newParametersModel, newParametersModel));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
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
	public LNGPortfolioModel getPortfolioModel() {
		if (portfolioModel != null && portfolioModel.eIsProxy()) {
			InternalEObject oldPortfolioModel = (InternalEObject)portfolioModel;
			portfolioModel = (LNGPortfolioModel)eResolveProxy(oldPortfolioModel);
			if (portfolioModel != oldPortfolioModel) {
				InternalEObject newPortfolioModel = (InternalEObject)portfolioModel;
				NotificationChain msgs = oldPortfolioModel.eInverseRemove(this, EOPPOSITE_FEATURE_BASE - LNGScenarioPackage.LNG_SCENARIO_MODEL__PORTFOLIO_MODEL, null, null);
				if (newPortfolioModel.eInternalContainer() == null) {
					msgs = newPortfolioModel.eInverseAdd(this, EOPPOSITE_FEATURE_BASE - LNGScenarioPackage.LNG_SCENARIO_MODEL__PORTFOLIO_MODEL, null, msgs);
				}
				if (msgs != null) msgs.dispatch();
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, LNGScenarioPackage.LNG_SCENARIO_MODEL__PORTFOLIO_MODEL, oldPortfolioModel, portfolioModel));
			}
		}
		return portfolioModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public LNGPortfolioModel basicGetPortfolioModel() {
		return portfolioModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetPortfolioModel(LNGPortfolioModel newPortfolioModel, NotificationChain msgs) {
		LNGPortfolioModel oldPortfolioModel = portfolioModel;
		portfolioModel = newPortfolioModel;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, LNGScenarioPackage.LNG_SCENARIO_MODEL__PORTFOLIO_MODEL, oldPortfolioModel, newPortfolioModel);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setPortfolioModel(LNGPortfolioModel newPortfolioModel) {
		if (newPortfolioModel != portfolioModel) {
			NotificationChain msgs = null;
			if (portfolioModel != null)
				msgs = ((InternalEObject)portfolioModel).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - LNGScenarioPackage.LNG_SCENARIO_MODEL__PORTFOLIO_MODEL, null, msgs);
			if (newPortfolioModel != null)
				msgs = ((InternalEObject)newPortfolioModel).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - LNGScenarioPackage.LNG_SCENARIO_MODEL__PORTFOLIO_MODEL, null, msgs);
			msgs = basicSetPortfolioModel(newPortfolioModel, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, LNGScenarioPackage.LNG_SCENARIO_MODEL__PORTFOLIO_MODEL, newPortfolioModel, newPortfolioModel));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case LNGScenarioPackage.LNG_SCENARIO_MODEL__PORT_MODEL:
				return basicSetPortModel(null, msgs);
			case LNGScenarioPackage.LNG_SCENARIO_MODEL__FLEET_MODEL:
				return basicSetFleetModel(null, msgs);
			case LNGScenarioPackage.LNG_SCENARIO_MODEL__PRICING_MODEL:
				return basicSetPricingModel(null, msgs);
			case LNGScenarioPackage.LNG_SCENARIO_MODEL__COMMERCIAL_MODEL:
				return basicSetCommercialModel(null, msgs);
			case LNGScenarioPackage.LNG_SCENARIO_MODEL__SPOT_MARKETS_MODEL:
				return basicSetSpotMarketsModel(null, msgs);
			case LNGScenarioPackage.LNG_SCENARIO_MODEL__PARAMETERS_MODEL:
				return basicSetParametersModel(null, msgs);
			case LNGScenarioPackage.LNG_SCENARIO_MODEL__ANALYTICS_MODEL:
				return basicSetAnalyticsModel(null, msgs);
			case LNGScenarioPackage.LNG_SCENARIO_MODEL__PORTFOLIO_MODEL:
				return basicSetPortfolioModel(null, msgs);
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
			case LNGScenarioPackage.LNG_SCENARIO_MODEL__PORT_MODEL:
				if (resolve) return getPortModel();
				return basicGetPortModel();
			case LNGScenarioPackage.LNG_SCENARIO_MODEL__FLEET_MODEL:
				if (resolve) return getFleetModel();
				return basicGetFleetModel();
			case LNGScenarioPackage.LNG_SCENARIO_MODEL__PRICING_MODEL:
				if (resolve) return getPricingModel();
				return basicGetPricingModel();
			case LNGScenarioPackage.LNG_SCENARIO_MODEL__COMMERCIAL_MODEL:
				if (resolve) return getCommercialModel();
				return basicGetCommercialModel();
			case LNGScenarioPackage.LNG_SCENARIO_MODEL__SPOT_MARKETS_MODEL:
				if (resolve) return getSpotMarketsModel();
				return basicGetSpotMarketsModel();
			case LNGScenarioPackage.LNG_SCENARIO_MODEL__PARAMETERS_MODEL:
				if (resolve) return getParametersModel();
				return basicGetParametersModel();
			case LNGScenarioPackage.LNG_SCENARIO_MODEL__ANALYTICS_MODEL:
				if (resolve) return getAnalyticsModel();
				return basicGetAnalyticsModel();
			case LNGScenarioPackage.LNG_SCENARIO_MODEL__PORTFOLIO_MODEL:
				if (resolve) return getPortfolioModel();
				return basicGetPortfolioModel();
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
			case LNGScenarioPackage.LNG_SCENARIO_MODEL__PORT_MODEL:
				setPortModel((PortModel)newValue);
				return;
			case LNGScenarioPackage.LNG_SCENARIO_MODEL__FLEET_MODEL:
				setFleetModel((FleetModel)newValue);
				return;
			case LNGScenarioPackage.LNG_SCENARIO_MODEL__PRICING_MODEL:
				setPricingModel((PricingModel)newValue);
				return;
			case LNGScenarioPackage.LNG_SCENARIO_MODEL__COMMERCIAL_MODEL:
				setCommercialModel((CommercialModel)newValue);
				return;
			case LNGScenarioPackage.LNG_SCENARIO_MODEL__SPOT_MARKETS_MODEL:
				setSpotMarketsModel((SpotMarketsModel)newValue);
				return;
			case LNGScenarioPackage.LNG_SCENARIO_MODEL__PARAMETERS_MODEL:
				setParametersModel((ParametersModel)newValue);
				return;
			case LNGScenarioPackage.LNG_SCENARIO_MODEL__ANALYTICS_MODEL:
				setAnalyticsModel((AnalyticsModel)newValue);
				return;
			case LNGScenarioPackage.LNG_SCENARIO_MODEL__PORTFOLIO_MODEL:
				setPortfolioModel((LNGPortfolioModel)newValue);
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
			case LNGScenarioPackage.LNG_SCENARIO_MODEL__PORT_MODEL:
				setPortModel((PortModel)null);
				return;
			case LNGScenarioPackage.LNG_SCENARIO_MODEL__FLEET_MODEL:
				setFleetModel((FleetModel)null);
				return;
			case LNGScenarioPackage.LNG_SCENARIO_MODEL__PRICING_MODEL:
				setPricingModel((PricingModel)null);
				return;
			case LNGScenarioPackage.LNG_SCENARIO_MODEL__COMMERCIAL_MODEL:
				setCommercialModel((CommercialModel)null);
				return;
			case LNGScenarioPackage.LNG_SCENARIO_MODEL__SPOT_MARKETS_MODEL:
				setSpotMarketsModel((SpotMarketsModel)null);
				return;
			case LNGScenarioPackage.LNG_SCENARIO_MODEL__PARAMETERS_MODEL:
				setParametersModel((ParametersModel)null);
				return;
			case LNGScenarioPackage.LNG_SCENARIO_MODEL__ANALYTICS_MODEL:
				setAnalyticsModel((AnalyticsModel)null);
				return;
			case LNGScenarioPackage.LNG_SCENARIO_MODEL__PORTFOLIO_MODEL:
				setPortfolioModel((LNGPortfolioModel)null);
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
			case LNGScenarioPackage.LNG_SCENARIO_MODEL__PORT_MODEL:
				return portModel != null;
			case LNGScenarioPackage.LNG_SCENARIO_MODEL__FLEET_MODEL:
				return fleetModel != null;
			case LNGScenarioPackage.LNG_SCENARIO_MODEL__PRICING_MODEL:
				return pricingModel != null;
			case LNGScenarioPackage.LNG_SCENARIO_MODEL__COMMERCIAL_MODEL:
				return commercialModel != null;
			case LNGScenarioPackage.LNG_SCENARIO_MODEL__SPOT_MARKETS_MODEL:
				return spotMarketsModel != null;
			case LNGScenarioPackage.LNG_SCENARIO_MODEL__PARAMETERS_MODEL:
				return parametersModel != null;
			case LNGScenarioPackage.LNG_SCENARIO_MODEL__ANALYTICS_MODEL:
				return analyticsModel != null;
			case LNGScenarioPackage.LNG_SCENARIO_MODEL__PORTFOLIO_MODEL:
				return portfolioModel != null;
		}
		return super.eIsSet(featureID);
	}

} //LNGScenarioModelImpl
