/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
/**
 */
package com.mmxlabs.models.lng.parameters.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import com.mmxlabs.models.lng.parameters.ParametersPackage;
import com.mmxlabs.models.lng.parameters.SimilarityInterval;
import com.mmxlabs.models.lng.parameters.SimilaritySettings;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Similarity Settings</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.parameters.impl.SimilaritySettingsImpl#getLowInterval <em>Low Interval</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.parameters.impl.SimilaritySettingsImpl#getMedInterval <em>Med Interval</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.parameters.impl.SimilaritySettingsImpl#getHighInterval <em>High Interval</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.parameters.impl.SimilaritySettingsImpl#getOutOfBoundsWeight <em>Out Of Bounds Weight</em>}</li>
 * </ul>
 *
 * @generated
 */
public class SimilaritySettingsImpl extends EObjectImpl implements SimilaritySettings {
	/**
	 * The cached value of the '{@link #getLowInterval() <em>Low Interval</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLowInterval()
	 * @generated
	 * @ordered
	 */
	protected SimilarityInterval lowInterval;

	/**
	 * The cached value of the '{@link #getMedInterval() <em>Med Interval</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMedInterval()
	 * @generated
	 * @ordered
	 */
	protected SimilarityInterval medInterval;

	/**
	 * The cached value of the '{@link #getHighInterval() <em>High Interval</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getHighInterval()
	 * @generated
	 * @ordered
	 */
	protected SimilarityInterval highInterval;

	/**
	 * The default value of the '{@link #getOutOfBoundsWeight() <em>Out Of Bounds Weight</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOutOfBoundsWeight()
	 * @generated
	 * @ordered
	 */
	protected static final int OUT_OF_BOUNDS_WEIGHT_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getOutOfBoundsWeight() <em>Out Of Bounds Weight</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOutOfBoundsWeight()
	 * @generated
	 * @ordered
	 */
	protected int outOfBoundsWeight = OUT_OF_BOUNDS_WEIGHT_EDEFAULT;

	/**
	 * This is true if the Out Of Bounds Weight attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean outOfBoundsWeightESet;

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
	@Override
	public SimilarityInterval getLowInterval() {
		return lowInterval;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetLowInterval(SimilarityInterval newLowInterval, NotificationChain msgs) {
		SimilarityInterval oldLowInterval = lowInterval;
		lowInterval = newLowInterval;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ParametersPackage.SIMILARITY_SETTINGS__LOW_INTERVAL, oldLowInterval, newLowInterval);
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
	public void setLowInterval(SimilarityInterval newLowInterval) {
		if (newLowInterval != lowInterval) {
			NotificationChain msgs = null;
			if (lowInterval != null)
				msgs = ((InternalEObject)lowInterval).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ParametersPackage.SIMILARITY_SETTINGS__LOW_INTERVAL, null, msgs);
			if (newLowInterval != null)
				msgs = ((InternalEObject)newLowInterval).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ParametersPackage.SIMILARITY_SETTINGS__LOW_INTERVAL, null, msgs);
			msgs = basicSetLowInterval(newLowInterval, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ParametersPackage.SIMILARITY_SETTINGS__LOW_INTERVAL, newLowInterval, newLowInterval));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public SimilarityInterval getMedInterval() {
		return medInterval;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetMedInterval(SimilarityInterval newMedInterval, NotificationChain msgs) {
		SimilarityInterval oldMedInterval = medInterval;
		medInterval = newMedInterval;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ParametersPackage.SIMILARITY_SETTINGS__MED_INTERVAL, oldMedInterval, newMedInterval);
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
	public void setMedInterval(SimilarityInterval newMedInterval) {
		if (newMedInterval != medInterval) {
			NotificationChain msgs = null;
			if (medInterval != null)
				msgs = ((InternalEObject)medInterval).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ParametersPackage.SIMILARITY_SETTINGS__MED_INTERVAL, null, msgs);
			if (newMedInterval != null)
				msgs = ((InternalEObject)newMedInterval).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ParametersPackage.SIMILARITY_SETTINGS__MED_INTERVAL, null, msgs);
			msgs = basicSetMedInterval(newMedInterval, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ParametersPackage.SIMILARITY_SETTINGS__MED_INTERVAL, newMedInterval, newMedInterval));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public SimilarityInterval getHighInterval() {
		return highInterval;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetHighInterval(SimilarityInterval newHighInterval, NotificationChain msgs) {
		SimilarityInterval oldHighInterval = highInterval;
		highInterval = newHighInterval;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ParametersPackage.SIMILARITY_SETTINGS__HIGH_INTERVAL, oldHighInterval, newHighInterval);
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
	public void setHighInterval(SimilarityInterval newHighInterval) {
		if (newHighInterval != highInterval) {
			NotificationChain msgs = null;
			if (highInterval != null)
				msgs = ((InternalEObject)highInterval).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ParametersPackage.SIMILARITY_SETTINGS__HIGH_INTERVAL, null, msgs);
			if (newHighInterval != null)
				msgs = ((InternalEObject)newHighInterval).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ParametersPackage.SIMILARITY_SETTINGS__HIGH_INTERVAL, null, msgs);
			msgs = basicSetHighInterval(newHighInterval, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ParametersPackage.SIMILARITY_SETTINGS__HIGH_INTERVAL, newHighInterval, newHighInterval));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getOutOfBoundsWeight() {
		return outOfBoundsWeight;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setOutOfBoundsWeight(int newOutOfBoundsWeight) {
		int oldOutOfBoundsWeight = outOfBoundsWeight;
		outOfBoundsWeight = newOutOfBoundsWeight;
		boolean oldOutOfBoundsWeightESet = outOfBoundsWeightESet;
		outOfBoundsWeightESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ParametersPackage.SIMILARITY_SETTINGS__OUT_OF_BOUNDS_WEIGHT, oldOutOfBoundsWeight, outOfBoundsWeight, !oldOutOfBoundsWeightESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void unsetOutOfBoundsWeight() {
		int oldOutOfBoundsWeight = outOfBoundsWeight;
		boolean oldOutOfBoundsWeightESet = outOfBoundsWeightESet;
		outOfBoundsWeight = OUT_OF_BOUNDS_WEIGHT_EDEFAULT;
		outOfBoundsWeightESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, ParametersPackage.SIMILARITY_SETTINGS__OUT_OF_BOUNDS_WEIGHT, oldOutOfBoundsWeight, OUT_OF_BOUNDS_WEIGHT_EDEFAULT, oldOutOfBoundsWeightESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isSetOutOfBoundsWeight() {
		return outOfBoundsWeightESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case ParametersPackage.SIMILARITY_SETTINGS__LOW_INTERVAL:
				return basicSetLowInterval(null, msgs);
			case ParametersPackage.SIMILARITY_SETTINGS__MED_INTERVAL:
				return basicSetMedInterval(null, msgs);
			case ParametersPackage.SIMILARITY_SETTINGS__HIGH_INTERVAL:
				return basicSetHighInterval(null, msgs);
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
			case ParametersPackage.SIMILARITY_SETTINGS__LOW_INTERVAL:
				return getLowInterval();
			case ParametersPackage.SIMILARITY_SETTINGS__MED_INTERVAL:
				return getMedInterval();
			case ParametersPackage.SIMILARITY_SETTINGS__HIGH_INTERVAL:
				return getHighInterval();
			case ParametersPackage.SIMILARITY_SETTINGS__OUT_OF_BOUNDS_WEIGHT:
				return getOutOfBoundsWeight();
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
			case ParametersPackage.SIMILARITY_SETTINGS__LOW_INTERVAL:
				setLowInterval((SimilarityInterval)newValue);
				return;
			case ParametersPackage.SIMILARITY_SETTINGS__MED_INTERVAL:
				setMedInterval((SimilarityInterval)newValue);
				return;
			case ParametersPackage.SIMILARITY_SETTINGS__HIGH_INTERVAL:
				setHighInterval((SimilarityInterval)newValue);
				return;
			case ParametersPackage.SIMILARITY_SETTINGS__OUT_OF_BOUNDS_WEIGHT:
				setOutOfBoundsWeight((Integer)newValue);
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
			case ParametersPackage.SIMILARITY_SETTINGS__LOW_INTERVAL:
				setLowInterval((SimilarityInterval)null);
				return;
			case ParametersPackage.SIMILARITY_SETTINGS__MED_INTERVAL:
				setMedInterval((SimilarityInterval)null);
				return;
			case ParametersPackage.SIMILARITY_SETTINGS__HIGH_INTERVAL:
				setHighInterval((SimilarityInterval)null);
				return;
			case ParametersPackage.SIMILARITY_SETTINGS__OUT_OF_BOUNDS_WEIGHT:
				unsetOutOfBoundsWeight();
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
			case ParametersPackage.SIMILARITY_SETTINGS__LOW_INTERVAL:
				return lowInterval != null;
			case ParametersPackage.SIMILARITY_SETTINGS__MED_INTERVAL:
				return medInterval != null;
			case ParametersPackage.SIMILARITY_SETTINGS__HIGH_INTERVAL:
				return highInterval != null;
			case ParametersPackage.SIMILARITY_SETTINGS__OUT_OF_BOUNDS_WEIGHT:
				return isSetOutOfBoundsWeight();
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
		result.append(" (outOfBoundsWeight: ");
		if (outOfBoundsWeightESet) result.append(outOfBoundsWeight); else result.append("<unset>");
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
			return (this.getLowInterval().equals(otherSettings.getLowInterval())
					&& this.getMedInterval().equals(otherSettings.getMedInterval())
					&& this.getHighInterval().equals(otherSettings.getHighInterval())
					&& this.getOutOfBoundsWeight() == otherSettings.getOutOfBoundsWeight());
		}
		return super.equals(obj);
	}
	
} //SimilaritySettingsImpl
