

/**
 */
package com.mmxlabs.models.lng.fleet.impl;
import java.util.Collection;

import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.fleet.ScenarioFleetModel;
import com.mmxlabs.models.lng.fleet.VesselAvailability;
import com.mmxlabs.models.lng.fleet.VesselEvent;
import com.mmxlabs.models.mmxcore.impl.UUIDObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Scenario Fleet Model</b></em>'.
 * @since 4.0
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.ScenarioFleetModelImpl#getVesselAvailabilities <em>Vessel Availabilities</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.ScenarioFleetModelImpl#getVesselEvents <em>Vessel Events</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ScenarioFleetModelImpl extends UUIDObjectImpl implements ScenarioFleetModel {
	/**
	 * The cached value of the '{@link #getVesselAvailabilities() <em>Vessel Availabilities</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVesselAvailabilities()
	 * @generated
	 * @ordered
	 */
	protected EList<VesselAvailability> vesselAvailabilities;

	/**
	 * The cached value of the '{@link #getVesselEvents() <em>Vessel Events</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVesselEvents()
	 * @generated
	 * @ordered
	 */
	protected EList<VesselEvent> vesselEvents;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ScenarioFleetModelImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return FleetPackage.Literals.SCENARIO_FLEET_MODEL;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<VesselAvailability> getVesselAvailabilities() {
		if (vesselAvailabilities == null) {
			vesselAvailabilities = new EObjectContainmentEList<VesselAvailability>(VesselAvailability.class, this, FleetPackage.SCENARIO_FLEET_MODEL__VESSEL_AVAILABILITIES);
		}
		return vesselAvailabilities;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<VesselEvent> getVesselEvents() {
		if (vesselEvents == null) {
			vesselEvents = new EObjectContainmentEList<VesselEvent>(VesselEvent.class, this, FleetPackage.SCENARIO_FLEET_MODEL__VESSEL_EVENTS);
		}
		return vesselEvents;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case FleetPackage.SCENARIO_FLEET_MODEL__VESSEL_AVAILABILITIES:
				return ((InternalEList<?>)getVesselAvailabilities()).basicRemove(otherEnd, msgs);
			case FleetPackage.SCENARIO_FLEET_MODEL__VESSEL_EVENTS:
				return ((InternalEList<?>)getVesselEvents()).basicRemove(otherEnd, msgs);
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
			case FleetPackage.SCENARIO_FLEET_MODEL__VESSEL_AVAILABILITIES:
				return getVesselAvailabilities();
			case FleetPackage.SCENARIO_FLEET_MODEL__VESSEL_EVENTS:
				return getVesselEvents();
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
			case FleetPackage.SCENARIO_FLEET_MODEL__VESSEL_AVAILABILITIES:
				getVesselAvailabilities().clear();
				getVesselAvailabilities().addAll((Collection<? extends VesselAvailability>)newValue);
				return;
			case FleetPackage.SCENARIO_FLEET_MODEL__VESSEL_EVENTS:
				getVesselEvents().clear();
				getVesselEvents().addAll((Collection<? extends VesselEvent>)newValue);
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
			case FleetPackage.SCENARIO_FLEET_MODEL__VESSEL_AVAILABILITIES:
				getVesselAvailabilities().clear();
				return;
			case FleetPackage.SCENARIO_FLEET_MODEL__VESSEL_EVENTS:
				getVesselEvents().clear();
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
			case FleetPackage.SCENARIO_FLEET_MODEL__VESSEL_AVAILABILITIES:
				return vesselAvailabilities != null && !vesselAvailabilities.isEmpty();
			case FleetPackage.SCENARIO_FLEET_MODEL__VESSEL_EVENTS:
				return vesselEvents != null && !vesselEvents.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //ScenarioFleetModelImpl

// finish type fixing

