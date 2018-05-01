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
 * Current behaviour - 1.51 First time drawn is set to the scale you set
 * When resized, the plot is fit to the window but the first resize is blank.
 * Can recover by re-resizing the window or zoom in or out (apple+/-)
 */

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;

public class printit extends JFrame 
	implements ActionListener,
	WindowListener,
	ComponentListener,
	Serializable//save position of the Coulson plot frame or else it's annoying
	{
  /*public static void main(String[] args) {
    new printit();
  }*/
	
	
	
	/* Items to serialize */
	static String gowindowfile = ".cpg/coulsonplotprintable";

	private static Point oldlocation;
	private static Dimension oldscreensize;
	private static boolean prefasktoclose = true;//remember you asked it to close (FinderIntegration for Mac only)
	private static String cppsavepath = "~";//store path to get file from last time... (see store()) .. this is default location (/Users home directory)

	private boolean gaps4Row2;//gap between Row2 separators if you check (or not)
	private boolean gaps4Row1;//NO gap between Row1 separators if TRUE if if checked
	private int ColSep;//separateion of cols
	private CPGcolors CPGcolors;

  private PageFormat mPageFormat;
  public CoulsonPlotPrintable coul;
  HifStackString v;//table
  private boolean yellowbox;
  private boolean cross;
  String path; JTextArea parentlog; JTable datain;
  /** these items are all passed in from ShowTable... to CoulsonPlotPrintable */
  int gap;  int vert;  int high;  int fontsz; int diam; boolean addvertlab; boolean addvertlab2;//set for user choosing
  String fontchoice;String fontchoice2;
  float shrinkheight = 1; float shrinkwidth = 1;//calculate expected size then shrink output accordingly, pass to CPP
  String colourFilename;
  boolean useMyColours;
  boolean firstfittowindow = true;//use once
  boolean firstresize = false;
 
  public printit(HifStackString vv, String pathv, JTextArea parentlogv, JTable datainv, int a, int b, int c, int d, int e, boolean AVL,boolean AVL2,boolean YEL,boolean cr,
		  boolean g, boolean gg, int colsep, String FC,String FC2,CPGcolors cols) {
	    super("Coulson Plot");
        addComponentListener(this);
        addWindowListener(this);
        CPGcolors = cols;
	    //colourFilename = ColourFile;//useMyColours = wwCol;
		v = vv;path=pathv;parentlog=parentlogv;datain=datainv;
	    gap=a; high=b; diam=c; fontsz=d; vert=e; addvertlab=AVL;addvertlab2=AVL2;ColSep = colsep; fontchoice=FC;fontchoice2=FC2;//working, so must set variables before createUI();
	    yellowbox = YEL;cross = cr; gaps4Row2 = g;gaps4Row1 = gg;
		createUI(b);
	    PrinterJob pj = PrinterJob.getPrinterJob();
	    mPageFormat = pj.defaultPage();
	    setVisible(true);
	    p("Colset is "+ColSep);
	}
  
	private void setLocations(){
		if(oldlocation !=null)
			this.setLocation(oldlocation);
		if(oldscreensize !=null)
		 this.setSize(oldscreensize);
	}
	

  protected void createUI(int CanvasH) {
      String delim = ",";//determine this
  	  String ss = v.get(4);//should have all labels for width setting
  	  if(ss.contains("\t") && !ss.contains(",")){delim="\t";}
  	  String[] s=ss.split(delim);
  	  int overallwidth = CanvasH*(3+2*s.length);
  	  int overallheight = CanvasH*v.size();
     // System.out.println("overallwidth, overallheight"+overallwidth+", "+overallheight);
  	  if(System.getProperty("os.name").contains("Mac"))
  			  System.setProperty("apple.laf.useScreenMenuBar", "true");//puts menu in top window not app
      
 	    Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
   	 	if(screen.width<overallwidth) {
   	 		//shrink factor
   	 		shrinkwidth = (float)screen.width / (float)overallwidth;
   	 		overallwidth = screen.width;
   	 	}
  	 	if(screen.height<overallheight)  {
  	 		shrinkheight = (float)screen.height/(float)overallheight;
 	 		overallheight = screen.height;
  	 	}
     setSize(overallheight,overallwidth);
    center();

	try{
		load();//recover things from previous session
	}catch(Exception e){
		System.out.println("Could not save your screen location or filename because "+e);
		//e.printStackTrace();///this stops the program so use to debug
	}
	setLocations();//to last known positions
    // Add the menu bar.
	/** Specialised menubar for the Coulson Plot itself
	 * 
	 */
    JMenuBar mb = new JMenuBar();
    JMenu file = new JMenu("File", true);
    file.add(new FilePrintAction()).setAccelerator(
        KeyStroke.getKeyStroke(KeyEvent.VK_P, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));//Event.CTRL_MASK));
    file.add(new FilePageSetupAction()).setAccelerator(
            KeyStroke.getKeyStroke(KeyEvent.VK_U, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));//Event.CTRL_MASK| Event.SHIFT_MASK));
    file.add(new FilePageSaveAction()).setAccelerator(
            KeyStroke.getKeyStroke(KeyEvent.VK_S, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));//Event.CTRL_MASK | Event.SHIFT_MASK));
//    file.add(new CopyPageAction()).setAccelerator(
  //  		KeyStroke.getKeyStroke(KeyEvent.VK_C,Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
    file.add(new CloseAction()).setAccelerator(
    		KeyStroke.getKeyStroke(KeyEvent.VK_W,Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
/**
    JMenuItem jmi = new JMenuItem("Copy");
	jmi.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C,
	    Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
*/
  //
    file.addSeparator();
    //save as...not working - see SaveImage.java works for jpgs
   /** file.add(new FilePageSaveAction()).setAccelerator(
            KeyStroke.getKeyStroke(KeyEvent.VK_S, Event.CTRL_MASK
                | Event.SHIFT_MASK));
    file.addSeparator();*/
    mb.add(file);

    new CPGmenu(this).getMenu("View", mb);//creates View menu and adds it //automatic, above mb.add(viewMenu);
    setJMenuBar(mb);

    // Add the contents of the window.
    /* This is where CoulsonPlotPrintable actually creates the figure */
    if(gap>0 && high>0 && diam>0 && fontsz>0 && vert>0){
    	p("Doing a plot at "+gap+" "+high+"\n&&&& colgap "+ColSep);
    	///"Users/hif/Documents/workspace/CoulsonPlot/imogenart/cp/colors.txt"//works
        coul = new CoulsonPlotPrintable(v, path, parentlog, datain, gap, high, diam, fontsz, vert, addvertlab,addvertlab2,yellowbox,cross, gaps4Row2,gaps4Row1,ColSep,shrinkheight, shrinkwidth, fontchoice, fontchoice2, CPGcolors);   	
    }
    scrollPane = new JScrollPane(coul);
    scrollPane.setAutoscrolls(true);
    //paints over the borders
    add(scrollPane);
    

    // Exit the application when) the window is closed.
    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
    	  store();
      }
    });
  }
  
  JScrollPane scrollPane;
  
  public void shrinkfactor(){
	  shrinkwidth=1;
	  shrinkheight=1;
	  
	  
  }

  protected void center() {
    Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
    Dimension us = getSize();
    int x = (screen.width - us.width) / 2;
    int y = (screen.height - us.height) / 2;
    setLocation(x, y);
  }
  
  

  public class FilePrintAction extends AbstractAction {
    public FilePrintAction() {
      super("Print and Save as pdf");
    }
    
    /* This method needed to generate the Coulson Plot figure when using the print (and save to PDF) menu item */
    public void actionPerformed(ActionEvent ae) {
    	
    	
        ///System.out.println("About to PRINT AAA in FilePrintAction");
        PrinterJob pj = PrinterJob.getPrinterJob();
      ComponentPrintable cp = new ComponentPrintable(coul,shrinkwidth,shrinkheight);

  	int iWidth = coul.getPreferredSize().width;
	int iHeight = coul.getPreferredSize().height;
	/** Works... after a fashion
	Image i = CPP.createImage(iWidth,iHeight);
    bi = (BufferedImage) i;// this creates a white image of the JFrame?
    */
	/* This next part prints exactly what is printed with the Print option */
	//BufferedImage bi = new BufferedImage(iWidth,iHeight,BufferedImage.TYPE_INT_RGB);
	//Graphics2D g = (Graphics2D) bi.getGraphics();//if coul.getGraphics,prints over the top of intself which print selected
	/* Without paint, background is black! but with it get those top labels! */
	//CPP.paint(g); // uses paint to get top labels only (in Snow Leopard)
	/* end of paint what CPP paints - now to get it to repaint all components */
	//coul.paint(g); // use paint to get top labels only (in Snow Leopard)

      //pj.setPageable(cp);
      pj.setPrintable(coul, mPageFormat);//set to coul not cp... and use paint therein
     // Book b = new Book();
     // b.append(cp,new PageFormat());
      //pj.setPageable(b);
      //PageFormat pf = pj.defaultPage();
      //System.out.print("printit actionPerformed: Format of page is "+pf.toString()+" and after is ");
      //pf.setOrientation(PageFormat.PORTRAIT);
     // System.out.println(pf.toString());
      //Graphics g = pj.getGraphics();
     // System.out.print("now...");
     if (pj.printDialog()) {
        try {
          //  System.out.print("pj print...");
          pj.print();//should use CPP print method
        } catch (PrinterException e) {
          System.out.println(e);
        }
      }
    }
  }

  public void hideWindy(){
	  this.setVisible(false);
  }


  public class FilePageSetupAction extends AbstractAction {
	    public FilePageSetupAction() {
	      super("Page setup...");
	    }

	    public void actionPerformed(ActionEvent ae) {
	    	PrinterJob pj= PrinterJob.getPrinterJob();
	    	mPageFormat = pj.pageDialog(mPageFormat);

	     /* if (pageRenderer != null) {
	        pageRenderer.pageInit(mPageFormat);
	        showTitle();
	      }*/
	    }
	  }


 /* public class CopyPageAction extends AbstractAction {
	    public CopyPageAction() {
	      super("Copy...");
	      System.out.println("About to COPY");
	    }

	    public void actionPerformed(ActionEvent ae) {
	        // saving same graphic as print...
	      
	    }
}*/

  public class FilePageSaveAction extends AbstractAction {
	    public FilePageSaveAction() {
	      super("Save...");
	    }

	    public void actionPerformed(ActionEvent ae) {
		      p("About to SAvE");
		      /* Hand off to saveimage dialog and program, use new paint method in CoulsonPlotPrintable */
	      SaveImage si = new SaveImage(coul);//now saving
	    }
}

 public class CloseAction extends AbstractAction {
    public CloseAction() {
      super("Close");
    }

    public void actionPerformed(ActionEvent ae) {
      hideWindy();
    }
  }

public void actionPerformed(ActionEvent e) {
	// TODO Auto-generated method stub
	p("Why gekko");
}

/* Items for serializable
 */

void store(){//goWindow o, File f){//throws IOException
		File f = new File(gowindowfile);
		if(!f.exists()){
			try{ f.createNewFile();
			p("Created new file "+f.getName());
			}catch(IOException e){p("Could not create file "+f.getName()+" because "+e);}
		}
		try{
			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream (f));            
			Vector goObjects = new Vector();
			goObjects.add(oldscreensize);
			goObjects.add(oldlocation);
			goObjects.add(prefasktoclose);
			goObjects.add(cppsavepath);
			p("store cppsavepath ? "+cppsavepath);
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
			cppsavepath= (String) goObjects.elementAt(3);
	//	System.out.println("Loaded "+goObjects.size()+" objects and path is "+currecpath);
		// so far only the location and size is needed, but could be anything in goWindow object not marked as transient
	}

	/* ComponentListener abstract method implementation */
	
	public void componentMoved(ComponentEvent ce){
		oldlocation = new Point(this.getLocation());//store resize information for next reopen
	}
	
	public void componentHidden(ComponentEvent ce){
		//store current plugin displayed
	}
	
	public void componentResized(ComponentEvent ce){
		p("componentResized");
		//if(firstresize){firstresize=false;componentResized(ce);}
		oldscreensize = new Dimension(this.getSize());
		p("RESIzE coul.firsttime is "+coul.firsttime);
		if(coul!=null){
			if(!coul.firsttime)//first painting wont appear
				coul.nozoom();//resize now works without making painting disappear
		}
	}
	
	public void componentShown(ComponentEvent ce){
	}
	/* End component methods storing window placement */
	/* New methods for WindowListener */
	/* Definately not listening in Mac */
	public void windowClosing(WindowEvent w){
		p("CPP Window closing  - storing");
		store();
	}
	
	public void windowClosed(WindowEvent w){
		try{
			load();
		}catch(Exception e){
			p("Could not save your screen location or filename becuase "+e);
		}
	}
	
	public void windowOpened(WindowEvent w){
	}
	
	public void windowActivated(WindowEvent w){
		p("Window activated firsttime is "+coul.firsttime);
		if(coul!=null){
			coul.firsttime = false;//the RIGHT place
		}
	}
	
	public void windowDeactivated(WindowEvent w){
		p("WindowDeactivated");
	}
	
	public void windowDeiconified(WindowEvent w){
	}
	
	public void windowIconified(WindowEvent w){
	}
	
	private void setLocations(JFrame superframe){
		superframe.setLocation(oldlocation);
		superframe.setSize(oldscreensize);
	}
	


	/** Methods for view menu
	 * 
	*/
	 public void zoom(boolean makebigger){
		 p("zoom (bigger?"+makebigger);
		  if(coul!=null)coul.zoom(makebigger);
	 }
	 
	  
	 public void fittowindow(){	
		 p("START: fittowindow in printit firstfittowindow is "+firstfittowindow);
		  if(coul!=null){
			  coul.fittowindow();//with resize working (componentResized) this is blank FIRST time and recovered on zoom in/out Jul13
			 /* if(firstfittowindow){
				  firstfittowindow=false;
				  //can't get display to happen whatever I try: coul.nozoom(), coul.zoom(), coul.repaint(coul.getGraphics())
				  //have to repeat command
				  Graphics g = scrollPane.getGraphics();
				  g.drawString("Please repeat command", 20, 20);
			  }*/
		  }
	 }

	boolean printok = false;//false if not debuggins
	public void p(String a){if(printok) System.out.println("printit:: "+a);}

}

class ComponentPrintable implements Printable {
  //these are passed from printit
  private Component CPGomponent;
  File saveFile = new File("saveit.test");
  float shrinkheight = 1; float shrinkwidth = 1;//calculate expected size then shrink output accordingly, pass to CPP
	
  boolean printok = true;
  public void p(String a){if(printok) System.out.println("printit:: "+a);}

  //from the web
 /* public static final Dimension A4_SIZE = new Dimension(595, 842);
  public static final Dimension A3_SIZE = new Dimension(842, 595 * 2);
  public static final Dimension A2_SIZE = new Dimension(595 * 2, 842 * 2);
  public static Dimension DEFAULT_SIZE = A4_SIZE;    */

  public ComponentPrintable(Component c, float shrinkw, float shrinkh) {
	  CPGomponent = c;
    shrinkwidth=shrinkw;shrinkheight=shrinkh;
  }

  public int print(Graphics g, PageFormat pageFormat, int pageIndex) {
	  p("START *** print");
	/** This method used by the Print command but not for creating the original graphic
	*/
    if (pageIndex > 0)
      return NO_SUCH_PAGE;
    //System.out.println("Start printing. For scale: shrinkheight="+shrinkheight+", shrinkwidth="+shrinkwidth);
    if(shrinkwidth<shrinkheight)
    	shrinkheight = shrinkwidth;
    if(shrinkheight < shrinkwidth)
    	shrinkwidth = shrinkheight;
    //this bit used for pdf
    if (shrinkwidth > 0.9999999 ){
    	shrinkwidth = (float) 0.7;shrinkheight = (float) 0.7;
    }
    Graphics2D g2 = (Graphics2D) g;
    g2.translate(pageFormat.getImageableX(), pageFormat.getImageableY());//get nada without this
    p("Scale by "+shrinkwidth+" x "+shrinkheight);
    g2.scale(shrinkwidth, shrinkheight);
    //float shrink=shrinkwidth; if(shrinkheight<shrink)shrink=shrinkheight;
    //if(shrink<1) g2.scale(shrink,shrink);
    //hurrah - I can scale it FOR PRINTING here - what I see is what I get BUT not for Snow Leopard
    //if(shrink>0.99) 
    //	g2.scale(0.5,0.5);////  *** CRITICAL TO SHRINK FOR PDF
    // System.out.println("Graphics "+g2);
    // System.out.println(" from graphics "+g);
     boolean wasBuffered = disableDoubleBuffering(CPGomponent);
     CPGomponent.paint(g2);//paint(g2) used... // use paint to get top labels only (in Snow Leopard)
     restoreDoubleBuffering(CPGomponent, wasBuffered);
    // System.out.println("Prinint at line 351, printit.java - was it Doublebuffered? "+wasBuffered);
    return PAGE_EXISTS;
  }

  private boolean disableDoubleBuffering(Component c) {
    if (c instanceof JComponent == false)
      return false;
    JComponent jc = (JComponent) c;
    boolean wasBuffered = jc.isDoubleBuffered();
    jc.setDoubleBuffered(false);
    return wasBuffered;
  }

  private void restoreDoubleBuffering(Component c, boolean wasBuffered) {
    if (c instanceof JComponent)
      ((JComponent) c).setDoubleBuffered(wasBuffered);
  }
  
  
 
}
