import java.io.BufferedReader;
import java.io.FileReader;

public class WordPuzzle {
    public static void main(String[] args) {
        int wordCount = Integer.parseInt(args[0]);
        QuadraticProbingHashTable<String> dictionary = new QuadraticProbingHashTable<>(wordCount * 2);

        // Read the dictionary file
        try (BufferedReader reader = new BufferedReader(new FileReader(args[1]))) {
            String word;
            while ((word = reader.readLine()) != null) {
                dictionary.insert(word);
            }
        } catch (Exception e) {
            System.out.println("Error reading dictionary file: " + e.getMessage());
        }

        // Print to std error the dictionary stats
        System.err.print("Number of words " + dictionary.getNumberOfElements() + ", ");
        System.err.print("Table size " + dictionary.getHashTableSize() + ", ");
        System.err.print("Load factor " + dictionary.getLoadFactor() + "\n");
        System.err.print("Collisions " + dictionary.getCollisions() + ", ");
        System.err.print("Average chain length " + dictionary.getAvgCollisionChain() + ", ");
        System.err.print("Longest chain length " + dictionary.getLongestCollisionChain() + "\n");

        // Read the puzzle file
        int rows = 0, cols = 0;
        char[][] puzzle = null;
        try (BufferedReader reader = new BufferedReader(new FileReader(args[2]))) {
            rows = Integer.parseInt(reader.readLine());
            cols = Integer.parseInt(reader.readLine());

            puzzle = new char[rows][cols];
            char[] line = reader.readLine().toCharArray();
            for (int i = 0; i < rows; i++ ) {
                for (int j = i * cols; j < (i+1) * cols; j++) {
                    puzzle[i][j%cols] = line[j];
                }
            }
        } catch (Exception e) {
            System.out.println("Error reading puzzle file: " + e.getMessage());
        }

        // Traverse the puzzle and check if words are in dictionary

        int i =0, j= 0; // increment to go through rows and cols of puzzle
        int len = 0; // incremented then added or subtracted from i or j to build a word of length len+1 in a particular direction
        int wordsFound = 0;
        StringBuilder search; // build a word from letters in puzzle to chech if its in dictionary

        // East
        for (i = 0; i < rows; i++) {
            for (j = 0; j < cols - 2; j++) {
                search = new StringBuilder();

                for (int k = 0; k < 2; k++ ) //initial first 2 letters
                    search.append(puzzle[i][j+k]);
                
                // build the word from 3 letters to 22 letters or end of grid
                len = 2;
                while (j+len < cols && len <= 22) {
                    search.append(puzzle[i][j+len]);
                    if (dictionary.contains(search.toString())) {
                        System.out.println("(" + i + ", " + j + ", E): " + search.toString());
                        wordsFound++;
                    }
                    len++;
                }
            }
        }

        // South
        for (i = 0; i < rows - 2; i++) {
            for (j = 0; j < cols; j++) {
                search = new StringBuilder();

                for (int k = 0; k < 2; k++ ) //initial first 2 letters
                    search.append(puzzle[i+k][j]);
                
                // build the word from 3 letters to 22 letters or end of grid
                len = 2;
                while (i+len < rows && len <= 22) {
                    search.append(puzzle[i+len][j]);
                    if (dictionary.contains(search.toString())) {
                        System.out.println("(" + i + ", " + j + ", S): " + search.toString());
                        wordsFound++;
                    }
                    len++;
                }
            }
        }

        // West
        for (i = 0; i < rows; i++) {
            for (j = cols - 1; j >= 2; j--) {
                search = new StringBuilder();

                for (int k = 0; k < 2; k++ ) //initial first 2 letters
                    search.append(puzzle[i][j-k]);
                
                // build the word from 3 letters to 22 letters or end of grid
                len = 2;
                while (j-len >= 0 && len <= 22) {
                    search.append(puzzle[i][j-len]);
                    if (dictionary.contains(search.toString())) {
                        System.out.println("(" + i + ", " + j + ", W): " + search.toString());
                        wordsFound++;
                    }
                    len++;
                }
            }
        }

        // North
        for (i = rows - 1; i >= 2; i--) {
            for (j = 0; j < cols; j++) {
                search = new StringBuilder();

                for (int k = 0; k < 2; k++ ) //initial first 2 letters
                    search.append(puzzle[i-k][j]);
                
                // build the word from 3 letters to 22 letters or end of grid
                len = 2;
                while (i-len >= 0 && len <= 22) {
                    search.append(puzzle[i-len][j]);
                    if (dictionary.contains(search.toString())) {
                        System.out.println("(" + i + ", " + j + ", N): " + search.toString());
                        wordsFound++;
                    }
                    len++;
                }
            }
        }

        // Southeast
        for (i = 0; i < rows - 2; i++) {
            for (j = 0; j < cols - 2; j++) {
                search = new StringBuilder();

                for (int k = 0; k < 2; k++ ) //initial first 2 letters
                    search.append(puzzle[i+k][j+k]);
                
                // build the word from 3 letters to 22 letters or end of grid
                len = 2;
                while (i+len < rows && j+len < cols && len <= 22) {
                    search.append(puzzle[i+len][j+len]);
                    if (dictionary.contains(search.toString())) {
                        System.out.println("(" + i + ", " + j + ", SE): " + search.toString());
                        wordsFound++;
                    }
                    len++;
                }
            }
        }

        // Southwest
        for (i = 0; i < rows - 2; i++) {
            for (j = cols - 1; j >= 2; j--) {
                search = new StringBuilder();

                for (int k = 0; k < 2; k++ ) //initial first 2 letters
                    search.append(puzzle[i+k][j-k]);
                
                // build the word from 3 letters to 22 letters or end of grid
                len = 2;
                while (i+len < rows && j-len >= 0 && len <= 22) {
                    search.append(puzzle[i+len][j-len]);
                    if (dictionary.contains(search.toString())) {
                        System.out.println("(" + i + ", " + j + ", SW): " + search.toString());
                        wordsFound++;
                    }
                    len++;
                }
            }
        }

        // Northwest
        for (i = rows - 1; i >= 2; i--) {
            for (j = cols - 1; j >= 2; j--) {
                search = new StringBuilder();

                for (int k = 0; k < 2; k++ ) //initial first 2 letters
                    search.append(puzzle[i-k][j-k]);
                
                // build the word from 3 letters to 22 letters or end of grid
                len = 2;
                while (i-len >= 0 && j-len >= 0 && len <= 22) {
                    search.append(puzzle[i-len][j-len]);
                    if (dictionary.contains(search.toString())) {
                        System.out.println("(" + i + ", " + j + ", NW): " + search.toString());
                        wordsFound++;
                    }
                    len++;
                }
            }
        }

        // Northeast
        for (i = rows - 1; i >= 2; i--) {
            for (j = 0; j < cols - 2; j++) {
                search = new StringBuilder();

                for (int k = 0; k < 2; k++ ) //initial first 2 letters
                    search.append(puzzle[i-k][j+k]);
                
                // build the word from 3 letters to 22 letters or end of grid
                len = 2;
                while (i-len >= 0 && j+len < cols && len <= 22) {
                    search.append(puzzle[i-len][j+len]);
                    if (dictionary.contains(search.toString())) {
                        System.out.println("(" + i + ", " + j + ", NE): " + search.toString());
                        wordsFound++;
                    }
                    len++;
                }
            }
        }

        System.out.println(wordsFound + " words found");
    }
}
