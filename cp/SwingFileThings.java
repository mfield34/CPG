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
/**
*
* Contact:
* hif22@cam.ac.uk
* 
* SwingFileThings was created by Helen Imogen Field,  
* adapting from DoFileThings 
* Copyright. Helen Imogen Field 2010-2060 
* 
* Please note all software is released under the Artistic Licence 2.0 until further notice.
* It may be freely distributed and copied providing our notes remain as part of the code.
* Note that under the terms of this licence, versions of classes that you use have to
* continue to work and must be made available to us.
* If you would like modifications made please contact me first 
* to see if the functions you require are already part of this package.
* 
*/

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Vector;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

public class SwingFileThings {

	File getFile;
	File saveFile;
	File tempFile;
	String getFilename;
	String saveFilename;
	String tempFilename;
	DoFileThings dft = new DoFileThings();
	
	boolean printOK = false;
	
	private void p(String s){ if (printOK) System.out.println(s);}
	
	public String [] readFiletoArray(String file){/* file too big to make a String */
		String filecontent = dft.getAll(file);		/* Used to open curation files */
		String [] contents=filecontent.split("\n");
		return contents;
	}
	
	public String readFiletoString(String file){/* file too big to make a String */
		String filecontent = dft.getAll(file);		/* Used to open curation files */
		p(file+" contains:\n"+filecontent);
		return filecontent;
	}
	
	public Vector readFiletoVector(String file, String splitterm, String endterm){
		Vector v = new Vector();
		int c=0;
		p("File "+file+" to open and . Split on "+splitterm+", end at "+endterm);
		if(dft.openReadFile(file)){
			//p("File "+file+" opened.");
			String s;
			String completeterm = "";//build all items for one term here
			boolean start = false;
			if(dft.readFileOpen()){
				s = dft.getLine();//MUST call this once before calling endoffile...
				//p("Processing first line "+s);
				if(s==null){
					System.out.println("Trying to read file "+file+" PROBLEM possible in programming of file name");
				}
				if(s.contains(splitterm)){ //splitterm not in first line for GO!
					//System.out.println("SPlit Term "+splitterm+ " in first line: please check code");
					start = true;
				}
				while(!dft.endoffile()){//test of eof is null string
					s = dft.getLine();
					//p("Processing "+s);
					if(s!=null && s.contains(splitterm) && start){
						//p(splitterm + " found and start is "+start);
						String oldcompleteterm = completeterm;//make a new String or else the old one gets changed...
						p("OldComplete term is \n"+oldcompleteterm);
						v.add(oldcompleteterm);
						c++;
						completeterm = "";
					}
					else if(s!=null && s.contains(splitterm) && !start){ 
						//p("Complete term is \n"+completeterm);
							start = true; p("splitterm "+s+" and start is "+start);
					}//when to start filling the vector - miss the items above the first splitterm [Term] for GO
					else if(s!=null && endterm.compareTo("")!=0 && s.contains(endterm)){
						String oldcompleteterm = completeterm;
						v.add(oldcompleteterm);
						c++;
						start=false;
						//p("Changed start to "+start);
						
					}
					else if(s!=null && start){
						completeterm = completeterm+"\n"+s;
						//p("Now have "+completeterm);
					}
				}
				if(!dft.closeReadFile()){ System.out.println("Could not close "+file); }
			}
		}
		else{System.out.println("Could not open file "+file);}
		return v;
	}

	public Vector readFiletoVector(String file, String splitTerm){//for big files (300K lines)
		/* Return items that are separated by splitTerm like [Term] in obo file */
	    //File inFile = new File (file);   
	    try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String tt=br.readLine();
			p("Try "+tt);
		} catch (Exception e1) {
			System.out.println("Can not open file: "+file+" due to: "+e1);
			e1.printStackTrace();
		}
		Vector v = new Vector();
		String t="";//each item in s []
		String tt="";
		int a = 0;//linecount
		boolean startterm = false;
		
		if(dft.openReadFile(file)){
			//if (dft.readFileOpen()){//file is open
				try{	
				//first line
					tt = dft.getLine();
					p("readFiletoVector 0: "+tt);
				}catch(Exception e){e.printStackTrace();}
				
				if(tt!=null && tt.compareTo("")!=0){
					if(tt.contains(splitTerm)){
						if(t.compareTo("")!=0){
							String x = t;
							v.add(x);
						}
						t = "";// start over
					}
					else
						t = t+"\n"+tt;
				}
				while (!dft.endoffile())
				{	//p(""+a);
					try{
					tt = dft.getLine();
					p("readFiletoVector "+(++a)+": "+tt);
					}catch(Exception e){e.printStackTrace();}
					
					if(tt!=null && tt.compareTo("")!=0){
						if(tt.contains(splitTerm))
							t = tt;// start over
						else
							t = t+"\n"+tt;
					}
				}
				//close file
				dft.closeReadFile();
			//}else{p("File is not OPEN");}
		}else{p("Can't open file "+file);}
		return v;
	}

	/** Method to get Name of a file from a JFileChoose open dialog
	 * With options for permitting single filename or multiple filenames.
	 * Returns String, with \n separated paths if multiple
	 * @param initialFile
	 * @param multi
	 * @return
	 */
	public String getReadFile(String initialFile, int multi){
		JFrame fr = new JFrame();
		JFileChooser fileopener = new JFileChooser(new File (initialFile));
		fileopener.setFileSelectionMode(JFileChooser.FILES_ONLY);
		
		if(multi>0){
			fileopener.setMultiSelectionEnabled(true);
		}

		//FileFilter filter = new FileNameExtensionFilter("c files", "");
	    //fileopen.addChoosableFileFilter(filter);
	    int ret = fileopener.showDialog(null, "Open curation record ");
	   // File file = null;
	    if (ret == JFileChooser.APPROVE_OPTION) {
	    	if(multi>0){
			  File [] files = fileopener.getSelectedFiles();
		      String listoffiles = "";
		      for (int i = 0; i < files.length; i++){
			    listoffiles = listoffiles+files[i].toString()+"\n";
			    System.out.println(files[i].toString());
		      }
			  return listoffiles;
		    }
	    	else if(multi==0){
	    	  File file = fileopener.getSelectedFile();
		      String listoffiles = file.toString();
			  return listoffiles;
		    }
	    }
	    else{//(CANCEL_OPTION OR ERROR_OPTION){
	    	return null;
	    }
	    return null;
	  }
	
	public String saveFile(String initialFile){
		JFrame fr = new JFrame();
		JFileChooser filesaver = new JFileChooser(new File (initialFile));
		//FileFilter filter = new FileNameExtensionFilter("c files", "");
	    //fileopen.addChoosableFileFilter(filter);
	    int ret = filesaver.showDialog(null, "Save curation record  ");
	    if (ret == JFileChooser.APPROVE_OPTION) {
	       File [] file = filesaver.getSelectedFiles();
	      System.out.println(file);
		    String filename = file.toString();
		    return filename;
	    }
	    else{//(CANCEL_OPTION OR ERROR_OPTION){
	    	return null;
	    }
	  }

	public boolean fileexists(String filename){
		File f = new File(filename);
		if (f.exists())
			return true;
		else
			return false;
	}

}
