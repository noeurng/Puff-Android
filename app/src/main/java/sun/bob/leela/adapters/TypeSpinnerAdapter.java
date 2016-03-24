package sun.bob.leela.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.BitmapFactory;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import sun.bob.leela.R;
import sun.bob.leela.db.AcctType;
import sun.bob.leela.db.TypeHelper;

/**
 * Created by bob.sun on 16/3/24.
 */
public class TypeSpinnerAdapter extends ArrayAdapter implements View.OnClickListener {

    private ArrayList<AcctType> types;

    public TypeSpinnerAdapter(Context context, int resource) {
        super(context, resource);
        types = TypeHelper.getInstance(context).getAllTypes();
    }

    public TypeSpinnerAdapter(Context context, int resource, int textViewResourceId) {
        super(context, resource, textViewResourceId);
        types = TypeHelper.getInstance(context).getAllTypes();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (position == types.size()) {
            AppCompatButton button = new AppCompatButton(getContext());
            button.setText("Add New Account Type");
            button.setOnClickListener(this);
            return button;
        }
        AcctType type = this.types.get(position);
        TypeViewHolder viewHolder;

        //Drop the add button here too.
        if (convertView == null || convertView instanceof AppCompatButton) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.dropdown_category, parent, false);
            viewHolder = new TypeViewHolder();
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.image_view);
            viewHolder.textView = (TextView) convertView.findViewById(R.id.text_view);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (TypeViewHolder) convertView.getTag();
            if (viewHolder == null) {
                viewHolder = new TypeViewHolder();
            }
        }
        viewHolder.imageView = (ImageView) convertView.findViewById(R.id.image_view);
        viewHolder.textView = (TextView) convertView.findViewById(R.id.text_view);
        viewHolder.textView.setText(type.getName());
        try {
            viewHolder.imageView.setImageBitmap(BitmapFactory.decodeStream(getContext().getAssets().open(type.getIcon())));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getView(position, convertView, parent);
    }

    @Override
    public int getCount(){
        return types.size() + 1;
    }

    @Override
    public Object getItem(int position) {
        if (position == types.size()) {
            return null;
        }
        return types.get(position);
    }

    @Override
    public void onClick(View v) {
        View dialogView = View.inflate(getContext(),,null);
        AppCompatDialog dialog =
                new AlertDialog.Builder(getContext())
                .setView(dialogView)
                .setCancelable(true)
                .setTitle("Create New Account Type")
                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("Later", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .create();
        dialog.show();

    }

    class TypeViewHolder {
        public ImageView imageView;
        public TextView textView;
    }
}