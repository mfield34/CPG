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

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ByteLookupTable;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.awt.image.LookupOp;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.TreeSet;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.apache.batik.dom.svg.SVGDOMImplementation;

/** formats contains the save items that go with Component: 
 *  format is the string that describes the format
 *  Save as button added in 1.1 Patch1 (1.4.7)
 * @author lime
 *
 */
public class SaveImage extends Component implements ActionListener {

	  String descs[] = { "Original", "Convolve : LowPass", "Convolve : Sharpen",
	      "LookupOp", };

	  int opIndex;
	  private BufferedImage bi, biFiltered;
	  private String format = "jpg";//default!
	  int w, h;
	  double scale=1;
	  JFrame f;
	  public static final float[] SHARPEN3x3 = { // sharpening filter kernel
	  0.f, -1.f, 0.f, -1.f, 5.f, -1.f, 0.f, -1.f, 0.f };

	  public static final float[] BLUR3x3 = { 0.1f, 0.1f, 0.1f, // low-pass filter
	                                                            // kernel
	      0.1f, 0.2f, 0.1f, 0.1f, 0.1f, 0.1f };
	  
	  String scales[] = {"0.2","0.4","0.6","0.8","1","1.2","1.4","1.6","2","3","4","5","6","8","10"};//for scaling
	  //need big numbers to get nice resolution!
	  
	  CoulsonPlotPrintable CPP;
	  
	  JButton saveButton = new JButton("Save as..");
	  
	  private void changeScale (double change){scale = change;}

	  public SaveImage(CoulsonPlotPrintable CPP) {
		   f = new JFrame("Save Coulson Plot");
		    f.addWindowListener(new WindowAdapter() {
		      public void windowClosing(WindowEvent e) {
		      }
		    });
		    this.CPP = CPP;
		    w=CPP.getBounds().width;
		    h = CPP.getBounds().height;
	    	paint();//actually paints the CPP
	    	
		    JComboBox choices = new JComboBox(this.getScales());
		    choices.setActionCommand("Scale");//SetFilter
		    choices.setSelectedItem("1");//otherwise you have to select to get anything: top item means nothing hidden
		    choices.addActionListener(this);
		    JComboBox choices1 = new JComboBox(this.getDescriptions());
		    choices1.setActionCommand("SetFilter");
		    choices1.addActionListener(this);
		    JComboBox formats = new JComboBox(this.getFormats());
		    formats.addItem("svg");
		    formats.setActionCommand("Formats");
		    formats.addActionListener(this);
		    JPanel panel = new JPanel();
		    panel.add(new JLabel("Scale "));
		    panel.add(choices);
		    panel.add(choices1);
		    panel.add(new JLabel("Format"));
		    panel.add(formats);
		    formats.setSelectedItem(format);
		    panel.add(saveButton);
		    saveButton.addActionListener(this);
		    f.add("South", panel);
		    f.pack();
		    f.setVisible(true);
		  }

	  /** This paints to the saved file
	   * 
	   */
	  public void paint(){
			CPP.setScale((float)scale);
			//if(scale<1)
			scale=1;//let CPP paint method take care of scaling
	    	Rectangle r = CPP.getBounds();
		   	bi = new BufferedImage((int)scale*(r.width), (int)scale*(r.height),BufferedImage.TYPE_INT_RGB);
	    	Graphics2D g = (Graphics2D) bi.getGraphics();
	    	//System.out.println("SAVEIMAGE Scale is "+scale);
	    	/* May need to make this a feature that is user configurable */
	    	g.scale(scale,scale);//for some reason paints a very small image to file
	    	/* Without paint, background is black! but with it get those top labels! */
	    	/* end of paint what CPP paints - now to get it to repaint all components */
			g.setColor(Color.white);
			g.setBackground(Color.white);
	    	CPP.paint(g); //explicit method newly implemented!
	  }

	  public Dimension getPreferredSize() {
	    return new Dimension(w, h);
	  }

	  String[] getDescriptions() {
		    return descs;
		  }

	  String[] getScales() {
		    return scales;
		  }

	  void setOpIndex(int i) {
	    opIndex = i;
	  }

	  public void paint(Graphics g) {
	    filterImage();
	    Graphics2D g2 = (Graphics2D)g;
	    
	    g2.drawImage(biFiltered, 0, 0, null);
	    
 
	  }

	  int lastOp;

	  public void filterImage() {
	    BufferedImageOp op = null;

	    if (opIndex == lastOp) {
	      return;
	    }
	    lastOp = opIndex;
	    switch (opIndex) {

	    case 0:
	      biFiltered = bi; /* original */
	      return;
	    case 1: /* low pass filter */
	    case 2: /* sharpen */
	      float[] data = (opIndex == 1) ? BLUR3x3 : SHARPEN3x3;
	      op = new ConvolveOp(new Kernel(3, 3, data), ConvolveOp.EDGE_NO_OP, null);

	      break;

	    case 3: /* lookup */
	      byte lut[] = new byte[256];
	      for (int j = 0; j < 256; j++) {
	        lut[j] = (byte) (256 - j);
	      }
	      ByteLookupTable blut = new ByteLookupTable(0, lut);
	      op = new LookupOp(blut, null);
	      break;
	    }

	    /*
	     * Rather than directly drawing the filtered image to the destination,
	     * filter it into a new image first, then that filtered image is ready for
	     * writing out or painting.
	     */
	    biFiltered = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
	    op.filter(bi, biFiltered);
	  }

	  /* Return the formats sorted alphabetically and in lower case */
	  public String[] getFormats() {
	    String[] formats = ImageIO.getWriterFormatNames();
	    TreeSet<String> formatSet = new TreeSet<String>();
	    for (String s : formats) {
	      formatSet.add(s.toLowerCase());
	    }
	    return formatSet.toArray(new String[0]);
	  }

	  public void actionPerformed(ActionEvent e) {
		  if(e.getSource() instanceof JComboBox){
		    JComboBox cb = (JComboBox) e.getSource();
		    if (cb.getActionCommand().equals("Scale")) {
		    	double newscale = Double.parseDouble((String)cb.getSelectedItem());
		    	changeScale(newscale);
		    	//System.out.println("Change scale to "+newscale+" ("+scale+")");
		    	//setOpIndex(0);
			      //repaint();
			      paint();
			    }	    
		    if (cb.getActionCommand().equals("SetFilter")) {
				      setOpIndex(cb.getSelectedIndex());
				      //repaint();
					  filterImage();
				      paint();
				    }
			else if (cb.getActionCommand().equals("Formats")) {
		      /*
		       * Save the filtered image in the selected format. The selected item will
		       * be the name of the format to use
		       */
		       format = (String) cb.getSelectedItem();
		      /*
		       * Use the format name to initialise the file suffix. Format names
		       * typically correspond to suffixes
		       */
		       
			}
		  }//end of JComboBox items
		else {
			if(e.getSource() == saveButton){
				File saveFile = new File("savedimage." + format);
				JFileChooser chooser = new JFileChooser();
				chooser.setSelectedFile(saveFile);
				int rval = chooser.showSaveDialog(saveButton);//cb
				if (rval == JFileChooser.APPROVE_OPTION) {
					saveFile = chooser.getSelectedFile();
			        /*
			         * Write the filtered image in the selected format, to the file chosen
			         * by the user.
			         */
			        if(bi == null){
			        	System.out.println("No image to save: will hang");
			        	CPP.log("No image to save: will hang\n");
			        	System.exit(0);
			        }
			        if(!format.equals("svg")){
			        try {
			          ImageIO.write(bi, format, saveFile);//bi is null
			         	this.setVisible(false);
			        } catch (IOException ex) {
			        	System.out.println("Can't save image: "+ex);
			        	CPP.log("Can't save image because: "+ex+"\n");
			        	//ex.printStackTrace();
			        } catch (Exception ex){
			        	CPP.log("Can't save image: "+ex+"\n");
			        	//ex.printStackTrace();
			        }
			        }
			        else{
			        	//svg format 
			        	SVGgenerator svggen = new SVGgenerator();
			        	svggen.setUpSVG(CPP, saveFile);
			        	svggen.generateSVG();
			        	
			        }
			        f.setVisible(false);
			    }
			}
		}//end of non-JComboBox items
	  };
	  

	  
	  

}