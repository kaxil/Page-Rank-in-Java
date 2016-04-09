import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.regex.Pattern;

public class UrlExtractor {
							// Question 1(1)
	public UrlExtractor() {
		// TODO Auto-generated constructor stub
	}
	
							// Question 1(2)
	public static void scanAndPrint ( Scanner in ) throws FileNotFoundException {
// Using the logic learned in BoldFind.java in lab
// Scanner will scan line by line
		
		while(in.hasNextLine()){
			String page = in.useDelimiter(Pattern.compile("\\A")).nextLine();
// Giving the condition that it should exit the scanner when it encounters just "exit" on a Line
			if(page.equals("exit")){break;}
			int flag = -1; 

// Extracting the URLs from the File			
// Checking for "<a href" in the string page			
			for (int i = 0; i < page.length(); i++) {
				if (i + "<a href".length() <= page.length()) { 
					
					String tag = page.substring(i, i + "<a href".length());
					if (tag.equalsIgnoreCase("<a href") ){  //Checks for all possibility of Cases for <a href
						 flag = i;						
					}
				}
				
				if (i + ">".length() <= page.length()) {
					String tag = page.substring(i,i+">".length());
					
					// Question 1(3) : Ignores Case
// Checking the closing tag ">" & creating a substring
// Then checking for the closing double quote after the link to extract the url
					
					if (tag.equalsIgnoreCase(">")  && flag != -1) { 
						String dummy=page.substring(flag + ("<a href=".length() + 1), i);
						int dindex=dummy.indexOf("\"");
						String dummy1 = dummy.substring(0, dindex);
						String printlink= MyURL.parseURL(dummy1);
						if(printlink!=null){
						System.out.println(printlink);
						}
						flag = -1;
					}
				}
			}
		}
	}
							// Question 2(1)
	private static boolean matchServer(String url, String str){
		// Checks the server path of the URL with the given string
		if((url!=null && str!=null)   )
		{
			String[] url1=new String[2];
			if(url.contains("http://"))
			{
				url1= url.split("http://",2);
			}     
			else{
				if(url.contains("https://"))
				{
				url1= url.split("https://",2);
				}
			}
		String[] url2= url1[1].split("/",2);		// Gives the server path of the link
		
			if(url2[0].toLowerCase().contains(str.toLowerCase())){
				return true;	
			}
			else
				return false;
		}
		else
			return false;
		
	}
	
							// Question 2(2)
	public static void scanAndPrint(Scanner in , String str ){
		// Using the logic learned in BoldFind.java in lab
		// Scanner will scan line by line
					
		while(in.hasNextLine()){
			String page = in.useDelimiter(Pattern.compile("\\A")).nextLine(); 
			
			int flag = -1;

		// Extracting the URLs from the File			
			
			for (int i = 0; i < page.length(); i++) {
				if (i + "<a href".length() <= page.length()) {
					String tag = page.substring(i, i + "<a href".length());
					if (tag.equalsIgnoreCase("<a href") ){  //Checks for all possibility of Cases for <a href
						 flag = i;						
					}
				}
				
				if (i + ">".length() <= page.length()) {
					String tag = page.substring(i,i+">".length());
					
					// Checking the closing tag ">" & creating a substring
					// Then checking for the closing double quote after the link to extract the url
					
					if (tag.equalsIgnoreCase(">")  && flag != -1) { 
						String dummy=page.substring(flag + ("<a href=".length()+1), i);
						int dindex=dummy.indexOf("\"");
						String dummy1 = dummy.substring(0, dindex);

						String printlink= MyURL.parseURL(dummy1);
						if(printlink!=null){		
							// Using matchServer to check whether the str is present in url or not
							if(matchServer(printlink,str)==true)
							{
								 System.out.println(printlink);
							}
						}
																		
						flag = -1;
					}
				}
			}
		}
	}
	
	
	public static void main(String[] args) throws FileNotFoundException {

// Question 1(4.2): The DataSet is provided in a file
		// Path of the File where three-page.txt is stored
		// Assuming that the File to be read is stored in the WorkSpace
		File inFile = new File("three-page.txt");
		Scanner in = new Scanner(inFile);
		System.out.println("Output of Question 1(4.2)");
		scanAndPrint(in);
		
		System.out.println();
		
			    
	    				// Question 2(3):  matchServer on 7 Examples
	    System.out.println("Question 2(3) Output");
		String[] myURLs1 = { "http://www.google.com", "google.com"};
		String[] myURLs2 = { "http://www.theguardian.com", "GUARDIAN"};
		String[] myURLs3 = { "http://www.Guardian.co.uk/global-development", "guardian"};
		String[] myURLs4 = { null, "guardian.co.uk"};
		String[] myURLs5 = { "http://theguardian.com", "theguardian.com"};
		String[] myURLs6 = { "http://www.yahoo.com", "yahoo.com"};
		String[] myURLs7 = { "http://www.msn.com", "MSN"};
		
		boolean strmatch=matchServer(myURLs1[0], myURLs1[1]);
		System.out.println("'" + myURLs1[0] + "' and '" + myURLs1[1] + "'=>" + strmatch);
		
		strmatch=matchServer(myURLs2[0], myURLs2[1]);
		System.out.println("'" + myURLs2[0] + "' and '" + myURLs2[1] + "'=>" + strmatch);
		
		strmatch=matchServer(myURLs3[0], myURLs3[1]);
		System.out.println("'" + myURLs3[0] + "' and '" + myURLs3[1] + "'=>" + strmatch);
	
		strmatch=matchServer(myURLs4[0], myURLs4[1]);
		System.out.println("'" + myURLs4[0] + "' and '" + myURLs4[1] + "'=>" + strmatch);
		
		strmatch=matchServer(myURLs5[0], myURLs5[1]);
		System.out.println("'" + myURLs5[0] + "' and '" + myURLs5[1] + "'=>" + strmatch);
		
		strmatch=matchServer(myURLs6[0], myURLs6[1]);
		System.out.println("'" + myURLs6[0] + "' and '" + myURLs6[1] + "'=>" + strmatch);
		
		strmatch=matchServer(myURLs7[0], myURLs7[1]);
		System.out.println("'" + myURLs7[0] + "' and '" + myURLs7[1] + "'=>" + strmatch);
		System.out.println();
	
		
		System.out.println("Question 2(4) Output");
		System.out.println();
			
		// Question 2(4.2): The DataSet is provided in a file
				Scanner in1=new Scanner(inFile);
			    String str2="theguardian.com";
			    scanAndPrint(in1,str2);
		
		// Question 1 (4.1) The DataSet is Provided via Standard input
		System.out.println();
		System.out.println("Enter Dataset [For e.g: <a href=\"google.com\"></a>] : [Type \"exit\" and press Enter when you want to exit this scanner ]");
		Scanner in2= new Scanner(System.in);
	    scanAndPrint(in2);	
	    // Type exit and press enter in console when you want to exit the above scanner
	    
	    
	    // Question 2 (4.1) The DataSet is Provided via Standard input
	    System.out.println("Enter the Full DataSet or just copy/paste contents of file: ");
		Scanner in3= new Scanner(System.in);
	    scanAndPrint(in3,str2);	
	    in3.close();

	}


}
