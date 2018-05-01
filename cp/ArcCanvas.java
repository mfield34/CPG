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

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Arc2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JTextArea;

public class ArcCanvas  extends Canvas {
		  int Diameter;
		  private Double SegmentAngle;
		  private int NumSegments;
		  private Color myColor;//it this is set the pie is all one color - and need to specify which are blank
		  private int[] whichSegments;//set size to NumSegments, defines which are blank 
		  private boolean MultiColor; 
		  private boolean Labelit;
		  private HifStackString LabelStack;
		  private Font[] fonts = new Font[15];
		  private Font font = new Font("sansserif", Font.PLAIN+Font.BOLD, 10);//default if none set
		  private String colorFilename;//use to pick saved color set
		  private JTextArea log;//passed in
		  
		  private Color[] Colors=null;//set this up from CPtabs using CPGcolors
		  //now for colors

		  public ArcCanvas (Color [] Colors) {
			    setBackground (Color.white); 
			    MultiColor=true;
			    Labelit = false;
			    this.Colors = Colors;
		  }

		  public ArcCanvas (String colorfile, Color [] Colors) {
			    setBackground (Color.white); 
			    MultiColor=true;
			    Labelit = false;
			    this.Colors = Colors;
		  }
		  
		  public void setLog(JTextArea l){
			 log=l;
		  }
		  
		  /** used by CPtabs if colors.txt is empty AND if color.txt not set as colour file when first opened
		   * 
		   * @return
		   */
		  
		  public void setDiameter (int Value) {
		    Diameter = Value;
		  }

		  public void setSegment (int Segments) {
			  //constructor for demo pie uses colours from the Color[] in rotation
			     NumSegments = Segments;
			     SegmentAngle = (double)360/Segments;
	     }
		  
		  public void setSegment (int[] SegmentsArray, int Col){
			  //constructor to set single color pi and specify which segments are blank
			  if(Col>=Colors.length) Col = Col % Colors.length;
			  setSegment (SegmentsArray.length);//so this comes through
			  myColor = Colors[Col];
			  MultiColor=false;
			  whichSegments = SegmentsArray;
		  }
		  
		  public int[] getoffsets(){
			  //new method to take account of labelling going offg the edge.
			  int[] offsets = getresize();
			  int[] ints = new int[offsets.length];
			  for(int i = 0;i<offsets.length; i++){
				  int x = offsets[i];
				  if(x % 2 != 0) x--;
				  Integer closestinteger = x/2;
				  ints[i]=closestinteger;
				  //{10, 10};//make it 20 pix larger in both dimensions 
			  }
			  return ints;
		  }
		  		  
		  public int[] getresize(){//manual, here
			  //new method to take account of labelling going offg the edge.
			  int[] ints = {40, 20};//make it 20 pix larger in both dimensions
			  return ints;
		  }
		  		  
		  public void setFont(int FontNo,String fontchoice){//thes are smaller than in TextCanvas
			  fonts[0] = new Font(fontchoice, Font.PLAIN+Font.BOLD, 20);
			  fonts[1] = new Font(fontchoice, Font.PLAIN, 18);
			  fonts[2] = new Font(fontchoice, Font.PLAIN+Font.BOLD, 16);
			  fonts[3] = new Font(fontchoice, Font.PLAIN, 14);
			  fonts[4] = new Font(fontchoice, Font.PLAIN, 13);
			  fonts[5] = new Font(fontchoice, Font.PLAIN, 12);
			  fonts[6] = new Font(fontchoice, Font.PLAIN, 11);
			  fonts[7] = new Font(fontchoice, Font.PLAIN, 10);
			  fonts[8] = new Font(fontchoice, Font.PLAIN, 9);
			  fonts[9] = new Font(fontchoice, Font.PLAIN, 8);
			  fonts[10] = new Font(fontchoice, Font.PLAIN, 7);
			  fonts[11] = new Font(fontchoice, Font.PLAIN, 6);
			  fonts[12] = new Font(fontchoice, Font.PLAIN, 5);
			  fonts[13] = new Font(fontchoice, Font.PLAIN, 4);
			  fonts[14] = new Font(fontchoice, Font.PLAIN, 3);
			  FontNo--;
			  if(FontNo>14){FontNo=14;}
			  if(FontNo<0){FontNo=0;}
			  font = fonts[FontNo];
		  }
		  
		 public void setSegment (HifStackString SegmentsLabels, int Col){
			  //constructor to set single color pi and specify which segments are blank
			  setSegment (SegmentsLabels.size());//so this comes through
		      int[] Segs = new int[SegmentsLabels.size()];
		      /*for(int i=0; i<SegmentsLabels.size(); i++){
		    	  if(i==0 || i==2) Segs[i]=1;//colour first and third segments, to see where they lie! 1 @ right, up, 3 @ up
		      }*/
		      
		      if(Col<Colors.length)
		    	  myColor = Colors[Col];
		      else{
		    	  //cycle through colours
		    	  Col = Col % Colors.length;
		    	  myColor = Colors[Col];
		      }
			  MultiColor=false;
			  whichSegments = Segs;
			  Labelit=true;
			  LabelStack = SegmentsLabels;
		  }
		  		  
		  public void paint (Graphics g) {//paintComponent not working
			  Graphics2D g2;
			  int Width;
			  int Height;
			  int pi = Arc2D.PIE;
			  
			  Width = getWidth ();//width of canvas, programmed externally
			  Height = getHeight ();

			  g2 = (Graphics2D)g;	    
			  g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			  //this should smooth everything	
			  g2.setFont(font);
			  int HalfDiameter = Diameter;
		      if(Labelit) HalfDiameter = Diameter*2/3;
			  Double startAngle = (double) 0;//start here and increment by extent for start angle, extent
			  int i;
	  	      g2.setColor(Color.black);

	  	      FontMetrics metrics = g2.getFontMetrics();
	  	    	  //first part draws the segments, second is for labelled pies... even if one segment it has to be labelled
	  	      for (i=0;i<NumSegments;i++) {
	  	    	  
		        	if(NumSegments==1){
		        		pi = Arc2D.OPEN;
		        	}
		        	Arc2D Arc1 = new Arc2D.Double((double) ((Width - Diameter) / 2),
		    	              (double) ((Height - Diameter) / 2),
		    	              (double) Diameter, (double)Diameter, 
		    	              (double)startAngle,(double)SegmentAngle, pi);
		        	if(Labelit) { 
		        		Arc1 = new Arc2D.Double((double) ((Width - HalfDiameter) / 2),
		    	              (double) ((Height - HalfDiameter) / 2),
		    	              (double) HalfDiameter, (double)HalfDiameter, 
		    	              (double)startAngle,(double)SegmentAngle, pi);
		        	}
		     		if (MultiColor == true)
		     		{ 
		     			g2.setColor(Colors[i]); 
		     			g2.fill(Arc1);
		     		} 
		     		else {//test if you want this
		     			if(whichSegments[i]==1){
		     			g2.setColor(myColor);
			    		g2.fill(Arc1);}
		     		}
		       		g2.setColor(Color.black);    
		     		g2.draw(Arc1);
		      		startAngle+=SegmentAngle;
		      		int depthR = 0; int depthL = 0;//keep track of depth of labels
		      		/** This section is an add on and writes labels around a smaller pie */
		      		if(Labelit) {

		      			double ThisSegmentAngle = startAngle;//just incremented
		      			String printlabel = LabelStack.get(i);
		      			/* Get dimensions of string */
		      			Rectangle2D rect = metrics.getStringBounds(printlabel, g2);
		      			double depthLet = rect.getBounds().height;
		      			double widthLet = rect.getBounds().width;
		      			double across = (Width/2);//start labelling at centre
		      			double up = (Height/2);//ditto
		      			double ThisAngle=ThisSegmentAngle-( SegmentAngle/2 );//decrease by half to put label in the middle	
		      			int fudge = 4;
		      			//System.out.println("This angle is "+ThisAngle+", Incremental angle is "+SegmentAngle);
			      		double RadianAngle = Math.toRadians(ThisAngle);
		      			ThisAngle = 2*Math.PI - RadianAngle; //System.out.println("became "+ThisAngle);
		      			/*Segments start with line at middle to right at 90* from top vertical and go UP */
			      		if((ThisSegmentAngle-(SegmentAngle/2)) <= 83 || (ThisSegmentAngle-(SegmentAngle/2)) > 277){
			      			/* right half going up from middle then right half going up from bottom */
		      				//System.out.print("<90 or >270 ");
			      			//eg if i == 0, first one
		      				across = across + (HalfDiameter * Math.cos(ThisAngle))-fudge;//space away from circle edge
		      				up = up + (HalfDiameter * Math.sin(ThisAngle));
		      			}
			      		else if(((ThisSegmentAngle-(SegmentAngle/2)) > 83 && (ThisSegmentAngle-(SegmentAngle/2)) <=97) ||//top part
			      				((ThisSegmentAngle-(SegmentAngle/2)) > 263 && (ThisSegmentAngle-(SegmentAngle/2)) <=277)){//bottom part
		      				//System.out.print("<90 or >270 ");
		      				across = across-(widthLet/2);// + (HalfDiameter * Math.cos(ThisAngle));//space away from circle edge
		      				up = up + (HalfDiameter * Math.sin(ThisAngle));
		      			}
		      			else if((ThisSegmentAngle-(SegmentAngle/2)) >97 && (ThisSegmentAngle-(SegmentAngle/2)) <= 263 ){/* left half going down */
		      				//System.out.print(" >90 and <270 ");
		      				across = across + (HalfDiameter * Math.cos(ThisAngle));
		      				across = across - (widthLet)+fudge;//space away from circle edge
		      				up = up + (HalfDiameter * Math.sin(ThisAngle));
		      			}
		      			//System.out.println(i+": "+printlabel+" "+ThisAngle +" degrees => across (-40 centre): "+across+", down (-20): "+up+")");
		      			//up=up+6;
			      		up = up+(depthLet/2);
		      			g2.drawString(printlabel, (int)across, (int)up);//either method... text won't stay still on repaint
		      		}//end if Labelit
	  	      }
		  }
		  
		  public boolean Labelit(){return Labelit;}
		  
		  private void p(String s){if(printok)System.out.println("ArcCanvas:: "+s);}
		  private boolean printok = true;
}
