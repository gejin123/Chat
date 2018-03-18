package educn.buct.cse39301p.mychatui;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class ChatSetting extends AppCompatActivity {
    private ImageView imageViewQuit;
    private ImageView imageViewchangebutton;
    private int flag = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_setting);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        imageViewQuit = (ImageView) findViewById(R.id.quit_btn);
        imageViewQuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentQuit = new Intent(ChatSetting.this, MainActivity.class);
                startActivity(intentQuit);
            }
        });
        initView();


    }

    public void initView() {
        imageViewchangebutton = (ImageView) findViewById(R.id.change_button);
        imageViewchangebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flag == 1) {
                    imageViewchangebutton.setImageResource(R.drawable.kaiguankai);
                    flag = 0;
                } else {
                    imageViewchangebutton.setImageResource(R.drawable.kaiguanguan);
                    flag = 1;
                }
            }
        });
    }

}
