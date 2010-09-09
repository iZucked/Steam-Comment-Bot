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
		result.append(')');
		return result.toString();
	}

} //RandomMoveGeneratorSettingsImpl
