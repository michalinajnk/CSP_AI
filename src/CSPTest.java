import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CSPTest {

    static String rootPath = "./src/data/";

    public  static void main(String[] args) throws Exception {

    //    Solutions sol = getSolutionsFor("futoshiki", "futoshiki6_6");
    //    for(Solution s: sol.getSolutions()){
     //       System.out.println(s.toString());
     //   }

        //Grid binaryG = GridReader.initializeFromFile("binary/binary_6x6");
        //System.out.println(binaryG.toString());


        Solutions sol = getSolutionsFor("futoshiki", "futoshiki5_5");
       for(Solution s: sol.getSolutions()){
          System.out.println(s.toString());
       }

    }

    static Solutions getSolutionsFor(String dir, String fileName) throws Exception {
        //SolverAlgorithm backtracking = new Backtracking();
        SolverAlgorithm forwardchecking = new ForwardChecking();
        Solver solver = new Solver(forwardchecking, dir + "/" + fileName);
        Solutions solutions = solver.run();
        System.out.println(solutions);
        return solutions;
    }

    public static void manualRun() throws Exception {
        Grid grid = GridReader.initializeFromFile("futoshiki/futoshiki_6x6");
        System.out.println(grid);
        Scanner scanner = new Scanner(System.in);
        while (grid.validate() && !grid.isPoorlyCompleted()) {
            System.out.print("Next params: ");
            int row = scanner.nextInt();
            int col = scanner.nextInt();
            int val = scanner.nextInt();

            grid.move(new Indices(row, col), val);
            System.out.println(grid);
        }
    }

    static List<Solutions> runforFiles(String directory, List<String> fileNames) throws Exception {

        List<Solutions> allSolutions = new ArrayList<>();
        for (String fileName : fileNames) {
            Solver Solver = new Solver(new Backtracking(), directory + "/" + fileName);
            Solutions solutions = Solver.run();
            allSolutions.add(solutions);
            System.out.println(solutions);
        }
        return allSolutions;
    }

    static List<String> getFilesInDirectory(String directory) {
        List<String> result = new ArrayList<>();
        try (Stream<Path> walk = Files.walk(Paths.get(rootPath + "/" + directory))) {

            result = walk.filter(Files::isRegularFile).map(Path::getFileName)
                    .map(Path::toString).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}


