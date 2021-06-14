package com.example.thumboard;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import java.lang.reflect.Field;

public class Keyboard extends ConstraintLayout implements View.OnTouchListener {

    private float keys_horz_width, keys_horz_height, keys_vert_width, keys_vert_height, x_start, y_start, fixed_y;

    private final String[][] horizontal_keyboard = {{"N", "M", "dot", "comma", "enter", "space"}, {"H", "J", "K", "L", "question", "space"}, {"Y", "U", "I", "O", "P", "space"}};
    private final String[][] vertical_keyboard = {{"T", "G", "B"}, {"R", "F", "V"}, {"E", "D", "C"}, {"W", "S", "X"}, {"Q", "A", "Z"}};
    private final String key_words[] = {"dot", "comma", "enter", "space", "question"};
    private  int themeColor;


    public Keyboard(Context context) {
        this(context, null, 0);
    }

    public Keyboard(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Keyboard(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.activity_keyboard, this, true);
        findViewById(R.id.touchpad).setOnTouchListener(this);
        Log.i("horz: ", this.horizontal_keyboard.length + ", " + this.horizontal_keyboard[0].length);
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(R.attr.colorPrimary, typedValue, true);
        this.themeColor = typedValue.data;
    }

    float scale_factor = (float) 0.8;

    private float getScaledX(float x_diff, float y_diff, float init_x){
        if(y_diff >= 0){
            if(x_diff > 0){
                return (float) ((this.keys_horz_width/2 - (this.keys_horz_width/2) * (Math.pow(x_diff/4,2)/init_x)) * scale_factor);
            }else{
                return  (this.keys_horz_width/2) * ( 1  + (Math.abs(x_diff)/ init_x)) * scale_factor;
            }
        }else{
            if(x_diff > 0){
                return (float) ((this.keys_vert_width - (this.keys_vert_width) * (Math.pow(x_diff/4,2)/init_x)) * scale_factor);
            }else{
                return (this.keys_vert_width)* scale_factor;
            }
        }
    }

    private float getScaledY(float x_diff, float y_diff, float init_y){
        if(y_diff >= 0){
            return (float) (this.keys_horz_height * (Math.pow(y_diff/4, 2)/init_y) * scale_factor);
        }else{
            return (float) (this.keys_vert_height * Math.abs(Math.pow(y_diff/4,2)/init_y) * scale_factor);
        }
    }


    private String getKey(float x_diff, float y_diff, float init_x, float init_y){
        int row,col = 0;
        float scaled_x = this.getScaledX(x_diff, y_diff, init_x);
        float scaled_y = this.getScaledY(x_diff, y_diff, init_y);
        if(y_diff >= 0) {
            float value = (float) Math.floor(scaled_y % ( this.keys_horz_height / 3));
            row =  (int) (scaled_y / ( this.keys_horz_height / 3));
            if(row >= this.horizontal_keyboard.length){
                row = this.horizontal_keyboard.length - 1;
            }else if(row < 0){
                row = 0;
            }else if(value != 0 && row < this.horizontal_keyboard.length - 2){
                row += 1;
            }
            float x_idx = Math.round(scaled_x % (this.keys_horz_width/ 6));
            col = (int) (scaled_x / (this.keys_horz_width/6));
            if(col >= this.horizontal_keyboard[0].length){
                col = this.horizontal_keyboard[0].length - 1;
            }else if(col < 0){
                col = 0;
            }else if (x_idx != 0 && col <= this.horizontal_keyboard[0].length - 2){
                col += 1;
            }
        }else{
            float value = (float) Math.floor(scaled_y % ( this.keys_vert_height / 5));
            row =  (int) (scaled_y / ( this.keys_horz_height / 5));
            if(row >= this.vertical_keyboard.length){
                row = this.vertical_keyboard.length - 1;
            }else if(row < 0){
                row = 0;
            }else if(value != 0 && row < this.vertical_keyboard.length - 2){
                row += 1;
            }

            float x_idx = Math.round(scaled_x % (this.keys_horz_width/ 3));
            col = (int) (scaled_x / (this.keys_horz_width/3));
            if(col >= this.vertical_keyboard[0].length){
                col = this.vertical_keyboard[0].length - 1;
            }else if(col < 0){
                col = 0;
            }else if(x_idx != 0 && col < this.vertical_keyboard[0].length - 2){
                col += 1;
            }
        }

//        Log.i("Row, Col:  ",  row + " , " + col);

        if(y_diff >= 0){
//            Log.i("Horizontal:  ",  row + " , " + col);
            return horizontal_keyboard[row][col];
//            return 'c';
        }else{
            return vertical_keyboard[row][col];
//            return 'c';
        }
    }

    public int getResId(String resName, Class<?> c) {

        try {
            Field idField = c.getDeclaredField(resName);
            return idField.getInt(idField);
        } catch (Exception e) {
//            e.printStackTrace();
            Log.e("MyTag", "Failure to get drawable id.", e);
            return -1;
        }
    }

    String selected = "";
    private void selectKey(String key){

        if(!this.selected.isEmpty() && !this.selected.equals(key)) {
//            Log.i("MyTag", this.selected);
            findViewById(this.getResId( this.selected, R.id.class)).setBackgroundColor(themeColor);
        }
        if(!key.isEmpty()) {
            int id = this.getResId(key, R.id.class);
            findViewById(id).setBackgroundColor(Color.RED);
            this.selected = key;
        }
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {

        float xpos = event.getX();
        float ypos = event.getY();
        float diff_x = this.x_start - xpos;
        float diff_y = this.y_start - ypos;
        TextView t = (TextView)findViewById(R.id.keyWord);
        TextView fullText =  (TextView) findViewById(R.id.fullText2);

        if(event.getAction() == MotionEvent.ACTION_DOWN){
            this.keys_horz_width = findViewById(R.id.keysHorizontal).getWidth();
            this.keys_horz_height = findViewById(R.id.keysHorizontal).getHeight();
            this.keys_vert_width = findViewById(R.id.keysVerticle).getWidth();
            this.keys_vert_height = findViewById(R.id.keysVerticle).getHeight();
            this.fixed_y = findViewById(R.id.touchpad).getHeight()/4;
            this.x_start = xpos;
            this.y_start = ypos;

//              Log.i("Width", this.keys_horz_height + " - " + this.keys_horz_width);
        }else if(event.getAction() == MotionEvent.ACTION_MOVE){
            if(xpos >= 0 && ypos >= 0 ){
//                Log.d("DEBUG", "On touch (move)" + String.valueOf(this.getScaledX(diff_x, diff_y, this.x_start)) +" , " + this.getScaledY(diff_x, diff_y, this.y_start));
//                Log.i("Key : ", " " + this.getKey(diff_x, diff_y, this.x_start, this.y_start));
                this.selectKey(this.getKey(diff_x, diff_y, this.x_start, this.y_start));
            }
        }else if(event.getAction()== MotionEvent.ACTION_UP){
               if(!this.selected.isEmpty()){

                  t.setText(this.selected);
                  switch (this.selected){
                      case "comma":
                          fullText.setText(fullText.getText() + ",");
                          break;
                      case "dot":
                          fullText.setText(fullText.getText() + ".");
                          break;
                      case "space":
                          fullText.setText(fullText.getText() + " ");
                          break;
                      case "question":
                          fullText.setText(fullText.getText() + "?");
                          break;
                      case "enter":
                          fullText.setText(fullText.getText() + "\n");
                          break;
                      default:
                          fullText.setText(fullText.getText() + this.selected);
                  }

               }
        }
//        invalidate();
        return true;
    }
}