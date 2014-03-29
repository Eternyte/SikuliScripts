/**
 * HelloSikuli.java
 * description: Hello World for Sikuli.
 * 				Basically, code from http://www.paulfrymire.com/2011/08/30
 * 
 */
import org.sikuli.script.*;

public class HelloSikuli {
	public static void main(String[] args) {
		// Decimal value from 0.0 to 1.0 representing the 
		// similarity between the image and the screen shot.
		float similarity = (float) 0.8;
		
		// Number of seconds Sikuli will spend attempting to find
		// a match between the pattern and the screen shot.
		int timeout = 5;
		
		// Instantiate Sikuli Pattern object by passing in an image.
		Pattern windowsStartButton = 
				new Pattern("images/WindowsStartButton.png");
		
		// Instantiate Sikuli Screen object which takes a snapshot of your
		// current desktop. All comparisons will be performed against the
		// contents of this object.
		Screen screen = new Screen();
		
		// Attempt to find the pattern in the screen shot using the similarity
		// level and the timeout specified above. These values are optional.
		// If left out, Sikuli will use its default.
		if (screen.exists(windowsStartButton.similar(similarity), timeout)
				!= null) {
			try {
				// Pattern was found in screen and we will now 
				// attempt to click the pattern that was located.
				screen.click(windowsStartButton);
			} catch (FindFailed e) {
				// Pattern was lost between the screen.exists statement
				// and the attempt to click.
				System.err.println("Lost sight of pattern.");
				System.exit(1);
			}
		} else {
			System.out.println("Couldn't find the pattern in the beginning!");
		}
	}
}

// Important notes:
// [error] Region.exists: seems that imagefile could not be found on disk
// If you've received the error above, your file path is wrong.
// Check your file path when instantiating a new pattern.