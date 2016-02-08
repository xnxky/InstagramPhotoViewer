package com.xxy.instagramclient;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import com.xxy.instagramclient.adapter.CommentsViewAdapter;
import com.xxy.instagramclient.object.Comment;

import java.util.List;

/**
 * Created by xiangyang_xiao on 2/7/16.
 */
public class CommentDialogFragment extends DialogFragment {

  private List<Comment> comments;

  public CommentDialogFragment() {
  }

  public static CommentDialogFragment newInstance(
      List<Comment> comments
  ) {
    CommentDialogFragment dialogFragment = new CommentDialogFragment();
    dialogFragment.setComments(comments);
    return dialogFragment;
  }

  public void setComments(List<Comment> comments) {
    this.comments = comments;
  }

  @Override
  public Dialog onCreateDialog(Bundle savedInstance) {
    LayoutInflater inflater = LayoutInflater.from(getActivity());
    View view = inflater.inflate(R.layout.comments_view, null);
    ListView lvComments = (ListView) view.findViewById(R.id.lvComments);
    CommentsViewAdapter adapter = new CommentsViewAdapter(getContext(), comments);
    lvComments.setAdapter(adapter);
    return new AlertDialog.Builder(getActivity()).setView(view).create();
  }

}
