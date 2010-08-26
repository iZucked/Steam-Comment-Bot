package com.mmxlabs.jobmanager.ui.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.handlers.HandlerUtil;

import scenario.Scenario;
import scenario.ScenarioPackage;

import com.mmxlabs.jobcontroller.core.impl.ScenarioOptimisationJob;
import com.mmxlabs.jobcontroller.views.JobManagerView;

/**
 * Run an optimisation for the scenarios selected in the navigator
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class ScenarioOptimisationHandler extends AbstractHandler {
	/**
	 * The constructor.
	 */
	public ScenarioOptimisationHandler() {
	}

	/**
	 * the command has been executed, so extract extract the needed information
	 * from the application context.
	 */
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		
		ISelection selection = window.getSelectionService().getSelection(IPageLayout.ID_RES_NAV);
		System.out.println("Selection = " + selection);
		if (selection instanceof IStructuredSelection) {
			IStructuredSelection items = (IStructuredSelection) selection;
			
			ResourceSet resourceSet = new ResourceSetImpl();
			resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put
				(Resource.Factory.Registry.DEFAULT_EXTENSION, 
						new XMIResourceFactoryImpl());
			resourceSet.getPackageRegistry().put(ScenarioPackage.eNS_URI, ScenarioPackage.eINSTANCE);
			
			//get the job manager view
			try {
				JobManagerView jmv =
					(JobManagerView) window.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(JobManagerView.ID);
				for (Object x : items.toList()) {
					if (x instanceof IFile) {
						IFile file = (IFile) x;
						System.out.println("Loading resource from " + file);
						//Load scenario model from file
						Resource resource = resourceSet.getResource(URI.createFileURI( file.getLocation().toOSString() ), true);
						
						for (EObject e : resource.getContents()) {
							if (e instanceof Scenario) {
								//start job for scenario
								Scenario s = (Scenario) e;
								ScenarioOptimisationJob job = new ScenarioOptimisationJob("Optimise " + file.getName(), s);
								jmv.addJob(job);
							}
						}
					}
				}
			} catch (PartInitException e1) {
				e1.printStackTrace();
				return null;
			}
			
			
		}
		
		return null;
	}
}
