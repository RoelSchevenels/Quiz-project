/**
 * multiplechois question 
 */
package BussinesLayer.questions;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import BussinesLayer.QuizMaster;


@Entity
@DiscriminatorValue("MUCHOIS")
public class MultipleChoise extends Question{
	@OneToMany(mappedBy="question",fetch=FetchType.EAGER)
	@Cascade(value={CascadeType.SAVE_UPDATE,CascadeType.DELETE})
	private List<MultipleChoisePosebility> possibilities = new ArrayList<MultipleChoisePosebility>();
	
	public MultipleChoise(QuizMaster creator) {
		super(creator);
	}
	
	@SuppressWarnings("unused")
	private MultipleChoise() {} //private constructor for hibernate
	
	public String[] getValues() {
		String[] values;
		synchronized (possibilities) {
			values = new String[possibilities.size()];
			for(int i = 0; i < possibilities.size(); i++) {
				values[i] = possibilities.get(i).getValue();
			}
		}
		return values;	
	}
	
	public void addValue(String value) {
		MultipleChoisePosebility pos = new MultipleChoisePosebility();
		pos.setValue(value);
		pos.setQuestion(this);
		synchronized (possibilities) {
			possibilities.add(pos);
		}
		
	}
	
}
