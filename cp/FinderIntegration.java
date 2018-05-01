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

import javax.swing.JDialog;

import com.apple.eawt.ApplicationEvent;
import com.apple.mrj.MRJAboutHandler;
import com.apple.mrj.MRJApplicationUtils;
import com.apple.mrj.MRJPrefsHandler;
import com.apple.mrj.MRJQuitHandler;



public class FinderIntegration implements //ApplicationListener, // not working and also deprecated
	MRJAboutHandler, MRJQuitHandler, MRJPrefsHandler {
	
	CPtabs cpgwindow;
    public static boolean pref_askToClose;
    
	//debug
	boolean printok = false;
	private void p(String s){if(printok){System.out.println(s);}}

    public FinderIntegration(){
    	p("In finder integration");
    }
    
    /* Used by FinderIntegrationPlugin to remember preferences: passes cpgwindow parent in here */
    public void setGoWindow(CPtabs g){ 
    	cpgwindow = g; 
    	p("cpgwindown is "+g);
     	setPref_askToClose(g);//set this pref from the one in CPtabs
    	
    } // called by FinderIntegrationPlugin

    public void setPref_askToClose(CPtabs go){//use in CP tabls for default settings for ShowTable?
    	pref_askToClose = go.getPref_asktoClose();// remember preference!
    }
    /* End preference rememberings */
     
   	
   	public void execute(){//invoked by the FinderIntegrationPlugin
    			//register handlers - still works
   			MRJApplicationUtils.registerAboutHandler(this);
   			MRJApplicationUtils.registerQuitHandler(this);
   			MRJApplicationUtils.registerPrefsHandler(this);
   	}

    static JDialog AboutDialog = null; //only one About needed
    
    
    public void handleAbout(){// override function
		System.out.println("FinderIntegration handleAbout via MRJ");
    	new doAbout();
    }
     /// override function in Application menu
   
    	
    public void handleOpenApplication(ApplicationEvent event){
        new DoOpenApplication().start();
    }

    public void handleReOpenApplication(ApplicationEvent event){
        new DoOpenApplication().start();
    }

   class DoOpenApplication extends Thread{
        public void run(){
            System.out.println("FinderIntegration Open application");
           // cpgwindow.doNew();
        }
    }

    public void handlePrefs(){
		p("FinderIntegration handlePrefs via FinderIntegration.handlePrefs()");
		new DoPrefs(cpgwindow);
    }
    
    DoQuit quitThread = null;

    public void handleQuit(){
    	// can only cancel twice then quit won't work !
		p("FinderIntegration handleQuit via FinderIntegration.handleQuit()");
	       if(cpgwindow.getPref_asktoClose()){
	            if(quitThread == null){
	                quitThread = new DoQuit(cpgwindow);
	                //quitThread.setDaemon(true); // won't wait for the thread
	                //quitThread.start();
	            }
	            else
	            	quitThread.showDialog();

	        }   else {
	            cpgwindow.hideWindow();
	            cpgwindow.quitProgram();
	            //System.exit(0);//allows quit even if window hidden
	        }
   }
  
}

