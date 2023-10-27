package com.example.Notify;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class NoticeStuAdapter extends RecyclerView.Adapter<NoticeStuAdapter.MyViewHolder> {
    Context context;
    ArrayList<NoticeItem> list;

    public interface OnItemClickListener {
        void onItemClick(NoticeItem noticeItem);
    }

    private OnItemClickListener mListener;
    public NoticeStuAdapter(Context context,ArrayList<NoticeItem> list,OnItemClickListener listener){
        this.context=context;
        this.list=list;
        mListener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.notice_demo,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoticeStuAdapter.MyViewHolder holder, int position) {
        final NoticeItem noticeItem = list.get(position);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onItemClick(noticeItem);
                }
            }
        });
        holder.noticeHead.setText(noticeItem.getNoticeHead());
        holder.noticeBody.setText(noticeItem.getNoticebody());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView noticeHead , noticeBody;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            noticeHead = itemView.findViewById(R.id.noticeHead);
            noticeBody = itemView.findViewById(R.id.noticeBody);
        }
    }
}
