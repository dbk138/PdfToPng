import java.awt.Image;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.ghost4j.document.PDFDocument;
import org.ghost4j.renderer.SimpleRenderer;

/**
 * This class should take the full path of a PDF or series of PDFs as an argument, loop through each one and
 * convert each PDF into a series of PNG files. The PNG files can then be scaled and compressed. 
 * 
 * The program will generate a folder in the C: drive with the images.
 * 
 * @author dkelly
 *
 */
public class PdfToPng {	
		
	public static void main(String[] args) {
								 
		for(int i = 0; i<args.length; i++){
			convertPdfToPng(args[i]);
		}
	 }	
	
	public static void convertPdfToPng(String pdfFileName) {
		
		try {			 
	         PDFDocument document = new PDFDocument();
	         File baseFile = new File(pdfFileName);
	         document.load(baseFile);
	         
	         //Get todays date as a String so that you can append it to the directory. Format is Month/Day/Year/Hour/Minute/Second
	         String df = new SimpleDateFormat("MMddyyHHmmss").format(new Date());
	              	   
	         //Get the filename without extension so that you can create a directory based on the name
	         Path path = Paths.get(pdfFileName);	
	         String filenameWithExtension = path.getFileName().toString();
	         String filename = FilenameUtils.removeExtension(filenameWithExtension);
	         
	         //System.out.println("Begin converting PDF: " + filenameWithExtension + " at " + new Date());
	         
	         //add the current date to the filename to get the directory name
	         String dirname = filename + "-" + df;        
	         
	         //Create the directory
	         Path dir =  Paths.get("C:/" + dirname);
	         Path p = Files.createDirectory(dir);
	         
	         //System.out.println("New directory created: " + dir.toString());
	         
	         //Move the pdf to the newly created directory
	         FileUtils.copyFileToDirectory(baseFile, p.toFile());
	         
	         // create renderer
	         SimpleRenderer renderer = new SimpleRenderer();
	 
	         // set resolution (in DPI)
	         renderer.setResolution(300);
	 
	         // render
	         List<Image> images = renderer.render(document);
	 
	         // write images to files to disk as PNG
	            try {
	            	
	                for (int i = 0; i < images.size(); i++) {
	                    	                	
	                	RenderedImage renderedImage = (RenderedImage) images.get(i);
	                	String formatName = "png";
	                	
	                	String outputFilePath = "";
	                	 if(i <= 8)
	                		outputFilePath = "C:/" + dirname + "/" + filename + "_00" + (i + 1) + ".png";
	                	 else if(i <= 98) 
	                	 	outputFilePath = "C:/" + dirname + "/" + filename + "_0" + (i + 1) + ".png";
	                	 else
	                		outputFilePath = "C:/" + dirname + "/" + filename + "_" + (i + 1) + ".png";
	                	 
	                	File outputFile = new File(outputFilePath);
	                	ImageIO.write(renderedImage, formatName, outputFile);
	                }
	                
	                //System.out.println("End PDF conversion at " + new Date());
	                Files.delete(path);
	                
	            } catch (IOException e) {
	            	System.out.println("IOERROR: " + e.getMessage());
	            }
	 
	       } catch (Exception e) {
	    	   System.out.println("ERROR: " + e.getMessage());
	       }
	}

}	
