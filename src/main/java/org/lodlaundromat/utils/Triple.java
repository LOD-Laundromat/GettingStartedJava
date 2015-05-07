package org.lodlaundromat.utils;


public class Triple {
    public Node sub;
    public Node pred;
    public Node obj;
    public String graph = null;
    
    /**
     * Does some naive but efficient parsing
     * @param triple
     */
    public Triple(String triple) {
        int offset = 1;//remove first <
        String subString = triple.substring(offset, triple.indexOf("> "));
        offset += subString.length()+3;//remove '> <'
        String predString = triple.substring(offset, triple.indexOf("> ", offset));
        offset += predString.length() + 2;//remove '> '
        
        
        
        int endIndex = triple.lastIndexOf(' '); //remove final ' .';
        boolean objIsUri = false;
        if (triple.charAt(offset) == '<') {
            //a uri
            objIsUri = true;
            endIndex--;//remove '>'
            offset++;//remove '<' as well
        }
        String objString = triple.substring(offset, endIndex);
        
        
        //there might be a graph specified in this statement
        if (objIsUri) {
            int separatorIndex = objString.indexOf(' ');
            if (separatorIndex > 0) {
                //ah, this line has graph specified
                graph = objString.substring(separatorIndex + 2);//remove ' <' of ng ('>' is already removed)
                objString = objString.substring(0, separatorIndex-1);//remove '>' of obj
            }
        } else {
            if (objString.charAt(objString.length() - 1) == '>' && objString.lastIndexOf(' ') > objString.lastIndexOf('^')) {
                //ah, this line has graph specified. Tricky condition, because we don't want to confuse datatyped literals: "literal"^^<datatype>
                int separatorIndex = objString.lastIndexOf(' ');
                graph = objString.substring(separatorIndex + 2, objString.length() - 1);
                objString = objString.substring(0, separatorIndex);
            }
        }
        
        
        //take indexes above to return the nodes in this triple
        sub = new Node(subString);
        pred = new Node(predString);
        obj = new Node(objString);
    }
}