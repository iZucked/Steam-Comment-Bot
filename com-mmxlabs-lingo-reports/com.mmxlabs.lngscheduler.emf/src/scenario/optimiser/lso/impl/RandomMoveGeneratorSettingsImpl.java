/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package scenario.optimiser.lso.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

import scenario.optimiser.lso.LsoPackage;
import scenario.optimiser.lso.RandomMoveGeneratorSettings;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Random Move Generator Settings</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link scenario.optimiser.lso.impl.RandomMoveGeneratorSettingsImpl#isUsing2over2 <em>Using2over2</em>}</li>
 *   <li>{@link scenario.optimiser.lso.impl.RandomMoveGeneratorSettingsImpl#isUsing3over2 <em>Using3over2</em>}</li>
 *   <li>{@link scenario.optimiser.lso.impl.RandomMoveGeneratorSettingsImpl#isUsing4over1 <em>Using4over1</em>}</li>
 *   <li>{@link scenario.optimiser.lso.impl.RandomMoveGeneratorSettingsImpl#isUsing4over2 <em>Using4over2</em>}</li>
 *   <li>{@link scenario.optimiser.lso.impl.RandomMoveGeneratorSettingsImpl#getWeightFor2opt2 <em>Weight For2opt2</em>}</li>
 *   <li>{@link scenario.optimiser.lso.impl.RandomMoveGeneratorSettingsImpl#getWeightFor3opt2 <em>Weight For3opt2</em>}</li>
 *   <li>{@link scenario.optimiser.lso.impl.RandomMoveGeneratorSettingsImpl#getWeightFor4opt1 <em>Weight For4opt1</em>}</li>
 *   <li>{@link scenario.optimiser.lso.impl.RandomMoveGeneratorSettingsImpl#getWeightFor4opt2 <em>Weight For4opt2</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class RandomMoveGeneratorSettingsImpl extends MoveGeneratorSettingsImpl implements RandomMoveGeneratorSettings {
	/**
	 * The default value of the '{@link #isUsing2over2() <em>Using2over2</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isUsing2over2()
	 * @generated
	 * @ordered
	 */
	protected static final boolean USING2OVER2_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isUsing2over2() <em>Using2over2</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isUsing2over2()
	 * @generated
	 * @ordered
	 */
	protected boolean using2over2 = USING2OVER2_EDEFAULT;

	/**
	 * The default value of the '{@link #isUsing3over2() <em>Using3over2</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isUsing3over2()
	 * @generated
	 * @ordered
	 */
	protected static final boolean USING3OVER2_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isUsing3over2() <em>Using3over2</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isUsing3over2()
	 * @generated
	 * @ordered
	 */
	protected boolean using3over2 = USING3OVER2_EDEFAULT;

	/**
	 * The default value of the '{@link #isUsing4over1() <em>Using4over1</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isUsing4over1()
	 * @generated
	 * @ordered
	 */
	protected static final boolean USING4OVER1_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isUsing4over1() <em>Using4over1</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isUsing4over1()
	 * @generated
	 * @ordered
	 */
	protected boolean using4over1 = USING4OVER1_EDEFAULT;

	/**
	 * The default value of the '{@link #isUsing4over2() <em>Using4over2</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isUsing4over2()
	 * @generated
	 * @ordered
	 */
	protected static final boolean USING4OVER2_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isUsing4over2() <em>Using4over2</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isUsing4over2()
	 * @generated
	 * @ordered
	 */
	protected boolean using4over2 = USING4OVER2_EDEFAULT;

	/**
	 * The default value of the '{@link #getWeightFor2opt2() <em>Weight For2opt2</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getWeightFor2opt2()
	 * @generated
	 * @ordered
	 */
	protected static final double WEIGHT_FOR2OPT2_EDEFAULT = 1.0;

	/**
	 * The cached value of the '{@link #getWeightFor2opt2() <em>Weight For2opt2</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getWeightFor2opt2()
	 * @generated
	 * @ordered
	 */
	protected double weightFor2opt2 = WEIGHT_FOR2OPT2_EDEFAULT;

	/**
	 * The default value of the '{@link #getWeightFor3opt2() <em>Weight For3opt2</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getWeightFor3opt2()
	 * @generated
	 * @ordered
	 */
	protected static final double WEIGHT_FOR3OPT2_EDEFAULT = 1.0;

	/**
	 * The cached value of the '{@link #getWeightFor3opt2() <em>Weight For3opt2</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getWeightFor3opt2()
	 * @generated
	 * @ordered
	 */
	protected double weightFor3opt2 = WEIGHT_FOR3OPT2_EDEFAULT;

	/**
	 * The default value of the '{@link #getWeightFor4opt1() <em>Weight For4opt1</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getWeightFor4opt1()
	 * @generated
	 * @ordered
	 */
	protected static final double WEIGHT_FOR4OPT1_EDEFAULT = 1.0;

	/**
	 * The cached value of the '{@link #getWeightFor4opt1() <em>Weight For4opt1</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getWeightFor4opt1()
	 * @generated
	 * @ordered
	 */
	protected double weightFor4opt1 = WEIGHT_FOR4OPT1_EDEFAULT;

	/**
	 * The default value of the '{@link #getWeightFor4opt2() <em>Weight For4opt2</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getWeightFor4opt2()
	 * @generated
	 * @ordered
	 */
	protected static final double WEIGHT_FOR4OPT2_EDEFAULT = 1.0;

	/**
	 * The cached value of the '{@link #getWeightFor4opt2() <em>Weight For4opt2</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getWeightFor4opt2()
	 * @generated
	 * @ordered
	 */
	protected double weightFor4opt2 = WEIGHT_FOR4OPT2_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected RandomMoveGeneratorSettingsImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return LsoPackage.Literals.RANDOM_MOVE_GENERATOR_SETTINGS;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isUsing2over2() {
		return using2over2;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setUsing2over2(boolean newUsing2over2) {
		boolean oldUsing2over2 = using2over2;
		using2over2 = newUsing2over2;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, LsoPackage.RANDOM_MOVE_GENERATOR_SETTINGS__USING2OVER2, oldUsing2over2, using2over2));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isUsing3over2() {
		return using3over2;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setUsing3over2(boolean newUsing3over2) {
		boolean oldUsing3over2 = using3over2;
		using3over2 = newUsing3over2;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, LsoPackage.RANDOM_MOVE_GENERATOR_SETTINGS__USING3OVER2, oldUsing3over2, using3over2));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isUsing4over1() {
		return using4over1;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setUsing4over1(boolean newUsing4over1) {
		boolean oldUsing4over1 = using4over1;
		using4over1 = newUsing4over1;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, LsoPackage.RANDOM_MOVE_GENERATOR_SETTINGS__USING4OVER1, oldUsing4over1, using4over1));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isUsing4over2() {
		return using4over2;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setUsing4over2(boolean newUsing4over2) {
		boolean oldUsing4over2 = using4over2;
		using4over2 = newUsing4over2;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, LsoPackage.RANDOM_MOVE_GENERATOR_SETTINGS__USING4OVER2, oldUsing4over2, using4over2));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public double getWeightFor2opt2() {
		return weightFor2opt2;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setWeightFor2opt2(double newWeightFor2opt2) {
		double oldWeightFor2opt2 = weightFor2opt2;
		weightFor2opt2 = newWeightFor2opt2;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, LsoPackage.RANDOM_MOVE_GENERATOR_SETTINGS__WEIGHT_FOR2OPT2, oldWeightFor2opt2, weightFor2opt2));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public double getWeightFor3opt2() {
		return weightFor3opt2;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setWeightFor3opt2(double newWeightFor3opt2) {
		double oldWeightFor3opt2 = weightFor3opt2;
		weightFor3opt2 = newWeightFor3opt2;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, LsoPackage.RANDOM_MOVE_GENERATOR_SETTINGS__WEIGHT_FOR3OPT2, oldWeightFor3opt2, weightFor3opt2));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public double getWeightFor4opt1() {
		return weightFor4opt1;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setWeightFor4opt1(double newWeightFor4opt1) {
		double oldWeightFor4opt1 = weightFor4opt1;
		weightFor4opt1 = newWeightFor4opt1;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, LsoPackage.RANDOM_MOVE_GENERATOR_SETTINGS__WEIGHT_FOR4OPT1, oldWeightFor4opt1, weightFor4opt1));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public double getWeightFor4opt2() {
		return weightFor4opt2;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setWeightFor4opt2(double newWeightFor4opt2) {
		double oldWeightFor4opt2 = weightFor4opt2;
		weightFor4opt2 = newWeightFor4opt2;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, LsoPackage.RANDOM_MOVE_GENERATOR_SETTINGS__WEIGHT_FOR4OPT2, oldWeightFor4opt2, weightFor4opt2));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case LsoPackage.RANDOM_MOVE_GENERATOR_SETTINGS__USING2OVER2:
				return isUsing2over2();
			case LsoPackage.RANDOM_MOVE_GENERATOR_SETTINGS__USING3OVER2:
				return isUsing3over2();
			case LsoPackage.RANDOM_MOVE_GENERATOR_SETTINGS__USING4OVER1:
				return isUsing4over1();
			case LsoPackage.RANDOM_MOVE_GENERATOR_SETTINGS__USING4OVER2:
				return isUsing4over2();
			case LsoPackage.RANDOM_MOVE_GENERATOR_SETTINGS__WEIGHT_FOR2OPT2:
				return getWeightFor2opt2();
			case LsoPackage.RANDOM_MOVE_GENERATOR_SETTINGS__WEIGHT_FOR3OPT2:
				return getWeightFor3opt2();
			case LsoPackage.RANDOM_MOVE_GENERATOR_SETTINGS__WEIGHT_FOR4OPT1:
				return getWeightFor4opt1();
			case LsoPackage.RANDOM_MOVE_GENERATOR_SETTINGS__WEIGHT_FOR4OPT2:
				return getWeightFor4opt2();
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
			case LsoPackage.RANDOM_MOVE_GENERATOR_SETTINGS__USING2OVER2:
				setUsing2over2((Boolean)newValue);
				return;
			case LsoPackage.RANDOM_MOVE_GENERATOR_SETTINGS__USING3OVER2:
				setUsing3over2((Boolean)newValue);
				return;
			case LsoPackage.RANDOM_MOVE_GENERATOR_SETTINGS__USING4OVER1:
				setUsing4over1((Boolean)newValue);
				return;
			case LsoPackage.RANDOM_MOVE_GENERATOR_SETTINGS__USING4OVER2:
				setUsing4over2((Boolean)newValue);
				return;
			case LsoPackage.RANDOM_MOVE_GENERATOR_SETTINGS__WEIGHT_FOR2OPT2:
				setWeightFor2opt2((Double)newValue);
				return;
			case LsoPackage.RANDOM_MOVE_GENERATOR_SETTINGS__WEIGHT_FOR3OPT2:
				setWeightFor3opt2((Double)newValue);
				return;
			case LsoPackage.RANDOM_MOVE_GENERATOR_SETTINGS__WEIGHT_FOR4OPT1:
				setWeightFor4opt1((Double)newValue);
				return;
			case LsoPackage.RANDOM_MOVE_GENERATOR_SETTINGS__WEIGHT_FOR4OPT2:
				setWeightFor4opt2((Double)newValue);
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
			case LsoPackage.RANDOM_MOVE_GENERATOR_SETTINGS__USING2OVER2:
				setUsing2over2(USING2OVER2_EDEFAULT);
				return;
			case LsoPackage.RANDOM_MOVE_GENERATOR_SETTINGS__USING3OVER2:
				setUsing3over2(USING3OVER2_EDEFAULT);
				return;
			case LsoPackage.RANDOM_MOVE_GENERATOR_SETTINGS__USING4OVER1:
				setUsing4over1(USING4OVER1_EDEFAULT);
				return;
			case LsoPackage.RANDOM_MOVE_GENERATOR_SETTINGS__USING4OVER2:
				setUsing4over2(USING4OVER2_EDEFAULT);
				return;
			case LsoPackage.RANDOM_MOVE_GENERATOR_SETTINGS__WEIGHT_FOR2OPT2:
				setWeightFor2opt2(WEIGHT_FOR2OPT2_EDEFAULT);
				return;
			case LsoPackage.RANDOM_MOVE_GENERATOR_SETTINGS__WEIGHT_FOR3OPT2:
				setWeightFor3opt2(WEIGHT_FOR3OPT2_EDEFAULT);
				return;
			case LsoPackage.RANDOM_MOVE_GENERATOR_SETTINGS__WEIGHT_FOR4OPT1:
				setWeightFor4opt1(WEIGHT_FOR4OPT1_EDEFAULT);
				return;
			case LsoPackage.RANDOM_MOVE_GENERATOR_SETTINGS__WEIGHT_FOR4OPT2:
				setWeightFor4opt2(WEIGHT_FOR4OPT2_EDEFAULT);
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
			case LsoPackage.RANDOM_MOVE_GENERATOR_SETTINGS__USING2OVER2:
				return using2over2 != USING2OVER2_EDEFAULT;
			case LsoPackage.RANDOM_MOVE_GENERATOR_SETTINGS__USING3OVER2:
				return using3over2 != USING3OVER2_EDEFAULT;
			case LsoPackage.RANDOM_MOVE_GENERATOR_SETTINGS__USING4OVER1:
				return using4over1 != USING4OVER1_EDEFAULT;
			case LsoPackage.RANDOM_MOVE_GENERATOR_SETTINGS__USING4OVER2:
				return using4over2 != USING4OVER2_EDEFAULT;
			case LsoPackage.RANDOM_MOVE_GENERATOR_SETTINGS__WEIGHT_FOR2OPT2:
				return weightFor2opt2 != WEIGHT_FOR2OPT2_EDEFAULT;
			case LsoPackage.RANDOM_MOVE_GENERATOR_SETTINGS__WEIGHT_FOR3OPT2:
				return weightFor3opt2 != WEIGHT_FOR3OPT2_EDEFAULT;
			case LsoPackage.RANDOM_MOVE_GENERATOR_SETTINGS__WEIGHT_FOR4OPT1:
				return weightFor4opt1 != WEIGHT_FOR4OPT1_EDEFAULT;
			case LsoPackage.RANDOM_MOVE_GENERATOR_SETTINGS__WEIGHT_FOR4OPT2:
				return weightFor4opt2 != WEIGHT_FOR4OPT2_EDEFAULT;
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
		result.append(" (using2over2: ");
		result.append(using2over2);
		result.append(", using3over2: ");
		result.append(using3over2);
		result.append(", using4over1: ");
		result.append(using4over1);
		result.append(", using4over2: ");
		result.append(using4over2);
		result.append(", weightFor2opt2: ");
		result.append(weightFor2opt2);
		result.append(", weightFor3opt2: ");
		result.append(weightFor3opt2);
		result.append(", weightFor4opt1: ");
		result.append(weightFor4opt1);
		result.append(", weightFor4opt2: ");
		result.append(weightFor4opt2);
		result.append(')');
		return result.toString();
	}

} //RandomMoveGeneratorSettingsImpl
