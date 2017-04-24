package com.mygdx.tetris;

/**
 * @author Nkosingiphile
 */
public class Tetronimoes {
    public static int[][][] tetronimoes = {
        {
            //square
            {1,1},
            {1,1}
        },
        {
            //line
            {2},
            {2},
            {2},
            {2}
        },
        {
            //rightL
            {3,3},
            {3,0},
            {3,0}
        },
        {
            //leftL
            {4,4},
            {0,4},
            {0,4}
        },
        {
            //smallT
            {5,5,5},
            {0,5,0}
        },
        {
            //rightZ
            {6,6,0},
            {0,6,6}
        },
        {
            //leftZ
            {0,7,7},
            {7,7,0}
        }
            
    };

}
