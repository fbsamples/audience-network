/**
 * Copyright (c) 2016-present, Facebook, Inc. All rights reserved.
 *
 * You are hereby granted a non-exclusive, worldwide, royalty-free license to
 * use, copy, modify, and distribute this software in source code or binary form
 * for use in connection with the web services and APIs provided by Facebook.
 *
 * As with any software that integrates with the Facebook platform, your use of
 * this software is subject to the Facebook Developer Principles and Policies
 * [http://developers.facebook.com/policy/], Your use of this software is also
 * subject to the Audience Network Terms
 * [https://www.facebook.com/ads/manage/audience_network/publisher_tos].
 * This copyright notice shall be included in all copies or substantial portions
 * of the software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.sample.flipanimation;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.Button;


/**
 * This class is to be used together with the shining_button.xml resource file to specify the style
 * attributes, then you can use the class directly in the layout xml files.
 *
 * The shining effect is achieved by moving the effect image, the white_gradient.png file across the
 * button on certain interval, see the method {@link ShiningButton#initAnimation()}.
 */
public class ShiningButton extends Button {

    private Drawable efx;
    private int efxWidth;
    private Drawable background;

    private static final int EFX_FRAME_COUNT = 20;
    private static final int EFX_TIME_PER_FRAME = 30;
    private static final int NON_EFX_TIME = 1000;

    public ShiningButton(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.ShiningButton,
                0, 0);

        try {
            int color = a.getColor(R.styleable.ShiningButton_color, 0xffffff);
            efx = a.getDrawable(R.styleable.ShiningButton_efx);
            efxWidth = a.getDimensionPixelSize(R.styleable.ShiningButton_efx_width, 100);
            background = new ColorDrawable(color);
        } catch(Exception e) {
            Log.e("ShiningButton", "Failed to initialize!");
        } finally {
            a.recycle();
        }
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        // To ensure that we have the size ready
        if (w != 0 && oldw == 0) {
            initAnimation();
        }
    }

    /**
     * The effect image moves from left to right in {@link ShiningButton#EFX_FRAME_COUNT} frames,
     * where each frame is {@link ShiningButton#EFX_TIME_PER_FRAME} milliseconds long.
     */
    private void initAnimation() {

        final AnimationDrawable gradientAnimation = new AnimationDrawable();
        gradientAnimation.addFrame(background, NON_EFX_TIME);

        final int buttonWidth = getWidth();

        for (int i=0; i<EFX_FRAME_COUNT; i++) {
            Drawable[] layers = {background, efx};
            LayerDrawable layerDrawable = new LayerDrawable(layers);

            int left = (int) ((1f * i * (buttonWidth + efxWidth)) / (EFX_FRAME_COUNT - 1) - efxWidth);
            int right = buttonWidth - efxWidth - left;
            layerDrawable.setLayerInset(1, left, 0, right, 0);
            gradientAnimation.addFrame(layerDrawable, EFX_TIME_PER_FRAME);
        }

        setBackground(gradientAnimation);
        gradientAnimation.start();
    }
}
