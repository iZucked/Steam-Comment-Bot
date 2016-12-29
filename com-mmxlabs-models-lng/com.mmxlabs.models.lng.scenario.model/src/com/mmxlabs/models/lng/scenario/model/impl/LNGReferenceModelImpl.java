/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
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
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.pricing.CostModel;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.scenario.model.LNGReferenceModel;
import com.mmxlabs.models.lng.scenario.model.LNGScenarioPackage;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsModel;
import com.mmxlabs.models.mmxcore.impl.UUIDObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>LNG Reference Model</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.scenario.model.impl.LNGReferenceModelImpl#getPortModel <em>Port Model</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.scenario.model.impl.LNGReferenceModelImpl#getFleetModel <em>Fleet Model</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.scenario.model.impl.LNGReferenceModelImpl#getPricingModel <em>Pricing Model</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.scenario.model.impl.LNGReferenceModelImpl#getCommercialModel <em>Commercial Model</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.scenario.model.impl.LNGReferenceModelImpl#getSpotMarketsModel <em>Spot Markets Model</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.scenario.model.impl.LNGReferenceModelImpl#getCostModel <em>Cost Model</em>}</li>
 * </ul>
 *
 * @generated
 */
public class LNGReferenceModelImpl extends UUIDObjectImpl implements LNGReferenceModel {
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
	 * The cached value of the '{@link #getCostModel() <em>Cost Model</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCostModel()
	 * @generated
	 * @ordered
	 */
	protected CostModel costModel;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected LNGReferenceModelImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return LNGScenarioPackage.eINSTANCE.getLNGReferenceModel();
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
				NotificationChain msgs = oldPortModel.eInverseRemove(this, EOPPOSITE_FEATURE_BASE - LNGScenarioPackage.LNG_REFERENCE_MODEL__PORT_MODEL, null, null);
				if (newPortModel.eInternalContainer() == null) {
					msgs = newPortModel.eInverseAdd(this, EOPPOSITE_FEATURE_BASE - LNGScenarioPackage.LNG_REFERENCE_MODEL__PORT_MODEL, null, msgs);
				}
				if (msgs != null) msgs.dispatch();
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, LNGScenarioPackage.LNG_REFERENCE_MODEL__PORT_MODEL, oldPortModel, portModel));
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
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, LNGScenarioPackage.LNG_REFERENCE_MODEL__PORT_MODEL, oldPortModel, newPortModel);
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
				msgs = ((InternalEObject)portModel).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - LNGScenarioPackage.LNG_REFERENCE_MODEL__PORT_MODEL, null, msgs);
			if (newPortModel != null)
				msgs = ((InternalEObject)newPortModel).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - LNGScenarioPackage.LNG_REFERENCE_MODEL__PORT_MODEL, null, msgs);
			msgs = basicSetPortModel(newPortModel, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, LNGScenarioPackage.LNG_REFERENCE_MODEL__PORT_MODEL, newPortModel, newPortModel));
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
				NotificationChain msgs = oldFleetModel.eInverseRemove(this, EOPPOSITE_FEATURE_BASE - LNGScenarioPackage.LNG_REFERENCE_MODEL__FLEET_MODEL, null, null);
				if (newFleetModel.eInternalContainer() == null) {
					msgs = newFleetModel.eInverseAdd(this, EOPPOSITE_FEATURE_BASE - LNGScenarioPackage.LNG_REFERENCE_MODEL__FLEET_MODEL, null, msgs);
				}
				if (msgs != null) msgs.dispatch();
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, LNGScenarioPackage.LNG_REFERENCE_MODEL__FLEET_MODEL, oldFleetModel, fleetModel));
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
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, LNGScenarioPackage.LNG_REFERENCE_MODEL__FLEET_MODEL, oldFleetModel, newFleetModel);
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
				msgs = ((InternalEObject)fleetModel).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - LNGScenarioPackage.LNG_REFERENCE_MODEL__FLEET_MODEL, null, msgs);
			if (newFleetModel != null)
				msgs = ((InternalEObject)newFleetModel).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - LNGScenarioPackage.LNG_REFERENCE_MODEL__FLEET_MODEL, null, msgs);
			msgs = basicSetFleetModel(newFleetModel, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, LNGScenarioPackage.LNG_REFERENCE_MODEL__FLEET_MODEL, newFleetModel, newFleetModel));
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
				NotificationChain msgs = oldPricingModel.eInverseRemove(this, EOPPOSITE_FEATURE_BASE - LNGScenarioPackage.LNG_REFERENCE_MODEL__PRICING_MODEL, null, null);
				if (newPricingModel.eInternalContainer() == null) {
					msgs = newPricingModel.eInverseAdd(this, EOPPOSITE_FEATURE_BASE - LNGScenarioPackage.LNG_REFERENCE_MODEL__PRICING_MODEL, null, msgs);
				}
				if (msgs != null) msgs.dispatch();
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, LNGScenarioPackage.LNG_REFERENCE_MODEL__PRICING_MODEL, oldPricingModel, pricingModel));
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
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, LNGScenarioPackage.LNG_REFERENCE_MODEL__PRICING_MODEL, oldPricingModel, newPricingModel);
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
				msgs = ((InternalEObject)pricingModel).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - LNGScenarioPackage.LNG_REFERENCE_MODEL__PRICING_MODEL, null, msgs);
			if (newPricingModel != null)
				msgs = ((InternalEObject)newPricingModel).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - LNGScenarioPackage.LNG_REFERENCE_MODEL__PRICING_MODEL, null, msgs);
			msgs = basicSetPricingModel(newPricingModel, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, LNGScenarioPackage.LNG_REFERENCE_MODEL__PRICING_MODEL, newPricingModel, newPricingModel));
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
				NotificationChain msgs = oldCommercialModel.eInverseRemove(this, EOPPOSITE_FEATURE_BASE - LNGScenarioPackage.LNG_REFERENCE_MODEL__COMMERCIAL_MODEL, null, null);
				if (newCommercialModel.eInternalContainer() == null) {
					msgs = newCommercialModel.eInverseAdd(this, EOPPOSITE_FEATURE_BASE - LNGScenarioPackage.LNG_REFERENCE_MODEL__COMMERCIAL_MODEL, null, msgs);
				}
				if (msgs != null) msgs.dispatch();
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, LNGScenarioPackage.LNG_REFERENCE_MODEL__COMMERCIAL_MODEL, oldCommercialModel, commercialModel));
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
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, LNGScenarioPackage.LNG_REFERENCE_MODEL__COMMERCIAL_MODEL, oldCommercialModel, newCommercialModel);
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
				msgs = ((InternalEObject)commercialModel).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - LNGScenarioPackage.LNG_REFERENCE_MODEL__COMMERCIAL_MODEL, null, msgs);
			if (newCommercialModel != null)
				msgs = ((InternalEObject)newCommercialModel).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - LNGScenarioPackage.LNG_REFERENCE_MODEL__COMMERCIAL_MODEL, null, msgs);
			msgs = basicSetCommercialModel(newCommercialModel, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, LNGScenarioPackage.LNG_REFERENCE_MODEL__COMMERCIAL_MODEL, newCommercialModel, newCommercialModel));
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
				NotificationChain msgs = oldSpotMarketsModel.eInverseRemove(this, EOPPOSITE_FEATURE_BASE - LNGScenarioPackage.LNG_REFERENCE_MODEL__SPOT_MARKETS_MODEL, null, null);
				if (newSpotMarketsModel.eInternalContainer() == null) {
					msgs = newSpotMarketsModel.eInverseAdd(this, EOPPOSITE_FEATURE_BASE - LNGScenarioPackage.LNG_REFERENCE_MODEL__SPOT_MARKETS_MODEL, null, msgs);
				}
				if (msgs != null) msgs.dispatch();
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, LNGScenarioPackage.LNG_REFERENCE_MODEL__SPOT_MARKETS_MODEL, oldSpotMarketsModel, spotMarketsModel));
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
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, LNGScenarioPackage.LNG_REFERENCE_MODEL__SPOT_MARKETS_MODEL, oldSpotMarketsModel, newSpotMarketsModel);
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
				msgs = ((InternalEObject)spotMarketsModel).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - LNGScenarioPackage.LNG_REFERENCE_MODEL__SPOT_MARKETS_MODEL, null, msgs);
			if (newSpotMarketsModel != null)
				msgs = ((InternalEObject)newSpotMarketsModel).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - LNGScenarioPackage.LNG_REFERENCE_MODEL__SPOT_MARKETS_MODEL, null, msgs);
			msgs = basicSetSpotMarketsModel(newSpotMarketsModel, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, LNGScenarioPackage.LNG_REFERENCE_MODEL__SPOT_MARKETS_MODEL, newSpotMarketsModel, newSpotMarketsModel));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CostModel getCostModel() {
		if (costModel != null && costModel.eIsProxy()) {
			InternalEObject oldCostModel = (InternalEObject)costModel;
			costModel = (CostModel)eResolveProxy(oldCostModel);
			if (costModel != oldCostModel) {
				InternalEObject newCostModel = (InternalEObject)costModel;
				NotificationChain msgs = oldCostModel.eInverseRemove(this, EOPPOSITE_FEATURE_BASE - LNGScenarioPackage.LNG_REFERENCE_MODEL__COST_MODEL, null, null);
				if (newCostModel.eInternalContainer() == null) {
					msgs = newCostModel.eInverseAdd(this, EOPPOSITE_FEATURE_BASE - LNGScenarioPackage.LNG_REFERENCE_MODEL__COST_MODEL, null, msgs);
				}
				if (msgs != null) msgs.dispatch();
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, LNGScenarioPackage.LNG_REFERENCE_MODEL__COST_MODEL, oldCostModel, costModel));
			}
		}
		return costModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CostModel basicGetCostModel() {
		return costModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetCostModel(CostModel newCostModel, NotificationChain msgs) {
		CostModel oldCostModel = costModel;
		costModel = newCostModel;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, LNGScenarioPackage.LNG_REFERENCE_MODEL__COST_MODEL, oldCostModel, newCostModel);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setCostModel(CostModel newCostModel) {
		if (newCostModel != costModel) {
			NotificationChain msgs = null;
			if (costModel != null)
				msgs = ((InternalEObject)costModel).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - LNGScenarioPackage.LNG_REFERENCE_MODEL__COST_MODEL, null, msgs);
			if (newCostModel != null)
				msgs = ((InternalEObject)newCostModel).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - LNGScenarioPackage.LNG_REFERENCE_MODEL__COST_MODEL, null, msgs);
			msgs = basicSetCostModel(newCostModel, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, LNGScenarioPackage.LNG_REFERENCE_MODEL__COST_MODEL, newCostModel, newCostModel));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case LNGScenarioPackage.LNG_REFERENCE_MODEL__PORT_MODEL:
				return basicSetPortModel(null, msgs);
			case LNGScenarioPackage.LNG_REFERENCE_MODEL__FLEET_MODEL:
				return basicSetFleetModel(null, msgs);
			case LNGScenarioPackage.LNG_REFERENCE_MODEL__PRICING_MODEL:
				return basicSetPricingModel(null, msgs);
			case LNGScenarioPackage.LNG_REFERENCE_MODEL__COMMERCIAL_MODEL:
				return basicSetCommercialModel(null, msgs);
			case LNGScenarioPackage.LNG_REFERENCE_MODEL__SPOT_MARKETS_MODEL:
				return basicSetSpotMarketsModel(null, msgs);
			case LNGScenarioPackage.LNG_REFERENCE_MODEL__COST_MODEL:
				return basicSetCostModel(null, msgs);
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
			case LNGScenarioPackage.LNG_REFERENCE_MODEL__PORT_MODEL:
				if (resolve) return getPortModel();
				return basicGetPortModel();
			case LNGScenarioPackage.LNG_REFERENCE_MODEL__FLEET_MODEL:
				if (resolve) return getFleetModel();
				return basicGetFleetModel();
			case LNGScenarioPackage.LNG_REFERENCE_MODEL__PRICING_MODEL:
				if (resolve) return getPricingModel();
				return basicGetPricingModel();
			case LNGScenarioPackage.LNG_REFERENCE_MODEL__COMMERCIAL_MODEL:
				if (resolve) return getCommercialModel();
				return basicGetCommercialModel();
			case LNGScenarioPackage.LNG_REFERENCE_MODEL__SPOT_MARKETS_MODEL:
				if (resolve) return getSpotMarketsModel();
				return basicGetSpotMarketsModel();
			case LNGScenarioPackage.LNG_REFERENCE_MODEL__COST_MODEL:
				if (resolve) return getCostModel();
				return basicGetCostModel();
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
			case LNGScenarioPackage.LNG_REFERENCE_MODEL__PORT_MODEL:
				setPortModel((PortModel)newValue);
				return;
			case LNGScenarioPackage.LNG_REFERENCE_MODEL__FLEET_MODEL:
				setFleetModel((FleetModel)newValue);
				return;
			case LNGScenarioPackage.LNG_REFERENCE_MODEL__PRICING_MODEL:
				setPricingModel((PricingModel)newValue);
				return;
			case LNGScenarioPackage.LNG_REFERENCE_MODEL__COMMERCIAL_MODEL:
				setCommercialModel((CommercialModel)newValue);
				return;
			case LNGScenarioPackage.LNG_REFERENCE_MODEL__SPOT_MARKETS_MODEL:
				setSpotMarketsModel((SpotMarketsModel)newValue);
				return;
			case LNGScenarioPackage.LNG_REFERENCE_MODEL__COST_MODEL:
				setCostModel((CostModel)newValue);
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
			case LNGScenarioPackage.LNG_REFERENCE_MODEL__PORT_MODEL:
				setPortModel((PortModel)null);
				return;
			case LNGScenarioPackage.LNG_REFERENCE_MODEL__FLEET_MODEL:
				setFleetModel((FleetModel)null);
				return;
			case LNGScenarioPackage.LNG_REFERENCE_MODEL__PRICING_MODEL:
				setPricingModel((PricingModel)null);
				return;
			case LNGScenarioPackage.LNG_REFERENCE_MODEL__COMMERCIAL_MODEL:
				setCommercialModel((CommercialModel)null);
				return;
			case LNGScenarioPackage.LNG_REFERENCE_MODEL__SPOT_MARKETS_MODEL:
				setSpotMarketsModel((SpotMarketsModel)null);
				return;
			case LNGScenarioPackage.LNG_REFERENCE_MODEL__COST_MODEL:
				setCostModel((CostModel)null);
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
			case LNGScenarioPackage.LNG_REFERENCE_MODEL__PORT_MODEL:
				return portModel != null;
			case LNGScenarioPackage.LNG_REFERENCE_MODEL__FLEET_MODEL:
				return fleetModel != null;
			case LNGScenarioPackage.LNG_REFERENCE_MODEL__PRICING_MODEL:
				return pricingModel != null;
			case LNGScenarioPackage.LNG_REFERENCE_MODEL__COMMERCIAL_MODEL:
				return commercialModel != null;
			case LNGScenarioPackage.LNG_REFERENCE_MODEL__SPOT_MARKETS_MODEL:
				return spotMarketsModel != null;
			case LNGScenarioPackage.LNG_REFERENCE_MODEL__COST_MODEL:
				return costModel != null;
		}
		return super.eIsSet(featureID);
	}

} //LNGReferenceModelImpl
