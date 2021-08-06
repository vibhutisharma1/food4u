package com.example.food4u;

public class Cuisine {
    private String cuisineName;

    public String getCuisineName() {
        return cuisineName;
    }

    public int getDrawableName(String name) {
      if(name.equals("Indian")){
          return R.drawable.india;
      }else if(name.equals("Chinese")){
          return R.drawable.china;
      }else if(name.equals("Italian")){
          return R.drawable.itally;
      }else if(name.equals("Middle Eastern")){
          return R.drawable.middle_east;
      }else if(name.equals("Mexican")){
          return R.drawable.mexican_img;
      }else if(name.equals("Asian")) {
          return R.drawable.asia;
      }else if(name.equals("Japanese")){
          return R.drawable.japan;
      }
          return R.drawable.america;

    }

    public Cuisine(String name){
        this.cuisineName = name;
    }
}
