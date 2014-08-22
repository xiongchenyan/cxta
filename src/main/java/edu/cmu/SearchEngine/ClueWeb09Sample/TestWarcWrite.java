package edu.cmu.SearchEngine.ClueWeb09Sample;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import edu.cmu.lemurproject.WarcHTMLResponseRecord;
import edu.cmu.lemurproject.WarcRecord;

public class TestWarcWrite{
	 public String inputDocName;
	 public String outDocName;
	 public Double fSampleRate = 0.01;
	 public String sWhiteList;
	
	 
	
	 public static void main(String[] args) throws IOException {
		 if (args.length != 3){
			 System.out.println("3 para: in clueweb warc doc + output name + whitelist doc no name");
			 return;
		 }
		 
		 
		 TestWarcWrite Sampler = new TestWarcWrite();
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
		    
		    // iterate through our stream
		    WarcRecord thisWarcRecord;
		    //the random number generator
		    while ((thisWarcRecord=WarcRecord.readNextWarcRecord(inStream))!=null) {
		      // see if it's a response record
		      if (thisWarcRecord.getHeaderRecordType().equals("response")) {
	        	thisWarcRecord.write(outStream);
		      }
		    }
		    inStream.close();
		    outStream.close();
		 
		 return true;
	 }
	 
	
}
