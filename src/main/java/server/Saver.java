package server;

import java.io.*;

/**
 * Created by ragvalod on 12.06.16.
 */
public class Saver {
    private String name;
    private String data;
    private static Integer partCount = 0;
    private static boolean isLast = false;


    public Saver(String name, String data, boolean isLast) {
        this.name = name;
        this.data = data;
        Saver.isLast = (Saver.isLast || isLast);
        partCount = Integer.valueOf(name);

    }

    public void savePart() {
        File file = new File("../work/" + name);
        try (FileWriter fileWriter = new FileWriter(file)) {
            if (file.canWrite()) {
                fileWriter.write(data);
                fileWriter.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (isLast) {
            makeFile();
        }
    }

    public void makeFile() {
        File dir = new File("../work/");
        File[] files = dir.listFiles();

        System.out.println("dir contain " + files.length + " files");
        if (files.length >= partCount) {
            File finalFile = new File("../work/Final");

            try (FileWriter fileWriter = new FileWriter(finalFile)) {
                for (int i = 1; i <= partCount; i++) {
                    File file = new File("../work/" + String.valueOf(i));
                    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                        String tmp = reader.readLine();
                        while (tmp != null) {
                            if (finalFile.canWrite()) {
                                fileWriter.write(tmp);
                            }
                            tmp = reader.readLine();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
