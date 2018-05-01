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


import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/* Class to export a link which opens up a hyperlink in your web browser (as a new window) */

public class Link {
	
	boolean printok = false;
	private void p(String s){ if (printok) System.out.println("Link:: "+s); }
    
	public JButton GoLink (String url, String embeddingtext, String linktext, String followtext) throws URISyntaxException {
        final URI uri = new URI(url);

        // JButton
        JButton button = new JButton();
        button.setText("<HTML>"+embeddingtext+" <FONT color=\"#000099\"><U>"+linktext+"</U></FONT>"+followtext+"</HTML>");
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setBorderPainted(false);
        button.setOpaque(false);
        button.setBackground(Color.WHITE);
        button.setToolTipText(uri.toString());
        button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                        open(uri);
                }
        });
         return button;//looks like a hyperlink with blue underlined text
    }

	public JButton buttonLink (String url, String embeddingtext, String linktext, String followtext, Font smallerfont) throws URISyntaxException {
        final URI uri = new URI(url);

        // JButton
        JButton button = new JButton();
        button.setName(url);
        button.setText("<HTML>"+embeddingtext+" <FONT color=\"#000099\"><U>"+linktext+"</U></FONT>"+followtext+"</HTML>");
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setFont(smallerfont);
        button.setForeground(Color.red);
       // button.setBorderPainted(false);
        //button.setOpaque(false);
       // button.setBackground(Color.WHITE);
        button.setToolTipText(uri.toString());
        button.addActionListener(new ActionListener(){
         public void actionPerformed(ActionEvent e) {
                    open(uri);
            }
        });
        return button;
    }

    private void open(URI uri) {
      	        if (Desktop.isDesktopSupported()) {
                Desktop desktop = Desktop.getDesktop();
                try {
                        desktop.browse(uri);
                } catch (IOException e) {
                	p("IOException ")	;e.printStackTrace();
                }
        } else {
           	System.out.println("Desktop.isDesktopSupported() - NOT")	;
        }
    }
    
    public void openBrowser(String url) {
    	URI uri=null;
    	try{
    		uri = new URI(url);
    	}catch(URISyntaxException us){
    		System.out.println("URI syntax exception in "+url);
    		us.printStackTrace();
    	}
	        if (Desktop.isDesktopSupported()) {
        Desktop desktop = Desktop.getDesktop();
        try {
        	if(uri!=null)
                desktop.browse(uri);
        } catch (IOException e) {
        	p("IOException ")	;e.printStackTrace();
        }
	} else {
   	System.out.println("Desktop.isDesktopSupported() - NOT")	;
	}
}


      
    /* Return the complete link item. It is a button lying in a panel
     *  - you have already initialized a link
     *  - used by GOontology
     */
    public JPanel getLinkbutton(String url, String embeddingtext, String linktext, String followtext, Font font){
    	JPanel J = new JPanel();
    	J.setBackground(Color.white);
    	try{
    		JButton B = GoLink(url, embeddingtext, linktext, followtext);
    		B.setFont(font);
    		B.setBackground(Color.white);
    		J.add(B);
    		p("Added button for "+url+" ie "+B);
    	}catch(Exception e){
    		p("Could not make link beacuse: "+e);
    		e.printStackTrace();
    	}
    	p("panel with link is "+J);
    	//J.setSize(new Dimension(10,100));
    	return J;
    }
    
    public Font smallerfont(){
		  Font smallerfont;
		  Font oldfont = new JButton().getFont();
		  String fontstring = new JButton("hello").toString();
			if(fontstring.contains("apple")){
				smallerfont = new Font(oldfont.getFamily(),oldfont.getStyle(), (oldfont.getSize()-2));//oldfont.deriveFont(new AffineTransform());
			}	/* end getting smaller font for apple */
			else {smallerfont = oldfont;} // dont change font for other OS
			return smallerfont;
	  }
}
