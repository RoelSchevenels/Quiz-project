/**
 * de abstracte klass die questions met blob resources gebruiken
 * @author vrolijkx
 */
package BuisinesLayer.questions;


import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.hibernate.Hibernate;
import org.hibernate.engine.jdbc.LobCreator;

import BuisinesLayer.QuizMaster;
import BuisinesLayer.resources.MediaResource;
import Util.ConnectionUtil;


@Entity
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@Table(name="RESOURCES")
public abstract class MediaQuestion extends Question {
	protected static final LobCreator lobCreator;
	
	static {
		lobCreator=Hibernate.getLobCreator(ConnectionUtil.getSession());
	}
	
	
	@Lob
	@Column(name="RECOURCE_VALUE",columnDefinition="BLOB")
	Blob data;
	
	
	public MediaQuestion(QuizMaster creator) {
		super(creator);
	}
	
	public MediaQuestion() {};//consturctor voor hibernate
	
	protected void setDataStream(InputStream input,long length) {
		data = lobCreator.createBlob(input,length);
	}
	
	protected void setData(byte[] bytes) {
		data = lobCreator.createBlob(bytes);
	}
	
	protected InputStream getDataStream() throws SQLException {
		return data.getBinaryStream();
	}
	
	protected void setResource(MediaResource r) throws IOException {
		this.setDataStream(r.getInputStream(), r.getSize());
	}
	
}
