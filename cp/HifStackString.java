package imogenart.cp;

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


import java.util.Vector;


class HifStackString //extends HifStack JUST REWRITE FOR SPEED

{
	public HifStackString()
	{
		vec = new java.util.Vector();
	}
	public HifStackString(String [] x)
	{
		vec = new java.util.Vector();
		for(int i = 0; i<x.length; i++){
			vec.add(x[i]);
		}
	}

public void replaceVec(Vector v)
{ 
	vec = v;
}

public String writeVec()//writes it out backwards
{	 
	String s = "";
	if (vec.size()>1) 
 	{  
    	for (int z = (vec.size()-1); z > -1; z--)
      	{	
       		s+= (vec.elementAt(z).toString()+",");
    	}
    	s=s.substring(0,s.length()-1);//remove last comma
    	s+=("\n");
   }
	else if (vec.size()==1) s+=(vec.elementAt(0).toString()); 
	else s += ("Vector empty.");
	return s;
}

public void push(String ob)
{
	vec.addElement(ob);
	//System.out.println("pushed "+ob);
}

public String pop()
{
	//String obj = (String) "Hello: there's nothing in this stack.";
	//check vector has elements
	if (!empty())
	{	
    	String obj = (String) vec.lastElement();//returns without removing 
    	vec.removeElementAt(vec.size()-1);
    	return obj;
	}
    return null;
}

public String peekLast()
{	//String ob = null;
  	if (vec.size()>0) return (String) vec.lastElement();
  	return null;	
}

public String peekFirst()
{	//String ob = null;
  	if (vec.size()>0) return (String) vec.firstElement();
  	return null;	
}

public String peekPenultimate()
{	//String ob = null;
  	if (vec.size()>1) return (String) vec.elementAt(vec.size()-2);
  	return null;	
}

public String get(int i)
{	if (i < vec.size()) return (String) vec.elementAt(i);
	return null;
}

public void clear()
{
	vec = new Vector();//.removeAllElements();
}

public final boolean contains(Object object)
{
    return vec.contains(object);
}

public boolean empty()
{
	if (vec.size() == 0)
	{
   		return true;
	}
	return false;
}

public Vector vector()
{ 	return vec; }

public int size()
{
	return vec.size();
}

public String[] array()//return string array equive of vector
{
	String[] strings = new String[size()];
	int i;
	for (i = 0; i < size(); i++)
	{
		strings[i] = get(i);
	}
	return strings;
}

//Use a Vector for this stack because it may or may not need to be very large.
//Never circularise an array: could be looking for the entire genome set of clusters.
private java.util.Vector vec;

}