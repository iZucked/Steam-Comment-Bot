/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2019
 * All rights reserved.
 */
package com.mmxlabs.models.lng.port.impl;

import com.mmxlabs.models.datetime.DateTimePackage;
import com.mmxlabs.models.lng.port.CanalEntry;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EGenericType;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.impl.EPackageImpl;

import com.mmxlabs.models.lng.port.CapabilityGroup;
import com.mmxlabs.models.lng.port.ContingencyMatrix;
import com.mmxlabs.models.lng.port.ContingencyMatrixEntry;
import com.mmxlabs.models.lng.port.EntryPoint;
import com.mmxlabs.models.lng.port.Location;
import com.mmxlabs.models.lng.port.OtherIdentifiers;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortCountryGroup;
import com.mmxlabs.models.lng.port.PortFactory;
import com.mmxlabs.models.lng.port.PortGroup;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.port.PortPackage;
import com.mmxlabs.models.lng.port.Route;
import com.mmxlabs.models.lng.port.RouteLine;
import com.mmxlabs.models.lng.port.RouteOption;
import com.mmxlabs.models.lng.types.TypesPackage;
import com.mmxlabs.models.mmxcore.MMXCorePackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class PortPackageImpl extends EPackageImpl implements PortPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass portEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass routeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass portGroupEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass routeLineEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass portModelEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass capabilityGroupEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass locationEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass portCountryGroupEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass entryPointEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass contingencyMatrixEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass contingencyMatrixEntryEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass otherIdentifiersEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum routeOptionEEnum = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum canalEntryEEnum = null;

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
	 * @see com.mmxlabs.models.lng.port.PortPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private PortPackageImpl() {
		super(eNS_URI, PortFactory.eINSTANCE);
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
	 * <p>This method is used to initialize {@link PortPackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static PortPackage init() {
		if (isInited) return (PortPackage)EPackage.Registry.INSTANCE.getEPackage(PortPackage.eNS_URI);

		// Obtain or create and register package
		Object registeredPortPackage = EPackage.Registry.INSTANCE.get(eNS_URI);
		PortPackageImpl thePortPackage = registeredPortPackage instanceof PortPackageImpl ? (PortPackageImpl)registeredPortPackage : new PortPackageImpl();

		isInited = true;

		// Initialize simple dependencies
		DateTimePackage.eINSTANCE.eClass();
		TypesPackage.eINSTANCE.eClass();
		MMXCorePackage.eINSTANCE.eClass();

		// Create package meta-data objects
		thePortPackage.createPackageContents();

		// Initialize created meta-data
		thePortPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		thePortPackage.freeze();

		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(PortPackage.eNS_URI, thePortPackage);
		return thePortPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getPort() {
		return portEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getPort_Capabilities() {
		return (EAttribute)portEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getPort_LoadDuration() {
		return (EAttribute)portEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getPort_DischargeDuration() {
		return (EAttribute)portEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getPort_Berths() {
		return (EAttribute)portEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getPort_CvValue() {
		return (EAttribute)portEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getPort_DefaultStartTime() {
		return (EAttribute)portEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getPort_AllowCooldown() {
		return (EAttribute)portEClass.getEStructuralFeatures().get(8);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getPort_DefaultWindowSize() {
		return (EAttribute)portEClass.getEStructuralFeatures().get(9);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getPort_DefaultWindowSizeUnits() {
		return (EAttribute)portEClass.getEStructuralFeatures().get(10);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getPort_Location() {
		return (EReference)portEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getPort_MinCvValue() {
		return (EAttribute)portEClass.getEStructuralFeatures().get(11);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getPort_MaxCvValue() {
		return (EAttribute)portEClass.getEStructuralFeatures().get(12);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EOperation getPort__GetZoneId() {
		return portEClass.getEOperations().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EOperation getPort__MmxID() {
		return portEClass.getEOperations().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EOperation getPort__GetTempMMXID() {
		return portEClass.getEOperations().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getPort_ShortName() {
		return (EAttribute)portEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getRoute() {
		return routeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getRoute_Lines() {
		return (EReference)routeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getRoute_RouteOption() {
		return (EAttribute)routeEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getRoute_VirtualPort() {
		return (EReference)routeEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getRoute_NorthEntrance() {
		return (EReference)routeEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getRoute_SouthEntrance() {
		return (EReference)routeEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getRoute_Distance() {
		return (EAttribute)routeEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getPortGroup() {
		return portGroupEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getPortGroup_Contents() {
		return (EReference)portGroupEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getRouteLine() {
		return routeLineEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getRouteLine_From() {
		return (EReference)routeLineEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getRouteLine_To() {
		return (EReference)routeLineEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getRouteLine_Distance() {
		return (EAttribute)routeLineEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getRouteLine_Provider() {
		return (EAttribute)routeLineEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getRouteLine_ErrorCode() {
		return (EAttribute)routeLineEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getPortModel() {
		return portModelEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getPortModel_Ports() {
		return (EReference)portModelEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getPortModel_PortGroups() {
		return (EReference)portModelEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getPortModel_Routes() {
		return (EReference)portModelEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getPortModel_SpecialPortGroups() {
		return (EReference)portModelEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getPortModel_PortCountryGroups() {
		return (EReference)portModelEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getPortModel_ContingencyMatrix() {
		return (EReference)portModelEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getPortModel_PortVersionRecord() {
		return (EReference)portModelEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getPortModel_PortGroupVersionRecord() {
		return (EReference)portModelEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getPortModel_DistanceVersionRecord() {
		return (EReference)portModelEClass.getEStructuralFeatures().get(8);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getCapabilityGroup() {
		return capabilityGroupEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getCapabilityGroup_Capability() {
		return (EAttribute)capabilityGroupEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getLocation() {
		return locationEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getLocation_MmxId() {
		return (EAttribute)locationEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getLocation_TimeZone() {
		return (EAttribute)locationEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getLocation_Country() {
		return (EAttribute)locationEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getLocation_Lat() {
		return (EAttribute)locationEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getLocation_Lon() {
		return (EAttribute)locationEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getLocation_OtherIdentifiers() {
		return (EReference)locationEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EOperation getLocation__GetZoneId() {
		return locationEClass.getEOperations().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EOperation getLocation__GetTempMMXID() {
		return locationEClass.getEOperations().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getPortCountryGroup() {
		return portCountryGroupEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getEntryPoint() {
		return entryPointEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getEntryPoint_Port() {
		return (EReference)entryPointEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getContingencyMatrix() {
		return contingencyMatrixEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getContingencyMatrix_Entries() {
		return (EReference)contingencyMatrixEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getContingencyMatrix_DefaultDuration() {
		return (EAttribute)contingencyMatrixEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getContingencyMatrixEntry() {
		return contingencyMatrixEntryEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getContingencyMatrixEntry_FromPort() {
		return (EReference)contingencyMatrixEntryEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getContingencyMatrixEntry_ToPort() {
		return (EReference)contingencyMatrixEntryEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getContingencyMatrixEntry_Duration() {
		return (EAttribute)contingencyMatrixEntryEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getOtherIdentifiers() {
		return otherIdentifiersEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getOtherIdentifiers_Identifier() {
		return (EAttribute)otherIdentifiersEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getOtherIdentifiers_Provider() {
		return (EAttribute)otherIdentifiersEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EEnum getRouteOption() {
		return routeOptionEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EEnum getCanalEntry() {
		return canalEntryEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public PortFactory getPortFactory() {
		return (PortFactory)getEFactoryInstance();
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
		portEClass = createEClass(PORT);
		createEAttribute(portEClass, PORT__SHORT_NAME);
		createEReference(portEClass, PORT__LOCATION);
		createEAttribute(portEClass, PORT__CAPABILITIES);
		createEAttribute(portEClass, PORT__LOAD_DURATION);
		createEAttribute(portEClass, PORT__DISCHARGE_DURATION);
		createEAttribute(portEClass, PORT__BERTHS);
		createEAttribute(portEClass, PORT__CV_VALUE);
		createEAttribute(portEClass, PORT__DEFAULT_START_TIME);
		createEAttribute(portEClass, PORT__ALLOW_COOLDOWN);
		createEAttribute(portEClass, PORT__DEFAULT_WINDOW_SIZE);
		createEAttribute(portEClass, PORT__DEFAULT_WINDOW_SIZE_UNITS);
		createEAttribute(portEClass, PORT__MIN_CV_VALUE);
		createEAttribute(portEClass, PORT__MAX_CV_VALUE);
		createEOperation(portEClass, PORT___GET_ZONE_ID);
		createEOperation(portEClass, PORT___MMX_ID);
		createEOperation(portEClass, PORT___GET_TEMP_MMXID);

		routeEClass = createEClass(ROUTE);
		createEReference(routeEClass, ROUTE__LINES);
		createEAttribute(routeEClass, ROUTE__ROUTE_OPTION);
		createEReference(routeEClass, ROUTE__VIRTUAL_PORT);
		createEReference(routeEClass, ROUTE__NORTH_ENTRANCE);
		createEReference(routeEClass, ROUTE__SOUTH_ENTRANCE);
		createEAttribute(routeEClass, ROUTE__DISTANCE);

		portGroupEClass = createEClass(PORT_GROUP);
		createEReference(portGroupEClass, PORT_GROUP__CONTENTS);

		routeLineEClass = createEClass(ROUTE_LINE);
		createEReference(routeLineEClass, ROUTE_LINE__FROM);
		createEReference(routeLineEClass, ROUTE_LINE__TO);
		createEAttribute(routeLineEClass, ROUTE_LINE__DISTANCE);
		createEAttribute(routeLineEClass, ROUTE_LINE__PROVIDER);
		createEAttribute(routeLineEClass, ROUTE_LINE__ERROR_CODE);

		portModelEClass = createEClass(PORT_MODEL);
		createEReference(portModelEClass, PORT_MODEL__PORTS);
		createEReference(portModelEClass, PORT_MODEL__PORT_GROUPS);
		createEReference(portModelEClass, PORT_MODEL__ROUTES);
		createEReference(portModelEClass, PORT_MODEL__SPECIAL_PORT_GROUPS);
		createEReference(portModelEClass, PORT_MODEL__PORT_COUNTRY_GROUPS);
		createEReference(portModelEClass, PORT_MODEL__CONTINGENCY_MATRIX);
		createEReference(portModelEClass, PORT_MODEL__PORT_VERSION_RECORD);
		createEReference(portModelEClass, PORT_MODEL__PORT_GROUP_VERSION_RECORD);
		createEReference(portModelEClass, PORT_MODEL__DISTANCE_VERSION_RECORD);

		capabilityGroupEClass = createEClass(CAPABILITY_GROUP);
		createEAttribute(capabilityGroupEClass, CAPABILITY_GROUP__CAPABILITY);

		locationEClass = createEClass(LOCATION);
		createEAttribute(locationEClass, LOCATION__MMX_ID);
		createEAttribute(locationEClass, LOCATION__TIME_ZONE);
		createEAttribute(locationEClass, LOCATION__COUNTRY);
		createEAttribute(locationEClass, LOCATION__LAT);
		createEAttribute(locationEClass, LOCATION__LON);
		createEReference(locationEClass, LOCATION__OTHER_IDENTIFIERS);
		createEOperation(locationEClass, LOCATION___GET_ZONE_ID);
		createEOperation(locationEClass, LOCATION___GET_TEMP_MMXID);

		portCountryGroupEClass = createEClass(PORT_COUNTRY_GROUP);

		entryPointEClass = createEClass(ENTRY_POINT);
		createEReference(entryPointEClass, ENTRY_POINT__PORT);

		contingencyMatrixEClass = createEClass(CONTINGENCY_MATRIX);
		createEReference(contingencyMatrixEClass, CONTINGENCY_MATRIX__ENTRIES);
		createEAttribute(contingencyMatrixEClass, CONTINGENCY_MATRIX__DEFAULT_DURATION);

		contingencyMatrixEntryEClass = createEClass(CONTINGENCY_MATRIX_ENTRY);
		createEReference(contingencyMatrixEntryEClass, CONTINGENCY_MATRIX_ENTRY__FROM_PORT);
		createEReference(contingencyMatrixEntryEClass, CONTINGENCY_MATRIX_ENTRY__TO_PORT);
		createEAttribute(contingencyMatrixEntryEClass, CONTINGENCY_MATRIX_ENTRY__DURATION);

		otherIdentifiersEClass = createEClass(OTHER_IDENTIFIERS);
		createEAttribute(otherIdentifiersEClass, OTHER_IDENTIFIERS__IDENTIFIER);
		createEAttribute(otherIdentifiersEClass, OTHER_IDENTIFIERS__PROVIDER);

		// Create enums
		routeOptionEEnum = createEEnum(ROUTE_OPTION);
		canalEntryEEnum = createEEnum(CANAL_ENTRY);
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
		TypesPackage theTypesPackage = (TypesPackage)EPackage.Registry.INSTANCE.getEPackage(TypesPackage.eNS_URI);
		DateTimePackage theDateTimePackage = (DateTimePackage)EPackage.Registry.INSTANCE.getEPackage(DateTimePackage.eNS_URI);
		MMXCorePackage theMMXCorePackage = (MMXCorePackage)EPackage.Registry.INSTANCE.getEPackage(MMXCorePackage.eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes
		EGenericType g1 = createEGenericType(theTypesPackage.getAPortSet());
		EGenericType g2 = createEGenericType(this.getPort());
		g1.getETypeArguments().add(g2);
		portEClass.getEGenericSuperTypes().add(g1);
		routeEClass.getESuperTypes().add(theMMXCorePackage.getNamedObject());
		routeEClass.getESuperTypes().add(theMMXCorePackage.getUUIDObject());
		g1 = createEGenericType(theTypesPackage.getAPortSet());
		g2 = createEGenericType(this.getPort());
		g1.getETypeArguments().add(g2);
		portGroupEClass.getEGenericSuperTypes().add(g1);
		routeLineEClass.getESuperTypes().add(theMMXCorePackage.getMMXObject());
		portModelEClass.getESuperTypes().add(theMMXCorePackage.getUUIDObject());
		g1 = createEGenericType(theTypesPackage.getAPortSet());
		g2 = createEGenericType(this.getPort());
		g1.getETypeArguments().add(g2);
		capabilityGroupEClass.getEGenericSuperTypes().add(g1);
		locationEClass.getESuperTypes().add(theMMXCorePackage.getNamedObject());
		locationEClass.getESuperTypes().add(theMMXCorePackage.getOtherNamesObject());
		g1 = createEGenericType(theTypesPackage.getAPortSet());
		g2 = createEGenericType(this.getPort());
		g1.getETypeArguments().add(g2);
		portCountryGroupEClass.getEGenericSuperTypes().add(g1);
		g1 = createEGenericType(theMMXCorePackage.getNamedObject());
		portCountryGroupEClass.getEGenericSuperTypes().add(g1);
		entryPointEClass.getESuperTypes().add(theMMXCorePackage.getNamedObject());

		// Initialize classes, features, and operations; add parameters
		initEClass(portEClass, Port.class, "Port", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getPort_ShortName(), ecorePackage.getEString(), "shortName", null, 0, 1, Port.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getPort_Location(), this.getLocation(), null, "location", null, 1, 1, Port.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getPort_Capabilities(), theTypesPackage.getPortCapability(), "capabilities", null, 0, -1, Port.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getPort_LoadDuration(), ecorePackage.getEInt(), "loadDuration", null, 1, 1, Port.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getPort_DischargeDuration(), ecorePackage.getEInt(), "dischargeDuration", null, 1, 1, Port.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getPort_Berths(), ecorePackage.getEInt(), "berths", "1", 0, 1, Port.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getPort_CvValue(), ecorePackage.getEDouble(), "cvValue", null, 1, 1, Port.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getPort_DefaultStartTime(), ecorePackage.getEInt(), "defaultStartTime", null, 1, 1, Port.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getPort_AllowCooldown(), ecorePackage.getEBoolean(), "allowCooldown", null, 1, 1, Port.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getPort_DefaultWindowSize(), ecorePackage.getEInt(), "defaultWindowSize", null, 1, 1, Port.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getPort_DefaultWindowSizeUnits(), theTypesPackage.getTimePeriod(), "defaultWindowSizeUnits", "HOURS", 1, 1, Port.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getPort_MinCvValue(), ecorePackage.getEDouble(), "minCvValue", null, 0, 1, Port.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getPort_MaxCvValue(), ecorePackage.getEDouble(), "maxCvValue", null, 0, 1, Port.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEOperation(getPort__GetZoneId(), theDateTimePackage.getZoneId(), "getZoneId", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEOperation(getPort__MmxID(), ecorePackage.getEString(), "mmxID", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEOperation(getPort__GetTempMMXID(), ecorePackage.getEString(), "getTempMMXID", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEClass(routeEClass, Route.class, "Route", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getRoute_Lines(), this.getRouteLine(), null, "lines", null, 0, -1, Route.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getRoute_RouteOption(), this.getRouteOption(), "routeOption", null, 0, 1, Route.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getRoute_VirtualPort(), this.getPort(), null, "virtualPort", null, 0, 1, Route.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getRoute_NorthEntrance(), this.getEntryPoint(), null, "northEntrance", null, 0, 1, Route.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getRoute_SouthEntrance(), this.getEntryPoint(), null, "southEntrance", null, 0, 1, Route.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getRoute_Distance(), ecorePackage.getEDouble(), "distance", null, 1, 1, Route.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(portGroupEClass, PortGroup.class, "PortGroup", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		g1 = createEGenericType(theTypesPackage.getAPortSet());
		g2 = createEGenericType(this.getPort());
		g1.getETypeArguments().add(g2);
		initEReference(getPortGroup_Contents(), g1, null, "contents", null, 0, -1, PortGroup.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(routeLineEClass, RouteLine.class, "RouteLine", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getRouteLine_From(), this.getPort(), null, "from", null, 1, 1, RouteLine.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getRouteLine_To(), this.getPort(), null, "to", null, 1, 1, RouteLine.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getRouteLine_Distance(), ecorePackage.getEDouble(), "distance", null, 1, 1, RouteLine.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getRouteLine_Provider(), ecorePackage.getEString(), "provider", null, 0, 1, RouteLine.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getRouteLine_ErrorCode(), ecorePackage.getEString(), "errorCode", null, 0, 1, RouteLine.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(portModelEClass, PortModel.class, "PortModel", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getPortModel_Ports(), this.getPort(), null, "ports", null, 0, -1, PortModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getPortModel_PortGroups(), this.getPortGroup(), null, "portGroups", null, 0, -1, PortModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getPortModel_Routes(), this.getRoute(), null, "routes", null, 0, -1, PortModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getPortModel_SpecialPortGroups(), this.getCapabilityGroup(), null, "specialPortGroups", null, 0, -1, PortModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getPortModel_PortCountryGroups(), this.getPortCountryGroup(), null, "portCountryGroups", null, 0, -1, PortModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getPortModel_ContingencyMatrix(), this.getContingencyMatrix(), null, "contingencyMatrix", null, 0, 1, PortModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getPortModel_PortVersionRecord(), theMMXCorePackage.getVersionRecord(), null, "portVersionRecord", null, 0, 1, PortModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getPortModel_PortGroupVersionRecord(), theMMXCorePackage.getVersionRecord(), null, "portGroupVersionRecord", null, 0, 1, PortModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getPortModel_DistanceVersionRecord(), theMMXCorePackage.getVersionRecord(), null, "distanceVersionRecord", null, 0, 1, PortModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(capabilityGroupEClass, CapabilityGroup.class, "CapabilityGroup", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getCapabilityGroup_Capability(), theTypesPackage.getPortCapability(), "capability", null, 1, 1, CapabilityGroup.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(locationEClass, Location.class, "Location", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getLocation_MmxId(), ecorePackage.getEString(), "mmxId", null, 0, 1, Location.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getLocation_TimeZone(), ecorePackage.getEString(), "timeZone", null, 1, 1, Location.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getLocation_Country(), ecorePackage.getEString(), "country", null, 0, 1, Location.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getLocation_Lat(), ecorePackage.getEDouble(), "lat", null, 0, 1, Location.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getLocation_Lon(), ecorePackage.getEDouble(), "lon", null, 0, 1, Location.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getLocation_OtherIdentifiers(), this.getOtherIdentifiers(), null, "otherIdentifiers", null, 0, -1, Location.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEOperation(getLocation__GetZoneId(), theDateTimePackage.getZoneId(), "getZoneId", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEOperation(getLocation__GetTempMMXID(), ecorePackage.getEString(), "getTempMMXID", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEClass(portCountryGroupEClass, PortCountryGroup.class, "PortCountryGroup", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(entryPointEClass, EntryPoint.class, "EntryPoint", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getEntryPoint_Port(), this.getPort(), null, "port", null, 0, 1, EntryPoint.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(contingencyMatrixEClass, ContingencyMatrix.class, "ContingencyMatrix", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getContingencyMatrix_Entries(), this.getContingencyMatrixEntry(), null, "entries", null, 0, -1, ContingencyMatrix.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getContingencyMatrix_DefaultDuration(), ecorePackage.getEInt(), "defaultDuration", null, 0, 1, ContingencyMatrix.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(contingencyMatrixEntryEClass, ContingencyMatrixEntry.class, "ContingencyMatrixEntry", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getContingencyMatrixEntry_FromPort(), this.getPort(), null, "fromPort", null, 0, 1, ContingencyMatrixEntry.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getContingencyMatrixEntry_ToPort(), this.getPort(), null, "toPort", null, 0, 1, ContingencyMatrixEntry.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getContingencyMatrixEntry_Duration(), ecorePackage.getEInt(), "duration", null, 0, 1, ContingencyMatrixEntry.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(otherIdentifiersEClass, OtherIdentifiers.class, "OtherIdentifiers", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getOtherIdentifiers_Identifier(), ecorePackage.getEString(), "identifier", null, 0, 1, OtherIdentifiers.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getOtherIdentifiers_Provider(), ecorePackage.getEString(), "provider", null, 0, 1, OtherIdentifiers.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		// Initialize enums and add enum literals
		initEEnum(routeOptionEEnum, RouteOption.class, "RouteOption");
		addEEnumLiteral(routeOptionEEnum, RouteOption.DIRECT);
		addEEnumLiteral(routeOptionEEnum, RouteOption.SUEZ);
		addEEnumLiteral(routeOptionEEnum, RouteOption.PANAMA);

		initEEnum(canalEntryEEnum, CanalEntry.class, "CanalEntry");
		addEEnumLiteral(canalEntryEEnum, CanalEntry.NORTHSIDE);
		addEEnumLiteral(canalEntryEEnum, CanalEntry.SOUTHSIDE);

		// Create resource
		createResource(eNS_URI);

		// Create annotations
		// http://www.mmxlabs.com/models/ui/numberFormat
		createNumberFormatAnnotations();
		// http://www.mmxlabs.com/models/csv
		createCsvAnnotations();
		// http://www.mmxlabs.com/models/mmxcore/annotations/namedobject
		createNamedobjectAnnotations();
	}

	/**
	 * Initializes the annotations for <b>http://www.mmxlabs.com/models/mmxcore/annotations/namedobject</b>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void createNamedobjectAnnotations() {
		String source = "http://www.mmxlabs.com/models/mmxcore/annotations/namedobject";
		addAnnotation
		  (locationEClass,
		   source,
		   new String[] {
			   "showOtherNames", "true"
		   });
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
		  (getPort_LoadDuration(),
		   source,
		   new String[] {
			   "unit", "hours",
			   "formatString", "##,##0"
		   });
		addAnnotation
		  (getPort_DischargeDuration(),
		   source,
		   new String[] {
			   "unit", "hours",
			   "formatString", "##,##0"
		   });
		addAnnotation
		  (getPort_CvValue(),
		   source,
		   new String[] {
			   "unit", "mmBtu/m\u00b3",
			   "formatString", "#0.###"
		   });
		addAnnotation
		  (getPort_DefaultWindowSize(),
		   source,
		   new String[] {
			   "formatString", "##,##0"
		   });
		addAnnotation
		  (getPort_DefaultWindowSizeUnits(),
		   source,
		   new String[] {
			   "unit", "hours",
			   "formatString", "##,##0"
		   });
		addAnnotation
		  (getPort_MinCvValue(),
		   source,
		   new String[] {
			   "formatString", "#0.###"
		   });
		addAnnotation
		  (getPort_MaxCvValue(),
		   source,
		   new String[] {
			   "formatString", "#0.###"
		   });
		addAnnotation
		  (getLocation_Lat(),
		   source,
		   new String[] {
			   "formatString", "-##0.###",
			   "exportFormatString", "##0.###"
		   });
		addAnnotation
		  (getLocation_Lon(),
		   source,
		   new String[] {
			   "formatString", "-##0.###",
			   "exportFormatString", "##0.###"
		   });
		addAnnotation
		  (getContingencyMatrix_DefaultDuration(),
		   source,
		   new String[] {
			   "unit", "days",
			   "formatString", "#0"
		   });
		addAnnotation
		  (getContingencyMatrixEntry_Duration(),
		   source,
		   new String[] {
			   "unit", "days",
			   "formatString", "#0"
		   });
	}

	/**
	 * Initializes the annotations for <b>http://www.mmxlabs.com/models/csv</b>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void createCsvAnnotations() {
		String source = "http://www.mmxlabs.com/models/csv";
		addAnnotation
		  (portGroupEClass,
		   source,
		   new String[] {
			   "namePrefix", "Group"
		   });
		addAnnotation
		  (capabilityGroupEClass,
		   source,
		   new String[] {
			   "namePrefix", "CapGroup"
		   });
		addAnnotation
		  (portCountryGroupEClass,
		   source,
		   new String[] {
			   "namePrefix", "CountryGroup"
		   });
	}

} //PortPackageImpl
