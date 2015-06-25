/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2015
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.parameters.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;
import org.joda.time.Interval;

import com.mmxlabs.models.lng.parameters.ParametersPackage;
import com.mmxlabs.models.lng.parameters.SimilarityInterval;
import com.mmxlabs.models.lng.parameters.SimilaritySettings;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Similarity Settings</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.parameters.impl.SimilaritySettingsImpl#getThreshold <em>Threshold</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.parameters.impl.SimilaritySettingsImpl#getIntervals <em>Intervals</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class SimilaritySettingsImpl extends EObjectImpl implements SimilaritySettings {
	/**
	 * The default value of the '{@link #getThreshold() <em>Threshold</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getThreshold()
	 * @generated
	 * @ordered
	 */
	protected static final int THRESHOLD_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getThreshold() <em>Threshold</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getThreshold()
	 * @generated
	 * @ordered
	 */
	protected int threshold = THRESHOLD_EDEFAULT;

	/**
	 * This is true if the Threshold attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean thresholdESet;

	/**
	 * The cached value of the '{@link #getIntervals() <em>Intervals</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getIntervals()
	 * @generated
	 * @ordered
	 */
	protected EList<SimilarityInterval> intervals;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected SimilaritySettingsImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ParametersPackage.Literals.SIMILARITY_SETTINGS;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getThreshold() {
		return threshold;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setThreshold(int newThreshold) {
		int oldThreshold = threshold;
		threshold = newThreshold;
		boolean oldThresholdESet = thresholdESet;
		thresholdESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ParametersPackage.SIMILARITY_SETTINGS__THRESHOLD, oldThreshold, threshold, !oldThresholdESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetThreshold() {
		int oldThreshold = threshold;
		boolean oldThresholdESet = thresholdESet;
		threshold = THRESHOLD_EDEFAULT;
		thresholdESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, ParametersPackage.SIMILARITY_SETTINGS__THRESHOLD, oldThreshold, THRESHOLD_EDEFAULT, oldThresholdESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetThreshold() {
		return thresholdESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<SimilarityInterval> getIntervals() {
		if (intervals == null) {
			intervals = new EObjectContainmentEList<SimilarityInterval>(SimilarityInterval.class, this, ParametersPackage.SIMILARITY_SETTINGS__INTERVALS);
		}
		return intervals;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case ParametersPackage.SIMILARITY_SETTINGS__INTERVALS:
				return ((InternalEList<?>)getIntervals()).basicRemove(otherEnd, msgs);
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
			case ParametersPackage.SIMILARITY_SETTINGS__THRESHOLD:
				return getThreshold();
			case ParametersPackage.SIMILARITY_SETTINGS__INTERVALS:
				return getIntervals();
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
			case ParametersPackage.SIMILARITY_SETTINGS__THRESHOLD:
				setThreshold((Integer)newValue);
				return;
			case ParametersPackage.SIMILARITY_SETTINGS__INTERVALS:
				getIntervals().clear();
				getIntervals().addAll((Collection<? extends SimilarityInterval>)newValue);
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
			case ParametersPackage.SIMILARITY_SETTINGS__THRESHOLD:
				unsetThreshold();
				return;
			case ParametersPackage.SIMILARITY_SETTINGS__INTERVALS:
				getIntervals().clear();
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
			case ParametersPackage.SIMILARITY_SETTINGS__THRESHOLD:
				return isSetThreshold();
			case ParametersPackage.SIMILARITY_SETTINGS__INTERVALS:
				return intervals != null && !intervals.isEmpty();
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
		result.append(" (threshold: ");
		if (thresholdESet) result.append(threshold); else result.append("<unset>");
		result.append(')');
		return result.toString();
	}
	
	/**
	 * Equals method comparing interval definition.
	 * Implemented for the ParameterModesDialog
	 * @generated NOT
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof SimilaritySettings) {
			SimilaritySettings otherSettings = (SimilaritySettings) obj;
			Map<Integer, Integer> local = new HashMap<>();
			for (SimilarityInterval interval : getIntervals()) {
				local.put(interval.getWeight(), interval.getThreshold());
			}
			Map<Integer, Integer> other = new HashMap<>();
			for (SimilarityInterval interval : otherSettings.getIntervals()) {
				other.put(interval.getWeight(), interval.getThreshold());
			}
			return local.equals(other);
			
		}
		return super.equals(obj);
	}

} //SimilaritySettingsImpl
