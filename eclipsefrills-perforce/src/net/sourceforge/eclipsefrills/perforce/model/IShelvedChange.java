package net.sourceforge.eclipsefrills.perforce.model;

import org.eclipse.core.runtime.IPath;


public interface IShelvedChange {
	
	void setDescription(final String description);
	
	String getDescription();
	
	void setFilePaths(IPath[] filePaths);
	
	IPath[] getFilePaths();
	
	String getId();
	
	String getTitle();
}
