/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.schedule.impl;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EcoreEMap;
import org.eclipse.emf.ecore.util.InternalEList;

import com.mmxlabs.models.lng.cargo.CharterOutEvent;
import com.mmxlabs.models.lng.cargo.DryDockEvent;
import com.mmxlabs.models.lng.cargo.MaintenanceEvent;
import com.mmxlabs.models.lng.cargo.VesselEvent;
import com.mmxlabs.models.lng.schedule.CapacityViolationType;
import com.mmxlabs.models.lng.schedule.CapacityViolationsHolder;
import com.mmxlabs.models.lng.schedule.GroupProfitAndLoss;
import com.mmxlabs.models.lng.schedule.PortVisit;
import com.mmxlabs.models.lng.schedule.ProfitAndLossContainer;
import com.mmxlabs.models.lng.schedule.SchedulePackage;
import com.mmxlabs.models.lng.schedule.VesselEventVisit;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Vessel Event Visit</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.VesselEventVisitImpl#getViolations <em>Violations</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.VesselEventVisitImpl#getPortCost <em>Port Cost</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.VesselEventVisitImpl#getGroupProfitAndLoss <em>Group Profit And Loss</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.VesselEventVisitImpl#getGroupProfitAndLossNoTimeCharter <em>Group Profit And Loss No Time Charter</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.VesselEventVisitImpl#getVesselEvent <em>Vessel Event</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class VesselEventVisitImpl extends EventImpl implements VesselEventVisit {
	/**
	 * The cached value of the '{@link #getViolations() <em>Violations</em>}' map.
	 * <!-- begin-user-doc -->
	 * @since 3.0
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
	 * The cached value of the '{@link #getGroupProfitAndLoss() <em>Group Profit And Loss</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * @since 4.0
	 * <!-- end-user-doc -->
	 * @see #getGroupProfitAndLoss()
	 * @generated
	 * @ordered
	 */
	protected GroupProfitAndLoss groupProfitAndLoss;
	/**
	 * The cached value of the '{@link #getGroupProfitAndLossNoTimeCharter() <em>Group Profit And Loss No Time Charter</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * @since 6.0
	 * <!-- end-user-doc -->
	 * @see #getGroupProfitAndLossNoTimeCharter()
	 * @generated
	 * @ordered
	 */
	protected GroupProfitAndLoss groupProfitAndLossNoTimeCharter;
	/**
	 * The cached value of the '{@link #getVesselEvent() <em>Vessel Event</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVesselEvent()
	 * @generated
	 * @ordered
	 */
	protected com.mmxlabs.models.lng.cargo.VesselEvent vesselEvent;

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
	 * @since 3.0
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
	 * @since 4.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public GroupProfitAndLoss getGroupProfitAndLoss() {
		return groupProfitAndLoss;
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 4.0
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
	 * @since 4.0
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
	 * @since 6.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public GroupProfitAndLoss getGroupProfitAndLossNoTimeCharter() {
		return groupProfitAndLossNoTimeCharter;
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 6.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetGroupProfitAndLossNoTimeCharter(GroupProfitAndLoss newGroupProfitAndLossNoTimeCharter, NotificationChain msgs) {
		GroupProfitAndLoss oldGroupProfitAndLossNoTimeCharter = groupProfitAndLossNoTimeCharter;
		groupProfitAndLossNoTimeCharter = newGroupProfitAndLossNoTimeCharter;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, SchedulePackage.VESSEL_EVENT_VISIT__GROUP_PROFIT_AND_LOSS_NO_TIME_CHARTER, oldGroupProfitAndLossNoTimeCharter, newGroupProfitAndLossNoTimeCharter);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 6.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setGroupProfitAndLossNoTimeCharter(GroupProfitAndLoss newGroupProfitAndLossNoTimeCharter) {
		if (newGroupProfitAndLossNoTimeCharter != groupProfitAndLossNoTimeCharter) {
			NotificationChain msgs = null;
			if (groupProfitAndLossNoTimeCharter != null)
				msgs = ((InternalEObject)groupProfitAndLossNoTimeCharter).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - SchedulePackage.VESSEL_EVENT_VISIT__GROUP_PROFIT_AND_LOSS_NO_TIME_CHARTER, null, msgs);
			if (newGroupProfitAndLossNoTimeCharter != null)
				msgs = ((InternalEObject)newGroupProfitAndLossNoTimeCharter).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - SchedulePackage.VESSEL_EVENT_VISIT__GROUP_PROFIT_AND_LOSS_NO_TIME_CHARTER, null, msgs);
			msgs = basicSetGroupProfitAndLossNoTimeCharter(newGroupProfitAndLossNoTimeCharter, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.VESSEL_EVENT_VISIT__GROUP_PROFIT_AND_LOSS_NO_TIME_CHARTER, newGroupProfitAndLossNoTimeCharter, newGroupProfitAndLossNoTimeCharter));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public com.mmxlabs.models.lng.cargo.VesselEvent getVesselEvent() {
		if (vesselEvent != null && vesselEvent.eIsProxy()) {
			InternalEObject oldVesselEvent = (InternalEObject)vesselEvent;
			vesselEvent = (com.mmxlabs.models.lng.cargo.VesselEvent)eResolveProxy(oldVesselEvent);
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
	public com.mmxlabs.models.lng.cargo.VesselEvent basicGetVesselEvent() {
		return vesselEvent;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setVesselEvent(com.mmxlabs.models.lng.cargo.VesselEvent newVesselEvent) {
		com.mmxlabs.models.lng.cargo.VesselEvent oldVesselEvent = vesselEvent;
		vesselEvent = newVesselEvent;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.VESSEL_EVENT_VISIT__VESSEL_EVENT, oldVesselEvent, vesselEvent));
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
			case SchedulePackage.VESSEL_EVENT_VISIT__GROUP_PROFIT_AND_LOSS:
				return basicSetGroupProfitAndLoss(null, msgs);
			case SchedulePackage.VESSEL_EVENT_VISIT__GROUP_PROFIT_AND_LOSS_NO_TIME_CHARTER:
				return basicSetGroupProfitAndLossNoTimeCharter(null, msgs);
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
			case SchedulePackage.VESSEL_EVENT_VISIT__GROUP_PROFIT_AND_LOSS:
				return getGroupProfitAndLoss();
			case SchedulePackage.VESSEL_EVENT_VISIT__GROUP_PROFIT_AND_LOSS_NO_TIME_CHARTER:
				return getGroupProfitAndLossNoTimeCharter();
			case SchedulePackage.VESSEL_EVENT_VISIT__VESSEL_EVENT:
				if (resolve) return getVesselEvent();
				return basicGetVesselEvent();
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
			case SchedulePackage.VESSEL_EVENT_VISIT__GROUP_PROFIT_AND_LOSS:
				setGroupProfitAndLoss((GroupProfitAndLoss)newValue);
				return;
			case SchedulePackage.VESSEL_EVENT_VISIT__GROUP_PROFIT_AND_LOSS_NO_TIME_CHARTER:
				setGroupProfitAndLossNoTimeCharter((GroupProfitAndLoss)newValue);
				return;
			case SchedulePackage.VESSEL_EVENT_VISIT__VESSEL_EVENT:
				setVesselEvent((com.mmxlabs.models.lng.cargo.VesselEvent)newValue);
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
			case SchedulePackage.VESSEL_EVENT_VISIT__GROUP_PROFIT_AND_LOSS:
				setGroupProfitAndLoss((GroupProfitAndLoss)null);
				return;
			case SchedulePackage.VESSEL_EVENT_VISIT__GROUP_PROFIT_AND_LOSS_NO_TIME_CHARTER:
				setGroupProfitAndLossNoTimeCharter((GroupProfitAndLoss)null);
				return;
			case SchedulePackage.VESSEL_EVENT_VISIT__VESSEL_EVENT:
				setVesselEvent((com.mmxlabs.models.lng.cargo.VesselEvent)null);
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
			case SchedulePackage.VESSEL_EVENT_VISIT__GROUP_PROFIT_AND_LOSS:
				return groupProfitAndLoss != null;
			case SchedulePackage.VESSEL_EVENT_VISIT__GROUP_PROFIT_AND_LOSS_NO_TIME_CHARTER:
				return groupProfitAndLossNoTimeCharter != null;
			case SchedulePackage.VESSEL_EVENT_VISIT__VESSEL_EVENT:
				return vesselEvent != null;
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
				default: return -1;
			}
		}
		if (baseClass == ProfitAndLossContainer.class) {
			switch (derivedFeatureID) {
				case SchedulePackage.VESSEL_EVENT_VISIT__GROUP_PROFIT_AND_LOSS: return SchedulePackage.PROFIT_AND_LOSS_CONTAINER__GROUP_PROFIT_AND_LOSS;
				case SchedulePackage.VESSEL_EVENT_VISIT__GROUP_PROFIT_AND_LOSS_NO_TIME_CHARTER: return SchedulePackage.PROFIT_AND_LOSS_CONTAINER__GROUP_PROFIT_AND_LOSS_NO_TIME_CHARTER;
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
				default: return -1;
			}
		}
		if (baseClass == ProfitAndLossContainer.class) {
			switch (baseFeatureID) {
				case SchedulePackage.PROFIT_AND_LOSS_CONTAINER__GROUP_PROFIT_AND_LOSS: return SchedulePackage.VESSEL_EVENT_VISIT__GROUP_PROFIT_AND_LOSS;
				case SchedulePackage.PROFIT_AND_LOSS_CONTAINER__GROUP_PROFIT_AND_LOSS_NO_TIME_CHARTER: return SchedulePackage.VESSEL_EVENT_VISIT__GROUP_PROFIT_AND_LOSS_NO_TIME_CHARTER;
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
