package lingying.example.gittext_ibrary;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.util.TypedValue;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class GridEditText extends androidx.appcompat.widget.AppCompatEditText{
    String mSpacingSizes;
    String mSplitLines;
    String mCharBackgrounds;
    String mCharTextColors;
    String mCharTextSizes;
    String mCharTextTypefaces;
    String mCharTextStyles;
    int mGridNum = 0;

    CompositeAttSet<Integer> mSpacingSizeAttr;
    CompositeAttSet<Drawable> mSplitLineAttr;
    CompositeAttSet<Drawable> mCharBackgroundAttr;
    CompositeAttSet<ColorStateList> mCharBgColorsAttr;
    CompositeAttSet<ColorStateList> mCharTextColorsAttr;
    CompositeAttSet<Float> mCharTextSizesAttr;
    CompositeAttSet<Typeface> mCharTextTypefaceAttr;

    private boolean isInited;
    private CharSequence cachedText;
    private String pkgname;
    private float defTextSize;
    private ColorStateList defTextColor;
    private Typeface defTextTypeface;

    public GridEditText(Context context) {
        this(context, null, 0);
    }

    public GridEditText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GridEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr, 0);
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        if (!isInited) {
            cachedText = text;
            return;
        }

        SpannableString ss = new SpannableString(text);

        int[] stateSet = getDrawableState();
        for (int i = 0; i < text.length(); ++i) {
            int spacing = 0;
            int textColor = 0;
            float textSize = 0;
            Drawable sl = null;
            Drawable bg = getPositionFeature(i, mCharBackgroundAttr);
            ColorStateList textColorState = getPositionFeature(i, mCharTextColorsAttr, defTextColor);
            Float size = getPositionFeature(i, mCharTextSizesAttr, defTextSize);
            Typeface textTypeface = getPositionFeature(i, mCharTextTypefaceAttr);

            if (null != textColorState) {
                textColor = textColorState.getColorForState(stateSet, defTextColor.getDefaultColor());
            }
            if (null != size) {
                textSize = Float.valueOf(size);
            }
            if (i > 0) {
                sl = getPositionFeature(i, mSplitLineAttr);
                spacing = getPositionFeature(i, mSpacingSizeAttr, 0);
            }

            ss.setSpan(new CharacterSpan(bg, sl, spacing, textColor, textSize, textTypeface), i, i + 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        }

        super.setText(ss, type);
        postInvalidate();
    }

    public void setUnifiedSplitLine(Drawable drawable) {
        this.mSplitLineAttr.unified = drawable;
    }

    public void setUnifiedCharBackground(Drawable drawable) {
        this.mCharBackgroundAttr.unified = drawable;
    }

    public void setCustomizedSplitLines(Drawable[] drawables) {
        this.mSplitLineAttr.customizeds = drawables;
    }

    public void setCustomizedCharBackgrounds(Drawable[] drawables) {
        this.mCharBackgroundAttr.customizeds = drawables;
    }

    public void setRegularSplitLines(SparseArray<Drawable> drawables) {
        this.mSplitLineAttr.regulars = drawables;
    }

    public void setRegularCharBackgrounds(SparseArray<Drawable> drawables) {
        this.mCharBackgroundAttr.regulars = drawables;
    }

    public void setUnifiedSpacingSize(Integer size) {
        this.mSpacingSizeAttr.unified = size;
    }

    public void setCustomizedSpacingSizes(Integer[] sizes) {
        this.mSpacingSizeAttr.customizeds = sizes;
    }

    public void setRegularSpacingSizes(SparseArray<Integer> sizes) {
        this.mSpacingSizeAttr.regulars = sizes;
    }

    public void setUnifiedCharTextColor(ColorStateList colorStateList) {
        this.mCharTextColorsAttr.unified = colorStateList;
    }

    public void setCustomizedTextColors(ColorStateList[] colorStateLists) {
        this.mCharTextColorsAttr.customizeds = colorStateLists;
    }

    public void setRegularTextColors(SparseArray<ColorStateList> colorStateLists) {
        this.mCharTextColorsAttr.regulars = colorStateLists;
    }

    public void setUnifiedCharTextSize(Float size) {
        this.mCharTextSizesAttr.unified = size;
    }

    public void setCustomizedTextSizes(Float[] sizes) {
        this.mCharTextSizesAttr.customizeds = sizes;
    }

    public void setRegularTextSizes(SparseArray<Float> sizes) {
        this.mCharTextSizesAttr.regulars = sizes;
    }

    public void setUnifiedCharTextTypeface(Typeface typeface) {
        this.mCharTextTypefaceAttr.unified = typeface;
    }

    public void setCustomizedTextTypefaces(Typeface[] typefaces) {
        this.mCharTextTypefaceAttr.customizeds = typefaces;
    }

    public void setRegularTextTypefaces(SparseArray<Typeface> typefaces) {
        this.mCharTextTypefaceAttr.regulars = typefaces;
    }

    public void setmGridNum(int num) {
        StringBuilder sb = new StringBuilder(mGridNum);
        for (int i = 0; i < mGridNum; ++i) {
            sb.append(" ");
        }
        setText(sb.toString());
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        try {
            pkgname = context.getPackageName();
        } catch (Exception e) {
        }

        mCharBackgroundAttr = new CompositeAttSet<>();
        mCharBgColorsAttr = new CompositeAttSet<>();
        mSplitLineAttr = new CompositeAttSet<>();
        mSpacingSizeAttr = new CompositeAttSet<>();
        mCharTextColorsAttr = new CompositeAttSet<>();
        mCharTextSizesAttr = new CompositeAttSet<>();
        mCharTextTypefaceAttr = new CompositeAttSet<>();

        if (null == attrs) {
            setBackgroundResource(0);

            isInited = true;
            if (null != cachedText) {
                setText(cachedText);
                cachedText = null;
            }
        } else {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.GridEditText, defStyleAttr, defStyleRes);
            mSpacingSizes = a.getString(R.styleable.GridEditText_spacingSizes);
            mSplitLines = a.getString(R.styleable.GridEditText_splitLines);
            mCharBackgrounds = a.getString(R.styleable.GridEditText_charBackgrounds);
            mCharTextColors = a.getString(R.styleable.GridEditText_charTextColors);
            mCharTextSizes = a.getString(R.styleable.GridEditText_charTextSizes);
            mCharTextStyles = a.getString(R.styleable.GridEditText_charTextStyles);
            mCharTextTypefaces = a.getString(R.styleable.GridEditText_charTextTypefaces);
            mGridNum = a.getInt(R.styleable.GridEditText_gridNum, mGridNum);

            // 字号
            defTextSize = getTextSize();
            if (defTextSize <= 0) {
                defTextSize = new TextView(context).getTextSize();
            }

            // 字色
            defTextColor = getTextColors();
            if (null == defTextColor) {
                defTextColor = new TextView(context).getTextColors();
            }

            // 字体样式
            defTextTypeface = getTypeface();

            // 背景
            Drawable bg = getBackground();
            if (null == bg) {
                setBackgroundResource(0);
            }

            initCustomizedAttrParse();

            if (null != cachedText) {
                mGridNum = Math.max(mGridNum, cachedText.length());
            }

            if (mGridNum > 0) {
                StringBuilder sb = new StringBuilder(mGridNum);
                int i = 0;
                if (null != cachedText) {
                    sb.append(cachedText);
                    i = cachedText.length();
                }

                while (i < mGridNum) {
                    sb.append(" ");
                    ++i;
                }

                isInited = true;
                setText(sb.toString());
                cachedText = null;
            }
        }
    }

    private void initCustomizedAttrParse() {
        parseDrawableResource(mCharBackgrounds, mCharBackgroundAttr, mCharBgColorsAttr, defTextColor);
        parseDrawableResource(mSplitLines, mSplitLineAttr, null, null);
        parseIntAttr(mSpacingSizes, mSpacingSizeAttr);
        parseDimisionAttr(mCharTextSizes, mCharTextSizesAttr);
        parseColorAttr(mCharTextColors, mCharTextColorsAttr, defTextColor);
        parseTextTypefaceAttr(mCharTextTypefaces, mCharTextStyles, mCharTextTypefaceAttr, defTextTypeface);
    }

    private void parseIntAttr(String attr, CompositeAttSet<Integer> attrSet) {
        // 初始
        attrSet.unified = null;
        attrSet.customizeds = null;
        attrSet.regulars = null;

        if (null == attr || "".equals(attr.trim())) {
            return;
        }

        if (attr.startsWith("@")) {
            int resId = Integer.parseInt(attr.substring(1));
            try {
                int val = getResources().getInteger(resId);
                attrSet.unified = val;

                return;
            } catch (Resources.NotFoundException e) {

            }

            try {
                String[] str = getResources().getStringArray(resId);

                int len = str.length;
                attrSet.customizeds = new Integer[len];
                attrSet.regulars = new SparseArray<>(len);

                try {
                    if (str[0].endsWith("*")) {
                        Integer v = Integer.valueOf(str[0].substring(0, str[0].length() - 1));
                        attrSet.customizeds[0] = v;
                        attrSet.unified = v;
                    } else {
                        Integer v = Integer.valueOf(str[0]);
                        attrSet.customizeds[0] = v;
                    }
                } catch (NumberFormatException e) {

                }

                for (int i = 1; i < len; ++i) {
                    try {
                        if (str[i].endsWith("*")) {
                            Integer v = Integer.valueOf(str[i].substring(0, str[i].length() - 1));
                            attrSet.customizeds[i] = v;
                            attrSet.regulars.put(i + 1, v);
                        } else {
                            Integer v = Integer.valueOf(str[i]);
                            attrSet.customizeds[i] = v;
                        }
                    } catch (NumberFormatException e) {

                    }
                }
            } catch (Resources.NotFoundException e) {

            }
        } else {
            if (attr.endsWith("*")) {
                attr = attr.substring(0, attr.length() - 1);
            }

            if (attr.matches("^-?[1-9]\\d*$")) {
                attrSet.unified = Integer.valueOf(attr);
            }
        }
    }

    private void parseDimisionAttr(String attr, CompositeAttSet<Float> attrSet) {
        // 初始
        attrSet.unified = null;
        attrSet.customizeds = null;
        attrSet.regulars = null;

        if (null == attr || "".equals(attr.trim())) {
            return;
        }

        if (attr.startsWith("@")) {
            int resId = Integer.parseInt(attr.substring(1));
            try {
                float val = getResources().getDimension(resId);
                attrSet.unified = val;

                return;
            } catch (Resources.NotFoundException e) {

            }

            try {
                String[] str = getResources().getStringArray(resId);

                int len = str.length;
                attrSet.customizeds = new Float[len];
                attrSet.regulars = new SparseArray<>(len);

                Float v = getDimensionCfg(str[0]);
                attrSet.customizeds[0] = v;
                if (str[0].endsWith("*")) {
                    attrSet.unified = v;
                }

                for (int i = 1; i < len; ++i) {
                    v = getDimensionCfg(str[i]);
                    attrSet.customizeds[i] = v;
                    if (str[i].endsWith("*")) {
                        attrSet.regulars.put(i + 1, v);
                    }
                }
            } catch (Resources.NotFoundException e) {

            }
        } else {
            if (attr.endsWith("*")) {
                attr = attr.substring(0, attr.length() - 1);
            }

            if (attr.matches("^-?\\d+(\\.\\d+)?(px|sp|dp|dip)?$")) {
                attrSet.unified = getDimensionCfg(attr);
            }
        }
    }

    private void parseColorAttr(String attr, CompositeAttSet<ColorStateList> attrSet, ColorStateList defColorStateList) {
        // 初始
        attrSet.unified = null;
        attrSet.customizeds = null;
        attrSet.regulars = null;

        if (null == attr || "".equals(attr.trim())) {
            return;
        }

        if (attr.startsWith("@")) {
            int resId = Integer.parseInt(attr.substring(1));
            // 判断是否是统一状态色
            try {
                ColorStateList colorStateList = getResources().getColorStateList(resId);
                attrSet.unified = colorStateList;

                return;
            } catch (Resources.NotFoundException e) {

            }

            // 获取个性配置
            try {
                String[] str = getResources().getStringArray(resId);
                int[] colors = getResources().getIntArray(resId);

                int len = str.length;
                attrSet.customizeds = new ColorStateList[len];
                attrSet.regulars = new SparseArray<>(len);

                ColorStateList cs;
                if (null == str[0]) {
                    cs = getColorStateListCfg(Integer.toString(colors[0]), defColorStateList);
                } else {
                    cs = getColorStateListCfg(str[0], defColorStateList);
                }
                attrSet.customizeds[0] = cs;
                if (null != str[0] && str[0].endsWith("*")) {
                    attrSet.unified = cs;
                }

                for (int i = 1; i < len; ++i) {
                    if (null == str[i]) {
                        cs = getColorStateListCfg(Integer.toString(colors[i]), defColorStateList);
                    } else {
                        cs = getColorStateListCfg(str[i], defColorStateList);
                    }
                    attrSet.customizeds[i] = cs;
                    if (null != str[i] && str[i].endsWith("*")) {
                        attrSet.regulars.put(i + 1, cs);
                    }
                }
            } catch (Resources.NotFoundException e) {

            }
        } else {
            attrSet.unified = getColorStateListCfg(attr, defColorStateList);
        }
    }

    private void parseDrawableResource(String attr, CompositeAttSet<Drawable> attrSet, CompositeAttSet<ColorStateList> attrColorSet, ColorStateList defColorStateList) {
        // 初始
        attrSet.unified = null;
        attrSet.customizeds = null;
        attrSet.regulars = null;

        if (null != attrColorSet) {
            attrColorSet.unified = null;
            attrColorSet.customizeds = null;
            attrColorSet.regulars = null;
        }

        if (null == attr || "".equals(attr.trim())) {
            return;
        }

        if (attr.startsWith("@")) {
            int resId = Integer.parseInt(attr.substring(1));
            // 判断是否是统一图片
            try {
                Drawable d = getResources().getDrawable(resId);
                attrSet.unified = d;

                return;
            } catch (Resources.NotFoundException e) {

            }

            if (null != attrColorSet) {
                // 判断是否是统一背景色
                try {
                    ColorStateList cl = getResources().getColorStateList(resId);
                    attrColorSet.unified = cl;

                    return;
                } catch (Resources.NotFoundException e) {

                }
            }

            // 获取个性配置
            try {
                String[] str = getResources().getStringArray(resId);

                int len = str.length;
                Drawable d = getDrawableResource(str[0]);
                // 背景色
                if (null == d && null != attrColorSet) {
                    ColorStateList csl = getColorStateListCfg(str[0], defColorStateList);
                    attrColorSet.customizeds = new ColorStateList[len];
                    attrColorSet.regulars = new SparseArray<>(len);
                    attrColorSet.customizeds[0] = csl;
                    if (str[0].endsWith("*")) {
                        attrColorSet.unified = csl;
                    }

                    for (int i = 1; i < len; ++i) {
                        csl = getColorStateListCfg(str[i], defColorStateList);
                        attrColorSet.customizeds[i] = csl;
                        if (str[i].endsWith("*")) {
                            attrColorSet.regulars.put(i + 1, csl);
                        }
                    }

                    return;
                }

                // 背景图
                attrSet.customizeds = new Drawable[len];
                attrSet.regulars = new SparseArray<>(len);
                attrSet.customizeds[0] = d;
                if (str[0].endsWith("*")) {
                    attrSet.unified = d;
                }

                for (int i = 1; i < len; ++i) {
                    d = getDrawableResource(str[i]);
                    attrSet.customizeds[i] = d;
                    if (str[i].endsWith("*")) {
                        attrSet.regulars.put(i + 1, d);
                    }
                }
            } catch (Resources.NotFoundException e) {

            }
        } else {
            attrSet.unified = getDrawableResource(attr);
        }
    }

    private void parseTextTypefaceAttr(String attrTypeface, String attrStyle, CompositeAttSet<Typeface> attrSet, Typeface defTypeface) {
        // 初始
        attrSet.unified = null;
        attrSet.customizeds = null;
        attrSet.regulars = null;

        List<Typeface> typefaceList = new ArrayList<>();
        if (null != attrTypeface && !"".equals(attrTypeface.trim())) {
            int style = Typeface.NORMAL;
            if (null != defTypeface) {
                style = defTypeface.getStyle();
            }
            if (attrTypeface.startsWith("@")) {
                int resId = Integer.parseInt(attrTypeface.substring(1));
                // 获取个性配置
                try {
                    String[] str = getResources().getStringArray(resId);

                    attrSet.regulars = new SparseArray<>();
                    int len = str.length;
                    Typeface tf = getTypefaceCfg(str[0], style);
                    typefaceList.add(tf);

                    if (str[0].endsWith("*")) {
                        attrSet.unified = tf;
                    }

                    for (int i = 1; i < len; ++i) {
                        tf = getTypefaceCfg(str[i], style);
                        typefaceList.add(tf);
                        if (str[i].endsWith("*")) {
                            attrSet.regulars.put(i + 1, tf);
                        }
                    }
                } catch (Resources.NotFoundException e) {

                }
            } else {
                // 统一字体
                attrSet.unified = getTypefaceCfg(attrTypeface, style);
            }
        }

        if (null != attrStyle && !"".equals(attrStyle.trim())) {
            Typeface family = null;
            if (attrStyle.startsWith("@")) {
                int resId = Integer.parseInt(attrStyle.substring(1));
                // 获取个性配置
                try {
                    String[] str = getResources().getStringArray(resId);

                    if (null == attrSet.regulars) {
                        attrSet.regulars = new SparseArray<>();
                    }
                    int len = str.length;
                    if (typefaceList.size() > 0) {
                        family = typefaceList.get(0);
                    }
                    if (null == family) {
                        family = attrSet.unified;
                    }
                    if (null == family) {
                        family = defTypeface;
                    }
                    if (null == family) {
                        family = Typeface.DEFAULT;
                    }
                    Typeface tf = getTypefaceCfg(str[0], family);
                    if (null != tf) {
                        if (typefaceList.size() > 0) {
                            typefaceList.set(0, tf);
                        } else {
                            typefaceList.add(tf);
                        }
                    } else if (len > 1) {
                        typefaceList.add(null);
                    }

                    if (null != tf && str[0].endsWith("*")) {
                        attrSet.unified = tf;
                    }

                    for (int i = 1; i < len; ++i) {
                        family = null;
                        if (i < typefaceList.size()) {
                            family = typefaceList.get(i);
                        } else {
                            typefaceList.add(null);
                        }
                        if (null == family) {
                            family = attrSet.unified;
                        }
                        if (null == family) {
                            family = defTypeface;
                        }
                        if (null == family) {
                            family = Typeface.DEFAULT;
                        }

                        tf = getTypefaceCfg(str[i], family);
                        if (null != tf) {
                            typefaceList.set(i, tf);

                            if (str[i].endsWith("*")) {
                                attrSet.regulars.put(i + 1, tf);
                            }
                        }
                    }
                } catch (Resources.NotFoundException e) {

                }
            } else {
                family = attrSet.unified;
                if (null == family) {
                    family = defTypeface;
                }
                if (null == family) {
                    family = Typeface.DEFAULT;
                }
                Typeface tf = getTypefaceCfg(attrStyle, family);

                if (null != tf) {
                    attrSet.unified = tf;
                }
            }
        }

        int size = typefaceList.size();
        if (size > 0) {
            attrSet.customizeds = typefaceList.toArray(new Typeface[size]);
        }
    }

    private Drawable getDrawableResource(String attribute) {
        if (null == attribute) {
            return null;
        }

        if (attribute.startsWith("res/")) {
            attribute = attribute.substring(4);
        }

        if (attribute.endsWith("*")) {
            attribute = attribute.substring(0, attribute.length() - 1);
        }

        Drawable res = null;
        String[] p = attribute.split("/");
        if (2 == p.length) {
            String resType = p[0].split("-")[0];
            String resName = p[1].split("\\.")[0];

            int drawableid = getResources().getIdentifier(resName, resType, pkgname);

            try {
                res = getResources().getDrawable(drawableid);
            } catch (Resources.NotFoundException e) {
                e.printStackTrace();
            }
        }

        return res;
    }

    private Typeface getTypefaceCfg(String attribute, int style) {
        if (null == attribute || "".equals(attribute.trim())) {
            return null;
        }

        if (attribute.endsWith("*")) {
            attribute = attribute.substring(0, attribute.length() - 1);
        }

        Typeface tf = null;
        try {
            tf = Typeface.createFromAsset(getResources().getAssets(), attribute);
        } catch (RuntimeException e) {
            tf = Typeface.create(attribute, style);
        }

        return tf;
    }

    private Typeface getTypefaceCfg(String attribute, Typeface family) {
        if (null == attribute) {
            return null;
        }

        if (attribute.endsWith("*")) {
            attribute = attribute.substring(0, attribute.length() - 1);
        }

        int style = 0;
        if (-1 != attribute.indexOf("|")) {
            String[] p = attribute.split("\\|");
            for (String a : p) {
                if (style > 3) {
                    continue;
                }
                if (style < 2 && "italic".equalsIgnoreCase(a)) {
                    style += Typeface.ITALIC;
                } else if ("bold".equalsIgnoreCase(a) && (style == 0 || style == 2)) {
                    style += Typeface.BOLD;
                }
            }
        } else {
            if ("italic".equalsIgnoreCase(attribute)) {
                style = Typeface.ITALIC;
            } else if ("bold".equalsIgnoreCase(attribute)) {
                style = Typeface.BOLD;
            } else {
                try {
                    if (attribute.startsWith("0x") || attribute.startsWith("0X")) {
                        style = Integer.parseInt(attribute.substring(2), 16);
                    } else {
                        style = Integer.parseInt(attribute);
                    }
                } catch (NumberFormatException e) {
                }
            }
        }

        Typeface typeface = Typeface.create(family, style);
        return typeface;
    }

    private ColorStateList getColorStateListCfg(String attribute, ColorStateList defColorStateList) {
        if (null == attribute) {
            return defColorStateList;
        }

        if (attribute.endsWith("*")) {
            attribute = attribute.substring(0, attribute.length() - 1);
        }

        ColorStateList res;
        int color;
        if (attribute.startsWith("#")) {
            color = Color.parseColor(attribute);

            int[][] states = new int[1][];
            states[0] = new int[]{};
            res = new ColorStateList(states, new int[]{color});
        } else if (attribute.startsWith("@")) {
            int resId = Integer.parseInt(attribute.substring(1));
            // 判断是否是统一图片
            try {
                res = getResources().getColorStateList(resId);
                return res;
            } catch (Resources.NotFoundException e) {

            }

            color = getResources().getColor(resId);
            int[][] states = new int[1][];
            states[0] = new int[]{};
            res = new ColorStateList(states, new int[]{color});
        } else if (attribute.matches("^-?[1-9]\\d*$")) {
            color = Integer.parseInt(attribute);

            try {
                res = getResources().getColorStateList(color);
                return res;
            } catch (Resources.NotFoundException e) {

            }

            int[][] states = new int[1][];
            states[0] = new int[]{};
            res = new ColorStateList(states, new int[]{color});
        } else {
            res = defColorStateList;
        }

        return res;
    }

    private float getDimensionCfg(String attribute) {
        if (null == attribute) {
            return 0;
        }

        if (attribute.endsWith("*")) {
            attribute = attribute.substring(0, attribute.length() - 1);
        }

        Float ret;
        try {
            if (attribute.endsWith("sp")) {
                attribute = attribute.substring(0, attribute.length() - 2);
                ret = Float.valueOf(sp2px(Float.valueOf(attribute).floatValue()));
            } else if (attribute.endsWith("px")) {
                attribute = attribute.substring(0, attribute.length() - 2);
                ret = Float.valueOf(attribute);
            } else if (attribute.endsWith("dp")) {
                attribute = attribute.substring(0, attribute.length() - 2);
                ret = Float.valueOf(dip2px(Float.valueOf(attribute)));
            } else if (attribute.endsWith("dip")) {
                attribute = attribute.substring(0, attribute.length() - 3);
                ret = Float.valueOf(dip2px(Float.valueOf(attribute)));
            } else {
                ret = Float.valueOf(attribute);
            }
        } catch (NumberFormatException e) {
            ret = Float.valueOf(0);
        }

        return ret;
    }

    private <T> T getPositionFeature(int index, CompositeAttSet<T> attributeSet) {
        return getPositionFeature(index, attributeSet, null);
    }

    private <T> T getPositionFeature(int index, CompositeAttSet<T> attributeSet, T defval) {
        if (index < 0 || null == attributeSet) {
            return defval;
        }

        T ret = null;
        if (null != attributeSet.customizeds && index < attributeSet.customizeds.length) {
            ret = attributeSet.customizeds[index];
        } else if (null != attributeSet.regulars) {
            boolean flag = false;
            for (int i = attributeSet.regulars.size() - 1; i >= 0; --i) {
                int divisor = attributeSet.regulars.keyAt(i);
                if (0 == divisor) {
                    continue;
                }
                T t = attributeSet.regulars.get(divisor);
                if (0 == (index + 1) % divisor) {
                    ret = t;
                    flag = true;
                }
            }

            if (!flag) {
                ret = attributeSet.unified;
            }
        } else {
            ret = attributeSet.unified;
        }

        if (null == ret) {
            ret = defval;
        }

        return ret;
    }

    private float dip2px(float dpValue) {
        //        final float scale = getResources().getDisplayMetrics().density;
        //        return dpValue * scale;
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, getResources().getDisplayMetrics());
    }

    private float sp2px(float spValue) {
        //        final float fontScale = getResources().getDisplayMetrics().scaledDensity;
        //        return spValue * fontScale;
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spValue, getResources().getDisplayMetrics());
    }

    private class CompositeAttSet<T> {
        T unified;
        T[] customizeds;
        SparseArray<T> regulars;
    }
}
