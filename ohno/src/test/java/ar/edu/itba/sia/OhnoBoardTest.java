package ar.edu.itba.sia;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import ar.edu.itba.sia.Ohn0.Board;
import ar.edu.itba.sia.Ohn0.FileManager;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Paths;

/**
 * Unit test for simple App.
 */
public class OhnoBoardTest
{
    FileManager fm = new FileManager();

    @Test
    public void isChangeValidBluesOutOfDistance() throws IOException{
        Board board = fm.readStateFromFile(Paths.get("src","test","testBoards","MoreBluesOutOfDistance"));
        //System.out.println(board);
        assertFalse(board.isChangeValid(0, 2) );

    }

    @Test
    public void isChangeValidBluesWithBlanksInMiddle() throws IOException{
        Board board = fm.readStateFromFile(Paths.get("src","test","testBoards","MoreBluesWithBlanksInMiddle"));
        //System.out.println(board);
        assertTrue(board.isChangeValid(0, 1) );

    }

    @Test
    public void isChangeValidWithValidPosition() throws IOException{
        Board board = fm.readStateFromFile(Paths.get("src","test","testBoards","GoalBoard"));
        //System.out.println(board);
        assertTrue(board.isChangeValid(0, 2) );
        assertTrue(board.isChangeValid(2, 2) );
        assertTrue(board.isChangeValid(3, 3) );



    }
}
