import java.io.*;
import java.util.*;
/** Replace %CODE% with requested id, and
 ** replace %ALTCODE% w/"dashed" version of id.
 */
public class CodeReplacer {
    public final String TEMPLATE_DIR = "templatedir";
	String sourceTemplate;
	/**
	* @param reqId java.lang.String
	* @param oStream java.io.OutputStream
	* @exception java.io.IOException The exception description.
	*/

    private String readTemplate() {
		String templateName = System.getProperty(TEMPLATE_DIR, "") + "template.html";
		StringBuffer sb = new StringBuffer("");
		BufferedReader br = null;
		try {
			br = new BufferedReader( new FileReader( templateName ));
			String line;
			while(((line=br.readLine())!= null)) {
				sb.append( line);
				sb.append( "\n");
			}
	    
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println( "exception caught" + e.getCause() +e.getMessage());
		} finally {
			try {
				br.close();
			} catch( IOException ignored) { // coz u can't do anything 
			}
		}
		return sb.toString();
    }

    //Refactored Code in the below Method
    private String substituteForCode( String template, String reqId, String substitutablePattern) {
		if (substitutablePattern.equalsIgnoreCase("%CODE%")) {
			template = template.replaceAll(substitutablePattern, reqId);   
		} else if (substitutablePattern.equalsIgnoreCase("%ALTCODE%")) {
			template = template.replaceAll(substitutablePattern, reqId.substring(0,5) +"-" +reqId.substring(5));
		}
		return template;
    }


    public void substitute(String reqId, PrintWriter out) throws IOException {
		sourceTemplate = readTemplate();
		try {
			String template = sourceTemplate;
			template = substituteForCode( template, reqId, "%CODE%");
			String result = substituteForCode( template, reqId, "%ALTCODE%");
			out.print(result);
		} catch (Exception e) {
			System.out.println("Error in substitute()");
		}
		out.flush();
		out.close();
    }
}


