package com.mmxlabs.rcp.navigator.actions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.util.OpenStrategy;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.OpenSystemEditorAction;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.internal.ide.DialogUtil;
import org.eclipse.ui.internal.ide.IDEWorkbenchMessages;
import org.eclipse.ui.internal.ide.IIDEHelpContextIds;

import com.mmxlabs.rcp.navigator.scenario.ScenarioTreeNodeClass;

public class OpenScenarioAction extends OpenSystemEditorAction {

	/**
	 * The editor to open.
	 */
	private IEditorDescriptor editorDescriptor;

	private final IWorkbenchPage workbenchPage;

	/**
	 * Creates a new action that will open editors on the then-selected file
	 * resources. Equivalent to <code>OpenFileAction(page,null)</code>.
	 * 
	 * @param page
	 *            the workbench page in which to open the editor
	 */
	public OpenScenarioAction(IWorkbenchPage page) {
		this(page, null);
	}

	/**
	 * Creates a new action that will open instances of the specified editor on
	 * the then-selected file resources.
	 * 
	 * @param page
	 *            the workbench page in which to open the editor
	 * @param descriptor
	 *            the editor descriptor, or <code>null</code> if unspecified
	 */
	public OpenScenarioAction(IWorkbenchPage page, IEditorDescriptor descriptor) {
		super(page);
		this.workbenchPage = page;
		setText(descriptor == null ? IDEWorkbenchMessages.OpenFileAction_text
				: descriptor.getLabel());
		PlatformUI.getWorkbench().getHelpSystem()
				.setHelp(this, IIDEHelpContextIds.OPEN_FILE_ACTION);
		setToolTipText(IDEWorkbenchMessages.OpenFileAction_toolTip);
		setId(ID);
		this.editorDescriptor = descriptor;
	}

	/**
	 * Ensures that the contents of the given file resource are local.
	 * 
	 * @param file
	 *            the file resource
	 * @return <code>true</code> if the file is local, and <code>false</code> if
	 *         it could not be made local for some reason
	 */
	boolean ensureFileLocal(final IFile file) {
		// Currently fails due to Core PR. Don't do it for now
		// 1G5I6PV: ITPCORE:WINNT - IResource.setLocal() attempts to modify
		// immutable tree
		// file.setLocal(true, IResource.DEPTH_ZERO);
		return true;
	}

	/**
	 * Opens an editor on the given file resource.
	 * 
	 * @param file
	 *            the file resource
	 */
	void openFile(ScenarioTreeNodeClass node) {
		IFile file = (IFile) node.getResource();
		ScenarioEditorInput editorInput = new ScenarioEditorInput(node);
		try {
			boolean activate = OpenStrategy.activateOnOpen();
			if (editorDescriptor == null) {
				// Lookup default editor
				IEditorDescriptor ed = IDE.getDefaultEditor(file, true);
				if (ed != null) {
					IDE.openEditor(getWorkbenchPage(), editorInput, ed.getId(),
							activate);
				} else {
					// Fall back to opening the raw file
					IDE.openEditor(getWorkbenchPage(), file, activate);
				}
			} else {
				if (ensureFileLocal(file)) {
					getWorkbenchPage().openEditor(editorInput,
							editorDescriptor.getId(), activate);
				}
			}
		} catch (PartInitException e) {
			DialogUtil.openError(getWorkbenchPage().getWorkbenchWindow()
					.getShell(),
					IDEWorkbenchMessages.OpenFileAction_openFileShellTitle, e
							.getMessage(), e);
		}
	}

	/**
	 * Return the workbench page to open the editor in.
	 * 
	 * @return the workbench page to open the editor in
	 */
	/* package */final IWorkbenchPage getWorkbenchPage() {
		return workbenchPage;
	}

	/**
	 * Extracts <code>IResource</code>s from the current selection and adds them
	 * to the resources list, and the rest into the non-resources list.
	 */
	private final List computeScenarios() {

		List resources = null;
		for (Iterator e = getStructuredSelection().iterator(); e.hasNext();) {
			Object next = e.next();
			if (next instanceof ScenarioTreeNodeClass) {
				if (resources == null) {
					// assume selection contains mostly resources most times
					resources = new ArrayList(getStructuredSelection().size());
				}
				resources.add(next);
				continue;
			} else if (next instanceof IAdaptable) {
				Object resource = ((IAdaptable) next)
						.getAdapter(ScenarioTreeNodeClass.class);
				if (resource != null) {
					if (resources == null) {
						// assume selection contains mostly resources most times
						resources = new ArrayList(getStructuredSelection()
								.size());
					}
					resources.add(resource);
					continue;
				}
				// } else {
				//
				// boolean resourcesFoundForThisSelection = false;
				//
				// IAdapterManager adapterManager =
				// Platform.getAdapterManager();
				// ResourceMapping mapping = (ResourceMapping) adapterManager
				// .getAdapter(next, ResourceMapping.class);
				//
				// if (mapping != null) {
				//
				// ResourceTraversal[] traversals = null;
				// try {
				// traversals = mapping.getTraversals(
				// ResourceMappingContext.LOCAL_CONTEXT,
				// new NullProgressMonitor());
				// } catch (CoreException exception) {
				// IDEWorkbenchPlugin.log(exception.getLocalizedMessage(),
				// exception.getStatus());
				// }
				//
				// if (traversals != null) {
				//
				// for (int i = 0; i < traversals.length; i++) {
				//
				// IResource[] traversalResources = traversals[i]
				// .getResources();
				//
				// if (traversalResources != null) {
				//
				// resourcesFoundForThisSelection = true;
				//
				// if (resources == null) {
				// resources = new ArrayList(
				// getStructuredSelection().size());
				// }
				//
				// for (int j = 0; j < traversalResources.length; j++) {
				// resources.add(traversalResources[j]);
				// }// for
				//
				// }// if
				//
				// }// for
				//
				// }// if
				//
				// }// if

				// if (resourcesFoundForThisSelection) {
				// continue;
				// }
			}

			// if (nonResources == null) {
			// // assume selection contains mostly resources most times
			// nonResources = new ArrayList(1);
			// }
			// nonResources.add(next);
		}
		return resources;
	}

	/*
	 * (non-Javadoc) Method declared on IAction.
	 */
	public void run() {
		Iterator itr = computeScenarios().iterator();
		while (itr.hasNext()) {
			ScenarioTreeNodeClass resource = (ScenarioTreeNodeClass) itr.next();
			openFile(resource);
		}
	}

}
