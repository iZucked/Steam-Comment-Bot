/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2013
 * All rights reserved.
 */
package com.mmxlabs.models.lng.scenario.modelCorrector;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;

import com.mmxlabs.common.Pair;
import com.mmxlabs.models.lng.assignment.AssignmentFactory;
import com.mmxlabs.models.lng.assignment.AssignmentModel;
import com.mmxlabs.models.lng.assignment.AssignmentPackage;
import com.mmxlabs.models.lng.assignment.ElementAssignment;
import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.commercial.CommercialFactory;
import com.mmxlabs.models.lng.commercial.CommercialModel;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.LegalEntity;
import com.mmxlabs.models.lng.commercial.TaxRate;
import com.mmxlabs.models.lng.fleet.FleetFactory;
import com.mmxlabs.models.lng.fleet.FleetModel;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.fleet.VesselClass;
import com.mmxlabs.models.lng.fleet.VesselEvent;
import com.mmxlabs.models.lng.fleet.VesselType;
import com.mmxlabs.models.lng.fleet.VesselTypeGroup;
import com.mmxlabs.models.lng.port.CapabilityGroup;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortFactory;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.port.PortPackage;
import com.mmxlabs.models.lng.port.Route;
import com.mmxlabs.models.lng.pricing.DataIndex;
import com.mmxlabs.models.lng.pricing.PricingFactory;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.lng.pricing.RouteCost;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.lng.schedule.SchedulePackage;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SequenceType;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.spotmarkets.SpotAvailability;
import com.mmxlabs.models.lng.spotmarkets.SpotMarket;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketGroup;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsFactory;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsModel;
import com.mmxlabs.models.lng.spotmarkets.SpotMarketsPackage;
import com.mmxlabs.models.lng.spotmarkets.SpotType;
import com.mmxlabs.models.lng.types.AVesselSet;
import com.mmxlabs.models.lng.types.PortCapability;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.mmxcore.MMXSubModel;
import com.mmxlabs.models.mmxcore.UUIDObject;

/**
 * The {@link LNGModelCorrector} is intended to correct models when they are loaded. For example new references added to a metamodel may be in an incorrect state for existing model instances. This
 * class can be invoked to correct such references. Much of this should now be ported to the model migration.
 * 
 * @author Simon Goodall
 * 
 */
public class LNGModelCorrector {

	public void correctModel(final MMXRootObject rootObject, final EditingDomain ed) {

		final CompoundCommand cmd = new CompoundCommand("Fix model issues");

		fixDuplicateUUIDs(cmd, rootObject, ed);
		fixMissingPortLocations(cmd, rootObject, ed);
		fixMissingCargoElementAssignments(cmd, rootObject, ed);
		fixMissingVesselEventElementAssignments(cmd, rootObject, ed);
		removeBadElementAssignments(cmd, rootObject, ed);
		fixMissingSpotCargoMarkets(cmd, rootObject, ed);
		fixSequenceTypes(cmd, rootObject, ed);
		// fixRedirectionContracts(cmd, rootObject, ed);
		fixPortCapabilityGroups(cmd, rootObject, ed);
		fixVesselTypeGroups(cmd, rootObject, ed);
		correctDateObjects(cmd, rootObject, ed);
		fixCanalCostVesselTypes(cmd, rootObject, ed);

		addMissingTaxCurves(cmd, rootObject, ed);

		if (!cmd.isEmpty()) {
			ed.getCommandStack().execute(cmd);
		}

	}

	private void addMissingTaxCurves(final CompoundCommand parent, final MMXRootObject rootObject, final EditingDomain ed) {

		final CompoundCommand cmd = new CompoundCommand("Fix canal cost vessel types");

		final CommercialModel commercialModel = rootObject.getSubModel(CommercialModel.class);

		if (commercialModel != null) {
			for (final LegalEntity entity : commercialModel.getEntities()) {
				if (entity.getTaxRates() == null || entity.getTaxRates().isEmpty()) {
					final TaxRate taxRate = CommercialFactory.eINSTANCE.createTaxRate();
					taxRate.setValue(0.0f);

					final Calendar cal = Calendar.getInstance();

					cal.set(Calendar.YEAR, 2000);
					cal.set(Calendar.MONTH, Calendar.APRIL);
					cal.set(Calendar.DAY_OF_MONTH, 1);
					cal.set(Calendar.YEAR, 2000);
					cal.set(Calendar.HOUR, 0);
					cal.set(Calendar.MINUTE, 0);
					cal.set(Calendar.SECOND, 0);
					cal.set(Calendar.MILLISECOND, 0);

					taxRate.setDate(cal.getTime());

					cmd.append(AddCommand.create(ed, entity, CommercialPackage.Literals.LEGAL_ENTITY__TAX_RATES, taxRate));

				}
			}
		}

		if (!cmd.isEmpty()) {
			parent.append(cmd);
		}

	}

	private void fixCanalCostVesselTypes(final CompoundCommand parent, final MMXRootObject rootObject, final EditingDomain ed) {

		final CompoundCommand cmd = new CompoundCommand("Fix canal cost vessel types");

		final PricingModel pricingModel = rootObject.getSubModel(PricingModel.class);
		final FleetModel fleetModel = rootObject.getSubModel(FleetModel.class);
		final PortModel portModel = rootObject.getSubModel(PortModel.class);

		if (pricingModel != null) {
			final Set<VesselClass> vesselClasses = new HashSet<VesselClass>(fleetModel.getVesselClasses());
			final Set<Route> routes = new HashSet<Route>(portModel.getRoutes());
			final Set<Pair<Route, VesselClass>> entriesExpected = new HashSet<Pair<Route, VesselClass>>();
			final Set<Pair<Route, VesselClass>> entriesFound = new HashSet<Pair<Route, VesselClass>>();

			// populate the expected entries set
			for (final Route route : routes) {
				// only specify route costs for canal routes
				if (route.isCanal()) {
					// we expect a route cost for each vessel class for this route
					for (final VesselClass vesselClass : vesselClasses) {
						entriesExpected.add(new Pair<Route, VesselClass>(route, vesselClass));
					}
				}
			}

			// populate the found entries set
			for (final RouteCost rc : pricingModel.getRouteCosts()) {
				final Route route = rc.getRoute();
				final VesselClass vc = rc.getVesselClass();
				final Pair<Route, VesselClass> pair = new Pair<Route, VesselClass>(route, vc);

				// remove duplicate entries or unexpected entries
				if (entriesFound.contains(pair) || !entriesExpected.contains(pair)) {
					cmd.append(RemoveCommand.create(ed, pricingModel, PricingPackage.Literals.PRICING_MODEL__ROUTE_COSTS, rc));
				}
				// remember all other entries
				else {
					entriesFound.add(pair);
				}
			}

			// add missing entries
			entriesExpected.removeAll(entriesFound);
			for (final Pair<Route, VesselClass> pair : entriesExpected) {
				final RouteCost rc = PricingFactory.eINSTANCE.createRouteCost();
				rc.setRoute(pair.getFirst());
				rc.setVesselClass(pair.getSecond());
				cmd.append(AddCommand.create(ed, pricingModel, PricingPackage.Literals.PRICING_MODEL__ROUTE_COSTS, rc));
			}

		}

		if (!cmd.isEmpty()) {
			parent.append(cmd);
		}
	}

	private void fixPortCapabilityGroups(final CompoundCommand parent, final MMXRootObject rootObject, final EditingDomain ed) {

		final CompoundCommand cmd = new CompoundCommand("Fix port capability groups");

		final PortModel portModel = rootObject.getSubModel(PortModel.class);
		if (portModel != null) {
			for (final PortCapability capability : PortCapability.values()) {
				boolean found = false;
				for (final CapabilityGroup g : portModel.getSpecialPortGroups()) {
					if (g.getCapability().equals(capability)) {
						found = true;
						break;
					}
				}
				if (found == false) {
					final CapabilityGroup g = PortFactory.eINSTANCE.createCapabilityGroup();
					g.setName("All " + capability.getName() + " Ports");
					g.setCapability(capability);
					cmd.append(AddCommand.create(ed, portModel, PortPackage.Literals.PORT_MODEL__SPECIAL_PORT_GROUPS, g));
				}
			}
		}
		if (!cmd.isEmpty()) {
			parent.append(cmd);
		}
	}

	private void fixVesselTypeGroups(final CompoundCommand parent, final MMXRootObject rootObject, final EditingDomain ed) {

		final CompoundCommand cmd = new CompoundCommand("Fix vessel type groups");

		final FleetModel fleetModel = rootObject.getSubModel(FleetModel.class);
		if (fleetModel != null) {
			for (final VesselType vesselType : VesselType.values()) {
				boolean found = false;
				for (final VesselTypeGroup g : fleetModel.getSpecialVesselGroups()) {
					if (g.getVesselType().equals(vesselType)) {
						found = true;
						break;
					}
				}
				if (found == false) {
					final VesselTypeGroup g = FleetFactory.eINSTANCE.createVesselTypeGroup();
					g.setName("All " + vesselType.getName().replaceAll("_", " ") + " Vessels");
					g.setVesselType(vesselType);
					cmd.append(AddCommand.create(ed, fleetModel, FleetPackage.Literals.FLEET_MODEL__SPECIAL_VESSEL_GROUPS, g));
				}
			}
		}
		if (!cmd.isEmpty()) {
			parent.append(cmd);
		}
	}

	// private void fixRedirectionContracts(final CompoundCommand parent, final MMXRootObject rootObject, final EditingDomain ed) {
	// final CompoundCommand cmd = new CompoundCommand("Fix redirection contracts");
	//
	// final CargoModel cargoModel = rootObject.getSubModel(CargoModel.class);
	// final CommercialModel commercialModel = rootObject.getSubModel(CommercialModel.class);
	// if (cargoModel != null && commercialModel != null) {
	// LOOP_SLOTS: for (final LoadSlot slot : cargoModel.getLoadSlots()) {
	// if (slot.getContract() instanceof RedirectionPurchaseContract) {
	// final RedirectionPurchaseContract redirectionPurchaseContract = (RedirectionPurchaseContract) slot.getContract();
	//
	// if (slot.getPort() != redirectionPurchaseContract.getBaseSalesMarketPort()) {
	// return;
	// }
	//
	// final Calendar cal = Calendar.getInstance();
	// cal.setTime(slot.getWindowStartWithSlotOrPortTime());
	// cal.add(Calendar.DAY_OF_YEAR, -redirectionPurchaseContract.getDaysFromSource());
	//
	// RedirectionContractOriginalDate redirectionContractOriginalDate = null;
	// for (final UUIDObject ext : slot.getExtensions()) {
	// if (ext instanceof RedirectionContractOriginalDate) {
	// redirectionContractOriginalDate = (RedirectionContractOriginalDate) ext;
	// // Check valid
	// if (cal.getTime() == redirectionContractOriginalDate.getDate()) {
	//
	// // We're ok, next slot
	// continue LOOP_SLOTS;
	// }
	// }
	// }
	// // Nothing found, create extension
	// if (redirectionContractOriginalDate == null) {
	// redirectionContractOriginalDate = CommercialFactory.eINSTANCE.createRedirectionContractOriginalDate();
	//
	// cmd.append(AddCommand.create(ed, slot, MMXCorePackage.eINSTANCE.getMMXObject_Extensions(), redirectionContractOriginalDate));
	// cmd.append(AddCommand.create(ed, commercialModel, CommercialPackage.eINSTANCE.getCommercialModel_ContractSlotExtensions(), redirectionContractOriginalDate));
	// }
	// cmd.append(SetCommand.create(ed, redirectionContractOriginalDate, CommercialPackage.eINSTANCE.getRedirectionContractOriginalDate_Date(), cal.getTime()));
	// }
	// }
	// }
	// if (!cmd.isEmpty()) {
	// parent.append(cmd);
	// }
	// }

	private void fixSequenceTypes(final CompoundCommand parent, final MMXRootObject rootObject, final EditingDomain ed) {
		final CompoundCommand cmd = new CompoundCommand("Fix sequence types");
		final ScheduleModel scheduleModel = rootObject.getSubModel(ScheduleModel.class);
		if (scheduleModel != null) {

			if (scheduleModel.getSchedule() != null) {
				LOOP_SEQUENCES: for (final Sequence seq : scheduleModel.getSchedule().getSequences()) {
					if (seq.getSequenceType() == null) {
						if (seq.isSetVessel()) {
							cmd.append(SetCommand.create(ed, seq, SchedulePackage.eINSTANCE.getSequence_SequenceType(), SequenceType.VESSEL));
							continue LOOP_SEQUENCES;
						} else if (seq.isSetVesselClass()) {
							cmd.append(SetCommand.create(ed, seq, SchedulePackage.eINSTANCE.getSequence_SequenceType(), SequenceType.SPOT_VESSEL));
							continue LOOP_SEQUENCES;
						} else {
							for (final Event e : seq.getEvents()) {
								if (e instanceof SlotVisit) {
									final Slot slot = ((SlotVisit) e).getSlotAllocation().getSlot();
									if (slot != null) {
										if (slot instanceof LoadSlot) {
											if (((LoadSlot) slot).isDESPurchase()) {
												cmd.append(SetCommand.create(ed, seq, SchedulePackage.eINSTANCE.getSequence_SequenceType(), SequenceType.DES_PURCHASE));
												continue LOOP_SEQUENCES;
											}
										}
										if (slot instanceof DischargeSlot) {
											if (((DischargeSlot) slot).isFOBSale()) {
												cmd.append(SetCommand.create(ed, seq, SchedulePackage.eINSTANCE.getSequence_SequenceType(), SequenceType.FOB_SALE));
												continue LOOP_SEQUENCES;

											}
										}
									}
								}
							}
						}
						cmd.append(SetCommand.create(ed, seq, SchedulePackage.eINSTANCE.getSequence_SequenceType(), SequenceType.CARGO_SHORTS));
					}
				}
			}
		}
		if (!cmd.isEmpty()) {
			parent.append(cmd);
		}

	}

	private void fixDuplicateUUIDs(final CompoundCommand cmd, final MMXRootObject rootObject, final EditingDomain ed) {
		final Set<String> seenUUIDs = new HashSet<String>();
		for (final MMXSubModel subModel : rootObject.getSubModels()) {

			final TreeIterator<EObject> itr = subModel.getSubModelInstance().eAllContents();
			while (itr.hasNext()) {
				final EObject eObj = itr.next();
				if (eObj instanceof UUIDObject) {
					final UUIDObject uuidObject = (UUIDObject) eObj;
					if (!seenUUIDs.add(uuidObject.getUuid())) {
						cmd.append(SetCommand.create(ed, eObj, MMXCorePackage.eINSTANCE.getUUIDObject_Uuid(), EcoreUtil.generateUUID()));
					}
				}
			}
		}
	}

	private void fixMissingPortLocations(final CompoundCommand parent, final MMXRootObject rootObject, final EditingDomain ed) {

		final CompoundCommand cmd = new CompoundCommand("Fix missing port locations");
		final PortModel portModel = rootObject.getSubModel(PortModel.class);
		if (portModel != null) {

			for (final Port p : portModel.getPorts()) {
				if (p.getLocation() == null) {
					cmd.append(SetCommand.create(ed, p, PortPackage.eINSTANCE.getPort_Location(), PortFactory.eINSTANCE.createLocation()));
				}
			}

		}
		if (!cmd.isEmpty()) {
			parent.append(cmd);
		}
	}

	private void fixMissingCargoElementAssignments(final CompoundCommand parent, final MMXRootObject rootObject, final EditingDomain ed) {

		final CompoundCommand cmd = new CompoundCommand("Fix missing cargo element assignments");
		final CargoModel cargoModel = rootObject.getSubModel(CargoModel.class);
		final AssignmentModel assignmentModel = rootObject.getSubModel(AssignmentModel.class);
		if (assignmentModel != null && cargoModel != null) {

			final Set<Cargo> cargoes = new HashSet<Cargo>();
			for (final Cargo c : cargoModel.getCargoes()) {
				cargoes.add(c);
			}

			for (final ElementAssignment ea : assignmentModel.getElementAssignments()) {
				cargoes.remove(ea.getAssignedObject());
			}

			for (final Cargo c : cargoes) {
				final ElementAssignment ea = AssignmentFactory.eINSTANCE.createElementAssignment();
				ea.setAssignedObject(c);
				cmd.append(AddCommand.create(ed, assignmentModel, AssignmentPackage.eINSTANCE.getAssignmentModel_ElementAssignments(), ea));
			}

		}
		if (!cmd.isEmpty()) {
			parent.append(cmd);
		}
	}

	private void fixMissingVesselEventElementAssignments(final CompoundCommand parent, final MMXRootObject rootObject, final EditingDomain ed) {

		final CompoundCommand cmd = new CompoundCommand("Fix missing vessel event element assignments");
		final FleetModel fleetModel = rootObject.getSubModel(FleetModel.class);
		final AssignmentModel assignmentModel = rootObject.getSubModel(AssignmentModel.class);
		if (assignmentModel != null && fleetModel != null) {

			final Set<VesselEvent> events = new HashSet<VesselEvent>();
			for (final VesselEvent e : fleetModel.getVesselEvents()) {
				events.add(e);
			}

			for (final ElementAssignment ea : assignmentModel.getElementAssignments()) {
				events.remove(ea.getAssignedObject());
				if (ea.getAssignedObject() instanceof VesselEvent) {
					final VesselEvent vesselEvent = (VesselEvent) ea.getAssignedObject();
					final EList<AVesselSet> allowedVessels = vesselEvent.getAllowedVessels();
					if (allowedVessels.size() == 1) {
						if (ea.getAssignment() != allowedVessels.get(0)) {
							cmd.append(SetCommand.create(ed, ea, AssignmentPackage.eINSTANCE.getElementAssignment_Assignment(), allowedVessels.get(0)));
						}
					}
				}
			}

			for (final VesselEvent e : events) {
				final ElementAssignment ea = AssignmentFactory.eINSTANCE.createElementAssignment();
				ea.setAssignedObject(e);
				cmd.append(AddCommand.create(ed, assignmentModel, AssignmentPackage.eINSTANCE.getAssignmentModel_ElementAssignments(), ea));

				final VesselEvent vesselEvent = (VesselEvent) ea.getAssignedObject();
				final EList<AVesselSet> allowedVessels = vesselEvent.getAllowedVessels();
				if (allowedVessels.size() == 1) {
					cmd.append(SetCommand.create(ed, ea, AssignmentPackage.eINSTANCE.getElementAssignment_Assignment(), allowedVessels.get(0)));
				}
			}

		}
		if (!cmd.isEmpty()) {
			parent.append(cmd);
		}
	}

	private void removeBadElementAssignments(final CompoundCommand parent, final MMXRootObject rootObject, final EditingDomain ed) {

		final CompoundCommand cmd = new CompoundCommand("Remove bad element assignments");
		final AssignmentModel assignmentModel = rootObject.getSubModel(AssignmentModel.class);
		final Set<UUIDObject> seenObjects = new HashSet<UUIDObject>();
		if (assignmentModel != null) {

			for (final ElementAssignment ea : assignmentModel.getElementAssignments()) {
				final UUIDObject assignedObject = ea.getAssignedObject();
				// Delete duplicates
				if (!seenObjects.add(assignedObject)) {
					cmd.append(DeleteCommand.create(ed, ea));
					continue;
				}
				if (assignedObject instanceof Cargo) {
					continue;
				}
				if (assignedObject instanceof VesselEvent) {
					continue;
				}
				cmd.append(DeleteCommand.create(ed, ea));
			}
		}
		if (!cmd.isEmpty()) {
			parent.append(cmd);
		}
	}

	private void fixMissingSpotCargoMarkets(final CompoundCommand parent, final MMXRootObject rootObject, final EditingDomain ed) {

		final CompoundCommand cmd = new CompoundCommand("Fix missing spot cargo markets");
		final SpotMarketsModel spotMarketsModel = rootObject.getSubModel(SpotMarketsModel.class);
		if (spotMarketsModel != null) {

			if (spotMarketsModel.getDesPurchaseSpotMarket() == null) {
				final SpotMarketGroup group = SpotMarketsFactory.eINSTANCE.createSpotMarketGroup();
				group.setType(SpotType.DES_PURCHASE);
				cmd.append(SetCommand.create(ed, spotMarketsModel, SpotMarketsPackage.eINSTANCE.getSpotMarketsModel_DesPurchaseSpotMarket(), group));
				fixSpotMarketGroupAvailabilityName(parent, group, ed);
			} else {
				fixSpotMarketGroupAvailabilityName(parent, spotMarketsModel.getDesPurchaseSpotMarket(), ed);
				for (final SpotMarket market : spotMarketsModel.getDesPurchaseSpotMarket().getMarkets()) {
					fixSpotMarketAvailabilityName(cmd, market, ed);
				}
			}
			if (spotMarketsModel.getDesSalesSpotMarket() == null) {
				final SpotMarketGroup group = SpotMarketsFactory.eINSTANCE.createSpotMarketGroup();
				group.setType(SpotType.DES_SALE);
				cmd.append(SetCommand.create(ed, spotMarketsModel, SpotMarketsPackage.eINSTANCE.getSpotMarketsModel_DesSalesSpotMarket(), group));
				fixSpotMarketGroupAvailabilityName(parent, group, ed);
			} else {
				fixSpotMarketGroupAvailabilityName(parent, spotMarketsModel.getDesSalesSpotMarket(), ed);
				for (final SpotMarket market : spotMarketsModel.getDesSalesSpotMarket().getMarkets()) {
					fixSpotMarketAvailabilityName(cmd, market, ed);
				}
			}
			if (spotMarketsModel.getFobPurchasesSpotMarket() == null) {
				final SpotMarketGroup group = SpotMarketsFactory.eINSTANCE.createSpotMarketGroup();
				group.setType(SpotType.FOB_PURCHASE);
				cmd.append(SetCommand.create(ed, spotMarketsModel, SpotMarketsPackage.eINSTANCE.getSpotMarketsModel_FobPurchasesSpotMarket(), group));
				fixSpotMarketGroupAvailabilityName(parent, group, ed);
			} else {
				fixSpotMarketGroupAvailabilityName(parent, spotMarketsModel.getFobPurchasesSpotMarket(), ed);
				for (final SpotMarket market : spotMarketsModel.getFobPurchasesSpotMarket().getMarkets()) {
					fixSpotMarketAvailabilityName(cmd, market, ed);
				}
			}
			if (spotMarketsModel.getFobSalesSpotMarket() == null) {
				final SpotMarketGroup group = SpotMarketsFactory.eINSTANCE.createSpotMarketGroup();
				group.setType(SpotType.FOB_SALE);
				cmd.append(SetCommand.create(ed, spotMarketsModel, SpotMarketsPackage.eINSTANCE.getSpotMarketsModel_FobSalesSpotMarket(), group));
				fixSpotMarketGroupAvailabilityName(parent, group, ed);
			} else {
				fixSpotMarketGroupAvailabilityName(parent, spotMarketsModel.getFobSalesSpotMarket(), ed);
				for (final SpotMarket market : spotMarketsModel.getFobSalesSpotMarket().getMarkets()) {
					fixSpotMarketAvailabilityName(cmd, market, ed);
				}
			}

		}

		if (!cmd.isEmpty()) {
			parent.append(cmd);
		}
	}

	private void fixSpotMarketAvailabilityName(final CompoundCommand parent, final SpotMarket market, final EditingDomain ed) {
		final SpotAvailability availability = market.getAvailability();
		if (availability != null) {
			final DataIndex<Integer> curve = availability.getCurve();
			if (curve != null) {
				if (curve.getName() == null || curve.getName().isEmpty()) {
					parent.append(SetCommand.create(ed, curve, MMXCorePackage.eINSTANCE.getNamedObject_Name(), market.getName()));
				}
			}
		}
	}

	private void fixSpotMarketGroupAvailabilityName(final CompoundCommand parent, final SpotMarketGroup market, final EditingDomain ed) {
		final SpotAvailability availability = market.getAvailability();
		if (availability != null) {
			final DataIndex<Integer> curve = availability.getCurve();
			if (curve != null) {
				if (curve.getName() == null || curve.getName().isEmpty()) {
					parent.append(SetCommand.create(ed, curve, MMXCorePackage.eINSTANCE.getNamedObject_Name(), market.getType().getName()));
				}
			}
		}
	}

	private void correctDateObjects(final CompoundCommand parent, final MMXRootObject rootObject, final EditingDomain ed) {

		final CompoundCommand cmd = new CompoundCommand("Fix date objects");

		// Loop over all models
		for (final MMXSubModel subModel : rootObject.getSubModels()) {
			final EObject model = subModel.getSubModelInstance();
			if (model != null) {
				// Loop over all contents
				final Iterator<EObject> itr = model.eAllContents();
				while (itr.hasNext()) {
					final EObject eObj = itr.next();
					// Find all set date attributes
					for (final EAttribute attrib : eObj.eClass().getEAllAttributes()) {
						if (eObj.eIsSet(attrib)) {
							if (attrib.getEAttributeType().equals(EcorePackage.eINSTANCE.getEDate())) {
								final Date originalDate = (Date) eObj.eGet(attrib);
								final Calendar c = Calendar.getInstance();
								c.setTime(originalDate);
								// Are the lower date fields set?
								if (c.get(Calendar.MILLISECOND) != 0 || c.get(Calendar.SECOND) != 0 || c.get(Calendar.MINUTE) != 0) {
									// Clear them
									c.set(Calendar.MILLISECOND, 0);
									c.set(Calendar.SECOND, 0);
									c.set(Calendar.MINUTE, 0);

									// Round upwards. Demo scenario in question
									// has times of 6:55 which should be 7:00
									// SG: Discussion with proshun - 2013/02/13
									c.add(Calendar.HOUR_OF_DAY, 1);

									cmd.append(SetCommand.create(ed, eObj, attrib, c.getTime()));
								}
							}
						}
					}
				}
			}
		}

		if (!cmd.isEmpty()) {
			parent.append(cmd);
		}
	}
}
