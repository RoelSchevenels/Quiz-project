package javaFXpanels.MessageProvider;

import javafx.beans.property.DoubleProperty;
import javafx.geometry.Pos;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.text.Text;


public class LoadingPane extends StackPane {
	private AnchorPane parrent;
	private ProgressIndicator progress;
	private Text loadText;

	public LoadingPane() {
		progress = new ProgressIndicator();
		progress.setProgress(-1);
		loadText = new Text("");

		VBox box = VBoxBuilder.create()
				.alignment(Pos.CENTER)
				.children(progress,loadText)
				.build();

		this.getChildren().add(box);
		this.setVisible(false);
		this.setAlignment(Pos.CENTER);
		this.setFocused(true);

		AnchorPane.setBottomAnchor(this, 0.0);
		AnchorPane.setTopAnchor(this, 0.0);
		AnchorPane.setTopAnchor(this, 0.0);
		AnchorPane.setBottomAnchor(this, 0.0);
		this.getStyleClass().add("loading-pane");

	}


	public void showLoading(String text,AnchorPane parrent) {
		this.loadText.setText(text);
		
		if(!this.isVisible() && this.parrent!=null  &&!this.parrent.getChildren().contains(this)) {
			parrent.getChildren().add(this);
			parrent.requestFocus();
			this.setVisible(true);
		}
	}

	public DoubleProperty getProgressProperty() {
		return progress.progressProperty();
	}

	public void hide() {
		if(parrent!=null) {
			parrent.getChildren().remove(this);
		}
		this.visibleProperty().set(false);
		this.setVisible(false);
	}
}
