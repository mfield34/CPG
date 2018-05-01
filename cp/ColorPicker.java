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

/** Can pick multiple times, each getting fed back to log, and to ?
 *  Close by using the red button
 */


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/* ColorChooserDemo.java requires no other files. */
public class ColorPicker extends JPanel
                              implements ChangeListener,  ActionListener, ComponentListener,
                              Serializable{

	boolean printok = false;
    JFrame framef = new JFrame("Custom Colors");
    boolean savetothisfile = false;
    boolean worksaved = true;//AsK to save if work not saved and you click Done
    boolean close = false;
    
    private CPtabs CPtabs;
    
    protected JColorChooser tcc;
    private JFileChooser fc;
    private String currentpath="~";//serialize
    protected JLabel banner;
    protected JLabel banner2;
       protected String myColorCoords;
    static JButton b;static JButton d;static JButton done = new JButton("Done");JButton unpick = new JButton("Unpick");
    static boolean picked;
    int red; int blue; int green;
    private JTextArea log;//pass from CPtabs
    private HifStackString his;//record colours
    private String filename;//pass from CPtabs and write his to this file on Save Colours
    private CPtabs mainwindow;//all of CPtabs passed, to use p function (write to log)
    private JPanel bannerPanel;
    private int countColours;
    private static Point oldlocation = new Point(20,200);
    private JButton savetofile;
    private String ffile=".colorpicker";//for serializing
    private Hashtable squares = new Hashtable();
    private int squareindex = 0;//keys for squares, to keep order
    private int doneone = 0;//Done hits twice
    QuitConfirmJDialog myQuitDialog_nosave=null;

    private void p(String s){ if (printok) System.out.println("ColorPicker:"+s); }
    
    public void load(){
		File f = new File(ffile);
		try{
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(f));
			Vector goObjects = (Vector) in.readObject();
			/*if(goObjects.size()>0)
				oldscreensize = (Dimension) goObjects.elementAt(0);*/
			if(goObjects.size()>0)
				oldlocation = (Point) goObjects.elementAt(0);
			if(goObjects.size()>1)
				currentpath = (String) goObjects.elementAt(1);
		}catch(Exception e){System.out.println(e);}

    }
    
    public void store(){
		File f = new File(ffile);
		try{
			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream (f));            
			Vector goObjects = new Vector();
			//goObjects.add(oldscreensize);
			goObjects.add(oldlocation);
			goObjects.add(currentpath);
			out.writeObject(goObjects);
			out.close();
		}catch(IOException e){System.out.println(e);}
    }

    public void componentMoved(ComponentEvent ce){
    	oldlocation = super.getLocation();
    }
    public void componentResized(ComponentEvent ce){
    }
    public void componentShown(ComponentEvent ce){
    	load();
    	super.setLocation(oldlocation);
    }
    public void componentHidden(ComponentEvent ce){
    	System.out.println("ColorPicker::componentHidden "+oldlocation);
    	store();
    }
    
    public ColorPicker(JTextArea l, String file, CPtabs cpt) {
        super(new BorderLayout());
        load();
        CPtabs = cpt;
        fc = new JFileChooser(new File (currentpath));
 
        log = l;
        mainwindow=cpt;
        his = new HifStackString();
        filename = file;
        countColours=0;
        //Set up the banner at the top of the window
        banner = new JLabel("Choose colors for pies (and text)");
        JLabel banner1 = new JLabel("'Pick' to remember a color");
        banner2 = new JLabel("'Save' to file: remembered colors in order picked");
        banner.setBorder(new EmptyBorder(2,2,2,2));
        banner.setBackground(Color.white);
        banner1.setBorder(new EmptyBorder(2,2,2,2));
        banner2.setBorder(new EmptyBorder(2,2,2,2));
        banner.setAlignmentX(Component.LEFT_ALIGNMENT);
        banner1.setAlignmentX(Component.LEFT_ALIGNMENT);
        banner2.setAlignmentX(Component.LEFT_ALIGNMENT);
               if(oldlocation!=null)
        	banner.setLocation(oldlocation);
        bannerPanel = new JPanel(new BorderLayout());//new GridLayout(0,1));
        JPanel colors = new JPanel();
        colors.setLayout(new BoxLayout(colors,BoxLayout.LINE_AXIS));//make nice big squares that fill the width                                                      
        for(int i =0; i<20; i++){ 
        	//make squares to put your picked colors in
        	JTextField tf = new JTextField();tf.setBackground(Color.white);
        	squares.put(squareindex, tf); squareindex++; 
        	tf.setSize(new Dimension(20,20));
        	colors.add(tf);
        }
        squareindex=0;//increment up when you pick
        JPanel pan = new JPanel();
        pan.setLayout(new BoxLayout(pan,BoxLayout.Y_AXIS));
        pan.add(banner);
        pan.add(banner1);
        pan.add(banner2);
        bannerPanel.add(colors, BorderLayout.CENTER);
        bannerPanel.add(pan,BorderLayout.SOUTH);
        bannerPanel.setBorder(BorderFactory.createTitledBorder("Color picker"));

        //Set up color chooser for setting text color
        tcc = new JColorChooser(Color.white);
        tcc.getSelectionModel().addChangeListener(this);
        //tcc.setBorder(BorderFactory.createTitledBorder(
       //  "Choose Text Color"));
        tcc.setBackground(Color.white);
        picked=false;

        
        JPanel p = new JPanel();
        b = new JButton("Pick");
        b.addActionListener(this);
        unpick.addActionListener(this);
        
        d = new JButton("Save Colors");
        d.addActionListener(this);
        done.addActionListener(this);
        p.add(b);
        p.add(unpick);
        p.add(d);
        p.add(done);
       // pan.setBackground(Color.red);
        pan.add(p);//so put button panel p into the instruction panel instead
        add(bannerPanel, BorderLayout.NORTH);
        //add(p, BorderLayout.CENTER);//disappears at centre
        add(tcc, BorderLayout.PAGE_END);
    }
    
    public void communicate(JTextArea l){
    	//log = l;//use from CPtabs
    }

    public void stateChanged(ChangeEvent e) {
        Color newColor = tcc.getColor();
        //System.out.println(newColor.toString());
        myColorCoords = newColor.toString();
        banner.setBackground(Color.white);
        banner.setForeground(Color.black);
               //banner.setText(myColorCoords);
        red = newColor.getRed();
        green = newColor.getGreen();
        blue = newColor.getBlue();
        //if blue chamge bg color
        //java.awt.Color[r=102,g=255,b=55]
        banner.setText("r="+red+", g="+green+", b="+blue);
        int FG=newColor.getRGB();
        int BG = -(FG);
        //banner.setBackground(new Color(BG));
        banner.setForeground(new Color(FG));
        //mainwindow.p("Hello I found a colour");
        picked = true;
        worksaved = false;
       //addNotify();//lights up the pick button
    }
    
    public HifStackString getColours(){
    	return his;//return all the picked colours.
    	/** Not satisfactory as you need to see them
    	 * 
    	 */
    }
    
    private boolean writeColors2file(String colorFilename){
    	try{
    		FileIO fio = new FileIO();
    	    // Create file 
    	    FileWriter fstream = new FileWriter(colorFilename);
    	    BufferedWriter out = new BufferedWriter(fstream);
    	    for (int i=0;i<his.size();i++){out.write(his.get(i)+"\n");}
    	    //Close the output stream
    	    out.close();
     			mainwindow.log(his.size()+" colors written to file "+colorFilename);
    			System.out.println(his.size()+" colors written to file "+colorFilename);
     	}
    	catch (Exception e){ 
    		mainwindow.log("Couldn't write color array to file "+colorFilename+": "+e.getMessage());
    		System.out.println("Couldn't write color array to file "+colorFilename+": "+e.getMessage());
    		return false;
    	}
    	return true;
    }
    
    public void actionPerformed(ActionEvent e) {
        //Handle open button action.
    	p("Button: "+e.getSource());
        if (e.getSource() == b) {//Pick button
        		
        	mainwindow.log("Selected colour: red="+red+", green="+green+", blue="+blue+"\n");
                //if you haven't saved it already...
            String colourstring = red+","+green+","+blue;
       		if(red==0 && green==0 && blue==0){
        			mainwindow.log("You haven't chosen a color yet\n");
        		}
                else if(!his.contains(colourstring)){
                	his.push(colourstring);
                	//make new colour
                	colourstring.replaceAll(" ", "");
                	Color pickedcolor=null;
					if(colourstring.contains(",")){//ignore blank lines
						String [] cols = colourstring.split(",");
						int[] colornums = new int[3];
						colornums[0]=new Integer(cols[0]);
						colornums[1]=new Integer(cols[1]);
						colornums[2]=new Integer(cols[2]);
    					pickedcolor =new Color(colornums[0],colornums[1],colornums[2]);
    					p("Pickedcolor "+pickedcolor);
					}//ignore blank line spacers
					if(pickedcolor!=null){
						worksaved = false;
						((JTextField)squares.get(squareindex)).setBackground(pickedcolor);
						p("Color should be in "+squareindex); 
						squareindex++;
					}else p("No color");
                }
                else{mainwindow.log("You already picked this color - repeats not allowed\n");}
        }
        
        else if(e.getSource() == unpick){
        	worksaved = false;
        	squareindex--;
        	his.pop();//remove last...
        	((JTextField)squares.get(squareindex)).setBackground(Color.white);
        }
        
        
            
        else if(e.getSource() == d){//Save button - pick file and save... (write to file but don't close the window)
        	
        	//NEW file dialog to get file...
            fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
            int returnVal = fc.showSaveDialog(this);
    		boolean savefilechosen = false;
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                //if cancel, currentpath is not changed
                currentpath = file.getAbsolutePath();//for store to remember for next time
                mainwindow.log("Opening: " + file.getName() + ".\n");
                mainwindow.log("Directory path: " + file.getPath() + ".\n");
                savefilechosen = true;
                                
            } else {
                mainwindow.log("Open command cancelled by user.\n");
                //currentpath = "";
                savefilechosen = false;
            }
            log.setCaretPosition(log.getDocument().getLength());

	        if(savefilechosen){//otherwise tries to save out to last file specified
	            File f = new File(currentpath);
	            
	            if(f.isFile()){//ask 2 overwrite
	            	p("File "+currentpath+" exists. Do you want to overwrite?");
	            	String filen = currentpath;
	            	if(filen.contains("/")) filen=filen.substring(filen.lastIndexOf("/")+1);
	                QuitConfirmJDialog myQuitDialog = new QuitConfirmJDialog(framef, true, " overwrite "+filen, this);
	                myQuitDialog.setVisible(true);//need this for repeat show (if you cancel repeatedly)
	                //QuitConfirmDialog sends colpik.setsavetothisfile(true); - it answers back!
	                if (savetothisfile) mainwindow.log("Overwrite confirmed");
	                else mainwindow.log("Overwrite denied");
	            }else{
	            	savetothisfile =true;
	            	p("New file "+currentpath);
	            }
	            if(savetothisfile){
	        		mainwindow.log("Saving these colors in the order you picked them.\n");
	        	 	if(writeColors2file(currentpath)){
	        	 		mainwindow.log("Colors written to file "+currentpath);
	        	 		banner2.setText("Saved to file "+currentpath);
	        	 		sendfilename();
	        	 		worksaved = true;
	        	 		p("Set worksaved true");
	        	 	}//filename is passed from CPtabs, his is also available    	 
	        	 	else{
	        	 		banner2.setText("Could not save to file "+currentpath);
	        	 		mainwindow.log("Cannot write colors to file "+currentpath);
	        	 	}
	            }
	        }
        }
        
        else if(e.getSource() == done){
        	//doneone++;
        	//done.setEnabled(false);
        	//p("done once "+doneone);
        	//if(doneone>1) return;
        	p("Done clicked, worksaved "+worksaved+" picked is "+picked);
 
        	if(worksaved && !picked){
           		p("Work not done at all? do you want to close ColorPick?");
           		killcolorpik();//don't ask - not working
           		return;
                //QuitConfirmJDialog myQuitDialog_nocol = new QuitConfirmJDialog(framef, true, " no colors", this);
                               //myQuitDialog_nocol.setVisible(true);//need this for repeat show (if you cancel repeatedly)
        	}
        	else if(worksaved) { killcolorpik(); return; }
        	else if(!worksaved){
        		p("Work not saved? do you want to close ColorPick?");
                if(myQuitDialog_nosave==null){
                	myQuitDialog_nosave = new QuitConfirmJDialog(framef, true, " not save colors", this);
                }
                myQuitDialog_nosave.setVisible(true);//need this for repeat show (if you cancel repeatedly)
        	//}
                p("Close now "+close);//get answer from dialog
                if (close){ p ("CLOSE YES!");
        	    	mainwindow.log("close confirmed");
                }else if(!close){ 
                	p ("CLOSE DENIED!");
                	mainwindow.log("close denied so you can save your colors.");
                	return;
                }
        	
        	p("worksaved, "+worksaved+" close "+close);
        	if(close){
        		p("Do close");
        		if(worksaved){
        			p("worksaved and file is "+currentpath);
        		if(!currentpath.equals("~")){//default ie no color file if name is ~
        			mainwindow.setMycolors(true);
        			mainwindow.setColorfile(currentpath);
        		}
        		store();
        		killcolorpik();//not working for some reason repeats the question!!!
        		}  
        	}
        	}
        }
        //if(e.getSource() != done) done.setEnabled(true);//doneone = 0;
    } 
    
    private void killcolorpik(){
   		p("Now get rid of window");
   		store();
		//this.setVisible(false);
   		sendfilename();
		framef.setVisible(false);//does not work
		//framef.dispose();//for some reason we go around again with the quit dialog!
		p("Done. No window");
    }
    
    public void show(){
        picked = false;
        worksaved = true;

    	framef.setVisible(true);
    }
    
    public void setclose(boolean b){//for QuitConfirm...
    	close = b;//set to true if you want to go ahead and close (Done), or false to carry on
    }
    
    private void sendfilename(){
    	//tell the CPtabs what the color file is
    	if(!currentpath.equals("~")){
	    	p("sendfilename "+currentpath);
	    	CPtabs.setColorfile(currentpath);//also sets to prefs
	    	CPtabs.changePieDisplay();
    	}
    }
    
    /** boolean writecolours2file(){
    	DoFileThings dof = new DoFileThings();
    	if(!dof.openSaveFile(filename)){
    		mainwindow.log("Could not open colour file: "+filename);
    		return false;
    	}
    	String astring = "";
    	for(int i=0;i<his.size();i++){
    		String ss=his.get(i);
    		ss+="\n";
    		astring+=ss;
    	}
    	if(!dof.writeLine(astring)){mainwindow.log("Could not write colours out to file: "+filename);return false;}
    	if(!dof.closeSaveFile()){mainwindow.log("Could not close the file : "+filename); return false;}
    	return true;
    }*/


    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    public void createAndShowGUI(CPtabs CPtabs, Dimension oldsize) {
        //Create and set up the window.
        this.CPtabs = CPtabs;
        //Create and set up the content pane.
        JComponent newContentPane = new ColorPicker(log,filename,mainwindow);
        ((ColorPicker) newContentPane).load();//restore oldlocation
        newContentPane.setOpaque(true); //content panes must be opaque
        framef.setContentPane(newContentPane);
        //frame.setLocation(oldlocation);
        //Display the window.
        framef.pack();
        framef.setVisible(true);
    }
    
    public void addNotify ()//overrides 
    {
    	super.addNotify ();
    	if(picked){
    		super.getRootPane().setDefaultButton(d);
    		b.requestFocus();//glow if you've started picking
    	}
    	else{super.getRootPane().setDefaultButton(b);}//Pick button is default to start with
    }
    
    public void setsavetothisfile(boolean b){savetothisfile=b;}//used by QuitConfirmJDialog fo roverwrite file
    

}