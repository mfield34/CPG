/** 	
	Copyright 2004 Helen Imogen Field.
	This source code is part of the JadeKestrel.GalaXy program.
	GalaXy was created to be a viewer for any annotated list of paired objects.
	
    JadeKestrel.GalaXy is certified open source software; you can redistribute it and/or modify
    it under the terms of The Artistic License as published by
    the Open Source Initiative.

    JadeKestrel.GalaXy is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  

	class to open and close files, read and write
*/
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


package imogenart.cp;

import java.awt.FileDialog;
import java.awt.Frame;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

class FileIO
{
	String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

	BufferedReader br;
	BufferedWriter bw;
	File f;//if you want to read and write to same
	String path, sLine;
	String readFile, saveFile;
	//String directorypath = "../JadeKestrel/Database/";//for creating unique files by URLData
	boolean printOK = false;
	//default contstructor used
	
	public FileIO()
	{
		br = null;
		bw = null;
		f = null;
		path = null;
		sLine = null;
		readFile = null;
		saveFile = null;
	}
		
	private void p(String s){ if (printOK) System.out.println(s);}
	
	public boolean readFileOpen()
	{	boolean b = false;
		if (br != null) b = true;
		p("ReadFileopen: "+b);
		return b;
	}
	
	public boolean writeFileOpen()
	{	boolean b = true;
		if (bw == null) b = false;
		p("WriteFileopen: "+b);
		return b;
	}
	
	@SuppressWarnings("deprecation")
	public String getDataFile()
	{
		String filenamepath = null;
		FileDialog fd = new FileDialog(new Frame(), "Find data file", FileDialog.LOAD);
		fd.show();
		if (fd == null) return filenamepath;
		String filename = fd.getFile();
		if (filename != null)
		{
			path = (fd.getDirectory());
			path += filename;
			filenamepath = path;
			readFile = path;
		}
		return filenamepath;
	}
	
	public String getReadName()
	{	return readFile;
	}
	
	public String getSaveName()
	{	return saveFile;
	}
	
	public boolean endoffile()//must read first line... before testing
	{
		boolean b = false;
		if (sLine == null) b = true;
		return b;
	}
	
	public boolean openReadFile(String file)
	{	boolean b = false;
		String filename = file;
		readFile = filename;
		//p("File name is "+file);
		if (filename != null)
		{
			try
			{
				f = new File(filename);
				if (f.exists())
				{
					br = new BufferedReader(new FileReader(f));
					//p("DoFileThings.openReadFile get br");
					b = true;
				}
			}
			catch (Exception e){ System.out.println("DoFileThings.openReadFile: "+e);}
		}
		return b;
	}
		
	public String getSaveFileName(String s1, String s2)
	{	String s = null;
		if (s1 == null) s1 = "Save data file";
		if (s2 == null) s2 = "";
		FileDialog fd = new FileDialog(new Frame(), s1, FileDialog.SAVE);
		if (fd == null) return s;
		fd.setFile(s2);
		fd.show();
		String filename = fd.getFile();
		if (filename != null)
		{
			path = (fd.getDirectory());
			path += filename;
			s = path;
			saveFile = path;
		}
		return s;
	}
	
	private String getLine()//used by getAll use getDataLine to read file line by line
	{
		String s = null;
		if (readFileOpen())
		{
			try
			{	s = br.readLine();
			} catch (Exception e){ p("Error: DoFileThings.getLine: "+e);}//new ErrorBox("Error", "Cannot read file "+e, new Frame());}
			/*if (s.compareTo("")==0) 
			{	try
				{	s = br.readLine();
				}	catch (Exception e){s = null;}
				if (s.compareTo("")==0 || s == null) s = null;
			}*/
		} else p("DoFileThings.getLine br closed");
		//p("DoFileThings.getLine: "+s);
		sLine = s;//for test endoffile
		return s;
	}
	
	public String getAll(String filename)
	{
		openReadFile(filename);
		String s = getAll();
		return s;
	}
	
	public String getAll()
	{	
		String s = null;
		if (readFileOpen())
		{//file is open
			try
			{	s = getLine();
			}
			catch (Exception e){ s = "";}
			while (!endoffile())
			{	String t = getLine();
				s += "\n"+t;
				//s += getLine()+"\n";
			}
			//close file
			closeReadFile();
			//p(s);
		}
		return s;
	}
	
	public boolean openSaveFile(String file)
	{	boolean b = false;
		saveFile = file;
		p("openSaveFile: "+file);
		try
		{	bw = new BufferedWriter(new FileWriter(new File(file)));//was path
			b = true;
		} catch (Exception e) {p("DoFileThings.openSaveFile: "+e);}
		return b;
	}		
	
	public String readDataLine()
	{	String firstline = null;
		try
		{	 firstline = br.readLine();
		}
		catch (IOException e)
		{	p("DoFileThings.readDataLine: "+e);
		}
		return firstline;
	}
				
	public boolean writeLine(String s)
	{	boolean b = false;
		//see if it ends in return
		if (s.charAt(s.length()-1)!='\n')
			s+='\n';
		try
		{	
			bw.write(s);
			b = true;
		} catch (Exception e){p("DoFileThings.writeLine: "+e);}	
		return b;
	}
	
	public boolean closeReadFile()
	{	boolean b = false;
		try
		{	
			//br.flush();
			br.close();
			b = true;
		} catch (Exception e) {p("DoFileThings.closeSaveFile: "+e);}
		p("Close read file");
		return b;	
	}
		
	public boolean closeSaveFile()
	{	boolean b = false;
		try
		{
			bw.flush();
			bw.close();
			b = true;
		} catch (Exception e) {p("DoFileThings.closeSaveFile: "+e);	}
		p("Close save file");
		return b;
	}	
	
	private String date()
	{	String date = "";
	    java.util.Calendar calendar = java.util.Calendar.getInstance();//static method
    	//date, month, year e,g, 05Jan2001
    	String month = months[(calendar.get(java.util.Calendar.MONTH))];
    	date += (calendar.get(java.util.Calendar.DATE)+month+calendar.get(java.util.Calendar.YEAR));
		return date;
	}
	
	//next 2 methods are from Logfile.java (HIF) for autocreation and open of unique named files
/*	public boolean openUnqFile(String file, String subfix)//for directory: Database (download data)
    {	
    	boolean boo = false;
		//get file names
		int number = 0;//no more than 998 files in a day, I hope
		
		String filename = file+date()+"_"+number+subfix;
		f = new File(directorypath, filename);//
		filename = directorypath+filename;
		//System.out.println("Original log file is: "+f.getParent()+f.getName());
		if (!fileExists(filename, subfix, number)) //tests for existence of file with this name
		{
			//boo = true;//else fileOpen returned true so all is great
			saveFile = filename;
			boo = openSaveFile(filename);
		}
		return boo;
	}
*/	
	public boolean openUnqFile(String file, String subfix, String directory)//as above, any directory
    {	
    	boolean boo = false;
		//get file names
		int number = 0;//no more than 998 files in a day, I hope
		
		String filename = file+date()+"_"+number+subfix;
		f = new File(directory, filename);//
		filename = directory+filename;
		//System.out.println("Original log file is: "+f.getParent()+f.getName());
		//if (!fileExists(filename, subfix, number)) //tests for existence of file with this name
		//{
			//boo = true;//else fileOpen returned true so all is great
			
			//alter filename so that it is unique
			saveFile = fileExists(filename, subfix, number);//filename;
			boo = openSaveFile(saveFile);
		//}
		p("openUnqfile");
		return boo;
	}
	
	//is returned negative, when novel name created
	//return new filename
	private String fileExists(String filename, String subfix, int number)//false for file does not exist so write it
	{
		boolean boo = true;
		f = new File(filename);
		do
		{
			if (!f.exists()) //no file of that name and location exists so write it
			{	//System.out.println("File does not exist "+filename);
				boo = false;
			}
			else if (f.isFile()) //...create a name for a new file 
			{	//remove last digit
				int i = filename.indexOf('_');
				
				String date = date();
				int j = date.length();
		//System.out.println("datelength is "+j+" and i is "+i);
				boolean b = false;
				do
				{	
					String test = filename.substring((i-j), i);//take last bit and check for date
		//System.out.println(filename+" "+test);
					while (!test.equals(date))
					{
					 	i = filename.indexOf('_', ++i);//look for next _
					 	test = filename.substring((i-j), i);
						System.out.println(" Now i is "+i);
					}
					if (test.equals(date)) b = true;
					filename = filename.substring(0, ++i);//remove subfix and number
				//System.out.println("Filename substring: "+filename);
					number++;		//keep adding numbers until a new name is found
					filename += (number+subfix);
				System.out.println("New filename: "+filename);
					f = new File(filename);//this is the result of having an existing file
				}  while (!b && i != -1);
			}
		} while (boo);
		p("fileExists");
		return filename;//end method once file and stream opened
	}


	
}