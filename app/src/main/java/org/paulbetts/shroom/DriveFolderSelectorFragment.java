package org.paulbetts.shroom;

import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.paulbetts.shroom.core.ModelViewHolder;
import org.paulbetts.shroom.core.OAuthTokenMixin;
import org.paulbetts.shroom.core.ReactiveArrayList;
import org.paulbetts.shroom.core.ReactiveListAdapter;
import org.paulbetts.shroom.core.RxDaggerFragment;
import org.paulbetts.shroom.gdrive.DriveItem;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class DriveFolderSelectorFragment extends RxDaggerFragment {
    private ReactiveArrayList<DriveItem> rootFolders = new ReactiveArrayList<>();

    @Inject
    OAuthTokenMixin oauthToken;

    public DriveFolderSelectorFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View ret = inflater.inflate(R.layout.fragment_drive_folder_selector, container, false);
        RecyclerView list = (RecyclerView)ret.findViewById(R.id.folder_list);

        list.setAdapter(new ReactiveListAdapter(rootFolders) {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
                View view = inflater.inflate(R.layout.tile_drive_folder, viewGroup, false);

                ModelViewHolder<DriveItem> holder = new ModelViewHolder<DriveItem>(view) {
                    @InjectView(R.id.driveItem) TextView driveItem;

                    @Override
                    public void bindModel(DriveItem item) {
                        driveItem.setText(item.getTitle());
                    }
                };

                ButterKnife.inject(holder, view);
                return holder;
            }
        });

        list.setHasFixedSize(true);
        return ret;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
