package geekbrains.ru.weather;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.util.AttributeSet;
import android.view.View;

public class WeatherView extends View implements SensorEventListener {

    private Rect rctText;
    private Rect rctIcon;
    Bitmap icon;
    Bitmap bufferBitmap;
    String unit;
    Paint paint;
    int value = -60;
    int color;

    public WeatherView(Context context) {
        super(context);
        icon = BitmapFactory.decodeResource(getResources(),R.drawable.humidity_mask);
        unit = getResources().getString(R.string.humidity_unit);
        color = Color.BLUE;
        init();
    }

    public WeatherView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs,R.styleable.WeatherView);
        icon = BitmapFactory.decodeResource(getResources(),typedArray.getResourceId(R.styleable.WeatherView_wv_icon, R.drawable.humidity_mask));
        unit = getResources().getString(typedArray.getResourceId(R.styleable.WeatherView_wv_unit, R.string.humidity_unit));
        color = typedArray.getColor(R.styleable.WeatherView_wv_color, Color.BLUE);
        typedArray.recycle();
        init();
    }

    private void init() {
        rctIcon = new Rect();
        rctText = new Rect();
        paint = new Paint();
        paint.setColor(color);
        paint.setStyle(Paint.Style.FILL);
    }

    private void calcTextSize() {
        String text = "900CC";//в качестве текста выберем максимальновозможный текст.
        Rect rct = new Rect();
        paint.getTextBounds(text,0, text.length(),rct);//расчитывем, габариты текста, если записать их текущим размером шрифтов
        float scale = (float) rctText.width() / (float)rct.width();//расчитываем разницу между текущим размером шрифта и требуемым
        // мы ожидаем, что весь текст по ширине полностью поместится в 5 и 6 квадрат нашего компонента
        float textSize = paint.getTextSize() * scale;
        paint.setTextSize(textSize);//задаем новое значение шрифта
        rctText.top = rctText.top + (int)((rctText.height() - rct.height() * scale)/2.0f);// выравниваем текст шрифта по вертикали по центру 5 и 6 квадратов
        rctText.bottom = rctText.top + (int)(rct.height() * scale);
    }

    private void makeColoredImage() {
        bufferBitmap = Bitmap.createBitmap(rctIcon.width(),rctIcon.height(),Bitmap.Config.ARGB_8888);//создаем битмап
        Canvas bufferCanvas = new Canvas(bufferBitmap);//создаем канвас чтобы рисовать в созданную битовую карту
        Rect bmRect = new Rect(0,0,rctIcon.width(),rctIcon.height());

        Paint paintBm = new Paint();
        paintBm.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
        bufferCanvas.drawBitmap(icon, new Rect(0,0,icon.getWidth(), icon.getHeight()), bmRect, paintBm);
        //рисуем заданную из ресурсов иконку в битовую карту с флагом перезатереть все что под ним включая альфаканал

        Paint paintColor = new Paint();
        paintColor.setColor(color);
        paintColor.setStyle(Paint.Style.FILL);
        paintColor.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        bufferCanvas.drawRect(0,0, rctIcon.width(), rctIcon.height(), paintColor);
        //рисуем в битовую карту квадрат требуемого цвета с флагом задать прозрачность как пересечение альфа каналов текущего слоя биткарты
        // со слоем рисуемого квадрата. Но в качестве цвета для не прозрачных пикселей использовать цвет квадрата.
        //в результате альфа канал исходной иконки вырежет из рисуемого квадрата все пиксели которые не видны в иконке.
        // И резултирующая картинка заданного цвета сохранится в битовой карте
    }

    @Override
    public void layout(int l, int t, int r, int b) {
        super.layout(l, t, r, t + r - l);//делаем компонент квадратным
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        //Расчитываем местоположение и размеры элементов которые мы будем отрисовывать
        //Если представить компонент как квадрат разбитый на 9 одинаковых квадратов по 3 в ряд и про нумеруем их слева на право сверху вниз,
        //то 4 квадрат будет картинкой, а 5 и 6 текстом.
        int height = (right - left)/3;
        rctText.top = rctIcon.top = height;
        rctText.bottom = rctIcon.bottom = height * 2;
        rctIcon.left = 0;
        rctText.left = height;
        rctIcon.right = height;
        rctText.right = height * 3;

        calcTextSize();//расчитываем размер шрифта, чтобы вписать его в заданные размеры
        makeColoredImage();// создаем картинку необходимого размера и цвета.
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(bufferBitmap,rctIcon.left,rctIcon.top, paint);//рисуем картинку в заданный квадрат

        Rect rct = new Rect();
        String text = value + unit;
        paint.getTextBounds(text,0, text.length(),rct);//учитываем смещения текста в зависимости от шрифта
        canvas.drawText(text,rctText.left - rct.left, rctText.top - rct.top, paint);//отображаем текст в заданный квадрат
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        value = (int)event.values[0];
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
