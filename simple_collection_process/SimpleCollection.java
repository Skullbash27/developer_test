import java.net.URL;
import java.net.*;
import java.io.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

//Import jsoup Web Scrapper Libraries
import org.jsoup.*;  
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;

//Import CommonCSV Libraries
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
 
public class SimpleCollection{  
	
	private static final String folderPath = System.getProperty("user.dir");//Get the Location of the Local Directory	
	private static final String csvOutputFile = System.getProperty("user.dir")+"/Ouput.csv"; //CSV file name

	private static final String URL = "http://ifpartners.com/cut-the-wire/";

	private static final String NEW_LINE_SEPARATOR = "\n";
	private static final Object[] FILE_HEADER = {"Article No", "Title Name", "System DateTime", "Day/Date/Time", "Author", "href", "Content"}; //File Header of the CSV File
	
        public static void main( String[] args ) throws IOException, InterruptedException{  
		
		CSVFormat csvFileFormat = CSVFormat.DEFAULT.withRecordSeparator(NEW_LINE_SEPARATOR);
		
		FileWriter fileWriter = new FileWriter(csvOutputFile);
		CSVPrinter csvFilePrinter = new CSVPrinter(fileWriter, csvFileFormat);
		
		csvFilePrinter.printRecord(FILE_HEADER);

		System.setProperty("http.agent", "Mozilla/5.0"); //Important to let the web server know that this is a valid connection and not a Bot
		    
		//Connect to the website
    		Connection connection = Jsoup.connect(URL);
		Document doc = connection.get();

		Elements ele = doc.select("article"); //Extracting from the 'Article' Tag in the HTML
		int count = 1;
		for (Element elem : ele) {
			String name = elem.select("article h2 a").text();
			String dateTime = elem.select("article time").attr("datetime");
			String dayDate = elem.select("article time").text().substring(10);
			String href = elem.select("article h2 a").attr("href");
			String author = elem.select("article p.byline a").text();
			String content = elem.select("article div.entry-content").text();

			if (author == null) {
				author = "No Author Present";
			}
			
           		ArrayList<String> article = new ArrayList<>();
			article.add(String.valueOf(count));
			article.add(name);
			article.add(dateTime);
			article.add(dayDate);
			article.add(author);
			article.add(href);
			article.add(content);
			csvFilePrinter.printRecord(article);

			count++;

			if(count == 7) {
				break;
			}
		}
  
		try {
	        	fileWriter.flush();
	                fileWriter.close();
	                csvFilePrinter.close();
		} catch (IOException e) {
	                System.out.println("Error while closing CSVPrinter");
	                e.printStackTrace();
		}
	       
		//Extra Credit
		Thread.sleep(500); //Hold for 5 milisecond in order to avoid any issue with the Web Throwing a 403 Response.
		Elements img = doc.select("img");
		int imageNo = 0;

		for(Element imageElement : img){
			if (imageNo == 0) {			
				imageNo++;
			 	continue;
			}            	
			
			if(imageNo > 1) {	
				break;
			}	 

            		// Geting the absolute URL using abs: prefix
            	 	String src = imageElement.attr("abs:src");
			getImages(src);
			imageNo++;			
		}
	}  
	
	private static void getImages(String strImageURL) throws IOException {

	      	String strImageName = strImageURL.substring( strImageURL.lastIndexOf("/") + 1 );       
        	System.out.println("Saving: " + strImageName + ", from: " + strImageURL);
        
		try {           
            		//open the stream from URL
            		URL urlImage = new URL(strImageURL);
            		InputStream in = urlImage.openStream();
            		byte[] buffer = new byte[4096];
            		int n = -1;

            		OutputStream os = new FileOutputStream( folderPath + "/" + strImageName );
            
           		//write bytes to the output stream
            		while ( (n = in.read(buffer)) != -1 ){
                		os.write(buffer, 0, n);
            		}
            
            		//close the stream
            		os.close();
            		System.out.println("Image saved");
            
        	} 
		catch (IOException e) {
			System.out.println("Error while Saving Image");
            		e.printStackTrace();
        	}   
	}
}
