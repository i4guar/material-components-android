/*
 * Copyright 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.material.catalog.transition.hero;

import io.material.catalog.R;

import android.annotation.TargetApi;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.android.material_m.appbar.AppBarLayout;
import com.google.android.material_m.floatingactionbutton.FloatingActionButton;
import com.google.android.material_m.transition.MaterialContainerTransform;
import io.material.catalog.transition.hero.MusicData.Album;
import io.material.catalog.transition.hero.MusicData.Track;

/** A Fragment that displays an album's details. */
@TargetApi(VERSION_CODES.LOLLIPOP)
public class TransitionMusicAlbumDemoFragment extends Fragment {

  public static final String TAG = "TransitionMusicAlbumDemoFragment";
  private static final String ALBUM_ID_KEY = "album_id_key";

  public static TransitionMusicAlbumDemoFragment newInstance(long albumId) {
    TransitionMusicAlbumDemoFragment fragment = new TransitionMusicAlbumDemoFragment();
    Bundle bundle = new Bundle();
    bundle.putLong(ALBUM_ID_KEY, albumId);
    fragment.setArguments(bundle);
    return fragment;
  }

  @Override
  public void onCreate(@Nullable Bundle bundle) {
    super.onCreate(bundle);
    setUpTransitions();
  }

  @Nullable
  @Override
  public View onCreateView(
      @NonNull LayoutInflater layoutInflater,
      @Nullable ViewGroup viewGroup,
      @Nullable Bundle bundle) {
    return layoutInflater.inflate(R.layout.cat_transition_music_album_fragment, viewGroup, false);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle bundle) {
    View container = view.findViewById(R.id.container);
    Toolbar toolbar = view.findViewById(R.id.toolbar);
    AppBarLayout appBarLayout = view.findViewById(R.id.app_bar_layout);
    FloatingActionButton fab = view.findViewById(R.id.fab);
    ImageView albumImage = view.findViewById(R.id.album_image);
    TextView albumTitle = view.findViewById(R.id.album_title);
    TextView albumArtist = view.findViewById(R.id.album_artist);
    RecyclerView songRecyclerView = view.findViewById(R.id.song_recycler_view);

    long albumId = getArguments().getLong(ALBUM_ID_KEY, 0L);
    Album album = MusicData.getAlbumById(albumId);

    // Set the transition name which matches the list/grid item to be transitioned from for
    // the shared element transition.
    ViewCompat.setTransitionName(container, album.title);

    appBarLayout.addOnOffsetChangedListener(
        (appBarLayout1, verticalOffset) -> {
          float verticalOffsetPercentage =
              (float) Math.abs(verticalOffset) / (float) appBarLayout1.getTotalScrollRange();
          if (verticalOffsetPercentage > 0.2F && fab.isOrWillBeShown()) {
            fab.hide();
          } else if (verticalOffsetPercentage <= 0.2F && fab.isOrWillBeHidden()) {
            fab.show();
          }
        });

    // Set up toolbar
    ViewCompat.setElevation(toolbar, 0F);
    toolbar.setNavigationOnClickListener(
        v -> getActivity().onBackPressed());

    // Set up album info area
    albumImage.setImageResource(album.cover);
    albumTitle.setText(album.title);
    albumArtist.setText(album.artist);

    // Set up track list
    songRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
    TrackAdapter adapter = new TrackAdapter();
    songRecyclerView.setAdapter(adapter);
    adapter.submitList(album.tracks);
  }

  private void setUpTransitions() {
    MaterialContainerTransform transform = new MaterialContainerTransform();
    setSharedElementEnterTransition(transform);
  }

  /** An adapter to hold an albums list of tracks. */
  class TrackAdapter extends ListAdapter<Track, TrackAdapter.TrackViewHolder> {

    TrackAdapter() {
      super(Track.DIFF_CALLBACK);
    }

    @NonNull
    @Override
    public TrackViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
      return new TrackViewHolder(
          LayoutInflater.from(viewGroup.getContext())
              .inflate(R.layout.cat_transition_track_list_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TrackViewHolder trackViewHolder, int i) {
      trackViewHolder.bind(getItem(i));
    }

    /** A ViewHolder for a single track item. */
    class TrackViewHolder extends RecyclerView.ViewHolder {

      private final ImageView playingIcon;
      private final TextView trackNumber;
      private final TextView trackTitle;
      private final TextView trackDuration;

      TrackViewHolder(View view) {
        super(view);
        playingIcon = view.findViewById(R.id.playing_icon);
        trackNumber = view.findViewById(R.id.track_number);
        trackTitle = view.findViewById(R.id.track_title);
        trackDuration = view.findViewById(R.id.track_duration);
      }

      public void bind(Track track) {
        playingIcon.setVisibility(track.playing ? View.VISIBLE : View.INVISIBLE);
        trackNumber.setText(String.valueOf(track.track));
        trackTitle.setText(track.title);
        trackDuration.setText(track.duration);
      }
    }
  }
}
