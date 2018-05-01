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


	/*
	 * TablePrintDemo.java requires no other files.
	 */

	import java.awt.BorderLayout;
import java.awt.CheckboxGroup;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.table.AbstractTableModel;



	public class ShowTable extends JPanel 
	                            implements java.awt.event.ActionListener,
	                            ComponentListener, //when window is moved
	                        	WindowListener, //so that you can store things when window closes
	                            Serializable //remember last session
	                            {
	    private boolean DEBUG = false;
		boolean printok = false;//false if not debugging
		private void p(String a){if(printok) System.out.println("ShowTable::"+a);}

		private static JFrame frame;
		private static FileMenu menubar = new FileMenu();
		public static CPtabs parent;
       
		//private JPanel top = new JPanel();
		JScrollPane scrollPane;//top
		//foroptions private JPanel middle = new JPanel();
		private JPanel bottom = new JPanel();
		
	    private CPGcolors CPGcolors;
	    private JTable table;
	    private String path;//get this in constructor, and pass from CPtabs
	    private JTextArea log;//pass information to parent 
	    /** this String vector contains the table contents and is created by MyTableModel */
        HifStackString v = new HifStackString();//contains the table.... from the file, as is
        private JButton figButton;
        private JButton defaultButton;
        private JTextField fontField;
        private JTextField diamField;
        private JTextField gapField;
        private JTextField htField;
        private JTextField wdField;
        private JTextField colField;
        private JComboBox fontchoice = new JComboBox();//for fonts for labels (TextCanvas)
        private JComboBox fontchoice2 = new JComboBox();//for fonts for pie labels (ArcCanvas)
        private String[] fontdescription; //match TextCanvas font
        private String foundata="";

        private JPanel foroptions = new JPanel();//placeholder to hide/show options
        private JPanel blank = new JPanel();//swap out for foroptions to hide

        private JCheckBox check4vertlabels;
        private JCheckBox check4vertlabels2;
        private JCheckBox checkyellowbox;
        private JCheckBox checkcrossbox;
        private JCheckBox checkGap4row2;
        private JCheckBox checkGap4row1;
        private static String FONTCHOICE="";
        private static String FONTCHOICE2="";
        private static boolean ADDVERTLABELS=true;
        private static boolean ADD2ndVERTLABELS=true;
        private static boolean YELLOWBOX=false;
        private static boolean gaps4Row2=false;
        private static boolean gaps4Row1=false;
        private static boolean CROSS = false;//transpose
        
        private String colourFileName;//boolean useMyColours;
                     
        private static int defaultColSep=30;//2*Hi
        private static int defaultGap=3;//between sets of pies
        private static int defaultHi=30;//square for pie
        private static int defaultWdV=(defaultGap*3);//dist between vertical lines
        private static int defaultDiam=25;//of pie
        private static int defaultFon=10;//indicator of font
        private static String defaultFonttype="SansSerif";
 
		private static int ColSep=40;
        private static int Gap=16;//between sets of pies
        private static int Hi=40;//square for pie
        private static int WdV=Gap;//dist between vertical lines
        private static int Diam=35;//of pie
        private static int Fon=12;//indicator of font
        //private static int YELLOWbox = 0;//no yellow boxes , 1 to show

    	/* Items to serialize */
    	static String gowindowfile = ".cpg/showtable";

    	private static Point oldlocation;
    	private static Dimension oldscreensize;
    	private static boolean prefasktoclose = true;//remember you asked it to close (FinderIntegration for Mac only)
    	private static String currecpath = "~";//store path to record from last time... (see store()) .. this is default location (/Users home directory)

    	private static boolean ShowTableusedefaults=false;
    	private Options options;
    	private GridBagLayout gb = new GridBagLayout();
    	private GridBagConstraints constraints = new GridBagConstraints();
    	
    	private Font smallerfont;
    	private boolean disableFig = false;//set to true to disable after reading files...
    	
    	public JFrame frame(){ return frame; }
    	
    	Hashtable splitpanes = new Hashtable();
    	JSplitPane secondsplit;
    	JSplitPane split1;
    	
	    public ShowTable(String path, JTextArea parentlog, String ColourFile,
	    		boolean ShowTableusedefaults, CPGcolors cols, CPtabs mainw) {
	        super();
	        parent = mainw;
			parent.addwindow();
		       
		    setLayout(new BorderLayout());//GridLayout(0,1));
	        smallerfont = new JButton().getFont();
	        smallerfont = new Font(smallerfont.getFamily(),smallerfont.getSize()-2, smallerfont.getStyle());
	        this.ShowTableusedefaults=ShowTableusedefaults;
	        //Prepare log for log method
	        log = parentlog;
	        //prepare Colors as passed
	        CPGcolors = cols;
	        colourFileName=ColourFile;
	        log("Preparing colors from file "+ColourFile+"\n"); 
	        //prepare table to recieve data
	        table = new JTable(new MyTableModel(path));
	        //table.setFillsViewportHeight(true);
	        table.setPreferredScrollableViewportSize(new Dimension(100, 300));

	        //Create the scroll pane and add the table to it.
	        JScrollPane scrollPane = new JScrollPane(table);
	        
	        //THIS lot controlled by Options...
	        JPanel buttonPanel = new JPanel();
	        buttonPanel.setAlignmentX(JPanel.CENTER_ALIGNMENT);
	        fontField = new JTextField(5);
	        diamField = new JTextField(5);
	        gapField = new JTextField(5);
	        htField = new JTextField(5);
	        wdField = new JTextField(5);
	        colField = new JTextField(5);
	        String sFon = Integer.toString(Fon);
	        String sDiam = Integer.toString(Diam);
	        String sWdV = Integer.toString(WdV);
	        String sHi = Integer.toString(Hi);
	        String sGap = Integer.toString(Gap);
	        String sColSep = Integer.toString(Hi);
	        fontField.setText(sFon);
	        diamField.setText(sDiam);
	        gapField.setText(sWdV);
	        htField.setText(sHi);
	        wdField.setText(sGap);
	        colField.setText(sColSep);
	        JLabel fontLabel = new JLabel(" Font Size (7-15 pt) ");
	        JLabel diamLabel = new JLabel(" Pie Diameter (5-78 px) ");
	        JLabel gapLabel = new JLabel(" Group Padding (2-80 px) ");
	        JLabel wdLabel = new JLabel(" Subgroup Padding (0-40 px) ");
	        JLabel htLabel = new JLabel(" Row Height (10-80 px) ");
	        JLabel colLabel = new JLabel(" Column Gap (0-400) px) ");

	        JPanel sizePanel = new JPanel();
	        GridBagConstraints c = new GridBagConstraints();
	        GridBagLayout g = new GridBagLayout();
	        g.setConstraints(sizePanel,c);
	        sizePanel.setLayout(g);//4 across
	        
	        c.gridx=0;
	        c.gridy=0;
	        c.weightx=0.4;
	        sizePanel.add(fontLabel,c); c.gridy++;
	        sizePanel.add(htLabel,c); c.gridy++;
	        sizePanel.add(diamLabel,c);c.gridy++;//pie diam
	        sizePanel.add(wdLabel,c);c.gridy++;
	        sizePanel.add(gapLabel,c);c.gridy++;
	        sizePanel.add(colLabel,c);c.gridy=0;
	          
	        /* Not working, so just log it
	        fontField.addActionListener(this);//to rest to allowed values
	        htField.addActionListener(this);//to rest to allowed values
	        diamField.addActionListener(this);//to rest to allowed values
	        wdField.addActionListener(this);//to rest to allowed values
	        gapField.addActionListener(this);//to rest to allowed values
	        colField.addActionListener(this);//to rest to allowed values
	*/
	        c.gridx=1;c.weightx=0.1;
	        sizePanel.add(fontField,c); c.gridy++;
	 	    sizePanel.add(htField,c);c.gridy++;
	 	    sizePanel.add(diamField,c);c.gridy++;
	        sizePanel.add(wdField,c);c.gridy++;
	 	    sizePanel.add(gapField,c);c.gridy++;
	 	    sizePanel.add(colField,c);c.gridy=0;
	        
	 	    //put in load items, not defaults
	        check4vertlabels = new JCheckBox("Show Top Group Label", ADDVERTLABELS);
	        check4vertlabels2 = new JCheckBox("Show Secondary Label", ADD2ndVERTLABELS);
	        checkyellowbox = new JCheckBox("Mark Group Borders",YELLOWBOX);
	        checkcrossbox = new JCheckBox("Transpose",CROSS);
	        checkGap4row2 = new JCheckBox("Gap for second headers", gaps4Row2);
	        checkGap4row1 = new JCheckBox("No gaps", gaps4Row1);
	        checkGap4row2.addActionListener(this);//to get the other box to swithc off 
	        checkGap4row1.addActionListener(this);//to get the other box to swithc off 

	        CheckboxGroup group = new CheckboxGroup();
	        CheckboxGroup group1 = new CheckboxGroup();
	        //add(new Checkbox("one", cbg, true));
	        JPanel groupPan = new JPanel();
	        groupPan.setLayout(new BoxLayout(groupPan,BoxLayout.Y_AXIS));
	        groupPan.add(checkGap4row2,group,0);
	        groupPan.add(checkGap4row1,group,1);
	        JPanel groupPan1 = new JPanel();
	        groupPan1.setLayout(new BoxLayout(groupPan1,BoxLayout.Y_AXIS));
	        groupPan1.add(checkyellowbox,group1,0);
	        groupPan1.add(checkcrossbox,group1,1);
	        c.gridx=2; c.weightx=0.5;//start at top, col3
	        sizePanel.add(check4vertlabels,c); c.gridy++;
	        sizePanel.add(check4vertlabels2,c); c.gridy++;
	        //sizePanel.add(checkyellowbox,c); c.gridy++;
	        sizePanel.add(groupPan1,c);c.gridy++;
	        //sizePanel.add(checkGap4row2,c); c.gridy++;
	        //sizePanel.add(checkGap4row1,c); c.gridy++;
	        sizePanel.add(groupPan, c); c.gridy++;
	        /** Choice box for fonts */
	        TextCanvas tc = new TextCanvas(CPGcolors.getColorArray());
	        fontdescription = tc.getFontDescription();
	        for (int i = 0; i < 3; i++){//// UPDATE with TextCanvas String[]
	            fontchoice.addItem(fontdescription[i]);
	        }
	        for (int i = 0; i < 3; i++){//// UPDATE with TextCanvas String[]
	            fontchoice2.addItem(fontdescription[i]);
	        }
	       
	        JPanel check1 = new JPanel();
	        check1.add(fontchoice); check1.add(new JLabel("Main font"));
	        JPanel check2 = new JPanel();
	        check2.add(fontchoice2); check2.add(new JLabel("Pie labels"));
	        sizePanel.add(check1,c);c.gridy++;
	        sizePanel.add(check2,c);
	        //======= end items in options panel =========//
		      
	        defaultButton = new JButton("Default options");
	        defaultButton.addActionListener(this);
	        
	        // Resetting defaults? should be a menu item? 
	        figButton = new JButton("Plot");//MAIN button to produce figure using printit/CoulsonPlotPrintable
	        figButton.addActionListener(this);
	        options = new Options(sizePanel, sizePanel.getPreferredSize().width, defaultButton);//add ready made panel
	        options.hideOptions();//have to initialize before I can hide
	        foroptions.add(options);
	        JPanel figPan = new JPanel();
	        bottom.setLayout(new BorderLayout());
	        bottom.add(figButton, BorderLayout.CENTER);
	        secondsplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT,foroptions,bottom);
	         split1 = new JSplitPane(JSplitPane.VERTICAL_SPLIT,scrollPane, 
	        	secondsplit);
	        split1.setEnabled(false);
	        secondsplit.setEnabled(false);
	        this.add(split1);
	        splitpanes.put(0, split1);
	        splitpanes.put(1, secondsplit);
	        foroptions.addComponentListener(this);//when window size changed, or Show/hide options pressed, position of splitpanes reset
	        if(disableFig){
	        	figButton.setEnabled(false);
	        }
	        
        	
	    }
	    
	    public void addNotify ()//overrides
	    {
	    	super.addNotify ();
	    	if(foundata.equals("")){figButton.requestFocus();}//makes it glow
	    	else{super.getRootPane().setDefaultButton(figButton);}//makes it default
	    }

		private void log(String message){
			p(message);
			log.append(message);log.setCaretPosition(log.getSelectionEnd());
		}

	    public void actionPerformed(java.awt.event.ActionEvent ignore) {
	    	if(ignore.equals((String)"Print")){
	    		MessageFormat header = new MessageFormat("Page {0,number,integer}");
	    		try {
	    			table.print(JTable.PrintMode.FIT_WIDTH, header, null);
	    		} catch (java.awt.print.PrinterException e) {
	    			System.err.format("Cannot print %s%n", e.getMessage());
	    		}
	    	}
	    	else if(ignore.getSource() == defaultButton){
	    		ShowTableusedefaults=true;
	    		Gap = defaultGap;
	    		Diam = defaultDiam;
	    		Hi = defaultHi;
	    		WdV = defaultWdV;
	    		Fon = defaultFon;
	    		ColSep = (Hi);
	          	ADDVERTLABELS = true;//check4vertlabels.isSelected();
            	ADD2ndVERTLABELS = true;//check4vertlabels2.isSelected();
            	FONTCHOICE = defaultFonttype;//(String) fontchoice.getSelectedItem();
            	FONTCHOICE2 = defaultFonttype;//(String) fontchoice2.getSelectedItem();
            	YELLOWBOX = false;
            	CROSS = false;
	           	store();//doesn't have to wait for window to close
	           	check4vertlabels.setSelected(ADDVERTLABELS);
	           	check4vertlabels2.setSelected(ADD2ndVERTLABELS);
	           	checkyellowbox.setSelected(YELLOWBOX);
	           	checkcrossbox.setSelected(CROSS);
	           	checkGap4row2.setSelected(false);
	           	checkGap4row1.setSelected(false);//default, gaps between outer labels (ROW1)
	           	fontchoice.setSelectedItem(FONTCHOICE);
	           	fontchoice2.setSelectedItem(FONTCHOICE2);
	           	String sFon = Integer.toString(Fon);
		        String sDiam = Integer.toString(Diam);
		        String sWdV = Integer.toString(WdV);
		        String sHi = Integer.toString(Hi);
		        String sGap = Integer.toString(Gap);
		        String sColSep = Integer.toString(ColSep);
		        fontField.setText(sFon);
		        diamField.setText(sDiam);
		        gapField.setText(sWdV);
		        htField.setText(sHi);
		        wdField.setText(sGap);
		        colField.setText(sColSep);
		        
	    	}
 	    	//make em like radio buttons except that you can have BOTH OFF
	    	else if(ignore.getSource() == checkGap4row2){
	    		boolean x = checkGap4row2.isSelected();
	    		p("Got "+x+" for check Gap 2");
	    		if(x) checkGap4row1.setSelected(false);
	    	}
	    	else if(ignore.getSource() == checkGap4row1){
	    		boolean x = checkGap4row1.isSelected();
	    		p("Got "+x+" for check Gap 1");
	    		if(x) checkGap4row2.setSelected(false);
	    	}
	    	else if (ignore.getSource() == figButton){
	    		ShowTableusedefaults=false;
	    		String sGap = gapField.getText();
	    		if(!sGap.equals(""))
	    			Gap = new Integer(sGap);
	    		String sDiam = diamField.getText();
	    		if(!sDiam.equals(""))
	    			Diam = new Integer(sDiam); 
	    		String sHi = htField.getText();
	    		if(!sHi.equals("")){
	    			Hi = new Integer(sHi);}
	    		String sWd = wdField.getText();
	    		if(!sWd.equals("")){
	    		WdV = new Integer(sWd);}
	    		String sFon = fontField.getText();
	    		if(!sFon.equals("")){
	    		Fon = new Integer(sFon);}
	       		/* correct user inputs if out of range */
	    		//THEY DON"T respond to actionlistener as text fields but can reset here to avoid confusion
		    	//else if (ignore.getSource() == fontField){
		    		if(Fon>15){Fon=15;log("Font cannot be larger than "+Fon+"\n");
		    		fontField.setText(Integer.toString(Fon));} 
		    		if(Fon<7){Fon=7;log("Font cannot be smaller than "+Fon+"\n");
		    		fontField.setText(Integer.toString(Fon));}
		    	//}
		    	//else if (ignore.getSource()==htField){
		    		if(Hi>80){Hi=80;log("Height of pie squares cannot be larger than "+Hi+"\n");
		    		htField.setText(Integer.toString(Hi));}   
		    		if(Hi<10){Hi=10;log("Height of pie squares cannot be smaller than "+Hi+"\n");
		    		htField.setText(Integer.toString(Hi));}
		    	//}
		    	//else if (ignore.getSource()==wdField){
		    		if(WdV>Hi/2){WdV=Hi/2;log("Gap between pie squares cannot be larger than half height ("+(Hi/2)+")\n");
		    		wdField.setText(Integer.toString(WdV));} 
		    		if(WdV<1){WdV=1;log("Gap between pie squares cannot be less than "+WdV+"\n");
		    		wdField.setText(Integer.toString(WdV));}
		    	//}
		    	//else if (ignore.getSource()==gapField){
		    		if(Gap<1){Gap=1;log("Gap between groups of pies cannot be less than "+Gap+"\n");
		    		gapField.setText(Integer.toString(Gap));}   
		    		if(Gap>(Hi*4)){Gap=(Hi*4);log("Gap between groups of pies cannot be more than 4Xheight ("+Gap+")\n");
		    		gapField.setText(Integer.toString(Gap));}
		    	//}
		    	//else if (ignore.getSource()==diamField){
			    	if(Diam<5){Diam=5;log("Diameter of pies cannot be less than "+Diam+"\n");
			    	diamField.setText(Integer.toString(Diam));} 
		    		if(Diam>(Hi-2)){Diam=Hi-2;log("Diameter of pies cannot exceed height of pie squares-2 ("+(Hi-2)+")\n");
		    		diamField.setText(Integer.toString(Diam));}//can't have pies bigger than their canvi
		    	//}
		    	//else if (ignore.getSource()==colField){
		    		if(ColSep<1){ColSep=Hi;log("Gap between pie columns cannot be less than "+(Hi)+"\n");
		    		colField.setText(Integer.toString(ColSep));} 
		    		if(ColSep>(Hi*5)){ColSep=Hi*5;log("Gap between pie columns cannot exceed 5 x height of pie squares ("+(Hi*5)+")\n");
		    		colField.setText(Integer.toString(ColSep));}//can't have pies bigger than their canvi
		    	//}

	    		//p("PASSING "+Gap+" (gap) + Hi "+Hi+" + (Font) "+Fon+" diam: "+Diam+"WdV "+WdV);
            	ADDVERTLABELS = check4vertlabels.isSelected();
            	ADD2ndVERTLABELS = check4vertlabels2.isSelected();
               	YELLOWBOX = checkyellowbox.isSelected();
               	CROSS = checkcrossbox.isSelected();
                gaps4Row2 = checkGap4row2.isSelected();
            	gaps4Row1 = checkGap4row1.isSelected();
            	FONTCHOICE = (String) fontchoice.getSelectedItem();
            	FONTCHOICE2 = (String) fontchoice2.getSelectedItem();
            	ColSep = Integer.parseInt(colField.getText());
            	store();//doesn't have to wait for window to close
	    		new printit(v, path, log, table, Gap, Hi, Diam, Fon, WdV, ADDVERTLABELS,ADD2ndVERTLABELS,YELLOWBOX,CROSS,gaps4Row2,gaps4Row1,ColSep,FONTCHOICE,FONTCHOICE2,CPGcolors);//10,60,40
	    	}
	    }

	    /** Inner Class MyTableModel to extract and parse data
	     * If the data appears IN THE table then parsing was successful - 
	     * errors have to come out in the logging.
	     * @author lime
	     *
	     */
 public class MyTableModel extends AbstractTableModel {
	        private String[] columnNames;//can reference but must initialise all at once
	        private Object[][] data ;
		    		        
	        public MyTableModel(String path){
	        	/** new constructor to get table from a file of my choice */
		        log("Opening data file: "+path+" to put into Table\n"); 
	        	//open file
	 		    int HeaderLineNo = 4;
		        int DataLineStart = 0;
		        boolean DataLineStartSet=false;
      			String HeaderLine = "";//column headers (species) ROW3
    			String delim = ",";
    			try{
	        		FileIO fio = new FileIO();
	        		if(!fio.openReadFile(path)){
	        			log("Can't open data file"+path+"\n");
	        		}
	        		else{	
    					log("Reading data file "+path+"\n");
	        			int count=0;
	        	/* read file and put everything into a String(HIFstack)vector */
	        			String t = fio.readDataLine();
	        			int countline = 1;
	        			delim = "\t";//KEEP to deal with tab delim text
	        			log("Default delimiter is '"+delim+"'\n");
	        			while (t != null)//fio.endoffile())
	        			{	if(t.contains(",") && delim.equals("\t")){
	        					delim = ","; log("Delimiter is now '"+delim+"'\n");
	        				}
	        				if(delim.equals(",")) t.replaceAll("\\s", "");//not working
	        				if(t.contains(delim)){//ignore blank lines
	        					if(!DataLineStartSet){
	        						v.push(t);
	        						log("Header line (ignoring first two columns): "+t+"\n");
	        					}//add all lines of headers
	        				}//ignore blank line spacers
	        				else{
		        				log("ERROR: No delimiter found (, or <tab>) - not using line: "+t+"\n");
	        				}
	        				if(countline>3 && (t.contains("+") || t.contains("-"))){
	        					foundata = "+";
	        					if(!DataLineStartSet){//message this once
	        						DataLineStart = countline-1; DataLineStartSet=true;
	        						log("CPG thinks you are using - and + data. [If this is not correct please report as a bug with your input file ]\n");
	        						//p("Set DataLineStart to "+(countline-1)+" ("+DataLineStart);
	        					}else {v.push(t);}
	        				}else if(countline>3 && (t.contains("0") || t.contains("1"))){//may be confused with protein names
	        					foundata="1";
	        					if(!DataLineStartSet){//message this once
	        						DataLineStart = countline-1; DataLineStartSet=true;
	        						log("CPG thinks you are using 0 and 1 as data rather than - and +. [If this is not correct please report as a bug with your input file ]\n");
	        						log("Not using line: "+t+"\n");
    	        				}else {v.push(t);}
	        				}
	        				count++;
	        				if (count==(HeaderLineNo-1)){
	        					HeaderLine = t;
	        					log("Headers (pie labels left) on line "+HeaderLineNo+": "+HeaderLine+"\n");
	        				}
	        				t = fio.readDataLine(); countline++;
	        			}//add lines t to vector v
	        			if(fio.closeReadFile()){
	        				log("Data file closed\n");
	        			} else {
	        				log("Can't close data file, sorry\n");
	        			}
	        			if(foundata.equals("")){ 
	        				log("STOP !!! I can't get your data type: did you use 0,1 or -,+ to indicate pie fillings?");
							log("Not using "+t+"\n");
							figButton.setEnabled(false);
	        			}
	        		}
	        	}catch (Exception e){log("Can't open file "+path+" due to: "+e);}
	        	int NoDataRows = v.size()-DataLineStart;
	        	log("CPG has "+v.size()+" rows of which "+NoDataRows+" are potentially data (pies)");
        		
	        	/* create columnNames array (to pass) */
	        	columnNames = HeaderLine.split(delim);
	        	data = new String[NoDataRows][columnNames.length];//set size up front
				int commacount=columnNames.length;
	        	if(commacount==0) {
	        		log("NO delimiters ("+delim+") - IS IT A CSV or tabbed text FILE - can't make table\n");
	        		delim = "\t";
	        		log("Note that if the data does not appear in the cells of the table it has not been properly parsed: please review this log for details.\n");
	        		figButton.setEnabled(false);
	        	}
	        	else{
	            	//now can read lines and make arrays
		        	/* create data array of arrays = rows (to pass) */
					log("There are "+commacount+" delimiters per line\n");
					int CountRow=0;
					for(int k = DataLineStart;k <v.size();k++)//iterate through lines of file in HifStackString...
					{
						String row;row = v.get(k);
						if(delim.equals(",")) row.replaceAll("\\s", "");
						//p("Line "+k+": shall we use "+row+"?");
						//log.append("Line "+k+": shall we use "+row+"? \n");log.setCaretPosition(log.getSelectionEnd());
						
						if(row.contains("-")||row.contains("+")){
   	        				String [] thisrow = row.split(delim);
		        				data[CountRow]=thisrow;CountRow++;
		        				log("Adding row "+CountRow+": "+row+"' to table\n");
    					}
						else if(row.contains("0")||row.contains("1")){
   	        				row.replace('1', '+');row.replace('0', '-');
   	        					String [] thisrow = row.split(delim);
   	          	            	data[CountRow]=thisrow;CountRow++;
		        				log("Adding row "+CountRow+": "+row+"' to table\n");
    					}
    					else{
    						log(CountRow+" is not a data row: won't appear in table: "+row+"\n");
    					}
					}//end data
					log("Size of dataset: "+data.length+" rows \n");
    				//check that you have at least 3 rows of data HEADINGS
    				String foundata_opp = "-";
    				if(foundata.equals("1")) foundata_opp = "0";
    				log("Testing third row: should contain main left labels: "+v.get(2)+"\n");
    				String totest = v.get(2).substring(v.get(2).indexOf(delim));
    				totest = totest.substring(totest.indexOf(delim));//remove first 2 columns
    				p("test "+totest);
    				
    				if(totest.contains(foundata)||totest.contains(foundata_opp)){
    					String message =("Row 3 looks like a data line: you must have at least 3 header lines\nSTOPPING\nPLEASE CORRECT your data file\n\n");
    					log(message); System.out.println(message);
    					disableFig = true;//even though it can plot it's not quite right, wi 1 or 2 phenotype headers
    				}

					//System.exit(0);//exit if testing inputs
	        	}
	        	
	        }
	        	
	        public int getColumnCount() {
	            return columnNames.length;
	        }

	        public int getRowCount() {
	            return data.length;
	        }

	        public String getColumnName(int col) {
	            return columnNames[col];
	        }

	        public Object getValueAt(int row, int col) {
	            return data[row][col];
	        }
	        
	        /*
	         * JTable uses this method to determine the default renderer/
	         * editor for each cell.  If we didn't implement this method,
	         * then the last column would contain text ("true"/"false"),
	         * rather than a check box.
	         */
	        public Class getColumnClass(int c) {
	            return getValueAt(0, c).getClass();
	        }

	        /*
	         * Don't need to implement this method unless your table's
	         * editable.
	         */
	        public boolean isCellEditable(int row, int col) {
	            //Note that the data/cell address is constant,
	            //no matter where the cell appears onscreen.
	            if (col < 2) {
	                return false;
	            } else {
	                return true;
	            }
	        }

	        /*
	         * Don't need to implement this method unless your table's
	         * data can change.
	         */
	        public void setValueAt(Object value, int row, int col) {
	            if (DEBUG) {
	                p("Setting value at " + row + "," + col
	                                   + " to " + value
	                                   + " (an instance of "
	                                   + value.getClass() + ")");
	            }
	            data[row][col] = value;
	            fireTableCellUpdated(row, col);
	            if (DEBUG) {
	                System.out.println("New value of data:");
	                printDebugData();
	            }
	        }

	        private void printDebugData() {
	            int numRows = getRowCount();
	            int numCols = getColumnCount();
	            for (int i=0; i < numRows; i++) {
	                System.out.print("    row " + i + ":");
	                for (int j=0; j < numCols; j++) {
	                    System.out.print("  " + data[i][j]);
	                }
	                System.out.println();
	            }
	            System.out.println("--------------------------");
	        }
	    }
	    
	    static Dimension optsize = new Dimension(527,514);
	    /**
	     * Create the GUI and show it.  For thread safety,
	     * this method should be invoked from the
	     * event-dispatching thread.
	     */
	    public static void createAndShowGUI(String path, JTextArea parentlog, String ColourFile, 
	    		boolean ShowTableusedefaults, CPGcolors cols) {
	        //Create and set up the window.
	    	if(!ShowTableusedefaults){//if defaults in  use DON;t restore your last save! (do if you change your mind)
		    	try{
		    		load();//get defaults for Hi, Lo, and etc
		    	}catch(Exception e){
		    		System.out.println("ShowTable: Can't restore location or previous settings because "+e);
		    	}
	    	}
	        frame = new JFrame("Your file... table");
	        
	        //Use filename for title of this window...
	        String filename = path;
	        if(filename.indexOf("/")>-1)
	        	filename = filename.substring((filename.lastIndexOf("/")+1));
	        frame.setTitle(filename);

	        //frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        //Create and set up the content pane.
	        ShowTable newContentPane = new ShowTable(path, parentlog,ColourFile, ShowTableusedefaults, cols, parent);
	        newContentPane.setOpaque(true); //content panes must be opaque
			setLocations(frame);//restore last location
			frame.setSize(optsize);
	        frame.setContentPane(newContentPane);
	        //frame.setLocation(10,80);
	        //Display the window.
	        frame.setJMenuBar(menubar);
	        menubar.addShowTable(newContentPane);
	        //parent.menubar.addShowTable(newContentPane);
	        parent.centerit(frame);
	        frame.pack();
	        frame.setVisible(true);
	        frame.addWindowListener(newContentPane);
	    }
	    
		public void doClose(){//for apple-w and doMenu
			p("In doClose for ShowTable");
			//this.setVisible(false);//hides content only
			this.frame.setVisible(false);
			parent.removewindow();
		}

		   /* Items for serializable
		    */
		private static void setLocations(JFrame superframe){
			if(oldlocation!=null && oldscreensize!=null){
				//p("Going back to ole location and size");
				superframe.setLocation(oldlocation);
				superframe.setSize(oldscreensize);
				//to centralize need this: but not sure any of this is working
		        //put it in the centre and give it a size
		        //java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		       // frame.setSize(oldscreensize);//new java.awt.Dimension(366,116));
		       // superframe.setLocation((screenSize.width-oldscreensize.width)/2,(screenSize.height-oldscreensize.height)/2);
			}

		}
		


		private class Options extends JPanel{
			//this Jpanel contains an arrow icon wiht the lable Make fig, Options
			//Upon hitting this button the new window appears, stretching the frame to accomodate it
			JPanel buttons = new JPanel();
			JButton optionalButt;
			//JFrame optionwindow = new JFrame();
			JPanel options;
			//JPanel blank = new JPanel();//little grey box...
			JLabel showhide = new JLabel();
			Dimension minsize = new Dimension(5,5);
			int optWidth;
	
			
			public Options(JPanel checkmadeabove, int width, JButton defaults){
				JPanel optionpanel = new JPanel();
				optionpanel.setBorder(new CompoundBorder
		        	      (BorderFactory.createEmptyBorder(),
		        	       new EmptyBorder(10,10,10,10)));
				optionpanel.setBorder(BorderFactory.createTitledBorder("Figure options")
		        	       );
				JPanel figurepart = new JPanel();
				figurepart.setBorder(new CompoundBorder
		        	      (BorderFactory.createEmptyBorder(),
		        	       new EmptyBorder(10,10,10,10)));
				figurepart.setBorder(BorderFactory.createTitledBorder("Plot")
		        	       );
				optWidth = width;
				gb.setConstraints(this, constraints);
				constraints.gridx=0;
				constraints.gridy=0;
				constraints.weighty = 0.2;
				setLayout(gb);
				//setLayout(new GridLayout(0,1));//one panel above the other
				options = checkmadeabove;//checkPanel - feed it in as it's been made already
				//JButton makefigure = new JButton("Create diagram");
				//JLabel optional = new JLabel("Figure options");
				//should be an image
				 JButton image = new JButton(new ImageIcon(".cpg/Cpgsm.png"));//too big
				 image.setFocusable(false);
	             JLabel optionalButt = new JLabel("Hide Options");
	             //optionalButt.setMultiClickThreshhold(1);
	             optionpanel.add(image);
	             optionpanel.add(optionalButt);
	             optionpanel.add(defaults);
	             optionpanel.setMinimumSize(new Dimension(400, 25));
	             buttons.add(optionpanel);
	             
	              //buttons.add(figure);
		              gb.setConstraints(buttons,constraints);
	              this.add(buttons,constraints);
	             // this.setSize(width, image.getHeight());
	              image.addActionListener(new ActionListener(){
	            	  public void actionPerformed(ActionEvent e){
	            		  putUpOptions();
	            	  }
	              });
	              constraints.gridx=0;
	            constraints.gridy=1;
	            constraints.weighty=0.8;	
				Component [] ca = this.getComponents();
				for(int i = 0; i<ca.length; i++){
					ca[i].setMinimumSize(minsize);//need to be able to shrink or it disappears
					ca[i].setFont(smallerfont);
				}
	            /* Need to add BOTH panels here if I want them to show
	             * and swap out
	             */
				this.add(options,constraints);//If i add this here, it shows...
				//initialize with just buttons
	             doshowoptions();//free
			}
			
			boolean isUp = false;
			
			public void hideOptions(){
				isUp = true;
			}
			
			public void putUpOptions(){
				p("putUpOptions isUp "+isUp);
				if(!isUp){//show WHOLE thing
					p("remove blank");
					this.remove(blank);
				    constraints.gridx=0;
		            constraints.gridy=1;
		            constraints.weighty=0.8;
		            this.add(options,constraints);
		            p("Added options");
					isUp = true;
				}
			
				else if(isUp){//hide options and show only buttons
					this.remove(options);
				    constraints.gridx=0;
		            constraints.gridy=1;
		            constraints.weighty=0.8;
					this.add(blank,constraints);
					isUp = false;
				}
				doshowoptions();
			}
			
		}
		
		/* Method in main panel to repaint component with and without option panel
		 *  Note that foroptions panel has ComponentListener on it, 
		 *  which resets the bottom divider to give consisten button size
		 *  in componentResized
		 */
		public void doshowoptions(){
			if(options!=null){
				Component[] comps = options.getComponents();
				for(int i = 0;i<comps.length; i++){
					p("repaint "+comps[i]);
					comps[i].repaint();
				}

				options.repaint();
				if(!options.isUp){split1.setDividerLocation(this.getSize().height-140);}
				else{split1.setDividerLocation(this.getSize().height-368);}
				
			}else p("options NULL");
		}
		
		void store(){//goWindow o, File f){//throws IOException
				File f = new File(gowindowfile);
				if(!f.isFile()){
					try{f.createNewFile();					
					}catch(IOException e){System.out.println("Can't create "+gowindowfile+" because "+e);}
				}
				try{
					ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream (f));            
					Vector goObjects = new Vector();
					goObjects.add(oldscreensize);
					goObjects.add(oldlocation);
					goObjects.add(prefasktoclose);
					goObjects.add(currecpath);
					
					goObjects.add(WdV);
					goObjects.add(Hi);
					goObjects.add(Gap);
					goObjects.add(Diam);
					goObjects.add(Fon);
					
					goObjects.add(FONTCHOICE);
					goObjects.add(FONTCHOICE2);
					goObjects.add(ADDVERTLABELS);
					goObjects.add(ADD2ndVERTLABELS);

					goObjects.add(gaps4Row2);
					goObjects.add(gaps4Row1);
					goObjects.add(ColSep);
					goObjects.add(YELLOWBOX);
					goObjects.add(CROSS);

					out.writeObject(goObjects);
					out.close();
				}catch(IOException e){System.out.println("Could not store your program parameters "+e);}
			}
			
			static void load() throws IOException, ClassNotFoundException {
				File f = new File(gowindowfile);
				if(!f.isFile()) return;
				ObjectInputStream in = new ObjectInputStream(new FileInputStream(gowindowfile));
				Vector goObjects = (Vector) in.readObject();
				if(goObjects.size()>0)
					oldscreensize = (Dimension) goObjects.elementAt(0);
				if(goObjects.size()>1)
					oldlocation = (Point) goObjects.elementAt(1);
				if(goObjects.size()>2)
					prefasktoclose = (Boolean) goObjects.elementAt(2);
				if(goObjects.size()>3)
					currecpath = (String) goObjects.elementAt(3);
				if(goObjects.size()>4)
					WdV = (Integer) goObjects.elementAt(4);
				if(goObjects.size()>5)
					Hi = (Integer) goObjects.elementAt(5);
				if(goObjects.size()>6)
					Gap = (Integer) goObjects.elementAt(6);
				if(goObjects.size()>7)
					Diam = (Integer) goObjects.elementAt(7);
				if(goObjects.size()>8)
					Fon = (Integer) goObjects.elementAt(8);
				if(goObjects.size()>9)
					FONTCHOICE = (String) goObjects.elementAt(9);
				if(goObjects.size()>10)
					FONTCHOICE2 = (String) goObjects.elementAt(10);
				if(goObjects.size()>11)
					ADDVERTLABELS = (Boolean) goObjects.elementAt(11);
				if(goObjects.size()>12)
					ADD2ndVERTLABELS = (Boolean) goObjects.elementAt(12);
				if(goObjects.size()>13)
					gaps4Row2 = (Boolean) goObjects.elementAt(13);
				if(goObjects.size()>14)
					gaps4Row1 = (Boolean) goObjects.elementAt(14);
				if(goObjects.size()>15)
					ColSep = (Integer) goObjects.elementAt(15);
				if(goObjects.size()>16)
					YELLOWBOX = (Boolean) goObjects.elementAt(16);
				if(goObjects.size()>17)
					CROSS = (Boolean) goObjects.elementAt(17);
			}

			/* ComponentListener abstract method implementation */
			
			public void componentMoved(ComponentEvent ce){
				oldlocation = new Point(this.getLocation());//store resize information for next reopen
			}
			
			public void componentHidden(ComponentEvent ce){
				//store current plugin displayed
			}
			
			public void componentResized(ComponentEvent ce){
				oldscreensize = new Dimension(this.getSize());
				p("RESize to "+this.getSize());
				secondsplit.setDividerLocation((secondsplit.getSize().height-42));//big enough for button
				//this.revalidate();CAUSES LOOPING
			}
			
	
			public void componentShown(ComponentEvent ce){
		    	try{
		    		load();//get defaults for Hi, Lo, and etc
		    	}catch(Exception e){
		    		System.out.println("ShowTable Can't restore previous settings because "+e);
		    		e.printStackTrace();
		    	}

			}
			/* End component methods storing window placement */
			
			public void windowClosing(WindowEvent w){
				p("ShowTable Window closing  - storing");
				store();
				parent.removewindow();
		    }
			
			public void windowClosed(WindowEvent w){
				p("ShowTable windowClosed ");
				store();
			}
			
			public void windowOpened(WindowEvent w){
				p("ShowTable windowOpened ");
			}
			public void windowActivated(WindowEvent w){
				p("ShowTable windowActivated try grab menubar");
		    	try{
		    		load();//get defaults for Hi, Lo, and etc
		    	}catch(Exception e){
		    		System.out.println("Can't restore location or previous settings because "+e);
		    	//	e.printStackTrace();
		    	}
			}
			
			public void windowDeactivated(WindowEvent w){
				p("ShowTable windowDeactivated, store location.size");
				//serialize 
				//eg red dot selected
				store();
			}
			
			public void windowDeiconified(WindowEvent w){
			}
			
			public void windowIconified(WindowEvent w){
			}
			
			

	
	}

