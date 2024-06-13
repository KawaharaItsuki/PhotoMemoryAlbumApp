package com.example.photomemoryalbumapp;

import android.app.Application;
import android.util.Log;

import java.util.ArrayList;

public class LinkedList {
    //フィールドの作成
    Cell header;
    Data data;
    public int ListSize;
    public int matchCount = 0;
    //public ArrayList<Integer> match_array = new ArrayList<Integer>();

    public LinkedList(){
        header = new Cell(data);
        header.next = header;
        ListSize= 0;
        //マッチしたarraylistを初期化
        //match_array.clear();
    }

    //先頭にdataをもったセルを挿入
    public void insertFirst(Data data){
        Cell first = new Cell(data);
        first.next = header;
        header = first;
        ListSize++;
    }

    public boolean deleteFirst(){
        if(header != null){
            Cell p = header;
            header = p.next;
            ListSize--;
            return true;
        }
        return false;
    }

    public Cell getfirstCell(){
        return header;
    }

    public Data getLinkedeListData(int year, int month, int day){
        return getCell(year, month, day).getData();
    }

    public Cell getCell(int year, int month, int day){
        Cell current = header;
        for(; current.next != null; current = current.next){
            int cell_current_year = current.data.getYear();
            int cell_current_month = current.data.getMonth();
            int cell_cureent_day = current.data.getDay();
            if(year == cell_current_year &&
                    month == cell_current_month &&
                    day == cell_cureent_day){

                return current;
            }
        }
        return null;
    }

    /**
     * ArrayListに格納されているセルのデータをData配列に格納し，その配列を返す
     * @return　
     */
    public Data[] get_AllCellData(){
        Cell current = header;
        Data[] return_data = new Data[ListSize];
        for(int i = 0; current.next != null; current = current.next, i++){
            if(i < ListSize) {
                return_data[i] = current.getData();
            }else{
                break;
            }
        }
        return return_data;
    }

    public Data getNextData(int year, int month, int day){
        Cell current = getCell(year, month, day).next;
        for(; current.next != null; current = current.next){
            int cell_current_year = current.getData().getYear();
            int cell_current_month = current.getData().getMonth();
            int cell_cureent_day = current.getData().getDay();
            if(year == cell_current_year &&
                    month == cell_current_month &&
                    day == cell_cureent_day){
                data = current.data;
                return data;
            }
        }
        return null;
    }

    public int MatchCountClear(){
        return matchCount = 0;
    }

    public int matchCount(int year, int month, int day, ArrayLinkedList matchLinkedList){
        MatchCountClear();
        ArrayList<Integer> match_array = matchLinkedList.getArrayList(year, month, day);
        Log.i("matchArrayList", String.valueOf(matchLinkedList.getSize()));

        int count = 0;
        int loopcount = 0;
        int matchindex = 1;
        Cell current = header;
        for(; current.next != null; current = current.next){
            int cell_current_year = current.data.getYear();
            int cell_current_month = current.data.getMonth();
            int cell_current_day = current.data.getDay();

            //ログ
            Log.i("getYear", String.valueOf(cell_current_year));
            Log.i("getMonth", String.valueOf(cell_current_month));
            Log.i("getDay", String.valueOf(cell_current_day));

            if(year == cell_current_year &&
                    month == cell_current_month &&
                    day == cell_current_day){
                matchCount++;
                count++;
                Log.i("matchcount", String.valueOf(matchCount));
                Log.i("count", String.valueOf(count));

                //matchindexがmatch_arrayにすでに存在するかどうか
                //存在しないとき，追加
                if(!(match_array.contains(matchindex))){
                    match_array.add(matchindex);
                    Log.i("要素",String.valueOf(match_array.size()));
                }

//                if(match_array.size() == 0){
//                    match_array.add(matchindex);
//                }else {
//                    for (int index = 0; index < match_array.size(); index++) {
//                        if (match_array.get(index) != matchindex) {
//                            Log.i("要素",String.valueOf(match_array.get(index)));
//                            match_array.add(matchindex);
//                        }else{
//
//                        }
//                    }
//                }
            }
            matchindex++;
            loopcount++;
            Log.i("LoopCount", String.valueOf(loopcount));
        }

        Log.i("returncount", String.valueOf(matchCount));
        return count;

    }

    //public ArrayList getMatchArray(){
    //    return match_array;
    //}

    public int getMatchCount(){
        return matchCount;
    }

    public int getSize(){
        return ListSize;
    }

    public Data getIndexData(int indexnum){
        Cell current = header;
        int size = 1;
        for(; current.next != null; size++, current = current.next){
            if(size == indexnum){
                data = current.data;
                Log.i("CurrentDataLandmarkname", data.getLandmark_name());
                Log.i("MatchIndex<LinkedList>", String.valueOf(size));
                return data;
            }
        }
        return null;
    }

    public Cell getIndexCell(int indexnum){
        Cell current = header;
        int size = 0;
        for(; current.next != null; size++, current = current.next){
            if(size == indexnum){
                return current;
            }
        }
        return null;
    }

    public Cell getnextCell(int year, int month, int day, Cell current){
        Cell now = current;
        for(; now.next != null; now = now.next){
            int cell_current_year = now.getData().getYear();
            int cell_current_month = now.getData().getMonth();
            int cell_cureent_day = now.getData().getDay();
            if(year == cell_current_year &&
                    month == cell_current_month &&
                    day == cell_cureent_day){

                return now;
            }
        }
        return null;
    }

}
