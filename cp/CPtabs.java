
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
/*
 * Copyright (c) 1995 - 2008 Sun Microsystems, Inc.  All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Sun Microsystems nor the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */ 

package imogenart.cp;

/**
 * At the moment, CPtabs and CoulsonPlotPrintable uses default encoded colours. 
 * User can save colours to a file and use that.
 *   
 */




import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;

public class CPtabs extends JPanel 
	implements ActionListener,
	FocusListener,//if you change colors
	Serializable,
	ComponentListener, //when window is moved
	WindowListener //so that you can store things when window closes
{
	
	private static boolean printok = false;
	private String name;
	protected final String README = "Resources/README_CPG";//README_CPG per release displayed under Help
	//put directory and file into package application CPG.app/Contents/Resources/Java/Resources/README_CPG alongside jar file
	
	private JPanel piePanel;
	private JPanel Panelj= new JPanel();
	private JLabel colorfileLabel = new JLabel();
	//private boolean pref=true;//use for use default settings in ShowTable...
	//private int numWindows;//count so that you don't quit after closing (or opening a dialog)
	private static JFrame frame;
	
    private JButton openButton;
    private JButton colourButton,defaultButton,fileButton;
    private JTextArea log;
    private JFileChooser fc;
    private int RGBcolours;//Max no. colours allowed - we set this to 20 in CPGcolours.java
    protected static String colourFileName;//ACTUALLY used ="imogenart/cp/colors.txt";//initially not set, to use default array in ArcCanvas
    protected static String customFileName;//keep hold of user colours=".cpp/colors.txt";//need a default file to store custom colours
    /**
     * THIS FILE is used for custom colours: if in use only custom colours will be displayed, 
     * if not then 15 colours are hard coded in ArcCanvas
     */
 
    private int countcolr=0;
    public static boolean useMyColours=false;//initial setting, serialzied
    public static int numwindows = 0;
    private ColorPicker ColPik = null;
    private static String lastlog = "log0.txt";//saved between sessions so replaced by store/load
    
    private final String defaultpath = "~";
	/* Items to serialize */
	static String gowindowfile = ".cpg/cptabs";//secret file to store stuff
	private static String currecpath = "~";//store path to record from last time... (see store()) .. this is default location (/Users home directory)

	private static Point oldlocation=new Point(10,20);//serialize
	private static Dimension oldscreensize=new Dimension(500,275);//permanent wide, high:: Set overarching frame
	//Size actually depends on the colours window (tab) see maketextpanel
	
	//prefs are initialized statically... but then work with prefkeys, prefs
	private static boolean prefasktoclose = true;//remember you asked it to close (FinderIntegration for Mac only)
	private static boolean prefshowlog = true;//Ver 1.1 Patch1 set to show by default
	private static boolean prefrestorecolors = false;//for ColorPicker and CPP (FinderIntegration for Mac only)
	private static boolean prefrestoredefaults = false;//for ShowTable settings (FinderIntegration for Mac only)

	private boolean ShowTableusedefaults = false;
	
	private static Hashtable prefs = new Hashtable();//serialized
	private Hashtable prefkeys = new Hashtable(); 
	private Hashtable preflegends = new Hashtable(); 
	
	private CPGcolors CPGcolors = new CPGcolors();//set up colors once.
    FileMenu menubar = new FileMenu();
    private QuitConfirmJDialog QuitConfirmJDialog=null;
    
	public String getCurrentPath(){ return currecpath; }//for save file --- use same location as the open box... another class
	
	public void addwindow(){
		numwindows++;
	}
	public void removewindow(){
		numwindows--;
	}
	/** Put new prefs here
	 * 
	 * @return
	 */
	public Hashtable [] getPrefs(){//called by DoPrefs
		p("Called getPrefs currently have "+prefs.size()+" ie "+prefs);
		return new Hashtable [] { prefkeys, prefs, preflegends };
	}

	public void setPrefs(Hashtable prefsfromDoPrefs){//called by DoPrefs to get the prefs to take
		p("setPrefs from doPrefs, in CPtabs "+prefsfromDoPrefs.size()+" NEW PREFS: "+prefsfromDoPrefs);
		for(Enumeration e = prefkeys.elements();e.hasMoreElements();){
			String key = (String)e.nextElement();
			prefs.put(key, prefsfromDoPrefs.get(key));
			p("Prefs NOW has "+(Boolean) prefs.get(key));
			if(key.equals("ask2close")) 
				prefasktoclose = (Boolean) prefs.get(key);
			else if(key.equals("restoreColors")) useMyColours =  (Boolean) prefs.get(key);					
			/*else if(key.equals("customFileName")) {
				customFileName = (String) prefs.get(key);
				colourFileName = (String) prefs.get(key);//set this for now but see below
			}*/
			else if(key.equals("restoreDefaults")) {
				CPGcolors.resetDefaults();
				prefrestoredefaults =  (Boolean) prefs.get(key);//for ShowTable settings (FinderIntegration for Mac only)
				changePieDisplay();
			}
			else if(key.equals("showLog"))
				prefshowlog = (Boolean) prefs.get(key);
			p("setPrefs fromDoPrefs "+prefsfromDoPrefs.get(key)+" for key "+key);
		}
		if(useMyColours) {
			//CPGcolors.setColors(customFileName);
			colourFileName = customFileName;
		}
		else if(!useMyColours) {
			colourFileName = null;//use defaults
			//CPGcolors.setColors(null);
		}
	}
	
	private void setupPrefs(){
		p("START setupPrefs");
		//keep KEYS in the order you want them to appear in Prefs (see doPrefs)
		prefkeys.put(0,"ask2close");
		prefkeys.put(1,"showLog");
		//prefkeys.put(2,"restoreColors");//useMyColours and use customFileName (not as a pref)?
		//prefkeys.put(3,"restoreDefaults");
		//prefkeys.put(4,"customFileName");

		for(Enumeration e = prefkeys.elements();e.hasMoreElements();){
			String key = (String)e.nextElement();
			if(prefs.containsKey(key)){
				log("Saved preference "+key+" "+prefs.get(key));
				p("Value of "+key+" is "+prefs.get(key));
				if(key.equals("ask2close")) prefasktoclose = (Boolean) prefs.get(key);
				if(key.equals("restoreColors")) prefrestorecolors = (Boolean) prefs.get(key);
				if(key.equals("restoreDefaults")) prefrestoredefaults = (Boolean) prefs.get(key);
				if(key.equals("showLog")) prefshowlog = (Boolean) prefs.get(key);
			}//already set from load() deserialization
			else{
				//p("setupPrefs "+key);
				if(key.equals("ask2close")) prefs.put(key, prefasktoclose);//remember you asked it to close (FinderIntegration for Mac only)
				else if (key.equals("restoreColors")) prefs.put(key, prefrestorecolors);//for ColorPicker and CPP (FinderIntegration for Mac only)
				else if (key.equals("restoreDefaults")) prefs.put(key, prefrestoredefaults);//for ShowTable settings (FinderIntegration for Mac only)
				//else if (key.equals("customFileName") && customFileName != null) prefs.put(key, customFileName);//for ShowTable settings (FinderIntegration for Mac only)
				//else if (key.equals("customFileName") && customFileName == null) prefs.put(key, "(none)");//for ShowTable settings (FinderIntegration for Mac only)
				else if (key.equals("showLog")) prefs.put(key, prefshowlog);
				log("Default preference "+key+" "+prefs.get(key));
			}
		}
		
		preflegends.put("showLog"," Show Log (tab)");
		preflegends.put("ask2close"," Ask before Quit");
		preflegends.put("restoreColors"," Restore default colors");
		preflegends.put("restoreDefaults"," Restore default settings (for graphic)");//in ShowTable
		preflegends.put("customFileName"," Custom color file (for graphic)");//in ShowTable
		//p("Prefs "+prefs);
		//p("Pref legends "+preflegends);
		//p("theother one "+prefkeys);
	}
	
    public CPtabs() {
        super(new GridLayout(1, 1));
    	if(currecpath.equals(defaultpath)){//~
    		currecpath = (getPath4OS());
    		//System.out.println("PATH: "+currecpath);
    	}
        //do this early
    }
    //separate constructor from grabbing stored prefs and so on
    public void carryon(){
        makelog();
        p("In CPtabs constructor: prefs: "+prefs);
        setupPrefs();//first time run, use hard coded defaults
        
		Panelj.setBackground(Color.white);
		 
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setForeground(Color.black);
        
        ImageIcon icon = createImageIcon("images/middle.gif");//don't like image --- can put one in later
        
        //checkscreensize();
        
        JComponent panel1 = makeTextPanel("Panel #1");
        tabbedPane.addTab("Coulson Plot", icon, panel1,
                "Please select a file");
        tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);
        
        JComponent panel3 = makeTextPanel("Colors");//this changes the default size!!!
        //panel3.setSize(oldscreensize);
        tabbedPane.addTab("Colors", icon, panel3,
                "");
        tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);

        JComponent panel4 = makeTextPanel("Help");//this one has to go last or else the log won't record
        tabbedPane.addTab("Help", icon, panel4,
                "");
        tabbedPane.setMnemonicAt(2, KeyEvent.VK_3);
        p("ABOUT TO MAKE LOG: showlog? "+prefshowlog);
        JComponent panel2 = makeTextPanel("Log");//this one has to go last or else the log won't record
        // IF you don't bother with log comment out the next 2 lines...
        if(prefshowlog){//so you need to close and reopen to action the prefshowlog
        	tabbedPane.addTab("Log", icon, panel2,"");
        	tabbedPane.setMnemonicAt(3, KeyEvent.VK_4);
        }
       //Add the tabbed pane to this panel.
        add(tabbedPane);
        //this.setSize(oldscreensize);
        //The following line enables to use scrolling tabs.
        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        
        menubar.addCPtabs(this);
        addwindow();
    }
    
    public JFrame frame(){ return frame; }
       
    public void makelog() {
        //Create the log first, because the action listeners
        //need to refer to it.
        log = new JTextArea(5,20);///////// this seems to control the size of the window
        log.setMargin(new Insets(5,5,5,5));
        log.setEditable(false);
        log.setText("Recording your actions...\n");
    }
    
   private void checkscreensize(){
    	//reset screensize if screen too big...
    	if(this.isShowing()){
    	   	Dimension s = Toolkit.getDefaultToolkit().getScreenSize();
    	    if(oldscreensize.width>(s.width-50)) oldscreensize.width = s.width - 50; 
    	    if(oldscreensize.height>(s.height-100)) oldscreensize.height = s.height -100;
    	    p("checkscreensize biggest dimension now "+oldscreensize);
    	}
    }
    
    /* return the JPanels for each tab
     * 
     */
    protected JComponent makeTextPanel(String text) {
        JPanel panel = new JPanel(false);
        JScrollPane logScrollPane = new JScrollPane(log);
        logScrollPane.setBackground(Color.white);
        //checkscreensize();//can't be too big
       // panel.setSize(oldscreensize);//reset if too big (above method)
        panel.setBackground(Color.white);
        //panel.setLayout(new GridLayout(1, 1));//in order to see all items
        
        if(text == "Panel #1"){
         //Create a file chooser
            fc = new JFileChooser(new File (currecpath));
    		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
            openButton = new JButton("Open Data File...");
            openButton.addActionListener(this);
            
    	   JPanel InputPanel1 = new JPanel ();//for open file button
             InputPanel1.setBackground(Color.white);
              InputPanel1.add(openButton);
            JLabel label = new JLabel("Select Input File");
            InputPanel1.add (label, BorderLayout.CENTER);
                       
            panel.add(InputPanel1, BorderLayout.SOUTH);
            ArcCanvas Drawing = setPie (70,50,5);//Pie diam, Canvas size, Num segs
            ArcCanvas Drawing1 = setPie (70,50,5);//Pie diam, Canvas size, Num segs
            int array[] = {1,0,0,1,0};
            ArcCanvas Drawing2 = setPie (70,50,array,5);//Pie diam, Canvas size, Num segs
                      /* this next part makes a canvas with a circle picture on it *//**
 
            /* Now the pie is drawn */
       	   JPanel InputPanel;//these two for pie diagram
	          InputPanel = new JPanel ();
	          	InputPanel.setBackground(Color.white);
	          	InputPanel.setLayout (new FlowLayout ());  
	          	
	          	InputPanel.add (Drawing, BorderLayout.CENTER);
	          	InputPanel.add (Drawing1, BorderLayout.CENTER);
	          	InputPanel.add (Drawing2, BorderLayout.CENTER);
	          	panel.add(InputPanel, BorderLayout.SOUTH);
        }
	    else if (text == "Colors"){//Colours TAB
	    	piePanel = new JPanel();//to get nice button
	    	piePanel.setBackground(Color.white);
	        //RGBcolours
	        //p("Use my colours "+useMyColours);
	        if(colourFileName!=null){
	        	File f = new File(colourFileName);//initially unset - use hard coded defaults
	        	if(f.isFile())log ("Current color file is "+colourFileName);
	        }
	        else log("Default colours used (shown in colors panel)");
	        
	        	
	        colourButton = new JButton("New");
	        fileButton = new JButton("Open");
	        defaultButton = new JButton("Default");
	        colourButton.addActionListener(this);
            fileButton.addActionListener(this);
            defaultButton.addActionListener(this);
	              
            JPanel j1 = new JPanel();
        	//j1.setLayout(new GridLayout(1,0));
        	j1.setBackground(Color.white);
         	j1.add(defaultButton);
          	j1.add(colourButton);
        	j1.add(fileButton);
        	j1.add(colorfileLabel);
 	        panel.add(j1);//buttons
	        panel.add(piePanel);//colours,FlowLayout
	        /* Put up initial pie display */
	        changePieDisplay();//adds to pie panel

	    }
	    else if (text == "Log"){
            JLabel filler = new JLabel("Log of actions, debugging");
            filler.setHorizontalAlignment(JLabel.CENTER);
            panel.setLayout(new BorderLayout());
            panel.add(filler,BorderLayout.NORTH);
            panel.add(logScrollPane,BorderLayout.CENTER);  
            p("ADDED log");
        }
	    else if (text == "Help"){
            JLabel filler = new JLabel("How to and information");
            panel.setLayout(new BorderLayout());
            JTextArea ta = new JTextArea();
            SwingFileThings sft = new SwingFileThings();
            File f = new File(README);
            String path = f.getAbsolutePath();
             String filecontents = sft.readFiletoString(path); //works on Mac and Ubuntu
             Vector vv = sft.readFiletoVector(path, "");//ok for Mac
            p("filecontents (see below) and vv is "+vv);
            if(filecontents==null && vv!=null){
            	for(int i = 0; i<vv.size(); i++){
            			filecontents=filecontents+vv.elementAt(i)+"\n";
            	}
            }
            p("_________read me___________");
            p("I am at "+f.getAbsolutePath());
            p(filecontents);
            p("_________read me end___________");
            if(filecontents.endsWith("null"))
            	filecontents = filecontents.substring(0,filecontents.length()-5);
            ta.setText("\n"+
            		filecontents);
            ta.setLineWrap(true);
            ta.setWrapStyleWord(true);
            ta.setEditable(false);
            ta.setCaretPosition(0);//put it at the TOP not the end
            JScrollPane pane = new JScrollPane(ta);
            filler.setHorizontalAlignment(JLabel.CENTER);
            panel.add(filler, BorderLayout.NORTH);
            panel.add(pane,BorderLayout.CENTER);  
            p("ADDED readme");
        }
       // panel.setSize(oldscreensize);
        return panel;
   }
        
    private ArcCanvas setPie(int Canvassquare, int ArcDiam, int Segs){
        /* this next part makes a canvas with a multicoloured pie picture on it */
   	     ArcCanvas Drawing;
         Drawing = new ArcCanvas (CPGcolors.getColors());//default colours
         Drawing.setDiameter (ArcDiam);
         Drawing.setSegment (Segs);//nothing happens if not set
         Drawing.setSize(Canvassquare,Canvassquare);  //here, size of pie (half size of Drawing)
         /* Now the pie is drawn */
         return Drawing;	
    }
    
    private ArcCanvas setPie(int Canvassquare, int ArcDiam, int[] Segs, int singleColour){
        /* this next part makes a canvas with a pie picture on it -one color with empty segments */
    	/* used by CoulsonPlot also */
   	     ArcCanvas Drawing;
         Drawing = new ArcCanvas(CPGcolors.getColors());
         Drawing.setDiameter (ArcDiam); //here, size of pie (half size of Drawing)
         Drawing.setSegment (Segs, singleColour);//nothing happens if not set
         Drawing.setSize(Canvassquare,Canvassquare); //here, size of Drawing/canvas
         /* Now the pie is drawn */
         return Drawing;	
    }
    
    public void doOpen(){
    	fc.setCurrentDirectory(new File(currecpath));
        int returnVal = fc.showOpenDialog(CPtabs.this);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            currecpath = file.getAbsolutePath();//for store to remember for next time
            //This is where a real application would open the file.
            log.append("Opening: " + file.getName() + ".\n");
            log.append("Directory path: " + file.getPath() + ".\n");
            String Filepath = file.getPath();
            //DealWithFile showtable = new DealWithFile();
            //showtable.createAndShowGUI();
            final ShowTable showtable = new ShowTable(Filepath,log,colourFileName,ShowTableusedefaults, CPGcolors, this);
            showtable.createAndShowGUI(Filepath,log,colourFileName, ShowTableusedefaults, CPGcolors);
                            
        } else {
            log.append("Open command cancelled by user.\n");
        }
        log.setCaretPosition(log.getDocument().getLength());
    }
    
    public void actionPerformed(ActionEvent e) {

        //Handle open button action.
        if (e.getSource() == openButton) {
        	doOpen();
        } 
        if (e.getSource() == colourButton) {//NEW colours (may need to be last button set)
            doColors();
        } 
        if (e.getSource() == defaultButton) {//use default colours [ reset ]
            log.append("Using your colors (in file colors.txt)\n");                 
            log.setCaretPosition(log.getDocument().getLength());
            colourFileName=null;//set the colorFile so that defaults get used
            useMyColours=false;
            //can we get a redraw of the pies to reflect your colours?
            setColorfile(null);
            changePieDisplay();
        } 
        if (e.getSource() == fileButton) {//choose existing colours
            if (e.getSource() == fileButton) {
                int returnVal = fc.showOpenDialog(CPtabs.this);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = fc.getSelectedFile();
                    //This is where a real application would open the file.
                    log.append("Opening colours file: " + file.getName() + ".\n");
                    log.append("Directory path: " + file.getPath() + ".\n");
                    String Filepath = file.getPath();
                    customFileName=Filepath;
                    log.append("Colors file should be: " +customFileName+ ".\n");
                    log.setCaretPosition(log.getDocument().getLength());
                    setColorfile(Filepath);//also sets to prefs
                	changePieDisplay();
                } else {
                    p("Open command cancelled by user.\n");
                }
           }
       log.append("Using your colors (in file colors.txt)\n");                 
            log.setCaretPosition(log.getDocument().getLength());
            colourFileName=colourFileName;//set the colorFile so that it gets used
            useMyColours=true;
            //can we get a redraw of the pies to reflect your colours?
        } 
    }
    
    public void doColors(){
        countcolr=0;//reset when you open the colour picker
        log.append("Select color "+countcolr+".\n");        
        if(ColPik==null){ ColPik = new ColorPicker(log, customFileName, this);
        	ColPik.createAndShowGUI(this, oldscreensize);
        }
        ColPik.show();
        log.setCaretPosition(log.getDocument().getLength());
    }
    
    /** Returns an ImageIcon, or null if the path was invalid. */
    protected static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = CPtabs.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            //System.err.println("Couldn't find file: " + path);
            return null;
        }
    }
    
    private String getPath4OS(){
    	File f = new File (".cpg");
    	if(System.getProperty("os.name").contains("Mac")){
        	String whereami = f.getAbsolutePath();
        	if(whereami.contains("Contents/Resources/Java"))
        		return "../../../../";//Mac.app/Contents/Resources/Java/HEREisjar, .cpg etc
    	}
    	return "../";//WinLin
    }
    
     private void addFinderIntegration(CPtabs cp){
    	if(System.getProperty("os.name").contains("Mac")){
    		log("Mac so using FinderIntegration");
    		FinderIntegration fi = new FinderIntegration();
    		fi.execute();//initiate handlers
    		fi.setGoWindow(cp);
     	    System.setProperty("apple.laf.useScreenMenuBar", "true");//puts menu in top window not app
    	    System.setProperty("com.apple.mrj.application.apple.menu.about.name", "CPG");//puts correct name in main Mac menu bar
    	}
    }
    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from
     * the event dispatch thread.
     */
    public static void createAndShowGUI() {
    	
        System.setProperty("apple.laf.useScreenMenuBar", "true");//puts menu in top window not app
        //Create and set up the window.
        frame = new JFrame("Coulson Plot");
        //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //Add content to the window.
        CPtabs cp = new CPtabs();
        try{
			cp.load();//recover things from previous session
		}catch(Exception e){
			System.out.println("Could not load your screen location or filename because "+e);
			e.printStackTrace();///this stops the program so use to debug
		}
		cp.carryon();
		p("oldlocation during load: "+cp.oldlocation);
		cp.addFinderIntegration(cp);
        frame.add(cp, BorderLayout.CENTER);
        //frame.setResizable(false);- need to see the instructions!
        frame.setName("CPG");
        //cp.setLocations(frame);//works for screen location not SIZE
        
       // frame.setSize(oldscreensize);
        //oldlocation = new Point(oldlocation.x,oldlocation.y-22);
       // frame.setLocation(oldlocation);
        frame.addComponentListener(cp);//store old location
        frame.addWindowListener(cp);
        //frame.addKeyListener(cp);
               //centre it
        centerit(frame);
        //Display the window.
        //p("I am "+frame.getSize());
        //cp.checkscreensize();
        //p("after checking screen I am "+frame.getSize());
        frame.pack();
        frame.setSize(oldscreensize);
        frame.setJMenuBar(cp.menubar);//need this menu bar else it does nto show when wind
        //z.addCPtabs(cp);
        frame.setVisible(true);
    }
    
    public static void centerit(JFrame frame){
        //put it in the centre and give it a size
        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        Component [] comps = frame.getComponents();
        for(int i = 0; i<comps.length; i++){
        		comps[i].setMaximumSize(oldscreensize);
        		p("Size of "+comps[i]+" is "+comps[i].getSize());
        }
        frame.setSize(oldscreensize);//new java.awt.Dimension(366,116));
        frame.setLocation((screenSize.width-oldscreensize.width)/2,(screenSize.height-oldscreensize.height)/2);

   	
    }
        
    public void addNotify ()//overrides
    {
    	super.addNotify ();
    	//openButton.requestFocus();}//makes it glow
    	super.getRootPane().setDefaultButton(openButton);//makes it default
    }

    
    public void log(String message){
		log.append(message+"\n");log.setCaretPosition(log.getSelectionEnd());
		p(message);
    }
    
    private static void p(String message){
	if(printok) System.out.println("CPtabs::"+message);
    } 
    

   /* Items for serializable
    */
   private static void checkDir(){
	   printok = true;
		File f = new File(gowindowfile);
		if(!f.exists()){
			p("Dir .cpg does not exist");
			String dir = ".cpg";
			File d = new File(dir);
			if(!d.exists()){
				boolean success = (new File(dir)).mkdir();
				if(success) {
					System.out.println("Directory: "  + dir + " created");
					//log("Secret directory created for "+gowindowfile);
				} 
			}
			try{
				f.createNewFile(); p("Created "+gowindowfile);
			}catch(IOException e){System.out.println("Could not create "+gowindowfile+": " +e);}
		}
		printok = false;
   }

   void store(){//goWindow o, File f){//throws IOException
		checkDir();//create dir if it does not exist
		File f = new File(gowindowfile);
		if(!f.exists()){
			try{
			f.createNewFile();
			}catch(IOException e){p("Could not create file "+gowindowfile+" due to "+e);}
		}
		if(f.exists()){
		try{
			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream (f));            
			Vector goObjects = new Vector();
			//goObjects.add(oldscreensize);
			goObjects.add(oldlocation);
			goObjects.add(prefs);//includes useMyColours
			goObjects.add(currecpath);
			goObjects.add(customFileName);//not a pref
			goObjects.add(lastlog);//save running through them all and can delete the one 100 before
			p("store prefs "+prefs);
			p("store currectpath ? "+currecpath);
			p("store oldlocation "+oldlocation+" oldscreensize is "+oldscreensize);
			out.writeObject(goObjects);
			out.close();
		}catch(IOException e){log("Could not store your program parameters "+e);}
		}
		String templog = lastlog;//log0.txt is default
		if(templog==null)templog = "log0.txt";
		File l = new File(templog);
		while(l.exists()){//look for next number
			int a = Integer.parseInt(templog.substring(3,1));
			a++;
			if(a==99){
				log("You have 100 log files: overwriting early ones.\n");
				a = 0;//no more than 99, overwrite
			}
			templog = "log"+a+".txt";
			//by def this is next file so delete if there is one already
			if(l.exists()){
				if(l.isFile()){
					l.delete();
					log("Deleted log file "+templog+" (Rolling log system - 99 logs)\n");
				}else{
					String message = (templog+" exists but is not a file: please remove it so that logs can be saved!\n");
					System.out.println(message);log(message);
				}
			}
			if(!l.exists() &&!l.isFile()) try{
				l.createNewFile(); log("New file "+templog+" created."); lastlog = templog;
				String alllog = log.getText();
        		DoFileThings fio = new DoFileThings();
        		if(!fio.openSaveFile(templog)){
        			log("Can't open log file"+l+"\n");
        		}
        		else{	
        			String [] lines = alllog.split("\n");
        			for (int i = 0;i<lines.length;i++){
        				fio.writeLine(lines[i]);
        			}
        			fio.closeSaveFile();
        		}
			}catch(IOException e){log("Can't create file "+templog+" because: "+e);}
		}
	}
	
	static void load() throws IOException, ClassNotFoundException {
		File f = new File(gowindowfile);
		if(f.exists()){
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(gowindowfile));
			Vector goObjects = (Vector) in.readObject();
			/*if(goObjects.size()>0)
				oldscreensize = (Dimension) goObjects.elementAt(0);*/
			if(goObjects.size()>0)
				oldlocation = (Point) goObjects.elementAt(0);
			if(goObjects.size()>1)
				prefs = (Hashtable) goObjects.elementAt(1);//2);//MUST BE INITIALISES statically	
			if(goObjects.size()>2)
				currecpath = (String) goObjects.elementAt(2);//3);
			if(goObjects.size()>3)
				customFileName = (String) goObjects.elementAt(3);
			if(goObjects.size()>4)
				lastlog = (String) goObjects.elementAt(4);
			//if this is ported to another computer the file paths may not exist.
		}
		File fi = new File(currecpath);
		if(!fi.exists()) currecpath = "~";
		if(customFileName!=null){
			fi = new File(customFileName);
			if(!fi.exists())customFileName = null;
		}
		
		//p("Loaded "+goObjects.size()+" objects and path is "+currecpath);
		//p("oldlocation "+oldlocation+" oldscreensize is "+oldscreensize);
	}
	
	/*private void setLocations(JFrame f){
		p("Set locations "+oldlocation);
		p("Set size "+oldscreensize);
			f.setLocation(oldlocation.x+10, oldlocation.y+10);
			oldlocation = new Point(oldlocation.x+10, oldlocation.y+10);
			f.setSize(oldscreensize);
	}
	

	/* ComponentListener abstract method implementation */	
	public void componentMoved(ComponentEvent ce){
		p("componentMoved from "+oldlocation);
		if(frame.isVisible())
			oldlocation = new Point(super.getLocationOnScreen());//store position information for next reopen
		p("componentMoved so remember new location "+oldlocation);
	}
	
	public void componentHidden(ComponentEvent ce){
		//store current plugin displayed
	}
	
	public void componentResized(ComponentEvent ce){//don't remember resize
		//p("old screen size"+oldscreensize);
		//oldscreensize = new Dimension(this.getSize());
		//p("componentResized so remember new screen size"+oldscreensize);
	}
	
	public void componentShown(ComponentEvent ce){
	}
	/* End component methods storing window placement */
	
	/* New methods for WindowListener */
	/* Definately not listening in Mac */
	public void windowClosing(WindowEvent w){
		p("in windowClosing");
		//doClose();//hide and store
		store();
    }
	
	public void windowClosed(WindowEvent w){
		oldscreensize = new Dimension(super.getSize());
		p("in windowClosed "+frame.getName());
		//doClose();
		store();
		//reacts to clicking red button only, not quit
	}
	
	public void windowOpened(WindowEvent w){
		p("Window opened"+oldscreensize +" "+frame.getName());
		/**/
	}
	
	public void windowActivated(WindowEvent w){
		p("Window activates "+oldscreensize+" "+frame.getName());
		//frame().setJMenuBar(menubar);//grab back the menubar

	}
	
	public void windowDeactivated(WindowEvent w){
		p("windowDeactivated "+oldscreensize+" "+frame.getName());
		//parent.frame().setJMenuBar(parent.menubar);//give back the menubar
		store();
	}
	
	public void windowDeiconified(WindowEvent w){
		p("CPtabs deiconify");//only CPtabs comes back after minimising, the other windows just don't
	}
	
	public void windowIconified(WindowEvent w){
		p("CPtabs iconify");
	}
	
	public void quitProgram(){
		p("in quitProgram (goWindow)"+" "+frame.getName());
		store();//set back to old location
		//ASK
		if(prefasktoclose){
			if(QuitConfirmJDialog==null)
				QuitConfirmJDialog = new QuitConfirmJDialog(frame(), true, "Quit", this);
			QuitConfirmJDialog.setVisible(true);
			if(QuitConfirmJDialog.shallwequit())
				System.exit(0);
			else
				QuitConfirmJDialog = null;
		}
		else
			System.exit(0);
	}
	
	public void hideWindow() // dont close if no windows are open 
	{   
		p("in hideWindow"+" "+frame.getName());
		if(frame.isVisible()) numwindows--;
		frame.setVisible(false);//bye bye menu
	       
		p("Only "+numwindows+" windows left");
		//if Mac then FinderIntegration leaves a menu up...
	}
	
	public void doNew(){
		p("START doNew and frame is visible? "+frame.isVisible());
		if(!frame.isVisible()){
			p("Doing New"+" "+frame.getName());	
			showWindow();//you only need one but may have hidden this controller
			//setLocations(frame);
		}
	}
	
	public void showWindow(){
		p("in showWindow"+" "+frame.getName()+" visible "+frame.isVisible());
		if(!frame.isVisible()){
			addwindow();
			frame.setVisible(true);//must be this... for doNew to throw up the new window
		}
		p(numwindows+" winders");
	}
	
	public void doClose(){
		p("In doClose"+" "+frame.getName());
		hideWindow();
	}

	public boolean getPref_asktoClose(){//used by FinderIntegration - pref for reset defaults?
		//return prefasktoclose;
		return (Boolean)prefs.get("ask2close");
	}
	
	public void setPref_asktoClose(boolean pref){//only save if you say so
		//prefasktoclose=pref;
		prefs.put("ask2close", pref);
		prefasktoclose = pref;
	}
	
	public void restoreColors(boolean pref){
		prefs.put("restorecolors", pref);//use default colours
		prefrestorecolors = pref;
		//CODE to have orig colours
		if(pref==true){
			setColorfile(null);
		}
		else{
			setColorfile(customFileName);
		}
		changeColors();
	}
	
	public void restoreDefaults(boolean pref){
		prefs.put("restoredefaults", pref);
		prefrestoredefaults = pref;
		ShowTableusedefaults = pref;
		//tricky one this! Serialization stores your last settings
	}
	
	public void showLog(boolean pref){
		prefs.put("showLog", pref);
		prefshowlog = pref;
		//tricky one this! 
	}
	
	/** for colorpicker
	 * 
	 */
	public void setMycolors(boolean t){ 
		useMyColours = t; 
	}
	
	public void setColorfile(String currentpath){ 
		if(currentpath!=null){
			customFileName = currentpath; 
			colourFileName = currentpath; 
			useMyColours=true; 
			prefs.put("restorecolors", currentpath);
			CPGcolors.setColors(colourFileName);
			log("Set colors file to "+currentpath);
		}
		else{
			colourFileName = null;//use defaults
			CPGcolors.setColors(null);		
		}
	}

	/* Focus, color change */
	public void focusGained(FocusEvent fe){
		p("Hello did you change color: "+customFileName);
		changeColors();
	}
	
	private void changeColors(){
		if (customFileName == null)
			CPGcolors.setColors(null);
		//else if(colourFileName.equals(customFileName)){
			//do nowt as no change to colours
			//BUT may save to same file name...
		//}
		else{
			setColorfile(customFileName);//also sets to prefs
			CPGcolors.setColors(customFileName);
		}
		changePieDisplay();
	}
	
	public void focusLost(FocusEvent fe){}
	
	public void changePieDisplay(){
		p("Change Pie Display: colourFileName "+colourFileName);
		if(colourFileName != null){
			/* Put up filename */
			String filen = colourFileName;
			if(filen.contains("/")) 
				filen=filen.substring(filen.lastIndexOf("/")+1);
			colorfileLabel.setText(filen);
		}else
			colorfileLabel.setText(colourFileName);
       
		 RGBcolours = CPGcolors.maxcolors;//readColours(colourFileName);
	        /**uses defaults if nothing in file, from ArcCanvas
	         * If colourFileName not set then the hard coded colours are used
	         * If only one color set then other colors use hard coded...
	         */
		 //make sure colors were read:
		 //CPGcolors.setColors(colourFileName);
		 p(RGBcolours+" colors");
		 if(colourFileName == null)
		    log("Change Pie Display:  using default colours ("+RGBcolours+" colors)");
		 else
	        log("Change Pie Display:  using file "+colourFileName+" ("+RGBcolours+" colors)");
	        //b1.setLayout(new GridLayout(RGBcolours.size()+1,0));//12 colours to start with
	        piePanel.remove(Panelj);
	    	Panelj = new JPanel();
	    	Panelj.setBackground(Color.white);
	    	Panelj.setLayout(new GridLayout(0,1));
	    	//JScrollPane p = new JScrollPane(Panelj);
	    	//p.setBorder(null);
	    	//p.setBackground(Color.white);
	    	Panelj.setBackground(Color.white);

	        /* Draw current colours as pie pairs */
	    	// 2 rows if > 10 colors
	    	//max is 20 colors
	    	JPanel a = new JPanel();
	    	a.setBackground(Color.white);
	    	JPanel b = null;
	    	if(RGBcolours>10) b = new JPanel();
	    	if(b!=null)b.setBackground(Color.white);
	    	Panelj.add(a);
	    	if(b!=null)Panelj.add(b);
	        for (int i=0;i<RGBcolours;i++){//RGBcolours.size();i++){
	        	//set a pie this colour i
	        	//log("Pie "+i);//comes out as null after we have used up...
	        	int array1[] = {1,1,0,1,1};
	        	ArcCanvas Drawing3 = setPie (40,30,array1,i);//i is read in ArcCanvas, so need to set the one there, not here
	        	if(i<10)a.add(Drawing3);
	        	if(i>9)b.add(Drawing3);
	        }
	        piePanel.add(Panelj);//for subsequent redraws
	}
	
}