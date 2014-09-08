/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2014
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.model.impl;

import java.io.IOException;

import com.mmxlabs.scenario.service.model.ModelReference;
import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.ScenarioServicePackage;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Model Reference</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.mmxlabs.scenario.service.model.impl.ModelReferenceImpl#getScenarioInstance <em>Scenario Instance</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ModelReferenceImpl extends EObjectImpl implements ModelReference {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ModelReferenceImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ScenarioServicePackage.eINSTANCE.getModelReference();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ScenarioInstance getScenarioInstance() {
		if (eContainerFeatureID() != ScenarioServicePackage.MODEL_REFERENCE__SCENARIO_INSTANCE)
			return null;
		return (ScenarioInstance) eInternalContainer();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetScenarioInstance(ScenarioInstance newScenarioInstance, NotificationChain msgs) {
		msgs = eBasicSetContainer((InternalEObject) newScenarioInstance, ScenarioServicePackage.MODEL_REFERENCE__SCENARIO_INSTANCE, msgs);
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setScenarioInstance(ScenarioInstance newScenarioInstance) {
		if (newScenarioInstance != eInternalContainer() || (eContainerFeatureID() != ScenarioServicePackage.MODEL_REFERENCE__SCENARIO_INSTANCE && newScenarioInstance != null)) {
			if (EcoreUtil.isAncestor(this, newScenarioInstance))
				throw new IllegalArgumentException("Recursive containment not allowed for " + toString());
			NotificationChain msgs = null;
			if (eInternalContainer() != null)
				msgs = eBasicRemoveFromContainer(msgs);
			if (newScenarioInstance != null)
				msgs = ((InternalEObject) newScenarioInstance).eInverseAdd(this, ScenarioServicePackage.SCENARIO_INSTANCE__MODEL_REFERENCES, ScenarioInstance.class, msgs);
			msgs = basicSetScenarioInstance(newScenarioInstance, msgs);
			if (msgs != null)
				msgs.dispatch();
		} else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ScenarioServicePackage.MODEL_REFERENCE__SCENARIO_INSTANCE, newScenarioInstance, newScenarioInstance));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public synchronized EObject getInstance() {

		ScenarioInstance scenarioInstance = getScenarioInstance();

		EObject modelInstance = scenarioInstance.getInstance();
		if (modelInstance == null) {
			try {
				modelInstance = scenarioInstance.load();
			} catch (IOException e) {
				throw new RuntimeException("Unable to load scenario", e);
			}
		}

		return modelInstance;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public void close() {
		ScenarioInstance scenarioInstance = getScenarioInstance();
		setScenarioInstance(null);
		if (scenarioInstance != null) {
			scenarioInstance.getModelReferences().remove(this);
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
		case ScenarioServicePackage.MODEL_REFERENCE__SCENARIO_INSTANCE:
			if (eInternalContainer() != null)
				msgs = eBasicRemoveFromContainer(msgs);
			return basicSetScenarioInstance((ScenarioInstance) otherEnd, msgs);
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
		case ScenarioServicePackage.MODEL_REFERENCE__SCENARIO_INSTANCE:
			return basicSetScenarioInstance(null, msgs);
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
		case ScenarioServicePackage.MODEL_REFERENCE__SCENARIO_INSTANCE:
			return eInternalContainer().eInverseRemove(this, ScenarioServicePackage.SCENARIO_INSTANCE__MODEL_REFERENCES, ScenarioInstance.class, msgs);
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
		case ScenarioServicePackage.MODEL_REFERENCE__SCENARIO_INSTANCE:
			return getScenarioInstance();
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
		case ScenarioServicePackage.MODEL_REFERENCE__SCENARIO_INSTANCE:
			setScenarioInstance((ScenarioInstance) newValue);
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
		case ScenarioServicePackage.MODEL_REFERENCE__SCENARIO_INSTANCE:
			setScenarioInstance((ScenarioInstance) null);
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
		case ScenarioServicePackage.MODEL_REFERENCE__SCENARIO_INSTANCE:
			return getScenarioInstance() != null;
		}
		return super.eIsSet(featureID);
	}

} //ModelReferenceImpl
