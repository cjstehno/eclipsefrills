package net.sourceforge.eclipsefrills.perforce;

import net.sourceforge.eclipsefrills.perforce.manager.IShelvedChangeManager;
import net.sourceforge.eclipsefrills.perforce.manager.ShelvedChangeManager;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import com.perforce.p4api.IChangelist;
import com.perforce.p4api.PerforceConnection;
import com.perforce.p4api.PerforceException;
import com.perforce.team.core.PerforceProviderPlugin;
import com.perforce.team.core.PerforceTeamProvider;
import com.perforce.team.ui.IPerforceRunnable;
import com.perforce.team.ui.PerforceAccess;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	public static final String PLUGIN_ID = "net.sourceforge.eclipsefrills.perforce";

	private IShelvedChangeManager shelvedChangeManager;
	private static Activator plugin;
	
	public Activator() {
	}

	@Override
	public void start(final BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	@Override
	public void stop(final BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

	/**
	 * Finds the first configured Perforce connection and returns it.
	 * 
	 * @return
	 */
	public final PerforceConnection findConnection(){
		PerforceConnection p4conn = null;

		final boolean changelistSelected = false;
		if(changelistSelected){
			// dump only the specified changelist
			// FIXME: impl
			
		} else {
			// dump changelists for first active project (assumes that all share same p4 server)
			// FIXME: big assumption!

			PerforceTeamProvider provider = null;
			for(final IProject project : ResourcesPlugin.getWorkspace().getRoot().getProjects()){
				provider = PerforceProviderPlugin.getPerforceProviderFor(project);
				if(provider != null) break;
			}
			
			if(provider != null){
				p4conn = PerforceAccess.getPerforceConnection(provider);		
			}
		}
		
		return p4conn;
	}
	
	public final IChangelist[] extractPendingLocalChanges(){
		final PerforceConnection p4conn = findConnection();
		
        final IChangelist pending[] = (IChangelist[])PerforceAccess.exec(p4conn, new IPerforceRunnable() {
            public Object run() throws PerforceException {
                return p4conn.getChangelistAccess().getLocalPendingChanges();
            }
        });
        
        return pending;
	}
	
	public IShelvedChangeManager getShelvedChangeManager(){
		if(shelvedChangeManager == null) shelvedChangeManager = new ShelvedChangeManager(){}; // FIXME: interesting, but is this ghetto?
		return shelvedChangeManager;
	}
}
