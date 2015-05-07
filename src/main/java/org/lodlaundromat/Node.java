package org.lodlaundromat;
public class Node {
        private final String stringRepresentation;
        public Node(String node) {
            this.stringRepresentation = node;
        }
        public NodeType type() {
            if (stringRepresentation.charAt(0) == '"') return NodeType.LITERAL;
            if (stringRepresentation.startsWith("<http://lodlaundromat.org/.well-known")) return NodeType.BNODE;
            return NodeType.URI;
        }
        public String value() {
            if (stringRepresentation.charAt(0) == '<') return stringRepresentation.substring(1, stringRepresentation.length()-1);
//          
            //it is a literal
            return stringRepresentation.substring(1, stringRepresentation.lastIndexOf("\""));
        }
        
        public String dataType() {
            if (stringRepresentation.charAt(0) == '"') {
                int typeIndex = stringRepresentation.lastIndexOf("^", stringRepresentation.length() - stringRepresentation.lastIndexOf("\""));
                if (typeIndex > 0) return stringRepresentation.substring(typeIndex + 2, stringRepresentation.length()-1);//this removes the < and > from datatype as well
            }
            return null;
        }
        public String lang() {
            if (stringRepresentation.charAt(0) == '"') {
                int langIndex = stringRepresentation.lastIndexOf("@", stringRepresentation.length() - stringRepresentation.lastIndexOf("\""));
                if (langIndex > 0) return stringRepresentation.substring(langIndex + 1);
            }
            return null;
        }
        public String toString() {
            return stringRepresentation;
        }
    }