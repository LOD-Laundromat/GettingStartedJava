package org.lodlaundromat;
public class Triple {
    public Node sub;
    public Node pred;
    public Node obj;
    public Triple(String triple) {
        //at which points of this string do the predicate and object start?
        int startOfPred = triple.indexOf(' ')+1;
        int startOfObj = triple.indexOf(' ', startOfPred)+1;
        
        //take indexes above to return the nodes in this triple
        sub = new Node(triple.substring(0, startOfPred-1));
        pred = new Node(triple.substring(startOfPred, startOfObj-1));
        obj = new Node(triple.substring(startOfObj, triple.length()-2));//remove final ' .' as well
    }
}