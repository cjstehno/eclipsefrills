package net.sourceforge.eclipsefrills.perforce.manager;

import net.sourceforge.eclipsefrills.perforce.model.IShelvedChange;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.eclipse.core.runtime.IPath;

public class ShelvedChange implements IShelvedChange {

	private String id, title, description;
	private IPath[] files;
	
	ShelvedChange(final String id, final String title){
		this.id = id;
		this.title = title;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(final String title) {
		this.title = title;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(final String description) {
		this.description = description;
	}

	public IPath[] getFilePaths() {
		return files;
	}

	public String getId() {
		return id;
	}

	public void setFilePaths(final IPath[] filePaths) {
		this.files = filePaths;
	}
	
	@Override
	public boolean equals(final Object obj){
		boolean eq = false;
		if(obj instanceof ShelvedChange){
			final ShelvedChange changes = (ShelvedChange)obj;
			eq = new EqualsBuilder().append(id,changes.id).append(title, changes.title).append(description,changes.description).append(files,changes.files).isEquals();
		}
		return eq;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(7,13).append(id).append(title).append(description).append(files).toHashCode();
	}
	
	@Override
	public String toString() {
		return new ToStringBuilder(this,ToStringStyle.DEFAULT_STYLE)
			.append("id",id,true).append("title",title,true).append("description",description,true).append("files",files,true)
			.toString();
	}
}