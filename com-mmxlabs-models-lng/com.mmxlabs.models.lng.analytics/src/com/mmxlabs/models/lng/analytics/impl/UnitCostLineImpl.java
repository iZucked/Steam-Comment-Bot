/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.analytics.impl;
import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.CostComponent;
import com.mmxlabs.models.lng.analytics.UnitCostLine;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.mmxcore.impl.MMXObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Unit Cost Line</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.UnitCostLineImpl#getUnitCost <em>Unit Cost</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.UnitCostLineImpl#getMmbtuDelivered <em>Mmbtu Delivered</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.UnitCostLineImpl#getFrom <em>From</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.UnitCostLineImpl#getTo <em>To</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.UnitCostLineImpl#getDuration <em>Duration</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.UnitCostLineImpl#getVolumeLoaded <em>Volume Loaded</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.UnitCostLineImpl#getVolumeDischarged <em>Volume Discharged</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.UnitCostLineImpl#getHireCost <em>Hire Cost</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.UnitCostLineImpl#getFuelCost <em>Fuel Cost</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.UnitCostLineImpl#getCanalCost <em>Canal Cost</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.UnitCostLineImpl#getCostComponents <em>Cost Components</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.UnitCostLineImpl#getPortCost <em>Port Cost</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.analytics.impl.UnitCostLineImpl#getProfit <em>Profit</em>}</li>
 * </ul>
 *
 * @generated
 */
public class UnitCostLineImpl extends MMXObjectImpl implements UnitCostLine {
	/**
	 * The default value of the '{@link #getUnitCost() <em>Unit Cost</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getUnitCost()
	 * @generated
	 * @ordered
	 */
	protected static final double UNIT_COST_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getUnitCost() <em>Unit Cost</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getUnitCost()
	 * @generated
	 * @ordered
	 */
	protected double unitCost = UNIT_COST_EDEFAULT;

	/**
	 * The default value of the '{@link #getMmbtuDelivered() <em>Mmbtu Delivered</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMmbtuDelivered()
	 * @generated
	 * @ordered
	 */
	protected static final int MMBTU_DELIVERED_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getMmbtuDelivered() <em>Mmbtu Delivered</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMmbtuDelivered()
	 * @generated
	 * @ordered
	 */
	protected int mmbtuDelivered = MMBTU_DELIVERED_EDEFAULT;

	/**
	 * The cached value of the '{@link #getFrom() <em>From</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFrom()
	 * @generated
	 * @ordered
	 */
	protected Port from;

	/**
	 * The cached value of the '{@link #getTo() <em>To</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTo()
	 * @generated
	 * @ordered
	 */
	protected Port to;

	/**
	 * The default value of the '{@link #getDuration() <em>Duration</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDuration()
	 * @generated
	 * @ordered
	 */
	protected static final int DURATION_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getDuration() <em>Duration</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDuration()
	 * @generated
	 * @ordered
	 */
	protected int duration = DURATION_EDEFAULT;

	/**
	 * The default value of the '{@link #getVolumeLoaded() <em>Volume Loaded</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVolumeLoaded()
	 * @generated
	 * @ordered
	 */
	protected static final int VOLUME_LOADED_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getVolumeLoaded() <em>Volume Loaded</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVolumeLoaded()
	 * @generated
	 * @ordered
	 */
	protected int volumeLoaded = VOLUME_LOADED_EDEFAULT;

	/**
	 * The default value of the '{@link #getVolumeDischarged() <em>Volume Discharged</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVolumeDischarged()
	 * @generated
	 * @ordered
	 */
	protected static final int VOLUME_DISCHARGED_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getVolumeDischarged() <em>Volume Discharged</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVolumeDischarged()
	 * @generated
	 * @ordered
	 */
	protected int volumeDischarged = VOLUME_DISCHARGED_EDEFAULT;

	/**
	 * The default value of the '{@link #getHireCost() <em>Hire Cost</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getHireCost()
	 * @generated
	 * @ordered
	 */
	protected static final int HIRE_COST_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getHireCost() <em>Hire Cost</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getHireCost()
	 * @generated
	 * @ordered
	 */
	protected int hireCost = HIRE_COST_EDEFAULT;

	/**
	 * The default value of the '{@link #getFuelCost() <em>Fuel Cost</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFuelCost()
	 * @generated
	 * @ordered
	 */
	protected static final int FUEL_COST_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getFuelCost() <em>Fuel Cost</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFuelCost()
	 * @generated
	 * @ordered
	 */
	protected int fuelCost = FUEL_COST_EDEFAULT;

	/**
	 * The default value of the '{@link #getCanalCost() <em>Canal Cost</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCanalCost()
	 * @generated
	 * @ordered
	 */
	protected static final int CANAL_COST_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getCanalCost() <em>Canal Cost</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCanalCost()
	 * @generated
	 * @ordered
	 */
	protected int canalCost = CANAL_COST_EDEFAULT;

	/**
	 * The cached value of the '{@link #getCostComponents() <em>Cost Components</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCostComponents()
	 * @generated
	 * @ordered
	 */
	protected EList<CostComponent> costComponents;

	/**
	 * The default value of the '{@link #getPortCost() <em>Port Cost</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPortCost()
	 * @generated
	 * @ordered
	 */
	protected static final int PORT_COST_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getPortCost() <em>Port Cost</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPortCost()
	 * @generated
	 * @ordered
	 */
	protected int portCost = PORT_COST_EDEFAULT;

	/**
	 * The default value of the '{@link #getProfit() <em>Profit</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getProfit()
	 * @generated
	 * @ordered
	 */
	protected static final int PROFIT_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getProfit() <em>Profit</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getProfit()
	 * @generated
	 * @ordered
	 */
	protected int profit = PROFIT_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected UnitCostLineImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return AnalyticsPackage.Literals.UNIT_COST_LINE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public double getUnitCost() {
		return unitCost;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setUnitCost(double newUnitCost) {
		double oldUnitCost = unitCost;
		unitCost = newUnitCost;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.UNIT_COST_LINE__UNIT_COST, oldUnitCost, unitCost));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getTotalCost() {
		return getHireCost() + getFuelCost() + getCanalCost() + getPortCost();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getMmbtuDelivered() {
		return mmbtuDelivered;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setMmbtuDelivered(int newMmbtuDelivered) {
		int oldMmbtuDelivered = mmbtuDelivered;
		mmbtuDelivered = newMmbtuDelivered;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.UNIT_COST_LINE__MMBTU_DELIVERED, oldMmbtuDelivered, mmbtuDelivered));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Port getFrom() {
		if (from != null && from.eIsProxy()) {
			InternalEObject oldFrom = (InternalEObject)from;
			from = (Port)eResolveProxy(oldFrom);
			if (from != oldFrom) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, AnalyticsPackage.UNIT_COST_LINE__FROM, oldFrom, from));
			}
		}
		return from;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Port basicGetFrom() {
		return from;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setFrom(Port newFrom) {
		Port oldFrom = from;
		from = newFrom;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.UNIT_COST_LINE__FROM, oldFrom, from));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Port getTo() {
		if (to != null && to.eIsProxy()) {
			InternalEObject oldTo = (InternalEObject)to;
			to = (Port)eResolveProxy(oldTo);
			if (to != oldTo) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, AnalyticsPackage.UNIT_COST_LINE__TO, oldTo, to));
			}
		}
		return to;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Port basicGetTo() {
		return to;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setTo(Port newTo) {
		Port oldTo = to;
		to = newTo;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.UNIT_COST_LINE__TO, oldTo, to));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getDuration() {
		return duration;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setDuration(int newDuration) {
		int oldDuration = duration;
		duration = newDuration;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.UNIT_COST_LINE__DURATION, oldDuration, duration));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getVolumeLoaded() {
		return volumeLoaded;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setVolumeLoaded(int newVolumeLoaded) {
		int oldVolumeLoaded = volumeLoaded;
		volumeLoaded = newVolumeLoaded;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.UNIT_COST_LINE__VOLUME_LOADED, oldVolumeLoaded, volumeLoaded));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getVolumeDischarged() {
		return volumeDischarged;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setVolumeDischarged(int newVolumeDischarged) {
		int oldVolumeDischarged = volumeDischarged;
		volumeDischarged = newVolumeDischarged;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.UNIT_COST_LINE__VOLUME_DISCHARGED, oldVolumeDischarged, volumeDischarged));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getHireCost() {
		return hireCost;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setHireCost(int newHireCost) {
		int oldHireCost = hireCost;
		hireCost = newHireCost;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.UNIT_COST_LINE__HIRE_COST, oldHireCost, hireCost));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getFuelCost() {
		return fuelCost;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setFuelCost(int newFuelCost) {
		int oldFuelCost = fuelCost;
		fuelCost = newFuelCost;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.UNIT_COST_LINE__FUEL_COST, oldFuelCost, fuelCost));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getCanalCost() {
		return canalCost;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setCanalCost(int newCanalCost) {
		int oldCanalCost = canalCost;
		canalCost = newCanalCost;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.UNIT_COST_LINE__CANAL_COST, oldCanalCost, canalCost));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<CostComponent> getCostComponents() {
		if (costComponents == null) {
			costComponents = new EObjectContainmentEList<CostComponent>(CostComponent.class, this, AnalyticsPackage.UNIT_COST_LINE__COST_COMPONENTS);
		}
		return costComponents;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getPortCost() {
		return portCost;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setPortCost(int newPortCost) {
		int oldPortCost = portCost;
		portCost = newPortCost;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.UNIT_COST_LINE__PORT_COST, oldPortCost, portCost));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getProfit() {
		return profit;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setProfit(int newProfit) {
		int oldProfit = profit;
		profit = newProfit;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalyticsPackage.UNIT_COST_LINE__PROFIT, oldProfit, profit));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case AnalyticsPackage.UNIT_COST_LINE__COST_COMPONENTS:
				return ((InternalEList<?>)getCostComponents()).basicRemove(otherEnd, msgs);
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
			case AnalyticsPackage.UNIT_COST_LINE__UNIT_COST:
				return getUnitCost();
			case AnalyticsPackage.UNIT_COST_LINE__MMBTU_DELIVERED:
				return getMmbtuDelivered();
			case AnalyticsPackage.UNIT_COST_LINE__FROM:
				if (resolve) return getFrom();
				return basicGetFrom();
			case AnalyticsPackage.UNIT_COST_LINE__TO:
				if (resolve) return getTo();
				return basicGetTo();
			case AnalyticsPackage.UNIT_COST_LINE__DURATION:
				return getDuration();
			case AnalyticsPackage.UNIT_COST_LINE__VOLUME_LOADED:
				return getVolumeLoaded();
			case AnalyticsPackage.UNIT_COST_LINE__VOLUME_DISCHARGED:
				return getVolumeDischarged();
			case AnalyticsPackage.UNIT_COST_LINE__HIRE_COST:
				return getHireCost();
			case AnalyticsPackage.UNIT_COST_LINE__FUEL_COST:
				return getFuelCost();
			case AnalyticsPackage.UNIT_COST_LINE__CANAL_COST:
				return getCanalCost();
			case AnalyticsPackage.UNIT_COST_LINE__COST_COMPONENTS:
				return getCostComponents();
			case AnalyticsPackage.UNIT_COST_LINE__PORT_COST:
				return getPortCost();
			case AnalyticsPackage.UNIT_COST_LINE__PROFIT:
				return getProfit();
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
			case AnalyticsPackage.UNIT_COST_LINE__UNIT_COST:
				setUnitCost((Double)newValue);
				return;
			case AnalyticsPackage.UNIT_COST_LINE__MMBTU_DELIVERED:
				setMmbtuDelivered((Integer)newValue);
				return;
			case AnalyticsPackage.UNIT_COST_LINE__FROM:
				setFrom((Port)newValue);
				return;
			case AnalyticsPackage.UNIT_COST_LINE__TO:
				setTo((Port)newValue);
				return;
			case AnalyticsPackage.UNIT_COST_LINE__DURATION:
				setDuration((Integer)newValue);
				return;
			case AnalyticsPackage.UNIT_COST_LINE__VOLUME_LOADED:
				setVolumeLoaded((Integer)newValue);
				return;
			case AnalyticsPackage.UNIT_COST_LINE__VOLUME_DISCHARGED:
				setVolumeDischarged((Integer)newValue);
				return;
			case AnalyticsPackage.UNIT_COST_LINE__HIRE_COST:
				setHireCost((Integer)newValue);
				return;
			case AnalyticsPackage.UNIT_COST_LINE__FUEL_COST:
				setFuelCost((Integer)newValue);
				return;
			case AnalyticsPackage.UNIT_COST_LINE__CANAL_COST:
				setCanalCost((Integer)newValue);
				return;
			case AnalyticsPackage.UNIT_COST_LINE__COST_COMPONENTS:
				getCostComponents().clear();
				getCostComponents().addAll((Collection<? extends CostComponent>)newValue);
				return;
			case AnalyticsPackage.UNIT_COST_LINE__PORT_COST:
				setPortCost((Integer)newValue);
				return;
			case AnalyticsPackage.UNIT_COST_LINE__PROFIT:
				setProfit((Integer)newValue);
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
			case AnalyticsPackage.UNIT_COST_LINE__UNIT_COST:
				setUnitCost(UNIT_COST_EDEFAULT);
				return;
			case AnalyticsPackage.UNIT_COST_LINE__MMBTU_DELIVERED:
				setMmbtuDelivered(MMBTU_DELIVERED_EDEFAULT);
				return;
			case AnalyticsPackage.UNIT_COST_LINE__FROM:
				setFrom((Port)null);
				return;
			case AnalyticsPackage.UNIT_COST_LINE__TO:
				setTo((Port)null);
				return;
			case AnalyticsPackage.UNIT_COST_LINE__DURATION:
				setDuration(DURATION_EDEFAULT);
				return;
			case AnalyticsPackage.UNIT_COST_LINE__VOLUME_LOADED:
				setVolumeLoaded(VOLUME_LOADED_EDEFAULT);
				return;
			case AnalyticsPackage.UNIT_COST_LINE__VOLUME_DISCHARGED:
				setVolumeDischarged(VOLUME_DISCHARGED_EDEFAULT);
				return;
			case AnalyticsPackage.UNIT_COST_LINE__HIRE_COST:
				setHireCost(HIRE_COST_EDEFAULT);
				return;
			case AnalyticsPackage.UNIT_COST_LINE__FUEL_COST:
				setFuelCost(FUEL_COST_EDEFAULT);
				return;
			case AnalyticsPackage.UNIT_COST_LINE__CANAL_COST:
				setCanalCost(CANAL_COST_EDEFAULT);
				return;
			case AnalyticsPackage.UNIT_COST_LINE__COST_COMPONENTS:
				getCostComponents().clear();
				return;
			case AnalyticsPackage.UNIT_COST_LINE__PORT_COST:
				setPortCost(PORT_COST_EDEFAULT);
				return;
			case AnalyticsPackage.UNIT_COST_LINE__PROFIT:
				setProfit(PROFIT_EDEFAULT);
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
			case AnalyticsPackage.UNIT_COST_LINE__UNIT_COST:
				return unitCost != UNIT_COST_EDEFAULT;
			case AnalyticsPackage.UNIT_COST_LINE__MMBTU_DELIVERED:
				return mmbtuDelivered != MMBTU_DELIVERED_EDEFAULT;
			case AnalyticsPackage.UNIT_COST_LINE__FROM:
				return from != null;
			case AnalyticsPackage.UNIT_COST_LINE__TO:
				return to != null;
			case AnalyticsPackage.UNIT_COST_LINE__DURATION:
				return duration != DURATION_EDEFAULT;
			case AnalyticsPackage.UNIT_COST_LINE__VOLUME_LOADED:
				return volumeLoaded != VOLUME_LOADED_EDEFAULT;
			case AnalyticsPackage.UNIT_COST_LINE__VOLUME_DISCHARGED:
				return volumeDischarged != VOLUME_DISCHARGED_EDEFAULT;
			case AnalyticsPackage.UNIT_COST_LINE__HIRE_COST:
				return hireCost != HIRE_COST_EDEFAULT;
			case AnalyticsPackage.UNIT_COST_LINE__FUEL_COST:
				return fuelCost != FUEL_COST_EDEFAULT;
			case AnalyticsPackage.UNIT_COST_LINE__CANAL_COST:
				return canalCost != CANAL_COST_EDEFAULT;
			case AnalyticsPackage.UNIT_COST_LINE__COST_COMPONENTS:
				return costComponents != null && !costComponents.isEmpty();
			case AnalyticsPackage.UNIT_COST_LINE__PORT_COST:
				return portCost != PORT_COST_EDEFAULT;
			case AnalyticsPackage.UNIT_COST_LINE__PROFIT:
				return profit != PROFIT_EDEFAULT;
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
		result.append(" (unitCost: ");
		result.append(unitCost);
		result.append(", mmbtuDelivered: ");
		result.append(mmbtuDelivered);
		result.append(", duration: ");
		result.append(duration);
		result.append(", volumeLoaded: ");
		result.append(volumeLoaded);
		result.append(", volumeDischarged: ");
		result.append(volumeDischarged);
		result.append(", hireCost: ");
		result.append(hireCost);
		result.append(", fuelCost: ");
		result.append(fuelCost);
		result.append(", canalCost: ");
		result.append(canalCost);
		result.append(", portCost: ");
		result.append(portCost);
		result.append(", profit: ");
		result.append(profit);
		result.append(')');
		return result.toString();
	}

} // end of UnitCostLineImpl

// finish type fixing
