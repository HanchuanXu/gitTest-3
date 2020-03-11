package P1;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.stream.Collectors;

/*
 * HIT Software Construction Lab1 MagicSquare
 * 
 *  
 * @version V1.0
 */
public class MagicSquare{

    private static final String relativePath = "./src/P1/txt/";
    private static final int radix = 10;

    /*
     * The main method will generate a random Magic Square
     * in 6.txt using the generateMagicSquare(random).Then
     * it will invoke isLegalMagicSquare to check all the
     * files under txt dir(inlcuding the 6.txt). The ouput of
     * result is out-of-order.
     * 
     * @param the command line strings
     * 
     * @return void
     * 
     * @throws if it failed to open the dir or txt files
     */
    public static void main(String[] args) throws IOException {

        // choose an odd int to generate Magic square
        final int max = 50;
        int random = (new Random()).nextInt(max);
        random += (random % 2 == 0 ? 1 : 0);

        generateMagicSquare(random);

        // get all the files under txt dir
        List<Path> pathsOfTxt = Files.walk(Paths.get(relativePath)).filter(Files::isRegularFile)
                .collect(Collectors.toList());

        // check if they are Magic square
        for (Path pathOfTxt : pathsOfTxt) {
            isLegalMagicSquare(pathOfTxt.toString());
        }

    }

    /*
     * Check if fileName is a Magic Square: n*n matrix;
     * splited by \t;all numbers are positive ints;the sum
     * of rows equals the sum of columns and the sum of diagonal.
     * 
     * @param the name of the file
     * 
     * @return true if it's a Magic Square, otherwise false and
     * print the error message on output
     * 
     * @throws if it failed to open txt file
     */
    public static boolean isLegalMagicSquare(String fileName) throws IOException {

        List<String[]> matrix = new ArrayList<>();
        Scanner in = new Scanner(Paths.get(fileName));

        System.out.println(fileName + ":");

        // read and split every line in file and
        // store it in matrix
        while (in.hasNextLine()) {
            String line = in.nextLine();
            String[] words = line.split("\t");
            matrix.add(words);
        }
        in.close();

        // check whether it's a n*n matrix splited by \t
        int rows = matrix.size();
        final String error1 = "It's not a n*n matrix splited by \\t\n";
        for (String[] words : matrix) {
            if (words.length != rows) {
                System.out.println(error1);
                return false;
            }
        }

        // check if it only contains positive ints splited by \t
        final String error2 = "The numbers should be ints(splited by \\t)\n";
        final String error3 = "The numbers should be positive ints\n";
        for (String[] words : matrix) {
            for (String word : words) {
                Scanner sc = new Scanner(word);
                if (sc.hasNextInt(radix)) {
                    int number = sc.nextInt(radix);
                    // we know it starts with a valid int, now make sure
                    // there's nothing left!
                    if (!sc.hasNext()) {
                        if (number > 0) {
                            continue;
                        } else {
                            System.out.println(error3);
                            sc.close();
                            return false;
                        }
                    } else {
                        System.out.println(error2);
                        sc.close();
                        return false;
                    }
                } else {
                    System.out.println(error2);
                    sc.close();
                    return false;
                }
            }
        }

        // check if it meet the characters of magic square
        final String error4 = "The n numbers in all rows, all columns, and both\n"
                + "diagonals should sum to the same constant\n";
        int[] sumOfRows = new int[rows];
        int[] sumOfColumns = new int[rows];
        // compute and store the sum of rows
        for (int i = 0; i < rows; i++) {
            String[] words = matrix.get(i);
            for (String word : words) {
                Scanner sc = new Scanner(word);
                int number = sc.nextInt(radix);
                sc.close();
                sumOfRows[i] += number;
            }
        }
        // compute and store the sum of columns
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < rows; j++) {
                String word = matrix.get(j)[i];
                Scanner sc = new Scanner(word);
                int number = sc.nextInt(radix);
                sc.close();
                sumOfColumns[i] += number;
            }
        }
        // compute and store the sum of diagonal
        int sum1 = 0, sum2 = 0;
        for (int i = 0; i < rows; i++) {
            String word1 = matrix.get(i)[i];
            Scanner sc1 = new Scanner(word1);
            int number1 = sc1.nextInt(radix);
            sc1.close();
            String word2 = matrix.get(i)[rows - i - 1];
            Scanner sc2 = new Scanner(word2);
            int number2 = sc2.nextInt(radix);
            sc2.close();
            sum1 += number1;
            sum2 += number2;
        }
        // check if the sum of diagonal equals
        if (sum1 != sum2) {
            System.out.println(error4);
            return false;
        }
        // check if the sum of rows equals
        for (int i : sumOfRows) {
            if (i != sum1) {
                System.out.println(error4);
                return false;
            }
        }
        // check if the sum of columns equals
        for (int i : sumOfColumns) {
            if (i != sum1) {
                System.out.println(error4);
                return false;
            }
        }

        final String success = "It's a Magic Square\n";
        System.out.println(success);
        return true;
    }

    
    /*
     * Create a n*n Magic Square in 6.txt. If n is not
     * an odd positive ints, print error message and return
     * false.
     * 
     * @param the length of side
     * 
     * @return ture if it create Magic Square successfully,
     * otherwise false
     */
    public static boolean generateMagicSquare(int n) {

        // n should be an positive odd int
        final String error1 = "The parameter of generateMagicSquare should be odd positive int\n";
        if (n % 2 == 0 || n < 0) {
            System.out.println(error1);
            return false;
        }

        // the algorithm of creating a Magic Square
        int magic[][] = new int[n][n];
        int row = 0, col = n / 2, i, j, square = n * n;
        for (i = 1; i <= square; i++) {
            magic[row][col] = i;
            if (i % n == 0)
                row++;
            else {
                if (row == 0)
                    row = n - 1;
                else
                    row--;
                if (col == (n - 1))
                    col = 0;
                else
                    col++;
            }
        }
        /*
         * for (i = 0; i < n; i++) {
         * for (j = 0; j < n; j++)
         * System.out.print(magic[i][j] + "\t");
         * System.out.println();
         * }
         */

        // store the square in 6.txt line by line
        try {
            PrintWriter out = new PrintWriter(relativePath + "6.txt", "UTF-8");
            String line;
            for (i = 0; i < n; i++) {
                line = "";
                for (j = 0; j < n - 1; j++)
                    line += magic[i][j] + "\t";
                line += magic[i][j];
                out.println(line);
            }
            out.close();
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return true;
    }

}
