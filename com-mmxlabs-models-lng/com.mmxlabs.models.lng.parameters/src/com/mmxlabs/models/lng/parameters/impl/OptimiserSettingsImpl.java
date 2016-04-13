/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2016
 * All rights reserved.
 */
package com.mmxlabs.models.lng.parameters.impl;
import com.mmxlabs.models.lng.parameters.ActionPlanSettings;
import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import com.mmxlabs.models.lng.parameters.AnnealingSettings;
import com.mmxlabs.models.lng.parameters.Argument;
import com.mmxlabs.models.lng.parameters.Constraint;
import com.mmxlabs.models.lng.parameters.IndividualSolutionImprovementSettings;
import com.mmxlabs.models.lng.parameters.Objective;
import com.mmxlabs.models.lng.parameters.OptimisationRange;
import com.mmxlabs.models.lng.parameters.OptimiserSettings;
import com.mmxlabs.models.lng.parameters.ParametersPackage;
import com.mmxlabs.models.lng.parameters.SimilaritySettings;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.mmxcore.NamedObject;
import com.mmxlabs.models.mmxcore.impl.UUIDObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Settings</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.mmxlabs.models.lng.parameters.impl.OptimiserSettingsImpl#getName <em>Name</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.parameters.impl.OptimiserSettingsImpl#getObjectives <em>Objectives</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.parameters.impl.OptimiserSettingsImpl#getConstraints <em>Constraints</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.parameters.impl.OptimiserSettingsImpl#getRange <em>Range</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.parameters.impl.OptimiserSettingsImpl#getAnnealingSettings <em>Annealing Settings</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.parameters.impl.OptimiserSettingsImpl#getSeed <em>Seed</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.parameters.impl.OptimiserSettingsImpl#getArguments <em>Arguments</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.parameters.impl.OptimiserSettingsImpl#isGenerateCharterOuts <em>Generate Charter Outs</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.parameters.impl.OptimiserSettingsImpl#isShippingOnly <em>Shipping Only</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.parameters.impl.OptimiserSettingsImpl#getSimilaritySettings <em>Similarity Settings</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.parameters.impl.OptimiserSettingsImpl#getSolutionImprovementSettings <em>Solution Improvement Settings</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.parameters.impl.OptimiserSettingsImpl#isBuildActionSets <em>Build Action Sets</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.parameters.impl.OptimiserSettingsImpl#getActionPlanSettings <em>Action Plan Settings</em>}</li>
 *   <li>{@link com.mmxlabs.models.lng.parameters.impl.OptimiserSettingsImpl#getFloatingDaysLimit <em>Floating Days Limit</em>}</li>
 * </ul>
 *
 * @generated
 */
public class OptimiserSettingsImpl extends UUIDObjectImpl implements OptimiserSettings {
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
	 * The default value of the '{@link #isGenerateCharterOuts() <em>Generate Charter Outs</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isGenerateCharterOuts()
	 * @generated
	 * @ordered
	 */
	protected static final boolean GENERATE_CHARTER_OUTS_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isGenerateCharterOuts() <em>Generate Charter Outs</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isGenerateCharterOuts()
	 * @generated
	 * @ordered
	 */
	protected boolean generateCharterOuts = GENERATE_CHARTER_OUTS_EDEFAULT;

	/**
	 * The default value of the '{@link #isShippingOnly() <em>Shipping Only</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isShippingOnly()
	 * @generated
	 * @ordered
	 */
	protected static final boolean SHIPPING_ONLY_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isShippingOnly() <em>Shipping Only</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isShippingOnly()
	 * @generated
	 * @ordered
	 */
	protected boolean shippingOnly = SHIPPING_ONLY_EDEFAULT;

	/**
	 * The cached value of the '{@link #getSimilaritySettings() <em>Similarity Settings</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSimilaritySettings()
	 * @generated
	 * @ordered
	 */
	protected SimilaritySettings similaritySettings;

	/**
	 * The cached value of the '{@link #getSolutionImprovementSettings() <em>Solution Improvement Settings</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSolutionImprovementSettings()
	 * @generated
	 * @ordered
	 */
	protected IndividualSolutionImprovementSettings solutionImprovementSettings;

	/**
	 * The default value of the '{@link #isBuildActionSets() <em>Build Action Sets</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isBuildActionSets()
	 * @generated
	 * @ordered
	 */
	protected static final boolean BUILD_ACTION_SETS_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isBuildActionSets() <em>Build Action Sets</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isBuildActionSets()
	 * @generated
	 * @ordered
	 */
	protected boolean buildActionSets = BUILD_ACTION_SETS_EDEFAULT;

	/**
	 * The cached value of the '{@link #getActionPlanSettings() <em>Action Plan Settings</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getActionPlanSettings()
	 * @generated
	 * @ordered
	 */
	protected ActionPlanSettings actionPlanSettings;

	/**
	 * The default value of the '{@link #getFloatingDaysLimit() <em>Floating Days Limit</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFloatingDaysLimit()
	 * @generated
	 * @ordered
	 */
	protected static final int FLOATING_DAYS_LIMIT_EDEFAULT = 15;

	/**
	 * The cached value of the '{@link #getFloatingDaysLimit() <em>Floating Days Limit</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFloatingDaysLimit()
	 * @generated
	 * @ordered
	 */
	protected int floatingDaysLimit = FLOATING_DAYS_LIMIT_EDEFAULT;

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
		return ParametersPackage.Literals.OPTIMISER_SETTINGS;
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
			eNotify(new ENotificationImpl(this, Notification.SET, ParametersPackage.OPTIMISER_SETTINGS__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<Objective> getObjectives() {
		if (objectives == null) {
			objectives = new EObjectContainmentEList<Objective>(Objective.class, this, ParametersPackage.OPTIMISER_SETTINGS__OBJECTIVES);
		}
		return objectives;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<Constraint> getConstraints() {
		if (constraints == null) {
			constraints = new EObjectContainmentEList<Constraint>(Constraint.class, this, ParametersPackage.OPTIMISER_SETTINGS__CONSTRAINTS);
		}
		return constraints;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
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
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ParametersPackage.OPTIMISER_SETTINGS__RANGE, oldRange, newRange);
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
	public void setRange(OptimisationRange newRange) {
		if (newRange != range) {
			NotificationChain msgs = null;
			if (range != null)
				msgs = ((InternalEObject)range).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ParametersPackage.OPTIMISER_SETTINGS__RANGE, null, msgs);
			if (newRange != null)
				msgs = ((InternalEObject)newRange).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ParametersPackage.OPTIMISER_SETTINGS__RANGE, null, msgs);
			msgs = basicSetRange(newRange, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ParametersPackage.OPTIMISER_SETTINGS__RANGE, newRange, newRange));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
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
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ParametersPackage.OPTIMISER_SETTINGS__ANNEALING_SETTINGS, oldAnnealingSettings, newAnnealingSettings);
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
	public void setAnnealingSettings(AnnealingSettings newAnnealingSettings) {
		if (newAnnealingSettings != annealingSettings) {
			NotificationChain msgs = null;
			if (annealingSettings != null)
				msgs = ((InternalEObject)annealingSettings).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ParametersPackage.OPTIMISER_SETTINGS__ANNEALING_SETTINGS, null, msgs);
			if (newAnnealingSettings != null)
				msgs = ((InternalEObject)newAnnealingSettings).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ParametersPackage.OPTIMISER_SETTINGS__ANNEALING_SETTINGS, null, msgs);
			msgs = basicSetAnnealingSettings(newAnnealingSettings, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ParametersPackage.OPTIMISER_SETTINGS__ANNEALING_SETTINGS, newAnnealingSettings, newAnnealingSettings));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getSeed() {
		return seed;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setSeed(int newSeed) {
		int oldSeed = seed;
		seed = newSeed;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ParametersPackage.OPTIMISER_SETTINGS__SEED, oldSeed, seed));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<Argument> getArguments() {
		if (arguments == null) {
			arguments = new EObjectContainmentEList<Argument>(Argument.class, this, ParametersPackage.OPTIMISER_SETTINGS__ARGUMENTS);
		}
		return arguments;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isGenerateCharterOuts() {
		return generateCharterOuts;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setGenerateCharterOuts(boolean newGenerateCharterOuts) {
		boolean oldGenerateCharterOuts = generateCharterOuts;
		generateCharterOuts = newGenerateCharterOuts;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ParametersPackage.OPTIMISER_SETTINGS__GENERATE_CHARTER_OUTS, oldGenerateCharterOuts, generateCharterOuts));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isShippingOnly() {
		return shippingOnly;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setShippingOnly(boolean newShippingOnly) {
		boolean oldShippingOnly = shippingOnly;
		shippingOnly = newShippingOnly;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ParametersPackage.OPTIMISER_SETTINGS__SHIPPING_ONLY, oldShippingOnly, shippingOnly));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public SimilaritySettings getSimilaritySettings() {
		return similaritySettings;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetSimilaritySettings(SimilaritySettings newSimilaritySettings, NotificationChain msgs) {
		SimilaritySettings oldSimilaritySettings = similaritySettings;
		similaritySettings = newSimilaritySettings;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ParametersPackage.OPTIMISER_SETTINGS__SIMILARITY_SETTINGS, oldSimilaritySettings, newSimilaritySettings);
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
	public void setSimilaritySettings(SimilaritySettings newSimilaritySettings) {
		if (newSimilaritySettings != similaritySettings) {
			NotificationChain msgs = null;
			if (similaritySettings != null)
				msgs = ((InternalEObject)similaritySettings).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ParametersPackage.OPTIMISER_SETTINGS__SIMILARITY_SETTINGS, null, msgs);
			if (newSimilaritySettings != null)
				msgs = ((InternalEObject)newSimilaritySettings).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ParametersPackage.OPTIMISER_SETTINGS__SIMILARITY_SETTINGS, null, msgs);
			msgs = basicSetSimilaritySettings(newSimilaritySettings, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ParametersPackage.OPTIMISER_SETTINGS__SIMILARITY_SETTINGS, newSimilaritySettings, newSimilaritySettings));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public IndividualSolutionImprovementSettings getSolutionImprovementSettings() {
		return solutionImprovementSettings;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetSolutionImprovementSettings(IndividualSolutionImprovementSettings newSolutionImprovementSettings, NotificationChain msgs) {
		IndividualSolutionImprovementSettings oldSolutionImprovementSettings = solutionImprovementSettings;
		solutionImprovementSettings = newSolutionImprovementSettings;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ParametersPackage.OPTIMISER_SETTINGS__SOLUTION_IMPROVEMENT_SETTINGS, oldSolutionImprovementSettings, newSolutionImprovementSettings);
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
	public void setSolutionImprovementSettings(IndividualSolutionImprovementSettings newSolutionImprovementSettings) {
		if (newSolutionImprovementSettings != solutionImprovementSettings) {
			NotificationChain msgs = null;
			if (solutionImprovementSettings != null)
				msgs = ((InternalEObject)solutionImprovementSettings).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ParametersPackage.OPTIMISER_SETTINGS__SOLUTION_IMPROVEMENT_SETTINGS, null, msgs);
			if (newSolutionImprovementSettings != null)
				msgs = ((InternalEObject)newSolutionImprovementSettings).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ParametersPackage.OPTIMISER_SETTINGS__SOLUTION_IMPROVEMENT_SETTINGS, null, msgs);
			msgs = basicSetSolutionImprovementSettings(newSolutionImprovementSettings, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ParametersPackage.OPTIMISER_SETTINGS__SOLUTION_IMPROVEMENT_SETTINGS, newSolutionImprovementSettings, newSolutionImprovementSettings));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isBuildActionSets() {
		return buildActionSets;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setBuildActionSets(boolean newBuildActionSets) {
		boolean oldBuildActionSets = buildActionSets;
		buildActionSets = newBuildActionSets;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ParametersPackage.OPTIMISER_SETTINGS__BUILD_ACTION_SETS, oldBuildActionSets, buildActionSets));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ActionPlanSettings getActionPlanSettings() {
		return actionPlanSettings;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetActionPlanSettings(ActionPlanSettings newActionPlanSettings, NotificationChain msgs) {
		ActionPlanSettings oldActionPlanSettings = actionPlanSettings;
		actionPlanSettings = newActionPlanSettings;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ParametersPackage.OPTIMISER_SETTINGS__ACTION_PLAN_SETTINGS, oldActionPlanSettings, newActionPlanSettings);
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
	public void setActionPlanSettings(ActionPlanSettings newActionPlanSettings) {
		if (newActionPlanSettings != actionPlanSettings) {
			NotificationChain msgs = null;
			if (actionPlanSettings != null)
				msgs = ((InternalEObject)actionPlanSettings).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ParametersPackage.OPTIMISER_SETTINGS__ACTION_PLAN_SETTINGS, null, msgs);
			if (newActionPlanSettings != null)
				msgs = ((InternalEObject)newActionPlanSettings).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ParametersPackage.OPTIMISER_SETTINGS__ACTION_PLAN_SETTINGS, null, msgs);
			msgs = basicSetActionPlanSettings(newActionPlanSettings, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ParametersPackage.OPTIMISER_SETTINGS__ACTION_PLAN_SETTINGS, newActionPlanSettings, newActionPlanSettings));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getFloatingDaysLimit() {
		return floatingDaysLimit;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setFloatingDaysLimit(int newFloatingDaysLimit) {
		int oldFloatingDaysLimit = floatingDaysLimit;
		floatingDaysLimit = newFloatingDaysLimit;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ParametersPackage.OPTIMISER_SETTINGS__FLOATING_DAYS_LIMIT, oldFloatingDaysLimit, floatingDaysLimit));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case ParametersPackage.OPTIMISER_SETTINGS__OBJECTIVES:
				return ((InternalEList<?>)getObjectives()).basicRemove(otherEnd, msgs);
			case ParametersPackage.OPTIMISER_SETTINGS__CONSTRAINTS:
				return ((InternalEList<?>)getConstraints()).basicRemove(otherEnd, msgs);
			case ParametersPackage.OPTIMISER_SETTINGS__RANGE:
				return basicSetRange(null, msgs);
			case ParametersPackage.OPTIMISER_SETTINGS__ANNEALING_SETTINGS:
				return basicSetAnnealingSettings(null, msgs);
			case ParametersPackage.OPTIMISER_SETTINGS__ARGUMENTS:
				return ((InternalEList<?>)getArguments()).basicRemove(otherEnd, msgs);
			case ParametersPackage.OPTIMISER_SETTINGS__SIMILARITY_SETTINGS:
				return basicSetSimilaritySettings(null, msgs);
			case ParametersPackage.OPTIMISER_SETTINGS__SOLUTION_IMPROVEMENT_SETTINGS:
				return basicSetSolutionImprovementSettings(null, msgs);
			case ParametersPackage.OPTIMISER_SETTINGS__ACTION_PLAN_SETTINGS:
				return basicSetActionPlanSettings(null, msgs);
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
			case ParametersPackage.OPTIMISER_SETTINGS__NAME:
				return getName();
			case ParametersPackage.OPTIMISER_SETTINGS__OBJECTIVES:
				return getObjectives();
			case ParametersPackage.OPTIMISER_SETTINGS__CONSTRAINTS:
				return getConstraints();
			case ParametersPackage.OPTIMISER_SETTINGS__RANGE:
				return getRange();
			case ParametersPackage.OPTIMISER_SETTINGS__ANNEALING_SETTINGS:
				return getAnnealingSettings();
			case ParametersPackage.OPTIMISER_SETTINGS__SEED:
				return getSeed();
			case ParametersPackage.OPTIMISER_SETTINGS__ARGUMENTS:
				return getArguments();
			case ParametersPackage.OPTIMISER_SETTINGS__GENERATE_CHARTER_OUTS:
				return isGenerateCharterOuts();
			case ParametersPackage.OPTIMISER_SETTINGS__SHIPPING_ONLY:
				return isShippingOnly();
			case ParametersPackage.OPTIMISER_SETTINGS__SIMILARITY_SETTINGS:
				return getSimilaritySettings();
			case ParametersPackage.OPTIMISER_SETTINGS__SOLUTION_IMPROVEMENT_SETTINGS:
				return getSolutionImprovementSettings();
			case ParametersPackage.OPTIMISER_SETTINGS__BUILD_ACTION_SETS:
				return isBuildActionSets();
			case ParametersPackage.OPTIMISER_SETTINGS__ACTION_PLAN_SETTINGS:
				return getActionPlanSettings();
			case ParametersPackage.OPTIMISER_SETTINGS__FLOATING_DAYS_LIMIT:
				return getFloatingDaysLimit();
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
			case ParametersPackage.OPTIMISER_SETTINGS__NAME:
				setName((String)newValue);
				return;
			case ParametersPackage.OPTIMISER_SETTINGS__OBJECTIVES:
				getObjectives().clear();
				getObjectives().addAll((Collection<? extends Objective>)newValue);
				return;
			case ParametersPackage.OPTIMISER_SETTINGS__CONSTRAINTS:
				getConstraints().clear();
				getConstraints().addAll((Collection<? extends Constraint>)newValue);
				return;
			case ParametersPackage.OPTIMISER_SETTINGS__RANGE:
				setRange((OptimisationRange)newValue);
				return;
			case ParametersPackage.OPTIMISER_SETTINGS__ANNEALING_SETTINGS:
				setAnnealingSettings((AnnealingSettings)newValue);
				return;
			case ParametersPackage.OPTIMISER_SETTINGS__SEED:
				setSeed((Integer)newValue);
				return;
			case ParametersPackage.OPTIMISER_SETTINGS__ARGUMENTS:
				getArguments().clear();
				getArguments().addAll((Collection<? extends Argument>)newValue);
				return;
			case ParametersPackage.OPTIMISER_SETTINGS__GENERATE_CHARTER_OUTS:
				setGenerateCharterOuts((Boolean)newValue);
				return;
			case ParametersPackage.OPTIMISER_SETTINGS__SHIPPING_ONLY:
				setShippingOnly((Boolean)newValue);
				return;
			case ParametersPackage.OPTIMISER_SETTINGS__SIMILARITY_SETTINGS:
				setSimilaritySettings((SimilaritySettings)newValue);
				return;
			case ParametersPackage.OPTIMISER_SETTINGS__SOLUTION_IMPROVEMENT_SETTINGS:
				setSolutionImprovementSettings((IndividualSolutionImprovementSettings)newValue);
				return;
			case ParametersPackage.OPTIMISER_SETTINGS__BUILD_ACTION_SETS:
				setBuildActionSets((Boolean)newValue);
				return;
			case ParametersPackage.OPTIMISER_SETTINGS__ACTION_PLAN_SETTINGS:
				setActionPlanSettings((ActionPlanSettings)newValue);
				return;
			case ParametersPackage.OPTIMISER_SETTINGS__FLOATING_DAYS_LIMIT:
				setFloatingDaysLimit((Integer)newValue);
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
			case ParametersPackage.OPTIMISER_SETTINGS__NAME:
				setName(NAME_EDEFAULT);
				return;
			case ParametersPackage.OPTIMISER_SETTINGS__OBJECTIVES:
				getObjectives().clear();
				return;
			case ParametersPackage.OPTIMISER_SETTINGS__CONSTRAINTS:
				getConstraints().clear();
				return;
			case ParametersPackage.OPTIMISER_SETTINGS__RANGE:
				setRange((OptimisationRange)null);
				return;
			case ParametersPackage.OPTIMISER_SETTINGS__ANNEALING_SETTINGS:
				setAnnealingSettings((AnnealingSettings)null);
				return;
			case ParametersPackage.OPTIMISER_SETTINGS__SEED:
				setSeed(SEED_EDEFAULT);
				return;
			case ParametersPackage.OPTIMISER_SETTINGS__ARGUMENTS:
				getArguments().clear();
				return;
			case ParametersPackage.OPTIMISER_SETTINGS__GENERATE_CHARTER_OUTS:
				setGenerateCharterOuts(GENERATE_CHARTER_OUTS_EDEFAULT);
				return;
			case ParametersPackage.OPTIMISER_SETTINGS__SHIPPING_ONLY:
				setShippingOnly(SHIPPING_ONLY_EDEFAULT);
				return;
			case ParametersPackage.OPTIMISER_SETTINGS__SIMILARITY_SETTINGS:
				setSimilaritySettings((SimilaritySettings)null);
				return;
			case ParametersPackage.OPTIMISER_SETTINGS__SOLUTION_IMPROVEMENT_SETTINGS:
				setSolutionImprovementSettings((IndividualSolutionImprovementSettings)null);
				return;
			case ParametersPackage.OPTIMISER_SETTINGS__BUILD_ACTION_SETS:
				setBuildActionSets(BUILD_ACTION_SETS_EDEFAULT);
				return;
			case ParametersPackage.OPTIMISER_SETTINGS__ACTION_PLAN_SETTINGS:
				setActionPlanSettings((ActionPlanSettings)null);
				return;
			case ParametersPackage.OPTIMISER_SETTINGS__FLOATING_DAYS_LIMIT:
				setFloatingDaysLimit(FLOATING_DAYS_LIMIT_EDEFAULT);
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
			case ParametersPackage.OPTIMISER_SETTINGS__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case ParametersPackage.OPTIMISER_SETTINGS__OBJECTIVES:
				return objectives != null && !objectives.isEmpty();
			case ParametersPackage.OPTIMISER_SETTINGS__CONSTRAINTS:
				return constraints != null && !constraints.isEmpty();
			case ParametersPackage.OPTIMISER_SETTINGS__RANGE:
				return range != null;
			case ParametersPackage.OPTIMISER_SETTINGS__ANNEALING_SETTINGS:
				return annealingSettings != null;
			case ParametersPackage.OPTIMISER_SETTINGS__SEED:
				return seed != SEED_EDEFAULT;
			case ParametersPackage.OPTIMISER_SETTINGS__ARGUMENTS:
				return arguments != null && !arguments.isEmpty();
			case ParametersPackage.OPTIMISER_SETTINGS__GENERATE_CHARTER_OUTS:
				return generateCharterOuts != GENERATE_CHARTER_OUTS_EDEFAULT;
			case ParametersPackage.OPTIMISER_SETTINGS__SHIPPING_ONLY:
				return shippingOnly != SHIPPING_ONLY_EDEFAULT;
			case ParametersPackage.OPTIMISER_SETTINGS__SIMILARITY_SETTINGS:
				return similaritySettings != null;
			case ParametersPackage.OPTIMISER_SETTINGS__SOLUTION_IMPROVEMENT_SETTINGS:
				return solutionImprovementSettings != null;
			case ParametersPackage.OPTIMISER_SETTINGS__BUILD_ACTION_SETS:
				return buildActionSets != BUILD_ACTION_SETS_EDEFAULT;
			case ParametersPackage.OPTIMISER_SETTINGS__ACTION_PLAN_SETTINGS:
				return actionPlanSettings != null;
			case ParametersPackage.OPTIMISER_SETTINGS__FLOATING_DAYS_LIMIT:
				return floatingDaysLimit != FLOATING_DAYS_LIMIT_EDEFAULT;
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
		if (baseClass == NamedObject.class) {
			switch (derivedFeatureID) {
				case ParametersPackage.OPTIMISER_SETTINGS__NAME: return MMXCorePackage.NAMED_OBJECT__NAME;
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
		if (baseClass == NamedObject.class) {
			switch (baseFeatureID) {
				case MMXCorePackage.NAMED_OBJECT__NAME: return ParametersPackage.OPTIMISER_SETTINGS__NAME;
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
		result.append(" (name: ");
		result.append(name);
		result.append(", seed: ");
		result.append(seed);
		result.append(", generateCharterOuts: ");
		result.append(generateCharterOuts);
		result.append(", shippingOnly: ");
		result.append(shippingOnly);
		result.append(", buildActionSets: ");
		result.append(buildActionSets);
		result.append(", floatingDaysLimit: ");
		result.append(floatingDaysLimit);
		result.append(')');
		return result.toString();
	}

} // end of OptimiserSettingsImpl

// finish type fixing
