/**
 * Copyright (C) Minimaxlabs, 2010
 * All rights reserved.
 */

package com.mmxlabs.demo.app.actions;

import org.eclipse.core.resources.IFile;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;

import scenario.Scenario;
import scenario.ScenarioPackage;

import com.mmxlabs.common.Pair;
import com.mmxlabs.jobcontroller.emf.IncompleteScenarioException;
import com.mmxlabs.jobcontroller.emf.LNGScenarioTransformer;
import com.mmxlabs.jobcontroller.emf.optimisationsettings.OptimisationTransformer;
import com.mmxlabs.optimiser.core.IOptimisationContext;
import com.mmxlabs.optimiser.core.scenario.IOptimisationData;
import com.mmxlabs.optimiser.lso.impl.LocalSearchOptimiser;
import com.mmxlabs.scheduler.optimiser.components.ISequenceElement;

public class OptimiseActionDelegate implements IViewActionDelegate {
	ISelection lastSelection = null;
	@Override
	public void run(IAction action) {
		if (lastSelection instanceof IStructuredSelection) {
			IStructuredSelection items = (IStructuredSelection) lastSelection;
			
			ResourceSet resourceSet = new ResourceSetImpl();
			resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put
				(Resource.Factory.Registry.DEFAULT_EXTENSION, 
						new XMIResourceFactoryImpl());
			resourceSet.getPackageRegistry().put(ScenarioPackage.eNS_URI, ScenarioPackage.eINSTANCE);
			
			for (Object x : items.toList()) {
				if (x instanceof IFile) {
					IFile file = (IFile) x;
					//Load scenario model from file
					Resource resource = resourceSet.getResource(URI.createFileURI( file.getLocation().toOSString() ), true);
					
					for (EObject e : resource.getContents()) {
						if (e instanceof Scenario) {
							//this resource contains a scenario, which we must now transform
							LNGScenarioTransformer transformer = new LNGScenarioTransformer((Scenario) e);
							try {
								IOptimisationData<ISequenceElement> data = transformer.createOptimisationData();
								
								OptimisationTransformer optTransformer = new OptimisationTransformer(transformer.getOptimisationSettings());
								
								Pair<IOptimisationContext<ISequenceElement>, LocalSearchOptimiser<ISequenceElement>> 
									optimiserAndContext = optTransformer.createOptimiserAndContext(data);
								
								//create and invoke a job.
								
							} catch (IncompleteScenarioException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
					}
				}
			}
		}
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {
		lastSelection = selection;
	}

	@Override
	public void init(IViewPart view) {
		
	}
}
