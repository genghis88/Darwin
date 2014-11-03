package hps.nyu.fa14;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.Random;

public class Matrix {

    public final int rows;
    public final int cols;

    public final boolean[][] values;

    public Matrix(int rCount, int cCount) {
        rows = rCount;
        cols = cCount;
        values = new boolean[rows][cols];
    }

    // TODO: Could make this more efficient by
    // only iterating over the matrix once
    public boolean satisfies(TableSum tableSum) {

        if (rows != tableSum.rows || cols != tableSum.cols) {
            throw new IndexOutOfBoundsException("Unexpected matrix size");
        }

        // Check all rows
        for (int r = 0; r < rows; r++) {
            if (rowSum(r) != tableSum.rowSums[r]) {
                //System.out.println(String.format("Row %d not satisfied", r));
                return false;
            }
        }

        // Check all columns
        for (int c = 0; c < cols; c++) {
            if (columnSum(c) != tableSum.colSums[c]) {
                //System.out.println(String.format("Column %d not satisfied", c));
                return false;
            }
        }

        return true;
    }

    public TableSum getTableSum(){
        TableSum tableSum = new TableSum(rows, cols);
        for(int r = 0; r < rows; r++){
            tableSum.rowSums[r] = rowSum(r);
        }
        for(int c = 0; c < cols; c++){
            tableSum.colSums[c] = columnSum(c);
        }
        return tableSum;
    }
    
    
    public int columnSum(int col) {
        int sum = 0;
        for (int i = 0; i < rows; i++) {
            sum += (values[i][col] ? 1 : 0);
        }
        return sum;
    }

    public int rowSum(int row) {
        int sum = 0;
        for (int i = 0; i < cols; i++) {
            sum += (values[row][i] ? 1 : 0);
        }
        return sum;
    }
    
    /**
     * Creates a deep copy of the matrix
     */
    public Matrix clone(){
        Matrix m = new Matrix(rows, cols);
        for(int r = 0; r < rows; r++){
            for(int c = 0; c < cols; c++){
                m.values[r][c] = this.values[r][c];
            }
        }
        return m;
    }
    
    @Override
    public boolean equals(Object other){
        if(this == other){
            return true; // same object
        }
        if(!(other instanceof Matrix)){
            return false; // incompatible types
        }
        
        Matrix that = (Matrix)other;
        if(this.rows != that.rows
                || this.cols != that.cols){
            return false; // different sizes
        }
        
        for(int r = 0; r < rows; r++){
            for(int c = 0; c < cols; c++){
                if(this.values[r][c] != that.values[r][c]){
                    return false; // Don't match in all points
                }
            }
        }
        return true; // otherwise all the same
    }

    @Override
    public int hashCode(){
        final int multiplier = 37; // prime
        int hash = (rows * multiplier) + cols;

        for(int r = 0; r < rows; r++){
            hash = (hash * multiplier) + rowSum(r);
        }
        return hash;
    }
    
    
    public void write(OutputStream output) throws IOException {
        // TODO: Fix so that id doesn't write the very last space
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(output));
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                bw.write(String.format("%d ", values[r][c] ? 1 : 0));
            }
        }
        bw.newLine();
    }

    public static void write(OutputStream output, List<Matrix> matrices)
            throws IOException {
        for (Matrix m : matrices) {
            m.write(output);
        }
        output.close(); // TODO: Is this an acceptable convention?
    }
    
    private static final Random RAND = new Random();
    
    public static Matrix random(int rows, int cols){
    
        Matrix m = new Matrix(rows, cols);
        for(int r = 0; r < rows; r++){
            for(int c = 0; c < cols; c++){
                m.values[r][c] = RAND.nextBoolean();
            }
        }
        return m;
    }
}
