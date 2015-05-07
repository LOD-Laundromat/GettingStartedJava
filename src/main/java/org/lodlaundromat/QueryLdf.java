package org.lodlaundromat;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URLEncoder;

import org.apache.any23.Any23;
import org.apache.any23.extractor.ExtractionException;
import org.apache.any23.http.HTTPClient;
import org.apache.any23.source.DocumentSource;
import org.apache.any23.source.HTTPDocumentSource;
import org.apache.any23.writer.NTriplesWriter;
import org.apache.any23.writer.TripleHandler;
import org.apache.any23.writer.TripleHandlerException;
import org.apache.commons.io.output.ByteArrayOutputStream;

public class QueryLdf {

    public static void queryLdf(String md5, String sub, String pred, String obj) throws IOException, URISyntaxException, ExtractionException, TripleHandlerException {
       String ldfUrl = "http://ldf.lodlaundromat.org/" + md5 + "?";
       if (sub != null) ldfUrl += "subject=" + URLEncoder.encode(sub, "UTF-8") + "&";
       if (pred != null) ldfUrl += "subject=" + URLEncoder.encode(pred, "UTF-8") + "&";
       if (obj != null) ldfUrl += "subject=" + URLEncoder.encode(obj, "UTF-8") + "&";
       System.out.println("Querying " + ldfUrl);
       
       Any23 runner = new Any23();
       runner.setHTTPUserAgent("test-user-agent");
       HTTPClient httpClient = runner.getHTTPClient();
       DocumentSource source = new HTTPDocumentSource(
               httpClient,
               ldfUrl
            );
       ByteArrayOutputStream out = new ByteArrayOutputStream();
       TripleHandler handler = new NTriplesWriter(out);
       try {
           runner.extract(source, handler);
       } finally {
           handler.close();
       }
       String n3 = out.toString("UTF-8");
       System.out.println(n3);
    }

    public static void main(String[] args) throws IOException, URISyntaxException, ExtractionException, TripleHandlerException {
        String sub = null;
        String pred = null;
        String obj = null;
        String md5 = null;
        if (args.length == 0) {
            System.out.println("Please pass an md5 as argument. Use --subject <sub>, --predicate <pred> or --object <obj> to filter for those");
            System.exit(1);
        }
        boolean inSubOption = false;
        boolean inPredOption = false;
        boolean inObjOption = false;
        for (int i = 0; i < args.length; i++) {
            if (args[i].startsWith("--")) {
                if (args[i].equals("--subject")) inSubOption = true;
                if (args[i].equals("--predicate")) inPredOption = true;
                if (args[i].equals("--object")) inObjOption = true;
                continue;
            }
            if (inSubOption == true) {
                sub = args[i];
                inSubOption = false;
                continue;
            }
            if (inPredOption == true) {
                pred = args[i];
                inPredOption = false;
                continue;
            }
            if (inObjOption == true) {
                obj = args[i];
                inObjOption = false;
                continue;
            }
            //must be an md5
            md5 = args[i];
            
        }
        
        if (md5 == null) {
            System.out.println("Please pass an md5 as argument. Use --subject <sub>, --predicate <pred> or --object <obj> to filter for those");
            System.exit(1);
        } else if (md5.length() != 32) {
            System.out.println("Incorrect document identifier. Expected an md5 (of 32 characters)");
            System.exit(1);
        }
        queryLdf(md5, sub, pred, obj);

    }
}
