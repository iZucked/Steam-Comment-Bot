/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2012
 * All rights reserved.
 */
package com.mmxlabs.models.lng.scenario.modelCorrector;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.cargo.CargoPackage;
import com.mmxlabs.models.lng.cargo.DischargeSlot;
import com.mmxlabs.models.lng.cargo.LoadSlot;
import com.mmxlabs.models.lng.cargo.Slot;
import com.mmxlabs.models.lng.commercial.CommercialFactory;
import com.mmxlabs.models.lng.commercial.CommercialModel;
import com.mmxlabs.models.lng.commercial.CommercialPackage;
import com.mmxlabs.models.lng.commercial.RedirectionContractOriginalDate;
import com.mmxlabs.models.lng.commercial.RedirectionPurchaseContract;
import com.mmxlabs.models.lng.fleet.FleetFactory;
import com.mmxlabs.models.lng.fleet.FleetModel;
import com.mmxlabs.models.lng.fleet.FleetPackage;
import com.mmxlabs.models.lng.fleet.VesselEvent;
import com.mmxlabs.models.lng.fleet.VesselType;
import com.mmxlabs.models.lng.fleet.VesselTypeGroup;
import com.mmxlabs.models.lng.input.ElementAssignment;
import com.mmxlabs.models.lng.input.InputFactory;
import com.mmxlabs.models.lng.input.InputModel;
import com.mmxlabs.models.lng.input.InputPackage;
import com.mmxlabs.models.lng.port.CapabilityGroup;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortFactory;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.port.PortPackage;
import com.mmxlabs.models.lng.pricing.DataIndex;
import com.mmxlabs.models.lng.pricing.PricingFactory;
import com.mmxlabs.models.lng.pricing.PricingModel;
import com.mmxlabs.models.lng.pricing.PricingPackage;
import com.mmxlabs.models.lng.pricing.SpotAvailability;
import com.mmxlabs.models.lng.pricing.SpotMarket;
import com.mmxlabs.models.lng.pricing.SpotMarketGroup;
import com.mmxlabs.models.lng.pricing.SpotType;
import com.mmxlabs.models.lng.schedule.Event;
import com.mmxlabs.models.lng.schedule.ScheduleModel;
import com.mmxlabs.models.lng.schedule.SchedulePackage;
import com.mmxlabs.models.lng.schedule.Sequence;
import com.mmxlabs.models.lng.schedule.SequenceType;
import com.mmxlabs.models.lng.schedule.SlotVisit;
import com.mmxlabs.models.lng.types.AVesselSet;
import com.mmxlabs.models.lng.types.PortCapability;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.mmxcore.MMXRootObject;
import com.mmxlabs.models.mmxcore.MMXSubModel;
import com.mmxlabs.models.mmxcore.UUIDObject;

/**
 * The {@link LNGModelCorrector} is intended to correct models when they are loaded. For example new references added to a metamodel may be in an incorrect state for existing model instances. This
 * class can be invoked to correct such references. Should model migration be added at some point, this class should become redundant.
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
		fixFixedPriceOverrides(cmd, rootObject, ed);
		fixSequenceTypes(cmd, rootObject, ed);
		fixRedirectionContracts(cmd, rootObject, ed);
		fixPortCapabilityGroups(cmd, rootObject, ed);
		fixVesselTypeGroups(cmd, rootObject, ed);
		if (!cmd.isEmpty()) {
			ed.getCommandStack().execute(cmd);
		}

	}

	private void fixPortCapabilityGroups(CompoundCommand parent, MMXRootObject rootObject, EditingDomain ed) {

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

	private void fixVesselTypeGroups(CompoundCommand parent, MMXRootObject rootObject, EditingDomain ed) {

		final CompoundCommand cmd = new CompoundCommand("Fix port capability groups");

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

	private void fixRedirectionContracts(final CompoundCommand parent, final MMXRootObject rootObject, final EditingDomain ed) {
		final CompoundCommand cmd = new CompoundCommand("Fix redirection contracts");

		final CargoModel cargoModel = rootObject.getSubModel(CargoModel.class);
		final CommercialModel commercialModel = rootObject.getSubModel(CommercialModel.class);
		if (cargoModel != null && commercialModel != null) {
			LOOP_SLOTS: for (final LoadSlot slot : cargoModel.getLoadSlots()) {
				if (slot.getContract() instanceof RedirectionPurchaseContract) {
					final RedirectionPurchaseContract redirectionPurchaseContract = (RedirectionPurchaseContract) slot.getContract();

					if (slot.getPort() != redirectionPurchaseContract.getBaseSalesMarketPort()) {
						return;
					}

					final Calendar cal = Calendar.getInstance();
					cal.setTime(slot.getWindowStartWithSlotOrPortTime());
					cal.add(Calendar.DAY_OF_YEAR, -redirectionPurchaseContract.getDaysFromSource());

					RedirectionContractOriginalDate redirectionContractOriginalDate = null;
					for (final UUIDObject ext : slot.getExtensions()) {
						if (ext instanceof RedirectionContractOriginalDate) {
							redirectionContractOriginalDate = (RedirectionContractOriginalDate) ext;
							// Check valid
							if (cal.getTime() == redirectionContractOriginalDate.getDate()) {

								// We're ok, next slot
								continue LOOP_SLOTS;
							}
						}
					}
					// Nothing found, create extension
					if (redirectionContractOriginalDate == null) {
						redirectionContractOriginalDate = CommercialFactory.eINSTANCE.createRedirectionContractOriginalDate();

						cmd.append(AddCommand.create(ed, slot, MMXCorePackage.eINSTANCE.getMMXObject_Extensions(), redirectionContractOriginalDate));
						cmd.append(AddCommand.create(ed, commercialModel, CommercialPackage.eINSTANCE.getCommercialModel_ContractSlotExtensions(), redirectionContractOriginalDate));
					}
					cmd.append(SetCommand.create(ed, redirectionContractOriginalDate, CommercialPackage.eINSTANCE.getRedirectionContractOriginalDate_Date(), cal.getTime()));
				}
			}
		}
		if (!cmd.isEmpty()) {
			parent.append(cmd);
		}
	}

	private void fixSequenceTypes(final CompoundCommand parent, final MMXRootObject rootObject, final EditingDomain ed) {
		final CompoundCommand cmd = new CompoundCommand("Fix sequence types");
		final ScheduleModel scheduleModel = rootObject.getSubModel(ScheduleModel.class);
		if (scheduleModel != null) {

			if (scheduleModel.getInitialSchedule() != null) {
				LOOP_SEQUENCES: for (final Sequence seq : scheduleModel.getInitialSchedule().getSequences()) {
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

	@SuppressWarnings("deprecation")
	private void fixFixedPriceOverrides(final CompoundCommand parent, final MMXRootObject rootObject, final EditingDomain ed) {
		final CompoundCommand cmd = new CompoundCommand("Fix price expressions");
		final CargoModel cargoModel = rootObject.getSubModel(CargoModel.class);
		if (cargoModel != null) {

			final Set<Slot> slots = new HashSet<Slot>();
			slots.addAll(cargoModel.getLoadSlots());
			slots.addAll(cargoModel.getDischargeSlots());

			for (final Slot slot : slots) {

				if (slot.isSetFixedPrice()) {
					if (!slot.isSetPriceExpression()) {
						final double d = slot.getFixedPrice();
						final String str = Double.toString(d);
						cmd.append(SetCommand.create(ed, slot, CargoPackage.eINSTANCE.getSlot_PriceExpression(), str));
					}

					cmd.append(SetCommand.create(ed, slot, CargoPackage.eINSTANCE.getSlot_FixedPrice(), SetCommand.UNSET_VALUE));

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
		final InputModel inputModel = rootObject.getSubModel(InputModel.class);
		if (inputModel != null && cargoModel != null) {

			final Set<Cargo> cargoes = new HashSet<Cargo>();
			for (final Cargo c : cargoModel.getCargoes()) {
				cargoes.add(c);
			}

			for (final ElementAssignment ea : inputModel.getElementAssignments()) {
				cargoes.remove(ea.getAssignedObject());
			}

			for (final Cargo c : cargoes) {
				final ElementAssignment ea = InputFactory.eINSTANCE.createElementAssignment();
				ea.setAssignedObject(c);
				cmd.append(AddCommand.create(ed, inputModel, InputPackage.eINSTANCE.getInputModel_ElementAssignments(), ea));
			}

		}
		if (!cmd.isEmpty()) {
			parent.append(cmd);
		}
	}

	private void fixMissingVesselEventElementAssignments(final CompoundCommand parent, final MMXRootObject rootObject, final EditingDomain ed) {

		final CompoundCommand cmd = new CompoundCommand("Fix missing vessel event element assignments");
		final FleetModel fleetModel = rootObject.getSubModel(FleetModel.class);
		final InputModel inputModel = rootObject.getSubModel(InputModel.class);
		if (inputModel != null && fleetModel != null) {

			final Set<VesselEvent> events = new HashSet<VesselEvent>();
			for (final VesselEvent e : fleetModel.getVesselEvents()) {
				events.add(e);
			}

			for (final ElementAssignment ea : inputModel.getElementAssignments()) {
				events.remove(ea.getAssignedObject());
				if (ea.getAssignedObject() instanceof VesselEvent) {
					final VesselEvent vesselEvent = (VesselEvent) ea.getAssignedObject();
					final EList<AVesselSet> allowedVessels = vesselEvent.getAllowedVessels();
					if (allowedVessels.size() == 1) {
						if (ea.getAssignment() != allowedVessels.get(0)) {
							cmd.append(SetCommand.create(ed, ea, InputPackage.eINSTANCE.getElementAssignment_Assignment(), allowedVessels.get(0)));
						}
					}
				}
			}

			for (final VesselEvent e : events) {
				final ElementAssignment ea = InputFactory.eINSTANCE.createElementAssignment();
				ea.setAssignedObject(e);
				cmd.append(AddCommand.create(ed, inputModel, InputPackage.eINSTANCE.getInputModel_ElementAssignments(), ea));

				final VesselEvent vesselEvent = (VesselEvent) ea.getAssignedObject();
				final EList<AVesselSet> allowedVessels = vesselEvent.getAllowedVessels();
				if (allowedVessels.size() == 1) {
					cmd.append(SetCommand.create(ed, ea, InputPackage.eINSTANCE.getElementAssignment_Assignment(), allowedVessels.get(0)));
				}
			}

		}
		if (!cmd.isEmpty()) {
			parent.append(cmd);
		}
	}

	private void removeBadElementAssignments(final CompoundCommand parent, final MMXRootObject rootObject, final EditingDomain ed) {

		final CompoundCommand cmd = new CompoundCommand("Remove bad element assignments");
		final InputModel inputModel = rootObject.getSubModel(InputModel.class);
		final Set<UUIDObject> seenObjects = new HashSet<UUIDObject>();
		if (inputModel != null) {

			for (final ElementAssignment ea : inputModel.getElementAssignments()) {
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
		final PricingModel pricingModel = rootObject.getSubModel(PricingModel.class);
		if (pricingModel != null) {

			if (pricingModel.getDesPurchaseSpotMarket() == null) {
				final SpotMarketGroup group = PricingFactory.eINSTANCE.createSpotMarketGroup();
				group.setType(SpotType.DES_PURCHASE);
				cmd.append(SetCommand.create(ed, pricingModel, PricingPackage.eINSTANCE.getPricingModel_DesPurchaseSpotMarket(), group));
				fixSpotMarketGroupAvailabilityName(parent, group, ed);
			} else {
				fixSpotMarketGroupAvailabilityName(parent, pricingModel.getDesPurchaseSpotMarket(), ed);
				for (final SpotMarket market : pricingModel.getDesPurchaseSpotMarket().getMarkets()) {
					fixSpotMarketAvailabilityName(cmd, market, ed);
				}
			}
			if (pricingModel.getDesSalesSpotMarket() == null) {
				final SpotMarketGroup group = PricingFactory.eINSTANCE.createSpotMarketGroup();
				group.setType(SpotType.DES_SALE);
				cmd.append(SetCommand.create(ed, pricingModel, PricingPackage.eINSTANCE.getPricingModel_DesSalesSpotMarket(), group));
				fixSpotMarketGroupAvailabilityName(parent, group, ed);
			} else {
				fixSpotMarketGroupAvailabilityName(parent, pricingModel.getDesSalesSpotMarket(), ed);
				for (final SpotMarket market : pricingModel.getDesSalesSpotMarket().getMarkets()) {
					fixSpotMarketAvailabilityName(cmd, market, ed);
				}
			}
			if (pricingModel.getFobPurchasesSpotMarket() == null) {
				final SpotMarketGroup group = PricingFactory.eINSTANCE.createSpotMarketGroup();
				group.setType(SpotType.FOB_PURCHASE);
				cmd.append(SetCommand.create(ed, pricingModel, PricingPackage.eINSTANCE.getPricingModel_FobPurchasesSpotMarket(), group));
				fixSpotMarketGroupAvailabilityName(parent, group, ed);
			} else {
				fixSpotMarketGroupAvailabilityName(parent, pricingModel.getFobPurchasesSpotMarket(), ed);
				for (final SpotMarket market : pricingModel.getFobPurchasesSpotMarket().getMarkets()) {
					fixSpotMarketAvailabilityName(cmd, market, ed);
				}
			}
			if (pricingModel.getFobSalesSpotMarket() == null) {
				final SpotMarketGroup group = PricingFactory.eINSTANCE.createSpotMarketGroup();
				group.setType(SpotType.FOB_SALE);
				cmd.append(SetCommand.create(ed, pricingModel, PricingPackage.eINSTANCE.getPricingModel_FobSalesSpotMarket(), group));
				fixSpotMarketGroupAvailabilityName(parent, group, ed);
			} else {
				fixSpotMarketGroupAvailabilityName(parent, pricingModel.getFobSalesSpotMarket(), ed);
				for (final SpotMarket market : pricingModel.getFobSalesSpotMarket().getMarkets()) {
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
}
