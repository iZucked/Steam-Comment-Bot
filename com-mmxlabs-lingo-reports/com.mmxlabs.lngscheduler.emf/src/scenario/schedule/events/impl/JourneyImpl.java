/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package scenario.schedule.events.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import scenario.fleet.VesselState;

import scenario.port.Port;

import scenario.schedule.events.EventsPackage;
import scenario.schedule.events.FuelMixture;
import scenario.schedule.events.FuelQuantity;
import scenario.schedule.events.Journey;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Journey</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link scenario.schedule.events.impl.JourneyImpl#getFuelUsage <em>Fuel Usage</em>}</li>
 *   <li>{@link scenario.schedule.events.impl.JourneyImpl#getToPort <em>To Port</em>}</li>
 *   <li>{@link scenario.schedule.events.impl.JourneyImpl#getVesselState <em>Vessel State</em>}</li>
 *   <li>{@link scenario.schedule.events.impl.JourneyImpl#getRoute <em>Route</em>}</li>
 *   <li>{@link scenario.schedule.events.impl.JourneyImpl#getSpeed <em>Speed</em>}</li>
 *   <li>{@link scenario.schedule.events.impl.JourneyImpl#getDistance <em>Distance</em>}</li>
 *   <li>{@link scenario.schedule.events.impl.JourneyImpl#getFromPort <em>From Port</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class JourneyImpl extends ScheduledEventImpl implements Journey {
	/**
	 * The cached value of the '{@link #getFuelUsage() <em>Fuel Usage</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFuelUsage()
	 * @generated
	 * @ordered
	 */
	protected EList<FuelQuantity> fuelUsage;

	/**
	 * The cached value of the '{@link #getToPort() <em>To Port</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getToPort()
	 * @generated
	 * @ordered
	 */
	protected Port toPort;

	/**
	 * The default value of the '{@link #getVesselState() <em>Vessel State</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVesselState()
	 * @generated
	 * @ordered
	 */
	protected static final VesselState VESSEL_STATE_EDEFAULT = VesselState.LADEN;

	/**
	 * The cached value of the '{@link #getVesselState() <em>Vessel State</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVesselState()
	 * @generated
	 * @ordered
	 */
	protected VesselState vesselState = VESSEL_STATE_EDEFAULT;

	/**
	 * The default value of the '{@link #getRoute() <em>Route</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRoute()
	 * @generated
	 * @ordered
	 */
	protected static final String ROUTE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getRoute() <em>Route</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRoute()
	 * @generated
	 * @ordered
	 */
	protected String route = ROUTE_EDEFAULT;

	/**
	 * The default value of the '{@link #getSpeed() <em>Speed</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSpeed()
	 * @generated
	 * @ordered
	 */
	protected static final double SPEED_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getSpeed() <em>Speed</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSpeed()
	 * @generated
	 * @ordered
	 */
	protected double speed = SPEED_EDEFAULT;

	/**
	 * The default value of the '{@link #getDistance() <em>Distance</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDistance()
	 * @generated
	 * @ordered
	 */
	protected static final int DISTANCE_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getDistance() <em>Distance</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDistance()
	 * @generated
	 * @ordered
	 */
	protected int distance = DISTANCE_EDEFAULT;

	/**
	 * The cached value of the '{@link #getFromPort() <em>From Port</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFromPort()
	 * @generated
	 * @ordered
	 */
	protected Port fromPort;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected JourneyImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return EventsPackage.Literals.JOURNEY;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<FuelQuantity> getFuelUsage() {
		if (fuelUsage == null) {
			fuelUsage = new EObjectContainmentEList<FuelQuantity>(FuelQuantity.class, this, EventsPackage.JOURNEY__FUEL_USAGE);
		}
		return fuelUsage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Port getToPort() {
		if (toPort != null && toPort.eIsProxy()) {
			InternalEObject oldToPort = (InternalEObject)toPort;
			toPort = (Port)eResolveProxy(oldToPort);
			if (toPort != oldToPort) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, EventsPackage.JOURNEY__TO_PORT, oldToPort, toPort));
			}
		}
		return toPort;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Port basicGetToPort() {
		return toPort;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setToPort(Port newToPort) {
		Port oldToPort = toPort;
		toPort = newToPort;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, EventsPackage.JOURNEY__TO_PORT, oldToPort, toPort));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public VesselState getVesselState() {
		return vesselState;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setVesselState(VesselState newVesselState) {
		VesselState oldVesselState = vesselState;
		vesselState = newVesselState == null ? VESSEL_STATE_EDEFAULT : newVesselState;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, EventsPackage.JOURNEY__VESSEL_STATE, oldVesselState, vesselState));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getRoute() {
		return route;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setRoute(String newRoute) {
		String oldRoute = route;
		route = newRoute;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, EventsPackage.JOURNEY__ROUTE, oldRoute, route));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public double getSpeed() {
		return speed;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSpeed(double newSpeed) {
		double oldSpeed = speed;
		speed = newSpeed;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, EventsPackage.JOURNEY__SPEED, oldSpeed, speed));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getDistance() {
		return distance;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDistance(int newDistance) {
		int oldDistance = distance;
		distance = newDistance;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, EventsPackage.JOURNEY__DISTANCE, oldDistance, distance));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Port getFromPort() {
		if (fromPort != null && fromPort.eIsProxy()) {
			InternalEObject oldFromPort = (InternalEObject)fromPort;
			fromPort = (Port)eResolveProxy(oldFromPort);
			if (fromPort != oldFromPort) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, EventsPackage.JOURNEY__FROM_PORT, oldFromPort, fromPort));
			}
		}
		return fromPort;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Port basicGetFromPort() {
		return fromPort;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setFromPort(Port newFromPort) {
		Port oldFromPort = fromPort;
		fromPort = newFromPort;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, EventsPackage.JOURNEY__FROM_PORT, oldFromPort, fromPort));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case EventsPackage.JOURNEY__FUEL_USAGE:
				return ((InternalEList<?>)getFuelUsage()).basicRemove(otherEnd, msgs);
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
			case EventsPackage.JOURNEY__FUEL_USAGE:
				return getFuelUsage();
			case EventsPackage.JOURNEY__TO_PORT:
				if (resolve) return getToPort();
				return basicGetToPort();
			case EventsPackage.JOURNEY__VESSEL_STATE:
				return getVesselState();
			case EventsPackage.JOURNEY__ROUTE:
				return getRoute();
			case EventsPackage.JOURNEY__SPEED:
				return getSpeed();
			case EventsPackage.JOURNEY__DISTANCE:
				return getDistance();
			case EventsPackage.JOURNEY__FROM_PORT:
				if (resolve) return getFromPort();
				return basicGetFromPort();
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
			case EventsPackage.JOURNEY__FUEL_USAGE:
				getFuelUsage().clear();
				getFuelUsage().addAll((Collection<? extends FuelQuantity>)newValue);
				return;
			case EventsPackage.JOURNEY__TO_PORT:
				setToPort((Port)newValue);
				return;
			case EventsPackage.JOURNEY__VESSEL_STATE:
				setVesselState((VesselState)newValue);
				return;
			case EventsPackage.JOURNEY__ROUTE:
				setRoute((String)newValue);
				return;
			case EventsPackage.JOURNEY__SPEED:
				setSpeed((Double)newValue);
				return;
			case EventsPackage.JOURNEY__DISTANCE:
				setDistance((Integer)newValue);
				return;
			case EventsPackage.JOURNEY__FROM_PORT:
				setFromPort((Port)newValue);
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
			case EventsPackage.JOURNEY__FUEL_USAGE:
				getFuelUsage().clear();
				return;
			case EventsPackage.JOURNEY__TO_PORT:
				setToPort((Port)null);
				return;
			case EventsPackage.JOURNEY__VESSEL_STATE:
				setVesselState(VESSEL_STATE_EDEFAULT);
				return;
			case EventsPackage.JOURNEY__ROUTE:
				setRoute(ROUTE_EDEFAULT);
				return;
			case EventsPackage.JOURNEY__SPEED:
				setSpeed(SPEED_EDEFAULT);
				return;
			case EventsPackage.JOURNEY__DISTANCE:
				setDistance(DISTANCE_EDEFAULT);
				return;
			case EventsPackage.JOURNEY__FROM_PORT:
				setFromPort((Port)null);
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
			case EventsPackage.JOURNEY__FUEL_USAGE:
				return fuelUsage != null && !fuelUsage.isEmpty();
			case EventsPackage.JOURNEY__TO_PORT:
				return toPort != null;
			case EventsPackage.JOURNEY__VESSEL_STATE:
				return vesselState != VESSEL_STATE_EDEFAULT;
			case EventsPackage.JOURNEY__ROUTE:
				return ROUTE_EDEFAULT == null ? route != null : !ROUTE_EDEFAULT.equals(route);
			case EventsPackage.JOURNEY__SPEED:
				return speed != SPEED_EDEFAULT;
			case EventsPackage.JOURNEY__DISTANCE:
				return distance != DISTANCE_EDEFAULT;
			case EventsPackage.JOURNEY__FROM_PORT:
				return fromPort != null;
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
		if (baseClass == FuelMixture.class) {
			switch (derivedFeatureID) {
				case EventsPackage.JOURNEY__FUEL_USAGE: return EventsPackage.FUEL_MIXTURE__FUEL_USAGE;
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
		if (baseClass == FuelMixture.class) {
			switch (baseFeatureID) {
				case EventsPackage.FUEL_MIXTURE__FUEL_USAGE: return EventsPackage.JOURNEY__FUEL_USAGE;
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
		result.append(" (vesselState: ");
		result.append(vesselState);
		result.append(", route: ");
		result.append(route);
		result.append(", speed: ");
		result.append(speed);
		result.append(", distance: ");
		result.append(distance);
		result.append(')');
		return result.toString();
	}

} //JourneyImpl
