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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class DoPrefs{
	static JDialog PrefsDialog = null;
	final CPtabs cpgwindow;
    private JCheckBox myQuitPrefButton;
    private JButton savebutton;
    private JButton donebutton=new JButton("Done");
    //private boolean pref = false;
    
    
    private Hashtable prefButtons = new Hashtable();//new for DoPrefs
    /* These retrieved from cpgwindow overlord
     * 
     */
    private Hashtable prefs;
    private Hashtable prefkeys;
    private Hashtable preflegends;
          //key is name of boolean, value is the checkbox
    boolean printok = false;
    private void p (String s ){ if (printok) System.out.println("DoPrefs: "+s); }
    
	public DoPrefs(CPtabs cpgwindow){
		this.cpgwindow = cpgwindow;
		Hashtable [] prefhashes = cpgwindow.getPrefs();//
		
		Font smallerfont;
		String fontString = new JButton().getFont().toString();
		Font oldfont = new JButton().getFont();
		if(fontString.contains("apple")){
			smallerfont = new Font(oldfont.getFamily(),oldfont.getStyle(), (oldfont.getSize()-2));//oldfont.deriveFont(new AffineTransform());
		}	/* end getting smaller font for apple */
		else {smallerfont = oldfont;} // dont change font for other OS

		prefkeys = prefhashes[0];//prefkeys, prefs, preflegends
		prefs = prefhashes[1];
		preflegends = prefhashes[2];
		p("Prefs "+prefs.size()+" "+prefs);//excellent readout of prefs
		p("Pref keys "+prefkeys.size()+"Pref labels "+preflegends.size());
		JPanel prefpanel = new JPanel();
		GridLayout gl = new GridLayout(0,1);// labels too long...2);
		gl.setHgap(2);
		gl.setVgap(0);
		prefpanel.setLayout(gl);//2 cols and rows as needed
		//gl can have specified gaps
		for (int i = 0; i<prefkeys.size();i++){
			//for (Enumeration en = prefkeys.keys(); en.hasMoreElements();){//int i = 0; i<prefkeys.size();i++){
			//String keykey = Integer.parseInt(((Integer)en.nextElement()));//(String)prefkeys.get(i);
			//String key = (String)prefkeys.get(keykey);
			String key = (String)prefkeys.get(i);
			p(i+" "+key);p(" prefs "+prefs.get(key));p(" legend "+preflegends.get(key));
			//p("KEY "+key+" prefkey "+keykey);
			JPanel lpan = new JPanel();
			JLabel l;
			//l.setBorder(null);
			lpan.setLayout(new BorderLayout());
			lpan.setAlignmentX(JPanel.LEFT_ALIGNMENT);
			lpan.setAlignmentY(JPanel.CENTER_ALIGNMENT);
			if(prefs.containsKey(key)){
				l = new JLabel((String)preflegends.get(key));//use legend unless spacer
				l.setBorder(new EmptyBorder(0,20,0,0));//push out from the side
				l.setFont(smallerfont);
				lpan.add(l,BorderLayout.CENTER);//stretch it or layout won't be seen
				JPanel cbpanel = new JPanel();
				lpan.setAlignmentX(JPanel.RIGHT_ALIGNMENT);
				cbpanel.setAlignmentX(JPanel.LEFT_ALIGNMENT);
				JCheckBox cb = new JCheckBox();
				cb.setSelected((Boolean)prefs.get(key));
				cb.setName(key);
				cb.setBorder(null);
				cb.setSelected((Boolean)prefs.get(key));//recover old pref
				cb.addActionListener(new java.awt.event.ActionListener(){
		            public void actionPerformed(java.awt.event.ActionEvent evt){
		                checkboxhandler(evt);
		             }
		        });
				cbpanel.setLayout(new BorderLayout());//sticky! get checkboxes to stick to the side and label to take the rest of the spaec
				cb.setBorder(new EmptyBorder(0,20,0,0));//push out from the side
				cbpanel.add(cb, BorderLayout.WEST);
				cbpanel.add(lpan, BorderLayout.CENTER);//take rest of space
				prefpanel.add(cbpanel);//left
				
				//prefpanel.add(lpan);//right
				p(key+" added cb and key label "+cb);
			}else{ 
				l = new JLabel(key);//spacer
				l.setBorder(new EmptyBorder(0,30,0,0));//push out from the side
				lpan.setAlignmentX(JPanel.LEFT_ALIGNMENT);
				lpan.add(l,BorderLayout.CENTER);
				prefpanel.add(lpan);//it's a header, bigger font
				p(key+" LABEL only ");
			}
		}

		if(PrefsDialog == null){
	        PrefsDialog = new JDialog();
	        PrefsDialog.setResizable(true);
	        PrefsDialog.setTitle("GO helper Preferences");
	        PrefsDialog.setSize(300,300);
	        Dimension screensize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
	        int width = new Double((screensize.getWidth() - 350)/2).intValue();
	        int height = new Double((screensize.getHeight()/2)/-150).intValue();
	        PrefsDialog.setLocation(width,height);
	
	        PrefsDialog.getContentPane().add(prefpanel);
	         savebutton = new JButton("Save");
	        PrefsDialog.getRootPane().setDefaultButton(savebutton);
	        JPanel buttons = new JPanel();
	        buttons.add(savebutton);
	        buttons.add(donebutton);
	        PrefsDialog.getContentPane().add(buttons, java.awt.BorderLayout.SOUTH);
	        addlis();//to save and done buttons
	    }
	    PrefsDialog.setVisible(true);
    }
	
	private void addlis(){
        savebutton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent evt){
                savebuttonhandler(evt);
             }
        });
	    donebutton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent evt){
                donebuttonhandler(evt);
             }
        });
	}
	
	/** Add specific handlers here (API)
	 *  Save will pass these to cpgwindow to save and serialize
	 *  but this checkboxhandler actually passes to the applications themselves for update.
	 * 
	 * @param evt
	 */
	
	public void checkboxhandler(ActionEvent evt){
		String source = ((JCheckBox)evt.getSource()).getName();
		boolean pref = false;
		if(((JCheckBox)evt.getSource()).isSelected()) pref = true;
		prefs.put(source, pref);//change this set of prefs (but nothing done unless you save.
		p(source+" set to "+pref);
	}
	
	public void savebuttonhandler (ActionEvent evt){
        cpgwindow.setPrefs(prefs);//set cpgwindow copy ready to serialize on quit
    }
	
	public void donebuttonhandler(ActionEvent evt){
		PrefsDialog.setVisible(false);
	}
	
	
 }
