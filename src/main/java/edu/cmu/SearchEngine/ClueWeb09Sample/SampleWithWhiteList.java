package edu.cmu.SearchEngine.ClueWeb09Sample;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import edu.cmu.lemurproject.WarcHTMLResponseRecord;
import edu.cmu.lemurproject.WarcRecord;

public class SampleWithWhiteList {
	 public String inputDocName;
	 public String outDocName;
	 public Double fSampleRate = 0.01;
	 public String sWhiteList;
	
	 
	
	 public static void main(String[] args) throws IOException {
		 if (args.length != 3){
			 System.out.println("3 para: in clueweb warc doc + output name + whitelist doc no name");
			 return;
		 }
		 
		 
		 SampleWithWhiteList Sampler = new SampleWithWhiteList();
		 
		 Sampler.inputDocName = args[0];
		 Sampler.outDocName = args[1];
		 Sampler.sWhiteList = args[2];
		 
		 Sampler.ProcessOneFile(Sampler.inputDocName,Sampler.outDocName);
		 
		 System.out.println("finished");
		 return;
		 
		 
		  }
	 
	 

	 
	 public Boolean ProcessOneFile(String sInputWarcFile,String sOutName) throws IOException{
		// open our gzip input stream
		    GZIPInputStream gzInputStream=new GZIPInputStream(new FileInputStream(sInputWarcFile));
		    
		    // cast to a data input stream
		    DataInputStream inStream=new DataInputStream(gzInputStream);
		    
		    
		    //open out gzip stream
		    GZIPOutputStream gzOutStream = new GZIPOutputStream(new FileOutputStream(sOutName));
		    //cast to a data output stream
		    DataOutputStream outStream = new DataOutputStream(gzOutStream);
		    
		    
		    SampleWithWhiteList Sampler = new SampleWithWhiteList();
		    
		    Map<String, Boolean> hDocNo = Sampler.ReadWhiteListDocNo(sWhiteList);
		    System.out.format("read white list complete total [%d]\n", hDocNo.size());
		    
		    // iterate through our stream
		    WarcRecord thisWarcRecord;
		    int OutCnt = 0;
		    int TotalCnt = 0;
		    //the random number generator
		    Random randomGenerator = new Random();
		    while ((thisWarcRecord=WarcRecord.readNextWarcRecord(inStream))!=null) {
		      // see if it's a response record
		      if (thisWarcRecord.getHeaderRecordType().equals("response")) {
		    	  TotalCnt += 1;
		        // it is - create a WarcHTML record
		        WarcHTMLResponseRecord htmlRecord=new WarcHTMLResponseRecord(thisWarcRecord);
		        // get our TREC ID and target URI
		        String thisTRECID=htmlRecord.getTargetTrecID();
//		        String thisTargetURI=htmlRecord.getTargetURI();
		        // print our data
//		        System.out.println(thisTRECID + " : " + thisTargetURI);
		        int iRandom = randomGenerator.nextInt(100000);
		        if (hDocNo.containsKey(thisTRECID)){
		        	System.out.println(thisTRECID + " in list");
		        	thisWarcRecord.write(outStream);
		        	OutCnt += 1;
		        	continue;
		        }
		        if ((iRandom / 100000.0 < fSampleRate)){
		        	System.out.println(thisTRECID + " passed 0.02 sample rate");
		        	thisWarcRecord.write(outStream);
		        	OutCnt += 1;
		        	continue;
		        }
		      }
		    }
		    System.out.format("file [%s] finished total [%d]/[%d] doc kept\n", sInputWarcFile,
		    		OutCnt,
		    		TotalCnt);
		    inStream.close();
		    outStream.close();
		 
		 return true;
	 }
	 
	 
	 public Map<String,Boolean> ReadWhiteListDocNo(String sWhiteListName) throws IOException{
		 Map<String,Boolean> hDocNo = new HashMap<String,Boolean>();
		 
		 BufferedReader brReader = new BufferedReader(new FileReader(sWhiteListName));
		 String line;
		 while ((line = brReader.readLine()) != null) {
//			 System.out.println("read: [" + line + "]");
//			 line = line.replace("\n", "");
			 hDocNo.put(line,true);
		 }
		 brReader.close();
		 return hDocNo;
		 
	 }
}
