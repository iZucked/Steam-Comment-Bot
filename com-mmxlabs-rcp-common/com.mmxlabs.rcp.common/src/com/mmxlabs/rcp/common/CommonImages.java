package com.mmxlabs.rcp.common;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ResourceLocator;
import org.eclipse.swt.SWT;
import org.eclipse.ui.plugin.AbstractUIPlugin;

/** 
 * Snippet for client code: 
 		setImageDescriptor(CommonImages.getImageDescriptor(IconPaths., IconMode.Enabled));
 		setDisabledImageDescriptor(CommonImages.getImageDescriptor(IconPaths., IconMode.Disabled));
//		For plugin.xml: TRY platform:/plugin/com.example.plugin.workspace/icons/workspace.png  OR  platform://com.mmxlabs.rcp.common/icons/plug.png
 * Old code from pre-this class:
 			// setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_TOOL_DELETE));
			// setDisabledImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_TOOL_DELETE_DISABLED));
*/
public final class CommonImages {

	public enum IconPaths {
				
		Fork("icons/fork.png", true),
		Pack("icons/pack.png"),
		Plus("icons/plus.png", true),
		Plusplus("icons/plusplus.png", true), 
		Delete("icons/delete.png", true), 
		Filter("icons/filter.png"), 		
		Save("icons/save.png", true), 
		Saveall("icons/saveall.png", true), 
		Sort("icons/sort.png"), 
		Play("icons/optimise.png"), 
		Copy("icons/copy.png");
		
		
//		PackGridHand 44: element.setIconURI(FrameworkUtil.getBundle(PackGridHandler.class).getEntry("/icons/pack.png").toString());  
//		FilterField 71: setImageDescriptor(ImageDescriptor.createFromURL(new URL("platform:/plugin/com.mmxlabs.models.ui.tabular/icons/filter.png")));
// ContractPage 378:		ResourceLocator.imageDescriptorFromBundle("com.mmxlabs.rcp.common", "/icons/pack.png").ifPresent(packAction::setImageDescriptor);

					
		private String path;
		private String disabledPath;
		
		private IconPaths(String path, String disabledPath) {
			this.path = path;
			this.disabledPath = disabledPath;
		}
				
		private IconPaths(String path) {
			this.path = path;
			this.disabledPath = null;
		}		

		private IconPaths(String path, boolean defaultDisabled) {
			this.path = path;
			StringBuilder sb = new StringBuilder(path);
			disabledPath = sb.insert(path.lastIndexOf("."), "_disabled").toString();
		}		

	}
	
	public enum IconMode{
		Enabled, Disabled;
	}
	
	public static ImageDescriptor getImageDescriptor(IconPaths iconPath, IconMode mode){
	
		switch(mode) {
			case Enabled:
//				return AbstractUIPlugin.imageDescriptorFromPlugin("com.mmxlabs.rcp.common", iconPath.path);
				return ResourceLocator.imageDescriptorFromBundle("com.mmxlabs.rcp.common", iconPath.path).orElse(null);
			case Disabled:
				if(iconPath.disabledPath != null) { 
					return ResourceLocator.imageDescriptorFromBundle("com.mmxlabs.rcp.common", iconPath.disabledPath).orElse(null);
//					return AbstractUIPlugin.imageDescriptorFromPlugin("com.mmxlabs.rcp.common", iconPath.disabledPath);
				} else{
					return ImageDescriptor.createWithFlags(AbstractUIPlugin.imageDescriptorFromPlugin("com.mmxlabs.rcp.common", iconPath.path), SWT.IMAGE_DISABLE);
				}
			default : 
				throw new IllegalArgumentException("Bad icon spec");
		}
	}
	
	public static void setImageDescriptors(Action a, IconPaths iconPath) {
		
		a.setImageDescriptor(getImageDescriptor(iconPath, IconMode.Enabled));
		a.setDisabledImageDescriptor(getImageDescriptor(iconPath, IconMode.Disabled));
	}
}
