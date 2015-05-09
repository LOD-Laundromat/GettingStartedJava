package org.lodlaundromat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import org.apache.any23.Any23;
import org.apache.any23.extractor.ExtractionContext;
import org.apache.any23.extractor.ExtractionException;
import org.apache.any23.source.DocumentSource;
import org.apache.any23.source.StringDocumentSource;
import org.apache.any23.writer.TripleHandlerException;
import org.lodlaundromat.utils.SimpleQuadHandler;
import org.openrdf.model.Resource;
import org.openrdf.model.URI;
import org.openrdf.model.Value;

public class QueryLdf {
    private class QuadHandler extends SimpleQuadHandler {
        public boolean nextPage = false;
        public void receiveTriple(Resource subject, URI predicate, Value object, URI graph, ExtractionContext context) throws TripleHandlerException {
            
            if (graph != null) {
                //this is meta-data about the response
                if (predicate.toString().equals("http://www.w3.org/ns/hydra/core#nextPage")) {
                    nextPage = true;
                }
            } else {
                //this is the actual data we get back
                System.out.println("Subject: " + subject.toString());
                System.out.println("Predicate: " + predicate.toString());
                System.out.println("Object: " + object.toString());
            }
        }
    }
    
    public void execLdfRequest(String md5, String sub, String pred, String obj) throws IOException, ExtractionException, TripleHandlerException {
        parseString(md5, sub, pred, obj, 1);
    }
    public void parseString(String md5, String sub, String pred, String obj, int page) throws IOException, ExtractionException, TripleHandlerException {
        String ldfUrl = "http://ldf.lodlaundromat.org/" + md5 + "?page=" + page;
        if (sub != null) ldfUrl += "subject=" + URLEncoder.encode(sub, "UTF-8") + "&";
        if (pred != null) ldfUrl += "subject=" + URLEncoder.encode(pred, "UTF-8") + "&";
        if (obj != null) ldfUrl += "subject=" + URLEncoder.encode(obj, "UTF-8") + "&";
        System.out.println("Querying " + ldfUrl);
        
        //do request
        Any23 runner = new Any23();
        URL url = new URL(ldfUrl);
        URLConnection connection = url.openConnection();
        connection.setRequestProperty("Accept", "application/n-quads");
        //fetch response
        String inputLine;
        BufferedReader in = new BufferedReader(new InputStreamReader(
                connection.getInputStream()));
        String totalString = "";
        while ((inputLine = in.readLine()) != null) 
            totalString += inputLine +"\n";
        in.close();
        //parse response
        DocumentSource source = new StringDocumentSource(totalString, "http://host.com/service");
        QuadHandler handler = new QuadHandler();
        try {
            runner.extract(source, handler);
        } finally {
            handler.close();
        }
        //if there is a next page, fetch that one
        if (handler.nextPage) {
            parseString(md5, sub, pred, obj, page+1);
        }
        
     }
    public static void main(String[] args) throws IOException, ExtractionException, TripleHandlerException {
        /**
         * parse arguments
         */
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
        QueryLdf query = new QueryLdf();
        query.execLdfRequest(md5, sub, pred, obj);

    }
}
