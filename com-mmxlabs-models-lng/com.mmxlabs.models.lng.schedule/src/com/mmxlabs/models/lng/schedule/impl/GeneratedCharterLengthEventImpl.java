/**
 */
package com.mmxlabs.models.lng.schedule.impl;

import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.EventGrouping;
import com.mmxlabs.models.lng.schedule.FuelQuantity;
import com.mmxlabs.models.lng.schedule.FuelUsage;
import com.mmxlabs.models.lng.schedule.GeneralPNLDetails;
import com.mmxlabs.models.lng.schedule.GeneratedCharterLengthEvent;
import com.mmxlabs.models.lng.schedule.GroupProfitAndLoss;
import com.mmxlabs.models.lng.schedule.ProfitAndLossContainer;
import com.mmxlabs.models.lng.schedule.SchedulePackage;

import java.lang.reflect.InvocationTargetException;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Generated Charter Length Event</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.GeneratedCharterLengthEventImpl#getGroupProfitAndLoss <em>Group Profit And Loss</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.GeneratedCharterLengthEventImpl#getGeneralPNLDetails <em>General PNL Details</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.GeneratedCharterLengthEventImpl#getEvents <em>Events</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.GeneratedCharterLengthEventImpl#getFuels <em>Fuels</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.GeneratedCharterLengthEventImpl#getDuration <em>Duration</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.GeneratedCharterLengthEventImpl#isLaden <em>Laden</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.GeneratedCharterLengthEventImpl#getContingencyHours <em>Contingency Hours</em>}</li>
 * </ul>
 *
 * @generated
 */
public class GeneratedCharterLengthEventImpl extends PortVisitImpl implements GeneratedCharterLengthEvent {
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
	 * The cached value of the '{@link #getFuels() <em>Fuels</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFuels()
	 * @generated
	 * @ordered
	 */
	protected EList<FuelQuantity> fuels;

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
	 * The default value of the '{@link #isLaden() <em>Laden</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isLaden()
	 * @generated
	 * @ordered
	 */
	protected static final boolean LADEN_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isLaden() <em>Laden</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isLaden()
	 * @generated
	 * @ordered
	 */
	protected boolean laden = LADEN_EDEFAULT;

	/**
	 * The default value of the '{@link #getContingencyHours() <em>Contingency Hours</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getContingencyHours()
	 * @generated
	 * @ordered
	 */
	protected static final int CONTINGENCY_HOURS_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getContingencyHours() <em>Contingency Hours</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getContingencyHours()
	 * @generated
	 * @ordered
	 */
	protected int contingencyHours = CONTINGENCY_HOURS_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected GeneratedCharterLengthEventImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return SchedulePackage.Literals.GENERATED_CHARTER_LENGTH_EVENT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
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
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, SchedulePackage.GENERATED_CHARTER_LENGTH_EVENT__GROUP_PROFIT_AND_LOSS, oldGroupProfitAndLoss, newGroupProfitAndLoss);
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
	public void setGroupProfitAndLoss(GroupProfitAndLoss newGroupProfitAndLoss) {
		if (newGroupProfitAndLoss != groupProfitAndLoss) {
			NotificationChain msgs = null;
			if (groupProfitAndLoss != null)
				msgs = ((InternalEObject)groupProfitAndLoss).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - SchedulePackage.GENERATED_CHARTER_LENGTH_EVENT__GROUP_PROFIT_AND_LOSS, null, msgs);
			if (newGroupProfitAndLoss != null)
				msgs = ((InternalEObject)newGroupProfitAndLoss).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - SchedulePackage.GENERATED_CHARTER_LENGTH_EVENT__GROUP_PROFIT_AND_LOSS, null, msgs);
			msgs = basicSetGroupProfitAndLoss(newGroupProfitAndLoss, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.GENERATED_CHARTER_LENGTH_EVENT__GROUP_PROFIT_AND_LOSS, newGroupProfitAndLoss, newGroupProfitAndLoss));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<GeneralPNLDetails> getGeneralPNLDetails() {
		if (generalPNLDetails == null) {
			generalPNLDetails = new EObjectContainmentEList<GeneralPNLDetails>(GeneralPNLDetails.class, this, SchedulePackage.GENERATED_CHARTER_LENGTH_EVENT__GENERAL_PNL_DETAILS);
		}
		return generalPNLDetails;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<Event> getEvents() {
		if (events == null) {
			events = new EObjectResolvingEList<Event>(Event.class, this, SchedulePackage.GENERATED_CHARTER_LENGTH_EVENT__EVENTS);
		}
		return events;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<FuelQuantity> getFuels() {
		if (fuels == null) {
			fuels = new EObjectContainmentEList<FuelQuantity>(FuelQuantity.class, this, SchedulePackage.GENERATED_CHARTER_LENGTH_EVENT__FUELS);
		}
		return fuels;
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
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.GENERATED_CHARTER_LENGTH_EVENT__DURATION, oldDuration, duration));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isLaden() {
		return laden;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setLaden(boolean newLaden) {
		boolean oldLaden = laden;
		laden = newLaden;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.GENERATED_CHARTER_LENGTH_EVENT__LADEN, oldLaden, laden));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getContingencyHours() {
		return contingencyHours;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setContingencyHours(int newContingencyHours) {
		int oldContingencyHours = contingencyHours;
		contingencyHours = newContingencyHours;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.GENERATED_CHARTER_LENGTH_EVENT__CONTINGENCY_HOURS, oldContingencyHours, contingencyHours));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public double getFuelCost() {
		int sum = 0;
		for (final FuelQuantity fq : getFuels()) {
			sum += fq.getCost();
		}
		return sum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case SchedulePackage.GENERATED_CHARTER_LENGTH_EVENT__GROUP_PROFIT_AND_LOSS:
				return basicSetGroupProfitAndLoss(null, msgs);
			case SchedulePackage.GENERATED_CHARTER_LENGTH_EVENT__GENERAL_PNL_DETAILS:
				return ((InternalEList<?>)getGeneralPNLDetails()).basicRemove(otherEnd, msgs);
			case SchedulePackage.GENERATED_CHARTER_LENGTH_EVENT__FUELS:
				return ((InternalEList<?>)getFuels()).basicRemove(otherEnd, msgs);
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
			case SchedulePackage.GENERATED_CHARTER_LENGTH_EVENT__GROUP_PROFIT_AND_LOSS:
				return getGroupProfitAndLoss();
			case SchedulePackage.GENERATED_CHARTER_LENGTH_EVENT__GENERAL_PNL_DETAILS:
				return getGeneralPNLDetails();
			case SchedulePackage.GENERATED_CHARTER_LENGTH_EVENT__EVENTS:
				return getEvents();
			case SchedulePackage.GENERATED_CHARTER_LENGTH_EVENT__FUELS:
				return getFuels();
			case SchedulePackage.GENERATED_CHARTER_LENGTH_EVENT__DURATION:
				return getDuration();
			case SchedulePackage.GENERATED_CHARTER_LENGTH_EVENT__LADEN:
				return isLaden();
			case SchedulePackage.GENERATED_CHARTER_LENGTH_EVENT__CONTINGENCY_HOURS:
				return getContingencyHours();
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
			case SchedulePackage.GENERATED_CHARTER_LENGTH_EVENT__GROUP_PROFIT_AND_LOSS:
				setGroupProfitAndLoss((GroupProfitAndLoss)newValue);
				return;
			case SchedulePackage.GENERATED_CHARTER_LENGTH_EVENT__GENERAL_PNL_DETAILS:
				getGeneralPNLDetails().clear();
				getGeneralPNLDetails().addAll((Collection<? extends GeneralPNLDetails>)newValue);
				return;
			case SchedulePackage.GENERATED_CHARTER_LENGTH_EVENT__EVENTS:
				getEvents().clear();
				getEvents().addAll((Collection<? extends Event>)newValue);
				return;
			case SchedulePackage.GENERATED_CHARTER_LENGTH_EVENT__FUELS:
				getFuels().clear();
				getFuels().addAll((Collection<? extends FuelQuantity>)newValue);
				return;
			case SchedulePackage.GENERATED_CHARTER_LENGTH_EVENT__DURATION:
				setDuration((Integer)newValue);
				return;
			case SchedulePackage.GENERATED_CHARTER_LENGTH_EVENT__LADEN:
				setLaden((Boolean)newValue);
				return;
			case SchedulePackage.GENERATED_CHARTER_LENGTH_EVENT__CONTINGENCY_HOURS:
				setContingencyHours((Integer)newValue);
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
			case SchedulePackage.GENERATED_CHARTER_LENGTH_EVENT__GROUP_PROFIT_AND_LOSS:
				setGroupProfitAndLoss((GroupProfitAndLoss)null);
				return;
			case SchedulePackage.GENERATED_CHARTER_LENGTH_EVENT__GENERAL_PNL_DETAILS:
				getGeneralPNLDetails().clear();
				return;
			case SchedulePackage.GENERATED_CHARTER_LENGTH_EVENT__EVENTS:
				getEvents().clear();
				return;
			case SchedulePackage.GENERATED_CHARTER_LENGTH_EVENT__FUELS:
				getFuels().clear();
				return;
			case SchedulePackage.GENERATED_CHARTER_LENGTH_EVENT__DURATION:
				setDuration(DURATION_EDEFAULT);
				return;
			case SchedulePackage.GENERATED_CHARTER_LENGTH_EVENT__LADEN:
				setLaden(LADEN_EDEFAULT);
				return;
			case SchedulePackage.GENERATED_CHARTER_LENGTH_EVENT__CONTINGENCY_HOURS:
				setContingencyHours(CONTINGENCY_HOURS_EDEFAULT);
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
			case SchedulePackage.GENERATED_CHARTER_LENGTH_EVENT__GROUP_PROFIT_AND_LOSS:
				return groupProfitAndLoss != null;
			case SchedulePackage.GENERATED_CHARTER_LENGTH_EVENT__GENERAL_PNL_DETAILS:
				return generalPNLDetails != null && !generalPNLDetails.isEmpty();
			case SchedulePackage.GENERATED_CHARTER_LENGTH_EVENT__EVENTS:
				return events != null && !events.isEmpty();
			case SchedulePackage.GENERATED_CHARTER_LENGTH_EVENT__FUELS:
				return fuels != null && !fuels.isEmpty();
			case SchedulePackage.GENERATED_CHARTER_LENGTH_EVENT__DURATION:
				return duration != DURATION_EDEFAULT;
			case SchedulePackage.GENERATED_CHARTER_LENGTH_EVENT__LADEN:
				return laden != LADEN_EDEFAULT;
			case SchedulePackage.GENERATED_CHARTER_LENGTH_EVENT__CONTINGENCY_HOURS:
				return contingencyHours != CONTINGENCY_HOURS_EDEFAULT;
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
		if (baseClass == ProfitAndLossContainer.class) {
			switch (derivedFeatureID) {
				case SchedulePackage.GENERATED_CHARTER_LENGTH_EVENT__GROUP_PROFIT_AND_LOSS: return SchedulePackage.PROFIT_AND_LOSS_CONTAINER__GROUP_PROFIT_AND_LOSS;
				case SchedulePackage.GENERATED_CHARTER_LENGTH_EVENT__GENERAL_PNL_DETAILS: return SchedulePackage.PROFIT_AND_LOSS_CONTAINER__GENERAL_PNL_DETAILS;
				default: return -1;
			}
		}
		if (baseClass == EventGrouping.class) {
			switch (derivedFeatureID) {
				case SchedulePackage.GENERATED_CHARTER_LENGTH_EVENT__EVENTS: return SchedulePackage.EVENT_GROUPING__EVENTS;
				default: return -1;
			}
		}
		if (baseClass == FuelUsage.class) {
			switch (derivedFeatureID) {
				case SchedulePackage.GENERATED_CHARTER_LENGTH_EVENT__FUELS: return SchedulePackage.FUEL_USAGE__FUELS;
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
		if (baseClass == ProfitAndLossContainer.class) {
			switch (baseFeatureID) {
				case SchedulePackage.PROFIT_AND_LOSS_CONTAINER__GROUP_PROFIT_AND_LOSS: return SchedulePackage.GENERATED_CHARTER_LENGTH_EVENT__GROUP_PROFIT_AND_LOSS;
				case SchedulePackage.PROFIT_AND_LOSS_CONTAINER__GENERAL_PNL_DETAILS: return SchedulePackage.GENERATED_CHARTER_LENGTH_EVENT__GENERAL_PNL_DETAILS;
				default: return -1;
			}
		}
		if (baseClass == EventGrouping.class) {
			switch (baseFeatureID) {
				case SchedulePackage.EVENT_GROUPING__EVENTS: return SchedulePackage.GENERATED_CHARTER_LENGTH_EVENT__EVENTS;
				default: return -1;
			}
		}
		if (baseClass == FuelUsage.class) {
			switch (baseFeatureID) {
				case SchedulePackage.FUEL_USAGE__FUELS: return SchedulePackage.GENERATED_CHARTER_LENGTH_EVENT__FUELS;
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
	public int eDerivedOperationID(int baseOperationID, Class<?> baseClass) {
		if (baseClass == ProfitAndLossContainer.class) {
			switch (baseOperationID) {
				default: return -1;
			}
		}
		if (baseClass == EventGrouping.class) {
			switch (baseOperationID) {
				default: return -1;
			}
		}
		if (baseClass == FuelUsage.class) {
			switch (baseOperationID) {
				case SchedulePackage.FUEL_USAGE___GET_FUEL_COST: return SchedulePackage.GENERATED_CHARTER_LENGTH_EVENT___GET_FUEL_COST;
				default: return -1;
			}
		}
		return super.eDerivedOperationID(baseOperationID, baseClass);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eInvoke(int operationID, EList<?> arguments) throws InvocationTargetException {
		switch (operationID) {
			case SchedulePackage.GENERATED_CHARTER_LENGTH_EVENT___GET_FUEL_COST:
				return getFuelCost();
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

		StringBuilder result = new StringBuilder(super.toString());
		result.append(" (duration: ");
		result.append(duration);
		result.append(", laden: ");
		result.append(laden);
		result.append(", contingencyHours: ");
		result.append(contingencyHours);
		result.append(')');
		return result.toString();
	}

} //GeneratedCharterLengthEventImpl
