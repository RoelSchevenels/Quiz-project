package GUI.player;

import java.io.IOException;
import javaFXpanels.MessageProvider.MessageProvider;

import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import Protocol.submits.TetrisSubmit;
/**
 * @author Roel
 */
public class TetrisPane extends AnchorPane {
	public TetrisPane()
	{
		this.getStylesheets().add(this.getClass().getResource("tetris-style.css").toString());
		build();
		System.out.println(this.getStylesheets().toString());
	}
	
	private void build()
	{
		Label instructions = new Label("←↓→ om te bewegen\n↑ om te roteren");
		instructions.setId("instructions");
		StackPane pane = new StackPane();
		pane.getChildren().add(instructions);
		getChildren().add(pane);
		setBottomAnchor(pane, 0.0);
		setLeftAnchor(pane, 0.0);
		setTopAnchor(pane,0.0);
		setRightAnchor(pane,0.0);		
		
		requestFocus();
		setFocusTraversable(true);
		setOnKeyPressed(new EventHandler<KeyEvent>(){
			public void handle(KeyEvent ke)
			{
				int code = ke.getCode().impl_getCode();
				if(code >= 37 && code <= 40) {
					char[] keys = {'l', 'u', 'r', 'd'};	// Left, up (rotate), right, down
					char dir = keys[code-37];
					
					try {
						//throw new IOException();
						new TetrisSubmit(dir).send();
					} catch (IOException e) {
						new MessageProvider(TetrisPane.this).showError("Kan geen verbinding maken met de server.");
					}
				}
			}
		});
	}
}
