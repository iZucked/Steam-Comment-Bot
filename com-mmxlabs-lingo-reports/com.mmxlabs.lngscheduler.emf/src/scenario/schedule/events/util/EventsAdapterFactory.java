/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package scenario.schedule.events.util;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;

import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;

import org.eclipse.emf.ecore.EObject;

import scenario.ScenarioObject;
import scenario.schedule.events.*;

/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see scenario.schedule.events.EventsPackage
 * @generated
 */
public class EventsAdapterFactory extends AdapterFactoryImpl {
	/**
	 * The cached model package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected static EventsPackage modelPackage;

	/**
	 * Creates an instance of the adapter factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EventsAdapterFactory() {
		if (modelPackage == null) {
			modelPackage = EventsPackage.eINSTANCE;
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
	protected EventsSwitch<Adapter> modelSwitch =
		new EventsSwitch<Adapter>() {
			@Override
			public Adapter caseFuelMixture(FuelMixture object) {
				return createFuelMixtureAdapter();
			}
			@Override
			public Adapter caseFuelQuantity(FuelQuantity object) {
				return createFuelQuantityAdapter();
			}
			@Override
			public Adapter caseScheduledEvent(ScheduledEvent object) {
				return createScheduledEventAdapter();
			}
			@Override
			public Adapter caseIdle(Idle object) {
				return createIdleAdapter();
			}
			@Override
			public Adapter caseJourney(Journey object) {
				return createJourneyAdapter();
			}
			@Override
			public Adapter casePortVisit(PortVisit object) {
				return createPortVisitAdapter();
			}
			@Override
			public Adapter caseSlotVisit(SlotVisit object) {
				return createSlotVisitAdapter();
			}
			@Override
			public Adapter caseCharterOutVisit(CharterOutVisit object) {
				return createCharterOutVisitAdapter();
			}
			@Override
			public Adapter caseScenarioObject(ScenarioObject object) {
				return createScenarioObjectAdapter();
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
	 * Creates a new adapter for an object of class '{@link scenario.schedule.events.FuelMixture <em>Fuel Mixture</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see scenario.schedule.events.FuelMixture
	 * @generated
	 */
	public Adapter createFuelMixtureAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link scenario.schedule.events.FuelQuantity <em>Fuel Quantity</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see scenario.schedule.events.FuelQuantity
	 * @generated
	 */
	public Adapter createFuelQuantityAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link scenario.schedule.events.ScheduledEvent <em>Scheduled Event</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see scenario.schedule.events.ScheduledEvent
	 * @generated
	 */
	public Adapter createScheduledEventAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link scenario.schedule.events.Idle <em>Idle</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see scenario.schedule.events.Idle
	 * @generated
	 */
	public Adapter createIdleAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link scenario.schedule.events.Journey <em>Journey</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see scenario.schedule.events.Journey
	 * @generated
	 */
	public Adapter createJourneyAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link scenario.schedule.events.PortVisit <em>Port Visit</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see scenario.schedule.events.PortVisit
	 * @generated
	 */
	public Adapter createPortVisitAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link scenario.schedule.events.SlotVisit <em>Slot Visit</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see scenario.schedule.events.SlotVisit
	 * @generated
	 */
	public Adapter createSlotVisitAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link scenario.schedule.events.CharterOutVisit <em>Charter Out Visit</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see scenario.schedule.events.CharterOutVisit
	 * @generated
	 */
	public Adapter createCharterOutVisitAdapter() {
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

} //EventsAdapterFactory
