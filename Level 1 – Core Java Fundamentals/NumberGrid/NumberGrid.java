// Task 1: Display a 3×3 number grid using arrays and check if a number exists

import java.util.*;

class NumberGrid {
    private final int[][] grid;
    public NumberGrid() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the size of the Grid: ");
        int n = sc.nextInt();
        this.grid = new int[n][n];

        System.out.println("Enter the digits in the grid: ");
        for(int i = 0; i < n; i++){
            for(int j = 0; j < n; j++){
                grid[i][j] = sc.nextInt();
            }
        }
    }

    public void displayGrid() {
        System.out.println("=== Grid ===");
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                System.out.print(grid[i][j] + "\t");
            }
            System.out.println();
        }
        System.out.println();
    }

    public void searchNumber(int number) {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j] == number) {
                    System.out.println("Number " + number + " found ");
                    return;
                }
            }
        }
        System.out.println("Number " + number + " not found in the grid.");
    }

    public int[] getPosition(int number) {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j] == number) {
                    return new int[]{i, j};
                }
            }
        }
        return null;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        NumberGrid gridObj = null;

        while(true) {
            System.out.println("\n=== Number Grid Menu ===");
            System.out.println("1. Make the grid");
            System.out.println("2. Display Grid");
            System.out.println("3. Search digit in the grid");
            System.out.println("4. Get the position");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");

            int c = sc.nextInt();

            switch(c){
                case 1:
                    gridObj = new NumberGrid();
                    System.out.println("Grid created successfully!");
                    break;

                case 2:
                    if(gridObj == null) {
                        System.out.println("Please create the grid first!");
                    } else {
                        gridObj.displayGrid();
                    }
                    break;

                case 3:
                    if(gridObj == null) {
                        System.out.println("Please create the grid first!");
                    } else {
                        System.out.print("Enter the number you want to search: ");
                        int num = sc.nextInt();
                        gridObj.searchNumber(num);
                    }
                    break;

                case 4:
                    if(gridObj == null) {
                        System.out.println("Please create the grid first!");
                    } else {
                        System.out.print("Enter the number you want the position of: ");
                        int num = sc.nextInt();
                        int[] pos = gridObj.getPosition(num);
                        if(pos != null) {
                            System.out.println("Position: Row " + (pos[0] + 1) + ", Column " + (pos[1] + 1));
                        } else {
                            System.out.println("Number not found in the grid.");
                        }
                    }
                    break;

                case 5:
                    System.out.println("Exiting...");
                    sc.close();
                    return;

                default:
                    System.out.println("Invalid choice! Please try again.");
            }
        }
    }
}