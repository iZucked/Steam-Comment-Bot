/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.pricing.impl;
import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import com.mmxlabs.models.lng.pricing.CooldownPrice;
import com.mmxlabs.models.lng.pricing.FleetCostModel;
import com.mmxlabs.models.lng.pricing.Index;
import com.mmxlabs.models.lng.pricing.PortCost;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.lng.pricing.RouteCost;
import com.mmxlabs.models.lng.pricing.SpotMarketGroup;
import com.mmxlabs.models.mmxcore.impl.UUIDObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Model</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.pricing.impl.PricingModelImpl#getCommodityIndices <em>Commodity Indices</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.impl.PricingModelImpl#getCharterIndices <em>Charter Indices</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.impl.PricingModelImpl#getFleetCost <em>Fleet Cost</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.impl.PricingModelImpl#getRouteCosts <em>Route Costs</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.impl.PricingModelImpl#getPortCosts <em>Port Costs</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.impl.PricingModelImpl#getCooldownPrices <em>Cooldown Prices</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.impl.PricingModelImpl#getDesPurchaseSpotMarket <em>Des Purchase Spot Market</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.impl.PricingModelImpl#getDesSalesSpotMarket <em>Des Sales Spot Market</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.impl.PricingModelImpl#getFobPurchasesSpotMarket <em>Fob Purchases Spot Market</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.pricing.impl.PricingModelImpl#getFobSalesSpotMarket <em>Fob Sales Spot Market</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class PricingModelImpl extends UUIDObjectImpl implements PricingModel {
	/**
	 * The cached value of the '{@link #getCommodityIndices() <em>Commodity Indices</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCommodityIndices()
	 * @generated
	 * @ordered
	 */
	protected EList<Index<Double>> commodityIndices;

	/**
	 * The cached value of the '{@link #getCharterIndices() <em>Charter Indices</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCharterIndices()
	 * @generated
	 * @ordered
	 */
	protected EList<Index<Integer>> charterIndices;

	/**
	 * The cached value of the '{@link #getFleetCost() <em>Fleet Cost</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFleetCost()
	 * @generated
	 * @ordered
	 */
	protected FleetCostModel fleetCost;

	/**
	 * The cached value of the '{@link #getRouteCosts() <em>Route Costs</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRouteCosts()
	 * @generated
	 * @ordered
	 */
	protected EList<RouteCost> routeCosts;

	/**
	 * The cached value of the '{@link #getPortCosts() <em>Port Costs</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPortCosts()
	 * @generated
	 * @ordered
	 */
	protected EList<PortCost> portCosts;

	/**
	 * The cached value of the '{@link #getCooldownPrices() <em>Cooldown Prices</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCooldownPrices()
	 * @generated
	 * @ordered
	 */
	protected EList<CooldownPrice> cooldownPrices;

	/**
	 * The cached value of the '{@link #getDesPurchaseSpotMarket() <em>Des Purchase Spot Market</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDesPurchaseSpotMarket()
	 * @generated
	 * @ordered
	 */
	protected SpotMarketGroup desPurchaseSpotMarket;

	/**
	 * The cached value of the '{@link #getDesSalesSpotMarket() <em>Des Sales Spot Market</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDesSalesSpotMarket()
	 * @generated
	 * @ordered
	 */
	protected SpotMarketGroup desSalesSpotMarket;

	/**
	 * The cached value of the '{@link #getFobPurchasesSpotMarket() <em>Fob Purchases Spot Market</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFobPurchasesSpotMarket()
	 * @generated
	 * @ordered
	 */
	protected SpotMarketGroup fobPurchasesSpotMarket;

	/**
	 * The cached value of the '{@link #getFobSalesSpotMarket() <em>Fob Sales Spot Market</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFobSalesSpotMarket()
	 * @generated
	 * @ordered
	 */
	protected SpotMarketGroup fobSalesSpotMarket;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected PricingModelImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return PricingPackage.Literals.PRICING_MODEL;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Index<Double>> getCommodityIndices() {
		if (commodityIndices == null) {
			commodityIndices = new EObjectContainmentEList<Index<Double>>(Index.class, this, PricingPackage.PRICING_MODEL__COMMODITY_INDICES);
		}
		return commodityIndices;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Index<Integer>> getCharterIndices() {
		if (charterIndices == null) {
			charterIndices = new EObjectContainmentEList<Index<Integer>>(Index.class, this, PricingPackage.PRICING_MODEL__CHARTER_INDICES);
		}
		return charterIndices;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public FleetCostModel getFleetCost() {
		return fleetCost;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetFleetCost(FleetCostModel newFleetCost, NotificationChain msgs) {
		FleetCostModel oldFleetCost = fleetCost;
		fleetCost = newFleetCost;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, PricingPackage.PRICING_MODEL__FLEET_COST, oldFleetCost, newFleetCost);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setFleetCost(FleetCostModel newFleetCost) {
		if (newFleetCost != fleetCost) {
			NotificationChain msgs = null;
			if (fleetCost != null)
				msgs = ((InternalEObject)fleetCost).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - PricingPackage.PRICING_MODEL__FLEET_COST, null, msgs);
			if (newFleetCost != null)
				msgs = ((InternalEObject)newFleetCost).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - PricingPackage.PRICING_MODEL__FLEET_COST, null, msgs);
			msgs = basicSetFleetCost(newFleetCost, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PricingPackage.PRICING_MODEL__FLEET_COST, newFleetCost, newFleetCost));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<RouteCost> getRouteCosts() {
		if (routeCosts == null) {
			routeCosts = new EObjectContainmentEList<RouteCost>(RouteCost.class, this, PricingPackage.PRICING_MODEL__ROUTE_COSTS);
		}
		return routeCosts;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<PortCost> getPortCosts() {
		if (portCosts == null) {
			portCosts = new EObjectContainmentEList<PortCost>(PortCost.class, this, PricingPackage.PRICING_MODEL__PORT_COSTS);
		}
		return portCosts;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<CooldownPrice> getCooldownPrices() {
		if (cooldownPrices == null) {
			cooldownPrices = new EObjectContainmentEList<CooldownPrice>(CooldownPrice.class, this, PricingPackage.PRICING_MODEL__COOLDOWN_PRICES);
		}
		return cooldownPrices;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SpotMarketGroup getDesPurchaseSpotMarket() {
		return desPurchaseSpotMarket;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetDesPurchaseSpotMarket(SpotMarketGroup newDesPurchaseSpotMarket, NotificationChain msgs) {
		SpotMarketGroup oldDesPurchaseSpotMarket = desPurchaseSpotMarket;
		desPurchaseSpotMarket = newDesPurchaseSpotMarket;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, PricingPackage.PRICING_MODEL__DES_PURCHASE_SPOT_MARKET, oldDesPurchaseSpotMarket, newDesPurchaseSpotMarket);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDesPurchaseSpotMarket(SpotMarketGroup newDesPurchaseSpotMarket) {
		if (newDesPurchaseSpotMarket != desPurchaseSpotMarket) {
			NotificationChain msgs = null;
			if (desPurchaseSpotMarket != null)
				msgs = ((InternalEObject)desPurchaseSpotMarket).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - PricingPackage.PRICING_MODEL__DES_PURCHASE_SPOT_MARKET, null, msgs);
			if (newDesPurchaseSpotMarket != null)
				msgs = ((InternalEObject)newDesPurchaseSpotMarket).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - PricingPackage.PRICING_MODEL__DES_PURCHASE_SPOT_MARKET, null, msgs);
			msgs = basicSetDesPurchaseSpotMarket(newDesPurchaseSpotMarket, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PricingPackage.PRICING_MODEL__DES_PURCHASE_SPOT_MARKET, newDesPurchaseSpotMarket, newDesPurchaseSpotMarket));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SpotMarketGroup getDesSalesSpotMarket() {
		return desSalesSpotMarket;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetDesSalesSpotMarket(SpotMarketGroup newDesSalesSpotMarket, NotificationChain msgs) {
		SpotMarketGroup oldDesSalesSpotMarket = desSalesSpotMarket;
		desSalesSpotMarket = newDesSalesSpotMarket;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, PricingPackage.PRICING_MODEL__DES_SALES_SPOT_MARKET, oldDesSalesSpotMarket, newDesSalesSpotMarket);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDesSalesSpotMarket(SpotMarketGroup newDesSalesSpotMarket) {
		if (newDesSalesSpotMarket != desSalesSpotMarket) {
			NotificationChain msgs = null;
			if (desSalesSpotMarket != null)
				msgs = ((InternalEObject)desSalesSpotMarket).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - PricingPackage.PRICING_MODEL__DES_SALES_SPOT_MARKET, null, msgs);
			if (newDesSalesSpotMarket != null)
				msgs = ((InternalEObject)newDesSalesSpotMarket).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - PricingPackage.PRICING_MODEL__DES_SALES_SPOT_MARKET, null, msgs);
			msgs = basicSetDesSalesSpotMarket(newDesSalesSpotMarket, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PricingPackage.PRICING_MODEL__DES_SALES_SPOT_MARKET, newDesSalesSpotMarket, newDesSalesSpotMarket));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SpotMarketGroup getFobPurchasesSpotMarket() {
		return fobPurchasesSpotMarket;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetFobPurchasesSpotMarket(SpotMarketGroup newFobPurchasesSpotMarket, NotificationChain msgs) {
		SpotMarketGroup oldFobPurchasesSpotMarket = fobPurchasesSpotMarket;
		fobPurchasesSpotMarket = newFobPurchasesSpotMarket;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, PricingPackage.PRICING_MODEL__FOB_PURCHASES_SPOT_MARKET, oldFobPurchasesSpotMarket, newFobPurchasesSpotMarket);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setFobPurchasesSpotMarket(SpotMarketGroup newFobPurchasesSpotMarket) {
		if (newFobPurchasesSpotMarket != fobPurchasesSpotMarket) {
			NotificationChain msgs = null;
			if (fobPurchasesSpotMarket != null)
				msgs = ((InternalEObject)fobPurchasesSpotMarket).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - PricingPackage.PRICING_MODEL__FOB_PURCHASES_SPOT_MARKET, null, msgs);
			if (newFobPurchasesSpotMarket != null)
				msgs = ((InternalEObject)newFobPurchasesSpotMarket).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - PricingPackage.PRICING_MODEL__FOB_PURCHASES_SPOT_MARKET, null, msgs);
			msgs = basicSetFobPurchasesSpotMarket(newFobPurchasesSpotMarket, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PricingPackage.PRICING_MODEL__FOB_PURCHASES_SPOT_MARKET, newFobPurchasesSpotMarket, newFobPurchasesSpotMarket));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SpotMarketGroup getFobSalesSpotMarket() {
		return fobSalesSpotMarket;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetFobSalesSpotMarket(SpotMarketGroup newFobSalesSpotMarket, NotificationChain msgs) {
		SpotMarketGroup oldFobSalesSpotMarket = fobSalesSpotMarket;
		fobSalesSpotMarket = newFobSalesSpotMarket;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, PricingPackage.PRICING_MODEL__FOB_SALES_SPOT_MARKET, oldFobSalesSpotMarket, newFobSalesSpotMarket);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setFobSalesSpotMarket(SpotMarketGroup newFobSalesSpotMarket) {
		if (newFobSalesSpotMarket != fobSalesSpotMarket) {
			NotificationChain msgs = null;
			if (fobSalesSpotMarket != null)
				msgs = ((InternalEObject)fobSalesSpotMarket).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - PricingPackage.PRICING_MODEL__FOB_SALES_SPOT_MARKET, null, msgs);
			if (newFobSalesSpotMarket != null)
				msgs = ((InternalEObject)newFobSalesSpotMarket).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - PricingPackage.PRICING_MODEL__FOB_SALES_SPOT_MARKET, null, msgs);
			msgs = basicSetFobSalesSpotMarket(newFobSalesSpotMarket, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PricingPackage.PRICING_MODEL__FOB_SALES_SPOT_MARKET, newFobSalesSpotMarket, newFobSalesSpotMarket));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case PricingPackage.PRICING_MODEL__COMMODITY_INDICES:
				return ((InternalEList<?>)getCommodityIndices()).basicRemove(otherEnd, msgs);
			case PricingPackage.PRICING_MODEL__CHARTER_INDICES:
				return ((InternalEList<?>)getCharterIndices()).basicRemove(otherEnd, msgs);
			case PricingPackage.PRICING_MODEL__FLEET_COST:
				return basicSetFleetCost(null, msgs);
			case PricingPackage.PRICING_MODEL__ROUTE_COSTS:
				return ((InternalEList<?>)getRouteCosts()).basicRemove(otherEnd, msgs);
			case PricingPackage.PRICING_MODEL__PORT_COSTS:
				return ((InternalEList<?>)getPortCosts()).basicRemove(otherEnd, msgs);
			case PricingPackage.PRICING_MODEL__COOLDOWN_PRICES:
				return ((InternalEList<?>)getCooldownPrices()).basicRemove(otherEnd, msgs);
			case PricingPackage.PRICING_MODEL__DES_PURCHASE_SPOT_MARKET:
				return basicSetDesPurchaseSpotMarket(null, msgs);
			case PricingPackage.PRICING_MODEL__DES_SALES_SPOT_MARKET:
				return basicSetDesSalesSpotMarket(null, msgs);
			case PricingPackage.PRICING_MODEL__FOB_PURCHASES_SPOT_MARKET:
				return basicSetFobPurchasesSpotMarket(null, msgs);
			case PricingPackage.PRICING_MODEL__FOB_SALES_SPOT_MARKET:
				return basicSetFobSalesSpotMarket(null, msgs);
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
			case PricingPackage.PRICING_MODEL__COMMODITY_INDICES:
				return getCommodityIndices();
			case PricingPackage.PRICING_MODEL__CHARTER_INDICES:
				return getCharterIndices();
			case PricingPackage.PRICING_MODEL__FLEET_COST:
				return getFleetCost();
			case PricingPackage.PRICING_MODEL__ROUTE_COSTS:
				return getRouteCosts();
			case PricingPackage.PRICING_MODEL__PORT_COSTS:
				return getPortCosts();
			case PricingPackage.PRICING_MODEL__COOLDOWN_PRICES:
				return getCooldownPrices();
			case PricingPackage.PRICING_MODEL__DES_PURCHASE_SPOT_MARKET:
				return getDesPurchaseSpotMarket();
			case PricingPackage.PRICING_MODEL__DES_SALES_SPOT_MARKET:
				return getDesSalesSpotMarket();
			case PricingPackage.PRICING_MODEL__FOB_PURCHASES_SPOT_MARKET:
				return getFobPurchasesSpotMarket();
			case PricingPackage.PRICING_MODEL__FOB_SALES_SPOT_MARKET:
				return getFobSalesSpotMarket();
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
			case PricingPackage.PRICING_MODEL__COMMODITY_INDICES:
				getCommodityIndices().clear();
				getCommodityIndices().addAll((Collection<? extends Index<Double>>)newValue);
				return;
			case PricingPackage.PRICING_MODEL__CHARTER_INDICES:
				getCharterIndices().clear();
				getCharterIndices().addAll((Collection<? extends Index<Integer>>)newValue);
				return;
			case PricingPackage.PRICING_MODEL__FLEET_COST:
				setFleetCost((FleetCostModel)newValue);
				return;
			case PricingPackage.PRICING_MODEL__ROUTE_COSTS:
				getRouteCosts().clear();
				getRouteCosts().addAll((Collection<? extends RouteCost>)newValue);
				return;
			case PricingPackage.PRICING_MODEL__PORT_COSTS:
				getPortCosts().clear();
				getPortCosts().addAll((Collection<? extends PortCost>)newValue);
				return;
			case PricingPackage.PRICING_MODEL__COOLDOWN_PRICES:
				getCooldownPrices().clear();
				getCooldownPrices().addAll((Collection<? extends CooldownPrice>)newValue);
				return;
			case PricingPackage.PRICING_MODEL__DES_PURCHASE_SPOT_MARKET:
				setDesPurchaseSpotMarket((SpotMarketGroup)newValue);
				return;
			case PricingPackage.PRICING_MODEL__DES_SALES_SPOT_MARKET:
				setDesSalesSpotMarket((SpotMarketGroup)newValue);
				return;
			case PricingPackage.PRICING_MODEL__FOB_PURCHASES_SPOT_MARKET:
				setFobPurchasesSpotMarket((SpotMarketGroup)newValue);
				return;
			case PricingPackage.PRICING_MODEL__FOB_SALES_SPOT_MARKET:
				setFobSalesSpotMarket((SpotMarketGroup)newValue);
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
			case PricingPackage.PRICING_MODEL__COMMODITY_INDICES:
				getCommodityIndices().clear();
				return;
			case PricingPackage.PRICING_MODEL__CHARTER_INDICES:
				getCharterIndices().clear();
				return;
			case PricingPackage.PRICING_MODEL__FLEET_COST:
				setFleetCost((FleetCostModel)null);
				return;
			case PricingPackage.PRICING_MODEL__ROUTE_COSTS:
				getRouteCosts().clear();
				return;
			case PricingPackage.PRICING_MODEL__PORT_COSTS:
				getPortCosts().clear();
				return;
			case PricingPackage.PRICING_MODEL__COOLDOWN_PRICES:
				getCooldownPrices().clear();
				return;
			case PricingPackage.PRICING_MODEL__DES_PURCHASE_SPOT_MARKET:
				setDesPurchaseSpotMarket((SpotMarketGroup)null);
				return;
			case PricingPackage.PRICING_MODEL__DES_SALES_SPOT_MARKET:
				setDesSalesSpotMarket((SpotMarketGroup)null);
				return;
			case PricingPackage.PRICING_MODEL__FOB_PURCHASES_SPOT_MARKET:
				setFobPurchasesSpotMarket((SpotMarketGroup)null);
				return;
			case PricingPackage.PRICING_MODEL__FOB_SALES_SPOT_MARKET:
				setFobSalesSpotMarket((SpotMarketGroup)null);
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
			case PricingPackage.PRICING_MODEL__COMMODITY_INDICES:
				return commodityIndices != null && !commodityIndices.isEmpty();
			case PricingPackage.PRICING_MODEL__CHARTER_INDICES:
				return charterIndices != null && !charterIndices.isEmpty();
			case PricingPackage.PRICING_MODEL__FLEET_COST:
				return fleetCost != null;
			case PricingPackage.PRICING_MODEL__ROUTE_COSTS:
				return routeCosts != null && !routeCosts.isEmpty();
			case PricingPackage.PRICING_MODEL__PORT_COSTS:
				return portCosts != null && !portCosts.isEmpty();
			case PricingPackage.PRICING_MODEL__COOLDOWN_PRICES:
				return cooldownPrices != null && !cooldownPrices.isEmpty();
			case PricingPackage.PRICING_MODEL__DES_PURCHASE_SPOT_MARKET:
				return desPurchaseSpotMarket != null;
			case PricingPackage.PRICING_MODEL__DES_SALES_SPOT_MARKET:
				return desSalesSpotMarket != null;
			case PricingPackage.PRICING_MODEL__FOB_PURCHASES_SPOT_MARKET:
				return fobPurchasesSpotMarket != null;
			case PricingPackage.PRICING_MODEL__FOB_SALES_SPOT_MARKET:
				return fobSalesSpotMarket != null;
		}
		return super.eIsSet(featureID);
	}

} // end of PricingModelImpl

// finish type fixing
