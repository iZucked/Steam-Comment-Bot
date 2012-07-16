package com.mmxlabs.shiplingo.platform.models.manifest.modelCorrector;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;

import com.mmxlabs.models.lng.cargo.Cargo;
import com.mmxlabs.models.lng.cargo.CargoModel;
import com.mmxlabs.models.lng.input.ElementAssignment;
import com.mmxlabs.models.lng.input.InputFactory;
import com.mmxlabs.models.lng.input.InputModel;
import com.mmxlabs.models.lng.input.InputPackage;
import com.mmxlabs.models.lng.port.Port;
import com.mmxlabs.models.lng.port.PortFactory;
import com.mmxlabs.models.lng.port.PortModel;
import com.mmxlabs.models.lng.port.PortPackage;
import com.mmxlabs.models.mmxcore.MMXRootObject;

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
		fixMissingElementAssignments(cmd, rootObject, ed);

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

	private void fixMissingElementAssignments(final CompoundCommand parent, final MMXRootObject rootObject, final EditingDomain ed) {

		final CompoundCommand cmd = new CompoundCommand("Fix missing element assignments");
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
}
