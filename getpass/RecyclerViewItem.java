package com.tomi5548319.getpass;

class RecyclerViewItem {
    private int mID;
    private int mImageResource1;
    private String mText1;

    RecyclerViewItem(int id, int imageResource1, String text1){
        mID = id;
        mImageResource1 = imageResource1;
        mText1 = text1;
    }

    void changeText1(String text){
        mText1 = text;
    }

    int getID(){return mID;}

    int getImageResource1(){
        return mImageResource1;
    }

    String getText1(){
        return mText1;
    }

}
