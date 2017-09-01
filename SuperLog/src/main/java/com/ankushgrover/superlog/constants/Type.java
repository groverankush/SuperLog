package com.ankushgrover.superlog.constants;

/**
 * Created by Ankush Grover(ankush.dev2@gmail.com) on 1/9/17.
 */

public class Type {

    private int color;
    private String text;

   public Type(int color, String text){
       this.color = color;
       this.text = text;
   }

    public void setColor(int color) {
        this.color = color;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getColor() {
        return color;
    }

    public String getText() {
        return text;
    }
}
