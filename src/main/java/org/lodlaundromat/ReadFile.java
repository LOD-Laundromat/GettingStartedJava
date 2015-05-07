package org.lodlaundromat;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.zip.GZIPInputStream;

import org.lodlaundromat.utils.Triple;

public class ReadFile {

    public static void readStream(InputStream inputStream) throws IOException {
        BufferedReader buffered = new BufferedReader(new InputStreamReader(new GZIPInputStream(inputStream), "UTF-8"));
        String tripleString;
        while ((tripleString = buffered.readLine()) != null) {
            Triple triple = new Triple(tripleString);

            // Do something!
            System.out.println("==================");
            System.out.println("subject: ");
            System.out.println("\ttoString: " + triple.sub.toString());
            System.out.println("\tvalue: " + triple.sub.value());
            System.out.println("\ttype: " + triple.sub.type());
            System.out.println("predicate:");
            System.out.println("\ttoString: " + triple.pred.toString());
            System.out.println("\tvalue: " + triple.pred.value());
            System.out.println("\ttype: " + triple.pred.type());
            System.out.println("object:");
            System.out.println("\ttoString: " + triple.obj.toString());
            System.out.println("\tvalue: " + triple.obj.value());
            System.out.println("\ttype: " + triple.obj.type());
            System.out.println("\tdata type: " + triple.obj.dataType());
            System.out.println("\tlang: " + triple.obj.lang());
        }
        buffered.close();

    }

    public static void main(String[] args) throws IOException {
//        readStream(new URL("http://download.lodlaundromat.org/5fcba614db053cbb6600d6b44d3d170c").openStream());
        if (args.length == 0) {
            System.out.println("Please pass the gzip data source (either URL or file) as argument");
            System.exit(1);
        }
        if (args[0].startsWith("http")) {
            readStream(new URL(args[0]).openStream());
        } else {
            readStream(new FileInputStream(args[0]));
        }

    }
}
