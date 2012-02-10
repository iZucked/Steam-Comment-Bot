/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package scenario.optimiser.lso.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

import scenario.optimiser.impl.OptimisationSettingsImpl;
import scenario.optimiser.lso.LSOSettings;
import scenario.optimiser.lso.LsoPackage;
import scenario.optimiser.lso.MoveGeneratorSettings;
import scenario.optimiser.lso.ThresholderSettings;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>LSO Settings</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link scenario.optimiser.lso.impl.LSOSettingsImpl#getNumberOfSteps <em>Number Of Steps</em>}</li>
 *   <li>{@link scenario.optimiser.lso.impl.LSOSettingsImpl#getThresholderSettings <em>Thresholder Settings</em>}</li>
 *   <li>{@link scenario.optimiser.lso.impl.LSOSettingsImpl#getMoveGeneratorSettings <em>Move Generator Settings</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class LSOSettingsImpl extends OptimisationSettingsImpl implements LSOSettings {
	/**
	 * The default value of the '{@link #getNumberOfSteps() <em>Number Of Steps</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getNumberOfSteps()
	 * @generated
	 * @ordered
	 */
	protected static final int NUMBER_OF_STEPS_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getNumberOfSteps() <em>Number Of Steps</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getNumberOfSteps()
	 * @generated
	 * @ordered
	 */
	protected int numberOfSteps = NUMBER_OF_STEPS_EDEFAULT;

	/**
	 * The cached value of the '{@link #getThresholderSettings() <em>Thresholder Settings</em>}' containment reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getThresholderSettings()
	 * @generated
	 * @ordered
	 */
	protected ThresholderSettings thresholderSettings;

	/**
	 * The cached value of the '{@link #getMoveGeneratorSettings() <em>Move Generator Settings</em>}' containment reference.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @see #getMoveGeneratorSettings()
	 * @generated
	 * @ordered
	 */
	protected MoveGeneratorSettings moveGeneratorSettings;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	protected LSOSettingsImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return LsoPackage.Literals.LSO_SETTINGS;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getNumberOfSteps() {
		return numberOfSteps;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setNumberOfSteps(int newNumberOfSteps) {
		int oldNumberOfSteps = numberOfSteps;
		numberOfSteps = newNumberOfSteps;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, LsoPackage.LSO_SETTINGS__NUMBER_OF_STEPS, oldNumberOfSteps, numberOfSteps));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ThresholderSettings getThresholderSettings() {
		if (thresholderSettings != null && thresholderSettings.eIsProxy()) {
			InternalEObject oldThresholderSettings = (InternalEObject)thresholderSettings;
			thresholderSettings = (ThresholderSettings)eResolveProxy(oldThresholderSettings);
			if (thresholderSettings != oldThresholderSettings) {
				InternalEObject newThresholderSettings = (InternalEObject)thresholderSettings;
				NotificationChain msgs = oldThresholderSettings.eInverseRemove(this, EOPPOSITE_FEATURE_BASE - LsoPackage.LSO_SETTINGS__THRESHOLDER_SETTINGS, null, null);
				if (newThresholderSettings.eInternalContainer() == null) {
					msgs = newThresholderSettings.eInverseAdd(this, EOPPOSITE_FEATURE_BASE - LsoPackage.LSO_SETTINGS__THRESHOLDER_SETTINGS, null, msgs);
				}
				if (msgs != null) msgs.dispatch();
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, LsoPackage.LSO_SETTINGS__THRESHOLDER_SETTINGS, oldThresholderSettings, thresholderSettings));
			}
		}
		return thresholderSettings;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public ThresholderSettings basicGetThresholderSettings() {
		return thresholderSettings;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetThresholderSettings(ThresholderSettings newThresholderSettings, NotificationChain msgs) {
		ThresholderSettings oldThresholderSettings = thresholderSettings;
		thresholderSettings = newThresholderSettings;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, LsoPackage.LSO_SETTINGS__THRESHOLDER_SETTINGS, oldThresholderSettings, newThresholderSettings);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setThresholderSettings(ThresholderSettings newThresholderSettings) {
		if (newThresholderSettings != thresholderSettings) {
			NotificationChain msgs = null;
			if (thresholderSettings != null)
				msgs = ((InternalEObject)thresholderSettings).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - LsoPackage.LSO_SETTINGS__THRESHOLDER_SETTINGS, null, msgs);
			if (newThresholderSettings != null)
				msgs = ((InternalEObject)newThresholderSettings).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - LsoPackage.LSO_SETTINGS__THRESHOLDER_SETTINGS, null, msgs);
			msgs = basicSetThresholderSettings(newThresholderSettings, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, LsoPackage.LSO_SETTINGS__THRESHOLDER_SETTINGS, newThresholderSettings, newThresholderSettings));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public MoveGeneratorSettings getMoveGeneratorSettings() {
		if (moveGeneratorSettings != null && moveGeneratorSettings.eIsProxy()) {
			InternalEObject oldMoveGeneratorSettings = (InternalEObject)moveGeneratorSettings;
			moveGeneratorSettings = (MoveGeneratorSettings)eResolveProxy(oldMoveGeneratorSettings);
			if (moveGeneratorSettings != oldMoveGeneratorSettings) {
				InternalEObject newMoveGeneratorSettings = (InternalEObject)moveGeneratorSettings;
				NotificationChain msgs = oldMoveGeneratorSettings.eInverseRemove(this, EOPPOSITE_FEATURE_BASE - LsoPackage.LSO_SETTINGS__MOVE_GENERATOR_SETTINGS, null, null);
				if (newMoveGeneratorSettings.eInternalContainer() == null) {
					msgs = newMoveGeneratorSettings.eInverseAdd(this, EOPPOSITE_FEATURE_BASE - LsoPackage.LSO_SETTINGS__MOVE_GENERATOR_SETTINGS, null, msgs);
				}
				if (msgs != null) msgs.dispatch();
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, LsoPackage.LSO_SETTINGS__MOVE_GENERATOR_SETTINGS, oldMoveGeneratorSettings, moveGeneratorSettings));
			}
		}
		return moveGeneratorSettings;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public MoveGeneratorSettings basicGetMoveGeneratorSettings() {
		return moveGeneratorSettings;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetMoveGeneratorSettings(MoveGeneratorSettings newMoveGeneratorSettings, NotificationChain msgs) {
		MoveGeneratorSettings oldMoveGeneratorSettings = moveGeneratorSettings;
		moveGeneratorSettings = newMoveGeneratorSettings;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, LsoPackage.LSO_SETTINGS__MOVE_GENERATOR_SETTINGS, oldMoveGeneratorSettings, newMoveGeneratorSettings);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setMoveGeneratorSettings(MoveGeneratorSettings newMoveGeneratorSettings) {
		if (newMoveGeneratorSettings != moveGeneratorSettings) {
			NotificationChain msgs = null;
			if (moveGeneratorSettings != null)
				msgs = ((InternalEObject)moveGeneratorSettings).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - LsoPackage.LSO_SETTINGS__MOVE_GENERATOR_SETTINGS, null, msgs);
			if (newMoveGeneratorSettings != null)
				msgs = ((InternalEObject)newMoveGeneratorSettings).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - LsoPackage.LSO_SETTINGS__MOVE_GENERATOR_SETTINGS, null, msgs);
			msgs = basicSetMoveGeneratorSettings(newMoveGeneratorSettings, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, LsoPackage.LSO_SETTINGS__MOVE_GENERATOR_SETTINGS, newMoveGeneratorSettings, newMoveGeneratorSettings));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case LsoPackage.LSO_SETTINGS__THRESHOLDER_SETTINGS:
				return basicSetThresholderSettings(null, msgs);
			case LsoPackage.LSO_SETTINGS__MOVE_GENERATOR_SETTINGS:
				return basicSetMoveGeneratorSettings(null, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case LsoPackage.LSO_SETTINGS__NUMBER_OF_STEPS:
				return getNumberOfSteps();
			case LsoPackage.LSO_SETTINGS__THRESHOLDER_SETTINGS:
				if (resolve) return getThresholderSettings();
				return basicGetThresholderSettings();
			case LsoPackage.LSO_SETTINGS__MOVE_GENERATOR_SETTINGS:
				if (resolve) return getMoveGeneratorSettings();
				return basicGetMoveGeneratorSettings();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case LsoPackage.LSO_SETTINGS__NUMBER_OF_STEPS:
				setNumberOfSteps((Integer)newValue);
				return;
			case LsoPackage.LSO_SETTINGS__THRESHOLDER_SETTINGS:
				setThresholderSettings((ThresholderSettings)newValue);
				return;
			case LsoPackage.LSO_SETTINGS__MOVE_GENERATOR_SETTINGS:
				setMoveGeneratorSettings((MoveGeneratorSettings)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case LsoPackage.LSO_SETTINGS__NUMBER_OF_STEPS:
				setNumberOfSteps(NUMBER_OF_STEPS_EDEFAULT);
				return;
			case LsoPackage.LSO_SETTINGS__THRESHOLDER_SETTINGS:
				setThresholderSettings((ThresholderSettings)null);
				return;
			case LsoPackage.LSO_SETTINGS__MOVE_GENERATOR_SETTINGS:
				setMoveGeneratorSettings((MoveGeneratorSettings)null);
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case LsoPackage.LSO_SETTINGS__NUMBER_OF_STEPS:
				return numberOfSteps != NUMBER_OF_STEPS_EDEFAULT;
			case LsoPackage.LSO_SETTINGS__THRESHOLDER_SETTINGS:
				return thresholderSettings != null;
			case LsoPackage.LSO_SETTINGS__MOVE_GENERATOR_SETTINGS:
				return moveGeneratorSettings != null;
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (numberOfSteps: ");
		result.append(numberOfSteps);
		result.append(')');
		return result.toString();
	}

} // LSOSettingsImpl
