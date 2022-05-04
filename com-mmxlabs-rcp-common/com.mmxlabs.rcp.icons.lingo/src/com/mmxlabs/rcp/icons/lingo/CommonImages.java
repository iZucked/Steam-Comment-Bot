/**
 * Copyright (C) Minimax Labs Ltd., 2010 - 2022
 * All rights reserved.
 */
package com.mmxlabs.rcp.icons.lingo;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.resource.ResourceLocator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Item;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.application.IWorkbenchConfigurer;

import com.mmxlabs.rcp.icons.lingo.internal.Activator;

/**
 * Snippet for client code:
 * setImageDescriptor(CommonImages.getImageDescriptor(IconPaths.,
 * IconMode.Enabled));
 * setDisabledImageDescriptor(CommonImages.getImageDescriptor(IconPaths.,
 * IconMode.Disabled)); // For plugin.xml: TRY
 * platform:/plugin/com.example.plugin.workspace/icons/workspace.png OR
 * platform://com.mmxlabs.rcp.common/icons/plug.png Old code from pre-this
 * class: //
 * setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_TOOL_DELETE));
 * //
 * setDisabledImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_TOOL_DELETE_DISABLED));
 */
public final class CommonImages {

	private static final String PLUGIN_ID = "com.mmxlabs.rcp.icons.lingo";

	public enum IconPaths {
		// 6x8
		DarkArrowDown(8, "icons/6x8/dark_arrow_down.png"), //
		DarkArrowUp(8, "icons/6x8/dark_arrow_up.png"), //
		GreenArrowDown(8, "icons/6x8/green_arrow_down.png"), //
		GreenArrowUp(8, "icons/6x8/green_arrow_up.png"), //
		RedArrowDown(8, "icons/6x8/red_arrow_down.png"), //
		RedArrowUp(8, "icons/6x8/red_arrow_up.png"), //
		SteadyArrow(8, "icons/6x8/steady_arrow.png"), //
		
		// 8x8
		Pin_8(8, "icons/8x8/pin.png"), //
		PinnedRow(8, "icons/8x8/pin.png"), //

		// 16x16
		BaseFlag(16, "icons/16x16/base_flag.png"), //
		BaseFlagGreen(16, "icons/16x16/base_flag_green.png"), //
		CanalPanama(16, "icons/16x16/canal_panama.png"), //
		CanalPlain(16, "icons/16x16/canal_plain.png"), //
		CanalSuez(16, "icons/16x16/canal_suez.png"), //
		Cloud_16(16, "icons/16x16/cloud.png"), //
		CloudPlay_16(16, "icons/16x16/cloud_run.png"), //
		CollapseAll(16, "icons/16x16/collapse_all.png"), //
		ConnectedNotAuth(16, "icons/16x16/connected_not_auth.png"), //
		Console(16, "icons/16x16/console.png"), //
		Copy(16, "icons/16x16/copy.png"), //
		Cut(16, "icons/16x16/cut.png"), //
		Date(16, "icons/16x16/date.png"), //
		Delete(16, "icons/16x16/delete.png"), //
		Delta(16, "icons/16x16/delta.png"), //
		Econs(16, "icons/16x16/econs.png"), //
		Edit(16, "icons/16x16/edit.png"), //
		ExpandAll(16, "icons/16x16/expand_all.png"), //
		Filter(16, "icons/16x16/filter.png"), //
		Folder(16, "icons/16x16/folder.png"), //
		Fuel(16, "icons/16x16/fuel.png"), //
		Highlighter(16, "icons/16x16/highlighter.png"), //
		Hub(16, "icons/16x16/hub.png"), //
		Label(16, "icons/16x16/label.png"), //
		Local(16, "icons/16x16/local.png"), //
		Nominations(16, "icons/16x16/nominations.png"), //
		OptimisationResult(16, "icons/16x16/optimisation_result.png"), //
		Pack(16, "icons/16x16/pack.png"), //
		Paste(16, "icons/16x16/paste.png"), //
		Pin(16, "icons/16x16/pin.png"), //
		Play_16(16, "icons/16x16/optimise.png"), //
		Plus(16, "icons/16x16/plus.png"), //
		Plusplus(16, "icons/16x16/plusplus.png"), //
		ReEvaluate_16(16, "icons/16x16/reevaluate.png"), //
		ResetWiring(16, "icons/16x16/reset_wiring.png"), //
		Sandbox(16, "icons/16x16/sandbox.png"), //
		Scenario(16, "icons/16x16/scenario.png"), //
		ScenarioFragment(16, "icons/16x16/scenario_fragment.png"), //
		Sort(16, "icons/16x16/sort.png"), //
		ZoomIn(16, "icons/16x16/zoom_in.png"), //
		ZoomOut(16, "icons/16x16/zoom_out.png"), //


		Error(16, "icons/legacy/16x16/error.gif"), //
		Information(16, "icons/legacy/16x16/information.gif"), //
		Warning(16, "icons/legacy/16x16/warning.gif"), //

		// 24x24
		Cloud_24(24, "icons/24x24/cloud.png"), //
		CloudPlay_24(24, "icons/24x24/cloud_run_beta.png"), //
		Evaluate(24, "icons/24x24/evaluate_schedule.png"), //
		Fork(24, "icons/24x24/fork.png"), //
		Pin_24(24, "icons/24x24/pin.png"), //
		Play(24, "icons/24x24/optimise.png"), //
		Risk(24, "icons/24x24/risk.png"), //
		Save(24, "icons/24x24/save.png"), //
		Saveall(24, "icons/24x24/save_all.png"), //
		Today(24, "icons/24x24/today.png"), //
		Terminate(24, "icons/24x24/terminate.png"), //
		ReEvaluate_24(24, "icons/24x24/reevaluate.png"), //
//		Resume(24,"icons/24x24/terminate.png", true), //
		;

		private int size;
		private final String path;
		private final String disabledPath;

		private IconPaths(final int size, final String path) {
			this.size = size;
			this.path = path;
			final StringBuilder sb = new StringBuilder(path);

			this.disabledPath = sb.insert(path.lastIndexOf("."), "_disabled").toString();

			final ImageRegistry imageRegistry = Activator.getInstance().getImageRegistry();

			final ImageDescriptor enabledDescriptor = ResourceLocator.imageDescriptorFromBundle(PLUGIN_ID, path).orElseThrow(() -> new RuntimeException("Unable to find image " + path));
			imageRegistry.put(path, enabledDescriptor);

			final ImageDescriptor d = ResourceLocator.imageDescriptorFromBundle(PLUGIN_ID, disabledPath).orElse(null);
			if (d != null) {
				imageRegistry.put(disabledPath, d);
			} else {
				imageRegistry.put(disabledPath, ImageDescriptor.createWithFlags(enabledDescriptor, SWT.IMAGE_DISABLE));
			}
		}

		public String getPath() {
			return path;
		}

		public int getSize() {
			return size;
		}

		public boolean match(final int size, final String name) {
			if (size == 0 || size == this.size) {
				// Exact name match
				if (name().equalsIgnoreCase(name)) {
					return true;
				}
				if (size != 0) {
					// Append e.g. _16 to the name and try again.
					if (name().equalsIgnoreCase(name + "_" + size)) {
						return true;

					}
				}
			}
			return false;
		}

		public ImageDescriptor getImageDescriptor(IconMode mode) {
			if (mode == IconMode.Enabled) {
				return Activator.getInstance().getImageRegistry().getDescriptor(path);
			} else {
				assert mode == IconMode.Disabled;
				return Activator.getInstance().getImageRegistry().getDescriptor(disabledPath);
			}
		}

		/**
		 * Returns a shared image instance. Do not dispose.
		 * 
		 * @param iconPath
		 * @param mode
		 * @return
		 */
		public Image getImage(IconMode mode) {
			if (mode == IconMode.Enabled) {
				return Activator.getInstance().getImageRegistry().get(path);
			} else {
				assert mode == IconMode.Disabled;
				return Activator.getInstance().getImageRegistry().get(disabledPath);
			}
		}

		public URL getImageURL(IconMode mode) {
			try {
				if (mode == IconMode.Enabled) {
					return new URL(String.format("icons:/%s", name()));
				} else {
					assert mode == IconMode.Disabled;
					return new URL(String.format("icons:/%s?disabled", name()));
				}
			} catch (MalformedURLException e) {
				throw new RuntimeException(e);
			}
		}
	}

	public enum IconMode {
		Enabled, Disabled;
	}

	public static ImageDescriptor getImageDescriptor(final IconPaths iconPath, final IconMode mode) {
		return iconPath.getImageDescriptor(mode);
	}

	/**
	 * Returns a shared image instance. Do not dispose.
	 * 
	 * @param iconPath
	 * @param mode
	 * @return
	 */
	public static Image getImage(final IconPaths iconPath, final IconMode mode) {
		return iconPath.getImage(mode);
	}

	public static void setImageDescriptors(final IAction a, final IconPaths iconPath) {
		setImageDescriptors(a, iconPath, false);
	}

	public static URL getImageURL(final IconPaths iconPath, final IconMode mode) {
		return iconPath.getImageURL(mode);
	}

	public static URL getImageURL(final IconPaths iconPath) {
		return iconPath.getImageURL(IconMode.Enabled);
	}

	public static String getImageURI(final IconPaths iconPath) {
		return getImageURL(iconPath).toString();
	}

	public static void setImageDescriptors(final IAction a, final IconPaths iconPath, final boolean includeHover) {

		a.setImageDescriptor(getImageDescriptor(iconPath, IconMode.Enabled));
		a.setDisabledImageDescriptor(getImageDescriptor(iconPath, IconMode.Disabled));
		if (includeHover) {
			a.setHoverImageDescriptor(getImageDescriptor(iconPath, IconMode.Enabled));
		}
	}

	public static void setImage(final Item item, final IconPaths iconPath) {
		item.setImage(iconPath.getImage(IconMode.Enabled));
	}

	/**
	 * Map images to well-known shared images.
	 * 
	 * @param workbenchConfigurer
	 */
	public static void declareWorkbenchImages(IWorkbenchConfigurer workbenchConfigurer) {

		workbenchConfigurer.declareImage(ISharedImages.IMG_OBJ_FOLDER, IconPaths.Folder.getImageDescriptor(IconMode.Enabled), true);

		workbenchConfigurer.declareImage(ISharedImages.IMG_ETOOL_DELETE, IconPaths.Delete.getImageDescriptor(IconMode.Enabled), true);
		workbenchConfigurer.declareImage(ISharedImages.IMG_ETOOL_DELETE_DISABLED, IconPaths.Delete.getImageDescriptor(IconMode.Disabled), true);
		workbenchConfigurer.declareImage(ISharedImages.IMG_TOOL_DELETE, IconPaths.Delete.getImageDescriptor(IconMode.Enabled), true);
		workbenchConfigurer.declareImage(ISharedImages.IMG_TOOL_DELETE_DISABLED, IconPaths.Delete.getImageDescriptor(IconMode.Disabled), true);

		workbenchConfigurer.declareImage(ISharedImages.IMG_ELCL_COLLAPSEALL, IconPaths.CollapseAll.getImageDescriptor(IconMode.Enabled), true);
		workbenchConfigurer.declareImage(ISharedImages.IMG_ELCL_COLLAPSEALL_DISABLED, IconPaths.CollapseAll.getImageDescriptor(IconMode.Disabled), true);

		workbenchConfigurer.declareImage(ISharedImages.IMG_ETOOL_SAVEALL_EDIT, IconPaths.Saveall.getImageDescriptor(IconMode.Enabled), true);
		workbenchConfigurer.declareImage(ISharedImages.IMG_ETOOL_SAVEALL_EDIT_DISABLED, IconPaths.Saveall.getImageDescriptor(IconMode.Disabled), true);

		workbenchConfigurer.declareImage(ISharedImages.IMG_ETOOL_SAVE_EDIT, IconPaths.Save.getImageDescriptor(IconMode.Enabled), true);
		workbenchConfigurer.declareImage(ISharedImages.IMG_ETOOL_SAVE_EDIT_DISABLED, IconPaths.Save.getImageDescriptor(IconMode.Disabled), true);

		workbenchConfigurer.declareImage(ISharedImages.IMG_TOOL_COPY, IconPaths.Copy.getImageDescriptor(IconMode.Enabled), true);
		workbenchConfigurer.declareImage(ISharedImages.IMG_TOOL_COPY_DISABLED, IconPaths.Copy.getImageDescriptor(IconMode.Disabled), true);

		workbenchConfigurer.declareImage(ISharedImages.IMG_TOOL_PASTE, IconPaths.Paste.getImageDescriptor(IconMode.Enabled), true);
		workbenchConfigurer.declareImage(ISharedImages.IMG_TOOL_PASTE, IconPaths.Paste.getImageDescriptor(IconMode.Disabled), true);
	}
}
