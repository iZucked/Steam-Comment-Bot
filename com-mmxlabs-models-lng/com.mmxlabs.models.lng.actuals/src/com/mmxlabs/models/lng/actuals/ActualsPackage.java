/**
 */
package com.mmxlabs.models.lng.actuals;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each operation of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see com.mmxlabs.models.lng.actuals.ActualsFactory
 * @model kind="package"
 * @generated
 */
public interface ActualsPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "actuals";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://www.mmxlabs.com/models/lng/actuals/1/";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "lng.actuals";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	ActualsPackage eINSTANCE = com.mmxlabs.models.lng.actuals.impl.ActualsPackageImpl.init();

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.actuals.impl.SlotActualsImpl <em>Slot Actuals</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.actuals.impl.SlotActualsImpl
	 * @see com.mmxlabs.models.lng.actuals.impl.ActualsPackageImpl#getSlotActuals()
	 * @generated
	 */
	int SLOT_ACTUALS = 0;

	/**
	 * The feature id for the '<em><b>CV</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_ACTUALS__CV = 0;

	/**
	 * The feature id for the '<em><b>Volume</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_ACTUALS__VOLUME = 1;

	/**
	 * The feature id for the '<em><b>Mm Btu</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_ACTUALS__MM_BTU = 2;

	/**
	 * The feature id for the '<em><b>Port Charges</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_ACTUALS__PORT_CHARGES = 3;

	/**
	 * The feature id for the '<em><b>Base Fuel Consumption</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_ACTUALS__BASE_FUEL_CONSUMPTION = 4;

	/**
	 * The number of structural features of the '<em>Slot Actuals</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_ACTUALS_FEATURE_COUNT = 5;

	/**
	 * The number of operations of the '<em>Slot Actuals</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SLOT_ACTUALS_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.actuals.impl.CargoActualsImpl <em>Cargo Actuals</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.actuals.impl.CargoActualsImpl
	 * @see com.mmxlabs.models.lng.actuals.impl.ActualsPackageImpl#getCargoActuals()
	 * @generated
	 */
	int CARGO_ACTUALS = 1;

	/**
	 * The feature id for the '<em><b>Base Fuel Price</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_ACTUALS__BASE_FUEL_PRICE = 0;

	/**
	 * The feature id for the '<em><b>Volume</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_ACTUALS__VOLUME = 1;

	/**
	 * The feature id for the '<em><b>Insurance Premium</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_ACTUALS__INSURANCE_PREMIUM = 2;

	/**
	 * The feature id for the '<em><b>Crew Bonus</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_ACTUALS__CREW_BONUS = 3;

	/**
	 * The feature id for the '<em><b>Actuals</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_ACTUALS__ACTUALS = 4;

	/**
	 * The number of structural features of the '<em>Cargo Actuals</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_ACTUALS_FEATURE_COUNT = 5;

	/**
	 * The number of operations of the '<em>Cargo Actuals</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CARGO_ACTUALS_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.actuals.impl.LoadActualsImpl <em>Load Actuals</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.actuals.impl.LoadActualsImpl
	 * @see com.mmxlabs.models.lng.actuals.impl.ActualsPackageImpl#getLoadActuals()
	 * @generated
	 */
	int LOAD_ACTUALS = 2;

	/**
	 * The number of structural features of the '<em>Load Actuals</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_ACTUALS_FEATURE_COUNT = 0;

	/**
	 * The number of operations of the '<em>Load Actuals</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOAD_ACTUALS_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link com.mmxlabs.models.lng.actuals.impl.DischargeActualsImpl <em>Discharge Actuals</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see com.mmxlabs.models.lng.actuals.impl.DischargeActualsImpl
	 * @see com.mmxlabs.models.lng.actuals.impl.ActualsPackageImpl#getDischargeActuals()
	 * @generated
	 */
	int DISCHARGE_ACTUALS = 3;

	/**
	 * The number of structural features of the '<em>Discharge Actuals</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_ACTUALS_FEATURE_COUNT = 0;

	/**
	 * The number of operations of the '<em>Discharge Actuals</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DISCHARGE_ACTUALS_OPERATION_COUNT = 0;


	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.actuals.SlotActuals <em>Slot Actuals</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Slot Actuals</em>'.
	 * @see com.mmxlabs.models.lng.actuals.SlotActuals
	 * @generated
	 */
	EClass getSlotActuals();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.actuals.SlotActuals#getCV <em>CV</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>CV</em>'.
	 * @see com.mmxlabs.models.lng.actuals.SlotActuals#getCV()
	 * @see #getSlotActuals()
	 * @generated
	 */
	EAttribute getSlotActuals_CV();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.actuals.SlotActuals#getVolume <em>Volume</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Volume</em>'.
	 * @see com.mmxlabs.models.lng.actuals.SlotActuals#getVolume()
	 * @see #getSlotActuals()
	 * @generated
	 */
	EAttribute getSlotActuals_Volume();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.actuals.SlotActuals#getMmBtu <em>Mm Btu</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Mm Btu</em>'.
	 * @see com.mmxlabs.models.lng.actuals.SlotActuals#getMmBtu()
	 * @see #getSlotActuals()
	 * @generated
	 */
	EAttribute getSlotActuals_MmBtu();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.actuals.SlotActuals#getPortCharges <em>Port Charges</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Port Charges</em>'.
	 * @see com.mmxlabs.models.lng.actuals.SlotActuals#getPortCharges()
	 * @see #getSlotActuals()
	 * @generated
	 */
	EAttribute getSlotActuals_PortCharges();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.actuals.SlotActuals#getBaseFuelConsumption <em>Base Fuel Consumption</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Base Fuel Consumption</em>'.
	 * @see com.mmxlabs.models.lng.actuals.SlotActuals#getBaseFuelConsumption()
	 * @see #getSlotActuals()
	 * @generated
	 */
	EAttribute getSlotActuals_BaseFuelConsumption();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.actuals.CargoActuals <em>Cargo Actuals</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Cargo Actuals</em>'.
	 * @see com.mmxlabs.models.lng.actuals.CargoActuals
	 * @generated
	 */
	EClass getCargoActuals();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.actuals.CargoActuals#getBaseFuelPrice <em>Base Fuel Price</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Base Fuel Price</em>'.
	 * @see com.mmxlabs.models.lng.actuals.CargoActuals#getBaseFuelPrice()
	 * @see #getCargoActuals()
	 * @generated
	 */
	EAttribute getCargoActuals_BaseFuelPrice();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.actuals.CargoActuals#getVolume <em>Volume</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Volume</em>'.
	 * @see com.mmxlabs.models.lng.actuals.CargoActuals#getVolume()
	 * @see #getCargoActuals()
	 * @generated
	 */
	EAttribute getCargoActuals_Volume();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.actuals.CargoActuals#getInsurancePremium <em>Insurance Premium</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Insurance Premium</em>'.
	 * @see com.mmxlabs.models.lng.actuals.CargoActuals#getInsurancePremium()
	 * @see #getCargoActuals()
	 * @generated
	 */
	EAttribute getCargoActuals_InsurancePremium();

	/**
	 * Returns the meta object for the attribute '{@link com.mmxlabs.models.lng.actuals.CargoActuals#getCrewBonus <em>Crew Bonus</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Crew Bonus</em>'.
	 * @see com.mmxlabs.models.lng.actuals.CargoActuals#getCrewBonus()
	 * @see #getCargoActuals()
	 * @generated
	 */
	EAttribute getCargoActuals_CrewBonus();

	/**
	 * Returns the meta object for the reference list '{@link com.mmxlabs.models.lng.actuals.CargoActuals#getActuals <em>Actuals</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Actuals</em>'.
	 * @see com.mmxlabs.models.lng.actuals.CargoActuals#getActuals()
	 * @see #getCargoActuals()
	 * @generated
	 */
	EReference getCargoActuals_Actuals();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.actuals.LoadActuals <em>Load Actuals</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Load Actuals</em>'.
	 * @see com.mmxlabs.models.lng.actuals.LoadActuals
	 * @generated
	 */
	EClass getLoadActuals();

	/**
	 * Returns the meta object for class '{@link com.mmxlabs.models.lng.actuals.DischargeActuals <em>Discharge Actuals</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Discharge Actuals</em>'.
	 * @see com.mmxlabs.models.lng.actuals.DischargeActuals
	 * @generated
	 */
	EClass getDischargeActuals();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	ActualsFactory getActualsFactory();

	/**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 *   <li>each class,</li>
	 *   <li>each feature of each class,</li>
	 *   <li>each operation of each class,</li>
	 *   <li>each enum,</li>
	 *   <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.actuals.impl.SlotActualsImpl <em>Slot Actuals</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.actuals.impl.SlotActualsImpl
		 * @see com.mmxlabs.models.lng.actuals.impl.ActualsPackageImpl#getSlotActuals()
		 * @generated
		 */
		EClass SLOT_ACTUALS = eINSTANCE.getSlotActuals();

		/**
		 * The meta object literal for the '<em><b>CV</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SLOT_ACTUALS__CV = eINSTANCE.getSlotActuals_CV();

		/**
		 * The meta object literal for the '<em><b>Volume</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SLOT_ACTUALS__VOLUME = eINSTANCE.getSlotActuals_Volume();

		/**
		 * The meta object literal for the '<em><b>Mm Btu</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SLOT_ACTUALS__MM_BTU = eINSTANCE.getSlotActuals_MmBtu();

		/**
		 * The meta object literal for the '<em><b>Port Charges</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SLOT_ACTUALS__PORT_CHARGES = eINSTANCE.getSlotActuals_PortCharges();

		/**
		 * The meta object literal for the '<em><b>Base Fuel Consumption</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SLOT_ACTUALS__BASE_FUEL_CONSUMPTION = eINSTANCE.getSlotActuals_BaseFuelConsumption();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.actuals.impl.CargoActualsImpl <em>Cargo Actuals</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.actuals.impl.CargoActualsImpl
		 * @see com.mmxlabs.models.lng.actuals.impl.ActualsPackageImpl#getCargoActuals()
		 * @generated
		 */
		EClass CARGO_ACTUALS = eINSTANCE.getCargoActuals();

		/**
		 * The meta object literal for the '<em><b>Base Fuel Price</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CARGO_ACTUALS__BASE_FUEL_PRICE = eINSTANCE.getCargoActuals_BaseFuelPrice();

		/**
		 * The meta object literal for the '<em><b>Volume</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CARGO_ACTUALS__VOLUME = eINSTANCE.getCargoActuals_Volume();

		/**
		 * The meta object literal for the '<em><b>Insurance Premium</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CARGO_ACTUALS__INSURANCE_PREMIUM = eINSTANCE.getCargoActuals_InsurancePremium();

		/**
		 * The meta object literal for the '<em><b>Crew Bonus</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CARGO_ACTUALS__CREW_BONUS = eINSTANCE.getCargoActuals_CrewBonus();

		/**
		 * The meta object literal for the '<em><b>Actuals</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CARGO_ACTUALS__ACTUALS = eINSTANCE.getCargoActuals_Actuals();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.actuals.impl.LoadActualsImpl <em>Load Actuals</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.actuals.impl.LoadActualsImpl
		 * @see com.mmxlabs.models.lng.actuals.impl.ActualsPackageImpl#getLoadActuals()
		 * @generated
		 */
		EClass LOAD_ACTUALS = eINSTANCE.getLoadActuals();

		/**
		 * The meta object literal for the '{@link com.mmxlabs.models.lng.actuals.impl.DischargeActualsImpl <em>Discharge Actuals</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see com.mmxlabs.models.lng.actuals.impl.DischargeActualsImpl
		 * @see com.mmxlabs.models.lng.actuals.impl.ActualsPackageImpl#getDischargeActuals()
		 * @generated
		 */
		EClass DISCHARGE_ACTUALS = eINSTANCE.getDischargeActuals();

	}

} //ActualsPackage
