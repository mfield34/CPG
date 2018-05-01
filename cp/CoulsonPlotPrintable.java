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
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.io.Serializable;
import java.util.Hashtable;

import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JViewport;

public class CoulsonPlotPrintable extends JPanel implements Printable,
						Serializable //remember last session
{
	//for debug use the LOG functions, see data in LOG tab of main window, as output from here exceeds space in Eclipse
	private boolean printok = false;//false unless DEBUG
	private boolean printtop = false;
    private String file;
    private JTextArea log;//pass information to parent 
    private HifStackString table;
    private JTable data;
    public int FontSize;
    public int Cross=1;//set this to put panels vertically...
    private boolean addvertlab;private boolean addvertlab2;
    private int twotimesheight;
    
    double scale;//not set - set up by ShowTable via printit
	double defaultscale;//keep track of this: it is 0.7 to make a what you see is one A4 page

    private String fontchoice = "";//for lables
    private String fontchoice2 = "";//for pies
    private String colourFilename;//set from CPtabs = "imogenart/cp/colors.txt";
    
    private GridBagLayout gridbaglayout = new GridBagLayout();

    /* Items for repainting for save in SnowLeopard */
    private Hashtable things2paint;
    private int nextItem;

    private int CanvasSize;
    private CPGcolors CPGcolors;
    private char leftright = 'l';//left or right justify species labels
    private char center = 'c';
    private boolean right = false;
    private boolean left = true;//left justify Species labels
    
	private void p(String s){ if (printok) System.out.println("CoulsonPlotPrintable:: "+s); }
	private void pp(String s){ if (printtop) System.out.println("CoulsonPlotPrintable:: TOP "+s); }
	   
	public void log(String message){//for SaveImage to use
		p(message);
		log.append(message);log.setCaretPosition(log.getSelectionEnd());
	}
   
    //this printable method is passed to the PrinterJob if you use File
	//note if you do apple-p you ONLY get the first protein label
	//while if you use File Print to PDF ...
	public int print(Graphics g, PageFormat pf, int pageIndex) {
		pp("START print");
	    if (pageIndex != 0)
	      return (NO_SUCH_PAGE);
	    p("CPP ^^^^^^ print ... scale "+defaultscale);
	    //p("CPP printing. For scale: shrinkheight="+shrinkheight+", shrinkwidth="+shrinkwidth);
	    Graphics2D g2d = (Graphics2D)g;
	    g2d.translate(pf.getImageableX(), pf.getImageableY());
	    //p("New scale: already shrunk in paint method");
	    //scale here makes it bigger, affects post Print to PDF
	    this.paint(g2d);
	    p("Finish print");
	    return PAGE_EXISTS;
	}
 
    public CoulsonPlotPrintable(HifStackString v, String path, JTextArea parentlog, JTable datain, 
    		int a, int b, int c, int d, int e, boolean f, boolean i, boolean y, boolean cross, boolean gap2, boolean gap1, int colgap, float g, float h, String j,String k,
    		CPGcolors cols){
    	pp("CPP");
    		CPGcolors = cols;
         	file = path;
        	log = parentlog;
        	table = v;
            data = datain;
            scale = 0;
            defaultscale = 0.7;
            setLayout(null);
            addvertlab=f;addvertlab2=i;
            fontchoice = j;fontchoice2 = k;
            p("CPP font is "+j);
            CanvasSize = b;
            //dont see this this.setBackground(Color.DARK_GRAY);
            if(cross) Cross = 0;//transverse, with pie label keys at side, left
     	 	makepies(a, b, c, d, e, y, gap2,gap1,colgap);//Gap,Hi,Diam,Font,Vertgap...Cross
     	 	//can't start again just yet, ok for when we start zooming
    }
    
    /** Original layout and setting of all positions: pies are printed in canvases (ArcCanvas) and each has its own position.
     * When repainting (for print function) then paint is used and the positions of the canvases recovered.
     * So the paint methods in TextCanvas and ArcCanvas (also used for labelled pies) are used multiple times: first when drawing the diagram
     * for the very first time, and again after calling print, and for the new picture (e.g. PDF); whereas paint here is called when printing and makepies creates the original diagram.
     * For transposition:
     *  Cross=0 is transposed with spp across the top and pie labels on the x axis;
     *  Cross=1 is the normal way with pie lables across the top
     *  They are simply counted the other way 
     *  (count x and y in a grid bag layout, so that outer labels may be placed across one or more other labels.)
     * @param CanvasW
     * @param CanvasH
     * @param PieDiam
     * @param fontsz
     * @param vertgap
     * @param yellowbox
     * @param gaps4Row2
     * @param gaps4Row1
     * @param colgap
     */
    private void makepies(int CanvasW, int CanvasH, int PieDiam, int fontsz, 
    		int vertgap, boolean yellowbox, boolean gaps4Row2, boolean gaps4Row1, int colgap){
    	//As pies are made, store them for later paint/print - in things2paint
    	//  boolean gaps4Row2 = false;//if true then GAPS for Row2 Kingdom
    	//  boolean gaps4Row1 = false;//if True then NO GAPS for Row1 Phylum in makepies Horiz label
    	pp("START makepies"); 
    	things2paint = new Hashtable();//store everything you lay down, for the paint method to recover
    	nextItem = 0;//counter...
    	twotimesheight = (2*CanvasH);
    	if(colgap>-1) twotimesheight = colgap+CanvasH;//so that you start to see an affect at 1
    	if(Cross==0) twotimesheight = CanvasH;//otherwise get 2 spaces between ALL pies but need to expand while drawing the pie labels (only)
    	if(twotimesheight<CanvasH) twotimesheight = CanvasH;//pies never overlap so why should text?
    	if(twotimesheight>5*CanvasH) twotimesheight = 5*CanvasH;//too big a gap not worth considering
    	p("&&&&& Set colgap"+colgap+" so twotimesheight is "+twotimesheight);
    	//uses v and unpacks it to make pies    	
    	//your input values
    	String message="Gap is "+CanvasW+", pie canvas height is "+CanvasH+", pie diameter is "+PieDiam+" and vertgap = "+vertgap;   	
    	log(message);
    	
    	/** unpick file, then draw pies */
        
        /** test file has the following structure:
         * 1: Kingdoms
         * 2: Phyla = no. pies across - and count gaps for no.segs per pie
         * 3: Species = name of segments per pie
         * 4: Status = sublabel for pie segments ==== optional?
         * 5: Data rows... start + and - = could be empirically determined
         * Col1: Vert labels for bunch of pies, one pie per label (Protein)
         * Col2: Sub labels for each pie (assign labels to Col1 labels)
         * Col3 onwards... data - group up into pie vectors
         * 
         */
        String delim = ",";//determine this
    	String ss = table.get(0);
    	if(ss.contains("\t") && !ss.contains(",")){delim="\t";}
    	else if(ss.contains(",") && !ss.contains("\t")){delim=",";}
    	else {
    		message="Cannot find delimiter - do you have additional tabs in a comma separated file, or additional commas in a tab delimited file?";
    	   	log(message);//System.exit(0);
    	}
     	int prevCountbetweenFamily;
	     	       
        /* first parse for good data lines and start, stop of data */
        /** PROTEIN/Subunits are columns 0 and 1 (first 2), so gather pie data vertically 
         *  Horizontally use every species  
         */
        int HeaderLineNo = 0;// = 4;
        int DataLineStart = 0;// = 7;
        int CountData = 0;// No. data lines to be gathered into pies!
        boolean DataLineStartSet=false;
        HifStackString proteinNames = new HifStackString();
        HifStackString subunitProteinNames = new HifStackString();
        int NumProteinNames = 0;
	        
	        
        /** Get dimension of longest protein and or spp label 
         * Also set the start data line - supposed to be automatic
         * */
        int CanvasP = 10;//dimension of longest protein lable
        int CanvasSpp = 10;//dimension of longest spp lable
        String longestProt="";
        
        for(int i=0;i<table.size();i++){//iterate through rows of orig. file
        	String s = table.get(i);//getting e.g Adaptin,AP1(g),+,_,+... or  ,AP1(a),+,+,+... (no protein name)
        	p(i+":"+s);
        	message = "\nParse line "+(i+1)+": "+s+"\n";
    		log(message);
        	if((s.contains("+") || s.contains("-")) && !DataLineStartSet){
        		DataLineStartSet=true;
        		DataLineStart=i;//eg 4 = 5th line is start of data i+1 as count starts at 0
        		HeaderLineNo = 2;//ALWAYS ie 3rd line is pie headings (count 0, 1, 2)
        		message = "\nData starts at line "+(DataLineStart+1)+"\n";
        		log(message);
         	}
        	if((s.contains("+") || s.contains("-")) && DataLineStartSet && i>=DataLineStart){
        		CountData++;
        		String[] vert = s.split(delim);
        		String l1 = vert[0];
        		if(!l1.equals("")) {NumProteinNames++;
        			if(longestProt.length()<l1.length()){longestProt=l1;}
        		} 
        		String l2 = vert[1];
        		proteinNames.push(l1);//these keep pace....so there are gaps
        		subunitProteinNames.push(l2);//this will be used to label pies up
        	}
        }
        //GET A REAL LENGTH
        CanvasP = longestProt.length()*9;

        //end of getting longest protein or species name (latter current)
        message="\nCreating figure:\n Your data has "+DataLineStart+" Header lines,\n "+NumProteinNames+" proteins,\n "+CountData+" data rows ie "+subunitProteinNames.size()+" subunits\n"; 
        message+="\n***** Using colours from "+colourFilename+"\n\n";
        log(message);
        log("Longest left label "+longestProt+"\n");
	        
        /* Now get primary headers ie grouping for row2 */
        String headers1 = table.get(0); //line 2,no. 1    
        String[] OrderName = headers1.split(delim); //TOP ROW for FAMILY small portion populated
        /* Secondary headers are groupings for species and correctly interpreted */
        String headers2 = table.get(1); //line 1,no. 0  //SECOND row for KINGDOM  subset populated  
        String[] KingdomName = headers2.split(delim);//there will be 2 unused
        /* These headers are primary left side Species labels */
        String headers = table.get(2); //line 3,no. 2 for SPECIES ALL populated , THIRD row
        String[] SpeciesName = headers.split(delim);//there will be 2 unused
        log("Your species are on line "+HeaderLineNo);
        log("Species list: "+headers);
        log("Delimiter is "+delim);
        printLabels(OrderName,"orders");
        printLabels(KingdomName, "phyla");
        printLabels(SpeciesName, "species");
        if(KingdomName.length<SpeciesName.length){
        	int missing = SpeciesName.length-KingdomName.length;
        	String[] newKingdomNames = new String[(missing+KingdomName.length)];
        	for(int x=0;x<(missing+KingdomName.length);x++){ 
        		if(x<KingdomName.length) newKingdomNames[x]=KingdomName[x];
        		else newKingdomNames[x]="";//add in the missing values or counts go awol
        	}
        	KingdomName = newKingdomNames;
        }
        if(OrderName.length<SpeciesName.length){
        	int missing = (SpeciesName.length-OrderName.length);
        	p(missing+" missing from Orders");
        	String[] newKingdomNames = new String[(missing+OrderName.length)];
        	for(int x=0;x<(missing+OrderName.length);x++){ 
        		if(x<OrderName.length) newKingdomNames[x]=OrderName[x];
        		else newKingdomNames[x]="";//add in the missing values or counts go awol
        	}
        	OrderName = newKingdomNames;
        }
		boolean onlyOneOrderName = false;
		if(onlyOneOrderName(OrderName))
		    	 onlyOneOrderName = true;
        String headers3 = table.get(3); //line 4,no. 3  for ExtraLabels
        String[] extraLabels = headers3.split(delim);//there will be 2 unused/blanks
        if(extraLabels.length>0 && extraLabels.length<SpeciesName.length){
        	int missing = (SpeciesName.length-extraLabels.length);
        	p(missing+" missing from Orders");
        	String[] newKingdomNames = new String[(missing+extraLabels.length)];
        	for(int x=0;x<(missing+extraLabels.length);x++){ 
        		if(x<extraLabels.length) newKingdomNames[x]=extraLabels[x];
        		else newKingdomNames[x]="";//add in the missing values or counts go awol
        	}
        	extraLabels = newKingdomNames;
        }else extraLabels = null;

        /*Useful display for label checks:
         *  2  Family1 ---- Metazoa ------> Homo sapeins
			3   ----  ------> Ciona intestinalis
			4   ----  ------> Drosophila melanogaster
			10  Plants ---- Viridiplantae ------> Arabidopsis thaliana
			11   ----  ------> Chlamydomonas reinhardtii
			17  Wigglies ---- Kinetoplastida ------> Trypanosoma brucei
			18   ---- Diplomonadida ------> Giardia lamblia
         */
        	p("orders: "+OrderName.length);
        	p("orders: "+KingdomName.length);
        	p("orders: "+SpeciesName.length);
        	for(int x = 0; x<SpeciesName.length; x++){
        		log(x+"  "+OrderName[x]+" ---- "+KingdomName[x]+" ------> "+SpeciesName[x]+"\n");
        	}
        	
        int MaxCount=SpeciesName.length;//don't mess with this!
        String longestSpp="";
        if(Cross==1){
        	for(int i=0; i<MaxCount; i++){
        		String spp=SpeciesName[i];	        		
        		if(spp.length()>longestSpp.length()){longestSpp=spp;}//approx since m>>i, etc
        	}
        }
        if(Cross==0){//tran
        	for(int i=0; i<proteinNames.size(); i++){
        		String spp=proteinNames.get(i);	        		
        		if(spp.length()>longestSpp.length()){longestSpp=spp;}//approx since m>>i, etc
        	}
        }
        //calculate room for side headings
      	CanvasSpp = longestSpp.length()*9;//works only if more than one TOP label
      	
        if(extraLabels != null && extraLabels.length>1 && (extraLabels[2].contains("+")||
        		extraLabels[2].contains("-")||
        		extraLabels[2].contains("0")||
        		extraLabels[2].contains("1"))){
        	log("Row 4 does not look like extra labels but a data line: assuming only 3 header lines\n");
        	extraLabels = null;
        }
        if(MaxCount>OrderName.length){MaxCount=OrderName.length;}
        if(MaxCount>KingdomName.length){MaxCount=KingdomName.length;}
        if(extraLabels!=null){
        	if(MaxCount>extraLabels.length){MaxCount=extraLabels.length;}//MaxCount inlcudes 2 cols for protein, subunit, then each Species
        }

	        /** Now set first rows: labels (Kindom, Phylum, Species, Sublabel) */
	        log("There are "+MaxCount+" columns including 2 which describe Protein and Subunit\n");
	        
			/* Now parse again and set up Label canvases */
	        /* Now determine vertically the pie slices from the labelling */
	        String [] ProtNames = new String[NumProteinNames];
	
	      	int CountProteins = 0;//for assigning to subunitCollections
	        HifStackString Subunits = new HifStackString();
        	HifStackString[] subunitCollections = new HifStackString[NumProteinNames];
         	int[] PieSegs = new int[NumProteinNames];//gives no. segments per pie, one for each protein
        	int CountSubunits = 0;int CountPieSegs = 0;//respectively for countingSegsPerProt and assigning to array PieSegs
          	int CountOthervec = 0;
        	
          	for (int ii=0; ii<subunitProteinNames.size(); ii++)//first 4 are not included: starts with a Protein/Subunit
        	{	
           		String Protein = proteinNames.get(ii);
	  	  	    if(ii==0 && Protein.equals("")){//ErrorBox e = new ErrorBox
 	  	  	    	message = "ERROR in DATA: There should be a protein in row "+(DataLineStart+1)+"\n"; 
 	  	  	    	log(message);System.out.println(message);
  	  	  	    }
	  	  	    if(!Protein.equals("")){
     				ProtNames[CountProteins++]=Protein; //comes first, and if >0, put other away
     	  	  		//p("ProtNames["+CountProteins+"]=Protein "+Protein);
     	  	  		if(ii>0){//keep for assignment to vec - start at 0 ---- first item must be a protein
    //    				p("subunitCollections item "+CountProteins+"="+Subunits.writeVec());
        				subunitCollections[CountOthervec++]=Subunits;
      //  				p("PieSegs item "+CountPieSegs+"="+CountSubunits);
        				PieSegs[CountPieSegs++] = CountSubunits;
        				CountSubunits=0;
        				Subunits = new HifStackString();           	 	  
           			}
          		}
          		CountSubunits++;
     			String subunit = subunitProteinNames.get(ii);
     			Subunits.push(subunit);	
        	}
 			subunitCollections[CountOthervec]=Subunits;
//	 			p("PieSegs item "+CountPieSegs+"="+CountSubunits);
  			PieSegs[CountPieSegs++] = CountSubunits;

			/** Now make pie arrays **
			 * start drawing... note this is reiterated in paint! for print methods, vi things2paint
			 * Iterate through subunitCollections to get numbers of segments per pie....
			 * Make a pie collection for each species 
			 * 
			 */
  
	        int CountDataRow = 0;//ALWAYS 0: reference to data JTable... keep track of what row each protein starts at - removed next line: BUG in 161 and prev!
	        setBackground(Color.white);        
	        setLayout(gridbaglayout);
	        GridBagConstraints c = new GridBagConstraints();
	        c.fill=GridBagConstraints.NONE;//don't overflow(this is defaults setting)
	        int x=0; int y=0;//keep track of GRID BAG LAYOUT cell
	        //start at 3 because there are 3 rows of headers for Phylum, Kingdom, Specie
	        int max_x = 3+NumProteinNames;//grid for layout
	        int max_y = 2+MaxCount+KingdomName.length;//grid for layout
	        int first=0;
	        if(Cross==0){
	        	max_x = 2+SpeciesName.length;//Pie name, pie key then 1 col per species, now at top (row)
	        	max_y = 1+NumProteinNames;//multilabel then 1 row per protein
	        }
	        if(Cross==1){
	        	max_y = 2+SpeciesName.length;//Pie name, pie key then 1 col per species, now at top (row)
	        	max_x = 1+NumProteinNames;//multilabel then 1 row per protein
	        }
         	/* Now start iterating, start at the top and work down each column (in the graphic) 
          	 * Do the first column WITH the labels.
          	 */
	        //jUST GO THE OTHER WAY withx and y for transverse
	        for (int iii = 0; iii<NumProteinNames;iii++){ //iterate through Proteins, add one row of pies per protein
	        		//Cross == 0 was the original (transverse) layout and is no longer an option nor tested
	        	// if(Cross==0): for (int iii = 0; iii<SpeciesName.length;iii++){ //iterate through Proteins, add one row of pies per protein
	        	if(Cross==1)y=0; //start each new column at the TOP and work DOWN, with x++ at the bottom
	        	if(Cross==0)x=0;//start at side and work ACROSS
	        	p("ITERATE iii "+iii+" (x,y) = "+x+","+y);
	        	
	        	int color=-1;//so start with color 0 each time you start a column
	           	
	    /* The top pie-sized strip used for 3 rows of labelling, use x, xa, xb,(with y,ya,yb) */
		        TextCanvas LastMainHead = null;//outer labelling col
	        	TextCanvas LastHeadKing = null;//next labelling col

	        	
	    /* for each protein, make a set of arrays describing segment occupancy per spp */
	        	int Segs = PieSegs[iii];//this should be in synch with protein names.
	        	String protein = ProtNames[iii];
	        	message = (iii+" Starting Pies for protein "+protein+", there are "+Segs+" segments");
	        	log(message);
	        	int[][] proteinSegs = new int[SpeciesName.length][Segs];	
	        	
	        	
	      /* For each species, */
	        	for(int j = 2;j<SpeciesName.length;j++){//iterate across columns, horiz - or vert if Cross==1
	        	//for(int j = 2;j<NumProteinNames;j++){//iterate across rows, vert - or horiz if Cross==0
	      /* take the right number of items for array */
	        		int[] plusminus = new int[Segs];
	        		for(int k = 0; k < Segs; k++){
	        			//message = ((k+1)+" subunits...About to get from data row "+(CountDataRow+k)+" col "+j+" for Species "+SpeciesName[j]);
	        			String onezero="";
	        			try{
	        			 onezero = (String) data.getValueAt(CountDataRow+k, j);
	        			 //message+=(" -> "+onezero);
	        			 //log(message);
	        			}catch(Exception e){
	        				log ("ERROR: Unable to get value from data row "+(CountDataRow+k)+" col "+j+" for Species "+SpeciesName[j]);
	        			}
	        			//p(k+" item...Getting back "+onezero+" from "+(CountDataRow+k)+" (row), spp "+j);
	        			int oneornot=0;
	        			if(onezero.contains("+")){oneornot = 1;}//allow for spaces, don't use equals
	        			else if(onezero.contains("-")){oneornot = 0;}
	        			else log("PLEASE CHECK your data: from row "+(CountDataRow+k)+" col "+j+" for Species "+SpeciesName[j]);
	        			plusminus[k]=oneornot;
	        			log("\n");
	        		}
	        		proteinSegs[j-2]=plusminus;	
		        }
	        	CountDataRow = CountDataRow+Segs;
	        	//finish getting segment information for a protein
	        	
	        	/* Now can set pies for row */
	        	//boolean changecolor4order = false;
	        	int FontNo=0;
        		if(fontsz==15){FontNo=0;}
        		if(fontsz==14){FontNo=1;}
        		if(fontsz==13){FontNo=2;}
         		if(fontsz==12){FontNo=3;}
        		if(fontsz==11){FontNo=4;}
        		if(fontsz==10){FontNo=5;}
         		if(fontsz==9){FontNo=6;}
        		if(fontsz==8){FontNo=7;}
           		if(fontsz==7){FontNo=8;}
           		if(fontsz==6){FontNo=9;}

	        	/** First row ie 0th protein, so top labels, added at the same time as row 1
	        	 * Remember that you are iterating row by row (vert) then spp by spp (horiz), so this is just the white bits at the start */
	        	String s = " ";
	        	if(iii==0){//create the labels panel and first row of pies at the same time
				    //can omit all the top left blanks and in fact not added for print job
	        		if (Cross==1){x=3;if(extraLabels!=null)x=4;}//space out, since no gaps
				    if (Cross==0){ y=3; if(extraLabels!=null) y=4; }//override
      		}//end top blank spaces for iii==0
		        
            c.fill=GridBagConstraints.NONE;//do not resize to fit, horiz and vert (opp to BOTH)

    		/* continue with first row of pies*/
    		//first the PROTEIN label
	        //p("Finished any labels, x and y should be 3, 0: have "+x+", "+y);
   		    
		    /* These Protein labels are printed and saved, 
		     * an exception to all other graphics in Snow Leopard 
		     * They are 2Xwidth like labelled pie keys */
            //never need a yellowbox for protein headers
            int widthforlabel = twotimesheight;
            if(Cross==0)widthforlabel = 2*CanvasH;
		    TextCanvas tc1 = setTextCanvas(false,fontchoice,widthforlabel,CanvasH,protein,FontNo,0,center);//protein label, 0 degrees, centered (last int)	
	    	tc1.setMinimumSize(new Dimension(10,10));
		    tc1.setName("prot_"+protein);
		    //try and limit size so spacing is good
		    if(Cross==1){c.gridx = x;c.gridy = y++;}//first should be 3//first should be 0
		    //now the new orig boy...
		    if(Cross==0){c.gridx = x++;
		    c.gridy = y;}//plot and move along, not down
	    	add(tc1,c);
	    	p("Add protein label "+protein+" at x "+x+" y "+y);
	    	/** Add spacer to first row..? */
          	log("\nProtein "+protein+" contains "+(subunitCollections[iii]).writeVec()+"\n");
	    	
          	/** This is the part with the labels around the pie */	        	
          	int labelpiewidth = twotimesheight;
          	if(Cross==0) labelpiewidth = 2*CanvasH;//remember that you shrank it so pies sit togethr in this directin...
          	ArcCanvas lablePie = setLabelledSegs(labelpiewidth,CanvasH,subunitCollections[iii],FontNo+8);
          	lablePie.setSize(labelpiewidth,CanvasH);//where pie size is set
		    p("labelled pie at, x and y (3, 1): "+x+", "+y);
		    if(Cross==1){c.gridx = x;c.gridy = y++;}
		    if(Cross==0){c.gridx = x++;c.gridy = y;}
	    	add(lablePie,c);
          	/** Now count down through the species (a pie for each) - already counting across proteins */          	
          	String Kingdom="";
          	String Kingdomlabel="";
          	int CountbetweenKingdom=0;
          	int CountbetweenFamily=0;
          	for (int n=2;n<SpeciesName.length;n++)//miss first 2 cols of table
        	{
        		/* if Phylum, don't leave a space or start colour..that's done with Kingdom: just label it */
        		CountbetweenFamily=0;
        		String Phylum;
        		if(n<OrderName.length){Phylum = OrderName[n];} else {Phylum="";}
        		if(n<KingdomName.length){Kingdom = KingdomName[n];} else {Kingdom="";}//change Kingdom
        		String extraL=null;
        		if(extraLabels!=null){
        			if(n<extraLabels.length){
        				extraL = extraLabels[n]; 
        			}else{extraL = "";}
        		}
        		int[] segs = proteinSegs[n-2];
        		String Specie = SpeciesName[n];
        		log("Doing "+Specie+" (color "+color+") ----- "+Phylum+" ----> "+Kingdom+"\n");
 
    			if(!Phylum.equals("")){//don't change between Kin
    				color++;
    			}
 
           		if(!Kingdom.equals("")){//||(!Phylum.equals("")){//signal to add a blank spacer, but there may be no 2nd lable
        		    if(iii==0){
        				p("gaps4Row2 is "+gaps4Row2);
		       		    p(Kingdom+" <=======Big HORIZONTAL spacer at x,y 0,"+y+" width "+max_x+" cols");
		       		    if(Cross==1){
    	       		        c.gridwidth=max_x;//// go across entire space
    	       		        c.gridx = 0;       //aligned with button 2
    	       		 	    c.gridy = y;       //third row
		       		    }else if(Cross==0){
		   	       		    c.gridheight=max_x;//// go across entire space
	    	       		    c.gridx = x;       //aligned with button 2
	    	       		 	c.gridy = 0;       //third row
		       		    }
	        			Canvas blan = new Canvas();
	        			blan.setSize(CanvasW,CanvasW);
	        			blan.setBackground(Color.white);
	        			//adds horiz spacer correctly for phylum change, OR if gaps4Row2 at Kingdom change as well
	        			if(!gaps4Row1 && x>2 && (gaps4Row2 || !Phylum.equals(""))){ 
	        				add(blan,c); 
	        				things2paint.put(++nextItem,blan);
	        				}
	        			c.gridwidth=1;//reset
	        			c.gridheight=1;
        			}//end signal to add a blank spacer... ie new Kingdom
        			if(Cross==1)y++;//increment y always
        			if(Cross==0)x++;//increment x always
 
        			/** spacers done, now get the vertical labels */
 				    if(iii==0){
  	       				CountbetweenKingdom=0;
        				//get distance to next kingdom for size of gridheight
       					int start=0;int finish=0;//split removes last item
       					//start counting at this kingdom
       				    for (int i=0; i<(MaxCount); i++){
        					String k = KingdomName[i];
        					if(k.equals(Kingdom)){start = 1;CountbetweenKingdom++;p(Kingdom+" count1 ++"+CountbetweenKingdom);}
        					if(start==1 && finish==0 && k.equals("")){CountbetweenKingdom++;p(Kingdom+" count2 ++"+CountbetweenKingdom);}
        					p("Start "+start+" finish "+finish+" kingdom "+Kingdom+" k "+k+" count "+CountbetweenKingdom+" NO spacers ");
        					if(start==1 && finish==0 && !k.equals(Kingdom) && !k.equals("")){ finish=1;p(Kingdom+" countend "+CountbetweenKingdom);}
        				}
      				    log(Kingdom+" includes "+ CountbetweenKingdom+" species (2nd col)");
       				    /* create the TOP header label
       				     * ORDER
       				     */
	        			TextCanvas MainheadK;
	        			if(Cross==1){
	        			   	//omit (color+1) for black outlines on outer groups
	        				MainheadK = setTextCanvas(yellowbox,fontchoice,CanvasW, CanvasH, Kingdom, (FontNo+1),270,(color+1),center);
	        				MainheadK.setSize(CanvasH,CanvasH*CountbetweenKingdom);
 		       		    	c.gridx = 1;       //aligned with button 2
 		       		    	p("Big vertical KING label for "+Kingdom+" at 1,"+y+" height "+CountbetweenKingdom);
 		       		    	c.gridy = y;       //third row
 		       		    	c.gridheight=CountbetweenKingdom;
 		        			if(addvertlab2){
 		        				MainheadK.setMinimumSize(new Dimension(10,10));
 		        				add(MainheadK,c) ;
 		        			}
 				    	}
	        			if(Cross==0){
	        			   	//omit (color+1) for black outlines on outer groups
	        	        	MainheadK = setTextCanvas(yellowbox,fontchoice,CanvasW, CanvasH, Kingdom, (FontNo+1),0,(color+1),center);
	        				MainheadK.setSize(twotimesheight*CountbetweenKingdom, CanvasH);
 		       		    	c.gridy = 1;       //aligned with button 2
 		       		    	p("Big vertical KING label for "+Kingdom+" at 1,"+y+" height "+CountbetweenKingdom);
 		       		    	c.gridx = x;       //third row
 		       		    	c.gridwidth=CountbetweenKingdom;
 		        			if(addvertlab2){
 		        				MainheadK.setMinimumSize(new Dimension(10,10));
 		        				add(MainheadK,c) ;
 		        			}
 				    	}
	        			c.gridheight=1;
	        			c.gridwidth=1;
   				    }//end if iii==0 and new Kingdome... spacer
 	        	
 	        	/** Far left labels for Kingdom  or TOP */
        		if((!Phylum.equals("")) && iii==0){
        			//you have just declared a phylum (special case for first item)
        			CountbetweenFamily=0;
   					int start=0;int finish=0;int numspacers=0;//for some reason, last item missing on split
   					//start counting at this kingdom
   					// p("Max count "+MaxCount+", size FamilyName "+FamilyName.length);
   				    for (int i=0; i<(MaxCount); i++){
    					String k = OrderName[i];
    					String spacerk=KingdomName[i];
    					if(k.equals(Phylum)){start = 1;CountbetweenFamily++;}
    					if(start==1 && finish==0 && k.equals("")){CountbetweenFamily++;}
       					if(start==1 && finish==0 && !k.equals(Phylum) && !k.equals("")){ finish=1;}
       					p("Start "+start+" finish "+finish+" family "+Phylum+" k "+k+" count "+CountbetweenFamily+" spacers "+numspacers);

       					if(start==1 && finish==0 && !spacerk.equals("") ){ numspacers++;}//CountbetweenFamily++; }//GAP
       				}
   				    if(numspacers>0){//for(int z=0;z<numspacers;z++){
   				    	numspacers--;}//remove spanning across the last spacer
   				    //if(finish==0){CountbetweenFamily++;}
   				    log("\n                       ORDER "+Phylum+" inludes "+ (CountbetweenFamily)+" species (spaces: "+numspacers+")");
   				    //counting correct; number of spp and number of gaps corresponding to subgroup spacers
   				    /* create the SECOND header label
   				     * PHYLUM
   				     */
	       		    p("Big vertical label for Phylum "+Phylum+" at 1,"+y+" height "+CountbetweenFamily);
	       		    if(Cross==1){
	       		    	//omit (color+1) for black outlines on outer groups
	    	        	TextCanvas MainheadK = setTextCanvas(yellowbox,fontchoice,CanvasW, CanvasH, Phylum, (FontNo+1),270,(color+1),center);
	        			MainheadK.setSize(CanvasH,CanvasH*CountbetweenFamily);
	       		    	c.gridx = 0;       //aligned with button 2
	       		    	c.gridy = y;       //third row
	       		    	c.gridheight=CountbetweenFamily+numspacers;
	       		    	if(addvertlab){ 
	       		    		MainheadK.setMinimumSize(new Dimension(10,10));
	       		    		MainheadK.setName(Phylum);
	       		    		MainheadK.setPreferredSize(new Dimension(CanvasH,CountbetweenFamily*CanvasH));
	       		    		p("Place phylum MainheadK label "+Phylum);
	       		    		c.fill=GridBagConstraints.NONE;//don't let component overflow
	       		    		add(MainheadK,c) ;
	       		    	}
	       		    }
	       		    if(Cross==0){
	        			TextCanvas MainheadK = setTextCanvas(yellowbox,fontchoice,CanvasW, CanvasH, Phylum, (FontNo+1),0,(color+1),center);
	        			MainheadK.setSize(CanvasH,CanvasH*CountbetweenFamily);
	       		    	c.gridy = 0;       //aligned with button 2
	       		    	c.gridx = x;       //third row
	       		    	c.gridwidth=CountbetweenFamily+numspacers;
	       		    	if(addvertlab){ 
	       		    		MainheadK.setMinimumSize(new Dimension(10,10));
	       		    		MainheadK.setName(Phylum);
	       		    		MainheadK.setPreferredSize(new Dimension(twotimesheight*CountbetweenFamily,CanvasH));
	       		    		p("Place phylum MainheadK label "+Phylum);
	       		    		c.fill=GridBagConstraints.NONE;//don't let component overflow
	       		    		add(MainheadK,c) ;
	       		    	}
	       		    }
        			c.gridheight=1;
        			c.gridwidth=1;
        		}///NOTE for 270, text swings out as though around a clock... so need a translation also...
       			}//end Cross==1 no vert labels for transvers

        		/** This is the row of species or (Cross==0) double labels, one set beside each pie */
        		if(iii==0){
         			if(Cross==0){//Species labels across the top, vertical orientation, and need to be LONG
	        			//p("SPPP: DoublLab's are "+Kingdom+" "+Specie+" "+extraL+" at x,y "+2+","+y);
         				p("Species label transposed is H "+CanvasSpp);
               			TextCanvas SppLabl = setTextCanvas(yellowbox,fontchoice, CanvasW, CanvasSpp, Specie, (FontNo+1),270,(color+1),leftright);//inc color since first color is black for writing
             			SppLabl.setName("spp_"+Specie);
        			    //same no. of each --- log("\n"+KingdomName.length+" TOP labels, "+OrderName.length+"Second labels\n");
        			    if(onlyOneOrderName)
        			    	SppLabl.setSize(CanvasH,4*CanvasSpp);
        			    else
        			    	SppLabl.setSize(CanvasH,CanvasH*2+CanvasSpp);//make it HIGH and VERT:  CanvasSpp  = longestspp (String length) * 9 pixels; but not right so fudget with 4x
	        			c.gridx=x;
	        			c.gridy=2;//DON'T increment here... goes by the BOTTOM
				    	c.fill=GridBagConstraints.NONE;//don't let component overflow
				    	p("Add SppLabl "+Specie);
					    add(SppLabl,c);
					    
	           			//can't use getTextlen until it's showing CanvasSpp = SppLabl.getTextlen();
	           		 	if(extraL!=null){
					    	TextCanvas extraText = setTextCanvas(yellowbox,fontchoice,CanvasW,CanvasH, extraL, (FontNo+1),0,(color+1),leftright);//inc color since first color is black for writing
		        			SppLabl.setMinimumSize(new Dimension(10,10));
		        			SppLabl.setName(extraL);
					    	c.gridx=x++;
					    	c.fill=GridBagConstraints.NONE;//don't let component overflow
					    	add(extraText,c);
					    }
        			}
        			if(Cross==1){//single lable -- Specie only
        				//Species label
               			TextCanvas SppLabl = setTextCanvas(yellowbox,fontchoice,CanvasW, CanvasH, Specie, (FontNo+1),0,(color+1),leftright);//inc color since first color is black for writing
            			SppLabl.setName("spp_"+Specie);
	        			SppLabl.setSize(CanvasSpp,CanvasH);
	        			c.gridx=2;
	        			c.gridy=y;//DON'T increment here... goes by the SIDE
				    	c.fill=GridBagConstraints.NONE;//don't let component overflow
				    	p("Add SppLabl "+"spp_"+Specie);
					    add(SppLabl,c);
					    if(extraL!=null){
					    	TextCanvas extraText = setTextCanvas(yellowbox,fontchoice,CanvasW,CanvasH, extraL, (FontNo+1),0,(color+1),leftright);//inc color since first color is black for writing
		        			SppLabl.setMinimumSize(new Dimension(10,10));
		        			SppLabl.setName(extraL);
					    	c.gridx=3;
					    	c.fill=GridBagConstraints.NONE;//don't let component overflow
					    	add(extraText,c);
					    }
        			}
				    c.gridwidth=1; c.gridheight=1;
        		}//end if iii = 0	       		   
        		/** This at last is the PIE */
        		//p("About to setPie color "+color);
       		    ArcCanvas Drawing2 = setPie (CanvasH,PieDiam,segs,color);//Canvas size, Pie diam, filled Segments {01101}, Color
       		    Drawing2.setSize(CanvasH,CanvasH);
       		    p("Pie at "+x+","+y+" for "+Specie+" color "+color);
       		    if(Cross==1){c.gridx = x;   c.gridy = y++; }   //aligned with button 2//third row
       		    else if(Cross==0){c.gridy = y; c.gridx = x++;}
       		    add(Drawing2,c);  
 
        	}//end per spp (per pie)
        	if(Cross==1)x++;//move one across (new column)
        	if(Cross==0)y++;//move down one!
        }//end PER PROTEIN NAME (or per SPP if Cross==0)
	        pp("END makepies");
	        printtop=false;printok = false;
   }//end MAKEPIES subroutine

		/*new method for returning a column not defined in JTable so put it here */
        public Object[] getColumnArray(JTable tab, int col){
        	Object[] colContents = new Object[tab.getRowCount()];
        	for (int i=0; i< tab.getRowCount(); i++){
        		colContents[i]= tab.getValueAt(i,col);
        	}
        	return colContents;	        	
        }
			
	    private TextCanvas setTextCanvas(boolean yellowbox, String fontchoose, int wide, int high, String text, int font, double angle, char leftrightcenter){	
	    	pp("setTextCanvas");
	    	TextCanvas tl = new TextCanvas (CPGcolors.getColorArraywblack());
	    	if(yellowbox)tl.doYellowbox(yellowbox);
			tl.setText (text, fontchoose, font, angle, leftrightcenter);
		    tl.setSize(wide,high);//20 high	
		    things2paint.put(++nextItem, tl);
		    return tl;
		}
	    
	    private TextCanvas setTextCanvas(boolean yellowbox, String fontchoose,int wide, int high, String text, int font, double angle, int color, char leftrightcenter){	
	    	pp("setTextCanvas");
	    	TextCanvas tl = new TextCanvas (CPGcolors.getColorArraywblack());//to set same as pies, from file
	    	if(yellowbox)tl.doYellowbox(yellowbox);
	    	tl.setText (text,fontchoose,font,angle,color,leftrightcenter);
		    tl.setSize(wide,20);//20 high	
		    things2paint.put(++nextItem, tl);//not necessarily if if(addvertlab2)  or addvertlab!!!!!
		    return tl;
		}
	    
	    private ArcCanvas setLabelledSegs(int wide, int high, HifStackString texts,int font)//LabelledSegs
	    {	 	    	pp("setLabelledSegs");
	    	 font=font-4;//get it smaller
		   	 ArcCanvas Drawing;
	         Drawing = new ArcCanvas (CPGcolors.getColorArray());//no filename required as no colours needed for default works
	         Drawing.setLog(log);
	         Drawing.setFont(font,fontchoice2);
	         Drawing.setDiameter (high/2); //here, size of pie (half size of Drawing)
	         int[] Segs = new int[texts.size()];
	         for(int i=0; i<texts.size(); i++){
	        	 if(i==0 || i==2) Segs[i]=1;
	         }
	         Drawing.setSegment(texts,17);
	         /* Now the pie is drawn */
   			//change size for repaintiing...
   		    Rectangle2D rectthis = Drawing.getBounds();
	    	Drawing.setBounds((int)rectthis.getBounds().getX(),(int)rectthis.getBounds().getY(),(int)rectthis.getBounds().width,(int)rectthis.getBounds().height);

			 Drawing.setMinimumSize(new Dimension(10,10));
			 things2paint.put(++nextItem, Drawing);
	         return Drawing;	
	    }
	    
    
	    
	    /* duplicate method */
	    private ArcCanvas setPie(int Canvassquare, int ArcDiam, int[] Segs, int oneCol){
	    	pp("setPie");
	        /* this next part makes a canvas with a pie picture on it -one color with empty segments */
	    	/* used by CoulsonPlot also */
	   	   ArcCanvas Drawing;
	         Drawing = new ArcCanvas (CPGcolors.getColorArray());//if filename is null then defaults are used}//should use file of colours
	         Drawing.setLog(log);
	         Drawing.setDiameter (ArcDiam); //here, size of pie (half size of Drawing)
	         if(oneCol<0){log("Colour selected for pies is <1,  is "+oneCol);}//oneCol=0;}//just in case
	         Drawing.setSegment (Segs, oneCol);//nothing happens if not set
	         Drawing.setSize(Canvassquare,Canvassquare); //here, size of Drawing/canvas
	         /* Now the pie is drawn */
			 Drawing.setMinimumSize(new Dimension(10,10));
			 things2paint.put(++nextItem, Drawing);
	         return Drawing;	
	    }
	    
	       
	   /* Special method for repainting with SAVE and now PRINT */
	   public void setScale(float scale){
		   double newscale = (defaultscale*scale);
		   this.scale = newscale;
		   log("Save Image: set scale to "+scale);
	   }
	   
	   
	   /** paint essential override
	    *  Repaint all items in things2paint
	    *  --not used for the original PAINT...as TextCanvas/ArcCanvas paint their own canvi
	    *  this IS used for zoom and shrink and fit to window and speeds things up a lot
	    *  is also used for print (to PDF) by printit
	    *  */
	   public void paint(Graphics2D g){ //with this present the save function uses this to print --- 
		   
		   pp("\nSTART paint !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		   //This needs to be an override (paint)
		   /* background is black if used with SaveImage */
		  g.setColor(Color.white);
		  
		  g.fill(this.getParent().getBounds());//getVisibleRect());//fill in the picture
		  g.fill(getBounds());//fill in the rest of the page -- IMPORTANT for jpegs etc!
		  //g.drawRect(0, 0, getVisibleRect().width, getVisibleRect().height);//this.getWidth(), this.getHeight());
		  //NO renderinghints
		   if(scale == 0) scale = defaultscale;//otherwise it is set by SaveImage
		   g.scale(scale, scale);
		   /* Access items for reprinting on save */
		   Point po = gridbaglayout.getLayoutOrigin();
		   AffineTransform saveXform = g.getTransform();
		   g.translate(0,0);//po.getX(),po.getY());//start drawing where we did before:
		   g.setBackground(Color.white);
		   for(int i = 0;i<things2paint.size();i++){
			   String wotami = things2paint.get((i+1)).toString();
			   g.setBackground(Color.white);
			   
			   if(wotami.contains("TextCanvas")){
				   TextCanvas tc = (TextCanvas) things2paint.get((i+1));
				   int length = tc.getTextlen(g);//uses fontmetrics
				   p("\n"+tc.getText()+":: Real text length is: "+length);
				   Dimension origdim = tc.getSize();
				   Point p = tc.getLocation();
				   g.translate(0,0);
				   g.translate(po.getX(),po.getY());//comment out if you DONT want to go back to where you started--only STOP THIS HERE
				   g.setTransform(saveXform);
				   p("Transform is "+saveXform);
				   double angle = tc.getAngle();
				   p("Angle of text: "+angle);
				   int W = length;
				   int H = CanvasSize;
				   //there is another method for centering somewhere
				   char leftrightcenter = tc.getLeftrightcenter();
				   p("leftrightcenter? "+leftrightcenter);
				   p("---------------------\n                  Text box "+tc.getText());
				   int offsetx = (origdim.width-W)/2;
				   int offsety = (origdim.height-W)/2;
				   if(angle>0){
					   g.translate(p.getX(), p.getY());//THIS is what drops the text labels into place
					   if(origdim.height>=W){//W is text width and angle at 90 or 270 so will be height
						   p("Not bigger: orig dim is  W "+W+" cf "+origdim.height+" * H "+H+" origH "+origdim.width);
						   tc.setSize(H,origdim.height);
						   g.setClip(new Rectangle(H,origdim.height));
					   }
					   else{
						   p(tc.getText()+" is bigger than the box... W "+W+" cf "+origdim.height+" * H "+H+" origH "+origdim.width);
						   if(tc.getName().startsWith("spp_"))
							   g.translate(0, (2*offsety));//line up spp labels even when oversized, if at 90deg
						   else 
							   g.translate(0,offsety);
						   g.setClip(new Rectangle(H,W));//as long as H and W switched for both
						   tc.setSize(H,W);//now draws all the text 
						   p("Drew it H*W");
					   }
				   }else{//angle 0
					   g.translate(p.getX(), p.getY());//in new method add offsetx here
					   p("angle "+angle+"; orig W,h "+origdim.width+","+origdim.height+" and text W "+W+" (H text "+H+")");
					   if(tc.getName().startsWith("prot_"))//keep prot labels central
						   g.translate(offsetx, 0);
					   if(origdim.width>=W){
						   g.setClip(new Rectangle(origdim.width,origdim.height));
					   }else{
						   p("XXX "+W+H);
							   g.translate(offsetx, 0);
						   g.setClip(new Rectangle(W,H));//but clip needs to be offset
						   tc.setSize(W,H);//now we paint the whole for the PDF
					   }
					   if(tc.getName().startsWith("prot_")){
						   g.translate(-offsetx,(H/6));//-offsetx lines up prot labels... if on y axis and H/6 drops em a little
					   }
				   }
				   //end of new part to unmask text
				   tc.paint(g);
				   tc.setSize(origdim);
			   }
			   else if(wotami.contains("ArcCanvas")){
				   ArcCanvas ac = (ArcCanvas) things2paint.get((i+1));
				   ac.setMinimumSize(new Dimension(10,10));
				   Point p = ac.getLocation();
				   g.translate(0,0);
				   g.translate(po.getX(),po.getY());
				   g.setTransform(saveXform);
				   //only used if you hit Save... 
				   p("B prelim or if not ac.labelit(): "+ac.toString());
				   Dimension origsize = ac.getSize();
				   if(ac.Labelit()){//the pie draws itself masked (same size as the normal canvas, no bigger
					   int xoffset = ac.getoffsets()[0];
					   int yoffset = ac.getoffsets()[1];
					   p("Offsets are x "+xoffset+" and y "+yoffset+" and resize by "+ac.getWidth()+"+"+ac.getresize()[0]+" x "+ac.getHeight()+"+"+ac.getresize()[1]);
					   g.translate(p.getX()-xoffset, p.getY()-yoffset);//offsets are half resize by...but if here the pies go to the wrong place
					   ac.setSize(ac.getWidth()+ac.getresize()[0],ac.getHeight()+ac.getresize()[1]);//now we paint the whole for the PDF
					   g.setClip(new Rectangle((ac.getWidth()+ac.getresize()[0]),
							   (ac.getHeight()+ac.getresize()[1])));//but clip needs to be offset
				   }else{
					   g.translate(p.getX(), p.getY());
					   g.setClip(new Rectangle(ac.getWidth(),ac.getHeight()));
					   g.setBackground(Color.white);
				   }				    
				   /* The Canvas, or this Clip, acts as a clipping mask in Illustrator, so
				    * The clipping mask on printing is enlarged to encompass all the text...
				    */
				   ac.paint(g);
				   g.translate(0,0);
				   ac.setSize(origsize);//or else it redraws it on the original - now it just flickers but comes back phew
			   }
			   else{
				   Component ob = (Component)things2paint.get((i+1));
			       ob.setMinimumSize(new Dimension(10,10));
				   Point p = ob.getLocation();
				   g.translate(0,0);
				   g.translate(po.getX(),po.getY());
				   g.setTransform(saveXform);
				   g.translate(p.getX(), p.getY());
				   g.setClip(new Rectangle(ob.getHeight(),ob.getWidth()));
				   p("C Component "+(i+1)+" is at "+p.toString()+" printing at "+p.getX()+","+p.getY());
				   ob.paint(g);
				   g.translate(0,0);
				   p("ITEM: "+things2paint.get((i+1)).toString());				   
			   }
			   scale = defaultscale;//reset when printing done
			   //p("Print DONE\n");printok = false;
			   firsttime = false;
		   }
		   pp("END paint");
	   }
	   	   
	   private void printLabels(String[] hif, String name){
		   HifStackString s = new HifStackString(hif);
		   p("Collection: "+name);
		   p(""+s.writeVec());
		   p("-------------");
	   }
	   
	   private void printArray(String[] array, String s){
		   if(printok){
			   System.out.println("Contents of "+s+"\n");
			   for(int i = 0;i<array.length;i++){
				   System.out.println(i+" ==> "+array[i]);
			   }
		   }
	   }
	   
	   private boolean onlyOneOrderName(String[] OrderName){
		   int countOrders = 0;
			   for(int i = 0;i<OrderName.length;i++){
				   //System.out.println("Try ordername: "+OrderName[i]);
				   if(OrderName[i].matches("\\S+")){
					   //System.out.println(OrderName[i]+" has digits or letters");
					   countOrders++;
				   }
			   }
	   	   if(countOrders==1) return true;
	   	   return false;
	   }
	   
	   /** Methods for view menu
	    * 
	    */
	   private double currentZoom = 1;
	   public boolean firsttime = true;
	   private boolean zoomdone = false;
	   private boolean fitdone = false;
	   
	   public void zoom(boolean makebigger){
		   p("ZOOM "+makebigger);
		   if(currentZoom==0 && defaultscale>0)currentZoom = 1;
		   //printok = true;
		   p("ZOOM: makebigger "+makebigger);
		  // Rectangle r = getVisibleRect();//these are the same?
		   //double visiblewidth = r.getWidth();
		   //double visibleheight = r.getHeight();
		   p("currentZoom "+currentZoom);
		   double scaleby = 0.95*currentZoom;
		   if(makebigger) scaleby = 1.05*currentZoom;
		   if(scaleby==0) scaleby = 0.000001;//not allowed to get smaller than 1.100K
		   currentZoom = scaleby;
		   p("ZOOM to "+scaleby);
		   //printok = false;		   
		   Graphics2D g = startagain();
		   if(g==null) return;
		   g.setColor(Color.white);
		   g.fillRect(0,0,((JViewport)getParent()).getVisibleRect().width,((JViewport)getParent()).getVisibleRect().height);
		   g.scale(scaleby,scaleby);
		   g.translate(0,0);//remove top and left whitespace
		   paint(g);
		   p("END zoom");
	   }
	   
	   public void nozoom(){
		   pp("ZOOM neutral");
		   if(currentZoom==0 && defaultscale>0)currentZoom = 1;
		   //printok = true;
		   //Parent is javax.swing.JViewport[,2,2,649x188,layout=javax.swin...
		   //Rectangle r = ((JViewport)getParent()).getVisibleRect();//these are the same?
		   //double visiblewidth = r.getWidth();
		   //double visibleheight = r.getHeight();
		   p("currentZoom "+currentZoom);
		   double scaleby = 1*currentZoom;
		   if(scaleby==0) scaleby = 0.000001;//not allowed to get smaller than 1.100K
		   currentZoom = scaleby;
		   p("ZOOM to "+scaleby);
		   //printok = false;		   
		   Graphics2D g = startagain();
		   if(g==null) return;
		   g.setColor(Color.white);
		   g.fillRect(0,0,((JViewport)getParent()).getVisibleRect().width,((JViewport)getParent()).getVisibleRect().height);
		   g.scale(scaleby,scaleby);
		   g.translate(0,0);//remove top and left whitespace
		   paint(g);
		   p("END nozoom");
	   }
	   
	   int countfittowindow=0;
	   public void fittowindow(){//use this ONCE only, not when window resized
		   boolean printfit = false;
		   pp("FIT TO WINDOW "+(++countfittowindow));
		   if(printfit)printok = true;
		   pp("START fittowindow (firsttime is "+firsttime+")");
		   if(firsttime){//set to false after window opened...
			   //firsttime = false;
			   //return;
			   }//otherwise hangs, used by window resize which happens when window first opened (see printit::componentResized)
		   if(actualDrawing == null){
			   getDrawingsize();//drawing size must be adjusted for currentZoom
		   }
		   Rectangle r = getVisibleRect();//these are the same?
		   double visiblewidth = r.getWidth();
		   double visibleheight = r.getHeight();
		   double picturewidth = ((actualDrawing.getSize().width)*1);
		   double pictureheight = ((actualDrawing.getSize().height)*1);

		   p("Measure VISIBLE w "+visiblewidth+" h "+visibleheight);
		   p("Measure PICTURE w "+picturewidth+" h "+pictureheight);
		   
		   double scaleby = -1;
		   double scalewidth = -1; double scaleheight = -1;//take the biggest/smallest
		   if(visiblewidth<picturewidth || visibleheight<pictureheight){
			   p("Try Pic bigger than...");
			   if(visiblewidth<picturewidth){ 
				   scalewidth = (visiblewidth/picturewidth); 
				   if(visibleheight<pictureheight){ 
					   scaleheight = (visibleheight/pictureheight); 
				   }
				   if(scalewidth>-1){ 
					   scaleby = scalewidth; 
			   	       if(scaleheight>-1 && scaleheight<scalewidth)  scaleby = scaleheight;//Take the smaller
			   	       p("scale by "+scaleby+" from picture BIGGER THAN win: \n *** "+scalewidth+" for width and "+scaleheight+" for height");
			   		//picture is bigger than window
				   }
			   }
			   if(scaleby == -1){
				   if(visibleheight<pictureheight){ 
					   scaleheight = (visibleheight/pictureheight); 
					   if(visiblewidth<picturewidth){ 
						   scalewidth = (visiblewidth/picturewidth);
					   }
					   if(scaleheight>-1){ scaleby = scaleheight; 
				   	        if(scalewidth>-1 && scaleheight>scalewidth)  scaleby = scalewidth;//Take the smaller
				   	        p("scale by "+scaleby+" from picture BIGGER THAN win: \n *** "+scalewidth+" for width and "+scaleheight+" for height");
				   		//picture is bigger than window
					   }
				   }
			   }
		   }
		   else{
		   if(scaleby == -1){
			   p("Try smaller than");
			   if(visiblewidth>=picturewidth){ 
				   scalewidth = (visiblewidth/picturewidth); 
				   if(visibleheight>=pictureheight){ 
					   scaleheight = (visibleheight/pictureheight); 
				   }
				   if(scalewidth>-1){ 
					   scaleby = scalewidth; 
			   	       if(scaleheight>-1 && scaleheight<scalewidth)  scaleby = scaleheight;//Take the smaller
			   	       p("scale by "+scaleby+" from picture LESS THAN win: \n *** "+scalewidth+" for width and "+scaleheight+" for height");
			   		//picture is bigger than window
				   }
			   }
			   if(scaleby == -1){
				   if(visibleheight>=pictureheight){ 
					   scaleheight = (visibleheight/pictureheight); 
					   if(visiblewidth>=picturewidth){ 
						   scalewidth = (visiblewidth/picturewidth);
					   }
					   if(scaleheight>-1){ scaleby = scaleheight; 
				   	        if(scalewidth>-1 && scaleheight>scalewidth)  scaleby = scalewidth;//Take the smaller
				   	        p("scale by "+scaleby+" from picture LESS THAN win: \n *** "+scalewidth+" for width and "+scaleheight+" for height");
				   		//picture is bigger than window
					   }
				   }
			   }

		   }}		   

		   //scaleby = currentZoom-scaleby;//fudge
		   p("scale factor is "+scaleby+" current ZOmm = "+currentZoom);
		   if(scaleby==1) return;//nothing to do
		   if(scaleby==-1) return;//nothing to do
		   //now actually repaint
		  // p("Real scale is "+scaleby);
		   currentZoom = scaleby*1.4;//PERFECT don't knock 1.4
		   p("Real scale is "+scaleby);
		  // printok = false;
		   Graphics2D g = startagain();//how to get rid of everything so far drawn?
		   p("g after startagain is "+g);
		   if(g==null) return;//first time as fittowindow is called by printit componentResized
		   g.setColor(Color.white);
		   p("Parent is "+getParent());
		   p("Parent is "+getParent().getComponentCount());
		   //g.fillRect(0,0,getVisibleRect().width,getVisibleRect().height);
		   g.scale(currentZoom,currentZoom);
		   g.translate(0,0);//remove top and left whitespace
		   if(printfit)printok = false;
		   if(printfit)printok = true;
		   p("END fit");
		   p("ZOOM ");//adding this makes it work a treat! when you resize the window
		   p("About to nozoom...");
		   if(printfit)printok = false;
		   nozoom();//July 13 removing this causes NO redraw
		   p("END zoom");
		   if(printfit)printok = false;
	   }
	   
	   private Graphics2D startagain(){
	    	for(int i = 0;i<things2paint.size();i++){
				Canvas tc = (Canvas) things2paint.get((i+1));
				remove(tc);//after placing on, they all have a position
	    	}
		   return (Graphics2D) this.getGraphics();
	   }
	   
   /** Drawing Rect is a simple container for the original drawing size
    * and origin
    * @author lime
    *
    */
   class DrawingRect{
		Rectangle rec;
		Point origin;
		DrawingRect (Rectangle r){ rec = r; }
		public void addOrigin(Point p){ origin = p; }
		public Point getOrigin(){ return origin; }
		public Dimension getSize() { return rec.getSize(); }
	}

	DrawingRect actualDrawing = null;

	private void getDrawingsize(){//this is done once per picture
		   Point po = gridbaglayout.getLayoutOrigin();
		   int [] actualDrawingInts = {-1,-1,-1,-1};//for rect
	       if(actualDrawingInts[0]==-1)actualDrawingInts[0]=(int)po.getX();
	       if(actualDrawingInts[1]==-1)actualDrawingInts[1]=(int)po.getY();
	       for(int i = 0;i<things2paint.size();i++){
				Canvas tc = (Canvas) things2paint.get((i+1));
				int w = actualDrawingInts[0]+tc.getWidth();
				int h = actualDrawingInts[1]+tc.getHeight();
				int x = actualDrawingInts[0]+tc.getLocation().x;//<<< want these - biggest location + the width
				int y = actualDrawingInts[1]+tc.getLocation().y;
				if(x>actualDrawingInts[2])actualDrawingInts[2]=(x+(w-1));
				if(y>actualDrawingInts[3])actualDrawingInts[3]=(y+(h-1));
	    	}
	    	actualDrawing = new DrawingRect( new Rectangle(actualDrawingInts[0],actualDrawingInts[1],actualDrawingInts[2],actualDrawingInts[3]));
	    	actualDrawing.addOrigin(po);
	 }

}
