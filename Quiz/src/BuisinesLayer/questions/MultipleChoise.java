/**
 * multiplechois question 
 */
package BuisinesLayer.questions;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.JoinColumnOrFormula;

import BuisinesLayer.QuizMaster;


@Entity
@DiscriminatorValue("MUCHOIS")
public class MultipleChoise extends Question{
	@OneToMany(mappedBy="question",fetch=FetchType.EAGER)
	@Cascade(value={CascadeType.SAVE_UPDATE,CascadeType.DELETE})
	private List<MultipleChoisePosebility> possibilities = new ArrayList<MultipleChoisePosebility>();
	
	public MultipleChoise(QuizMaster creator) {
		super(creator);
		// TODO Auto-generated constructor stub
	}
	
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
