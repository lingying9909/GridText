package lingying.example.gittext_ibrary;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.style.ReplacementSpan;

import java.lang.reflect.Type;

public class CharacterSpan extends ReplacementSpan {

    private Drawable background;
    private Drawable splitLine;
    private int spacing;
    private int textColor;
    private float textSize;
    private Typeface textTypeface;

    public CharacterSpan(Drawable background, Drawable splitLine, int spacing, int textColor, float textSize, Typeface textTypeface) {
        super();
        this.background = background;
        this.splitLine = splitLine;
        this.spacing = spacing;
        this.textColor = textColor;
        this.textSize = textSize;
        this.textTypeface = textTypeface;
        init();
    }

    @Override
    public int getSize(Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fm) {
        int textLen = 0;
        int bgLen = 0;
        int slLen = 0;

        if (null != text) {
            String str = text.subSequence(start, end).toString();
            Rect textRect = getSubTextRect(str, 0, str.length());
            textLen = (int) (paint.measureText(text, start, end) + 0.5f);
            textLen = Math.max(textRect.right, textLen);
        }

        if (null != background) {
            Rect bgRect = background.getBounds();
            if (fm != null) {
                fm.ascent = bgRect.top - bgRect.bottom;
                fm.descent = 0;

                fm.top = fm.ascent;
                fm.bottom = 0;
            }

            bgLen = bgRect.right;
        }

        if (null != splitLine) {
            Rect slRect = splitLine.getBounds();
            slLen = slRect.right;
        }

        return spacing + Math.max(textLen, bgLen) + slLen;
    }

    @Override
    public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {
        float offsetX = x;
        int offsetY = y;
        if (null != splitLine) {
            canvas.save();
            if (spacing != 0) {
                offsetX += spacing / 2.0;
            }
            canvas.translate(offsetX, top);

            splitLine.draw(canvas);
            offsetX += splitLine.getBounds().width();

            if (spacing != 0) {
                offsetX += spacing / 2.0;
            }

            canvas.restore();
        } else if (spacing != 0) {
            offsetX += spacing;
        }

        if (null != background) {
            canvas.save();

            Rect bgRect = background.getBounds();
            canvas.translate(offsetX, bottom - bgRect.bottom);
            background.draw(canvas);

            offsetX += bgRect.width() / 2;
            offsetY -= bgRect.height() / 2;

            canvas.restore();
        }

        paint.setAntiAlias(true);

        if (null != text) {
            String str = text.subSequence(start, end).toString();
            //得到文字宽高
            Rect textRect = getSubTextRect(str, 0, str.length());

            //根据背景图算出 字符串居中绘制的位置
            float textX = offsetX;
            float textY = offsetY;

            if (null != background) {
                textX -= textRect.width() / 2;
                textY += textRect.height() / 2;
            }

            paint.setColor(textColor);
            if (textSize > 0) {
                paint.setTextSize(textSize);
            }
            if (null != textTypeface) {
                paint.setTypeface(textTypeface);
            }

            canvas.drawText(text.toString(), start, end, textX, textY, paint);
        }
    }

    private void init() {
        if (null != background) {
            int width = background.getIntrinsicWidth();
            int height = background.getIntrinsicHeight();
            this.background.setBounds(0, 0, width > 0 ? width : 0, height > 0 ? height : 0);
        }

        if (null != splitLine) {
            int width = splitLine.getIntrinsicWidth();
            int height = splitLine.getIntrinsicHeight();
            this.splitLine.setBounds(0, 0, width > 0 ? width : 0, height > 0 ? height : 0);
        }
    }

    private Rect getSubTextRect(String text, int start, int end) {
        Rect rect = new Rect();
        Paint paint = new Paint();

        if (textSize > 0) {
            // 先根据TextView 属性 设置字体大小
            paint.setTextSize(textSize);
        }
        // 获得字符串所占空间大小
        paint.getTextBounds(text, start, end, rect);

        return rect;
    }
}
