package net.sourceforge.eclipsefrills.perforce.view;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

class ViewLabelProvider extends LabelProvider implements ITableLabelProvider {
	
	public String getColumnText(final Object obj, final int index) {
		return getText(obj);
	}
	
	public Image getColumnImage(final Object obj, final int index) {
		return getImage(obj);
	}
	
	@Override
	public Image getImage(final Object obj) {
		return PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_ELEMENT);
	}
}