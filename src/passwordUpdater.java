import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

    public class PasswordUpdater {

        public static void main(String args[]) throws IOException {

            Scanner console = new Scanner(System.in);
            boolean hasUpdatedPassword = false;
            int wrongPasswordCounter=5;
            while(!hasUpdatedPassword) {
                System.out.print("Enter Current Password : ");
                String currentPassword = console.nextLine();
                int enteredCurrentPassword = currentPassword.hashCode();


                File originalFile = new File("passwordStorage.txt");
                BufferedReader br = new BufferedReader(new FileReader(originalFile));

                File tempFile = new File("tempfile.txt");
                PrintWriter pw = new PrintWriter(new FileWriter(tempFile));

                String line = null;
                int numberOfPasswords = 2;

                while (numberOfPasswords > 0) {
                    line = br.readLine();
                    String storedCurrPassword = line.substring(line.lastIndexOf(" ") + 1, line.length());

                    if (storedCurrPassword.equals(Integer.toString(enteredCurrentPassword))) {
                        System.out.print("Enter New Alphanumeric Password: ");
                        boolean isValidPassword = false;
                        String newPassword = null;
                        while (!isValidPassword) {
                            String newEnteredPassword = console.nextLine();
                            if (newEnteredPassword.matches(".*\\d+.*") && newEnteredPassword.matches(".*[a-zA-Z]+.*")) {
                                newPassword = newEnteredPassword;
                                isValidPassword = true;
                            } else if (newEnteredPassword.matches(".*\\d+.*") && !newEnteredPassword.matches(".*[a-zA-Z]+.*")) {
                                System.out.println("Your new password must contain at least one alphabet. Please try again.");
                                System.out.print("Enter New Alphanumeric Password: ");
                            } else if (!newEnteredPassword.matches(".*\\d+.*") && newEnteredPassword.matches(".*[a-zA-Z]+.*")) {
                                System.out.println("Your new password must contain at least one number. Please try again.");
                                System.out.print("Enter New Alphanumeric Password: ");
                            } else {
                                System.out.println("Your new password can only be alphanumeric");
                                System.out.print("Enter New Alphanumeric Password: ");
                            }
                        }
                        int storedNewPassword = newPassword.hashCode();
                        if (storedCurrPassword != null || !storedCurrPassword.trim().isEmpty()) {
                            line = line.substring(0, line.lastIndexOf(" ") + 1) + Integer.toString(storedNewPassword);
                            System.out.println("You have updated your password to : " + newPassword);
                            hasUpdatedPassword = true;
                        }
                    }
                    pw.println(line);
                    pw.flush();
                    numberOfPasswords--;
                }
                pw.close();
                br.close();

                // Delete the original file
                if (!originalFile.delete()) {
                    System.out.println("Could not delete file");
                    return;
                }
                // Rename the new file to the filename the original file had.
                if (!tempFile.renameTo(originalFile)) {
                    System.out.println("Could not rename file");
                }
                if (!hasUpdatedPassword) {
                    System.out.println("Wrong Password. Please try again.");
                    if(wrongPasswordCounter==1){
                        System.out.println("You have " + wrongPasswordCounter + " attempt left.");
                        System.out.println("System will shut down if password is incorrect");
                        wrongPasswordCounter--;
                    }
                    else if (wrongPasswordCounter==0){
                        System.out.println("System shutting down.");
                        return;
                    }
                    else {
                        System.out.println("You have " + wrongPasswordCounter + " attempts left.");
                        wrongPasswordCounter--;
                    }
                }
            }
        }
    }

