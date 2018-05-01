package imogenart.cp;

/**
*
* Contact:
* hif22@cam.ac.uk
* 
* CoulsonPlotGenerator was created by Helen Imogen Field Copyright. 2010-
* 
* Please note all software is released under the Artistic Licence 2.0 until further notice.
* It may be freely distributed and copied providing our notes remain as part of the code.
* Note that under the terms of this licence, versions of classes that you use have to
* continue to work (with the GOtool) and must be made available to us.
* If you would like modifications made please contact me first 
* to see if the functions you require are already part of this package.
* 
*/

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;

public class FileMenu extends JMenuBar{
	
	/** Try to use one menu for all windows?
	 * 		goWindowFrame.setJMenuBar(initMenuBar());  
	 */
	
	private boolean printok = false;//Debug
	private void p(String s){if(printok) System.out.println("FileMenu:: "+s);}
	private JFrame thisFrame;
	private CPtabs thisCPtabs=null;
	private ShowTable thisShowTable=null;
	public printit thisCPGplot = null;//for CPGmenu since can't translate actions over
	//or have listeners... no they don't work

	public FileMenu(){
		//thisFrame = frame;
		inti();
	}
	
	public void addCPtabs(CPtabs cpt){ thisCPtabs = cpt; thisFrame = cpt.frame(); }
	public void addShowTable(ShowTable cpt){ thisShowTable = cpt; thisFrame = thisShowTable.frame(); }
	public void addPrintableCPG(printit cpt){ thisCPGplot = cpt; thisFrame = thisCPGplot; }
	
	
	private Object[][] fileItems =
	{
			//{"New", new Integer(KeyEvent.VK_N)},
			//{"Open", new Integer(KeyEvent.VK_O)},//Open a CPG source file
			//{null,null},
			{"Close", new Integer(KeyEvent.VK_W)},//Close the window
			{null,null},
			//{"Save", new Integer(KeyEvent.VK_S)},//Save a Figure
			//{"Save as", null},//ditto
	};
	
	private Object[][] editItems =
	{
			{"Color", new Integer(KeyEvent.VK_K)},//open the color picker
	};

	private Object[][] fileItems_Nonmac = {
		{"About", new Integer(KeyEvent.VK_A)},
			{null,null},
		{"Close", new Integer(KeyEvent.VK_W)},//Close the window
		{"Quit", new Integer(KeyEvent.VK_Q)},};//Quit
   Object[][] editItems_Nonmac = {
		{"Preferences", new Integer(KeyEvent.VK_R)},//open the color picker
		{null,null},
		{"Color", new Integer(KeyEvent.VK_K)},};//Quit

	private void inti(){
		

		JMenu menuFile = new JMenu("File");
		if(!System.getProperty("os.name").contains("Mac")){//Quit in File
			setupMenu(menuFile, fileItems_Nonmac);}
		else setupMenu(menuFile, fileItems);
		this.add(menuFile);
		
		JMenu menuEdit = new JMenu("Edit");
		if(!System.getProperty("os.name").contains("Mac")){//Prefs in Edit
			setupMenu(menuEdit, editItems_Nonmac);}
		else setupMenu(menuEdit, editItems);
		this.add(menuEdit);
	}
	
					
	public JMenu setupMenu (JMenu myMenu, Object[][] menuconfig){
		JMenuItem currentMenuItem;//remainder in use by FileMenu for the remaining CPG menus
		for (int i = 0;i < menuconfig.length;i++){
			if(menuconfig[i][0] != null){
				currentMenuItem = new JMenuItem();
				currentMenuItem.setText((String)menuconfig[i][0]);
				if(menuconfig[i][1] != null){
					int keyCode = ((Integer)menuconfig[i][1]).intValue();
					KeyStroke key = KeyStroke.getKeyStroke(keyCode,
						Toolkit.getDefaultToolkit().getMenuShortcutKeyMask());
					currentMenuItem.setAccelerator(key);
					String item = menuconfig[0].toString();
					if(thisCPGplot==null && item.startsWith("Save")){
						currentMenuItem.setEnabled(false);
					}
					if(thisCPtabs==null && item.startsWith("Color")){
						currentMenuItem.setEnabled(false);
					}

				}
				currentMenuItem.setEnabled(true);
				currentMenuItem.setActionCommand((String)menuconfig[i][0]);
				currentMenuItem.putClientProperty("window", thisFrame);
				currentMenuItem.addActionListener(actionListenerHandler);
				p("Added actionListenerHandler to "+menuconfig[0].toString());
				myMenu.add(currentMenuItem);
			}else{
				JSeparator sep = new JSeparator();
				myMenu.add(sep);
			}
				
		}
		return myMenu;//not for FileMenu when used for CPG first and second windows, but when used for printit for view menu
	}

/* What happens when you select from the menu, or use a tool button */
private ActionListener actionListenerHandler = new ActionListener(){
	public void actionPerformed(ActionEvent evt){
		p("HMmM "+evt);
		Object src = evt.getSource();
		if(src instanceof JMenuItem){
			String input = ((JMenuItem)src).getText();
			dispatchEvent(evt,input);
		}
		if(src instanceof JButton){
			String input = ((JButton)src).getText();
			dispatchEvent(evt,input);
		}
	}
};// end of dispatch event

	private void dispatchEvent(ActionEvent evt, String tag){//tag is TimeStamp or GO-line or MacOS
		//first, take focus
		//mainInternalFrame.requestFocus();
		((JMenuItem)evt.getSource()).getComponent().requestFocus();
		p("Start dispatchEvent for "+evt.getSource());
		p("Request from? "+((JMenuItem)evt.getSource()).getComponent());
		p("Tag "+tag);
			
		if(tag.compareTo("Color")==0){
		if(thisCPtabs!=null)	thisCPtabs.doColors();// need this to fire up doNew from anywhere
		if(thisShowTable!=null)	thisShowTable.parent.doColors();
			//newWindows incremented in init (to position as a tile)
		}
		/*if(tag.compareTo("New")==0){
			if(thisCPtabs!=null) thisCPtabs.doNew();//simply unhides
			else if(thisShowTable !=null) thisShowTable.parent.doNew();
			//evt.getSource().doNew();// need this to fire up doNew from anywhere
			//newWindows incremented in init (to position as a tile)
		}*///tbis not working
		/*if(tag.compareTo("Open")==0){
			if(thisCPtabs!=null)
				thisCPtabs.doOpen();
		}
		if(tag.compareTo("Save image")==0){
			p("Save to? "+evt.getSource());//.saveFile();
			
		}
		if(tag.compareTo("Save as")==0){
			p("Save to? "+evt.getSource());//.saveFileAs();
		}*/
		if(tag.compareTo("Close")==0){
			//don't want to close CPtabs tho... it doesn't reopen!
			if(thisShowTable!=null)
				thisShowTable.doClose();//ALLOW apple-w to work
		}
		if(tag.compareTo("Quit")==0){//for non-Mac
			if(thisCPtabs!=null) thisCPtabs.quitProgram();
			else if(thisShowTable !=null) thisShowTable.parent.quitProgram();
		}
		if(tag.compareTo("Preferences")==0){//for non-Mac
			if(thisCPtabs != null) new DoPrefs(thisCPtabs);
			else if(thisShowTable !=null) new DoPrefs(thisShowTable.parent);
		}
		if(tag.compareTo("About")==0){//for non-Mac
			new doAbout();
		}
		//printok = true;p("Menu--- "+tag+" connected to CPGplot? "+thisCPGplot);printok = false;
		//new methods for View, defined in CPGmenu, a bit scruffy to have a separate class
		if(tag.compareTo("Zoom in")==0){
			if(thisCPGplot!=null)	thisCPGplot.zoom(true);
		}
		else if(tag.compareTo("Zoom out")==0){
			if(thisCPGplot!=null)	thisCPGplot.zoom(false);
		}
		else if(tag.compareTo("Fit to window")==0){
			if(thisCPGplot != null) thisCPGplot.fittowindow();
		}
	}

}
