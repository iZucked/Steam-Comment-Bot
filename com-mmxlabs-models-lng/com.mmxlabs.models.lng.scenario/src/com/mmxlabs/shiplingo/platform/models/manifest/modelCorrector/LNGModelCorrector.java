package com.mmxlabs.shiplingo.platform.models.manifest.modelCorrector;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.fleet.FleetModel;
import com.mmxlabs.models.lng.fleet.VesselEvent;
import com.mmxlabs.models.lng.input.ElementAssignment;
import com.mmxlabs.models.lng.input.InputFactory;
import com.mmxlabs.models.lng.input.InputModel;
import com.mmxlabs.models.lng.input.InputPackage;
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
import com.mmxlabs.models.lng.types.AVesselSet;
import com.mmxlabs.models.mmxcore.MMXCorePackage;
import com.mmxlabs.models.mmxcore.MMXRootObject;
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

		fixMissingPortLocations(cmd, rootObject, ed);
		fixMissingCargoElementAssignments(cmd, rootObject, ed);
		fixMissingVesselEventElementAssignments(cmd, rootObject, ed);
		removeBadElementAssignments(cmd, rootObject, ed);
		fixMissingSpotCargoMarkets(cmd, rootObject, ed);
		if (!cmd.isEmpty()) {
			ed.getCommandStack().execute(cmd);
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
			}
			if (pricingModel.getDesSalesSpotMarket() == null) {
				final SpotMarketGroup group = PricingFactory.eINSTANCE.createSpotMarketGroup();
				group.setType(SpotType.DES_SALE);
				cmd.append(SetCommand.create(ed, pricingModel, PricingPackage.eINSTANCE.getPricingModel_DesSalesSpotMarket(), group));
			}
			if (pricingModel.getFobPurchasesSpotMarket() == null) {
				final SpotMarketGroup group = PricingFactory.eINSTANCE.createSpotMarketGroup();
				group.setType(SpotType.FOB_PURCHASE);
				cmd.append(SetCommand.create(ed, pricingModel, PricingPackage.eINSTANCE.getPricingModel_FobPurchasesSpotMarket(), group));
			}
			if (pricingModel.getFobSalesSpotMarket() == null) {
				final SpotMarketGroup group = PricingFactory.eINSTANCE.createSpotMarketGroup();
				group.setType(SpotType.FOB_SALE);
				cmd.append(SetCommand.create(ed, pricingModel, PricingPackage.eINSTANCE.getPricingModel_FobSalesSpotMarket(), group));
			}

			for (final SpotMarket market : pricingModel.getDesPurchaseSpotMarket().getMarkets()) {
				fixSpotMarketAvailabilityName(cmd, market, ed);
			}

			for (final SpotMarket market : pricingModel.getDesSalesSpotMarket().getMarkets()) {
				fixSpotMarketAvailabilityName(cmd, market, ed);
			}

			for (final SpotMarket market : pricingModel.getFobPurchasesSpotMarket().getMarkets()) {
				fixSpotMarketAvailabilityName(cmd, market, ed);
			}

			for (final SpotMarket market : pricingModel.getFobSalesSpotMarket().getMarkets()) {
				fixSpotMarketAvailabilityName(cmd, market, ed);
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
}
