package educn.buct.cse39301p.mychatui;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class RightHeadActivity extends AppCompatActivity {

    private ImageView imageViewQuit;
    private LinearLayout maxcodeLayout;
    private Intent maxCodeintent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_right_head);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.hide();
        }
        initView();

    }

    public void initView(){
        imageViewQuit=(ImageView)findViewById(R.id.quit_btn);
        imageViewQuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        maxcodeLayout=(LinearLayout)findViewById(R.id.max_codelayout);
        maxcodeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent maxCodeintent=new Intent(RightHeadActivity.this,MaxCode.class);
                startActivity(maxCodeintent);
                finish();

            }
        });
    }
}
