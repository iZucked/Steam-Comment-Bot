/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package scenario.impl;

import java.util.Date;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;

import scenario.*;
import scenario.Scenario;
import scenario.ScenarioFactory;
import scenario.ScenarioPackage;

import com.mmxlabs.lngscheduler.emf.datatypes.DateAndOptionalTime;

/**
 * <!-- begin-user-doc --> An implementation of the model <b>Factory</b>. <!--
 * end-user-doc -->
 * @generated
 */
public class ScenarioFactoryImpl extends EFactoryImpl implements
		ScenarioFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 */
	public static ScenarioFactory init() {
		try {
			ScenarioFactory theScenarioFactory = (ScenarioFactory)EPackage.Registry.INSTANCE.getEFactory("http://com.mmxlabs.lng.emf2"); 
			if (theScenarioFactory != null) {
				return theScenarioFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new ScenarioFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 */
	public ScenarioFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
			case ScenarioPackage.SCENARIO: return createScenario();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object createFromString(EDataType eDataType, String initialValue) {
		switch (eDataType.getClassifierID()) {
			case ScenarioPackage.DATE_AND_OPTIONAL_TIME:
				return createDateAndOptionalTimeFromString(eDataType, initialValue);
			case ScenarioPackage.PERCENTAGE:
				return createPercentageFromString(eDataType, initialValue);
			default:
				throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String convertToString(EDataType eDataType, Object instanceValue) {
		switch (eDataType.getClassifierID()) {
			case ScenarioPackage.DATE_AND_OPTIONAL_TIME:
				return convertDateAndOptionalTimeToString(eDataType, instanceValue);
			case ScenarioPackage.PERCENTAGE:
				return convertPercentageToString(eDataType, instanceValue);
			default:
				throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public Scenario createScenario() {
		ScenarioImpl scenario = new ScenarioImpl();
		return scenario;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NO
	 */
	public DateAndOptionalTime createDateAndOptionalTimeFromString(
			EDataType eDataType, String initialValue) {
		if (initialValue.charAt(0) == 'D') {
			final Date date = (Date) EcoreFactory.eINSTANCE.createFromString(
					EcorePackage.eINSTANCE.getEDate(),
					initialValue.substring(1));
			return new DateAndOptionalTime(date, true);
		} else {
			final Date date = (Date) EcoreFactory.eINSTANCE.createFromString(
					EcorePackage.eINSTANCE.getEDate(), initialValue);
			return new DateAndOptionalTime(date, false);
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NO
	 */
	public String convertDateAndOptionalTimeToString(EDataType eDataType,
			Object instanceValue) {
		final DateAndOptionalTime daot = (DateAndOptionalTime) instanceValue;

		final String datePart = EcoreFactory.eINSTANCE.convertToString(
				EcorePackage.eINSTANCE.getEDate(), daot);

		return (daot.isOnlyDate() ? "D" : "") + datePart;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NO
	 */
	public Double createPercentageFromString(EDataType eDataType,
			String initialValue) {
		// return (Percentage)super.createFromString(eDataType, initialValue);

//		final int t = Integer.parseInt(initialValue);
		
		final double d = Double.parseDouble(initialValue);
		
		return d;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated NO
	 */
	public String convertPercentageToString(EDataType eDataType,
			Object instanceValue) {
		// return super.convertToString(eDataType, instanceValue);
		return instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	public ScenarioPackage getScenarioPackage() {
		return (ScenarioPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static ScenarioPackage getPackage() {
		return ScenarioPackage.eINSTANCE;
	}

} // ScenarioFactoryImpl
