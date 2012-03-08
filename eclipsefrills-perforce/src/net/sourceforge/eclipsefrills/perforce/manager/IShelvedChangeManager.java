package net.sourceforge.eclipsefrills.perforce.manager;

import net.sourceforge.eclipsefrills.perforce.model.IShelvedChange;

public interface IShelvedChangeManager {
	
	IShelvedChange create(String title);
	
	void shelve(IShelvedChange shelvedChanges, boolean autoRevert);
	
	IShelvedChange unshelve(String id);
}
