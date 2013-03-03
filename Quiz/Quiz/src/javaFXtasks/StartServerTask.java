package javaFXtasks;

import java.io.IOException;

import screenManger.ScreenManager;
import tetris.Game;

import network.AutoDiscoverServer;
import network.Server;
import javafx.concurrent.Task;
/**
 * @author Roel
 */
@SuppressWarnings("rawtypes")
public class StartServerTask extends Task {

	@Override
	protected Object call() throws IOException {
		int port = Server.getInstance().getPort();
		updateMessage("Server gestart op poort " + port);
		AutoDiscoverServer.getInstance().start();
		updateMessage("Autodiscovery Server gestart op poort " + AutoDiscoverServer.getInstance().getPort());
		Game.initAndGet(ScreenManager.getInstance().getMediaFrame().getDisplayPanel(true));
		return null;
	}
}
