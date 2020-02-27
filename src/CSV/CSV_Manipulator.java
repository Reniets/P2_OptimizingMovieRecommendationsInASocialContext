package CSV;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;

public class CSV_Manipulator {

    public void cleanUp(ArrayList<Integer> columns, ArrayList<HashSet<String>> requirements, String filePath) {

        try {

            // Prepare reading from file
            CSV_Reader CSVR = new CSV_Reader(filePath);

            // Prepare writing to file
            String[] filePathSplit = filePath.split("\\.");

            int strLen = filePathSplit.length;
            String newFilePath = "";

            for (int i = 0; i < strLen-1; i++){

                if (((strLen - 2) != i)){
                    newFilePath += filePathSplit[i] + ".";
                } else {
                    newFilePath += filePathSplit[i] + "_CLEANED." + filePathSplit[strLen-1];
                }
            }

            // Prepare printWriter to write in specified file.
            FileWriter fileWriter = new FileWriter(newFilePath, false);
            PrintWriter printWriter = new PrintWriter(fileWriter);

            boolean requirementMet;
            int totalRequirements = columns.size();

            // While there are data to read.
            for (ArrayList<String> readLine : CSVR){
                requirementMet = true;

                // Check if all requirements are met.
                for (int i = 0; i < totalRequirements; i++) {
                    requirementMet = requirementMet && existInArray(readLine.get(columns.get(i)), requirements.get(i));
                }

                // If the requirements are met, then print the line to our new file.
                if (requirementMet) {
                    printReadLineToFile(printWriter, readLine);
                }

                CSVR.printPercentage();
            }

            printWriter.close();

            // If an error occurred, catch it and print the error.
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void printReadLineToFile(PrintWriter printWriter, ArrayList<String> readLine){

        int totalElements = readLine.size();
        for (int i = 0; i < totalElements; i++){
            printWriter.print(readLine.get(i));

            if (i != totalElements - 1){
                printWriter.print(",");
            } else {
                printWriter.println();
            }
        }

    }

    public boolean existInArray(String param, HashSet<String> reqArray) {

        // Check param exist in the req set. Return true if it does and false otherwise.
        for (String req : reqArray) {
            if (param.equals(req)) {
                return true;
            }
        }

        return false;
    }

    public HashSet<String> extractUnique(int column, String filePath) {

        HashSet<String> uniqueSet = new HashSet<>(0);
        CSV_Reader CSVR = new CSV_Reader(filePath);

        for (ArrayList<String> readLine : CSVR){
            uniqueSet.add(readLine.get(column));
        }

        return uniqueSet;
    }

    public ArrayList<String> extractAll(int column, String filePath) {

        ArrayList<String> arrayList = new ArrayList<>();
        CSV_Reader CSVR = new CSV_Reader(filePath);

        for (ArrayList<String> readLine : CSVR){
            arrayList.add(readLine.get(column));
        }

        return arrayList;
    }

}

