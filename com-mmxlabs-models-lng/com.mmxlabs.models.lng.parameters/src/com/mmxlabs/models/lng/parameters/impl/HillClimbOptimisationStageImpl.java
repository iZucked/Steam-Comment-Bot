/**
 */
package com.mmxlabs.models.lng.parameters.impl;

import com.mmxlabs.models.lng.parameters.AnnealingSettings;
import com.mmxlabs.models.lng.parameters.ConstraintAndFitnessSettings;
import com.mmxlabs.models.lng.parameters.ConstraintsAndFitnessSettingsStage;
import com.mmxlabs.models.lng.parameters.HillClimbOptimisationStage;
import com.mmxlabs.models.lng.parameters.ParametersPackage;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Hill Climb Optimisation Stage</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.parameters.impl.HillClimbOptimisationStageImpl#getConstraintAndFitnessSettings <em>Constraint And Fitness Settings</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.parameters.impl.HillClimbOptimisationStageImpl#getSeed <em>Seed</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.parameters.impl.HillClimbOptimisationStageImpl#getAnnealingSettings <em>Annealing Settings</em>}</li>
 * </ul>
 *
 * @generated
 */
public class HillClimbOptimisationStageImpl extends ParallisableOptimisationStageImpl implements HillClimbOptimisationStage {
	/**
	 * The cached value of the '{@link #getConstraintAndFitnessSettings() <em>Constraint And Fitness Settings</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getConstraintAndFitnessSettings()
	 * @generated
	 * @ordered
	 */
	protected ConstraintAndFitnessSettings constraintAndFitnessSettings;

	/**
	 * The default value of the '{@link #getSeed() <em>Seed</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSeed()
	 * @generated
	 * @ordered
	 */
	protected static final int SEED_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getSeed() <em>Seed</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSeed()
	 * @generated
	 * @ordered
	 */
	protected int seed = SEED_EDEFAULT;

	/**
	 * The cached value of the '{@link #getAnnealingSettings() <em>Annealing Settings</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAnnealingSettings()
	 * @generated
	 * @ordered
	 */
	protected AnnealingSettings annealingSettings;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected HillClimbOptimisationStageImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ParametersPackage.Literals.HILL_CLIMB_OPTIMISATION_STAGE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getSeed() {
		return seed;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSeed(int newSeed) {
		int oldSeed = seed;
		seed = newSeed;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ParametersPackage.HILL_CLIMB_OPTIMISATION_STAGE__SEED, oldSeed, seed));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AnnealingSettings getAnnealingSettings() {
		return annealingSettings;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetAnnealingSettings(AnnealingSettings newAnnealingSettings, NotificationChain msgs) {
		AnnealingSettings oldAnnealingSettings = annealingSettings;
		annealingSettings = newAnnealingSettings;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ParametersPackage.HILL_CLIMB_OPTIMISATION_STAGE__ANNEALING_SETTINGS, oldAnnealingSettings, newAnnealingSettings);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setAnnealingSettings(AnnealingSettings newAnnealingSettings) {
		if (newAnnealingSettings != annealingSettings) {
			NotificationChain msgs = null;
			if (annealingSettings != null)
				msgs = ((InternalEObject)annealingSettings).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ParametersPackage.HILL_CLIMB_OPTIMISATION_STAGE__ANNEALING_SETTINGS, null, msgs);
			if (newAnnealingSettings != null)
				msgs = ((InternalEObject)newAnnealingSettings).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ParametersPackage.HILL_CLIMB_OPTIMISATION_STAGE__ANNEALING_SETTINGS, null, msgs);
			msgs = basicSetAnnealingSettings(newAnnealingSettings, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ParametersPackage.HILL_CLIMB_OPTIMISATION_STAGE__ANNEALING_SETTINGS, newAnnealingSettings, newAnnealingSettings));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ConstraintAndFitnessSettings getConstraintAndFitnessSettings() {
		return constraintAndFitnessSettings;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetConstraintAndFitnessSettings(ConstraintAndFitnessSettings newConstraintAndFitnessSettings, NotificationChain msgs) {
		ConstraintAndFitnessSettings oldConstraintAndFitnessSettings = constraintAndFitnessSettings;
		constraintAndFitnessSettings = newConstraintAndFitnessSettings;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ParametersPackage.HILL_CLIMB_OPTIMISATION_STAGE__CONSTRAINT_AND_FITNESS_SETTINGS, oldConstraintAndFitnessSettings, newConstraintAndFitnessSettings);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setConstraintAndFitnessSettings(ConstraintAndFitnessSettings newConstraintAndFitnessSettings) {
		if (newConstraintAndFitnessSettings != constraintAndFitnessSettings) {
			NotificationChain msgs = null;
			if (constraintAndFitnessSettings != null)
				msgs = ((InternalEObject)constraintAndFitnessSettings).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ParametersPackage.HILL_CLIMB_OPTIMISATION_STAGE__CONSTRAINT_AND_FITNESS_SETTINGS, null, msgs);
			if (newConstraintAndFitnessSettings != null)
				msgs = ((InternalEObject)newConstraintAndFitnessSettings).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ParametersPackage.HILL_CLIMB_OPTIMISATION_STAGE__CONSTRAINT_AND_FITNESS_SETTINGS, null, msgs);
			msgs = basicSetConstraintAndFitnessSettings(newConstraintAndFitnessSettings, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ParametersPackage.HILL_CLIMB_OPTIMISATION_STAGE__CONSTRAINT_AND_FITNESS_SETTINGS, newConstraintAndFitnessSettings, newConstraintAndFitnessSettings));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case ParametersPackage.HILL_CLIMB_OPTIMISATION_STAGE__CONSTRAINT_AND_FITNESS_SETTINGS:
				return basicSetConstraintAndFitnessSettings(null, msgs);
			case ParametersPackage.HILL_CLIMB_OPTIMISATION_STAGE__ANNEALING_SETTINGS:
				return basicSetAnnealingSettings(null, msgs);
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
			case ParametersPackage.HILL_CLIMB_OPTIMISATION_STAGE__CONSTRAINT_AND_FITNESS_SETTINGS:
				return getConstraintAndFitnessSettings();
			case ParametersPackage.HILL_CLIMB_OPTIMISATION_STAGE__SEED:
				return getSeed();
			case ParametersPackage.HILL_CLIMB_OPTIMISATION_STAGE__ANNEALING_SETTINGS:
				return getAnnealingSettings();
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
			case ParametersPackage.HILL_CLIMB_OPTIMISATION_STAGE__CONSTRAINT_AND_FITNESS_SETTINGS:
				setConstraintAndFitnessSettings((ConstraintAndFitnessSettings)newValue);
				return;
			case ParametersPackage.HILL_CLIMB_OPTIMISATION_STAGE__SEED:
				setSeed((Integer)newValue);
				return;
			case ParametersPackage.HILL_CLIMB_OPTIMISATION_STAGE__ANNEALING_SETTINGS:
				setAnnealingSettings((AnnealingSettings)newValue);
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
			case ParametersPackage.HILL_CLIMB_OPTIMISATION_STAGE__CONSTRAINT_AND_FITNESS_SETTINGS:
				setConstraintAndFitnessSettings((ConstraintAndFitnessSettings)null);
				return;
			case ParametersPackage.HILL_CLIMB_OPTIMISATION_STAGE__SEED:
				setSeed(SEED_EDEFAULT);
				return;
			case ParametersPackage.HILL_CLIMB_OPTIMISATION_STAGE__ANNEALING_SETTINGS:
				setAnnealingSettings((AnnealingSettings)null);
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
			case ParametersPackage.HILL_CLIMB_OPTIMISATION_STAGE__CONSTRAINT_AND_FITNESS_SETTINGS:
				return constraintAndFitnessSettings != null;
			case ParametersPackage.HILL_CLIMB_OPTIMISATION_STAGE__SEED:
				return seed != SEED_EDEFAULT;
			case ParametersPackage.HILL_CLIMB_OPTIMISATION_STAGE__ANNEALING_SETTINGS:
				return annealingSettings != null;
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int eBaseStructuralFeatureID(int derivedFeatureID, Class<?> baseClass) {
		if (baseClass == ConstraintsAndFitnessSettingsStage.class) {
			switch (derivedFeatureID) {
				case ParametersPackage.HILL_CLIMB_OPTIMISATION_STAGE__CONSTRAINT_AND_FITNESS_SETTINGS: return ParametersPackage.CONSTRAINTS_AND_FITNESS_SETTINGS_STAGE__CONSTRAINT_AND_FITNESS_SETTINGS;
				default: return -1;
			}
		}
		return super.eBaseStructuralFeatureID(derivedFeatureID, baseClass);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int eDerivedStructuralFeatureID(int baseFeatureID, Class<?> baseClass) {
		if (baseClass == ConstraintsAndFitnessSettingsStage.class) {
			switch (baseFeatureID) {
				case ParametersPackage.CONSTRAINTS_AND_FITNESS_SETTINGS_STAGE__CONSTRAINT_AND_FITNESS_SETTINGS: return ParametersPackage.HILL_CLIMB_OPTIMISATION_STAGE__CONSTRAINT_AND_FITNESS_SETTINGS;
				default: return -1;
			}
		}
		return super.eDerivedStructuralFeatureID(baseFeatureID, baseClass);
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
		result.append(" (seed: ");
		result.append(seed);
		result.append(')');
		return result.toString();
	}

} //HillClimbOptimisationStageImpl
