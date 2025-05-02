/*
 * Copyright (c) Meta Platforms, Inc. and affiliates.
 * All rights reserved.
 *
 * This source code is licensed under the license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.facebook.samples.AdUnitsSample.models;

import com.facebook.infer.annotation.Nullsafe;

@Nullsafe(Nullsafe.Mode.LOCAL)
public class RecyclerPostItem {

  // NULLSAFE_FIXME[Field Not Initialized]
  private String postContent;

  public RecyclerPostItem(String postContent) {
    setPostContent(postContent);
  }

  public String getPostContent() {
    return postContent;
  }

  public void setPostContent(String postContent) {
    this.postContent = postContent;
  }
}
