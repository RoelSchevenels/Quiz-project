package BuisinesLayer.questions;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("VIDEO")
public class VideoQuestion extends MediaQuestion{

}
