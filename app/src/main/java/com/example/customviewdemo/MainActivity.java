package com.example.customviewdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.customviewdemo.animation.Rotate3dAnimation;
import com.example.customviewdemo.view.HorizontalScrollViewEx;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

 //   private HorizontalScrollViewEx mListContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView(){
        /*mListContainer = findViewById(R.id.horizontal_scroll_view);
        int screenWidth = getWindowManager().getDefaultDisplay().getWidth();
        int screenHeight = getWindowManager().getDefaultDisplay().getHeight();
        for(int i=0; i<3; i++){
            ViewGroup viewGroup = (ViewGroup) getLayoutInflater().inflate(R.layout.item_list_view, mListContainer, false);
            viewGroup.getLayoutParams().width = screenWidth;
            TextView tvTitle = viewGroup.findViewById(R.id.tv_title);
            tvTitle.setText("page : " + i);
            createList(viewGroup);
            mListContainer.addView(viewGroup);
        }*/

        Button btView = findViewById(R.id.bt_view);
        btView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Rotate3dAnimation animation = new Rotate3dAnimation(0, 270, 0.5f, 0.5f, 0.5f, false);
                animation.setDuration(3000);
                btView.startAnimation(animation);
            }
        });
    }

    private void createList(ViewGroup viewGroup){
        /*ListView recyclerView = viewGroup.findViewById(R.id.recyclerView);
        ArrayList<String> datas = new ArrayList<>();
        for(int i=0; i<50; i++){
            datas.add("name : " + i);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, datas);
        recyclerView.setAdapter(adapter);*/
    }
}
