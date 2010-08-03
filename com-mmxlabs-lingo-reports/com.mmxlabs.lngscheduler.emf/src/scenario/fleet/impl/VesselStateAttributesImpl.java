/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package scenario.fleet.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import scenario.fleet.FleetPackage;
import scenario.fleet.FuelConsumptionLine;
import scenario.fleet.VesselState;
import scenario.fleet.VesselStateAttributes;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Vessel State Attributes</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link scenario.fleet.impl.VesselStateAttributesImpl#getVesselState <em>Vessel State</em>}</li>
 *   <li>{@link scenario.fleet.impl.VesselStateAttributesImpl#getNboRate <em>Nbo Rate</em>}</li>
 *   <li>{@link scenario.fleet.impl.VesselStateAttributesImpl#getIdleNBORate <em>Idle NBO Rate</em>}</li>
 *   <li>{@link scenario.fleet.impl.VesselStateAttributesImpl#getIdleConsumptionRate <em>Idle Consumption Rate</em>}</li>
 *   <li>{@link scenario.fleet.impl.VesselStateAttributesImpl#getFuelConsumptionCurve <em>Fuel Consumption Curve</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class VesselStateAttributesImpl extends EObjectImpl implements VesselStateAttributes {
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
	 * The default value of the '{@link #getNboRate() <em>Nbo Rate</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNboRate()
	 * @generated
	 * @ordered
	 */
	protected static final int NBO_RATE_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getNboRate() <em>Nbo Rate</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNboRate()
	 * @generated
	 * @ordered
	 */
	protected int nboRate = NBO_RATE_EDEFAULT;

	/**
	 * The default value of the '{@link #getIdleNBORate() <em>Idle NBO Rate</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getIdleNBORate()
	 * @generated
	 * @ordered
	 */
	protected static final int IDLE_NBO_RATE_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getIdleNBORate() <em>Idle NBO Rate</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getIdleNBORate()
	 * @generated
	 * @ordered
	 */
	protected int idleNBORate = IDLE_NBO_RATE_EDEFAULT;

	/**
	 * The default value of the '{@link #getIdleConsumptionRate() <em>Idle Consumption Rate</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getIdleConsumptionRate()
	 * @generated
	 * @ordered
	 */
	protected static final int IDLE_CONSUMPTION_RATE_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getIdleConsumptionRate() <em>Idle Consumption Rate</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getIdleConsumptionRate()
	 * @generated
	 * @ordered
	 */
	protected int idleConsumptionRate = IDLE_CONSUMPTION_RATE_EDEFAULT;

	/**
	 * The cached value of the '{@link #getFuelConsumptionCurve() <em>Fuel Consumption Curve</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFuelConsumptionCurve()
	 * @generated
	 * @ordered
	 */
	protected EList<FuelConsumptionLine> fuelConsumptionCurve;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected VesselStateAttributesImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return FleetPackage.Literals.VESSEL_STATE_ATTRIBUTES;
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
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.VESSEL_STATE_ATTRIBUTES__VESSEL_STATE, oldVesselState, vesselState));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getNboRate() {
		return nboRate;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setNboRate(int newNboRate) {
		int oldNboRate = nboRate;
		nboRate = newNboRate;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.VESSEL_STATE_ATTRIBUTES__NBO_RATE, oldNboRate, nboRate));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getIdleNBORate() {
		return idleNBORate;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setIdleNBORate(int newIdleNBORate) {
		int oldIdleNBORate = idleNBORate;
		idleNBORate = newIdleNBORate;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.VESSEL_STATE_ATTRIBUTES__IDLE_NBO_RATE, oldIdleNBORate, idleNBORate));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getIdleConsumptionRate() {
		return idleConsumptionRate;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setIdleConsumptionRate(int newIdleConsumptionRate) {
		int oldIdleConsumptionRate = idleConsumptionRate;
		idleConsumptionRate = newIdleConsumptionRate;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.VESSEL_STATE_ATTRIBUTES__IDLE_CONSUMPTION_RATE, oldIdleConsumptionRate, idleConsumptionRate));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<FuelConsumptionLine> getFuelConsumptionCurve() {
		if (fuelConsumptionCurve == null) {
			fuelConsumptionCurve = new EObjectContainmentEList<FuelConsumptionLine>(FuelConsumptionLine.class, this, FleetPackage.VESSEL_STATE_ATTRIBUTES__FUEL_CONSUMPTION_CURVE);
		}
		return fuelConsumptionCurve;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case FleetPackage.VESSEL_STATE_ATTRIBUTES__FUEL_CONSUMPTION_CURVE:
				return ((InternalEList<?>)getFuelConsumptionCurve()).basicRemove(otherEnd, msgs);
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
			case FleetPackage.VESSEL_STATE_ATTRIBUTES__VESSEL_STATE:
				return getVesselState();
			case FleetPackage.VESSEL_STATE_ATTRIBUTES__NBO_RATE:
				return getNboRate();
			case FleetPackage.VESSEL_STATE_ATTRIBUTES__IDLE_NBO_RATE:
				return getIdleNBORate();
			case FleetPackage.VESSEL_STATE_ATTRIBUTES__IDLE_CONSUMPTION_RATE:
				return getIdleConsumptionRate();
			case FleetPackage.VESSEL_STATE_ATTRIBUTES__FUEL_CONSUMPTION_CURVE:
				return getFuelConsumptionCurve();
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
			case FleetPackage.VESSEL_STATE_ATTRIBUTES__VESSEL_STATE:
				setVesselState((VesselState)newValue);
				return;
			case FleetPackage.VESSEL_STATE_ATTRIBUTES__NBO_RATE:
				setNboRate((Integer)newValue);
				return;
			case FleetPackage.VESSEL_STATE_ATTRIBUTES__IDLE_NBO_RATE:
				setIdleNBORate((Integer)newValue);
				return;
			case FleetPackage.VESSEL_STATE_ATTRIBUTES__IDLE_CONSUMPTION_RATE:
				setIdleConsumptionRate((Integer)newValue);
				return;
			case FleetPackage.VESSEL_STATE_ATTRIBUTES__FUEL_CONSUMPTION_CURVE:
				getFuelConsumptionCurve().clear();
				getFuelConsumptionCurve().addAll((Collection<? extends FuelConsumptionLine>)newValue);
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
			case FleetPackage.VESSEL_STATE_ATTRIBUTES__VESSEL_STATE:
				setVesselState(VESSEL_STATE_EDEFAULT);
				return;
			case FleetPackage.VESSEL_STATE_ATTRIBUTES__NBO_RATE:
				setNboRate(NBO_RATE_EDEFAULT);
				return;
			case FleetPackage.VESSEL_STATE_ATTRIBUTES__IDLE_NBO_RATE:
				setIdleNBORate(IDLE_NBO_RATE_EDEFAULT);
				return;
			case FleetPackage.VESSEL_STATE_ATTRIBUTES__IDLE_CONSUMPTION_RATE:
				setIdleConsumptionRate(IDLE_CONSUMPTION_RATE_EDEFAULT);
				return;
			case FleetPackage.VESSEL_STATE_ATTRIBUTES__FUEL_CONSUMPTION_CURVE:
				getFuelConsumptionCurve().clear();
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
			case FleetPackage.VESSEL_STATE_ATTRIBUTES__VESSEL_STATE:
				return vesselState != VESSEL_STATE_EDEFAULT;
			case FleetPackage.VESSEL_STATE_ATTRIBUTES__NBO_RATE:
				return nboRate != NBO_RATE_EDEFAULT;
			case FleetPackage.VESSEL_STATE_ATTRIBUTES__IDLE_NBO_RATE:
				return idleNBORate != IDLE_NBO_RATE_EDEFAULT;
			case FleetPackage.VESSEL_STATE_ATTRIBUTES__IDLE_CONSUMPTION_RATE:
				return idleConsumptionRate != IDLE_CONSUMPTION_RATE_EDEFAULT;
			case FleetPackage.VESSEL_STATE_ATTRIBUTES__FUEL_CONSUMPTION_CURVE:
				return fuelConsumptionCurve != null && !fuelConsumptionCurve.isEmpty();
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
		result.append(" (vesselState: ");
		result.append(vesselState);
		result.append(", nboRate: ");
		result.append(nboRate);
		result.append(", idleNBORate: ");
		result.append(idleNBORate);
		result.append(", idleConsumptionRate: ");
		result.append(idleConsumptionRate);
		result.append(')');
		return result.toString();
	}

} //VesselStateAttributesImpl
