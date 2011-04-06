/**
 * Copyright (C) Minimax Labs Ltd., 2010
 * All rights reserved.
 */

package scenario.port.impl;

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

import scenario.port.Canal;
import scenario.port.DistanceModel;
import scenario.port.PortPackage;
import scenario.port.VesselClassCost;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Canal</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link scenario.port.impl.CanalImpl#getName <em>Name</em>}</li>
 *   <li>{@link scenario.port.impl.CanalImpl#getClassCosts <em>Class Costs</em>}</li>
 *   <li>{@link scenario.port.impl.CanalImpl#getDefaultCost <em>Default Cost</em>}</li>
 *   <li>{@link scenario.port.impl.CanalImpl#getDistanceModel <em>Distance Model</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class CanalImpl extends EObjectImpl implements Canal {
	/**
	 * The default value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected static final String NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected String name = NAME_EDEFAULT;

	/**
	 * The cached value of the '{@link #getClassCosts() <em>Class Costs</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getClassCosts()
	 * @generated
	 * @ordered
	 */
	protected EList<VesselClassCost> classCosts;

	/**
	 * The default value of the '{@link #getDefaultCost() <em>Default Cost</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDefaultCost()
	 * @generated
	 * @ordered
	 */
	protected static final int DEFAULT_COST_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getDefaultCost() <em>Default Cost</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDefaultCost()
	 * @generated
	 * @ordered
	 */
	protected int defaultCost = DEFAULT_COST_EDEFAULT;

	/**
	 * The cached value of the '{@link #getDistanceModel() <em>Distance Model</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDistanceModel()
	 * @generated
	 * @ordered
	 */
	protected DistanceModel distanceModel;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected CanalImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return PortPackage.Literals.CANAL;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setName(String newName) {
		String oldName = name;
		name = newName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PortPackage.CANAL__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<VesselClassCost> getClassCosts() {
		if (classCosts == null) {
			classCosts = new EObjectContainmentEList<VesselClassCost>(VesselClassCost.class, this, PortPackage.CANAL__CLASS_COSTS);
		}
		return classCosts;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getDefaultCost() {
		return defaultCost;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setDefaultCost(int newDefaultCost) {
		int oldDefaultCost = defaultCost;
		defaultCost = newDefaultCost;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PortPackage.CANAL__DEFAULT_COST, oldDefaultCost, defaultCost));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public DistanceModel getDistanceModel() {
		return distanceModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetDistanceModel(DistanceModel newDistanceModel, NotificationChain msgs) {
		DistanceModel oldDistanceModel = distanceModel;
		distanceModel = newDistanceModel;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, PortPackage.CANAL__DISTANCE_MODEL, oldDistanceModel, newDistanceModel);
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
	public void setDistanceModel(DistanceModel newDistanceModel) {
		if (newDistanceModel != distanceModel) {
			NotificationChain msgs = null;
			if (distanceModel != null)
				msgs = ((InternalEObject)distanceModel).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - PortPackage.CANAL__DISTANCE_MODEL, null, msgs);
			if (newDistanceModel != null)
				msgs = ((InternalEObject)newDistanceModel).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - PortPackage.CANAL__DISTANCE_MODEL, null, msgs);
			msgs = basicSetDistanceModel(newDistanceModel, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PortPackage.CANAL__DISTANCE_MODEL, newDistanceModel, newDistanceModel));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case PortPackage.CANAL__CLASS_COSTS:
				return ((InternalEList<?>)getClassCosts()).basicRemove(otherEnd, msgs);
			case PortPackage.CANAL__DISTANCE_MODEL:
				return basicSetDistanceModel(null, msgs);
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
			case PortPackage.CANAL__NAME:
				return getName();
			case PortPackage.CANAL__CLASS_COSTS:
				return getClassCosts();
			case PortPackage.CANAL__DEFAULT_COST:
				return getDefaultCost();
			case PortPackage.CANAL__DISTANCE_MODEL:
				return getDistanceModel();
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
			case PortPackage.CANAL__NAME:
				setName((String)newValue);
				return;
			case PortPackage.CANAL__CLASS_COSTS:
				getClassCosts().clear();
				getClassCosts().addAll((Collection<? extends VesselClassCost>)newValue);
				return;
			case PortPackage.CANAL__DEFAULT_COST:
				setDefaultCost((Integer)newValue);
				return;
			case PortPackage.CANAL__DISTANCE_MODEL:
				setDistanceModel((DistanceModel)newValue);
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
			case PortPackage.CANAL__NAME:
				setName(NAME_EDEFAULT);
				return;
			case PortPackage.CANAL__CLASS_COSTS:
				getClassCosts().clear();
				return;
			case PortPackage.CANAL__DEFAULT_COST:
				setDefaultCost(DEFAULT_COST_EDEFAULT);
				return;
			case PortPackage.CANAL__DISTANCE_MODEL:
				setDistanceModel((DistanceModel)null);
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
			case PortPackage.CANAL__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case PortPackage.CANAL__CLASS_COSTS:
				return classCosts != null && !classCosts.isEmpty();
			case PortPackage.CANAL__DEFAULT_COST:
				return defaultCost != DEFAULT_COST_EDEFAULT;
			case PortPackage.CANAL__DISTANCE_MODEL:
				return distanceModel != null;
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
		result.append(" (name: ");
		result.append(name);
		result.append(", defaultCost: ");
		result.append(defaultCost);
		result.append(')');
		return result.toString();
	}

} //CanalImpl
