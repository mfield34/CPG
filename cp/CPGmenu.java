package imogenart.cp;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Hashtable;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class CPGmenu extends FileMenu{
	
	private boolean printok = false;//Debug
	private void p(String s){if(printok) System.out.println("CPGMenu:: "+s);}

	public CPGmenu(printit pi){ thisCPGplot = pi; //connect to the CPG plot graphic window
	//set up hash
		hash.put("View", menuItems_View);
	
	}
	
	 private Object[][] menuItems_View = {
				{"Zoom in", new Integer(KeyEvent.VK_EQUALS)},//CAN'T get + to work off the keyboard
				{"Zoom out", new Integer(KeyEvent.VK_MINUS)},
				{"Fit to window", new Integer(KeyEvent.VK_F)},
				{null,null},
				{"Close", new Integer(KeyEvent.VK_W)},//Close the window
				{null,null},
	  };
	 
	 Hashtable<String,Object[][]> hash = new Hashtable();
	 
	  

	 //return a View JMenu item, haven't got time to put the rest of the menu in
	 public void getMenu(String title, JMenuBar wheretoputit){
		 JMenu myMenu = new JMenu(title);
		 if(!hash.containsKey(title)) {
			 System.out.println("No menu config for "+title+" in CPGMenu::getMenu");
			 return;
		 }
		 myMenu = setupMenu (myMenu,hash.get(title));
		 wheretoputit.add(myMenu);
	 }
	 

}
