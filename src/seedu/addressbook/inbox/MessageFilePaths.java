//@@author ongweekeong
package seedu.addressbook.inbox;

import seedu.addressbook.password.Password;

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
        switch(userId) {

            case Password.MESSAGE_HQP:
                return MessageFilePaths.FILEPATH_HQP_INBOX;
            case Password.MESSAGE_ONE:
                return MessageFilePaths.FILEPATH_PO1_INBOX;
            case Password.MESSAGE_TWO:
                return MessageFilePaths.FILEPATH_PO2_INBOX;
            case Password.MESSAGE_THREE:
                return MessageFilePaths.FILEPATH_PO3_INBOX;
            case Password.MESSAGE_FOUR:
                return MessageFilePaths.FILEPATH_PO4_INBOX;
            case Password.MESSAGE_FIVE:
                return MessageFilePaths.FILEPATH_PO5_INBOX;
            default:
                return FILEPATH_DEFAULT;
        }
    }

}
