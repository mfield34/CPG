# CPG============================

Coulson Plot Generator (CPG)

============================
Release 1.6 (v1.6.1)
============================

Last updated 2014-08-02 by H.I.Field
CPG copyright Helen Field 2009-2050


CONTENTS:

Disclaimer and license.
Copyright
System requirements
CPG Description
Input data formatting
How to use CPG
BUG fixes and updates, Xalan-J
Contact.


ACCOMPANYING DOCUMENTS:
xalanLICENSE.txt
CPG_LICENSE.txt

ALSO Note that this software is now bundled with Apache xalan-j in order to provide svg output, so is also accompanied by the Apache licences.

This software is written in java and packaged as a Mac application.
It is subject to the Artistic License 2.0, or as previously released:

============================

Coulson Plot Generator (CPG)

============================
Release 1.6 (v1.6.1)
============================

Artistic License 2.0
=====================
Artistic License 2.0
Copyright (c) 2000-2006, The Perl Foundation.

Everyone is permitted to copy and distribute verbatim copies of this license document, but changing it is not allowed.

Preamble
This license establishes the terms under which a given free software Package may be copied, modified, distributed, and/or redistributed. The intent is that the Copyright Holder maintains some artistic control over the development of that Package while still keeping the Package available as open source and free software.

You are always permitted to make arrangements wholly outside of this license directly with the Copyright Holder of a given Package. If the terms of this license do not permit the full use that you propose to make of the Package, you should contact the Copyright Holder and seek a different licensing arrangement.

Definitions
"Copyright Holder" means the individual(s) or organization(s) named in the copyright notice for the entire Package.

"Contributor" means any party that has contributed code or other material to the Pack-age, in accordance with the Copyright Holder's procedures.

"You" and "your" means any person who would like to copy, distribute, or modify the Package.

"Package" means the collection of files distributed by the Copyright Holder, and derivatives of that collection and/or of those files. A given Package may consist of either the Standard Version, or a Modified Version.

"Distribute" means providing a copy of the Package or making it accessible to anyone else, or in the case of a company or organization, to others outside of your company or organization.

"Distributor Fee" means any fee that you charge for Distributing this Package or providing support for this Package to another party. It does not mean licensing fees.

"Standard Version" refers to the Package if it has not been modified, or has been modified only in ways explicitly requested by the Copyright Holder.

"Modified Version" means the Package, if it has been changed, and such changes were not explicitly requested by the Copyright Holder.

"Original License" means this Artistic License as Distributed with the Standard Version of the Package, in its current version or as it may be modified by The Perl Foundation in the future.

"Source" form means the source code, documentation source, and configuration files for the Package.

"Compiled" form means the compiled bytecode, object code, binary, or any other form resulting from mechanical transformation or translation of the Source form.

Permission for Use and Modification Without Distribution
(1) You are permitted to use the Standard Version and create and use Modified Ver-sions for any purpose without restriction, provided that you do not Distribute the Modified Version.

Permissions for Redistribution of the Standard Version
(2) You may Distribute verbatim copies of the Source form of the Standard Version of this Package in any medium without restriction, either gratis or for a Distributor Fee, provided that you duplicate all of the original copyright notices and associated disclaimers. At your discretion, such verbatim copies may or may not include a Compiled form of the Package.

(3) You may apply any bug fixes, portability changes, and other modifications made available from the Copyright Holder. The resulting Package will still be considered the Standard Version, and as such will be subject to the Original License.

Distribution of Modified Versions of the Package as Source
(4) You may Distribute your Modified Version as Source (either gratis or for a Distributor Fee, and with or without a Compiled form of the Modified Version) provided that you clearly document how it differs from the Standard Version, including, but not limited to, documenting any non-standard features, executables, or modules, and provided that you do at least ONE of the following:

(a) make the Modified Version available to the Copyright Holder of the Standard Ver-sion, under the Original License, so that the Copyright Holder may include your modifications in the Standard Version.
(b) ensure that installation of your Modified Version does not prevent the user installing or running the Standard Version. In addition, the Modified Version must bear a name that is different from the name of the Standard Version.
(c) allow anyone who receives a copy of the Modified Version to make the Source form of the Modified Version available to others under
(i) the Original License or
(ii) a license that permits the licensee to freely copy, modify and redistribute the Modified Version using the same licensing terms that apply to the copy that the licensee received, and requires that the Source form of the Modified Version, and of any works de-rived from it, be made freely available in that license fees are prohibited but Distributor Fees are allowed.
Distribution of Compiled Forms of the Standard Version or Modified Versions without the Source
(5) You may Distribute Compiled forms of the Standard Version without the Source, provided that you include complete instructions on how to get the Source of the Standard Version. Such instructions must be valid at the time of your distribution. If these instructions, at any time while you are carrying out such distribution, become invalid, you must provide new instructions on demand or cease further distribution. If you provide valid instructions or cease distribution within thirty days after you become aware that the instructions are invalid, then you do not forfeit any of your rights under this license.

(6) You may Distribute a Modified Version in Compiled form without the Source, provided that you comply with Section 4 with respect to the Source of the Modified Version.

Aggregating or Linking the Package
(7) You may aggregate the Package (either the Standard Version or Modified Version) with other packages and Distribute the resulting aggregation provided that you do not charge a licensing fee for the Package. Distributor Fees are permitted, and licensing fees for other components in the aggregation are permitted. The terms of this license apply to the use and Distribution of the Standard or Modified Versions as included in the aggregation.

(8) You are permitted to link Modified and Standard Versions with other works, to em-bed the Package in a larger work of your own, or to build stand-alone binary or bytecode versions of applications that include the Package, and Distribute the result without restriction, provided the result does not expose a direct interface to the Package.

Items That are Not Considered Part of a Modified Version
(9) Works (including, but not limited to, modules and scripts) that merely extend or make use of the Package, do not, by themselves, cause the Package to be a Modified Ver-sion. In addition, such works are not considered parts of the Package itself, and are not subject to the terms of this license.

General Provisions
(10) Any use, modification, and distribution of the Standard or Modified Versions is governed by this Artistic License. By using, modifying or distributing the Package, you accept this license. Do not use, modify, or distribute the Package, if you do not accept this license.

(11) If your Modified Version has been derived from a Modified Version made by some-one other than you, you are nevertheless required to ensure that your Modified Version complies with the requirements of this license.

(12) This license does not grant you the right to use any trademark, service mark, tradename, or logo of the Copyright Holder.

(13) This license includes the non-exclusive, worldwide, free-of-charge patent license to make, have made, use, offer to sell, sell, import and otherwise transfer the Package with respect to any patent claims licensable by the Copyright Holder that are necessarily in-fringed by the Package. If you institute patent litigation (including a cross-claim or counterclaim) against any party alleging that the Package constitutes direct or contributory patent infringement, then this Artistic License to you shall terminate on the date that such litigation is filed.

(14) Disclaimer of Warranty: THE PACKAGE IS PROVIDED BY THE COPYRIGHT HOLDER AND CONTRIBUTORS "AS IS' AND WITHOUT ANY EXPRESS OR IMPLIED WARRANTIES. THE IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE, OR NON-INFRINGEMENT ARE DISCLAIMED TO THE EXTENT PERMITTED BY YOUR LOCAL LAW. UNLESS REQUIRED BY LAW, NO COPYRIGHT HOLDER OR CONTRIBUTOR WILL BE LIABLE FOR ANY DIRECT, IN-DIRECT, INCIDENTAL, OR CONSEQUENTIAL DAMAGES ARISING IN ANY WAY OUT OF THE USE OF THE PACKAGE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.


+++++++++
COPYRIGHT
=========

Created by H.I.Field copyright 2011

==========
DISCLAIMER
==========

Disclaimer: no responsibility is taken for use or development of this software.


================================
TESTING and Systems requirements
================================
Software was developed for Mac
However, it is pure java and is in theory be compiled for any OS.

Tested on Mac OSX versions 10.6.8 (Snow Leopard) and 10.7.2 (Lion) and 10.8.x (Mountain Lion)
Linux RedHat RHEN5, Ubuntu, Solaris 11g
Windows 7, 2008, Server 2003.

Memory requirements: 
---------------------
For proper rendering of saved images at least 1 GB RAM is required: -Xmx2G -XX:MaxPermSize=512M

In the Mac app these settings are automatically applied. 
If running the jar file alone these may be entered on command line or the cpg.sh used (below).

USING THE DOWNLOADS:
---------------------
Test input data and a sample color file are provided with the Download.

To use on MAC:

The installer places the Application in your /Applications folder
together with a folder containing the test data.
Navigate to your personal Applications folder, and into the folder CPG.
Double click on the CPG icon.

To use on LINUX (command line):

Ensure that the .cpg, CPG_lib and Resources folders are present alongside the jar file, by entering on the command line*:
> ls -la

To run from the command line enter these two commands:
> cd WinLin/CPG
> java -Xmx2G -XX:MaxPermSize=512M -jar CPG.jar

OR use the shell script*:
> cd WinLin/CPG
> sh CPG.sh 

An alternative to the latter command:
> ./CPG.sh

* You can try increasing the -Xmx and -XX:MaxPermSize parameters for java memory.


To use on WINDOWS:

The application is provided for Windows as a double-clickable jar file.

Unpack the WinLin.zip folder (by double clicking on it).
Put the WinLin folder into a convenient location (anywhere).
In Windows Explorer, navigate to the folder WinLin/CPG and 
double click on CPG.jar. 
IMPORTANT: do not remove CPG.jar from its location inside the its parent folder or it will not work.
Keep the whole WinLin folder intact.


====================
CPG Description 
====================

The Coulson Plot Generator (CPG) provides a visual comparison of entities, as an array of pie diagrams in rows and columns:
		oooo
		oooo
		oooo
		
The diagram is labeled (if required) and may resized, recolored and then exported for touch up in a PDF or drawing editor.

Each pie represents an entity which is divided into subcomponents (wedges).
Each pie can represent a component whose subcomponents (genes, traits or other fac-tors) are present or absent (shown as filled or empty pie slices, respectively)
 in different entities (e.g. species).

The input is a .csv (comma or tab separated) text file, which governs
- the number of pies per row 
- the number of pies in each column
- the number of wedges per pie (these can differ)
- the labels and sublabels for rows (contained in the top 4 rows heading each column)
- the pie labels, wedge labels, and the number of  wedges per pie (contained in the first 2 columns)
- presence or absence of a subcomponent (denoted by + or - representing presence and absence (1 and 0 may also be used))

CPG creates a set of pie diagrams from a .csv file containing four column headings (4), and two row labels (2). 
See accompanying document (pies.jpeg) for an example of the output graphic, and the Coulson_Plot.csv input file for an actual input.

======================
Input data formatting:
======================

Generate an input file containing your data, in a table editor that can generate tab or comma separated output.
Input is a text file (tab or comma separated) containing a table of pie wedges (use + or 1 to fill, - or 0 for empty).

In the input file there are 4 lines of headers, describing e.g. 
-Phylum (row 1) 
-Kingdom (row 2)
-SPECIES (row 3) - the main header in the plot, labelling each row of pies
-other info (row 4)
Omit the first two columns: these are used to describe the pies.

The pies and wedges are described in Columns 1 and 2 (omit the header rows):
The pie title appears in column 1 and the pie wedge names in column 2:
- in Column 1, the pie name must be placed once, at the top of the list of pie wedges alongside the first pie wedge name in Column 2 
- in Column 2, *every* pie wedge has a title/entry

Data
- in other columns the wedge occupancy data is placed, using +/- or 1/0 for filled/empty wedges.Use the same system throughout.


See the INPUT examples for details:
- pie.txt is an example of multi-wedged pies
- dot.csv is an example used for a dot plot


IMPORTANT: data appears transposed in the default figure, compared to the layout in the input file. However, the figure may be transposed by CPG.
- row 3  i used for your headers (species) and these will appear on the left as pie ROW labels.
- the first two COLUMNS contain the names of the pie entities (column 1) with subcomponent names (column 2) and appear across the top of the graphic
	as entity names, and a labeled pie. 

To trouble shoot data inputs, you may wish to review the output messages in the log file.
This is available as a log tab, in the first CPG window: to see it, select 'Show Log' in the Preferences (and then restart).

A Log, available in the first window as a fourth tab (set using Preferences) 
  gives you information on file formatting and data processing. 
  
If input data is unacceptable the Figure button is inactivated;
  also the data may not appear to be inserted into the table (second window).
  Informative messages appear in the Log (first window), 
  and important messages also appear in Console output (system log). 

INPUT examples (pie.txt, dot.csv)

==============================
Using CPG to generate figures:
==============================

Open the application (above). 
In the first window with 4 tabs, use 'Open Data File' to select the input file.

To choose your own colors, or return to the default (hard coded) selection,
use the Color tab in the first window. 
A Color picker is available for creating a file of colors: if only a few are selected then they are iterated in the final diagram.

The third tab contains the Help message (this document).

The fourth tab (optional) contains the Log; this may be hidden (use Preferences to deselect 'Show Log' and then restart CPG). The log is used to help sort out any formatting issues with input data.

INPUT DATA FILES
Data input files must be prepared 'by hand', as a spreadsheet.
Please review the accompanying test data file examples,
 and ensure the following rules apply to your own dataset:
- text file
- tab or comma separated
- 3-4 headers across the top (labels e.g. taxa) and 2 down the left hand side (pies e.g. protein complexes)
- 3 or 4 header rows which may or may not be filled in (first 2 items empty)
- 2 'header' columns (top rows empty)
	- column 1 only populated at the start of a 'pie'
        - column 2 labels every segment
  Note that for a dot blot every 'header' column 1 row has a value/label in column 1
- body of table populated with either + and - or 0 and 1


After selecting an input file, a table window appears.
If the input is correct then the data appears in the table 
(omitting all but the header line on Row 3, which should read as text labels: 
Although you cannot see Row1 and Row2 headings they will appear on your figure.
If you have a fourth row of headers these labels appear alongside those from Row3 but only in the figure. 

If the figure button is not active, please review the Log (fourth tab in first window) to detect problems with the input data.

You may use the 'Open Data File' button to open several table views on different input files.

From the table window, you can generate a Coulson Plots, many times with variations.
Use 'Figure' to generate a plot.
Each time you may wish to test some parameters e.g. font or pie size. 
If any item is out of range it is automatically corrected.

In the CPG diagram window, you may Save the picture as a PDF or other image format.
Use a very large JPEG for good resolution.
You may change the sizes of the pies especially for a diagram with many pies.

For a large diagram, you can increase the number of pies per page by:
- reducing the size of individual pies (Row height, Pie diameter) and the font size
- increasing the size of the view window containing the Coulson Plot diagram
- for non-editable images, decrease the scale: NOTE - increase the scale for better resolution 
- NOTE for printing to an editable PDF, you must see all the pies in the window, 
  and use Page set up to create a printout of the correct size and orientation
  (there is no warning about clipping).
 
DO center the image in the view window, in order to avoid white space in your PDF.
However, from Release 1.1 (which has a zoom facility), if you resize the window then CPG will attempt to resize the drawing for the window size. 

Use the zoom in/out to create the view that you need: this zooming will not affect the final output drawing size

In Preferences you can set the application to quit without asking (implemented without restarting the program).

The output (PDF) is NOT camera ready: it is designed to be opened with a Scalable Vector Graphics  package
such as Adobe Illustrator or Inkscape,
and edited: each element may be fully unmasked and reformatted, but not within CPG.

For convenience, figures may be saved in several graphics formats including svg (use large scale for better resolution). 
Page set up will also affect the output (amount of data that can be viewed per page).

When using Save image to save non-editable images as jpg, jpeg, bmp, wbmp, png, svg etc,
the scale can be decreased to fit more into a page, or increased for a high resolution image (and shrunk later for printing).
Selecting the output image format will then action the 'Save as' File dialog.

---------------------
UPDATES and BUG FIXES
---------------------
Most recent first:


---------------------
Current Release 1.6 
on 2 August 2014 
---------------------

Development Version 1.6.1 --- 2 August 2014

New feature
	None
Bugs fixed: 
	CoulsonPlotPrintable added a missing top line of data when using 4 headers (correct if 3 headers).
	Link on About screen updated for University of Dundee (Mark Field)

Development Version 1.4.9 --- 19 March 2013

New features
     * Log tab SHOWN by default 1.4.5 (1.1 Patch 1)
     * Remove line from singleton 1.4.6 (1.1 Patch 2)
     * Add Save as button for saving image to file, and SVG save capability 1.4.7 (1.1 Patch 3)
     * Refresh problem: increased memory. Change Figure to Plot.. 1.4.8 (1.1 Patch 4)
     * Zoom on window resize working. Fit to window working. First plot left alone so blanks not so obvious. Zoom set to increase or decrease by 1/20 instead of 1/10 when resizing (1.1 Patch 5)

Bugs fixed: 
	Zoom on window resize working. Fit to window working. 
	First plot left alone so blanks not so obvious. 
	Zoom set to increase or decrease by 1/20 instead of 1/10 when resizing with Cmd- or Cmd=


Development Version 1.4.8 --- 18 March 2013

Bugs fixed: 
	Refresh in windows addressed. Increased memory (1G and 2G releases)

Known issue:
	When Coulson plot is first drawn after the initial plot the refresh removes the diagram until either the window or the diagram is resized (use Cmd- or Cmd=, or resize the picture).


Development Version 1.4.7 --- 9 March 2013

Bugs fixed: 
	Single spoke in a circle of one segment now removed.

New features: 
A. Log now shown by default, as fourth tab, to assist with data importing.

B. SVG graphic support is provided, so that images may be saved straight to svg.
This is accessed from the Coulson Plot image window, using File > Save... and choosing svg

SVG generation is provided by xalan-j (apache.org), which is included in the CPG application.

Xalan-j was downloaded as follows on 26 February 2013:
svn checkout http://svn.apache.org/repos/asf/xalan/java/trunk xalan
The apache licence is included as a separate document. 

To create the application a runnable jar file must be exported from Eclipse, with CPG_lib folder containing shared libraries. This can be packaged for Apple using the Jar bundler and putting .cpg, Resources and CPG_lib alongside the CPG.jar in CPG.app/Contents/Resources/Java/


---------------------
Release 1.1 
on 29 October 2012 
---------------------

Development Version 1.4.4.3 --- 24 Oct 2012

Bugs fixed: 
	slight misplacement of central protein labels in non-transposed mode. Grey square appearance.

New feature: 
A. Default path when first opened is that where Application, and TestData folder is found 
	ie in the application folder that one downloads. 

B. The last file picked is used as the next path and 
	*is now* remembered between sessions.


---------------------

Development Version 1.4.4.2 --- 22 Oct 2012

NEW features: 

A.    Figure can be readily enlarged and decreased using the
	- apple/Alt-minus to shrink and apple/Alt-equals to grow it: 
    	- size of the figure as viewed will not affect printing size.

B.    Window resize (in CPG plot window) results in a resizing of the CPG plot to fit the window.
    
C.    Table window remembers the last parameters used (e.g. transpose)

D.    All labels are colored along with the pies (except for the pie labels and pie titles)
	- same colors apply to boxes when drawn around label groupings
	NOTE: if a TOP line label is missing then groupings are not always correctly drawn.

E.    Text clipping occurs when the Coulson plot is first drawn. However
	AFTER resizing or zooming, all of the text is drawn full size. 
	Text boxes should be aligned correctly.

	Previously, in a Graphics editor, the masking clip had to be removed from the editable PDF.


    
KNOWN BUGS/ISSUES:

A. When first shrinking or resizing (fit to window) a large white rectangle may cover the drawing 	- use the Alt- or Alt=
	or resize the window (or fit to window) to recover the CPG plot.
    
B. Fit to window is an attempt to make the drawing fit the window
	- it is not perfect - if resize is not quite right use Alt-/Alt= to correct it.
	
C. When printing to PDF, there is no Warning if the picture will be clipped: 
	- use the print PREVIEW to check what you will get. 
   	- use **Page Setup** to make the print the correct size for the drawing:
	 an external editor can later be used to reduce the size. 
	 Larger printouts for the original PDF will improve resolution in the print quality.
    
---------------------
	

Development Version 1.4.4.1 --- 17 Oct 2012

NEW features: 
	For the figure:
	- when text is larger than the bounding label box, it is clipped in the original image.
	  Now when saved to PDF the text is unmasked (and the bounding box increased in size to surround the text)
	  While these overlap, they are more readily available for workup since the text is unmasked. --- v 1.4.4.1
	  This is true for layout of pies in either orientation (transpose)
	  
	- when transposed, pies are very close together, while the left outer (protein) labels and labelled pies have more space. --- v 1.4.4
	  In the untransposed view with pie labels on top, all the pies are double-spaced in order to give labelled pies sufficient space.
	
	- boxes for outer group labels are now outlined in the same color as the species text, and labels themselves
	  follow the same color scheme as the pies --- v 1.4.4

Bugs fixed: 
	Labels no longer jump around when saving to PDF --- v 1.4.4.
	When hiding then showing layout options in the Table window, items are no longer hidden.
	
KNOWN ISSUE (PLEASE READ):
1	When you choose to leave the color picker (Done) without saving the colors to file, and you are warned
	that you need to save, and Cancel - the color picker still quits. Remember to 'Save' colors to file first.

---------------------

Release 1.0
on 15 March 2012
---------------------


Development Version 0.9.4.2 --- 25 Feb 2012

NEW features: 
	For the figure: 
		Transpose option plots species across the top and protein/pie keys left (input is the same)
		Column gap may be specified. 
		Horizontal gaps may be for Row1, Row1 plus Row2 or no gaps at all.
		Yellow boxes may be drawn around groups for clarity (you may easily create another figure without them
	When you create a figure, if any settings are jigged because out of range you see this in the window.
	In Color Picker the color squares showing what you picked are a decent size  -- 20120206.
	Tiny bug manifesting as ClassCastException preventing restore localisation. -- v0.9.4.1

Bug fixed: 
	In figure, spacing of outer, vertical labels (Row1 and Row2 of input data)
	If ask before quit not required, program just quits (apple-q)
	In colors tab the default colors are properly restored.
	Can use Colors menu item when available (apple-k)
	
Pre release Version 1.1 Dev 0.9.2 -- 4 Jan 2012

New features: Preferences contains:
	 option to see the Log (4th tab in first window), 
	 option to ask before quitting.

Known BUG: in color picker, if you hit Done without saving your color picks, you 
are warned that you need to save. However if you cancel (in order to save) the color picker window closes.

Pre release Version 1.0 Dev 0.9.1 -- Dec 2011

Minor bug fixes and interface changes prior to release.

Since version 0.8.40:

New features:
Consistent menuing and can use Cmd-w to close windows.
Asks if you really want to quit when you use Cmd-q or Quit menu item.
Defaults button in table view restores all parameters (pie size etc) to defaults.
The last parameters you entered are kept between sessions (CPG quit and reopen).
File inputs for 0,1 or +/- data, with tab and comma separators all work.
You may have 3 or 4 header rows, the third being the one that labels the pie rows.

Bug fixes: 
Log removed (again) and Help revised to reflect a full release.

Since version 0.8.3:

New workflow for choosing colors: can intuitively add your own color swatch, 
save this in a file for reuse and remember the last color set used, or go back to defaults.

Since version 0.8.2:
 
About menu, and quit [Programmer: handled through FinderIntegration for Mac]
Preferences added (ditto).
Parameters set in various windows are remembered between sessions.
Location of windows remembered.

	
====================================
Contact: 
Please report bugs to:
mfield@mac.com
====================================

