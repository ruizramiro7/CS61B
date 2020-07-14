package gitlet;

import java.io.File;
import java.io.IOException;

/** Driver class for Gitlet, the tiny stupid version-control system.
 *  @author Ramiro Ruiz
 *  @author Brandon Bizzarro
 */
public class Main {
    static final File CWD = new File(".");
    static final File METADATA_DIR = Utils.join(CWD, ".gitlet");

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND> .... */
    public static void main(String... args) {
        if (args.length == 0) {
            exitWithError("Must have at least one argument");
        }
        if (!args[0].equals("init") && !checkInit()) {
            exitWithError("Gitlet has not been initialized in this directory.");
        }
        switch (args[0]) {
            case "init":
                System.out.println("initializing");
                initialize();
                break;
            case "add":
                break;
            case "commit":
                break;
            case "rm":
                break;
            case "log":
                break;
            case "global-log":
                break;
            case "find":
                break;
            case "status":
                break;
            case "checkout":
                break;
            case "branch":
                break;
            case "rm-branch":
                break;
            case "reset":
                break;
            case "merge":
                break;
            case "rebase":
                break;
        }
    }

    public static boolean checkInit() {
        return METADATA_DIR.exists() && CommitTree.COMMIT_LOG.exists();
    }

    public static void initialize() {
        if (!METADATA_DIR.exists()) {
            METADATA_DIR.mkdir();
        }
        try {
            CommitTree.COMMIT_LOG.createNewFile();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @source CS61BL Lab09
     * @param message
     */
    public static void exitWithError(String message) {
        if (message != null && !message.equals("")) {
            System.out.println(message);
        }
        System.exit(-1);
    }


}
