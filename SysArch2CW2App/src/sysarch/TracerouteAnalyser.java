package sysarch;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.RoundingMode;
import java.net.InetAddress;
import java.text.DecimalFormat;
import java.util.Locale;
import java.util.Scanner;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.WebServiceClient;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.model.InsightsResponse;

public class TracerouteAnalyser {
	
	private static File database;
	private static DatabaseReader databaseReader;
	private static WebServiceClient client;
	private static BufferedReader br;
	
	public static void main(String[] args) {
		client = new WebServiceClient.Builder(1337, "MaxMindPASS").build(); // Removed my username/pass from a free trial I signed up for for this assignment!
		database = new File("database.mmdb");
		
		try {
			databaseReader = new DatabaseReader.Builder(database).build();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		try {
			br = new BufferedReader(new FileReader("routes.txt"));
		} 
		catch (FileNotFoundException e) { 
			System.out.println("Couldn't load the traceroutes file (routes.txt)!");
		}
		scanRoutes(); // Called for section A 
		printData(); // Called for section B
	}
	
	public static void scanIP (IPAddress ip) {
		try {
			InetAddress ipAddress = InetAddress.getByName(ip.getIPNumber());
			//CityResponse response = databaseReader.city(ipAddress);
			InsightsResponse response = client.insights(ipAddress);
			ip.setLocation(response.getContinent().getName() + ", " + response.getCountry().getName() + ", " + response.getCity().getName());
			ip.setCoords(response.getLocation().getLatitude() + "/" + response.getLocation().getLongitude());
			ip.setNetwork(response.getTraits().getIsp());
			ip.setCompany(response.getTraits().getOrganization());
			ip.setPostcode(response.getPostal().getCode());
		}
 		catch (GeoIp2Exception e) {
 			System.out.println("    *** Couldn't find the IP " + ip.getIPNumber() + " ***");
 		}
		catch (Exception e) {}
	}
	
	public static void printData () {
		try {
			DecimalFormat dformat = new DecimalFormat("0.000");
			dformat.setRoundingMode(RoundingMode.HALF_UP);
		    String line;
		    int traceNumber = 0;
		    int hops = 0;
		    double totalMs = 0;
		    while ((line = br.readLine()) != null) {
		    	if (!(line.contains("traceroute to"))) {
		    		double ms = scanLine(line, false);
		    		if (ms > 0) {
		    			hops++;
		    			totalMs = ms;
		    		}
		    	}
		    	else {
		    		System.out.println(dformat.format(totalMs));
		    		System.out.println(hops); 
		    		traceNumber++;
		    		hops = 0;
		    		totalMs = 0;
		    	}
		    }
		    System.out.println(dformat.format(totalMs));
		    System.out.println(hops);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		try {
			br.close();
		} 
		catch (IOException e) { 
			System.exit(0);
		}
	}
	
	public static void scanRoutes () {
		try {
		    String line;
		    int traceNumber = 0;

		    System.out.println();
		    System.out.println("Traceroute 1:");
		    while ((line = br.readLine()) != null) {
		    	if (!(line.contains("traceroute to"))) {
		    		scanLine(line, true);
		    	}
		    	else {
		    		if (traceNumber > 0) {
		    			System.out.println("Traceroute " + (traceNumber + 1) + ":");
		    		}
		    		traceNumber++;
		    	}
		    }
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		try {
			br.close();
		} 
		catch (IOException e) { 
			System.exit(0);
		}
	}
	
	
	private static double scanLine(String line, boolean doPrint) {
		Scanner scan = new Scanner(line.trim().replaceAll(" +", " "));
		scan.useLocale(Locale.ENGLISH);
		double ms = 0;
		IPAddress ip = null;
		String address = "";
		String last = "";
		boolean ipFound = false;
		while (scan.hasNext()) {
			if (scan.hasNextDouble() && ipFound) {
				ms += scan.nextDouble();
	    	}
	    	else {
	    		String potentialIP = scan.next();
	    		if (!ipFound && potentialIP.charAt(0) == '(' && potentialIP.charAt(potentialIP.length() - 1) == ')') {
	    			ip = new IPAddress(potentialIP.substring(1, potentialIP.length() - 1), last);
	    			ipFound = true;
	    		}
	    		last = potentialIP;
	    	}
	    }
		if (ipFound && doPrint) {
			scanIP(ip);
			System.out.println("    " + ip.getDomainName() + " (" + ip.getIPNumber() + "):");
			System.out.println("        Location: " + ip.getLocation() + ", " + ip.getPostcode());
			System.out.println("        Latitude/Longitude: " + ip.getCoords());
			System.out.println("        Network Name: " + ip.getNetwork());
			System.out.println("        Organization Name: " + ip.getCompany());
		}
		return ms/3;
	}
}
