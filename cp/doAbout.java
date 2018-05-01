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

//Make sure that ".cpg/Cpg.png"is a real image for the About

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class doAbout{

	public JDialog AboutDialog = null; //only one About needed
    private String VERSION = "1.62";
    /** Change log 
	 *  1.61 - fixed bug in 4 string header missing out top line
	 *  	 - changed web link on doAbout screen
	 *  1.62 To do:
	 *  Make the above generic; fixing the save to keep the whole plot however big
	 *  remove the fit to window if you resize it...
	 *  Change emails on README_CPG
     */
    private String DevVersion = "1.62";//Remove line from single dot
    private String lastUpdate = "14 August 2014";
    private String path2icon = ".cpg/Cpg.png";
    //button links to website
    private String website = "https://dl.dropboxusercontent.com/u/6701906/Web/Sites/Labsite/Software.html";
	Link link = new Link();
	
	public String getDevVersion (){return DevVersion;}

  	public doAbout(){
         if(AboutDialog == null){
             AboutDialog = new JDialog();
             AboutDialog.setResizable(false);
             AboutDialog.setTitle("About CPG");
             AboutDialog.setSize(350,350);
             Dimension screensize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
             int width = new Double((screensize.getWidth() - 350)/2).intValue();
             int height = new Double((screensize.getHeight()/2)/-150).intValue();
             AboutDialog.setLocation(width,height);
             JLabel myAppTitle = new JLabel();
             myAppTitle.setHorizontalAlignment(myAppTitle.CENTER);
             JLabel myAppStuff = new JLabel();
      			Font smallerfont = new Font(myAppStuff.getFont().getFamily(),myAppStuff.getFont().getStyle(), (myAppStuff.getFont().getSize()-2));//oldfont.deriveFont(new AffineTransform());
      			myAppStuff.setHorizontalAlignment(myAppTitle.CENTER);
      			myAppStuff.setFont(smallerfont);
      			JPanel jp = new JPanel();
              jp.setLayout(new GridLayout(0,1)); 
              JLabel myAppStuff1 = new JLabel();
              JLabel myAppTitle1 = new JLabel();
              myAppTitle1.setFont(smallerfont);
              JPanel myAppStuff2 = link2web();
              myAppTitle1.setHorizontalAlignment(myAppTitle.CENTER);
              myAppStuff1.setHorizontalAlignment(myAppTitle.CENTER);
                         myAppStuff1.setFont(smallerfont);
            
              JButton image = new JButton(new ImageIcon(path2icon));
              JPanel imagePanel = new JPanel();
              imagePanel.add(image);
              image.setSize(imagePanel.getSize());
              myAppTitle.setText("Coulson Plot Generator Release: "+VERSION);
              myAppTitle1.setText("Version "+DevVersion+" "+lastUpdate);
              myAppStuff.setText("Software by Helen Imogen Field copyright 2009-");
              myAppStuff1.setText("Coulson plot by Richard Coulson copyright 2009-");
              //myAppStuff2.setText("Data from Mark Field");
              jp.add(myAppTitle1);
              //jp.add(myAppStuff2);
              myAppStuff.setForeground(Color.gray);
              myAppStuff1.setForeground(Color.gray);
              jp.add(myAppStuff);
              jp.add(myAppStuff1);
             AboutDialog.getContentPane().setLayout(new GridLayout(0,1));
             AboutDialog.getContentPane().add(myAppTitle);//HEADER
             AboutDialog.getContentPane().add(imagePanel);//picture
             AboutDialog.getContentPane().add(myAppStuff2);//button
             AboutDialog.getContentPane().add(jp);//other copyright and dev version,date strings
 
         }
         AboutDialog.setVisible(true);
     }
  	
  	private JPanel link2web(){
  		Font smallerfont = link.smallerfont();
		JPanel buttonPan = new JPanel();
		JButton getWeb = new JButton("Website link");
		getWeb.setFont(smallerfont);
		buttonPan.add(getWeb);
		getWeb.addActionListener(new ActionListener(){
	           public void actionPerformed(ActionEvent e) {
	       				link.openBrowser(website);
	           }
		});
		return buttonPan;
	}
  	
  }
