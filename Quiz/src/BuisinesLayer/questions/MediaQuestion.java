package BuisinesLayer.questions;


import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.SQLException;

import javassist.bytecode.ByteArray;

import javax.persistence.*;

import main.test.TestDataBase;

import org.hibernate.Hibernate;
import org.hibernate.engine.jdbc.BlobImplementer;
import org.hibernate.engine.jdbc.LobCreator;

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
			lobCreator=Hibernate.getLobCreator(TestDataBase.getSession());
		}
		
	}
	
	public MediaQuestion() {};
	
	protected void setData(InputStream input,int length) {
		data = lobCreator.createBlob(input,length);
	}
	
//	protected void setData(Byte[] bytes) {
//		data = lobCreator.createBlob(bytes);
//	}
	
	protected InputStream getDataStream() throws SQLException {
		return data.getBinaryStream();
	}
	
}
