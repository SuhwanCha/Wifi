package kr.gdb.wifi;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

public class info extends AppCompatActivity {
    LinearLayout ll;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        ll = (LinearLayout)findViewById(R.id.linear);
        LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//        TextView[] tv=new TextView(this);
//        tv.setLayoutParams(lparams);
//        tv.setText("test");
//        this.ll.addView(tv);

//        it.putExtra("name", (String)temp.get("name"));
//        it.putExtra("tel",(String)temp.get("tel"));
//        it.putExtra("addr", (String)temp.get("addr"));
//        it.putExtra("ssid",(String)temp.get("ssid"));
        Intent it = getIntent();
        TextView[] tv = new TextView[4];
        for(int i=0;i<3;i++){
            tv[i] = new TextView(this);
            tv[i].setLayoutParams(lparams);
            tv[i].setTextSize(20);
//            tv[i].setGravity(Gravity.CENTER);
            switch (i){
                case 0:
                    tv[i].setText("설치장소 명 : "+it.getStringExtra("name"));
                    break;
                case 1:
                    tv[i].setText("전화번호 : "+it.getStringExtra("tel"));
                    break;
                case 2:
                    tv[i].setText("주소 : " + it.getStringExtra("addr"));
                    break;

            }
            this.ll.addView(tv[i]);
        }


    }
}
