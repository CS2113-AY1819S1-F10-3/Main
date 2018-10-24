//@@author ongweekeong
package seedu.addressbook.inbox;

/**
 * Container for filepaths to storage files of each user's messages.
 */
public class MessageFilePaths {
    public static final String FILEPATH_HQP_INBOX = "inboxMessages/headquartersInbox";
    public static final String FILEPATH_PO1_INBOX = "inboxMessages/PO1";
    public static final String FILEPATH_PO2_INBOX = "inboxMessages/PO2";
    public static final String FILEPATH_PO3_INBOX = "inboxMessages/PO3";
    public static final String FILEPATH_PO4_INBOX = "inboxMessages/PO4";
    public static final String FILEPATH_PO5_INBOX = "inboxMessages/PO5";
    public static final String FILEPATH_DEFAULT = "notifications.txt";

    public static String getFilePathFromUserId(String userId){
        String filepath;
        switch(userId) {
            case "hqp":
                return MessageFilePaths.FILEPATH_HQP_INBOX;
            case "po1":
                return MessageFilePaths.FILEPATH_PO1_INBOX;
            case "po2":
                return MessageFilePaths.FILEPATH_PO2_INBOX;
            case "po3":
                return MessageFilePaths.FILEPATH_PO3_INBOX;
            case "po4":
                return MessageFilePaths.FILEPATH_PO4_INBOX;
            case "po5":
                return MessageFilePaths.FILEPATH_PO5_INBOX;
            default:
                return FILEPATH_DEFAULT;
        }
    }

}
