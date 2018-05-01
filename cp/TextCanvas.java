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
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;



class TextCanvas extends Canvas {
	  boolean printok = false;
	  private void p(String x){ if(printok){System.out.println("TextCanvas: "+getText()+": "+x+"\n");}}
	  
	  private boolean putYellowbox=false;//for YELLOW boxes
	  private Dimension putDivvyDimension = null;//ditto: when resized the dividers should stay the same even if text box has grown
	  private String Tex;
	  private Font[] fonts = new Font[15];
	  private Font font = new Font("serif", Font.ITALIC+Font.BOLD, 20);
	  private double rotateangle;
	  private int Colr;
	  private char layout;//c for centre, r for right, default is left
	  private String[] fontdescription = { //generic fonts only
			  "Serif", "SansSerif", "Monospaced"//, "Dialog", "DialogInput" in Mac these last are just SansSerif
			  //"Arial", "Times New Roman", "Courier New", "Comic Sans", "Gill Sans" 
	  };
	  private String fontchoice="";//sent in
	  private Color[] Colors;// = getColors();//have to have colourFilename to set
	  private String colourFilename="";
	  private int Textlen=0;//measure width according to text
	  
	  public int getTextlen(Graphics g){
		   if(Textlen>0)
				  return Textlen;
		   Graphics2D g2 = (Graphics2D) g;//this.getGraphics();
		   FontMetrics metrics = g2.getFontMetrics();
		   Rectangle2D rect = metrics.getStringBounds(Tex, g2);
		   return (int) rect.getWidth();	
	  }
	  
	 /* public int[] getoffsets(){
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
	  }*/
	  		  
	  public int[] getresize(Graphics g){//text based, here
		  //new method to take account of labelling going offg the edge.
		  int[] ints = {getTextlen(g), getHeight()};//make it 20 pix larger in both dimensions
		  if(rotateangle==0)
			  return ints;
		  else{
			  return new int[]{ints[1],ints[0]};//good but height which becomes width is erroneous in paint!
		  }
	  }
	  		  
	  public TextCanvas (Color [] c ) {
		    setBackground (Color.white); 
		    setForeground(Color.white);
			  Colr=0;
			  Colors=c;//getColors();
	  }
	   
	  public TextCanvas (String filecolours, Color [] c) {
		  colourFilename= filecolours;
		  setBackground (Color.white); 
		  setForeground(Color.white);
		  Colr=0;
		  Colors=c;
	  }
	   
	  public TextCanvas (char Layout, Color [] c) {
		    setBackground (Color.white); 
		    setForeground(Color.white);
		    layout = Layout;
		    Colr=0;
			Colors=c;
	  }
		  
	  
	  private void setFonts(){
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
	  }
	  
	  public Font getFont(int FontNo){//for external use
		  Font f = fonts[FontNo];
		  return f;
	  }
	  
	  public String[] getFontDescription(){//for ShowTable to set up choice boxs
		  String[] copyFontDesc = fontdescription;
		  return copyFontDesc;
	  }
	  
	  public String getText(){
		  return Tex;
	  }
		  
	  public void setText (String myText, String fontplease, int j, double angle, char leftrightcenter) {
		    Tex = myText;
		    fontchoice = fontplease;
		    setFonts();
		    font = fonts[j];
		    rotateangle=angle;
		    layout = leftrightcenter;
	  }

	  public void setText (String myText, String fontplease, int j, double angle, int Col, char leftrightcenter) {
		    Tex = myText;
		    fontchoice = fontplease;
		    setFonts();
		    font = fonts[j];
		    rotateangle=angle;
		    Colr=Col;
		    layout = leftrightcenter;
	  }
	  
	  public double getAngle(){ return rotateangle; }

	  //Color[] testcolors = {Color.red,Color.green,Color.blue,Color.orange};
	  //int testcolorno = 0;
	  
	  /**Problems with painting labels and recentering are found HERE
	   * Use the layout options so that the labels don't center when you repaint (move scrollbar or print)
	   * When repainting (see paint in CoulsonPlotPrintable) we move the graphics to the right place and set size and placement from outside
	   * so it would require a different method to paint rectangles with text (if text is oversized, rectangles are bigger too, 
	   * as both clip and this canvas are enlarged!
	   */
	  public void paint (Graphics g) { //get rid of curtailment...
		Graphics2D g2;	    
	    double theta;

	    g2 = (Graphics2D) g;
	    g2.setFont(font);
	    AffineTransform origtransa = g2.getTransform();
	    AffineTransform transa = new AffineTransform();
		
		Color color = Colors[Colr];
		g2.setColor(color);
	    
	    FontMetrics metrics = g2.getFontMetrics();
	    Rectangle2D rect = metrics.getStringBounds(Tex, g2);
	    Rectangle2D rectthis = this.getBounds();
	    
	    int width = (int) rectthis.getWidth();
	    int height = (int) rectthis.getHeight();	
	    int textwidth = getTextlen(g);
	    int textheight = (int)rect.getHeight();	
	    Textlen = textwidth;//in case needed from outside
	  ///NOTE for 270, text swings out as though around a clock... so need a translation also...
	    int across; int up;
	    //have to draw box first if an transform applied
	    if(putYellowbox && rotateangle>0){//need to do this here but if angle is 0 do it with text, below
	    	if(width>=textwidth)
	    		g2.drawRect(0,0, (width-1),(height-1));
	    	else
	    		g2.drawRect(0,0, width-1, height-1);
		} 

		theta = (2 * Math.PI * rotateangle) / 360;
		transa.setToRotation( theta, 0.0, 0.0);
		g2.transform(transa);

		if(layout == 'l'){ //used for left justified Species labels on left
	    	p("Layout l for "+getText());
	    	if(rotateangle==0){
		    	//KEEP since at least they DON"T MOVE when you redraw or touch the scroll bar!
		    	across = 10;//increase or reduce start space in front of label...
		    	up = height/2+textheight/2;//exact center and drop text a little
		    	g2.drawString(Tex,across,up);
		    	
		    	if(putYellowbox){
		    		g2.drawRect(0,0,width-1, height-1);
		    	}
	    	}
	    	else if(rotateangle>0){//270
	    	//transpose
	    		across = -height+10;
	    		up = width/2+textheight/2;//dead centre... (x)
		    	g2.drawString(Tex,across,up);
		    	//if(putYellowbox){
		    	//	g2.drawRect(0,0,height-1, width-1);
		    	//}
	    	}
	    }
	    else{
	    	p("not l layout... '"+layout+"' for "+getText());
	    	if(rotateangle==0){//used for centrally justified Protein labels above pie keys
	    		p("draw "+getText()+"at 0");
 		    	if(layout=='c') g2.drawString(Tex,(width/2-(textwidth/2)),height/2);//center it
 		    	else //if(layout == 'l') 
 		    		g2.drawString(Tex,2,height/2);//start half way down and a little way in
		    	if(putYellowbox){
		    		g2.drawRect(0,0,width-1, height-1);
		    	}
	    	}
	    	else if(rotateangle>180){//270 degrees used for centrally justified 180 labels left
	    		p("draw "+getText()+" for 180 layout "+layout);
				up = width/2+textheight/2;//dead centre... (x)
				//start at Bottom = -height! 
				across = 0;//-(height);//on 270 vertical labels, affects height (y) - this centers
				if(layout=='c'){ //used for vert labels left
					across = -(height)+(height/2-textwidth/2);
				}
				g2.drawString(Tex,across,up);//center it
				
		    	//if(putYellowbox){
		    		//g2.drawRect(0,0,width-1, height-1);
		    	//}
	    	}	  //should be pointing up and away like 10pm
	     }
	  }
	  
	  public void doYellowbox(boolean yellowbox){
		  putYellowbox = yellowbox;
	  }
	  
	  public char getLeftrightcenter(){ return layout; }

}
