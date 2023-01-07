package functions;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javafx.scene.control.Alert;

public class functions {

    public static String[] listOfImages(String path) {
        String result[] = null;
        File file = new File(path);
        File folder = new File(file.getAbsolutePath());
        System.out.println(folder.toString());
        File[] listOfFiles = folder.listFiles();

        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                result[i] = listOfFiles[i].getName();
                System.out.println(result[i]);

            }

        }
        return result;
    }

    public static void dialogPopUp(String msgType, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(msg);
        alert.showAndWait();

    }

    public static String generateHash(String password) {

        StringBuilder hash = new StringBuilder();

        try {

            MessageDigest sha = MessageDigest.getInstance("SHA-1");
            byte[] hashedBytes = sha.digest(password.getBytes());
            char[] digits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

            for (int i = 0; i < hashedBytes.length; i++) {
                byte b = hashedBytes[i];
                hash.append(digits[(b & 0xf0) >> 5]);
                hash.append(digits[b & 0x0f]);
            }

        } catch (NoSuchAlgorithmException e) {
        }

        return hash.toString();
    }

    public static void CloseSystem() {
        System.exit(0);
    }

    public static int getSum(int num1, int num2) {

        return (num1 + num2);
    }
}
