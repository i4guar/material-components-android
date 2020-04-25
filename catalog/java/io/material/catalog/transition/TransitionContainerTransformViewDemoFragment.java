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

package io.material.catalog.transition;

import io.material.catalog.R;

import android.annotation.TargetApi;
import android.graphics.Color;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import com.google.android.material_m.transition.MaterialContainerTransform;
import io.material.catalog.feature.DemoFragment;
import io.material.catalog.feature.OnBackPressedHandler;

/** A fragment that displays the main Transition demo for the Catalog app. */
@TargetApi(VERSION_CODES.LOLLIPOP)
public class TransitionContainerTransformViewDemoFragment extends DemoFragment
    implements OnBackPressedHandler {

  @Nullable private View startView;
  @Nullable private View endCard;
  @Nullable private FrameLayout root;

  @NonNull
  private final ContainerTransformConfigurationHelper configurationHelper =
      getContainerTransformConfigurationHelper();

  @Override
  public View onCreateDemoView(
      LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
    View view =
        layoutInflater.inflate(
            R.layout.cat_transition_container_transform_view_fragment, viewGroup, false);
    root = view.findViewById(R.id.root);
    endCard = view.findViewById(R.id.end_card);
    return view;
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle bundle) {
    addTransitionableTarget(view, R.id.start_fab);
    addTransitionableTarget(view, R.id.single_line_list_item);
    addTransitionableTarget(view, R.id.vertical_card_item);
    addTransitionableTarget(view, R.id.horizontal_card_item);
    addTransitionableTarget(view, R.id.grid_card_item);
    addTransitionableTarget(view, R.id.grid_tall_card_item);
    addTransitionableTarget(view, R.id.end_card);
  }

  private void addTransitionableTarget(@NonNull View view, @IdRes int id) {
    View target = view.findViewById(id);
    if (target != null) {
      if (id == R.id.end_card) {
        target.setOnClickListener(v -> showStartView());
      } else {
        target.setOnClickListener(this::showEndView);
      }
    }
  }

  private void showEndView(@Nullable View startView) {
    // Save a reference to the start view that triggered the transition in order to know which view
    // to transition into during the return transition.
    this.startView = startView;

    // Construct a container transform transition between two views.
    MaterialContainerTransform transition = buildContainerTransform(true);
    transition.setStartView(startView);
    transition.setEndView(endCard);

    // Trigger the container transform transition.
    TransitionManager.beginDelayedTransition(root, transition);
    if (startView != null) {
      startView.setVisibility(View.INVISIBLE);
    }
    if (endCard != null) {
      endCard.setVisibility(View.VISIBLE);
    }
  }

  private void showStartView() {
    // Construct a container transform transition between two views.
    MaterialContainerTransform transition = buildContainerTransform(false);
    transition.setStartView(endCard);
    transition.setEndView(startView);

    // Trigger the container transform transition.
    TransitionManager.beginDelayedTransition(root, transition);
    if (startView != null) {
      startView.setVisibility(View.VISIBLE);
    }
    if (endCard != null) {
      endCard.setVisibility(View.INVISIBLE);
    }
  }

  @NonNull
  private MaterialContainerTransform buildContainerTransform(boolean entering) {
    MaterialContainerTransform transform = new MaterialContainerTransform();
    transform.setScrimColor(Color.TRANSPARENT);
    configurationHelper.configure(transform, entering);
    return transform;
  }

  @NonNull
  protected ContainerTransformConfigurationHelper getContainerTransformConfigurationHelper() {
    return new ContainerTransformConfigurationHelper();
  }

  @Override
  public boolean onBackPressed() {
    if (endCard != null && endCard.getVisibility() == View.VISIBLE) {
      showStartView();
      return true;
    }
    return false;
  }

  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
    menuInflater.inflate(R.menu.configure_menu, menu);
    super.onCreateOptionsMenu(menu, menuInflater);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem menuItem) {
    if (menuItem.getItemId() == R.id.configure) {
      configurationHelper.showConfigurationChooser(
          requireContext(), dialog -> buildContainerTransform(true));
      return true;
    }
    return super.onOptionsItemSelected(menuItem);
  }
}
