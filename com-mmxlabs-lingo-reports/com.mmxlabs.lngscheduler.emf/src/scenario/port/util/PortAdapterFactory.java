/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package scenario.port.util;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;
import org.eclipse.emf.ecore.EObject;

import scenario.AnnotatedObject;
import scenario.NamedObject;
import scenario.ScenarioObject;
import scenario.UUIDObject;
import scenario.port.*;
import scenario.port.Canal;
import scenario.port.CanalModel;
import scenario.port.DistanceLine;
import scenario.port.DistanceModel;
import scenario.port.Port;
import scenario.port.PortModel;
import scenario.port.PortPackage;

/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see scenario.port.PortPackage
 * @generated
 */
public class PortAdapterFactory extends AdapterFactoryImpl {
	/**
	 * The cached model package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected static PortPackage modelPackage;

	/**
	 * Creates an instance of the adapter factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public PortAdapterFactory() {
		if (modelPackage == null) {
			modelPackage = PortPackage.eINSTANCE;
		}
	}

	/**
	 * Returns whether this factory is applicable for the type of the object.
	 * <!-- begin-user-doc -->
	 * This implementation returns <code>true</code> if the object is either the model's package or is an instance object of the model.
	 * <!-- end-user-doc -->
	 * @return whether this factory is applicable for the type of the object.
	 * @generated
	 */
	@Override
	public boolean isFactoryForType(Object object) {
		if (object == modelPackage) {
			return true;
		}
		if (object instanceof EObject) {
			return ((EObject)object).eClass().getEPackage() == modelPackage;
		}
		return false;
	}

	/**
	 * The switch that delegates to the <code>createXXX</code> methods.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected PortSwitch<Adapter> modelSwitch =
		new PortSwitch<Adapter>() {
			@Override
			public Adapter casePortModel(PortModel object) {
				return createPortModelAdapter();
			}
			@Override
			public Adapter casePort(Port object) {
				return createPortAdapter();
			}
			@Override
			public Adapter caseDistanceModel(DistanceModel object) {
				return createDistanceModelAdapter();
			}
			@Override
			public Adapter caseDistanceLine(DistanceLine object) {
				return createDistanceLineAdapter();
			}
			@Override
			public Adapter caseCanal(Canal object) {
				return createCanalAdapter();
			}
			@Override
			public Adapter caseCanalModel(CanalModel object) {
				return createCanalModelAdapter();
			}
			@Override
			public Adapter casePortSelection(PortSelection object) {
				return createPortSelectionAdapter();
			}
			@Override
			public Adapter casePortGroup(PortGroup object) {
				return createPortGroupAdapter();
			}
			@Override
			public Adapter caseUUIDObject(UUIDObject object) {
				return createUUIDObjectAdapter();
			}
			@Override
			public Adapter caseScenarioObject(ScenarioObject object) {
				return createScenarioObjectAdapter();
			}
			@Override
			public Adapter caseNamedObject(NamedObject object) {
				return createNamedObjectAdapter();
			}
			@Override
			public Adapter caseAnnotatedObject(AnnotatedObject object) {
				return createAnnotatedObjectAdapter();
			}
			@Override
			public Adapter defaultCase(EObject object) {
				return createEObjectAdapter();
			}
		};

	/**
	 * Creates an adapter for the <code>target</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param target the object to adapt.
	 * @return the adapter for the <code>target</code>.
	 * @generated
	 */
	@Override
	public Adapter createAdapter(Notifier target) {
		return modelSwitch.doSwitch((EObject)target);
	}


	/**
	 * Creates a new adapter for an object of class '{@link scenario.port.PortModel <em>Model</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see scenario.port.PortModel
	 * @generated
	 */
	public Adapter createPortModelAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link scenario.port.Port <em>Port</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see scenario.port.Port
	 * @generated
	 */
	public Adapter createPortAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link scenario.port.DistanceModel <em>Distance Model</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see scenario.port.DistanceModel
	 * @generated
	 */
	public Adapter createDistanceModelAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link scenario.port.DistanceLine <em>Distance Line</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see scenario.port.DistanceLine
	 * @generated
	 */
	public Adapter createDistanceLineAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link scenario.port.Canal <em>Canal</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see scenario.port.Canal
	 * @generated
	 */
	public Adapter createCanalAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link scenario.port.CanalModel <em>Canal Model</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see scenario.port.CanalModel
	 * @generated
	 */
	public Adapter createCanalModelAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link scenario.port.PortSelection <em>Selection</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see scenario.port.PortSelection
	 * @generated
	 */
	public Adapter createPortSelectionAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link scenario.port.PortGroup <em>Group</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see scenario.port.PortGroup
	 * @generated
	 */
	public Adapter createPortGroupAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link scenario.UUIDObject <em>UUID Object</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see scenario.UUIDObject
	 * @generated
	 */
	public Adapter createUUIDObjectAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link scenario.ScenarioObject <em>Object</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see scenario.ScenarioObject
	 * @generated
	 */
	public Adapter createScenarioObjectAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link scenario.NamedObject <em>Named Object</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see scenario.NamedObject
	 * @generated
	 */
	public Adapter createNamedObjectAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link scenario.AnnotatedObject <em>Annotated Object</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see scenario.AnnotatedObject
	 * @generated
	 */
	public Adapter createAnnotatedObjectAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for the default case.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @generated
	 */
	public Adapter createEObjectAdapter() {
		return null;
	}

} //PortAdapterFactory
