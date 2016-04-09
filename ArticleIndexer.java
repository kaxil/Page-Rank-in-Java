import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;

public class ArticleIndexer {

	public ArticleIndexer() {
		// TODO Auto-generated constructor stub
	}

	
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
		
	public static Map<String, Integer> scanAndMap(Scanner in , String str ){		
		// This Method will Extract the URLs from Scanner and check with matchserver
		// And return a Hashmap with URLs & its corresponding Sequence Number
		
		int linkseqnum=0;
		Map<String,Integer> urlseq = new HashMap<String, Integer>();
		
		while(in.hasNextLine()){
			String page1 = in.useDelimiter(Pattern.compile("\\A")).nextLine(); 
			
// Giving the condition that it should exit the scanner when it encounters just "exit" on a Line
						if(page1.equals("exit")){break;}
			
			String[] page2=page1.split(" ", 2);
			String page=page2[1];
			
			// Stores in the URLs in the beginning of the line to the HashMap

			if (!urlseq.containsKey(page2[0])){
				urlseq.put(page2[0], linkseqnum++);
			}
			
			
			int flag = -1;

			// Extracting the URLs from the File			
			
			for (int i = 0; i < page.length(); i++) 
			{
				if (i + "<a href".length() <= page.length()) 
				{
					String tag = page.substring(i, i + "<a href".length());
					if (tag.equalsIgnoreCase("<a href") )
					{  //Checks for all possibility of Cases for <a href
						 flag = i;						
					}
				}
				
				if (i + ">".length() <= page.length()) 
				{
					String tag = page.substring(i,i+">".length());
					if (tag.equalsIgnoreCase(">")  && flag != -1)
					{ 
						String dummy=page.substring(flag + ("<a href=".length() + 1), i);
						int dindex=dummy.indexOf("\"");
						String dummy1 = dummy.substring(0, dindex);

						String printlink= MyURL.parseURL(dummy1);
						if(printlink!=null)
						{								
							if(matchServer(printlink,str)==true)
							{

								if (!urlseq.containsKey(printlink)){
									urlseq.put(printlink, linkseqnum++);
								}
								
								
							}
						}
						
												
						flag = -1;
					}
				}
			}
		}
		
		
		System.out.println("The total number of links are: " + linkseqnum + "\n");
		
		return urlseq;
		
	}
	
	
	public static void main(String[] args) throws FileNotFoundException {
		
		// Read the File Article2Content.txt
		// Assuming that the File to be read is stored in the WorkSpace
		
		File inFile = new File("article2content.txt");
		Scanner in = null;
		
		// If the File doesn't exist in the current directory,
		// And you want to give input file from the console[System Input],
		// Then Just copy Paste the entire file in the Console & Enter "exit" and press Enter
		// The Code will first check for File Input, if it doesn't exist it will use System Input
		
		try {
		    in = new Scanner(inFile);
		} catch (FileNotFoundException fe) {
		    System.out.println("File not found!");
		    System.out.println("Enter or Copy Paste Your DataSet over Here:");
		    System.out.println("Type 'exit' and press Enter once you have completed giving input. ");
		    in=new Scanner(System.in);		
		}

		String str = "guardian";
		
		// Calling the method scanAndMap which returns HashMap of URLs & Corresponding Sequence Number
		Map<String,Integer> urlseq = scanAndMap(in, str);
		in.close();
		
		for (String s : urlseq.keySet())
	          System.out.println(s + " " + urlseq.get(s));
		
		// Prints the URLs with its sequence number in a File 
		// The File is stored in Current Workspace in the Projects Folder
		PrintWriter out = new PrintWriter("urlwithseq.txt");
		
		for (String s : urlseq.keySet())
	          out.println(s + " " + urlseq.get(s));

		out.close();
		
		
		
	}
}
	
	