package com.example.coko;

import java.util.Comparator;

public class MiniComparator implements Comparator<Place> {
    @Override
    public int compare(Place first, Place second){
        double firstValue=first.getDistance();
        double secondValue=second.getDistance();

        //오름차순 정렬
        if(firstValue>secondValue){
            return 1;
        }
        else if(firstValue<secondValue)
        {
            return -1;
        }
        else
            return 0;
    }
}
