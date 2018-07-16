package com.udacity.baking_app.ui;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;
import com.udacity.baking_app.R;
import com.udacity.baking_app.model.Step;
import com.udacity.baking_app.utility.NetworkConnectionUtility;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeStepDetailFragment extends Fragment {
    private static final String className = RecipeStepDetailFragment.class.toString();

    @BindView(R.id.simpleExoPlayerView) SimpleExoPlayerView simpleExoPlayerView;
    @BindView(R.id.imageview_step_thumbnail) ImageView thumbnailImageView;
    @BindView(R.id.textview_step_instructions) TextView stepInstructionsTextView;

    private SimpleExoPlayer exoPlayer;

    public RecipeStepDetailFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe_step_detail, container, false);
        ButterKnife.bind(this, view);

        final Step step = (Step) getArguments().getParcelable("step");

        if (step != null) {
            loadVideo(step);
            loadThumbnail(step);
            loadInstructions(step);
        }
        else {
            Toast.makeText(getContext(), "Step could not be found, step not loaded.", Toast.LENGTH_SHORT).show();
        }

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }

    private void loadVideo(Step step) {
        if (step.getVideoURL() != null &&
            !"".equals(step.getVideoURL())) {
            if (NetworkConnectionUtility.haveActiveNetworkConnection(getConnectivityManager())) {
                beginPlayer(createVideoURI(step.getVideoURL()));
            }

        }
    }

    private void loadThumbnail(Step step) {
        if (null != step.getThumbnailURL() &&
            !"".equals(step.getThumbnailURL())) {
            if (NetworkConnectionUtility.haveActiveNetworkConnection(getConnectivityManager())) {
                Picasso.with(getContext())
                        .load(step.getThumbnailURL())
                        .into(thumbnailImageView);
            }
        }
    }

    private void loadInstructions(Step step) {
        stepInstructionsTextView.setText(step.getDescription());
    }

    private Uri createVideoURI (String videoURL) {
        return Uri.parse(videoURL);
    }

    private ConnectivityManager getConnectivityManager() {
        return (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    private void beginPlayer(Uri videoUri) {
        if (simpleExoPlayerView == null) {
            TrackSelector trackSelector = new DefaultTrackSelector();
            exoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector);
            simpleExoPlayerView.setPlayer(exoPlayer);
            String userAgent = Util.getUserAgent(getContext(), "StepVideo");
            MediaSource mediaSource = new ExtractorMediaSource(videoUri,
                                                               new DefaultDataSourceFactory(getContext(),
                                                                                            userAgent),
                                                               new DefaultExtractorsFactory(),
                                                               null,
                                                               null);
            exoPlayer.prepare(mediaSource);
            exoPlayer.setPlayWhenReady(true);
        }
    }

    private void releasePlayer() {
        exoPlayer.stop();
        exoPlayer.release();
        exoPlayer = null;
    }
}
