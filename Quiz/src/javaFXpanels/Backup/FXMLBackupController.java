/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package javaFXpanels.Backup;

import Util.SizeConversion;
import Util.backup.Backup;
import Util.backup.BackupInfo;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javaFXtasks.backup.backupTask;
import javaFXtasks.backup.searchBackupTask;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.util.Callback;

/**
 * FXML Controller class
 * voor het controleren van de gui
 * @see FXMLtest.fxml
 * @author vrolijkx
 */
public class FXMLBackupController implements Initializable {
    @FXML
    private AnchorPane backupAnchor;
    @FXML
    private ProgressIndicator progressIndicator;
    @FXML
    private Button restoreButton;
    @FXML
    private Button selectFileButton;
    @FXML
    private Button createBackupButton;
    @FXML
    private Button searchFolderButton;
    @FXML
    private TableView<BackupInfo> table;
    private ObservableList<BackupInfo> info;
    private BackupInfo selectedBackupinfo;
    private FileChooser fileChooser;
    private DirectoryChooser directoryChooser;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        progressIndicator.setVisible(false);
        progressIndicator.setProgress(-0.1);
        
        //resizelisteners toevoegen voor de progrees indicator in het midden te houden
        backupAnchor.widthProperty().addListener(new ChangeListener<Number>(){
            @Override
            public void changed(ObservableValue<? extends Number> ov, Number t, Number t1) {
                centerprogress();
            }
        });
        backupAnchor.heightProperty().addListener(new ChangeListener<Number>() {

            @Override
            public void changed(ObservableValue<? extends Number> ov, Number t, Number t1) {
                centerprogress();
            }
        });
        
        initTable();
        centerprogress();
    }  
    
    private void initTable() {
        info = FXCollections.observableArrayList();
        
        table.setEditable(false);
        
        TableColumn<BackupInfo,String> backupDateColumn = new TableColumn<BackupInfo,String>("Datum");
        TableColumn<BackupInfo,String> databseNameColumn = new TableColumn<BackupInfo,String>("Database");
        TableColumn<BackupInfo,String> sizeColumn = new TableColumn<BackupInfo,String>("Size");
        
        //breete aanpassen
        backupDateColumn.setPrefWidth(400);
        databseNameColumn.setPrefWidth(800);
        
        //de backupinfo property's aan de collomen binden
        backupDateColumn.setCellValueFactory(new Callback<CellDataFeatures<BackupInfo, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(CellDataFeatures<BackupInfo, String> info) {
            BackupInfo i = info.getValue();
            StringProperty p = new SimpleStringProperty(String.format("%1$te/%1$tm/%1$tY om %1$tH:%1$tM:%1$tS", i.getBackupDate()));
            return p;
            }
        });
        
        databseNameColumn.setCellValueFactory(new Callback<CellDataFeatures<BackupInfo, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(CellDataFeatures<BackupInfo, String> info) {
            BackupInfo i = info.getValue();
            StringProperty p = new SimpleStringProperty(i.getDatabaseName());
            return p;
            }
        });
        
        sizeColumn.setCellValueFactory( new Callback<CellDataFeatures<BackupInfo, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(CellDataFeatures<BackupInfo, String> info) {
            BackupInfo i = info.getValue();
            StringProperty p = new SimpleStringProperty(SizeConversion.makeReadable(i.getPath().length()));
            return p;
            }
         });
        
        
        table.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        table.getColumns().addAll(backupDateColumn,databseNameColumn,sizeColumn);
        table.setItems(info);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        
    }
    
    private void centerprogress() {
        double width  = (backupAnchor.getWidth() - progressIndicator.getWidth()) / 2; //x plaats progress
        double height = (backupAnchor.getHeight() - progressIndicator.getHeight()) /2; //y plaats progress
        progressIndicator.relocate(width, height);
    }
    
    private void setCurrentBackupInfo(BackupInfo b) {
        this.selectedBackupinfo = b;
        this.restoreButton.setDisable(false);
        //Todo backupInfo weergeven
    
    }
    
    private void showMessage(String message) {
        System.out.println("message " + message);
     };
    
    @SuppressWarnings("unchecked")
	@FXML 
    private void restore() {
        
        progressIndicator.setVisible(true);
        centerprogress();
        //create the backup task
        backupTask task = new backupTask(this.selectedBackupinfo.getPath());
        progressIndicator.progressProperty().bind(task.progressProperty());
        restoreButton.setDisable(true);
        
        task.setOnSucceeded(new EventHandler<Event>() {
            @Override
            public void handle(Event t) {
                progressIndicator.progressProperty().unbind();
                progressIndicator.setProgress(-1.0);
                progressIndicator.setVisible(false);
            }
        });
        
        task.setOnFailed(new EventHandler() {

            @Override
            public void handle(Event t) {
                progressIndicator.progressProperty().unbind();
                progressIndicator.setProgress(-1.0);
                progressIndicator.setVisible(false);
                showMessage("terugzetten van backup mislukt");
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
        
        File f = directoryChooser.showDialog(null);
        if(f == null || !f.isDirectory()) {
            return;
        }
        centerprogress();
        progressIndicator.setProgress(-1.0);
        progressIndicator.setVisible(true);
        restoreButton.setDisable(true);
        
        
        //de searchFolderTask aanmaken
        final searchBackupTask task = new searchBackupTask(f);
        
        task.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent t) {
                if(task.getBackupInfo() != null) {
                    info.clear();
                    info.addAll(task.getBackupInfo());
                }
                progressIndicator.setVisible(false);
                
                
            }
        });
        
        task.setOnFailed(new EventHandler<WorkerStateEvent>() {

            @Override
            public void handle(WorkerStateEvent t) {
                progressIndicator.setVisible(false);
                showMessage("Error when searching for backups");
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
       fileChooser.getExtensionFilters().add(ext);
       restoreButton.setDisable(true);
       
       File f = fileChooser.showOpenDialog(null);
       if(f!=null && f.exists()) { //niet geanuleerd
           if(Backup.isValidBackup(f)) {
               info.clear();
               try {
                   b = new BackupInfo(f);
                   info.add(b);
                   table.getSelectionModel().select(b);
               } catch (Exception e) {
                   showMessage("Problem getting backup Info");
               }
           } else {
               showMessage("Geen geldige backupFile");
           }
       }
    }
             
    @FXML
    private void createBackup() {
        String name;
        File dir;
        
        if(fileChooser == null) {
            fileChooser = new FileChooser();   
       }
       
       fileChooser.setTitle("Kies een backup");
       FileChooser.ExtensionFilter ext = new FileChooser.ExtensionFilter("backup files *.quiz", "*.quiz");
       fileChooser.getExtensionFilters().add(ext);
       restoreButton.setDisable(true);
       
       File f = fileChooser.showSaveDialog(null);
       if(f == null) {
           return;
       }
       
       name = f.getName();
       dir = f.getParentFile();
           
       if(!dir.isDirectory()) {
           showMessage("Geen geldige backupLocatie");
           return;
       }
       progressIndicator.setVisible(true);
       centerprogress();
       
       backupTask task = new backupTask(dir,name);
       //mss een method voor maken om herhaling te voorkomen
       progressIndicator.progressProperty().bind(task.progressProperty());
       
       task.setOnSucceeded(new EventHandler() {
            @Override
            public void handle(Event t) {
                progressIndicator.progressProperty().unbind();
                progressIndicator.setProgress(-1.0);
                progressIndicator.setVisible(false);
            }
        });
        
       task.setOnFailed(new EventHandler() {
            @Override
            public void handle(Event t) {
                progressIndicator.progressProperty().unbind();
                progressIndicator.setProgress(-1.0);
                progressIndicator.setVisible(false);
                showMessage("maken van backup mislukt");
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
    
    }

