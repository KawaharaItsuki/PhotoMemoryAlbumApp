package com.example.photomemoryalbumapp;

import android.util.Log;

import java.util.ArrayList;

//連結リスト
public class ArrayLinkedList {
    ArrayCell header;
    ArrayData data;
    public int Size;

    public ArrayLinkedList(){
        header = new ArrayCell(data);
        header.next = header;
        Size = 0;
    }

    //先頭にdataをもったセルを挿入
    public void insertFirst(ArrayData data){
        ArrayCell first = new ArrayCell(data);
        first.next = header;
        header = first;
        Size++;
    }

    /**
     * ArrayListに格納されているセルのデータをData配列に格納し，その配列を返す
     * @return　
     */
//    public Data[] all_getCellData(){
//        Cell current = null;
//        Data[] return_data = new Data[Size];
//        for(int i = 0; current.next != null; current = current.next, i++){
//            return_data[i] = current.getData();
//        }
//        return return_data;
//    }


    /**
     * 連結リストにおいて，引数に与えた日付に対応する連結リストのArrayListを返す
     * 存在する場合は，見つけたArrayListを返す
     * 存在しない場合は，-1を末尾に含んだArrayListを生成し，連結リストに入れて，返す
     * @param year
     * @param month
     * @param day
     * @return ArrayList<Integer>
     */
    public ArrayList<Integer> getArrayList(int year, int month, int day){

            ArrayCell current = header;
            try{
            for (; current.next != null; current = current.next) {
                int current_cell_year = current.getData().getYear();
                int current_cell_month = current.getData().getMonth();
                int current_cell_day = current.getData().getDay();
                if (current_cell_year == year &&
                        current_cell_month == month &&
                        current_cell_day == day) {
                    return current.getData().getArrayList();
                }
            }
        }catch(Exception e){
            ArrayList<Integer> new_list = new ArrayList<Integer>();
            ArrayData array_data = new ArrayData(new_list, year, month, day);
            insertFirst(array_data);
            return new_list;
        }
        return current.data.getArrayList();
    }

    public void clearArrayList(int indexnum){
        ArrayCell current = header;
        int size = 0;
        for(; current.next != null; size++, current = current.next){
            if(size == indexnum){
                current.data.arrayList.clear();
            }
        }
    }

    public int getSize(){
        return Size;
    }
}
