/**
 */
package com.mmxlabs.models.lng.assignment.presentation;

import org.eclipse.emf.common.EMFPlugin;
import org.eclipse.emf.common.ui.EclipseUIPlugin;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.swt.widgets.Display;
import org.osgi.framework.BundleContext;

import com.mmxlabs.models.lng.assignment.editor.AssignmentEditorColors;
import com.mmxlabs.models.lng.types.provider.LNGTypesEditPlugin;
import com.mmxlabs.models.mmxcore.provider.MmxcoreEditPlugin;

/**
 * This is the central singleton for the Assignment editor plugin.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public final class AssignmentEditorPlugin extends EMFPlugin {
	/**
	 * Keep track of the singleton.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final AssignmentEditorPlugin INSTANCE = new AssignmentEditorPlugin();
	
	/**
	 * Keep track of the singleton.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static Implementation plugin;

	/**
	 * Create the instance.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AssignmentEditorPlugin() {
		super
			(new ResourceLocator [] {
				LNGTypesEditPlugin.INSTANCE,
				MmxcoreEditPlugin.INSTANCE,
			});
	}

	/**
	 * Returns the singleton instance of the Eclipse plugin.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the singleton instance.
	 * @generated
	 */
	@Override
	public ResourceLocator getPluginResourceLocator() {
		return plugin;
	}
	
	/**
	 * Returns the singleton instance of the Eclipse plugin.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the singleton instance.
	 * @generated
	 */
	public static Implementation getPlugin() {
		return plugin;
	}
	
	/**
	 * The actual implementation of the Eclipse <b>Plugin</b>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static class Implementation extends EclipseUIPlugin {

		// Shared instance for editors.
		private AssignmentEditorColors assignmentEditorColors = null;

		/**
		 * Creates an instance.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		public Implementation() {
			super();
	
			// Remember the static instance.
			//
			plugin = this;
		}

		@Override
		public void stop(BundleContext context) throws Exception {
			if (assignmentEditorColors != null) {
				assignmentEditorColors.dispose();
				assignmentEditorColors = null;
			}

			super.stop(context);
		}
		
		public synchronized AssignmentEditorColors getAssignmentEditorColours() {
			if (assignmentEditorColors == null) {
				assignmentEditorColors = new AssignmentEditorColors(Display.getDefault());
			}
			return assignmentEditorColors;
		}
	}

}
