package educn.buct.cse39301p.mychatui;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gejin on 2017/7/6.
 */

public class ListViewAdapter extends BaseAdapter {
    private static final String TAG = "ListViewAdapter";
    private Context context;
    private List<Message> datas = new ArrayList<>();    private ViewHolder viewHolder;

    public void addDataToAdapter(Message e) {
        datas.add(e);
    }

    public ListViewAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.listview_item, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        String left = datas.get(position).getLeftMsg();
        String right = datas.get(position).getRightMsg();
        if (left == null) {
            viewHolder.right_Message.setText(right);
            viewHolder.right.setVisibility(View.VISIBLE);
            viewHolder.left.setVisibility(View.INVISIBLE);
        }
        if (right == null) {
            viewHolder.left_Message.setText(left);
            viewHolder.left.setVisibility(View.VISIBLE);
            viewHolder.right.setVisibility(View.INVISIBLE);
        }
        return convertView;
    }

    public class ViewHolder {
        public View rootView;
        public TextView left_Message;
        public LinearLayout left;
        public TextView right_Message;
        public LinearLayout right;

        public ViewHolder(View rootView) {

            this.rootView = rootView;
            this.left_Message = rootView.findViewById(R.id.left_msg);
            this.left = rootView.findViewById(R.id.left);
            this.right_Message = rootView.findViewById(R.id.right_msg);
            this.right = rootView.findViewById(R.id.right);
            rootView.findViewById(R.id.head_left).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent ibtnHeadLeft = new Intent(context, LeftHeadActivity.class);
                    context.startActivity(ibtnHeadLeft);
                }
            });
            rootView.findViewById(R.id.head_right).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent ibtnHeadRight = new Intent(context, RightHeadActivity.class);
                    context.startActivity(ibtnHeadRight);
                }
            });


        }
    }
}
