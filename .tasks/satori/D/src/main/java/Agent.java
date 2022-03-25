/**
 * PO 2021/22, Problem D - Alfabet Morse'a
 * @author YOUR NAME
 */

import java.nio.CharBuffer;

public class Agent implements Readable, TajnyAgent {

    Agent() {    }
    Agent(TajnyAgent remote) {    }
    Agent(String password) { }
    Agent(TajnyAgent remote, String password) { }

    public void transmituj() { }

    @Override
    public int read(CharBuffer cb) { return  0; }
}


