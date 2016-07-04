/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.scenario.service.model.impl;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;

import com.mmxlabs.scenario.service.model.ScenarioInstance;
import com.mmxlabs.scenario.service.model.ScenarioLock;
import com.mmxlabs.scenario.service.model.ScenarioServicePackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Scenario Lock</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.scenario.service.model.impl.ScenarioLockImpl#isAvailable <em>Available</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.model.impl.ScenarioLockImpl#isClaimed <em>Claimed</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.model.impl.ScenarioLockImpl#getKey <em>Key</em>}</li>
 *   <li>{@link com.mmxlabs.scenario.service.model.impl.ScenarioLockImpl#getInstance <em>Instance</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ScenarioLockImpl extends EObjectImpl implements ScenarioLock {
	/**
	 * The default value of the '{@link #isAvailable() <em>Available</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isAvailable()
	 * @generated
	 * @ordered
	 */
	protected static final boolean AVAILABLE_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isAvailable() <em>Available</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isAvailable()
	 * @generated
	 * @ordered
	 */
	protected boolean available = AVAILABLE_EDEFAULT;

	/**
	 * The default value of the '{@link #isClaimed() <em>Claimed</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isClaimed()
	 * @generated
	 * @ordered
	 */
	protected static final boolean CLAIMED_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isClaimed() <em>Claimed</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isClaimed()
	 * @generated
	 * @ordered
	 */
	protected boolean claimed = CLAIMED_EDEFAULT;

	/**
	 * The default value of the '{@link #getKey() <em>Key</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getKey()
	 * @generated
	 * @ordered
	 */
	protected static final String KEY_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getKey() <em>Key</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getKey()
	 * @generated
	 * @ordered
	 */
	protected String key = KEY_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ScenarioLockImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ScenarioServicePackage.eINSTANCE.getScenarioLock();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isAvailable() {
		return available;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setAvailable(boolean newAvailable) {
		boolean oldAvailable = available;
		available = newAvailable;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ScenarioServicePackage.SCENARIO_LOCK__AVAILABLE, oldAvailable, available));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isClaimed() {
		return claimed;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setClaimed(boolean newClaimed) {
		boolean oldClaimed = claimed;
		claimed = newClaimed;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ScenarioServicePackage.SCENARIO_LOCK__CLAIMED, oldClaimed, claimed));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getKey() {
		return key;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setKey(String newKey) {
		String oldKey = key;
		key = newKey;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ScenarioServicePackage.SCENARIO_LOCK__KEY, oldKey, key));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ScenarioInstance getInstance() {
		if (eContainerFeatureID() != ScenarioServicePackage.SCENARIO_LOCK__INSTANCE)
			return null;
		return (ScenarioInstance) eInternalContainer();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetInstance(ScenarioInstance newInstance, NotificationChain msgs) {
		msgs = eBasicSetContainer((InternalEObject) newInstance, ScenarioServicePackage.SCENARIO_LOCK__INSTANCE, msgs);
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setInstance(ScenarioInstance newInstance) {
		if (newInstance != eInternalContainer() || (eContainerFeatureID() != ScenarioServicePackage.SCENARIO_LOCK__INSTANCE && newInstance != null)) {
			if (EcoreUtil.isAncestor(this, newInstance))
				throw new IllegalArgumentException("Recursive containment not allowed for " + toString());
			NotificationChain msgs = null;
			if (eInternalContainer() != null)
				msgs = eBasicRemoveFromContainer(msgs);
			if (newInstance != null)
				msgs = ((InternalEObject) newInstance).eInverseAdd(this, ScenarioServicePackage.SCENARIO_INSTANCE__LOCKS, ScenarioInstance.class, msgs);
			msgs = basicSetInstance(newInstance, msgs);
			if (msgs != null)
				msgs.dispatch();
		} else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ScenarioServicePackage.SCENARIO_LOCK__INSTANCE, newInstance, newInstance));
	}

	private boolean claiming = false;

	/**
	 * Depth of stacked calls to claim()
	 */
	private int claimCount = 0;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public boolean claim() {
		//		System.err.println(getKey() + " claim");
		final ScenarioInstance instance = getInstance();
		synchronized (instance) {
			//			System.err.println(getKey() + " synch");
			if (isClaimed()) {
				//				System.err.println(getKey() + " already claimed");	
				claimCount++;
				return true;
			}
			claiming = true;
			if (instance.isLocked()) {
				//				System.err.println(getKey() + " can't claim (held by someone else)");
				claiming = false;
				return false;
			}

			//			System.err.println(getKey() + " acquired");

			claiming = false;
			setClaimed(true);
			instance.setLocked(true);
			claimCount++;
			return true;
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public boolean awaitClaim() {
		final ScenarioInstance instance = getInstance();
		synchronized (instance) {
			//			System.err.println(getKey() + " await synch");
			if (isClaimed()) {
				//				System.err.println(getKey() + " already claimed");
				claimCount++;
				return true;
			}
			claiming = true;
			while (instance.isLocked()) {
				try {
					//					System.err.println(getKey() + " waiting for unlock");
					instance.wait();
				} catch (InterruptedException e) {

				}
			}
			//			System.err.println(getKey() + " acquired");
			claiming = false;
			setClaimed(true);
			instance.setLocked(true);
			claimCount++;
			return true;
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public void release() {
		//		System.err.println(getKey() + " release");
		final ScenarioInstance instance = getInstance();
		synchronized (instance) {
			//			System.err.println(getKey() + " release synch");
			if (isClaimed()) {
				claimCount--;
				if (claimCount == 0) {
					setClaimed(false);
					getInstance().setLocked(false);
					//		System.err.println(getKey() + " waking up others");
					getInstance().notifyAll();
				}
			} else {
				//				System.err.println(getKey() + " not actually claimed");
			}
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated NOT
	 */
	public void init() {
		final Adapter lockWatcher = new AdapterImpl() {
			@Override
			public void notifyChanged(final Notification n) {
				if (n.isTouch() == false && n.getFeature() == ScenarioServicePackage.eINSTANCE.getScenarioInstance_Locked()) {
					if (n.getNewBooleanValue()) {
						if (!claiming) {
							setAvailable(false);
						}
					} else {
						// it is becoming released
						setAvailable(true);
					}
				}
			}
		};
		final ScenarioInstance instance = getInstance();
		synchronized (instance) {
			setAvailable(!instance.isLocked());
			getInstance().eAdapters().add(lockWatcher);
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
		case ScenarioServicePackage.SCENARIO_LOCK__INSTANCE:
			if (eInternalContainer() != null)
				msgs = eBasicRemoveFromContainer(msgs);
			return basicSetInstance((ScenarioInstance) otherEnd, msgs);
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
		case ScenarioServicePackage.SCENARIO_LOCK__INSTANCE:
			return basicSetInstance(null, msgs);
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
		case ScenarioServicePackage.SCENARIO_LOCK__INSTANCE:
			return eInternalContainer().eInverseRemove(this, ScenarioServicePackage.SCENARIO_INSTANCE__LOCKS, ScenarioInstance.class, msgs);
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
		case ScenarioServicePackage.SCENARIO_LOCK__AVAILABLE:
			return isAvailable();
		case ScenarioServicePackage.SCENARIO_LOCK__CLAIMED:
			return isClaimed();
		case ScenarioServicePackage.SCENARIO_LOCK__KEY:
			return getKey();
		case ScenarioServicePackage.SCENARIO_LOCK__INSTANCE:
			return getInstance();
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
		case ScenarioServicePackage.SCENARIO_LOCK__AVAILABLE:
			setAvailable((Boolean) newValue);
			return;
		case ScenarioServicePackage.SCENARIO_LOCK__CLAIMED:
			setClaimed((Boolean) newValue);
			return;
		case ScenarioServicePackage.SCENARIO_LOCK__KEY:
			setKey((String) newValue);
			return;
		case ScenarioServicePackage.SCENARIO_LOCK__INSTANCE:
			setInstance((ScenarioInstance) newValue);
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
		case ScenarioServicePackage.SCENARIO_LOCK__AVAILABLE:
			setAvailable(AVAILABLE_EDEFAULT);
			return;
		case ScenarioServicePackage.SCENARIO_LOCK__CLAIMED:
			setClaimed(CLAIMED_EDEFAULT);
			return;
		case ScenarioServicePackage.SCENARIO_LOCK__KEY:
			setKey(KEY_EDEFAULT);
			return;
		case ScenarioServicePackage.SCENARIO_LOCK__INSTANCE:
			setInstance((ScenarioInstance) null);
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
		case ScenarioServicePackage.SCENARIO_LOCK__AVAILABLE:
			return available != AVAILABLE_EDEFAULT;
		case ScenarioServicePackage.SCENARIO_LOCK__CLAIMED:
			return claimed != CLAIMED_EDEFAULT;
		case ScenarioServicePackage.SCENARIO_LOCK__KEY:
			return KEY_EDEFAULT == null ? key != null : !KEY_EDEFAULT.equals(key);
		case ScenarioServicePackage.SCENARIO_LOCK__INSTANCE:
			return getInstance() != null;
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
		if (eIsProxy())
			return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (available: ");
		result.append(available);
		result.append(", claimed: ");
		result.append(claimed);
		result.append(", key: ");
		result.append(key);
		result.append(')');
		return result.toString();
	}

} //ScenarioLockImpl
