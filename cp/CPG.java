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

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class CPG {
	//java -Xdock:name="CPG" -Xmx800m -XX:MaxPermSize=128M -jar CPG.jar
	public static void main(String[] args) {
		doCPG();
	}
	
	private static void doCPG(){
        //Schedule a job for the event dispatch thread:
        //creating and showing this application's GUI.
		System.out.println("Running on System: "+System.getProperty("os.name"));
		if(System.getProperty("os.name").contains("Mac"))
			System.setProperty("com.apple.mrj.application.apple.menu.about.name", "CPG");
	    
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                //Turn off metal's use of bold fonts
            	//System.out.println("Hello again Imogen");
            	UIManager.put("swing.boldMetal", Boolean.TRUE);
            	CPtabs CPtabs = new CPtabs();
            	CPtabs.createAndShowGUI();
            }
        });
    }
}
