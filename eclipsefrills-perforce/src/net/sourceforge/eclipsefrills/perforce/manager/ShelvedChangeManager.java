package net.sourceforge.eclipsefrills.perforce.manager;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.UUID;

import net.sourceforge.eclipsefrills.perforce.Activator;
import net.sourceforge.eclipsefrills.perforce.model.IShelvedChange;

import org.apache.commons.io.IOUtils;
import org.eclipse.core.runtime.IPath;

import com.stehno.commons.lang.zip.ZipBuilder;

public class ShelvedChangeManager implements IShelvedChangeManager {

	/**
	 * Instances of this class should not be created directly. The valid instance(s)
	 * of this class should be retrieved from the plug-in activator class, {@link Activator}
	 * 
	 * @see Activator
	 */
	protected ShelvedChangeManager(){
		super();
	}

	public IShelvedChange create(final String title) {
		return new ShelvedChange(UUID.randomUUID().toString(),title);
	}

	public void shelve(final IShelvedChange shelvedChanges, final boolean autoRevert) {

		final ZipBuilder zip = new ZipBuilder(new BufferedOutputStream(new FileOutputStream(shelfFile)));
		zip.useCompression().setCompressionLevel(9);

		try {
			storeMetaProperties(shelvedChanges, zip);

			for(final IPath path : shelvedChanges.getFilePaths()){
				zip.addEntry(path.toPortableString(), IOUtils.toByteArray(new FileInputStream(path.toFile())));
			}

			zip.zip();

		} catch (final Exception ex){
			ex.printStackTrace();
		}		

	}

	public IShelvedChange unshelve(final String id) {
		// TODO Auto-generated method stub
		return null;
	}

	private void storeMetaProperties(final IShelvedChange shelvedChanges,final ZipBuilder zip) throws IOException {
		final Properties props = new Properties();
		props.setProperty("title", shelvedChanges.getTitle());
		props.setProperty("description", shelvedChanges.getDescription());

		final ByteArrayOutputStream propsout = new ByteArrayOutputStream();
		props.store(propsout, "");

		zip.addEntry("shelf-inf", propsout.toByteArray());
	}
}
