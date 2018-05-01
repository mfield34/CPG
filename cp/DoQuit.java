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

public class DoQuit{// extends Thread{
	
    private QuitConfirmJDialog myQuitDialog = null;
    int showDialog = 0;
    
    public void showDialog()
    {   showDialog = 1;
    	myQuitDialog.setVisible(true);//need this for repeat show (if you cancel repeatedly)
    }

    public DoQuit(CPtabs goWindow){
        if(myQuitDialog == null) 
        	myQuitDialog = new QuitConfirmJDialog(new javax.swing.JFrame(), true, "Quit", goWindow);
         
        	showDialog = 1;

            if(showDialog == 1){
                myQuitDialog.setVisible(true);
                showDialog = 0;
            }
        
    }
}
