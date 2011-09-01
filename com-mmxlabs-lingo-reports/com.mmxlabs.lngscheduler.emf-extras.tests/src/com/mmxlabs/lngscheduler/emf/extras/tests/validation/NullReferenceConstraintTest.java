/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2011
 * All rights reserved.
 */
package com.mmxlabs.lngscheduler.emf.extras.tests.validation;

/**
 * This test is meant to check for certain null values on scenario objects - because of how it works you probably don't want to use mock objects for the targets (you'll have to mock loads of EMF
 * generated methods), but instead construct them using the EMF generated factories.
 * 
 * For example to create a VesselClass you do FleetFactory.eINSTANCE.createVesselClass(), or to create an Entity you do ContractPackage.eINSTANCE.createEntity(). If you look in lng.ecore each EClass
 * belongs to a package, which has an associated factory which is called <Packagename>Factory and has a single instance you can get through the .eINSTANCE field.
 * 
 * Anyway, the following fields on the following classes should make the validation constraint produce an error if they are null:
 * 
 * VesselClass basefuel ladenAttributes ballastAttributes
 * 
 * Vessel class (called class_ in the generated class, because of the java getClass() method)
 * 
 * Slot port
 * 
 * Cargo loadSlot dischargeSlot
 * 
 * PortAndTime port
 * 
 * VesselEvent startPort
 * 
 * CharterOut endPort
 * 
 */
public class NullReferenceConstraintTest {

}
