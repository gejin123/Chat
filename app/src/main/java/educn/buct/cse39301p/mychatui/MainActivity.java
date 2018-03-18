package educn.buct.cse39301p.mychatui;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.hyphenate.EMCallBack;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCmdMessageBody;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;

import java.util.List;

public class MainActivity extends AppCompatActivity implements EMMessageListener {

    private static final String TAG = "MainActivity";
    private ListViewAdapter adapter = null;
    private ListView listview;
    private EditText msg_edit;
    private ImageView tbMore;
    private LinearLayout layoutPlus;
    private ImageView chat_setting;
    private ImageView quit_btn;
    private boolean isRotate = false;
    private String username;

    private EMConversation conversation;
    private EMMessageListener messageListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        if (username != null) {
            if (username.trim().equals("zgj123")) {
                username = "ylzf0000";
            } else if (username.trim().equals("ylzf0000")) {//880210
                username = "zgj123";
            }
            ((TextView) findViewById(R.id.username)).setText(username);
        }
        messageListener = this;
        initView();
        adapter = new ListViewAdapter(this);
        listview.setAdapter(adapter);
        initConversation();
    }

    private void initView() {
        chat_setting = (ImageView) findViewById(R.id.connectInfo_btn);
        chat_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ChatSettingIntent = new Intent(MainActivity.this, ChatSetting.class);
                startActivity(ChatSettingIntent);
            }
        });
        quit_btn = (ImageView) findViewById(R.id.quit_btn);
        quit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setLogout();
                Intent QuitIntent = new Intent(MainActivity.this, QuitChat.class);
                startActivity(QuitIntent);
                finish();
            }

        });
        listview = (ListView) findViewById(R.id.list_view);
        tbMore = (ImageView) findViewById(R.id.tb_more);
        tbMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isRotate)
                    tbMore.setRotation(0f);
                else
                    tbMore.setRotation(45f);
                isRotate = !isRotate;
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                switch (layoutPlus.getVisibility()) {
                    case View.VISIBLE:
                        layoutPlus.setVisibility(View.GONE);
                        break;
                    case View.GONE:
                        layoutPlus.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });
        msg_edit = (EditText) findViewById(R.id.msg_edit);
        msg_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layoutPlus.setVisibility(View.GONE);
                tbMore.setRotation(0f);
            }
        });
        msg_edit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEND
                        || i == EditorInfo.IME_ACTION_DONE
                        || (keyEvent != null && KeyEvent.KEYCODE_ENTER == keyEvent.getKeyCode()
                        && KeyEvent.ACTION_DOWN == keyEvent.getAction())) {
                    sendMessage();
                }
                return true;
            }
        });
        layoutPlus = (LinearLayout) findViewById(R.id.layout_plus);

    }

    //初始化会话对象
    private void initConversation() {
        conversation = EMClient.getInstance().chatManager().getConversation(username, null, true);
        if (conversation != null) {
            conversation.markAllMessagesAsRead();
            int count = conversation.getAllMessages().size();
            if (count < conversation.getAllMsgCount() && count < 20) {
                String msgId = conversation.getAllMessages().get(0).getMsgId();
                conversation.loadMoreMsgFromDB(msgId, 20 - count);
            }
            for (EMMessage message : conversation.getAllMessages()) {
                String content = ((EMTextMessageBody) message.getBody()).getMessage();
                adapter.addDataToAdapter(new Message(content, null));
                adapter.notifyDataSetChanged();
                listview.smoothScrollToPosition(listview.getCount() - 1);
            }
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    EMMessage message = (EMMessage) msg.obj;
                    EMTextMessageBody body = (EMTextMessageBody) message.getBody();
                    adapter.addDataToAdapter(new Message(body.getMessage(), null));
                    adapter.notifyDataSetChanged();
                    listview.smoothScrollToPosition(listview.getCount() - 1);
                    msg_edit.setText("");
                    break;
            }
        }
    };

    private void sendMessage() {
        String content = msg_edit.getText().toString();
        EMMessage message = EMMessage.createTxtSendMessage(content, username);
        adapter.addDataToAdapter(new Message(null, content));
        adapter.notifyDataSetChanged();
        listview.smoothScrollToPosition(listview.getCount() - 1);
        msg_edit.setText("");
        EMClient.getInstance().chatManager().sendMessage(message);
    }

    @Override
    protected void onResume() {
        super.onResume();
        EMClient.getInstance().chatManager().addMessageListener(messageListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EMClient.getInstance().chatManager().removeMessageListener(messageListener);
    }

    @Override
    public void onMessageReceived(List<EMMessage> messages) {
        for (EMMessage message : messages) {
            if (message.getFrom().equals(username)) {
                conversation.markMessageAsRead(message.getMsgId());
                android.os.Message msg = handler.obtainMessage();
                msg.what = 0;
                msg.obj = message;
                handler.sendMessage(msg);
            }
        }
    }

    @Override
    public void onCmdMessageReceived(List<EMMessage> list) {
        for (int i = 0; i < list.size(); i++) {
            EMMessage cmdMessage = list.get(i);
            EMCmdMessageBody body = (EMCmdMessageBody) cmdMessage.getBody();
        }
    }

    @Override
    public void onMessageChanged(EMMessage emMessage, Object o) {

    }

    private void setLogout() {
        EMClient.getInstance().logout(false, new EMCallBack() {
            @Override
            public void onSuccess() {
                finish();
            }

            @Override
            public void onError(int i, String s) {

            }

            @Override
            public void onProgress(int i, String s) {

            }
        });
    }

    @Override
    public void onMessageRead(List<EMMessage> list) {

    }

    @Override
    public void onMessageDelivered(List<EMMessage> list) {

    }
}
