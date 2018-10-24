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

    public String getFilePathFromUserId(String userId){
        String filepath;
        switch(userId) {
            case "hqp":
                filepath = MessageFilePaths.FILEPATH_HQP_INBOX;
                break;
            case "po1":
                filepath = MessageFilePaths.FILEPATH_PO1_INBOX;
                break;
            case "po2":
                filepath = MessageFilePaths.FILEPATH_PO2_INBOX;
                break;
            case "po3":
                filepath = MessageFilePaths.FILEPATH_PO3_INBOX;
                break;
            case "po4":
                filepath = MessageFilePaths.FILEPATH_PO4_INBOX;
                break;
            case "po5":
                filepath = MessageFilePaths.FILEPATH_PO5_INBOX;
                break;
            default:
                filepath = FILEPATH_DEFAULT;
        }
        return filepath;
    }

}
