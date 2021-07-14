/**
 */
package com.mmxlabs.models.lng.schedule.impl;

import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.EventGrouping;
import com.mmxlabs.models.lng.schedule.GeneralPNLDetails;
import com.mmxlabs.models.lng.schedule.GroupProfitAndLoss;
import com.mmxlabs.models.lng.schedule.GroupedCharterOutEvent;
import com.mmxlabs.models.lng.schedule.ProfitAndLossContainer;
import com.mmxlabs.models.lng.schedule.SchedulePackage;
import com.mmxlabs.models.lng.schedule.Sequence;

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
 * An implementation of the model object '<em><b>Grouped Charter Out Event</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.GroupedCharterOutEventImpl#getEvents <em>Events</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.GroupedCharterOutEventImpl#getGroupProfitAndLoss <em>Group Profit And Loss</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.GroupedCharterOutEventImpl#getGeneralPNLDetails <em>General PNL Details</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.GroupedCharterOutEventImpl#getLinkedSequence <em>Linked Sequence</em>}</li>
 * </ul>
 *
 * @generated
 */
public class GroupedCharterOutEventImpl extends EventImpl implements GroupedCharterOutEvent {
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
	 * The cached value of the '{@link #getLinkedSequence() <em>Linked Sequence</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLinkedSequence()
	 * @generated
	 * @ordered
	 */
	protected Sequence linkedSequence;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected GroupedCharterOutEventImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return SchedulePackage.Literals.GROUPED_CHARTER_OUT_EVENT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<Event> getEvents() {
		if (events == null) {
			events = new EObjectResolvingEList<Event>(Event.class, this, SchedulePackage.GROUPED_CHARTER_OUT_EVENT__EVENTS);
		}
		return events;
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
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, SchedulePackage.GROUPED_CHARTER_OUT_EVENT__GROUP_PROFIT_AND_LOSS, oldGroupProfitAndLoss, newGroupProfitAndLoss);
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
				msgs = ((InternalEObject)groupProfitAndLoss).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - SchedulePackage.GROUPED_CHARTER_OUT_EVENT__GROUP_PROFIT_AND_LOSS, null, msgs);
			if (newGroupProfitAndLoss != null)
				msgs = ((InternalEObject)newGroupProfitAndLoss).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - SchedulePackage.GROUPED_CHARTER_OUT_EVENT__GROUP_PROFIT_AND_LOSS, null, msgs);
			msgs = basicSetGroupProfitAndLoss(newGroupProfitAndLoss, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.GROUPED_CHARTER_OUT_EVENT__GROUP_PROFIT_AND_LOSS, newGroupProfitAndLoss, newGroupProfitAndLoss));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<GeneralPNLDetails> getGeneralPNLDetails() {
		if (generalPNLDetails == null) {
			generalPNLDetails = new EObjectContainmentEList<GeneralPNLDetails>(GeneralPNLDetails.class, this, SchedulePackage.GROUPED_CHARTER_OUT_EVENT__GENERAL_PNL_DETAILS);
		}
		return generalPNLDetails;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public Sequence getSequence() {
		return getLinkedSequence();
	}
	
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Sequence getLinkedSequence() {
		if (linkedSequence != null && linkedSequence.eIsProxy()) {
			InternalEObject oldLinkedSequence = (InternalEObject)linkedSequence;
			linkedSequence = (Sequence)eResolveProxy(oldLinkedSequence);
			if (linkedSequence != oldLinkedSequence) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, SchedulePackage.GROUPED_CHARTER_OUT_EVENT__LINKED_SEQUENCE, oldLinkedSequence, linkedSequence));
			}
		}
		return linkedSequence;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Sequence basicGetLinkedSequence() {
		return linkedSequence;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setLinkedSequence(Sequence newLinkedSequence) {
		Sequence oldLinkedSequence = linkedSequence;
		linkedSequence = newLinkedSequence;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.GROUPED_CHARTER_OUT_EVENT__LINKED_SEQUENCE, oldLinkedSequence, linkedSequence));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case SchedulePackage.GROUPED_CHARTER_OUT_EVENT__GROUP_PROFIT_AND_LOSS:
				return basicSetGroupProfitAndLoss(null, msgs);
			case SchedulePackage.GROUPED_CHARTER_OUT_EVENT__GENERAL_PNL_DETAILS:
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
			case SchedulePackage.GROUPED_CHARTER_OUT_EVENT__EVENTS:
				return getEvents();
			case SchedulePackage.GROUPED_CHARTER_OUT_EVENT__GROUP_PROFIT_AND_LOSS:
				return getGroupProfitAndLoss();
			case SchedulePackage.GROUPED_CHARTER_OUT_EVENT__GENERAL_PNL_DETAILS:
				return getGeneralPNLDetails();
			case SchedulePackage.GROUPED_CHARTER_OUT_EVENT__LINKED_SEQUENCE:
				if (resolve) return getLinkedSequence();
				return basicGetLinkedSequence();
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
			case SchedulePackage.GROUPED_CHARTER_OUT_EVENT__EVENTS:
				getEvents().clear();
				getEvents().addAll((Collection<? extends Event>)newValue);
				return;
			case SchedulePackage.GROUPED_CHARTER_OUT_EVENT__GROUP_PROFIT_AND_LOSS:
				setGroupProfitAndLoss((GroupProfitAndLoss)newValue);
				return;
			case SchedulePackage.GROUPED_CHARTER_OUT_EVENT__GENERAL_PNL_DETAILS:
				getGeneralPNLDetails().clear();
				getGeneralPNLDetails().addAll((Collection<? extends GeneralPNLDetails>)newValue);
				return;
			case SchedulePackage.GROUPED_CHARTER_OUT_EVENT__LINKED_SEQUENCE:
				setLinkedSequence((Sequence)newValue);
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
			case SchedulePackage.GROUPED_CHARTER_OUT_EVENT__EVENTS:
				getEvents().clear();
				return;
			case SchedulePackage.GROUPED_CHARTER_OUT_EVENT__GROUP_PROFIT_AND_LOSS:
				setGroupProfitAndLoss((GroupProfitAndLoss)null);
				return;
			case SchedulePackage.GROUPED_CHARTER_OUT_EVENT__GENERAL_PNL_DETAILS:
				getGeneralPNLDetails().clear();
				return;
			case SchedulePackage.GROUPED_CHARTER_OUT_EVENT__LINKED_SEQUENCE:
				setLinkedSequence((Sequence)null);
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
			case SchedulePackage.GROUPED_CHARTER_OUT_EVENT__EVENTS:
				return events != null && !events.isEmpty();
			case SchedulePackage.GROUPED_CHARTER_OUT_EVENT__GROUP_PROFIT_AND_LOSS:
				return groupProfitAndLoss != null;
			case SchedulePackage.GROUPED_CHARTER_OUT_EVENT__GENERAL_PNL_DETAILS:
				return generalPNLDetails != null && !generalPNLDetails.isEmpty();
			case SchedulePackage.GROUPED_CHARTER_OUT_EVENT__LINKED_SEQUENCE:
				return linkedSequence != null;
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
		if (baseClass == EventGrouping.class) {
			switch (derivedFeatureID) {
				case SchedulePackage.GROUPED_CHARTER_OUT_EVENT__EVENTS: return SchedulePackage.EVENT_GROUPING__EVENTS;
				default: return -1;
			}
		}
		if (baseClass == ProfitAndLossContainer.class) {
			switch (derivedFeatureID) {
				case SchedulePackage.GROUPED_CHARTER_OUT_EVENT__GROUP_PROFIT_AND_LOSS: return SchedulePackage.PROFIT_AND_LOSS_CONTAINER__GROUP_PROFIT_AND_LOSS;
				case SchedulePackage.GROUPED_CHARTER_OUT_EVENT__GENERAL_PNL_DETAILS: return SchedulePackage.PROFIT_AND_LOSS_CONTAINER__GENERAL_PNL_DETAILS;
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
		if (baseClass == EventGrouping.class) {
			switch (baseFeatureID) {
				case SchedulePackage.EVENT_GROUPING__EVENTS: return SchedulePackage.GROUPED_CHARTER_OUT_EVENT__EVENTS;
				default: return -1;
			}
		}
		if (baseClass == ProfitAndLossContainer.class) {
			switch (baseFeatureID) {
				case SchedulePackage.PROFIT_AND_LOSS_CONTAINER__GROUP_PROFIT_AND_LOSS: return SchedulePackage.GROUPED_CHARTER_OUT_EVENT__GROUP_PROFIT_AND_LOSS;
				case SchedulePackage.PROFIT_AND_LOSS_CONTAINER__GENERAL_PNL_DETAILS: return SchedulePackage.GROUPED_CHARTER_OUT_EVENT__GENERAL_PNL_DETAILS;
				default: return -1;
			}
		}
		return super.eDerivedStructuralFeatureID(baseFeatureID, baseClass);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public String name() {

		String prefix = "Charter Out - ";
		final Sequence sequence = getLinkedSequence();
		prefix = prefix + " " + sequence.getName();

		if (getLinkedSequence().getVesselAvailability() != null) {
			return String.format("%s (%d)", prefix, getLinkedSequence().getVesselAvailability().getCharterNumber());
		} else if (getLinkedSequence().getCharterInMarket() != null) {
			return String.format("%s (%d)", prefix, getLinkedSequence().getSpotIndex() + 1);
		}
		return prefix;

	}
	
} //GroupedCharterOutEventImpl
