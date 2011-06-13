/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package scenario.schedule.events.impl;

import java.lang.reflect.InvocationTargetException;

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

import scenario.schedule.events.EventsPackage;
import scenario.schedule.events.FuelMixture;
import scenario.schedule.events.FuelQuantity;
import scenario.schedule.events.Idle;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Idle</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link scenario.schedule.events.impl.IdleImpl#getFuelUsage <em>Fuel Usage</em>}</li>
 *   <li>{@link scenario.schedule.events.impl.IdleImpl#getVesselState <em>Vessel State</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class IdleImpl extends PortVisitImpl implements Idle {
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
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected IdleImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return EventsPackage.Literals.IDLE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<FuelQuantity> getFuelUsage() {
		if (fuelUsage == null) {
			fuelUsage = new EObjectContainmentEList<FuelQuantity>(FuelQuantity.class, this, EventsPackage.IDLE__FUEL_USAGE);
		}
		return fuelUsage;
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
			eNotify(new ENotificationImpl(this, Notification.SET, EventsPackage.IDLE__VESSEL_STATE, oldVesselState, vesselState));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public long getTotalCost() {
		return getTotalFuelCost() + getHireCost();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public long getTotalFuelCost() {
		long totalCost = 0;
		
		for (final FuelQuantity quantity : getFuelUsage()) {
			totalCost += quantity.getTotalPrice();
		}
		
		return totalCost;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case EventsPackage.IDLE__FUEL_USAGE:
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
			case EventsPackage.IDLE__FUEL_USAGE:
				return getFuelUsage();
			case EventsPackage.IDLE__VESSEL_STATE:
				return getVesselState();
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
			case EventsPackage.IDLE__FUEL_USAGE:
				getFuelUsage().clear();
				getFuelUsage().addAll((Collection<? extends FuelQuantity>)newValue);
				return;
			case EventsPackage.IDLE__VESSEL_STATE:
				setVesselState((VesselState)newValue);
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
			case EventsPackage.IDLE__FUEL_USAGE:
				getFuelUsage().clear();
				return;
			case EventsPackage.IDLE__VESSEL_STATE:
				setVesselState(VESSEL_STATE_EDEFAULT);
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
			case EventsPackage.IDLE__FUEL_USAGE:
				return fuelUsage != null && !fuelUsage.isEmpty();
			case EventsPackage.IDLE__VESSEL_STATE:
				return vesselState != VESSEL_STATE_EDEFAULT;
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
				case EventsPackage.IDLE__FUEL_USAGE: return EventsPackage.FUEL_MIXTURE__FUEL_USAGE;
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
				case EventsPackage.FUEL_MIXTURE__FUEL_USAGE: return EventsPackage.IDLE__FUEL_USAGE;
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
		if (baseClass == FuelMixture.class) {
			switch (baseOperationID) {
				case EventsPackage.FUEL_MIXTURE___GET_TOTAL_FUEL_COST: return EventsPackage.IDLE___GET_TOTAL_FUEL_COST;
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
			case EventsPackage.IDLE___GET_TOTAL_COST:
				return getTotalCost();
			case EventsPackage.IDLE___GET_TOTAL_FUEL_COST:
				return getTotalFuelCost();
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
		result.append(" (vesselState: ");
		result.append(vesselState);
		result.append(')');
		return result.toString();
	}

} //IdleImpl
