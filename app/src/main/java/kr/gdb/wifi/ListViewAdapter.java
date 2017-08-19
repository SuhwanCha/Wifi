package kr.gdb.wifi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.R.attr.name;

/**
 * Created by junwon on 2017-08-17.
 */

public class ListViewAdapter extends BaseAdapter {
    ArrayList<ListViewItem> items = new ArrayList<>();
    Context context;

    public ListViewAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final Context context = viewGroup.getContext();

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list, viewGroup, false);
        }

        ListViewItem item = this.items.get(i);
        ((TextView) view.findViewById(R.id.name)).setText(item.getName());
        ((TextView) view.findViewById(R.id.tel)).setText(item.getTel());
        ((TextView) view.findViewById(R.id.addr)).setText(item.getAddr());

        return view;
    }

    public void addItem(Map<String, Object> data) {
        this.items.add(new ListViewItem(
                data.get("name").toString(),
                data.get("tel").toString(),
                data.get("addr").toString(),
                Double.valueOf(data.get("longt").toString()),
                Double.valueOf(data.get("lat").toString())
        ));
    }
}
