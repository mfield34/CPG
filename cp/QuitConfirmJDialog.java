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

import java.awt.Frame;

import javax.swing.JDialog;
import javax.swing.JLabel;

public class QuitConfirmJDialog extends JDialog {

        private String wotImDoing;
        private CPtabs gowin=null;
        private ColorPicker colpik=null;
        private boolean pleasequit = false;

        public QuitConfirmJDialog(Frame parent, boolean modal, String s, CPtabs go){
            super(parent,modal);
            wotImDoing = s;
            gowin = go;
            //System.out.println("QuitConfirmJDialog: "+go);
            //wotImDoing = text;
            initComponents();
        }
        
        public QuitConfirmJDialog(Frame parent, boolean modal, String s, ColorPicker go){
            super(parent,modal);
            wotImDoing = s;
            colpik = go;
            //System.out.println("QuitConfirmJDialog: "+go);
            //wotImDoing = text;
            initComponents();
        }

        private void initComponents(){
            buttonPanel = new javax.swing.JPanel();
            cancelButton = new javax.swing.JButton();
            okButton = new javax.swing.JButton();
            jLabel1 = new JLabel();
            setTitle("Confirm "+wotImDoing);
            setModal(true);
            setResizable(false);
             addFocusListener(new java.awt.event.FocusAdapter(  )
            {
                public void focusGained(java.awt.event.FocusEvent evt)
                {
                    formFocusHandler(evt);
                }
            });

            addWindowListener(new java.awt.event.WindowAdapter(){
                public void windowClosing(java.awt.event.WindowEvent evt){
                    closeDialog(evt);
                }
            });
            cancelButton.setText("Cancel");
            cancelButton.addActionListener(new java.awt.event.ActionListener(){
                public void actionPerformed(java.awt.event.ActionEvent evt){
                    cancelButtonHandler(evt);
                }
            });
            buttonPanel.add(cancelButton);
            okButton.setText("OK");
            this.getRootPane().setDefaultButton(okButton);
            okButton.addActionListener(new java.awt.event.ActionListener(){
                public void actionPerformed(java.awt.event.ActionEvent evt){
                    okButtonHandler(evt);
                }
            });
            buttonPanel.add(okButton);
            getContentPane().add(buttonPanel, java.awt.BorderLayout.SOUTH);

            jLabel1.setText("Really "+wotImDoing+"?");
            jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            jLabel1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
            getContentPane().add(jLabel1, java.awt.BorderLayout.CENTER);

            pack();
            java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
            setSize(new java.awt.Dimension(366,116));
            setLocation((screenSize.width-366)/2,(screenSize.height-116)/2);


        }

        private void formFocusHandler(java.awt.event.FocusEvent evt){
            okButton.requestFocus();
        }

        private void cancelButtonHandler(java.awt.event.ActionEvent evt){
            okButton.requestFocus();
            if(colpik!=null){
        		//System.out.println("colpik is not null");
        		//if(wotImDoing.contains("Overwrite"))
        		//colpik.setsavetothisfile(true);
                colpik.setclose(false);
            }
            this.setVisible(false);
        }

        private void okButtonHandler(java.awt.event.ActionEvent evt){
        	//case for ColorPicker file overwrite
        	if(colpik!=null){
        		//System.out.println("colpik is not null");
        		//if(wotImDoing.contains("Overwrite"))
        		colpik.setsavetothisfile(true);
                colpik.setclose(true);
                this.setVisible(false);
                this.dispose();
                //colpik.setVisible(false);
                //colpik.framef.setVisible(false);
        	}
        	//orig case for shall I quit?
        	else if(wotImDoing.compareTo("Quit")==0){
            	pleasequit = true;
            	//System.out.println("Inside okButtonHandler in QuitConfirmDialog");
            	try{
            		gowin.hideWindow();// remembers last size/position, invoking store...
            	}catch(Exception e){
            		e.printStackTrace();
            	}
                System.exit(0);//get rid of everything
            }
            else{
                goElsewhere();
            }
        }

        private int goElsewhere(){
            int i = 1;
            return i;
        }

        private void closeDialog(java.awt.event.WindowEvent evt){
            this.setVisible(false);

        }
        
        public boolean shallwequit(){
        	boolean thisquit = pleasequit;//set by the user
        	pleasequit = false;
        	return thisquit;
        }

        private javax.swing.JPanel buttonPanel;
        private javax.swing.JButton okButton;
        private javax.swing.JButton cancelButton;
        private javax.swing.JLabel jLabel1;


}
