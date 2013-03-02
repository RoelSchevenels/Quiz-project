/**
f * dit is de controller voor het backupPanel / backupAnchor
 * wordt automatisch uit de fxml geladen
 * @author vrolijkx
 */
package javaFXpanels.Backup;

import Util.SizeConversion;
import Util.backup.Backup;
import Util.backup.BackupInfo;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


import javaFXpanels.MessageProvider.MessageProvider;
import javaFXtasks.BackupTask;
import javaFXtasks.SearchBackupTask;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import javafx.util.Callback;
import javafx.util.Duration;

/**
 * FXML Controller class
 * wordt aangemaakt bij het aanmaken van de gui
 * 
 * @see FXMLtest.fxml
 * @author vrolijkx
 */
public class BackupController implements Initializable {
    @FXML
    private AnchorPane backupAnchor;
    @FXML
    private AnchorPane backupInfoAnchor;
    @FXML
    private VBox verticalBox;
    @FXML
    private ProgressIndicator progressIndicator;
    @FXML
    private Label databaseNameLabel;
    @FXML
    private Label databaseUserLabel;
    @FXML
    private Label sizeLabel;
    @FXML
    private Label dateLabel;
    @FXML
    private Label amountOfFilesLabel;
    @FXML
    private Label locationLabel;
    @FXML
    private Label pervieusLocationLabel;
    @FXML
    private Button restoreButton;
    @FXML
    private Button selectFileButton;
    @FXML
    private Button createBackupButton;
    @FXML
    private Button searchFolderButton;
    
    //voor de tabel
    @FXML
    private TableView<BackupInfo> table;
    private ObservableList<BackupInfo> info;
    private BackupInfo selectedBackupinfo;
    private FileChooser fileChooser;
    private DirectoryChooser directoryChooser;
    
    //de animatie
    private Timeline showAnimation;
    private Timeline hideAnimation;
    private boolean backup_Expanded;
    
    //weergeven van messages
    private MessageProvider messageMaker;
    
    
    /**
     * Initializes the controller class.
     */
    @Override
     public void initialize(URL url, ResourceBundle rb) {
    	messageMaker = new MessageProvider(backupAnchor);
        progressIndicator.setVisible(false);
        progressIndicator.setProgress(-0.1);
        
        ChangeListener<Number> sizeListener = new ChangeListener<Number>(){
            @Override
            public void changed(ObservableValue<? extends Number> ov, Number t, Number t1) {
                centerprogress();
            }
        };
        
        //resizelisteners toevoegen voor de progrees indicator in het midden te houden
        backupAnchor.widthProperty().addListener(sizeListener);
        backupAnchor.heightProperty().addListener(sizeListener);
        
        initTable();
        centerprogress();
        initAnimation();  
    }  
    
    /**
     * de animatie voor backupInfo tevoorschijn
     * en te laten komen en te laten verdwijnen
     */
    private void initAnimation() {        
    	
    	//uitgeklapt zetten
        KeyValue kv1 = new KeyValue(backupInfoAnchor.prefHeightProperty(), 100.0, Interpolator.EASE_BOTH);
        KeyValue kv2 = new KeyValue(backupInfoAnchor.maxHeightProperty(), 100.0, Interpolator.LINEAR);
        KeyValue kv3 = new KeyValue(backupInfoAnchor.opacityProperty(),	 1, Interpolator.EASE_BOTH);
        
        //alles op null zetten
        KeyValue kv4 = new KeyValue(backupInfoAnchor.prefHeightProperty(), 0.0, Interpolator.EASE_BOTH);
        KeyValue kv5 = new KeyValue(backupInfoAnchor.maxHeightProperty(), 0.0, Interpolator.LINEAR);
        KeyValue kv6 = new KeyValue(backupInfoAnchor.opacityProperty(),	 0.0, Interpolator.EASE_BOTH);
        
        //keyframes voor openen
        KeyFrame kf1 = new KeyFrame(Duration.millis(500),kv1,kv2,kv3);
        KeyFrame kf2 = new KeyFrame(Duration.ZERO, kv4,kv5,kv6);
        
        //keyframes voor sluiten
        KeyFrame kf3 = new KeyFrame(Duration.millis(500),  kv4,kv5,kv6);
        KeyFrame kf4 = new KeyFrame(Duration.ZERO, kv1,kv2,kv3);
        
        showAnimation = new Timeline(kf2,kf1);
        hideAnimation = new Timeline(kf3,kf4);
        backup_Expanded = false;
        
        //de cliping region mooi instellen
        //zorgt voor een overflow hidden effect 
        Rectangle r = new Rectangle();
        r.heightProperty().bind(backupInfoAnchor.heightProperty());
        r.widthProperty().bind(backupInfoAnchor.widthProperty());
        backupInfoAnchor.setClip(r);
	}

    /**
     * kolomen aan de tabel toevoegen en de obserabel list
     * declareren en binden
     */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void initTable() {
        info = FXCollections.observableArrayList();
        
        table.setEditable(false);
        //om de tekst no content in table te verbergen
        table.setPlaceholder(new Pane());
        
        TableColumn<BackupInfo,String> backupDateColumn = new TableColumn<BackupInfo,String>("Datum");
        TableColumn<BackupInfo,String> databseNameColumn = new TableColumn<BackupInfo,String>("Database");
        TableColumn<BackupInfo,String> sizeColumn = new TableColumn<BackupInfo,String>("Size");
        
        //breete aanpassen
        backupDateColumn.setPrefWidth(400);
        databseNameColumn.setPrefWidth(600);
        
        //de backupinfo property's aan de collomen binden
        backupDateColumn.setCellValueFactory(new Callback<CellDataFeatures<BackupInfo, String>, ObservableValue<String>>() {
            @Override
			public ObservableValue<String> call(CellDataFeatures<BackupInfo, String> info) {
            BackupInfo i = info.getValue();
            StringProperty p = new SimpleStringProperty(String.format("%1$te/%1$tm/%1$tY om %1$tH:%1$tM:%1$tS", i.getBackupDate()));
            return p;
            }
        });
        
        databseNameColumn.setCellValueFactory(new Callback<CellDataFeatures<BackupInfo, String>, ObservableValue<String>>() {
            @Override
			public ObservableValue<String> call(CellDataFeatures<BackupInfo, String> info) {
            BackupInfo i = info.getValue();
            StringProperty p = new SimpleStringProperty(i.getDatabaseName());
            return p;
            }
        });
        
        sizeColumn.setCellValueFactory( new Callback<CellDataFeatures<BackupInfo, String>, ObservableValue<String>>() {
            @Override
			public ObservableValue<String> call(CellDataFeatures<BackupInfo, String> info) {
            BackupInfo i = info.getValue();
            StringProperty p = new SimpleStringProperty(SizeConversion.makeReadable(i.getPath().length()));
            return p;
            }
         });
        
        
        table.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        table.getSelectionModel().getSelectedCells()
        	 .addListener(new ListChangeListener() {

			@Override
			public void onChanged(Change c) {
				checkTableSelection();
				
			}
        	
        });
        
        
        table.getColumns().addAll(backupDateColumn,databseNameColumn,sizeColumn);
        table.setItems(info);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        
    }
    
    private void centerprogress() {
        double width  = (backupAnchor.getWidth() - progressIndicator.getWidth()) / 2; //x plaats progress
        double height = (backupAnchor.getHeight() - progressIndicator.getHeight()) /2; //y plaats progress
        progressIndicator.relocate(width, height);
    }
    
    private void hideCurrentBackup() {
    	if(backup_Expanded) {
    		hideAnimation.play();
    		restoreButton.setDisable(true);
    		backup_Expanded = false;
    	}
    	
    }
    
    private void setCurrentBackupInfo(BackupInfo b) {
        this.selectedBackupinfo = b;
        this.restoreButton.setDisable(false);
        this.backupInfoAnchor.setVisible(true);
        
        //zet de backup gegeven 
        dateLabel.setText(String.format("%1$te/%1$tm/%1$tY om %1$tH:%1$tM:%1$tS", b.getBackupDate()));
        databaseNameLabel.setText(b.getDatabaseName());
        databaseUserLabel.setText(b.getDatabaseUser());
        locationLabel.setText(b.getPath().toString());
        amountOfFilesLabel.setText("" + b.getAmountOfFiles());
        sizeLabel.setText(SizeConversion.makeReadable(b.getPath().length()));
        pervieusLocationLabel.setText(b.getDatabaseLocation().toString());
       
        
        if(!backup_Expanded) {
        	backup_Expanded = true;
        	showAnimation.play();
        } 
    }
       
    @SuppressWarnings("unchecked")
	@FXML 
    private void restore() {
        hideCurrentBackup();
        progressIndicator.setVisible(true);
        centerprogress();
        //create the backup task
        BackupTask task = new BackupTask(this.selectedBackupinfo.getPath());
        progressIndicator.progressProperty().bind(task.progressProperty());
        restoreButton.setDisable(true);
        
        task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent t) {
                progressIndicator.progressProperty().unbind();
                progressIndicator.setProgress(-1.0);
                progressIndicator.setVisible(false);
            }
        });
        
        task.setOnFailed(new EventHandler<WorkerStateEvent>() {

            @Override
            public void handle(WorkerStateEvent t) {
                progressIndicator.progressProperty().unbind();
                progressIndicator.setProgress(-1.0);
                progressIndicator.setVisible(false);
                messageMaker.showError("terugzetten van backup mislukt");
            }
        });
        
        ExecutorService ex = Executors.newSingleThreadExecutor();
        ex.execute(task);
        
    };
    
    @FXML
    private void searchFolder() {
        if(directoryChooser == null){
            directoryChooser = new DirectoryChooser();
            directoryChooser.setTitle("Kies folder om in te zoeken");
        }
        

        File f = directoryChooser.showDialog(getWindow()); 
        if(f == null || !f.isDirectory()) {
            return;
        }
        
        centerprogress();
        hideCurrentBackup();
        info.clear();
        progressIndicator.setProgress(-1.0);
        progressIndicator.setVisible(true);
        
        
        //de searchFolderTask aanmaken
        final SearchBackupTask task = new SearchBackupTask(f);
        
        task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent t) {
                if(task.getBackupInfo() != null) {
                    info.addAll(task.getBackupInfo());
                }
                progressIndicator.setVisible(false);
                
                
            }
        });
        
        task.setOnFailed(new EventHandler<WorkerStateEvent>() {

            @Override
            public void handle(WorkerStateEvent t) {
                progressIndicator.setVisible(false);
                messageMaker.showError("fout bij zoeken naar backups");
            }
        });
                
                
        ExecutorService ex = Executors.newSingleThreadExecutor();
        ex.execute(task);
    }
    
    @FXML
    private void selectFile() {
       BackupInfo b;
       if(fileChooser == null) {
            fileChooser = new FileChooser();   
       }
       
       fileChooser.setTitle("Kies een backup");
       FileChooser.ExtensionFilter ext = new FileChooser.ExtensionFilter("backup files *.quiz", "*.quiz");
       FileChooser.ExtensionFilter ext2 = new FileChooser.ExtensionFilter("Alle bestanden", "*");
       fileChooser.getExtensionFilters().addAll(ext,ext2);
       
       
       File f = fileChooser.showOpenDialog(getWindow());
       if(f!=null && f.exists()) { //niet geanuleerd
    	   hideCurrentBackup();
    	   info.clear();
           if(Backup.isValidBackup(f)) {
               try {
                   b = new BackupInfo(f);
                   info.add(b);
                   table.getSelectionModel().select(b);
               } catch (Exception e) {
                   messageMaker.showError("Probleem bij opvragen backup informatie");
               }
           } else {
        	   messageMaker.showError("Geen geldige backup bestand");
           }
       }
    }
             
    @SuppressWarnings("unchecked")
	@FXML
    private void createBackup() {
        String name;
        File dir;
        
        if(fileChooser == null) {
            fileChooser = new FileChooser();   
       }
       
       fileChooser.setTitle("Kies een locatie");
       FileChooser.ExtensionFilter ext = new FileChooser.ExtensionFilter("backup files *.quiz", "*.quiz");
       fileChooser.getExtensionFilters().add(ext);
       
       
       
       File f = fileChooser.showSaveDialog(getWindow());
       if(f == null) {
           return;
       }
       hideCurrentBackup();
       
       name = f.getName();
       dir = f.getParentFile();
           
       if(!dir.isDirectory()) {
    	   messageMaker.showWarning("Geen geldige backuplocatie");
           return;
       }
       progressIndicator.setVisible(true);
       centerprogress();
       
       BackupTask task = new BackupTask(dir,name);
       //mss een method voor maken om herhaling te voorkomen
       progressIndicator.progressProperty().bind(task.progressProperty());
       
       task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent t) {
                progressIndicator.progressProperty().unbind();
                progressIndicator.setProgress(-1.0);
                progressIndicator.setVisible(false);
            }
        });
        
       task.setOnFailed(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent t) {
                progressIndicator.progressProperty().unbind();
                progressIndicator.setProgress(-1.0);
                progressIndicator.setVisible(false);
                messageMaker.showError("maken van backup mislukt");
            }
        });
        
        ExecutorService ex = Executors.newSingleThreadExecutor();
        ex.execute(task);
       
    }
      
    @FXML
    private void checkTableSelection() {
        BackupInfo b = table.getSelectionModel().getSelectedItem();
        if(b != null) {
            this.setCurrentBackupInfo(b);
        }  
    };

    private Window getWindow(){
    	return backupAnchor.getScene().getWindow();
    }
    
    public AnchorPane getComponent() {
    	return this.backupAnchor;
    }
    
	public static BackupController getBackupPane() throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(BackupController.class.getResource("FXMLBackup.fxml"));
		loader.load();
		return loader.getController();
	}
}

