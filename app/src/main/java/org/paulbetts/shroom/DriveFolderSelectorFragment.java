package org.paulbetts.shroom;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.paulbetts.shroom.cloudapi.FolderMetadata;
import org.paulbetts.shroom.core.ModelViewHolder;
import org.paulbetts.shroom.core.ReactiveArrayList;
import org.paulbetts.shroom.core.ReactiveListAdapter;
import org.paulbetts.shroom.core.RxDaggerActivity;
import org.paulbetts.shroom.core.RxDaggerFragment;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import rx.android.schedulers.AndroidSchedulers;

public class DriveFolderSelectorFragment extends RxDaggerFragment {
    private ReactiveArrayList<FolderMetadata> rootFolders = new ReactiveArrayList<>();

    @Inject
    CategoryScanners scanners;

    @Inject
    RxDaggerActivity hostActivity;

    public DriveFolderSelectorFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View ret = inflater.inflate(R.layout.fragment_drive_folder_selector, container, false);
        RecyclerView list = (RecyclerView)ret.findViewById(R.id.folder_list);

        scanners.performFullScan()
                //.timeout(5, TimeUnit.SECONDS).retry(2)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(x -> {
                    /*
                    for(PlayableRom di: x) {
                        rootFolders.add(di);
                    }
                    */
                });

        list.setAdapter(new ReactiveListAdapter(rootFolders) {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
                View view = inflater.inflate(R.layout.tile_drive_folder, viewGroup, false);
                return new DriveFolderTileViewHolder(view);
            }
        });

        list.setLayoutManager(new LinearLayoutManager(hostActivity));
        list.setItemAnimator(new DefaultItemAnimator());
        list.setHasFixedSize(true);
        return ret;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {  super.onDetach(); }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }


    class DriveFolderTileViewHolder extends ModelViewHolder<FolderMetadata> {
        @InjectView(R.id.driveItem) TextView driveItem;

        public DriveFolderTileViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }

        @Override
        public void bindModel(FolderMetadata item) {
            driveItem.setText(item.getName());
        }
    }
}
