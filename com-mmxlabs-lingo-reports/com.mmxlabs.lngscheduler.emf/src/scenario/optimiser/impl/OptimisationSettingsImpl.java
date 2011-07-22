/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package scenario.optimiser.impl;

import java.util.Collection;
import java.util.Date;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import scenario.impl.NamedObjectImpl;
import scenario.optimiser.Constraint;
import scenario.optimiser.DiscountCurve;
import scenario.optimiser.Objective;
import scenario.optimiser.OptimisationSettings;
import scenario.optimiser.OptimiserPackage;
import scenario.schedule.Schedule;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Optimisation Settings</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link scenario.optimiser.impl.OptimisationSettingsImpl#getRandomSeed <em>Random Seed</em>}</li>
 *   <li>{@link scenario.optimiser.impl.OptimisationSettingsImpl#getInitialSchedule <em>Initial Schedule</em>}</li>
 *   <li>{@link scenario.optimiser.impl.OptimisationSettingsImpl#getFreezeDaysFromStart <em>Freeze Days From Start</em>}</li>
 *   <li>{@link scenario.optimiser.impl.OptimisationSettingsImpl#getIgnoreElementsAfter <em>Ignore Elements After</em>}</li>
 *   <li>{@link scenario.optimiser.impl.OptimisationSettingsImpl#getConstraints <em>Constraints</em>}</li>
 *   <li>{@link scenario.optimiser.impl.OptimisationSettingsImpl#getObjectives <em>Objectives</em>}</li>
 *   <li>{@link scenario.optimiser.impl.OptimisationSettingsImpl#getDefaultDiscountCurve <em>Default Discount Curve</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class OptimisationSettingsImpl extends NamedObjectImpl implements OptimisationSettings {
	/**
	 * The default value of the '{@link #getRandomSeed() <em>Random Seed</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRandomSeed()
	 * @generated
	 * @ordered
	 */
	protected static final long RANDOM_SEED_EDEFAULT = 0L;

	/**
	 * The cached value of the '{@link #getRandomSeed() <em>Random Seed</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRandomSeed()
	 * @generated
	 * @ordered
	 */
	protected long randomSeed = RANDOM_SEED_EDEFAULT;

	/**
	 * The cached value of the '{@link #getInitialSchedule() <em>Initial Schedule</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getInitialSchedule()
	 * @generated
	 * @ordered
	 */
	protected Schedule initialSchedule;

	/**
	 * The default value of the '{@link #getFreezeDaysFromStart() <em>Freeze Days From Start</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFreezeDaysFromStart()
	 * @generated
	 * @ordered
	 */
	protected static final int FREEZE_DAYS_FROM_START_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getFreezeDaysFromStart() <em>Freeze Days From Start</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFreezeDaysFromStart()
	 * @generated
	 * @ordered
	 */
	protected int freezeDaysFromStart = FREEZE_DAYS_FROM_START_EDEFAULT;

	/**
	 * The default value of the '{@link #getIgnoreElementsAfter() <em>Ignore Elements After</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getIgnoreElementsAfter()
	 * @generated
	 * @ordered
	 */
	protected static final Date IGNORE_ELEMENTS_AFTER_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getIgnoreElementsAfter() <em>Ignore Elements After</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getIgnoreElementsAfter()
	 * @generated
	 * @ordered
	 */
	protected Date ignoreElementsAfter = IGNORE_ELEMENTS_AFTER_EDEFAULT;

	/**
	 * This is true if the Ignore Elements After attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean ignoreElementsAfterESet;

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
	 * The cached value of the '{@link #getObjectives() <em>Objectives</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getObjectives()
	 * @generated
	 * @ordered
	 */
	protected EList<Objective> objectives;

	/**
	 * The cached value of the '{@link #getDefaultDiscountCurve() <em>Default Discount Curve</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDefaultDiscountCurve()
	 * @generated
	 * @ordered
	 */
	protected DiscountCurve defaultDiscountCurve;

	/**
	 * This is true if the Default Discount Curve containment reference has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean defaultDiscountCurveESet;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected OptimisationSettingsImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return OptimiserPackage.Literals.OPTIMISATION_SETTINGS;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public long getRandomSeed() {
		return randomSeed;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setRandomSeed(long newRandomSeed) {
		long oldRandomSeed = randomSeed;
		randomSeed = newRandomSeed;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, OptimiserPackage.OPTIMISATION_SETTINGS__RANDOM_SEED, oldRandomSeed, randomSeed));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Constraint> getConstraints() {
		if (constraints == null) {
			constraints = new EObjectContainmentEList.Resolving<Constraint>(Constraint.class, this, OptimiserPackage.OPTIMISATION_SETTINGS__CONSTRAINTS);
		}
		return constraints;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Objective> getObjectives() {
		if (objectives == null) {
			objectives = new EObjectContainmentEList.Resolving<Objective>(Objective.class, this, OptimiserPackage.OPTIMISATION_SETTINGS__OBJECTIVES);
		}
		return objectives;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Schedule getInitialSchedule() {
		if (initialSchedule != null && initialSchedule.eIsProxy()) {
			InternalEObject oldInitialSchedule = (InternalEObject)initialSchedule;
			initialSchedule = (Schedule)eResolveProxy(oldInitialSchedule);
			if (initialSchedule != oldInitialSchedule) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, OptimiserPackage.OPTIMISATION_SETTINGS__INITIAL_SCHEDULE, oldInitialSchedule, initialSchedule));
			}
		}
		return initialSchedule;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Schedule basicGetInitialSchedule() {
		return initialSchedule;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setInitialSchedule(Schedule newInitialSchedule) {
		Schedule oldInitialSchedule = initialSchedule;
		initialSchedule = newInitialSchedule;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, OptimiserPackage.OPTIMISATION_SETTINGS__INITIAL_SCHEDULE, oldInitialSchedule, initialSchedule));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DiscountCurve getDefaultDiscountCurve() {
		if (defaultDiscountCurve != null && defaultDiscountCurve.eIsProxy()) {
			InternalEObject oldDefaultDiscountCurve = (InternalEObject)defaultDiscountCurve;
			defaultDiscountCurve = (DiscountCurve)eResolveProxy(oldDefaultDiscountCurve);
			if (defaultDiscountCurve != oldDefaultDiscountCurve) {
				InternalEObject newDefaultDiscountCurve = (InternalEObject)defaultDiscountCurve;
				NotificationChain msgs = oldDefaultDiscountCurve.eInverseRemove(this, EOPPOSITE_FEATURE_BASE - OptimiserPackage.OPTIMISATION_SETTINGS__DEFAULT_DISCOUNT_CURVE, null, null);
				if (newDefaultDiscountCurve.eInternalContainer() == null) {
					msgs = newDefaultDiscountCurve.eInverseAdd(this, EOPPOSITE_FEATURE_BASE - OptimiserPackage.OPTIMISATION_SETTINGS__DEFAULT_DISCOUNT_CURVE, null, msgs);
				}
				if (msgs != null) msgs.dispatch();
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, OptimiserPackage.OPTIMISATION_SETTINGS__DEFAULT_DISCOUNT_CURVE, oldDefaultDiscountCurve, defaultDiscountCurve));
			}
		}
		return defaultDiscountCurve;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DiscountCurve basicGetDefaultDiscountCurve() {
		return defaultDiscountCurve;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetDefaultDiscountCurve(DiscountCurve newDefaultDiscountCurve, NotificationChain msgs) {
		DiscountCurve oldDefaultDiscountCurve = defaultDiscountCurve;
		defaultDiscountCurve = newDefaultDiscountCurve;
		boolean oldDefaultDiscountCurveESet = defaultDiscountCurveESet;
		defaultDiscountCurveESet = true;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, OptimiserPackage.OPTIMISATION_SETTINGS__DEFAULT_DISCOUNT_CURVE, oldDefaultDiscountCurve, newDefaultDiscountCurve, !oldDefaultDiscountCurveESet);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDefaultDiscountCurve(DiscountCurve newDefaultDiscountCurve) {
		if (newDefaultDiscountCurve != defaultDiscountCurve) {
			NotificationChain msgs = null;
			if (defaultDiscountCurve != null)
				msgs = ((InternalEObject)defaultDiscountCurve).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - OptimiserPackage.OPTIMISATION_SETTINGS__DEFAULT_DISCOUNT_CURVE, null, msgs);
			if (newDefaultDiscountCurve != null)
				msgs = ((InternalEObject)newDefaultDiscountCurve).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - OptimiserPackage.OPTIMISATION_SETTINGS__DEFAULT_DISCOUNT_CURVE, null, msgs);
			msgs = basicSetDefaultDiscountCurve(newDefaultDiscountCurve, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else {
			boolean oldDefaultDiscountCurveESet = defaultDiscountCurveESet;
			defaultDiscountCurveESet = true;
			if (eNotificationRequired())
				eNotify(new ENotificationImpl(this, Notification.SET, OptimiserPackage.OPTIMISATION_SETTINGS__DEFAULT_DISCOUNT_CURVE, newDefaultDiscountCurve, newDefaultDiscountCurve, !oldDefaultDiscountCurveESet));
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicUnsetDefaultDiscountCurve(NotificationChain msgs) {
		DiscountCurve oldDefaultDiscountCurve = defaultDiscountCurve;
		defaultDiscountCurve = null;
		boolean oldDefaultDiscountCurveESet = defaultDiscountCurveESet;
		defaultDiscountCurveESet = false;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.UNSET, OptimiserPackage.OPTIMISATION_SETTINGS__DEFAULT_DISCOUNT_CURVE, oldDefaultDiscountCurve, null, oldDefaultDiscountCurveESet);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetDefaultDiscountCurve() {
		if (defaultDiscountCurve != null) {
			NotificationChain msgs = null;
			msgs = ((InternalEObject)defaultDiscountCurve).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - OptimiserPackage.OPTIMISATION_SETTINGS__DEFAULT_DISCOUNT_CURVE, null, msgs);
			msgs = basicUnsetDefaultDiscountCurve(msgs);
			if (msgs != null) msgs.dispatch();
		}
		else {
			boolean oldDefaultDiscountCurveESet = defaultDiscountCurveESet;
			defaultDiscountCurveESet = false;
			if (eNotificationRequired())
				eNotify(new ENotificationImpl(this, Notification.UNSET, OptimiserPackage.OPTIMISATION_SETTINGS__DEFAULT_DISCOUNT_CURVE, null, null, oldDefaultDiscountCurveESet));
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetDefaultDiscountCurve() {
		return defaultDiscountCurveESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getFreezeDaysFromStart() {
		return freezeDaysFromStart;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setFreezeDaysFromStart(int newFreezeDaysFromStart) {
		int oldFreezeDaysFromStart = freezeDaysFromStart;
		freezeDaysFromStart = newFreezeDaysFromStart;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, OptimiserPackage.OPTIMISATION_SETTINGS__FREEZE_DAYS_FROM_START, oldFreezeDaysFromStart, freezeDaysFromStart));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Date getIgnoreElementsAfter() {
		return ignoreElementsAfter;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setIgnoreElementsAfter(Date newIgnoreElementsAfter) {
		Date oldIgnoreElementsAfter = ignoreElementsAfter;
		ignoreElementsAfter = newIgnoreElementsAfter;
		boolean oldIgnoreElementsAfterESet = ignoreElementsAfterESet;
		ignoreElementsAfterESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, OptimiserPackage.OPTIMISATION_SETTINGS__IGNORE_ELEMENTS_AFTER, oldIgnoreElementsAfter, ignoreElementsAfter, !oldIgnoreElementsAfterESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void unsetIgnoreElementsAfter() {
		Date oldIgnoreElementsAfter = ignoreElementsAfter;
		boolean oldIgnoreElementsAfterESet = ignoreElementsAfterESet;
		ignoreElementsAfter = IGNORE_ELEMENTS_AFTER_EDEFAULT;
		ignoreElementsAfterESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, OptimiserPackage.OPTIMISATION_SETTINGS__IGNORE_ELEMENTS_AFTER, oldIgnoreElementsAfter, IGNORE_ELEMENTS_AFTER_EDEFAULT, oldIgnoreElementsAfterESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isSetIgnoreElementsAfter() {
		return ignoreElementsAfterESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case OptimiserPackage.OPTIMISATION_SETTINGS__CONSTRAINTS:
				return ((InternalEList<?>)getConstraints()).basicRemove(otherEnd, msgs);
			case OptimiserPackage.OPTIMISATION_SETTINGS__OBJECTIVES:
				return ((InternalEList<?>)getObjectives()).basicRemove(otherEnd, msgs);
			case OptimiserPackage.OPTIMISATION_SETTINGS__DEFAULT_DISCOUNT_CURVE:
				return basicUnsetDefaultDiscountCurve(msgs);
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
			case OptimiserPackage.OPTIMISATION_SETTINGS__RANDOM_SEED:
				return getRandomSeed();
			case OptimiserPackage.OPTIMISATION_SETTINGS__INITIAL_SCHEDULE:
				if (resolve) return getInitialSchedule();
				return basicGetInitialSchedule();
			case OptimiserPackage.OPTIMISATION_SETTINGS__FREEZE_DAYS_FROM_START:
				return getFreezeDaysFromStart();
			case OptimiserPackage.OPTIMISATION_SETTINGS__IGNORE_ELEMENTS_AFTER:
				return getIgnoreElementsAfter();
			case OptimiserPackage.OPTIMISATION_SETTINGS__CONSTRAINTS:
				return getConstraints();
			case OptimiserPackage.OPTIMISATION_SETTINGS__OBJECTIVES:
				return getObjectives();
			case OptimiserPackage.OPTIMISATION_SETTINGS__DEFAULT_DISCOUNT_CURVE:
				if (resolve) return getDefaultDiscountCurve();
				return basicGetDefaultDiscountCurve();
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
			case OptimiserPackage.OPTIMISATION_SETTINGS__RANDOM_SEED:
				setRandomSeed((Long)newValue);
				return;
			case OptimiserPackage.OPTIMISATION_SETTINGS__INITIAL_SCHEDULE:
				setInitialSchedule((Schedule)newValue);
				return;
			case OptimiserPackage.OPTIMISATION_SETTINGS__FREEZE_DAYS_FROM_START:
				setFreezeDaysFromStart((Integer)newValue);
				return;
			case OptimiserPackage.OPTIMISATION_SETTINGS__IGNORE_ELEMENTS_AFTER:
				setIgnoreElementsAfter((Date)newValue);
				return;
			case OptimiserPackage.OPTIMISATION_SETTINGS__CONSTRAINTS:
				getConstraints().clear();
				getConstraints().addAll((Collection<? extends Constraint>)newValue);
				return;
			case OptimiserPackage.OPTIMISATION_SETTINGS__OBJECTIVES:
				getObjectives().clear();
				getObjectives().addAll((Collection<? extends Objective>)newValue);
				return;
			case OptimiserPackage.OPTIMISATION_SETTINGS__DEFAULT_DISCOUNT_CURVE:
				setDefaultDiscountCurve((DiscountCurve)newValue);
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
			case OptimiserPackage.OPTIMISATION_SETTINGS__RANDOM_SEED:
				setRandomSeed(RANDOM_SEED_EDEFAULT);
				return;
			case OptimiserPackage.OPTIMISATION_SETTINGS__INITIAL_SCHEDULE:
				setInitialSchedule((Schedule)null);
				return;
			case OptimiserPackage.OPTIMISATION_SETTINGS__FREEZE_DAYS_FROM_START:
				setFreezeDaysFromStart(FREEZE_DAYS_FROM_START_EDEFAULT);
				return;
			case OptimiserPackage.OPTIMISATION_SETTINGS__IGNORE_ELEMENTS_AFTER:
				unsetIgnoreElementsAfter();
				return;
			case OptimiserPackage.OPTIMISATION_SETTINGS__CONSTRAINTS:
				getConstraints().clear();
				return;
			case OptimiserPackage.OPTIMISATION_SETTINGS__OBJECTIVES:
				getObjectives().clear();
				return;
			case OptimiserPackage.OPTIMISATION_SETTINGS__DEFAULT_DISCOUNT_CURVE:
				unsetDefaultDiscountCurve();
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
			case OptimiserPackage.OPTIMISATION_SETTINGS__RANDOM_SEED:
				return randomSeed != RANDOM_SEED_EDEFAULT;
			case OptimiserPackage.OPTIMISATION_SETTINGS__INITIAL_SCHEDULE:
				return initialSchedule != null;
			case OptimiserPackage.OPTIMISATION_SETTINGS__FREEZE_DAYS_FROM_START:
				return freezeDaysFromStart != FREEZE_DAYS_FROM_START_EDEFAULT;
			case OptimiserPackage.OPTIMISATION_SETTINGS__IGNORE_ELEMENTS_AFTER:
				return isSetIgnoreElementsAfter();
			case OptimiserPackage.OPTIMISATION_SETTINGS__CONSTRAINTS:
				return constraints != null && !constraints.isEmpty();
			case OptimiserPackage.OPTIMISATION_SETTINGS__OBJECTIVES:
				return objectives != null && !objectives.isEmpty();
			case OptimiserPackage.OPTIMISATION_SETTINGS__DEFAULT_DISCOUNT_CURVE:
				return isSetDefaultDiscountCurve();
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
		result.append(" (randomSeed: ");
		result.append(randomSeed);
		result.append(", freezeDaysFromStart: ");
		result.append(freezeDaysFromStart);
		result.append(", ignoreElementsAfter: ");
		if (ignoreElementsAfterESet) result.append(ignoreElementsAfter); else result.append("<unset>");
		result.append(')');
		return result.toString();
	}

} //OptimisationSettingsImpl
