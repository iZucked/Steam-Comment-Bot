/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.optimiser.impl;
import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import com.mmxlabs.models.lng.optimiser.AnnealingSettings;
import com.mmxlabs.models.lng.optimiser.Argument;
import com.mmxlabs.models.lng.optimiser.Constraint;
import com.mmxlabs.models.lng.optimiser.Objective;
import com.mmxlabs.models.lng.optimiser.OptimisationRange;
import com.mmxlabs.models.lng.optimiser.OptimiserPackage;
import com.mmxlabs.models.lng.optimiser.OptimiserSettings;
import com.mmxlabs.models.lng.types.impl.AOptimisationSettingsImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Settings</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.optimiser.impl.OptimiserSettingsImpl#getObjectives <em>Objectives</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.optimiser.impl.OptimiserSettingsImpl#getConstraints <em>Constraints</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.optimiser.impl.OptimiserSettingsImpl#getRange <em>Range</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.optimiser.impl.OptimiserSettingsImpl#getAnnealingSettings <em>Annealing Settings</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.optimiser.impl.OptimiserSettingsImpl#getSeed <em>Seed</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.optimiser.impl.OptimiserSettingsImpl#getArguments <em>Arguments</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.optimiser.impl.OptimiserSettingsImpl#isRewire <em>Rewire</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class OptimiserSettingsImpl extends AOptimisationSettingsImpl implements OptimiserSettings {
	/**
	 * The cached value of the '{@link #getObjectives() <em>Objectives</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getObjectives()
	 * @generated
	 * @ordered
	 */
	protected EList<Objective> objectives;

	/**
	 * The cached value of the '{@link #getConstraints() <em>Constraints</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getConstraints()
	 * @generated
	 * @ordered
	 */
	protected EList<Constraint> constraints;

	/**
	 * The cached value of the '{@link #getRange() <em>Range</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRange()
	 * @generated
	 * @ordered
	 */
	protected OptimisationRange range;

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
	 * The cached value of the '{@link #getArguments() <em>Arguments</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getArguments()
	 * @generated
	 * @ordered
	 */
	protected EList<Argument> arguments;

	/**
	 * The default value of the '{@link #isRewire() <em>Rewire</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isRewire()
	 * @generated
	 * @ordered
	 */
	protected static final boolean REWIRE_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isRewire() <em>Rewire</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isRewire()
	 * @generated
	 * @ordered
	 */
	protected boolean rewire = REWIRE_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected OptimiserSettingsImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return OptimiserPackage.Literals.OPTIMISER_SETTINGS;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Objective> getObjectives() {
		if (objectives == null) {
			objectives = new EObjectContainmentEList<Objective>(Objective.class, this, OptimiserPackage.OPTIMISER_SETTINGS__OBJECTIVES);
		}
		return objectives;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Constraint> getConstraints() {
		if (constraints == null) {
			constraints = new EObjectContainmentEList<Constraint>(Constraint.class, this, OptimiserPackage.OPTIMISER_SETTINGS__CONSTRAINTS);
		}
		return constraints;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public OptimisationRange getRange() {
		return range;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetRange(OptimisationRange newRange, NotificationChain msgs) {
		OptimisationRange oldRange = range;
		range = newRange;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, OptimiserPackage.OPTIMISER_SETTINGS__RANGE, oldRange, newRange);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setRange(OptimisationRange newRange) {
		if (newRange != range) {
			NotificationChain msgs = null;
			if (range != null)
				msgs = ((InternalEObject)range).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - OptimiserPackage.OPTIMISER_SETTINGS__RANGE, null, msgs);
			if (newRange != null)
				msgs = ((InternalEObject)newRange).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - OptimiserPackage.OPTIMISER_SETTINGS__RANGE, null, msgs);
			msgs = basicSetRange(newRange, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, OptimiserPackage.OPTIMISER_SETTINGS__RANGE, newRange, newRange));
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
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, OptimiserPackage.OPTIMISER_SETTINGS__ANNEALING_SETTINGS, oldAnnealingSettings, newAnnealingSettings);
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
				msgs = ((InternalEObject)annealingSettings).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - OptimiserPackage.OPTIMISER_SETTINGS__ANNEALING_SETTINGS, null, msgs);
			if (newAnnealingSettings != null)
				msgs = ((InternalEObject)newAnnealingSettings).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - OptimiserPackage.OPTIMISER_SETTINGS__ANNEALING_SETTINGS, null, msgs);
			msgs = basicSetAnnealingSettings(newAnnealingSettings, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, OptimiserPackage.OPTIMISER_SETTINGS__ANNEALING_SETTINGS, newAnnealingSettings, newAnnealingSettings));
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
			eNotify(new ENotificationImpl(this, Notification.SET, OptimiserPackage.OPTIMISER_SETTINGS__SEED, oldSeed, seed));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Argument> getArguments() {
		if (arguments == null) {
			arguments = new EObjectContainmentEList<Argument>(Argument.class, this, OptimiserPackage.OPTIMISER_SETTINGS__ARGUMENTS);
		}
		return arguments;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isRewire() {
		return rewire;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setRewire(boolean newRewire) {
		boolean oldRewire = rewire;
		rewire = newRewire;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, OptimiserPackage.OPTIMISER_SETTINGS__REWIRE, oldRewire, rewire));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case OptimiserPackage.OPTIMISER_SETTINGS__OBJECTIVES:
				return ((InternalEList<?>)getObjectives()).basicRemove(otherEnd, msgs);
			case OptimiserPackage.OPTIMISER_SETTINGS__CONSTRAINTS:
				return ((InternalEList<?>)getConstraints()).basicRemove(otherEnd, msgs);
			case OptimiserPackage.OPTIMISER_SETTINGS__RANGE:
				return basicSetRange(null, msgs);
			case OptimiserPackage.OPTIMISER_SETTINGS__ANNEALING_SETTINGS:
				return basicSetAnnealingSettings(null, msgs);
			case OptimiserPackage.OPTIMISER_SETTINGS__ARGUMENTS:
				return ((InternalEList<?>)getArguments()).basicRemove(otherEnd, msgs);
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
			case OptimiserPackage.OPTIMISER_SETTINGS__OBJECTIVES:
				return getObjectives();
			case OptimiserPackage.OPTIMISER_SETTINGS__CONSTRAINTS:
				return getConstraints();
			case OptimiserPackage.OPTIMISER_SETTINGS__RANGE:
				return getRange();
			case OptimiserPackage.OPTIMISER_SETTINGS__ANNEALING_SETTINGS:
				return getAnnealingSettings();
			case OptimiserPackage.OPTIMISER_SETTINGS__SEED:
				return getSeed();
			case OptimiserPackage.OPTIMISER_SETTINGS__ARGUMENTS:
				return getArguments();
			case OptimiserPackage.OPTIMISER_SETTINGS__REWIRE:
				return isRewire();
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
			case OptimiserPackage.OPTIMISER_SETTINGS__OBJECTIVES:
				getObjectives().clear();
				getObjectives().addAll((Collection<? extends Objective>)newValue);
				return;
			case OptimiserPackage.OPTIMISER_SETTINGS__CONSTRAINTS:
				getConstraints().clear();
				getConstraints().addAll((Collection<? extends Constraint>)newValue);
				return;
			case OptimiserPackage.OPTIMISER_SETTINGS__RANGE:
				setRange((OptimisationRange)newValue);
				return;
			case OptimiserPackage.OPTIMISER_SETTINGS__ANNEALING_SETTINGS:
				setAnnealingSettings((AnnealingSettings)newValue);
				return;
			case OptimiserPackage.OPTIMISER_SETTINGS__SEED:
				setSeed((Integer)newValue);
				return;
			case OptimiserPackage.OPTIMISER_SETTINGS__ARGUMENTS:
				getArguments().clear();
				getArguments().addAll((Collection<? extends Argument>)newValue);
				return;
			case OptimiserPackage.OPTIMISER_SETTINGS__REWIRE:
				setRewire((Boolean)newValue);
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
			case OptimiserPackage.OPTIMISER_SETTINGS__OBJECTIVES:
				getObjectives().clear();
				return;
			case OptimiserPackage.OPTIMISER_SETTINGS__CONSTRAINTS:
				getConstraints().clear();
				return;
			case OptimiserPackage.OPTIMISER_SETTINGS__RANGE:
				setRange((OptimisationRange)null);
				return;
			case OptimiserPackage.OPTIMISER_SETTINGS__ANNEALING_SETTINGS:
				setAnnealingSettings((AnnealingSettings)null);
				return;
			case OptimiserPackage.OPTIMISER_SETTINGS__SEED:
				setSeed(SEED_EDEFAULT);
				return;
			case OptimiserPackage.OPTIMISER_SETTINGS__ARGUMENTS:
				getArguments().clear();
				return;
			case OptimiserPackage.OPTIMISER_SETTINGS__REWIRE:
				setRewire(REWIRE_EDEFAULT);
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
			case OptimiserPackage.OPTIMISER_SETTINGS__OBJECTIVES:
				return objectives != null && !objectives.isEmpty();
			case OptimiserPackage.OPTIMISER_SETTINGS__CONSTRAINTS:
				return constraints != null && !constraints.isEmpty();
			case OptimiserPackage.OPTIMISER_SETTINGS__RANGE:
				return range != null;
			case OptimiserPackage.OPTIMISER_SETTINGS__ANNEALING_SETTINGS:
				return annealingSettings != null;
			case OptimiserPackage.OPTIMISER_SETTINGS__SEED:
				return seed != SEED_EDEFAULT;
			case OptimiserPackage.OPTIMISER_SETTINGS__ARGUMENTS:
				return arguments != null && !arguments.isEmpty();
			case OptimiserPackage.OPTIMISER_SETTINGS__REWIRE:
				return rewire != REWIRE_EDEFAULT;
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
		result.append(" (seed: ");
		result.append(seed);
		result.append(", rewire: ");
		result.append(rewire);
		result.append(')');
		return result.toString();
	}

} // end of OptimiserSettingsImpl

// finish type fixing
