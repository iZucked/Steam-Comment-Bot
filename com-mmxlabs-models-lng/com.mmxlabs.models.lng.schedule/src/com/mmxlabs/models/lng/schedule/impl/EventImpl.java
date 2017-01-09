/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2017
 * All rights reserved.
 */
package com.mmxlabs.models.lng.schedule.impl;
import java.lang.reflect.InvocationTargetException;
import java.time.ZonedDateTime;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;

import com.mmxlabs.common.time.Hours;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.SchedulePackage;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.types.ITimezoneProvider;
import com.mmxlabs.models.lng.types.TypesPackage;
import com.mmxlabs.models.mmxcore.impl.MMXObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Event</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.EventImpl#getStart <em>Start</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.EventImpl#getEnd <em>End</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.EventImpl#getPort <em>Port</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.EventImpl#getPreviousEvent <em>Previous Event</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.EventImpl#getNextEvent <em>Next Event</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.EventImpl#getSequence <em>Sequence</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.EventImpl#getCharterCost <em>Charter Cost</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.EventImpl#getHeelAtStart <em>Heel At Start</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.schedule.impl.EventImpl#getHeelAtEnd <em>Heel At End</em>}</li>
 * </ul>
 *
 * @generated
 */
public class EventImpl extends MMXObjectImpl implements Event {
	/**
	 * The default value of the '{@link #getStart() <em>Start</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStart()
	 * @generated
	 * @ordered
	 */
	protected static final ZonedDateTime START_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getStart() <em>Start</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStart()
	 * @generated
	 * @ordered
	 */
	protected ZonedDateTime start = START_EDEFAULT;

	/**
	 * The default value of the '{@link #getEnd() <em>End</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEnd()
	 * @generated
	 * @ordered
	 */
	protected static final ZonedDateTime END_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getEnd() <em>End</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEnd()
	 * @generated
	 * @ordered
	 */
	protected ZonedDateTime end = END_EDEFAULT;

	/**
	 * The cached value of the '{@link #getPort() <em>Port</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPort()
	 * @generated
	 * @ordered
	 */
	protected Port port;

	/**
	 * The cached value of the '{@link #getPreviousEvent() <em>Previous Event</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPreviousEvent()
	 * @generated
	 * @ordered
	 */
	protected Event previousEvent;

	/**
	 * The cached value of the '{@link #getNextEvent() <em>Next Event</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNextEvent()
	 * @generated
	 * @ordered
	 */
	protected Event nextEvent;

	/**
	 * The default value of the '{@link #getCharterCost() <em>Charter Cost</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCharterCost()
	 * @generated
	 * @ordered
	 */
	protected static final int CHARTER_COST_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getCharterCost() <em>Charter Cost</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCharterCost()
	 * @generated
	 * @ordered
	 */
	protected int charterCost = CHARTER_COST_EDEFAULT;

	/**
	 * The default value of the '{@link #getHeelAtStart() <em>Heel At Start</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getHeelAtStart()
	 * @generated
	 * @ordered
	 */
	protected static final int HEEL_AT_START_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getHeelAtStart() <em>Heel At Start</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getHeelAtStart()
	 * @generated
	 * @ordered
	 */
	protected int heelAtStart = HEEL_AT_START_EDEFAULT;

	/**
	 * The default value of the '{@link #getHeelAtEnd() <em>Heel At End</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getHeelAtEnd()
	 * @generated
	 * @ordered
	 */
	protected static final int HEEL_AT_END_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getHeelAtEnd() <em>Heel At End</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getHeelAtEnd()
	 * @generated
	 * @ordered
	 */
	protected int heelAtEnd = HEEL_AT_END_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EventImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return SchedulePackage.Literals.EVENT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ZonedDateTime getStart() {
		return start;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setStart(ZonedDateTime newStart) {
		ZonedDateTime oldStart = start;
		start = newStart;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.EVENT__START, oldStart, start));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ZonedDateTime getEnd() {
		return end;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setEnd(ZonedDateTime newEnd) {
		ZonedDateTime oldEnd = end;
		end = newEnd;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.EVENT__END, oldEnd, end));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Port getPort() {
		if (port != null && port.eIsProxy()) {
			InternalEObject oldPort = (InternalEObject)port;
			port = (Port)eResolveProxy(oldPort);
			if (port != oldPort) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, SchedulePackage.EVENT__PORT, oldPort, port));
			}
		}
		return port;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Port basicGetPort() {
		return port;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setPort(Port newPort) {
		Port oldPort = port;
		port = newPort;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.EVENT__PORT, oldPort, port));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Event getPreviousEvent() {
		if (previousEvent != null && previousEvent.eIsProxy()) {
			InternalEObject oldPreviousEvent = (InternalEObject)previousEvent;
			previousEvent = (Event)eResolveProxy(oldPreviousEvent);
			if (previousEvent != oldPreviousEvent) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, SchedulePackage.EVENT__PREVIOUS_EVENT, oldPreviousEvent, previousEvent));
			}
		}
		return previousEvent;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Event basicGetPreviousEvent() {
		return previousEvent;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetPreviousEvent(Event newPreviousEvent, NotificationChain msgs) {
		Event oldPreviousEvent = previousEvent;
		previousEvent = newPreviousEvent;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, SchedulePackage.EVENT__PREVIOUS_EVENT, oldPreviousEvent, newPreviousEvent);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setPreviousEvent(Event newPreviousEvent) {
		if (newPreviousEvent != previousEvent) {
			NotificationChain msgs = null;
			if (previousEvent != null)
				msgs = ((InternalEObject)previousEvent).eInverseRemove(this, SchedulePackage.EVENT__NEXT_EVENT, Event.class, msgs);
			if (newPreviousEvent != null)
				msgs = ((InternalEObject)newPreviousEvent).eInverseAdd(this, SchedulePackage.EVENT__NEXT_EVENT, Event.class, msgs);
			msgs = basicSetPreviousEvent(newPreviousEvent, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.EVENT__PREVIOUS_EVENT, newPreviousEvent, newPreviousEvent));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Event getNextEvent() {
		if (nextEvent != null && nextEvent.eIsProxy()) {
			InternalEObject oldNextEvent = (InternalEObject)nextEvent;
			nextEvent = (Event)eResolveProxy(oldNextEvent);
			if (nextEvent != oldNextEvent) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, SchedulePackage.EVENT__NEXT_EVENT, oldNextEvent, nextEvent));
			}
		}
		return nextEvent;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Event basicGetNextEvent() {
		return nextEvent;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetNextEvent(Event newNextEvent, NotificationChain msgs) {
		Event oldNextEvent = nextEvent;
		nextEvent = newNextEvent;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, SchedulePackage.EVENT__NEXT_EVENT, oldNextEvent, newNextEvent);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setNextEvent(Event newNextEvent) {
		if (newNextEvent != nextEvent) {
			NotificationChain msgs = null;
			if (nextEvent != null)
				msgs = ((InternalEObject)nextEvent).eInverseRemove(this, SchedulePackage.EVENT__PREVIOUS_EVENT, Event.class, msgs);
			if (newNextEvent != null)
				msgs = ((InternalEObject)newNextEvent).eInverseAdd(this, SchedulePackage.EVENT__PREVIOUS_EVENT, Event.class, msgs);
			msgs = basicSetNextEvent(newNextEvent, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.EVENT__NEXT_EVENT, newNextEvent, newNextEvent));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Sequence getSequence() {
		if (eContainerFeatureID() != SchedulePackage.EVENT__SEQUENCE) return null;
		return (Sequence)eInternalContainer();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetSequence(Sequence newSequence, NotificationChain msgs) {
		msgs = eBasicSetContainer((InternalEObject)newSequence, SchedulePackage.EVENT__SEQUENCE, msgs);
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSequence(Sequence newSequence) {
		if (newSequence != eInternalContainer() || (eContainerFeatureID() != SchedulePackage.EVENT__SEQUENCE && newSequence != null)) {
			if (EcoreUtil.isAncestor(this, newSequence))
				throw new IllegalArgumentException("Recursive containment not allowed for " + toString());
			NotificationChain msgs = null;
			if (eInternalContainer() != null)
				msgs = eBasicRemoveFromContainer(msgs);
			if (newSequence != null)
				msgs = ((InternalEObject)newSequence).eInverseAdd(this, SchedulePackage.SEQUENCE__EVENTS, Sequence.class, msgs);
			msgs = basicSetSequence(newSequence, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.EVENT__SEQUENCE, newSequence, newSequence));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getCharterCost() {
		return charterCost;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setCharterCost(int newCharterCost) {
		int oldCharterCost = charterCost;
		charterCost = newCharterCost;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.EVENT__CHARTER_COST, oldCharterCost, charterCost));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getHeelAtStart() {
		return heelAtStart;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setHeelAtStart(int newHeelAtStart) {
		int oldHeelAtStart = heelAtStart;
		heelAtStart = newHeelAtStart;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.EVENT__HEEL_AT_START, oldHeelAtStart, heelAtStart));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getHeelAtEnd() {
		return heelAtEnd;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setHeelAtEnd(int newHeelAtEnd) {
		int oldHeelAtEnd = heelAtEnd;
		heelAtEnd = newHeelAtEnd;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, SchedulePackage.EVENT__HEEL_AT_END, oldHeelAtEnd, heelAtEnd));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public int getDuration() {
		return Hours.between(getStart(),  getEnd());
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public String type() {
		return "Event";
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public String name() {
		return "";
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public String getTimeZone(final EAttribute attribute) {
		Port p = getPort();
		if (p == null) return "UTC";
		if (p.getTimeZone() == null) return "UTC";
		if (p.getTimeZone().isEmpty()) return "UTC";
		return p.getTimeZone();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case SchedulePackage.EVENT__PREVIOUS_EVENT:
				if (previousEvent != null)
					msgs = ((InternalEObject)previousEvent).eInverseRemove(this, SchedulePackage.EVENT__NEXT_EVENT, Event.class, msgs);
				return basicSetPreviousEvent((Event)otherEnd, msgs);
			case SchedulePackage.EVENT__NEXT_EVENT:
				if (nextEvent != null)
					msgs = ((InternalEObject)nextEvent).eInverseRemove(this, SchedulePackage.EVENT__PREVIOUS_EVENT, Event.class, msgs);
				return basicSetNextEvent((Event)otherEnd, msgs);
			case SchedulePackage.EVENT__SEQUENCE:
				if (eInternalContainer() != null)
					msgs = eBasicRemoveFromContainer(msgs);
				return basicSetSequence((Sequence)otherEnd, msgs);
		}
		return super.eInverseAdd(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case SchedulePackage.EVENT__PREVIOUS_EVENT:
				return basicSetPreviousEvent(null, msgs);
			case SchedulePackage.EVENT__NEXT_EVENT:
				return basicSetNextEvent(null, msgs);
			case SchedulePackage.EVENT__SEQUENCE:
				return basicSetSequence(null, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eBasicRemoveFromContainerFeature(NotificationChain msgs) {
		switch (eContainerFeatureID()) {
			case SchedulePackage.EVENT__SEQUENCE:
				return eInternalContainer().eInverseRemove(this, SchedulePackage.SEQUENCE__EVENTS, Sequence.class, msgs);
		}
		return super.eBasicRemoveFromContainerFeature(msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case SchedulePackage.EVENT__START:
				return getStart();
			case SchedulePackage.EVENT__END:
				return getEnd();
			case SchedulePackage.EVENT__PORT:
				if (resolve) return getPort();
				return basicGetPort();
			case SchedulePackage.EVENT__PREVIOUS_EVENT:
				if (resolve) return getPreviousEvent();
				return basicGetPreviousEvent();
			case SchedulePackage.EVENT__NEXT_EVENT:
				if (resolve) return getNextEvent();
				return basicGetNextEvent();
			case SchedulePackage.EVENT__SEQUENCE:
				return getSequence();
			case SchedulePackage.EVENT__CHARTER_COST:
				return getCharterCost();
			case SchedulePackage.EVENT__HEEL_AT_START:
				return getHeelAtStart();
			case SchedulePackage.EVENT__HEEL_AT_END:
				return getHeelAtEnd();
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
			case SchedulePackage.EVENT__START:
				setStart((ZonedDateTime)newValue);
				return;
			case SchedulePackage.EVENT__END:
				setEnd((ZonedDateTime)newValue);
				return;
			case SchedulePackage.EVENT__PORT:
				setPort((Port)newValue);
				return;
			case SchedulePackage.EVENT__PREVIOUS_EVENT:
				setPreviousEvent((Event)newValue);
				return;
			case SchedulePackage.EVENT__NEXT_EVENT:
				setNextEvent((Event)newValue);
				return;
			case SchedulePackage.EVENT__SEQUENCE:
				setSequence((Sequence)newValue);
				return;
			case SchedulePackage.EVENT__CHARTER_COST:
				setCharterCost((Integer)newValue);
				return;
			case SchedulePackage.EVENT__HEEL_AT_START:
				setHeelAtStart((Integer)newValue);
				return;
			case SchedulePackage.EVENT__HEEL_AT_END:
				setHeelAtEnd((Integer)newValue);
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
			case SchedulePackage.EVENT__START:
				setStart(START_EDEFAULT);
				return;
			case SchedulePackage.EVENT__END:
				setEnd(END_EDEFAULT);
				return;
			case SchedulePackage.EVENT__PORT:
				setPort((Port)null);
				return;
			case SchedulePackage.EVENT__PREVIOUS_EVENT:
				setPreviousEvent((Event)null);
				return;
			case SchedulePackage.EVENT__NEXT_EVENT:
				setNextEvent((Event)null);
				return;
			case SchedulePackage.EVENT__SEQUENCE:
				setSequence((Sequence)null);
				return;
			case SchedulePackage.EVENT__CHARTER_COST:
				setCharterCost(CHARTER_COST_EDEFAULT);
				return;
			case SchedulePackage.EVENT__HEEL_AT_START:
				setHeelAtStart(HEEL_AT_START_EDEFAULT);
				return;
			case SchedulePackage.EVENT__HEEL_AT_END:
				setHeelAtEnd(HEEL_AT_END_EDEFAULT);
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
			case SchedulePackage.EVENT__START:
				return START_EDEFAULT == null ? start != null : !START_EDEFAULT.equals(start);
			case SchedulePackage.EVENT__END:
				return END_EDEFAULT == null ? end != null : !END_EDEFAULT.equals(end);
			case SchedulePackage.EVENT__PORT:
				return port != null;
			case SchedulePackage.EVENT__PREVIOUS_EVENT:
				return previousEvent != null;
			case SchedulePackage.EVENT__NEXT_EVENT:
				return nextEvent != null;
			case SchedulePackage.EVENT__SEQUENCE:
				return getSequence() != null;
			case SchedulePackage.EVENT__CHARTER_COST:
				return charterCost != CHARTER_COST_EDEFAULT;
			case SchedulePackage.EVENT__HEEL_AT_START:
				return heelAtStart != HEEL_AT_START_EDEFAULT;
			case SchedulePackage.EVENT__HEEL_AT_END:
				return heelAtEnd != HEEL_AT_END_EDEFAULT;
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int eDerivedOperationID(int baseOperationID, Class<?> baseClass) {
		if (baseClass == ITimezoneProvider.class) {
			switch (baseOperationID) {
				case TypesPackage.ITIMEZONE_PROVIDER___GET_TIME_ZONE__EATTRIBUTE: return SchedulePackage.EVENT___GET_TIME_ZONE__EATTRIBUTE;
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
			case SchedulePackage.EVENT___GET_DURATION:
				return getDuration();
			case SchedulePackage.EVENT___TYPE:
				return type();
			case SchedulePackage.EVENT___NAME:
				return name();
			case SchedulePackage.EVENT___GET_TIME_ZONE__EATTRIBUTE:
				return getTimeZone((EAttribute)arguments.get(0));
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
		result.append(" (start: ");
		result.append(start);
		result.append(", end: ");
		result.append(end);
		result.append(", charterCost: ");
		result.append(charterCost);
		result.append(", heelAtStart: ");
		result.append(heelAtStart);
		result.append(", heelAtEnd: ");
		result.append(heelAtEnd);
		result.append(')');
		return result.toString();
	}

} // end of EventImpl

// finish type fixing
