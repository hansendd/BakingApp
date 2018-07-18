package com.udacity.baking_app.ui;

import android.content.Context;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeDetailStepFragment extends Fragment {
    private static final String className = RecipeDetailStepFragment.class.toString();

    @BindView(R.id.simpleExoPlayerView) SimpleExoPlayerView simpleExoPlayerView;
    @BindView(R.id.imageview_step_thumbnail) ImageView thumbnailImageView;
    @BindView(R.id.textview_step_instructions) TextView stepInstructionsTextView;
    @BindView(R.id.button_previous_step) Button previousStepButton;
    @BindView(R.id.button_next_step) Button nextStepButton;

    private SimpleExoPlayer exoPlayer;
    private List<Step> stepList;
    private int selectedIndex;

    public RecipeDetailStepFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe_detail_step, container, false);
        ButterKnife.bind(this, view);

        stepList = getArguments().getParcelableArrayList(getString(R.string.extra_step_list));
        selectedIndex = getArguments().getInt(getString(R.string.extra_selected_index));
        final Step step = stepList.get(selectedIndex);

        if (step != null) {
            loadVideo(step);
            loadThumbnail(step);
            loadInstructions(step);
        }
        else {
            Toast.makeText(getContext(), getString(R.string.error_step_not_found), Toast.LENGTH_SHORT).show();
        }

        previousStepButton.setEnabled(selectedIndex > 0);
        nextStepButton.setEnabled(selectedIndex < stepList.size()-1);

        previousStepButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                selectedIndex--;
                final Step step = stepList.get(selectedIndex);
                loadVideo(step);
                loadThumbnail(step);
                loadInstructions(step);
                previousStepButton.setEnabled(selectedIndex > 0);
                nextStepButton.setEnabled(selectedIndex < stepList.size()-1);
            }
        });

        nextStepButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                selectedIndex++;
                final Step step = stepList.get(selectedIndex);
                loadVideo(step);
                loadThumbnail(step);
                loadInstructions(step);
                previousStepButton.setEnabled(selectedIndex > 0);
                nextStepButton.setEnabled(selectedIndex < stepList.size()-1);
            }
        });

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }

    private void loadVideo(Step step) {
        if (exoPlayer != null) {
            exoPlayer.stop();
        }
        if (step.getVideoURL() != null &&
            !"".equals(step.getVideoURL())) {
            if (NetworkConnectionUtility.haveActiveNetworkConnection(getConnectivityManager())) {
                simpleExoPlayerView.setVisibility(View.VISIBLE);
                beginPlayer(createVideoURI(step.getVideoURL()));
            }
        }
        else {
            simpleExoPlayerView.setVisibility(View.GONE);
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
        if (exoPlayer == null) {
            TrackSelector trackSelector = new DefaultTrackSelector();
            exoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector);
            simpleExoPlayerView.setPlayer(exoPlayer);
        }
        String userAgent = Util.getUserAgent(getContext(), "StepVideo");
        MediaSource mediaSource = new ExtractorMediaSource(videoUri,
                                                           new DefaultDataSourceFactory(getContext(),
                                                                                        userAgent),
                                                           new DefaultExtractorsFactory(),
                                                           null,
                                                           null);
        exoPlayer.prepare(mediaSource);

            // Source of logic:
            // https://stackoverflow.com/questions/46713761/how-to-play-video-full-screen-in-landscape-using-exoplayer

//            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)simpleExoPlayerView.getLayoutParams();
//            params.width = params.MATCH_PARENT;
//            params.height = 500;
//            ((AppCompatActivity)getActivity()).getSupportActionBar().show();
//
//            if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
//                params.height = params.MATCH_PARENT;
//                ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
//            }

        exoPlayer.setPlayWhenReady(true);
//        }
    }

    private void releasePlayer() {
        if (exoPlayer != null) {
            exoPlayer.stop();
            exoPlayer.release();
            exoPlayer = null;
        }
    }
}
