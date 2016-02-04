package Entity;

import java.applet.Applet;
import java.applet.AudioClip;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;
import java.util.Vector;

	public class Sound {
		public static void playMusic(String directory, String mode)
		 {
			 try { 
				 URL cb; 
				 File f = new File(directory); //absolute directory
				 cb = f.toURL(); 
				 AudioClip aau; 
				 aau = Applet.newAudioClip(cb); 
				 
				 if(mode.equals("play"))			 aau.play();
//				 System.out.println("Playing");
		
				 else if(mode.equals("loop")) aau.loop();
				 else if(mode.equals("stop")) aau.stop();

				 
				 } catch (MalformedURLException e) { 
				 e.printStackTrace(); 
				 } 
		 }
	}
