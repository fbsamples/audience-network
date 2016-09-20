/*
 * Copyright (c) 2016 Razeware LLC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.fb.audiencenetwork.scrollapp;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class NasaPost implements Serializable {

  private String mDate;
  private String mHumanDate;
  private String mExplanation;
  private String mUrl;

  public NasaPost(JSONObject photoJSON) {
    try {
      mDate = photoJSON.getString("date");
      mHumanDate = convertDateToHumanDate();
      mExplanation = photoJSON.getString("explanation");
      mUrl = photoJSON.getString("url");
    } catch (JSONException e) {
      e.printStackTrace();
    }
  }

  public String getHumanDate() {
    return mHumanDate;
  }

  public String getExplanation() {
    return mExplanation;
  }

  public String getUrl() {
    return mUrl;
  }

  private String convertDateToHumanDate() {

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat humanDateFormat = new SimpleDateFormat("dd MMMM yyyy");

    try {
      Date date = dateFormat.parse(mDate);
      Calendar cal = Calendar.getInstance();
      cal.setTime(date);
      return humanDateFormat.format(cal.getTime());
    } catch (ParseException e) {
      e.printStackTrace();
      return null;
    }
  }
}
