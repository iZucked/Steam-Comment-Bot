/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package scenario.optimiser.impl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import scenario.optimiser.LSOSettings;
import scenario.optimiser.OptimiserPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>LSO Settings</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link scenario.optimiser.impl.LSOSettingsImpl#getNumberOfSteps <em>Number Of Steps</em>}</li>
 *   <li>{@link scenario.optimiser.impl.LSOSettingsImpl#getStepSize <em>Step Size</em>}</li>
 *   <li>{@link scenario.optimiser.impl.LSOSettingsImpl#getInitialThreshold <em>Initial Threshold</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class LSOSettingsImpl extends OptimisationSettingsImpl implements LSOSettings {
	/**
	 * The default value of the '{@link #getNumberOfSteps() <em>Number Of Steps</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNumberOfSteps()
	 * @generated
	 * @ordered
	 */
	protected static final int NUMBER_OF_STEPS_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getNumberOfSteps() <em>Number Of Steps</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNumberOfSteps()
	 * @generated
	 * @ordered
	 */
	protected int numberOfSteps = NUMBER_OF_STEPS_EDEFAULT;

	/**
	 * The default value of the '{@link #getStepSize() <em>Step Size</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStepSize()
	 * @generated
	 * @ordered
	 */
	protected static final int STEP_SIZE_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getStepSize() <em>Step Size</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStepSize()
	 * @generated
	 * @ordered
	 */
	protected int stepSize = STEP_SIZE_EDEFAULT;

	/**
	 * The default value of the '{@link #getInitialThreshold() <em>Initial Threshold</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getInitialThreshold()
	 * @generated
	 * @ordered
	 */
	protected static final int INITIAL_THRESHOLD_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getInitialThreshold() <em>Initial Threshold</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getInitialThreshold()
	 * @generated
	 * @ordered
	 */
	protected int initialThreshold = INITIAL_THRESHOLD_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected LSOSettingsImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return OptimiserPackage.Literals.LSO_SETTINGS;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getNumberOfSteps() {
		return numberOfSteps;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setNumberOfSteps(int newNumberOfSteps) {
		int oldNumberOfSteps = numberOfSteps;
		numberOfSteps = newNumberOfSteps;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, OptimiserPackage.LSO_SETTINGS__NUMBER_OF_STEPS, oldNumberOfSteps, numberOfSteps));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getStepSize() {
		return stepSize;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setStepSize(int newStepSize) {
		int oldStepSize = stepSize;
		stepSize = newStepSize;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, OptimiserPackage.LSO_SETTINGS__STEP_SIZE, oldStepSize, stepSize));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getInitialThreshold() {
		return initialThreshold;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setInitialThreshold(int newInitialThreshold) {
		int oldInitialThreshold = initialThreshold;
		initialThreshold = newInitialThreshold;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, OptimiserPackage.LSO_SETTINGS__INITIAL_THRESHOLD, oldInitialThreshold, initialThreshold));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case OptimiserPackage.LSO_SETTINGS__NUMBER_OF_STEPS:
				return getNumberOfSteps();
			case OptimiserPackage.LSO_SETTINGS__STEP_SIZE:
				return getStepSize();
			case OptimiserPackage.LSO_SETTINGS__INITIAL_THRESHOLD:
				return getInitialThreshold();
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
			case OptimiserPackage.LSO_SETTINGS__NUMBER_OF_STEPS:
				setNumberOfSteps((Integer)newValue);
				return;
			case OptimiserPackage.LSO_SETTINGS__STEP_SIZE:
				setStepSize((Integer)newValue);
				return;
			case OptimiserPackage.LSO_SETTINGS__INITIAL_THRESHOLD:
				setInitialThreshold((Integer)newValue);
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
			case OptimiserPackage.LSO_SETTINGS__NUMBER_OF_STEPS:
				setNumberOfSteps(NUMBER_OF_STEPS_EDEFAULT);
				return;
			case OptimiserPackage.LSO_SETTINGS__STEP_SIZE:
				setStepSize(STEP_SIZE_EDEFAULT);
				return;
			case OptimiserPackage.LSO_SETTINGS__INITIAL_THRESHOLD:
				setInitialThreshold(INITIAL_THRESHOLD_EDEFAULT);
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
			case OptimiserPackage.LSO_SETTINGS__NUMBER_OF_STEPS:
				return numberOfSteps != NUMBER_OF_STEPS_EDEFAULT;
			case OptimiserPackage.LSO_SETTINGS__STEP_SIZE:
				return stepSize != STEP_SIZE_EDEFAULT;
			case OptimiserPackage.LSO_SETTINGS__INITIAL_THRESHOLD:
				return initialThreshold != INITIAL_THRESHOLD_EDEFAULT;
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
		result.append(" (numberOfSteps: ");
		result.append(numberOfSteps);
		result.append(", stepSize: ");
		result.append(stepSize);
		result.append(", initialThreshold: ");
		result.append(initialThreshold);
		result.append(')');
		return result.toString();
	}

} //LSOSettingsImpl
