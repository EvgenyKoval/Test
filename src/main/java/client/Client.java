package client;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;


public class Client {

    public final static void main(String[] args) {

        File file = new File("testFile");
        System.out.println("_________");
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String data1 = reader.readLine();
            String hash1 = "";
            Integer number1 = 0;
            String tmp = "";
            do {
                tmp = reader.readLine();
                hash1 = DigestUtils.md5Hex(data1);
                data1 = Base64.encodeBase64String(data1.getBytes());
                if (tmp != null) {
                    new Thread(new Sender(String.valueOf(++number1), hash1, data1, String.valueOf(false))).start();
                } else {
                    new Thread(new Sender(String.valueOf(++number1), hash1, data1, String.valueOf(true))).start();
                }
                data1 = tmp;
            } while (data1 != null);
        } catch (IOException e) {

        }
//        String data = "Man is distinguished, not only by his reason, but by this singular " +
//                "passion from other animals, which is a lust of the mind, that by a persev" +
//                "erance of delight in the continued and indefatigable generation of knowle" +
//                "dge, exceeds the short vehemence of any carnal pleasure.";
//        String hash = DigestUtils.md5Hex(data);
//        data = "TWFuIGlzIGRpc3Rpbmd1aXNoZWQsIG5vdCBvbmx5IGJ5IGhpcyByZWFzb24sIGJ1dCBieSB0" +
//                "aGlzIHNpbmd1bGFyIHBhc3Npb24gZnJvbSBvdGhlciBhbmltYWxzLCB3aGljaCBpcyBhIGx1" +
//                "c3Qgb2YgdGhlIG1pbmQsIHRoYXQgYnkgYSBwZXJzZXZlcmFuY2Ugb2YgZGVsaWdodCBpbiB0" +
//                "aGUgY29udGludWVkIGFuZCBpbmRlZmF0aWdhYmxlIGdlbmVyYXRpb24gb2Yga25vd2xlZGdl" +
//                "LCBleGNlZWRzIHRoZSBzaG9ydCB2ZWhlbWVuY2Ugb2YgYW55IGNhcm5hbCBwbGVhc3VyZS4=";
//
//        int n = 100;
//        for (int i = 1; i < n; i++) {
//            new Thread(new Sender(String.valueOf(i), hash, data, String.valueOf(false))).start();
//        }
//        new Thread(new Sender(String.valueOf(n), hash, data, String.valueOf(true))).start();
    }

    private static class Sender implements Runnable {
        private String number;
        private String hash;
        private String data;
        private String isLast;

        public Sender(String number, String hash, String data, String isLast) {
            this.number = number;
            this.hash = hash;
            this.data = data;
            this.isLast = isLast;
        }

        @Override
        public void run() {

            try {
                URLConnection connection = new URL("http://localhost:8080").openConnection();
                connection.setDoOutput(true);
                try (PrintWriter writer = new PrintWriter(new OutputStreamWriter(connection.getOutputStream()))) {
                    int responseCode = 0;
                    do {
                        writer.print("number=" + number + "&isLast=" + isLast + "&hash=" + hash + "&data=" + data);
                        writer.flush();
                        responseCode = ((HttpURLConnection) connection).getResponseCode();
                        System.out.println("number:" + number + " respCode" + responseCode); // Should be 200
                    } while (responseCode != 200);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}