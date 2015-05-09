package org.lodlaundromat.utils;

import org.apache.any23.extractor.ExtractionContext;
import org.apache.any23.writer.TripleHandler;
import org.apache.any23.writer.TripleHandlerException;
import org.openrdf.model.Resource;
import org.openrdf.model.URI;
import org.openrdf.model.Value;

public abstract class SimpleQuadHandler implements TripleHandler {
    public void startDocument(URI documentURI) throws TripleHandlerException {
        // ignore
    }

    public void openContext(ExtractionContext context) throws TripleHandlerException {
        // ignore
    }

    public void closeContext(ExtractionContext context) throws TripleHandlerException {
        // ignore
    }

    public void receiveTriple(Resource s, URI p, Value o, URI g, ExtractionContext context) throws TripleHandlerException {
        System.out.println(s.toString());
        System.out.println(p.toString());
        System.out.println(o.toString());
        System.out.println(g.toString());
    }

    public void receiveNamespace(String prefix, String uri, ExtractionContext context) throws TripleHandlerException {
        // ignore
    }

    public void close() throws TripleHandlerException {
        // ignore
    }

    public void endDocument(URI documentURI) throws TripleHandlerException {
        //ignore
    }

    public void setContentLength(long contentLength) {
        //ignore
    }
}
