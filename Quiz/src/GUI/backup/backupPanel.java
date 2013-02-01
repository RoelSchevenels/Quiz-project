/**
 * @author vrolijkx
 * een Jpannel om backups te regelen
 */
package GUI.backup;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.filechooser.FileFilter;

import com.jgoodies.binding.adapter.AbstractTableAdapter;
import com.jgoodies.common.collect.ArrayListModel;

import Util.SizeConversion;
import Util.backup.BackupInfo;
import Util.backup.Backup;

public class backupPanel extends JPanel{
	private final ArrayListModel<BackupInfo> backupInfoList = new ArrayListModel<BackupInfo>();
	private JTable infoTable;
	private JButton selectFileButton;
	private JButton searchFolderButton;
	private JButton executeButton;
	private JFileChooser filechooser;
	private BackupInfo currentBackupInfo;
	
	private JLabel backupDateLabel;
	private JLabel amountOfFilesLabel;
	private JLabel backupSizeLabel;
	private JLabel databasUserLabel;
	private JLabel databaseNameLabel;
	private JLabel databaseLocationLabel;
	private JLabel backupPathLabel;
	
	public backupPanel() {
		initComponents();
		buildContent();
	}
	
	private void initComponents() {
		filechooser = new JFileChooser();
		filechooser.addChoosableFileFilter(new quizBackupFilter());
		filechooser.addChoosableFileFilter(new standardBackupFilter());
		infoTable = new JTable(new backupInfoTableAdapter(backupInfoList));
		selectFileButton = new JButton(new selectFileAction("Select File"));
		searchFolderButton = new JButton(new searchFolderAction("Search Folder"));
		
		
		
	}
	
	private void buildContent() {
		//build the gui here
	}
		
	private void setCurrentBackup(BackupInfo b) {
		currentBackupInfo = b;
		backupDateLabel.setText(String.format("%1$te/%1$tm/%1$tY  %1$tH:%1$tM",
				b.getBackupDate()));
		amountOfFilesLabel.setText("" + b.getAmountOfFiles());
		backupSizeLabel.setText(SizeConversion.makeReadable(b.getPath().length()));
		databasUserLabel.setText(b.getDatabaseUser());
		databaseNameLabel.setText(b.getDatabaseName());
		databaseLocationLabel.setText(b.getDatabaseLocation().getAbsolutePath());
		backupPathLabel.setText(b.getPath().getAbsolutePath());
	}
		
	
	@SuppressWarnings("serial")
 	private class backupInfoTableAdapter extends AbstractTableAdapter<BackupInfo> {

		public backupInfoTableAdapter(ArrayListModel<BackupInfo> info) {
			super(info,new String[] {"Datum","Database naam","Grootte"});
		}
		
		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			BackupInfo info = getRow(rowIndex);
			if(columnIndex == 0) {
				return String.format( "%1$te/%1$tm/%1$tY om %1$tH:%1$tM:%1$tS", info.getBackupDate());
			} else if(columnIndex == 2) {
				return info.getDatabaseName();
			} else if(columnIndex == 3) {
				return SizeConversion.makeReadable(info.getPath().length());
			} else {
				return null;
			}
		}
		
	}
	
	private class restoreAction extends AbstractAction {

		@Override
		public void actionPerformed(ActionEvent e) {
			if(currentBackupInfo != null) {
				enabled = false;
				try {
					Backup.RestorBackup(currentBackupInfo.getPath());
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				enabled = true;
			}
			
		}
		
	}
	
	@SuppressWarnings("serial")
	private class selectFileAction extends AbstractAction {

		public selectFileAction(String name) {
			super(name);
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			filechooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			if(filechooser.showDialog(backupPanel.this, "Kies") == JFileChooser.APPROVE_OPTION) {
				try {
					currentBackupInfo = new BackupInfo(filechooser.getSelectedFile());
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			
		}	
	}
	
	@SuppressWarnings("serial")
	private  class searchFolderAction extends AbstractAction {

		public searchFolderAction(String name) {
			super(name);
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			filechooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			backupInfoList.clear();
			if(filechooser.showDialog(backupPanel.this, "Kies") == JFileChooser.APPROVE_OPTION) {
				for(BackupInfo b : BackupInfo.searchBackups(filechooser.getSelectedFile(),true,true)) {
					backupInfoList.add(b);
				}
			}
			
			
		}	
	}
	
	private class standardBackupFilter extends FileFilter {

		@Override
		public boolean accept(File f) {
			if(f.isFile() && Backup.isValidBackup(f)) {
				return true;
			} else {
				return false;
			}
		}

		@Override
		public String getDescription() {
			return "All backup files";
		}	
	}
	
	private class quizBackupFilter extends FileFilter {

		@Override
		public boolean accept(File f) {
			if(f.isFile() && f.getName().toLowerCase().contains(".quiz")) {
				return Backup.isValidBackup(f);
			} else {
				return false;
			}
		}

		@Override
		public String getDescription() {
			return ".quiz backup files";
		}	
	}
	
	
}
