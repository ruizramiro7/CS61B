package gitlet;

import java.sql.Timestamp;
import java.util.HashMap;

public class CommitTree {

    public class CommitNode {
        private String message;
        private Timestamp timestamp;
        private HashMap<Integer, String> references;
        public CommitNode[] next;
        public CommitNode prev;
    }

    private CommitNode head;
    private CommitNode staged;
}
