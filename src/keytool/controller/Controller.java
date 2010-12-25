package keytool.controller;
 
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;

import keytool.model.Model;
import keytool.model.ModelException;
import keytool.view.MainWindow;
import keytool.view.View;

/*
@startuml

[*] --> StateWait
StateWait --> StateSaving : menuSave
StateSaving --> StateWait : FOopen
StateSaving --> StateWait : FOcancel

StateWait --> StateOpening : menuOpen
StateOpening --> StateWait : FOopen
StateOpening --> StateWait : FOcancel

StateWait --> StateExporting : itemExport
StateExporting --> StateWait : FOopen
StateExporting --> StateWait : FOcancel

StateWait --> StateImporting : itemImport
StateImporting --> StateWait : FOopen
StateImporting --> StateWait : FOcancel

@enduml
*/
public class Controller {
  private View view;
  private Model model;
  private enum State {StateWAIT, StateSAVING, StateOPENING, StateIMPORTING, StateEXPORTING};
  private State state;
  
  public Controller(Model model, View view){
    this.view = view;
    this.model = model;
    this.state = State.StateWAIT;
    
    initMainWindowListener();
    refreshKeysList();
    refreshCertificateList();
    
    initFOWindowListener();
  }
 
  private void initMainWindowListener() {
	MainWindow mw = this.view.getMainWindow();
	mw.addItemOpenListener(new ItemOpenListener());
	mw.addItemSaveListener(new ItemSaveListener());
	mw.addItemSaveAsListener(new ItemSaveAsListener());
	mw.addItemQuitListener(new ItemQuitListener());
	mw.addBtnImportListener(new BtnImportListener());
	mw.addBtnExportListener(new BtnExportListener());
  }
  
  private void refreshKeysList() {
	  try {
		DefaultListModel list = this.model.getKeys();
		this.view.getMainWindow().setKeysList(list);
	} catch (ModelException e) {
		this.view.createErrorWIndow(e.getMessage());
	}
  }
  
  private void refreshCertificateList() {
	  try {
			DefaultListModel list = this.model.getCertificates();
			this.view.getMainWindow().setCertificatesList(list);
		} catch (ModelException e) {
			this.view.createErrorWIndow(e.getMessage());
		}
  }
 
  class ItemOpenListener implements ActionListener {
	    public void actionPerformed(ActionEvent e) {
	    	view.showFileOpenWindow();
	    }
	  }
  
  class ItemQuitListener implements ActionListener {
    public void actionPerformed(ActionEvent e) {
    	view.disposeAll();
    }
  }
  
  class ItemSaveListener implements ActionListener {
	    public void actionPerformed(ActionEvent e) {
	    	try {
				model.save();
			} catch (ModelException e1) {
				view.createErrorWIndow(e1.getMessage());
			}
	    }
	  }
  
  class ItemSaveAsListener implements ActionListener {
	    public void actionPerformed(ActionEvent e) {
	    	
	    }
	  }
  
  class BtnImportListener implements ActionListener {
    public void actionPerformed(ActionEvent e) {

    }
  }
  
  class BtnExportListener implements ActionListener {
    public void actionPerformed(ActionEvent e) {

    }
  }
  
  private void initFOWindowListener() {
	  this.view.getFileOpenWindow().addFOActionListener(new FOActionListener());
	  this.view.getFileOpenWindow().addFOWindowListener(new FOWindowListener());
  }
  
  class FOActionListener implements ActionListener {
	    public void actionPerformed(ActionEvent e) {
	        if (JFileChooser.APPROVE_SELECTION.equals(e.getActionCommand())) {
	            System.out.println("Ouverture !");
	        } else if (JFileChooser.CANCEL_SELECTION.equals(e.getActionCommand())) {
	            view.hideFileOpenWindow();
	        }
	    }
  }
  
  class FOWindowListener extends WindowAdapter {
	    public void windowClosing(WindowEvent e) {
	    	view.hideFileOpenWindow();
	    }
	  }
  
}