package com.mmxlabs.models.lng.cargo.ui.commands;

import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;

import com.mmxlabs.models.common.commandservice.IModelCommandProvider;
import com.mmxlabs.models.mmxcore.MMXRootObject;

/**
 * An abstract command provider class which produces commands to synchronise specific features
 * of given pairs of EMF objects. When a command to change one of the specified features is
 * executed, a command is created to change the corresponding feature on the paired EMF object. 
 * 
 * Concrete subclasses need to implement the makeBoundFeatures method, to specify which features
 * are synchronised, and the getSynchronisedObject method, to specify which EMF objects are paired
 * with which.
 * 
 * @author Simon McGregor
 *
 */
public abstract class SynchronisedFeatureCommandProvider implements
		IModelCommandProvider {

	final protected Set<EStructuralFeature> boundFeatures = makeBoundFeatures();
	
	/**
	 * Creates a set of structural features which should be synchronised by the command
	 * provider.
	 *  
	 * @return
	 */
	abstract protected Set<EStructuralFeature> makeBoundFeatures();
	
	/**
	 * Returns an EMF object which is expected to have its features synchronised with
	 * the specified EMF object.
	 *  
	 * @param owner The object to synchronise with.
	 * @return The paired object for synchronisation, or null if none.
	 */
	abstract protected EObject getSynchronisedObject(final MMXRootObject root, final EObject owner);
	
	@Override
	public Command provideAdditionalCommand(EditingDomain editingDomain,
			MMXRootObject rootObject, Map<EObject, EObject> overrides,
			Set<EObject> editSet, Class<? extends Command> commandClass,
			CommandParameter parameter, Command input) {
		
		final EObject owner = parameter.getEOwner();
		final EObject syncedObject = getSynchronisedObject(rootObject, owner);
		
		// ignore commands which act on unpaired objects
		if (syncedObject == null) {
			return null;
		}
		
		// respond to commands which SET features of a paired object
		if (commandClass == SetCommand.class) {
			EStructuralFeature feature = parameter.getEStructuralFeature();
			if (boundFeatures.contains(feature)) {
				final EObject value = parameter.getEValue();
				// don't try to synchronise a value which is already synced
				// (since this is likely to lead to infinite loops)
				if (syncedObject.eGet(feature).equals(value)) {
					return null;
				}
				// create a new command to set the synchronised 
				return new SetCommand(editingDomain, syncedObject, feature, value);
			}
		}
		
		// TODO respond correctly to other modifying commands, e.g. Add, Remove, Replace, Move

		
		return null;
	}

	@Override
	public void startCommandProvision() {
		// TODO Auto-generated method stub

	}

	@Override
	public void endCommandProvision() {
		// TODO Auto-generated method stub

	}

}
