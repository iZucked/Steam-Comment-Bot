/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.fleet.impl;
import com.mmxlabs.models.lng.fleet.BaseFuel;
import com.mmxlabs.models.lng.fleet.FleetModel;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.fleet.ScenarioFleetModel;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.fleet.VesselGroup;
import com.mmxlabs.models.lng.fleet.VesselTypeGroup;
import com.mmxlabs.models.mmxcore.impl.UUIDObjectImpl;
import java.util.Collection;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;
import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import com.mmxlabs.models.lng.fleet.BaseFuel;
import com.mmxlabs.models.lng.fleet.FleetModel;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.fleet.ScenarioFleetModel;
import com.mmxlabs.models.lng.fleet.Vessel;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.fleet.VesselGroup;
import com.mmxlabs.models.lng.fleet.VesselTypeGroup;
import com.mmxlabs.models.mmxcore.impl.UUIDObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Model</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.FleetModelImpl#getVessels <em>Vessels</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.FleetModelImpl#getVesselClasses <em>Vessel Classes</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.FleetModelImpl#getBaseFuels <em>Base Fuels</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.FleetModelImpl#getVesselGroups <em>Vessel Groups</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.FleetModelImpl#getSpecialVesselGroups <em>Special Vessel Groups</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.fleet.impl.FleetModelImpl#getScenarioFleetModel <em>Scenario Fleet Model</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class FleetModelImpl extends UUIDObjectImpl implements FleetModel {
	/**
	 * The cached value of the '{@link #getVessels() <em>Vessels</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVessels()
	 * @generated
	 * @ordered
	 */
	protected EList<Vessel> vessels;

	/**
	 * The cached value of the '{@link #getVesselClasses() <em>Vessel Classes</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVesselClasses()
	 * @generated
	 * @ordered
	 */
	protected EList<VesselClass> vesselClasses;

	/**
	 * The cached value of the '{@link #getBaseFuels() <em>Base Fuels</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBaseFuels()
	 * @generated
	 * @ordered
	 */
	protected EList<BaseFuel> baseFuels;

	/**
	 * The cached value of the '{@link #getVesselGroups() <em>Vessel Groups</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVesselGroups()
	 * @generated
	 * @ordered
	 */
	protected EList<VesselGroup> vesselGroups;

	/**
	 * The cached value of the '{@link #getSpecialVesselGroups() <em>Special Vessel Groups</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @see #getSpecialVesselGroups()
	 * @generated
	 * @ordered
	 */
	protected EList<VesselTypeGroup> specialVesselGroups;

	/**
	 * The cached value of the '{@link #getScenarioFleetModel() <em>Scenario Fleet Model</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * @since 3.0
	 * <!-- end-user-doc -->
	 * @see #getScenarioFleetModel()
	 * @generated
	 * @ordered
	 */
	protected ScenarioFleetModel scenarioFleetModel;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected FleetModelImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return FleetPackage.Literals.FLEET_MODEL;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Vessel> getVessels() {
		if (vessels == null) {
			vessels = new EObjectContainmentEList<Vessel>(Vessel.class, this, FleetPackage.FLEET_MODEL__VESSELS);
		}
		return vessels;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<VesselClass> getVesselClasses() {
		if (vesselClasses == null) {
			vesselClasses = new EObjectContainmentEList<VesselClass>(VesselClass.class, this, FleetPackage.FLEET_MODEL__VESSEL_CLASSES);
		}
		return vesselClasses;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<BaseFuel> getBaseFuels() {
		if (baseFuels == null) {
			baseFuels = new EObjectContainmentEList<BaseFuel>(BaseFuel.class, this, FleetPackage.FLEET_MODEL__BASE_FUELS);
		}
		return baseFuels;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<VesselGroup> getVesselGroups() {
		if (vesselGroups == null) {
			vesselGroups = new EObjectContainmentEList<VesselGroup>(VesselGroup.class, this, FleetPackage.FLEET_MODEL__VESSEL_GROUPS);
		}
		return vesselGroups;
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 2.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<VesselTypeGroup> getSpecialVesselGroups() {
		if (specialVesselGroups == null) {
			specialVesselGroups = new EObjectContainmentEList<VesselTypeGroup>(VesselTypeGroup.class, this, FleetPackage.FLEET_MODEL__SPECIAL_VESSEL_GROUPS);
		}
		return specialVesselGroups;
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 3.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ScenarioFleetModel getScenarioFleetModel() {
		return scenarioFleetModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 3.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetScenarioFleetModel(ScenarioFleetModel newScenarioFleetModel, NotificationChain msgs) {
		ScenarioFleetModel oldScenarioFleetModel = scenarioFleetModel;
		scenarioFleetModel = newScenarioFleetModel;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, FleetPackage.FLEET_MODEL__SCENARIO_FLEET_MODEL, oldScenarioFleetModel, newScenarioFleetModel);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * @since 3.0
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setScenarioFleetModel(ScenarioFleetModel newScenarioFleetModel) {
		if (newScenarioFleetModel != scenarioFleetModel) {
			NotificationChain msgs = null;
			if (scenarioFleetModel != null)
				msgs = ((InternalEObject)scenarioFleetModel).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - FleetPackage.FLEET_MODEL__SCENARIO_FLEET_MODEL, null, msgs);
			if (newScenarioFleetModel != null)
				msgs = ((InternalEObject)newScenarioFleetModel).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - FleetPackage.FLEET_MODEL__SCENARIO_FLEET_MODEL, null, msgs);
			msgs = basicSetScenarioFleetModel(newScenarioFleetModel, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, FleetPackage.FLEET_MODEL__SCENARIO_FLEET_MODEL, newScenarioFleetModel, newScenarioFleetModel));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case FleetPackage.FLEET_MODEL__VESSELS:
				return ((InternalEList<?>)getVessels()).basicRemove(otherEnd, msgs);
			case FleetPackage.FLEET_MODEL__VESSEL_CLASSES:
				return ((InternalEList<?>)getVesselClasses()).basicRemove(otherEnd, msgs);
			case FleetPackage.FLEET_MODEL__BASE_FUELS:
				return ((InternalEList<?>)getBaseFuels()).basicRemove(otherEnd, msgs);
			case FleetPackage.FLEET_MODEL__VESSEL_GROUPS:
				return ((InternalEList<?>)getVesselGroups()).basicRemove(otherEnd, msgs);
			case FleetPackage.FLEET_MODEL__SPECIAL_VESSEL_GROUPS:
				return ((InternalEList<?>)getSpecialVesselGroups()).basicRemove(otherEnd, msgs);
			case FleetPackage.FLEET_MODEL__SCENARIO_FLEET_MODEL:
				return basicSetScenarioFleetModel(null, msgs);
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
			case FleetPackage.FLEET_MODEL__VESSELS:
				return getVessels();
			case FleetPackage.FLEET_MODEL__VESSEL_CLASSES:
				return getVesselClasses();
			case FleetPackage.FLEET_MODEL__BASE_FUELS:
				return getBaseFuels();
			case FleetPackage.FLEET_MODEL__VESSEL_GROUPS:
				return getVesselGroups();
			case FleetPackage.FLEET_MODEL__SPECIAL_VESSEL_GROUPS:
				return getSpecialVesselGroups();
			case FleetPackage.FLEET_MODEL__SCENARIO_FLEET_MODEL:
				return getScenarioFleetModel();
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
			case FleetPackage.FLEET_MODEL__VESSELS:
				getVessels().clear();
				getVessels().addAll((Collection<? extends Vessel>)newValue);
				return;
			case FleetPackage.FLEET_MODEL__VESSEL_CLASSES:
				getVesselClasses().clear();
				getVesselClasses().addAll((Collection<? extends VesselClass>)newValue);
				return;
			case FleetPackage.FLEET_MODEL__BASE_FUELS:
				getBaseFuels().clear();
				getBaseFuels().addAll((Collection<? extends BaseFuel>)newValue);
				return;
			case FleetPackage.FLEET_MODEL__VESSEL_GROUPS:
				getVesselGroups().clear();
				getVesselGroups().addAll((Collection<? extends VesselGroup>)newValue);
				return;
			case FleetPackage.FLEET_MODEL__SPECIAL_VESSEL_GROUPS:
				getSpecialVesselGroups().clear();
				getSpecialVesselGroups().addAll((Collection<? extends VesselTypeGroup>)newValue);
				return;
			case FleetPackage.FLEET_MODEL__SCENARIO_FLEET_MODEL:
				setScenarioFleetModel((ScenarioFleetModel)newValue);
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
			case FleetPackage.FLEET_MODEL__VESSELS:
				getVessels().clear();
				return;
			case FleetPackage.FLEET_MODEL__VESSEL_CLASSES:
				getVesselClasses().clear();
				return;
			case FleetPackage.FLEET_MODEL__BASE_FUELS:
				getBaseFuels().clear();
				return;
			case FleetPackage.FLEET_MODEL__VESSEL_GROUPS:
				getVesselGroups().clear();
				return;
			case FleetPackage.FLEET_MODEL__SPECIAL_VESSEL_GROUPS:
				getSpecialVesselGroups().clear();
				return;
			case FleetPackage.FLEET_MODEL__SCENARIO_FLEET_MODEL:
				setScenarioFleetModel((ScenarioFleetModel)null);
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
			case FleetPackage.FLEET_MODEL__VESSELS:
				return vessels != null && !vessels.isEmpty();
			case FleetPackage.FLEET_MODEL__VESSEL_CLASSES:
				return vesselClasses != null && !vesselClasses.isEmpty();
			case FleetPackage.FLEET_MODEL__BASE_FUELS:
				return baseFuels != null && !baseFuels.isEmpty();
			case FleetPackage.FLEET_MODEL__VESSEL_GROUPS:
				return vesselGroups != null && !vesselGroups.isEmpty();
			case FleetPackage.FLEET_MODEL__SPECIAL_VESSEL_GROUPS:
				return specialVesselGroups != null && !specialVesselGroups.isEmpty();
			case FleetPackage.FLEET_MODEL__SCENARIO_FLEET_MODEL:
				return scenarioFleetModel != null;
		}
		return super.eIsSet(featureID);
	}

} // end of FleetModelImpl

// finish type fixing
