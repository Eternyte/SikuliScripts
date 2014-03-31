/**
 * ConvertImage.java
 * description: Using Sikuli, this program will ask the user for a folder
 * 				filled with the images the user desires to convert from one
 * 				extension to another. Using GIMP, the program will convert the
 * 				images from one extension to another.
 * preconditions:
 * 				As of now, the program assumes the following conditions.
 * 					1. The desired extension is .png.
 * 					2. The user has GIMP downloaded on their computer.
 * 					3. The user has GIMP listed as a Start Menu program.
 * 					4. GIMP is the default application for .psd files.
 * 					5. The folder has an Input folder with the current images.
 * 					6. The folder has an Output folder for the new images.
 * 					7. There are no files of the same name, possibly different
 * 					   extensions, in the output folder.
 * 					8. GIMP > File has to be visible upon running the program.
 * 					9. All images are unique, especially of GIMP's File.
 * 				Given enough motivation, I may make this more generic and
 * 				remove some of the preconditions listed here. Because that is
 * 				an absurd amount of preconditions.
 * motivations: 1. This program is useful for when my parents receive images 
 * 				that they cannot open on their own computers.
 * 				2. This is practice using Sikuli, which I've wanted to learn 
 * 				for a while now but I haven't had anything to use it for.
 * 				3. This is practice automating something, because I don't
 * 				automate when I think I'll never need to do this again.
 * 				4. Actually, I could use this to convert my own GIMP drawings
 * 				to .png extension, which I haven't done because it's a pain.
 * author: Stacy Chen (sjc5938@rit.edu)
 */
import org.sikuli.basics.Settings;
import org.sikuli.script.*;
import java.util.Scanner;
import java.io.File;
import java.awt.Desktop;
import java.io.IOException;

public class ConvertImage {
	// The default folder path, change if desired.
	private static final String DEFAULTFILENAME = 
			"C:/Users/Shadow/Desktop/Images";
	// The similarity of the pattern and the screen.
	private static float similarity = (float) 0.8;
	// The time given to find this pattern on the screen.
	private static int timeout = 5;
	
	/**
	 * Main runs the program and asks the user for a folder. It then converts 
	 * the images in this folder to a new extension using GIMP.
	 * @param args	Nothing.
	 */
	public static void main(String[] args) {
		// The list of files from the folder path specified by the user.
		File[] listOfFiles = getListOfFiles();
		// Turn off the Sikuli default action logs.
		Settings.ActionLogs = false;
		// Boolean is true if all steps have been performed.
		boolean doesWork = true;
		
		// File - clicking this in GIMP opens a drop down.
		Pattern fileDropdown = new Pattern("images/FileDropdown.png");
		// Export - clicking this in GIMP allows you to export images.
		Pattern export = new Pattern("images/Export.png");
		// Images - clicking this moves up from the Input folder.
		Pattern images = new Pattern("images/Images.png");
		// Output - clicking this moves down to the Output folder.
		Pattern output = new Pattern("images/Output.png");
		// SelectFileType - clicking this shows all the extensions.
		Pattern selectFileType = new Pattern("images/SelectFileType.png");
		// Dropdown - the dropdown shown from select file type.
		Pattern dropdown = new Pattern("images/Dropdown.png");
		// Scrollbar - scroll bar next to the drop down
		Pattern scrollbar = new Pattern("images/Scrollbar.png");
		// PNG - clicking this chooses the PNG file extension.
		Pattern png = new Pattern("images/png.png");
		// ExportButton - clicking this finalizes exporting the image.
		Pattern exportButton = new Pattern("images/ExportButton.png");
		// Quit - closes GIMP.
		Pattern quit = new Pattern("images/Quit.png");
		
		// For every file, convert the file to the new extension.
		File tempFile;
		for (int i = 0; i < listOfFiles.length; i++) {
			tempFile = listOfFiles[i];
			
			// Try opening the file with the default application.
			try {
				Desktop.getDesktop().open(tempFile);
				
				// User feedback with attempt to export image.
				System.out.println("Opened " + tempFile.getName());
				doesWork &= attemptClick(fileDropdown, "file drop down");
				doesWork &= attemptClick(export, "export image");
				doesWork &= attemptClick(images, "up a folder to Images");
				doesWork &= attemptClick(output, "down a folder to Output", 2);
				doesWork &= attemptClick(selectFileType, "to see extensions");
				doesWork &= attemptDrag(dropdown, scrollbar);
				doesWork &= attemptClick(png, "to select extension png");
				doesWork &= attemptClick(exportButton, "to export image", 2);
				doesWork &= attemptClick(exportButton, "to export image", 2);
				
				// Return the final result of the attempt to export.
				if (doesWork) {
					System.out.println("Exported " + tempFile.getName());
				} else {
					System.out.println("Failed with " + tempFile.getName());
				}
			} catch (IOException e) {
				System.err.println("Cannot open file of the name " +
						tempFile.getName() + " with default application.");
				System.err.println("Exiting program.");
				System.exit(1);
			}
		}
		
		// Clean up the mess you've made.
		attemptClick(fileDropdown, "file drop down");
		attemptClick(quit, "to close GIMP");
	} // main
	
	/**
	 * getListOfFiles returns an array of files given the folder path specified
	 * by the user. If no such path was specified, then use the default path.
	 * @return an array of files given the folder path
	 */
	private static File[] getListOfFiles() {
		// Set up a scanner which uses whitespace as a delimiter. 
		// How to set up delimiter: http://stackoverflow.com/questions/5071458/
		Scanner in = new Scanner(System.in);
		java.util.regex.Pattern delimiters = 
				java.util.regex.Pattern.compile("\\s");
		in.useDelimiter(delimiters);
		
		// Ask the user to input a folder path.
		System.out.print("Folder with the images to convert: ");
		String fileName = in.next();
		// If the user did not specify a path, then use the default file name.
		if (fileName.length() == 0) {
			fileName = DEFAULTFILENAME;
		}
		
		// Get the list of images to convert.
		// How to get files: http://stackoverflow.com/questions/5694385/
		// Difference in paths: http://stackoverflow.com/questions/1099300/
		File folder = new File(fileName + "/Input");
		File[] listOfFiles = folder.listFiles();
		return listOfFiles;
	} // getListOfFiles
	
	/**
	 * attemptClick attempts to click the pattern given and prints the given
	 * feedback if the click was successful. Otherwise, it returns an error.
	 * This function allows clicking only once.
	 * @param pattern	the pattern to click
	 * @param feedback	the string to print if the click was successful
	 * @return	true	if the click was successful
	 * 			false	if the click failed
	 */
	private static boolean attemptClick(Pattern pattern, String feedback) {
		return attemptClick(pattern, feedback, 1);
	}
	
	/**
	 * attemptClick attempts to click the pattern given and prints the given
	 * feedback if the click was successful. Otherwise, it returns an error.
	 * This function allows clicking once or twice.
	 * @param pattern	the pattern to click
	 * @param feedback	the string to print if the click was successful
	 * @param count		the number of times to click
	 * @return	true	if the click was successful
	 * 			false	if the click failed
	 */
	private static boolean attemptClick
							(Pattern pattern, String feedback, int count) {
		// The snapshot of the current desktop screen.
		Screen screen = new Screen();
		
		// Attempt to find the pattern in the screen shot.
		if (screen.exists(pattern.similar(similarity), timeout) != null) {
			try {
				// Try to click the pattern.
				if (count == 1) { screen.click(pattern); }
				else if (count == 2) { screen.doubleClick(pattern); }
				
				if (feedback.isEmpty() == false) {
					System.out.println("\t Clicked " + feedback + ".");
				}
				return true;
			} catch (FindFailed e) {
				// If failed to click the pattern, then the pattern was lost.
				System.err.println("Lost sight of pattern.");
				return false;
			}
		} else {
			System.err.println("Could not find the pattern in the beginning!");
			return false;
		}
	} // attemptClick
	
	/**
	 * attemptDrag attempts to drag the pattern given and prints the given
	 * feedback if the drag was successful. Otherwise, it returns an error.
	 * @param pNextTo	the pattern that is to the left of the pattern to drag	
	 * @param pDrop		the pattern to drag
	 * @return	true	if the drag was successful
	 * 			false	if the drag failed
	 */
	private static boolean attemptDrag(Pattern pNextTo, Pattern pDrop) {
		// The snapshot of the current desktop screen.
		Screen screen = new Screen();
		
		try {
			// Find the closest match to pDrop that's to the right of pNextTo.
			// Try to drag and drop the pattern down to the location.
			Match match = screen.find(pNextTo).right().find(pDrop);
			Location location = new Location(match.x, match.y + 100);
			screen.dragDrop(match, location);
			System.out.println("\t Dragged and dropped the sidebar.");
			return true;
		} catch (FindFailed e) {
			// If failed to click the pattern, then the pattern was lost.
			System.err.println("Lost sight of pattern.");
			return false;
		}
	} // attemptDrag
} // ConvertImage

// ERRORS ENCOUNTERED:
// Null pointer exception when trying to access listOfFiles
// most likely means that your folder path is wrong.