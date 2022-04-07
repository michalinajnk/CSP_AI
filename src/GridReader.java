import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class GridReader {
    private static String file_path;
    public static GridReader instance = new GridReader();
    public static final int EMPTY_FIELD = -1;
    private static FileReader fileReader;
    private GridReader(){
        file_path = "./src/data/";
    }

    static Grid initializeFromFile(String fileName) throws Exception {
        if (fileName.contains("futoshiki")) {
            return  initializeFutoshikiFromFile(fileName);
        } else if (fileName.contains("binary")) {
            return initializeBinaryFromFile(fileName);
        } else {
            throw new IllegalStateException("Not recognized");
        }
    }


    public static FutoshikiGrid initializeFutoshikiFromFile(String fileName) throws Exception {
        FutoshikiGrid futoshikiGrid = null;
        fileReader = new FileReader(file_path + fileName);

        try (BufferedReader fr = new BufferedReader(fileReader)) {
            String line = fr.readLine();
            Integer size = Integer.valueOf(line);
            futoshikiGrid = new FutoshikiGrid(size);
            line = fr.readLine();
            if (!line.contains("START")) {
                throw new IllegalStateException("START string was expected");
            }
            for (Integer rowNum = 0; rowNum < size; rowNum++) {
                String row = fr.readLine();
                String[] splitedRow = row.split(";");
                for (int colNum = 0; colNum < splitedRow.length; colNum++) {
                    if (!splitedRow[colNum].equals("0")) {
                        int value = Integer.parseInt(splitedRow[colNum]);
                        Indices ind = new Indices(rowNum, colNum);
                        futoshikiGrid.setField(Field.createForSingleValue(ind, value), ind);
                    }
                }
            }
            line = fr.readLine();
            if (!line.contains("REL:")) {
                throw new IllegalStateException("REL string was expected");
            }
            line = fr.readLine();
            while (line != null) {
                String[] splittedLine = line.split(";");
                if (splittedLine.length == 2) {
                    Indices smaller = new Indices(splittedLine[0].charAt(0) - 'A', Integer.valueOf(splittedLine[0].substring(1)) - 1);
                    Indices bigger = new Indices(splittedLine[1].charAt(0) - 'A', Integer.valueOf(splittedLine[1].substring(1)) - 1);
                    futoshikiGrid.constraints.add(new FutoshikiConstraint(smaller, bigger));
                }
                line = fr.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return futoshikiGrid;
    }

    public static BinaryGrid initializeBinaryFromFile(String fileName) throws FileNotFoundException {
        int sizeOfGrid = Integer.valueOf(String.valueOf(fileName.charAt(fileName.length() - 1)));
        BinaryGrid binaryGrid = new BinaryGrid(sizeOfGrid);
        fileReader = new FileReader(file_path + fileName);
        try (BufferedReader fr = new BufferedReader(fileReader)) {
            for (int i = 0; i < sizeOfGrid; i++) {
                String row2 = fr.readLine();
                char[] row = row2.toCharArray();
                for (int j = 0; j < sizeOfGrid; j++) {
                    Indices ind = new Indices(i, j);
                    if(row[j] !='x'){
                        int value = Integer.parseInt(String.valueOf(row[j]));
                        binaryGrid.setField(Field.createForSingleValue(ind, value), ind);
                    } else {
                       // binaryGrid.setField(Field.createForAvailableValues(ind, binaryGrid.getGridDomain()),ind);
                    }


                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        binaryGrid.rebuildConstraints();
        return binaryGrid;
    }


    public static FutoshikiGrid initializeFutoshikiFromFile2(String fileName) throws Exception {
        int sizeOfGrid = Integer.valueOf(String.valueOf(fileName.charAt(fileName.length() - 1)));
        FutoshikiGrid futoshikiGrid = new FutoshikiGrid(sizeOfGrid);
        fileReader = new FileReader(file_path + fileName);
        int lineCounter = 1;
        try (BufferedReader fr = new BufferedReader(fileReader)) {
            for (Integer i = 0, rowNum = 0;rowNum < sizeOfGrid; i++) {
                String row2 = fr.readLine();
                char[] row = row2.toCharArray();
                for (int j = 0, colNum = 0; colNum < sizeOfGrid; j++) {
                    if(lineCounter%2 == 1) {
                        if (row[j] !='-') {
                            if(row[j] =='<') {
                                Indices smaller = new Indices(rowNum, colNum);
                                Indices bigger = new Indices(rowNum, colNum+1);
                                futoshikiGrid.constraints.add(new FutoshikiConstraint(smaller, bigger));
                            }
                            else if (row[j] =='>') {
                                Indices smaller = new Indices(rowNum, colNum +1);
                                Indices bigger = new Indices(rowNum, colNum);
                                futoshikiGrid.constraints.add(new FutoshikiConstraint(smaller, bigger));
                            }
                            else {

                                if(row[j] !='x'){
                                    int value = Integer.parseInt(String.valueOf(row[j]));
                                    Indices ind = new Indices(rowNum, colNum);
                                    futoshikiGrid.setField(Field.createForSingleValue(ind, value), ind);
                                }
                                colNum++;
                            }
                        }
                    }else {

                        if (row[j] != '-') {
                            if (row[j] == '<') {
                                Indices smaller = new Indices(rowNum, colNum);
                                Indices bigger = new Indices(rowNum+1, colNum);
                                futoshikiGrid.constraints.add(new FutoshikiConstraint(smaller, bigger));
                            } else if (row[j] == '>') { //ten wyżej jest większy //niższy
                                Indices bigger = new Indices(rowNum+1, colNum);
                                Indices smaller = new Indices(rowNum, colNum);
                                futoshikiGrid.constraints.add(new FutoshikiConstraint(smaller, bigger));
                            }
                        }
                        colNum++;
                    }
                }

                lineCounter++;
                if(lineCounter%2 == 1)
                    rowNum++;
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
        return futoshikiGrid;
    }


}
