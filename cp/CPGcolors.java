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
import java.io.File;

public class CPGcolors {
	
	/** New class to do the colors as few times as possible
	 *  Use to set up defaults.
	 *  Use setColorArray(String filename) to set up different colors
	 *  Use getColorArray to get Color[] (current)
	 *    or getColors to get Color[] with black at Color[0]
	 *  Use getColorStrings to get HifStackSTrinf of colors
	 *  User resetDefaults to go back to defaults (hardcoded)
	 */
	protected final int maxcolors = 20;//max of 20 colors allowed
	private String customcolorfile = null;
	
	 private final Color[] defaultcolors = new Color []{
			  new Color(21,102,182),//dark Turquoise
			  new Color(153,8,63),//reddy purple
			  new Color(11,69,55),//dark bluegreen
			  
			  new Color(210,116,22),//tan-orange
			  new Color(119,30 ,117),//deepmajenta
			  new Color(51,204,255),//Bluegreen 0
			  
			  new Color(153,31,10),//DarkRed, 1
			  new Color(98,128,88),//SeaGreen2
			  new Color(84,37,117),//Purple 4
			  
			  new Color(53,184,60),//DarkGreen,5 
			  new Color(123,81,139),//Mauve, 7
			  new Color(230,107,41),//,//Tangerine8

			  new Color(21,102,182),//dark Turquoise
			  new Color(153,8,63),//reddy purple
			  new Color(11,69,55),//dark bluegreen
			  
			  new Color(210,116,22),//tan-orange
			  new Color(119,30 ,117),//deepmajenta
			  new Color(51,204,255),//Bluegreen 0
			  
			  new Color(153,31,10),//DarkRed, 1
			  new Color(98,128,88),//SeaGreen2
			  /*
			  new Color(84,37,117),//Purple 4
			  
			  new Color(53,184,60),//DarkGreen,5 
			  new Color(123,81,139),//Mauve, 7
			  new Color(230,107,41),//,//Tangerine8
			  */
	  };

	 private Color[] colors = null;
	  
	  public CPGcolors(){
		  colors = defaultcolors;
	  }
	  //now for colors
	  /** These colours are the default. If there is a file, the colours will be replaced
	   * */
	  /** used by CPtabs if colors.txt is empty AND if color.txt not set as colour file when first opened
	   * 
	   * @return
	   
	  public void defaultColorArray(){//from ArcCanvas
		  defaultcolorsasabove = new HifStackString();
		  defaultcolorsasabove.push("21,102,182");//dark Turquoise
		  defaultcolorsasabove.push("153,8,63");//reddy purple
		  defaultcolorsasabove.push("11,69,55");//dark bluegreen
		  
		  defaultcolorsasabove.push("210,116,22");//tan-orange
		  defaultcolorsasabove.push("119,30 ,117");//deepmajenta
		  defaultcolorsasabove.push("51,204,255");//Bluegreen 0
		  
		  defaultcolorsasabove.push("153,31,10");//DarkRed, 1
		  defaultcolorsasabove.push("98,128,88");//SeaGreen2
		  defaultcolorsasabove.push("84,37,117");//Purple 4
		  
		  defaultcolorsasabove.push("53,184,60");//DarkGreen,5 
		  defaultcolorsasabove.push("123,81,139");//Mauve, 7
		  defaultcolorsasabove.push("230,107,41");//Tangerine8
		  
	  }*/
	  
	  public void setColors(String colorfilename){
		  p(colorfilename+" is colorfilename");
   		 if(colorfilename!=null){
 			File f= new File(colorfilename);
 			if(!f.isFile() || !f.canRead()){colorfilename=null;}//from a previous setting on a different computer or different user space
  		 }
  		 if(colorfilename==null){
			  p("resetting defaults");
			  customcolorfile=null;
			  resetDefaults(); 
			  return; 
		  }
		  else if(colorfilename!=null ){//if this is a new color file then reset, otherwise you have it
			  //EVEN if same file name - may be saving more colors to same file
			  customcolorfile = colorfilename;
			  setColorArray(colorfilename);//create colors again...
		  }
	  }
	  
	  public void resetDefaults(){
		  p("In resetDefaults");
		  colors = defaultcolors;//always keep a copy of default colors - but for others, change them out
	  }
	  
	  public Color[] getColors() {//for changing pie colors only (changePieDisplay in CPtabs)
		  return colors;
	  }
	  
	  public Color[] getColorswblack() {//non-default - set colors using setColors(Filename) if you;re not using defaults
		  Color[] colswithblack = new Color[colors.length+1];
		  for(int colc=colors.length;colc>0;colc--){
			  colswithblack[colc]=colors[colc-1];
		  }
		  colswithblack[0]=Color.black;
		  return colswithblack;
	  }

	  public Color[] getColorArray(){ return colors; }//used by ArcCanvas? 
	  
	  public Color[] getColorArraywblack(){ return getColorswblack(); }//used by TextCanvas for text after setColorArray for pies

	  /** This method opens a file and then repopulates the Color[] Colors
	   *  Ideally the new color array should have the same number of values as lines in the file. 
	   *  We start with 15!
	   *  Creates the colorsasabove HifStackString (v) 
	   * @param colorFilename
	   * @return
	   */
	  private void setColorArray(String colorFilename){//Use color picker to get rgb values...
		  colors = new Color[maxcolors];//MUST reset for defaults, or else current colors start the list
		  if(colorFilename == null)	{
			  colors = new Color[maxcolors];
			  colors = defaultcolors;
			  return;
		  }
		  //do this ONCE per CPtabs, or when file is chosen... and new colors set
		  /** NB No log if using from CoulsonPlotPrintable */
		  	String delim = ",";int countline=1;
   			String s="Using my own colours from file "+colorFilename;
   			//System.out.println("ArcCanvas:"+s);
		HifStackString v = new HifStackString();//expandable, ready for colors for which I need a size!
		if(colorFilename != null && !colorFilename.equals("")){

    		int countcolor=0;//use 0,1,2 to keep countng 3 colours
    		int allcolors = 0;//eg 3 colours
			if(colorFilename!=null && colorFilename.compareTo("")!=0){
     		try{
        		FileIO fio = new FileIO();
        		if(!fio.openReadFile(colorFilename)){
        			//if(log){log.append("Can't open  "+colorFilename);log.setCaretPosition(log.getSelectionEnd());}
        			System.out.println("Can't open file of colours "+colorFilename);
        		}
        		else{	
					System.out.println("Reading colors from file "+colorFilename+"\n");
	        	/* read file and put everything into a String(HIFstack)vector */
					/**count no. lines to set colour array size 
					 * 
					 * */
					int count=0;
					while(!fio.endoffile()){count++;}
					
        			String t = fio.readDataLine();
	        			while (t != null && fio.endoffile())
        				{ 
        					t.replaceAll(" ", "");
        					if(t.contains(delim)){//ignore blank lines
        						String [] cols = t.split(delim);
        						int[] colornums = new int[3];
        						colornums[0]=new Integer(cols[0]);
        						colornums[1]=new Integer(cols[1]);
        						colornums[2]=new Integer(cols[2]);
	        					colors[countcolor++]=new Color(colornums[0],colornums[1],colornums[2]);
	        					//System.out.println("col "+colors[countcolor]);
	        					v.push((String) cols[0]+","+cols[1]+","+cols[2]);
        					}//ignore blank line spacers
        					//if no colour then don't replace the default
         					t = fio.readDataLine(); 
        					countline++;
        				}
        			if(fio.closeReadFile()){
        				//log.append("File closed\n");log.setCaretPosition(log.getSelectionEnd());
        			} else {
        				//log.append("Can't close file, sorry\n");log.setCaretPosition(log.getSelectionEnd());
        			}
        		}
        			
    		
        	}catch (Exception e){ 
				//log.append("Could NOT open file "+colorFilename+", sorry\n");log.setCaretPosition(log.getSelectionEnd());
				System.out.println("Could NOT USE file "+colorFilename+", sorry\n"+e);
				/** This sometimes happens if 60 is '60 '
				 * 
				 */
             }//log.append("Can't open file "+path);log.setCaretPosition(log.getSelectionEnd());}
			}
			allcolors = countcolor;//count to end then start again...
		    //String ss = v.writeVec(); System.out.println(ss);
			p("AVOUT to set colors: allcolors is "+allcolors+" and maxcolors is "+maxcolors);
			countcolor=0;
			colors=new Color[maxcolors];
			/**THIS IS WHERE you create the cyclcing of colors so have to go up to max colors
			 * 
			 */
			for (int c = 0; c<maxcolors; c++){
				//cannot use the color[item] as all points to null! strangely (all pies black)
				//replace ALL colours in array, and cycle if you need to
				countcolor = c % allcolors;//starts 3, 3 so becomes 0
				String [] cols = (v.get(countcolor)).split(",");
				int[] colornums = new int[3];
				colornums[0]=new Integer(cols[0]);
				colornums[1]=new Integer(cols[1]);
				colornums[2]=new Integer(cols[2]);
				colors[c]=new Color(colornums[0],colornums[1],colornums[2]);
				p("Modulo "+countcolor+" new col "+colors[c]);
				//if(c>v.size())
					//v.push((String) cols[0]+","+cols[1]+","+cols[2]);//just in case, should expand
				//colors[countcolor++]=new Color(colornums[0],colornums[1],colornums[2]);
				}
			  //colorsasabove = v;
		  }
		  //NEW, added for CPtabs, not used by ArcCanvas, where colours array is set here!
	  }
	  
	  void p(String s){ if (printok) System.out.println("CPGcolors: "+s); }
	  boolean printok = false;
}
