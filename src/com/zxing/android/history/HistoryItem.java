/*
 * Copyright 2012 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.zxing.android.history;

import com.google.zxing.Result;

public final class HistoryItem {

  private final String text;
  private final String display;
  private final String details;
  
  HistoryItem(String text, String display, String details) {
    this.text = text;
    this.display = display;
    this.details = details;
  }

  public String getResult() {
    return text;
  }

  public String getDisplayAndDetails() {
    StringBuilder displayResult = new StringBuilder();
    if (display == null || display.isEmpty()) {
      displayResult.append(text);
    } else {
      displayResult.append(display);
    }
    if (details != null && !details.isEmpty()) {
      displayResult.append(" : ").append(details);
    }
    return displayResult.toString();
  }
  
}
