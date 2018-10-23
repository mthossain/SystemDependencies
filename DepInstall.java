package mycode;

/*
 * code bundle ver 1.01
 * 
 * 11/5/17: fix to readSoln() to work with Windows files
 * 
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;


public class DepInstall {

	public final Integer MAXCOMS = 1000;
	

	public DepInstall() {

	}
	
	public void runNCommands (Vector<String> commands, Integer N) {
		// PRE: commands contains set of commands read in by readCommandsFromFile()
		// POST: executed min(N, all) commands
		
		Map <String, List<String>> c = new HashMap<String, List<String>>();
		ArrayList<String> installed = new ArrayList<String>();
		ArrayList<String> installedSepa = new ArrayList<String>();
		ArrayList<String> allList = new ArrayList<String>();
		
		for (int i=0; i<N; i++){
			if(i==commands.size())
				break;
			
			String[] splitStr = commands.get(i).split("\\s+");
			
			if (splitStr[0].equals("DEPEND")){
				c.put(splitStr[1], new ArrayList<String>());
				
				System.out.println(commands.get(i));
				for (int j=2;j<splitStr.length; j++){
					if (c.containsKey(splitStr[1])){
						c.get(splitStr[1]).add(splitStr[j]);
						allList.add(splitStr[j]);
					}
				}
			
			}
			
			else if (splitStr[0].equals("INSTALL")){
				System.out.println(commands.get(i));
				boolean alreadyInstalled = false;
				if (installed.toString().contains(splitStr[1])){
					System.out.println("   "+splitStr[1]+" is already installed");
					alreadyInstalled = true;
				}
					
				if (!alreadyInstalled){
					installedSepa.add(splitStr[1]);
					for (int j=checkCycleString(splitStr[1],splitStr[1],c).size()-1; j>=0; j--){
						
						if (!installed.contains(checkCycleString(splitStr[1],splitStr[1],c).get(j))){
							System.out.println("   Installing "+checkCycleString(splitStr[1],splitStr[1],c).get(j));
							installed.add((String) checkCycleString(splitStr[1],splitStr[1],c).get(j));
						}
						
						
					}
					
				}

			}
			
			else if (splitStr[0].equals("REMOVE")){
				System.out.println(commands.get(i));
				if (!installed.contains(splitStr[1])){
					System.out.println("   "+splitStr[1]+" is not installed");
					continue;
				}
				if (c.values().toString().contains(splitStr[1]))
					System.out.println("   "+splitStr[1]+" is still needed");
				else{

					installedSepa.remove(splitStr[1]);
					for (int j=0; j<checkCycleString(splitStr[1],splitStr[1],c).size(); j++){
						
						if ( (!installedSepa.contains(checkCycleString(splitStr[1],splitStr[1],c).get(j)))){
							if((timesChecker(allList,(String) checkCycleString(splitStr[1],splitStr[1],c).get(j))==0)){
								
								System.out.println("   Removing "+checkCycleString(splitStr[1],splitStr[1],c).get(j));
								installed.remove(checkCycleString(splitStr[1],splitStr[1],c).get(j));
								if (c.containsKey(checkCycleString(splitStr[1],splitStr[1],c).get(j))){
									for (int w=0; w<c.get(checkCycleString(splitStr[1],splitStr[1],c).get(j)).size(); w++){
										allList.remove(c.get(checkCycleString(splitStr[1],splitStr[1],c).get(j)).get(w));
									}
										
								}

								
							}
						
							
						}
						
					}
							
				}

				
			}
			
			else if (splitStr[0].equals("LIST")){
				System.out.println(commands.get(i));
				for (int j=0; j<installed.size();j++)
					System.out.println("   "+installed.get(j));
			}
			
			else if (splitStr[0].equals("END")){
				System.out.println(commands.get(i));
				break;
			}		
		}
		

	}
	
	public int timesChecker (ArrayList<String> x, String str){
		int count=0;
		for (int i=0; i<x.size(); i++){
			if (x.get(i).equals(str))
				count++;
		}
		return count;
	}
	
	public ArrayList checkCycleString(String to, String from, Map <String, List<String>> a){
		ArrayList<String> bal = new ArrayList<String>();
		ArrayList<String> listOfAll = new ArrayList<String>();
		return checkCycleStringDoNotCall(from, bal, to, from, a, listOfAll);
	}
	
	public ArrayList checkCycleStringDoNotCall(String iniFrom, ArrayList<String> x, String to, String from, Map <String, List<String>> a, ArrayList n){
		if (!a.containsKey(from)){
			n.add(from);
			return n;
		}
			
		if (a.get(from).contains(to)){
			if (!n.contains(from))
				n.add(from);
			if (!n.contains(to))
				n.add(to);
			return n;
		}
			
		for (int i=a.get(from).size()-1; i>=0;i--){
			int q = a.get(from).size();
			if (!n.contains(from) && a.get(from).size()!=0)
				n.add(from);
			if (n.contains(a.get(from).get(i)))
				n.remove(a.get(from).get(i));
			n.add(a.get(from).get(i));
			if(a.containsKey(a.get(from).get(i))==false){
				//does the member have a key group? If NO-> the element does not work depend then it ends here
				continue;
			}
				
			else {
				
				x.add(a.get(from).get(i));
				if (!n.contains(from))
					n.add(from);
				checkCycleStringDoNotCall(iniFrom, x, to,a.get(from).get(i),a, n);
			}
				
		}
		if (!n.contains(from))
			n.add(from);
		return n;
	}
	
	
	
	public boolean checkCycle(String s, String k, Map <String, List<String>> a){
		ArrayList<String> aal = new ArrayList<String>();
		return checkCycleDoNotCall(k, aal, s, k, a);
	}
	
	public boolean checkCycleDoNotCall(String iniFrom, ArrayList<String> x, String s, String k, Map <String, List<String>> a){
		
		if (a.get(k).contains(s))
			return true;
		
		for (int i=0; i<a.get(k).size();i++){
			if(a.containsKey(a.get(k).get(i))==false)
				continue;
			else if(a.get(k).get(i)==iniFrom || !x.contains(a.get(k).get(i))){
				x.add(a.get(k).get(i));
				return checkCycleDoNotCall(iniFrom, x, s,a.get(k).get(i),a);
			}
				
		}
		return false;
	
	}
	
	
	public int checkCycleNo(String s, String k, Map <String, List<String>> a, int n){
		ArrayList<String> bal = new ArrayList<String>();
		return checkCycleNoDoNotCall(k, bal, s, k, a, n);
	}
	
	public int checkCycleNoDoNotCall(String iniFrom, ArrayList<String> x, String s, String k, Map <String, List<String>> a, int n){
		if (a.get(k).contains(s)){
			n++;
			return n;
		}
			
		for (int i=0; i<a.get(k).size();i++){
			if(a.containsKey(a.get(k).get(i))==false)
				continue;
			else if(a.get(k).get(i)==iniFrom || !x.contains(a.get(k).get(i))){
				x.add(a.get(k).get(i));
				return checkCycleNoDoNotCall(iniFrom, x, s,a.get(k).get(i),a, n=n+1);
			}
				
		}
		return n;
	}
	
	public void runNCommandswCheck (Vector<String> commands, Integer N) {
		// PRE: commands contains set of commands read in by readCommandsFromFile()
		// POST: executed min(N, all) commands, checking for cycles


		boolean circularDepen = false;
		Map <String, List<String>> c = new HashMap<String, List<String>>();
		
		ArrayList<String> installed = new ArrayList<String>();
		ArrayList<String> keys = new ArrayList<String>();
		boolean isCycleDetected = false;
		
		for (int i=0; i<N; i++){
			if(i==commands.size())
				break;
			String[] splitStr = commands.get(i).split("\\s+");
			
			if (splitStr[0].equals("DEPEND")){
				c.put(splitStr[1], new ArrayList<String>());
				keys.add(splitStr[1]);
				System.out.println(commands.get(i));
				for (int j=2;j<splitStr.length; j++){
				
					if (c.containsKey(splitStr[1])){
						c.get(splitStr[1]).add(splitStr[j]);

						if(isCycleDetected || checkCycle(splitStr[1],splitStr[1],c)){
							isCycleDetected = true;
							circularDepen=true;	
						}
							
					}
				}
				
				if(isCycleDetected || checkCycle(splitStr[1],splitStr[1],c)){
					System.out.println("   Found cycle in dependencies");
				}
				
			}
			
			else if (splitStr[0].equals("INSTALL")){
				System.out.println(commands.get(i));
				boolean alreadyInstalled = false;
				if (circularDepen)
					continue;
				if (installed.toString().contains(splitStr[1])){
					System.out.println("   "+splitStr[1]+" is already installed");
					alreadyInstalled = true;
				}
					
				if (c.containsKey(splitStr[1]) && !alreadyInstalled){
					for (int j=c.get(splitStr[1]).size()-1; j>=0; j--){
						if (installed.indexOf(c.get(splitStr[1]).get(j))==-1){
							System.out.println("   Installing "+c.get(splitStr[1]).get(j));
							installed.add(c.get(splitStr[1]).get(j));
						}
					}
					System.out.println("   Installing "+splitStr[1]);
					installed.add(splitStr[1]);
				}
				else if (!alreadyInstalled){
					System.out.println("   Installing "+splitStr[1]);
					installed.add(splitStr[1]);
				}
			}
			
			else if (splitStr[0].equals("REMOVE")){
				System.out.println(commands.get(i));
				if (circularDepen)
					continue;
				
				if (!installed.contains(splitStr[1])){
					System.out.println("   "+splitStr[1]+" is not installed");
					continue;
				}
				
				if (c.values().toString().contains(splitStr[1]))
					System.out.println("   "+splitStr[1]+" is still needed");
				else{
					System.out.println("   Removing "+splitStr[1]);
					
					ArrayList<String> names = new ArrayList<String>();
					int count=0;
					for (int j=0; j<c.get(splitStr[1]).size(); j++){
						names.add(c.get(splitStr[1]).get(j));
						count++;
					}
					c.remove(splitStr[1]);
					installed.remove(splitStr[1]);
					
					for (int j=count-1; j>=0; j--)
						if(!c.values().toString().contains(names.get(j))){
							System.out.println("   Removing "+names.get(j));
							installed.remove(names.get(j));
						}		
				}
				
			}
			
			else if (splitStr[0].equals("LIST")){
				System.out.println(commands.get(i));
				if (circularDepen)
					continue;
				for (int j=0; j<installed.size();j++)
					System.out.println("   "+installed.get(j));
			}
			
			else if (splitStr[0].equals("END")){
				System.out.println(commands.get(i));
				break;
			}
		
		}
	}
	
	public void runNCommandswCheckRecLarge (Vector<String> commands, Integer N) {
		// PRE: commands contains set of commands read in by readCommandsFromFile()
		// POST: executed min(N, all) commands, checking for cycles and 
		//       recommending fix by removing largest cycle

		
		boolean circularDepen = false;
		Map <String, List<String>> c = new HashMap<String, List<String>>();
		
		ArrayList<String> installed = new ArrayList<String>();
		ArrayList<String> keys = new ArrayList<String>();
		
		Map <Integer, String> cycle = new HashMap<Integer, String>();
		ArrayList<Integer> cycle_keys = new ArrayList<Integer>();
		boolean isCycleDetected = false;
		
		for (int i=0; i<N; i++){
			if(i==commands.size())
				break;
			String[] splitStr = commands.get(i).split("\\s+");
			
			if (splitStr[0].equals("DEPEND")){
				c.put(splitStr[1], new ArrayList<String>());
				keys.add(splitStr[1]);
				System.out.println(commands.get(i));
				for (int j=2;j<splitStr.length; j++){
					if (c.containsKey(splitStr[1])){
						c.get(splitStr[1]).add(splitStr[j]);
						
						if(isCycleDetected || checkCycle(splitStr[1],splitStr[1],c)){
							isCycleDetected = true;
							cycle.put(checkCycleNo(splitStr[1],splitStr[1],c,0), commands.get(i));
							cycle_keys.add(checkCycleNo(splitStr[1],splitStr[1],c,0));
							c.remove(splitStr[1]);
							circularDepen=true;
							int large=0;
							for (int m=0; m<cycle_keys.size(); m++){
								if(cycle_keys.get(m)>large)
									large=cycle_keys.get(m);
							}
							
							System.out.println("   Found cycle in dependencies");
							if (cycle.containsKey(large)){
								System.out.println("   Suggest removing "+cycle.get(large));
							}
							
						}
							
					}
				}
				
			}
			
			else if (splitStr[0].equals("INSTALL")){
				System.out.println(commands.get(i));
				boolean alreadyInstalled = false;
				if (circularDepen)
					continue;
				if (installed.toString().contains(splitStr[1])){
					System.out.println("   "+splitStr[1]+" is already installed");
					alreadyInstalled = true;
				}
					
				if (c.containsKey(splitStr[1]) && !alreadyInstalled){
					for (int j=c.get(splitStr[1]).size()-1; j>=0; j--){
						if (installed.indexOf(c.get(splitStr[1]).get(j))==-1){
							System.out.println("   Installing "+c.get(splitStr[1]).get(j));
							installed.add(c.get(splitStr[1]).get(j));
						}
					}
					System.out.println("   Installing "+splitStr[1]);
					installed.add(splitStr[1]);
				}
				else if (!alreadyInstalled){
					System.out.println("   Installing "+splitStr[1]);
					installed.add(splitStr[1]);
				}
			}
			
			else if (splitStr[0].equals("REMOVE")){
				System.out.println(commands.get(i));
				if (circularDepen)
					continue;
				
				if (!installed.contains(splitStr[1])){
					System.out.println("   "+splitStr[1]+" is not installed");
					continue;
				}
				
				if (c.values().toString().contains(splitStr[1]))
					System.out.println("   "+splitStr[1]+" is still needed");
				else{
					System.out.println("   Removing "+splitStr[1]);
					
					ArrayList<String> names = new ArrayList<String>();
					int count=0;
					for (int j=0; j<c.get(splitStr[1]).size(); j++){
						names.add(c.get(splitStr[1]).get(j));
						count++;
					}
					c.remove(splitStr[1]);
					installed.remove(splitStr[1]);
					
					for (int j=count-1; j>=0; j--)
						if(!c.values().toString().contains(names.get(j))){
							System.out.println("   Removing "+names.get(j));
							installed.remove(names.get(j));
						}	
				}
				
			}
			
			else if (splitStr[0].equals("LIST")){
				System.out.println(commands.get(i));
				if (circularDepen)
					continue;
				for (int j=0; j<installed.size();j++)
					System.out.println("   "+installed.get(j));
			}
			
			else if (splitStr[0].equals("END")){
				System.out.println(commands.get(i));
				break;
			}		
		}	
	}

	public void runNCommandswCheckRecSmall (Vector<String> commands, Integer N) {
		// PRE: commands contains set of commands read in by readCommandsFromFile()
		// POST: executed min(N, all) commands, checking for cycles and 
		//       recommending fix by removing smallest cycle


		
		boolean circularDepen = false;
		Map <String, List<String>> c = new HashMap<String, List<String>>();
		
		ArrayList<String> installed = new ArrayList<String>();
		ArrayList<String> keys = new ArrayList<String>();
		
		Map <Integer, String> cycle = new HashMap<Integer, String>();
		ArrayList<Integer> cycle_keys = new ArrayList<Integer>();
		boolean isCycleDetected = false;
		
		for (int i=0; i<N; i++){
			if(i==commands.size())
				break;
			String[] splitStr = commands.get(i).split("\\s+");
			int cycleCount=0;
			if (splitStr[0].equals("DEPEND")){
				c.put(splitStr[1], new ArrayList<String>());
				keys.add(splitStr[1]);
				System.out.println(commands.get(i));
				for (int j=2;j<splitStr.length; j++){
				
					if (c.containsKey(splitStr[1])){
						c.get(splitStr[1]).add(splitStr[j]);
						
						if(isCycleDetected || checkCycle(splitStr[1],splitStr[1],c)){
							isCycleDetected = true;
							cycleCount+=checkCycleNo(splitStr[1],splitStr[1],c,0);
							circularDepen=true;
						}		
					}
				}
				
				if(isCycleDetected || checkCycle(splitStr[1],splitStr[1],c)){
					
					cycle.put(cycleCount, commands.get(i));
					cycle_keys.add(cycleCount);
					
					int small=100;
					for (int m=0; m<cycle_keys.size(); m++){
						if(cycle_keys.get(m)<=small)
							small=cycle_keys.get(m);
					}
					
					System.out.println("   Found cycle in dependencies");

					if (cycle.containsKey(small)){
						System.out.println("   Suggest removing "+cycle.get(small));
					}
				}
				
				
			}
			
			else if (splitStr[0].equals("INSTALL")){
				System.out.println(commands.get(i));
				boolean alreadyInstalled = false;
				if (circularDepen)
					continue;
				if (installed.toString().contains(splitStr[1])){
					System.out.println("   "+splitStr[1]+" is already installed");
					alreadyInstalled = true;
				}
					
				if (c.containsKey(splitStr[1]) && !alreadyInstalled){
					for (int j=c.get(splitStr[1]).size()-1; j>=0; j--){
						if (installed.indexOf(c.get(splitStr[1]).get(j))==-1){
							System.out.println("   Installing "+c.get(splitStr[1]).get(j));
							installed.add(c.get(splitStr[1]).get(j));
						}
					}
					System.out.println("   Installing "+splitStr[1]);
					installed.add(splitStr[1]);
				}
				else if (!alreadyInstalled){
					System.out.println("   Installing "+splitStr[1]);
					installed.add(splitStr[1]);
				}
			}
			
			else if (splitStr[0].equals("REMOVE")){
				System.out.println(commands.get(i));
				if (circularDepen)
					continue;
				
				if (!installed.contains(splitStr[1])){
					System.out.println("   "+splitStr[1]+" is not installed");
					continue;
				}
				
				if (c.values().toString().contains(splitStr[1]))
					System.out.println("   "+splitStr[1]+" is still needed");
				else{
					System.out.println("   Removing "+splitStr[1]);
					
					ArrayList<String> names = new ArrayList<String>();
					int count=0;
					for (int j=0; j<c.get(splitStr[1]).size(); j++){
						names.add(c.get(splitStr[1]).get(j));
						count++;
					}
					c.remove(splitStr[1]);
					installed.remove(splitStr[1]);
					
					for (int j=count-1; j>=0; j--)
						if(!c.values().toString().contains(names.get(j))){
							System.out.println("   Removing "+names.get(j));
							installed.remove(names.get(j));
						}		
				}
				
			}
			
			else if (splitStr[0].equals("LIST")){
				System.out.println(commands.get(i));
				if (circularDepen)
					continue;
				for (int j=0; j<installed.size();j++)
					System.out.println("   "+installed.get(j));
			}
			
			else if (splitStr[0].equals("END")){
				System.out.println(commands.get(i));
				break;
			}	
				
		}

	}
	

	public Vector<String> readCommandsFromFile(String fInName) throws IOException {
		// PRE: -
		// POST: returns lines from input file as vector of string
		BufferedReader fIn = new BufferedReader(
							 new FileReader(fInName));
		String s;
		Vector<String> comList = new Vector<String>();
		
		while ((s = fIn.readLine()) != null) {
			comList.add(s);
		}
		fIn.close();
		
		return comList;
	}
	
	
	public String readSoln(String fInName, Integer N) throws IOException {
		// PRE: -
		// POST: returns N lines from input file as single string
		BufferedReader fIn = new BufferedReader(
							 new FileReader(fInName));
		String s;
		String out = "";
		Integer i = 0;

		while (((s = fIn.readLine()) != null) && (i <= N)) {
			if ((i != N) || s.startsWith("   ")) // responses to commands start with three spaces
				out += s + System.lineSeparator();
			if (!s.startsWith("   "))  
				i += 1;
		}
		fIn.close();
		
		return out;
	}


	public static void main(String[] args) {
		
		DepInstall d = new DepInstall();
		Vector<String> inCommands = null;
		//String PATH = "/home/madras/teaching/17comp225/ass/data/";
		String PATH = "/Users/Killx0ne/Downloads/sample-data/"; // IMPORTANT: change to your own path

		
		Integer N = d.MAXCOMS;
		
		try {
			inCommands = d.readCommandsFromFile(PATH+"sample_P1.in");
		}
		catch (IOException e) {
			System.out.println("in exception: " + e);
		}
		d.runNCommands(inCommands, 20);
		
		System.out.println();
		System.out.println(inCommands);
	}
}
