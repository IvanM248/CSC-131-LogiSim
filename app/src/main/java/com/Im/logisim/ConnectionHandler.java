package com.Im.logisim;

import java.util.List;
/*
 * A static utility class which handles connecting certain nodes to each other and
 * resetting node connections. Valid node connections are: not gates to not gates,
 * not gates to and/or gates, and gates to and/or gates, and gates to not gates,
 * or gates to and/or gates, or gates to not gates, switches to and/ or gates,
 * switches to not gates, and switches to light bulbs. If a connection can be made,
 * then the method that is called will return true. If a connection cannot be made,
 * then the method that was called will return false. A connection will not be able
 * to be made if the node in which a connection will be made with already contains
 * full connections.
 */
public final class ConnectionHandler {

    /*
    * As this class is a utility class, the default constructor is made private inordet to avoid
    * attempts to instantiate this class.
    */
    private ConnectionHandler(){}

    /*
    * When a connection is to be established between two nodes, this method will determine they
    * types of both nodes and call the appropriate method to attempt to make the connection.
    */
    public static boolean nodeToNode(Node a, Node b){
        if(b instanceof AndGate){
            return nodeToAndGate(a, (AndGate)b);
        }
        else if(b instanceof OrGate){
            return nodeToOrGate(a, (OrGate)b);
        }
        else if(b instanceof NotGate){
            return nodeToNotGate(a, (NotGate)b);
        }
        return false;
    }

    /*
    * Handles connecting a switch or logic gate to a light bulb. The variable a can represent a
    * switch object or a gate object. The variable b represents a light bulb object. The array list
    * containing all Output light objects must be passed in inorder to check that the node being
    * passed in is not already connected to a different output light object.
    */
    public static boolean nodeToLightBulb(Node a, Light b, List<Light> lights){
        for(Light light : lights){
            if(light.getSource() == a){
                return false;
            }
        }
        if(b.getSource() == null){
            b.setSource(a);
            return true;
        }
        return false;
    }

    /*
    * This method will search through every gate object and compare each gate objects node
    * connection to the Node object passed in as a parameter that is to be deleted. If
    * a certain gate object contains a connection to this object to be deleted, then its
    * connection will be reset.
    */
    public static void resetNodeConnections(Node n, List<Node> gates){
        for(Node gate : gates){
            if(gate instanceof AndGate){
                checkAndGateConnections(n, (AndGate)gate);
            }
            else if(gate instanceof OrGate){
                checkOrGateConnections(n, (OrGate)gate);
            }
            else if(gate instanceof NotGate){
                checkNotGateConnection(n, (NotGate)gate);
            }
        }
    }

    /*
    * This method will search through every light object and compare each light objects node
    * connection to the node passed in as a parameter that is to be deleted. If a certain light
    * object is found that contains a connection to the node passed in as a parameter, then that
    * light objects connection will be reset to null.
    */
    public static void resetLightConnections(Node n, List<Light> lights){
        for(Light light : lights){
            if(light.getSource() == n){
                light.setSource(null);
            }
        }
    }

    private static void checkAndGateConnections(Node n, AndGate gate){
        if(gate.getSourceA() == n){
            gate.setSourceA(null);
        }
        if(gate.getSourceB() == n){
            gate.setSourceB(null);
        }
    }

    private static void checkOrGateConnections(Node n, OrGate gate){
        if(gate.getSourceA() == n){
            gate.setSourceA(null);
        }
        if(gate.getSourceB() == n){
            gate.setSourceB(null);
        }
    }

    private static void checkNotGateConnection(Node n, NotGate gate){
        if(gate.getSource() == n){
            gate.setSource(null);
        }
    }

    /*
    * Handles connecting any type of logic gate or switch to an and gate.
    * a represents any particular logic gate, b represents an and gate.
    */
    private static boolean nodeToAndGate(Node a, AndGate b){
        if(b.getSourceA() == null){
            b.setSourceA(a);
            return true;
        }
        else if(b.getSourceB() == null){
            b.setSourceB(a);
            return true;
        }
        return false;
    }

    /*
    * Handles connecting any type of logic gate or switch to an or gate.
    * a represents any particular logic gate or switch, b represents an or gate.
    */
    private static boolean nodeToOrGate(Node a, OrGate b){
        if(b.getSourceA() == null){
            b.setSourceA(a);
            return true;
        }
        else if(b.getSourceB() == null){
            b.setSourceB(a);
            return true;
        }
        return false;
    }

    /*
    * Handles connecting any type of logic gate or switch to a not gate.
    * a represents any particular logic gate or switch , b represents a not gate.
    */
    private static boolean nodeToNotGate(Node a, NotGate b){
        if(b.getSource() == null){
            b.setSource(a);
            return true;
        }
        return false;
    }
}