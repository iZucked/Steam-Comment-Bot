/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package scenario.schedule.events;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see scenario.schedule.events.EventsPackage
 * @generated
 */
public interface EventsFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	EventsFactory eINSTANCE = scenario.schedule.events.impl.EventsFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>Fuel Mixture</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Fuel Mixture</em>'.
	 * @generated
	 */
	FuelMixture createFuelMixture();

	/**
	 * Returns a new object of class '<em>Fuel Quantity</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Fuel Quantity</em>'.
	 * @generated
	 */
	FuelQuantity createFuelQuantity();

	/**
	 * Returns a new object of class '<em>Scheduled Event</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Scheduled Event</em>'.
	 * @generated
	 */
	ScheduledEvent createScheduledEvent();

	/**
	 * Returns a new object of class '<em>Idle</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Idle</em>'.
	 * @generated
	 */
	Idle createIdle();

	/**
	 * Returns a new object of class '<em>Journey</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Journey</em>'.
	 * @generated
	 */
	Journey createJourney();

	/**
	 * Returns a new object of class '<em>Port Visit</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Port Visit</em>'.
	 * @generated
	 */
	PortVisit createPortVisit();

	/**
	 * Returns a new object of class '<em>Slot Visit</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Slot Visit</em>'.
	 * @generated
	 */
	SlotVisit createSlotVisit();

	/**
	 * Returns a new object of class '<em>Charter Out Visit</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Charter Out Visit</em>'.
	 * @generated
	 */
	CharterOutVisit createCharterOutVisit();

	/**
	 * Returns a new object of class '<em>Vessel Event Visit</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Vessel Event Visit</em>'.
	 * @generated
	 */
	VesselEventVisit createVesselEventVisit();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	EventsPackage getEventsPackage();

} //EventsFactory
