/**
 * de abstracte klass die questions met blob resources gebruiken
 * @author vrolijkx
 */
package BuisinesLayer.questions;


import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;
import javax.persistence.*;
import org.hibernate.Hibernate;
import org.hibernate.engine.jdbc.LobCreator;
import Util.ConnectionUtil;

import BuisinesLayer.QuizMaster;



@Entity
//TODO: Kijken of deze join strategie werkt
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@Table(name="RECOURCES")
public abstract class MediaQuestion extends Question {
	protected static LobCreator lobCreator;
	
	@Column(name="RECOURCE_VALUE")
	@Lob
	Blob data;
	
	
	public MediaQuestion(QuizMaster creator) {
		super(creator);
		if(lobCreator==null) {
			//TODO util klassen maken en dit stuk vervangen
			lobCreator=Hibernate.getLobCreator(ConnectionUtil.getSession());
		}
		
	}
	
	public MediaQuestion() {};
	
	protected void setDataStream(InputStream input,int length) {
		data = lobCreator.createBlob(input,length);
	}
	
	protected InputStream getDataStream() throws SQLException {
		return data.getBinaryStream();
	}
	
}
