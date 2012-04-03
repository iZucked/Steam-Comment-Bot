/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package com.mmxlabs.models.lng.analytics.impl;

import com.mmxlabs.models.lng.analytics.AnalyticsFactory;
import com.mmxlabs.models.lng.analytics.AnalyticsModel;
import com.mmxlabs.models.lng.analytics.AnalyticsPackage;
import com.mmxlabs.models.lng.analytics.CostComponent;
import com.mmxlabs.models.lng.analytics.FuelCost;
import com.mmxlabs.models.lng.analytics.UnitCostLine;
import com.mmxlabs.models.lng.analytics.UnitCostMatrix;

import com.mmxlabs.models.lng.analytics.Visit;
import com.mmxlabs.models.lng.analytics.Voyage;
import com.mmxlabs.models.lng.types.TypesPackage;

import com.mmxlabs.models.mmxcore.MMXCorePackage;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import org.eclipse.emf.ecore.impl.EPackageImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class AnalyticsPackageImpl extends EPackageImpl implements AnalyticsPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass analyticsModelEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass unitCostMatrixEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass unitCostLineEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass voyageEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass visitEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass costComponentEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass fuelCostEClass = null;

	/**
	 * Creates an instance of the model <b>Package</b>, registered with
	 * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package
	 * package URI value.
	 * <p>Note: the correct way to create the package is via the static
	 * factory method {@link #init init()}, which also performs
	 * initialization of the package, or returns the registered package,
	 * if one already exists.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.ecore.EPackage.Registry
	 * @see com.mmxlabs.models.lng.analytics.AnalyticsPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private AnalyticsPackageImpl() {
		super(eNS_URI, AnalyticsFactory.eINSTANCE);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static boolean isInited = false;

	/**
	 * Creates, registers, and initializes the <b>Package</b> for this model, and for any others upon which it depends.
	 * 
	 * <p>This method is used to initialize {@link AnalyticsPackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static AnalyticsPackage init() {
		if (isInited) return (AnalyticsPackage)EPackage.Registry.INSTANCE.getEPackage(AnalyticsPackage.eNS_URI);

		// Obtain or create and register package
		AnalyticsPackageImpl theAnalyticsPackage = (AnalyticsPackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof AnalyticsPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new AnalyticsPackageImpl());

		isInited = true;

		// Initialize simple dependencies
		TypesPackage.eINSTANCE.eClass();

		// Create package meta-data objects
		theAnalyticsPackage.createPackageContents();

		// Initialize created meta-data
		theAnalyticsPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theAnalyticsPackage.freeze();

  
		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(AnalyticsPackage.eNS_URI, theAnalyticsPackage);
		return theAnalyticsPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getAnalyticsModel() {
		return analyticsModelEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getAnalyticsModel_RoundTripMatrices() {
		return (EReference)analyticsModelEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getUnitCostMatrix() {
		return unitCostMatrixEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getUnitCostMatrix_Ports() {
		return (EReference)unitCostMatrixEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getUnitCostMatrix_Vessel() {
		return (EReference)unitCostMatrixEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getUnitCostMatrix_NotionalDayRate() {
		return (EAttribute)unitCostMatrixEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getUnitCostMatrix_Speed() {
		return (EAttribute)unitCostMatrixEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getUnitCostMatrix_RoundTrip() {
		return (EAttribute)unitCostMatrixEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getUnitCostMatrix_MinimumLoad() {
		return (EAttribute)unitCostMatrixEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getUnitCostMatrix_MaximumLoad() {
		return (EAttribute)unitCostMatrixEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getUnitCostMatrix_MinimumDischarge() {
		return (EAttribute)unitCostMatrixEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getUnitCostMatrix_MaximumDischarge() {
		return (EAttribute)unitCostMatrixEClass.getEStructuralFeatures().get(8);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getUnitCostMatrix_CargoPrice() {
		return (EAttribute)unitCostMatrixEClass.getEStructuralFeatures().get(9);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getUnitCostMatrix_BaseFuelPrice() {
		return (EAttribute)unitCostMatrixEClass.getEStructuralFeatures().get(10);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getUnitCostMatrix_CvValue() {
		return (EAttribute)unitCostMatrixEClass.getEStructuralFeatures().get(11);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getUnitCostMatrix_DischargeIdleTime() {
		return (EAttribute)unitCostMatrixEClass.getEStructuralFeatures().get(12);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getUnitCostMatrix_ReturnIdleTime() {
		return (EAttribute)unitCostMatrixEClass.getEStructuralFeatures().get(13);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getUnitCostMatrix_CostLines() {
		return (EReference)unitCostMatrixEClass.getEStructuralFeatures().get(14);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getUnitCostLine() {
		return unitCostLineEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getUnitCostLine_UnitCost() {
		return (EAttribute)unitCostLineEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getUnitCostLine_MmbtuDelivered() {
		return (EAttribute)unitCostLineEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getUnitCostLine_From() {
		return (EReference)unitCostLineEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getUnitCostLine_To() {
		return (EReference)unitCostLineEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getUnitCostLine_Duration() {
		return (EAttribute)unitCostLineEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getUnitCostLine_VolumeLoaded() {
		return (EAttribute)unitCostLineEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getUnitCostLine_VolumeDischarged() {
		return (EAttribute)unitCostLineEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getUnitCostLine_HireCost() {
		return (EAttribute)unitCostLineEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getUnitCostLine_FuelCost() {
		return (EAttribute)unitCostLineEClass.getEStructuralFeatures().get(8);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getUnitCostLine_CanalCost() {
		return (EAttribute)unitCostLineEClass.getEStructuralFeatures().get(9);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getUnitCostLine_CostComponents() {
		return (EReference)unitCostLineEClass.getEStructuralFeatures().get(10);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getUnitCostLine_PortCost() {
		return (EAttribute)unitCostLineEClass.getEStructuralFeatures().get(11);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getVoyage() {
		return voyageEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getVoyage_Route() {
		return (EAttribute)voyageEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getVoyage_RouteCost() {
		return (EAttribute)voyageEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getVoyage_Speed() {
		return (EAttribute)voyageEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getVoyage_Distance() {
		return (EAttribute)voyageEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getVoyage_IdleTime() {
		return (EAttribute)voyageEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getVoyage_TravelTime() {
		return (EAttribute)voyageEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getVisit() {
		return visitEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getVisit_PortCost() {
		return (EAttribute)visitEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getCostComponent() {
		return costComponentEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getCostComponent_Duration() {
		return (EAttribute)costComponentEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getCostComponent_HireCost() {
		return (EAttribute)costComponentEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getCostComponent_FuelCosts() {
		return (EReference)costComponentEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getFuelCost() {
		return fuelCostEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getFuelCost_Name() {
		return (EAttribute)fuelCostEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getFuelCost_Unit() {
		return (EAttribute)fuelCostEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getFuelCost_Quantity() {
		return (EAttribute)fuelCostEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getFuelCost_Cost() {
		return (EAttribute)fuelCostEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AnalyticsFactory getAnalyticsFactory() {
		return (AnalyticsFactory)getEFactoryInstance();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isCreated = false;

	/**
	 * Creates the meta-model objects for the package.  This method is
	 * guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void createPackageContents() {
		if (isCreated) return;
		isCreated = true;

		// Create classes and their features
		analyticsModelEClass = createEClass(ANALYTICS_MODEL);
		createEReference(analyticsModelEClass, ANALYTICS_MODEL__ROUND_TRIP_MATRICES);

		unitCostMatrixEClass = createEClass(UNIT_COST_MATRIX);
		createEReference(unitCostMatrixEClass, UNIT_COST_MATRIX__PORTS);
		createEReference(unitCostMatrixEClass, UNIT_COST_MATRIX__VESSEL);
		createEAttribute(unitCostMatrixEClass, UNIT_COST_MATRIX__NOTIONAL_DAY_RATE);
		createEAttribute(unitCostMatrixEClass, UNIT_COST_MATRIX__SPEED);
		createEAttribute(unitCostMatrixEClass, UNIT_COST_MATRIX__ROUND_TRIP);
		createEAttribute(unitCostMatrixEClass, UNIT_COST_MATRIX__MINIMUM_LOAD);
		createEAttribute(unitCostMatrixEClass, UNIT_COST_MATRIX__MAXIMUM_LOAD);
		createEAttribute(unitCostMatrixEClass, UNIT_COST_MATRIX__MINIMUM_DISCHARGE);
		createEAttribute(unitCostMatrixEClass, UNIT_COST_MATRIX__MAXIMUM_DISCHARGE);
		createEAttribute(unitCostMatrixEClass, UNIT_COST_MATRIX__CARGO_PRICE);
		createEAttribute(unitCostMatrixEClass, UNIT_COST_MATRIX__BASE_FUEL_PRICE);
		createEAttribute(unitCostMatrixEClass, UNIT_COST_MATRIX__CV_VALUE);
		createEAttribute(unitCostMatrixEClass, UNIT_COST_MATRIX__DISCHARGE_IDLE_TIME);
		createEAttribute(unitCostMatrixEClass, UNIT_COST_MATRIX__RETURN_IDLE_TIME);
		createEReference(unitCostMatrixEClass, UNIT_COST_MATRIX__COST_LINES);

		unitCostLineEClass = createEClass(UNIT_COST_LINE);
		createEAttribute(unitCostLineEClass, UNIT_COST_LINE__UNIT_COST);
		createEAttribute(unitCostLineEClass, UNIT_COST_LINE__MMBTU_DELIVERED);
		createEReference(unitCostLineEClass, UNIT_COST_LINE__FROM);
		createEReference(unitCostLineEClass, UNIT_COST_LINE__TO);
		createEAttribute(unitCostLineEClass, UNIT_COST_LINE__DURATION);
		createEAttribute(unitCostLineEClass, UNIT_COST_LINE__VOLUME_LOADED);
		createEAttribute(unitCostLineEClass, UNIT_COST_LINE__VOLUME_DISCHARGED);
		createEAttribute(unitCostLineEClass, UNIT_COST_LINE__HIRE_COST);
		createEAttribute(unitCostLineEClass, UNIT_COST_LINE__FUEL_COST);
		createEAttribute(unitCostLineEClass, UNIT_COST_LINE__CANAL_COST);
		createEReference(unitCostLineEClass, UNIT_COST_LINE__COST_COMPONENTS);
		createEAttribute(unitCostLineEClass, UNIT_COST_LINE__PORT_COST);

		voyageEClass = createEClass(VOYAGE);
		createEAttribute(voyageEClass, VOYAGE__ROUTE);
		createEAttribute(voyageEClass, VOYAGE__ROUTE_COST);
		createEAttribute(voyageEClass, VOYAGE__SPEED);
		createEAttribute(voyageEClass, VOYAGE__DISTANCE);
		createEAttribute(voyageEClass, VOYAGE__IDLE_TIME);
		createEAttribute(voyageEClass, VOYAGE__TRAVEL_TIME);

		visitEClass = createEClass(VISIT);
		createEAttribute(visitEClass, VISIT__PORT_COST);

		costComponentEClass = createEClass(COST_COMPONENT);
		createEAttribute(costComponentEClass, COST_COMPONENT__DURATION);
		createEAttribute(costComponentEClass, COST_COMPONENT__HIRE_COST);
		createEReference(costComponentEClass, COST_COMPONENT__FUEL_COSTS);

		fuelCostEClass = createEClass(FUEL_COST);
		createEAttribute(fuelCostEClass, FUEL_COST__NAME);
		createEAttribute(fuelCostEClass, FUEL_COST__UNIT);
		createEAttribute(fuelCostEClass, FUEL_COST__QUANTITY);
		createEAttribute(fuelCostEClass, FUEL_COST__COST);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isInitialized = false;

	/**
	 * Complete the initialization of the package and its meta-model.  This
	 * method is guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void initializePackageContents() {
		if (isInitialized) return;
		isInitialized = true;

		// Initialize package
		setName(eNAME);
		setNsPrefix(eNS_PREFIX);
		setNsURI(eNS_URI);

		// Obtain other dependent packages
		MMXCorePackage theMMXCorePackage = (MMXCorePackage)EPackage.Registry.INSTANCE.getEPackage(MMXCorePackage.eNS_URI);
		TypesPackage theTypesPackage = (TypesPackage)EPackage.Registry.INSTANCE.getEPackage(TypesPackage.eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes
		analyticsModelEClass.getESuperTypes().add(theMMXCorePackage.getUUIDObject());
		analyticsModelEClass.getESuperTypes().add(theMMXCorePackage.getNamedObject());
		unitCostMatrixEClass.getESuperTypes().add(theMMXCorePackage.getUUIDObject());
		unitCostMatrixEClass.getESuperTypes().add(theMMXCorePackage.getNamedObject());
		unitCostLineEClass.getESuperTypes().add(theMMXCorePackage.getMMXObject());
		voyageEClass.getESuperTypes().add(this.getCostComponent());
		visitEClass.getESuperTypes().add(this.getCostComponent());

		// Initialize classes and features; add operations and parameters
		initEClass(analyticsModelEClass, AnalyticsModel.class, "AnalyticsModel", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getAnalyticsModel_RoundTripMatrices(), this.getUnitCostMatrix(), null, "roundTripMatrices", null, 0, -1, AnalyticsModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(unitCostMatrixEClass, UnitCostMatrix.class, "UnitCostMatrix", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getUnitCostMatrix_Ports(), theTypesPackage.getAPortSet(), null, "ports", null, 0, -1, UnitCostMatrix.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getUnitCostMatrix_Vessel(), theTypesPackage.getAVessel(), null, "vessel", null, 1, 1, UnitCostMatrix.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getUnitCostMatrix_NotionalDayRate(), ecorePackage.getEInt(), "notionalDayRate", null, 1, 1, UnitCostMatrix.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getUnitCostMatrix_Speed(), ecorePackage.getEDouble(), "speed", null, 1, 1, UnitCostMatrix.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getUnitCostMatrix_RoundTrip(), ecorePackage.getEBoolean(), "roundTrip", "true", 1, 1, UnitCostMatrix.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getUnitCostMatrix_MinimumLoad(), ecorePackage.getEInt(), "minimumLoad", null, 1, 1, UnitCostMatrix.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getUnitCostMatrix_MaximumLoad(), ecorePackage.getEInt(), "maximumLoad", null, 1, 1, UnitCostMatrix.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getUnitCostMatrix_MinimumDischarge(), ecorePackage.getEInt(), "minimumDischarge", null, 1, 1, UnitCostMatrix.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getUnitCostMatrix_MaximumDischarge(), ecorePackage.getEInt(), "maximumDischarge", null, 1, 1, UnitCostMatrix.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getUnitCostMatrix_CargoPrice(), ecorePackage.getEDouble(), "cargoPrice", null, 1, 1, UnitCostMatrix.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getUnitCostMatrix_BaseFuelPrice(), ecorePackage.getEDouble(), "baseFuelPrice", null, 1, 1, UnitCostMatrix.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getUnitCostMatrix_CvValue(), ecorePackage.getEDouble(), "cvValue", null, 1, 1, UnitCostMatrix.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getUnitCostMatrix_DischargeIdleTime(), ecorePackage.getEInt(), "dischargeIdleTime", null, 1, 1, UnitCostMatrix.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getUnitCostMatrix_ReturnIdleTime(), ecorePackage.getEInt(), "returnIdleTime", null, 1, 1, UnitCostMatrix.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getUnitCostMatrix_CostLines(), this.getUnitCostLine(), null, "costLines", null, 0, -1, UnitCostMatrix.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(unitCostLineEClass, UnitCostLine.class, "UnitCostLine", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getUnitCostLine_UnitCost(), ecorePackage.getEDouble(), "unitCost", null, 1, 1, UnitCostLine.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getUnitCostLine_MmbtuDelivered(), ecorePackage.getEInt(), "mmbtuDelivered", null, 1, 1, UnitCostLine.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getUnitCostLine_From(), theTypesPackage.getAPort(), null, "from", null, 1, 1, UnitCostLine.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getUnitCostLine_To(), theTypesPackage.getAPort(), null, "to", null, 1, 1, UnitCostLine.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getUnitCostLine_Duration(), ecorePackage.getEInt(), "duration", null, 1, 1, UnitCostLine.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getUnitCostLine_VolumeLoaded(), ecorePackage.getEInt(), "volumeLoaded", null, 1, 1, UnitCostLine.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getUnitCostLine_VolumeDischarged(), ecorePackage.getEInt(), "volumeDischarged", null, 1, 1, UnitCostLine.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getUnitCostLine_HireCost(), ecorePackage.getEInt(), "hireCost", null, 1, 1, UnitCostLine.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getUnitCostLine_FuelCost(), ecorePackage.getEInt(), "fuelCost", null, 1, 1, UnitCostLine.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getUnitCostLine_CanalCost(), ecorePackage.getEInt(), "canalCost", null, 1, 1, UnitCostLine.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getUnitCostLine_CostComponents(), this.getCostComponent(), null, "costComponents", null, 0, -1, UnitCostLine.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getUnitCostLine_PortCost(), ecorePackage.getEInt(), "portCost", null, 1, 1, UnitCostLine.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		addEOperation(unitCostLineEClass, ecorePackage.getEInt(), "getTotalCost", 1, 1, IS_UNIQUE, IS_ORDERED);

		initEClass(voyageEClass, Voyage.class, "Voyage", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getVoyage_Route(), ecorePackage.getEString(), "route", null, 1, 1, Voyage.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getVoyage_RouteCost(), ecorePackage.getEInt(), "routeCost", null, 1, 1, Voyage.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getVoyage_Speed(), ecorePackage.getEDouble(), "speed", null, 1, 1, Voyage.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getVoyage_Distance(), ecorePackage.getEInt(), "distance", null, 1, 1, Voyage.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getVoyage_IdleTime(), ecorePackage.getEInt(), "idleTime", null, 1, 1, Voyage.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getVoyage_TravelTime(), ecorePackage.getEInt(), "travelTime", null, 1, 1, Voyage.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(visitEClass, Visit.class, "Visit", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getVisit_PortCost(), ecorePackage.getEInt(), "portCost", null, 1, 1, Visit.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(costComponentEClass, CostComponent.class, "CostComponent", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getCostComponent_Duration(), ecorePackage.getEInt(), "duration", null, 1, 1, CostComponent.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getCostComponent_HireCost(), ecorePackage.getEInt(), "hireCost", null, 1, 1, CostComponent.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getCostComponent_FuelCosts(), this.getFuelCost(), null, "fuelCosts", null, 0, -1, CostComponent.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		addEOperation(costComponentEClass, ecorePackage.getEInt(), "getFuelCost", 1, 1, IS_UNIQUE, IS_ORDERED);

		initEClass(fuelCostEClass, FuelCost.class, "FuelCost", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getFuelCost_Name(), ecorePackage.getEString(), "name", null, 1, 1, FuelCost.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getFuelCost_Unit(), ecorePackage.getEString(), "unit", null, 1, 1, FuelCost.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getFuelCost_Quantity(), ecorePackage.getEInt(), "quantity", null, 1, 1, FuelCost.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getFuelCost_Cost(), ecorePackage.getEInt(), "cost", null, 1, 1, FuelCost.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		// Create resource
		createResource(eNS_URI);

		// Create annotations
		// http://www.mmxlabs.com/models/ui/numberFormat
		createNumberFormatAnnotations();
	}

	/**
	 * Initializes the annotations for <b>http://www.mmxlabs.com/models/ui/numberFormat</b>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void createNumberFormatAnnotations() {
		String source = "http://www.mmxlabs.com/models/ui/numberFormat";		
		addAnnotation
		  (getUnitCostMatrix_NotionalDayRate(), 
		   source, 
		   new String[] {
			 "unit", "$/day"
		   });		
		addAnnotation
		  (getUnitCostMatrix_Speed(), 
		   source, 
		   new String[] {
			 "unit", "kts"
		   });		
		addAnnotation
		  (getUnitCostMatrix_MinimumLoad(), 
		   source, 
		   new String[] {
			 "unit", "M3"
		   });		
		addAnnotation
		  (getUnitCostMatrix_MaximumLoad(), 
		   source, 
		   new String[] {
			 "unit", "M3"
		   });		
		addAnnotation
		  (getUnitCostMatrix_MinimumDischarge(), 
		   source, 
		   new String[] {
			 "unit", "M3"
		   });		
		addAnnotation
		  (getUnitCostMatrix_MaximumDischarge(), 
		   source, 
		   new String[] {
			 "unit", "M3"
		   });		
		addAnnotation
		  (getUnitCostMatrix_CargoPrice(), 
		   source, 
		   new String[] {
			 "unit", "$/mmbtu"
		   });		
		addAnnotation
		  (getUnitCostMatrix_BaseFuelPrice(), 
		   source, 
		   new String[] {
			 "unit", "$/MT"
		   });		
		addAnnotation
		  (getUnitCostMatrix_DischargeIdleTime(), 
		   source, 
		   new String[] {
			 "unit", "hrs"
		   });		
		addAnnotation
		  (getUnitCostMatrix_ReturnIdleTime(), 
		   source, 
		   new String[] {
			 "unit", "hrs"
		   });		
	}

} //AnalyticsPackageImpl
