/*
 * Copyright 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.material.catalog.button;

import io.material.catalog.R;

import android.os.Bundle;
import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.material_m.button.MaterialButtonToggleGroup;
import com.google.android.material_m.switchmaterial.SwitchMaterial;
import io.material.catalog.feature.DemoFragment;
import io.material.catalog.feature.DemoUtils;
import java.util.List;

/** A fragment that displays a button toggle group demo for the Catalog app. */
public class ButtonToggleGroupDemoFragment extends DemoFragment {

  /**
   * Create a Demo View with different types of {@link MaterialButtonToggleGroup} and a switch to
   * toggle {@link MaterialButtonToggleGroup#setSelectionRequired(boolean)}
   */
  @Nullable
  @Override
  public View onCreateDemoView(
      LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
    View view =
        layoutInflater.inflate(getButtonToggleGroupContent(), viewGroup, /* attachToRoot= */false);
    SwitchMaterial requireSelectionToggle = view.findViewById(R.id.switch_toggle);

    requireSelectionToggle.setOnCheckedChangeListener(
        (buttonView, isChecked) -> {
          List<MaterialButtonToggleGroup> toggleGroups =
              DemoUtils.findViewsWithType(view, MaterialButtonToggleGroup.class);
          for (MaterialButtonToggleGroup toggleGroup : toggleGroups) {
            toggleGroup.setSelectionRequired(isChecked);
          }
        });

    return view;
  }

  @LayoutRes
  protected int getButtonToggleGroupContent() {
    return R.layout.cat_buttons_toggle_group_fragment;
  }
}
