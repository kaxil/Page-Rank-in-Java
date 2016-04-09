import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;

public class PageRank {

	public static Map<Integer,String> index_with_url = new HashMap<Integer, String>();
	public static Map<String,Integer> url_with_index = new HashMap<String, Integer>(); 
	public static int N ; 
	public static int[][] adjacency_matrix = null;
	public static int[] outDegree = null;
	public static double[][] A = null;
	public static double[] rank ; 
	public static Map<String,Double> url_with_pagerank = new HashMap<String,Double>();
	public static Map<Double,String> url_with_dummyrank = new HashMap<Double,String>();
	
	
//	public static Map<String,ArrayList> url_with_fileurls = new HashMap<String, ArrayList>();
	
	public PageRank() {
		// TODO Auto-generated constructor stub
	}

	
	private static boolean matchServer(String url, String str){
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
		String[] url2= url1[1].split("/",2);
		
			if(url2[0].toLowerCase().contains(str.toLowerCase())){
				return true;	
			}
			else
				return false;
		}
		else
			return false;
		
	}
	
	public static Map<String, Integer> urlMap(String page, String str ){
		
		int flag = -1;
		Map<String,Integer> urlfreqin = new HashMap<String, Integer>();
			
		//Extracting the URLs from the File			
		
		
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
					String dummy=page.substring(flag + ("<a href=".length()+1), i);
					int dindex=dummy.indexOf("\"");
					String dummy1 = dummy.substring(0, dindex);

					String prlink= MyURL.parseURL(dummy1);
					if(prlink!=null)
					{								
						if(matchServer(prlink,str)==true)
						{
							 
							 if (urlfreqin.containsKey(prlink))
							 { 
								 urlfreqin.put(prlink, urlfreqin.get(prlink) + 1);   
							 }
					         else                    
					         {
					        	 urlfreqin.put(prlink, 1);
					         }
							 	
						}
					}
					
											
					flag = -1;
				}
			}
		}
		
		
		return urlfreqin;
		
	}
	
	// Question 4(1)
	public static void indexurlmap(Scanner in ){

		// Using 2 HashMaps
		// 1st with Sequence Number as Key and URL as Value 
		// 2nd with URL as Key and Sequence Number as Value
		
		while(in.hasNextLine()){
			String page1 = in.useDelimiter(Pattern.compile("\\A")).nextLine(); // added while loop and changed to .nextLine() from .next()
			String[] page2=page1.split(" ", 2);
			
			int index=Integer.parseInt(page2[1]);
			String url = page2[0];
			
			if (!index_with_url.containsKey(index)){
				index_with_url.put(index, url);
			}
			
			if (!url_with_index.containsKey(url)){
				url_with_index.put(url, index);
			}
		}
		
	}
	
	public static void PrintMap(Map<String, Integer> urlseq){
		for(String s : urlseq.keySet())
		{
			System.out.println(s + " " + urlseq.get(s));
		}
	}
		
	public static void PrintMapIndex(Map<Integer, String> urlseq){
	for(Integer s : urlseq.keySet())
	{
		System.out.println(s + " " + urlseq.get(s));
	}
}

	public static void calculate_adj_matrix(Scanner in){
		
		while(in.hasNext())
		{
			String page1 = in.useDelimiter(Pattern.compile("\\A")).nextLine(); // added while loop and changed to .nextLine() from .next()
	
			// Splits the link into 2 Parts : 1st part the main URL of file
			// and the 2nd part the File Contents
			
			String[] page2=page1.split(" ", 2);
			String page=page2[1];
			
			// Get the Sequence Number of the main URL File
			int j = url_with_index.get(page2[0]);
			
			outDegree[j]=0;
			
			// Created a New HashMap the calculates the freq of each link in the file content
			Map<String,Integer> urlfreqin = new HashMap<String, Integer>();
			
			String str="guardian"; // Only want links with Guardian in the server path
			urlfreqin=urlMap(page,str);
			
			for (String s : urlfreqin.keySet())
			{
		         int z=url_with_index.get(s); // Gets the Seq Number of a particular URL
		         adjacency_matrix[j][z]=urlfreqin.get(s); // Stores the number of times a URL is present in a File 
		         outDegree[j]+=urlfreqin.get(s);   // Stores the total number of links in the File
			}
		}

		
	}
	
	public static void calculate_transition_prob_matrix(){
		// Creating Transition Probability Matrix
					A = new double[N][N];

					System.out.println("\n" + "The Transition Probability Matrix is:" + "\n");
					for (int i = 0; i < N; i++)
					{ 
						for (int j = 0; j < N; j++)
						{ // Storing probability for row i and column j in matrix 'A'.
							double aa = 0.90*adjacency_matrix[i][j]/outDegree[i] + 0.10/N;
							if(Double.isNaN(aa)){
								aa=0;
							}
							System.out.printf("%8.5f", aa);
							A[i][j]=aa;
						}
						System.out.println();
					}
	}
	
	public static void calculate_page_rank(){
		rank = new double[N];
		double[] oldrank = new double[N];
		rank[0] = 1.0;

		// The loop will stop
		// When the error rate between the PageRank of 1 article in
		// successful iterations is less than 0.1 % i.e less than 0.001 

		do
		{
			double[] newRank = new double[N]; 
			for(int j=0;j<N; j++)
			{ 					 
				for(int k=0;k<N; k++)
				{
					newRank[j] += rank[k] * A[k][j];
				}
			}
			for (int j = 0; j < N; j++)
			{
				oldrank[j]=rank[j];
				rank[j] = newRank[j];
			}

		} while((Math.abs(rank[1]-oldrank[1])/rank[1])>0.001);
		
		// Save the new PageRanks in a new HashMap

		double dummyrank=0.001; // Adding Dummy Rank to make rank as unique to store in HashMap
		for (String s : url_with_index.keySet())
		{
			url_with_pagerank.put(s, rank[url_with_index.get(s)]) ;
			url_with_dummyrank.put(rank[url_with_index.get(s)]+dummyrank, s);
			dummyrank=dummyrank+0.001;
		}
			
		
		System.out.println("The PageRanks are");
		for (int i = 0; i < N; i++)
		{
			System.out.printf("%8.5f", rank[i]);
		}
		
		System.out.println();
		System.out.println(url_with_pagerank);
	}
	
	public static void print_top_last_10_pr(){
		// Question 4(5)
		// Printing the PageRanks in Ascending Order
					
		System.out.println("\n");
		System.out.println("Sorted PageRanks are :");
		Object[] sorted_ranks_asc = url_with_dummyrank.keySet().toArray();
		Object[] sorted_ranks_desc = url_with_dummyrank.keySet().toArray();
		Arrays.sort(sorted_ranks_asc);
		Arrays.sort(sorted_ranks_desc, Collections.reverseOrder());
		for (int i = 0; i < sorted_ranks_asc.length; i++) {
			System.out.println(url_with_dummyrank.get(sorted_ranks_asc[i]) + " " + sorted_ranks_asc[i]);
		}

		// To Get 10 % Most Popular Links
		int top10 = (int) Math.round(0.1 * N);
		int last10 = top10;

		System.out.println("\n");
		System.out.println("Most popular 10%:");
		for (int i = 0; i < top10; i++) {
			double abc=(double) sorted_ranks_desc[i];
			if(abc<0.01){
				abc=0;
			}
			System.out.println(url_with_dummyrank.get(sorted_ranks_desc[i]) + " " + abc);
		}

		// To Get 10 % Least Popular Links
		System.out.println("\n");
		System.out.println("Least popular 10%:");
		for (int i = 0; i < last10; i++) {
			double abc=(double) sorted_ranks_desc[i];
			if(abc<0.01){
				abc=0;
			}
			System.out.println(url_with_dummyrank.get(sorted_ranks_asc[i]) + " " + abc);
		}
	}
	
	public static void main(String[] args) throws FileNotFoundException {
		
		File inFile= new File("urlwithseq.txt");		
		Scanner in1 = new Scanner(inFile);		
		
		indexurlmap(in1);
		in1.close();
		
		// Question 4(1)
		System.out.println("The SeqNumber with Links are:");
		PrintMapIndex(index_with_url);
		System.out.println();
		
		System.out.println("The Links with SeqNumber are:");
		PrintMap(url_with_index);
		System.out.println();

		N=url_with_index.size(); // Number of URLS

		// Question 4(2)
		adjacency_matrix = new int[N][N];
		outDegree = new int[N];
		
		// Path to file 
		inFile = new File("article2content.txt");
		Scanner in= new Scanner(inFile);
		calculate_adj_matrix(in); // Calculating Adjacency Matrix
		
		
			System.out.println("Links with each Seq. Num of URL:");
			for(int j=0;j<N;j++){
				System.out.println("j=" + j + " " + " Total Number of URLs: "+outDegree[j] + "  ");
			}
			
			System.out.println("\n" + "The Adjacency Matrix is" + "\n");
			
			for(int i=0;i<N;i++){
				for(int j=0;j<N;j++){
					System.out.print(adjacency_matrix[i][j] + "  ");
				}
				System.out.println("\n");
			}
			
		// Question 4(3)
		// Calculating The Transition Matrix
			
		calculate_transition_prob_matrix();
		
		// Question 4(4)
		// Calculating Page Rank of URLs
		calculate_page_rank();
		
		// Question 4(5)
		// Calculating & Printing Most Popular 10% & Least Popular 10% Link
		print_top_last_10_pr();
		
		in.close();
				
	}	
}