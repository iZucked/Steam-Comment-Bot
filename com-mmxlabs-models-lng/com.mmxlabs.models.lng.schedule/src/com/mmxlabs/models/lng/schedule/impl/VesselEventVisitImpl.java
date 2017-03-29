/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.schedule.impl;
import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;
import org.eclipse.emf.ecore.util.EcoreEMap;
import org.eclipse.emf.ecore.util.InternalEList;

import com.mmxlabs.models.lng.cargo.CharterOutEvent;
import com.mmxlabs.models.lng.cargo.DryDockEvent;
import com.mmxlabs.models.lng.cargo.MaintenanceEvent;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.schedule.CapacityViolationType;
import com.mmxlabs.models.lng.schedule.CapacityViolationsHolder;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.EventGrouping;
import com.mmxlabs.models.lng.schedule.GeneralPNLDetails;
import com.mmxlabs.models.lng.schedule.GroupProfitAndLoss;
import com.mmxlabs.models.lng.schedule.PortVisit;
import com.mmxlabs.models.lng.schedule.PortVisitLateness;
import com.mmxlabs.models.lng.schedule.ProfitAndLossContainer;
import com.mmxlabs.models.lng.schedule.SchedulePackage;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Vessel Event Visit</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.VesselEventVisitImpl#getViolations <em>Violations</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.VesselEventVisitImpl#getPortCost <em>Port Cost</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.VesselEventVisitImpl#getLateness <em>Lateness</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.VesselEventVisitImpl#getHeelCost <em>Heel Cost</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.VesselEventVisitImpl#getHeelRevenue <em>Heel Revenue</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.VesselEventVisitImpl#getGroupProfitAndLoss <em>Group Profit And Loss</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.VesselEventVisitImpl#getGeneralPNLDetails <em>General PNL Details</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.VesselEventVisitImpl#getEvents <em>Events</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.VesselEventVisitImpl#getVesselEvent <em>Vessel Event</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.VesselEventVisitImpl#getRedeliveryPort <em>Redelivery Port</em>}</li>
 * </ul>
 *
 * @generated
 */
public class VesselEventVisitImpl extends EventImpl implements VesselEventVisit {
	/**
	 * The cached value of the '{@link #getViolations() <em>Violations</em>}' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getViolations()
	 * @generated
	 * @ordered
	 */
	protected EMap<CapacityViolationType, Long> violations;
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
	 * The cached value of the '{@link #getLateness() <em>Lateness</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLateness()
	 * @generated
	 * @ordered
	 */
	protected PortVisitLateness lateness;
	/**
	 * The default value of the '{@link #getHeelCost() <em>Heel Cost</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getHeelCost()
	 * @generated
	 * @ordered
	 */
	protected static final int HEEL_COST_EDEFAULT = 0;
	/**
	 * The cached value of the '{@link #getHeelCost() <em>Heel Cost</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getHeelCost()
	 * @generated
	 * @ordered
	 */
	protected int heelCost = HEEL_COST_EDEFAULT;
	/**
	 * The default value of the '{@link #getHeelRevenue() <em>Heel Revenue</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getHeelRevenue()
	 * @generated
	 * @ordered
	 */
	protected static final int HEEL_REVENUE_EDEFAULT = 0;
	/**
	 * The cached value of the '{@link #getHeelRevenue() <em>Heel Revenue</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getHeelRevenue()
	 * @generated
	 * @ordered
	 */
	protected int heelRevenue = HEEL_REVENUE_EDEFAULT;
	/**
	 * The cached value of the '{@link #getGroupProfitAndLoss() <em>Group Profit And Loss</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getGroupProfitAndLoss()
	 * @generated
	 * @ordered
	 */
	protected GroupProfitAndLoss groupProfitAndLoss;
	/**
	 * The cached value of the '{@link #getGeneralPNLDetails() <em>General PNL Details</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getGeneralPNLDetails()
	 * @generated
	 * @ordered
	 */
	protected EList<GeneralPNLDetails> generalPNLDetails;
	/**
	 * The cached value of the '{@link #getEvents() <em>Events</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEvents()
	 * @generated
	 * @ordered
	 */
	protected EList<Event> events;
	/**
	 * The cached value of the '{@link #getVesselEvent() <em>Vessel Event</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVesselEvent()
	 * @generated
	 * @ordered
	 */
	protected VesselEvent vesselEvent;

	/**
	 * The cached value of the '{@link #getRedeliveryPort() <em>Redelivery Port</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRedeliveryPort()
	 * @generated
	 * @ordered
	 */
	protected Port redeliveryPort;
	/**
	 * This is true if the Redelivery Port reference has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean redeliveryPortESet;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected VesselEventVisitImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return SchedulePackage.Literals.VESSEL_EVENT_VISIT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EMap<CapacityViolationType, Long> getViolations() {
		if (violations == null) {
			violations = new EcoreEMap<CapacityViolationType,Long>(SchedulePackage.Literals.CAPACITY_MAP_ENTRY, CapacityMapEntryImpl.class, this, SchedulePackage.VESSEL_EVENT_VISIT__VIOLATIONS);
		}
		return violations;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getPortCost() {
		return portCost;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setPortCost(int newPortCost) {
		int oldPortCost = portCost;
		portCost = newPortCost;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.VESSEL_EVENT_VISIT__PORT_COST, oldPortCost, portCost));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public PortVisitLateness getLateness() {
		return lateness;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetLateness(PortVisitLateness newLateness, NotificationChain msgs) {
		PortVisitLateness oldLateness = lateness;
		lateness = newLateness;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, SchedulePackage.VESSEL_EVENT_VISIT__LATENESS, oldLateness, newLateness);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setLateness(PortVisitLateness newLateness) {
		if (newLateness != lateness) {
			NotificationChain msgs = null;
			if (lateness != null)
				msgs = ((InternalEObject)lateness).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - SchedulePackage.VESSEL_EVENT_VISIT__LATENESS, null, msgs);
			if (newLateness != null)
				msgs = ((InternalEObject)newLateness).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - SchedulePackage.VESSEL_EVENT_VISIT__LATENESS, null, msgs);
			msgs = basicSetLateness(newLateness, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.VESSEL_EVENT_VISIT__LATENESS, newLateness, newLateness));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getHeelCost() {
		return heelCost;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setHeelCost(int newHeelCost) {
		int oldHeelCost = heelCost;
		heelCost = newHeelCost;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.VESSEL_EVENT_VISIT__HEEL_COST, oldHeelCost, heelCost));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getHeelRevenue() {
		return heelRevenue;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setHeelRevenue(int newHeelRevenue) {
		int oldHeelRevenue = heelRevenue;
		heelRevenue = newHeelRevenue;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.VESSEL_EVENT_VISIT__HEEL_REVENUE, oldHeelRevenue, heelRevenue));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public GroupProfitAndLoss getGroupProfitAndLoss() {
		return groupProfitAndLoss;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetGroupProfitAndLoss(GroupProfitAndLoss newGroupProfitAndLoss, NotificationChain msgs) {
		GroupProfitAndLoss oldGroupProfitAndLoss = groupProfitAndLoss;
		groupProfitAndLoss = newGroupProfitAndLoss;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, SchedulePackage.VESSEL_EVENT_VISIT__GROUP_PROFIT_AND_LOSS, oldGroupProfitAndLoss, newGroupProfitAndLoss);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setGroupProfitAndLoss(GroupProfitAndLoss newGroupProfitAndLoss) {
		if (newGroupProfitAndLoss != groupProfitAndLoss) {
			NotificationChain msgs = null;
			if (groupProfitAndLoss != null)
				msgs = ((InternalEObject)groupProfitAndLoss).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - SchedulePackage.VESSEL_EVENT_VISIT__GROUP_PROFIT_AND_LOSS, null, msgs);
			if (newGroupProfitAndLoss != null)
				msgs = ((InternalEObject)newGroupProfitAndLoss).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - SchedulePackage.VESSEL_EVENT_VISIT__GROUP_PROFIT_AND_LOSS, null, msgs);
			msgs = basicSetGroupProfitAndLoss(newGroupProfitAndLoss, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.VESSEL_EVENT_VISIT__GROUP_PROFIT_AND_LOSS, newGroupProfitAndLoss, newGroupProfitAndLoss));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<GeneralPNLDetails> getGeneralPNLDetails() {
		if (generalPNLDetails == null) {
			generalPNLDetails = new EObjectContainmentEList<GeneralPNLDetails>(GeneralPNLDetails.class, this, SchedulePackage.VESSEL_EVENT_VISIT__GENERAL_PNL_DETAILS);
		}
		return generalPNLDetails;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Event> getEvents() {
		if (events == null) {
			events = new EObjectResolvingEList<Event>(Event.class, this, SchedulePackage.VESSEL_EVENT_VISIT__EVENTS);
		}
		return events;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public VesselEvent getVesselEvent() {
		if (vesselEvent != null && vesselEvent.eIsProxy()) {
			InternalEObject oldVesselEvent = (InternalEObject)vesselEvent;
			vesselEvent = (VesselEvent)eResolveProxy(oldVesselEvent);
			if (vesselEvent != oldVesselEvent) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, SchedulePackage.VESSEL_EVENT_VISIT__VESSEL_EVENT, oldVesselEvent, vesselEvent));
			}
		}
		return vesselEvent;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public VesselEvent basicGetVesselEvent() {
		return vesselEvent;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setVesselEvent(VesselEvent newVesselEvent) {
		VesselEvent oldVesselEvent = vesselEvent;
		vesselEvent = newVesselEvent;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.VESSEL_EVENT_VISIT__VESSEL_EVENT, oldVesselEvent, vesselEvent));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Port getRedeliveryPort() {
		if (redeliveryPort != null && redeliveryPort.eIsProxy()) {
			InternalEObject oldRedeliveryPort = (InternalEObject)redeliveryPort;
			redeliveryPort = (Port)eResolveProxy(oldRedeliveryPort);
			if (redeliveryPort != oldRedeliveryPort) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, SchedulePackage.VESSEL_EVENT_VISIT__REDELIVERY_PORT, oldRedeliveryPort, redeliveryPort));
			}
		}
		return redeliveryPort;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Port basicGetRedeliveryPort() {
		return redeliveryPort;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setRedeliveryPort(Port newRedeliveryPort) {
		Port oldRedeliveryPort = redeliveryPort;
		redeliveryPort = newRedeliveryPort;
		boolean oldRedeliveryPortESet = redeliveryPortESet;
		redeliveryPortESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.VESSEL_EVENT_VISIT__REDELIVERY_PORT, oldRedeliveryPort, redeliveryPort, !oldRedeliveryPortESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetRedeliveryPort() {
		Port oldRedeliveryPort = redeliveryPort;
		boolean oldRedeliveryPortESet = redeliveryPortESet;
		redeliveryPort = null;
		redeliveryPortESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, SchedulePackage.VESSEL_EVENT_VISIT__REDELIVERY_PORT, oldRedeliveryPort, null, oldRedeliveryPortESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetRedeliveryPort() {
		return redeliveryPortESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case SchedulePackage.VESSEL_EVENT_VISIT__VIOLATIONS:
				return ((InternalEList<?>)getViolations()).basicRemove(otherEnd, msgs);
			case SchedulePackage.VESSEL_EVENT_VISIT__LATENESS:
				return basicSetLateness(null, msgs);
			case SchedulePackage.VESSEL_EVENT_VISIT__GROUP_PROFIT_AND_LOSS:
				return basicSetGroupProfitAndLoss(null, msgs);
			case SchedulePackage.VESSEL_EVENT_VISIT__GENERAL_PNL_DETAILS:
				return ((InternalEList<?>)getGeneralPNLDetails()).basicRemove(otherEnd, msgs);
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
			case SchedulePackage.VESSEL_EVENT_VISIT__VIOLATIONS:
				if (coreType) return getViolations();
				else return getViolations().map();
			case SchedulePackage.VESSEL_EVENT_VISIT__PORT_COST:
				return getPortCost();
			case SchedulePackage.VESSEL_EVENT_VISIT__LATENESS:
				return getLateness();
			case SchedulePackage.VESSEL_EVENT_VISIT__HEEL_COST:
				return getHeelCost();
			case SchedulePackage.VESSEL_EVENT_VISIT__HEEL_REVENUE:
				return getHeelRevenue();
			case SchedulePackage.VESSEL_EVENT_VISIT__GROUP_PROFIT_AND_LOSS:
				return getGroupProfitAndLoss();
			case SchedulePackage.VESSEL_EVENT_VISIT__GENERAL_PNL_DETAILS:
				return getGeneralPNLDetails();
			case SchedulePackage.VESSEL_EVENT_VISIT__EVENTS:
				return getEvents();
			case SchedulePackage.VESSEL_EVENT_VISIT__VESSEL_EVENT:
				if (resolve) return getVesselEvent();
				return basicGetVesselEvent();
			case SchedulePackage.VESSEL_EVENT_VISIT__REDELIVERY_PORT:
				if (resolve) return getRedeliveryPort();
				return basicGetRedeliveryPort();
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
			case SchedulePackage.VESSEL_EVENT_VISIT__VIOLATIONS:
				((EStructuralFeature.Setting)getViolations()).set(newValue);
				return;
			case SchedulePackage.VESSEL_EVENT_VISIT__PORT_COST:
				setPortCost((Integer)newValue);
				return;
			case SchedulePackage.VESSEL_EVENT_VISIT__LATENESS:
				setLateness((PortVisitLateness)newValue);
				return;
			case SchedulePackage.VESSEL_EVENT_VISIT__HEEL_COST:
				setHeelCost((Integer)newValue);
				return;
			case SchedulePackage.VESSEL_EVENT_VISIT__HEEL_REVENUE:
				setHeelRevenue((Integer)newValue);
				return;
			case SchedulePackage.VESSEL_EVENT_VISIT__GROUP_PROFIT_AND_LOSS:
				setGroupProfitAndLoss((GroupProfitAndLoss)newValue);
				return;
			case SchedulePackage.VESSEL_EVENT_VISIT__GENERAL_PNL_DETAILS:
				getGeneralPNLDetails().clear();
				getGeneralPNLDetails().addAll((Collection<? extends GeneralPNLDetails>)newValue);
				return;
			case SchedulePackage.VESSEL_EVENT_VISIT__EVENTS:
				getEvents().clear();
				getEvents().addAll((Collection<? extends Event>)newValue);
				return;
			case SchedulePackage.VESSEL_EVENT_VISIT__VESSEL_EVENT:
				setVesselEvent((VesselEvent)newValue);
				return;
			case SchedulePackage.VESSEL_EVENT_VISIT__REDELIVERY_PORT:
				setRedeliveryPort((Port)newValue);
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
			case SchedulePackage.VESSEL_EVENT_VISIT__VIOLATIONS:
				getViolations().clear();
				return;
			case SchedulePackage.VESSEL_EVENT_VISIT__PORT_COST:
				setPortCost(PORT_COST_EDEFAULT);
				return;
			case SchedulePackage.VESSEL_EVENT_VISIT__LATENESS:
				setLateness((PortVisitLateness)null);
				return;
			case SchedulePackage.VESSEL_EVENT_VISIT__HEEL_COST:
				setHeelCost(HEEL_COST_EDEFAULT);
				return;
			case SchedulePackage.VESSEL_EVENT_VISIT__HEEL_REVENUE:
				setHeelRevenue(HEEL_REVENUE_EDEFAULT);
				return;
			case SchedulePackage.VESSEL_EVENT_VISIT__GROUP_PROFIT_AND_LOSS:
				setGroupProfitAndLoss((GroupProfitAndLoss)null);
				return;
			case SchedulePackage.VESSEL_EVENT_VISIT__GENERAL_PNL_DETAILS:
				getGeneralPNLDetails().clear();
				return;
			case SchedulePackage.VESSEL_EVENT_VISIT__EVENTS:
				getEvents().clear();
				return;
			case SchedulePackage.VESSEL_EVENT_VISIT__VESSEL_EVENT:
				setVesselEvent((VesselEvent)null);
				return;
			case SchedulePackage.VESSEL_EVENT_VISIT__REDELIVERY_PORT:
				unsetRedeliveryPort();
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
			case SchedulePackage.VESSEL_EVENT_VISIT__VIOLATIONS:
				return violations != null && !violations.isEmpty();
			case SchedulePackage.VESSEL_EVENT_VISIT__PORT_COST:
				return portCost != PORT_COST_EDEFAULT;
			case SchedulePackage.VESSEL_EVENT_VISIT__LATENESS:
				return lateness != null;
			case SchedulePackage.VESSEL_EVENT_VISIT__HEEL_COST:
				return heelCost != HEEL_COST_EDEFAULT;
			case SchedulePackage.VESSEL_EVENT_VISIT__HEEL_REVENUE:
				return heelRevenue != HEEL_REVENUE_EDEFAULT;
			case SchedulePackage.VESSEL_EVENT_VISIT__GROUP_PROFIT_AND_LOSS:
				return groupProfitAndLoss != null;
			case SchedulePackage.VESSEL_EVENT_VISIT__GENERAL_PNL_DETAILS:
				return generalPNLDetails != null && !generalPNLDetails.isEmpty();
			case SchedulePackage.VESSEL_EVENT_VISIT__EVENTS:
				return events != null && !events.isEmpty();
			case SchedulePackage.VESSEL_EVENT_VISIT__VESSEL_EVENT:
				return vesselEvent != null;
			case SchedulePackage.VESSEL_EVENT_VISIT__REDELIVERY_PORT:
				return isSetRedeliveryPort();
		}
		return super.eIsSet(featureID);
	}
	
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int eBaseStructuralFeatureID(int derivedFeatureID, Class<?> baseClass) {
		if (baseClass == CapacityViolationsHolder.class) {
			switch (derivedFeatureID) {
				case SchedulePackage.VESSEL_EVENT_VISIT__VIOLATIONS: return SchedulePackage.CAPACITY_VIOLATIONS_HOLDER__VIOLATIONS;
				default: return -1;
			}
		}
		if (baseClass == PortVisit.class) {
			switch (derivedFeatureID) {
				case SchedulePackage.VESSEL_EVENT_VISIT__PORT_COST: return SchedulePackage.PORT_VISIT__PORT_COST;
				case SchedulePackage.VESSEL_EVENT_VISIT__LATENESS: return SchedulePackage.PORT_VISIT__LATENESS;
				case SchedulePackage.VESSEL_EVENT_VISIT__HEEL_COST: return SchedulePackage.PORT_VISIT__HEEL_COST;
				case SchedulePackage.VESSEL_EVENT_VISIT__HEEL_REVENUE: return SchedulePackage.PORT_VISIT__HEEL_REVENUE;
				default: return -1;
			}
		}
		if (baseClass == ProfitAndLossContainer.class) {
			switch (derivedFeatureID) {
				case SchedulePackage.VESSEL_EVENT_VISIT__GROUP_PROFIT_AND_LOSS: return SchedulePackage.PROFIT_AND_LOSS_CONTAINER__GROUP_PROFIT_AND_LOSS;
				case SchedulePackage.VESSEL_EVENT_VISIT__GENERAL_PNL_DETAILS: return SchedulePackage.PROFIT_AND_LOSS_CONTAINER__GENERAL_PNL_DETAILS;
				default: return -1;
			}
		}
		if (baseClass == EventGrouping.class) {
			switch (derivedFeatureID) {
				case SchedulePackage.VESSEL_EVENT_VISIT__EVENTS: return SchedulePackage.EVENT_GROUPING__EVENTS;
				default: return -1;
			}
		}
		return super.eBaseStructuralFeatureID(derivedFeatureID, baseClass);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int eDerivedStructuralFeatureID(int baseFeatureID, Class<?> baseClass) {
		if (baseClass == CapacityViolationsHolder.class) {
			switch (baseFeatureID) {
				case SchedulePackage.CAPACITY_VIOLATIONS_HOLDER__VIOLATIONS: return SchedulePackage.VESSEL_EVENT_VISIT__VIOLATIONS;
				default: return -1;
			}
		}
		if (baseClass == PortVisit.class) {
			switch (baseFeatureID) {
				case SchedulePackage.PORT_VISIT__PORT_COST: return SchedulePackage.VESSEL_EVENT_VISIT__PORT_COST;
				case SchedulePackage.PORT_VISIT__LATENESS: return SchedulePackage.VESSEL_EVENT_VISIT__LATENESS;
				case SchedulePackage.PORT_VISIT__HEEL_COST: return SchedulePackage.VESSEL_EVENT_VISIT__HEEL_COST;
				case SchedulePackage.PORT_VISIT__HEEL_REVENUE: return SchedulePackage.VESSEL_EVENT_VISIT__HEEL_REVENUE;
				default: return -1;
			}
		}
		if (baseClass == ProfitAndLossContainer.class) {
			switch (baseFeatureID) {
				case SchedulePackage.PROFIT_AND_LOSS_CONTAINER__GROUP_PROFIT_AND_LOSS: return SchedulePackage.VESSEL_EVENT_VISIT__GROUP_PROFIT_AND_LOSS;
				case SchedulePackage.PROFIT_AND_LOSS_CONTAINER__GENERAL_PNL_DETAILS: return SchedulePackage.VESSEL_EVENT_VISIT__GENERAL_PNL_DETAILS;
				default: return -1;
			}
		}
		if (baseClass == EventGrouping.class) {
			switch (baseFeatureID) {
				case SchedulePackage.EVENT_GROUPING__EVENTS: return SchedulePackage.VESSEL_EVENT_VISIT__EVENTS;
				default: return -1;
			}
		}
		return super.eDerivedStructuralFeatureID(baseFeatureID, baseClass);
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
		result.append(" (portCost: ");
		result.append(portCost);
		result.append(", heelCost: ");
		result.append(heelCost);
		result.append(", heelRevenue: ");
		result.append(heelRevenue);
		result.append(')');
		return result.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public String name() {
		final VesselEvent event = getVesselEvent();
		if (event != null) {
			return event.getName();
		}
		return super.name();
	}

	/**
	 * @generated NOT
	 */
	@Override
	public String type() {
		final VesselEvent event = getVesselEvent();
		if (event != null) {
			if (event instanceof CharterOutEvent) {
				return "Charter Out";
			}
			if (event instanceof DryDockEvent) {
				return "Dry Dock";
			}
			if (event instanceof MaintenanceEvent) {
				return "Maintenance";
			}
		}
		return "Unknown Event";
	}
	
} // end of VesselEventVisitImpl

// finish type fixing
