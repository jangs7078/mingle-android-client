package com.example.mingle;

import android.graphics.drawable.Drawable;

public class ChattableUser {
	String comment;
	int num;
    Drawable pic;

    public ChattableUser(String comment, int num, Drawable pic) {
          super();
          this.comment = comment;
          this.num = num;
          this.pic = pic;
    }
    
    public String getComment() {
          return comment;
    }
    
    public void setComment(String comment) {
          this.comment = comment;
    }
    
    public int getNum() {
        return num;
    }
  
    public void setNum(int num) {
	  	this.num = num;
  	}
  
    public Drawable getPic() {
          return pic;
    }
    
    public void setpic(Drawable pic) {
          this.pic = pic;
    }

}
