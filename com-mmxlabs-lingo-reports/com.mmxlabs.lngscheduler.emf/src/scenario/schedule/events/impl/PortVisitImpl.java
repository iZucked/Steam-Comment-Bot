/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package scenario.schedule.events.impl;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

import scenario.port.Port;
import scenario.schedule.events.EventsPackage;
import scenario.schedule.events.PortVisit;
import scenario.schedule.events.ScheduledEvent;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Port Visit</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link scenario.schedule.events.impl.PortVisitImpl#getPort <em>Port</em>}</li>
 * </ul>
 * </p>
 * 
 * @generated
 */
public class PortVisitImpl extends ScheduledEventImpl implements PortVisit {
	/**
	 * The cached value of the '{@link #getPort() <em>Port</em>}' reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getPort()
	 * @generated
	 * @ordered
	 */
	protected Port port;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected PortVisitImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return EventsPackage.Literals.PORT_VISIT;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public Port getPort() {
		if ((port != null) && port.eIsProxy()) {
			final InternalEObject oldPort = (InternalEObject) port;
			port = (Port) eResolveProxy(oldPort);
			if (port != oldPort) {
				if (eNotificationRequired()) {
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, EventsPackage.PORT_VISIT__PORT, oldPort, port));
				}
			}
		}
		return port;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Port basicGetPort() {
		return port;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void setPort(final Port newPort) {
		final Port oldPort = port;
		port = newPort;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, EventsPackage.PORT_VISIT__PORT, oldPort, port));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public Object getLocalStartTime() {
		final java.util.Calendar calendar = java.util.Calendar.getInstance(java.util.TimeZone.getTimeZone(getPort().getTimeZone()));
		calendar.setTime(getStartTime());
		return calendar;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public Object getLocalEndTime() {
		final java.util.Calendar calendar = java.util.Calendar.getInstance(java.util.TimeZone.getTimeZone(getPort().getTimeZone()));
		calendar.setTime(getEndTime());
		return calendar;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public String getId() {
		return "visit to " + port.getName();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public String getDisplayTypeName() {
		return eClass().getName().replace("Visit", "");
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public Object eGet(final int featureID, final boolean resolve, final boolean coreType) {
		switch (featureID) {
		case EventsPackage.PORT_VISIT__PORT:
			if (resolve) {
				return getPort();
			}
			return basicGetPort();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void eSet(final int featureID, final Object newValue) {
		switch (featureID) {
		case EventsPackage.PORT_VISIT__PORT:
			setPort((Port) newValue);
			return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void eUnset(final int featureID) {
		switch (featureID) {
		case EventsPackage.PORT_VISIT__PORT:
			setPort((Port) null);
			return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public boolean eIsSet(final int featureID) {
		switch (featureID) {
		case EventsPackage.PORT_VISIT__PORT:
			return port != null;
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public int eDerivedOperationID(final int baseOperationID, final Class<?> baseClass) {
		if (baseClass == ScheduledEvent.class) {
			switch (baseOperationID) {
			case EventsPackage.SCHEDULED_EVENT___GET_LOCAL_START_TIME:
				return EventsPackage.PORT_VISIT___GET_LOCAL_START_TIME;
			case EventsPackage.SCHEDULED_EVENT___GET_LOCAL_END_TIME:
				return EventsPackage.PORT_VISIT___GET_LOCAL_END_TIME;
			case EventsPackage.SCHEDULED_EVENT___GET_DISPLAY_TYPE_NAME:
				return EventsPackage.PORT_VISIT___GET_DISPLAY_TYPE_NAME;
			default:
				return super.eDerivedOperationID(baseOperationID, baseClass);
			}
		}
		return super.eDerivedOperationID(baseOperationID, baseClass);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public Object eInvoke(final int operationID, final EList<?> arguments) throws InvocationTargetException {
		switch (operationID) {
		case EventsPackage.PORT_VISIT___GET_LOCAL_START_TIME:
			return getLocalStartTime();
		case EventsPackage.PORT_VISIT___GET_LOCAL_END_TIME:
			return getLocalEndTime();
		case EventsPackage.PORT_VISIT___GET_ID:
			return getId();
		case EventsPackage.PORT_VISIT___GET_DISPLAY_TYPE_NAME:
			return getDisplayTypeName();
		}
		return super.eInvoke(operationID, arguments);
	}

} // PortVisitImpl
