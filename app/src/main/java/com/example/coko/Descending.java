package com.example.coko;

import java.util.Comparator;

public class Descending implements Comparator<Place> {
    @Override
    public int compare(Place first, Place second){
        double firstValue=first.getVisitors();
        double secondValue=second.getVisitors();

        //내림차순 정렬
        if(firstValue<secondValue){
            return 1;
        }
        else if(firstValue>secondValue)
        {
            return -1;
        }
        else
            return 0;
    }
}
